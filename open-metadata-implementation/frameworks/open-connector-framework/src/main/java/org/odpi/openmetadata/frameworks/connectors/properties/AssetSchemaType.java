/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;

import java.util.Objects;

/**
 * <p>
 *     The AssetSchemaType object provides a base class for the pieces that make up a schema for an asset.
 *     A schema provides information about how the data is structured in the asset.  Schemas are typically
 *     described as nested structures of linked schema elements.
 * </p>
 *     AssetSchemaType is an abstract class - its subclasses enable the most accurate and precise mapping of the
 *     elements in a schema to the asset.
 *     <ul>
 *         <li>AssetPrimitiveSchemaType is for a leaf element in a schema.</li>
 *         <li>AssetStructSchemaType is a complex structure of nested schema elements.</li>
 *         <li>AssetMapSchemaType is for an attribute of type Map</li>
 *     </ul>
 *     Most assets will be linked to a AssetComplexSchemaType.
 * <p>
 *     StructSchemaType elements can be linked to one another using SchemaLink.
 * </p>
 */
public abstract class AssetSchemaType extends AssetSchemaElement
{
    private static final long     serialVersionUID = 1L;

    @SuppressWarnings(value = "deprecation")
    public static AssetSchemaType createAssetSchemaType(AssetDescriptor parentAsset, SchemaType bean)
    {
        AssetSchemaType assetSchemaType = null;

        if (bean instanceof PrimitiveSchemaType)
        {
            assetSchemaType = new AssetPrimitiveSchemaType(parentAsset, (PrimitiveSchemaType)bean);
        }
        else if (bean instanceof MapSchemaType)
        {
            assetSchemaType = new AssetMapSchemaType(parentAsset, (MapSchemaType)bean);
        }
        else if (bean instanceof EnumSchemaType)
        {
            assetSchemaType = new AssetEnumSchemaType(parentAsset, (EnumSchemaType)bean);
        }
        else if (bean instanceof BoundedSchemaType)
        {
            assetSchemaType = new AssetBoundedSchemaType(parentAsset, (BoundedSchemaType)bean);
        }
        else if (bean instanceof LiteralSchemaType)
        {
            assetSchemaType = new AssetLiteralSchemaType(parentAsset, (LiteralSchemaType)bean);
        }
        else if (bean instanceof ComplexSchemaType)
        {
            assetSchemaType = new AssetComplexSchemaType(parentAsset, (ComplexSchemaType)bean);
        }

        return assetSchemaType;
    }


    private SchemaType schemaTypeBean = null;

    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param schemaTypeBean bean containing the schema element properties
     */
    public AssetSchemaType(SchemaType schemaTypeBean)
    {
        super(schemaTypeBean);

        this.schemaTypeBean = schemaTypeBean;
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param schemaTypeBean bean containing the schema element properties
     */
    public AssetSchemaType(AssetDescriptor parentAsset,
                           SchemaType      schemaTypeBean)
    {
        super(parentAsset, schemaTypeBean);

        this.schemaTypeBean = schemaTypeBean;
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param templateSchema template object to copy.
     */
    public AssetSchemaType(AssetDescriptor parentAsset, AssetSchemaType templateSchema)
    {
        super(parentAsset, templateSchema);

        if (templateSchema == null)
        {
            this.schemaTypeBean = null;
        }
        else
        {
            this.schemaTypeBean = templateSchema.getSchemaTypeBean();
        }
    }


    /**
     * Return a clone of this schema type.  This method should be implemented by the subclasses.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    protected abstract AssetSchemaType cloneAssetSchemaType(AssetDescriptor parentAsset);


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(SchemaType bean)
    {
        super.setBean(bean);
        this.schemaTypeBean = bean;
    }


    /**
     * Return this schema type bean.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    protected SchemaType getSchemaTypeBean()
    {
        return schemaTypeBean;
    }


    /**
     * Return the version number of the schema element - null means no version number.
     *
     * @return String version number
     */
    public String getVersionNumber()
    {
        if (schemaTypeBean == null)
        {
            return null;
        }
        return this.getSchemaTypeBean().getVersionNumber();
    }


    /**
     * Return the name of the author of the schema element.  Null means the author is unknown.
     *
     * @return String author name
     */
    public String getAuthor()
    {
        if (schemaTypeBean == null)
        {
            return null;
        }

        return this.getSchemaTypeBean().getAuthor();
    }


    /**
     * Return the usage guidance for this schema element. Null means no guidance available.
     *
     * @return String usage guidance
     */
    public String getUsage()
    {
        if (schemaTypeBean == null)
        {
            return null;
        }

        return this.getSchemaTypeBean().getUsage();
    }


    /**
     * Return the format (encoding standard) used for this schema.  It may be XML, JSON, SQL DDL or something else.
     * Null means the encoding standard is unknown or there are many choices.
     *
     * @return String encoding standard
     */
    public String getEncodingStandard()
    {
        if (schemaTypeBean == null)
        {
            return null;
        }

        return this.getSchemaTypeBean().getEncodingStandard();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetSchemaType{" +
                "parentAsset=" + parentAsset +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", meanings=" + getMeanings() +
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
        AssetSchemaType that = (AssetSchemaType) objectToCompare;
        return Objects.equals(getSchemaTypeBean(), that.getSchemaTypeBean());
    }
}