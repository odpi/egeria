/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.unitycatalogfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Keeps open metadata in step with a Unity Catalog server that is changing.  The writable server is given a
 * catalog of this suite's own - a schema holding a table and a volume - which is catalogued and synchronized;
 * then a table is added in Unity Catalog, and another is dropped, and each change is checked to have reached
 * open metadata on the next refresh.
 * <br>
 * The catalog is the only thing this class creates on the writable server, and it is deleted again - with
 * everything in it - at the start and at the end of the class.  The server asset is told to catalogue only
 * that catalog, so whatever else is on the server is left out of open metadata as well as left alone.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnityCatalogSyncFVT
{
    private static final String SERVER_NAME     = UnityCatalogFvtTestSupport.serverUnderTestName("sync");
    private static final String NETWORK_ADDRESS = UnityCatalogFvtTestSupport.serverNetworkAddress(UnityCatalogFvtTestSupport.getWritableHostURL(),
                                                                                                  UnityCatalogFvtTestSupport.getWritablePort());

    private static final String CATALOG       = UnityCatalogFvtTestSupport.fixtureCatalogName("sync");
    private static final String SCHEMA        = "sales";
    private static final String TABLE         = "orders";
    private static final String ADDED_TABLE   = "returns";
    private static final String VOLUME        = "landing";

    private static String serverGUID = null;


    /**
     * Build the fixture catalog on the writable server, removing any copy a killed run left behind.
     *
     * @throws Exception the writable server is not usable, which is fatal to the class
     */
    @BeforeAll
    static void createFixtureCatalog() throws Exception
    {
        UnityCatalogFvtTestSupport.deleteWritableCatalog(CATALOG);

        UnityCatalogFvtTestSupport.createWritableCatalog(CATALOG);
        UnityCatalogFvtTestSupport.createWritableSchema(CATALOG, SCHEMA);
        UnityCatalogFvtTestSupport.createWritableTable(CATALOG, SCHEMA, TABLE);
        UnityCatalogFvtTestSupport.createWritableVolume(CATALOG, SCHEMA, VOLUME);
    }


    /**
     * Remove the fixture catalog from the writable server and the server asset from open metadata.
     *
     * @throws Exception the fixture catalog could not be removed
     */
    @AfterAll
    static void removeFixtureCatalog() throws Exception
    {
        UnityCatalogFvtTestSupport.deleteWritableCatalog(CATALOG);

        if (serverGUID != null)
        {
            UnityCatalogFvtTestSupport.purgeElement(ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore(), serverGUID);
        }
    }


    /**
     * Catalogue the writable server, limited to the fixture catalog, and synchronize it: the catalog, the
     * schema, the table with its columns, and the volume should all arrive.
     *
     * @throws Exception the fixture catalog was not synchronized
     */
    @Test
    @Order(1)
    @DisplayName("A catalog created in Unity Catalog is synchronized into open metadata")
    public void testFixtureCatalogIsSynchronized() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        Map<String, String> requestParameters = new HashMap<>(UnityCatalogFvtTestSupport.serverTemplatePlaceholders(SERVER_NAME,
                                                                                                                  UnityCatalogFvtTestSupport.getWritableHostURL(),
                                                                                                                  UnityCatalogFvtTestSupport.getWritablePort()));

        requestParameters.put(UnityCatalogConfigurationProperty.INCLUDE_CATALOG_NAMES.getName(), CATALOG);

        String processInstanceGUID = new AutomatedCurationClient().initiateGovernanceActionProcess(UnityCatalogServerCatalogFVT.CREATE_AS_CATALOG_TARGET_PROCESS,
                                                                                                     requestParameters,
                                                                                                     null);

        assertNotNull(processInstanceGUID, "The Automated Curation service returned no process instance to follow.");

        List<EngineActionElement> steps = new EngineActionWaiter().waitForProcess(processInstanceGUID,
                                                                                   UnityCatalogServerCatalogFVT.CREATE_AS_CATALOG_TARGET_PROCESS);

        assertEquals(2, steps.size(), "The create-as-catalog-target process ran " + steps.size() + " step(s) rather than two.");

        serverGUID = EngineActionWaiter.getActionTargetGUID(steps, ActionTarget.NEW_ASSET.getName());

        assertNotNull(serverGUID, "The create-as-catalog-target process recorded no asset.");

        /*
         * The server synchronizer catalogues the catalog and hands it on; only then is there anything for the
         * inside-catalog synchronizer to refresh.
         */
        OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnector(IntegrationConnectorDefinition.UC_SERVER_CATALOGUER.getConnectorName());

        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore, qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG, CATALOG), "the fixture catalog");

        /*
         * The catalog element is created a moment before the catalog is handed on, so the hand-off itself is
         * what is waited for.  A refresh asked for in between finds nothing new to do, and the catalog is then
         * not picked up until the synchronizer's own refresh interval comes round.
         */
        UnityCatalogFvtTestSupport.waitFor("catalog " + CATALOG + " to be handed to " + IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER.getDisplayName(),
                                           () -> UnityCatalogServerCatalogFVT.getCatalogTargetNames(openMetadataStore,
                                                                                                    IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER,
                                                                                                    serverGUID).contains(CATALOG));

        refreshInsideCatalogSynchronizer();

        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore, qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA, CATALOG + "." + SCHEMA), "schema " + SCHEMA);

        String tableQualifiedName = qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_TABLE, CATALOG + "." + SCHEMA + "." + TABLE);

        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore, tableQualifiedName, "table " + TABLE);
        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore, tableQualifiedName + "::id", "column id");
        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore, tableQualifiedName + "::description", "column description");
        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore, qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_VOLUME, CATALOG + "." + SCHEMA + "." + VOLUME), "volume " + VOLUME);

        /*
         * Every other catalog on the writable server is left out.
         */
        for (String catalogName : UnityCatalogFvtTestSupport.listCatalogs(NETWORK_ADDRESS))
        {
            if (! CATALOG.equals(catalogName))
            {
                assertNull(openMetadataStore.getMetadataElementByUniqueName(qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG, catalogName),
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name),
                           "Catalog " + catalogName + " was catalogued although includeCatalogNames names only " + CATALOG + ".");
            }
        }
    }


    /**
     * Add a table in Unity Catalog and check that the next refresh brings it into open metadata.
     *
     * @throws Exception the new table did not arrive
     */
    @Test
    @Order(2)
    @DisplayName("A table added in Unity Catalog is synchronized on the next refresh")
    public void testAddedTableIsSynchronized() throws Exception
    {
        assertNotNull(serverGUID, "The fixture catalog was never catalogued - the first test did not complete.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        UnityCatalogFvtTestSupport.createWritableTable(CATALOG, SCHEMA, ADDED_TABLE);

        refreshInsideCatalogSynchronizer();

        UnityCatalogFvtTestSupport.waitForElement(openMetadataStore,
                                                  qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_TABLE, CATALOG + "." + SCHEMA + "." + ADDED_TABLE),
                                                  "table " + ADDED_TABLE);
    }


    /**
     * Drop a table in Unity Catalog and check that the next refresh removes it from open metadata.  The
     * synchronizer owns what it catalogued, so a table that has gone from Unity Catalog should go from open
     * metadata too rather than linger as a description of something that no longer exists.
     *
     * @throws Exception the dropped table was not removed
     */
    @Test
    @Order(3)
    @DisplayName("A table dropped in Unity Catalog is removed from open metadata on the next refresh")
    public void testDroppedTableIsRemoved() throws Exception
    {
        assertNotNull(serverGUID, "The fixture catalog was never catalogued - the first test did not complete.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        UnityCatalogFvtTestSupport.deleteWritableTable(CATALOG, SCHEMA, TABLE);

        refreshInsideCatalogSynchronizer();

        UnityCatalogFvtTestSupport.waitForElementToGo(openMetadataStore,
                                                      qualifiedName(UnityCatalogDeployedImplementationType.OSS_UC_TABLE, CATALOG + "." + SCHEMA + "." + TABLE),
                                                      "table " + TABLE);
    }


    /**
     * Refresh the inside-catalog synchronizer, which is the one that catalogues what is inside each catalog.
     *
     * @throws Exception the integration daemon could not be reached
     */
    private static void refreshInsideCatalogSynchronizer() throws Exception
    {
        OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnector(IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER.getConnectorName());
    }


    /**
     * Build the qualified name the synchronizers give an element on the writable server.
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
}
