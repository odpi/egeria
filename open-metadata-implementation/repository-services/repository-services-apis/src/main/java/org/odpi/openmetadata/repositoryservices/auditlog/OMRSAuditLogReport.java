/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReport;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAuditLogReport is a structure that describes the properties of the audit log running in a server.
 * It extends the AuditLogReport from the Audit Log Framework (ALF).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSAuditLogReport extends AuditLogReport
{
    private Map<String, String>            originatorProperties = null;
    private OMRSAuditLogDestinationsReport destinationsReport = null;


    /**
     * Default constructor
     */
    public OMRSAuditLogReport()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public OMRSAuditLogReport(AuditLogReport template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public OMRSAuditLogReport(OMRSAuditLogReport template)
    {
        super(template);

        if (template != null)
        {
            originatorProperties = template.getOriginatorProperties();
            destinationsReport   = template.getDestinationsReport();
        }
    }


    /**
     * Return the properties that describe the server that owns the audit log.
     *
     * @return map of property value pairs
     */
    public Map<String, String> getOriginatorProperties()
    {
        return originatorProperties;
    }


    /**
     * Set up the properties that describe the server that owns the audit log.
     *
     * @param originatorProperties map of property value pairs
     */
    public void setOriginatorProperties(Map<String, String> originatorProperties)
    {
        this.originatorProperties = originatorProperties;
    }


    /**
     * Return the list of destinations where audit log records are being sent.
     *
     * @return destinations report object
     */
    public OMRSAuditLogDestinationsReport getDestinationsReport()
    {
        return destinationsReport;
    }


    /**
     * Set up the list of destinations where audit log records are being sent.
     *
     * @param destinationsReport destinations report object
     */
    public void setDestinationsReport(OMRSAuditLogDestinationsReport destinationsReport)
    {
        this.destinationsReport = destinationsReport;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSAuditLogReport{" +
                "originatorProperties=" + originatorProperties +
                ", destinationsReport=" + destinationsReport +
                ", reportingComponent=" + getReportingComponent() +
                ", childAuditLogReports=" + getChildAuditLogReports() +
                ", severityIdentification=" + getSeverityIdentification() +
                ", severityCount=" + getSeverityCount() +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OMRSAuditLogReport report = (OMRSAuditLogReport) objectToCompare;
        return Objects.equals(originatorProperties, report.originatorProperties) &&
                Objects.equals(destinationsReport, report.destinationsReport);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), originatorProperties, destinationsReport);
    }
}
