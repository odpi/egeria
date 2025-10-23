/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;


import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * The ScopeDefinition is used to define the different scope of responsibility assigned to an individual, term, person or activity.
 */
public enum ScopeDefinition
{
    /**
     * Everyone, both inside and outside of this organization
     */
    WORLD ("The world","Everyone, both inside and outside of this organization."),

    /**
     * All of this organization
     */
    WITHIN_ORGANIZATION ("Within organization","All of this organization."),

    /**
     * Within a trading region (eg USA and Canada, UK, EU).
     */
    WITHIN_REGION ("Within Region","Within a trading region (eg USA and Canada, UK, EU)."),

    /**
     * Within the local country.
     */
    WITHIN_COUNTRY ("Within Country","Within the local country."),

    /**
     * Within a Business Area or Service.
     */
    WITHIN_BUSINESS_CAPABILITY("Within Business Capability", "Within a Business Area or Service."),

    /**
     * Within work of the same type performed by the same professionals for the same purposes.
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

    /**
     * Just for the agreement.
     */
    WITHIN_AGREEMENT ("Within agreement","Within the scope of an agreement such as a subscription, data sharing agreement etc."),

    /**
     * Just for the digital product.
     */
    WITHIN_PRODUCT ("Within digital product","Within the scope of a digital product."),

    /**
     * When scope is used in a template
     */
    TEMPLATE_PLACEHOLDER(PlaceholderProperty.SCOPE.getPlaceholder(), PlaceholderProperty.SCOPE.getName()),

    ;

    public static final String validValueSetName         = "Scope";
    public static final String validValueSetPropertyName = "scope";
    public static final String validValueSetDescription  = "Identifies the scope of responsibility assigned to an individual term, person or activity.";
    public static final String validValueSetUsage        = "Used in relationships and definition that imply responsibility.";
    public static final String validValueSetScope        = "For properties relating to this organization, their employees and partners.";

    private final String preferredValue;
    private final String description;


    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param description   name for the enum
     */
    ScopeDefinition(String preferredValue, String description)
    {
        this.preferredValue = preferredValue;
        this.description    = description;
    }

    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.SCOPE.name,
                                                null,
                                                preferredValue);
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
    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "Scope{" + description + '}';
    }
}
