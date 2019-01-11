/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * CommunityCollectionMemberMapper provides property name mapping for community collections.
 */
public class CommunityCollectionMemberMapper
{
    public static final String QUALIFIED_NAME_PROPERTY_NAME             = "qualifiedName";        /* from Referenceable entity */
    public static final String NAME_PROPERTY_NAME                       = "name";                 /* from Community entity */
    public static final String DESCRIPTION_PROPERTY_NAME                = "description";          /* from Community entity */
    public static final String MISSION_PROPERTY_NAME                    = "mission";              /* from Community entity */
    public static final String MEMBERSHIP_RATIONALE_PROPERTY_NAME       = "resourceUse";          /* from ResourceList classification */
    public static final String WATCH_STATUS_PROPERTY_NAME               = "watchResource";        /* from ResourceList classification */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME      = "additionalProperties"; /* from Referenceable entity */
}
