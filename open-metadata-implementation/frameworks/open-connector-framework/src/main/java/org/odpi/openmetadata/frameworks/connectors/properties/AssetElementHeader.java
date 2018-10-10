/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ElementHeader provides the common identifier and type information for all properties objects
 * that link off of the asset and have a guid associated with them.  This typically means it is
 * represented by an entity in the metadata repository.
 */
public abstract class AssetElementHeader extends AssetPropertyBase
{
    private ElementHeader   elementHeaderBean;

    /**
     * Bean constructor
     *
     * @param elementHeaderBean bean containing all to the properties
     */
    protected AssetElementHeader(ElementHeader   elementHeaderBean)
    {
        super(null);

        if (elementHeaderBean == null)
        {
            this.elementHeaderBean = new ElementHeader();
        }
        else
        {
            this.elementHeaderBean = elementHeaderBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param elementHeaderBean bean containing properties
     */
    protected AssetElementHeader(AssetDescriptor parentAsset,
                                 ElementHeader   elementHeaderBean)
    {
        super(parentAsset);

        if (elementHeaderBean == null)
        {
            this.elementHeaderBean = new ElementHeader();
        }
        else
        {
            this.elementHeaderBean = elementHeaderBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param assetElementHeader element to copy
     */
    protected AssetElementHeader(AssetDescriptor parentAsset, AssetElementHeader assetElementHeader)
    {
        super(parentAsset, assetElementHeader);

        if (assetElementHeader == null)
        {
            elementHeaderBean = new ElementHeader();
        }
        else
        {
            this.elementHeaderBean = assetElementHeader.getElementHeaderBean();
        }
    }


    /**
     * Return the element header bean - used during cloning
     *
     * @return bean
     */
    protected  ElementHeader getElementHeaderBean()
    {
        return elementHeaderBean;
    }


    /**
     * Return the element type properties for this properties object.  These values are set up by the metadata repository
     * and define details to the metadata entity used to represent this element.
     *
     * @return AssetElementType type information.
     */
    public AssetElementType getType()
    {
        ElementType   elementTypeBean = elementHeaderBean.getType();

        if (elementTypeBean == null)
        {
            return null;
        }
        else
        {
            return new AssetElementType(elementTypeBean);
        }
    }


    /**
     * Return the unique id for the properties object.  Null means no guid is assigned.
     *
     * @return String unique id
     */
    public String getGUID()
    {
        return elementHeaderBean.getGUID();
    }


    /**
     * Returns the URL to access the properties object in the metadata repository.
     * If no url is available then null is returned.
     *
     * @return String URL
     */
    public String getURL()
    {
        return elementHeaderBean.getURL();
    }


    /**
     * Return the list of classifications associated with the asset.
     *
     * @return Classifications  list of classifications
     */
    public List<AssetClassification> getAssetClassifications()
    {
        List<Classification>   classifications = elementHeaderBean.getClassifications();

        if (classifications == null)
        {
            return null;
        }
        else
        {
            List<AssetClassification>    assetClassifications = new ArrayList<>();

            for (Classification classification : classifications)
            {
                if (classification != null)
                {
                    assetClassifications.add(new AssetClassification(this.getParentAsset(), classification));
                }
            }

            if (assetClassifications.isEmpty())
            {
                return null;
            }
            else
            {
                return assetClassifications;
            }
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return elementHeaderBean.toString();
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
        if (!(objectToCompare instanceof AssetElementHeader))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetElementHeader that = (AssetElementHeader) objectToCompare;
        return Objects.equals(getElementHeaderBean(), that.getElementHeaderBean());
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return elementHeaderBean.hashCode();
    }
}