/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FeedbackRequestBody provides a base class for passing feedback objects as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes
        ({
                @JsonSubTypes.Type(value = CommentRequestBody.class,  name = "CommentRequestBody"),
                @JsonSubTypes.Type(value = RatingRequestBody.class,   name = "RatingRequestBody"),
                @JsonSubTypes.Type(value = TagRequestBody.class,   name = "TagRequestBody"),
        })
public class FeedbackRequestBody extends OCFOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    // todo this default should be false - need to fix up the problem with passing boolean values over REST APIs
    private boolean    isPublic  = true;


    /**
     * Default constructor
     */
    public FeedbackRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FeedbackRequestBody(FeedbackRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.isPublic = template.isPublic();
        }
    }




    /**
     * Return whether the feedback is private or not
     *
     * @return boolean
     */
    public boolean isPublic()
    {
        return isPublic;
    }


    /**
     * Set up the privacy flag.
     *
     * @param aPrivate boolean
     */
    public void setPublic(boolean aPrivate)
    {
        isPublic = aPrivate;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "FeedbackRequestBody{" +
                ", isPublic=" + isPublic +
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
        FeedbackRequestBody that = (FeedbackRequestBody) objectToCompare;
        return isPublic() == that.isPublic();
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(isPublic());
    }
}
