/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The LiskovAuditCode is used to define the message content for the Audit Log.
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
public enum LiskovAuditCode implements AuditLogMessageSet
{
    /**
     * LISKOV-DATA-HUB-MANAGER-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("LISKOV-DATA-HUB-MANAGER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot catalog one or more metadata elements in the metadata repository.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0009 - The {0} integration connector has stopped its monitoring of data sharing hubs from server {1} on platform {2} and is shutting down
     */
    CONNECTOR_STOPPING("LISKOV-DATA-HUB-MANAGER-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its monitoring of data sharing hubs from server {1} on platform {2} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down.",
                       "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0011 - The {0} integration connector is starting its monitoring of data sharing hubs from server {1} on platform {2}
     */
    STARTING_CONNECTOR("LISKOV-DATA-HUB-MANAGER-0011",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting its monitoring of data sharing hubs from server {1} on platform {2}",
                       "The connector is initializing its monitoring of the data sharing hubs connected as Catalog Targets.",
                       "Monitor the data dictionaries for these data sharing hubs are being maintained successfully.",
                       "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0012 - The {0} integration connector has created a new catalog target for data sharing hub {1} ({2})
     */
    NEW_DATA_HUB("LISKOV-DATA-HUB-MANAGER-0012",
                 AuditLogRecordSeverityLevel.INFO,
                 "The {0} integration connector has created a new catalog target for data sharing hub {1} ({2})",
                 "The connector is initiating its management of a new data sharing hub .",
                 "No action is required.  This message is for monitoring the set up of the data sharing hub management.",
                 "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0013 - The {0} integration connector has created a new data dictionary for data sharing hub {1} ({2})
     */
    NEW_DATA_DICTIONARY("LISKOV-DATA-HUB-MANAGER-0013",
                 AuditLogRecordSeverityLevel.INFO,
                 "The {0} integration connector has created a new data dictionary for data sharing hub {1} ({2})",
                 "The connector has created a data dictionary for a new data sharing hub.",
                 "No action is required.  This message is for monitoring the set up of the data sharing hub data dictionary.",
                 "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0014 - The {0} integration connector has created a new data field {1} ({2}) for data sharing hub {3} ({4})
     */
    NEW_DATA_FIELD("LISKOV-DATA-HUB-MANAGER-0014",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector has created a new data field {1} ({2}) for data sharing hub {3} ({4})",
                        "The connector has created a data field for a data sharing hub's data dictionary.",
                        "No action is required.  This message is for monitoring the set up of the data sharing hub's data fields.",
                        "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0016 - The {0} integration connector is refreshing data fields from {1} data store {2} ({3}) for data sharing hub {4} ({5})
     */
    REFRESHING_DATA_HUB_STORE("LISKOV-DATA-HUB-MANAGER-0016",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector is refreshing data fields from {1} data store {2} ({3}) for data sharing hub {4} ({5})",
                              "The connector is initiating its refreshing of a data sharing hub.",
                              "No action is required.  This message is for monitoring the activity of the data sharing hub management.",
                              "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0017 - The {0} integration connector is retrieving known data dictionary definitions for data sharing hub {1} ({2})
     */
    RETRIEVING_DATA_FIELDS("LISKOV-DATA-HUB-MANAGER-0017",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector is retrieving known data dictionary definitions for data sharing hub {1} ({2})",
                              "The connector is initiating its retrieving the contents of the data dictionary for a data sharing hub.",
                              "No action is required.  This message is for monitoring the progress of the data sharing hub management refresh.",
                              "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0018 - The {0} integration connector has created a new data structure {1} ({2}) for data sharing hub {3} ({4})
     */
    NEW_DATA_STRUCTURE("LISKOV-DATA-HUB-MANAGER-0018",
                   AuditLogRecordSeverityLevel.INFO,
                   "The {0} integration connector has created a new data structure {1} ({2}) for data sharing hub {3} ({4})",
                   "The connector has created a data structure for a data sharing hub's data dictionary.",
                   "No action is required.  This message is for monitoring the set up of the data sharing hub's data structures.",
                   "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0019 - The {0} integration connector is refreshing data fields from {1} {2} ({3}) for data sharing hub {4} ({5})
     */
    REFRESHING_TABULAR_FILE("LISKOV-DATA-HUB-MANAGER-0019",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector is refreshing data fields from {1} {2} ({3}) for data sharing hub {4} ({5})",
                            "The connector is reading the data fields from the columns of a file with a tabular schema, such as a CSV file or an Apache Parquet file, so that it can refresh a data sharing hub.",
                            "No action is required.  This message identifies the type of file, and the file, that the data fields are being read from.",
                            "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0020 - The {0} integration connector has started engine action {1} to enable the cataloguing of {2} {3} ({4}) using governance action type {5}
     */
    ENABLING_CATALOGUING("LISKOV-DATA-HUB-MANAGER-0020",
                         AuditLogRecordSeverityLevel.INFO,
                         "The {0} integration connector has started engine action {1} to enable the cataloguing of {2} {3} ({4}) using governance action type {5}",
                         "The connector has detected that the contents of a member of a data sharing hub are not being catalogued and has requested that the appropriate cataloguing integration connector begins to monitor it.  The contents of the member will appear in open metadata once the cataloguing integration connector has run.",
                         "No action is required.  This message is for monitoring the set up of the cataloguing for the members of a data sharing hub.",
                         "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0021 - The {0} integration connector has started engine action {1} to survey {2} {3} ({4}) using governance action type {5}
     */
    STARTING_SURVEY("LISKOV-DATA-HUB-MANAGER-0021",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} integration connector has started engine action {1} to survey {2} {3} ({4}) using governance action type {5} because {6}",
                    "The connector has requested a new survey of a member of a data sharing hub so that the latest characteristics of its contents are available to the data sharing hub's owner.",
                    "No action is required.  This message is for monitoring the surveying of the members of a data sharing hub.",
                    "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0022 - The {0} integration connector is unable to locate a technology type called {1} for {2} {3} ({4})
     */
    NO_TECHNOLOGY_TYPE("LISKOV-DATA-HUB-MANAGER-0022",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is unable to locate a technology type called {1} for {2} {3} ({4})",
                       "The connector is not able to determine which cataloguing and survey governance action types are appropriate for this element and so it skips them.  The rest of the refresh continues.",
                       "If this element should be catalogued and surveyed, check that its deployedImplementationType property is set to a technology type that is defined in the content packs loaded into this metadata store.",
                       "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0023 - The {0} integration connector has changed the path name of {1} {2} ({3}) from {4} to its canonical path name {5}
     */
    CANONICAL_PATH_NAME_SET("LISKOV-DATA-HUB-MANAGER-0023",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector has changed the path name of {1} {2} ({3}) from {4} to its canonical path name {5}",
                            "The folder or file is a member of a data sharing hub, or is catalogued beneath a folder that is, and it was catalogued with a path name that is local to one machine.  The connector has replaced the local mount point in its path name with the canonical mount point, so that it lines up with the folders and files catalogued by other runtimes.  Its qualified name and its connection are not changed.",
                            "No action is required unless the canonical path name is wrong.  In that case, correct the mount points and restore the path name.  The mount points come from the file system linked to the folder or one of the folders above it; then from the catalog target relationship that makes the folder a target of the folder cataloguer - its configuration properties, or the file system named as its metadata source - and otherwise from the localMountPoint and canonicalMountPoint configuration properties of this connector.",
                            "https://egeria-project.org/concepts/data-sharing-hub/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0024 - The {0} integration connector has linked {1} elements {2} and {3} as potential duplicates because they have the same canonical path name {4}
     */
    POTENTIAL_DUPLICATES_LINKED("LISKOV-DATA-HUB-MANAGER-0024",
                                AuditLogRecordSeverityLevel.INFO,
                                "The {0} integration connector has linked {1} elements {2} and {3} as potential duplicates because they have the same canonical path name {4}",
                                "The two elements are in a folder that is a member of a data sharing hub and, now that they have their canonical path names, they turn out to describe the same folder or file.  The connector has linked them with a PeerDuplicateLink relationship whose status is DISCOVERED.  They are not combined on retrieval until the link is validated.",
                                "No action is required if the Mendel Automated Duplicate Manager is running, since it validates links between files, or folders, that have the same path name.  Otherwise a steward needs to decide whether the elements are duplicates.",
                                "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * LISKOV-DATA-HUB-MANAGER-0025 - The {0} integration connector is ignoring the file systems for file folder {1} because the nearest ones are {2} file systems with mount points: {3}
     */
    AMBIGUOUS_FILE_SYSTEMS("LISKOV-DATA-HUB-MANAGER-0025",
                           AuditLogRecordSeverityLevel.ACTION,
                           "The {0} integration connector is ignoring the file systems for file folder {1} because the nearest ones are {2} file systems with mount points: {3}",
                           "The file folder is a member of a data sharing hub.  The connector takes the mount points for the folder from the FileSystem that the folder, or one of the folders above it, is linked to with a CapabilityAssetUse relationship.  The nearest file systems are used - the ones linked to the folder itself or, failing that, to the closest level of folders above it that has any.  More than one was found at that level, even after setting aside the ones whose useType is USES, so there is no telling which one's mount points apply.  The connector carries on as if the folder had no file system, taking the mount points from the folder's catalog target, its own configuration properties or the defaults.",
                           "Review the CapabilityAssetUse relationships that link the listed file systems to the folder, or to the nearest folders above it.  Remove the ones that are wrong, or set their useType to USES, so that a single file system is left.",
                           "https://egeria-project.org/concepts/data-sharing-hub/"),

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
    LiskovAuditCode(String                      messageId,
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
    LiskovAuditCode(String                      messageId,
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
        return "LiskovAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
