/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


/**
 * The ProductCategoryDefinition is used to define the common categories used by Coco Pharmaceuticals.
 */
public enum ProductCategoryDefinition
{
    /**
     * Part of Coco Pharmaceuticals' digital product catalog.
     */
    DIGITAL_PRODUCT_CATALOG ("Digital Product Catalog","Part of Coco Pharmaceuticals' digital product catalog."),

    /**
     * Support for clinical trials.
     */
    CLINICAL_TRIALS ("Clinical Trials","Support for clinical trials."),

    /**
     * Common values for code tables.
     */
    REFERENCE_DATA ("Reference Data","Common values for code tables."),

    /**
     * Common values for code tables.
     */
    MASTER_DATA ("Master Data","Core entities that enable linkage across many activities."),

    /**
     * Regular publishing of summaries, analysis and other insights
     */
    INSIGHT_NOTIFICATIONS ("Insight Notifications","Regular publishing of summaries, analysis and other insights."),

    /**
     * Publishing of insights from the surveys from the open survey framework.  These surveys may be run as engine actions in the Engine Host, or run in an external surveying process that published results through the Data Discovery API.
     */
    SURVEY_REPORTS ("Survey Reports","Publishing of insights from the surveys from the open survey framework.  These surveys may be run as engine actions in the Engine Host, or run in an external surveying process that published results through the Data Discovery API."),

    /**
     * Details of the open metadata types in operation.
     */
    OPEN_METADATA_TYPES ("Open Metadata Types","Details of the open metadata types in operation."),

    /**
     * Details of the security definitions in operation.
     */
    SECURITY ("Security Definitions","Details of the security definitions in operation."),

    /**
     * Details of the governance definitions and results.
     */
    GOVERNANCE ("Governance","Details of the governance definitions and results."),


    /**
     * Details about the IT Infrastructure catalogued in open metadata.
     */
    IT_INFRASTRUCTURE ("Security Definitions","Details about the IT Infrastructure catalogued in open metadata"),


    ;



    private final String preferredValue;
    private final String displayName;


    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param displayName   name for the enum
     */
    ProductCategoryDefinition(String preferredValue, String displayName)
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
        return "Category{" + displayName + '}';
    }
}
