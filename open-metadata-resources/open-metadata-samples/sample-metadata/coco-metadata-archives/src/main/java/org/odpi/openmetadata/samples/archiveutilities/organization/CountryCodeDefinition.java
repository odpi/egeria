/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


/**
 * The CountryCodeDefinition is used to feed the definition of the CountryCode valid value set for Coco Pharmaceuticals' employees and locations.
 */
public enum CountryCodeDefinition
{
    /**
     * United States of America (USA)
     */
    USA ("USA","United States of America (USA)"),

    /**
     * Netherlands
     */
    NL  ("NL", "Netherlands"),

    /**
     * United Kingdom (UK)
     */
    UK  ("UK", "United Kingdom (UK)"),
    ;

    public static final String validValueSetName         = "CountryCode";
    public static final String validValueSetPropertyName = "countryCode";
    public static final String validValueSetDescription  = "Identifies the countries that Coco Pharmaceuticals operates in.";
    public static final String validValueSetUsage        = "Used in locations and profiles to identify the country where the location or person is based.";
    public static final String validValueSetScope        = "For property of Coco Pharmaceuticals, their employees and partners.";

    private final String preferredValue;
    private final String displayName;


    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param displayName   name for the enum
     */
    CountryCodeDefinition(String preferredValue, String displayName)
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
        return "CountryCodeDefinition{" + displayName + '}';
    }
}
