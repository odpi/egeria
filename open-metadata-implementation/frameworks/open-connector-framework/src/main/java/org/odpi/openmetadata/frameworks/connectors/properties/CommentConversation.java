/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;

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
public class CommentConversation extends Comment
{
    private static final long     serialVersionUID = 1L;

    protected CommentReplies commentReplies = null;

    /**
     * Bean constructor
     *
     * @param commentBean bean that contains all the properties
     * @param commentReplies any replies to this comment
     */
    public CommentConversation(Comment        commentBean,
                               CommentReplies commentReplies)
    {
        super(commentBean);

        if (commentReplies == null)
        {
            this.commentReplies = null;
        }
        else
        {
            this.commentReplies = commentReplies.cloneIterator();
        }
    }



    /**
     * Copy/clone constructor.
     *
     * @param templateComment element to copy
     */
    public CommentConversation(CommentConversation templateComment)
    {
        super(templateComment);

        if (templateComment != null)
        {
            commentReplies = templateComment.getCommentReplies();
        }
    }



    /**
     * Copy/clone constructor.
     *
     * @param templateComment element to copy
     */
    public CommentConversation(Comment templateComment)
    {
        super(templateComment);
    }


    /**
     * Return an iterator of the replies to this comment - null means no replies are available.
     *
     * @return comment replies iterator
     */
    public CommentReplies getCommentReplies()
    {
        if (commentReplies == null)
        {
            return null;
        }
        else
        {
            return commentReplies.cloneIterator();
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
        return "CommentConversation{" +
                       "commentReplies=" + commentReplies +
                       ", commentType=" + getCommentType() +
                       ", commentText='" + getCommentText() + '\'' +
                       ", user='" + getUser() + '\'' +
                       ", isPublic=" + getIsPublic() +
                       ", URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        CommentConversation that = (CommentConversation) objectToCompare;
        return Objects.equals(commentReplies, that.commentReplies);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), commentReplies);
    }
}