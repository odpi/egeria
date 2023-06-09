/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.auditlog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AuditLogRecord provides a carrier for details about a single log record in the audit log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuditLogRecord
{
    private String                     guid                  = null;
    private Date                       timeStamp             = null;
    private Map<String, String>        originatorProperties  = null;
    private AuditLogReportingComponent originatorComponent   = null;
    private String                     actionDescription     = null;
    private long                       threadId              = 0L;
    private String                     threadName            = null;
    private int                        severityCode          = 0;
    private String                     severity              = null;
    private String                     messageId             = null;
    private String                     messageText           = null;
    private String[]                   messageParameters     = null;
    private List<String>               additionalInformation = null;
    private String                     systemAction          = null;
    private String                     userAction            = null;
    private String                     exceptionClassName    = null;
    private String                     exceptionMessage      = null;
    private String                     exceptionStackTrace   = null;


    /**
     * Default constructor
     */
    public AuditLogRecord()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AuditLogRecord(AuditLogRecord template)
    {
        this.guid                 = template.getGUID();
        this.timeStamp            = template.getTimeStamp();
        this.originatorProperties = template.getOriginatorProperties();
        this.originatorComponent  = template.getOriginatorComponent();
        this.actionDescription    = template.getActionDescription();
        this.threadId             = template.getThreadId();
        this.threadName           = template.getThreadName();
        this.severityCode         = template.getSeverityCode();
        this.severity             = template.getSeverity();
        this.messageId            = template.getMessageId();
        this.messageText          = template.getMessageText();
        this.messageParameters     = template.getMessageParameters();
        this.additionalInformation = template.getAdditionalInformation();
        this.systemAction          = template.getSystemAction();
        this.userAction            = template.getUserAction();
        this.exceptionClassName    = template.getExceptionClassName();
        this.exceptionMessage      = template.getExceptionMessage();
        this.exceptionStackTrace   = template.getExceptionStackTrace();
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
     * @return map of name value pairs
     */
    public Map<String, String> getOriginatorProperties()
    {
        return originatorProperties;
    }


    /**
     * Set up details of the originator of the log record.
     *
     * @param originatorProperties  map of name value pairs
     */
    public void setOriginatorProperties(Map<String, String> originatorProperties)
    {
        this.originatorProperties = originatorProperties;
    }


    /**
     * Return the name of the component that reported the situation recorded in the log record.
     *
     * @return ComponentDescription object
     */
    public AuditLogReportingComponent getOriginatorComponent()
    {
        return originatorComponent;
    }


    /**
     * Set up the name of the component that reported the situation recorded in the log record.
     *
     * @param originatorComponent  ComponentDescription object
     */
    public void setOriginatorComponent(AuditLogReportingComponent originatorComponent)
    {
        this.originatorComponent = originatorComponent;
    }


    /**
     * Return the description of the activity that this log record relates.
     *
     * @return string description
     */
    public String getActionDescription()
    {
        return actionDescription;
    }


    /**
     * Set up the description of the activity that this log record relates.
     *
     * @param actionDescription string description
     */
    public void setActionDescription(String actionDescription)
    {
        this.actionDescription = actionDescription;
    }


    /**
     * Return the identifier of the thread where the situation occurred.
     * This is useful for correlating records in a multi-threaded environment.
     *
     * @return long id
     */
    public long getThreadId()
    {
        return threadId;
    }


    /**
     * Set up the identifier of the thread where the situation occurred.
     *
     * @param threadId long id
     */
    public void setThreadId(long threadId)
    {
        this.threadId = threadId;
    }


    /**
     * Return the name of the thread where the situation occurred.
     *
     * @return string name
     */
    public String getThreadName()
    {
        return threadName;
    }


    /**
     * Set up he name of the thread where the situation occurred.
     *
     * @param threadName string name
     */
    public void setThreadName(String threadName)
    {
        this.threadName = threadName;
    }


    /**
     * Return the severity of the situation recorded in the log record.
     *
     * @return int unique id for the severity
     */
    public int getSeverityCode()
    {
        return severityCode;
    }


    /**
     * Set up the severity of the situation recorded in the log record.
     *
     * @param severityCode  unique id for the severity
     */
    public void setSeverityCode(int severityCode)
    {
        this.severityCode = severityCode;
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
     * Return the array of values that were inserted in the message text.
     *
     * @return array of strings
     */
    public String[] getMessageParameters()
    {
        return messageParameters;
    }


    /**
     * Set up the array of values that were inserted in the message text.
     *
     * @param messageParameters array of strings
     */
    public void setMessageParameters(String[] messageParameters)
    {
        this.messageParameters = messageParameters;
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
        return "AuditLogRecord{" +
                "guid='" + guid + '\'' +
                ", timeStamp=" + timeStamp +
                ", originator=" + originatorProperties +
                ", reportingComponent=" + originatorComponent +
                ", actionDescription='" + actionDescription + '\'' +
                ", threadId=" + threadId +
                ", threadName='" + threadName + '\'' +
                ", severityCode=" + severityCode +
                ", severity='" + severity + '\'' +
                ", messageId='" + messageId + '\'' +
                ", messageText='" + messageText + '\'' +
                ", messageParameters=" + Arrays.toString(messageParameters) +
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
        AuditLogRecord that = (AuditLogRecord) objectToCompare;
        return threadId == that.threadId &&
                severityCode == that.severityCode &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(timeStamp, that.timeStamp) &&
                Objects.equals(originatorProperties, that.originatorProperties) &&
                Objects.equals(originatorComponent, that.originatorComponent) &&
                Objects.equals(actionDescription, that.actionDescription) &&
                Objects.equals(threadName, that.threadName) &&
                Objects.equals(severity, that.severity) &&
                Objects.equals(messageId, that.messageId) &&
                Objects.equals(messageText, that.messageText) &&
                Arrays.equals(messageParameters, that.messageParameters) &&
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
        int result = Objects.hash(guid, timeStamp, originatorProperties, originatorComponent, actionDescription, threadId, threadName, severityCode, severity,
                                  messageId, messageText, additionalInformation, systemAction, userAction, exceptionClassName, exceptionMessage,
                                  exceptionStackTrace);
        result = 31 * result + Arrays.hashCode(messageParameters);
        return result;
    }
}
