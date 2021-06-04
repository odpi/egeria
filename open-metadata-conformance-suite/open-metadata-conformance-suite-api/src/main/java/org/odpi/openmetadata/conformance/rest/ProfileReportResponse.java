/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceProfileResults;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProfileReportResponse defines the response structure that includes the results from a single profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileReportResponse extends ConformanceServicesAPIResponse
{
    private static final long     serialVersionUID = 1L;

    private OpenMetadataConformanceProfileResults profileResult = null;

    /**
     * Default constructor
     */
    public ProfileReportResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProfileReportResponse(ProfileReportResponse template)
    {
        super(template);

        if (template != null)
        {
            profileResult = template.getProfileResult();
        }
    }


    /**
     * Return the results from running a single profile.
     *
     * @return results report
     */
    public OpenMetadataConformanceProfileResults getProfileResult()
    {
        return profileResult;
    }


    /**
     * Set up the results from running a single profile.
     *
     * @param profileResult results report
     */
    public void setProfileResult(OpenMetadataConformanceProfileResults profileResult)
    {
        this.profileResult = profileResult;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ProfileReportResponse{" +
                "profileResult=" + profileResult +
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
