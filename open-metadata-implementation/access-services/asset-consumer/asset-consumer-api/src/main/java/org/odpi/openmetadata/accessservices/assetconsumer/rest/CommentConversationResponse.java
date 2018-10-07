/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommentConversationResponse returns a comment and a list of the
 * unique identifiers (GUIDs) for its replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommentConversationResponse extends AssetConsumerOMASAPIResponse
{
    private Comment         comment             = null;
    private List<String>    commentReplies      = null;

    /**
     * Default constructor
     */
    public CommentConversationResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommentConversationResponse(CommentConversationResponse  template)
    {
        super(template);

        if (template != null)
        {
            this.comment = template.getComment();
            this.commentReplies = template.getCommentReplies();
        }
    }


    /**
     * Return the root comment.
     *
     * @return Comment object covering comment text and type
     */
    public Comment getComment()
    {
        if (comment == null)
        {
            return null;
        }
        else
        {
            return comment;
        }
    }


    /**
     * Set up the root comment.
     *
     * @param comment comment text and type
     */
    public void setComment(Comment comment)
    {
        this.comment = comment;
    }


    /**
     * Return the list of unique identifiers for the replies to the root comment.
     *
     * @return list of guids that are replies to the root comment.
     */
    public List<String> getCommentReplies()
    {
        if (commentReplies == null)
        {
            return null;
        }
        else if (commentReplies.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(commentReplies);
        }
    }


    /**
     * Set up the replies to the root comment.
     *
     * @param commentReplies list of guids that are replies to the root comment.
     */
    public void setCommentReplies(List<String> commentReplies)
    {
        this.commentReplies = commentReplies;
    }




    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommentConversationResponse{" +
                "comment=" + comment +
                ", commentReplies=" + commentReplies +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        CommentConversationResponse that = (CommentConversationResponse) objectToCompare;
        return Objects.equals(getComment(), that.getComment()) &&
                Objects.equals(getCommentReplies(), that.getCommentReplies());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getComment(), getCommentReplies());
    }
}
