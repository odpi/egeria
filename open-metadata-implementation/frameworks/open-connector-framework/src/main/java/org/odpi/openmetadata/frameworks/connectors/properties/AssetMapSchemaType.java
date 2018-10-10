/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.MapSchemaElement;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaElement;

import java.util.Objects;


/**
 * MapSchemaElement describes a schema element of type map.  It stores the type of schema element for the domain
 * (eg property name) for the map and the schema element for the range (eg property value) for the map.
 */
public class AssetMapSchemaType extends AssetSchemaType
{
    protected MapSchemaElement mapSchemaElementBean;
    protected AssetSchemaType  mapFromElement;
    protected AssetSchemaType  mapToElement;

    /**
     * Bean constructor
     *
     * @param mapSchemaElementBean bean containing the schema properties
     * @param mapFromElement schema element that represents the property name for the map.
     * @param mapToElement schema element that represents the property value for the map.
     * @param assetMeanings iterator for the asset assetMeanings
     */
    public AssetMapSchemaType(MapSchemaElement   mapSchemaElementBean,
                              AssetSchemaType mapFromElement,
                              AssetSchemaType mapToElement,
                              AssetMeanings      assetMeanings)
    {
        super(mapSchemaElementBean, assetMeanings);

        if (mapSchemaElementBean == null)
        {
            this.mapSchemaElementBean = new MapSchemaElement();
        }
        else
        {
            this.mapSchemaElementBean = mapSchemaElementBean;
        }

        if (mapFromElement == null)
        {
            this.mapFromElement = null;
        }
        else
        {
            this.mapFromElement = mapFromElement.cloneAssetSchemaElement(super.getParentAsset());
        }

        if (mapToElement == null)
        {
            this.mapToElement = null;
        }
        else
        {
            this.mapToElement = mapToElement.cloneAssetSchemaElement(super.getParentAsset());
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param mapSchemaElementBean bean containing the schema properties
     * @param mapFromElement schema element that represents the property name for the map.
     * @param mapToElement schema element that represents the property value for the map.
     * @param assetMeanings iterator for the asset assetMeanings
     */
    public AssetMapSchemaType(AssetDescriptor    parentAsset,
                              MapSchemaElement   mapSchemaElementBean,
                              AssetSchemaType mapFromElement,
                              AssetSchemaType mapToElement,
                              AssetMeanings      assetMeanings)
    {
        super(parentAsset, mapSchemaElementBean, assetMeanings);

        if (mapSchemaElementBean == null)
        {
            this.mapSchemaElementBean = new MapSchemaElement();
        }
        else
        {
            this.mapSchemaElementBean = mapSchemaElementBean;
        }

        if (mapFromElement == null)
        {
            this.mapFromElement = null;
        }
        else
        {
            this.mapFromElement = mapFromElement.cloneAssetSchemaElement(super.getParentAsset());
        }

        if (mapToElement == null)
        {
            this.mapToElement = null;
        }
        else
        {
            this.mapToElement = mapToElement.cloneAssetSchemaElement(super.getParentAsset());
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
    public AssetMapSchemaType(AssetDescriptor  parentAsset, AssetMapSchemaType templateSchema)
    {
        super(parentAsset, templateSchema);

        if (templateSchema == null)
        {
            this.mapSchemaElementBean = new MapSchemaElement();
            this.mapFromElement = null;
            this.mapToElement = null;
        }
        else
        {
            this.mapSchemaElementBean = templateSchema.getMapSchemaElementBean();

            AssetSchemaType templateMapFromElement = templateSchema.getMapFromElement();
            AssetSchemaType templateMapToElement   = templateSchema.getMapToElement();

            if (templateMapFromElement == null)
            {
                this.mapFromElement = null;
            }
            else
            {
                this.mapFromElement = templateMapFromElement.cloneAssetSchemaElement(super.getParentAsset());
            }

            if (templateMapToElement == null)
            {
                this.mapToElement = null;
            }
            else
            {
                this.mapToElement = templateMapToElement.cloneAssetSchemaElement(super.getParentAsset());
            }
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return map schema element bean
     */
    protected MapSchemaElement  getMapSchemaElementBean()
    {
        return mapSchemaElementBean;
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
            return mapFromElement.cloneAssetSchemaElement(super.getParentAsset());
        }
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
            return mapToElement.cloneAssetSchemaElement(super.getParentAsset());
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
    protected AssetSchemaType cloneAssetSchemaElement(AssetDescriptor  parentAsset)
    {
        return new AssetMapSchemaType(parentAsset, this);
    }


    /**
     * Return this schema element bean.  This method is needed because SchemaElement
     * is abstract.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    @Override
    protected SchemaElement getSchemaElementBean()
    {
        return mapSchemaElementBean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return mapSchemaElementBean.toString();
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
        return Objects.equals(getMapSchemaElementBean(), that.getMapSchemaElementBean()) &&
                Objects.equals(getMapFromElement(), that.getMapFromElement()) &&
                Objects.equals(getMapToElement(), that.getMapToElement());
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getMapSchemaElementBean(), getMapFromElement(), getMapToElement());
    }
}