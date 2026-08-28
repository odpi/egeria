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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

            System.out.println("postgres-fvt: survey of " + serverName + " produced " + annotationTypes.size()
                                       + " annotation(s): " + annotationTypes);

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
     * Return the annotation types recorded on one survey report.
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

        RelatedMetadataElementList annotations =
                openMetadataStore.getRelatedMetadataElements(surveyReportGUID,
                                                             1,
                                                             OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                             0,
                                                             PostgresFvtTestSupport.MAX_PAGE_SIZE);

        if ((annotations != null) && (annotations.getElementList() != null))
        {
            for (RelatedMetadataElement annotation : annotations.getElementList())
            {
                String annotationType = PostgresFvtTestSupport.getStringProperty(annotation.getElement(),
                                                                                 OpenMetadataProperty.ANNOTATION_TYPE.name);

                annotationTypes.add((annotationType == null) ? annotation.getElement().getType().getTypeName() : annotationType);
            }
        }

        return annotationTypes;
    }
}
