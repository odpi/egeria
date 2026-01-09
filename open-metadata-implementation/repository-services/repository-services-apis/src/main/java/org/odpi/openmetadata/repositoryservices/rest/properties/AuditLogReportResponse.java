/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReport;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AuditLogReportResponse describes the response structure for an OMRS REST API that returns
 * an AuditLogReport object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuditLogReportResponse extends OMRSAPIResponse
{
    private OMRSAuditLogReport report = null;

    /**
     * Default constructor
     */
    public AuditLogReportResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AuditLogReportResponse(AuditLogReportResponse template)
    {
        super(template);

        if (template != null)
        {
            report = template.getReport();
        }
    }


    /**
     * Return the resulting report object.
     *
     * @return report object
     */
    public OMRSAuditLogReport getReport()
    {
        if (report == null)
        {
            return null;
        }
        else
        {
            return new OMRSAuditLogReport(report);
        }
    }


    /**
     * Set up the resulting report object.
     *
     * @param report report object
     */
    public void setReport(OMRSAuditLogReport report)
    {
        this.report = report;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AuditLogReportResponse{" +
                "report=" + report +
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
        if (!(objectToCompare instanceof AuditLogReportResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AuditLogReportResponse
                that = (AuditLogReportResponse) objectToCompare;
        return Objects.equals(getReport(), that.getReport());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getReport());
    }
}
