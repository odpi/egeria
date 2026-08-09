/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceMetadataProperties describes the properties for the InstanceMetadata classification that captures the metadata
 * type of a specific instance of data (such as the type of an embedded media file).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstanceMetadataProperties extends ClassificationBeanProperties
{
    private String              instanceMetadataTypeName = null;
    private String              description              = null;
    private Map<String, String> additionalProperties     = null;


    /**
     * Default constructor
     */
    public InstanceMetadataProperties()
    {
        super();
        super.typeName = OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InstanceMetadataProperties(InstanceMetadataProperties template)
    {
        super(template);

        if (template != null)
        {
            instanceMetadataTypeName = template.getInstanceMetadataTypeName();
            description              = template.getDescription();
            additionalProperties     = template.getAdditionalProperties();
        }
    }


    /**
     * Return the name of the metadata data type extracted from an instance.
     *
     * @return string name
     */
    public String getInstanceMetadataTypeName()
    {
        return instanceMetadataTypeName;
    }


    /**
     * Set up the name of the metadata data type extracted from an instance.
     *
     * @param instanceMetadataTypeName string name
     */
    public void setInstanceMetadataTypeName(String instanceMetadataTypeName)
    {
        this.instanceMetadataTypeName = instanceMetadataTypeName;
    }


    /**
     * Return the description of this instance metadata.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of this instance metadata.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the additional properties for the instance metadata.
     *
     * @return name-value pairs for additional values
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties for the instance metadata.
     *
     * @param additionalProperties name-value pairs for additional values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "InstanceMetadataProperties{" +
                "instanceMetadataTypeName='" + instanceMetadataTypeName + '\'' +
                ", description='" + description + '\'' +
                ", additionalProperties=" + additionalProperties +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        InstanceMetadataProperties that = (InstanceMetadataProperties) objectToCompare;
        return Objects.equals(instanceMetadataTypeName, that.instanceMetadataTypeName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), instanceMetadataTypeName, description, additionalProperties);
    }
}
