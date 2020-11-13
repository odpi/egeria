/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAuditLogRecord provides a carrier for details about a single log record in the OMRS audit log.
 * It extends the AuditLogRecord class from the Audit Log Framework (ALF) with deprecated fields from
 * the original OMRS implementation.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSAuditLogRecord extends AuditLogRecord
{
    private static final long    serialVersionUID = 1L;

    private OMRSAuditLogRecordOriginator   originator           = null;
    private OMRSAuditLogReportingComponent reportingComponent   = null;


    /**
     * Default constructor
     */
    public OMRSAuditLogRecord()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAuditLogRecord(OMRSAuditLogRecord template)
    {
        super(template);

        if (template != null)
        {
            this.originator         = template.getOriginator();
            this.reportingComponent = template.getReportingComponent();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAuditLogRecord(AuditLogRecord template)
    {
        super(template);

        if (template != null)
        {
            this.originator         = new OMRSAuditLogRecordOriginator(template.getOriginatorProperties());
            this.reportingComponent = new OMRSAuditLogReportingComponent(template.getOriginatorComponent());
        }
    }


    /**
     * Return details of the originator of the log record.
     *
     * @return OMRSAuditLogRecordOriginator object
     */
    public OMRSAuditLogRecordOriginator getOriginator()
    {
        return originator;
    }


    /**
     * Set up details of the originator of the log record.
     *
     * @param originator  calling component
     */
    public void setOriginator(OMRSAuditLogRecordOriginator originator)
    {
        this.originator = originator;
    }



    /**
     * Return the name of the component that reported the situation recorded in the log record.
     *
     * @return OMRSAuditLogReportingComponent object
     */
    public OMRSAuditLogReportingComponent getReportingComponent()
    {
        return reportingComponent;
    }


    /**
     * Set up the name of the component that reported the situation recorded in the log record.
     *
     * @param reportingComponent  OMRSAuditLogReportingComponent object
     */
    public void setReportingComponent(OMRSAuditLogReportingComponent reportingComponent)
    {
        this.reportingComponent = reportingComponent;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSAuditLogRecord{" +
                "originator=" + originator +
                ", reportingComponent=" + reportingComponent +
                ", GUID='" + getGUID() + '\'' +
                ", timeStamp=" + getTimeStamp() +
                ", originatorProperties=" + getOriginatorProperties() +
                ", originatorComponent=" + getOriginatorComponent() +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", threadId=" + getThreadId() +
                ", threadName='" + getThreadName() + '\'' +
                ", severityCode=" + getSeverityCode() +
                ", severity='" + getSeverity() + '\'' +
                ", messageId='" + getMessageId() + '\'' +
                ", messageText='" + getMessageText() + '\'' +
                ", messageParameters=" + Arrays.toString(getMessageParameters()) +
                ", additionalInformation=" + getAdditionalInformation() +
                ", systemAction='" + getSystemAction() + '\'' +
                ", userAction='" + getUserAction() + '\'' +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionMessage='" + getExceptionMessage() + '\'' +
                ", exceptionStackTrace='" + getExceptionStackTrace() + '\'' +
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
        OMRSAuditLogRecord logRecord = (OMRSAuditLogRecord) objectToCompare;
        return Objects.equals(originator, logRecord.originator) &&
                Objects.equals(reportingComponent, logRecord.reportingComponent);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), originator, reportingComponent);
    }
}
