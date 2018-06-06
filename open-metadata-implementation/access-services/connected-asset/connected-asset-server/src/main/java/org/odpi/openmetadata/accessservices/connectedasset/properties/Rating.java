/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

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
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Rating extends ElementHeader
{
    /*
     * Attributes of a Rating
     */
    private StarRating starRating = null;
    private String     review = null;
    private String     user = null;


    /**
     * Default Constructor
     */
    public Rating()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateRating - element to copy
     */
    public Rating(Rating templateRating)
    {
        /*
         * Save the parent asset description.
         */
        super(templateRating);

        if (templateRating != null)
        {
            /*
             * Copy the values from the supplied tag.
             */
            user = templateRating.getUser();
            starRating = templateRating.getStarRating();
            review = templateRating.getReview();
        }
    }


    /**
     * Return the user id of the person who created the rating.  Null means the user id is not known.
     *
     * @return String - user
     */
    public String getUser() {
        return user;
    }


    /**
     * Set up the user id of the person who created the rating. Null means the user id is not known.
     *
     * @param user - String - user id of person providing the rating
     */
    public void setUser(String user) {
        this.user = user;
    }


    /**
     * Return the stars for the rating.
     *
     * @return StarRating - starRating
     */
    public StarRating getStarRating() {
        return starRating;
    }


    /**
     * Set up the star value for the rating. Null means no rating is supplied
     *
     * @param starRating - StarRating enum
     */
    public void setStarRating(StarRating starRating) { this.starRating = starRating; }

    /**
     * Return the review comments - null means no review is available.
     *
     * @return String - review comments
     */
    public String getReview()
    {
        return review;
    }


    /**
     * Set up the review comments - null means no review is available.
     *
     * @param review - String - review comments
     */
    public void setReview(String review) {
        this.review = review;
    }
}