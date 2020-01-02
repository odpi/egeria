/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAuditLogRecord provides a carrier for details about a single log record in the OMRS audit log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSAuditLogRecord implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                         guid                  = null;
    private Date                           timeStamp             = new Date();
    private OMRSAuditLogRecordOriginator   originator            = null;
    private String                         severity              = null;
    private OMRSAuditLogReportingComponent reportingComponent    = null;
    private String                         messageId             = null;
    private String                         messageText           = null;
    private List<String>                   additionalInformation = null;
    private String                         systemAction          = null;
    private String                         userAction            = null;
    private String                         exceptionClassName    = null;
    private String                         exceptionMessage      = null;
    private String                         exceptionStackTrace   = null;


    /**
     * Default constructor
     */
    public OMRSAuditLogRecord()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAuditLogRecord(OMRSAuditLogRecord template)
    {
        this.guid = template.getGUID();
        this.timeStamp = template.getTimeStamp();
        this.originator = template.getOriginator();
        this.severity = template.getSeverity();
        this.reportingComponent = template.getReportingComponent();
        this.messageId = template.getMessageId();
        this.messageText = template.getMessageText();
        this.additionalInformation = template.getAdditionalInformation();
        this.systemAction = template.getSystemAction();
        this.userAction = template.getUserAction();
        this.exceptionClassName = template.getExceptionClassName();
        this.exceptionMessage = template.getExceptionMessage();
        this.exceptionStackTrace = template.getExceptionStackTrace();
    }


    /**
     * Audit log records are immutable so the only way to update the values is through the constructor.
     *
     * @param originator  details of the originating server
     * @param reportingComponent  details of the component making the audit log entry.
     * @param severity  OMRSAuditLogRecordSeverity enum that indicates the severity of log record.
     * @param messageId  id of the message in the audit log record.
     * @param messageText  description of the message for the audit log record.
     * @param additionalInformation  additional properties that help to describe the situation.
     * @param systemAction  action taken by the system.
     * @param userAction  followup action that should be taken by the target end user (typically the server
     *                   administrator).
     */
    public OMRSAuditLogRecord(OMRSAuditLogRecordOriginator   originator,
                              OMRSAuditLogReportingComponent reportingComponent,
                              String                         severity,
                              String                         messageId,
                              String                         messageText,
                              List<String>                   additionalInformation,
                              String                         systemAction,
                              String                         userAction)
    {
        this.guid = UUID.randomUUID().toString();
        this.originator = originator;
        this.severity = severity;
        this.reportingComponent = reportingComponent;
        this.messageId = messageId;
        this.messageText = messageText;
        this.additionalInformation = additionalInformation;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    /**
     * Return the unique Id of the audit log record.
     *
     * @return String guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique Id of the audit log record.
     *
     * @param guid  String guid
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the time stamp for when the audit log record was created.
     *
     * @return Date object
     */
    public Date getTimeStamp()
    {
        return timeStamp;
    }


    /**
     * Set up the time stamp for when the audit log record was created.
     *
     * @param timeStamp Date object
     */
    public void setTimeStamp(Date timeStamp)
    {
        this.timeStamp = timeStamp;
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
     * Return the severity of the situation recorded in the log record.
     *
     * @return String severity
     */
    public String getSeverity()
    {
        return severity;
    }


    /**
     * Set up the severity of the situation recorded in the log record.
     *
     * @param severity  String severity
     */
    public void setSeverity(String severity)
    {
        this.severity = severity;
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
     * Return the identifier of the message within the log record.
     *
     * @return String message id
     */
    public String getMessageId()
    {
        return messageId;
    }


    /**
     * Set up  the identifier of the message within the log record.
     *
     * @param messageId  String message id
     */
    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }


    /**
     * Return the text of the message within the log record.
     *
     * @return String message text
     */
    public String getMessageText()
    {
        return messageText;
    }


    /**
     * Set up the text of the message within the log record.
     *
     * @param messageText  String message text
     */
    public void setMessageText(String messageText)
    {
        this.messageText = messageText;
    }


    /**
     * Return any additional information in the audit log record.
     *
     * @return List of String additional information
     */
    public List<String> getAdditionalInformation()
    {
        return additionalInformation;
    }


    /**
     * Set up any additional information in the audit log record.
     *
     * @param additionalInformation  List of String additional information
     */
    public void setAdditionalInformation(List<String> additionalInformation)
    {
        this.additionalInformation = additionalInformation;
    }


    /**
     * Return the description of the actions taken by the local server as a result of the reported situation.
     *
     * @return string description
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Set up the description of the actions taken by the local server as a result of the reported situation.
     *
     * @param systemAction  a description of the actions taken by the system as a result of the error.
     */
    public void setSystemAction(String systemAction)
    {
        this.systemAction = systemAction;
    }


    /**
     * Return details of the actions (if any) that a user can take in response to the reported situation.
     *
     * @return String instructions
     */
    public String getUserAction()
    {
        return userAction;
    }


    /**
     * Set up details of the actions (if any) that a user can take in response to the reported situation.
     *
     * @param userAction  String instructions
     */
    public void setUserAction(String userAction)
    {
        this.userAction = userAction;
    }


    /**
     * Return the name of any exception linked to the audit log record.
     *
     * @return the class name
     */
    public String getExceptionClassName()
    {
        return exceptionClassName;
    }


    /**
     * Set up the name of any exception linked to the audit log record.
     *
     * @param exceptionClassName string name
     */
    public void setExceptionClassName(String exceptionClassName)
    {
        this.exceptionClassName = exceptionClassName;
    }

    /**
     * Return the name of the message associated with any exception linked to the audit log record.
     *
     * @return string message
     */
    public String getExceptionMessage()
    {
        return exceptionMessage;
    }


    /**
     * Set up the name of the message associated with any exception linked to the audit log record.
     *
     * @param exceptionMessage string message
     */
    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionMessage = exceptionMessage;
    }


    /**
     * Return the stack trace associated with any exception linked to the audit log record.
     *
     * @return string stack trace
     */
    public String getExceptionStackTrace()
    {
        return exceptionStackTrace;
    }


    /**
     * Set up the stack trace associated with any exception linked to the audit log record.
     *
     * @param exceptionStackTrace string stack trace
     */
    public void setExceptionStackTrace(String exceptionStackTrace)
    {
        this.exceptionStackTrace = exceptionStackTrace;
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
                "guid='" + guid + '\'' +
                ", timeStamp=" + timeStamp +
                ", originator=" + originator +
                ", severity='" + severity + '\'' +
                ", reportingComponent=" + reportingComponent +
                ", messageId='" + messageId + '\'' +
                ", messageText='" + messageText + '\'' +
                ", additionalInformation=" + additionalInformation +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                ", exceptionStackTrace='" + exceptionStackTrace + '\'' +
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
        OMRSAuditLogRecord that = (OMRSAuditLogRecord) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(timeStamp, that.timeStamp) &&
                Objects.equals(originator, that.originator) &&
                Objects.equals(severity, that.severity) &&
                Objects.equals(reportingComponent, that.reportingComponent) &&
                Objects.equals(messageId, that.messageId) &&
                Objects.equals(messageText, that.messageText) &&
                Objects.equals(additionalInformation, that.additionalInformation) &&
                Objects.equals(systemAction, that.systemAction) &&
                Objects.equals(userAction, that.userAction) &&
                Objects.equals(exceptionClassName, that.exceptionClassName) &&
                Objects.equals(exceptionMessage, that.exceptionMessage) &&
                Objects.equals(exceptionStackTrace, that.exceptionStackTrace);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, timeStamp, originator, severity, reportingComponent, messageId, messageText, additionalInformation, systemAction,
                            userAction, exceptionClassName, exceptionMessage, exceptionStackTrace);
    }
}
