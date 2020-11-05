/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficerProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceOfficerListResponse is the response structure used on the OMAS REST API calls that return a
 * list of governance officers as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceOfficerListResponse extends GovernanceProgramOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private List<GovernanceOfficerProperties> governanceOfficers = null;


    /**
     * Default constructor
     */
    public GovernanceOfficerListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceOfficerListResponse(GovernanceOfficerListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.governanceOfficers = template.getGovernanceOfficers();
        }
    }


    /**
     * Return the list of governance officers.
     *
     * @return list of objects or null
     */
    public List<GovernanceOfficerProperties> getGovernanceOfficers()
    {
        if (governanceOfficers == null)
        {
            return null;
        }
        else if (governanceOfficers.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(governanceOfficers);
        }
    }


    /**
     * Set up the list of governance officers.
     *
     * @param governanceOfficers - list of objects or null
     */
    public void setGovernanceOfficers(List<GovernanceOfficerProperties> governanceOfficers)
    {
        this.governanceOfficers = governanceOfficers;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceOfficerListResponse{" +
                "governanceOfficers=" + governanceOfficers +
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
        if (!(objectToCompare instanceof GovernanceOfficerListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceOfficerListResponse that = (GovernanceOfficerListResponse) objectToCompare;
        return Objects.equals(this.getGovernanceOfficers(), that.getGovernanceOfficers());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceOfficers, super.hashCode());
    }
}
