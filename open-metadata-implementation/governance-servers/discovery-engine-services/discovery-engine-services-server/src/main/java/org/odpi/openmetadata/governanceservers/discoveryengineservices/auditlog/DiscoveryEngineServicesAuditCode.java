/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The DiscoveryServerAuditCode is used to define the message content for the OMRS Audit Log.
 *
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
public enum DiscoveryServerAuditCode
{
    SERVER_INITIALIZING("OMAS-DISCOVERY-SERVER-0001",
                        OMRSAuditLogRecordSeverity.INFO,
                        "Discovery server {0} is initializing",
                        "A new OMAG server has been started that is configured to run as a discovery server.  " +
                                 "Within the discovery server are one or more discovery engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "discovery engines is retrieved from the metadata server and the discovery engines are initialized.",
                        "No action is required.  This is part of the normal operation of the service."),

    NO_OMAS_SERVER_URL("OMAS-DISCOVERY-SERVER-0002",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Discovery server {0} is not configured with the platform URL root for the Discovery Engine OMAS",
                         "The server is not able to retrieve its configuration.  It fails to start.",
                         "Add the platform URL root of the open metadata server where the Discovery Engine OMAS is running " +
                               "to this discovery server's configuration document."),

    NO_OMAS_SERVER_NAME("OMAS-DISCOVERY-SERVER-0003",
                        OMRSAuditLogRecordSeverity.ERROR,
                       "Discovery server {0} is not configured with the name for the server running the Discovery Engine OMAS",
                      "The server is not able to retrieve its configuration.  It fails to start.",
                     "Add the server name of the open metadata server where the Discovery Engine OMAS is running " +
                                "to this discovery server's configuration document."),

    NO_DISCOVERY_ENGINES("OMAS-DISCOVERY-SERVER-0004",
                        OMRSAuditLogRecordSeverity.ERROR,
                       "Discovery server {0} is configured with no discovery engines",
                       "The server is not able to run any discovery requests.  It fails to start.",
                       "Add the qualified name for at least one discovery engine to this discovery server's configuration document."),

    ENGINE_INITIALIZING("OMAS-DISCOVERY-SERVER-0005",
                        OMRSAuditLogRecordSeverity.INFO,
                        "A new discovery engine instance {0} is initializing in discovery server {1}",
                        "The discovery server is initializing a discovery engine to analyze the " +
                                "content of assets on demand and create annotation metadata.  The configuration for this discovery engine was " +
                                "retrieved from the Discovery Engine OMAS running in the open metadata server",
                        "No action is required.  This is part of the normal operation of the service."),

    NO_CONFIG_SERVER("OMAS-DISCOVERY-SERVER-0006",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "Discovery engine {0} in discovery server {1} is not able to retrieve any metadata from the Discovery Engine OMAS" +
                             "running in server {2} on OMAG Server Platform {3}.  The Error message was: {4}",
                         "The discovery engine is unable to retrieved its configuration.  " +
                             "It has failed to start and will not be able to process any discovery requests.",
                         "Diagnose why the calls to Discovery Engine OMAS are not working.  " +
                             "It could be because of security, or the server is not running, or the Discovery Engine OMAS " +
                             "has not been started in this server.  The error message should help to narrow down the cause of the error.  " +
                             "Once the problem has been resolved, restart the discovery engine"),

    SERVER_NOT_AUTHORIZED("OMAS-DISCOVERY-SERVER-0007",
                     OMRSAuditLogRecordSeverity.EXCEPTION,
                     "Discovery engine {0} in discovery server {1} is not able to retrieve any its configuration from the Discovery Engine " +
                                  "OMAS running in server {2} on OMAG Server Platform {3} with userId {4}.  The Error message was: {5}",
                     "The discovery engine is unable to retrieved its configuration.  " +
                             "It has failed to start and will not be able to process any discovery requests.",
                     "Diagnose why the calls to Discovery Engine OMAS are not working.  " +
                             "It could be because because this discovery server is configured with the wrong userId, it is calling the wrong server, or the " +
                                  "remote server is correct but it needs updating to allow this userId.  The error message should help to narrow down the cause of the error.  " +
                             "Once the problem has been resolved, restart the discovery engine"),

    SUPPORTED_ASSET_TYPE("OMAS-DISCOVERY-SERVER-0008",
                        OMRSAuditLogRecordSeverity.INFO,
                        "Discovery engine {0} in discovery server {1} is configured to process discovery requests for asset type {2}",
                        "The discovery engine has successfully retrieved the configuration to run analysis requests for the named asset type.",
                        "No action is required.  This is part of the normal operation of the service."),

    NO_SUPPORTED_ASSET_TYPES("OMAS-DISCOVERY-SERVER-0009",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Discovery engine {0} in discovery server {1} is not configured to process discovery requests for an asset types",
                         "The discovery engine has no specific configuration.  It is not able to process any discovery requests.",
                         "Add the configuration for at least one asset type to this discovery engine."),

