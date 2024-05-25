/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataRelationshipEndDef describes the type of the entity and the attribute information for one end of a OpenMetadataRelationshipDef.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataRelationshipEndDef extends OpenMetadataTypeDefElementHeader
{
    private OpenMetadataTypeDefLink entityType    = null;
    private String                  attributeName = null;
    private String                     attributeDescription     = null;
    private String                                 attributeDescriptionGUID = null;
    private OpenMetadataRelationshipEndCardinality attributeCardinality     = OpenMetadataRelationshipEndCardinality.UNKNOWN;


    /**
     * Default constructor creates an empty end
     */
    public OpenMetadataRelationshipEndDef()
    {
        super();
    }


    /**
     * Copy/clone constructor copies the supplied template into the new end.
     *
     * @param template OpenMetadataRelationshipEndDef
     */
    public OpenMetadataRelationshipEndDef(OpenMetadataRelationshipEndDef template)
    {
        super(template);

        if (template != null)
        {
            entityType = template.getEntityType();
            attributeName = template.getAttributeName();
            attributeCardinality = template.getAttributeCardinality();
            attributeDescription = template.getAttributeDescription();
            attributeDescriptionGUID = template.getAttributeDescriptionGUID();
        }
    }


    /**
     * Return the identifiers of the OpenMetadataEntityDef describing the type of entity on this end of the relationship.
     *
     * @return OpenMetadataTypeDefLink unique identifiers
     */
    public OpenMetadataTypeDefLink getEntityType()
    {
        if (entityType == null)
        {
            return null;
        }
        else
        {
            return new OpenMetadataTypeDefLink(entityType);
        }
    }


    /**
     * Set up the guid of the OpenMetadataEntityDef describing the type of entity on this end of the relationship.
     *
     * @param entityType OpenMetadataTypeDefLink unique identifiers for the entity's type
     */
    public void setEntityType(OpenMetadataTypeDefLink entityType)
    {
        if (entityType == null)
        {
            this.entityType = null;
        }
        else
        {
            this.entityType = new OpenMetadataTypeDefLink(entityType);
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
     * Standard toString method.
     *
     * @return JSON style attributeDescription of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRelationshipEndDef{" +
                "attributeName='" + attributeName + '\'' +
                ", entityType='" + getEntityType() + '\'' +
                ", attributeDescription='" + attributeDescription + '\'' +
                ", attributeCardinality=" + attributeCardinality +
                '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof OpenMetadataRelationshipEndDef))
        {
            return false;
        }
        OpenMetadataRelationshipEndDef that = (OpenMetadataRelationshipEndDef) objectToCompare;
        return Objects.equals(getEntityType(), that.getEntityType()) &&
                Objects.equals(getAttributeName(), that.getAttributeName()) &&
                Objects.equals(getAttributeDescription(), that.getAttributeDescription()) &&
                Objects.equals(getAttributeDescriptionGUID(), that.getAttributeDescriptionGUID()) &&
                getAttributeCardinality() == that.getAttributeCardinality();
    }


    /**
     * Code for hash map
     *
     * @return hashcode
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getEntityType(),
                            getAttributeName(),
                            getAttributeDescription(),
                            getAttributeDescriptionGUID(),
                            getAttributeCardinality());
    }
}
