/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RatingRequestBody provides a structure for passing star rating as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RatingRequestBody extends FeedbackRequestBody
{
    private static final long    serialVersionUID = 1L;

    private StarRating starRating = null;
    private String     review     = null;


    /**
     * Default constructor
     */
    public RatingRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RatingRequestBody(RatingRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.starRating = template.getStarRating();
            this.review = template.getReview();
        }
    }


    /**
     * Return the star rating.
     *
     * @return enum
     */
    public StarRating getStarRating()
    {
        return starRating;
    }


    /**
     * Set up the star rating.
     *
     * @param starRating enum
     */
    public void setStarRating(StarRating starRating)
    {
        this.starRating = starRating;
    }


    /**
     * Return the attached review (if any)
     *
     * @return text
     */
    public String getReview()
    {
        return review;
    }


    /**
     * Set up the attached review (if any)
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
        return "RatingRequestBody{" +
                "starRating=" + starRating +
                ", review='" + review + '\'' +
                ", isPublic=" + isPublic() +
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
        RatingRequestBody that = (RatingRequestBody) objectToCompare;
        return isPublic() == that.isPublic() &&
                getStarRating() == that.getStarRating() &&
                Objects.equals(getReview(), that.getReview());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getStarRating(), getReview(), isPublic());
    }
}
