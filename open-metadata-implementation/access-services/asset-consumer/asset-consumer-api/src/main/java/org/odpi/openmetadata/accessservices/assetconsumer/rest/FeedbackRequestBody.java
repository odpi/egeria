/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

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
public class FeedbackRequestBody extends AssetConsumerOMASAPIRequestBody
{
    private boolean    isPublic  = false;


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
            this.isPublic = template.getIsPublic();
        }
    }




    /**
     * Return whether the feedback is private or not
     *
     * @return boolean
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up the privacy flag.
     *
     * @param aPrivate boolean
     */
    public void setIsPublic(boolean aPrivate)
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
        return getIsPublic() == that.getIsPublic();
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getIsPublic());
    }
}
