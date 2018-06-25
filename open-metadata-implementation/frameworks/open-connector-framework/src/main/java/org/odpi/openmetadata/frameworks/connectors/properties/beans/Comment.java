/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Comment bean stores information about a comment connected to an asset.  Comments provide informal feedback to assets
 * and can be added at any time.
 *
 * Comments have the userId of the person who added the feedback, along with their comment text.
 *
 * The content of the comment is a personal statement (which is why the user's id is in the comment)
 * and there is no formal review of the content.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Comment extends Referenceable
{
    protected CommentType commentType = null;
    protected String      commentText = null;
    protected String      user        = null;

    /**
     * Default constructor
     */
    public Comment()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateComment element to copy
     */
    public Comment(Comment templateComment)
    {
        super(templateComment);

        if (templateComment != null)
        {
            /*
             * Copy the values from the supplied comment.
             */
            commentType = templateComment.getCommentType();
            user        = templateComment.getUser();
            commentText = templateComment.getCommentText();
        }
    }


    /**
     * Return an enum that describes the type of comment.
     *
     * @return CommentType enum
     */
    public CommentType getCommentType()
    {
        return commentType;
    }


    /**
     * Set up the enum that describes the type of comment.
     *
     * @param commentType CommentType enum
     */
    public void setCommentType(CommentType commentType)
    {
        this.commentType = commentType;
    }


    /**
     * Return the comment text.
     *
     * @return String commentText
     */
    public String getCommentText()
    {
        return commentText;
    }


    /**
     * Set up the comment text.
     *
     * @param commentText String text
     */
    public void setCommentText(String commentText)
    {
        this.commentText = commentText;
    }


    /**
     * Return the user id of the person who created the comment.  Null means the user id is not known.
     *
     * @return String commenting user
     */
    public String getUser()
    {
        return user;
    }


    /**
     * Set up the user id of the person who created the comment.  Null means the user id is not known.
     *
     * @param user String commenting user
     */
    public void setUser(String user)
    {
        this.user = user;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Comment{" +
                "commentType=" + commentType +
                ", commentText='" + commentText + '\'' +
                ", user='" + user + '\'' +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        if (!(objectToCompare instanceof Comment))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Comment comment = (Comment) objectToCompare;
        return getCommentType() == comment.getCommentType() &&
                Objects.equals(getCommentText(), comment.getCommentText()) &&
                Objects.equals(getUser(), comment.getUser());
    }
}
