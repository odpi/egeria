/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * InformalTagMapper provides property name mapping for InformalTags and their relationships.
 */
public class RatingMapper
{
    public static final String RATING_TYPE_GUID                    = "7299d721-d17f-4562-8286-bcd451814478";
    public static final String RATING_TYPE_NAME                    = "Rating";          /* from Area 1 */

    public static final String STARS_PROPERTY_NAME                 = "stars";           /* from Rating entity */
    /* StarRating enum */
    public static final String REVIEW_PROPERTY_NAME                = "review";          /* from Rating entity */
    public static final String ANCHOR_GUID_PROPERTY_NAME           = "anchorGUID";      /* from Rating entity */

    public static final String REFERENCEABLE_TO_RATING_TYPE_GUID   = "0aaad9e9-9cc5-4ad8-bc2e-c1099bab6344";
    public static final String REFERENCEABLE_TO_RATING_TYPE_NAME   = "AttachedRating";
    /* End1 = Referenceable; End 2 = Rating */

    public static final String IS_PUBLIC_PROPERTY_NAME             = "isPublic";        /* from AttachedRating relationship */
}
