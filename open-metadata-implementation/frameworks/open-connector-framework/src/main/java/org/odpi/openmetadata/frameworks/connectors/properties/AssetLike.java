/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Like;

import java.util.Objects;


/**
 * The Like properties object records a single user's "like" of an asset.
 */
public class AssetLike extends AssetElementHeader
{
    private static final long     serialVersionUID = 1L;

    protected Like likeBean = null;


    /**
     * Bean constructor
     *
     * @param likeBean bean containing the like properties
     */
    public AssetLike(Like  likeBean)
    {
        super(likeBean);

        if (likeBean == null)
        {
            this.likeBean = new Like();
        }
        else
        {
            this.likeBean = likeBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset   descriptor for parent asset
     * @param likeBean bean containing the like properties
     */
    public AssetLike(AssetDescriptor      parentAsset,
                     Like                 likeBean)
    {
        super(parentAsset, likeBean);

        if (likeBean == null)
        {
            this.likeBean = new Like();
        }
        else
        {
            this.likeBean = likeBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset   descriptor for parent asset
     * @param templateLike   element to copy
     */
    public AssetLike(AssetDescriptor parentAsset, AssetLike templateLike)
    {
        super(parentAsset, templateLike);

        if (templateLike == null)
        {
            this.likeBean = new Like();
        }
        else
        {
            this.likeBean = templateLike.getLikeBean();
        }
    }


    /**
     * Return the bean with the properties.
     *
     * @return like bean
     */
    protected  Like  getLikeBean()
    {
        return likeBean;
    }


    /**
     * Return if this like is private to the creating user.
     *
     * @return boolean
     */
    public boolean isPublic()
    {
        return likeBean.getIsPublic();
    }


    /**
     * Return the user id of the person who created the like.  Null means the user id is not known.
     *
     * @return String   liking user
     */
    public String getUser() { return likeBean.getUser(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return likeBean.toString();
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
        AssetLike assetLike = (AssetLike) objectToCompare;
        return Objects.equals(likeBean, assetLike.likeBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), likeBean);
    }
}