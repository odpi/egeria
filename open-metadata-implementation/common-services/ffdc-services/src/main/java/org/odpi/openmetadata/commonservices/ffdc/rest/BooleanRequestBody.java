/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * BooleanRequestBody is the request body structure used on OMAG REST API calls that passed a boolean flag.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BooleanRequestBody
{
    private boolean   flag = true;


    /**
     * Default constructor
     */
    public BooleanRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public BooleanRequestBody(BooleanRequestBody template)
    {
        if (template != null)
        {
            flag = template.isFlag();
        }
    }


    /**
     * Return the boolean result.
     *
     * @return boolean
     */
    public boolean isFlag()
    {
        return flag;
    }


    /**
     * Set up the boolean result.
     *
     * @param flag boolean
     */
    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BooleanRequestBody{" + "flag=" + flag + '}';
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
        BooleanRequestBody that = (BooleanRequestBody) objectToCompare;
        return flag == that.flag;
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(flag);
    }
}
