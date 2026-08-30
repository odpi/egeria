/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FolderCatalogFVT hands a folder to the General Folder Cataloguer as a catalog target and checks that the
 * files inside it are catalogued.
 * <br>
 * Nothing here calls the integration connector.  The request goes to the view server, a governance service on
 * the engine host attaches the folder to the connector as a catalog target, and the connector then runs in the
 * <em>integration daemon</em> - a third server - and does the cataloguing.  That arrangement is as much what
 * is being tested as the connector itself: the same connector driven directly would prove far less.
 * <br>
 * The refresh is asked for rather than waited out.  A cataloguer has a refresh interval measured in minutes,
 * and a test that waited for one would spend that long doing nothing and would then be timing-dependent for
 * no benefit.  Asking the daemon to refresh is what an operator does when they do not want to wait either.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class FolderCatalogFVT
{
    private static final RequestTypeDefinition CREATE_FILE_FOLDER  = RequestTypeDefinition.CREATE_FILE_FOLDER;
    private static final RequestTypeDefinition CATALOG_FILE_FOLDER = RequestTypeDefinition.CATALOG_FILE_FOLDER;

    private static final IntegrationConnectorDefinition CATALOGUER = IntegrationConnectorDefinition.GENERAL_FOLDER_CATALOGUER;


    @Test
    @DisplayName("A folder handed to the folder cataloguer has its files catalogued")
    public void testCatalogueFolderContents() throws Exception
    {
        File   folder        = FilesFvtTestSupport.folderUnderTest("catalog");
        String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        String folderAssetGUID = null;

        try
        {
            assertTrue(folder.isDirectory(),
                       "The folder under test was not built: " + folder.getAbsolutePath());

            /*
             * Catalogue the folder itself first - the cataloguer is given an asset, not a path.
             */
            Map<String, String> createParameters = new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder));

            String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                    FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                    createParameters,
                    null);

            new EngineActionWaiter().waitForCompletion(createActionGUID,
                                                        FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            OpenMetadataElement folderAsset = FilesFvtTestSupport.waitForElement(openMetadataStore,
                                                                                 qualifiedName,
                                                                                 "the folder asset to catalogue");

            assertNotNull(folderAsset, "No folder asset called " + qualifiedName + " arrived in the repository.");

            folderAssetGUID = folderAsset.getElementGUID();

            /*
             * Hand it to the cataloguer.  This governance action type carries the connector it attaches to in
             * its own definition - see getCatalogTargetAssetActionTargets(GENERAL_FOLDER_CATALOGUER) - so the
             * request names only the asset.
             */
            String catalogActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                    FilesFvtTestSupport.governanceActionTypeQualifiedName(CATALOG_FILE_FOLDER),
                    new HashMap<>(),
                    List.of(FilesFvtTestSupport.newActionTarget(ActionTarget.NEW_ASSET.getName(), folderAssetGUID)));

            new EngineActionWaiter().waitForCompletion(catalogActionGUID,
                                                        FilesFvtTestSupport.governanceActionTypeQualifiedName(CATALOG_FILE_FOLDER));

            assertTrue(isCatalogTarget(openMetadataStore, folderAssetGUID),
                       "The folder was not attached to " + CATALOGUER.getConnectorName() + " as a catalog target, so the"
                               + " cataloguer will never see it.");

            /*
             * Now make the integration daemon run the connector, rather than waiting out its refresh interval.
             */
            OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnectors();

            /*
             * The files inside the folder should now be catalogued beneath it.  They are looked for by
             * waiting rather than by asking once: the refresh call returns as soon as the connector has been
             * told, not when it has finished.
             */
            List<String> catalogued = new ArrayList<>();

            FilesFvtTestSupport.waitFor("the folder cataloguer to catalogue the files in " + folder.getName(),
                                         "files.fvt.refresh.timeout.seconds",
                                         180,
                                         () ->
                                         {
                                             catalogued.clear();
                                             catalogued.addAll(getCataloguedFileNames(openMetadataStore, folder));

                                             return catalogued.size() >= FilesFvtTestSupport.FILES_IN_FOLDER;
                                         });

            assertFalse(catalogued.isEmpty(),
                        "The folder cataloguer ran but catalogued nothing inside " + folder.getAbsolutePath()
                                + ", which holds " + FilesFvtTestSupport.FILES_IN_FOLDER + " files.");

            assertTrue(catalogued.size() >= FilesFvtTestSupport.FILES_IN_FOLDER,
                       "The folder cataloguer catalogued " + catalogued.size() + " element(s) inside "
                               + folder.getName() + " - " + catalogued + " - but the folder holds "
                               + FilesFvtTestSupport.FILES_IN_FOLDER + " files.");
        }
        finally
        {
            if (folderAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, folderAssetGUID);
            }
        }
    }


    /**
     * Is this asset attached to the cataloguer as a catalog target?
     *
     * @param openMetadataStore store to read through
     * @param assetGUID asset to look at
     * @return true if a catalog target relationship reaches it
     * @throws Exception problem reading from the repository
     */
    private boolean isCatalogTarget(OpenMetadataStore openMetadataStore,
                                    String            assetGUID) throws Exception
    {
        RelatedMetadataElementList targets =
                openMetadataStore.getRelatedMetadataElements(assetGUID,
                                                              0,
                                                              OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                              0,
                                                              FilesFvtTestSupport.MAX_PAGE_SIZE);

        return (targets != null) && (targets.getElementList() != null) && (! targets.getElementList().isEmpty());
    }


    /**
     * Return the qualified names of the data files catalogued for the folder under test.
     * <br>
     * Found by searching for the files rather than by traversing a relationship from the folder asset.  The
     * cataloguer decides for itself how it links what it creates - and, more to the point, which folder asset
     * it links it to: it catalogues the directory it was given as a catalog target, and the assets it builds
     * carry the file system name the connector is configured with, not the one this suite used when it
     * catalogued the folder.  Searching by name asks the question the test actually cares about - are the
     * files in this folder in the catalogue - without asserting anything about how the connector chose to
     * arrange them.
     *
     * @param openMetadataStore store to read through
     * @param folder folder whose files should have been catalogued
     * @return qualified names of the data files found
     */
    private List<String> getCataloguedFileNames(OpenMetadataStore openMetadataStore,
                                                File              folder)
    {
        List<String> names = new ArrayList<>();

        try
        {
            SearchProperties searchProperties = new SearchProperties();

            searchProperties.setConditions(new PropertyHelper().addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   folder.getName(),
                                                                                   PropertyComparisonOperator.LIKE));

            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setMetadataElementTypeName(OpenMetadataType.DATA_FILE.typeName);
            queryOptions.setPageSize(FilesFvtTestSupport.MAX_PAGE_SIZE);

            List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

            if (found != null)
            {
                for (OpenMetadataElement element : found)
                {
                    names.add(FilesFvtTestSupport.getStringProperty(element, OpenMetadataProperty.QUALIFIED_NAME.name));
                }
            }
        }
        catch (Exception error)
        {
            /*
             * Read while the cataloguer is working, so a failure here is ordinary - the caller is polling and
             * will ask again.
             */
        }

        return names;
    }
}
