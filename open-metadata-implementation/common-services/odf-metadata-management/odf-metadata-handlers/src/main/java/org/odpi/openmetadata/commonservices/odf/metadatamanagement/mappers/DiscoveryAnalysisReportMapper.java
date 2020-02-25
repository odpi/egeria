/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers;

/**
 * DiscoveryAnalysisReportMapper maps between concepts from the Open Discovery Framework (ODF) to
 * the Open Metadata Types.
 */
public class DiscoveryAnalysisReportMapper
{
    public static final String DISCOVERY_ANALYSIS_REPORT_TYPE_GUID    = "acc7cbc8-09c3-472b-87dd-f78459323dcb";
    public static final String DISCOVERY_ANALYSIS_REPORT_TYPE_NAME    = "OpenDiscoveryAnalysisReport";
    /* Referenceable */

    public static final String DISPLAY_NAME_PROPERTY_NAME             = "displayName";                /* from OpenDiscoveryAnalysisReport entity */
    public static final String DESCRIPTION_PROPERTY_NAME              = "description";                /* from OpenDiscoveryAnalysisReport entity */
    public static final String CREATION_DATE_PROPERTY_NAME            = "executionDate";              /* from OpenDiscoveryAnalysisReport entity */
    public static final String ANALYSIS_PARAMS_PROPERTY_NAME          = "analysisParameters";         /* from OpenDiscoveryAnalysisReport entity */
    public static final String ANALYSIS_STEP_PROPERTY_NAME            = "discoveryRequestStep";       /* from OpenDiscoveryAnalysisReport entity */
    public static final String DISCOVERY_SERVICE_STATUS_PROPERTY_NAME = "discoveryServiceStatus";     /* from OpenDiscoveryAnalysisReport entity */
    public static final String ANCHOR_GUID_PROPERTY_NAME              = "anchorGUID";                 /* from OpenDiscoveryAnalysisReport entity */

    public static final String REPORT_TO_ASSET_TYPE_GUID              = "7eded424-f176-4258-9ae6-138a46b2845f";     /* from Area 6 */
    public static final String REPORT_TO_ASSET_TYPE_NAME              = "AssetDiscoveryReport";
    /* End1 = Asset; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_ENGINE_TYPE_GUID             = "2c318c3a-5dc2-42cd-a933-0087d852f67f";    /* from Area 6 */
    public static final String REPORT_TO_ENGINE_TYPE_NAME             = "DiscoveryEngineReport";
    /* End1 = OpenDiscoveryEngine; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_SERVICE_TYPE_GUID            = "1744d72b-903d-4273-9229-de20372a17e2";   /* from Area 6 */
    public static final String REPORT_TO_SERVICE_TYPE_NAME            = "DiscoveryInvocationReport";
    /* End1 = OpenDiscoveryService; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_ANNOTATIONS_TYPE_GUID        = "51d386a3-3857-42e3-a3df-14a6cad08b93";   /* from Area 6 */
    public static final String REPORT_TO_ANNOTATIONS_TYPE_NAME        = "DiscoveredAnnotation";
    /* End1 = Annotation; End 2 = OpenDiscoveryAnalysisReport */
}
