/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents one allowed value in the enumeration of a schema property (ODCS v3.2.0, RFC 0033).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractEnumValue
{
    private Object                             value = null;
    private String                             label = null;
    private String                             id = null;
    private String                             description = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public DataContractEnumValue()
    {
    }


    /**
     * Return the allowed value.
     *
     * @return Object
     */
    public Object getValue()
    {
        return value;
    }


    /**
     * Set up the allowed value.
     *
     * @param value Object
     */
    public void setValue(Object value)
    {
        this.value = value;
    }


    /**
     * Return the human-readable label for the value.
     *
     * @return String
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set up the human-readable label for the value.
     *
     * @param label String
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Return the stable identifier of the value.
     *
     * @return String
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of the value.
     *
     * @param id String
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the description of what the value represents.
     *
     * @return String
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of what the value represents.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the tags attached to the value.
     *
     * @return List<String>
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the tags attached to the value.
     *
     * @param tags List<String>
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the custom properties of the value, for example translations.
     *
     * @return List<BitolCustomProperty>
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the custom properties of the value, for example translations.
     *
     * @param customProperties List<BitolCustomProperty>
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Return links to the sources that define the value.
     *
     * @return List<BitolAuthoritativeDefinition>
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up links to the sources that define the value.
     *
     * @param authoritativeDefinitions List<BitolAuthoritativeDefinition>
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractEnumValue{" +
                       "value=" + value +
                       ", label=" + label +
                       ", id=" + id +
                       ", description=" + description +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
                       '}';
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
        DataContractEnumValue that = (DataContractEnumValue) objectToCompare;
        return Objects.equals(value, that.value) &&
                       Objects.equals(label, that.label) &&
                       Objects.equals(id, that.id) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(value, label, id, description, tags, customProperties, authoritativeDefinitions);
    }
}
