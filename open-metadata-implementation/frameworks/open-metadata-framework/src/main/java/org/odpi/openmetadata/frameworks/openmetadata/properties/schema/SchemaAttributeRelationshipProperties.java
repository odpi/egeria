/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributeRelationshipProperties defines a relationship between 2 SchemaAttributes.
 * It is used in network type schemas such as a graph
 * or to show relationships such as foreign key relationships in relational schemas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaAttributeRelationshipProperties extends RelationshipProperties
{
    private String              linkGUID            = null;
    private String              linkType            = null;
    private String              linkedAttributeName = null;
    private Map<String, Object> linkProperties      = null;
    private String              linkedAttributeGUID = null;


    /**
     * Default constructor
     */
    public SchemaAttributeRelationshipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor   makes a copy of the supplied object.
     *
     * @param template template object to copy
     */
    public SchemaAttributeRelationshipProperties(SchemaAttributeRelationshipProperties template)
    {
        super(template);

        if (template != null)
        {
            linkGUID = template.getLinkGUID();
            linkedAttributeName = template.getLinkedAttributeName();
            linkType = template.getLinkType();
            linkProperties = template.getLinkProperties();
            linkedAttributeGUID = template.getLinkedAttributeGUID();
        }
    }


    /**
     * Return the identifier for the schema link.
     *
     * @return String guid
     */
    public String getLinkGUID()
    {
        return linkGUID;
    }


    /**
     * Set up the identifier for the schema link.
     *
     * @param linkGUID String guid
     */
    public void setLinkGUID(String linkGUID)
    {
        this.linkGUID = linkGUID;
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
     * Return the name of this link
     *
     * @return String name
     */
    public String getLinkedAttributeName()
    {
        return linkedAttributeName;
    }


    /**
     * Set up the name of this link.
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
        return linkProperties;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaAttributeRelationshipProperties{" +
                "linkGUID='" + linkGUID + '\'' +
                ", linkType='" + linkType + '\'' +
                ", linkedAttributeName='" + linkedAttributeName + '\'' +
                ", linkProperties=" + linkProperties +
                ", linkedAttributeGUID='" + linkedAttributeGUID + '\'' +
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
        if (!(objectToCompare instanceof SchemaAttributeRelationshipProperties that))
        {
            return false;
        }
        if (!super.equals(objectToCompare)) return false;
        return Objects.equals(getLinkGUID(), that.getLinkGUID()) &&
                Objects.equals(getLinkType(), that.getLinkType()) &&
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
        return Objects.hash(getLinkGUID(),
                            getLinkType(),
                            getLinkedAttributeName(),
                            getLinkProperties(),
                            getLinkedAttributeGUID());
    }

}