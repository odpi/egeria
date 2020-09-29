/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommentRequestBody provides a structure for passing a comment as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnnotationRequestBody extends ODFOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private CommentType commentType = null;
    private String      commentText = null;


    /**
     * Default constructor
     */
    public AnnotationRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AnnotationRequestBody(AnnotationRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.commentType = template.getCommentType();
            this.commentText = template.getCommentText();
        }
    }
    

    /**
     * Return the type of comment.
     * 
     * @return string
     */
    public CommentType getCommentType()
    {
        return commentType;
    }


    /**
     * Set up the type of comment.
     * 
     * @param commentType string
     */
    public void setCommentType(CommentType commentType)
    {
        this.commentType = commentType;
    }


    /**
     * Return the comment content.
     * 
     * @return string description
     */
    public String getCommentText()
    {
        return commentText;
    }


    /**
     * Set up the comment content.
     * 
     * @param commentText text.
     */
    public void setCommentText(String commentText)
    {
        this.commentText = commentText;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */



    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */

}
