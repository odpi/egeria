/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;



/**
 * The GraphOMRSAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */


public enum GraphOMRSAuditCode implements AuditLogMessageSet
{

    GRAPH_REPOSITORY_CREATED("OMRS-GRAPH-REPOSITORY-0001",
                             AuditLogRecordSeverityLevel.INFO,
                             "The OMRS Graph Repository has been created.",
                             "The local server has created and initialized a new Local OMRS Graph Repository database.",
                             "This repository will be used as the local repository for this server. Verify that this is the first time that " +
                                     "the server is being started with the graph repository.  If it has not then shut down the server " +
                                     "immediately and track down why the server is not finding its repository.  It may be a configuration " +
                                     "change or the contents of the repository have been removed.  Once the repository has been restored, " +
                                     "restart the server and you should see OMRS-GRAPH-REPOSITORY-0003 at start up rather than this message."),

    GRAPH_REPOSITORY_HAS_DIFFERENT_METADATA_COLLECTION_ID("OMRS-GRAPH-REPOSITORY-0002",
                                                          AuditLogRecordSeverityLevel.EXCEPTION,
            "The OMRS Graph Database {0} contains a metadataCollectionId {1} that does not match the requested metadataCollectionId {2}.",
            "The graph database is for a different metadata repository. Cannot proceed with initialization of the graph repository.",
            "The likely cause of this error is either that the configuration document for the server has been deleted and recreated, " +
                                                                  "causing a new metadata collection id to be generated or there are two servers " +
                                                                  "with the same name." +
                                                                  "Check whether the repository has been " +
                                                                  "reconfigured" +
                                                                  " with a different metadataCollectionId. " +
                                                                  "Update the repository connection " +
                                                                  "configuration in the server's configuration " +
                                                                  "document to match the " +
                                                                  "metadataCollectionId for the database.  " +
                                                                  "Otherwise rename this server so that it has a unique name and restart it."),

    GRAPH_REPOSITORY_OPENED("OMRS-GRAPH-REPOSITORY-0003",
                            AuditLogRecordSeverityLevel.INFO,
            "The OMRS Graph Repository has been opened.",
            "The local server has created and initialized the Local OMRS Graph Repository database.",
            "No action is required. The existing graph repository has been opened and validated successfully."),
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;

    /**
     * The constructor for GraphOMRSAuditCode expects to be passed one of the enumeration rows defined in
     * GraphOMRSAuditCode above.   For example:
     *     GraphOMRSAuditCode   auditCode = GraphOMRSAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId unique Id for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    GraphOMRSAuditCode(String                      messageId,
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
}

