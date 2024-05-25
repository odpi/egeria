/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import java.util.Objects;

/**
 * Feedback contains the comments, tags, ratings and likes that consumers of the asset have created.
 */
public class AssetFeedback extends AssetPropertyElementBase
{
    /*
     * Lists of objects that make up the feedback on the asset.
     */
    protected InformalTags informalTags = null;
    protected Likes        likes        = null;
    protected Ratings      ratings      = null;
    protected Comments     comments     = null;


    /**
     * Simple constructor only for subclasses
     */
    protected AssetFeedback()
    {
        super();
    }


    /**
     * Typical Constructor
     *
     * @param informalTags list of tags for the asset.
     * @param likes list of likes (one object per person liking the asset) for the asset.
     * @param ratings list of ratings that people have given the asset one Rating object for each person's rating.
     * @param comments list of comments for the asset.
     */
    public AssetFeedback(InformalTags informalTags,
                         Likes        likes,
                         Ratings      ratings,
                         Comments     comments)
    {
        super();

        this.informalTags = informalTags;
        this.likes = likes;
        this.ratings = ratings;
        this.comments = comments;
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateFeedback template object to copy.
     */
    public AssetFeedback(AssetFeedback templateFeedback)
    {
        super(templateFeedback);

        /*
         * Only create a child object if the template is not null.
         */
        if (templateFeedback != null)
        {
            InformalTags templateInformalTags = templateFeedback.getInformalTags();
            Likes        templateLikes        = templateFeedback.getLikes();
            Ratings      templateRatings      = templateFeedback.getRatings();
            Comments     templateComments = templateFeedback.getComments();

            if (templateInformalTags != null)
            {
                this.informalTags = templateInformalTags.cloneIterator();
            }

            if (templateLikes != null)
            {
                this.likes = templateLikes.cloneIterator();
            }

            if (templateRatings != null)
            {
                this.ratings = templateRatings.cloneIterator();
            }

            if (templateComments != null)
            {
                this.comments = templateComments.cloneIterator();
            }
        }

    }


    /**
     * Returns a copy of the information tags for the asset in an iterator.  This iterator can be used to step
     * through the tags once.  Therefore call getInformalTags() for each scan of the asset's tags.
     *
     * @return InformalTags tag list
     */
    public InformalTags getInformalTags()
    {
        if (informalTags == null)
        {
            return null;
        }
        else
        {
            return informalTags.cloneIterator();
        }
    }


    /**
     * Returns a copy of the likes for the asset in an iterator.  This iterator can be used to step
     * through the list of like once.  Therefore call getLikes() for each scan of the asset's like objects.
     *
     * @return Likes like object list
     */
    public Likes getLikes()
    {
        if (likes == null)
        {
            return likes;
        }
        else
        {
            return likes.cloneIterator();
        }
    }


    /**
     * Returns a copy of the ratings for the asset in an iterator.  This iterator can be used to step
     * through the ratings once.  Therefore call getRatings() for each scan of the asset's ratings.
     *
     * @return Ratings rating list
     */
    public Ratings getRatings()
    {
        if (ratings == null)
        {
            return ratings;
        }
        else
        {
            return ratings.cloneIterator();
        }
    }


    /**
     * Returns a copy of the comments for the asset in an iterator.  This iterator can be used to step
     * through the comments once.  Therefore, call getComments() for each scan of the asset's comments.
     *
     * @return Comments comment list
     */
    public Comments getComments()
    {
        if (comments == null)
        {
            return null;
        }
        else
        {
            return comments.cloneIterator();
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
        return "AssetFeedback{" +
                "informalTags=" + informalTags +
                ", likes=" + likes +
                ", ratings=" + ratings +
                ", comments=" + comments +
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
        AssetFeedback that = (AssetFeedback) objectToCompare;
        return Objects.equals(informalTags, that.informalTags) &&
                       Objects.equals(likes, that.likes) &&
                       Objects.equals(ratings, that.ratings) &&
                       Objects.equals(comments, that.comments);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(informalTags, likes, ratings, comments);
    }
}