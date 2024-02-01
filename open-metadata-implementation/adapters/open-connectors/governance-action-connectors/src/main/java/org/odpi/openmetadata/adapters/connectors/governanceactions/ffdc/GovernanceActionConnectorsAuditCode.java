/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The GovernanceActionConnectorsAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum GovernanceActionConnectorsAuditCode implements AuditLogMessageSet
{
    /**
     * GOVERNANCE-ACTION-CONNECTORS-0001 - The {0} governance action service is copying source file {1} to destination file {2}
     */
    COPY_FILE("GOVERNANCE-ACTION-CONNECTORS-0001",
              AuditLogRecordSeverityLevel.INFO,
              "The {0} governance action service is copying source file {1} to destination file {2}",
              "The provisioning governance action service connector is designed to deploy files on request.  " +
                                  "This message confirms that a file has been copied.",
              "No specific action is required.  This message is to log that a copy provisioning action has taken place."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0002 - The {0} governance action service is moving source file {1} to destination file {2}
     */
    MOVE_FILE("GOVERNANCE-ACTION-CONNECTORS-0002",
              AuditLogRecordSeverityLevel.INFO,
              "The {0} governance action service is moving source file {1} to destination file {2}",
              "The provisioning governance action service connector is designed to deploy files on request.  " +
                      "This message confirms that a file has been moved.",
              "No specific action is required.  This message is to log that a move provisioning action has taken place."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0003 - The {0} governance action service is deleting file {1}
     */
    DELETE_FILE("GOVERNANCE-ACTION-CONNECTORS-0003",
                AuditLogRecordSeverityLevel.INFO,
              "The {0} governance action service is deleting file {1}",
              "The provisioning governance action service connector is designed to manage files on request.  " +
                      "This message confirms that a file has been delete.",
              "No specific action is required.  This message is to log that a delete provisioning action has taken place."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0004 - The {0} governance action service has created lineage from source {1} to process {2} to destination {3}
     */
    CREATED_LINEAGE("GOVERNANCE-ACTION-CONNECTORS-0004",
                    AuditLogRecordSeverityLevel.INFO,
                        "The {0} governance action service has created lineage from source {1} to process {2} to destination {3}",
                        "The provisioning governance action service connector has created lineage to cover the data movement it has just performed.",
                        "Validate that the lineage is being created between the correct metadata elements."),


    /**
     * GOVERNANCE-ACTION-CONNECTORS-0005 - The {0} governance action service has been called without a source file name to work with
     */
    NO_SOURCE_FILE_NAME("GOVERNANCE-ACTION-CONNECTORS-0005",
                        AuditLogRecordSeverityLevel.ERROR,
                "The {0} governance action service has been called without a source file name to work with",
                "The provisioning governance action service connector is designed to manage files on request.  " +
                        "It is unable to operate without the name of the source file and so it terminates with a FAILED completion status.",
                "The source file is passed to the governance action service through the request parameters or via the TargetForAction " +
                        "relationship.  Correct the information passed to the governance service and rerun the request."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0006 - The {0} governance action service is unable to provision file {1} 
     * to {2} destination folder using {3} file pattern
     */
    PROVISIONING_FAILURE("GOVERNANCE-ACTION-CONNECTORS-0006",
                         AuditLogRecordSeverityLevel.ERROR,
                              "The {0} governance action service is unable to provision file {1} to {2} destination folder using {3} file pattern",
                              "This message is logged and the governance action is marked as failed",
                              "Since no exception occurred it means that there are currently files already occupying all the possible file names allowed by the file pattern.  " +
                                      "Files in the destination folder need to be deleted or this connector needs to be reconfigured with a new destination folder or file pattern."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0007 - The {0} governance action service encountered an {1} exception when provisioning file {2} to {3}
     * destination folder using the {4} file pattern.  The exception message included was {5}
     */
    PROVISIONING_EXCEPTION("GOVERNANCE-ACTION-CONNECTORS-0007",
                           AuditLogRecordSeverityLevel.EXCEPTION,
                          "The {0} governance action service encountered an {1} exception when provisioning file {2} to {3} destination folder using the {4} file pattern.  The exception message included was {5}",
                          "The exception is logged.  More messages may follow if follow on attempts are made to provision the file.  These can help to determine how to recover from this error.",
                          "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                  "exception stack trace to determine why the connector is not able to access the directory and resolve that issue.  " +
                                  "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                  "connector in the integration daemon once the original cause of the error has been corrected."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0008 - The {0} governance action service encountered an {1} exception when attempting to retrieve the file 
     * path name from the attached endpoint.  The exception message included was {5}
     */
    ENDPOINT_EXCEPTION("GOVERNANCE-ACTION-CONNECTORS-0008",
                       AuditLogRecordSeverityLevel.EXCEPTION,
                           "The {0} governance action service encountered an {1} exception when attempting to retrieve the file path name from the attached endpoint.  The exception message included was {5}",
                           "The governance action connector will use the qualified name of the asset as the path name to work with",
                           "This message contains the exception that was the original cause of the problem. If using the qualified name is not " +
                                   "working, use the information from the " +
                                   "exception stack trace to determine why the connector is not able to access the endpoint and resolve that issue.  " +
                                   "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                   "connector in the integration daemon once the original cause of the error has been corrected."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0009 - The {0} governance action service is using the qualified name from the Folder asset as the path name: {1}
     */
    QUALIFIED_NAME_PATH_NAME("GOVERNANCE-ACTION-CONNECTORS-0009",
                             AuditLogRecordSeverityLevel.INFO,
                       "The {0} governance action service is using the qualified name from the Folder asset as the path name: {1}",
                       "The governance action connector will use the qualified name of the asset as the path name to work with.",
                       "Validate that the qualified name is a good choice for the path name.  If it is not, add a connection " +
                               "with an endpoint that has the desired path in its networkAddress property."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0010 - The {0} governance action service detected that asset {1} has no linked connection
     */
    NO_LINKED_CONNECTION("GOVERNANCE-ACTION-CONNECTORS-0010",
                         AuditLogRecordSeverityLevel.INFO,
                             "The {0} governance action service detected that asset {1} has no linked connection",
                             "Since the asset has no connection, the governance action connector will use the qualified name of the " +
                                     "asset as the path name to work with.",
                             "The governance action service will next produce the GOVERNANCE-ACTION-CONNECTORS-0006 message with the " +
                                     "qualified name.  Follow the instructions for this message."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0011 - The {0} governance action service detected that asset {1} has {2} linked connections for 
     * asset {3} and is not sure which one to use since they have inconsistent networkAddress properties in their endpoint
     */
    TOO_MANY_CONNECTIONS("GOVERNANCE-ACTION-CONNECTORS-0011",
                         AuditLogRecordSeverityLevel.INFO,
                         "The {0} governance action service detected that asset {1} has {2} linked connections for asset {3} and is not sure " +
                                 "which one to use since they have inconsistent networkAddress properties in their endpoint",
                         "Since the governance action service is unable to choose an appropriate endpoint, it " +
                                 "will use the qualified name of the asset as the path name to work with.",
                         "The governance action service will next produce the GOVERNANCE-ACTION-CONNECTORS-0006 message with the " +
                                 "qualified name embedded in it.  Follow the instructions for this message."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0012 - The context for {0} governance action service returned a RelatedMetadataElement with a
     * null related element: {1}
     */
    NO_RELATED_ASSET("GOVERNANCE-ACTION-CONNECTORS-0012",
                     AuditLogRecordSeverityLevel.ERROR,
                     "The context for {0} governance action service returned a RelatedMetadataElement with a null related element: {1}",
                     "The governance action service stops attempting extract the path name from the connection and " +
                                 "will use the qualified name of the asset as the path name to work with.",
                     "The governance action service will write the GOVERNANCE-ACTION-CONNECTORS-0006 message with the " +
                                 "qualified name embedded in it.  Follow the instructions for this message.  Also investigate why the related " +
                                 "element returned a null related element.  This is a logic error in the context or one of its " +
                                 "underlying services."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0013 - The {0} governance action service detected that asset {1} has no endpoint linked to connection {2}
     */
    NO_LINKED_ENDPOINT("GOVERNANCE-ACTION-CONNECTORS-0013",
                       AuditLogRecordSeverityLevel.INFO,
                         "The {0} governance action service detected that asset {1} has no endpoint linked to connection {2}",
                         "Since the asset's connection has no endpoint, the governance action connector will use the qualified name of the " +
                                 "asset as the path name to work with.",
                         "The governance action service will next write out the GOVERNANCE-ACTION-CONNECTORS-0006 message with the " +
                                 "qualified name.  Follow the instructions for this message."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0014 - The {0} governance action service detected that asset {1} has a linked connection {2} with {3} linked
     * endpoints which is not valid: {4}
     */
    TOO_MANY_ENDPOINTS("GOVERNANCE-ACTION-CONNECTORS-0014",
                       AuditLogRecordSeverityLevel.ERROR,
                         "The {0} governance action service detected that asset {1} has a linked connection {2} with {3} linked " +
                                 "endpoints which is not valid: {4}",
                         "The governance action service ignores this connection.",
                         "The governance action service will search for additional connections."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0015 - The {0} governance action service detected that the endpoint {1} linked to connection {2}
     * for asset {3} has no networkAddressProperty
     */
    NO_NETWORK_ADDRESS("GOVERNANCE-ACTION-CONNECTORS-0015",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} governance action service detected that the endpoint {1} linked to connection {2} for asset {3} has no networkAddressProperty",
                       "Since the asset's connection has no networkAddress in its endpoint, the governance action connector will use the qualified name of the " +
                               "asset as the path name to work with.",
                       "The governance action service will log the GOVERNANCE-ACTION-CONNECTORS-0006 message with the " +
                               "qualified name.  Follow the instructions for this message."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0016 - The {0} governance action service received a {1} exception when it registered its completion status.  
     * The exception's message is: {2}
     */
    UNABLE_TO_SET_COMPLETION_STATUS("GOVERNANCE-ACTION-CONNECTORS-0016",
                                    AuditLogRecordSeverityLevel.INFO,
                       "The {0} governance action service received a {1} exception when it registered its completion status.  The exception's message is: {2}",
                       "The governance action throws a GovernanceServiceException in the hope that the hosting server is able to clean up.",
                       "Review the exception messages that are logged about the same time as one of them will point to the root cause of the error."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0017 - The {0} governance action service received a {1} exception when it registered a listener with the 
     * governance context.  The exception's message is: {2}
     */
    UNABLE_TO_REGISTER_LISTENER("GOVERNANCE-ACTION-CONNECTORS-0017",
                                AuditLogRecordSeverityLevel.INFO,
                                    "The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception's message is: {2}",
                                    "The governance action service throws a GovernanceServiceException.",
                                    "This is likely to be a configuration error.  Review the description of the exception's message to understand what is not set up correctly and " +
                                            "and follow its instructions."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0018 - The {0} governance action service has no targets to operate on
     */
    NO_TARGETS("GOVERNANCE-ACTION-CONNECTORS-0018",
               AuditLogRecordSeverityLevel.ERROR,
               "The {0} governance action service has no targets to operate on",
               "The governance action service returns an INVALID completion status.",
               "This is an error in the way that the governance action service has been called." +
                                        "Identify the way it was called which could be a direct invocation through the initiateGovernanceAction() method," +
                                        "or as part of a governance action process.  Then correct this approach so that an action target is set up."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0019 - The {0} governance action service is not configured with a valid set of zones
     */
    NO_ZONES("GOVERNANCE-ACTION-CONNECTORS-0019",
             AuditLogRecordSeverityLevel.ERROR,
                                "The {0} governance action service is not configured with a valid set of zones",
                                "The governance action service will not operate without this zone value because setting the zones to null " +
                                        "will make the asset visible through every interface.  It returns an INVALID completion status.",
                                "This is likely to be a configuration error.  The zones are passed with as a configuration property or as a " +
                                        "request parameter.  Ensure either method provides a valid list of zone names expressed as a comma separated list" +
                                        "(for example: zone1,zone2)."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0020 - The {0} governance action service is publishing asset {1} to the following zones: {2}
     */
    SETTING_ZONES("GOVERNANCE-ACTION-CONNECTORS-0020",
                  AuditLogRecordSeverityLevel.INFO,
                                "The {0} governance action service is publishing asset {1} to the following zones: {2}",
                                "This governance action service completes normally.",
                                "Validate that these are the intended zones."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0021 - The {0} governance action service is initiating governance action process {1} with request parameters 
     * {2} for action targets {3}
     */
    INITIATE_PROCESS("GOVERNANCE-ACTION-CONNECTORS-0021",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} governance action service is initiating governance action process {1} with request parameters {2} for action targets {3}",
                     "The request is sent to the partner metadata server and executed.  This results in governance services running on one or more engine host servers.",
                     "Validate that the call to the process has the expected parameters and executes successfully."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0022 - The {0} governance action service encountered an {1} exception initiating governance action process {2}
     * with request parameters {3} for action targets {4}.  The exception message included was {5}
     */
    INITIATE_PROCESS_EXCEPTION("GOVERNANCE-ACTION-CONNECTORS-0022",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                           "The {0} governance action service encountered an {1} exception initiating governance action process {2} with request parameters {3} for action targets {4}.  The exception message included was {5}",
                           "The exception is logged.  More messages may follow if follow on attempts are made to initiate the process.  These can help to determine how to recover from this error.",
                           "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                   "exception stack trace to determine why the connector is not able to initiate the process and resolve that issue.  " +
                                   "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                   "connector in the integration daemon once the original cause of the error has been corrected."),

    /**
     * GOVERNANCE-ACTION-CONNECTORS-0023 - The {0} governance action service is unable to retrieve the template {1} configured in property {2}.  
     * The asset {3} was created without a template
     */
    MISSING_TEMPLATE("GOVERNANCE-ACTION-CONNECTORS-0023",
                     AuditLogRecordSeverityLevel.ERROR,
                               "The {0} governance action service is unable to retrieve the template {1} configured in property {2}.  The asset {3} was created without a template",
                               "The asset is created with the supplied parameters.",
                               "Determine whether the template name is specified incorrectly, or if the name is correct, why it is not accessible to governance service.  Once the situation has been corrected, future assets will be created with the right template.  However this asset may need some remediation to add the values that would have been added by the template."),
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for GovernanceActionConnectorsAuditCode expects to be passed one of the enumeration rows defined in
     * GovernanceActionConnectorsAuditCode above.   For example:
     *     GovernanceActionConnectorsAuditCode   auditCode = GovernanceActionConnectorsAuditCode.SERVER_NOT_AVAILABLE
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    GovernanceActionConnectorsAuditCode(String                      messageId,
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
        return "GovernanceActionConnectorsAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
