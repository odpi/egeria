/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.MapSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Objects;


/**
 * AssetMapSchemaType describes a schema element of type map.  It stores the type of schema element for the domain
 * (eg property name) for the map and the schema element for the range (eg property value) for the map.
 */
public class AssetMapSchemaType extends AssetSchemaType
{
    protected MapSchemaType   mapSchemaTypeBean = null;
    protected AssetSchemaType mapFromElement    = null;
    protected AssetSchemaType mapToElement      = null;


    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetMapSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     * @param schemaType bean containing the schema properties
     */
    public AssetMapSchemaType(AssetDescriptor parentAsset,
                              MapSchemaType   schemaType)
    {
        super(parentAsset, schemaType);
        mapSchemaTypeBean = schemaType;
    }


    /**
     * Bean constructor
     *
     * @param schemaType bean containing the schema properties
     * @param mapFromElement schema element that represents the property name for the map.
     * @param mapToElement schema element that represents the property value for the map.
     */
    public AssetMapSchemaType(MapSchemaType   schemaType,
                              AssetSchemaType mapFromElement,
                              AssetSchemaType mapToElement)
    {
        super(schemaType);

        if (mapFromElement == null)
        {
            this.mapFromElement = null;
        }
        else
        {
            this.mapFromElement = mapFromElement.cloneAssetSchemaType(super.getParentAsset());
        }

        if (mapToElement == null)
        {
            this.mapToElement = null;
        }
        else
        {
            this.mapToElement = mapToElement.cloneAssetSchemaType(super.getParentAsset());
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param schemaTypeBean bean containing the schema properties
     * @param mapFromElement schema element that represents the property name for the map.
     * @param mapToElement schema element that represents the property value for the map.
     */
    public AssetMapSchemaType(AssetDescriptor parentAsset,
                              SchemaType schemaTypeBean,
                              AssetSchemaType mapFromElement,
                              AssetSchemaType mapToElement)
    {
        super(parentAsset, schemaTypeBean);

        if (mapFromElement == null)
        {
            this.mapFromElement = null;
        }
        else
        {
            this.mapFromElement = mapFromElement.cloneAssetSchemaType(super.getParentAsset());
        }

        if (mapToElement == null)
        {
            this.mapToElement = null;
        }
        else
        {
            this.mapToElement = mapToElement.cloneAssetSchemaType(super.getParentAsset());
        }
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this map is attached to.
     * @param templateSchema template object to copy.
     */
    public AssetMapSchemaType(AssetDescriptor parentAsset, AssetMapSchemaType templateSchema)
    {
        super(parentAsset, templateSchema);

        if (templateSchema == null)
        {
            this.mapFromElement = null;
            this.mapToElement = null;
        }
        else
        {
            AssetSchemaType templateMapFromElement = templateSchema.getMapFromElement();
            AssetSchemaType templateMapToElement   = templateSchema.getMapToElement();

            if (templateMapFromElement == null)
            {
                this.mapFromElement = null;
            }
            else
            {
                this.mapFromElement = templateMapFromElement.cloneAssetSchemaType(super.getParentAsset());
            }

            if (templateMapToElement == null)
            {
                this.mapToElement = null;
            }
            else
            {
                this.mapToElement = templateMapToElement.cloneAssetSchemaType(super.getParentAsset());
            }
        }
    }



    /**
     * Return the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @return SchemaElement
     */
    public AssetSchemaType getMapFromElement()
    {
        if (mapFromElement == null)
        {
            return null;
        }
        else
        {
            return mapFromElement.cloneAssetSchemaType(super.getParentAsset());
        }
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(MapSchemaType bean)
    {
        super.setBean(bean);
        mapSchemaTypeBean = bean;
    }


    /**
     * Return the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @return SchemaElement
     */
    public AssetSchemaType getMapToElement()
    {
        if (mapToElement == null)
        {
            return null;
        }
        else
        {
            return mapToElement.cloneAssetSchemaType(super.getParentAsset());
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
        return new AssetMapSchemaType(parentAsset, this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetMapSchemaType{" +
                "mapFromElement=" + mapFromElement +
                ", mapToElement=" + mapToElement +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", assetClassifications=" + getAssetClassifications() +
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
        if (!(objectToCompare instanceof AssetMapSchemaType))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetMapSchemaType that = (AssetMapSchemaType) objectToCompare;
        return  Objects.equals(getMapFromElement(), that.getMapFromElement()) &&
                Objects.equals(getMapToElement(), that.getMapToElement());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMapFromElement(), getMapToElement());
    }
}