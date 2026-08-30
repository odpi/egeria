/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DataFolderActionsFVT takes a data folder through the three governance actions the Files content pack ships
 * for one: create it, hand it to a cataloguer, and delete it again.
 * <br>
 * A {@code DataFolder} is not a {@code FileFolder} with a different name.  A file folder is a directory whose
 * files are catalogued individually; a data folder is a directory that <em>is</em> the data - the files inside
 * it are how one data set happens to be stored, and are not catalogued separately.  The content pack ships a
 * separate template and a separate set of actions for that reason, and this test is what says the second set
 * works, rather than assuming it does because the file folder set does.
 * <br>
 * The delete case is the only place in this suite where an asset is removed by the content pack's own action
 * rather than by the suite's clean-up.  That is worth having: {@code delete-data-folder} finds the asset from
 * the template and the same placeholder values it was created with, not from a GUID handed to it, so it is
 * the one action here that has to derive what it operates on.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataFolderActionsFVT
{
    private static final RequestTypeDefinition CREATE_DATA_FOLDER  = RequestTypeDefinition.CREATE_DATA_FOLDER;
    private static final RequestTypeDefinition CATALOG_DATA_FOLDER = RequestTypeDefinition.CATALOG_DATA_FOLDER;
    private static final RequestTypeDefinition DELETE_DATA_FOLDER  = RequestTypeDefinition.DELETE_DATA_FOLDER;


    @Test
    @Order(1)
    @DisplayName("create-data-folder catalogues the directory as a DataFolder")
    void testCreateDataFolder() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        File   folder        = FilesFvtTestSupport.folderUnderTest("datafolder");
        String qualifiedName = FilesFvtTestSupport.dataFolderAssetQualifiedName(folder);

        assertTrue(folder.isDirectory(), "The folder under test was not built: " + folder.getAbsolutePath());

        runFolderAction(CREATE_DATA_FOLDER, folder);

        OpenMetadataElement folderAsset = FilesFvtTestSupport.waitForElement(openMetadataStore,
                                                                             qualifiedName,
                                                                             "the data folder asset");

        assertNotNull(folderAsset,
                      FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_DATA_FOLDER)
                              + " completed but no asset called " + qualifiedName + " arrived in the repository.");

        assertTrue(folderAsset.getType().getTypeName().contains(OpenMetadataType.DATA_FOLDER.typeName),
                   "The asset was catalogued as a " + folderAsset.getType().getTypeName() + " rather than a "
                           + OpenMetadataType.DATA_FOLDER.typeName + " - create-data-folder used the wrong template.");

        List<String> survivingPlaceholders = FilesFvtTestSupport.findPlaceholders("the catalogued data folder",
                                                                                   folderAsset.getElementProperties());

        assertTrue(survivingPlaceholders.isEmpty(),
                   "The data folder asset still carries unsubstituted placeholders: " + survivingPlaceholders);
    }


    @Test
    @Order(2)
    @DisplayName("catalog-data-folder attaches the folder to its cataloguer")
    void testCatalogDataFolder() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        File   folder        = FilesFvtTestSupport.folderUnderTest("datafolder");
        String qualifiedName = FilesFvtTestSupport.dataFolderAssetQualifiedName(folder);

        OpenMetadataElement folderAsset = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                           "qualifiedName");

        assertNotNull(folderAsset,
                      "The data folder asset is not there - testCreateDataFolder should have created it.");

        String actionName = FilesFvtTestSupport.governanceActionTypeQualifiedName(CATALOG_DATA_FOLDER);

        String engineActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                actionName,
                new HashMap<>(),
                List.of(FilesFvtTestSupport.newActionTarget(ActionTarget.NEW_ASSET.getName(),
                                                             folderAsset.getElementGUID())));

        assertNotNull(engineActionGUID,
                      "The Automated Curation service accepted the request to run " + actionName
                              + " but returned no engine action to follow.");

        new EngineActionWaiter().waitForCompletion(engineActionGUID, actionName);

        RelatedMetadataElementList targets =
                openMetadataStore.getRelatedMetadataElements(folderAsset.getElementGUID(),
                                                              0,
                                                              OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                              0,
                                                              FilesFvtTestSupport.MAX_PAGE_SIZE);

        assertTrue((targets != null) && (targets.getElementList() != null) && (! targets.getElementList().isEmpty()),
                   actionName + " completed but the data folder was not attached to a cataloguer as a catalog target,"
                           + " so nothing will ever look inside it.");
    }


    @Test
    @Order(3)
    @DisplayName("delete-data-folder removes the asset it created")
    void testDeleteDataFolder() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        File   folder        = FilesFvtTestSupport.folderUnderTest("datafolder");
        String qualifiedName = FilesFvtTestSupport.dataFolderAssetQualifiedName(folder);

        assertNotNull(openMetadataStore.getMetadataElementByUniqueName(qualifiedName, "qualifiedName"),
                      "The data folder asset is not there before the delete - the earlier cases should have left it.");

        runFolderAction(DELETE_DATA_FOLDER, folder);

        /*
         * The asset should be gone.  Asked for by name rather than by GUID because that is how the delete
         * service found it too: it is given the template and the placeholder values, and derives the
         * qualified name from them.
         */
        FilesFvtTestSupport.waitFor("the data folder asset to be removed by "
                                            + FilesFvtTestSupport.governanceActionTypeQualifiedName(DELETE_DATA_FOLDER),
                                     "files.fvt.refresh.timeout.seconds",
                                     180,
                                     () -> openMetadataStore.getMetadataElementByUniqueName(qualifiedName, "qualifiedName") == null);

        assertNull(openMetadataStore.getMetadataElementByUniqueName(qualifiedName, "qualifiedName"),
                   FilesFvtTestSupport.governanceActionTypeQualifiedName(DELETE_DATA_FOLDER)
                           + " completed but the asset " + qualifiedName + " is still in the repository.");

        assertTrue(folder.isDirectory(),
                   "delete-data-folder removed the directory " + folder.getAbsolutePath() + " from disk.  It removes"
                           + " the catalogue entry, not the data.");
    }


    /**
     * Run one of the folder actions, which take their subject as placeholder values rather than as an action
     * target - the same values for create and for delete, because the delete service derives the qualified
     * name of what it is removing from them.
     *
     * @param requestType action to run
     * @param folder folder it should act on
     * @throws Exception any failure - which is the finding
     */
    private void runFolderAction(RequestTypeDefinition requestType,
                                 File                  folder) throws Exception
    {
        String actionName = FilesFvtTestSupport.governanceActionTypeQualifiedName(requestType);

        Map<String, String> requestParameters = new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder));

        String engineActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(actionName,
                                                                                              requestParameters,
                                                                                              null);

        assertNotNull(engineActionGUID,
                      "The Automated Curation service accepted the request to run " + actionName
                              + " but returned no engine action to follow.");

        new EngineActionWaiter().waitForCompletion(engineActionGUID, actionName);
    }
}
