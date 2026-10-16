/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.controls;

import java.util.ArrayList;
import java.util.List;

/**
 * WSO2MIConfigurationProperty defines the configuration properties recognised by the WSO2 Micro
 * Integrator integration connector.
 */
public enum WSO2MIConfigurationProperty
{
    /**
     * A list of API names to exclude from cataloguing.  Overrides {@link #INCLUDE_API_LIST}.
     */
    EXCLUDE_API_LIST("excludeAPIList",
                     "A list of deployed API names to exclude from cataloguing.  If both an exclude and an include list are supplied, the exclude list wins.",
                     "array<string>",
                     null),

    /**
     * A list of API names to catalog.  If omitted, all deployed APIs are catalogued.
     */
    INCLUDE_API_LIST("includeAPIList",
                     "A list of deployed API names to catalog.  If omitted, all deployed APIs are catalogued (subject to the exclude list).",
                     "array<string>",
                     null),

    ;

    private final String name;
    private final String description;
    private final String dataType;
    private final String example;


    /**
     * Constructor.
     *
     * @param name        the name of the configuration property
     * @param description  a short description of what the property is for
     * @param dataType     the open metadata data type of the property
     * @param example      an example value
     */
    WSO2MIConfigurationProperty(String name, String description, String dataType, String example)
    {
        this.name        = name;
        this.description  = description;
        this.dataType     = dataType;
        this.example      = example;
    }


    /**
     * Return the name of the configuration property.
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
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the open metadata data type of the configuration property.
     *
     * @return string data type
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example value for the configuration property.
     *
     * @return string example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Return the names of the configuration properties recognised by the WSO2 Micro Integrator
     * integration connector.
     *
     * @return list of names
     */
    public static List<String> getWSO2MIIntegrationConnectorNames()
    {
        List<String> names = new ArrayList<>();

        for (WSO2MIConfigurationProperty property : WSO2MIConfigurationProperty.values())
        {
            names.add(property.getName());
        }

        return names;
    }
}
