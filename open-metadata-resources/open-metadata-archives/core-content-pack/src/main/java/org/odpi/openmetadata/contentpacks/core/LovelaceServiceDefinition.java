/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

/**
 * Describes the governance action types that should be registered with the Babbage Analytical Engine.
 */
public enum LovelaceServiceDefinition
{
    KARMA_POINTS(RequestTypeDefinition.AWARD_KARMA_POINTS.getGovernanceActionTypeGUID(),
                 "Award Karma Points",
                 ContentPackDefinition.ORGANIZATION_INSIGHT_CONTENT_PACK),

    ZONE_MEMBERSHIP_PROFILE(RequestTypeDefinition.BUILD_ZONE_MEMBERSHIP_PROFILE.getGovernanceActionTypeGUID(),
                 "Build Zone Membership Profile",
                 ContentPackDefinition.ORGANIZATION_INSIGHT_CONTENT_PACK),

    ;

    private final String                actionTargetGUID;
    private final String                actionTargetName;
    private final ContentPackDefinition contentPackDefinition;


    LovelaceServiceDefinition(String                actionTargetGUID,
                              String                actionTargetName,
                              ContentPackDefinition contentPackDefinition)
    {
        this.actionTargetGUID      = actionTargetGUID;
        this.actionTargetName      = actionTargetName;
        this.contentPackDefinition = contentPackDefinition;
    }


    /**
     * Return the unique identifier for the governance action type.
     *
     * @return string
     */
    public String getCatalogTargetGUID()
    {
        return actionTargetGUID;
    }


    /**
     * Return the unique name of the catalog target.  Make it a useful display name as babbage processes all
     * catalog targets, whatever their name is.
     *
     * @return string
     */
    public String getCatalogTargetName()
    {
        return actionTargetName;
    }


    /**
     * Get identifier of content pack where this definition should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LovelaceServiceDefinition{name='" + name() + "'}";
    }
}
