/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * OMAGServerPlatformConfigurationProperty defines the configuration properties used with the OMAG Server Platform connectors.
 */
public enum ReferenceDataConfigurationProperty
{
    /**
     * Unique identifier of a server running on the platform.
     */
    SERVER_NAME ("serverName",
                 "Unique name of the Metadata Access Server where the metadata is stored.",
                 "string",
                 "cocoMDS1",
                 false),

    /**
     * Unique identifier of the top level metadata element that is supplying metadata.
     */
    TOP_LEVEL_GUID ("topLevelGUID",
                          "Unique identifier of the top level metadata element that is supplying metadata.",
                          "string",
                          "fc29b307-67a9-4ebc-a8e7-baed96612e52",
                          false),

    /**
     * Unique identifier of the element that is supplying metadata.
     */
    STARTING_ELEMENT_GUID("startingElementGUID",
                          "Unique identifier of the element that is supplying metadata.",
                          "string",
                          "fc29b307-67a9-4ebc-a8e7-baed96612e52",
                          false),


    /**
     * Value of the identifier property to process.
     */
    IDENTIFIER_PROPERTY_VALUE("identifierPropertyValue",
                              "Value of the identifier property to process.",
                              "string",
                              OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                              false),

    /**
     * Canonical name - name used for the product and table name.
     */
    CANONICAL_NAME("canonicalName",
                              "Value of the identifier property to process.",
                              "string",
                              "Deployed Implementation Type",
                              false),


    /**
     * Description for the digital product.
     */
    PRODUCT_DESCRIPTION("productDescription",
                   "Description for the digital product.",
                   "string",
                   OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description,
                   false),


    /**
     * Maximum values to retrieve from the metadata store in one call.
     */
    MAX_PAGE_SIZE ("maxPageSize",
                          "Maximum number of values to retrieve from the metadata store in one call.  Zero means retrieve the maximum allowed by the server.  This is the default value.",
                          "int",
                          "10",
                          false),


    /**
     * Unique identifier of the top-level team to supply department/team reference information.
     */
    TOP_LEVEL_TEAM_GUID ("topLevelTeamGUID",
                          "Unique identifier of the top-level team to supply department reference information.",
                          "string",
                          "aa86a76c-0a9d-46ac-b33d-d7f690f0206a",
                          false),

    /**
     * Unique identifier of the top-level location to supply department/team reference information.
     */
    TOP_LEVEL_LOCATION_GUID ("topLevelLocationGUID",
                         "Unique identifier of the top-level location to supply reference information.",
                         "string",
                         "0e8f518b-644e-4254-ac59-43ce90651e22a",
                         false),



    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;
    public final boolean          isPlaceholder;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     * @param isPlaceholder is this also used as a placeholder property?
     */
    ReferenceDataConfigurationProperty(String  name,
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
     * Get recognizedConfigurationProperties for the connector.
     *
     * @return list of property names
     */
    public static List<String> getRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        for (ReferenceDataConfigurationProperty configurationProperty : ReferenceDataConfigurationProperty.values())
        {
            recognizedConfigurationProperties.add(configurationProperty.getName());
        }
        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (ReferenceDataConfigurationProperty configurationProperty : ReferenceDataConfigurationProperty.values())
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
        return "ConfigurationProperty{ name=" + name + "}";
    }
}
