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
 * This class represents the properties shared by the schema objects and schema properties of an Open Data
 * Contract Standard (ODCS) data contract. It is the base class for DataContractSchemaObject and
 * DataContractSchemaProperty.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractSchemaElement
{
    private String                             id = null;
    private String                             name = null;
    private String                             physicalName = null;
    private String                             physicalType = null;
    private String                             businessName = null;
    private String                             description = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public DataContractSchemaElement()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the name of the schema element.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the schema element.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the name of the schema element in the physical data source.
     *
     * @return string name
     */
    public String getPhysicalName()
    {
        return physicalName;
    }


    /**
     * Set up the name of the schema element in the physical data source.
     *
     * @param physicalName string name
     */
    public void setPhysicalName(String physicalName)
    {
        this.physicalName = physicalName;
    }


    /**
     * Return the data type of the schema element in the physical data source.
     *
     * @return string type name
     */
    public String getPhysicalType()
    {
        return physicalType;
    }


    /**
     * Set up the data type of the schema element in the physical data source.
     *
     * @param physicalType string type name
     */
    public void setPhysicalType(String physicalType)
    {
        this.physicalType = physicalType;
    }


    /**
     * Return the business name of the schema element.
     *
     * @return string name
     */
    public String getBusinessName()
    {
        return businessName;
    }


    /**
     * Set up the business name of the schema element.
     *
     * @param businessName string name
     */
    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }


    /**
     * Return the description of the schema element.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the schema element.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the list of tags attached to this element.
     *
     * @return list of tag strings
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the list of tags attached to this element.
     *
     * @param tags list of tag strings
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the list of custom (key/value) properties attached to this element.
     *
     * @return list of custom properties
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the list of custom (key/value) properties attached to this element.
     *
     * @param customProperties list of custom properties
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Return the list of links to sources that provide more details about this element.
     *
     * @return list of authoritative definitions
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up the list of links to sources that provide more details about this element.
     *
     * @param authoritativeDefinitions list of authoritative definitions
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
        return "DataContractSchemaElement{" +
                       "id='" + id + '\'' +
                       ", name='" + name + '\'' +
                       ", physicalName='" + physicalName + '\'' +
                       ", physicalType='" + physicalType + '\'' +
                       ", businessName='" + businessName + '\'' +
                       ", description='" + description + '\'' +
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
        DataContractSchemaElement that = (DataContractSchemaElement) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(physicalName, that.physicalName) &&
                       Objects.equals(physicalType, that.physicalType) &&
                       Objects.equals(businessName, that.businessName) &&
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
        return Objects.hash(id, name, physicalName, physicalType, businessName, description, tags, customProperties, authoritativeDefinitions);
    }
}
