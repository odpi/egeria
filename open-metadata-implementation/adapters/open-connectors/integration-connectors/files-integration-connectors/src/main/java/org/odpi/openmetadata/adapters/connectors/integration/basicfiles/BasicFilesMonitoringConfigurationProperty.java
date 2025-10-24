/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;


import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * BasicFilesMonitoringConfigurationProperty provides some standard definitions for configuration properties used to
 * pass properties
 * to connectors when they run.  Using standard names for request parameters wherever possible
 * helps to simplify the integration of governance services.
 */
public enum BasicFilesMonitoringConfigurationProperty
{
    NEW_FILE_PROCESS_NAME  ("newFileProcessName",
                            "Qualified name of the process to run each time a new file is detected and catalogued.",
                            "string",
                            "Coco:GovernanceActionProcess:ClinicalTrials:WeeklyMeasurements:Onboarding"),
    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     */
    BasicFilesMonitoringConfigurationProperty(String name,
                                              String description,
                                              String dataType,
                                              String example)
    {
        this.name        = name;
        this.description = description;
        this.dataType    = dataType;
        this.example     = example;
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
     * Return the description of the request parameter.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the request parameter.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the request parameter to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (BasicFilesMonitoringConfigurationProperty configurationProperty : BasicFilesMonitoringConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }



    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return request parameter type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType requestParameterType = new ConfigurationPropertyType();

        requestParameterType.setName(name);
        requestParameterType.setDescription(description);
        requestParameterType.setDataType(dataType);
        requestParameterType.setExample(example);

        return requestParameterType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "MoveCopyFileRequestParameter{ name=" + name + "}";
    }
}
