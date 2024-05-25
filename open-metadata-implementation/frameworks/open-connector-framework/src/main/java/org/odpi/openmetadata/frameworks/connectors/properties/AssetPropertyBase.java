/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import java.util.Objects;

/**
 * The AssetPropertyBase class is a base class for all properties that link off of the connected asset.
 * It manages the information about the parent asset.
 */
public abstract class AssetPropertyBase extends AssetPropertyElementBase
{
    protected AssetDescriptor parentAsset;


    /**
     * Typical constructor that sets the link to the connected asset to null
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetPropertyBase(AssetDescriptor parentAsset)
    {
        /*
         * Initialize superclass and save the parent asset.
         */
        super();
        this.parentAsset = parentAsset;
    }


    /**
     * Copy/clone constructor sets up details of the parent asset from the template
     *
     * @param parentAsset descriptor of asset that his property relates to.
     * @param  template AssetPropertyBase to copy
     */
    protected AssetPropertyBase(AssetDescriptor parentAsset, AssetPropertyBase template)
    {
        /*
         * Initialize superclass and save the parentAsset
         */
        super(template);
        this.parentAsset = parentAsset;
    }


    /**
     * Return the asset descriptor of the parent asset.
     *
     * @return AssetDescriptor
     */
    protected AssetDescriptor getParentAsset() { return parentAsset; }


    /**
     * Return the name of the connected asset that this property is connected to.
     *
     * @return String name of the connected asset
     */
    protected String getParentAssetName()
    {
        String  parentAssetName = "<Unknown>";

        if (parentAsset != null)
        {
            parentAssetName = parentAsset.getAssetName();
        }

        return parentAssetName;
    }


    /**
     * Return the type of the connected asset that this property relates to.
     *
     * @return String name of the connected asset's type.
     */
    protected String getParentAssetTypeName()
    {
        String  parentAssetTypeName = "<Unknown>";

        if (parentAsset != null)
        {
            parentAssetTypeName = parentAsset.getAssetTypeName();
        }

        return parentAssetTypeName;
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
        AssetPropertyBase that = (AssetPropertyBase) objectToCompare;
        return Objects.equals(parentAsset, that.parentAsset);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(parentAsset);
    }
}