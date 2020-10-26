/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DiscoveryAnalysisReportResponse is the response structure used on the Asset Consumer OMAS REST API calls that returns a
 * Connection object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryAnalysisReportResponse extends ODFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private DiscoveryAnalysisReport analysisReport = null;

    /**
     * Default constructor
     */
    public DiscoveryAnalysisReportResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryAnalysisReportResponse(DiscoveryAnalysisReportResponse template)
    {
        super(template);

        if (template != null)
        {
            this.analysisReport = template.getAnalysisReport();
        }
    }


    /**
     * Return the Connection object.
     *
     * @return analysisReport
     */
    public DiscoveryAnalysisReport getAnalysisReport()
    {
        if (analysisReport == null)
        {
            return null;
        }
        else
        {
            return analysisReport;
        }
    }


    /**
     * Set up the Connection object.
     *
     * @param analysisReport - analysisReport object
     */
    public void setAnalysisReport(DiscoveryAnalysisReport analysisReport)
    {
        this.analysisReport = analysisReport;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryAnalysisReportResponse{" +
                "analysisReport=" + analysisReport +
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
        if (!(objectToCompare instanceof DiscoveryAnalysisReportResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DiscoveryAnalysisReportResponse that = (DiscoveryAnalysisReportResponse) objectToCompare;
        return Objects.equals(getAnalysisReport(), that.getAnalysisReport());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (analysisReport == null)
        {
            return super.hashCode();
        }
        else
        {
            return analysisReport.hashCode();
        }
    }
}
