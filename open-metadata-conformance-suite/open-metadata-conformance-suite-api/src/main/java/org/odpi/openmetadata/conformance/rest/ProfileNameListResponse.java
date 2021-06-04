/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TestCaseListResponse defines the response structure that lists the test case IDs available.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileNameListResponse extends ConformanceServicesAPIResponse
{
    private static final long     serialVersionUID = 1L;

    private List<String> profileNames = null;

    /**
     * Default constructor
     */
    public ProfileNameListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProfileNameListResponse(ProfileNameListResponse template)
    {
        super(template);

        if (template != null)
        {
            profileNames = template.getProfileNames();
        }
    }


    /**
     * Return the test case IDs that are available.
     *
     * @return list of test case IDs
     */
    public List<String> getProfileNames()
    {
        return profileNames;
    }


    /**
     * Set up the list of test case IDs that are available.
     *
     * @param profileNames that are available
     */
    public void setProfileNames(List<String> profileNames)
    {
        this.profileNames = profileNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ProfileNameListResponse{" +
                "profileNames=" + profileNames +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", successMessage='" + getSuccessMessage() + '\'' +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
