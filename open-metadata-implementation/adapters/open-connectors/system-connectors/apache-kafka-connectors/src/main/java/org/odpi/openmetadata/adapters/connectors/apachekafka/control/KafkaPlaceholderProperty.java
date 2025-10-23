/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.control;


import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum KafkaPlaceholderProperty
{
    /**
     * The full name of the topic.
     */
    FULL_TOPIC_NAME ("fullTopicName", "The full name of the topic.", "string", "egeria.omag.server.active-metadata-store.omas.assetconsumer.outTopic"),

    /**
     * The display name of the topic.
     */
    SHORT_TOPIC_NAME ("shortTopicName", "The display name of the topic.", "string", "AssetConsumer.outTopic on active-metadata-store"),

    /**
     * By default, this connector supports both the receiving and sending of events on a particular topic. It is possible to turn off, either the ability to listen for events through the consumer, or send events through the producer. This is achieved by setting the eventDirection configuration property, which can be 'inOut' (default value), 'outOnly' or 'inOnly'.
     */
    EVENT_DIRECTION ("eventDirection", "By default, this connector supports both the receiving and sending of events on a particular topic. It is possible to turn off, either the ability to listen for events through the consumer, or send events through the producer. This is achieved by setting the eventDirection configuration property, which can be 'inOut' (default value), 'outOnly' or 'inOnly'.", "string", "outOnly"),
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
    KafkaPlaceholderProperty(String name,
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
     * Retrieve the Kafka Server defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getKafkaServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve the Kafka Topic defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getKafkaTopicPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        for (KafkaPlaceholderProperty placeholderProperty : KafkaPlaceholderProperty.values())
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
