/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenLineageCataloguerConfigurationProperty provides some standard definitions for configuration properties used to
 * control the amount of metadata that the Open Lineage Cataloguer integration connector creates as it receives
 * OpenLineage events.  Each property is a boolean that switches a class of metadata on or off.  The lineage
 * (processes, data assets and the data flows between them) and the run metrics on the processes are always catalogued.
 */
public enum OpenLineageCataloguerConfigurationProperty
{
    /**
     * Create a TransientEmbeddedProcess element for each run of a job (linked to the job's process via ProcessHierarchy).
     */
    CATALOG_RUNS("catalogRuns",
                 "Create a TransientEmbeddedProcess element for each run of a job, owned by the job's process via a ProcessHierarchy relationship.  " +
                         "The run element records the status and timing of the run and the details of the run facets.  " +
                         "This is off by default because it creates a metadata element for every run; the run metrics are always " +
                         "maintained on the job's process regardless of this setting.",
                 "boolean",
                 "false",
                 false),

    /**
     * Create a schema (TabularSchemaType and TabularColumns) for a data asset from the schema dataset facet.
     */
    CATALOG_SCHEMAS("catalogSchemas",
                    "Create a schema (TabularSchemaType and TabularColumns) for a data asset from the schema dataset facet when the data asset has no schema.  " +
                            "The columns are needed for column-level lineage (LineageMapping relationships) to be catalogued.",
                    "boolean",
                    "true",
                    false),

    /**
     * Capture input/output statistics and data quality metrics as annotations in a survey report.
     */
    CAPTURE_STATISTICS("captureStatistics",
                       "Capture the inputStatistics, outputStatistics and dataQualityMetrics facets as ResourceProfileAnnotations in a SurveyReport " +
                               "created for the run and linked to the data asset.",
                       "boolean",
                       "true",
                       false),

    /**
     * Capture data quality assertions and test results as quality annotations in a survey report.
     */
    CAPTURE_DATA_QUALITY("captureDataQuality",
                         "Capture the dataQualityAssertions input facet and the test run facet as QualityAnnotations in a SurveyReport " +
                                 "created for the run and linked to the data asset (or the process for the test run facet).",
                         "boolean",
                         "true",
                         false),

    /**
     * Maintain the DataScope classification on data assets written by the runs.
     */
    UPDATE_DATA_SCOPE("updateDataScope",
                      "Maintain the DataScope classification on the data assets written by each run.  The data collection start time is the " +
                              "first known write (restarted when the store is created, overwritten or truncated), the end time is the last known write, " +
                              "and the latest write statistics are recorded in the additional properties.",
                      "boolean",
                      "true",
                      false),

    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  example;
    public final boolean isPlaceholder;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     * @param isPlaceholder is this also used as a placeholder property?
     */
    OpenLineageCataloguerConfigurationProperty(String  name,
                                               String  description,
                                               String  dataType,
                                               String  example,
                                               boolean isPlaceholder)
    {
        this.name          = name;
        this.description   = description;
        this.dataType      = dataType;
        this.example       = example;
        this.isPlaceholder = isPlaceholder;
    }


    /**
     * Return the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the configuration property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the configuration property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the configuration property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Return whether this value is also used as a placeholder property.
     *
     * @return boolean
     */
    public boolean isPlaceholder()
    {
        return isPlaceholder;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (OpenLineageCataloguerConfigurationProperty configurationProperty : OpenLineageCataloguerConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }


    /**
     * Return the names of the defined configuration properties.
     *
     * @return list of names
     */
    public static List<String> getRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        for (OpenLineageCataloguerConfigurationProperty configurationProperty : OpenLineageCataloguerConfigurationProperty.values())
        {
            recognizedConfigurationProperties.add(configurationProperty.getName());
        }

        return recognizedConfigurationProperties;
    }


    /**
     * Return the configuration property type for this property.
     *
     * @return configuration property type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType configurationPropertyType = new ConfigurationPropertyType();

        configurationPropertyType.setName(name);
        configurationPropertyType.setDescription(description);
        configurationPropertyType.setDataType(dataType);
        configurationPropertyType.setExample(example);
        configurationPropertyType.setRequired(isPlaceholder);

        return configurationPropertyType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "OpenLineageCataloguerConfigurationProperty{ name=" + name + "}";
    }
}
