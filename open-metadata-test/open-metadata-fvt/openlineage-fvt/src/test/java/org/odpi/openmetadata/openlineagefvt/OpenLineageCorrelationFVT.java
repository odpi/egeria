/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageColumnLineageDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageColumnLineageDataSetFacetField;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageColumnLineageDataSetFacetInputField;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataSetFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEgeriaInformationSupplyChainRunFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunFacets;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DeployedDatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.RelationalDatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Verifies that the cataloguer attaches the lineage from OpenLineage events to the elements that describe the
 * physical landscape, and links the abstractions over them (tabular data sets and tabular data set collections, such
 * as the data sets of digital products) above them with DataSetContent.
 * <p>
 * The database is set up as the Postgres templates and the JDBC integration connector would leave it (the Postgres
 * content pack is not loaded by this suite, so the elements are created directly):
 * <ul>
 *     <li>a RelationalDatabase asset anchored to its software server, with a connection whose endpoint is the
 *     database's JDBC URL, and a DeployedDatabaseSchema {databaseQN}::public beneath it;</li>
 *     <li>RelationalTables {databaseQN}::public::orders (with column amount) and {databaseQN}::public::order_totals
 *     (with column total);</li>
 *     <li>a second catalogue entry for the same schema - a DeployedDatabaseSchema with its own endpoint and its own
 *     RelationalTable for orders - created later, so it is a duplicate;</li>
 *     <li>a tabular data set collection over the schema (like a digital product's data set) and a tabular data set
 *     over the orders table, each with a JDBC endpoint naming the schema.</li>
 * </ul>
 * The event reads orders and writes order_totals, with column lineage from amount to total, and names its
 * information supply chain in the egeria_informationSupplyChain facet.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OpenLineageCorrelationFVT
{
    private static final String SERVER_NAME   = "openlineage-fvt-server";
    private static final String HOST          = "openlineage-fvt-db";
    private static final String DATABASE_NAME = "fvtsales";
    private static final String SCHEMA_NAME   = "public";
    private static final String INPUT_TABLE   = "orders";
    private static final String OUTPUT_TABLE  = "order_totals";
    private static final String JOB_NAME      = "correlation.load_order_totals";
    private static final String ISC_QN        = "openlineage-fvt::correlation::information-supply-chain";

    private final PropertyHelper propertyHelper = new PropertyHelper();


    @Test
    @DisplayName("Lineage is attached to the catalogued tables and their schema, and the abstractions are linked above them")
    void lineageIsAttachedToThePhysicalLandscape() throws Exception
    {
        AssetClient       assetClient       = ConnectorContextFactory.newContext().getAssetClient();
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        String            jdbcURL           = "jdbc:postgresql://" + HOST + ":5432/" + DATABASE_NAME;
        String            schemaURL         = jdbcURL + "?currentSchema=" + SCHEMA_NAME;

        /*
         * The server, the database anchored to it, and the schema and tables the JDBC integration connector catalogues.
         */
        SoftwareServerProperties serverProperties = new SoftwareServerProperties();

        serverProperties.setQualifiedName(PostgresDeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType() + "::" + SERVER_NAME);
        serverProperties.setDisplayName(SERVER_NAME);

        String serverGUID = createAsset(assetClient, serverProperties, null);

        String databaseQN = PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType() + "::" + SERVER_NAME + "::" + DATABASE_NAME;

        RelationalDatabaseProperties databaseProperties = new RelationalDatabaseProperties();

        databaseProperties.setQualifiedName(databaseQN);
        databaseProperties.setDisplayName(DATABASE_NAME);

        String databaseGUID = createAsset(assetClient, databaseProperties, serverGUID);

        addConnection(openMetadataStore, databaseGUID, databaseQN, jdbcURL);

        DeployedDatabaseSchemaProperties schemaProperties = new DeployedDatabaseSchemaProperties();

        schemaProperties.setQualifiedName(databaseQN + "::" + SCHEMA_NAME);
        schemaProperties.setDisplayName(SCHEMA_NAME);

        String schemaGUID = createAsset(assetClient, schemaProperties, databaseGUID);

        String inputTableGUID  = createSchemaAttribute(openMetadataStore, OpenMetadataType.RELATIONAL_TABLE.typeName, databaseQN + "::" + SCHEMA_NAME + "::" + INPUT_TABLE, INPUT_TABLE, null);
        String amountGUID      = createSchemaAttribute(openMetadataStore, OpenMetadataType.RELATIONAL_COLUMN.typeName, databaseQN + "::" + SCHEMA_NAME + "::" + INPUT_TABLE + "::amount", "amount", inputTableGUID);
        String outputTableGUID = createSchemaAttribute(openMetadataStore, OpenMetadataType.RELATIONAL_TABLE.typeName, databaseQN + "::" + SCHEMA_NAME + "::" + OUTPUT_TABLE, OUTPUT_TABLE, null);
        String totalGUID       = createSchemaAttribute(openMetadataStore, OpenMetadataType.RELATIONAL_COLUMN.typeName, databaseQN + "::" + SCHEMA_NAME + "::" + OUTPUT_TABLE + "::total", "total", outputTableGUID);

        /*
         * A second catalogue entry for the same schema, created later: its orders table is a duplicate.
         */
        String duplicateSchemaQN = PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType() + "::" + SERVER_NAME + "::" + DATABASE_NAME + "." + SCHEMA_NAME;

        DeployedDatabaseSchemaProperties duplicateSchemaProperties = new DeployedDatabaseSchemaProperties();

        duplicateSchemaProperties.setQualifiedName(duplicateSchemaQN);
        duplicateSchemaProperties.setDisplayName(DATABASE_NAME + "." + SCHEMA_NAME);

        String duplicateSchemaGUID = createAsset(assetClient, duplicateSchemaProperties, null);

        addConnection(openMetadataStore, duplicateSchemaGUID, duplicateSchemaQN, schemaURL);

        String duplicateTableGUID = createSchemaAttribute(openMetadataStore, OpenMetadataType.RELATIONAL_TABLE.typeName, duplicateSchemaQN + "::" + INPUT_TABLE, INPUT_TABLE, null);

        /*
         * The abstractions: a collection over the schema (like a digital product's data set) and a data set over the
         * orders table.
         */
        String collectionQN   = PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType() + "::" + SERVER_NAME + "::" + DATABASE_NAME + "." + SCHEMA_NAME;
        String collectionGUID = createAbstraction(openMetadataStore, OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName, collectionQN, DATABASE_NAME + "." + SCHEMA_NAME);

        addConnection(openMetadataStore, collectionGUID, collectionQN, schemaURL);

        String dataSetQN   = PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getDeployedImplementationType() + "::" + SERVER_NAME + "::" + DATABASE_NAME + "." + SCHEMA_NAME + "." + INPUT_TABLE;
        String dataSetGUID = createAbstraction(openMetadataStore, OpenMetadataType.TABULAR_DATA_SET.typeName, dataSetQN, DATABASE_NAME + "." + SCHEMA_NAME + "." + INPUT_TABLE);

        addConnection(openMetadataStore, dataSetGUID, dataSetQN, schemaURL);

        /*
         * A run that reads orders and writes order_totals, named the OpenLineage way (the default port left out).
         */
        String namespace  = "postgres://" + HOST;
        String inputName  = DATABASE_NAME + "." + SCHEMA_NAME + "." + INPUT_TABLE;
        String outputName = DATABASE_NAME + "." + SCHEMA_NAME + "." + OUTPUT_TABLE;

        OpenLineageRunEvent event = OpenLineageEventFactory.runEvent("COMPLETE", Instant.now(), UUID.randomUUID(), JOB_NAME);

        OpenLineageEgeriaInformationSupplyChainRunFacet iscFacet = new OpenLineageEgeriaInformationSupplyChainRunFacet();

        iscFacet.setIscQualifiedName(ISC_QN);

        if (event.getRun().getFacets() == null)
        {
            event.getRun().setFacets(new OpenLineageRunFacets());
        }

        event.getRun().getFacets().setEgeriaInformationSupplyChain(iscFacet);

        OpenLineageInputDataSet input = new OpenLineageInputDataSet();

        input.setNamespace(namespace);
        input.setName(inputName);

        OpenLineageColumnLineageDataSetFacetInputField amountField = new OpenLineageColumnLineageDataSetFacetInputField();

        amountField.setNamespace(namespace);
        amountField.setName(inputName);
        amountField.setField("amount");

        OpenLineageColumnLineageDataSetFacetField totalField = new OpenLineageColumnLineageDataSetFacetField();

        totalField.setInputFields(List.of(amountField));

        OpenLineageColumnLineageDataSetFacet columnLineage = new OpenLineageColumnLineageDataSetFacet();

        columnLineage.setFields(Map.of("total", totalField));

        OpenLineageDataSetFacets outputFacets = new OpenLineageDataSetFacets();

        outputFacets.setColumnLineage(columnLineage);

        OpenLineageOutputDataSet output = new OpenLineageOutputDataSet();

        output.setNamespace(namespace);
        output.setName(outputName);
        output.setFacets(outputFacets);

        event.setInputs(List.of(input));
        event.setOutputs(List.of(output));

        OpenLineageFvtTestSupport.publish(event);

        /*
         * The lineage is attached to the tables that were catalogued first, and repeated at the level of their schema.
         */
        String processQN = OpenLineageFvtTestSupport.processQualifiedName(OpenLineageEventFactory.JOB_NAMESPACE, JOB_NAME);

        OpenMetadataRootElement process     = OpenLineageFvtTestSupport.waitForAsset(processQN, "the job's process");
        String                  processGUID = process.getElementHeader().getGUID();

        waitForRelationship(inputTableGUID, processGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, "the orders table to feed the job");
        waitForRelationship(processGUID, outputTableGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, "the job to feed the order_totals table");
        waitForRelationship(schemaGUID, processGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, "the schema to feed the job (asset-level lineage)");
        waitForRelationship(processGUID, schemaGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, "the job to feed the schema (asset-level lineage)");

        /*
         * Column and table mappings.
         */
        waitForRelationship(amountGUID, totalGUID, OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, "the amount column to map to the total column");
        waitForRelationship(inputTableGUID, outputTableGUID, OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, "the orders table to map to the order_totals table");

        /*
         * The duplicate catalogue entry takes no lineage but is linked as a peer duplicate.
         */
        OpenLineageFvtTestSupport.waitFor("the duplicate orders table to be linked as a peer duplicate",
                                          () -> arePeerDuplicates(openMetadataStore, inputTableGUID, duplicateTableGUID));

        assertTrue(OpenLineageFvtTestSupport.getRelatedElements(duplicateTableGUID, 0, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName).isEmpty(),
                   "The duplicate table should not take the lineage");

        /*
         * The abstractions are linked above the physical elements, for the information supply chain.
         */
        waitForRelationship(dataSetGUID, inputTableGUID, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, "the tabular data set to be linked to the orders table");
        waitForRelationship(collectionGUID, schemaGUID, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, "the collection to be linked to the schema");

        assertTrue(OpenLineageFvtTestSupport.getRelatedElements(dataSetGUID, 0, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName).isEmpty(),
                   "The tabular data set is an abstraction and should not take lineage");
        assertTrue(OpenLineageFvtTestSupport.getRelatedElements(collectionGUID, 0, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName).isEmpty(),
                   "The collection is an abstraction and should not take lineage");

        /*
         * No new asset was created for the tables.
         */
        assertNull(OpenLineageFvtTestSupport.getAsset(OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.TABULAR_DATA_SET.typeName, namespace, inputName)),
                   "No asset should be created for a table that is catalogued");
        assertNull(OpenLineageFvtTestSupport.getAsset(OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.TABULAR_DATA_SET.typeName, namespace, outputName)),
                   "No asset should be created for a table that is catalogued");
    }


    /**
     * Are two elements linked as peer duplicates?  Peer duplicate links are only returned to queries made for
     * duplicate processing.
     *
     * @param openMetadataStore client
     * @param oneGUID one element
     * @param otherGUID the other element
     * @return boolean
     * @throws Exception repository problem
     */
    private static boolean arePeerDuplicates(OpenMetadataStore openMetadataStore,
                                             String            oneGUID,
                                             String            otherGUID) throws Exception
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForDuplicateProcessing(true);
        queryOptions.setPageSize(OpenLineageFvtTestSupport.MAX_PAGE_SIZE);

        OpenMetadataRelationshipList relationships = openMetadataStore.findRelationshipsBetweenMetadataElements(OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                queryOptions);

        if ((relationships != null) && (relationships.getRelationships() != null))
        {
            for (OpenMetadataRelationship relationship : relationships.getRelationships())
            {
                if ((relationship != null) && (relationship.getElementAtEnd1() != null) && (relationship.getElementAtEnd2() != null))
                {
                    String end1GUID = relationship.getElementAtEnd1().getGUID();
                    String end2GUID = relationship.getElementAtEnd2().getGUID();

                    if (((oneGUID.equals(end1GUID)) && (otherGUID.equals(end2GUID))) || ((oneGUID.equals(end2GUID)) && (otherGUID.equals(end1GUID))))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Wait for a relationship tagged with the information supply chain.
     *
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param relationshipTypeName relationship type
     * @param description what is being waited for
     * @throws Exception timed out or repository problem
     */
    private void waitForRelationship(String end1GUID,
                                     String end2GUID,
                                     String relationshipTypeName,
                                     String description) throws Exception
    {
        OpenLineageFvtTestSupport.waitFor(description + " with " + relationshipTypeName + " for the information supply chain", () ->
        {
            for (RelatedMetadataElement related : OpenLineageFvtTestSupport.getRelatedElements(end1GUID, 1, relationshipTypeName))
            {
                if ((related.getElement() != null) && (end2GUID.equals(related.getElement().getElementGUID())) &&
                    (ISC_QN.equals(propertyHelper.getStringProperty("openlineage-fvt", OpenMetadataProperty.ISC_QUALIFIED_NAME.name, related.getRelationshipProperties(), "waitForRelationship"))))
                {
                    return true;
                }
            }

            return false;
        });
    }


    /**
     * Create and publish an asset.
     *
     * @param assetClient client
     * @param properties properties of the asset
     * @param anchorGUID anchor, or null for an asset that is its own anchor
     * @return guid
     * @throws Exception repository problem
     */
    private static String createAsset(AssetClient     assetClient,
                                      AssetProperties properties,
                                      String          anchorGUID) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

        if (anchorGUID == null)
        {
            newElementOptions.setIsOwnAnchor(true);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(anchorGUID);
        }

        String guid = assetClient.createAsset(newElementOptions, null, properties, null);

        assetClient.publishElement(guid);

        return guid;
    }


    /**
     * Create a tabular data set or tabular data set collection.
     *
     * @param openMetadataStore client
     * @param typeName type of abstraction
     * @param qualifiedName qualified name
     * @param displayName display name
     * @return guid
     * @throws Exception repository problem
     */
    private String createAbstraction(OpenMetadataStore openMetadataStore,
                                     String            typeName,
                                     String            qualifiedName,
                                     String            displayName) throws Exception
    {
        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName);

        return openMetadataStore.createMetadataElementInStore(typeName, null, null, properties);
    }


    /**
     * Create a table or column (a schema attribute), nested under its parent attribute if there is one.
     *
     * @param openMetadataStore client
     * @param typeName type of schema attribute
     * @param qualifiedName qualified name
     * @param displayName display name
     * @param parentGUID parent attribute (null for a table)
     * @return guid
     * @throws Exception repository problem
     */
    private String createSchemaAttribute(OpenMetadataStore openMetadataStore,
                                         String            typeName,
                                         String            qualifiedName,
                                         String            displayName,
                                         String            parentGUID) throws Exception
    {
        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName);

        String guid = openMetadataStore.createMetadataElementInStore(typeName, null, null, properties);

        if (parentGUID != null)
        {
            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, parentGUID, guid, null, null, null);
        }

        return guid;
    }


    /**
     * Add a connection with an endpoint to an asset, as the templates do.
     *
     * @param openMetadataStore client
     * @param assetGUID asset
     * @param assetQualifiedName qualified name of the asset
     * @param networkAddress endpoint network address
     * @throws Exception repository problem
     */
    private void addConnection(OpenMetadataStore openMetadataStore,
                               String            assetGUID,
                               String            assetQualifiedName,
                               String            networkAddress) throws Exception
    {
        ElementProperties connectionProperties = propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, assetQualifiedName + "::Connection");
        String            connectionGUID       = openMetadataStore.createMetadataElementInStore(OpenMetadataType.CONNECTION.typeName, null, null, connectionProperties);

        ElementProperties endpointProperties = propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, assetQualifiedName + "::Endpoint");

        endpointProperties = propertyHelper.addStringProperty(endpointProperties, OpenMetadataProperty.NETWORK_ADDRESS.name, networkAddress);

        String endpointGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.ENDPOINT.typeName, null, null, endpointProperties);

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, assetGUID, connectionGUID, null, null, null);
        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName, connectionGUID, endpointGUID, null, null, null);
    }
}
