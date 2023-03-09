/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * TeamMapper provides property name mapping for TeamProperties.
 */
public class TeamMapper
{
    public static final String QUALIFIED_NAME_PROPERTY_NAME        = "qualifiedName";        /* from Referenceable entity */
    public static final String NAME_PROPERTY_NAME                  = "name";                 /* from ActorProfile entity */
    public static final String DESCRIPTION_PROPERTY_NAME           = "description";          /* from ActorProfile entity */
    public static final String TEAM_TYPE_PROPERTY_NAME             = "teamType";             /* from TeamProperties entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties"; /* from Referenceable entity */

    public static final String TEAM_STRUCTURE_TYPE_NAME            = "TeamStructure";        /* from TeamStructure relationship */
}
