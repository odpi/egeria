/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;


import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * CommentResponse is the response structure used on the OMAS REST API calls that returns a
 * Comment object as a response.  It returns details of the comment and the count of the replies within it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommentResponse extends OCFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private Comment comment    = null;
    private int     replyCount = 0;


    /**
     * Default constructor
     */
    public CommentResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommentResponse(CommentResponse template)
    {
        if (template != null)
        {
            this.comment = template.getComment();
            this.replyCount = template.getReplyCount();
        }
    }


    /**
     * Return the comment properties.
     *
     * @return note log bean
     */
    public Comment getComment()
    {
        return comment;
    }


    /**
     * Set up the comment properties.
     *
     * @param comment bean
     */
    public void setComment(Comment comment)
    {
        this.comment = comment;
    }


    /**
     * Return the count of the replies to the comment.
     *
     * @return int
     */
    public int getReplyCount()
    {
        return replyCount;
    }


    /**
     * Set up the count of the replies to the comment.
     *
     * @param replyCount int
     */
    public void setReplyCount(int replyCount)
    {
        this.replyCount = replyCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommentResponse{" +
                "comment=" + comment +
                ", replyCount=" + replyCount +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
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
        CommentResponse that = (CommentResponse) objectToCompare;
        return getReplyCount() == that.getReplyCount() &&
                Objects.equals(getComment(), that.getComment());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getComment(), getReplyCount());
    }
}
