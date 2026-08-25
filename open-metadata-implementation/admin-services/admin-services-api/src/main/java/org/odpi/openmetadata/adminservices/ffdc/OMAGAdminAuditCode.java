/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OMAGAdminAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OMAGAdminAuditCode implements AuditLogMessageSet
{
    /**
     * OMAG-ADMIN-0001 - The {0} service is being ignored in the startup of server {1} because it is not registered to this platform
     */
    IGNORING_UNREGISTERED_SERVICE("OMAG-ADMIN-0001",
              AuditLogRecordSeverityLevel.STARTUP,
              "The {0} service is being ignored in the startup of server {1} because it is not registered to this platform",
              "The configured service will not be available in the running server because the code to run the service is missing from the platform's classpath.",
              "Determine if this service is needed in the server.  Remove it from the configuration is it is not.  If it is needed, add the jar file for the service into the platform's lib (or extra) directory to ensure it is picked up.  If the jar file is in the correct place then examine its implementation to ensure it registers with the runtime.",
              "https://egeria-project.org/guides/admin/"),

    /**
     * OMAG-ADMIN-0208 - The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property
     */
    BAD_CONFIG_PROPERTY("OMAG-ADMIN-0208",
                        AuditLogRecordSeverityLevel.ERROR,
                        "The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property",
                        "The access service has not been passed valid configuration in its option's map.",
                        "Correct the configuration property and restart the server.",
                        "https://egeria-project.org/guides/admin/"),

    /**
     * OMAG-ADMIN-0209 - The {0} Open Metadata Access Service (OMAS) is registering a listener with the enterprise OMRS Topic for server {1}
     */
    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAG-ADMIN-0209",
                                             AuditLogRecordSeverityLevel.STARTUP,
                                             "The {0} Open Metadata Access Service (OMAS) is registering a listener with the enterprise OMRS Topic for server {1}",
                                             "The OMAS is registering to receive events from the open metadata repositories registered with the cohort.",
                                             "This is part of the normal start up of an access service in a server.",
                                             "https://egeria-project.org/guides/admin/"),

    /**
     * OMAG-ADMIN-0210 - The {0} Open Metadata Access Service (OMAS) cannot register a listener with the enterprise OMRS Topic for server {1} because it is null
     */
    NO_ENTERPRISE_TOPIC("OMAG-ADMIN-0210",
                        AuditLogRecordSeverityLevel.ERROR,
                        "The {0} Open Metadata Access Service (OMAS) cannot register a listener with the enterprise OMRS Topic for server {1} because it is null",
                        "The OMAS is registering to receive events from the open metadata repositories registered with the cohort but cannot because the enterprise OMRS topic is null.",
                        "Review other error messages to determine why the connector to the enterprise topic is missing.",
                        "https://egeria-project.org/guides/admin/"),

    /**
     * OMAG-ADMIN-0211 - Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.
     * The error message was {3}
     */
    BAD_TOPIC_CONNECTOR("OMAG-ADMIN-0211",
                        AuditLogRecordSeverityLevel.EXCEPTION,
                        "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  " +
                                "The error message was {3}",
                        "The access service has not been passed valid configuration. The server where it is configured failed to start.",
                        "Use the information in the error message to determine the cause of the problem, then correct the failing configuration and" +
                                " restart the server.",
                                "https://egeria-project.org/guides/admin/"),

    /**
     * OMAG-ADMIN-0212 - Method {0} called on behalf of the {1} service detected a {2} exception when creating an open
     * metadata topic connection because the connector provider is incorrect.  The error message was {3}
     */
    BAD_TOPIC_CONNECTOR_PROVIDER("OMAG-ADMIN-0212",
                                 AuditLogRecordSeverityLevel.EXCEPTION,
                                 "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open " +
                                         "metadata topic connection because the connector provider is incorrect.  The error message was {3}",
                                 "This is an internal error.  The access service is not using a valid connector provider.",
                                 "Raise an issue on Egeria's GitHub and work with the Egeria community to resolve.",
                                 "https://egeria-project.org/guides/admin/"),


    /**
     * OMAG-ADMIN-0216 - The {0} Open Metadata View Service (OMAS) is supporting the access to all types of assets
     */
    ALL_SEARCH_TYPES("OMAG-ADMIN-0216",
              AuditLogRecordSeverityLevel.STARTUP,
              "The {0} Open Metadata View Service (OMAS) is supporting the access to all types of assets",
              "The view service has not been passed a list of asset types in the SupportedTypesForSearch property of the view services options.  " +
                      "This means it is providing access to all Assets irrespective of their type.",
              "No action is required if this view service should be giving access to all types of assets in the open metadata ecosystem.  " +
                      "If this scope is too broad then set up a list of asset types in the SupportedTypesForSearch property for this view service.",
                      "https://egeria-project.org/guides/admin/"),

    /**
     * OMAG-ADMIN-0217 - The {0} Open Metadata Access Service (OMAS) is supporting the following governance zones: {1}
     */
    SUPPORTED_SEARCH_TYPES("OMAG-ADMIN-0217",
                    AuditLogRecordSeverityLevel.STARTUP,
                    "The {0} Open Metadata View Service (OMAS) is supporting the following asset types when searching: {1}",
                    "The view service was passed a list of asset types in the SupportedTypesForSearch property of the view services options.  " +
                            "This means it is only providing access to these types of Assets.",
                    "Verify that these types are the right set for this service deployment.",
                    "https://egeria-project.org/guides/admin/"),
    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;
    private final String                     url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OMAGAdminAuditCode(String                     messageId,
                       AuditLogRecordSeverityLevel severity,
                       String                     message,
                       String                     systemAction,
                       String                     userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for OMAGAdminAuditCode expects to be passed one of the enumeration rows defined in
     * OMAGAdminAuditCode above.   For example:
     * <br><br>
     *     OMAGAdminAuditCode   auditCode = OMAGAdminAuditCode.SERVER_NOT_AVAILABLE;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    OMAGAdminAuditCode(String                     messageId,
                       AuditLogRecordSeverityLevel severity,
                       String                     message,
                       String                     systemAction,
                       String                     userAction,
                       String                     url)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                             userAction,
                                             url);
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
                                                                                    userAction,
                                                                                    url);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }
}
