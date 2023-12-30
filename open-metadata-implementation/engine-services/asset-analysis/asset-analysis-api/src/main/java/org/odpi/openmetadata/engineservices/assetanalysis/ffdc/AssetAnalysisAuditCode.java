/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;



/**
 * The AssetAnalysisAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum AssetAnalysisAuditCode implements AuditLogMessageSet
{
    /**
     * OMES-ASSET-ANALYSIS-0001 - The Asset Analysis engine services are initializing in server {0}
     */
    ENGINE_SERVICE_INITIALIZING("OMES-ASSET-ANALYSIS-0001",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "The Asset Analysis engine services are initializing in server {0}",
                                "A new OMAG server has been started that is configured to run the Asset Analysis OMES.  " +
                                 "Within this engine service are one or more discovery engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "discovery engines is retrieved from the metadata server and the discovery engines are initialized.",
                                "Verify that the start up sequence goes on to initialize the configured discovery engines."),

    /**
     * OMES-ASSET-ANALYSIS-0012 - The Asset Analysis OMES is unable to initialize a new instance of itself in server {0}; error message is {1}
     */
    SERVICE_INSTANCE_FAILURE("OMES-ASSET-ANALYSIS-0012",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Asset Analysis OMES is unable to initialize a new instance of itself in server {0}; error message is {1}",
                             "The engine services detected an error during the start up of a specific engine host server instance.  Its discovery services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMES-ASSET-ANALYSIS-0014 - The Asset Analysis OMES in server {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("OMES-ASSET-ANALYSIS-0014",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                    "The Asset Analysis OMES in server {0} is shutting down",
                    "The local administrator has requested shut down of this engine service.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    /**
     * OMES-ASSET-ANALYSIS-0016 - The Asset Analysis OMES in server {0} has completed shutdown
     */
   SERVER_SHUTDOWN("OMES-ASSET-ANALYSIS-0016",
                   AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Asset Analysis OMES in server {0} has completed shutdown",
                         "The local administrator has requested shut down of this engine service and the operation has completed.",
                         "Verify that all configured discovery engines shut down successfully."),

    /**
     * OMES-ASSET-ANALYSIS-0017 - The discovery service {0} is starting to analyze asset {1} with discovery request type {2}
     * in discovery engine {3} (guid={4}); the results will be stored in discovery analysis report {5}
     */
    DISCOVERY_SERVICE_STARTING("OMES-ASSET-ANALYSIS-0017",
                               AuditLogRecordSeverityLevel.STARTUP,
                    "The discovery service {0} is starting to analyze asset {1} with discovery request type {2} in discovery engine {3} (guid={4});" +
                                       " the results will be stored in discovery analysis report {5}",
                    "A new discovery request is being processed.",
                    "Verify that the discovery service ran to completion."),

    /**
     * OMES-ASSET-ANALYSIS-0018 - The discovery service {0} threw a {1} exception during the generation of discovery analysis report
     * {2} for asset {3} during discovery request type {4} in discovery engine {5} (guid={6}). The error message was {7}
     */
    DISCOVERY_SERVICE_FAILED("OMES-ASSET-ANALYSIS-0018",
                             AuditLogRecordSeverityLevel.EXCEPTION,
                    "The discovery service {0} threw a {1} exception during the generation of discovery analysis report {2} for asset {3} " +
                                     "during discovery request type {4} in discovery engine {5} (guid={6}). The error message was {7}",
                    "A discovery service failed to complete the analysis of an asset.",
                    "Review the exception to determine the cause of the error."),

    /**
     * OMES-ASSET-ANALYSIS-0019 - The discovery service {0} has completed the analysis of asset {1} with discovery request type {2} in {3}
     * milliseconds; the results are stored in discovery analysis report {4}
     */
    DISCOVERY_SERVICE_COMPLETE("OMES-ASSET-ANALYSIS-0019",
                               AuditLogRecordSeverityLevel.SHUTDOWN,
                               "The discovery service {0} has completed the analysis of asset {1} with discovery request type {2} in {3} " +
                                       "milliseconds; the results are stored in discovery analysis report {4}",
                               "A discovery request has completed.",
                               "It is possible to query the result of the discovery request through the engine service's REST API."),

    /**
     * OMES-ASSET-ANALYSIS-0020 - Asset Analysis OMES in server {0} is unable to start any discovery engines
     */
    NO_DISCOVERY_ENGINES_STARTED("OMES-ASSET-ANALYSIS-0020",
                                 AuditLogRecordSeverityLevel.ERROR,
                         "Asset Analysis OMES in server {0} is unable to start any discovery engines",
                         "The engine service is not able to run any discovery requests.  It fails to start.",
                         "Add the configuration for at least one discovery engine to this engine service."),

    /**
     * OMES-ASSET-ANALYSIS-0021 - Discovery engine {0} is unable to update the status for discovery service {1}.
     * The exception was {2} with error message {3}
     */
    EXC_ON_ERROR_STATUS_UPDATE("OMES-ASSET-ANALYSIS-0021",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                                 "Discovery engine {0} is unable to update the status for discovery service {1}.  The exception was {2} with error " +
                                       "message {3}",
                                 "The server is not able to record the failed result for a discovery request. The discovery report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the discovery request."),

    /**
     * OMES-ASSET-ANALYSIS-0029 - The discovery service {0} linked to discovery request type {1} can not be started.
     * The {2} exception was returned with message {3}
     */
    INVALID_DISCOVERY_SERVICE( "OMES-ASSET-ANALYSIS-0029",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                               "The discovery service {0} linked to discovery request type {1} can not be started.  " +
                                      "The {2} exception was returned with message {3}",
                              "The discovery request is not run and an error is returned to the caller.",
                              "This may be an error in the discovery service's logic or the discovery service may not be properly deployed or " +
                                      "there is a configuration error related to the discovery engine.  " +
                                      "The configuration that defines the discovery request type in the discovery engine and links " +
                                      "it to the discovery service is maintained in the metadata server by the Discovery " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "discovery service's implementation has been deployed so the Asset Analysis OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the discovery service in which case, " +
                                      "raise an issue with the author of the discovery service to get it fixed.  Once the cause is resolved, " +
                                      "retry the discovery request."),
    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for AssetAnalysisAuditCode expects to be passed one of the enumeration rows defined in
     * AssetAnalysisAuditCode above.   For example:
     *     AssetAnalysisAuditCode   auditCode = AssetAnalysisAuditCode.SERVER_SHUTDOWN;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    AssetAnalysisAuditCode(String                      messageId,
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
