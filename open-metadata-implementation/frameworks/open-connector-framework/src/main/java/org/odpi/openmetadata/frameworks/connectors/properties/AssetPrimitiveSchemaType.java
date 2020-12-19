/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Objects;

/**
 * PrimitiveSchemaType describes a schema element that has a primitive type.  This class stores which
 * type of primitive type it is an a default value if supplied.
 */
public class AssetPrimitiveSchemaType extends AssetSimpleSchemaType
{
    private static final long     serialVersionUID = 1L;

    protected PrimitiveSchemaType primitiveSchemaTypeBean;

    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetPrimitiveSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param primitiveSchemaTypeBean bean containing the schema element properties
     */
    public AssetPrimitiveSchemaType(PrimitiveSchemaType primitiveSchemaTypeBean)
    {
        super(primitiveSchemaTypeBean);

        if (primitiveSchemaTypeBean == null)
        {
            this.primitiveSchemaTypeBean = new PrimitiveSchemaType();
        }
        else
        {
            this.primitiveSchemaTypeBean = primitiveSchemaTypeBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param primitiveSchemaTypeBean bean containing the schema element properties
     */
    public AssetPrimitiveSchemaType(AssetDescriptor     parentAsset,
                                    PrimitiveSchemaType primitiveSchemaTypeBean)
    {
        super(parentAsset, primitiveSchemaTypeBean);

        if (primitiveSchemaTypeBean == null)
        {
            this.primitiveSchemaTypeBean = new PrimitiveSchemaType();
        }
        else
        {
            this.primitiveSchemaTypeBean = primitiveSchemaTypeBean;
        }
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param templateSchemaElement template object to copy.
     */
    public AssetPrimitiveSchemaType(AssetDescriptor parentAsset,
                                    AssetPrimitiveSchemaType templateSchemaElement)
    {
        super(parentAsset, templateSchemaElement);

        if (templateSchemaElement == null)
        {
            this.primitiveSchemaTypeBean = new PrimitiveSchemaType();
        }
        else
        {
            this.primitiveSchemaTypeBean = templateSchemaElement.getPrimitiveSchemaTypeBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return primitive element bean
     */
    protected PrimitiveSchemaType getPrimitiveSchemaTypeBean()
    {
        return primitiveSchemaTypeBean;
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
        return new AssetPrimitiveSchemaType(parentAsset, this);
    }


    /**
     * Return this schema element bean.  This method is needed because SchemaElement
     * is abstract.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    @Override
    protected SchemaType getSchemaTypeBean()
    {
        return primitiveSchemaTypeBean;
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(PrimitiveSchemaType bean)
    {
        super.setBean(bean);
        this.primitiveSchemaTypeBean = bean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetPrimitiveSchemaType{" +
                "parentAsset=" + parentAsset +
                ", dataType='" + getDataType() + '\'' +
                ", defaultValue='" + getDefaultValue() + '\'' +
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
        AssetPrimitiveSchemaType that = (AssetPrimitiveSchemaType) objectToCompare;
        return Objects.equals(primitiveSchemaTypeBean, that.primitiveSchemaTypeBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), primitiveSchemaTypeBean);
    }
}