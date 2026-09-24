/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.postgresfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs the PostgreSQL content pack's "create and survey" governance action process against a real PostgreSQL
 * server, through the Automated Curation API, and checks what came back.
 * <br>
 * This is the suite's longest chain, and every link in it is a different component:
 * <ol>
 *     <li>the <b>view server</b> accepts the request and records an engine action;</li>
 *     <li>the <b>engine host</b> hears about it on the Open Governance out topic and claims it;</li>
 *     <li><b>step 1</b> runs the Core content pack's create-asset service, which builds a PostgreSQL server
 *     asset from the PostgreSQL catalog template, substituting the placeholder values supplied as request
 *     parameters;</li>
 *     <li><b>step 2</b> runs the <b>PostgreSQL server survey action service</b> - the connector this suite
 *     exists to test - which uses the asset's own connection to reach the database server, reads its
 *     catalogue, and writes what it found as annotations on a new survey report;</li>
 *     <li><b>step 3</b> writes that report out as a markdown document.</li>
 * </ol>
 * So a passing run says the connector works <em>and</em> that everything needed to reach it is wired up.  A
 * failing one is diagnosable because the process is followed step by step: the failure names the step, the
 * request type and the governance engine, and the completion message the service itself recorded.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PostgresServerSurveyFVT
{
    /**
     * The governance action process under test.  Also referenced by {@link AutomatedCurationFVT}, which checks
     * that a curator would be offered it in the first place.
     */
    static final String CREATE_AND_SURVEY_PROCESS = "PostgreSQLServer:CreateAndSurveyGovernanceActionProcess";

    /**
     * Where the third step is asked to write its markdown report.  Under {@code build} so that a run leaves
     * nothing behind in the source tree, and so that the file can be checked for.
     */
    private static final String REPORT_DIRECTORY = "build/postgres-fvt-data/survey-reports";

    /**
     * Where the report writer puts its reports when it is not told otherwise.  Relative to this module's
     * directory, and outside {@code build}, which is why the suite asks for somewhere else.  Only used to
     * make the failure message useful when the report turns up here rather than where it was asked for.
     */
    private static final String SURVEY_REPORT_DEFAULT_DIRECTORY = "surveys/survey-reports";


    /**
     * Create a PostgreSQL server asset, survey the server it points at, and check the survey produced a report
     * with annotations in it.
     * <br>
     * The survey is scoped to this suite's own database with {@code includeDatabaseList}.  That is not just to
     * keep the run short: a survey of a shared development server would produce a different report every time
     * it ran, and there would be nothing specific to assert about it.
     *
     * @throws Exception the process failed, or produced something other than what was asked for
     */
    @Test
    @DisplayName("The create-and-survey process catalogues a PostgreSQL server and surveys it")
    public void testCreateAndSurveyProcess() throws Exception
    {
        String serverName    = PostgresFvtTestSupport.serverUnderTestName("survey");
        String qualifiedName = PostgresFvtTestSupport.serverAssetQualifiedName(serverName);

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        String newAssetGUID     = null;
        String surveyReportGUID = null;

        try
        {
            Map<String, String> requestParameters = new HashMap<>(PostgresFvtTestSupport.serverTemplatePlaceholders(serverName));

            /*
             * Both of these are passed to every step of the process.  Step 1 ignores them, step 2 uses the
             * database list to scope the survey, and step 3 uses the report directory.  A process's request
             * parameters are shared by all of its steps, which is why a name that means nothing to one step
             * does no harm.
             */
            requestParameters.put(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                  PostgresFvtTestSupport.getDatabaseName());
            requestParameters.put("reportDirectory", REPORT_DIRECTORY);

            String processInstanceGUID = new AutomatedCurationClient().initiateGovernanceActionProcess(CREATE_AND_SURVEY_PROCESS,
                                                                                                         requestParameters,
                                                                                                         null);

            assertNotNull(processInstanceGUID,
                          "The Automated Curation service accepted the request to run " + CREATE_AND_SURVEY_PROCESS
                                  + " but returned no process instance to follow.");

            List<EngineActionElement> steps = new EngineActionWaiter().waitForProcess(processInstanceGUID,
                                                                                       CREATE_AND_SURVEY_PROCESS);

            assertTrue(steps.size() >= 2,
                       "The create-and-survey process ran " + steps.size() + " step(s).  It is defined with three - create the"
                               + " asset, survey it, then write the report - so a run that stops earlier means a step's completion"
                               + " guard did not trigger the next one.");

            /*
             * The asset.  Its GUID comes from the action target the create service recorded when it completed,
             * which is also how the process hands the asset to the survey step - so reading it back checks the
             * hand-off as well as the creation.  The whole process is searched rather than one step, because a
             * completion action target is attached to the step that follows the one that produced it.
             */
            newAssetGUID = EngineActionWaiter.getActionTargetGUID(steps, ActionTarget.NEW_ASSET.getName());

            assertNotNull(newAssetGUID,
                          "No step of " + CREATE_AND_SURVEY_PROCESS + " recorded a '" + ActionTarget.NEW_ASSET.getName()
                                  + "' action target, so the survey step had nothing to survey.");

            OpenMetadataElement newAsset = openMetadataStore.getMetadataElementByGUID(newAssetGUID);

            assertNotNull(newAsset, "The asset the process created cannot be read back from the repository.");

            assertEquals(qualifiedName,
                         PostgresFvtTestSupport.getStringProperty(newAsset, OpenMetadataProperty.QUALIFIED_NAME.name),
                         "The process created an asset with an unexpected qualified name.");

            List<String> survivingPlaceholders = PostgresFvtTestSupport.findPlaceholders("the catalogued PostgreSQL server",
                                                                                          newAsset.getElementProperties());

            assertTrue(survivingPlaceholders.isEmpty(),
                       "The asset the process created still carries unsubstituted placeholders: " + survivingPlaceholders);

            /*
             * Step 2 - the survey report.  The survey action service records it as an action target when it
             * completes, which is the only place its GUID is published.
             */
            surveyReportGUID = EngineActionWaiter.getActionTargetGUID(steps, SurveyActionTarget.SURVEY_REPORT.getName());

            assertNotNull(surveyReportGUID,
                          "No step of " + CREATE_AND_SURVEY_PROCESS + " recorded a '"
                                  + SurveyActionTarget.SURVEY_REPORT.getName() + "' action target, so the survey did not"
                                  + " produce a report.");

            OpenMetadataElement surveyReport = openMetadataStore.getMetadataElementByGUID(surveyReportGUID);

            assertNotNull(surveyReport, "The survey report cannot be read back from the repository.");

            assertTrue(surveyReport.getType().getTypeName().contains(OpenMetadataType.SURVEY_REPORT.typeName)
                               || OpenMetadataType.SURVEY_REPORT.typeName.equals(surveyReport.getType().getTypeName()),
                       "The element the survey recorded as its report is a " + surveyReport.getType().getTypeName()
                               + " rather than a " + OpenMetadataType.SURVEY_REPORT.typeName + ".");

            /*
             * The annotations are the survey's actual findings.  A report with none means the service ran,
             * connected, and found nothing to say - which for a server that this suite has just created a
             * database on would be a defect in the connector rather than an empty server.
             */
            List<String> annotationTypes = getAnnotationTypes(openMetadataStore, surveyReportGUID);

            assertFalse(annotationTypes.isEmpty(),
                        "The survey of " + serverName + " produced a report with no annotations on it.  The PostgreSQL server"
                                + " survey service records what it found as annotations, so an empty report means it reached the"
                                + " server and extracted nothing - check the audit log for what it saw.");

            /*
             * Summarised by type rather than listed.  A survey of a real server produces hundreds of
             * annotations, and printing them one by one put a single unreadable line of several thousand
             * words into the log - which is where this suite's failures have to be diagnosed from.
             */
            Map<String, Integer> annotationTypeCounts = new TreeMap<>();

            for (String annotationType : annotationTypes)
            {
                annotationTypeCounts.put(annotationType, annotationTypeCounts.getOrDefault(annotationType, 0) + 1);
            }

            System.out.println("postgres-fvt: survey of " + serverName + " produced " + annotationTypes.size()
                                       + " annotation(s) of " + annotationTypeCounts.size() + " type(s): " + annotationTypeCounts);

            /*
             * Step 3 - the markdown report.  Checking for the file is what shows the last step really ran,
             * rather than merely being reported as complete.
             */
            if (steps.size() >= 3)
            {
                /*
                 * Checked in the directory the process asked for, not just anywhere.  The reportDirectory
                 * request parameter is given to the process, and the report is written by its last step - so
                 * a report that turns up in the report writer's own default location instead means the
                 * parameter was lost somewhere between the two, which is worth failing for.
                 */
                File requestedDirectory = new File(REPORT_DIRECTORY);

                assertTrue(containsMarkdownReport(requestedDirectory),
                           "The final step of " + CREATE_AND_SURVEY_PROCESS + " completed but wrote no markdown report into "
                                   + requestedDirectory.getAbsolutePath()
                                   + (containsMarkdownReport(new File(SURVEY_REPORT_DEFAULT_DIRECTORY))
                                              ? " - it wrote one into " + new File(SURVEY_REPORT_DEFAULT_DIRECTORY).getAbsolutePath()
                                                        + " instead, so the reportDirectory request parameter did not reach the step"
                                                        + " that writes the report."
                                              : "."));
            }
        }
        finally
        {
            /*
             * The report is purged first: it is anchored to the asset, so deleting the asset first would take
             * it with it and leave this call deleting something that is already gone.  Both are best-effort.
             */
            if (surveyReportGUID != null)
            {
                PostgresFvtTestSupport.purgeElement(openMetadataStore, surveyReportGUID);
            }

            if (newAssetGUID != null)
            {
                PostgresFvtTestSupport.purgeElement(openMetadataStore, newAssetGUID);
            }
        }
    }


    /**
     * Survey an asset and then delete it, and check that the survey report and every annotation on it went too.
     * <br>
     * A survey leaves a subgraph behind it, and that subgraph is two anchors deep: the report is anchored to the
     * asset it describes, and each annotation is anchored to the report that reports it.  So deleting the asset
     * has to reach the annotations <em>through</em> the report - one level further than a cascade that only
     * looks at what is anchored directly to the element being deleted.
     * <br>
     * This is worth its own test because getting it wrong is invisible.  The asset goes, the report goes, the
     * delete reports success, and the annotations simply stay - orphaned, anchored to a report that no longer
     * exists, and unreachable by any search that goes through an anchor, so nothing tidies them up afterwards
     * either.  They accumulate silently until a later survey of the same asset trips over one of them: the
     * report printer walks a soft-deleted annotation, that step fails, and the failure surfaces as "the survey
     * produced no report" on a completely different test.  This suite had 17,000 such annotations in its
     * repository, spanning a month, before anyone noticed.
     * <br>
     * The delete is done the way an operator would do it - through the content pack's delete process - rather
     * than by calling the repository directly, so a passing run says the whole operator path cascades, not just
     * the handler underneath it.
     *
     * @throws Exception the process failed, or left part of the survey behind
     */
    @Test
    @DisplayName("Deleting a surveyed asset removes its survey report and the report's annotations")
    public void testDeletingSurveyedAssetRemovesItsAnnotations() throws Exception
    {
        String serverName    = PostgresFvtTestSupport.serverUnderTestName("survey-delete");
        String qualifiedName = PostgresFvtTestSupport.serverAssetQualifiedName(serverName);

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        String       newAssetGUID     = null;
        String       surveyReportGUID = null;
        List<String> annotationGUIDs  = new ArrayList<>();

        try
        {
            Map<String, String> requestParameters = new HashMap<>(PostgresFvtTestSupport.serverTemplatePlaceholders(serverName));

            requestParameters.put(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                  PostgresFvtTestSupport.getDatabaseName());
            requestParameters.put("reportDirectory", REPORT_DIRECTORY);

            String processInstanceGUID = new AutomatedCurationClient().initiateGovernanceActionProcess(CREATE_AND_SURVEY_PROCESS,
                                                                                                         requestParameters,
                                                                                                         null);

            assertNotNull(processInstanceGUID,
                          "The Automated Curation service accepted the request to run " + CREATE_AND_SURVEY_PROCESS
                                  + " but returned no process instance to follow.");

            List<EngineActionElement> steps = new EngineActionWaiter().waitForProcess(processInstanceGUID,
                                                                                       CREATE_AND_SURVEY_PROCESS);

            newAssetGUID     = EngineActionWaiter.getActionTargetGUID(steps, ActionTarget.NEW_ASSET.getName());
            surveyReportGUID = EngineActionWaiter.getActionTargetGUID(steps, SurveyActionTarget.SURVEY_REPORT.getName());

            assertNotNull(newAssetGUID, "The survey process created no asset, so there is nothing to delete.");
            assertNotNull(surveyReportGUID, "The survey process produced no report, so there is no subgraph to check.");

            annotationGUIDs = getAnnotationGUIDs(openMetadataStore, surveyReportGUID);

            assertFalse(annotationGUIDs.isEmpty(),
                        "The survey of " + serverName + " produced a report with no annotations, so this test would pass"
                                + " without checking anything.  The cascade it exists to verify needs a report that has"
                                + " something anchored to it.");

            System.out.println("postgres-fvt: surveyed " + serverName + " - report " + surveyReportGUID + " has "
                                       + annotationGUIDs.size() + " annotation(s); deleting the asset");

            /*
             * Delete the asset, the way an operator would.
             */
            String deleteInstanceGUID = new AutomatedCurationClient().initiateGovernanceActionProcess(PostgresServerCatalogFVT.DELETE_ASSET_PROCESS,
                                                                                                        requestParameters,
                                                                                                        null);

            assertNotNull(deleteInstanceGUID,
                          "The Automated Curation service accepted the request to run "
                                  + PostgresServerCatalogFVT.DELETE_ASSET_PROCESS + " but returned no process instance to follow.");

            new EngineActionWaiter().waitForProcess(deleteInstanceGUID, PostgresServerCatalogFVT.DELETE_ASSET_PROCESS);

            /*
             * The asset itself.
             */
            OpenMetadataElement deletedAsset = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name);

            assertNull(deletedAsset,
                       "The delete process completed but " + qualifiedName + " is still in the repository.");

            /*
             * One level down: the report, which is anchored to the asset.
             */
            assertTrue(isGone(openMetadataStore, surveyReportGUID),
                       "The asset was deleted but its survey report " + surveyReportGUID + " is still retrievable."
                               + "  The report is anchored to the asset, so a cascading delete should have taken it.");

            /*
             * Two levels down, and the point of the test: the annotations, which are anchored to the report
             * rather than to the asset.
             */
            List<String> survivors = new ArrayList<>();

            for (String annotationGUID : annotationGUIDs)
            {
                if (! isGone(openMetadataStore, annotationGUID))
                {
                    survivors.add(annotationGUID);
                }
            }

            assertTrue(survivors.isEmpty(),
                       survivors.size() + " of the " + annotationGUIDs.size() + " annotation(s) on survey report "
                               + surveyReportGUID + " survived the delete of the asset they belong to: " + survivors
                               + ".  Each annotation is anchored to the report and the report to the asset, so the cascade has"
                               + " to follow anchors through two levels to reach them.  Survivors mean it stopped at the"
                               + " report, and nothing will ever find them again - they are anchored to an element that no"
                               + " longer exists.");

            System.out.println("postgres-fvt: delete of " + serverName + " removed the asset, its report and all "
                                       + annotationGUIDs.size() + " annotation(s)");

            assertFalse(steps.isEmpty(), "The survey process ran no steps.");
        }
        finally
        {
            /*
             * Best-effort, and harmless when the delete process did its job.  The report goes before the asset
             * because it is anchored to it, and the annotations before the report for the same reason - so a
             * partial failure above still leaves nothing for the next run.
             */
            for (String annotationGUID : annotationGUIDs)
            {
                PostgresFvtTestSupport.purgeElement(openMetadataStore, annotationGUID);
            }

            if (surveyReportGUID != null)
            {
                PostgresFvtTestSupport.purgeElement(openMetadataStore, surveyReportGUID);
            }

            if (newAssetGUID != null)
            {
                PostgresFvtTestSupport.purgeElement(openMetadataStore, newAssetGUID);
            }
        }
    }


    /**
     * Is this element no longer retrievable?
     * <br>
     * "Gone" is judged the way a consumer would see it: an ordinary read of the element finds nothing.  A
     * soft-deleted element answers either with null or by refusing the read, and both mean the same thing here.
     *
     * @param openMetadataStore store to read from
     * @param elementGUID element to look for
     * @return true if the element cannot be read
     */
    private boolean isGone(OpenMetadataStore openMetadataStore,
                           String            elementGUID)
    {
        try
        {
            return openMetadataStore.getMetadataElementByGUID(elementGUID) == null;
        }
        catch (Exception notRetrievable)
        {
            return true;
        }
    }


    /**
     * Return the unique identifiers of every annotation recorded on one survey report.
     *
     * @param openMetadataStore store to read from
     * @param surveyReportGUID report to read
     * @return annotation guids, empty if the report has none
     * @throws Exception the repository could not be read
     */
    private List<String> getAnnotationGUIDs(OpenMetadataStore openMetadataStore,
                                            String            surveyReportGUID) throws Exception
    {
        List<String> annotationGUIDs = new ArrayList<>();

        for (RelatedMetadataElement annotation : getAnnotations(openMetadataStore, surveyReportGUID))
        {
            annotationGUIDs.add(annotation.getElement().getElementGUID());
        }

        return annotationGUIDs;
    }


    /**
     * Does this directory hold at least one markdown report?
     *
     * @param directory directory to look in
     * @return true if it exists and holds a .md file
     */
    private boolean containsMarkdownReport(File directory)
    {
        File[] reportFiles = directory.listFiles((parent, name) -> name.endsWith(".md"));

        return (reportFiles != null) && (reportFiles.length > 0);
    }


    /**
     * Return the annotation types recorded on one survey report, one entry per annotation.
     *
     * @param openMetadataStore store to read from
     * @param surveyReportGUID report to read
     * @return annotation types, empty if the report has no annotations
     * @throws Exception the repository could not be read
     */
    private List<String> getAnnotationTypes(OpenMetadataStore openMetadataStore,
                                            String            surveyReportGUID) throws Exception
    {
        List<String> annotationTypes = new ArrayList<>();

        for (RelatedMetadataElement annotation : getAnnotations(openMetadataStore, surveyReportGUID))
        {
            String annotationType = PostgresFvtTestSupport.getStringProperty(annotation.getElement(),
                                                                             OpenMetadataProperty.ANNOTATION_TYPE.name);

            annotationTypes.add((annotationType == null) ? annotation.getElement().getType().getTypeName() : annotationType);
        }

        return annotationTypes;
    }


    /**
     * Return <b>every</b> annotation recorded on one survey report, paging to the end of the list.
     * <br>
     * Taking the first page is not good enough for either caller.  A survey of a real database produces more
     * annotations than one page holds, so a single read reports the page size where a count belongs - and the
     * defect this suite exists to catch was a delete cascade that discovered only the first page of an
     * anchor's members and left the rest anchored to an element it had deleted.  An assertion over one page
     * would have passed against exactly that bug, because the annotations it happened to check were the ones
     * the cascade did remove.
     *
     * @param openMetadataStore store to read from
     * @param surveyReportGUID report to read
     * @return the report's annotations, empty if it has none
     * @throws Exception the repository could not be read
     */
    private List<RelatedMetadataElement> getAnnotations(OpenMetadataStore openMetadataStore,
                                                        String            surveyReportGUID) throws Exception
    {
        List<RelatedMetadataElement> annotations = new ArrayList<>();
        Set<String>                  seenGUIDs   = new HashSet<>();
        int                          startFrom   = 0;

        while (true)
        {
            RelatedMetadataElementList page =
                    openMetadataStore.getRelatedMetadataElements(surveyReportGUID,
                                                                 1,
                                                                 OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                 startFrom,
                                                                 PostgresFvtTestSupport.MAX_PAGE_SIZE);

            if ((page == null) || (page.getElementList() == null) || (page.getElementList().isEmpty()))
            {
                break;
            }

            int pageSize   = page.getElementList().size();
            int addedCount = 0;

            for (RelatedMetadataElement annotation : page.getElementList())
            {
                if ((annotation != null) && (annotation.getElement() != null))
                {
                    if (seenGUIDs.add(annotation.getElement().getElementGUID()))
                    {
                        annotations.add(annotation);
                        addedCount = addedCount + 1;
                    }
                }
            }

            if (addedCount == 0)
            {
                /*
                 * The page held nothing new, so the offset is not moving the result set on.  Reading further
                 * would loop for ever - which is how the cascade's own paging failed - so stop instead.
                 */
                break;
            }

            startFrom = startFrom + pageSize;
        }

        return annotations;
    }
}
