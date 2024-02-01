/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


import java.util.HashMap;
import java.util.Map;

/**
 * The WorkLocationDefinition is used to feed the definition of the primary work locations for Coco Pharmaceuticals employees or its business partners.
 */
public enum WorkLocationDefinition
{
    /**
     * Amsterdam
     */
    AMSTERDAM_SITE("1",
                   "Amsterdam",
                   null,
                   "1833",
                   "Wilhelmdreef",
                   "Amsterdam-Zuidoost",
                   "Amsterdam",
                   "North Holland",
                   "Netherlands",
                   "UTC+1"),

    /**
     * London
     */
    LONDON_SITE("2",
                "London",
                null,
                "32",
                "Wibble Rd",
                "Corterville",
                "London",
                "Greater London",
                "United Kingdom",
                "UTC"),


    /**
     * New York
     */
    NEW_YORK_SITE("3",
                  "New York",
                  null,
                  "27",
                  "Code St",
                  "Harlem",
                  "New York",
                  "NY",
                  "United States",
                  "UTC-5"),

    /**
     * Hampton Hospital
     */
    HAMPTON_HOSPITAL("4",
                     "Hampton Hospital",
                     "Oncology Unit 1, Hampton Hospital",
                     null,
                     "Nightingale St",
                     "Harlem",
                     "New York",
                     "NY",
                     "United States",
                     "UTC-5"),

    /**
     * Oak Dene Hospital
     */
    OAK_DENE_HOSPITAL("5",
                      "Oak Dene Hospital",
                      null,
                      "56",
                      "Tile St",
                      "St Cross",
                      "Winchester",
                      "Hampshire",
                      "United Kingdom",
                      "UTC"),

    /**
     * Old Market Hospital
     */
    OLD_MARKET_HOSPITAL("6",
                        "Old Market Hospital",
                        null,
                        "10",
                        "Hooplstrect",
                        "North Holland",
                        "Amsterdam",
                        "North Holland",
                        "Netherlands",
                        "UTC+1"),

    /**
     * Austin
     */
    AUSTIN_SITE("7",
                "Austin",
                null,
                "10000",
                "38th St",
                "Austin",
                "Austin",
                "TX",
                "USA",
                "UTC-6"),

    /**
     * Winchester
     */
    WINCHESTER_SITE("8",
                    "Winchester",
                    null,
                    "1",
                    "Eagles Nest",
                    "",
                    "Winchester",
                    "Hampshire",
                    "UK",
                    "UTC"),

    /**
     * Kansas City
     */
    KANSAS_CITY_SITE("9",
                "Kansas City",
                "Coco Distribution Center",
                "1200",
                "Industrial Parkway",
                "",
                "Kansas City",
                "KS",
                "USA",
                "UTC-6"),

    /**
     * Edmonton
     */
    EDMONTON_SITE("10",
                  "Edmonton",
                  null,
                  "10828",
                  "102 Ave NW",
                  "",
                  "Edmonton",
                  "Alberta",
                  "Canada",
                  "UTC-8"),
    ;

    public static final String validValueSetName = "WorkLocation";
    public static final String validValueSetPropertyName = "workLocation";
    public static final String validValueSetDescription = "Describes the reason behind a team's formation.";
    public static final String validValueSetUsage = "Used as a tag to identify an employee's primary work location.";
    public static final String validValueSetScope = "Used for all types of people associated with Coco Pharmaceuticals.";

    private static final String BUILDING_NAME_PROPERTY = "buildingName";
    private static final String STREET_NUMBER_PROPERTY = "streetNumber";
    private static final String STREET_NAME_PROPERTY = "streetName";
    private static final String DISTRICT_NAME_PROPERTY = "district";
    private static final String CITY_NAME_PROPERTY = "city";
    private static final String AREA_NAME_PROPERTY = "area";
    private static final String COUNTRY_NAME_PROPERTY = "country";

    private final String workLocationId;
    private final String displayName;
    private final String buildingName;
    private final String streetNumber;
    private final String streetName;
    private final String district;
    private final String city;
    private final String area;
    private final String country;
    private final String timeZone;


    /**
     * The constructor creates an instance of the enum
     *
     * @param workLocationId identifier use in data sets to link to ths location
     * @param displayName name of the location
     * @param buildingName building if part of a larger site
     * @param streetNumber street number of postal address
     * @param streetName street name of postal address
     * @param district district of postal address
     * @param city city of postal address
     * @param area area of postal address
     * @param country country where located
     */
    WorkLocationDefinition(String                 workLocationId,
                           String                 displayName,
                           String                 buildingName,
                           String                 streetNumber,
                           String                 streetName,
                           String                 district,
                           String                 city,
                           String                 area,
                           String                 country,
                           String                 timeZone)
    {
        this.workLocationId = workLocationId;
        this.displayName = displayName;
        this.buildingName = buildingName;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.district = district;
        this.city = city;
        this.area = area;
        this.country = country;
        this.timeZone = timeZone;
    }

    public String getQualifiedName()
    {
        return WorkLocationDefinition.validValueSetName + "." + workLocationId;
    }

    /**
     * Return the standard value for work location.
     *
     * @return string id
     */
    public String getWorkLocationId()
    {
        return workLocationId;
    }


    /**
     * Return the common name for the work location.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the postal address of the location.
     *
     * @return string address (single line)
     */
    public String getPostalAddress()
    {
        String postalAddress = null;

        if (buildingName != null)
        {
            postalAddress = buildingName + ", ";
        }

        if (streetNumber != null)
        {
            postalAddress = postalAddress + streetNumber + " ";
        }

        if (streetName != null)
        {
            postalAddress = postalAddress + streetName + ", ";
        }

        if (district != null)
        {
            postalAddress = postalAddress + district + ", ";
        }

        if (city != null)
        {
            postalAddress = postalAddress + city + ", ";
        }

        if (area != null)
        {
            postalAddress = postalAddress + area + ", ";
        }

        if (country != null)
        {
            postalAddress = postalAddress + country;
        }

        return postalAddress;
    }


    /**
     * Return the individual segments of the address stores as a string map.
     *
     * @return map
     */
    public Map<String, String> getAddressProperties()
    {
        Map<String, String> addressProperties = new HashMap<>();

        addressProperties.put(BUILDING_NAME_PROPERTY, buildingName);
        addressProperties.put(STREET_NUMBER_PROPERTY, streetNumber);
        addressProperties.put(STREET_NAME_PROPERTY, streetName);
        addressProperties.put(DISTRICT_NAME_PROPERTY, district);
        addressProperties.put(CITY_NAME_PROPERTY, city);
        addressProperties.put(AREA_NAME_PROPERTY, area);
        addressProperties.put(COUNTRY_NAME_PROPERTY, country);

        return addressProperties;
    }


    /**
     * Return the name of the time zone.
     *
     * @return string name
     */
    public String getTimeZone()
    {
        return timeZone;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "WorkLocation{" + displayName + '}';
    }
}
