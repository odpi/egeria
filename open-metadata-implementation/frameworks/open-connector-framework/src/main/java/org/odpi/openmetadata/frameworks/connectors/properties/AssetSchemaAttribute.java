/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;

import java.util.Objects;

/**
 * <p>
 *     SchemaAttribute describes a single attribute within a schema.  The attribute has a name, order in the
 *     schema and cardinality.
 *     Its type is another SchemaElement (either Schema, MapSchemaElement or PrimitiveSchemaElement).
 * </p>
 * <p>
 *     If it is a PrimitiveSchemaElement it may have an override for the default value within.
 * </p>
 */
public class AssetSchemaAttribute extends AssetReferenceable
{
    protected SchemaAttribute schemaAttributeBean;
    protected AssetSchemaType assetSchemaType;


    /**
     * Bean constructor
     *
     * @param schemaAttributeBean bean containing all of the properties
     * @param assetSchemaType type for this schema attribute
     */
    public AssetSchemaAttribute(SchemaAttribute     schemaAttributeBean,
                                AssetSchemaType assetSchemaType)
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

        if (assetSchemaType == null)
        {
            this.assetSchemaType = null;
        }
        else
        {
            /*
             * SchemaElement is an abstract class with a placeholder method to clone an object
             * of its sub-class.  When cloneSchemaElement() is called, the implementation in the
             * sub-class is called.
             */
            this.assetSchemaType = assetSchemaType.cloneAssetSchemaElement(super.getParentAsset());
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param schemaAttributeBean bean containing all of the properties
     * @param assetSchemaType type for this schema attribute
     */
    public AssetSchemaAttribute(AssetDescriptor     parentAsset,
                                SchemaAttribute     schemaAttributeBean,
                                AssetSchemaType assetSchemaType)
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

        if (assetSchemaType == null)
        {
            this.assetSchemaType = null;
        }
        else
        {
            /*
             * SchemaElement is an abstract class with a placeholder method to clone an object
             * of its sub-class.  When cloneSchemaElement() is called, the implementation in the
             * sub-class is called.
             */
            this.assetSchemaType = assetSchemaType.cloneAssetSchemaElement(super.getParentAsset());
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param template template schema attribute to copy.
     */
    public AssetSchemaAttribute(AssetDescriptor   parentAsset, AssetSchemaAttribute template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
            this.assetSchemaType = null;
        }
        else
        {
            this.schemaAttributeBean = template.getSchemaAttributeBean();

            AssetSchemaType assetSchemaType = template.getAttributeType();
            if (assetSchemaType == null)
            {
                this.assetSchemaType = null;
            }
            else
            {
                /*
                 * SchemaElement is an abstract class with a placeholder method to clone an object
                 * of its sub-class.  When cloneSchemaElement() is called, the implementation in the
                 * sub-class is called.
                 */
                this.assetSchemaType = assetSchemaType.cloneAssetSchemaElement(super.getParentAsset());
            }
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return schema attribute bean
     */
    protected SchemaAttribute  getSchemaAttributeBean()
    {
        return schemaAttributeBean;
    }


    /**
     * Return the name of this schema attribute.
     *
     * @return String attribute name
     */
    public String getAttributeName() { return schemaAttributeBean.getAttributeName(); }


    /**
     * Return the position of this schema attribute in its parent schema.
     *
     * @return int position in schema 0 means first
     */
    public int getElementPosition() { return schemaAttributeBean.getElementPosition(); }


    /**
     * Return the cardinality defined for this schema attribute.
     *
     * @return String cardinality defined for this schema attribute.
     */
    public String getCardinality() { return schemaAttributeBean.getCardinality(); }


    /**
     * Return any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @return String default value override
     */
    public String getDefaultValueOverride() { return schemaAttributeBean.getDefaultValueOverride(); }


    /**
     * Return the SchemaElement that relates to the type of this attribute.
     *
     * @return SchemaElement
     */
    public AssetSchemaType getAttributeType()
    {
        if (assetSchemaType == null)
        {
            return null;
        }
        else
        {
            return assetSchemaType.cloneAssetSchemaElement(super.getParentAsset());
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return schemaAttributeBean.toString();
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
        if (!(objectToCompare instanceof AssetSchemaAttribute))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetSchemaAttribute that = (AssetSchemaAttribute) objectToCompare;
        return Objects.equals(getSchemaAttributeBean(), that.getSchemaAttributeBean()) &&
                Objects.equals(assetSchemaType, that.assetSchemaType);
    }
}