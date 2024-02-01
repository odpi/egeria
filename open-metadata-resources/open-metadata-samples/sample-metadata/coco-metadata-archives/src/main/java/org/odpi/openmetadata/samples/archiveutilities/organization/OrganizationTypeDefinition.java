/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


/**
 * The OrganizationTypeDefinition is used to feed the definition of the different organizations involved in Coco Pharmaceuticals scenarios.
 * It is the value that is stored in the "teamType" properties of the Team entity and is defined in the OrganizationTypes valid value set.
 */
public enum OrganizationTypeDefinition
{
    /**
     * This is the controlling organization that is the decision maker and owner of the open metadata landscape.
     */
    PRIMARY("Primary",
        "Primary Organization",
        "This is the controlling organization that is the decision maker and owner of the open metadata landscape.",
        "Use this for coco Pharmaceuticals' Organization entity."),

    /**
     * This is for a business partner's Organization entity.
     */
    PARTNER("Partner",
            "Partner Organization",
            "This is for a business partner's Organization entity.",
            "Use this for partner organizations such as hospitals and suppliers."),

    /**
     * This is for an internal department's Team entity.
     */
    DEPT("Dept",
            "Department",
            "This is for an internal department's Team entity.",
            "Use this for teams in an organization that are part of the organization structure."),

    /**
     * This is for the Team entity that defines the membership of a self-organizing community of interest.
     */
    COMMUNITY("Community",
         "Community of Interest",
         "This is for the Team entity that defines the membership of a self-organizing community of interest.",
         "Use this for the membership of communities."),

    /**
     * This is for the Team entity that defines the contributors to a project/campaign.
     */
    PROJECT("Project",
              "Project team",
              "This is for the Team entity that defines the contributors to a project/campaign.",
              "Use this for the team assembled to deliver a project."),
    ;

    public static final String validValueSetName = "OrganizationType";
    public static final String validValueSetDescription = "Describes the reason behind a team's formation.";
    public static final String validValueSetUsage = "Used in the 'teamType' property of the Team entity.";
    public static final String validValueSetScope = "Used for all types of Teams/Organizations.";


    private final String preferredValue;
    private final String displayName;
    private final String description;
    private final String usage;

    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param usage   criteria for use
     */
    OrganizationTypeDefinition(String preferredValue, String displayName, String description, String usage)
    {
        this.preferredValue = preferredValue;
        this.displayName = displayName;
        this.description = description;
        this.usage = usage;
    }


    /**
     * This is the preferred value that applications should use for this valid value.
     *
     * @return string value
     */
    public String getPreferredValue()
    {
        return preferredValue;
    }


    /**
     * Return the printable name.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the value's meaning.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return how the value should be used.
     *
     * @return string text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "OrganizationTypeDefinition{" + displayName + '}';
    }
}
