/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * BooleanResponse is the response structure used on the OMRS REST API calls that return a boolean response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BooleanResponse extends OMRSAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private boolean   flag = false;


    /**
     * Default constructor
     */
    public BooleanResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public BooleanResponse(BooleanResponse template)
    {
        super(template);

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
     * set up the boolean result.
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
        return "BooleanResponse{" +
                "flag=" + flag +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof BooleanResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        BooleanResponse
                that = (BooleanResponse) objectToCompare;
        return isFlag() == that.isFlag();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), isFlag());
    }
}
