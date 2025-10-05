/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataFeedProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TopicProperties is a class for representing a topic for an event broker or streaming service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TopicProperties extends DataFeedProperties
{
    private String topicType = null;
    private String topicName = null;

    /**
     * Default constructor
     */
    public TopicProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TOPIC.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public TopicProperties(TopicProperties template)
    {
        super(template);

        if (template != null)
        {
            topicType = template.getTopicType();
            topicName = template.getTopicName();
        }
    }


    /**
     * Return the type of topic.  Often this the type of data that can be placed on the topic.
     *
     * @return type name
     */
    public String getTopicType()
    {
        return topicType;
    }


    /**
     * Set up the type of topic.  Often this the type of data that can be placed on the topic.
     *
     * @param topicType type name
     */
    public void setTopicType(String topicType)
    {
        this.topicType = topicType;
    }


    /**
     * Return the name of topic.
     *
     * @return  name
     */
    public String getTopicName()
    {
        return topicName;
    }


    /**
     * Set up the name of topic.
     *
     * @param topicName  name
     */
    public void setTopicName(String topicName)
    {
        this.topicName = topicName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TopicProperties{" +
                "topicType='" + topicType + '\'' +
                ", topicName='" + topicName + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        TopicProperties that = (TopicProperties) objectToCompare;
        return Objects.equals(topicType, that.topicType) && Objects.equals(topicName, that.topicName);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), topicType, topicName);
    }
}
