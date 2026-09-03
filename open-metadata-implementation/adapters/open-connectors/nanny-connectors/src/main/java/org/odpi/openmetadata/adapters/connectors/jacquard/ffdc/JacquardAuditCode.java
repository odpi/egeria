/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The JacquardAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data for the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum JacquardAuditCode implements AuditLogMessageSet
{
    /**
     * JACQUARD-HARVESTER-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("JACQUARD-HARVESTER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot catalog one or more metadata elements in the metadata repository.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0002 - Integration connector {0} cannot determine if tabular data source {1} has changed since it has no last update time column
     */
    NO_LAST_UPDATE_DATE( "JACQUARD-HARVESTER-0002",
                         AuditLogRecordSeverityLevel.ERROR,
                         "Integration connector {0} cannot determine if tabular data source {1} has changed since it has no last update time column",
                         "The integration connector skips this data source.",
                         "Update the data source to ensure it has a column called 'updateTime'.",
                         "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0003 - Integration connector {0} cannot determine if tabular data source {1} has changed since it has no createTime column
     */
    NO_CREATION_DATE( "JACQUARD-HARVESTER-0003",
                         AuditLogRecordSeverityLevel.ERROR,
                         "Integration connector {0} cannot determine if tabular data source {1} has changed since it has no createTime column",
                         "The integration connector skips this data source because of the missing create time column.",
                         "Update the data source to ensure it has a column called 'createTime'.",
                         "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0006 - The {0} integration connector has initiated the Badot Subscription Manager running as engine action {1} with {2} action targets
     */
    BARDOT_STARTED("JACQUARD-HARVESTER-0006",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has initiated the Badot Subscription Manager running as engine action {1} with {2} action targets",
                       "The connector has started the Badot Subscription Manager.",
                       "No action is required unless there are errors that follow indicating that there were problems with the subscription manager.",
                       "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0009 - The {0} integration connector has stopped its monitoring of open metadata from server {1} on platform {2} and is shutting down
     */
    CONNECTOR_STOPPING("JACQUARD-HARVESTER-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its monitoring of open metadata from server {1} on platform {2} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down.",
                       "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0010 - The {0} integration connector has created a new {1} supporting definition with GUID {2}
     */
    CREATED_SUPPORTING_DEFINITION("JACQUARD-HARVESTER-0010",
                                  AuditLogRecordSeverityLevel.TRACE,
                                  "The {0} integration connector has created a new {1} supporting definition called {2} with GUID {3}",
                                  "The connector is creating the metadata elements that supports the definition of the Open Metadata Digital Product Catalog.",
                                  "No action is required.  This message is used to show the progress of the setup.",
                                  "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0011 - The {0} integration connector is starting its harvesting of open metadata from server {1} on platform {2} into digital products
     */
    STARTING_CONNECTOR("JACQUARD-HARVESTER-0011",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting its harvesting of open metadata from server {1} on platform {2} into digital products",
                       "The connector is initializing the definitions for the Open Metadata Digital Product Catalog.",
                       "Monitor the set up of the catalog and the switch over to monitoring.",
                       "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0012 - The {0} integration connector has created a new digital product {1} called {2}
     */
    NEW_OPEN_METADATA_PRODUCT("JACQUARD-HARVESTER-0012",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector has created a new digital product {1} called {2}",
                              "The connector is setting up the fixed open metadata digital products.",
                              "No action is required.  This message is for monitoring the set up of the fixed digital products.",
                              "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0025 - The {0} integration connector has updated the {1} supporting definition called {2} with GUID {3}
     */
    UPDATED_SUPPORTING_DEFINITION("JACQUARD-HARVESTER-0025",
                                  AuditLogRecordSeverityLevel.TRACE,
                                  "The {0} integration connector has updated the {1} supporting definition called {2} with GUID {3}",
                                  "The connector found a definition in the Open Metadata Digital Product Catalog that no longer described the deployment it is running in, and corrected it.",
                                  "No action is required.  This message records that the catalog has been brought back into line with the metadata access server that supplies it.",
                                  "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0026 - The {0} integration connector is unlinking {1} element {2} from {3} element {4} to remove relationship {5}
     */
    UNLINKING_ELEMENTS("JACQUARD-HARVESTER-0026",
                       AuditLogRecordSeverityLevel.TRACE,
                       "The {0} integration connector is unlinking {1} element {2} from {3} element {4} to remove relationship {5}",
                       "The connector is removing a link that no longer describes the deployment it is running in, so that the corrected link can take its place.",
                       "No action is required.  This message is for monitoring the maintenance of the Open Metadata Digital Product Catalog.",
                       "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0014 - The {0} integration connector is linking {1} element {2} to {3} element {4} using relationship {5}
     */
    LINKING_ELEMENTS("JACQUARD-HARVESTER-0014",
                     AuditLogRecordSeverityLevel.TRACE,
                     "The {0} integration connector is linking {1} element {2} to {3} element {4} using relationship {5}",
                     "The connector is linking product catalog elements together.",
                     "No action is required.  This message is for monitoring the set up of the Open Metadata Digital Product Catalog.",
                     "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0015 - The {0} integration connector has retrieved a new {1} supporting definition with GUID {2}
     */
    RETRIEVING_SUPPORTING_DEFINITION("JACQUARD-HARVESTER-0015",
                                     AuditLogRecordSeverityLevel.TRACE,
                                     "The {0} integration connector has retrieved a new {1} supporting definition called {2} with GUID {3}",
                                     "The connector is retrieving the metadata elements that supports the definition of the Open Metadata Digital Product Catalog.",
                                     "No action is required.  This message is used to show progress during the setup.",
                                     "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0016 - The {0} integration connector has retrieved an existing digital product {1} called {2}
     */
    RETRIEVING_OPEN_METADATA_PRODUCT("JACQUARD-HARVESTER-0016",
                                     AuditLogRecordSeverityLevel.INFO,
                                     "The {0} integration connector has retrieved an existing digital product {1} called {2}",
                                     "The connector is retrieving the fixed open metadata digital products.",
                                     "No action is required.  This message is for monitoring the retrieval of the fixed digital products.",
                                     "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0018 - The {0} integration connector has updated an existing digital product {1} called {2}
     */
    UPDATED_OPEN_METADATA_PRODUCT("JACQUARD-HARVESTER-0018",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector has updated an existing digital product {1} called {2}",
                                  "The connector is maintaining the fixed open metadata digital products.",
                                  "No action is required.  This message is for monitoring the updates to the fixed digital products.",
                                  "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0019 - The {0} integration connector is refreshing the {1} data set for digital product {2}
     */
    REFRESH_CATALOG_TARGET("JACQUARD-HARVESTER-0019",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector is refreshing the {1} data set for digital product {2}",
                                  "The connector is reviewing whether a particular digital product has changed since it was last refreshed. Details of its review are attached to the data asset for the product using the DataScope classification.",
                                  "No action is required.  This message is for monitoring the refresh progress of the digital products.",
                                  "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0020 - The {0} integration connector is maintaining the DataScope classification for the {1} data set for digital product {2}
     */
    MAINTAINED_DATA_SCOPE("JACQUARD-HARVESTER-0020",
                           AuditLogRecordSeverityLevel.INFO,
                           "The {0} integration connector is refreshing the {1} is maintaining the DataScope classification for the {1} data set for digital product {2}",
                           "The connector is maintaining the change record for a particular digital product. Details of its review are attached to the data asset for the product using the DataScope classification.",
                           "No action is required.  This message is for monitoring the refresh activity of the digital products.",
                           "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0021 - The {0} integration connector is harvesting valid values
     */
    HARVESTING_VALID_VALUES("JACQUARD-HARVESTER-0021",
                          AuditLogRecordSeverityLevel.INFO,
                          "The {0} integration connector is harvesting valid metadata values - this may take some time ...",
                          "The connector is reviewing the valid metadata sets in the open metadata ecosystem to discover if any new ones have been created.  If there are, it creates a new digital product for this set.",
                          "No action is required beyond patience as this process can take 10+ minutes depending on how many valid metadata sets you have.  This message is to indicate that Jacquard has embarked on a potentially large piece of work.",
                          "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0022 - The {0} integration connector is harvesting reference data sets
     */
    HARVESTING_REFERENCE_DATA_SETS("JACQUARD-HARVESTER-0022",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector is harvesting reference data sets - this may take some time ...",
                            "The connector is reviewing the reference data sets in the open metadata ecosystem to discover if any new ones have been created.  If there are, it creates a new digital product for this set.",
                            "No action is required beyond patience as this process can take 10+ minutes depending on how many reference data sets you have.  This message is to indicate that Jacquard has embarked on a potentially large piece of work.",
                            "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0023 - The {0} integration connector is creating connectors to existing products
     */
    HARVESTING_CATALOG_TARGETS("JACQUARD-HARVESTER-0023",
                                   AuditLogRecordSeverityLevel.INFO,
                                   "The {0} integration connector is creating connectors to access the data in existing products - this may take some time ...",
                                   "The connector is creating connectors to each of the existing digital products in the open metadata ecosystem so they can be processed.",
                                   "No action is required beyond patience as this process can take 10+ minutes depending on how many digital products you have.  This message is to indicate that Jacquard has embarked on a potentially large piece of work.",
                                   "https://egeria-project.org/patterns/harvest-and-publish/overview/"),

    /**
     * JACQUARD-HARVESTER-0024 - The {0} integration connector has linked its solution component {1} ({2}) to the equivalent solution component {3} as a validated duplicate
     */
    LINKING_DUPLICATE_SOLUTION_COMPONENTS("JACQUARD-HARVESTER-0024",
                                          AuditLogRecordSeverityLevel.INFO,
                                          "The {0} integration connector has linked its solution component {1} ({2}) to the equivalent solution component {3} as a validated duplicate",
                                          "The connector has found another solution component with the same display name as one of its own solution components.  The two components are linked with a validated PeerDuplicateLink relationship and both are classified as KnownDuplicate so that the retrieval processing combines them.",
                                          "No action is required.  This message is for monitoring the alignment of the Open Metadata Digital Product Catalog's solution blueprint with the solution components supplied by the content packs.",
                                          "https://egeria-project.org/features/duplicate-management/overview/"),

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
    JacquardAuditCode(String                      messageId,
                      AuditLogRecordSeverityLevel severity,
                      String                      message,
                      String                      systemAction,
                      String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for DistributeKafkaAuditCode expects to be passed one of the enumeration rows defined in
     * DistributeKafkaAuditCode above.   For example:
     * <br>
     *     DistributeKafkaAuditCode   auditCode = DistributeKafkaAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    JacquardAuditCode(String                      messageId,
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
        return "JacquardAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
