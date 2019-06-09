/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class OMRSAuditLogRecord
{
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


    /**
     * Default constructor
     */
    public OMRSAuditLogRecord()
    {
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
                ", GUID='" + getGUID() + '\'' +
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
                Objects.equals(getTimeStamp(), that.getTimeStamp()) &&
                Objects.equals(getOriginator(), that.getOriginator()) &&
                Objects.equals(getSeverity(), that.getSeverity()) &&
                Objects.equals(getReportingComponent(), that.getReportingComponent()) &&
                Objects.equals(getMessageId(), that.getMessageId()) &&
                Objects.equals(getMessageText(), that.getMessageText()) &&
                Objects.equals(getAdditionalInformation(), that.getAdditionalInformation()) &&
                Objects.equals(getSystemAction(), that.getSystemAction()) &&
                Objects.equals(getUserAction(), that.getUserAction());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, getTimeStamp(), getOriginator(), getSeverity(), getReportingComponent(),
                            getMessageId(),
                            getMessageText(), getAdditionalInformation(), getSystemAction(), getUserAction());
    }
}
