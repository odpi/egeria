/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * AnnotationHandlerReuseTest covers the form of addAnnotationToReport that takes the reuse candidates from the
 * caller.  A survey passes the same list for every annotation it adds against an element, so the handler must
 * keep that list current: an annotation created earlier in the survey has to be found again, a reused one has to
 * carry its new report, and a deleted duplicate has to be gone.  If the list went stale, a survey would either
 * create duplicates or link the same report twice - and nothing would fail.
 * <br><br>
 * The handler is a spy over a mocked metadata store, with the methods that write to the store stubbed, so these
 * tests check the decisions the handler makes and the state of the list it leaves behind.
 */
public class AnnotationHandlerReuseTest
{
    private static final String USER_ID     = "testUser";
    private static final String ASSET_GUID  = "asset-guid";
    private static final String REPORT_1    = "report-1";
    private static final String REPORT_2    = "report-2";

    private AnnotationHandler                    handler;
    private final Map<String, List<String>>      reportsForAnnotation = new HashMap<>();


    /**
     * Create the spy, with the store-writing methods recording what they did rather than calling the store.
     *
     * @throws Exception not expected
     */
    @BeforeMethod
    public void setUp() throws Exception
    {
        OpenMetadataClient openMetadataClient = mock(OpenMetadataClient.class);

        handler = spy(new AnnotationHandler("testServer", null, "testService", openMetadataClient));
        reportsForAnnotation.clear();

        doNothing().when(handler).linkAnnotationToDescribedElement(anyString(), anyString(), anyString(), any(), any());
        doNothing().when(handler).deleteAnnotation(anyString(), anyString(), any());
        doNothing().when(handler).attachAnnotationToReport(anyString(), anyString(), anyString(), any(), any());

        /*
         * Retrieving an annotation returns its current state, including every report it has been linked to.
         */
        doReturn(null).when(handler).getAnnotationByGUID(anyString(), anyString(), any());
    }


    /**
     * An annotation created in this survey is added to the candidates, so the same annotation reported again in
     * the same survey reuses it rather than creating a second one - without the candidates being retrieved again.
     *
     * @throws Exception not expected
     */
    @Test public void testAnnotationCreatedInSurveyIsReused() throws Exception
    {
        List<OpenMetadataRootElement> candidates = new ArrayList<>();

        doReturn("new-annotation").when(handler).createAnnotation(anyString(), any(), any(), any(), any());
        doReturn(this.getAnnotation("new-annotation", new Date(1000L), REPORT_1, "orders")).when(handler).getAnnotationByGUID(anyString(), eq("new-annotation"), any());

        AnnotationHandler.AnnotationResult first = this.addAnnotation(REPORT_1, "orders", candidates);

        assertTrue(first.created());
        assertEquals(candidates.size(), 1);

        AnnotationHandler.AnnotationResult second = this.addAnnotation(REPORT_1, "orders", candidates);

        assertFalse(second.created());
        assertEquals(second.annotationGUID(), "new-annotation");
        assertEquals(candidates.size(), 1);

        verify(handler, times(1)).createAnnotation(anyString(), any(), any(), any(), any());
        verify(handler, never()).attachAnnotationToReport(anyString(), anyString(), anyString(), any(), any());
        verify(handler, never()).getAnnotationsForElement(anyString(), anyString(), any());
    }


    /**
     * A different annotation is not mistaken for a candidate.
     *
     * @throws Exception not expected
     */
    @Test public void testDifferentAnnotationIsCreated() throws Exception
    {
        List<OpenMetadataRootElement> candidates = new ArrayList<>();

        candidates.add(this.getAnnotation("orders-annotation", new Date(1000L), REPORT_1, "orders"));

        doReturn("customers-annotation").when(handler).createAnnotation(anyString(), any(), any(), any(), any());
        doReturn(this.getAnnotation("customers-annotation", new Date(2000L), REPORT_2, "customers")).when(handler).getAnnotationByGUID(anyString(), eq("customers-annotation"), any());

        AnnotationHandler.AnnotationResult result = this.addAnnotation(REPORT_2, "customers", candidates);

        assertTrue(result.created());
        assertEquals(candidates.size(), 2);
    }


    /**
     * When an annotation from an earlier survey is reused, its entry is replaced with the version that carries
     * this survey's report, so reporting it again in the same survey does not link the report a second time.
     *
     * @throws Exception not expected
     */
    @Test public void testReusedAnnotationIsRefreshed() throws Exception
    {
        List<OpenMetadataRootElement> candidates = new ArrayList<>();

        candidates.add(this.getAnnotation("orders-annotation", new Date(1000L), REPORT_1, "orders"));

        doReturn(this.getAnnotation("orders-annotation", new Date(1000L), REPORT_2, "orders")).when(handler).getAnnotationByGUID(anyString(), eq("orders-annotation"), any());

        this.addAnnotation(REPORT_2, "orders", candidates);
        this.addAnnotation(REPORT_2, "orders", candidates);

        verify(handler, times(1)).attachAnnotationToReport(anyString(), eq(REPORT_2), eq("orders-annotation"), any(), any());
        verify(handler, never()).createAnnotation(anyString(), any(), any(), any(), any());
        assertEquals(candidates.size(), 1);
    }


