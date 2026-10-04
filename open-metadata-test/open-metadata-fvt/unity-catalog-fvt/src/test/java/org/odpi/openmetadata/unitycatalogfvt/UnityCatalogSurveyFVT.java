/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.unitycatalogfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTarget;
import org.odpi.openmetadata.contentpacks.core.GovernanceEngineDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Surveys the read-only Unity Catalog server three ways: the whole server, through the content pack's
 * create-and-survey process; one catalog; and one schema - the last two through their governance action
 * types, aimed at the server asset the process created.
 * <br>
 * The catalog survey is run twice, once as it comes and once with the sample schema excluded, so that the
 * survey's schema filter - and the route a request parameter takes to a survey service - is tested as well as
 * the survey itself.  The counts asserted are the ones the survey records in each annotation's resource
 * properties, which is what a reader of the survey report sees.
 * <br>
 * A survey only reads, so nothing is written to the read-only server.  The server asset created here is not
 * handed to a synchronizer, so it does not collide with the one {@link UnityCatalogServerCatalogFVT} uses.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnityCatalogSurveyFVT
{
    static final String CREATE_AND_SURVEY_PROCESS = "UnityCatalogServer:CreateAndSurveyGovernanceActionProcess";

    private static final String REPORT_DIRECTORY = "build/unity-catalog-fvt-data/survey-reports";

    private static final String SERVER_NAME = UnityCatalogFvtTestSupport.serverUnderTestName("survey");
    private static final String CATALOG     = UnityCatalogFvtTestSupport.getSampleCatalogName();
    private static final String SCHEMA      = UnityCatalogFvtTestSupport.getSampleSchemaName();

    /**
     * The server asset the process creates, surveyed again by the later tests.
     */
    private static String serverGUID = null;

    /**
     * Survey reports already seen on the server asset, so each test can pick out the one its survey wrote.
     */
    private static final Set<String> reportsSeen = new HashSet<>();

    /**
     * The annotation the most recent call to runSurvey read its metrics from, and the catalog metrics
     * annotation recorded by the first catalog survey.
     */
    private static String lastAnnotationGUID           = null;
    private static String firstCatalogMetricsAnnotation = null;


    /**
     * Remove the server asset, and the survey reports and annotations anchored beneath it.
     */
    @AfterAll
    static void removeServer() throws Exception
    {
        if (serverGUID != null)
        {
            UnityCatalogFvtTestSupport.purgeElement(ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore(), serverGUID);
        }
    }


    /**
     * Run the create-and-survey process against the read-only server and check each of its three steps did
     * its job: the asset was created, the server was surveyed, and the report was written out.
     *
     * @throws Exception the process failed or produced nothing
     */
    @Test
    @Order(1)
    @DisplayName("The create-and-survey process catalogues and surveys a Unity Catalog server")
    public void testCreateAndSurveyProcess() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        Map<String, String> requestParameters = new HashMap<>(UnityCatalogFvtTestSupport.serverTemplatePlaceholders(SERVER_NAME,
                                                                                                                  UnityCatalogFvtTestSupport.getReadOnlyHostURL(),
                                                                                                                  UnityCatalogFvtTestSupport.getReadOnlyPort()));
        requestParameters.put("reportDirectory", REPORT_DIRECTORY);

        String processInstanceGUID = new AutomatedCurationClient().initiateGovernanceActionProcess(CREATE_AND_SURVEY_PROCESS,
                                                                                                     requestParameters,
                                                                                                     null);

        assertNotNull(processInstanceGUID,
                      "The Automated Curation service accepted the request to run " + CREATE_AND_SURVEY_PROCESS
                              + " but returned no process instance to follow.");

        List<EngineActionElement> steps = new EngineActionWaiter().waitForProcess(processInstanceGUID, CREATE_AND_SURVEY_PROCESS);

        assertEquals(3,
                     steps.size(),
                     "The create-and-survey process is defined with three steps - create the asset, survey it, then write the"
                             + " report - but ran " + steps.size() + ".");

        serverGUID = EngineActionWaiter.getActionTargetGUID(steps, ActionTarget.NEW_ASSET.getName());

        assertNotNull(serverGUID, "No step of " + CREATE_AND_SURVEY_PROCESS + " recorded the asset it created.");

        String surveyReportGUID = EngineActionWaiter.getActionTargetGUID(steps, SurveyActionTarget.SURVEY_REPORT.getName());

        assertNotNull(surveyReportGUID, "No step of " + CREATE_AND_SURVEY_PROCESS + " recorded a survey report.");

        reportsSeen.add(surveyReportGUID);

        List<String> annotationTypes = new ArrayList<>();

        for (RelatedMetadataElement annotation : getAnnotations(openMetadataStore, surveyReportGUID))
        {
            annotationTypes.add(UnityCatalogFvtTestSupport.getStringProperty(annotation.getElement(), OpenMetadataProperty.ANNOTATION_TYPE.name));
        }

        assertTrue(annotationTypes.contains(UnityCatalogAnnotationType.SERVER_METRICS.getName()),
                   "The server survey recorded no " + UnityCatalogAnnotationType.SERVER_METRICS.getName() + " annotation.  It recorded: "
                           + annotationTypes);

        assertTrue(annotationTypes.contains(UnityCatalogAnnotationType.CATALOG_LIST.getName()),
                   "The server survey recorded no " + UnityCatalogAnnotationType.CATALOG_LIST.getName() + " annotation.  It recorded: "
                           + annotationTypes);

        File[] reports = new File(REPORT_DIRECTORY).listFiles((directory, name) -> name.endsWith(".md"));

        assertTrue((reports != null) && (reports.length > 0),
                   "The final step of " + CREATE_AND_SURVEY_PROCESS + " completed but wrote no markdown report into "
                           + new File(REPORT_DIRECTORY).getAbsolutePath() + ".");
    }


    /**
     * Survey the sample catalog and check the counts it records: one schema and the sample's four tables.
     *
     * @throws Exception the survey failed or recorded the wrong counts
     */
    @Test
    @Order(2)
    @DisplayName("The catalog survey counts the schemas and tables in a catalog")
    public void testCatalogSurvey() throws Exception
    {
        assertNotNull(serverGUID, "There is no server asset to survey - the create-and-survey test did not complete.");

        Map<String, String> metrics = runSurvey(RequestTypeDefinition.SURVEY_UC_CATALOG,
                                                Map.of(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), CATALOG),
                                                UnityCatalogAnnotationType.CATALOG_METRICS);

        assertEquals("1", metrics.get("schemaCount"), "The catalog survey miscounted the schemas in " + CATALOG + ": " + metrics);
        assertEquals("4", metrics.get("tableCount"), "The catalog survey miscounted the tables in " + CATALOG + ": " + metrics);

        firstCatalogMetricsAnnotation = lastAnnotationGUID;
    }


    /**
     * Survey the sample catalog again with its schema excluded, and check that the schema - and the tables
     * inside it - are no longer counted.  The exclusion is passed as a request parameter, which reaches the
     * survey service through its survey context rather than its connection.
     *
     * @throws Exception the survey failed or ignored the exclusion
     */
    @Test
    @Order(3)
    @DisplayName("The catalog survey leaves out a schema named in excludeSchemaNames")
    public void testCatalogSurveyWithExcludedSchema() throws Exception
    {
        assertNotNull(serverGUID, "There is no server asset to survey - the create-and-survey test did not complete.");

        Map<String, String> metrics = runSurvey(RequestTypeDefinition.SURVEY_UC_CATALOG,
                                                Map.of(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), CATALOG,
                                                       UnityCatalogConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(), SCHEMA),
                                                UnityCatalogAnnotationType.CATALOG_METRICS);

        assertEquals("0", metrics.get("schemaCount"), "Schema " + SCHEMA + " was surveyed although excludeSchemaNames names it: " + metrics);
        assertEquals("0", metrics.get("tableCount"), "The tables in excluded schema " + SCHEMA + " were counted: " + metrics);
    }


    /**
     * Survey the sample schema and check that it records the schema's metrics.
     *
     * @throws Exception the survey failed
     */
    @Test
    @Order(4)
    @DisplayName("The schema survey records metrics for a schema")
    public void testSchemaSurvey() throws Exception
    {
        assertNotNull(serverGUID, "There is no server asset to survey - the create-and-survey test did not complete.");

        Map<String, String> metrics = runSurvey(RequestTypeDefinition.SURVEY_UC_SCHEMA,
                                                Map.of(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), CATALOG,
                                                       UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), SCHEMA),
                                                UnityCatalogAnnotationType.SCHEMA_METRICS);

        assertEquals("4", metrics.get("tableCount"), "The schema survey miscounted the tables in " + CATALOG + "." + SCHEMA + ": " + metrics);
    }


    /**
     * Survey the sample catalog again, exactly as the first catalog survey did.  Nothing in the catalog has
     * changed, so the survey finds the same thing and must reuse the annotation the first survey recorded -
     * linking it to the new survey report as well - rather than create a duplicate.
     *
     * @throws Exception the survey failed or created a new annotation
     */
    @Test
    @Order(5)
    @DisplayName("Repeating an unchanged survey reuses the annotations from the earlier survey")
    public void testRepeatedSurveyReusesAnnotations() throws Exception
    {
        assertNotNull(firstCatalogMetricsAnnotation, "The first catalog survey did not complete, so there is nothing to compare with.");

        runSurvey(RequestTypeDefinition.SURVEY_UC_CATALOG,
                  Map.of(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), CATALOG),
                  UnityCatalogAnnotationType.CATALOG_METRICS);

        assertEquals(firstCatalogMetricsAnnotation,
                     lastAnnotationGUID,
                     "The repeated survey created a new " + UnityCatalogAnnotationType.CATALOG_METRICS.getName()
                             + " annotation instead of reusing the one the first survey recorded.");

        RelatedMetadataElementList reports = ConnectorContextFactory.newContext().getOpenMetadataStore()
                                                                    .getRelatedMetadataElements(lastAnnotationGUID,
                                                                                                2,
                                                                                                OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                0,
                                                                                                UnityCatalogFvtTestSupport.MAX_PAGE_SIZE);

        assertTrue((reports != null) && (reports.getElementList() != null) && (reports.getElementList().size() >= 2),
                   "The reused annotation should be reported by both catalog surveys' reports; it is reported by "
                           + (((reports == null) || (reports.getElementList() == null)) ? 0 : reports.getElementList().size()) + ".");
    }


    /**
     * Migrate an annotation left over from before annotations were reused.  Such an annotation is a copy of
     * one a later survey would record, anchored to - and reported only by - the survey report that created it.
     * One is built here from the catalog metrics annotation the earlier surveys reused, with a survey report of
     * its own, and the catalog survey is run again.  The survey must keep the original (the oldest), link the
     * leftover's report to it, and delete the leftover.
     *
     * @throws Exception the migration did not happen
     */
    @Test
    @Order(6)
    @DisplayName("A duplicate annotation from before annotations were reused is merged into the oldest match")
    public void testLeftoverAnnotationIsMigrated() throws Exception
    {
        assertNotNull(firstCatalogMetricsAnnotation, "The first catalog survey did not complete, so there is nothing to migrate.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        PropertyHelper    propertyHelper    = new PropertyHelper();

        /*
         * An old-style survey report for the server, and a copy of the reused annotation anchored to it.
         */
        ElementProperties reportProperties = propertyHelper.addStringProperty(null,
                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                              "SurveyReport::" + SERVER_NAME + "::leftover::" + System.currentTimeMillis());

        String leftoverReportGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.SURVEY_REPORT.typeName,
                                                                                   null,
                                                                                   serverGUID,
                                                                                   false,
                                                                                   null,
                                                                                   new NewElementProperties(reportProperties),
                                                                                   serverGUID,
                                                                                   OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName,
                                                                                   null,
                                                                                   true);

        OpenMetadataElement original = openMetadataStore.getMetadataElementByGUID(firstCatalogMetricsAnnotation);

        ElementProperties leftoverProperties = propertyHelper.addStringProperty(original.getElementProperties(),
                                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                leftoverReportGUID + "::leftover-annotation");

        String leftoverAnnotationGUID = openMetadataStore.createMetadataElementInStore(original.getType().getTypeName(),
                                                                                       null,
                                                                                       leftoverReportGUID,
                                                                                       false,
                                                                                       null,
                                                                                       new NewElementProperties(leftoverProperties),
                                                                                       leftoverReportGUID,
                                                                                       OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                       null,
                                                                                       true);

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                       serverGUID,
                                                       leftoverAnnotationGUID,
                                                       null,
                                                       null,
                                                       null);

        /*
         * Survey again.  The original is the oldest match, so it is the one kept.
         */
        runSurvey(RequestTypeDefinition.SURVEY_UC_CATALOG,
                  Map.of(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), CATALOG),
                  UnityCatalogAnnotationType.CATALOG_METRICS);

        assertEquals(firstCatalogMetricsAnnotation, lastAnnotationGUID, "The survey did not keep the oldest matching annotation.");

        assertTrue(isGone(openMetadataStore, leftoverAnnotationGUID),
                   "The leftover annotation " + leftoverAnnotationGUID + " was not deleted.");

        boolean leftoverReportLinked = false;

        RelatedMetadataElementList reports = openMetadataStore.getRelatedMetadataElements(firstCatalogMetricsAnnotation,
                                                                                          2,
                                                                                          OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                          0,
                                                                                          UnityCatalogFvtTestSupport.MAX_PAGE_SIZE);

        if ((reports != null) && (reports.getElementList() != null))
        {
            for (RelatedMetadataElement report : reports.getElementList())
            {
                if (leftoverReportGUID.equals(report.getElement().getElementGUID()))
                {
                    leftoverReportLinked = true;
                }
            }
        }

        assertTrue(leftoverReportLinked, "The leftover annotation's survey report was not linked to the annotation that replaced it.");
    }


    /**
     * Has an element gone?  A deleted element cannot be read back - the read reports it as not found or as
     * soft-deleted rather than returning nothing.
     *
     * @param openMetadataStore store to read from
     * @param elementGUID element to look for
     * @return true if it can no longer be read
     * @throws Exception the repository could not be read
     */
    private boolean isGone(OpenMetadataStore openMetadataStore,
                           String            elementGUID) throws Exception
    {
        try
        {
            return openMetadataStore.getMetadataElementByGUID(elementGUID) == null;
        }
        catch (InvalidParameterException notFound)
        {
            return true;
        }
    }


    /**
     * Run one survey governance action type against the server asset and return the resource properties of
     * the named annotation in the report it writes.
     *
     * @param requestType the survey's request type
     * @param requestParameters request parameters for the survey
     * @param annotationType the annotation to read
     * @return resource properties of that annotation
     * @throws Exception the survey failed, wrote no report, or recorded no such annotation
     */
    private Map<String, String> runSurvey(RequestTypeDefinition     requestType,
                                          Map<String, String>       requestParameters,
                                          UnityCatalogAnnotationType annotationType) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        String governanceActionType = GovernanceEngineDefinition.UNITY_CATALOG_SURVEY_ENGINE.getName() + "::" + requestType.getGovernanceRequestType();

        NewActionTarget actionTarget = new NewActionTarget();

        actionTarget.setActionTargetName(UnityCatalogTarget.UNITY_CATALOG_SERVER_TARGET.getName());
        actionTarget.setActionTargetGUID(serverGUID);

        String engineActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(governanceActionType,
                                                                                             new HashMap<>(requestParameters),
                                                                                             List.of(actionTarget));

        assertNotNull(engineActionGUID, "The Automated Curation service accepted " + governanceActionType + " but returned no engine action.");

        new EngineActionWaiter().waitForCompletion(engineActionGUID, governanceActionType);

        String surveyReportGUID = findNewSurveyReport(openMetadataStore);

        assertNotNull(surveyReportGUID, governanceActionType + " completed but no new survey report is attached to the server asset.");

        List<String> annotationTypes = new ArrayList<>();

        for (RelatedMetadataElement annotation : getAnnotations(openMetadataStore, surveyReportGUID))
        {
            String type = UnityCatalogFvtTestSupport.getStringProperty(annotation.getElement(), OpenMetadataProperty.ANNOTATION_TYPE.name);

            annotationTypes.add(type);

            if (annotationType.getName().equals(type))
            {
                lastAnnotationGUID = annotation.getElement().getElementGUID();

                return getResourceProperties(annotation);
            }
        }

        assertFalse(true, governanceActionType + " recorded no " + annotationType.getName() + " annotation.  It recorded: " + annotationTypes);

        return null;
    }


    /**
     * Find the survey report on the server asset that no earlier test has seen.
     *
     * @param openMetadataStore store to read from
     * @return GUID of the new report, or null
     * @throws Exception the repository could not be read
     */
    private String findNewSurveyReport(OpenMetadataStore openMetadataStore) throws Exception
    {
        RelatedMetadataElementList reports = openMetadataStore.getRelatedMetadataElements(serverGUID,
                                                                                          0,
                                                                                          OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName,
                                                                                          0,
                                                                                          UnityCatalogFvtTestSupport.MAX_PAGE_SIZE);

        if ((reports != null) && (reports.getElementList() != null))
        {
            for (RelatedMetadataElement report : reports.getElementList())
            {
                String reportGUID = report.getElement().getElementGUID();

                if (reportsSeen.add(reportGUID))
                {
                    return reportGUID;
                }
            }
        }

        return null;
    }


    /**
     * Return the resource properties of an annotation - the measurements a reader of the report sees.
     *
     * @param annotation annotation
     * @return resource properties (empty if there are none)
     */
    private Map<String, String> getResourceProperties(RelatedMetadataElement annotation)
    {
        Map<String, String> resourceProperties = new HashMap<>();

        String asString = UnityCatalogFvtTestSupport.getStringProperty(annotation.getElement(), OpenMetadataProperty.RESOURCE_PROPERTIES.name);

        if ((asString != null) && (asString.startsWith("{")) && (asString.endsWith("}")))
        {
            for (String entry : asString.substring(1, asString.length() - 1).split(", "))
            {
                int separator = entry.indexOf('=');

                if (separator > 0)
                {
                    resourceProperties.put(entry.substring(0, separator), entry.substring(separator + 1));
                }
            }
        }

        return resourceProperties;
    }


    /**
     * Return every annotation recorded on one survey report, paging to the end of the list.
     *
     * @param openMetadataStore store to read from
     * @param surveyReportGUID report to read
     * @return the report's annotations
     * @throws Exception the repository could not be read
     */
    private List<RelatedMetadataElement> getAnnotations(OpenMetadataStore openMetadataStore,
                                                        String            surveyReportGUID) throws Exception
    {
        List<RelatedMetadataElement> annotations = new ArrayList<>();
        int                          startFrom   = 0;

        while (true)
        {
            RelatedMetadataElementList page = openMetadataStore.getRelatedMetadataElements(surveyReportGUID,
                                                                                           1,
                                                                                           OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                           startFrom,
                                                                                           UnityCatalogFvtTestSupport.MAX_PAGE_SIZE);

            if ((page == null) || (page.getElementList() == null) || (page.getElementList().isEmpty()))
            {
                return annotations;
            }

            annotations.addAll(page.getElementList());

            if (page.getElementList().size() < UnityCatalogFvtTestSupport.MAX_PAGE_SIZE)
            {
                return annotations;
            }

            startFrom = startFrom + UnityCatalogFvtTestSupport.MAX_PAGE_SIZE;
        }
    }
}
