/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The FileBasedRegistryStoreConnectorAuditCode is used to define the message content for the Audit Log.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum FileBasedRegistryStoreConnectorAuditCode implements AuditLogMessageSet
{
    
    CREATE_REGISTRY_FILE("OCF-FILE-REGISTRY-STORE-CONNECTOR-0115",
                         OMRSAuditLogRecordSeverity.COHORT,
                         "Creating new cohort registry store {0}",
                         "The local server is creating a new cohort registry store. " +
                                 "The local server should continue to operate correctly.",
                         "Verify that the local server is connecting to the open metadata repository cohort for" +
                                 "the first time."),

    UNUSABLE_REGISTRY_FILE("OCF-FILE-REGISTRY-STORE-CONNECTOR-0116",
                           OMRSAuditLogRecordSeverity.EXCEPTION,
                           "Unable to write to cohort registry store {0}",
                           "The local server can not write to the cohort registry store. " +
                                   "This is a serious issue because the local server is not able to record its " +
                                   "interaction with other servers in the cohort.",
                           "Shutdown the local server and resolve the issue with the repository store."),

    NULL_MEMBER_REGISTRATION("OCF-FILE-REGISTRY-STORE-CONNECTOR-0117",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "Unable to read or write to cohort registry store {0} because registration information is null",
                             "The local server can not manage a member registration in the cohort registry store because " +
                                     "the registration information is null. " +
                                     "This is a serious issue because the local server is not able to record its " +
                                     "interaction with other servers in the cohort.",
                             "Shutdown the local server and resolve the issue with the cohort registry."),


    MISSING_MEMBER_REGISTRATION("OCF-FILE-REGISTRY-STORE-CONNECTOR-0118",
                                OMRSAuditLogRecordSeverity.ERROR,
                                "Unable to process the {0} request for cohort {1} from cohort member {2} " +
                                        "because there is no cohort registry store",
                                "The local server can not process a member registration event " +
                                        "because the registration information cal not be stored in the cohort registry store. " +
                                        "This may simply be a timing issue. " +
                                        "However, it may be the result of an earlier issue with the " +
                                        "local cohort registry store.",
                                "Verify that there are no issues with writing to the cohort registry store."),

    DUPLICATE_REGISTERED_MC_ID("OCF-FILE-REGISTRY-STORE-CONNECTOR-0119",
                               OMRSAuditLogRecordSeverity.ACTION,
                               "Metadata collection id {0} is being used by server {1} and server {2}",
                               "The local server has detected a duplicate record in its cohort registry store.",
                               "Verify that this is caused by the rename of a server."),

    NULL_REGISTERED_MC_ID("OCF-FILE-REGISTRY-STORE-CONNECTOR-0120",
                          OMRSAuditLogRecordSeverity.ACTION,
                          "Server {0} has registered with a null metadata collection id",
                          "The local server has detected an invalid record in its cohort registry store.",
                          "Correct the configuration of the named server so that it has a valid metadata collection id."),

    DUPLICATE_REGISTERED_SERVER_NAME("OCF-FILE-REGISTRY-STORE-CONNECTOR-0121",
                                     OMRSAuditLogRecordSeverity.ACTION,
                                     "Server name {0} is being used by metadata collection {1} and metadata collection {2}",
                                     "The local server has identified a duplicate record in its cohort registry store.",
                                     "This suggests that a server has been restarted with a different metadata collection id."),
    
    NULL_REGISTERED_SERVER_NAME("OCF-FILE-REGISTRY-STORE-CONNECTOR-0122",
                                OMRSAuditLogRecordSeverity.ACTION,
                                "The server using metadata collection id {0} has registered with a null server name",
                                "The local server has detected an suspicious record in its cohort registry store.",
                                "Correct the configuration of the named server so that it has a valid server name."),

    DUPLICATE_REGISTERED_SERVER_ADDR("OCF-FILE-REGISTRY-STORE-CONNECTOR-0123",
                                     OMRSAuditLogRecordSeverity.ACTION,
                                     "Server name {0} with metadata collection id {1} is using the same server address of {2} as server name {3} with metadata collection id {4}",
                                     "The local server has found a duplicate record in its cohort registry store.",
                                     "This indicates that a server has been restarted with a different metadata collection id."),

    NULL_REGISTERED_SERVER_ADDR("OCF-FILE-REGISTRY-STORE-CONNECTOR-0124",
                                OMRSAuditLogRecordSeverity.ACTION,
                                "The server name {0} using metadata collection id {1} has registered with a null server address",
                                "The local server has identified an suspicious record in its cohort registry store.",
                                "Correct the configuration of one of the named server so that it has a unique server address.  Otherwise one of the server will not be called during federated queries issued by the enterprise repository services."),

    NULL_REGISTERED_SERVER_CONNECTION("OCF-FILE-REGISTRY-STORE-CONNECTOR-0125",
                                      OMRSAuditLogRecordSeverity.ACTION,
                                      "The server name {0} using metadata collection id {1} has registered with a null server connection",
                                      "The local server has found an suspicious record in its cohort registry store.",
                                      "Correct the configuration of one of the named servers so that it has a unique server address.  Otherwise one of the server will not be called during federated queries issued by the enterprise repository services."),


    ;

    private final AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for FileBasedRegistryStoreConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * FileBasedRegistryStoreConnectorAuditCode above.   For example:
     *
     *     FileBasedRegistryStoreConnectorAuditCode   auditCode = FileBasedRegistryStoreConnectorAuditCode.BAD_FILE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId unique Id for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    FileBasedRegistryStoreConnectorAuditCode(String                     messageId,
                                             OMRSAuditLogRecordSeverity severity,
                                             String                     message,
                                             String                     systemAction,
                                             String                     userAction)
    {
        messageDefinition = new AuditLogMessageDefinition(messageId,
                                                          severity,
                                                          message,
                                                          systemAction,
                                                          userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
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
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }
}
