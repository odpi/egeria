/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AuditLogReport is a container for returning information about a hierarchy of audit logs.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuditLogReport implements Serializable
{
    private static final long          serialVersionUID = 1L;

    private AuditLogReportingComponent reportingComponent     = null;
    private List<AuditLogReport>       childAuditLogReports   = null;
    private Map<Integer, List<String>> severityIdentification = null;
    private Map<Integer, Integer>      severityCount          = null;


    /**
     * Default constructor
     */
    public AuditLogReport()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AuditLogReport(AuditLogReport template)
    {
        if (template != null)
        {
            reportingComponent = template.getReportingComponent();
            childAuditLogReports = template.getChildAuditLogReports();
            severityIdentification = template.getSeverityIdentification();
            severityCount = template.getSeverityCount();
        }
    }


    /**
     * Return the description of the component that is using this audit log.
     *
     * @return component id, name, description and doc URL
     */
    public AuditLogReportingComponent getReportingComponent()
    {
        return new AuditLogReportingComponent(reportingComponent);
    }


    /**
     * Set up the description of the component that is using this audit log.
     *
     * @param reportingComponent component id, name, description and doc URL
     */
    public void setReportingComponent(AuditLogReportingComponent reportingComponent)
    {
        this.reportingComponent = new AuditLogReportingComponent(reportingComponent);
    }


    /**
     * Return the reports for the hierarchy of audit logs nested in this audit log.
     *
     * @return reports from hierarchy of child audit logs
     */
    public List<AuditLogReport> getChildAuditLogReports()
    {
        if (childAuditLogReports == null)
        {
            return null;
        }
        else if (childAuditLogReports.isEmpty())
        {
            return null;
        }

        List<AuditLogReport> result = new ArrayList<>();

        for (AuditLogReport report : childAuditLogReports)
        {
            if (report != null)
            {
                result.add(new AuditLogReport(report));
            }
        }
        return result;
    }


    /**
     * Set up the reports for the hierarchy of audit logs nested in this audit log.
     *
     * @param childAuditLogReports reports from hierarchy of child audit logs
     */
    public void setChildAuditLogReports(List<AuditLogReport> childAuditLogReports)
    {
        this.childAuditLogReports = childAuditLogReports;
    }


    /**
     * Return the map of severity codes to severity names - if the audit log is set up correctly
     * they should be 1-1.   However, the audit log allows for components from different sources
     * clashing on the severity code.  The report helps an organization identify when this is occurring.
     *
     * @return map of severity codes to list of severity names associated with it.
     */
    public Map<Integer, List<String>> getSeverityIdentification()
    {
        if (severityIdentification == null)
        {
            return null;
        }
        else if (severityIdentification.isEmpty())
        {
            return null;
        }

        return new HashMap<>(severityIdentification);
    }


    /**
     * Set up the map of severity codes to severity names - if the audit log is set up correctly
     * they should be 1-1.   However, the audit log allows for components from different sources
     * clashing on the severity code.  The report helps an organization identify when this is occurring.
     *
     * @param severityIdentification map of severity codes to list of severity names associated with it.
     */
    public void setSeverityIdentification(Map<Integer, List<String>> severityIdentification)
    {
        this.severityIdentification = severityIdentification;
    }


    /**
     * Return the count of log records for each severity code.
     *
     * @return map of severity code to log record count
     */
    public Map<Integer, Integer> getSeverityCount()
    {
        if (severityCount == null)
        {
            return null;
        }
        else if (severityCount.isEmpty())
        {
            return null;
        }

        return new HashMap<>(severityCount);
    }


    /**
     * Set up the count of log records for each severity code.
     *
     * @param severityCount map of severity code to log record count
     */
    public void setSeverityCount(Map<Integer, Integer> severityCount)
    {
        this.severityCount = severityCount;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "AuditLogReport{" +
                "reportingComponent=" + reportingComponent +
                ", childAuditLogReports=" + childAuditLogReports +
                ", severityIdentification=" + severityIdentification +
                ", severityCount=" + severityCount +
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
        AuditLogReport that = (AuditLogReport) objectToCompare;
        return Objects.equals(reportingComponent, that.reportingComponent) &&
                Objects.equals(childAuditLogReports, that.childAuditLogReports) &&
                Objects.equals(severityIdentification, that.severityIdentification) &&
                Objects.equals(severityCount, that.severityCount);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(reportingComponent, childAuditLogReports, severityIdentification, severityCount);
    }
}
