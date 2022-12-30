/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.APIOperation;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * CommentResponse is the response structure used on the OMAS REST API calls that returns a
 * Comment object as a response.  It returns details of the comment and the count of the replies within it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIOperationResponse extends OCFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private APIOperation apiOperation           = null;
    private int          headerAttributeCount   = 0;
    private int          requestAttributeCount  = 0;
    private int          responseAttributeCount = 0;


    /**
     * Default constructor
     */
    public APIOperationResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public APIOperationResponse(APIOperationResponse template)
    {
        if (template != null)
        {
            this.apiOperation = template.getAPIOperation();
            this.headerAttributeCount = template.getHeaderAttributeCount();
            this.requestAttributeCount = template.getRequestAttributeCount();
            this.responseAttributeCount = template.getResponseAttributeCount();
        }
    }


    /**
     * Return the note log properties.
     *
     * @return note log bean
     */
    public APIOperation getAPIOperation()
    {
        return apiOperation;
    }


    /**
     * Set up the not log properties.
     *
     * @param apiOperation bean
     */
    public void setAPIOperation(APIOperation apiOperation)
    {
        this.apiOperation = apiOperation;
    }


    /**
     * Return the count of the attributes within the header.
     *
     * @return int
     */
    public int getHeaderAttributeCount()
    {
        return headerAttributeCount;
    }


    /**
     * Set up the count of attributes within the header.
     *
     * @param headerAttributeCount int
     */
    public void setHeaderAttributeCount(int headerAttributeCount)
    {
        this.headerAttributeCount = headerAttributeCount;
    }


    /**
     * Return the count of the attributes within the request.
     *
     * @return int
     */
    public int getRequestAttributeCount()
    {
        return requestAttributeCount;
    }


    /**
     * Set up the count of attributes within the request.
     *
     * @param requestAttributeCount int
     */
    public void setRequestAttributeCount(int requestAttributeCount)
    {
        this.requestAttributeCount = requestAttributeCount;
    }


    /**
     * Return the count of the attributes within the response.
     *
     * @return int
     */
    public int getResponseAttributeCount()
    {
        return responseAttributeCount;
    }


    /**
     * Set up the count of attributes within the response.
     *
     * @param responseAttributeCount int
     */
    public void setResponseAttributeCount(int responseAttributeCount)
    {
        this.responseAttributeCount = responseAttributeCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "APIOperationResponse{" +
                       "exceptionClassName='" + getExceptionClassName() + '\'' +
                       ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                       ", actionDescription='" + getActionDescription() + '\'' +
                       ", relatedHTTPCode=" + getRelatedHTTPCode() +
                       ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                       ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                       ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                       ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                       ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                       ", exceptionProperties=" + getExceptionProperties() +
                       ", apiOperation=" + apiOperation +
                       ", headerAttributeCount=" + headerAttributeCount +
                       ", requestAttributeCount=" + requestAttributeCount +
                       ", responseAttributeCount=" + responseAttributeCount +
                       ", APIOperation=" + getAPIOperation() +
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
        if (! (objectToCompare instanceof APIOperationResponse))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        APIOperationResponse that = (APIOperationResponse) objectToCompare;

        if (headerAttributeCount != that.headerAttributeCount)
        {
            return false;
        }
        if (requestAttributeCount != that.requestAttributeCount)
        {
            return false;
        }
        if (responseAttributeCount != that.responseAttributeCount)
        {
            return false;
        }
        return apiOperation != null ? apiOperation.equals(that.apiOperation) : that.apiOperation == null;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (apiOperation != null ? apiOperation.hashCode() : 0);
        result = 31 * result + headerAttributeCount;
        result = 31 * result + requestAttributeCount;
        result = 31 * result + responseAttributeCount;
        return result;
    }
}
