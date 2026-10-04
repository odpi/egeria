/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.unitycatalogfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Catalogues the read-only Unity Catalog server end to end, the way an operator would: create the server
 * asset and hand it to the Unity Catalog Server Synchronizer, let that synchronizer catalogue the server's
 * catalogs and pass each one to the Inside Catalog Synchronizer, let that one catalogue the schemas, tables,
 * columns, volumes and functions inside - and then delete it all again.
 * <br>
 * The four tests are the four stages of one lifecycle, so they share the server asset and run in order.
 * <br>
 * The server is catalogued with filters, which is how this class also tests them.  The catalog list keeps
 * only the sample catalog, and one table, one volume and one function in its schema are excluded by name.
 * Every filter is given in the short form - a table's own name, not its catalog-qualified name - and the
 * excluded elements are checked for absence only after their siblings have arrived, so that "not there yet"
 * cannot pass for "filtered out".
 * <br>
 * Nothing is written to the read-only server.  A catalog target with no direction set is synchronized both
 * ways, so this class restricts its catalog target to synchronizing from Unity Catalog before the first
 * refresh, and checks that the restriction is passed on to the inside-catalog synchronizer.
 * <br>
 * This is the only class that catalogues the read-only server.  The synchronizers name what they create
 * after the server's network address rather than after the server asset, so a second server asset pointing
 * at the same server would be writing the same elements.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnityCatalogServerCatalogFVT
{
    static final String CREATE_AS_CATALOG_TARGET_PROCESS = "UnityCatalogServer::CreateAsCatalogTargetGovernanceActionProcess";
    static final String DELETE_ASSET_PROCESS             = "UnityCatalogServer:DeleteAssetWithTemplateGovernanceActionProcess";

    private static final String SERVER_NAME     = UnityCatalogFvtTestSupport.serverUnderTestName("catalog");
    private static final String QUALIFIED_NAME  = UnityCatalogFvtTestSupport.serverAssetQualifiedName(SERVER_NAME);
    private static final String NETWORK_ADDRESS = UnityCatalogFvtTestSupport.serverNetworkAddress(UnityCatalogFvtTestSupport.getReadOnlyHostURL(),
                                                                                                  UnityCatalogFvtTestSupport.getReadOnlyPort());

    private static final String CATALOG = UnityCatalogFvtTestSupport.getSampleCatalogName();
    private static final String SCHEMA  = CATALOG + "." + UnityCatalogFvtTestSupport.getSampleSchemaName();

    /*
     * What the sample schema holds, split into what the filters keep and what they drop.
     */
    private static final List<String> KEPT_TABLES    = List.of("marksheet", "marksheet_uniform", "user_countries");
    private static final String       EXCLUDED_TABLE = "numbers";
    private static final List<String> KEPT_COLUMNS   = List.of("id", "name", "marks");
    private static final String       KEPT_VOLUME     = "json_files";
    private static final String       EXCLUDED_VOLUME = "txt_files";
    private static final String       KEPT_FUNCTION     = "lowercase";
    private static final String       EXCLUDED_FUNCTION = "sum";

    /**
     * The asset created by the first test and used by the ones that follow.
     */
    private static String newAssetGUID = null;


    /**
     * Build the request parameters used by both the create and the delete process.  The delete service
     * rebuilds the qualified name from the template and these values, so the two must match.
     * <br>
     * The filter settings ride along as request parameters too.  The service that attaches the asset to the
     * synchronizer copies every request parameter into the catalog target's configuration properties, and
     * the server synchronizer passes its catalog target's configuration on to the inside-catalog
     * synchronizer, which is how a schema-level filter set here reaches the connector that applies it.
     *
     * @return request parameters
     */
    private Map<String, String> getRequestParameters()
    {
        Map<String, String> requestParameters = new HashMap<>(UnityCatalogFvtTestSupport.serverTemplatePlaceholders(SERVER_NAME,
                                                                                                                  UnityCatalogFvtTestSupport.getReadOnlyHostURL(),
                                                                                                                  UnityCatalogFvtTestSupport.getReadOnlyPort()));

        requestParameters.put(UnityCatalogConfigurationProperty.INCLUDE_CATALOG_NAMES.getName(), CATALOG);
        requestParameters.put(UnityCatalogConfigurationProperty.EXCLUDE_TABLE_NAMES.getName(), EXCLUDED_TABLE);
        requestParameters.put(UnityCatalogConfigurationProperty.EXCLUDE_VOLUME_NAMES.getName(), EXCLUDED_VOLUME);
        requestParameters.put(UnityCatalogConfigurationProperty.EXCLUDE_FUNCTION_NAMES.getName(), EXCLUDED_FUNCTION);

        return requestParameters;
    }


    /**
     * Run the create-as-catalog-target process and check that it created the server asset and attached it to
     * the Unity Catalog Server Synchronizer.
     *
     * @throws Exception the process failed or did not do what it should
     */
    @Test
    @Order(1)
    @DisplayName("The create-as-catalog-target process catalogues a Unity Catalog server and hands it to the synchronizer")
    public void testCreateAsCatalogTargetProcess() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        String processInstanceGUID = new AutomatedCurationClient().initiateGovernanceActionProcess(CREATE_AS_CATALOG_TARGET_PROCESS,
                                                                                                     getRequestParameters(),
                                                                                                     null);

        assertNotNull(processInstanceGUID,
                      "The Automated Curation service accepted the request to run " + CREATE_AS_CATALOG_TARGET_PROCESS
                              + " but returned no process instance to follow.");

        List<EngineActionElement> steps = new EngineActionWaiter().waitForProcess(processInstanceGUID,
                                                                                   CREATE_AS_CATALOG_TARGET_PROCESS);

        assertEquals(2,
                     steps.size(),
                     "The create-as-catalog-target process is defined with two steps - create the asset, then attach it to the"
                             + " synchronizer - but ran " + steps.size() + ".");

        newAssetGUID = EngineActionWaiter.getActionTargetGUID(steps, ActionTarget.NEW_ASSET.getName());

        assertNotNull(newAssetGUID,
                      "No step of " + CREATE_AS_CATALOG_TARGET_PROCESS + " recorded a '" + ActionTarget.NEW_ASSET.getName()
                              + "' action target, so the second step had nothing to attach.");

        OpenMetadataElement newAsset = openMetadataStore.getMetadataElementByGUID(newAssetGUID);

        assertNotNull(newAsset, "The asset the process created cannot be read back from the repository.");

        assertEquals(QUALIFIED_NAME,
                     UnityCatalogFvtTestSupport.getStringProperty(newAsset, OpenMetadataProperty.QUALIFIED_NAME.name),
                     "The process created an asset with an unexpected qualified name.");

        List<String> survivingPlaceholders = UnityCatalogFvtTestSupport.findPlaceholders("the catalogued Unity Catalog server",
                                                                                          newAsset.getElementProperties());

        assertTrue(survivingPlaceholders.isEmpty(),
                   "The asset the process created still carries unsubstituted placeholders: " + survivingPlaceholders);

        RelatedMetadataElement catalogTarget = getCatalogTarget(openMetadataStore, IntegrationConnectorDefinition.UC_SERVER_CATALOGUER, newAssetGUID);

        assertNotNull(catalogTarget,
                      "The process completed but the new asset is not a catalog target of "
                              + IntegrationConnectorDefinition.UC_SERVER_CATALOGUER.getDisplayName()
                              + ", so the integration daemon will never be asked to catalogue it.");

        /*
         * A catalog target with no direction set is synchronized both ways, which would let the synchronizers
         * write to the read-only server.  It is restricted to synchronizing from Unity Catalog before the
         * first refresh is asked for.  The server synchronizer passes the direction on to the catalog targets it
         * creates for the inside-catalog synchronizer, which is checked in the next test.
         */
        CatalogTargetProperties fromThirdPartyOnly = new CatalogTargetProperties();

        fromThirdPartyOnly.setPermittedSynchronization(PermittedSynchronization.FROM_THIRD_PARTY);

        AssetClient assetClient = ConnectorContextFactory.newContext().getAssetClient();

        assetClient.updateCatalogTarget(catalogTarget.getRelationshipGUID(), assetClient.getUpdateOptions(true), fromThirdPartyOnly);
    }


    /**
     * Refresh the server synchronizer and check that it catalogued the sample catalog - and only that one -
     * and handed it to the inside-catalog synchronizer.
     * <br>
     * The hand-off is a catalog target too, from the inside-catalog synchronizer to the <em>server</em> asset,
     * named after the catalog.  It is checked here because nothing inside the catalog can be catalogued
     * without it.
     *
     * @throws Exception the synchronizer did not catalogue what it should
     */
    @Test
    @Order(2)
    @DisplayName("The server synchronizer catalogues the included catalog and hands it to the inside-catalog synchronizer")
    public void testServerSynchronizerCataloguesTheCatalog() throws Exception
    {
        assertNotNull(newAssetGUID, "There is no catalogued server to refresh - the create-as-catalog-target test did not complete.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnector(IntegrationConnectorDefinition.UC_SERVER_CATALOGUER.getConnectorName());

        OpenMetadataElement catalog = UnityCatalogFvtTestSupport.waitForElement(openMetadataStore,
                                                                                qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG, CATALOG),
                                                                                "the sample catalog");

        assertTrue(UnityCatalogFvtTestSupport.findPlaceholders("the catalogued catalog", catalog.getElementProperties()).isEmpty(),
                   "The catalog element still carries unsubstituted placeholders.");

        /*
         * The server asset is the inside-catalog synchronizer's catalog target once per catalog, each named
         * after its catalog.
         */
        UnityCatalogFvtTestSupport.waitFor("catalog " + CATALOG + " to be handed to " + IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER.getDisplayName(),
                                           () -> getCatalogTargetNames(openMetadataStore, IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER, newAssetGUID).contains(CATALOG));

        List<String> handedOn = getCatalogTargetNames(openMetadataStore, IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER, newAssetGUID);

        /*
         * The catalog must be synchronized in the same direction as its server - from Unity Catalog only -
         * or the inside-catalog synchronizer would be free to write to the read-only server.
         */
        String handOffDirection = propertyAsString(getCatalogTarget(openMetadataStore, IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER, newAssetGUID),
                                                   OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name);

        assertTrue(PermittedSynchronization.FROM_THIRD_PARTY.name().equals(handOffDirection)
                           || PermittedSynchronization.FROM_THIRD_PARTY.getDisplayName().equals(handOffDirection),
                   "The catalog target handed to the inside-catalog synchronizer does not carry its server's direction of"
                           + " synchronization (" + PermittedSynchronization.FROM_THIRD_PARTY.name() + "); it carries " + handOffDirection + ".");

        /*
         * Every catalog on the server is listed in one call, so by the time the included one is here an
         * excluded one would be too.
         */
        for (String catalogName : UnityCatalogFvtTestSupport.listCatalogs(NETWORK_ADDRESS))
        {
            if (! CATALOG.equals(catalogName))
            {
                assertNull(openMetadataStore.getMetadataElementByUniqueName(qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG, catalogName),
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name),
                           "Catalog " + catalogName + " was catalogued although includeCatalogNames names only " + CATALOG + ".");

                assertTrue(! handedOn.contains(catalogName),
                           "Catalog " + catalogName + " was handed to the inside-catalog synchronizer although includeCatalogNames names only "
                                   + CATALOG + ".  Catalog targets: " + handedOn);
            }
        }
    }


    /**
     * Refresh the inside-catalog synchronizer and check what it catalogued inside the sample catalog: the
     * schema, the tables with their columns, a volume and a function - and none of the elements the filters
     * name.
     *
     * @throws Exception the synchronizer did not catalogue what it should
     */
    @Test
    @Order(3)
    @DisplayName("The inside-catalog synchronizer catalogues the schema, tables, columns, volumes and functions the filters allow")
    public void testInsideCatalogSynchronizerCataloguesTheContents() throws Exception
    {
        assertNotNull(newAssetGUID, "There is no catalogued server - the create-as-catalog-target test did not complete.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnector(IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER.getConnectorName());

        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore,
                                                  qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA, SCHEMA),
                                                  "the sample schema");

        for (String table : KEPT_TABLES)
        {
            OpenMetadataElement tableElement = UnityCatalogFvtTestSupport.waitForElement(openMetadataStore,
                                                                                         qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_TABLE, SCHEMA + "." + table),
                                                                                         "table " + table);

            assertTrue(UnityCatalogFvtTestSupport.findPlaceholders("table " + table, tableElement.getElementProperties()).isEmpty(),
                       "Table " + table + " still carries unsubstituted placeholders.");
        }

        String marksheet = qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_TABLE, SCHEMA + "." + KEPT_TABLES.get(0));

        for (String column : KEPT_COLUMNS)
        {
            UnityCatalogFvtTestSupport.waitForElement(openMetadataStore, marksheet + "::" + column, "column " + column);
        }

        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore,
                                                  qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_VOLUME, SCHEMA + "." + KEPT_VOLUME),
                                                  "volume " + KEPT_VOLUME);

        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore,
                                                  qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION, SCHEMA + "." + KEPT_FUNCTION),
                                                  "function " + KEPT_FUNCTION);

        /*
         * Their siblings are all here, so the synchronizer has been through the schema: anything still
         * missing was filtered out rather than not reached yet.
         */
        assertNull(openMetadataStore.getMetadataElementByUniqueName(qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_TABLE, SCHEMA + "." + EXCLUDED_TABLE),
                                                                    OpenMetadataProperty.QUALIFIED_NAME.name),
                   "Table " + EXCLUDED_TABLE + " was catalogued although excludeTableNames names it.");

        assertNull(openMetadataStore.getMetadataElementByUniqueName(qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_VOLUME, SCHEMA + "." + EXCLUDED_VOLUME),
                                                                    OpenMetadataProperty.QUALIFIED_NAME.name),
                   "Volume " + EXCLUDED_VOLUME + " was catalogued although excludeVolumeNames names it.");

        assertNull(openMetadataStore.getMetadataElementByUniqueName(qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION, SCHEMA + "." + EXCLUDED_FUNCTION),
                                                                    OpenMetadataProperty.QUALIFIED_NAME.name),
                   "Function " + EXCLUDED_FUNCTION + " was catalogued although excludeFunctionNames names it.");
    }


    /**
     * Run the delete process and check that the server asset - and the catalog, schema and table elements
     * catalogued beneath it - have gone.
     *
     * @throws Exception the process failed, or left something behind
     */
    @Test
    @Order(4)
    @DisplayName("The delete process removes the catalogued server and everything catalogued beneath it")
    public void testDeleteAssetProcess() throws Exception
    {
        assertNotNull(newAssetGUID, "There is no catalogued server to delete - the create-as-catalog-target test did not complete.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        try
        {
            String processInstanceGUID = new AutomatedCurationClient().initiateGovernanceActionProcess(DELETE_ASSET_PROCESS,
                                                                                                         getRequestParameters(),
                                                                                                         null);

            assertNotNull(processInstanceGUID,
                          "The Automated Curation service accepted the request to run " + DELETE_ASSET_PROCESS
                                  + " but returned no process instance to follow.");

            new EngineActionWaiter().waitForProcess(processInstanceGUID, DELETE_ASSET_PROCESS);

            assertNull(openMetadataStore.getMetadataElementByUniqueName(QUALIFIED_NAME, OpenMetadataProperty.QUALIFIED_NAME.name),
                       "The delete process completed but " + QUALIFIED_NAME + " is still in the repository.");

            List<String> survivors = new ArrayList<>();

            for (String qualifiedName : List.of(qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG, CATALOG),
                                                qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA, SCHEMA),
                                                qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_TABLE, SCHEMA + "." + KEPT_TABLES.get(0))))
            {
                if (openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name) != null)
                {
                    survivors.add(qualifiedName);
                }
            }

            assertTrue(survivors.isEmpty(),
                       "The server asset was deleted but elements catalogued beneath it are still in the repository: " + survivors
                               + ".  The catalog is anchored to the server and everything inside it to the catalog, so a"
                               + " cascading delete should have removed them.");
        }
        finally
        {
            UnityCatalogFvtTestSupport.purgeElement(openMetadataStore, newAssetGUID);
            newAssetGUID = null;
        }
    }


    /**
     * Build the qualified name the synchronizers give an element on the read-only server.
     *
     * @param type deployed implementation type
     * @param fullName Unity Catalog full name
     * @return qualified name
     */
    private static String qualifiedName(UnityCatalogDeployedImplementationType type,
                                        String                                 fullName)
    {
        return UnityCatalogFvtTestSupport.ucElementQualifiedName(type, NETWORK_ADDRESS, fullName);
    }


    /**
     * Return the catalog target relationship from an integration connector to an asset, if there is one.
     *
     * @param openMetadataStore store to read from
     * @param connector integration connector
     * @param assetGUID asset to look for
     * @return the related element (with the relationship's properties), or null
     * @throws Exception the repository could not be read
     */
    static RelatedMetadataElement getCatalogTarget(OpenMetadataStore              openMetadataStore,
                                                   IntegrationConnectorDefinition connector,
                                                   String                         assetGUID) throws Exception
    {
        RelatedMetadataElementList catalogTargets = openMetadataStore.getRelatedMetadataElements(connector.getGUID(),
                                                                                                 1,
                                                                                                 OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                                                                 0,
                                                                                                 UnityCatalogFvtTestSupport.MAX_PAGE_SIZE);

        if ((catalogTargets != null) && (catalogTargets.getElementList() != null))
        {
            for (RelatedMetadataElement catalogTarget : catalogTargets.getElementList())
            {
                if (assetGUID.equals(catalogTarget.getElement().getElementGUID()))
                {
                    return catalogTarget;
                }
            }
        }

        return null;
    }


    /**
     * Return the names of every catalog target relationship from an integration connector to an asset.
     *
     * @param openMetadataStore store to read from
     * @param connector integration connector
     * @param assetGUID asset to look for
     * @return catalog target names (empty if there are none)
     * @throws Exception the repository could not be read
     */
    static List<String> getCatalogTargetNames(OpenMetadataStore              openMetadataStore,
                                              IntegrationConnectorDefinition connector,
                                              String                         assetGUID) throws Exception
    {
        List<String> names = new ArrayList<>();

        RelatedMetadataElementList catalogTargets = openMetadataStore.getRelatedMetadataElements(connector.getGUID(),
                                                                                                 1,
                                                                                                 OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                                                                 0,
                                                                                                 UnityCatalogFvtTestSupport.MAX_PAGE_SIZE);

        if ((catalogTargets != null) && (catalogTargets.getElementList() != null))
        {
            for (RelatedMetadataElement catalogTarget : catalogTargets.getElementList())
            {
                if (assetGUID.equals(catalogTarget.getElement().getElementGUID()))
                {
                    names.add(propertyAsString(catalogTarget, OpenMetadataProperty.CATALOG_TARGET_NAME.name));
                }
            }
        }

        return names;
    }


    /**
     * Return one property of a relationship as a string.
     *
     * @param relatedElement related element carrying the relationship's properties
     * @param propertyName property name
     * @return value, or null
     */
    private static String propertyAsString(RelatedMetadataElement relatedElement,
                                           String                 propertyName)
    {
        if ((relatedElement == null) || (relatedElement.getRelationshipProperties() == null)
                || (relatedElement.getRelationshipProperties().getPropertiesAsStrings() == null))
        {
            return null;
        }

        return relatedElement.getRelationshipProperties().getPropertiesAsStrings().get(propertyName);
    }
}
