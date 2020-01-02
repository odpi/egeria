/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Review records a star rating and review comment for an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Review extends UserFeedbackHeader
{
    private static final long    serialVersionUID = 1L;

    private StarRating  stars = null;
    private String      review = null;


    /**
     * Default constructor
     */
    public Review()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Review(Review template)
    {
        super(template);

        if (template != null)
        {
            stars = template.getStars();
            review = template.getReview();
        }
    }


    /**
     * Return the number of stars awarded.
     *
     *
     * @return star rating
     */
    public StarRating getStars()
    {
        return stars;
    }


    /**
     * Set up the number of stars awarded.
     *
     * @param stars star rating
     */
    public void setStars(StarRating stars)
    {
        this.stars = stars;
    }


    /**
     * Return an optional review comment.
     *
     * @return text
     */
    public String getReview()
    {
        return review;
    }


    /**
     * Set up an optional review comment.
     *
     * @param review text
     */
    public void setReview(String review)
    {
        this.review = review;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "Review{" +
                "stars=" + stars +
                ", review='" + review + '\'' +
                ", userId='" + getUserId() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        Review review1 = (Review) objectToCompare;
        return getStars() == review1.getStars() &&
                Objects.equals(getReview(), review1.getReview());
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getStars(), getReview());
    }
}
