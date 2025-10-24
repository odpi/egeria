/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


/**
 * The FacilityTypeDefinition is used to feed the definition of the FacilityTypes valid value set for
 * Coco Pharmaceuticals' sustainability program.
 */
public enum FacilityTypeDefinition
{
    /**
     * Primary Site
     */
    SITE("Site",
         "Primary Site",
         "Primary location containing multiple facilities within it.",
         "These are the organization's primary sites."),

    /**
     * Research Laboratory
     */
    LAB("Lab",
        "Research Laboratory",
        "Location where product research is conducted involving chemical and sample storage, experiments, candidate product creation/testing.",
        "Identify locations with lab facilities.  Do not include locations that focus on the analysis and office work associated with product development."),

    /**
     * Office
     */
    OFFICE("Office",
           "Office",
           "Location where staff work at desks with computer equipment.",
           "Identify locations where the primary focus is office workers."),

    /**
     * Manufacturing Facility
     */
    FACTORY("Factory",
            "Manufacturing Facility",
            "Location where products are manufactured from supplied chemicals.",
            "Identify locations with manufacturing facilities.  Do not include locations that are able to create test batches as part of product development."),

    /**
     * Warehouse and Distribution Depot
     */
    DEPOT("Depot",
          "Warehouse and Distribution Depot",
          "Location where product research is conducted involving chemical and sample storage, experiments, data capture and data science.",
          "Identify locations with lab facilities.  Do not include locations that focus on the analysis and office work associated with product development."),

    /**
     * Data centre/center
     */
    DATA_CENTER("Data centre",
          "Data centre/center",
          "Location where shared computing resource is housed and managed from.",
          "Identify locations with shared computer facilities.  Do not include locations where individuals are using their laptops and other machines allocated to them personally."),

    /**
     * Cloud provider
     */
    CLOUD("External cloud service",
          "Cloud provider",
          "Digital location provided by an external cloud vendor.",
          "Identify digital services and systems not managed by Coco Pharmaceuticals."),
    ;

    public static final String validValueSetName = "FacilityType";
    public static final String validValueSetPropertyName = "facilityType";
    public static final String validValueSetDescription = "Describes the type of facility at a physical location.";
    public static final String validValueSetUsage = "Used to tag Location entities to show the likely equipment that needs cataloging and managing.";
    public static final String validValueSetScope = "Used for physical types of Locations.";

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
    FacilityTypeDefinition(String preferredValue, String displayName, String description, String usage)
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
    public String getQualifiedName()
    {
        return validValueSetName + "." + preferredValue;
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
        return "FacilityTypeDefinition{" + displayName + '}';
    }
}
