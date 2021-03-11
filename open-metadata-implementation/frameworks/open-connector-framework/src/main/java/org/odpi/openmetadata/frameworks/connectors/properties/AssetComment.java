/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;

import java.util.Objects;


/**
 * Stores information about a comment connected to an asset.  Comments provide informal feedback to assets
 * and can be added at any time.
 *
 * Comments have the userId of the person who added the feedback, along with their comment text.
 *
 * Comments can have other comments attached.
 *
 * The content of the comment is a personal statement (which is why the user's id is in the comment)
 * and there is no formal review of the content.
 */
public class AssetComment extends AssetElementHeader
{
    private static final long     serialVersionUID = 1L;

    protected Comment              commentBean;
    protected AssetCommentReplies  commentReplies = null;

    /**
     * Bean constructor
     *
     * @param commentBean bean that contains all of the properties
     * @param commentReplies any replies to this comment
     */
    public AssetComment(Comment              commentBean,
                        AssetCommentReplies  commentReplies)
    {
        super(commentBean);

        if (commentBean == null)
        {
            this.commentBean = new Comment();
        }
        else
        {
            this.commentBean = commentBean;
        }

        if (commentReplies == null)
        {
            this.commentReplies = null;
        }
        else
        {
            /*
             * Ensure comment replies has this object's parent asset, not the template's.
             */
            this.commentReplies = commentReplies.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Bean constructor with parent asset and replies
     *
     * @param parentAsset     descriptor for parent asset
     * @param commentBean bean that contains all of the properties
     * @param commentReplies any replies to this comment
     */
    public    AssetComment(AssetDescriptor      parentAsset,
                           Comment              commentBean,
                           AssetCommentReplies  commentReplies)
    {
        super(parentAsset, commentBean);

        if (commentBean == null)
        {
            this.commentBean = new Comment();
        }
        else
        {
            this.commentBean = commentBean;
        }

        if (commentReplies == null)
        {
            this.commentReplies = null;
        }
        else
        {
            /*
             * Ensure comment replies has this object's parent asset, not the template's.
             */
            this.commentReplies = commentReplies.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset     descriptor for parent asset
     * @param templateComment element to copy
     */
    public AssetComment(AssetDescriptor parentAsset, AssetComment templateComment)
    {
        super(parentAsset, templateComment);

        if (templateComment == null)
        {
            this.commentBean = new Comment();
        }
        else
        {
            this.commentBean = templateComment.getCommentBean();

            AssetCommentReplies templateCommentReplies = templateComment.getCommentReplies();
            if (templateCommentReplies != null)
            {
                /*
                 * Ensure comment replies has this object's parent asset, not the template's.
                 */
                commentReplies = templateCommentReplies.cloneIterator(super.getParentAsset());
            }
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return Comment bean
     */
    protected Comment  getCommentBean()
    {
        return commentBean;
    }


    /**
     * Return if this comment is private to the creating user.
     *
     * @return boolean
     */
    public boolean isPublic()
    {
        return commentBean.getIsPublic();
    }


    /**
     * Return an enum that describes the type of comment.
     *
     * @return CommentType enum
     */
    public CommentType getCommentType()
    {
        return commentBean.getCommentType();
    }


    /**
     * Return the user id of the person who created the comment.  Null means the user id is not known.
     *
     * @return String commenting user
     */
    public String getUser()
    {
        return commentBean.getUser();
    }


    /**
     * Return the comment text.
     *
     * @return String commentText
     */
    public String getCommentText()
    {
        return commentBean.getCommentText();
    }


    /**
     * Return an iterator of the replies to this comment - null means no replies are available.
     *
     * @return comment replies iterator
     */
    public AssetCommentReplies getCommentReplies()
    {
        if (commentReplies == null)
        {
            return null;
        }
        else
        {
            return commentReplies.cloneIterator(super.getParentAsset());
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
        return commentBean.toString();
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
        AssetComment that = (AssetComment) objectToCompare;
        return Objects.equals(commentBean, that.commentBean) &&
                       Objects.equals(commentReplies, that.commentReplies);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), commentBean, commentReplies);
    }
}