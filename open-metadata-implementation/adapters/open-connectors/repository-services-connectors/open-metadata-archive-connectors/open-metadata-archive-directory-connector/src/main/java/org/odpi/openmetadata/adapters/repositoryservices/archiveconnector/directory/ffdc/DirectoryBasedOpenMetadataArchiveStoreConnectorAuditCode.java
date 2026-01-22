/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode implements AuditLogMessageSet
{
    /**
     * OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0001 - Opening directory "{0}" for Open Metadata Archive Store
     */
    OPENING_FILE("OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0001",
                 AuditLogRecordSeverityLevel.STARTUP,
                 "Opening directory \"{0}\" for Open Metadata Archive Store",
                 "The caller is requesting the contents of the open metadata archive store which is located in the named directory.",
                 "Validate that the directory name is correct.  "),

    /**
     * OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0002 - Unable to open directory "{0}".  Message from {1} exception was {2}
     */
    BAD_FILE("OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0002",
             AuditLogRecordSeverityLevel.EXCEPTION,
              "Unable to open directory \"{0}\".  Message from {1} exception was {2}",
              "The caller is cannot open an open metadata archive.",
              "Use the information from the exception to determine the cause of the error.  For example, is the directory (folder) name correct?  " +
                      "Look particularly for extraneous quotes, " +
                      "incorrect directory name (relative files are read from the perspective of the caller's home directory) or incorrect characters.  Does the server have permission to access the directory?  Once the cause of the error is corrected, restart the caller."),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    

    /**
     * The constructor for DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode above.   For example:
     *     DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode   auditCode = DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId unique id for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode(String                      messageId,
                                                             AuditLogRecordSeverityLevel severity,
                                                             String                      message,
                                                             String                      systemAction,
                                                             String                      userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return new AuditLogMessageDefinition(logMessageId,
                                             severity,
                                             logMessage,
                                             systemAction,
                                             userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String... params)
    {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                                                                                    severity,
                                                                                    logMessage,
                                                                                    systemAction,
                                                                                    userAction);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "AuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
