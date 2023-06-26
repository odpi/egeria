/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The CommentProperties bean stores information about a comment connected to an asset.  Comments provide informal feedback to assets
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
public class CommentProperties extends ReferenceableProperties
{
    private CommentType commentType = null;
    private String      commentText = null;
    private String      user        = null;

    /**
     * Default constructor
     */
    public CommentProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public CommentProperties(CommentProperties template)
    {
        super(template);

        if (template != null)
        {
            /*
             * Copy the values from the supplied comment.
             */
            commentType = template.getCommentType();
            user        = template.getUser();
            commentText = template.getCommentText();
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
        return "CommentProperties{" +
                       "commentType=" + commentType +
                       ", commentText='" + commentText + '\'' +
                       ", user='" + user + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        CommentProperties that = (CommentProperties) objectToCompare;
        return commentType == that.commentType &&
                       Objects.equals(commentText, that.commentText) &&
                       Objects.equals(user, that.user);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), commentType, commentText, user);
    }
}
