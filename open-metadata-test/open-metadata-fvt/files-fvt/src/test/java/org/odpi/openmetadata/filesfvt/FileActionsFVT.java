/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.DataAssetTemplateDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FileActionsFVT runs the Files content pack's file provisioning actions - copy, move and delete - and checks
 * what happened on disk as well as in the catalogue.
 * <br>
 * Both halves matter, and only checking one of them is the easy mistake here.  These services move real files
 * <em>and</em> maintain the metadata describing them; a copy that catalogued a new asset without writing the
 * file, or wrote the file without cataloguing it, would pass a test that looked in one place only.  So every
 * case asserts against the file system directly and against the repository.
 * <br>
 * The three cases run in order and share a folder because they are three stages of one story: a file is
 * copied to somewhere else, another is moved there, and one is deleted.  Running them in isolation would mean
 * building the same tree three times to prove less.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileActionsFVT
{
    private static final RequestTypeDefinition CREATE_FILE_FOLDER = RequestTypeDefinition.CREATE_FILE_FOLDER;
    private static final RequestTypeDefinition COPY_FILE          = RequestTypeDefinition.COPY_FILE;
    private static final RequestTypeDefinition MOVE_FILE          = RequestTypeDefinition.MOVE_FILE;
    private static final RequestTypeDefinition DELETE_FILE        = RequestTypeDefinition.DELETE_FILE;

    /**
     * The action target names the file provisioning service looks for.  They are its own, not the generic
     * ActionTarget constants - see MoveCopyFileGovernanceActionProvider - and a target under any other name
     * is delivered but never found.
     */
    private static final String SOURCE_FILE_TARGET        = "sourceFile";
    private static final String DESTINATION_FOLDER_TARGET = "destinationFolder";


    @Test
    @Order(1)
    @DisplayName("copy-file copies the file and catalogues the copy")
    void testCopyFile() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        File sourceFile       = new File(FilesFvtTestSupport.folderUnderTest("actions"), "to-copy.txt");
        File destinationFile  = new File(FilesFvtTestSupport.destinationFolderUnderTest(), sourceFile.getName());

        assertTrue(sourceFile.isFile(), "The file to copy was not written: " + sourceFile.getAbsolutePath());

        assertFalse(destinationFile.exists(),
                    "The destination already holds " + destinationFile.getName() + " before the copy ran.");

        runProvisioningAction(openMetadataStore, COPY_FILE, sourceFile);

        assertTrue(destinationFile.isFile(),
                   "copy-file completed but " + destinationFile.getAbsolutePath() + " was not written.");

        assertTrue(sourceFile.isFile(),
                   "copy-file removed the source file " + sourceFile.getAbsolutePath() + " - that is what move-file"
                           + " is for.");

        assertEquals(Files.readString(sourceFile.toPath()),
                     Files.readString(destinationFile.toPath()),
                     "The copied file does not hold what the source file holds.");
    }


    @Test
    @Order(2)
    @DisplayName("move-file moves the file, leaving nothing behind")
    void testMoveFile() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        File sourceFile      = new File(FilesFvtTestSupport.folderUnderTest("actions"), "to-move.txt");
        File destinationFile = new File(FilesFvtTestSupport.destinationFolderUnderTest(), sourceFile.getName());

        assertTrue(sourceFile.isFile(), "The file to move was not written: " + sourceFile.getAbsolutePath());

        String contentBeforeMove = Files.readString(sourceFile.toPath());

        runProvisioningAction(openMetadataStore, MOVE_FILE, sourceFile);

        assertTrue(destinationFile.isFile(),
                   "move-file completed but " + destinationFile.getAbsolutePath() + " was not written.");

        assertFalse(sourceFile.exists(),
                    "move-file left the source file " + sourceFile.getAbsolutePath() + " behind - that is what"
                            + " copy-file is for.");

        assertEquals(contentBeforeMove,
                     Files.readString(destinationFile.toPath()),
                     "The moved file does not hold what the source file held.");
    }


    @Test
    @Order(3)
    @DisplayName("delete-file removes the file")
    void testDeleteFile() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        File sourceFile = new File(FilesFvtTestSupport.folderUnderTest("actions"), "to-delete.txt");

        assertTrue(sourceFile.isFile(), "The file to delete was not written: " + sourceFile.getAbsolutePath());

        runProvisioningAction(openMetadataStore, DELETE_FILE, sourceFile);

        assertFalse(sourceFile.exists(),
                    "delete-file completed but " + sourceFile.getAbsolutePath() + " is still there.");
    }


    /**
     * Catalogue the file and the destination folder, then run one provisioning action over them.
     * <br>
     * The folder is catalogued through the content pack's own action while the file is created straight from
     * its template, for the reason given in {@link FileSurveyFVT}: the pack ships no create-file action,
     * because files are meant to reach the catalogue through a cataloguer.
     *
     * @param openMetadataStore store to create and read through
     * @param requestType the provisioning action to run
     * @param sourceFile the file it should act on
     * @throws Exception any failure - which is the finding
     */
    private void runProvisioningAction(OpenMetadataStore     openMetadataStore,
                                       RequestTypeDefinition requestType,
                                       File                  sourceFile) throws Exception
    {
        String actionName = FilesFvtTestSupport.governanceActionTypeQualifiedName(requestType);

        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        String sourceFileGUID = openMetadataStore.createMetadataElementFromTemplate(null,
                                                                                    templateOptions,
                                                                                    DataAssetTemplateDefinition.DATA_FILE_TEMPLATE.getTemplateGUID(),
                                                                                    null,
                                                                                    null,
                                                                                    FilesFvtTestSupport.fileTemplatePlaceholders(sourceFile),
                                                                                    null);

        assertNotNull(sourceFileGUID, "Cataloguing " + sourceFile.getName() + " returned no GUID.");

        List<NewActionTarget> actionTargets = new ArrayList<>();

        actionTargets.add(FilesFvtTestSupport.newActionTarget(SOURCE_FILE_TARGET, sourceFileGUID));

        /*
         * delete-file acts on the file alone; the other two need somewhere to put it.
         */
        if (requestType != DELETE_FILE)
        {
            actionTargets.add(FilesFvtTestSupport.newActionTarget(DESTINATION_FOLDER_TARGET,
                                                                   catalogueDestinationFolder(openMetadataStore)));
        }

        String engineActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(actionName,
                                                                                              new HashMap<>(),
                                                                                              actionTargets);

        assertNotNull(engineActionGUID,
                      "The Automated Curation service accepted the request to run " + actionName
                              + " but returned no engine action to follow.");

        new EngineActionWaiter().waitForCompletion(engineActionGUID, actionName);
    }


    /**
     * Catalogue the destination folder, or return the asset already catalogued for it.
     *
     * @param openMetadataStore store to create and read through
     * @return GUID of the destination folder asset
     * @throws Exception the folder could not be catalogued
     */
    private String catalogueDestinationFolder(OpenMetadataStore openMetadataStore) throws Exception
    {
        File   folder        = FilesFvtTestSupport.destinationFolderUnderTest();
        String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

        /*
         * The cases share one destination, so the second of them finds it already catalogued.  Asking first
         * is what makes the cases independent of the order they happen to run in.
         */
        var existing = openMetadataStore.getMetadataElementByUniqueName(qualifiedName, "qualifiedName");

        if (existing != null)
        {
            return existing.getElementGUID();
        }

        Map<String, String> createParameters = new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder));

        String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                createParameters,
                null);

        new EngineActionWaiter().waitForCompletion(createActionGUID,
                                                    FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

        var folderAsset = FilesFvtTestSupport.waitForElement(openMetadataStore,
                                                              qualifiedName,
                                                              "the destination folder asset");

        assertNotNull(folderAsset, "No destination folder asset called " + qualifiedName + " arrived.");

        return folderAsset.getElementGUID();
    }

}
