/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributeRelationship defines a relationship between 2 SchemaAttributes.
 * It is used in network type schemas such as a graph
 * or to show relationships such as foreign key relationships in relational schemas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaAttributeRelationship extends PropertyBase
{
    private static final long     serialVersionUID = 1L;

    private String              linkType            = null;
    private String              linkedAttributeGUID = null;
    private String              linkedAttributeName = null;
    private Map<String, Object> linkProperties      = null;


    /**
     * Default constructor
     */
    public SchemaAttributeRelationship()
    {
        super();
    }


    /**
     * Copy/clone constructor   makes a copy of the supplied object.
     *
     * @param template template object to copy
     */
    public SchemaAttributeRelationship(SchemaAttributeRelationship template)
    {
        super(template);

        if (template != null)
        {
            linkType = template.getLinkType();
            linkedAttributeGUID = template.getLinkedAttributeGUID();
            linkedAttributeName = template.getLinkedAttributeName();
            linkProperties = template.getLinkProperties();
        }
    }


    /**
     * Return the type of the link - this is related to the type of the schema it is a part of.
     *
     * @return String link type
     */
    public String getLinkType()
    {
        return linkType;
    }


    /**
     * Set up the type of the link - this is related to the type of the schema it is a part of.
     *
     * @param linkType String link type
     */
    public void setLinkType(String linkType)
    {
        this.linkType = linkType;
    }


    /**
     * Return the GUID of the schema attribute that this link connects together.
     *
     * @return  GUID for the attribute at the other end of the link.
     */
    public String getLinkedAttributeGUID()
    {
        return linkedAttributeGUID;
    }


    /**
     * Set up the GUIDs of the schema attributes that this link connects together.
     *
     * @param linkedAttributeGUID GUIDs for either end of the link - returned as a list.
     */
    public void setLinkedAttributeGUID(String linkedAttributeGUID)
    {
        this.linkedAttributeGUID = linkedAttributeGUID;
    }


    /**
     * Return the name of the far end of the relationship.
     *
     * @return String name
     */
    public String getLinkedAttributeName()
    {
        return linkedAttributeName;
    }


    /**
     * Set up the name of the far end of the relationship.
     *
     * @param linkedAttributeName String name
     */
    public void setLinkedAttributeName(String linkedAttributeName)
    {
        this.linkedAttributeName = linkedAttributeName;
    }


    /**
     * Return the properties associated with this schema link.
     *
     * @return property map
     */
    public Map<String, Object> getLinkProperties()
    {
        if (linkProperties == null)
        {
            return null;
        }
        else if (linkProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(linkProperties);
        }
    }


    /**
     * Set up the properties associated with this schema link.
     *
     * @param linkProperties property map
     */
    public void setLinkProperties(Map<String, Object> linkProperties)
    {
        this.linkProperties = linkProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaAttributeRelationship{" +
                "linkType='" + linkType + '\'' +
                ", linkedAttributeGUID='" + linkedAttributeGUID + '\'' +
                ", linkedAttributeName='" + linkedAttributeName + '\'' +
                ", linkProperties=" + linkProperties +
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
        if (!(objectToCompare instanceof SchemaAttributeRelationship))
        {
            return false;
        }
        SchemaAttributeRelationship that = (SchemaAttributeRelationship) objectToCompare;
        return Objects.equals(getLinkType(), that.getLinkType()) &&
                Objects.equals(getLinkedAttributeName(), that.getLinkedAttributeName()) &&
                Objects.equals(getLinkProperties(), that.getLinkProperties()) &&
                Objects.equals(getLinkedAttributeGUID(), that.getLinkedAttributeGUID());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getLinkType(),
                            getLinkedAttributeName(),
                            getLinkProperties(),
                            getLinkedAttributeGUID());
    }

}