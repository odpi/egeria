/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control;


import org.odpi.openmetadata.frameworks.governanceaction.controls.PlaceholderPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum OMAGServerPlatformPlaceholderProperty
{
    /**
     * The host IP address or domain name.
     */
    HOST_URL ("platformHostURL", "The host IP address or domain name of the platform with the HTTP protocol prefix.", "string", "https://coconet.com"),

    /**
     * The number of the port to use to connect to a service.
     */
    PORT_NUMBER ("platformPortNumber", "The number of the port to use to connect to the platform.", "string", "9443"),

    /**
     * The userId to store in the userId attribute of the connection.
     */
    CONNECTION_USER_ID ("platformUserId",
                      "The userId to store in the userId attribute of the connection. This is a platform user that is defined to the platform security connector and is used when making REST API calls to the platform.",
                      "string",
                      "garygeeke"),


    /**
     * The name of the server being catalogued.
     */
    PLATFORM_NAME ("platformName", "The name of the platform being catalogued.", "string", "Default OMAG Server Platform"),

    /**
     * The description of the server being catalogued.
     */
    PLATFORM_DESCRIPTION ("platformDescription", "The description of the platform being catalogued.", "string", null),

    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the placeholder property
     * @param description description of the placeholder property
     * @param dataType type of value of the placeholder property
     * @param example example of the placeholder property
     */
    OMAGServerPlatformPlaceholderProperty(String name,
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
     * Return the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the placeholder to use when building templates.
     *
     * @return placeholder property
     */
    public String getPlaceholder()
    {
        return "{{" + name + "}}";
    }


    /**
     * Return the description of the placeholder property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the placeholder property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the placeholder property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        for (OMAGServerPlatformPlaceholderProperty placeholderProperty : OMAGServerPlatformPlaceholderProperty.values())
        {
            placeholderPropertyTypes.add(placeholderProperty.getPlaceholderType());
        }

        return placeholderPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return placeholder property type
     */
    public PlaceholderPropertyType getPlaceholderType()
    {
        PlaceholderPropertyType placeholderPropertyType = new PlaceholderPropertyType();

        placeholderPropertyType.setName(name);
        placeholderPropertyType.setDescription(description);
        placeholderPropertyType.setDataType(dataType);
        placeholderPropertyType.setExample(example);
        placeholderPropertyType.setRequired(true);

        return placeholderPropertyType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PlaceholderProperty{ name=" + name + "}";
    }
}
