/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DiscoveryAnalysisReportListResponse is the response structure used on OMAS REST API calls that return a
 * list of DiscoveryAnalysisReport properties objects as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryAnalysisReportListResponse extends ODFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private List<DiscoveryAnalysisReport> discoveryAnalysisReports = null;

    /**
     * Default constructor
     */
    public DiscoveryAnalysisReportListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryAnalysisReportListResponse(DiscoveryAnalysisReportListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.discoveryAnalysisReports = template.getDiscoveryAnalysisReports();
        }
    }


    /**
     * Return the properties objects.
     *
     * @return list of properties objects
     */
    public List<DiscoveryAnalysisReport> getDiscoveryAnalysisReports()
    {
        if (discoveryAnalysisReports == null)
        {
            return null;
        }
        else
        {
            return discoveryAnalysisReports;
        }
    }


    /**
     * Set up the properties objects.
     *
     * @param discoveryAnalysisReports  list of properties objects
     */
    public void setDiscoveryAnalysisReports(List<DiscoveryAnalysisReport> discoveryAnalysisReports)
    {
        this.discoveryAnalysisReports = discoveryAnalysisReports;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryAnalysisReportListResponse{" +
                "discoveryAnalysisReports=" + discoveryAnalysisReports +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DiscoveryAnalysisReportListResponse that = (DiscoveryAnalysisReportListResponse) objectToCompare;
        return Objects.equals(getDiscoveryAnalysisReports(), that.getDiscoveryAnalysisReports());
    }

    
    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDiscoveryAnalysisReports());
    }
}
