/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Location;

import java.util.Objects;


/**
 * AssetLocation describes where the asset is located.  The model allows a very flexible definition of location
 * that can be set up at different levels of granularity.
 */
public class AssetLocation extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected Location locationBean;


    /**
     * Bean constructor
     *
     * @param locationBean bean containing all of the properties
     */
    public AssetLocation(Location   locationBean)
    {
        super(locationBean);

        if (locationBean == null)
        {
            this.locationBean = new Location();
        }
        else
        {
            this.locationBean = locationBean;
        }
    }


    /**
     * Bean constructor and parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param locationBean bean containing all of the properties
     */
    public AssetLocation(AssetDescriptor  parentAsset,
                         Location         locationBean)
    {
        super(parentAsset, locationBean);

        if (locationBean == null)
        {
            this.locationBean = new Location();
        }
        else
        {
            this.locationBean = locationBean;
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param parentAsset description of the asset that this location is attached to.
     * @param templateLocation template object to copy.
     */
    public AssetLocation(AssetDescriptor  parentAsset, AssetLocation templateLocation)
    {
        super(parentAsset, templateLocation);

        if (templateLocation == null)
        {
            this.locationBean = new Location();
        }
        else
        {
            this.locationBean = templateLocation.getLocationBean();
        }
    }


    /**
     * Return the bean with all of the properties in it.
     *
     * @return location bean
     */
    protected Location getLocationBean()
    {
        return locationBean;
    }


    /**
     * Returns the stored display name property for the location.
     * If no display name is available then null is returned.
     *
     * @return displayName
     */
    public String getDisplayName() { return locationBean.getDisplayName(); }


    /**
     * Returns the stored description property for the location.
     * If no description is provided then null is returned.
     *
     * @return description
     */
    public String getDescription() { return locationBean.getDescription(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString() { return locationBean.toString(); }


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
        AssetLocation that = (AssetLocation) objectToCompare;
        return Objects.equals(locationBean, that.locationBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), locationBean);
    }
}