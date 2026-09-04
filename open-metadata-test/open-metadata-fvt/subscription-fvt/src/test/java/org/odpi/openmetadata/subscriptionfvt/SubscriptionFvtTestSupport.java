/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductSubscriptionDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared conventions and helpers for the subscription-fvt suite: how this run names the destinations it
 * subscribes into, how it finds them again, how it prepares the PostgreSQL server that holds them, and how it
 * waits for work that is being done by another server.
 * <br>
 * Every subscription needs somewhere for the data to be delivered to, and the shape of that destination is
 * decided by what is being subscribed to: a single product delivers into one <b>tabular data set</b> - a
 * table - and a product family delivers into a <b>tabular data set collection</b> - a schema, one table per
 * product in the family.  Both destinations are real PostgreSQL objects on the server under test, catalogued
 * from the PostgreSQL content pack's own templates, so that what the subscription is pointed at is a genuine
 * asset rather than a stub that only exists in the repository.
 * <br>
 * Two kinds of debris have to be dealt with, and they live in different places.  In the <b>repository</b>,
 * every element this suite causes to be created carries {@link #TEST_MARKER} somewhere in its qualified name -
 * the destination assets because the templates build their names from the server name this suite chooses, and
 * the subscriptions because the create-subscription service names them after the destination they deliver to.
 * On the <b>PostgreSQL server under test</b>, the suite creates schemas of its own rather than subscribing
 * into whatever happens to be there.
 * <br>
 * Both are cleared at the <em>start</em> of a run rather than the end.  Clearing up afterwards leaves the
 * debris behind whenever a run is killed or crashes - which is exactly when it is most likely to be in a
 * state the next run should not inherit.
 */
final class SubscriptionFvtTestSupport
{
    /**
     * Marker carried by everything this suite creates, directly or through a governance action.  Server names
     * given to the catalog templates start with it, and the templates build their qualified names from those
     * server names, so a search for this string finds the whole graph a run produced.
     */
    static final String TEST_MARKER = "subscription-fvt";

    /**
     * Page size used by every client this suite creates, and configured on every server it starts.
     */
    static final int MAX_PAGE_SIZE = 500;

    /**
     * Destination label for the product family subscription.  The family delivers one table per product in
     * the family, so its destination is a schema rather than a table.
     */
    static final String FAMILY_DESTINATION_PURPOSE = "family";

    /**
     * Whether the catalogue has been built during this run - see {@link #ensureCatalogueBuilt()}.
     */
    private static boolean catalogueBuilt = false;

    private SubscriptionFvtTestSupport()
    {
        // no instances
    }


    /**
     * Return the name this suite gives the PostgreSQL server that holds its subscription destinations.
     * <br>
     * The templates build every destination asset's qualified name from this, which is what puts
     * {@link #TEST_MARKER} into names this suite does not otherwise choose - and therefore what makes the
     * clean-up sweep able to find them.
     *
     * @return server name to pass as the serverName placeholder
     */
    static String destinationServerName()
    {
        return TEST_MARKER + "-destinations";
    }


    /**
     * Return the name of the schema that holds one test's destination.
     * <br>
     * Each test gets its own schema.  They could share one, but a subscription's own name is built from the
     * destination it delivers to, so sharing would leave every test asserting against names that differ only
     * by a timestamp - and a failure would not say which test's subscription it was looking at.
     *
     * @param purpose short label for the test, for example "evaluation"
     * @return schema name, lower case and underscored because PostgreSQL folds unquoted identifiers
     */
    static String destinationSchemaName(String purpose)
    {
        return (TEST_MARKER + "_" + purpose).replace('-', '_').toLowerCase();
    }


    /**
     * Return the name of the single table inside a product destination schema.
     *
     * @return table name
     */
    static String destinationTableName()
    {
        return "delivered_data";
    }


    /**
     * Return the qualified name the PostgreSQL tabular data set template gives to a table catalogued as a
     * destination.  Derived from the template's own definition rather than typed out, because the template is
     * the thing that decides it.
     *
     * @param schemaName schema holding the table
     * @param tableName table catalogued
     * @return qualified name of the tabular data set asset
     */
    static String tabularDataSetQualifiedName(String schemaName,
                                              String tableName)
    {
        return PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getDeployedImplementationType()
                       + "::" + destinationServerName() + "::" + getDatabaseName() + "." + schemaName + "." + tableName;
    }


    /**
     * Return the qualified name the PostgreSQL tabular data set collection template gives to a schema
     * catalogued as a destination.  This is the shape a product family delivers into.
     *
     * @param schemaName schema catalogued
     * @return qualified name of the tabular data set collection asset
     */
    static String tabularDataSetCollectionQualifiedName(String schemaName)
    {
        return PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType()
                       + "::" + destinationServerName() + "::" + getDatabaseName() + "." + schemaName;
    }


    /*
     * =====================================================================================================
     * The PostgreSQL server under test
     */


    /**
     * Return the host identifier of the PostgreSQL server this suite asks Egeria to catalogue and survey.
     *
     * @return host name or IP address
     */
    static String getServerHost()
    {
        return OMAGPlatformExtension.getProperty("subscription.fvt.server.host", "localhost");
    }


    /**
     * Return the port of the PostgreSQL server under test.
     *
     * @return port number as a string, because that is how it is supplied to the catalog template
     */
    static String getServerPort()
    {
        return OMAGPlatformExtension.getProperty("subscription.fvt.server.port", "5442");
    }


    /**
     * Return the name of the database on the server under test that this suite catalogues and surveys.
     * <br>
     * The default is the database Egeria's own repository lives in, because it is certain to exist and needs
     * no privilege beyond creating a schema inside it.  Naming a database that does not exist makes the suite
     * create it, which needs CREATEDB - see {@link #prepareServerUnderTest()}.
     *
     * @return database name
     */
    static String getDatabaseName()
    {
        return OMAGPlatformExtension.getProperty("subscription.fvt.database.name", "egeria");
    }


    /**
     * Return the location of the secrets store holding the credentials for the server under test.
     * <br>
     * This is passed to the catalog template as a placeholder value, so it has to be a path the <em>server</em>
     * can resolve rather than one this test class can.  Both are the same JVM here, and Gradle runs the test
     * task with this module's project directory as its working directory, so a path relative to that works for
     * both - but that is why the default in application.properties is written the way it is.
     *
     * @return path name of the secrets store
     */
    static String getSecretsStoreLocation()
    {
        return OMAGPlatformExtension.getProperty("subscription.fvt.server.secrets.store",
                                                 "../../../open-metadata-resources/open-metadata-deployment/secrets/egeria-servers.omsecrets");
    }


    /**
     * Return the name of the collection inside that secrets store holding the credentials for the server under
     * test.
     *
     * @return collection name
     */
    static String getSecretsCollectionName()
    {
        return OMAGPlatformExtension.getProperty("subscription.fvt.server.secrets.collection", "PostgreSQLRepository");
    }


    /**
     * Return a value for every placeholder the PostgreSQL tabular data set template declares - the
     * destination a single product's subscription delivers into.
     * <br>
     * The placeholder names come from the connector's own control definitions rather than being written out,
     * because getting one wrong does not fail: the asset would simply be created with the placeholder
     * unsubstituted, carrying "~{secretsStorePathName}~" where a path belongs.  That is why every test that
     * creates from a template also checks for surviving placeholders - see {@link #findPlaceholders}.
     *
     * @param schemaName schema holding the table
     * @param tableName table to catalogue as the destination
     * @return placeholder values ready to pass to the template
     */
    static Map<String, String> dataSetTemplatePlaceholders(String schemaName,
                                                           String tableName)
    {
        Map<String, String> placeholderValues = dataSetCollectionTemplatePlaceholders(schemaName);

        placeholderValues.put(PostgresPlaceholderProperty.TABLE_NAME.getName(), tableName);
        placeholderValues.put(PostgresPlaceholderProperty.TABLE_DESCRIPTION.getName(),
                              "Destination table for a subscription-fvt product subscription.");

        return placeholderValues;
    }


    /**
     * Return a value for every placeholder the PostgreSQL tabular data set collection template declares - the
     * destination a product family's subscription delivers into.  A family delivers one table per product, so
     * its destination is a schema rather than a table.
     *
     * @param schemaName schema to catalogue as the destination
     * @return placeholder values ready to pass to the template
     */
    static Map<String, String> dataSetCollectionTemplatePlaceholders(String schemaName)
    {
        Map<String, String> placeholderValues = new HashMap<>();

        placeholderValues.put(PlaceholderProperty.HOST_IDENTIFIER.getName(), getServerHost());
        placeholderValues.put(PlaceholderProperty.PORT_NUMBER.getName(), getServerPort());
        placeholderValues.put(PlaceholderProperty.SERVER_NAME.getName(), destinationServerName());
        placeholderValues.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), TEST_MARKER);
        placeholderValues.put(PostgresPlaceholderProperty.DATABASE_NAME.getName(), getDatabaseName());
        placeholderValues.put(PostgresPlaceholderProperty.SCHEMA_NAME.getName(), schemaName);
        placeholderValues.put(PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getName(),
                              "Destination schema for a subscription-fvt subscription.");
        placeholderValues.put(PlaceholderProperty.SECRETS_STORE.getName(), getSecretsStoreLocation());
        placeholderValues.put(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(), getSecretsCollectionName());

        return placeholderValues;
    }


    /**
     * Return a JDBC URL for one database on the server under test.
     *
     * @param databaseName database to connect to
     * @return JDBC URL
     */
    static String getJdbcURL(String databaseName)
    {
        return "jdbc:postgresql://" + getServerHost() + ":" + getServerPort() + "/" + databaseName;
    }


    /**
     * Open a JDBC connection to one database on the server under test, using the same credentials the servers
     * are configured with.
     * <br>
     * These are plain driver connections, so they arrive with auto-commit <b>on</b> - the JDBC default.  That
     * is deliberately different from the connections Egeria's own JDBC resource connector hands out, which
     * turn auto-commit off and require the caller to commit.  Callers here must therefore <em>not</em> commit:
     * doing so fails with "Cannot commit when autoCommit is enabled".
     *
     * @param databaseName database to connect to
     * @return open connection - the caller closes it
     * @throws Exception the server is not reachable or the credentials do not work
     */
    static Connection getServerUnderTestConnection(String databaseName) throws Exception
    {
        String userId   = OMAGPlatformExtension.getServerUnderTestSecret("userId");
        String password = OMAGPlatformExtension.getServerUnderTestSecret("clearPassword");

        if (userId == null)
        {
            throw new IllegalStateException("No credentials for the PostgreSQL server under test - check that"
                                                    + " subscription.fvt.server.secrets.store names a readable secrets store and that"
                                                    + " subscription.fvt.server.secrets.collection names a collection inside it.");
        }

        return DriverManager.getConnection(getJdbcURL(databaseName), userId, password);
    }


    /**
     * Make sure the destination schemas this suite subscribes into are there, holding exactly what this run
     * expects.
     * <br>
     * One empty schema is created for each destination a test needs.  The tables are deliberately not created:
     * a subscription's destination table is created by the provisioning, with the columns the product it
     * delivers actually has, so anything put there first would simply be a table with the wrong shape.
     * <br>
     * The <b>schemas</b> are always dropped and rebuilt, so a run always starts from the same contents.  The
     * <b>database</b> is only created if it is not already there, because creating one needs the CREATEDB
     * privilege and this suite should not require more than it has to: the default configuration points at a
     * database that already exists - the same one Egeria's own repository lives in - so nothing needs
     * creating.  Point {@code subscription.fvt.database.name} at a database that does not exist and the suite
     * will try to create it, which is where the privilege becomes necessary.
     * <br>
     * Set {@code subscription.fvt.clear.down} to false to leave a previous run's data in place for
     * inspection.
     *
     * @throws Exception the server under test is not usable, which is fatal to the whole run
     */
    static void prepareServerUnderTest() throws Exception
    {
        String databaseName = getDatabaseName();

        if (! OMAGPlatformExtension.getBooleanProperty("subscription.fvt.clear.down", true))
        {
            System.out.println("subscription-fvt: leaving database " + databaseName + " as it is -"
                                       + " subscription.fvt.clear.down is false.  The subscription assertions describe"
                                       + " destinations this suite creates, so data left over from an earlier run may not"
                                       + " match them.");
            return;
        }

        ensureDatabaseExists(databaseName);

        try (Connection connection = getServerUnderTestConnection(databaseName);
             Statement statement = connection.createStatement())
        {
            for (String schemaName : allDestinationSchemaNames())
            {
                statement.execute("drop schema if exists " + schemaName + " cascade");
                statement.execute("create schema " + schemaName);
            }

            /*
             * The schemas are left empty.  A destination table is created by the provisioning with the columns
             * the product actually has, so a table put here first would only be one with the wrong columns -
             * which is worse than none, because the provisioning would then try to describe columns that its
             * table does not have.
             */
        }

        System.out.println("subscription-fvt: prepared " + allDestinationSchemaNames().size()
                                   + " destination schema(s) in database " + databaseName + " on " + getServerHost()
                                   + ":" + getServerPort());
    }


    /**
     * Return the destination label used for one subscription type.  Every subscription type is tested
     * separately and each needs its own destination, because a subscription is named after the destination it
     * delivers to - sharing one would leave four subscriptions whose names differ only by a timestamp.
     *
     * @param subscriptionType type being subscribed to
     * @return short label
     */
    static String destinationPurpose(ProductSubscriptionDefinition subscriptionType)
    {
        return subscriptionType.getIdentifier();
    }


    /**
     * Return every schema this suite creates on the server under test: one per subscription type for the
     * product subscriptions, plus one for the product family subscription.
     *
     * @return schema names
     */
    static List<String> allDestinationSchemaNames()
    {
        List<String> schemaNames = new ArrayList<>();

        for (ProductSubscriptionDefinition subscriptionType : ProductSubscriptionDefinition.values())
        {
            schemaNames.add(destinationSchemaName(destinationPurpose(subscriptionType)));
        }

        schemaNames.add(destinationSchemaName(FAMILY_DESTINATION_PURPOSE));

        return schemaNames;
    }


    /**
     * Create the database this suite works with, if it is not already there.
     * <br>
     * Whether it exists is decided by trying to connect to it rather than by asking the maintenance database
     * what it holds.  That answers the question that actually matters - can this suite use it - in one step,
     * and it does not require the credentials to reach {@code pg_database} at all in the normal case where the
     * database is already there.
     *
     * @param databaseName database the suite needs
     * @throws Exception the database is neither reachable nor creatable
     */
    private static void ensureDatabaseExists(String databaseName) throws Exception
    {
        try (Connection connection = getServerUnderTestConnection(databaseName))
        {
            if (connection != null)
            {
                return;
            }
        }
        catch (Exception databaseNotThere)
        {
            System.out.println("subscription-fvt: database " + databaseName + " is not reachable (" + databaseNotThere.getMessage()
                                       + ") - trying to create it");
        }

        /*
         * "postgres" is the maintenance database that always exists, and a database cannot be created from a
         * connection attached to the database being created.  CREATE DATABASE cannot run inside a transaction
         * either, which is why nothing here turns auto-commit off.
         */
        try (Connection connection = getServerUnderTestConnection("postgres");
             Statement statement = connection.createStatement())
        {
            statement.execute("create database " + databaseName);

            System.out.println("subscription-fvt: created database " + databaseName);
        }
        catch (Exception couldNotCreate)
        {
            throw new IllegalStateException("Database " + databaseName + " does not exist on " + getServerHost() + ":"
                                                    + getServerPort() + " and could not be created (" + couldNotCreate.getMessage()
                                                    + ").  Creating a database needs the CREATEDB privilege, which the role in"
                                                    + " secrets collection '" + getSecretsCollectionName() + "' does not appear to"
                                                    + " have.  Either grant it, or point subscription.fvt.database.name at a database"
                                                    + " that already exists - the suite only needs to be able to create a schema"
                                                    + " inside it.",
                                            couldNotCreate);
        }
    }


    /*
     * =====================================================================================================
     * The digital product catalogue
     */


    /**
     * Remove the digital product catalogue an earlier run left behind, so that this run's Jacquard builds it
     * from scratch.
     * <br>
     * Jacquard reuses whatever it finds: a product that is already in the repository is not rebuilt, and
     * neither are its subscription options.  That is right for a running deployment and wrong for a test of
     * how the catalogue gets built - a change to that would be invisible here, and the tests that check what
     * is <em>absent</em> would go on passing against options that should no longer be offered.
     * <br>
     * Rebuilding is nevertheless <b>off by default</b>, because building the whole catalogue is most of this
     * suite's run time - it is 47 products and 181 notification types, and Jacquard offers no way to ask for
     * fewer.  A run that rebuilds takes tens of minutes rather than about ten, and puts enough load on an
     * FVT-sized deployment to start failing for reasons that have nothing to do with subscriptions.
     * <br>
     * So the default is to reuse the catalogue, and {@link #ensureCatalogueBuilt()} rebuilds anyway if what is
     * there does not match the definitions.  Ask for a rebuild explicitly - with
     * {@code -Dsubscription.fvt.rebuild.catalogue=true} - after changing how the catalogue is built.  That is
     * a deliberate trade of one risk for another: a run that reuses the catalogue is testing subscriptions,
     * not catalogue construction.
     * <br>
     * Products are purged with their anchored content, which takes the notification types, subscription
     * options and product assets with them.  The folders, communities, glossary terms and governance
     * definitions Jacquard organises them into are anchored elsewhere and are left to be reused.
     *
     * @throws Exception problem communicating with the server
     */
    static void clearCatalogue() throws Exception
    {
        if (! OMAGPlatformExtension.getBooleanProperty("subscription.fvt.rebuild.catalogue", false))
        {
            System.out.println("subscription-fvt: reusing the existing product catalogue.  Set"
                                       + " subscription.fvt.rebuild.catalogue to rebuild it - needed after a change to"
                                       + " how the catalogue is built, because Jacquard reuses what it finds.");
            return;
        }

        if (! OMAGPlatformExtension.getBooleanProperty("subscription.fvt.clear.down", true))
        {
            System.out.println("subscription-fvt: leaving the existing product catalogue in place -"
                                       + " subscription.fvt.clear.down is false.");
            return;
        }

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        int purgedCount = 0;

        for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
        {
            OpenMetadataElement product = openMetadataStore.getMetadataElementByUniqueName(productDefinition.getQualifiedName(),
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name);

            if (product != null)
            {
                purgeElement(openMetadataStore, product.getElementGUID());
                purgedCount++;
            }
        }

        if (purgedCount > 0)
        {
            System.out.println("subscription-fvt: purged " + purgedCount + " product(s) from a previous run's catalogue"
                                       + " so that this run builds its own");
        }
    }


    /**
     * Make sure the digital product catalogue has been built, once for the whole run.
     * <br>
     * Nothing in this suite creates a product.  The catalogue is built by the Jacquard Digital Product Loom
     * running in the integration daemon, and Jacquard refreshes on its own schedule - every 60 minutes - so
     * the suite asks for a refresh rather than waiting for one.  The refresh call returns as soon as the
     * daemon has accepted it, so this then waits for the catalogue itself to appear.
     * <br>
     * The wait is for the <em>last</em> product Jacquard defines rather than the first.  Jacquard works
     * through its definitions in order, so waiting for an early one would let the tests start against a
     * half-built catalogue - and a product that is missing because it has not been created yet fails in
     * exactly the same way as one that is missing because it is broken.
     *
     * @throws Exception the catalogue was not built, which is fatal to every test in the suite
     */
    static synchronized void ensureCatalogueBuilt() throws Exception
    {
        if (catalogueBuilt)
        {
            return;
        }

        OpenMetadataStore       openMetadataStore  = ConnectorContextFactory.newContext().getOpenMetadataStore();
        ProductDefinitionEnum[] productDefinitions = ProductDefinitionEnum.values();

        /*
         * Jacquard is refreshed even when the catalogue is already complete, and that is not wasted work:
         * refreshing is also what hands each product's notification types to the Baudot Subscription Manager
         * as catalog targets, so a run that skips it may leave the manager with nothing to look after -
         * subscriptions are taken out and nothing ever delivers them.  Where the products already exist
         * Jacquard finds each one and moves on, which is far cheaper than building them.
         */
        if (catalogueIsComplete(openMetadataStore, productDefinitions))
        {
            System.out.println("subscription-fvt: the product catalogue is already complete - refreshing Jacquard to"
                                       + " register the notification types with the subscription manager, but not rebuilding it");
        }

        OMAGPlatformExtension.getIntegrationDaemonClient()
                             .refreshConnector(IntegrationConnectorDefinition.PRODUCT_HARVESTER.getConnectorName());

        waitForElement(openMetadataStore,
                       productDefinitions[productDefinitions.length - 1].getQualifiedName(),
                       "Jacquard finished building the digital product catalogue");

        catalogueBuilt = true;
    }


    /**
     * Return whether every product Jacquard defines is already in the repository.
     * <br>
     * This is what makes reusing a catalogue safe enough to be the default: a catalogue that is missing
     * anything is rebuilt whether or not a rebuild was asked for.  It cannot tell that an existing product is
     * <em>stale</em> - that is what {@code subscription.fvt.rebuild.catalogue} is for.
     *
     * @param openMetadataStore store to read through
     * @param productDefinitions the products Jacquard defines
     * @return true if all of them are catalogued
     * @throws Exception problem reading the repository
     */
    private static boolean catalogueIsComplete(OpenMetadataStore       openMetadataStore,
                                               ProductDefinitionEnum[] productDefinitions) throws Exception
    {
        for (ProductDefinitionEnum productDefinition : productDefinitions)
        {
            if (openMetadataStore.getMetadataElementByUniqueName(productDefinition.getQualifiedName(),
                                                                 OpenMetadataProperty.QUALIFIED_NAME.name) == null)
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Return the qualified name that Jacquard gives to the governance action process a consumer runs to take
     * out one type of subscription to one product.  Derived from Jacquard's own naming rather than typed out,
     * because Jacquard is the thing that decides it.
     *
     * @param productDefinition product being subscribed to
     * @param subscriptionType type of subscription being taken out
     * @return qualified name of the subscribing action process
     */
    static String subscriptionProcessQualifiedName(ProductDefinition             productDefinition,
                                                   ProductSubscriptionDefinition subscriptionType)
    {
        return OpenMetadataType.PROVISIONING_ACTION_PROCESS.typeName + "::" + productDefinition.getProductName()
                       + "::" + ResourceUse.CREATE_SUBSCRIPTION.getResourceUse() + "::" + subscriptionType.getIdentifier();
    }


    /**
     * Return every element related to the supplied one by a relationship of the named type.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID element to start from
     * @param relationshipTypeName type of relationship to follow
     * @param startingAtEnd which end the starting element is at - 1 or 2, or 0 for either
     * @return related elements, never null
     * @throws Exception problem communicating with the server
     */
    static List<RelatedMetadataElement> getRelatedElements(OpenMetadataStore openMetadataStore,
                                                           String            elementGUID,
                                                           String            relationshipTypeName,
                                                           int               startingAtEnd) throws Exception
    {
        RelatedMetadataElementList related = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                          startingAtEnd,
                                                                                          relationshipTypeName,
                                                                                          new QueryOptions());

        if ((related == null) || (related.getElementList() == null))
        {
            return new ArrayList<>();
        }

        return related.getElementList();
    }


    /**
     * Return the number of rows in one table on the server under test.  Used by the tabular data set test to
     * check what the connector actually wrote, rather than trusting the connector's own count of it.
     *
     * @param connection open connection to the database holding the table
     * @param schemaName schema holding the table
     * @param tableName table to count
     * @return row count
     * @throws Exception the table is not readable
     */
    static long getRowCount(Connection connection,
                            String     schemaName,
                            String     tableName) throws Exception
    {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select count(*) from " + schemaName + "." + tableName))
        {
            resultSet.next();

            return resultSet.getLong(1);
        }
        catch (SQLException tableNotThere)
        {
            /*
             * A destination table that does not exist yet holds no rows.  It is created by the provisioning
             * when it first delivers, so its absence is the normal state of a subscription that has not been
             * delivered to - a test waiting for delivery has to be able to ask before that has happened.
             */
            return 0;
        }
    }


    /*
     * =====================================================================================================
     * Repository clean-up
     */


    /**
     * Permanently remove an element, whatever status it is in.  Failures are swallowed: this is best-effort
     * clean-up, not something a test should fail on.
     * <br>
     * PURGE only succeeds on an element that is already soft-deleted, so this soft-deletes first - itself
     * best-effort, since the element may already be deleted, for example by a cascade from its own anchor.
     * The delete cascades because most of what this suite creates is a graph: the catalog template creates the
     * server, its database manager, its connection and its endpoint in one act, all anchored to the server.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     */
    static void purgeElement(OpenMetadataStore openMetadataStore,
                             String            elementGUID)
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);
            softDeleteOptions.setCascadedDelete(true);
            softDeleteOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort - see the method comment above for why this is expected to fail sometimes.
        }

        try
        {
            DeleteOptions purgeOptions = new DeleteOptions();

            purgeOptions.setDeleteMethod(DeleteMethod.PURGE);
            purgeOptions.setCascadedDelete(true);
            purgeOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, purgeOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort clean-up - nothing further can be done if this fails.
        }
    }


    /**
     * Find and permanently purge every element whose qualified name contains {@link #TEST_MARKER}, in any
     * status.  Called once, after the servers are activated and before any test runs, so that debris from an
     * earlier - possibly failed - run does not affect this run's assertions.
     * <br>
     * A contains match is used rather than a starts-with one because this suite does not choose most of these
     * qualified names.  The catalog templates do, and they put the marker in the middle: a server asset is
     * "PostgreSQL Server::subscription-fvt-catalog", and a database catalogued beneath it is "PostgreSQL
     * Relational Database::subscription-fvt-catalog::postgres_fvt".
     *
     * @throws Exception problem communicating with the server - fatal, since a dirty repository would
     * invalidate the whole run
     */
    static void cleanUpLeftoverTestElements() throws Exception
    {
        if (! OMAGPlatformExtension.getBooleanProperty("subscription.fvt.clear.down", true))
        {
            System.out.println("subscription-fvt: leaving previous runs' metadata in place - subscription.fvt.clear.down is false."
                                       + "  Tests that assert on what this run created may see an earlier run's elements too.");
            return;
        }

        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper       propertyHelper    = new PropertyHelper();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        TEST_MARKER,
                                                                        PropertyComparisonOperator.LIKE));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));
        queryOptions.setPageSize(MAX_PAGE_SIZE);
        queryOptions.setForLineage(true);

        /*
         * Purging shrinks the result set, and a cascaded delete removes elements the current page has not
         * reached yet, so it is simplest - and safest against paging through a set that is disappearing
         * underneath the query - to keep re-querying from the start until nothing more comes back.
         */
        int purgedCount    = 0;
        int emptyPassLimit = 50;

        while (emptyPassLimit > 0)
        {
            List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

            if ((found == null) || found.isEmpty())
            {
                break;
            }

            for (OpenMetadataElement element : found)
            {
                purgeElement(openMetadataStore, element.getElementGUID());
                purgedCount++;
            }

            emptyPassLimit--;
        }

        purgedCount = purgedCount + purgeRetiredElements(openMetadataStore);
        purgedCount = purgedCount + purgeLeftoverGovernanceWork(openMetadataStore);

        if (purgedCount > 0)
        {
            System.out.println("subscription-fvt: purged " + purgedCount + " leftover test element(s) from a previous run before starting");
        }
    }


    /**
     * The qualified names of elements that an earlier version of the content pack defined and this one does
     * not.  A repository that persists between runs keeps them, and everything anchored to them, for ever.
     * <br>
     * The Baudot subscription manager was a watchdog governance service before it became an integration
     * connector.  Every notification the watchdog form sent was anchored to its governance service element,
     * and by the time the connector form arrived there were over twenty thousand of them - enough that
     * sweeping notifications by type, as {@link #purgeLeftoverGovernanceWork} does, took hours.  Purging the
     * anchor takes them all with it in one cascade.
     */
    private static final List<String> RETIRED_QUALIFIED_NAMES = List.of("baudot-subscription-manager-governance-service",
                                                                         "EgeriaWatchdog::baudot-subscription-manager");


    /**
     * Purge the elements that a previous version of the content pack defined and this one has retired, along
     * with everything anchored to them.  Harmless when they are not there - which, after the first run against
     * a repository, they are not.
     *
     * @param openMetadataStore store to purge through
     * @return number of elements purged
     */
    private static int purgeRetiredElements(OpenMetadataStore openMetadataStore)
    {
        int purgedCount = 0;

        for (String qualifiedName : RETIRED_QUALIFIED_NAMES)
        {
            try
            {
                OpenMetadataElement retiredElement = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name);

                if (retiredElement != null)
                {
                    System.out.println("subscription-fvt: purging retired element " + qualifiedName
                                               + " and everything anchored to it");
                    purgeElement(openMetadataStore, retiredElement.getElementGUID());
                    purgedCount++;
                }
            }
            catch (Exception ignored)
            {
                // Not there - the normal case on every run but the first against a repository.
            }
        }

        return purgedCount;
    }


    /**
     * Purge every engine action and governance action process instance in the repository.
     * <br>
     * These have to be removed by type rather than by qualified name, because they are the one thing this suite
     * causes to be created that does <em>not</em> carry {@link #TEST_MARKER}: their names are built from the
     * process step they run, not from the server name this suite chose.  The marker-based sweep above cannot
     * see them.
     * <br>
     * Leaving them behind is not merely untidy - it changes what the next run does.  An engine action that was
     * created but never claimed stays at REQUESTED or APPROVED for ever, and an engine host <em>sweeps for
     * exactly those</em> when its engines load their configuration.  So a run that ends with unclaimed actions
     * hands them to the next run's engine host, which dutifully carries them out: a previous run's assets get
     * created moments after start-up, and a survey whose action target this run has just purged fails with
     * "no asset action target supplied".  Both look like defects in the current run and are nothing of the
     * kind.
     * <br>
     * Digital subscriptions are swept the same way and for the same reason: the create-subscription service
     * names a subscription after the destination it delivers to, and a PostgreSQL schema name cannot carry
     * the hyphens {@link #TEST_MARKER} is written with - so the marker sweep above cannot see them either.
     * <br>
     * Notifications are swept too.  The Baudot subscription manager creates one for every subscriber it
     * notifies - over a thousand in a run - and nothing else removes them, so they would accumulate run after
     * run.  They are attached to the notification types they came from, and every read of a notification type
     * drags its notifications back with it, so the pile makes each refresh of the subscription manager slower
     * than the last for no benefit: a notification from an earlier run tells nobody anything.
     * <br>
     * Purging by type is safe here because this is a dedicated FVT repository: nothing but this suite creates
     * engine actions, subscriptions or notifications in it - the catalogue Jacquard builds contains the
     * <em>offers</em> of a subscription, never a subscription itself.
     *
     * @param openMetadataStore store to purge through
     * @return number of elements purged
     * @throws Exception problem communicating with the server
     */
    private static int purgeLeftoverGovernanceWork(OpenMetadataStore openMetadataStore) throws Exception
    {
        int purgedCount = 0;

        for (String typeName : List.of(OpenMetadataType.ENGINE_ACTION.typeName,
                                       OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName,
                                       OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName,
                                       OpenMetadataType.NOTIFICATION.typeName))
        {
            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setMetadataElementTypeName(typeName);
            queryOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));
            queryOptions.setPageSize(MAX_PAGE_SIZE);
            queryOptions.setForLineage(true);

            int emptyPassLimit = 50;

            while (emptyPassLimit > 0)
            {
                List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(null, null, queryOptions);

                if ((found == null) || found.isEmpty())
                {
                    break;
                }

                for (OpenMetadataElement element : found)
                {
                    purgeElement(openMetadataStore, element.getElementGUID());
                    purgedCount++;
                }

                emptyPassLimit--;
            }
        }

        return purgedCount;
    }


    /*
     * =====================================================================================================
     * Waiting for work that is happening somewhere else
     */


    /**
     * A condition that a test is prepared to wait for.
     */
    @FunctionalInterface
    interface WaitableCondition
    {
        /**
         * Has the thing being waited for happened yet?
         *
         * @return true when it has
         * @throws Exception checking threw - treated as fatal rather than as "not yet", because a query that
         * cannot run will not start working on the next poll
         */
        boolean isMet() throws Exception;
    }


    /**
     * Wait until the supplied condition is true, or report that it never became true.
     * <br>
     * Almost everything this suite tests happens in another server, so almost every assertion has to wait for
     * it.  Waiting is free when the thing being waited for happens - the loop leaves as soon as the condition
     * holds - so the timeouts are set well above what a healthy deployment needs.
     *
     * @param description what is being waited for, used in the failure message
     * @param timeoutSecondsProperty name of the application.properties setting holding the limit
     * @param defaultTimeoutSeconds limit to use when that setting is absent
     * @param condition the thing being waited for
     * @throws Exception the condition never became true, or checking it threw
     */
    static void waitFor(String            description,
                        String            timeoutSecondsProperty,
                        long              defaultTimeoutSeconds,
                        WaitableCondition condition) throws Exception
    {
        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty(timeoutSecondsProperty, defaultTimeoutSeconds) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("subscription.fvt.refresh.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        while (System.currentTimeMillis() < giveUpTime)
        {
            if (condition.isMet())
            {
                return;
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new AssertionError(description + " did not happen within " + (timeoutMilliseconds / 1000)
                                         + " seconds.  The audit log at build/subscription-fvt-data/logs/audit.log says what the"
                                         + " servers were doing while this test waited.");
    }


    /**
     * Wait for an element with the supplied qualified name to appear, and return it.
     *
     * @param openMetadataStore store to search
     * @param qualifiedName qualified name to wait for
     * @param description what this element is, used in the failure message
     * @return the element
     * @throws Exception it never appeared
     */
    static OpenMetadataElement waitForElement(OpenMetadataStore openMetadataStore,
                                              String            qualifiedName,
                                              String            description) throws Exception
    {
        List<OpenMetadataElement> holder = new ArrayList<>();

        waitFor(description + " (" + qualifiedName + ")",
                "subscription.fvt.refresh.timeout.seconds",
                180,
                () ->
                {
                    OpenMetadataElement element = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name);

                    if (element != null)
                    {
                        holder.add(element);
                        return true;
                    }

                    return false;
                });

        return holder.get(0);
    }


    /*
     * =====================================================================================================
     * Assertions shared by more than one test
     */


    /**
     * Return one string property of an element, or null if it does not have one by that name.
     * <br>
     * {@code getPropertiesAsStrings} renders every property - primitives, arrays and maps alike - as a string,
     * which is what makes it usable for a check like "is the qualified name what the template should have
     * built" without the caller having to know the property's storage type.
     *
     * @param element element to read
     * @param propertyName name of the property wanted
     * @return property value as a string, or null
     */
    static String getStringProperty(OpenMetadataElement element,
                                    String              propertyName)
    {
        if ((element == null) || (element.getElementProperties() == null))
        {
            return null;
        }

        Map<String, String> properties = element.getElementProperties().getPropertiesAsStrings();

        return (properties == null) ? null : properties.get(propertyName);
    }


    /**
     * Return one string property of a <em>relationship</em>, or null if it does not have one by that name.
     * The properties that say what a relationship means - the resource use on a ResourceList, the membership
     * type on a CollectionMembership - live on the relationship rather than on either element it joins.
     *
     * @param relatedElement related element carrying the relationship
     * @param propertyName name of the property wanted
     * @return property value as a string, or null
     */
    static String getRelationshipStringProperty(RelatedMetadataElement relatedElement,
                                                String                 propertyName)
    {
        if ((relatedElement == null) || (relatedElement.getRelationshipProperties() == null))
        {
            return null;
        }

        Map<String, String> properties = relatedElement.getRelationshipProperties().getPropertiesAsStrings();

        return (properties == null) ? null : properties.get(propertyName);
    }


    /**
     * Search one set of properties for placeholder markers that were never substituted.
     * <br>
     * A placeholder appears in a catalog template as <code>~{variableName}~</code>.  Finding one in an element
     * created from that template means the substitution did not happen: the catalogued element is carrying a
     * variable name where a real value belongs, which is worse than an empty property because it looks like
     * data.  Every property is checked as its string form, which covers arrays and maps as well as plain
     * strings - a placeholder hidden in one entry of an additionalProperties map matters just as much as one
     * in a display name.
     *
     * @param location where these properties were found, for the failure message
     * @param properties properties to search, may be null
     * @return one description per property still holding a marker, empty if there are none
     */
    static List<String> findPlaceholders(String            location,
                                         ElementProperties properties)
    {
        List<String> found = new ArrayList<>();

        if (properties == null)
        {
            return found;
        }

        Map<String, String> asStrings = properties.getPropertiesAsStrings();

        if (asStrings == null)
        {
            return found;
        }

        for (Map.Entry<String, String> property : asStrings.entrySet())
        {
            String value = property.getValue();

            if ((value != null) && value.contains("~{") && value.contains("}~"))
            {
                found.add(location + " -> " + property.getKey() + " = \"" + value + "\"");
            }
        }

        return found;
    }
}
