/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.postgresfvt;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared conventions and helpers for the postgres-fvt suite: how this run names the things it creates, how it
 * finds them again, how it prepares the PostgreSQL server it asks Egeria to catalogue, and how it waits for
 * work that is being done by another server.
 * <br>
 * Two kinds of debris have to be dealt with, and they live in different places.  In the <b>repository</b>,
 * every element this suite causes to be created carries {@link #TEST_MARKER} somewhere in its qualified name
 * - not because the suite sets those qualified names itself (the content pack's catalog templates do that)
 * but because the suite chooses the server names the templates build them from.  On the <b>PostgreSQL server
 * under test</b>, the suite creates a database of its own rather than cataloguing whatever happens to be
 * there, so that assertions can name exactly what should have been found.
 * <br>
 * Both are cleared at the <em>start</em> of a run rather than the end.  Clearing up afterwards leaves the
 * debris behind whenever a run is killed or crashes - which is exactly when it is most likely to be in a
 * state the next run should not inherit.
 */
final class PostgresFvtTestSupport
{
    /**
     * Marker carried by everything this suite creates, directly or through a governance action.  Server names
     * given to the catalog templates start with it, and the templates build their qualified names from those
     * server names, so a search for this string finds the whole graph a run produced.
     */
    static final String TEST_MARKER = "postgres-fvt";

    /**
     * Page size used by every client this suite creates, and configured on every server it starts.
     */
    static final int MAX_PAGE_SIZE = 500;

    private PostgresFvtTestSupport()
    {
        // no instances
    }


    /**
     * Return the name to use for a PostgreSQL server asset created by one test.
     * <br>
     * Each test uses a different name because the catalog template builds the asset's qualified name from it -
     * two tests using one name would be creating and deleting the same element, and would interfere with each
     * other in ways that depend on the order they happened to run in.  The names are stable across runs rather
     * than random, so that a run which is killed part-way leaves debris the next run recognises and removes.
     *
     * @param purpose short label for what the test is doing, for example "catalog"
     * @return server name
     */
    static String serverUnderTestName(String purpose)
    {
        return TEST_MARKER + "-" + purpose;
    }


    /**
     * Return the qualified name that the PostgreSQL Server catalog template gives to the asset it creates for
     * the named server.  Derived rather than typed out, because the template is the thing that decides it.
     *
     * @param serverName value supplied for the serverName placeholder
     * @return qualified name of the SoftwareServer asset
     */
    static String serverAssetQualifiedName(String serverName)
    {
        return "PostgreSQL Server::" + serverName;
    }


    /**
     * Return the qualified name that the PostgreSQL Relational Database catalog template gives to a database
     * catalogued on the named server.  This is what the PostgreSQL Server Cataloguer produces for each
     * database it finds, so it is how a test checks that the cataloguer ran and found the right thing.
     *
     * @param serverName value supplied for the serverName placeholder
     * @param databaseName name of the database on that server
     * @return qualified name of the RelationalDatabase asset
     */
    static String databaseAssetQualifiedName(String serverName,
                                             String databaseName)
    {
        return "PostgreSQL Relational Database::" + serverName + "::" + databaseName;
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
        return OMAGPlatformExtension.getProperty("postgres.fvt.server.host", "localhost");
    }


    /**
     * Return the port of the PostgreSQL server under test.
     *
     * @return port number as a string, because that is how it is supplied to the catalog template
     */
    static String getServerPort()
    {
        return OMAGPlatformExtension.getProperty("postgres.fvt.server.port", "5442");
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
        return OMAGPlatformExtension.getProperty("postgres.fvt.database.name", "egeria");
    }


    /**
     * Return the name of the schema this suite creates inside its database.
     *
     * @return schema name
     */
    static String getSchemaName()
    {
        return OMAGPlatformExtension.getProperty("postgres.fvt.schema.name", "postgres_fvt_schema");
    }


    /**
     * Return the name of the table this suite creates inside its schema.
     *
     * @return table name
     */
    static String getTableName()
    {
        return OMAGPlatformExtension.getProperty("postgres.fvt.table.name", "postgres_fvt_table");
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
        return OMAGPlatformExtension.getProperty("postgres.fvt.server.secrets.store",
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
        return OMAGPlatformExtension.getProperty("postgres.fvt.server.secrets.collection", "PostgreSQLRepository");
    }


    /**
     * Return a value for every placeholder the PostgreSQL Server catalog template declares.
     * <br>
     * The placeholder names come from the framework's own definitions rather than being written out, because
     * getting one wrong does not fail: the template would be created with the placeholder unsubstituted, and
     * the asset would end up carrying "~{secretsStorePathName}~" where a path belongs.  That is why every test
     * that creates from a template also checks for surviving placeholders - see {@link #findPlaceholders}.
     *
     * @param serverName name to give the PostgreSQL server being catalogued
     * @return placeholder values, ready to pass as request parameters or template placeholder values
     */
    static Map<String, String> serverTemplatePlaceholders(String serverName)
    {
        Map<String, String> placeholderValues = new HashMap<>();

        placeholderValues.put(PlaceholderProperty.HOST_IDENTIFIER.getName(), getServerHost());
        placeholderValues.put(PlaceholderProperty.PORT_NUMBER.getName(), getServerPort());
        placeholderValues.put(PlaceholderProperty.SERVER_NAME.getName(), serverName);
        placeholderValues.put(PlaceholderProperty.DESCRIPTION.getName(),
                              "PostgreSQL server catalogued by the postgres-fvt suite.");
        placeholderValues.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), TEST_MARKER);
        placeholderValues.put(PlaceholderProperty.RESOURCE_NAME.getName(), serverName);
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
                                                    + " postgres.fvt.server.secrets.store names a readable secrets store and that"
                                                    + " postgres.fvt.server.secrets.collection names a collection inside it.");
        }

        return DriverManager.getConnection(getJdbcURL(databaseName), userId, password);
    }


    /**
     * Make sure the database, schema and table that this suite asks Egeria to catalogue and survey are there,
     * holding exactly what this run expects.
     * <br>
     * The suite brings its own data rather than cataloguing whatever is already on the server, for two
     * reasons.  The assertions can then name exactly what should have been found - "the cataloguer created an
     * asset for this database" is a real assertion where "the cataloguer created some assets" is not.  And the
     * cataloguer can be scoped to it, so a run against a shared development server does not walk every
     * database on it.
     * <br>
     * The <b>schema</b> is always dropped and rebuilt, so a run always starts from the same contents.  The
     * <b>database</b> is only created if it is not already there, because creating one needs the CREATEDB
     * privilege and this suite should not require more than it has to: the default configuration points at a
     * database that already exists - the same one Egeria's own repository lives in - so nothing needs
     * creating.  Point {@code postgres.fvt.database.name} at a database that does not exist and the suite will
     * try to create it, which is where the privilege becomes necessary.
     * <br>
     * Set {@code postgres.fvt.clear.down} to false to leave a previous run's data in place for inspection.
     *
     * @throws Exception the server under test is not usable, which is fatal to the whole run
     */
    static void prepareServerUnderTest() throws Exception
    {
        String databaseName = getDatabaseName();

        if (! OMAGPlatformExtension.getBooleanProperty("postgres.fvt.clear.down", true))
        {
            System.out.println("postgres-fvt: leaving database " + databaseName + " as it is - postgres.fvt.clear.down is false."
                                       + "  The cataloguing and survey assertions describe what this suite creates, so data"
                                       + " left over from an earlier run may not match them.");
            return;
        }

        ensureDatabaseExists(databaseName);

        try (Connection connection = getServerUnderTestConnection(databaseName);
             Statement statement = connection.createStatement())
        {
            statement.execute("drop schema if exists " + getSchemaName() + " cascade");
            statement.execute("create schema " + getSchemaName());
            statement.execute("create table " + getSchemaName() + "." + getTableName()
                                      + " (id integer primary key, description varchar(80))");
            statement.execute("insert into " + getSchemaName() + "." + getTableName()
                                      + " values (1, 'first row'), (2, 'second row'), (3, 'third row')");
        }

        System.out.println("postgres-fvt: prepared database " + databaseName + " on " + getServerHost() + ":" + getServerPort()
                                   + " with table " + getSchemaName() + "." + getTableName());
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
            System.out.println("postgres-fvt: database " + databaseName + " is not reachable (" + databaseNotThere.getMessage()
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

            System.out.println("postgres-fvt: created database " + databaseName);
        }
        catch (Exception couldNotCreate)
        {
            throw new IllegalStateException("Database " + databaseName + " does not exist on " + getServerHost() + ":"
                                                    + getServerPort() + " and could not be created (" + couldNotCreate.getMessage()
                                                    + ").  Creating a database needs the CREATEDB privilege, which the role in"
                                                    + " secrets collection '" + getSecretsCollectionName() + "' does not appear to"
                                                    + " have.  Either grant it, or point postgres.fvt.database.name at a database"
                                                    + " that already exists - the suite only needs to be able to create a schema"
                                                    + " inside it.",
                                            couldNotCreate);
        }
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
     * "PostgreSQL Server::postgres-fvt-catalog", and a database catalogued beneath it is "PostgreSQL
     * Relational Database::postgres-fvt-catalog::postgres_fvt".
     *
     * @throws Exception problem communicating with the server - fatal, since a dirty repository would
     * invalidate the whole run
     */
    static void cleanUpLeftoverTestElements() throws Exception
    {
        if (! OMAGPlatformExtension.getBooleanProperty("postgres.fvt.clear.down", true))
        {
            System.out.println("postgres-fvt: leaving previous runs' metadata in place - postgres.fvt.clear.down is false."
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

        purgedCount = purgedCount + purgeLeftoverGovernanceWork(openMetadataStore);

        if (purgedCount > 0)
        {
            System.out.println("postgres-fvt: purged " + purgedCount + " leftover test element(s) from a previous run before starting");
        }
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
     * Purging by type is safe here because this is a dedicated FVT repository: nothing but this suite creates
     * engine actions in it.
     *
     * @param openMetadataStore store to purge through
     * @return number of elements purged
     * @throws Exception problem communicating with the server
     */
    private static int purgeLeftoverGovernanceWork(OpenMetadataStore openMetadataStore) throws Exception
    {
        int purgedCount = 0;

        for (String typeName : List.of(OpenMetadataType.ENGINE_ACTION.typeName,
                                       OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName))
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
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("postgres.fvt.refresh.poll.seconds", 2) * 1000;
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
                                         + " seconds.  The audit log at build/postgres-fvt-data/logs/audit.log says what the"
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
                "postgres.fvt.refresh.timeout.seconds",
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
