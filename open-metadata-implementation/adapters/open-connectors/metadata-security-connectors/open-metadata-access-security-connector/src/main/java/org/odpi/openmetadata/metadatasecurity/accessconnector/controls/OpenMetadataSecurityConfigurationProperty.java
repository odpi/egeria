/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.accessconnector.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenMetadataSecurityConfigurationProperty provides definitions for the configuration properties used with the Open Metadata Access Security connector.
 */
public enum OpenMetadataSecurityConfigurationProperty
{
    /**
     * Name of the list of users/roles that are permitted to configure OMAG servers using the OMAG Server Platform's administration services.
     */
    SERVER_ADMINISTRATOR_CONTROL("serverAdministratorControlName",
                                 "Name of the list of users/roles that are permitted to configure OMAG servers using the OMAG Server Platform's administration services.",
                                 "string",
                                 "admin-services",
                                 false),

    /**
     * Name of the list of users/roles that are permitted to start and stop OMAG Servers on an OMAG Server Platform.
     */
    SERVER_OPERATORS_CONTROL("serverOperatorsControlName",
                             "Name of the list of users/roles that are permitted to start and stop OMAG Servers on an OMAG Server Platform through the Server Operations Services.",
                             "string",
                             "platform-services",
                             false),

    /**
     * Name of the list of users/roles that are permitted to start and stop OMAG Servers on an OMAG Server Platform.
     */
    SERVER_INVESTIGATORS_CONTROL("serverInvestigatorsControlName",
                                 "Name of the list of users/roles that are permitted to query the status of OMAG Servers on an OMAG Server Platform through the Server Operations Services.",
                                 "string",
                                 "server-operations",
                                 false),

    /**
     * A dynamic group that includes all users with an active account.
     */
    ALL_USERS_GROUP("allUsersGroupName",
                    "A dynamic group that includes all users with an active account.",
                    "string",
                    "allUsers",
                    false),

    /**
     * A dynamic group that includes the owners of the element.
     */
    INSTANCE_OWNER_GROUP("instanceOwnerGroupName",
                         "A dynamic group that includes the owners of the element.",
                         "string",
                         "instanceOwner",
                         false),

    /**
     * Name of a dynamic group that includes all the existing maintainers of the element.
     */
    EXISTING_MAINTAINER_GROUP("existingMaintainerGroupName",
                              "Name of a dynamic group that includes all the existing maintainers of the element.",
                              "string",
                              "existingMaintainer",
                              false),

    /**
     * Name of the dynamic group that supports the policy that the user must not be in the existing list of maintainers for the element.
     */
    NEW_MAINTAINER_GROUP("newMaintainerGroupName",
                         "Name of the dynamic group that supports the policy that the user must not be in the existing list of maintainers for the element.",
                         "string",
                         "newMaintainer",
                         false),
    /**
     * Name of the dynamic group that includes all contactors with a valid account.
     */
    CONTRACTORS_GROUP("contractorsGroupName",
                      "Name of the dynamic group that includes all contactors with a valid account.",
                      "string",
                      "contractUsers",
                      false),

    /**
     * Name of the dynamic group that includes all digital (automated process) users with a valid account.
     */
    DIGITAL_USERS_GROUP("digitalUsersGroupName",
                        "Name of the dynamic group that includes all digital (automated process) users with a valid account.",
                        "string",
                        "digitalUsers",
                        false),

    /**
     * Name of the dynamic group that includes all employees with a valid account.
     */
    EMPLOYEES_GROUP("employeeOnlyZonesAccessGroupName",
                    "Name of the dynamic group that includes all employees with a valid account.",
                    "string",
                    "employeeUsers",
                    false),


    /**
     * Name of the dynamic group that includes all external users with a valid account.
     */
    EXTERNAL_USERS_GROUP("externalUsersGroupName",
                         "Name of the dynamic group that includes all external users with a valid account.",
                         "string",
                         "externalUsers",
                         false),

    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  defaultValue;
    public final boolean isPlaceholder;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType data type of the request parameter
     * @param defaultValue example of the request parameter - this is also its default value
     * @param isPlaceholder is this also used as a placeholder property?
     */
    OpenMetadataSecurityConfigurationProperty(String  name,
                                              String  description,
                                              String  dataType,
                                              String  defaultValue,
                                              boolean isPlaceholder)
    {
        this.name          = name;
        this.description   = description;
        this.dataType      = dataType;
        this.defaultValue  = defaultValue;
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
    public String getDefaultValue()
    {
        return defaultValue;
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
    public static List<String> getRecognizedConfigurationPropertyTypes()
    {
        List<String> configurationProperties = new ArrayList<>();

        for (OpenMetadataSecurityConfigurationProperty configurationProperty : OpenMetadataSecurityConfigurationProperty.values())
        {
            configurationProperties.add(configurationProperty.getName());
        }

        return configurationProperties;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (OpenMetadataSecurityConfigurationProperty configurationProperty : OpenMetadataSecurityConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in a connector provider.
     *
     * @return request parameter type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType configurationPropertyType = new ConfigurationPropertyType();

        configurationPropertyType.setName(name);
        configurationPropertyType.setDescription(description);
        configurationPropertyType.setDataType(dataType);
        configurationPropertyType.setExample(defaultValue);
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
        return "OpenMetadataSecurityConfigurationProperty{ name=" + name + "}";
    }
}
