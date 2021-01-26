/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.engineservices.governanceaction.properties.EngineSummary;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * EngineSummaryListResponse is the response structure used on the OMAS REST API calls that return a
 * list of engine summaries as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineSummaryListResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private List<EngineSummary> engineSummaries = null;


    /**
     * Default constructor
     */
    public EngineSummaryListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineSummaryListResponse(EngineSummaryListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.engineSummaries = template.getEngineSummaries();
        }
    }


    /**
     * Return the name list result.
     *
     * @return list of engine summaries
     */
    public List<EngineSummary> getEngineSummaries()
    {
        if (engineSummaries == null)
        {
            return null;
        }
        else if (engineSummaries.isEmpty())
        {
            return null;
        }
        else
        {
            return engineSummaries;
        }
    }


    /**
     * Set up the summary list result.
     *
     * @param engineSummaries list of summaries
     */
    public void setEngineSummaries(List<EngineSummary> engineSummaries)
    {
        this.engineSummaries = engineSummaries;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EngineSummaryListResponse{" +
                       "engineSummaries=" + engineSummaries +
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
        if (!(objectToCompare instanceof EngineSummaryListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EngineSummaryListResponse that = (EngineSummaryListResponse) objectToCompare;
        return Objects.equals(engineSummaries, that.engineSummaries);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(engineSummaries);
    }
}

