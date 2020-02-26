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

    /* For AnnotationReview entity */
    public static final String ANNOTATION_REVIEW_TYPE_GUID         = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String ANNOTATION_REVIEW_TYPE_NAME         = "AnnotationReview";

    public static final String REVIEW_DATE_PROPERTY_NAME           = "reviewDate";     /* from AnnotationReview entity */
    public static final String STEWARD_PROPERTY_NAME               = "steward";        /* from AnnotationReview entity */
    public static final String COMMENT_PROPERTY_NAME               = "comment";        /* from AnnotationReview entity */

    /* For AnnotationReviewLink relationship */
    public static final String ANNOTATION_REVIEW_LINK_TYPE_GUID    = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String ANNOTATION_REVIEW_LINK_TYPE_NAME    = "AnnotationReviewLink";
    /* End1 = Annotation; End 2 = AnnotationReview */

    public static final String ANNOTATION_STATUS_PROPERTY_NAME     = "annotationStatus";        /* from AnnotationReviewLink relationship */
    /* Enum Type AnnotationStatus */

    /* For SemanticAnnotation entity */
    public static final String SEMANTIC_ANNOTATION_TYPE_GUID = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String SEMANTIC_ANNOTATION_TYPE_NAME = "SemanticAnnotation";
    /* plus DataFieldAnnotation */

    public static final String INFORMAL_TERM_PROPERTY_NAME                     = "informalTerm";   /* from SemanticAnnotation entity */
    public static final String CANDIDATE_GLOSSARY_TERM_GUIDS_PROPERTY_NAME     = "candidateGlossaryTermGUIDs";  /* from SemanticAnnotation entity */
    public static final String INFORMAL_TOPIC_PROPERTY_NAME                    = "informalTopic";   /* from SemanticAnnotation entity */
    public static final String CANDIDATE_GLOSSARY_CATEGORY_GUIDS_PROPERTY_NAME = "candidateGlossaryCategoryGUIDs";  /* from SemanticAnnotation entity */

    /* For ClassificationAnnotation entity */
    public static final String CLASSIFICATION_ANNOTATION_TYPE_GUID = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String CLASSIFICATION_ANNOTATION_TYPE_NAME = "ClassificationAnnotation";
    /* plus DataFieldAnnotation */

    public static final String CANDIDATE_CLASSIFICATIONS_PROPERTY_NAME  = "candidateClassifications";   /* from ClassificationAnnotation entity */

    /* For QualityAnnotation entity */
    public static final String QUALITY_ANNOTATION_TYPE_GUID = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String QUALITY_ANNOTATION_TYPE_NAME = "QualityAnnotation";
    /* plus DataFieldAnnotation */

    public static final String QUALITY_DIMENSION_PROPERTY_NAME        = "qualityDimension";   /* from QualityAnnotation entity */
    public static final String QUALITY_SCORE_PROPERTY_NAME            = "qualityScore";  /* from QualityAnnotation entity */

    /* For SuspectDuplicateAnnotation entity */
    public static final String SUSPECT_DUPLICATE_ANNOTATION_TYPE_GUID = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String SUSPECT_DUPLICATE_ANNOTATION_TYPE_NAME = "SuspectDuplicateAnnotation";
    /* plus Annotation */

    public static final String DUPLICATE_ANCHOR_GUIDS_PROPERTY_NAME        = "duplicateAnchorGUIDs";   /* from SuspectDuplicateAnnotation entity */
    public static final String MATCHING_PROPERTY_NAMES_PROPERTY_NAME       = "matchingPropertyNames";  /* from SuspectDuplicateAnnotation entity */
    public static final String MATCHING_CLASSIFICATION_NAMES_PROPERTY_NAME = "matchingClassificationNames"; /* from SuspectDuplicateAnnotation entity */
    public static final String MATCHING_ATTACHMENT_GUIDS_PROPERTY_NAME     = "matchingAttachmentGUIDS";  /* from SuspectDuplicateAnnotation entity */
    public static final String MATCHING_RELATIONSHIP_GUIDS_PROPERTY_NAME   = "matchingRelationshipGUIDs"; /* from SuspectDuplicateAnnotation entity */

    /* For DivergentDuplicateAnnotation entity */
    public static final String DIVERGENT_DUPLICATE_ANNOTATION_TYPE_GUID                = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DIVERGENT_DUPLICATE_ANNOTATION_TYPE_NAME                = "DivergentDuplicateAnnotation";
    /* plus Annotation */

    /* For DivergentValueAnnotation entity */
    public static final String DIVERGENT_VALUE_ANNOTATION_TYPE_GUID                    = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DIVERGENT_VALUE_ANNOTATION_TYPE_NAME                    = "DivergentValueAnnotation";
    /* plus DivergentDuplicateAnnotation */

    /* For DivergentClassificationAnnotation entity */
    public static final String DIVERGENT_CLASSIFICATION_ANNOTATION_TYPE_GUID           = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DIVERGENT_CLASSIFICATION_ANNOTATION_TYPE_NAME           = "DivergentClassificationAnnotation";
    /* plus DivergentDuplicateAnnotation */

    /* For DivergentRelationshipAnnotation entity */
    public static final String DIVERGENT_RELATIONSHIP_ANNOTATION_TYPE_GUID             = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DIVERGENT_RELATIONSHIP_ANNOTATION_TYPE_NAME             = "DivergentRelationshipAnnotation";
    /* plus DivergentDuplicateAnnotation */

    /* For DivergentAttachmentAnnotation entity */
    public static final String DIVERGENT_ATTACHMENT_ANNOTATION_TYPE_GUID               = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DIVERGENT_ATTACHMENT_ANNOTATION_TYPE_NAME               = "DivergentAttachmentAnnotation";
    /* plus DivergentDuplicateAnnotation */

    /* For DivergentAttachmentValueAnnotation entity */
    public static final String DIVERGENT_ATTACHMENT_VALUE_ANNOTATION_TYPE_GUID         = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DIVERGENT_ATTACHMENT_VALUE_ANNOTATION_TYPE_NAME         = "DivergentAttachmentValueAnnotation";
    /* plus DivergentAttachmentAnnotation */

    /* For DivergentAttachmentClassificationAnnotation entity */
    public static final String DIVERGENT_ATTACHMENT_CLASS_ANNOTATION_TYPE_GUID         = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DIVERGENT_ATTACHMENT_CLASS_ANNOTATION_TYPE_NAME         = "DivergentAttachmentClassificationAnnotation";
    /* plus DivergentAttachmentAnnotation */

    /* For DivergentAttachmentRelationshipAnnotation entity */
    public static final String DIVERGENT_ATTACHMENT_REL_ANNOTATION_TYPE_GUID           = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DIVERGENT_ATTACHMENT_REL_ANNOTATION_TYPE_NAME           = "DivergentAttachmentRelationshipAnnotation";
    /* plus DivergentAttachmentAnnotation */

    /* for divergent annotations */
    public static final String DUPLICATE_ANCHOR_GUID_PROPERTY_NAME                      = "duplicateAnchorGUID";
    public static final String ATTACHMENT_GUID_PROPERTY_NAME                            = "attachmentGUID";
    public static final String DUPLICATE_ATTACHMENT_GUID_PROPERTY_NAME                  = "duplicateAttachmentGUID";
    public static final String DIVERGENT_PROPERTY_NAMES_PROPERTY_NAME                   = "divergentPropertyNames";
    public static final String DIVERGENT_CLASSIFICATION_NAME_PROPERTY_NAME              = "divergentClassificationName";
    public static final String DIVERGENT_CLASSIFICATION_PROPERTY_NAMES_PROPERTY_NAME    = "divergentClassificationPropertyNames";
    public static final String DIVERGENT_RELATIONSHIP_GUID_PROPERTY_NAME                = "divergentRelationshipGUID";
    public static final String DIVERGENT_RELATIONSHIP_PROPERTY_NAMES_PROPERTY_NAME      = "divergentRelationshipPropertyNames";

    /* For DataSourceMeasurementAnnotation entity */
    public static final String DATA_SOURCE_MEASUREMENT_ANNOTATION_TYPE_GUID         = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DATA_SOURCE_MEASUREMENT_ANNOTATION_TYPE_NAME         = "DataSourceMeasurementAnnotation";
    /* plus Annotation */

    public static final String DATA_SOURCE_PROPERTIES_PROPERTY_NAME    = "dataSourceProperties";  /* from DataSourceMeasurementAnnotation entity */

    /* For DataSourcePhysicalStatusAnnotation entity */
    public static final String DS_PHYSICAL_STATUS_ANNOTATION_TYPE_GUID         = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DS_PHYSICAL_STATUS_ANNOTATION_TYPE_NAME         = "DataSourcePhysicalStatusAnnotation";
    /* plus DataSourceMeasurementAnnotation */

    public static final String CREATE_TIME_PROPERTY_NAME    = "createTime";      /* from DataSourcePhysicalStatusAnnotation entity */
    public static final String MODIFIED_TIME_PROPERTY_NAME  = "modifiedTime";    /* from DataSourcePhysicalStatusAnnotation entity */
    public static final String SIZE_PROPERTY_NAME           = "size";            /* from DataSourcePhysicalStatusAnnotation entity */
    public static final String ENCODING_PROPERTY_NAME       = "encoding";        /* from DataSourcePhysicalStatusAnnotation entity */

    /* For Request For Action Annotation entity */
    public static final String REQUEST_FOR_ACTION_ANNOTATION_TYPE_GUID         = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String REQUEST_FOR_ACTION_ANNOTATION_TYPE_NAME         = "RequestForActionAnnotation";
    /* plus DataFieldAnnotation */

    public static final String DISCOVERY_ACTIVITY_PROPERTY_NAME    = "discoveryActivity";      /* from RequestForActionAnnotation entity */
    public static final String ACTION_REQUESTED_PROPERTY_NAME      = "actionRequested";        /* from RequestForActionAnnotation entity */
    public static final String ACTION_PROPERTIES_PROPERTY_NAME     = "actionProperties";       /* from RequestForActionAnnotation entity */

    /* For Discovery Activity relationship */
    public static final String DISCOVERY_ACTIVITY_TYPE_GUID    = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DISCOVERY_ACTIVITY_TYPE_NAME    = "DiscoveryActivity";
    /* End1 = RequestForActionAnnotation; End 2 = RequestForAction */

    public static final String DESCRIPTION_PROPERTY_NAME     = "description";        /* from DiscoveryActivity relationship */

}
