/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * ReferenceableMapper provides property name mapping for all Referenceables.
 */
public class ReferenceableMapper
{
    public static final String REFERENCEABLE_TYPE_GUID                           = "a32316b8-dc8c-48c5-b12b-71c1b2a080bf";
    public static final String REFERENCEABLE_TYPE_NAME                           = "Referenceable";          /* from Area 0 */

    public static final String PERSONAL_PROFILE_TYPE_GUID                        = "ac406bf8-e53e-49f1-9088-2af28bbbd285";
    public static final String PERSONAL_PROFILE_TYPE_NAME                        = "Person";                 /* from Area 1 */

    public static final String GLOSSARY_TYPE_GUID                                = "36f66863-9726-4b41-97ee-714fd0dc6fe4";
    public static final String GLOSSARY_TYPE_NAME                                = "Glossary";               /* from Area 3 */

    public static final String GLOSSARY_CATEGORY_TYPE_GUID                       = "e507485b-9b5a-44c9-8a28-6967f7ff3672";
    public static final String GLOSSARY_CATEGORY_TYPE_NAME                       = "GlossaryCategory";       /* from Area 3 */

    public static final String GLOSSARY_TERM_TYPE_GUID                           = "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a";
    public static final String GLOSSARY_TERM_TYPE_NAME                           = "GlossaryTerm";           /* from Area 3 */

    public static final String QUALIFIED_NAME_PROPERTY_NAME                      = "qualifiedName";                        /* from Referenceable entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME               = "additionalProperties";                 /* from Referenceable entity */

    public static final String REFERENCEABLE_TO_CERTIFICATION_TYPE_GUID          = "390559eb-6a0c-4dd7-bc95-b9074caffa7f";
    public static final String REFERENCEABLE_TO_CERTIFICATION_TYPE_NAME          = "Certification";
    /* End1 = Referenceable; End 2 = CertificationType */

    public static final String REFERENCEABLE_TO_COMMENT_TYPE_GUID                = "0d90501b-bf29-4621-a207-0c8c953bdac9";
    public static final String REFERENCEABLE_TO_COMMENT_TYPE_NAME                = "AttachedComment";
    /* End1 = Referenceable; End 2 = Comment */

    public static final String REFERENCEABLE_TO_EXTERNAL_ID_TYPE_GUID            = "28ab0381-c662-4b6d-b787-5d77208de126";
    public static final String REFERENCEABLE_TO_EXTERNAL_ID_TYPE_NAME            = "ExternalIdLink";
    /* End1 = Referenceable; End 2 = ExternalId */

    public static final String REFERENCEABLE_TO_EXT_REF_TYPE_GUID                = "7d818a67-ab45-481c-bc28-f6b1caf12f06";
    public static final String REFERENCEABLE_TO_EXT_REF_TYPE_NAME                = "ExternalReferenceLink";
    /* End1 = Referenceable; End 2 = ExternalReference */

    public static final String REFERENCEABLE_TO_TAG_TYPE_GUID                    = "4b1641c4-3d1a-4213-86b2-d6968b6c65ab";
    public static final String REFERENCEABLE_TO_TAG_TYPE_NAME                    = "AttachedTag";
    /* End1 = Referenceable; End 2 = InformalTag */

    public static final String REFERENCEABLE_TO_LIKE_TYPE_GUID                   = "e2509715-a606-415d-a995-61d00503dad4";
    public static final String REFERENCEABLE_TO_LIKE_TYPE_NAME                   = "AttachedLike";
    /* End1 = Referenceable; End 2 = Like */

    public final static String IS_PUBLIC_FEEDBACK_PROPERTY_NAME                  = "isPublic";

    public static final String REFERENCEABLE_TO_LICENSE_TYPE_GUID                = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
    public static final String REFERENCEABLE_TO_LICENSE_TYPE_NAME                = "License";
    /* End1 = Referenceable; End 2 = LicenseType */

    public static final String REFERENCEABLE_TO_MEANING_TYPE_GUID                = "e6670973-645f-441a-bec7-6f5570345b92";
    public static final String REFERENCEABLE_TO_MEANING_TYPE_NAME                = "SemanticAssignment";
    /* End1 = Referenceable; End 2 = GlossaryTerm */

    public static final String TERM_ASSIGNMENT_STATUS_ENUM_TYPE_GUID             = "c8fe36ac-369f-4799-af75-46b9c1343ab3";
    public static final String TERM_ASSIGNMENT_STATUS_ENUM_TYPE_NAME             = "TermAssignmentStatus";

