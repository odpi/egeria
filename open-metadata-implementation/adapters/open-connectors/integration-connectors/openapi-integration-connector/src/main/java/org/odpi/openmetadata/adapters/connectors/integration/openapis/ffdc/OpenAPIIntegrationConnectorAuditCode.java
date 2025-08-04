/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OpenAPIIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OpenAPIIntegrationConnectorAuditCode implements AuditLogMessageSet
{
    CONNECTOR_CONFIGURATION_WITH_ENDPOINT("OPEN-API-INTEGRATION-CONNECTOR-0001",
                                          AuditLogRecordSeverityLevel.INFO,
                                          "The {0} integration connector has been initialized to monitor URL {1} with templateQualifiedName={2}",
                                          "The connector is designed to monitor changes to the Open API Specification located at the URL.  " +
                                                  "If the templateQualifiedName is set, it identifies a template entity to use when cataloging APIs.",
                                          "No specific action is required.  This message is to confirm the configuration for the integration connector."),

    CONNECTOR_CONFIGURATION_NO_ENDPOINT("OPEN-API-INTEGRATION-CONNECTOR-0002",
                                          AuditLogRecordSeverityLevel.INFO,
                                          "The {0} integration connector has been initialized to monitor all Http(s) Endpoints with templateQualifiedName={1}",
                                          "The connector is designed to monitor changes to the Open API Specification located at specific URLs.  " +
                                                  "These URLs will be retrieved by querying Endpoints with a protocol of 'Http(s)' from the open metadata repositories." +
                                                  "If the templateQualifiedName is set, it identifies a template entity to use when cataloging APIs",
                                          "No specific action is required.  This message is to confirm that the missing endpoint in the configuration is correct for the integration connector."),


    CONNECTOR_REFRESH_WITH_ENDPOINT("OPEN-API-INTEGRATION-CONNECTOR-0003",
                                          AuditLogRecordSeverityLevel.INFO,
                                          "The {0} integration connector has been refreshed to monitor URL {1}",
                                          "The connector will retrieve the API Specification for the URL",
                                          "Look to see if the spec is retrieved."),

    CONNECTOR_REFRESH_ALL_ENDPOINTS("OPEN-API-INTEGRATION-CONNECTOR-0004",
                                        AuditLogRecordSeverityLevel.INFO,
                                        "The {0} integration connector has been refreshed to monitor all Http(s) Endpoints.  Currently {1} are known.",
                                        "The connector will attempt to retrieve the specifications for each of the endpoints.",
                                        "Look to see if the right specs are retrieved."),

    NEW_ENDPOINT("OPEN-API-INTEGRATION-CONNECTOR-0005",
                                    AuditLogRecordSeverityLevel.INFO,
                                    "The {0} integration connector has retrieved a new endpoint {1} at URL {2}.",
                                    "The connector will attempt to retrieve the specification for this endpoint if supported.",
                                    "Look to see if an Open API Specification is retrieved where it is expected."),


    RETRIEVED_OPEN_API_SPEC("OPEN-API-INTEGRATION-CONNECTOR-0006",
                          AuditLogRecordSeverityLevel.INFO,
                          "The {0} integration connector retrieved the Open API Specification from URL {1}.  The API retrieved was {2}",
                          "The exception is passed back to the integration daemon that is hosting " +
                                  "this connector to enable it to perform error handling.  More messages are likely to follow describing the " +
                                  "error handling that was performed.  These can help to determine how to recover from this error",
                          "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                  "exception stack trace to determine why the connector is not able to access the directory and resolve that issue.  " +
                                  "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                  "connector in the integration daemon once the original cause of the error has been corrected."),

    CATALOGUED_OPEN_API_SPEC("OPEN-API-INTEGRATION-CONNECTOR-0007",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector retrieved the Open API Specification from URL {1} ({2} ({3})) and catalogued {4} APIs with a total of {5} operations.",
                            "The connector extracted the content of the Open API, retrieved/created the endpoint for it and created a DeployedAPI asset for each 'tag' linked to an APIOperation for each 'path/operation' pair",
                            "Validate that the connector is extracting all the required information for your use case."),

    UNABLE_TO_RETRIEVE_OPEN_API_SPEC("OPEN-API-INTEGRATION-CONNECTOR-0008",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "An unexpected {0} exception was returned to the {1} integration connector {2} " +
                                    "method when trying to retrieve the Open API Spec for URL {3}.  The error message was {4}",
                                     "The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.",
                                     "Use the message in the nested exception to determine the root cause of the error. Once this is " +
                                             "resolved, follow the instructions in the messages produced by the integration daemon to restart this connector."),

    CONNECTOR_STOPPING("OPEN-API-INTEGRATION-CONNECTOR-0009",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector has stopped its monitoring and is shutting down",
                                  "The connector is disconnecting.",
                                  "No action is required unless there are errors that follow indicating that there were problems shutting down."),


    MISSING_TEMPLATE("OPEN-API-INTEGRATION-CONNECTOR-0015",
                     AuditLogRecordSeverityLevel.ERROR,
                     "The {0} integration connector is unable to retrieve the DataFile template with qualified name: {1}",
                     "The metadata element for the template is not found in the open metadata repositories.  " +
                             "The template name was configured for the connector.  This means that files should be catalogued " +
                             "using the template.  Since the template is missing, files are not being catalogued.",
                     "Create the template in the metadata repository.  The connector will catalog the files during " +
                             "its next periodic refresh or you can force it to refresh immediately by calling the refresh" +
                             "operation on the integration daemon."),

    BAD_ELEMENT("OPEN-API-INTEGRATION-CONNECTOR-0016",
                     AuditLogRecordSeverityLevel.ERROR,
                     "The {0} integration connector retrieved an invalid {1} element in method {2}.  Element content is: {3}",
                     "The metadata element is ignored.",
                     "Investigate why this element is incomplete."),

    UNEXPECTED_EXCEPTION( "OPEN-API-INTEGRATION-CONNECTOR-0029",
                          AuditLogRecordSeverityLevel.EXCEPTION,
                          "The {0} integration connector received an unexpected exception {1} in method {2}; the error message was: {3}",
                         "The connector is unable to catalog one or more APIs.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    CLIENT_SIDE_REST_API_ERROR( "OPEN-API-INTEGRATION-CONNECTOR-0030",
                                AuditLogRecordSeverityLevel.EXCEPTION,
                                "A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}",
                                "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                                "Look for errors in the local server's console to understand and correct the source of the error.")

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for OpenAPIIntegrationConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * OpenAPIIntegrationConnectorAuditCode above.   For example:
     *     OpenAPIIntegrationConnectorAuditCode   auditCode = OpenAPIIntegrationConnectorAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OpenAPIIntegrationConnectorAuditCode(String                      messageId,
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
        return "OpenAPIIntegrationConnectorAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
