/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FolderSurveyFVT runs the Files content pack's folder survey against a directory this suite built, in the
 * two steps a curator would use: catalogue the directory, then survey what was catalogued.
 * <br>
 * Both steps are governance action types rather than one multi-step process, because that is how the Files
 * content pack is put together - it ships the actions, and leaves the sequencing to the caller.  That makes
 * the hand-off between them part of what is tested: the survey is asked to run against the asset the first
 * step created, and a survey that cannot open that asset fails here rather than somewhere a user would have
 * to go looking for it.
 * <br><br>
 * <b>Why this test exists.</b> A folder survey against a template-created asset was reported failing with a
 * {@code NullPointerException} inside {@code BasicFolderConnector} - the asset had no connection the survey
 * could open it through, {@code getConnectorForAsset()} returned null by contract, and the survey service
 * dereferenced it.  Nothing in that failure named the asset, which is what made it expensive to track down.
 * The null is now reported as {@code OPEN-SURVEY-400-008}, naming the asset; this test is what says whether
 * the survey gets a connector at all.  It asserts the survey <em>completed</em>, not merely that it ran: a
 * survey that fails on a null connector still produces an engine action, and one that only checked for the
 * action's existence would pass while the thing under test was broken.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class FolderSurveyFVT
{
    /**
     * The two governance action types this test runs, named from the content pack's own definitions rather
     * than as string literals - a rename in the pack is then a compile failure here rather than a request
     * the server rejects as unknown.
     */
    private static final RequestTypeDefinition CREATE_FILE_FOLDER = RequestTypeDefinition.CREATE_FILE_FOLDER;
    private static final RequestTypeDefinition SURVEY_FOLDER      = RequestTypeDefinition.SURVEY_FOLDER;


    @Test
    @DisplayName("A folder catalogued from its template can then be surveyed")
    public void testCatalogueThenSurveyFolder() throws Exception
    {
        File   folder        = FilesFvtTestSupport.folderUnderTest("survey");
        String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        String folderAssetGUID = null;

        try
        {
            assertTrue(folder.isDirectory(),
                       "The folder under test was not built: " + folder.getAbsolutePath()
                               + ".  Check files.fvt.data.directory is somewhere this JVM may write.");

            /*
             * Step 1 - catalogue the folder from the FileFolder template.  This is the step whose output the
             * reported defect blamed: the asset it creates is the one the survey then has to open.
             */
            Map<String, String> createParameters = new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder));

            String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                                                                                                  createParameters,
                                                                                                  null);

            assertNotNull(createActionGUID,
                          "The Automated Curation service accepted the request to run "
                                  + FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER) + " but returned no engine action to follow.");

            new EngineActionWaiter().waitForCompletion(createActionGUID,
                                                        FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            /*
             * The asset is found by the qualified name the template gives it, rather than by the action
             * target the create service records.  A standalone governance action type has no following step
             * for a completion action target to be attached to - that is a property of a multi-step process -
             * so the target is not reachable from the action here even though the service recorded one.
             * Looking it up by name is also closer to what a curator does next, and it checks that the
             * template built the name it is supposed to: this string is assembled from the template's own
             * shape, so an asset created under a different name fails here rather than being quietly used.
             */
            OpenMetadataElement folderAsset = FilesFvtTestSupport.waitForElement(openMetadataStore,
                                                                                 qualifiedName,
                                                                                 "the folder asset created by "
                                                                                         + FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            assertNotNull(folderAsset,
                          FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER)
                                  + " completed but no asset called " + qualifiedName + " arrived in the repository.");

            folderAssetGUID = folderAsset.getElementGUID();

            List<String> survivingPlaceholders = FilesFvtTestSupport.findPlaceholders("the catalogued folder",
                                                                                       folderAsset.getElementProperties());

            assertTrue(survivingPlaceholders.isEmpty(),
                       "The folder asset still carries unsubstituted placeholders: " + survivingPlaceholders);

            /*
             * The connection is checked before the survey is asked to run, because it is the difference
             * between the two failures this test can see.  Without a connection the survey cannot open the
             * folder however well it is written, and saying so here names the asset and the missing
             * relationship; leaving it to the survey step reports the same defect as a survey failure and
             * sends the reader to the survey service instead.
             */
            assertTrue(hasConnection(openMetadataStore, folderAssetGUID),
                       "The folder asset created from the " + FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER)
                               + " template has no " + OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName
                               + ", so nothing can open the folder it describes.  A survey against it can only fail.");

            /*
             * Step 2 - survey the asset that was just created.
             */
            Map<String, String> surveyParameters = new HashMap<>();

            /*
             * The asset is handed to the survey as an action target.  A survey action service takes whatever
             * asset it is given as its target, so the name here only has to be one; it is taken from
             * ActionTarget rather than written out so that it stays a name the framework recognises.
             */
            NewActionTarget actionTarget = FilesFvtTestSupport.newActionTarget(ActionTarget.ANY_ASSET.getName(), folderAssetGUID);

            String surveyActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(FilesFvtTestSupport.governanceActionTypeQualifiedName(SURVEY_FOLDER),
                                                                                                  surveyParameters,
                                                                                                  List.of(actionTarget));

            assertNotNull(surveyActionGUID,
                          "The Automated Curation service accepted the request to run "
                                  + FilesFvtTestSupport.governanceActionTypeQualifiedName(SURVEY_FOLDER) + " but returned no engine action to follow.");

            EngineActionElement surveyAction = new EngineActionWaiter().waitForCompletion(surveyActionGUID,
                                                                                           FilesFvtTestSupport.governanceActionTypeQualifiedName(SURVEY_FOLDER));

            /*
             * That the survey COMPLETED is already asserted - waitForCompletion() throws on any other
             * terminal status, quoting the completion message.  That single assertion is what this test was
             * written for: a survey that cannot open its asset ends FAILED with a "survey-failed" guard, and
             * would stop the test right there.
             *
             * The report is then found by following ReportOriginator back from the engine action that
             * produced it.  The survey service does record the report as a completion action target, but a
             * standalone governance action type has no following step for that target to be attached to -
             * the same reason the asset above is looked up by name rather than taken from a target.  Either
             * end is accepted because it is the link that matters here, not which way round it is stored.
             */
            String surveyReportGUID = FilesFvtTestSupport.getRelatedGUID(openMetadataStore,
                                                                          surveyActionGUID,
                                                      OpenMetadataType.REPORT_ORIGINATOR_RELATIONSHIP.typeName);

            assertNotNull(surveyReportGUID,
                          "The survey completed but no survey report is linked to the engine action that ran it."
                                  + "  Completion message: " + surveyAction.getCompletionMessage());

            OpenMetadataElement surveyReport = openMetadataStore.getMetadataElementByGUID(surveyReportGUID);

            assertNotNull(surveyReport, "The survey report cannot be read back from the repository.");

            /*
             * The annotations are the survey's actual findings.  A report with none means the service ran,
             * opened the folder, and found nothing to say about a directory this suite has just put files in -
             * which would be a defect in the connector rather than an empty folder.
             */
            List<String> annotationGUIDs = getAnnotationGUIDs(openMetadataStore, surveyReportGUID);

            assertFalse(annotationGUIDs.isEmpty(),
                        "The folder survey produced a report with no annotations on it, although the folder holds "
                                + FilesFvtTestSupport.FILES_IN_FOLDER + " files and a nested folder.");
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
     * Is a connection attached to this asset?
     *
     * @param openMetadataStore store to read through
     * @param assetGUID asset to look at
     * @return true if the asset names a connection
     * @throws Exception problem reading from the repository
     */
    private boolean hasConnection(OpenMetadataStore openMetadataStore,
                                  String            assetGUID) throws Exception
    {
        RelatedMetadataElementList connections =
                openMetadataStore.getRelatedMetadataElements(assetGUID,
                                                              1,
                                                              OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName,
                                                              0,
                                                              0);

        return (connections != null) && (connections.getElementList() != null) && (! connections.getElementList().isEmpty());
    }


    /**
     * Return the GUIDs of the annotations attached to a survey report.
     *
     * @param openMetadataStore store to read through
     * @param surveyReportGUID report to look at
     * @return annotation GUIDs, empty if the report carries none
     * @throws Exception problem reading from the repository
     */
    private List<String> getAnnotationGUIDs(OpenMetadataStore openMetadataStore,
                                            String            surveyReportGUID) throws Exception
    {
        List<String> annotationGUIDs = new ArrayList<>();

        RelatedMetadataElementList annotations =
                openMetadataStore.getRelatedMetadataElements(surveyReportGUID,
                                                              1,
                                                              OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                              0,
                                                              FilesFvtTestSupport.MAX_PAGE_SIZE);

        if ((annotations != null) && (annotations.getElementList() != null))
        {
            for (RelatedMetadataElement annotation : annotations.getElementList())
            {
                if ((annotation != null) && (annotation.getElement() != null))
                {
                    annotationGUIDs.add(annotation.getElement().getElementGUID());
                }
            }
        }

        return annotationGUIDs;
    }
}
