/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers;

/**
 * DiscoveryAnalysisReportMapper maps between concepts from the Open Discovery Framework (ODF) to
 * the Open Metadata Types.
 */
public class AnnotationMapper
{
    public static final String ANNOTATION_TYPE_GUID    = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String ANNOTATION_TYPE_NAME    = "Annotation";

    public static final String ANNOTATION_TYPE_PROPERTY_NAME       = "annotationType";        /* from Annotation entity */
    public static final String SUMMARY_PROPERTY_NAME               = "summary";               /* from Annotation entity */
    public static final String CONFIDENCE_LEVEL_PROPERTY_NAME      = "confidenceLevel";       /* from Annotation entity */
    public static final String EXPRESSION_PROPERTY_NAME            = "expression";            /* from Annotation entity */
    public static final String EXPLANATION_PROPERTY_NAME           = "explanation";           /* from Annotation entity */
    public static final String ANALYSIS_STEP_PROPERTY_NAME         = "analysisStep";          /* from Annotation entity */
    public static final String JSON_PROPERTIES_PROPERTY_NAME       = "jsonProperties";        /* from Annotation entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties";  /* from Annotation entity */
    public static final String ANCHOR_GUID_PROPERTY_NAME           = "anchorGUID";            /* from Annotation entity */

    public static final String ANNOTATION_TO_EXTENSION_TYPE_GUID   = "605aaa6d-682e-405c-964b-ca6aaa94be1b";     /* from Area 6 */
    public static final String ANNOTATION_TO_EXTENSION_TYPE_NAME   = "Annotation";
    /* End1 = (extended)Annotation; End 2 = Annotation(Extension) */

    public static final String REPORT_TO_ENGINE_TYPE_GUID          = "2c318c3a-5dc2-42cd-a933-0087d852f67f";    /* from Area 6 */
    public static final String REPORT_TO_ENGINE_TYPE_NAME          = "DiscoveryEngineReport";
    /* End1 = OpenDiscoveryEngine; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_SERVICE_TYPE_GUID          = "1744d72b-903d-4273-9229-de20372a17e2";   /* from Area 6 */
    public static final String REPORT_TO_SERVICE_TYPE_NAME          = "DiscoveryInvocationReport";
    /* End1 = OpenDiscoveryService; End 2 = OpenDiscoveryAnalysisReport */
}
