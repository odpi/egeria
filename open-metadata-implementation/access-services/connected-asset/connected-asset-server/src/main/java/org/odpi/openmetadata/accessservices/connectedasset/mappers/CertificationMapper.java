/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.mappers;

/**
 * CertificationMapper provides property name mapping for Certifications.
 */
public class CertificationMapper
{
    public static final String TYPE_NAME                                = "Certification";        /* from Certification entity */
    public static final String RELATIONSHIP_TYPE_NAME                   = "Dummy";                /* from Dummy relationship */

    public static final String QUALIFIED_NAME_PROPERTY_NAME             = "qualifiedName";        /* from Referenceable entity */
    public static final String NAME_PROPERTY_NAME                       = "name";                 /* from Asset entity */
    public static final String DESCRIPTION_PROPERTY_NAME                = "description";          /* from Asset entity */
    public static final String OWNER_PROPERTY_NAME                      = "owner";                /* from Asset entity */
    public static final String OWNER_TYPE_PROPERTY_NAME                 = "ownerType";            /* from Asset entity */
    public static final String ZONE_MEMBERSHIP_PROPERTY_NAME            = "zoneMembership";       /* from Asset entity */
    public static final String LATEST_CHANGE_PROPERTY_NAME              = "latestChange";         /* from Asset entity */
    public static final String MEMBERSHIP_RATIONALE_PROPERTY_NAME       = "resourceUse";          /* from ResourceList classification */
    public static final String WATCH_STATUS_PROPERTY_NAME               = "watchResource";        /* from ResourceList classification */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME      = "additionalProperties"; /* from Referenceable entity */
}
