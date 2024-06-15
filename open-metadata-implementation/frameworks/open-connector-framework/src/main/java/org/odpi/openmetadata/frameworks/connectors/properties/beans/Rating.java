/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Stores information about a rating connected to an asset.  Ratings provide informal feedback on the quality of assets
 * and can be added at any time.
 * Ratings have the userId of the person who added it, a star rating and an optional review comment.
 * The content of the rating is a personal judgement (which is why the user's id is in the object)
 * and there is no formal review of the ratings.  However, they can be used as a basis for crowd-sourcing
 * feedback to asset owners.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Rating extends ElementBase
{
    /*
     * Attributes of a Rating
     */
    protected StarRating starRating = null;
    protected String     review     = null;
    protected String     user       = null;
    protected boolean    isPublic   = false;

    /**
     * Default constructor
     */
    public Rating()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public Rating(Rating template)
    {
        super(template);

        if (template != null)
        {
            user = template.getUser();
            starRating = template.getStarRating();
            review = template.getReview();
            isPublic = template.isPublic;
        }
    }


    /**
     * Return the user id of the person who created the rating.  Null means the user id is not known.
     *
     * @return String user
     */
    public String getUser() {
        return user;
    }


    /**
     * Set up the user id of the person who created the rating.  Null means the user id is not known.
     *
     * @param user string
     */
    public void setUser(String user)
    {
        this.user = user;
    }


    /**
     * Return the stars for the rating.
     *
     * @return StarRating enum
     */
    public StarRating getStarRating() {
        return starRating;
    }


    /**
     * Set up the stars for the rating.
     *
     * @param starRating StarRating enum
     */
    public void setStarRating(StarRating starRating)
    {
        this.starRating = starRating;
    }


    /**
     * Return the review comments - null means no review is available.
     *
     * @return String review comments
     */
    public String getReview()
    {
        return review;
    }


    /**
     * Set up the review comments - null means no review is available.
     *
     * @param review String review comments
     */
    public void setReview(String review)
    {
        this.review = review;
    }


    /**
     * Return if this rating is private ot the creating user.
     *
     * @return boolean
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up whether the rating is private to the creating user or not.
     *
     * @param aPublic boolean
     */
    public void setIsPublic(boolean aPublic)
    {
        isPublic = aPublic;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Rating{" +
                       "extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", starRating=" + starRating +
                       ", review='" + review + '\'' +
                       ", user='" + user + '\'' +
                       ", isPublic=" + isPublic +
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
        Rating rating = (Rating) objectToCompare;
        return isPublic == rating.isPublic &&
                       getStarRating() == rating.getStarRating() &&
                       Objects.equals(getReview(), rating.getReview()) &&
                       Objects.equals(getUser(), rating.getUser());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getStarRating(), getReview(), getUser(), isPublic);
    }
}