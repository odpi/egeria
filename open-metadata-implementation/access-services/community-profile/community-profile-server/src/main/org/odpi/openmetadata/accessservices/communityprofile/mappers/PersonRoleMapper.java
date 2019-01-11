/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * PersonRoleMapper provides property name mapping for PersonRoles.
 */
public class PersonRoleMapper
{
    public static final String QUALIFIED_NAME_PROPERTY_NAME        = "qualifiedName";        /* from Referenceable entity */
    public static final String NAME_PROPERTY_NAME                  = "name";                 /* from PersonRole entity */
    public static final String DESCRIPTION_PROPERTY_NAME           = "description";          /* from PersonRole entity */
    public static final String HEAD_COUNT_PROPERTY_NAME            = "headCount";            /* from PersonRole entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties"; /* from Referenceable entity */

    public static final String PERSON_ROLE_APPOINTMENT_TYPE_NAME   = "PersonRoleAppointment"; /* from PersonRoleAppointment relationship */
}
