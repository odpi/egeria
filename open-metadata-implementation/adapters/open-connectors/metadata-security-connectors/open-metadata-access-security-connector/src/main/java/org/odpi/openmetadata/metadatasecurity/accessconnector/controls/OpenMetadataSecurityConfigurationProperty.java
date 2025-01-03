/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.accessconnector.controls;

import org.odpi.openmetadata.frameworks.connectors.controls.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenMetadataSecurityConfigurationProperty provides definitions for the configuration properties used with the Open Metadata Access Security connector.
 */
public enum OpenMetadataSecurityConfigurationProperty
{
    /**
     * Provide a comma-separated list of zone names that should be added to an Asset when it is created.
     */
    DEFAULT_ZONE_MEMBERSHIP ("defaultZoneMembership",
                             "Provide a comma-separated list of zone names that should be added to an Asset when it is created.",
                             "array<string>",
                             "quarantine",
                             false),

    /**
     * Name of the list of users/roles that are permitted to configure OMAG servers using the OMAG Server Platform's administration services.
     */
    SERVER_ADMINISTRATOR_GROUP ("serverAdministratorGroup",
                                "Name of the list of users/roles that are permitted to configure OMAG servers using the OMAG Server Platform's administration services.",
                                "string",
                                "group server administrators",
                                false),

    /**
     * Name of the list of users/roles that are permitted to start and stop OMAG Servers on an OMAG Server Platform.
     */
    SERVER_OPERATORS_GROUP ("serverOperatorsGroup",
                            "Name of the list of users/roles that are permitted to start and stop OMAG Servers on an OMAG Server Platform through the Server Operations Services.",
                            "string",
                            "group server operators",
                            false),

    /**
     * Name of the list of users/roles that are permitted to start and stop OMAG Servers on an OMAG Server Platform.
     */
    SERVER_INVESTIGATORS_GROUP ("serverInvestigatorsGroup",
                                "Name of the list of users/roles that are permitted to query the status of OMAG Servers on an OMAG Server Platform through the Server Operations Services.",
                                "string",
                                "group server investigators",
                                false),

    /**
     * Name of the list of users/roles that are permitted to modify the open metadata type definitions in a Metadata Access Server.
     */
    DYNAMIC_TYPE_AUTHOR_GROUP("dynamicTypeAuthorGroupName",
                                "Name of the list of users/roles that are permitted to modify the open metadata type definitions in a Metadata Access Server.",
                                "string",
                                "group dynamic type author",
                                false),

    /**
     * Name of the list of users/roles that are permitted to modify the header information for an existing open metadata instance in the Open Metadata Ecosystem.
     */
    INSTANCE_HEADER_AUTHOR_GROUP("instanceHeaderAuthorGroupName",
                              "Name of the list of users/roles that are permitted to modify the header information for an existing open metadata instance in the Open Metadata Ecosystem.",
                              "string",
                              "group instance header author",
                              false),

    /**
     * Provide a string pattern (for MessageFormat.format()) to convert a server name {0} into the group name to use to look up whether a user has access to a server's functions.
     */
    SERVER_GROUP_NAME_PATTERN("serverGroupNamePattern",
                              "Provide a string pattern (for MessageFormat.format()) to convert a server name {0} into the group name to use to look up whether a user has access to a server's functions.",
                              "string",
                              "group omag-server {0}",
                              false),

    /**
     * Provide a string pattern (for MessageFormat.format()) Provide a string pattern (for MessageFormat.format()) to convert a server name {0} and service name {1} into the group name to use to look up whether a user has access to a specific server's service.
     */
    SERVER_SERVICE_GROUP_NAME_PATTERN("serverServiceGroupNamePattern",
                                      "Provide a string pattern (for MessageFormat.format()) to convert a server name {0} and service name {1} into the group name to use to look up whether a user has access to a specific server's service.",
                                      "string",
                                      "group server service {0} - {1}",
                                      false),

    /**
     * Provide a string pattern (for MessageFormat.format()) to convert a server name {0}, a service name {1} and an operation {2} into the group name to use to look up whether a user has access to a specific server's service operation (method).
     */
    SERVER_SERVICE_OPERATION_GROUP_NAME_PATTERN("serverServiceOperationGroupNamePattern",
                                      "Provide a string pattern (for MessageFormat.format()) to convert a server name {0}, a service name {1} and an operation {2} into the group name to use to look up whether a user has access to a specific server's service operation (method).",
                                      "string",
                                      "group server service operation {0} - {1} - {2}",
                                      false),


