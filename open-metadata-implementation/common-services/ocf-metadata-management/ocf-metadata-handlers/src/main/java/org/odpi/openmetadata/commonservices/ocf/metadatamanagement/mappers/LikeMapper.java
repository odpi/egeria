/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * LikeMapper provides property name mapping for Likes and their relationships.
 */
public class LikeMapper
{
    public static final String LIKE_TYPE_GUID                            = "deaa5ca0-47a0-483d-b943-d91c76744e01";
    public static final String LIKE_TYPE_NAME                            = "Like";          /* from Area 1 */

    public static final String ANCHOR_GUID_PROPERTY_NAME                 = "anchorGUID";    /* from Like entity */

    public static final String REFERENCEABLE_TO_LIKE_TYPE_GUID           = "e2509715-a606-415d-a995-61d00503dad4";
    public static final String REFERENCEABLE_TO_LIKE_TYPE_NAME           = "AttachedLike";
    /* End1 = Referenceable; End 2 = Like */

    public static final String IS_PUBLIC_PROPERTY_NAME                   = "isPublic";      /* from AttachedLike relationship*/
}
