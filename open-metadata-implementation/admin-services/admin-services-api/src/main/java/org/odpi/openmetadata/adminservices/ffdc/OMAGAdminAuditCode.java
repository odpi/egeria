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
              "Determine if this service is needed in the server.  Remove it from the configuration is it is not.  If it is needed, add the jar file for the service into the platform's lib (or extra) directory to ensure it is picked up.  If the jar file is in the correct place then examine its implementation to ensure it registers with the runtime."),

    /**
     * OMAG-ADMIN-0201 - The {0} Open Metadata Access Service (OMAS) is supporting the access to assets for all governance zones
     */
    ALL_ZONES("OMAG-ADMIN-0201",
              AuditLogRecordSeverityLevel.STARTUP,
              "The {0} Open Metadata Access Service (OMAS) is supporting the access to assets for all governance zones",
              "The access service has not been passed a list of governance zones in the SupportedZones property of the access services options.  " +
                      "This means it is providing access to all Assets irrespective of the zone(s) they are assigned to.",
              "No action is required if this access service should be giving access to all assets in the open metadata ecosystem.  " +
                      "If this scope is too broad then set up a list of zones in the SupportedZones property for this access service."),

    /**
     * OMAG-ADMIN-0202 - The {0} Open Metadata Access Service (OMAS) is supporting the following governance zones: {1}
     */
    SUPPORTED_ZONES("OMAG-ADMIN-0202",
                    AuditLogRecordSeverityLevel.STARTUP,
                    "The {0} Open Metadata Access Service (OMAS) is supporting the following governance zones: {1}",
                    "The access service was passed a list of governance zones in the SupportedZones property of the access services options.  " +
                            "This means it is only providing access to the Assets from these zone(s).",
                    "Verify that these zones are the right set for this service."),

    /**
     * OMAG-ADMIN-0203 - The {0} Open Metadata Access Service (OMAS) is using the following governance zones as a default value for new Assets: {1}
     */
    DEFAULT_ZONES("OMAG-ADMIN-0203",
                  AuditLogRecordSeverityLevel.STARTUP,
                  "The {0} Open Metadata Access Service (OMAS) is using the following governance zones as a default value for new Assets: {1}",
                  "The access service was passed a list of governance zones in the DefaultZones property of the access services options.",
                  "Verify that this is the intended value for this service."),

    /**
     * OMAG-ADMIN-0204 - The {0} Open Metadata Access Service (OMAS) is awarding {1} karma point(s) to each person who contributes to open metadata
     */
    KARMA_POINT_COLLECTION_INCREMENT("OMAG-ADMIN-0204",
                                     AuditLogRecordSeverityLevel.STARTUP,
                                     "The {0} Open Metadata Access Service (OMAS) is awarding {1} karma point(s) to each person who contributes to open metadata",
                                     "The access service was passed this value in the KarmaPointInterval property of the access service's options.",
                                     "Verify that this interval is correct for your organization."),

    /**
     * OMAG-ADMIN-0205 - The {0} Open Metadata Access Service (OMAS) is not collecting karma points in this server
     */
    NO_KARMA_POINT_COLLECTION("OMAG-ADMIN-0205",
                              AuditLogRecordSeverityLevel.STARTUP,
                              "The {0} Open Metadata Access Service (OMAS) is not collecting karma points in this server",
                              "The access service can be configured to collect karma points by setting the KarmaPointIncrement property of the access service's options.",
                              "Verify that karma points are not required for your organization.  They are intended to reward individuals " +
                                      "who contribute to the open metadata ecosystem."),

    /**
     * OMAG-ADMIN-0206 - The {0} Open Metadata Access Service (OMAS) is using the following threshold for reporting Karma Point Plateaus: {1}
     */
    PLATEAU_THRESHOLD("OMAG-ADMIN-0206",
                      AuditLogRecordSeverityLevel.STARTUP,
                      "The {0} Open Metadata Access Service (OMAS) is using the following threshold for reporting Karma Point Plateaus: {1}",
                      "The access service was passed this value in the KarmaPointThreshold property of the access service's options.",
                      "Verify that this threshold is correct for your organization."),

    /**
     * OMAG-ADMIN-0207 - The {0} Open Metadata Access Service (OMAS) is using the default threshold for reporting Karma Point Plateaus: {1}
     */
    DEFAULT_PLATEAU_THRESHOLD("OMAG-ADMIN-0207",
                              AuditLogRecordSeverityLevel.STARTUP,
                              "The {0} Open Metadata Access Service (OMAS) is using the default threshold for reporting Karma Point Plateaus: {1}",
                              "This default value can be overridden with the KarmaPointThreshold property of the access service's options.",
                              "Verify that this default threshold is correct for your organization."),

    /**
     * OMAG-ADMIN-0208 - The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property
     */
    BAD_CONFIG_PROPERTY("OMAG-ADMIN-0208",
                        AuditLogRecordSeverityLevel.ERROR,
                        "The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property",
                        "The access service has not been passed valid configuration in its option's map.",
                        "Correct the configuration property and restart the server."),

    /**
     * OMAG-ADMIN-0209 - The {0} Open Metadata Access Service (OMAS) is registering a listener with the enterprise OMRS Topic for server {1}
     */
    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAG-ADMIN-0209",
                                             AuditLogRecordSeverityLevel.STARTUP,
                                             "The {0} Open Metadata Access Service (OMAS) is registering a listener with the enterprise OMRS Topic for server {1}",
                                             "The OMAS is registering to receive events from the open metadata repositories registered with the cohort.",
                                             "This is part of the normal start up of an access service in a server."),

    /**
     * OMAG-ADMIN-0210 - The {0} Open Metadata Access Service (OMAS) is unable to register a listener with the enterprise OMRS Topic for server {1} because it is null
     */
    NO_ENTERPRISE_TOPIC("OMAG-ADMIN-0210",
                        AuditLogRecordSeverityLevel.ERROR,
                        "The {0} Open Metadata Access Service (OMAS) is unable to register a listener with the enterprise OMRS Topic for server {1} because it is null",
                        "The OMAS is registering to receive events from the open metadata repositories registered with the cohort but is unable to because the enterprise OMRS topic is null.",
                        "Review other error messages to determine why the connector to the enterprise topic is missing."),

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
                                " restart the server."),

    /**
     * OMAG-ADMIN-0212 - Method {0} called on behalf of the {1} service detected a {2} exception when creating an open
     * metadata topic connection because the connector provider is incorrect.  The error message was {3}
     */
    BAD_TOPIC_CONNECTOR_PROVIDER("OMAG-ADMIN-0212",
                                 AuditLogRecordSeverityLevel.EXCEPTION,
                                 "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open " +
                                         "metadata topic connection because the connector provider is incorrect.  The error message was {3}",
                                 "This is an internal error.  The access service is not using a valid connector provider.",
                                 "Raise an issue on Egeria's GitHub and work with the Egeria community to resolve."),

    /**
     * OMAG-ADMIN-0213 - The {0} Open Metadata Access Service (OMAS) is using the following governance zones to determine when to publish completed Assets: {1}
     */
    PUBLISH_ZONES("OMAG-ADMIN-0213",
                  AuditLogRecordSeverityLevel.STARTUP,
                  "The {0} Open Metadata Access Service (OMAS) is using the following governance zones to determine when to publish completed Assets: {1}",
                  "The access service was passed a list of governance zones in the PublishZones property of the access services options " +
                          "and will use it to set the zones for an asset that is published from this access service.",
                  "Verify that this is the intended value for this service (null means that the published " +
                          "asset will be visible in all zones."),

    /**
     * OMAG-ADMIN-0214 - The {0} Open Metadata View Service (OMVS) is using the following resource endpoints as permitted endpoints: {1}
     */
    RESOURCE_ENDPOINTS("OMAG-ADMIN-0214",
                  AuditLogRecordSeverityLevel.STARTUP,
                  "The {0} Open Metadata View Service (OMVS) is using the following resource endpoints as permitted endpoints: {1}",
                  "The view service was passed a list of resource endpoints in the resourceEndpoints property of the view services options " +
                          "and will use it to set the endpoints that the view service may query.",
                  "Verify that this is the intended value for this service (null means that the view service will not be able to perform queries."),


    /**
     * OMAG-ADMIN-0215 - The {0} Open Metadata View Service (OMVS) for server {1} requires a max page size of at least {2}, but was configured with {3}
     */
    VIEW_SERVICE_MAX_PAGE_SIZE_TOO_LOW("OMAG-ADMIN-0215",
                                       AuditLogRecordSeverityLevel.STARTUP,
                                       "The {0} Open Metadata View Service (OMVS) for server {1} requires a max page size of at least {2}, but was configured with {3}",
                                       "The view service fails to start as it does not have a sufficiently large maxPageSize .",
                                       "Reconfigure the View service to have a maxPageSize that is sufficient."),

    /**
     * OMAG-ADMIN-0216 - The {0} Open Metadata View Service (OMAS) is supporting the access to all types of assets
     */
    ALL_SEARCH_TYPES("OMAG-ADMIN-0216",
              AuditLogRecordSeverityLevel.STARTUP,
              "The {0} Open Metadata View Service (OMAS) is supporting the access to all types of assets",
              "The view service has not been passed a list of asset types in the SupportedTypesForSearch property of the view services options.  " +
                      "This means it is providing access to all Assets irrespective of their type.",
              "No action is required if this view service should be giving access to all types of assets in the open metadata ecosystem.  " +
                      "If this scope is too broad then set up a list of asset types in the SupportedTypesForSearch property for this view service."),

    /**
     * OMAG-ADMIN-0217 - The {0} Open Metadata Access Service (OMAS) is supporting the following governance zones: {1}
     */
    SUPPORTED_SEARCH_TYPES("OMAG-ADMIN-0217",
                    AuditLogRecordSeverityLevel.STARTUP,
                    "The {0} Open Metadata View Service (OMAS) is supporting the following asset types when searching: {1}",
                    "The view service was passed a list of asset types in the SupportedTypesForSearch property of the view services options.  " +
                            "This means it is only providing access to these types of Assets.",
                    "Verify that these types are the right set for this service deployment."),
    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


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
     */
    OMAGAdminAuditCode(String                     messageId,
                       AuditLogRecordSeverityLevel severity,
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