    /**
     * Provide a string pattern (for MessageFormat.format()) to convert a governance zone name into the group name to use to look up whether a user has access to assets in the zone.
     */
    ZONE_GROUP_NAME_PATTERN("zoneGroupNamePattern",
                              "Provide a string pattern (for MessageFormat.format()) to convert a governance zone name {0} and operation {1} into the group name to use to look up whether a user has access to assets in the zone.",
                              "string",
                              "group {0} zone",
                              false),

    /**
     * Provide a string pattern (for MessageFormat.format()) to convert an element's type {0}, qualified name {1} and the operation {2} into the group name to use to look up whether a user has access to the element.
     */
    ELEMENT_GROUP_NAME_PATTERN("elementGroupNamePattern",
                               "Provide a string pattern (for MessageFormat.format()) to convert an element's type {0}, qualified name {1} and the operation {2} into the group name to use to look up whether a user has access to the element.",
                               "string",
                               "group element {1} - {2}",
                               false),

    /**
     * Provide a string pattern (for MessageFormat.format()) to convert an owner's name {0}, type name {1} and property name {2} into the group name to use to look up whether a user is the owner of an element.
     */
    OWNER_GROUP_NAME_PATTERN("ownerGroupNamePattern",
                             "Provide a string pattern (for MessageFormat.format()) to convert an owner's name {0}, type name {1} and property name {2} into the group name to use to look up whether a user is the owner of an element.",
                             "string",
                             "group owner {0}",
                             false),

    /**
     * A list of governance zones that require a different user from the creator to move a member asset out of the zone.
     */
    VALIDATION_ZONES_GROUP("validationZonesGroupName",
                           "A list of governance zones that require a different user from the creator to move a member asset out of the zone.",
                           "string",
                           "policy group validation zones",
                           false),

    /**
     * A list of governance zones that only allow update and access to the creator of the member assets.
     */
    PERSONAL_ZONES_GROUP("personalZonesGroupName",
                         "A list of governance zones that only allow update and access to the creator of the member assets.",
                         "string",
                         "policy group personal zones",
                         false),

    /**
     * A list of governance zones where only the owner of a member asset may read or maintain the asset.
     */
    STEWARDSHIP_ZONES_GROUP("stewardshipZonesGroupName",
                            "A list of governance zones where only the owner of a member asset may read or maintain the asset.",
                            "string",
                            "policy group stewardship zones",
                            false),

    /**
     * A list of governance zones where all user have read access.  Maintenance (create, update, delete) requires explicit access.
     */
    READABLE_ZONES_GROUP("readableZonesAccessGroupName",
                         "A list of governance zones where all user have read access.",
                         "string",
                         "access group data lake zones",
                         false),

    /**
     * A list of governance zones where only automated processes may read or maintain the member assets.
     */
    AUTOMATED_ZONES_GROUP("automatedZonesAccessGroupName",
                          "A list of governance zones where only automated processes may read or maintain the member assets.",
                          "string",
                          "access group automated zones",
                          false),

    /**
     * A list of governance zones where any defined user may read the member assets.
     */
    ALL_USER_ZONES_GROUP("allUsersZonesAccessGroupName",
                          "A list of governance zones where any defined user may read the member assets.",
                          "string",
                          "access group all user zones",
                          false),


    /**
     * A list of governance zones where defined user with an account type of EMPLOYEE may read or maintain the member assets.
     */
    EMPLOYEE_ONLY_ZONES_GROUP("employeeOnlyZonesAccessGroupName",
                              "A list of governance zones where any defined user with an account type of EMPLOYEE may read the member assets.",
                              "string",
                              "access group employee only user zones",
                              false),


    /**
     * A list of governance zones where defined user with an account type of EMPLOYEE or CONTRACTOR may read the member assets.
     */
    NOT_EXTERNAL_ZONES_GROUP("notExternalZonesAccessGroupName",
                             "A list of governance zones where any defined user with an account type of EMPLOYEE or CONTRACTOR may read the member assets.",
                             "string",
                             "access group non external user zones",
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
     * @param dataType type of value of the request parameter
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
        return "ConfigurationProperty{ name=" + name + "}";
    }
}
