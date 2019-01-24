/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * TeamLeaderMapper provides property name mapping for Team Leader.
 */
public class TeamLeaderMapper
{
    public static final String QUALIFIED_NAME_PROPERTY_NAME        = "qualifiedName";        /* from Referenceable entity */
    public static final String NAME_PROPERTY_NAME                  = "name";                 /* from PersonRole entity */
    public static final String DESCRIPTION_PROPERTY_NAME           = "description";          /* from PersonRole entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties"; /* from Referenceable entity */
    public static final String LEADERSHIP_POSITION_PROPERTY_NAME   = "position";             /* from TeamLeadership relationship */
}
