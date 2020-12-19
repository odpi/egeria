/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.LiteralSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Objects;

/**
 * AssetLiteralSchemaType describes a schema element that has a primitive type and a fixed value.  This class stores which
 * type of primitive type it is and the fixed value.
 */
public class AssetLiteralSchemaType extends AssetSchemaType
{
    private static final long     serialVersionUID = 1L;

    protected LiteralSchemaType literalSchemaTypeBean;

    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetLiteralSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param literalSchemaTypeBean bean containing the schema element properties
     */
    public AssetLiteralSchemaType(LiteralSchemaType literalSchemaTypeBean)
    {
        super(literalSchemaTypeBean);

        if (literalSchemaTypeBean == null)
        {
            this.literalSchemaTypeBean = new LiteralSchemaType();
        }
        else
        {
            this.literalSchemaTypeBean = literalSchemaTypeBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param literalSchemaTypeBean bean containing the schema element properties
     */
    public AssetLiteralSchemaType(AssetDescriptor   parentAsset,
                                  LiteralSchemaType literalSchemaTypeBean)
    {
        super(parentAsset, literalSchemaTypeBean);

        if (literalSchemaTypeBean == null)
        {
            this.literalSchemaTypeBean = new LiteralSchemaType();
        }
        else
        {
            this.literalSchemaTypeBean = literalSchemaTypeBean;
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
    public AssetLiteralSchemaType(AssetDescriptor        parentAsset,
                                  AssetLiteralSchemaType templateSchemaElement)
    {
        super(parentAsset, templateSchemaElement);

        if (templateSchemaElement == null)
        {
            this.literalSchemaTypeBean = new LiteralSchemaType();
        }
        else
        {
            this.literalSchemaTypeBean = templateSchemaElement.getLiteralSchemaTypeBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return primitive element bean
     */
    protected LiteralSchemaType getLiteralSchemaTypeBean()
    {
        return literalSchemaTypeBean;
    }


    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String DataType
     */
    public String getDataType() { return literalSchemaTypeBean.getDataType(); }


    /**
     * Return the fixed value for the element.  Null means this literal represents null.
     *
     * @return String containing fixed value
     */
    public String getFixedValue() { return literalSchemaTypeBean.getFixedValue(); }


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
        return new AssetLiteralSchemaType(parentAsset, this);
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
        return literalSchemaTypeBean;
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(LiteralSchemaType bean)
    {
        super.setBean(bean);
        this.literalSchemaTypeBean = bean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetLiteralSchemaType{" +
                "parentAsset=" + parentAsset +
                ", dataType='" + getDataType() + '\'' +
                ", fixedValue='" + getFixedValue() + '\'' +
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
        AssetLiteralSchemaType that = (AssetLiteralSchemaType) objectToCompare;
        return Objects.equals(literalSchemaTypeBean, that.literalSchemaTypeBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), literalSchemaTypeBean);
    }
}