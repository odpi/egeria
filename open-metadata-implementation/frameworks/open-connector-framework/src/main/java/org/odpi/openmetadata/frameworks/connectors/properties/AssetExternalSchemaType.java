/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ExternalSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Objects;

/**
 * SchemaTypeChoice describes a schema element that has a choice of type.  This class returns the list of schema choices.
 */
public class AssetExternalSchemaType extends AssetSchemaType
{
    private static final long     serialVersionUID = 1L;

    protected ExternalSchemaType externalSchemaTypeBean;


    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetExternalSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param externalSchemaTypeBean bean containing the schema element properties
     */
    public AssetExternalSchemaType(ExternalSchemaType externalSchemaTypeBean)
    {
        super(externalSchemaTypeBean);

        if (externalSchemaTypeBean == null)
        {
            this.externalSchemaTypeBean = new ExternalSchemaType();
        }
        else
        {
            this.externalSchemaTypeBean = externalSchemaTypeBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param externalSchemaTypeBean bean containing the schema element properties
     */
    public AssetExternalSchemaType(AssetDescriptor    parentAsset,
                                   ExternalSchemaType externalSchemaTypeBean)
    {
        super(parentAsset, externalSchemaTypeBean);

        if (externalSchemaTypeBean == null)
        {
            this.externalSchemaTypeBean = new ExternalSchemaType();
        }
        else
        {
            this.externalSchemaTypeBean = externalSchemaTypeBean;
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
    public AssetExternalSchemaType(AssetDescriptor         parentAsset,
                                   AssetExternalSchemaType templateSchemaElement)
    {
        super(parentAsset, templateSchemaElement);

        if (templateSchemaElement == null)
        {
            this.externalSchemaTypeBean = new ExternalSchemaType();
        }
        else
        {
            this.externalSchemaTypeBean = templateSchemaElement.getExternalSchemaTypeBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return  schema choice bean
     */
    protected ExternalSchemaType getExternalSchemaTypeBean()
    {
        return externalSchemaTypeBean;
    }


    /**
     * Return the external schema type.
     *
     * @return reusable schema type
     */
    public AssetSchemaType getLinkedSchemaType()
    {
        AssetSchemaType assetSchemaType = null;

        if ((externalSchemaTypeBean != null) && (externalSchemaTypeBean.getLinkedSchemaType() != null))
        {
            assetSchemaType = AssetSchemaType.createAssetSchemaType(parentAsset, externalSchemaTypeBean.getLinkedSchemaType());
        }

        return assetSchemaType;
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
        return new AssetExternalSchemaType(parentAsset, this);
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
        return externalSchemaTypeBean;
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(ExternalSchemaType bean)
    {
        super.setBean(bean);
        this.externalSchemaTypeBean = bean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetSchemaTypeChoice{" +
                "parentAsset=" + parentAsset +
                ", schemaTypeOptions=" + getLinkedSchemaType() +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
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
        AssetExternalSchemaType that = (AssetExternalSchemaType) objectToCompare;
        return Objects.equals(externalSchemaTypeBean, that.externalSchemaTypeBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSchemaTypeBean);
    }
}