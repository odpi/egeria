/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Rating;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;

import java.util.Objects;

/**
 * Stores information about a rating connected to an asset.  Ratings provide informal feedback on the quality of assets
 * and can be added at any time.
 *
 * Ratings have the userId of the person who added it, a star rating and an optional review comment.
 *
 * The content of the rating is a personal judgement (which is why the user's id is in the object)
 * and there is no formal review of the ratings.  However, they can be used as a basis for crowd-sourcing
 * feedback to asset owners.
 */
public class AssetRating extends AssetElementHeader
{
    private static final long     serialVersionUID = 1L;

    protected Rating ratingBean;


    /**
     * Bean constructor
     *
     * @param ratingBean bean containing all of the properties
     */
    public AssetRating(Rating ratingBean)
    {
        super(ratingBean);

        if (ratingBean == null)
        {
            this.ratingBean = new Rating();
        }
        else
        {
            this.ratingBean = ratingBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param ratingBean bean containing all of the properties
     */
    public AssetRating(AssetDescriptor parentAsset,
                       Rating          ratingBean)
    {
        super(parentAsset, ratingBean);

        if (ratingBean == null)
        {
            this.ratingBean = new Rating();
        }
        else
        {
            this.ratingBean = ratingBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateRating element to copy
     */
    public AssetRating(AssetDescriptor parentAsset, AssetRating templateRating)
    {
        /*
         * Save the parent asset description.
         */
        super(parentAsset, templateRating);

        if (templateRating == null)
        {
            this.ratingBean = new Rating();
        }
        else
        {
            this.ratingBean = templateRating.getRatingBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return rating bean
     */
    protected Rating  getRatingBean()
    {
        return ratingBean;
    }


    /**
     * Return if this rating is private to the creating user.
     *
     * @return boolean
     */
    public boolean isPublic()
    {
        return ratingBean.getIsPublic();
    }


    /**
     * Return the user id of the person who created the rating.  Null means the user id is not known.
     *
     * @return String user
     */
    public String getUser() { return ratingBean.getUser(); }


    /**
     * Return the stars for the rating.
     *
     * @return StarRating starRating
     */
    public StarRating getStarRating() { return ratingBean.getStarRating(); }


    /**
     * Return the review comments - null means no review is available.
     *
     * @return String review comments
     */
    public String getReview() { return ratingBean.getReview(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString() { return ratingBean.toString(); }


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
        AssetRating that = (AssetRating) objectToCompare;
        return Objects.equals(ratingBean, that.ratingBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), ratingBean);
    }
}