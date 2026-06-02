/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ActorRoleGroup identifies common groupings of roles.
 */
public enum ActorRoleGroup
{
    /**
     * A role defining a responsibility to manage one or more assets.
     */
    ASSET_OWNER("asset-owner",
                "A role defining a responsibility to manage one or more assets.",
                "640cf194-d182-4877-8732-d1902517a611"),

    /**
     * A role defining a responsibility to manage a part of the organization's business.  Often responsible for profit and loss.
     */
    BUSINESS_OWNER("business-owner",
                   "A role defining a responsibility to manage a part of the organization's business.  Often responsible for profit and loss",
                   "34cb031e-ec8d-4423-8891-85f766b27b3a"),

    /**
     * A person who has joined a community.
     */
    COMMUNITY_MEMBER("community-member",
                     "A person who has joined a community.",
                     "c8776f32-5c73-4a25-b968-0e684f0df94f"),

    /**
     * An ownership role for a component - typically part of an asset.
     */
    COMPONENT_OWNER("component-owner",
                    "An ownership role for a component - typically part of an asset.",
                    "4115fbb6-830a-48ce-bbc5-17092afe4315"),

    /**
     * Person contributing new content.
     */
    CROWD_SOURCING_CONTRIBUTOR("crowd-sourcing-contributor",
                               "Person contributing new content.",
                               "781c85ad-cfa7-4363-bea1-bc18bab8e3e9"),

    /**
     * An ownership role for a particular type of data value.  This may be stored as data fields in multiple assets and this person typically owns the end-to-end validation of the values as they move from asset to asset.
     */
    DATA_ITEM_OWNER("data-item-owner",
                    "An ownership role for a particular type of data value.  This may be stored as data fields in multiple assets and this person typically owns the end-to-end validation of the values as they move from asset to asset.",
                    "f3fe6d58-c115-4491-a7f7-e3c0c6dd4576"),

    /**
     * Role responsible for managing a digital product.
     */
    DATA_HUB_MANAGER("data-hub-manager",
                     "Role responsible for managing a data hub.",
                     "0d0f28f3-3ea5-4748-b05a-9cc7cfc4c5a5"),

    /**
     * Role responsible for managing a digital product.
     */
    DIGITAL_PRODUCT_MANAGER("digital-product-manager",
                            "Role responsible for managing a digital product.",
                            "7784711c-e06e-4f5a-b272-0843063f3a19"),

    /**
     * Person responsible for a governance domain.
     */
    GOVERNANCE_OFFICER("governance-officer",
                       "Person responsible for a governance domain.",
                       "752cd270-b487-4f1a-ad1b-cf7d4d14b30d"),

    /**
     * A role defining a responsibility to contribute to the operation of a governance activity.  Often represents the views of one or more interested parties.
     */
    GOVERNANCE_REPRESENTATIVE("governance-representative",
                              "A role defining a responsibility to contribute to the operation of a governance activity.  Often represents the views of one or more interested parties.",
                              "c89de5d5-3ebf-4ba0-b69f-1cf488053e99"),

    /**
     * Describes a set of goals, tasks and skills that can be assigned a person and contribute to the governance of a resource.
     */
    GOVERNANCE_ROLE("governance-role",
                    "Describes a set of goals, tasks and skills that can be assigned a person and contribute to the governance of a resource.",
                    "23029a43-8f57-49ce-9776-898da0e5cfbb"),

    /**
     * A role defining a responsibility for activity at a particular location.
     */
    LOCATION_OWNER("location-owner",
                   "A role defining a responsibility for activity at a particular location.",
                   "97c6c05a-5713-4f4c-a9dd-e871788f160d"),

    /**
     * A person adding notes to a note log.
     */
    NOTE_LOG_AUTHOR("note-log-author",
                    "A person adding notes to a note log.",
                    "b8c609ff-555b-4e9e-9d4c-137b4e49c7d1"),

    /**
     * A person with overall responsibility for one or more project.
     */
    PROJECT_MANAGER("project-manager",
                    "A person with overall responsibility for one or more projects.",
                    "5c033adf-3ded-4a18-aa6e-8ff8ef4b053e"),

    /**
     * A role defining a responsibility for writing software for an IT solution.
     */
    SOFTWARE_DEVELOPER("software-developer",
                   "A role defining a responsibility for writing, maintaining and testing software for an IT solution.",
                   "2d95edcb-85f2-4f27-bcf7-4387ee772205"),

    /**
     * A role defining a responsibility for deploying software for an IT solution.
     */
    DEVOPS_ENGINEER("devops-engineer",
                   "A role defining a responsibility for deploying software for an IT solution.",
                    "81e7ed65-7c7a-4dd2-90c1-bc8f04ac1bee"),

    /**
     * A role defining a responsibility for an IT solution.
     */
    SOLUTION_OWNER("solution-owner",
                   "A role defining a responsibility for an IT solution.",
                   "4880d86d-a48b-4e06-91dc-03be0c646ceb"),

    /**
     * A role defining a responsibility to manage the development and maintenance of a subject area.
     */
    SUBJECT_AREA_OWNER("subject-area-owner",
                       "A role defining a responsibility to manage the development and maintenance of a subject area.",
                       "0cf2c8f0-37fd-41b4-9d80-651c7a6e2439"),

    /**
     * Person leading a team.
     */
    TEAM_LEADER("team-leader",
                "Person leading a team.",
                "f92c2a5a-a422-4280-8350-bf52e16c751c"),

    /**
     * Person assigned to a team.
     */
    TEAM_MEMBER("team-member",
                "Person assigned to a team.",
                "18cbe878-e528-4d5f-b635-a61763e823c1"),

    ;

    /**
     * Property value.
     */
    private final String name;


    /**
     * Property value description.
     */
    private final String description;

    /**
     * Property value colour.
     */
    private final String guid;


    /**
     * Constructor for individual enum value.
     *
     * @param name the property value to use in project status
     * @param description description of the project status property value
     *
     */
    ActorRoleGroup(String name,
                   String description,
                   String guid)
    {
        this.name        = name;
        this.description = description;
        this.guid        = guid;
    }


    /**
     * Return the name of the value.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description for this value.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the unique identifier for this value - used in ValidMetadataDefinition.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.PROJECT_HEALTH.name,
                                                null,
                                                name);
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProjectHealth{" + name + '}';
    }
}
