/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * BooleanResponse is the response structure used on the OMRS REST API calls that return a boolean response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BooleanResponse extends OMRSRESTAPIResponse
{
    private boolean   flag = false;


    /**
     * Default constructor
     */
    public BooleanResponse()
    {
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


    @Override
    public String toString()
    {
        return "BooleanResponse{" +
                "flag=" + flag +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
