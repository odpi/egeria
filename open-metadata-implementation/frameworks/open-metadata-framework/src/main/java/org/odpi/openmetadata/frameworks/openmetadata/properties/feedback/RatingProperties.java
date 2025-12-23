/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.feedback;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RatingProperties stores information about a rating connected to an asset.  Ratings provide informal feedback on the quality of assets
 * and can be added at any time.
 * <br><br>
 * Ratings have the userId of the person who added it, a star rating and an optional review comment.
 * <br><br>
 * The content of the rating is a personal judgement (which is why the user's id is in the object)
 * and there is no formal review of the ratings.  However, they can be used as a basis for crowd-sourcing
 * feedback to asset owners.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RatingProperties extends OpenMetadataRootProperties
{
    private StarRating starRating = null;
    private String     review     = null;

    /**
     * Default constructor
     */
    public RatingProperties()
    {
        super();
        super.typeName = OpenMetadataType.RATING.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public RatingProperties(RatingProperties template)
    {
        super(template);

        if (template != null)
        {
            starRating = template.getStarRating();
            review = template.getReview();
        }
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RatingProperties{" +
                "starRating=" + starRating +
                ", review='" + review + '\'' +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        RatingProperties rating = (RatingProperties) objectToCompare;
        return getStarRating() == rating.getStarRating() &&
                Objects.equals(getReview(), rating.getReview());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), starRating, review);
    }
}