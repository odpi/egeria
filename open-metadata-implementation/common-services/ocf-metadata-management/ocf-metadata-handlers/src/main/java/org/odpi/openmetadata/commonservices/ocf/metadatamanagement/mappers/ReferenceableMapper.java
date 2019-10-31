/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * ReferenceableMapper provides property name mapping for all Referenceables.
 */
public class ReferenceableMapper
{
    public static final String REFERENCEABLE_TYPE_GUID                           = "a32316b8-dc8c-48c5-b12b-71c1b2a080bf";
    public static final String REFERENCEABLE_TYPE_NAME                           = "Referenceable";          /* from Area 0 */

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

    public static final String REFERENCEABLE_LICENCE_TYPE_GUID                   = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
    public static final String REFERENCEABLE_LICENSE_TYPE_NAME                   = "License";
    /* End1 = Referenceable; End 2 = LicenseType */

    public static final String REFERENCEABLE_TO_MEANING_TYPE_GUID                = "e6670973-645f-441a-bec7-6f5570345b92";
    public static final String REFERENCEABLE_TO_MEANING_TYPE_NAME                = "SemanticAssignment";
    /* End1 = Referenceable; End 2 = GlossaryTerm */

    public static final String REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID               = "4f798c0c-6769-4a2d-b489-d2714d89e0a4";
    public static final String REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME               = "AttachedNoteLog";
    /* End1 = Referenceable; End 2 = NoteLog */

    public static final String REFERENCEABLE_TO_RATING_TYPE_GUID                 = "0aaad9e9-9cc5-4ad8-bc2e-c1099bab6344";
    public static final String REFERENCEABLE_TO_RATING_TYPE_NAME                 = "AttachedRating";
    /* End1 = Referenceable; End 2 = Rating */

    public static final String REFERENCEABLE_TO_MORE_INTO_TYPE_GUID              = "1cbf059e-2c11-4e0c-8aae-1da42c1ee73f";
    public static final String REFERENCEABLE_TO_MORE_INFO_TYPE_NAME              = "MoreInformation";
    /* End1 = Referenceable; End 2 = Rating */

    public static final String REFERENCEABLE_TO_RELATED_MEDIA_TYPE_GUID          = "1353400f-b0ab-4ab9-ab09-3045dd8a7140";
    public static final String REFERENCEABLE_TO_RELATED_MEDIA_TYPE_NAME          = "MediaReference";
    /* End1 = Referenceable; End 2 = RelatedMedia */

}
