/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.SurveyReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationExtensionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationMatchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AssociatedAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ReportedAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.RequestForActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.RequestForActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileDataProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileLogAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DataDiscoveryFVT covers every request of the Data Discovery OMVS: the service that works with the annotations
 * that surveys attach to survey reports.
 * <br>
 * The tests build a small survey result by hand.  Asset Maker creates the surveyed asset, a second asset that
 * the annotations point at, and two survey reports.  A resource profile log annotation is created under the
 * first report and a request-for-action annotation on its own; both are anchored to the surveyed asset as a
 * survey would anchor them.  Each relationship the service manages is then created, checked from both ends
 * (and through the service's own queries where it has one) and removed again.  Finally the annotations are
 * deleted with the service, and the assets and reports with Asset Maker.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataDiscoveryFVT
{
    private static final ViewServiceClient dataDiscovery = new ViewServiceClient(ViewServiceDescription.DATA_DISCOVERY);
    private static final ViewServiceClient assetMaker    = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static final String ANNOTATION_TYPE  = ViewServicesFvtTestSupport.qualifiedName("AnnotationType", "profile-log");
    private static final String ANALYSIS_STEP    = ViewServicesFvtTestSupport.qualifiedName("AnalysisStep", "profile");
    private static final String UPDATED_SUMMARY  = "Profile of the surveyed data set, revised by " + DataDiscoveryFVT.class.getSimpleName();

    private static String subjectAssetGUID = null;
    private static String targetAssetGUID  = null;
    private static String firstReportGUID  = null;
    private static String secondReportGUID = null;

    private static String profileAnnotationGUID   = null;
    private static String actionAnnotationGUID    = null;
    private static String templatedAnnotationGUID = null;

    private static final List<String> createdAnnotationGUIDs = new ArrayList<>();
    private static final List<String> createdAssetGUIDs      = new ArrayList<>();


    /**
     * The surveyed asset, the asset the annotations point at, and two survey reports.
     */
    @Test
    @Order(1)
    void createSurveyContext()
    {
        subjectAssetGUID = createDataSet("subject", "Surveyed data set");
        targetAssetGUID  = createDataSet("target", "Data set the annotations refer to");
        firstReportGUID  = createSurveyReport("first");
        secondReportGUID = createSurveyReport("second");
    }


    /**
     * createAnnotation with a ReportedAnnotation parent: the annotation is retrievable with its properties and
     * shows the report it came from, and the report's new annotations include it.  A second annotation, a
     * request for action, is created without a parent.
     */
    @Test
    @Order(2)
    void createAndRetrieveAnnotations()
    {
        ResourceProfileLogAnnotationProperties profileProperties = new ResourceProfileLogAnnotationProperties();

        profileProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ResourceProfileLogAnnotation", "profile"));
        profileProperties.setDisplayName("Profile log annotation");
        profileProperties.setDescription("Created by " + DataDiscoveryFVT.class.getSimpleName());
        profileProperties.setAnnotationType(ANNOTATION_TYPE);
        profileProperties.setAnalysisStep(ANALYSIS_STEP);
        profileProperties.setSummary("Profile of the surveyed data set");
        profileProperties.setConfidence(80);

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setAnchorGUID(subjectAssetGUID);
        requestBody.setIsOwnAnchor(false);
        requestBody.setParentGUID(firstReportGUID);
        requestBody.setParentRelationshipTypeName(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);
        requestBody.setParentAtEnd1(true);
        requestBody.setProperties(profileProperties);

        profileAnnotationGUID = dataDiscovery.create("/annotations", requestBody);
        createdAnnotationGUIDs.add(profileAnnotationGUID);

        OpenMetadataRootElement profileAnnotation = retrieveAnnotation(profileAnnotationGUID);

        assertEquals(OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName, profileAnnotation.getElementHeader().getType().getTypeName());

        ResourceProfileLogAnnotationProperties retrievedProperties = assertInstanceOf(ResourceProfileLogAnnotationProperties.class, profileAnnotation.getProperties());

        assertEquals(profileProperties.getQualifiedName(), retrievedProperties.getQualifiedName());
        assertEquals(ANNOTATION_TYPE, retrievedProperties.getAnnotationType());
        assertEquals(ANALYSIS_STEP, retrievedProperties.getAnalysisStep());
        assertEquals(80, retrievedProperties.getConfidence());
        assertTrue(contains(profileAnnotation.getFromSurveyReports(), firstReportGUID),
                   "the annotation does not show the survey report it was created under");

        assertTrue(contains(dataDiscovery.elements("/survey-reports/" + firstReportGUID + "/new-annotations", new ResultsRequestBody()), profileAnnotationGUID),
                   "getNewAnnotations does not return the annotation created under the report");

        RequestForActionProperties actionProperties = new RequestForActionProperties();

        actionProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("RequestForAction", "action"));
        actionProperties.setDisplayName("Request for action annotation");
        actionProperties.setAnnotationType(ViewServicesFvtTestSupport.qualifiedName("AnnotationType", "request-for-action"));
        actionProperties.setActionRequested("Classify the data set");
        actionProperties.setActionSourceName(DataDiscoveryFVT.class.getSimpleName());
        actionProperties.setActionProperties(Map.of("reason", "test"));

        NewElementRequestBody actionRequestBody = new NewElementRequestBody();

        actionRequestBody.setAnchorGUID(subjectAssetGUID);
        actionRequestBody.setIsOwnAnchor(false);
        actionRequestBody.setProperties(actionProperties);

        actionAnnotationGUID = dataDiscovery.create("/annotations", actionRequestBody);
        createdAnnotationGUIDs.add(actionAnnotationGUID);

        RequestForActionProperties retrievedActionProperties = assertInstanceOf(RequestForActionProperties.class,
                                                                                retrieveAnnotation(actionAnnotationGUID).getProperties());

        assertEquals("Classify the data set", retrievedActionProperties.getActionRequested());
        assertEquals(Map.of("reason", "test"), retrievedActionProperties.getActionProperties());
    }


    /**
     * updateAnnotation with a merge update changes the summary and keeps the other properties.
     */
    @Test
    @Order(3)
    void updateAnnotation()
    {
        AnnotationProperties properties = new ResourceProfileLogAnnotationProperties();

        properties.setSummary(UPDATED_SUMMARY);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        assertTrue(dataDiscovery.post("/annotations/" + profileAnnotationGUID + "/update", requestBody, BooleanResponse.class).getFlag(),
                   "updateAnnotation reported that nothing was updated");

        AnnotationProperties updated = assertInstanceOf(AnnotationProperties.class, retrieveAnnotation(profileAnnotationGUID).getProperties());

        assertEquals(UPDATED_SUMMARY, updated.getSummary());
        assertEquals(ANNOTATION_TYPE, updated.getAnnotationType(), "the merge update lost the annotation type");
        assertEquals(ANALYSIS_STEP, updated.getAnalysisStep(), "the merge update lost the analysis step");
    }


    /**
     * The by-name, by-analysis-step, by-annotation-type and search-string queries all find the annotation by
     * the run-unique values it was created with.
     */
    @Test
    @Order(4)
    void findAnnotations()
    {
        assertTrue(contains(dataDiscovery.elements("/annotations/by-name", filter(ANNOTATION_TYPE)), profileAnnotationGUID),
                   "getAnnotationsByName does not find the annotation by its annotation type");
        assertTrue(contains(dataDiscovery.elements("/annotations/by-name", filter(ANALYSIS_STEP)), profileAnnotationGUID),
                   "getAnnotationsByName does not find the annotation by its analysis step");

        assertTrue(contains(dataDiscovery.elements("/annotations/by-analysis-step", filter(ANALYSIS_STEP)), profileAnnotationGUID),
                   "getAnnotationsByAnalysisStep does not find the annotation");
        assertFalse(contains(dataDiscovery.elements("/annotations/by-analysis-step", filter(ANNOTATION_TYPE)), profileAnnotationGUID),
                    "getAnnotationsByAnalysisStep matched the annotation type");

        assertTrue(contains(dataDiscovery.elements("/annotations/by-annotation-type", filter(ANNOTATION_TYPE)), profileAnnotationGUID),
                   "getAnnotationsByAnnotationType does not find the annotation");
        assertFalse(contains(dataDiscovery.elements("/annotations/by-annotation-type", filter(ANALYSIS_STEP)), profileAnnotationGUID),
                    "getAnnotationsByAnnotationType matched the analysis step");

        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        List<OpenMetadataRootElement> found = dataDiscovery.elements("/annotations/by-search-string", searchRequestBody);

        assertTrue(contains(found, profileAnnotationGUID), "findAnnotations does not find the profile annotation");
        assertTrue(contains(found, actionAnnotationGUID), "findAnnotations does not find the request for action annotation");
        assertFalse(contains(found, subjectAssetGUID), "findAnnotations returned an asset that is not an annotation");
    }


    /**
     * attachAnnotationToReport links an existing annotation to a second report; detach removes the link.
     */
    @Test
    @Order(5)
    void attachAndDetachReport()
    {
        String reportPath = "/survey-reports/" + secondReportGUID + "/new-annotations";

        ReportedAnnotationProperties properties = new ReportedAnnotationProperties();

        properties.setLabel("reused");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        dataDiscovery.call(reportPath + "/" + actionAnnotationGUID + "/attach", requestBody);

        assertTrue(contains(dataDiscovery.elements(reportPath, new ResultsRequestBody()), actionAnnotationGUID),
                   "getNewAnnotations does not return the attached annotation");
        assertTrue(contains(retrieveAnnotation(actionAnnotationGUID).getFromSurveyReports(), secondReportGUID),
                   "the annotation does not show the report it was attached to");

        dataDiscovery.call(reportPath + "/" + actionAnnotationGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(contains(dataDiscovery.elements(reportPath, new ResultsRequestBody()), actionAnnotationGUID),
                    "getNewAnnotations still returns the detached annotation");
        assertFalse(contains(retrieveAnnotation(actionAnnotationGUID).getFromSurveyReports(), secondReportGUID),
                    "the annotation still shows the report it was detached from");
    }


    /**
     * linkAnnotationToDescribedElement makes the annotation one of the asset's associated annotations.
     */
    @Test
    @Order(6)
    void linkAndDetachDescribedElement()
    {
        String path = "/elements/" + subjectAssetGUID + "/associated-annotations";

        AssociatedAnnotationProperties properties = new AssociatedAnnotationProperties();

        properties.setLabel("describes");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        dataDiscovery.call(path + "/" + profileAnnotationGUID + "/attach", requestBody);

        assertTrue(contains(dataDiscovery.elements(path, new ResultsRequestBody()), profileAnnotationGUID),
                   "getAnnotationsForElement does not return the linked annotation");
        assertTrue(contains(retrieveAnnotation(profileAnnotationGUID).getAnnotationSubjects(), subjectAssetGUID),
                   "the annotation does not show the element it describes");
        assertTrue(contains(retrieveAsset(subjectAssetGUID).getAssociatedAnnotations(), profileAnnotationGUID),
                   "the asset does not show the annotation that describes it");

        dataDiscovery.call(path + "/" + profileAnnotationGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(contains(dataDiscovery.elements(path, new ResultsRequestBody()), profileAnnotationGUID),
                    "getAnnotationsForElement still returns the detached annotation");
        assertFalse(contains(retrieveAnnotation(profileAnnotationGUID).getAnnotationSubjects(), subjectAssetGUID),
                    "the annotation still shows the element it was detached from");
    }


    /**
     * createAnnotationFromTemplate copies the profile annotation into a new annotation under the second report,
     * which is then linked to the original as its successor (AnnotationExtension) and unlinked again.
     */
    @Test
    @Order(7)
    void templateAndPredecessor()
    {
        ResourceProfileLogAnnotationProperties replacementProperties = new ResourceProfileLogAnnotationProperties();

        replacementProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ResourceProfileLogAnnotation", "templated"));
        replacementProperties.setDisplayName("Templated profile log annotation");

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(profileAnnotationGUID);
        requestBody.setReplacementProperties(replacementProperties);
        requestBody.setAnchorGUID(subjectAssetGUID);
        requestBody.setIsOwnAnchor(false);
        requestBody.setParentGUID(secondReportGUID);
        requestBody.setParentRelationshipTypeName(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);
        requestBody.setParentAtEnd1(true);

        templatedAnnotationGUID = dataDiscovery.create("/annotations/from-template", requestBody);
        createdAnnotationGUIDs.add(templatedAnnotationGUID);

        assertNotEquals(profileAnnotationGUID, templatedAnnotationGUID);

        OpenMetadataRootElement templated = retrieveAnnotation(templatedAnnotationGUID);

        assertEquals(OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName, templated.getElementHeader().getType().getTypeName());

        AnnotationProperties templatedProperties = assertInstanceOf(AnnotationProperties.class, templated.getProperties());

        assertEquals(replacementProperties.getQualifiedName(), templatedProperties.getQualifiedName());
        assertEquals(ANNOTATION_TYPE, templatedProperties.getAnnotationType(), "the annotation type was not copied from the template");
        assertEquals(UPDATED_SUMMARY, templatedProperties.getSummary(), "the summary was not copied from the template");
        assertTrue(contains(templated.getFromSurveyReports(), secondReportGUID),
                   "the templated annotation does not show the report it was created under");

        String linkPath = "/annotations/" + profileAnnotationGUID + "/later-annotations/" + templatedAnnotationGUID;

        AnnotationExtensionProperties properties = new AnnotationExtensionProperties();

        properties.setLabel("next survey");

        NewRelationshipRequestBody linkRequestBody = new NewRelationshipRequestBody();

        linkRequestBody.setProperties(properties);

        dataDiscovery.call(linkPath + "/attach", linkRequestBody);

        assertTrue(contains(dataDiscovery.elements("/annotations/" + profileAnnotationGUID + "/annotation-extensions", new ResultsRequestBody()), templatedAnnotationGUID),
                   "getAnnotationExtensions does not return the later annotation");
        assertTrue(contains(dataDiscovery.elements("/annotations/" + templatedAnnotationGUID + "/previous-annotations", new ResultsRequestBody()), profileAnnotationGUID),
                   "getPreviousAnnotations does not return the earlier annotation");
        assertFalse(contains(dataDiscovery.elements("/annotations/" + profileAnnotationGUID + "/previous-annotations", new ResultsRequestBody()), templatedAnnotationGUID),
                    "getPreviousAnnotations returned the later annotation as the earlier one's predecessor");
        assertTrue(contains(retrieveAnnotation(templatedAnnotationGUID).getPreviousAnnotations(), profileAnnotationGUID),
                   "the later annotation does not show its predecessor");

        dataDiscovery.call(linkPath + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(contains(dataDiscovery.elements("/annotations/" + profileAnnotationGUID + "/annotation-extensions", new ResultsRequestBody()), templatedAnnotationGUID),
                    "getAnnotationExtensions still returns the detached annotation");
        assertFalse(contains(dataDiscovery.elements("/annotations/" + templatedAnnotationGUID + "/previous-annotations", new ResultsRequestBody()), profileAnnotationGUID),
                    "getPreviousAnnotations still returns the detached annotation");
    }


    /**
     * linkResourceProfileData links the profile log annotation to the asset that holds the profile data.
     */
    @Test
    @Order(8)
    void linkAndDetachResourceProfileData()
    {
        String path = "/annotations/" + profileAnnotationGUID + "/resource-profile-data-assets/" + targetAssetGUID;

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(new ResourceProfileDataProperties());

        dataDiscovery.call(path + "/attach", requestBody);

        assertTrue(contains(retrieveAnnotation(profileAnnotationGUID).getResourceProfileData(), targetAssetGUID),
                   "the annotation does not show the asset holding its profile data");
        assertTrue(contains(retrieveAsset(targetAssetGUID).getResourceProfileAnnotations(), profileAnnotationGUID),
                   "the asset does not show the annotation whose profile data it holds");

        dataDiscovery.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(contains(retrieveAnnotation(profileAnnotationGUID).getResourceProfileData(), targetAssetGUID),
                    "the annotation still shows the profile data asset after detach");
    }


    /**
     * linkAnnotationMatch records that the target asset matched the annotation.
     */
    @Test
    @Order(9)
    void linkAndDetachAnnotationMatch()
    {
        String path = "/annotations/" + profileAnnotationGUID + "/matched-elements/" + targetAssetGUID;

        AnnotationMatchProperties properties = new AnnotationMatchProperties();

        properties.setLabel("matched");
        properties.setMethod("name comparison");
        properties.setThreshold(10);

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        dataDiscovery.call(path + "/attach", requestBody);

        assertTrue(contains(retrieveAnnotation(profileAnnotationGUID).getAnnotationMatches(), targetAssetGUID),
                   "the annotation does not show the element it matched");
        assertTrue(contains(retrieveAsset(targetAssetGUID).getMatchedByAnnotations(), profileAnnotationGUID),
                   "the asset does not show the annotation that matched it");

        dataDiscovery.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(contains(retrieveAnnotation(profileAnnotationGUID).getAnnotationMatches(), targetAssetGUID),
                    "the annotation still shows the matched element after detach");
    }


    /**
     * linkRequestForActionTarget points the request for action at the element that needs attention.
     */
    @Test
    @Order(10)
    void linkAndDetachRequestForActionTarget()
    {
        String path = "/annotations/" + actionAnnotationGUID + "/request-for-action-targets/" + targetAssetGUID;

        RequestForActionTargetProperties properties = new RequestForActionTargetProperties();

        properties.setActionTargetName("dataSetToClassify");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        dataDiscovery.call(path + "/attach", requestBody);

        assertTrue(contains(retrieveAnnotation(actionAnnotationGUID).getRequestForActionTargets(), targetAssetGUID),
                   "the request for action does not show its target");
        assertTrue(contains(retrieveAsset(targetAssetGUID).getIdentifiedByRequestForActions(), actionAnnotationGUID),
                   "the asset does not show the request for action that targets it");

        dataDiscovery.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(contains(retrieveAnnotation(actionAnnotationGUID).getRequestForActionTargets(), targetAssetGUID),
                    "the request for action still shows its target after detach");
    }


    /**
     * deleteAnnotation removes each annotation; the reports and the surveyed asset survive.
     */
    @Test
    @Order(11)
    void deleteAnnotations()
    {
        for (String annotationGUID : List.copyOf(createdAnnotationGUIDs))
        {
            dataDiscovery.call("/annotations/" + annotationGUID + "/delete", new DeleteElementRequestBody());

            ViewServicesFvtTestSupport.assertGone(dataDiscovery, "/annotations/" + annotationGUID + "/retrieve");
            createdAnnotationGUIDs.remove(annotationGUID);
        }

        assertTrue(dataDiscovery.elements("/survey-reports/" + firstReportGUID + "/new-annotations", new ResultsRequestBody()).isEmpty(),
                   "the report still has annotations after they were all deleted");

        retrieveAsset(subjectAssetGUID);
        retrieveAsset(firstReportGUID);
    }


    /**
     * Remove whatever the tests created, including what a failed test left behind.
     */
    @AfterAll
    static void cleanUp()
    {
        for (String annotationGUID : createdAnnotationGUIDs)
        {
            dataDiscovery.postUnchecked("/annotations/" + annotationGUID + "/delete", new DeleteElementRequestBody(), BooleanResponse.class);
        }

        for (String assetGUID : createdAssetGUIDs)
        {
            assetMaker.call("/assets/" + assetGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + assetGUID + "/retrieve");
        }
    }


    private static String createDataSet(String name,
                                        String displayName)
    {
        DataSetProperties properties = new DataSetProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataSet", name));
        properties.setDisplayName(displayName);

        return createAsset(properties);
    }


    private static String createSurveyReport(String name)
    {
        SurveyReportProperties properties = new SurveyReportProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SurveyReport", name));
        properties.setDisplayName("Survey report " + name);
        properties.setAnalysisStep(ANALYSIS_STEP);

        return createAsset(properties);
    }


    private static String createAsset(DataSetProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        String assetGUID = assetMaker.create("/assets", requestBody);

        createdAssetGUIDs.add(assetGUID);

        return assetGUID;
    }


    private static OpenMetadataRootElement retrieveAnnotation(String annotationGUID)
    {
        OpenMetadataRootElement annotation = dataDiscovery.element("/annotations/" + annotationGUID + "/retrieve", new GetRequestBody());

        assertNotNull(annotation, "getAnnotationByGUID returned nothing for " + annotationGUID);
        assertEquals(annotationGUID, annotation.getElementHeader().getGUID());

        return annotation;
    }


    private static OpenMetadataRootElement retrieveAsset(String assetGUID)
    {
        OpenMetadataRootElement asset = assetMaker.element("/assets/" + assetGUID + "/retrieve", new GetRequestBody());

        assertNotNull(asset, "asset " + assetGUID + " is missing");

        return asset;
    }


    private static FilterRequestBody filter(String value)
    {
        FilterRequestBody requestBody = new FilterRequestBody();

        requestBody.setFilter(value);

        return requestBody;
    }


    private static boolean contains(List<?> elements,
                                    String  guid)
    {
        if (elements == null)
        {
            return false;
        }

        Function<Object, String> guidOf = element -> switch (element)
        {
            case OpenMetadataRootElement rootElement -> rootElement.getElementHeader().getGUID();
            case RelatedMetadataElementSummary related -> related.getRelatedElement().getElementHeader().getGUID();
            default -> null;
        };

        return elements.stream().anyMatch(element -> guid.equals(guidOf.apply(element)));
    }
}
