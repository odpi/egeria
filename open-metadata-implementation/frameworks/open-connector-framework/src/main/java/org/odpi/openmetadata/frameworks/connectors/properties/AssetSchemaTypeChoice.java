/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaTypeChoice;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SchemaTypeChoice describes a schema element that has a choice of type.  This class returns the list of schema choices.
 */
public class AssetSchemaTypeChoice extends AssetSchemaType
{
    private static final long     serialVersionUID = 1L;

    protected SchemaTypeChoice schemaTypeChoiceBean;


    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetSchemaTypeChoice(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param schemaTypeChoiceBean bean containing the schema element properties
     */
    public AssetSchemaTypeChoice(SchemaTypeChoice schemaTypeChoiceBean)
    {
        super(schemaTypeChoiceBean);

        if (schemaTypeChoiceBean == null)
        {
            this.schemaTypeChoiceBean = new SchemaTypeChoice();
        }
        else
        {
            this.schemaTypeChoiceBean = schemaTypeChoiceBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param schemaTypeChoiceBean bean containing the schema element properties
     */
    public AssetSchemaTypeChoice(AssetDescriptor  parentAsset,
                                 SchemaTypeChoice schemaTypeChoiceBean)
    {
        super(parentAsset, schemaTypeChoiceBean);

        if (schemaTypeChoiceBean == null)
        {
            this.schemaTypeChoiceBean = new SchemaTypeChoice();
        }
        else
        {
            this.schemaTypeChoiceBean = schemaTypeChoiceBean;
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
    public AssetSchemaTypeChoice(AssetDescriptor parentAsset,
                                 AssetSchemaTypeChoice templateSchemaElement)
    {
        super(parentAsset, templateSchemaElement);

        if (templateSchemaElement == null)
        {
            this.schemaTypeChoiceBean = new SchemaTypeChoice();
        }
        else
        {
            this.schemaTypeChoiceBean = templateSchemaElement.getSchemaTypeChoiceBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return  schema choice bean
     */
    protected SchemaTypeChoice getSchemaTypeChoiceBean()
    {
        return schemaTypeChoiceBean;
    }


    /**
     * Return the list of schema types that an instance of the schema attribute can use as its type.
     *
     * @return list of schema types
     */
    public List<AssetSchemaType> getSchemaTypeOptions()
    {
        List<AssetSchemaType> assetSchemaTypes = new ArrayList<>();

        if ((schemaTypeChoiceBean != null) && (schemaTypeChoiceBean.getSchemaOptions() != null))
        {
            for (SchemaType schemaTypeBean : schemaTypeChoiceBean.getSchemaOptions())
            {
                if (schemaTypeBean != null)
                {
                    assetSchemaTypes.add(AssetSchemaType.createAssetSchemaType(parentAsset, schemaTypeBean));
                }
            }
        }

        return assetSchemaTypes;
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
        return new AssetSchemaTypeChoice(parentAsset, this);
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
        return schemaTypeChoiceBean;
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(SchemaTypeChoice bean)
    {
        super.setBean(bean);
        this.schemaTypeChoiceBean = bean;
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
                ", schemaTypeOptions=" + getSchemaTypeOptions() +
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
        AssetSchemaTypeChoice that = (AssetSchemaTypeChoice) objectToCompare;
        return Objects.equals(schemaTypeChoiceBean, that.schemaTypeChoiceBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), schemaTypeChoiceBean);
    }
}