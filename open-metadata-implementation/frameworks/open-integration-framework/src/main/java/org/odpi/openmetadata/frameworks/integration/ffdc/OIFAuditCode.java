/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OIFAuditCode is used to define the message content for the Audit Log.
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
public enum OIFAuditCode implements AuditLogMessageSet
{
    /**
     * OIF-CONNECTOR-0001 - The integration connector context manager is being initialized for calls to server {0} on platform {1}
     */
    CONTEXT_INITIALIZING("OIF-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The integration connector context manager is being initialized for calls to server {0} on platform {1}",
                         "The integration daemon is initializing its context manager.",
                         "Verify that the start up sequence goes on to initialize the context for each connector configured for this service.",
                         "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0005 - A {0} exception with message {1} occurred when parsing open lineage event: {2}
     */
    OPEN_LINEAGE_FORMAT_ERROR("OIF-CONNECTOR-0005",
                              AuditLogRecordSeverityLevel.ERROR,
                              "A {0} exception with message {1} occurred when parsing open lineage event: {2}",
                              "The integration daemon cannot parse an incoming open lineage event into Egeria's OpenLineageRunEvent bean.  " +
                                      "This may be due to either (1) an invalid open lineage event, or (2) Egeria's OpenLineageRunEvent not supporting an advancement in the open lineage standard.  " +
                                      "The raw event is passed to the listening connectors with a null OpenLineageRunEvent bean.  The connector can use the open lineage standard server to process the event facet by facet.",
                              "Verify the format of the open lineage event.  If incorrect, seek the source of the event.  If correct, look to enhance Egeria's OpenLineageRunEvent.",
                              "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0006 - A {0} exception with message {1} occurred when a listening integration connector tried to push an OpenLineage event
     */
    OPEN_LINEAGE_PUBLISH_ERROR("OIF-CONNECTOR-0006",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                               "A {0} exception with message {1} occurred when a listening integration connector tried to push an OpenLineage event",
                               "The integration daemon has caught the exception and will continue to pass the event to the remaining listening integration connectors.",
                               "Look at the resulting stack trace to understand what went wrong in the called integration connector.",
                               "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0007 - No action targets are defined for the {0} integration connector
     */
    NO_CATALOG_TARGETS("OIF-CONNECTOR-0007",
                                 AuditLogRecordSeverityLevel.INFO,
                                 "No catalog targets are defined for the {0} integration connector",
                                 "The integration connector waits for the next refresh.",
                                 "Add one or more action targets to the integration connector to provide it with work to do.",
                                 "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0008 - The {0} integration connector is refreshing action target {1}
     */
    REFRESHING_CATALOG_TARGET("OIF-CONNECTOR-0008",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is refreshing action target {1}",
                       "The integration connector performs the requested metadata synchronization.",
                       "Check for reported errors.   Otherwise, the connector is working as configured.",
                       "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0009 - The {0} integration connector has refreshed {1} action target(s)
     */
    REFRESHED_CATALOG_TARGETS("OIF-CONNECTOR-0009",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector has refreshed {1} action target(s)",
                              "The integration connector has completed refresh processing of the catalog targets.",
                              "Check that the correct action targets have been processes, and adjust them if necessary before the next refresh.",
                              "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0010 - The {0} connector has detected an unsynchronized {1} element ({2}) in metadata collection {3} ({4}) but the permitted synchronization to catalog target {5} is {6}
     */
    IGNORED_EGERIA_ELEMENT("OIF-CONNECTOR-0010",
                           AuditLogRecordSeverityLevel.ACTION,
                           "The {0} connector has detected an unsynchronized {1} element ({2}) in metadata collection {3} ({4}) but the permitted synchronization to catalog target {5} is {6}",
                           "The element is ignored.",
                           "Determine why this element is in the metadata collection and determine if it should be synchronized with the catalog target.  If it should, then set up the permitted synchronization direction to allow it.",
                           "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0011 - The {0} connector is unsure which action to take for an element. This is a logic error.  The member element information is {1}
     */
    UNKNOWN_ACTION ("OIF-CONNECTOR-0011",
                    AuditLogRecordSeverityLevel.ACTION,
                    "The {0} connector is unsure which action to take for an element. This is a logic error.  The member element information is {1}",
                    "The connector stops processing.",
                    "Using information from the element, the set up of the connector, and the connector's logic to determine why this 'should not occur' case has happened.",
                    "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0012 - The {0} integration connector received an unexpected exception {1} while trying to disconnect connector {2}; the error message was: {3}
     */
    DISCONNECT_EXCEPTION("OIF-CONNECTOR-0012",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} while trying to disconnect connector {2}; the error message was: {3}",
                         "The connector is cannot disconnect a connector to a catalog target.  Although it continues to run, it may have leaked a resource in the remote target.",
                         "Use the details from the error message to determine the cause of the error.  Check the remote target for errors and correct as needed.",
                         "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0013 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("OIF-CONNECTOR-0013",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector records the error anf tries to continue; subsequent errors may occur as a result of this initial failure",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0014 - The {0} integration connector has stopped its monitoring and is shutting down
     */
    CONNECTOR_STOPPING("OIF-CONNECTOR-0014",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its monitoring and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down.",
                       "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0015 - An unexpected {0} exception was returned to the {1} integration connector while retrieving the action targets.  The error message was {2}
     */
    GET_CATALOG_TARGET_EXCEPTION("OIF-CONNECTOR-0015",
                                 AuditLogRecordSeverityLevel.ERROR,
                                 "An unexpected {0} exception was returned to the {1} integration connector while retrieving the catalog targets.  The error message was {2}",
                                 "The exception is logged and the integration connector waits for the next refresh.",
                                 "Use the message in the unexpected exception to determine the root cause of the error. Once this is " +
                                         "resolved, follow the instructions to prepare the integration connector for the next refresh.",
                                         "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0016 - The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}
     */
    UNABLE_TO_REGISTER_LISTENER("OIF-CONNECTOR-0016",
                                AuditLogRecordSeverityLevel.EXCEPTION,
                                "The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}",
                                "The connector continues to scan and synchronize metadata as configured.  Without the listener, updates to open metadata elements with only be synchronized to the third party during a refresh scan.",
                                "The likely cause of this error is that the OMF in the metadata access server used by the integration daemon is not configured to support topics.  This can be changed by reconfiguring the metadata access server to support topics.  A less likely cause is that the metadata access server has stopped running",
                                "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0018 - The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}
     */
    BAD_OM_VALUE("OIF-CONNECTOR-0017",
                 AuditLogRecordSeverityLevel.ERROR,
                 "The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}",
                 "The connector throws an exception to indicate that it should not continue.",
                 "Check the templates that where used to create the element to be sure they include the correct value.  If the template is correct then check the element's history to discover which processing cleared the value, since this is where the problem was introduced.  Now decide if the test in the connector is incorrect, or the .",
                 "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0018 - The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}
     */
    BAD_OM_PROPERTY_TYPE( "OIF-CONNECTOR-0018",
                          AuditLogRecordSeverityLevel.ERROR,
                          "The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}",
                         "The connector throws an exception to indicate that it should not continue since something has gone very wrong with the connector or Open Metadata Framework since the connector is expecting an element of a certain type, with bean properties that match that type, but the OMF has returned something different.",
                         "Check that the type of element is that which was expected.  If not, this may be a connector logic problem, or another process may have created additional metadata that has confused the connector. Compare the element retrieved through the OMF with its stored value.  If the stored value is correct, then the problem is in the OMF converters.  If the stored value is not correct then look at the element's history to understand which processing caused the problem.",
                         "https://egeria-project.org/frameworks/oif/overview/"),

    /**
     * OIF-CONNECTOR-0019 - The {0} connector is recommending the {1} action to take for element {2}
     */
    MEMBER_ACTION ("OIF-CONNECTOR-0019",
                    AuditLogRecordSeverityLevel.ACTION,
                    "The {0} connector is recommending the {1} action to take for element {2}",
                    "The connector logs the action it has selected for the element and carries on processing.",
                    "No action is required.  This message traces the decision that the connector made about each element it processed.",
                    "https://egeria-project.org/frameworks/oif/overview/"),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OIFAuditCode(String                      messageId,
                 AuditLogRecordSeverityLevel severity,
                 String                      message,
                 String                      systemAction,
                 String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for OIFAuditCode expects to be passed one of the enumeration rows defined above.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    OIFAuditCode(String                      messageId,
                 AuditLogRecordSeverityLevel severity,
                 String                      message,
                 String                      systemAction,
                 String                      userAction,
                 String                      url)
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
                ", url='" + url + '\'' +
                '}';
    }
}
