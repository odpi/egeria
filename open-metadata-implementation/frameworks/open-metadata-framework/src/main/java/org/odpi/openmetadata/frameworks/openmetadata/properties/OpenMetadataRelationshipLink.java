/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataRelationshipLink describes a relationship that links this entity to another.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataRelationshipLink extends OpenMetadataTypeDefElementHeader
{
    private OpenMetadataTypeDefLink                relationshipType         = null;
    private String                                 attributeName            = null;
    private String                                 attributeDescription     = null;
    private String                                 attributeDescriptionGUID = null;
    private OpenMetadataRelationshipEndCardinality attributeCardinality     = OpenMetadataRelationshipEndCardinality.UNKNOWN;
    private boolean                                elementAtEnd1            = false;


    /**
     * Default constructor creates an empty end
     */
    public OpenMetadataRelationshipLink()
    {
        super();
    }


    /**
     * Copy/clone constructor copies the supplied template into the new end.
     *
     * @param template OpenMetadataRelationshipEndDef
     */
    public OpenMetadataRelationshipLink(OpenMetadataRelationshipLink template)
    {
        super(template);

        if (template != null)
        {
            relationshipType = template.getRelationshipType();
            attributeName    = template.getAttributeName();
            attributeCardinality = template.getAttributeCardinality();
            attributeDescription = template.getAttributeDescription();
            attributeDescriptionGUID = template.getAttributeDescriptionGUID();
            elementAtEnd1 = template.getElementAtEnd1();
        }
    }


    /**
     * Return the identifiers of the OpenMetadataEntityDef describing the type of entity on this end of the relationship.
     *
     * @return OpenMetadataTypeDefLink unique identifiers
     */
    public OpenMetadataTypeDefLink getRelationshipType()
    {
        if (relationshipType == null)
        {
            return null;
        }
        else
        {
            return new OpenMetadataTypeDefLink(relationshipType);
        }
    }


    /**
     * Set up the guid of the OpenMetadataEntityDef describing the type of entity on this end of the relationship.
     *
     * @param relationshipType OpenMetadataTypeDefLink unique identifiers for the entity's type
     */
    public void setRelationshipType(OpenMetadataTypeDefLink relationshipType)
    {
        if (relationshipType == null)
        {
            this.relationshipType = null;
        }
        else
        {
            this.relationshipType = new OpenMetadataTypeDefLink(relationshipType);
        }
    }


    /**
     * Return the attribute name used to describe this end of the relationship
     *
     * @return String name for the attribute
     */
    public String getAttributeName()
    {
        return attributeName;
    }


    /**
     * Set up the attribute name used to describe this end of the relationship.
     *
     * @param attributeName String name for the attribute
     */
    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    /**
     * Return the cardinality for this end of the relationship.
     *
     * @return OpenMetadataRelationshipEndCardinality Enum
     */
    public OpenMetadataRelationshipEndCardinality getAttributeCardinality()
    {
        return attributeCardinality;
    }


    /**
     * Set up the cardinality for this end of the relationship.
     *
     * @param attributeCardinality OpenMetadataRelationshipEndCardinality Enum
     */
    public void setAttributeCardinality(OpenMetadataRelationshipEndCardinality attributeCardinality)
    {
        this.attributeCardinality = attributeCardinality;
    }


    /**
     * Return the attributeDescription of this end of the relationship.
     *
     * @return String attributeDescription
     */
    public String getAttributeDescription()
    {
        return attributeDescription;
    }


    /**
     * Set up the attributeDescription for this end of the relationship.
     *
     * @param attributeDescription String
     */
    public void setAttributeDescription(String attributeDescription)
    {
        this.attributeDescription = attributeDescription;
    }


    /**
     * Return the unique identifier (guid) of the glossary term that describes this OpenMetadataRelationshipEndDef.
     *
     * @return String guid
     */
    public String getAttributeDescriptionGUID()
    {
        return attributeDescriptionGUID;
    }


    /**
     * Set up the unique identifier (guid) of the glossary term that describes this OpenMetadataRelationshipEndDef.
     *
     * @param attributeDescriptionGUID String guid
     */
    public void setAttributeDescriptionGUID(String attributeDescriptionGUID)
    {
        this.attributeDescriptionGUID = attributeDescriptionGUID;
    }



    /**
     * Return whether this element is at end 1 of the relationship.
     *
     * @return boolean
     */
    public boolean getElementAtEnd1()
    {
        return elementAtEnd1;
    }


    /**
     * Set up whether this element is at end 1 of the relationship.
     *
     * @param elementAtEnd1 boolean
     */
    public void setElementAtEnd1(boolean elementAtEnd1)
    {
        this.elementAtEnd1 = elementAtEnd1;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style attributeDescription of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRelationshipLink{" +
                "relationshipType=" + relationshipType +
                ", attributeName='" + attributeName + '\'' +
                ", attributeDescription='" + attributeDescription + '\'' +
                ", attributeDescriptionGUID='" + attributeDescriptionGUID + '\'' +
                ", attributeCardinality=" + attributeCardinality +
                ", elementAtEnd1=" + elementAtEnd1 +
                "} " + super.toString();
    }


    /**
     * Verify that supplied object has the same properties.
     *
     * @param objectToCompare object to test
     * @return result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        OpenMetadataRelationshipLink that = (OpenMetadataRelationshipLink) objectToCompare;
        return elementAtEnd1 == that.elementAtEnd1 &&
                Objects.equals(relationshipType, that.relationshipType) &&
                Objects.equals(attributeName, that.attributeName) &&
                Objects.equals(attributeDescription, that.attributeDescription) &&
                Objects.equals(attributeDescriptionGUID, that.attributeDescriptionGUID) &&
                attributeCardinality == that.attributeCardinality;
    }


    /**
     * Code for a hash map
     *
     * @return hashcode
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipType, attributeName, attributeDescription, attributeDescriptionGUID, attributeCardinality, elementAtEnd1);
    }
}
