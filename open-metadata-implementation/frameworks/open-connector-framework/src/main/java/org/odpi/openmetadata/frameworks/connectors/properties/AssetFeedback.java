/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import java.util.Objects;

/**
 * Feedback contains the comments, tags, ratings and likes that consumers of the asset have created.
 */
public class AssetFeedback extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    /*
     * Lists of objects that make up the feedback on the asset.
     */
    protected AssetInformalTags informalTags = null;
    protected AssetLikes        likes        = null;
    protected AssetRatings      ratings      = null;
    protected AssetComments     comments     = null;


    /**
     * Simple constructor only for subclasses
     *
     * @param parentAsset linked asset
     */
    protected AssetFeedback(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Typical Constructor
     *
     * @param parentAsset description of the asset that this feedback is attached to.
     * @param informalTags list of tags for the asset.
     * @param likes list of likes (one object per person liking the asset) for the asset.
     * @param ratings list of ratings that people have given the asset one Rating object for each person's rating.
     * @param comments list of comments for the asset.
     */
    public AssetFeedback(AssetDescriptor parentAsset,
                         AssetInformalTags informalTags,
                         AssetLikes likes,
                         AssetRatings ratings,
                         AssetComments comments)
    {
        super(parentAsset);

        this.informalTags = informalTags;
        this.likes = likes;
        this.ratings = ratings;
        this.comments = comments;
    }


    /**
     * Copy/clone constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the feedback clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this feedback is attached to.
     * @param templateFeedback template object to copy.
     */
    public AssetFeedback(AssetDescriptor parentAsset, AssetFeedback templateFeedback)
    {
        super(parentAsset, templateFeedback);

        /*
         * Only create a child object if the template is not null.
         */
        if (templateFeedback != null)
        {
            AssetInformalTags templateInformalTags = templateFeedback.getInformalTags();
            AssetLikes        templateLikes        = templateFeedback.getLikes();
            AssetRatings      templateRatings      = templateFeedback.getRatings();
            AssetComments     templateComments     = templateFeedback.getComments();

            if (templateInformalTags != null)
            {
                this.informalTags = templateInformalTags.cloneIterator(parentAsset);
            }

            if (templateLikes != null)
            {
                this.likes = templateLikes.cloneIterator(parentAsset);
            }

            if (templateRatings != null)
            {
                this.ratings = templateRatings.cloneIterator(parentAsset);
            }

            if (templateComments != null)
            {
                this.comments = templateComments.cloneIterator(parentAsset);
            }
        }

    }


    /**
     * Returns a copy of the information tags for the asset in an iterator.  This iterator can be used to step
     * through the tags once.  Therefore call getInformalTags() for each scan of the asset's tags.
     *
     * @return InformalTags tag list
     */
    public AssetInformalTags getInformalTags()
    {
        if (informalTags == null)
        {
            return null;
        }
        else
        {
            return informalTags.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Returns a copy of the likes for the asset in an iterator.  This iterator can be used to step
     * through the list of like once.  Therefore call getLikes() for each scan of the asset's like objects.
     *
     * @return Likes like object list
     */
    public AssetLikes getLikes()
    {
        if (likes == null)
        {
            return likes;
        }
        else
        {
            return likes.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Returns a copy of the ratings for the asset in an iterator.  This iterator can be used to step
     * through the ratings once.  Therefore call getRatings() for each scan of the asset's ratings.
     *
     * @return Ratings rating list
     */
    public AssetRatings getRatings()
    {
        if (ratings == null)
        {
            return ratings;
        }
        else
        {
            return ratings.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Returns a copy of the comments for the asset in an iterator.  This iterator can be used to step
     * through the comments once.  Therefore call getComments() for each scan of the asset's comments.
     *
     * @return Comments comment list
     */
    public AssetComments getComments()
    {
        if (comments == null)
        {
            return comments;
        }
        else
        {
            return comments.cloneIterator(super.getParentAsset());
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