/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * ContributionRecordMapper provides property name mapping for ContributionRecords that are linked off of
 * Personal Profiles via a PersonalContribution relationship.
 *
 * Specifically, a single PersonalProfile is represented as a Person entity with an optional ContributionRecord
 * entity linked to the Person using a PersonalContribution relationship.
 */
public class ContributionRecordMapper
{
    public static final String CONTRIBUTION_RECORD_TYPE_GUID = "ac406bf8-e53e-49f1-9088-2af28cccd285";
    public static final String CONTRIBUTION_RECORD_TYPE_NAME = "ContributionRecord";

    public static final String QUALIFIED_NAME_PROPERTY_NAME        = "qualifiedName";        /* from Referenceable entity */
    public static final String KARMA_POINTS_PROPERTY_NAME          = "karmaPoints";          /* from ContributionRecord entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties"; /* from Referenceable entity */

    public static final String PERSONAL_CONTRIBUTION_TYPE_GUID = "4a316abe-eeee-4d11-ad5a-4bfb4079b80b";
    public static final String PERSONAL_CONTRIBUTION_TYPE_NAME = "PersonalContribution";

}