    public static final String SEMANTIC_ASSIGNMENT_DESCRIPTION_PROPERTY_NAME     = "description";
    public static final String SEMANTIC_ASSIGNMENT_EXPRESSION_PROPERTY_NAME      = "expression";
    public static final String SEMANTIC_ASSIGNMENT_STATUS_PROPERTY_NAME          = "status";
    public static final String SEMANTIC_ASSIGNMENT_CONFIDENCE_PROPERTY_NAME      = "confidence";
    public static final String SEMANTIC_ASSIGNMENT_STEWARD_PROPERTY_NAME         = "steward";
    public static final String SEMANTIC_ASSIGNMENT_SOURCE_PROPERTY_NAME          = "source";

    public static final String REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID               = "4f798c0c-6769-4a2d-b489-d2714d89e0a4";
    public static final String REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME               = "AttachedNoteLog";
    /* End1 = Referenceable; End 2 = NoteLogHeader */

    public static final String REFERENCEABLE_TO_RATING_TYPE_GUID                 = "0aaad9e9-9cc5-4ad8-bc2e-c1099bab6344";
    public static final String REFERENCEABLE_TO_RATING_TYPE_NAME                 = "AttachedRating";
    /* End1 = Referenceable; End 2 = Rating */

    public static final String REFERENCEABLE_TO_MORE_INFO_TYPE_GUID              = "1cbf059e-2c11-4e0c-8aae-1da42c1ee73f";
    public static final String REFERENCEABLE_TO_MORE_INFO_TYPE_NAME              = "MoreInformation";
    /* End1 = Referenceable; End 2 = more info Referenceable */

    public static final String REFERENCEABLE_TO_RELATED_MEDIA_TYPE_GUID          = "1353400f-b0ab-4ab9-ab09-3045dd8a7140";
    public static final String REFERENCEABLE_TO_RELATED_MEDIA_TYPE_NAME          = "MediaReference";
    /* End1 = Referenceable; End 2 = RelatedMedia */

    public static final String REFERENCEABLE_TO_COLLECTION_TYPE_GUID             = "5cabb76a-e25b-4bb5-8b93-768bbac005af";
    public static final String REFERENCEABLE_TO_COLLECTION_TYPE_NAME             = "CollectionMembership";
    /* End1 = Collection; End 2 = Referenceable */
    public static final String MEMBERSHIP_RATIONALE_PROPERTY_NAME                = "membershipRationale";

    public static final String REFERENCEABLE_TO_REFERENCE_VALUE_TYPE_GUID        = "111e6d2e-94e9-43ed-b4ed-f0d220668cbf";
    public static final String REFERENCEABLE_TO_REFERENCE_VALUE_TYPE_NAME        = "ReferenceValueAssignment";
    /* End1 = Referenceable; End 2 = ValidValueDefinition */

    public static final String REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_GUID         = "111e6d2e-94e9-43ed-b4ed-f0d220668cbf";
    public static final String REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME         = "SearchKeywordLink";
    /* End1 = Referenceable; End 2 = SearchKeyword */

    public static final String SECURITY_TAG_CLASSIFICATION_TYPE_GUID             = "a0b07a86-9fd3-40ca-bb9b-fe83c6981deb";
    public static final String SECURITY_TAG_CLASSIFICATION_TYPE_NAME             = "SecurityTags";
    public static final String SECURITY_LABELS_PROPERTY_NAME                     = "securityLabels";
    public static final String SECURITY_PROPERTIES_PROPERTY_NAME                 = "securityProperties";

    public static final String ANCHORS_CLASSIFICATION_TYPE_GUID                  = "aa44f302-2e43-4669-a1e7-edaae414fc6e";
    public static final String ANCHORS_CLASSIFICATION_TYPE_NAME                  = "Anchors";
    public static final String ANCHOR_GUID_PROPERTY_NAME                         = "anchorGUID";
    public static final String SCHEMA_ROOT_GUID_PROPERTY_NAME                    = "schemaRootGUID";
    public static final String COMMENT_ROOT_GUID_PROPERTY_NAME                   = "commentTreeRootGUID";

    public static final String LATEST_CHANGE_TARGET_ENUM_TYPE_GUID               = "a0b7d7a0-4af5-4539-9b81-cbef52d8cc5d";
    public static final String LATEST_CHANGE_TARGET_ENUM_TYPE_NAME               = "LatestChangeTarget";

    public static final String LATEST_CHANGE_ACTION_ENUM_TYPE_GUID               = "032d844b-868f-4c4a-bc5d-81f0f9704c4d";
    public static final String LATEST_CHANGE_ACTION_ENUM_TYPE_NAME               = "LatestChangeAction";

