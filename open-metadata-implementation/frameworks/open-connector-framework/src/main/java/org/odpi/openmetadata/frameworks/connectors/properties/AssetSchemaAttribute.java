/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *     SchemaAttribute describes a single attribute within a schema.  The attribute has a name, order in the
 *     schema and cardinality.
 *     Its type is another SchemaElement of type SchemaType.  SchemaType has many
 * </p>
 */
public class AssetSchemaAttribute extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected SchemaAttribute schemaAttributeBean;


    /**
     * Bean constructor used by subclasses
     *
     * @param schemaAttributeBean bean containing all of the properties
     */
    protected AssetSchemaAttribute(SchemaAttribute schemaAttributeBean)
    {
        super(schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param schemaAttributeBean bean containing all of the properties
     */
    public AssetSchemaAttribute(AssetDescriptor parentAsset,
                                SchemaAttribute schemaAttributeBean)
    {
        super(parentAsset, schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param template template schema attribute to copy.
     */
    public AssetSchemaAttribute(AssetDescriptor parentAsset, AssetSchemaAttribute template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = template.getSchemaAttributeBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return schema attribute bean
     */
    protected SchemaAttribute getSchemaAttributeBean()
    {
        return schemaAttributeBean;
    }


    /**
     * Return the name of this schema attribute.
     *
     * @return String attribute name
     */
    public String getAttributeName() { return schemaAttributeBean.getDisplayName(); }


    /**
     * Return the position of this schema attribute in its parent schema.
     *
     * @return int position in schema 0 means first
     */
    public int getElementPosition() { return schemaAttributeBean.getElementPosition(); }


    /**
     * Return the category of the schema attribute.
     *
     * @return enum SchemaAttributeCategory
     */
    public SchemaAttributeCategory getCategory()
    {
        return schemaAttributeBean.getCategory();
    }


    /**
     * Return the cardinality defined for this schema attribute.
     *
     * @return String cardinality defined for this schema attribute.
     */
    public String getCardinality() { return schemaAttributeBean.getCardinality(); }

    /**
     * Return this minimum number of instances allowed for this attribute.
     *
     * @return int
     */
    public int getMinCardinality()
    {
        return schemaAttributeBean.getMinCardinality();
    }


    /**
     * Return the maximum number of instances allowed for this attribute.
     *
     * @return int (-1 means infinite)
     */
    public int getMaxCardinality()
    {
        return schemaAttributeBean.getMaxCardinality();
    }


    /**
     * Return whether the same value can be used by more than one instance of this attribute.
     *
     * @return boolean flag
     */
    public boolean allowsDuplicateValues()
    {
        return schemaAttributeBean.getAllowsDuplicateValues();
    }


    /**
     * Return whether the attribute instances are arranged in an order.
     *
     * @return boolean flag
     */
    public boolean hasOrderedValues()
    {
        return schemaAttributeBean.getOrderedValues();
    }


    /**
     * Return the order that the attribute instances are arranged in - if any.
     *
     * @return DataItemSortOrder enum
     */
    public DataItemSortOrder getSortOrder()
    {
        return schemaAttributeBean.getSortOrder();
    }


    /**
     * Return any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @return String default value override
     */
    public String getDefaultValueOverride() { return schemaAttributeBean.getDefaultValueOverride(); }


    /**
     * Return the SchemaType that relates to the type of this attribute.  It is possible to query its type and cast it to
     * specific types of schema type to retrieve all of the values.
     *
     * @return SchemaType object
     */
    @Deprecated
    public AssetSchemaType getLocalSchemaType()
    {
        return getAttributeType();
    }


    /**
     * Return the SchemaType that relates to the type of this attribute.  It is possible to query its type and cast it to
     * specific types of schema type to retrieve all of the values.
     *
     * @return SchemaType object
     */
    public AssetSchemaType getAttributeType()
    {
        if ((schemaAttributeBean != null) && (schemaAttributeBean.getAttributeType() != null))
        {
            return AssetSchemaType.createAssetSchemaType(parentAsset, schemaAttributeBean.getAttributeType());
        }

        return null;
    }


    /**
     * Return any relationships to other schema attributes.  This could be relationships in a Graph or foreign key relationships
     * in a relational schema
     *
     * @return list of attribute relationships
     */
    public List<AssetSchemaAttributeRelationship> getAttributeRelationships()
    {
        List<AssetSchemaAttributeRelationship>  attributeRelationships = new ArrayList<>();

        if (schemaAttributeBean.getAttributeRelationships() != null)
        {
            for (SchemaAttributeRelationship attributeRelationship : schemaAttributeBean.getAttributeRelationships())
            {
                if (attributeRelationship != null)
                {
                    attributeRelationships.add(new AssetSchemaAttributeRelationship(super.getParentAsset(), attributeRelationship));
                }
            }
        }

        if (attributeRelationships.isEmpty())
        {
            return null;
        }

        return attributeRelationships;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetSchemaAttribute{" +
                "parentAsset=" + parentAsset +
                ", attributeName='" + getAttributeName() + '\'' +
                ", elementPosition=" + getElementPosition() +
                ", cardinality='" + getCardinality() + '\'' +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", localSchemaType=" + getLocalSchemaType() +
                ", attributeRelationships=" + getAttributeRelationships() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", meanings=" + getMeanings() +
                ", securityTags=" + getSecurityTags() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", assetClassifications=" + getAssetClassifications() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetSchemaAttribute that = (AssetSchemaAttribute) objectToCompare;
        return Objects.equals(schemaAttributeBean, that.schemaAttributeBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), schemaAttributeBean);
    }
}