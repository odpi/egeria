/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  SchemaAttribute describes a single attribute within a schema.  The attribute has a name, order in the
 *  schema and cardinality.  Its type is a SchemaType (such as StructSchemaType or PrimitiveSchemaType) or a SchemaLink.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DerivedSchemaAttribute.class, name = "DerivedSchemaAttribute")
        })
public class SchemaAttribute extends SchemaElement
{
    private static final long     serialVersionUID = 1L;

    protected String                            attributeName          = null;
    protected int                               elementPosition        = 0;
    protected String                            cardinality            = null;
    protected String                            defaultValueOverride   = null;
    protected String                            anchorGUID             = null;
    protected SchemaType                        attributeType          = null;
    protected SchemaLink                        externalAttributeType  = null;
    protected List<SchemaAttributeRelationship> attributeRelationships = null;


    /**
     * Default constructor
     */
    public SchemaAttribute()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public SchemaAttribute(SchemaAttribute template)
    {
        super(template);

        if (template != null)
        {
            attributeName = template.getAttributeName();
            elementPosition = template.getElementPosition();
            cardinality = template.getCardinality();
            defaultValueOverride = template.getDefaultValueOverride();
            anchorGUID = template.getAnchorGUID();
            attributeType = template.getAttributeType();
            externalAttributeType = template.getExternalAttributeType();
            attributeRelationships = template.getAttributeRelationships();
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because schema element
     * is abstract.
     *
     * @return Clone of subclass.
     */
    public SchemaElement cloneSchemaElement()
    {
        return new SchemaAttribute(this);
    }


    /**
     * Return the name of this schema attribute.
     *
     * @return String attribute name
     */
    public String getAttributeName() { return attributeName; }


    /**
     * Set up the name of this schema attribute.
     *
     * @param attributeName String attribute name
     */
    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    /**
     * Return the position of this schema attribute in its parent schema.
     *
     * @return int position in schema 0 means first
     */
    public int getElementPosition() { return elementPosition; }


    /**
     * Set up the position of this schema attribute in its parent schema.
     *
     * @param elementPosition int position in schema 0 means first
     */
    public void setElementPosition(int elementPosition)
    {
        this.elementPosition = elementPosition;
    }


    /**
     * Return the cardinality defined for this schema attribute.
     *
     * @return String cardinality defined for this schema attribute.
     */
    public String getCardinality() { return cardinality; }


    /**
     * Set up the cardinality defined for this schema attribute.
     *
     * @param cardinality String cardinality defined for this schema attribute.
     */
    public void setCardinality(String cardinality)
    {
        this.cardinality = cardinality;
    }

    /**
     * Return any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @return String default value override
     */
    public String getDefaultValueOverride() { return defaultValueOverride; }


    /**
     * Set up any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @param defaultValueOverride String default value override
     */
    public void setDefaultValueOverride(String defaultValueOverride)
    {
        this.defaultValueOverride = defaultValueOverride;
    }

    /**
     * Return the anchorGUID defined for this schema attribute.
     *
     * @return String anchorGUID defined for this schema attribute.
     */
    public String getAnchorGUID() {
        return anchorGUID;
    }

    /**
     * Set up the anchorGUID of this schema attribute
     *
     * @param anchorGUID GUID of the anchor entity
     */
    public void setAnchorGUID(String anchorGUID) {
        this.anchorGUID = anchorGUID;
    }

    /**
     * Return the SchemaType that relates to the type of this attribute.
     *
     * @return SchemaType
     */
    public SchemaType getAttributeType()
    {
        if (attributeType == null)
        {
            return null;
        }
        else
        {
            return attributeType.cloneSchemaType();
        }
    }


    /**
     * Set up the SchemaElement that relates to the type of this attribute.
     *
     * @param attributeType SchemaElement
     */
    public void setAttributeType(SchemaType attributeType)
    {
        this.attributeType = attributeType;
    }


    /**
     * Set up optional link to another attribute.  For example, a foreign key relationship between relational
     * columns.
     *
     * @return SchemaLink object
     */
    public SchemaLink getExternalAttributeType()
    {
        return externalAttributeType;
    }


    /**
     * Set up optional links to another attribute.  For example, a foreign key relationship between relational
     * columns.
     *
     * @param externalAttributeType SchemaLink object
     */
    public void setExternalAttributeType(SchemaLink externalAttributeType)
    {
        this.externalAttributeType = externalAttributeType;
    }


    /**
     * Return any relationships to other schema attributes.
     *
     * @return list of attribute relationships
     */
    public List<SchemaAttributeRelationship> getAttributeRelationships()
    {
        if (attributeRelationships == null)
        {
            return null;
        }
        else if (attributeRelationships.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(attributeRelationships);
        }
    }


    /**
     * Set up any relationships to other schema attributes.
     *
     * @param attributeRelationships list of attribute relationships
     */
    public void setAttributeRelationships(List<SchemaAttributeRelationship> attributeRelationships)
    {
        this.attributeRelationships = attributeRelationships;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaAttribute{" +
                "attributeName='" + attributeName + '\'' +
                ", elementPosition=" + elementPosition +
                ", cardinality='" + cardinality + '\'' +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", anchorGUID='" + anchorGUID + '\'' +
                ", attributeType=" + attributeType +
                ", externalAttributeType=" + externalAttributeType +
                ", attributeRelationships=" + attributeRelationships +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaAttribute that = (SchemaAttribute) objectToCompare;
        return getElementPosition() == that.getElementPosition() &&
                Objects.equals(getAttributeName(), that.getAttributeName()) &&
                Objects.equals(getCardinality(), that.getCardinality()) &&
                Objects.equals(getDefaultValueOverride(), that.getDefaultValueOverride()) &&
                Objects.equals(getAnchorGUID(), that.getAnchorGUID()) &&
                Objects.equals(getAttributeRelationships(), that.getAttributeRelationships()) &&
                Objects.equals(getAttributeType(), that.getAttributeType()) &&
                Objects.equals(getExternalAttributeType(), that.getExternalAttributeType());
    }
}