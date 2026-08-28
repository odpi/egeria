/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.postgresfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs the PostgreSQL content pack's cataloguing lifecycle end to end: catalogue a PostgreSQL server, hand it
 * to the PostgreSQL Server Cataloguer as a catalog target, let the cataloguer run, and then take it all away
 * again.
 * <br>
 * This is the test that exercises the <b>integration connector</b>, and the shape of it is the point.  Nothing
 * here calls the connector.  A governance action process creates the asset and attaches it to the connector as
 * a catalog target; the integration daemon - a different server - then notices the new target and catalogues
 * what it finds behind it.  So what is being tested is not only "does the connector work" but "does the
 * arrangement the content pack describes actually deliver work to it".
 * <br>
 * The three tests run in order and share state, because they are three parts of one story: an asset that was
 * never catalogued cannot be refreshed, and an asset that was never created cannot be deleted.  Running them
 * as one test would make a failure in the middle say nothing about which half was at fault.
 * <br>
 * The connector is scoped to this suite's own database with {@code includeDatabaseList}.  Without it, a run
 * against a shared development server would catalogue every database on it - slow, and with nothing specific
 * to assert.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostgresServerCatalogFVT
{
    /**
     * The governance action process that creates the asset and attaches it to the cataloguer.  Also referenced
     * by {@link AutomatedCurationFVT}, which checks that a curator would be offered it.
     */
    static final String CREATE_AS_CATALOG_TARGET_PROCESS = "PostgreSQLServer::CreateAsCatalogTargetGovernanceActionProcess";

    /**
     * The governance action process that removes it again.
     */
    static final String DELETE_ASSET_PROCESS = "PostgreSQLServer:DeleteAssetWithTemplateGovernanceActionProcess";

    /**
     * The name this test gives the PostgreSQL server it catalogues, and the qualified name the catalog template
     * builds from it.
     */
    private static final String SERVER_NAME    = PostgresFvtTestSupport.serverUnderTestName("catalog");
    private static final String QUALIFIED_NAME = PostgresFvtTestSupport.serverAssetQualifiedName(SERVER_NAME);

    /**
     * The asset created by the first test and used by the two that follow.  Static because the three tests are
     * three stages of one lifecycle - see the class comment.
     */
    private static String newAssetGUID = null;


    /**
     * Build the request parameters used by both the create and the delete process.
     * <br>
     * The two processes are given the <em>same</em> parameters deliberately.  The delete service does not take
     * the asset's GUID: it rebuilds the qualified name from the template and the placeholder values, exactly as
     * the create service did, and deletes what it finds.  So passing the same values is what makes the delete
     * address the same asset - and a test that passed different ones would silently delete nothing.
     *
     * @return request parameters
     */
    private Map<String, String> getRequestParameters()
    {
        Map<String, String> requestParameters = new HashMap<>(PostgresFvtTestSupport.serverTemplatePlaceholders(SERVER_NAME));

        /*
         * The catalog step copies the request parameters onto the catalog target relationship as its
         * configuration properties, which is how a connector is told how to treat one particular target.  This
         * one scopes the cataloguer to the database this suite created.
         */
        requestParameters.put(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                              PostgresFvtTestSupport.getDatabaseName());

        return requestParameters;
    }


    /**
     * Run the create-as-catalog-target process, and check that it both created the asset and attached it to the
     * PostgreSQL Server Cataloguer.
     *
     * @throws Exception the process failed, or produced something other than what was asked for
     */
    @Test
    @Order(1)
    @DisplayName("The create-as-catalog-target process catalogues a server and hands it to the cataloguer")
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
                             + " integration connector - but ran " + steps.size() + ".");

        /*
         * The whole process is searched rather than its first step: a completion action target is attached to the
         * step that follows the one that produced it, so the asset created by step one appears on step two.
         */
        newAssetGUID = EngineActionWaiter.getActionTargetGUID(steps, ActionTarget.NEW_ASSET.getName());

        assertNotNull(newAssetGUID,
                      "No step of " + CREATE_AS_CATALOG_TARGET_PROCESS + " recorded a '" + ActionTarget.NEW_ASSET.getName()
                              + "' action target, so the second step had nothing to attach.");

        OpenMetadataElement newAsset = openMetadataStore.getMetadataElementByGUID(newAssetGUID);

        assertNotNull(newAsset, "The asset the process created cannot be read back from the repository.");

        assertEquals(QUALIFIED_NAME,
                     PostgresFvtTestSupport.getStringProperty(newAsset, OpenMetadataProperty.QUALIFIED_NAME.name),
                     "The process created an asset with an unexpected qualified name.");

        List<String> survivingPlaceholders = PostgresFvtTestSupport.findPlaceholders("the catalogued PostgreSQL server",
                                                                                      newAsset.getElementProperties());

        assertTrue(survivingPlaceholders.isEmpty(),
                   "The asset the process created still carries unsubstituted placeholders: " + survivingPlaceholders);

        /*
         * The second step's whole job is this relationship.  It is read from the connector's end rather than
         * the asset's, because that is the direction the integration daemon reads it in.
         */
        assertTrue(isCatalogTargetOf(openMetadataStore, newAssetGUID),
                   "The process completed but the new asset is not a catalog target of '"
                           + IntegrationConnectorDefinition.POSTGRES_SERVER_CATALOGUER.getDisplayName()
                           + "', so the integration daemon will never be asked to catalogue it.");
    }


    /**
     * Refresh the cataloguer and check that it catalogued the database this suite created on the server.
     * <br>
     * The refresh is asked for rather than waited for.  The connector's own refresh interval is 60 seconds, and
     * a test that waited for it would be a minute slower for no extra coverage - the integration daemon's
     * refresh API drives exactly the same {@code refresh()} call on the same running connector instance.
     *
     * @throws Exception the connector did not catalogue what it was given
     */
    @Test
    @Order(2)
    @DisplayName("The PostgreSQL Server Cataloguer catalogues the databases on its catalog target")
    public void testCataloguerCataloguesTheDatabase() throws Exception
    {
        assertNotNull(newAssetGUID,
                      "There is no catalogued server to refresh - the create-as-catalog-target test did not complete.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        String connectorName = IntegrationConnectorDefinition.POSTGRES_SERVER_CATALOGUER.getConnectorName();

        OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnector(connectorName);

        String databaseQualifiedName = PostgresFvtTestSupport.databaseAssetQualifiedName(SERVER_NAME,
                                                                                          PostgresFvtTestSupport.getDatabaseName());

        OpenMetadataElement databaseAsset = PostgresFvtTestSupport.waitForElement(openMetadataStore,
                                                                                   databaseQualifiedName,
                                                                                   "the database asset the PostgreSQL Server"
                                                                                           + " Cataloguer should have created");

        List<String> survivingPlaceholders = PostgresFvtTestSupport.findPlaceholders("the catalogued database",
                                                                                      databaseAsset.getElementProperties());

        assertTrue(survivingPlaceholders.isEmpty(),
                   "The database asset the cataloguer created still carries unsubstituted placeholders: " + survivingPlaceholders);

        System.out.println("postgres-fvt: the cataloguer created " + databaseQualifiedName);
    }


    /**
     * Run the delete process and check that the asset - and everything anchored to it - has gone.
     * <br>
     * This is the other half of the lifecycle, and it matters as much as the first: an automated cataloguer
     * that cannot be undone leaves a repository that only grows.  The database asset the cataloguer created is
     * anchored to the server asset, so it should go too, and that is checked rather than assumed.
     *
     * @throws Exception the process failed, or left something behind
     */
    @Test
    @Order(3)
    @DisplayName("The delete process removes the catalogued server and everything anchored to it")
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

            OpenMetadataElement deletedAsset = openMetadataStore.getMetadataElementByUniqueName(QUALIFIED_NAME,
                                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name);

            assertNull(deletedAsset,
                       "The delete process completed but " + QUALIFIED_NAME + " is still in the repository."
                               + "  The delete service finds the asset by rebuilding its qualified name from the template and the"
                               + " request parameters, so a surviving asset usually means the two runs were given different"
                               + " placeholder values.");

            String databaseQualifiedName = PostgresFvtTestSupport.databaseAssetQualifiedName(SERVER_NAME,
                                                                                              PostgresFvtTestSupport.getDatabaseName());

            OpenMetadataElement deletedDatabase = openMetadataStore.getMetadataElementByUniqueName(databaseQualifiedName,
                                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name);

            assertNull(deletedDatabase,
                       "The server asset was deleted but the database asset the cataloguer created beneath it, "
                               + databaseQualifiedName + ", is still in the repository.  It is anchored to the server, so a"
                               + " cascading delete should have removed it.");
        }
        finally
        {
            /*
             * Best-effort: if the process did not delete the asset, this run should still not leave it for the
             * next one.  Harmless when the process did its job.
             */
            PostgresFvtTestSupport.purgeElement(openMetadataStore, newAssetGUID);
            newAssetGUID = null;
        }
    }


    /**
     * Is the supplied asset a catalog target of the PostgreSQL Server Cataloguer?
     *
     * @param openMetadataStore store to read from
     * @param assetGUID asset to look for
     * @return true if the catalog target relationship is there
     * @throws Exception the repository could not be read
     */
    private boolean isCatalogTargetOf(OpenMetadataStore openMetadataStore,
                                      String            assetGUID) throws Exception
    {
        RelatedMetadataElementList catalogTargets =
                openMetadataStore.getRelatedMetadataElements(IntegrationConnectorDefinition.POSTGRES_SERVER_CATALOGUER.getGUID(),
                                                             1,
                                                             OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                             0,
                                                             PostgresFvtTestSupport.MAX_PAGE_SIZE);

        if ((catalogTargets != null) && (catalogTargets.getElementList() != null))
        {
            for (RelatedMetadataElement catalogTarget : catalogTargets.getElementList())
            {
                if (assetGUID.equals(catalogTarget.getElement().getElementGUID()))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
