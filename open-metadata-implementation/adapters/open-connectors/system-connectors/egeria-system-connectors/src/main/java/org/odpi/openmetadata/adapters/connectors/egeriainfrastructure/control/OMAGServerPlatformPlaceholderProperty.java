/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control;


import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;

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
    PLATFORM_URL_ROOT("platformURLRoot", "The host IP address or domain name of the platform with the HTTP protocol prefix and port.", "string", "https://coconet.com:9443"),

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
    PLATFORM_USER_ID("platformUserId",
                     "The userId to store in the userId attribute of the connection. This is a platform user that is defined to the platform security connector and is used when making REST API calls to the platform.",
                     "string",
                     "garygeeke"),


    /**
     * Location of the secrets store.
     */
    SECRETS_STORE (PlaceholderProperty.SECRETS_STORE.getName(),
                   PlaceholderProperty.SECRETS_STORE.getDescription(),
                   PlaceholderProperty.SECRETS_STORE.getDataType(),
                   PlaceholderProperty.SECRETS_STORE.getExample()),


    /**
     * Collection within the secrets store.
     */
    SECRETS_COLLECTION_NAME (PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(),
                             PlaceholderProperty.SECRETS_COLLECTION_NAME.getDescription(),
                             PlaceholderProperty.SECRETS_COLLECTION_NAME.getDataType(),
                             PlaceholderProperty.SECRETS_COLLECTION_NAME.getExample()),

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
        return "~{" + name + "}~";
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
    public static List<PlaceholderPropertyType> getPlatformPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(OMAGServerPlatformPlaceholderProperty.PLATFORM_URL_ROOT.getPlaceholderType());
        placeholderPropertyTypes.add(OMAGServerPlatformPlaceholderProperty.PLATFORM_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(OMAGServerPlatformPlaceholderProperty.PLATFORM_DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.ORGANIZATION_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_STORE.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    public static List<PlaceholderPropertyType> getOMAGServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(OMAGServerPlatformPlaceholderProperty.PLATFORM_URL_ROOT.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_STORE.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    public static List<PlaceholderPropertyType> getViewServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(OMAGServerPlatformPlaceholderProperty.PLATFORM_URL_ROOT.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_STORE.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholderType());

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