    ASSET_TYPE_CONFIG_ERROR("OMAS-DISCOVERY-SERVER-0010",
                             OMRSAuditLogRecordSeverity.EXCEPTION,
                             "Discovery engine {0} in discovery server {1} is not able to retrieve its configured asset types from the Discovery Engine OMAS running in server {2} on OMAG Server Platform {3}.  The error message was {4}",
                             "The discovery engine has not been able to retrieve its configuration.  It is not able to process any discovery requests.",
                             "Diagnose why the calls to Discovery Engine OMAS are not working.  " +
                                    "The error message should help to narrow down the cause of the error.  " +
                                    "Once the problem has been resolved, restart the discovery engine."),

    ENGINE_INITIALIZED("OMAS-DISCOVERY-SERVER-0011",
                        OMRSAuditLogRecordSeverity.INFO,
                        "The discovery engine {0} in discovery server {1} has initialized",
                        "The discovery engine has completed initialization and is ready to receive discovery requests.",
                        "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INSTANCE_FAILURE("OMAS-DISCOVERY-SERVER-0012",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "The discovery engine services are unable to initialize a new instance of discovery server {0}; error message is {1}",
                             "The discovery engine services detected an error during the start up of a specific discovery server instance.  Its discovery services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    SERVER_INITIALIZED("OMAS-DISCOVERY-SERVER-0013",
                       OMRSAuditLogRecordSeverity.INFO,
                       "The discovery server {0} has initialized",
                       "The discovery server has completed initialization.",
                       "No action is required.  This is part of the normal operation of the service."),

    SERVER_SHUTTING_DOWN("OMAS-DISCOVERY-SERVER-0014",
                    OMRSAuditLogRecordSeverity.INFO,
                    "The discovery server {0} is shutting down",
                    "The local administrator has requested shut down of this discovery server.",
                    "No action is required.  This is part of the normal operation of the service."),

    ENGINE_SHUTDOWN("OMAS-DISCOVERY-SERVER-0015",
                    OMRSAuditLogRecordSeverity.INFO,
                    "The discovery engine {0} in discovery server {1} is shutting down",
                    "The local administrator has requested shut down of this discovery engine.  No more discovery requests will be processed by this engine.",
                    "No action is required.  This is part of the normal operation of the service."),

    SERVER_SHUTDOWN("OMAS-DISCOVERY-SERVER-0016",
                         OMRSAuditLogRecordSeverity.INFO,
                         "The discovery server {0} has completed shutdown",
                         "The local administrator has requested shut down of this discovery server and the operation has completed.",
                         "No action is required.  This is part of the normal operation of the service."),

    DISCOVERY_SERVICE_STARTING("OMAS-DISCOVERY-SERVER-0017",
                    OMRSAuditLogRecordSeverity.INFO,
                    "The discovery service {0} is starting to analyze asset {1} of type {2} in discovery engine {3} (guid={4}); the results will be stored in discovery analysis report {5}",
                    "A new discovery request is being processed.",
                    "No action is required.  This is part of the normal operation of the service."),

    DISCOVERY_SERVICE_FAILED("OMAS-DISCOVERY-SERVER-0018",
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    "The discovery service {0} threw an exception of type {1} during the generation of discovery analysis report {2} for asset {3} of type {4} in discovery engine {5} (guid={6}). The error message was {7}",
                    "A discovery services failed to complete the analysis of .",
                    "No action is required.  This is part of the normal operation of the service."),

    DISCOVERY_SERVICE_COMPLETE("OMAS-DISCOVERY-SERVER-0019",
                               OMRSAuditLogRecordSeverity.INFO,
                               "The discovery service {0} has completed the analysis of asset {1} of type {2} in {3} milliseconds; results stored in report {4}",
                               "A discovery request has completed.",
                               "No action is required.  This is part of the normal operation of the service."),

    NO_DISCOVERY_ENGINES_STARTED("OMAS-DISCOVERY-SERVER-0020",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Discovery server {0} is unable to start any discovery engines",
                         "The server is not able to run any discovery requests.  It fails to start.",
                         "Add the configuration for at least one discovery engine to this discovery server."),

    EXC_ON_ERROR_STATUS_UPDATE("OMAS-DISCOVERY-SERVER-0021",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "Discovery engine {0} is unable to update failed status for discovery service {1}.  The exception was {2} with error message {3}",
                                 "The server is not able to record the failed result for a discovery request. The discovery report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the discovery request."),

    UNKNOWN_DISCOVERY_ENGINE_NAME( "OMAS-DISCOVERY-SERVER-0022",
                                   OMRSAuditLogRecordSeverity.EXCEPTION,
                                  "Properties for discovery engine called {0} are not returned by open metadata server {1}.  Exception {2} with message {3} returned to discovery server {4}",
                                  "The discovery server is not able to initialize the discovery engine and so it will not de able to support " +
                                           "discovery requests targeted to this discovery engine.",
                                  "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the discovery server.  Once the cause is resolved, restart the discovery server."),

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(DiscoveryServerAuditCode.class);


    /**
     * The constructor for DiscoveryServerAuditCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerAuditCode above.   For example:
     *
     *     DiscoveryServerAuditCode   auditCode = DiscoveryServerAuditCode.SERVER_SHUTDOWN;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DiscoveryServerAuditCode(String                     messageId,
                             OMRSAuditLogRecordSeverity severity,
                             String                     message,
                             String                     systemAction,
                             String                     userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== DiscoveryServerAuditCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> DiscoveryServerAuditCode.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }
}
