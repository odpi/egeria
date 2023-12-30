/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The BasicFilesIntegrationConnectorsAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum BasicFilesIntegrationConnectorsAuditCode implements AuditLogMessageSet
{
    CONNECTOR_CONFIGURATION("BASIC-FILES-INTEGRATION-CONNECTORS-0001",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector has been initialized to monitor file directory {1} with allowCatalogDelete={2} and templateQualifiedName={3}",
                            "The connector is designed to monitor changes to the content of the named file directory (folder).  " +
                                  "If allowCatalogDelete is set to true, it will delete catalog entries for files that are deleted from the file system, " +
                                  "otherwise the catalog entries are marked as archived so they remain available to act as tombstones in the lineage graphs.  " +
                                  "If the templateQualifiedName is set, it identifies a template entity to use ",
                            "No specific action is required.  This message is to confirm the configuration for the integration connector."),

    BAD_CONFIGURATION("BASIC-FILES-INTEGRATION-CONNECTORS-0002",
                      AuditLogRecordSeverityLevel.ERROR,
                          "The {0} integration connector encountered an {1} exception when opening directory {2} during the {3} method.  The exception message included was {4}",
                          "The exception is passed back to the Files Integrator OMIS in the integration daemon that is hosting " +
                                  "this connector to enable it to perform error handling.  More messages are likely to follow describing the " +
                                  "error handling that was performed.  These can help to determine how to recover from this error.",
                          "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                  "exception stack trace to determine why the connector is not able to access the directory and resolve that issue.  " +
                                  "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                  "connector in the integration daemon once the original cause of the error has been corrected."),

    BAD_FOLDER_ELEMENT("BASIC-FILES-INTEGRATION-CONNECTORS-0003",
                       AuditLogRecordSeverityLevel.ERROR,
                       "The {0} integration connector retrieved an incomplete FileFolder asset for directory {1}: {2}",
                       "The metadata element for the directory that was retrieved from the open metadata repositories has missing " +
                               "information.  This is likely to be a logic error in the Files Integrator OMIS or Data Manager OMAS.",
                       "Look for errors in the audit logs for the integration daemon where the connector and Files Integrator OMIS are " +
                               "running and the metadata server where the Data Manager OMAS is running.  Collect these diagnostics and " +
                               "ask the Egeria community for help to determine why the FileFolder asset is incomplete."),

    UNEXPECTED_EXC_RETRIEVING_FOLDER("BASIC-FILES-INTEGRATION-CONNECTORS-0004",
                                     AuditLogRecordSeverityLevel.ERROR,
                            "An unexpected {0} exception was returned to the {1} integration connector by the Files Integrator OMIS {2} " +
                                    "method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5}",
                                     "The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.",
                                     "Use the message in the nested exception to determine the root cause of the error. Once this is " +
                                             "resolved, follow the instructions in the messages produced by the integration daemon to restart this connector."),

    DIRECTORY_MONITORING_STARTING("BASIC-FILES-INTEGRATION-CONNECTORS-0005",
                                  AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector is initiating the monitoring of file directory {1}",
                              "The connector is calling the monitoring library from Apache Commons. " +
                                      "This will start a background thread to monitor the file directory.  Any changes to the files in the " +
                                      "directory will be reported to this integration connector.",
                              "No action is required unless there are errors that follow indicating that the monitoring of the directory failed to start."),

    UNEXPECTED_EXC_MONITOR_START("BASIC-FILES-INTEGRATION-CONNECTORS-0006",
                                 AuditLogRecordSeverityLevel.ERROR,
                                     "An unexpected {0} exception was returned to the {1} integration connector by the Apache Commons " +
                                             "FileAlterationMonitor for directory {2} while it was starting the monitoring service.  The error message was {3}",
                                     "The exception is logged and the integration connector continues to synchronize metadata " +
                                             "through the refresh process.",
                                     "Use the message in the unexpected exception to determine the root cause of the error. Once this is " +
                                             "resolved, follow the instructions in the messages produced by the integration daemon to restart the connector. " +
                                             "Then validate that the monitoring starts successfully."),

    DIRECTORY_MONITORING_STOPPING("BASIC-FILES-INTEGRATION-CONNECTORS-0007",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector is stopping the monitoring of file directory {1}",
                                  "The connector is calling the monitoring library from Apache Commons to stop the monitoring of the directory.  " +
                                          "This will stop the background thread monitoring the file directory.  Any changes to the files in the " +
                                          "directory will be ignored by the connector.",
                                  "No action is required unless there are errors that follow indicating that the monitoring failed to stop."),

    UNEXPECTED_EXC_MONITOR_STOP("BASIC-FILES-INTEGRATION-CONNECTORS-0008",
                                AuditLogRecordSeverityLevel.ERROR,
                                 "An unexpected {0} exception was returned to the {1} integration connector by the Apache Commons " +
                                         "FileAlterationMonitor for directory {2} while it stopping the monitoring service.  The error message was {3}",
                                 "The exception is logged and the integration connector continues to shutdown.",
                                 "Use the message in the unexpected exception to determine the root cause of the error. Once this is " +
                                         "resolved, follow the instructions in the messages produced by the integration daemon to restart the connector."),

    CONNECTOR_STOPPING("BASIC-FILES-INTEGRATION-CONNECTORS-0009",
                       AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector has stopped its file monitoring and is shutting down",
                                  "The connector is disconnecting.",
                                  "No action is required unless there are errors that follow indicating that there were problems shutting down."),

    DATA_FOLDER_UPDATED("BASIC-FILES-INTEGRATION-CONNECTORS-0010",
                        AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has updated the last updated time in the DataFolder {1} to {2}",
                       "The connector updated the DataFolder as part of its refresh processing.",
                       "No action is required.  This message is to record the reason for the update to the DataFolder."),

    DATA_FOLDER_UPDATED_FOR_FILE("BASIC-FILES-INTEGRATION-CONNECTORS-0011",
                                 AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector has updated the last updated time in the DataFolder {1} to {2} because of changes to file {3}",
                        "The connector updated the DataFolder as part of its monitoring of the files in the file directory.",
                        "No action is required.  This message is to record the reason why the DataFolder was updated."),

    UNEXPECTED_EXC_FOLDER_UPDATE("BASIC-FILES-INTEGRATION-CONNECTORS-0012",
                                 AuditLogRecordSeverityLevel.ERROR,
                                "An unexpected {0} exception was returned to the {1} integration connector when it tried to update the " +
                                        "DataFolder {2} in the metadata repositories for directory {3}.  The error message was {4}",
                                "The exception is logged and the integration connector continues to synchronize metadata.",
                                "Use the message in the unexpected exception to determine the root cause of the error and restart " +
                                        "the connector once it is resolved."),

    BAD_FILE_ELEMENT("BASIC-FILES-INTEGRATION-CONNECTORS-0013",
                     AuditLogRecordSeverityLevel.ERROR,
                       "The {0} integration connector retrieved an incomplete DataFile asset: {1}",
                       "The metadata element for the file that was retrieved from the open metadata repositories has missing " +
                               "information.  This is likely to be a logic error in the Files Integrator OMIS or Data Manager OMAS.",
                       "Look for errors in the audit logs for the integration daemon where the connector and Files Integrator OMIS are " +
                               "running and the metadata server where the Data Manager OMAS is running.  Collect these diagnostics and " +
                               "ask the Egeria community for help to determine why the DataFile element is incomplete."),

    UNEXPECTED_EXC_DATA_FILE_UPDATE("BASIC-FILES-INTEGRATION-CONNECTORS-0014",
                                    AuditLogRecordSeverityLevel.ERROR,
                                 "An unexpected {0} exception was returned to the {1} integration connector when it tried to update the " +
                                         "DataFile in the metadata repositories for file {2}.  The error message was {3}",
                                 "The exception is logged and the integration connector continues to synchronize metadata.  " +
                                         "This file is not catalogued at this time but may succeed later.",
                                 "Use the message in the unexpected exception to determine the root cause of the error and fix it."),

    MISSING_TEMPLATE("BASIC-FILES-INTEGRATION-CONNECTORS-0015",
                     AuditLogRecordSeverityLevel.ERROR,
                     "The {0} integration connector is unable to retrieve the DataFile template with qualified name: {1}",
                     "The metadata element for the template is not found in the open metadata repositories.  " +
                             "The template name was configured for the connector.  This means that files should be catalogued " +
                             "using the template.  Since the template is missing, files are not being catalogued.",
                     "Create the template in the metadata repository.  The connector will catalog the files during " +
                             "its next periodic refresh or you can force it to refresh immediately by calling the refresh" +
                             "operation on the integration daemon."),

    DATA_FILE_CREATED("BASIC-FILES-INTEGRATION-CONNECTORS-0016",
                      AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector created the DataFile {1} ({2}) for a new real-world file",
                     "The connector created the DataFile as part of its monitoring of the files in the file directory.",
                     "No action is required.  This message is to record the reason why the DataFolder was created."),

    DATA_FILE_CREATED_FROM_TEMPLATE("BASIC-FILES-INTEGRATION-CONNECTORS-0017",
                                    AuditLogRecordSeverityLevel.INFO,
                      "The {0} integration connector created the DataFile {1} ({2}) for a new real-world file using template {3} ({4})",
                      "The connector created the DataFile as part of its monitoring of the files in the file directory.  " +
                              "The template provides details of additional metadata that should also be attached to the new DataFile element.  " +
                              "It was specified in the templateQualifiedName configuration property of the connector.",
                      "No action is required.  This message is to record the reason why the DataFile was created with the template."),

    DATA_FILE_UPDATED("BASIC-FILES-INTEGRATION-CONNECTORS-0018",
                      AuditLogRecordSeverityLevel.INFO,
                      "The {0} integration connector has updated the DataFile {1} ({2}) because the real-world file changed",
                      "The connector updated the DataFile as part of its monitoring of the files in the file directory.",
                      "No action is required.  This message is to record the reason why the DataFile was updated."),

    DATA_FILE_DELETED("BASIC-FILES-INTEGRATION-CONNECTORS-0019",
                      AuditLogRecordSeverityLevel.INFO,
                      "The {0} integration connector has deleted the DataFile {1} ({2}) because the real-world file is no longer stored in the directory",
                      "The connector removed the DataFile as part of its monitoring of the files in the file directory.",
                      "No action is required.  This message is to record the reason why the DataFile was removed."),

    DATA_FILE_ARCHIVED("BASIC-FILES-INTEGRATION-CONNECTORS-0020",
                       AuditLogRecordSeverityLevel.INFO,
                      "The {0} integration connector has archived the DataFile {1} ({2}) because the real-world file is no longer stored in the directory",
                      "The connector updated the DataFile to reflect that is is now just a placeholder for an asset that no longer exists.  " +
                              "Its presence is still needed in the metadata repository for lineage reporting.",
                      "No action is required.  This message is to record the reason why the DataFile was archived."),


    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                     userAction;


    /**
     * The constructor for BasicFilesIntegrationConnectorsAuditCode expects to be passed one of the enumeration rows defined in
     * BasicFilesIntegrationConnectorsAuditCode above.   For example:
     *     BasicFilesIntegrationConnectorsAuditCode   auditCode = BasicFilesIntegrationConnectorsAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    BasicFilesIntegrationConnectorsAuditCode(String                      messageId,
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
    public AuditLogMessageDefinition getMessageDefinition(String ...params)
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
        return "OIFAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
