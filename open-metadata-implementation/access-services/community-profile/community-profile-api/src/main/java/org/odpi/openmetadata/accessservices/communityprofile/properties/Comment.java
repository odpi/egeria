/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Comment records a a comment added to an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Comment extends CommentHeader
{
    private static final long    serialVersionUID = 1L;

    private CommentType         commentType          = null;
    private List<String>        answeredBy           = null;
    private List<String>        answers              = null;


    /**
     * Default constructor
     */
    public Comment()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Comment(Comment template)
    {
        super(template);

        if (template != null)
        {
            commentType = template.getCommentType();
            answeredBy = template.getAnsweredBy();
            answers = template.getAnswers();
        }
    }


    /**
     * Return the type of comment this is.
     *
     * @return comment type enum
     */
    public CommentType getCommentType()
    {
        return commentType;
    }


    /**
     * Set up the type of comment this is.
     *
     * @param commentType comment type enum
     */
    public void setCommentType(CommentType commentType)
    {
        this.commentType = commentType;
    }


    /**
     * Return the list of unique identifiers (guids) for comments that answer a question posed in this comment.
     *
     * @return list of guids
     */
    public List<String> getAnsweredBy()
    {
        return answeredBy;
    }


    /**
     * Set up the list of unique identifiers (guids) for comments that answer a question posed in this comment.
     *
     * @param answeredBy list of guids
     */
    public void setAnsweredBy(List<String> answeredBy)
    {
        this.answeredBy = answeredBy;
    }


    /**
     * Return the list of unique identifiers (guids) for comments that contain a question that this comment answers.
     *
     * @return list of guids
     */
    public List<String> getAnswers()
    {
        return answers;
    }


    /**
     * Set up the list of unique identifiers (guids) for comments that contain a question that this comment answers.
     *
     * @param answers list of guids
     */
    public void setAnswers(List<String> answers)
    {
        this.answers = answers;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "Comment{" +
                "commentType=" + commentType +
                ", answeredBy=" + answeredBy +
                ", answers=" + answers +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", text='" + getText() + '\'' +
                ", commentProperties=" + getExtendedProperties() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", userId='" + getUserId() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        Comment comment = (Comment) objectToCompare;
        return getCommentType() == comment.getCommentType() &&
                Objects.equals(getAnsweredBy(), comment.getAnsweredBy()) &&
                Objects.equals(getAnswers(), comment.getAnswers());
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getCommentType(), getAnsweredBy(), getAnswers());
    }
}
