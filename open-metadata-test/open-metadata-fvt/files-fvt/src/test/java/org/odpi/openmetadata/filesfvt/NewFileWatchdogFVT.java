/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NewFileWatchdogFVT starts the new-file watchdog on a folder and checks it is running.
 * <br>
 * A watchdog is a different shape from every other action in this suite.  The others are asked for, do their
 * work and complete, so a test can wait for a terminal status and then assert on what was produced.  A
 * watchdog does not complete: it starts, registers its interest in a kind of change, and stays running until
 * something stops it.  Waiting for it to finish would time out on a watchdog that is working perfectly.
 * <br>
 * So what this asserts is that it started and stayed started.  That is a narrower claim than the other tests
 * make, and worth being plain about: it does not show that the watchdog reacts to a new file, only that it is
 * there to react.  It still catches the failure that matters most, and the one this suite has seen repeatedly
 * in the connectors around it - a governance service that throws on start-up, which for a watchdog means an
 * action that fails immediately instead of a watch that silently never fires.
 * <br>
 * Testing the reaction would mean cataloguing a new file inside the watched folder and waiting for the action
 * the watchdog initiates in response, through a second engine action this test does not request and cannot
 * name in advance.  That is a worthwhile test and a larger one; it is named in this suite's README as still
 * to add rather than approximated here.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class NewFileWatchdogFVT
{
    private static final RequestTypeDefinition CREATE_FILE_FOLDER  = RequestTypeDefinition.CREATE_FILE_FOLDER;
    private static final RequestTypeDefinition WATCH_FOR_NEW_FILES = RequestTypeDefinition.WATCH_FOR_NEW_FILES;

    /**
     * How long the watchdog is watched for before the test accepts that it is running.  Long enough that a
     * service which throws shortly after start-up is caught, short enough not to pad the suite.
     */
    private static final long SETTLE_MILLISECONDS = 5000;


    @Test
    @DisplayName("The new-file watchdog starts on a folder and keeps running")
    void testWatchdogStartsAndKeepsRunning() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        File   folder        = FilesFvtTestSupport.folderUnderTest("watchdog");
        String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

        String folderAssetGUID  = null;
        String watchdogGUID     = null;

        try
        {
            assertTrue(folder.isDirectory(), "The folder under test was not built: " + folder.getAbsolutePath());

            String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                    FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                    new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder)),
                    null);

            new EngineActionWaiter().waitForCompletion(createActionGUID,
                                                        FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            OpenMetadataElement folderAsset = FilesFvtTestSupport.waitForElement(openMetadataStore,
                                                                                 qualifiedName,
                                                                                 "the folder for the watchdog to watch");

            assertNotNull(folderAsset, "No folder asset called " + qualifiedName + " arrived in the repository.");

            folderAssetGUID = folderAsset.getElementGUID();

            String actionName = FilesFvtTestSupport.governanceActionTypeQualifiedName(WATCH_FOR_NEW_FILES);

            watchdogGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                    actionName,
                    new HashMap<>(),
                    List.of(FilesFvtTestSupport.newActionTarget(ActionTarget.NEW_ASSET.getName(), folderAssetGUID)));

            assertNotNull(watchdogGUID,
                          "The Automated Curation service accepted the request to run " + actionName
                                  + " but returned no engine action to follow.");

            /*
             * Give it time to fail if it is going to.  A watchdog that throws on start-up does so almost at
             * once; one that is working is still in progress after this.
             */
            Thread.sleep(SETTLE_MILLISECONDS);

            EngineActionElement watchdog = new EngineActionWaiter().getEngineAction(watchdogGUID);

            assertNotNull(watchdog, "The watchdog engine action " + watchdogGUID + " cannot be read back.");

            assertNotEquals(ActivityStatus.FAILED, watchdog.getActionStatus(),
                            actionName + " failed instead of watching.  It said: " + watchdog.getCompletionMessage());

            assertNotEquals(ActivityStatus.INVALID, watchdog.getActionStatus(),
                            actionName + " was rejected as invalid rather than started.  It said: "
                                    + watchdog.getCompletionMessage());
        }
        finally
        {
            if (folderAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, folderAssetGUID);
            }
        }
    }
}
