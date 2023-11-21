/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;


import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * DataFieldLink describes the properties of a peer to peer relationship between data fields.
 */
public class DataFieldLink extends PropertyBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    private int                 relationshipEnd      = 0;
    private String              relationshipTypeName = null;
    private String              name                 = null;
    private String              description          = null;
    private int                 minCardinality       = 0;
    private int                 maxCardinality       = 0;
    private Map<String, String> additionalProperties = null;



    /**
     * Default constructor
     */
    public DataFieldLink()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataFieldLink(DataFieldLink template)
    {
        super(template);

        if (template != null)
        {
            relationshipEnd = template.getRelationshipEnd();
            relationshipTypeName = template.getRelationshipTypeName();
            name = template.getName();
            description = template.getDescription();
            minCardinality = template.getMinCardinality();
            maxCardinality = template.getMaxCardinality();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the logical end of the relationship.  Use 0 if this does not make sense.
     *
     * @return integer
     */
    public int getRelationshipEnd()
    {
        return relationshipEnd;
    }


    /**
     * Set up the logical end of the relationship.  Use 0 if this does not make sense.
     *
     * @param relationshipEnd integer
     */
    public void setRelationshipEnd(int relationshipEnd)
    {
        this.relationshipEnd = relationshipEnd;
    }


    /**
     * Return the name of this relationship between data fields.
     *
     * @return string name
     */
    public String getRelationshipTypeName()
    {
        return relationshipTypeName;
    }


    /**
     * Set up the name of this relationship between data fields.
     *
     * @param relationshipTypeName string name
     */
    public void setRelationshipTypeName(String relationshipTypeName)
    {
        this.relationshipTypeName = relationshipTypeName;
    }


    /**
     * Return the name of this data field link.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of this data field link.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return description of this data field link.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of this data field link.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return this minimum number of instances allowed for this data field link.
     *
     * @return int
     */
    public int getMinCardinality()
    {
        return minCardinality;
    }


    /**
     * Set up the minimum number of instances allowed for this data field link.
     *
     * @param minCardinality int
     */
    public void setMinCardinality(int minCardinality)
    {
        this.minCardinality = minCardinality;
    }


    /**
     * Return the maximum number of instances allowed for this data field link.
     *
     * @return int (-1 means infinite)
     */
    public int getMaxCardinality()
    {
        return maxCardinality;
    }


    /**
     * Set up the maximum number of instances allowed for this data field link.
     *
     * @param maxCardinality int (-1 means infinite)
     */
    public void setMaxCardinality(int maxCardinality)
    {
        this.maxCardinality = maxCardinality;
    }
    

    /**
     * Return any additional properties.
     *
     * @return map of property name to property value
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }

        return new HashMap<>(additionalProperties);
    }


    /**
     * Set up any additional properties.
     *
     * @param additionalProperties map of property name to property value
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataFieldLink{" +
                       "relationshipEnd=" + relationshipEnd +
                       ", relationshipTypeName='" + relationshipTypeName + '\'' +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", minCardinality=" + minCardinality +
                       ", maxCardinality=" + maxCardinality +
                       ", additionalProperties=" + additionalProperties +
                       ", headerVersion=" + getHeaderVersion() +
                       ", elementHeader=" + getElementHeader() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (! (objectToCompare instanceof DataFieldLink that))
        {
            return false;
        }
        return relationshipEnd == that.relationshipEnd &&
                       minCardinality == that.minCardinality &&
                       maxCardinality == that.maxCardinality &&
                       Objects.equals(relationshipTypeName, that.relationshipTypeName) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipEnd, relationshipTypeName, name, description, minCardinality, maxCardinality, additionalProperties);
    }
}
