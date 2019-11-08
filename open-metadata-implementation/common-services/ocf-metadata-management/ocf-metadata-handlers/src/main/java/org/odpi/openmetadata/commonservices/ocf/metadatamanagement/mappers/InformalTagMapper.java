/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * InformalTagMapper provides property name mapping for InformalTags and their relationships.
 */
public class InformalTagMapper
{
    public static final String INFORMAL_TAG_TYPE_GUID                    = "ba846a7b-2955-40bf-952b-2793ceca090a";
    public static final String INFORMAL_TAG_TYPE_NAME                    = "InformalTag";          /* from Area 1 */

    public static final String IS_PUBLIC_PROPERTY_NAME                   = "isPublic";             /* from InformalTag entity and AttachedTag relationship*/
    public static final String TAG_NAME_PROPERTY_NAME                    = "tagName";              /* from InformalTag entity */
    public static final String TAG_DESCRIPTION_PROPERTY_NAME             = "tagDescription";       /* from InformalTag entity */
    public static final String USER_PROPERTY_NAME                        = "createdBy";            /* From Audit Header */

    public static final String REFERENCEABLE_TO_TAG_TYPE_GUID            = "4b1641c4-3d1a-4213-86b2-d6968b6c65ab";
    public static final String REFERENCEABLE_TO_TAG_TYPE_NAME            = "AttachedTag";
    /* End1 = Referenceable; End 2 = InformalTag */
    /* Also isPublic */
}
