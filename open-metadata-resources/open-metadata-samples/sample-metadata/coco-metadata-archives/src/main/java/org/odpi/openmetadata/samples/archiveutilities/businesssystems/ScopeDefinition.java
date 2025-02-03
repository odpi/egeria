/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;

/**
 * The ScopeDefinition is used to define the different scope of responsibility assigned to an individual, term, person or activity.
 */
public enum ScopeDefinition
{
    /**
     * Everyone, both inside and outside of Coco Pharmaceuticals.
     */
    WORLD ("The world","Everyone, both inside and outside of Coco Pharmaceuticals."),

    /**
     * All of Coco Pharmaceuticals.
     */
    ALL_COCO ("Across Coco Pharmaceuticals","All of Coco Pharmaceuticals."),

    /**
     * Within the trading region (USA and Canada, UK, EU).
     */
    WITHIN_REGION ("Within Region","Within the trading region (USA and Canada, UK, EU)."),

    /**
     * Within the local country.
     */
    WITHIN_COUNTRY ("Within County","Within the local country."),

    /**
     * Within the team.
     */
    WITHIN_DISCIPLINE ("Within Discipline","Within work of the same type performed by the same professionals for the same purposes."),

    /**
     * Within the team.
     */
    WITHIN_TEAM ("Within Team","Within the team."),

    /**
     * Within the project.
     */
    WITHIN_PROJECT ("Within Project","Within the project."),

    /**
     * Within the solution.
     */
    WITHIN_SOLUTION ("Within Solution","Within the solution."),

    /**
     * Within the local site.
     */
    WITHIN_SITE ("Within Site","Within the local site."),

    /**
     * Within the specific facility at the local site.
     */
    WITHIN_FACILITY ("Within Facility","Within the specific facility at the local site."),

    /**
     * Just for the individual.
     */
    INDIVIDUAL ("Individual","Just for the individual."),

    TEMPLATE_PLACEHOLDER(PlaceholderProperty.SCOPE.getPlaceholder(), PlaceholderProperty.SCOPE.getName()),

    ;

    public static final String validValueSetName         = "Scope";
    public static final String validValueSetPropertyName = "scope";
    public static final String validValueSetDescription  = "Identifies the scope of responsibility assigned to an individual term, person or activity.";
    public static final String validValueSetUsage        = "Used in relationships and definition that imply responsibility.";
    public static final String validValueSetScope        = "For properties relating to Coco Pharmaceuticals, their employees and partners.";

    private final String preferredValue;
    private final String displayName;


    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param displayName   name for the enum
     */
    ScopeDefinition(String preferredValue, String displayName)
    {
        this.preferredValue = preferredValue;
        this.displayName = displayName;
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
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "Scope{" + displayName + '}';
    }
}
