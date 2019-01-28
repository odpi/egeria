/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * ProjectCollectionMemberMapper provides property name mapping for project collections.
 */
public class ProjectCollectionMemberMapper
{
    public static final String QUALIFIED_NAME_PROPERTY_NAME             = "qualifiedName";        /* from Referenceable entity */
    public static final String NAME_PROPERTY_NAME                       = "name";                 /* from Project entity */
    public static final String DESCRIPTION_PROPERTY_NAME                = "description";          /* from Project entity */
    public static final String START_DATE_PROPERTY_NAME                 = "startDate";            /* from Project entity */
    public static final String PLANNED_END_DATE_PROPERTY_NAME           = "plannedEndDate";       /* from Project entity */
    public static final String STATUS_PROPERTY_NAME                     = "status";               /* from Project entity */
    public static final String MEMBERSHIP_RATIONALE_PROPERTY_NAME       = "resourceUse";          /* from ResourceList classification */
    public static final String WATCH_STATUS_PROPERTY_NAME               = "watchResource";        /* from ResourceList classification */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME      = "additionalProperties"; /* from Referenceable entity */
}
