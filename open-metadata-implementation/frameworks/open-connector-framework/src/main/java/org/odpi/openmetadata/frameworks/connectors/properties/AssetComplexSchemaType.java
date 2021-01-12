/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;

import java.util.Objects;

/**
 * An asset's schema provides information about how the asset structures the data it supports.
 * The AssetComplexSchemaType object describes a nested structure of schema attributes and types.
 */
public  class AssetComplexSchemaType extends AssetSchemaType
{
    private static final long     serialVersionUID = 1L;

    protected ComplexSchemaType     complexSchemaTypeBean = null;
    protected AssetSchemaAttributes schemaAttributes      = null;


    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetComplexSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }



    /**
     * Bean constructor
     *
     * @param schemaBean bean containing the schema properties
     */
    public AssetComplexSchemaType(ComplexSchemaType schemaBean)
    {
        super(schemaBean);

        this.complexSchemaTypeBean = schemaBean;
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param schemaBean bean containing the schema properties
     */
    public AssetComplexSchemaType(AssetDescriptor       parentAsset,
                                  ComplexSchemaType     schemaBean)
    {
        super(parentAsset, schemaBean);

        this.complexSchemaTypeBean = schemaBean;
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema is attached to.
     * @param template template object to copy.
     */
    public AssetComplexSchemaType(AssetDescriptor parentAsset, AssetComplexSchemaType template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.complexSchemaTypeBean = template.getComplexSchemaTypeBean();
            this.schemaAttributes = template.getSchemaAttributes(parentAsset);
        }
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(ComplexSchemaType bean)
    {
        super.setBean(bean);
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return complex element bean
     */
    protected ComplexSchemaType getComplexSchemaTypeBean()
    {
        return complexSchemaTypeBean;
    }


    /**
     * Return the list of schema attributes in this schema.
     *
     * @return SchemaAttributes
     */
    public AssetSchemaAttributes getSchemaAttributes()
    {
        if (schemaAttributes == null)
        {
            return null;
        }

        return getSchemaAttributes(parentAsset);
    }


    /**
     * Return the list of schema attributes in this schema.
     *
     * @param parentAsset description of the asset that this schema is attached to.
     * @return SchemaAttributes
     */
    protected AssetSchemaAttributes getSchemaAttributes(AssetDescriptor parentAsset)
    {
        if (schemaAttributes == null)
        {
            return null;
        }
        else
        {
            return schemaAttributes.cloneIterator(parentAsset);
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because AssetSchemaType
     * is abstract.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    @Override

    protected AssetSchemaType cloneAssetSchemaType(AssetDescriptor parentAsset)
    {
        return new AssetComplexSchemaType(parentAsset, this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetComplexSchemaType{" +
                "schemaAttributes=" + schemaAttributes +
                ", parentAsset=" + parentAsset +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", formula='" + getFormula() + '\'' +
                ", queries=" + getQueries() +
                ", deprecated=" + isDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
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
        AssetComplexSchemaType that = (AssetComplexSchemaType) objectToCompare;
        return Objects.equals(complexSchemaTypeBean, that.complexSchemaTypeBean) &&
                       Objects.equals(schemaAttributes, that.schemaAttributes);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), complexSchemaTypeBean, schemaAttributes);
    }
}