    /**
     * Of several matching annotations the oldest is kept; the others are deleted and leave the candidates.
     *
     * @throws Exception not expected
     */
    @Test public void testDeletedDuplicatesLeaveCandidates() throws Exception
    {
        List<OpenMetadataRootElement> candidates = new ArrayList<>();

        candidates.add(this.getAnnotation("newer-duplicate", new Date(5000L), REPORT_1, "orders"));
        candidates.add(this.getAnnotation("oldest", new Date(1000L), REPORT_1, "orders"));

        doReturn(this.getAnnotation("oldest", new Date(1000L), REPORT_2, "orders")).when(handler).getAnnotationByGUID(anyString(), eq("oldest"), any());

        AnnotationHandler.AnnotationResult result = this.addAnnotation(REPORT_2, "orders", candidates);

        assertEquals(result.annotationGUID(), "oldest");
        verify(handler, times(1)).deleteAnnotation(anyString(), eq("newer-duplicate"), any());
        assertEquals(candidates.size(), 1);
        assertEquals(candidates.get(0).getElementHeader().getGUID(), "oldest");
    }


    /**
     * Without get options the caller is not keeping a list, so it is left as it was.
     *
     * @throws Exception not expected
     */
    @Test public void testListUnchangedWithoutGetOptions() throws Exception
    {
        List<OpenMetadataRootElement> candidates = new ArrayList<>();

        doReturn("new-annotation").when(handler).createAnnotation(anyString(), any(), any(), any(), any());

        handler.addAnnotationToReport(USER_ID,
                                      ASSET_GUID,
                                      REPORT_1,
                                      ASSET_GUID,
                                      this.getProperties("orders"),
                                      new MetadataSourceOptions(),
                                      candidates,
                                      null);

        assertTrue(candidates.isEmpty());
        verify(handler, never()).getAnnotationByGUID(anyString(), anyString(), any());
    }


    /**
     * The original form still retrieves the candidates itself.
     *
     * @throws Exception not expected
     */
    @Test public void testOriginalFormRetrievesCandidates() throws Exception
    {
        doReturn(null).when(handler).getAnnotationsForElement(anyString(), anyString(), any());
        doReturn("new-annotation").when(handler).createAnnotation(anyString(), any(), any(), any(), any());

        AnnotationHandler.AnnotationResult result = handler.addAnnotationToReport(USER_ID,
                                                                                  ASSET_GUID,
                                                                                  REPORT_1,
                                                                                  null,
                                                                                  this.getProperties("orders"),
                                                                                  new MetadataSourceOptions(),
                                                                                  new QueryOptions());

        assertTrue(result.created());
        verify(handler, times(1)).getAnnotationsForElement(anyString(), eq(ASSET_GUID), any());
        verify(handler, never()).getAnnotationByGUID(anyString(), anyString(), any());
    }


    /**
     * Add an annotation describing the asset, keeping the supplied candidates up to date.
     *
     * @param reportGUID survey report
     * @param tableName distinguishes one annotation from another
     * @param candidates the survey's candidates
     * @return result
     * @throws Exception not expected
     */
    private AnnotationHandler.AnnotationResult addAnnotation(String                        reportGUID,
                                                             String                        tableName,
                                                             List<OpenMetadataRootElement> candidates) throws Exception
    {
        return handler.addAnnotationToReport(USER_ID,
                                             ASSET_GUID,
                                             reportGUID,
                                             ASSET_GUID,
                                             this.getProperties(tableName),
                                             new MetadataSourceOptions(),
                                             candidates,
                                             new GetOptions());
    }


    /**
     * Annotation properties as a survey would build them; the qualified name is unique each time.
     *
     * @param tableName distinguishes one annotation from another
     * @return properties
     */
    private ResourceMeasureAnnotationProperties getProperties(String tableName)
    {
        ResourceMeasureAnnotationProperties properties = new ResourceMeasureAnnotationProperties();

        properties.setQualifiedName("Annotation::" + tableName + "::" + Math.random());
        properties.setAnnotationType("Table Measurements");
        properties.setResourceProperties(Map.of("tableName", tableName));

        return properties;
    }


    /**
     * An annotation as it is retrieved from the store: anchored to the asset and linked to one report.
     *
     * @param guid unique identifier
     * @param createTime when it was created
     * @param reportGUID the report it is linked to
     * @param tableName distinguishes one annotation from another
     * @return annotation
     */
    private OpenMetadataRootElement getAnnotation(String guid,
                                                  Date   createTime,
                                                  String reportGUID,
                                                  String tableName)
    {
        ElementHeader elementHeader = new ElementHeader();
        elementHeader.setGUID(guid);

        ElementVersions versions = new ElementVersions();
        versions.setCreateTime(createTime);
        elementHeader.setVersions(versions);

        AnchorsProperties anchorsProperties = new AnchorsProperties();
        anchorsProperties.setAnchorGUID(ASSET_GUID);

        ElementClassification anchor = new ElementClassification();
        anchor.setClassificationProperties(anchorsProperties);
        elementHeader.setAnchor(anchor);

        ElementHeader reportHeader = new ElementHeader();
        reportHeader.setGUID(reportGUID);

        MetadataElementSummary report = new MetadataElementSummary();
        report.setElementHeader(reportHeader);

        RelatedMetadataElementSummary reportedAnnotation = new RelatedMetadataElementSummary();
        reportedAnnotation.setRelatedElement(report);

        OpenMetadataRootElement annotation = new OpenMetadataRootElement();
        annotation.setElementHeader(elementHeader);
        annotation.setProperties(this.getProperties(tableName));
        annotation.setFromSurveyReports(List.of(reportedAnnotation));

        return annotation;
    }
}
