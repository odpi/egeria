/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

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
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Comment extends ElementHeader
{
    /*
     * Attributes of a Comment
     */
    private String        commentText    = null;
    private String        user           = null;
    private List<Comment> commentReplies = null;
    private CommentType   commentType    = null;


    /**
     * Default Constructor
     */
    public Comment()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateComment - element to copy
     */
    public Comment(Comment templateComment)
    {
        super(templateComment);

        if (templateComment != null)
        {
            /*
             * Copy the values from the supplied comment.
             */
            user = templateComment.getUser();
            commentText = templateComment.getCommentText();

            List<Comment> templateCommentReplies = templateComment.getCommentReplies();
            if (templateCommentReplies != null)
            {
                commentReplies = new ArrayList<>(templateCommentReplies);
            }
        }
    }


    /**
     * Return the user id of the person who created the comment.  Null means the user id is not known.
     *
     * @return String - commenting user
     */
    public String getUser() {
        return user;
    }


    /**
     * Set up the user id of the person who created the comment. Null means the user id is not known.
     *
     * @param user - String - commenting user
     */
    public void setUser(String user) {
        this.user = user;
    }


    /**
     * Return the comment text.
     *
     * @return String - commentText
     */
    public String getCommentText() {
        return commentText;
    }


    /**
     * Set up the comment text.
     *
     * @param commentText String
     */
    public void setCommentText(String  commentText) { this.commentText = commentText; }


    /**
     * Return an enumeration of the replies to this comment - null means no replies are available.
     *
     * @return Comments - comment replies list
     */
    public List<Comment> getCommentReplies()
    {
        if (commentReplies == null)
        {
            return commentReplies;
        }
        else
        {
            return new ArrayList<>(commentReplies);
        }
    }


    /**
     * Set up the comment replies enumeration - null means no replies are available.
     *
     * @param commentReplies - List of Comments
     */
    public void setCommentReplies(List<Comment> commentReplies)
    {
        if (commentReplies == null)
        {
            this.commentReplies = null;
        }
        else
        {
            this.commentReplies = new ArrayList<>(commentReplies);
        }
    }


    /**
     * Return the type of comment.
     *
     * @return comment type enum
     */
    public CommentType getCommentType()
    {
        return commentType;
    }


    /**
     * Set up the type of comment.
     *
     * @param commentType - enum
     */
    public void setCommentType(CommentType commentType)
    {
        this.commentType = commentType;
    }
}