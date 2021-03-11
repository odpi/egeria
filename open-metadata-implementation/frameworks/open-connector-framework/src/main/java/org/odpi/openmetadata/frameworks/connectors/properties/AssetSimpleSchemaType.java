/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SimpleSchemaType;

import java.util.Objects;

/**
 * AssetSimpleSchemaType describes a schema element that has a primitive type.  This class stores which
 * type of primitive type it is an a default value if supplied.
 */
public class AssetSimpleSchemaType extends AssetSchemaType
{
    private static final long     serialVersionUID = 1L;

    protected SimpleSchemaType simpleSchemaTypeBean;

    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetSimpleSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param simpleSchemaTypeBean bean containing the schema element properties
     */
    public AssetSimpleSchemaType(SimpleSchemaType simpleSchemaTypeBean)
    {
        super(simpleSchemaTypeBean);

        this.simpleSchemaTypeBean = simpleSchemaTypeBean;
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param simpleSchemaTypeBean bean containing the schema element properties
     */
    public AssetSimpleSchemaType(AssetDescriptor  parentAsset,
                                 SimpleSchemaType simpleSchemaTypeBean)
    {
        super(parentAsset, simpleSchemaTypeBean);

        this.simpleSchemaTypeBean = simpleSchemaTypeBean;
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param template template object to copy.
     */
    public AssetSimpleSchemaType(AssetDescriptor       parentAsset,
                                 AssetSimpleSchemaType template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.simpleSchemaTypeBean = template.getSimpleSchemaTypeBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return primitive element bean
     */
    protected SimpleSchemaType getSimpleSchemaTypeBean()
    {
        return simpleSchemaTypeBean;
    }


    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String DataType
     */
    public String getDataType()
    {
        if (simpleSchemaTypeBean == null)
        {
            return null;
        }

        return simpleSchemaTypeBean.getDataType();
    }


    /**
     * Return the default value for the element.  Null means no default value set up.
     *
     * @return String containing default value
     */
    public String getDefaultValue()
    {
        if (simpleSchemaTypeBean == null)
        {
            return null;
        }

        return simpleSchemaTypeBean.getDefaultValue();
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
        return new AssetSimpleSchemaType(parentAsset, this);
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
        return simpleSchemaTypeBean;
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(SimpleSchemaType bean)
    {
        super.setBean(bean);
        this.simpleSchemaTypeBean = bean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetSimpleSchemaType{" +
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
        AssetSimpleSchemaType that = (AssetSimpleSchemaType) objectToCompare;
        return Objects.equals(simpleSchemaTypeBean, that.simpleSchemaTypeBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), simpleSchemaTypeBean);
    }
}