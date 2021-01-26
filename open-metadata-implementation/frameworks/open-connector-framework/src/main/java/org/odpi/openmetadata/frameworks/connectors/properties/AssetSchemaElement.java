/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaElement;

import java.util.Objects;

/**
 * The AssetSchemaElement object provides a base class for the pieces that make up a schema for a data asset.
 * A schema provides information about how the data is structured in the asset.
 */
public abstract class AssetSchemaElement extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected SchemaElement schemaElementBean = null;

    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetSchemaElement(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param schemaElementBean bean containing the schema element properties
     */
    public AssetSchemaElement(SchemaElement schemaElementBean)
    {
        super(schemaElementBean);
    }


    /**
     * Bean constructor with parent.
     *
     * @param parentAsset descriptor of asset that this property relates to.
     * @param schemaElementBean bean containing the schema element properties
     */
    public AssetSchemaElement(AssetDescriptor parentAsset, SchemaElement schemaElementBean)
    {
        super(parentAsset, schemaElementBean);
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param template template object to copy.
     */
    public AssetSchemaElement(AssetDescriptor parentAsset, AssetSchemaElement template)
    {
        super(parentAsset, template);
    }


    /**
     * Set up the bean that contains the properties of the schema element.
     *
     * @param schemaElementBean bean containing all of the properties
     */
    protected void  setBean(SchemaElement schemaElementBean)
    {
        super.setBean(schemaElementBean);
        this.schemaElementBean = schemaElementBean;
    }


    /**
     * Return this schema type bean.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    protected SchemaElement getSchemaElementBean()
    {
        return schemaElementBean;
    }


    /**
     * Is the schema element deprecated?
     *
     * @return boolean flag
     */
    public boolean isDeprecated()
    {
        if (schemaElementBean == null)
        {
            return false;
        }

        return this.schemaElementBean.getIsDeprecated();
    }


    /**
     * Return the simple name of the schema element.
     *
     * @return string name
     */
    public String  getDisplayName()
    {
        if (schemaElementBean == null)
        {
            return null;
        }

        return this.schemaElementBean.getDisplayName();
    }


    /**
     * Returns the stored description property for the schema element.
     *
     * @return string description
     */
    public String getDescription()
    {
        if (schemaElementBean == null)
        {
            return null;
        }

        return this.schemaElementBean.getDescription();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetSchemaElement{" +
                "displayName='" + getDisplayName() + '\'' +
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
        AssetSchemaElement that = (AssetSchemaElement) objectToCompare;
        return Objects.equals(schemaElementBean, that.schemaElementBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), schemaElementBean);
    }
}