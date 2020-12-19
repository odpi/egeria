/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.EnumSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Objects;

/**
 * EnumSchemaType describes a schema element that has a primitive type.  This class stores which
 * type of primitive type it is an a default value if supplied.
 */
public class AssetEnumSchemaType extends AssetSimpleSchemaType
{
    private static final long     serialVersionUID = 1L;

    protected EnumSchemaType enumSchemaTypeBean;

    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetEnumSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param enumSchemaTypeBean bean containing the schema element properties
     */
    public AssetEnumSchemaType(EnumSchemaType enumSchemaTypeBean)
    {
        super(enumSchemaTypeBean);

        if (enumSchemaTypeBean == null)
        {
            this.enumSchemaTypeBean = new EnumSchemaType();
        }
        else
        {
            this.enumSchemaTypeBean = enumSchemaTypeBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param enumSchemaTypeBean bean containing the schema element properties
     */
    public AssetEnumSchemaType(AssetDescriptor parentAsset,
                               EnumSchemaType  enumSchemaTypeBean)
    {
        super(parentAsset, enumSchemaTypeBean);

        if (enumSchemaTypeBean == null)
        {
            this.enumSchemaTypeBean = new EnumSchemaType();
        }
        else
        {
            this.enumSchemaTypeBean = enumSchemaTypeBean;
        }
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param template template object to copy.
     */
    public AssetEnumSchemaType(AssetDescriptor     parentAsset,
                               AssetEnumSchemaType template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.enumSchemaTypeBean = new EnumSchemaType();
        }
        else
        {
            this.enumSchemaTypeBean = template.getEnumSchemaTypeBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return enum element bean
     */
    protected EnumSchemaType getEnumSchemaTypeBean()
    {
        return enumSchemaTypeBean;
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
        return new AssetEnumSchemaType(parentAsset, this);
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
        return enumSchemaTypeBean;
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(EnumSchemaType bean)
    {
        super.setBean(bean);
        this.enumSchemaTypeBean = bean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetEnumSchemaType{" +
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
        AssetEnumSchemaType that = (AssetEnumSchemaType) objectToCompare;
        return Objects.equals(enumSchemaTypeBean, that.enumSchemaTypeBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), enumSchemaTypeBean);
    }
}