/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * PersonalProfileMapper provides property name mapping for Personal Profiles.
 *
 * Specifically, a single PersonalProfile is represented as a Person entity with an optional ContributionRecord
 * entity linked to the Person using a PersonalContribution relationship.
 */
public class PersonalProfileMapper
{
    public static final String PERSON_ENTITY_TYPE_GUID = "ac406bf8-e53e-49f1-9088-2af28bbbd285";
    public static final String PERSONAL_PROFILE_TYPE_NAME = "Person";

    public static final String QUALIFIED_NAME_PROPERTY_NAME        = "qualifiedName";        /* from Referenceable entity */
    public static final String NAME_PROPERTY_NAME                  = "name";                 /* from ActorProfile entity */
    public static final String DESCRIPTION_PROPERTY_NAME           = "description";          /* from ActorProfile entity */
    public static final String FULL_NAME_PROPERTY_NAME             = "fullName";             /* from Person entity */
    public static final String JOB_TITLE_PROPERTY_NAME             = "jobTitle";             /* from Person entity */
    public static final String KARMA_POINTS_PROPERTY_NAME          = "karmaPoints";          /* from ContributionRecord entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties"; /* from Referenceable entity */

}