    public static final String LATEST_CHANGE_CLASSIFICATION_TYPE_GUID            = "adce83ac-10f1-4279-8a35-346976e94466";
    public static final String LATEST_CHANGE_CLASSIFICATION_TYPE_NAME            = "LatestChange";
    public static final String CHANGE_TARGET_PROPERTY_NAME                       = "changeTarget";
    public static final String CHANGE_ACTION_PROPERTY_NAME                       = "changeAction";
    public static final String CLASSIFICATION_NAME_PROPERTY_NAME                 = "classificationName";
    public static final String ATTACHMENT_GUID_PROPERTY_NAME                     = "attachmentGUID";
    public static final String ATTACHMENT_TYPE_PROPERTY_NAME                     = "attachmentType";
    public static final String RELATIONSHIP_TYPE_PROPERTY_NAME                   = "relationshipType";
    public static final String USER_PROPERTY_NAME                                = "user";
    public static final String ACTION_DESCRIPTION_PROPERTY_NAME                  = "description";

    public static final String TEMPLATE_CLASSIFICATION_TYPE_GUID                 = "25fad4a2-c2d6-440d-a5b1-e537881f84ee";
    public static final String TEMPLATE_CLASSIFICATION_TYPE_NAME                 = "Template";
    public static final String TEMPLATE_NAME_PROPERTY_NAME                       = "name";
    public static final String TEMPLATE_DESCRIPTION_PROPERTY_NAME                = "description";
    public static final String TEMPLATE_ADDITIONAL_PROPERTIES_PROPERTY_NAME      = "additionalProperties";

    public static final String REFERENCEABLE_TO_TEMPLATE_TYPE_GUID               = "e5d7025d-8b4f-43c7-bcae-1147d650b94b";
    public static final String REFERENCEABLE_TO_TEMPLATE_TYPE_NAME               = "SourcedFrom";
    /* End1 = Referenceable; End 2 = Referenceable (template) */

    public static final String GOVERNANCE_CLASSIFICATION_STATUS_ENUM_TYPE_GUID   = "cc540586-ac7c-41ba-8cc1-4da694a6a8e4";
    public static final String GOVERNANCE_CLASSIFICATION_STATUS_ENUM_TYPE_NAME   = "GovernanceClassificationStatus";

    public static final String CONFIDENCE_LEVEL_ENUM_TYPE_GUID                   = "ae846797-d88a-4421-ad9a-318bf7c1fe6f";
    public static final String CONFIDENCE_LEVEL_ENUM_TYPE_NAME                   = "ConfidenceLevel";

    public static final String RETENTION_BASIS_ENUM_TYPE_GUID                    = "de79bf78-ecb0-4fd0-978f-ecc2cb4ff6c7";
    public static final String RETENTION_BASIS_ENUM_TYPE_NAME                    = "RetentionBasis";

    public static final String CRITICALITY_LEVEL_ENUM_TYPE_GUID                  = "22bcbf49-83e1-4432-b008-e09a8f842a1e";
    public static final String CRITICALITY_LEVEL_ENUM_TYPE_NAME                  = "CriticalityLevel";

    public static final String CONFIDENTIALITY_CLASSIFICATION_TYPE_GUID          = "742ddb7d-9a4a-4eb5-8ac2-1d69953bd2b6";
    public static final String CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME          = "Confidentiality";

    public static final String CONFIDENCE_CLASSIFICATION_TYPE_GUID               = "25d8f8d5-2998-4983-b9ef-265f58732965";
    public static final String CONFIDENCE_CLASSIFICATION_TYPE_NAME               = "Confidence";

    public static final String CRITICALITY_CLASSIFICATION_TYPE_GUID              = "d46d211a-bd22-40d5-b642-87b4954a167e";
    public static final String CRITICALITY_CLASSIFICATION_TYPE_NAME              = "Criticality";

    public static final String RETENTION_CLASSIFICATION_TYPE_GUID                = "83dbcdf2-9445-45d7-bb24-9fa661726553";
    public static final String RETENTION_CLASSIFICATION_TYPE_NAME                = "Retention";

    public static final String GOVERNANCE_CLASSIFICATION_STATUS_PROPERTY_NAME       = "status";
    public static final String GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME   = "confidence";
    public static final String GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME      = "steward";
    public static final String GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME       = "source";
    public static final String GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME        = "notes";

    public static final String CONFIDENTIALITY_LEVEL_PROPERTY_NAME                  = "level";
    public static final String CONFIDENCE_LEVEL_PROPERTY_NAME                       = "level";
    public static final String CRITICALITY_LEVEL_PROPERTY_NAME                      = "level";
    public static final String RETENTION_BASIS_PROPERTY_NAME                        = "basis";
    public static final String RETENTION_ASSOCIATED_GUID_PROPERTY_NAME              = "associatedGUID";
    public static final String RETENTION_ARCHIVE_AFTER_PROPERTY_NAME                = "archiveAfter";
    public static final String RETENTION_DELETE_AFTER_PROPERTY_NAME                 = "deleteAfter";
}
