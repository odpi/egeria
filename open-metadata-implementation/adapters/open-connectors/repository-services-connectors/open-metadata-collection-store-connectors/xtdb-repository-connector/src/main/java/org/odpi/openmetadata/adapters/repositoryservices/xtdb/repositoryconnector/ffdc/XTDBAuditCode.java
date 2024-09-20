/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The XTDBAuditCode is used to define the message content for the Audit Log.
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
public enum XTDBAuditCode implements AuditLogMessageSet 
{
    /**
     * OMRS-XTDB-REPOSITORY-0001 - A request to start an XTDB repository node has been received
     */
    REPOSITORY_NODE_STARTING("OMRS-XTDB-REPOSITORY-0001",
                             AuditLogRecordSeverityLevel.STARTUP,
                             "A request to start an XTDB repository node has been received",
                             "The local server is creating a new local node for the XTDB repository.",
                             "This repository will be used as the local repository for this server. Depending on the configuration " +
                                     "of your XTDB repository, this may be the only XTDB node (standalone) or one of many (for example, in " +
                                     "high-availability configurations). All nodes will display this initial message at startup, whether using " +
                                     "an existing persistent store or instantiating a new one. " +
                                     "You should see one of two subsequent messages: OMRS-XTDB-REPOSITORY-0002 indicates that the " +
                                     "node is starting with a configuration provided as part of the administration of the instance, or OMRS-XTDB-REPOSITORY-0003 " +
                                     "indicating that the node is starting up with a default, in-memory-only configuration."),

    /**
     * OMRS-XTDB-REPOSITORY-0002 - An XTDB repository node is starting with a persistent store
     */
    REPOSITORY_NODE_STARTING_WITH_CONFIG("OMRS-XTDB-REPOSITORY-0002",
                                         AuditLogRecordSeverityLevel.STARTUP,
                                         "An XTDB repository node is starting with a persistent store",
                                         "The local server is starting an XTDB node based on the configuration provided through the configurationProperties of the local repository connection.",
                                         "All nodes that are started with a provided configuration that can be interpreted will display this " +
                                                 "message. You should see a subsequent OMRS-XTDB-REPOSITORY-0004 indicating that the " +
                                                 "node has started based on this provided configuration."),

    /**
     * OMRS-XTDB-REPOSITORY-0003 - An XTDB repository node is starting in-memory (only)
     */
    REPOSITORY_NODE_STARTING_NO_CONFIG("OMRS-XTDB-REPOSITORY-0003",
                                       AuditLogRecordSeverityLevel.STARTUP,
                                       "An XTDB repository node is starting in-memory (only)",
                                       "The local server is starting an XTDB node in-memory (only), as no configuration was provided through configurationProperties of the local repository connection.",
                                       "All nodes that are started without any configuration provided will display this " +
                                               "message. You should see a subsequent OMRS-XTDB-REPOSITORY-0004 indicating that the " +
                                               "node has started purely in-memory."),

    /**
     * OMRS-XTDB-REPOSITORY-0004 - An XTDB repository node has started, running XTDB version {0} with: {1}
     */
    REPOSITORY_SERVICE_STARTED("OMRS-XTDB-REPOSITORY-0004",
                               AuditLogRecordSeverityLevel.STARTUP,
                               "An XTDB repository node has started, running XTDB version {0} with: {1}",
                               "The local server has completed startup of an XTDB repository node.",
                               "A XTDB repository node is only in a known running state once this message is displayed. When " +
                                       "the node is shutdown, an OMRS-XTDB-REPOSITORY-0005 will be displayed."),

    /**
     * OMRS-XTDB-REPOSITORY-0005 - The XTDB repository node has shutdown in server {0}
     */
    REPOSITORY_SERVICE_SHUTDOWN("OMRS-XTDB-REPOSITORY-0005",
                                AuditLogRecordSeverityLevel.SHUTDOWN,
                                "The XTDB repository node has shutdown in server {0}",
                                "The local server has requested shut down of a XTDB repository node.",
                                "No action is required. The existing XTDB repository node has been closed and shutdown."),

    /**
     * OMRS-XTDB-REPOSITORY-0006 - The XTDB repository failed to cascade deletions of all relationships for entity {0} due to {1}
     */
    FAILED_RELATIONSHIP_DELETE_CASCADE("OMRS-XTDB-REPOSITORY-0006",
                                       AuditLogRecordSeverityLevel.EXCEPTION,
                                       "The XTDB repository failed to cascade deletions of all relationships for entity {0} due to {1}",
                                       "The local server failed to delete all relationships of the entity during the entity's deletion.",
                                       "This exception and stacktrace indicate the reason why the relationships for the specified entity " +
                                               "could not be determined, and therefore could not be deleted. You will need to manually investigate " +
                                               "the cause of the error and then search for and remove any relationships that have the specified " +
                                               "entity as one end from the XTDB repository. Please raise an issue on GitHub with the details of the" +
                                               "error for guidance and assistance."),

    /**
     * OMRS-XTDB-REPOSITORY-0007 - The XTDB repository failed to cascade deletions of relationship {0} for entity {1} due to {2}
     */
    FAILED_RELATIONSHIP_DELETE("OMRS-XTDB-REPOSITORY-0007",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                               "The XTDB repository failed to cascade deletions of relationship {0} for entity {1} due to {2}",
                               "The local server failed to delete the specified relationship of the entity during the entity's deletion.",
                               "This exception and stacktrace indicate the reason why a single relationships for the specified entity " +
                                       "could not be deleted. You will need to manually investigate " +
                                       "the cause of the error and then search for and manually remove the specified relationship " +
                                       "from the XTDB repository. Please raise an issue on GitHub with the details of the" +
                                       "error for guidance and assistance."),

    /**
     * OMRS-XTDB-REPOSITORY-0008 - The XTDB repository node failed to start due to {0}
     */
    FAILED_REPOSITORY_STARTUP("OMRS-XTDB-REPOSITORY-0008",
                              AuditLogRecordSeverityLevel.EXCEPTION,
                              "The XTDB repository node failed to start due to {0}",
                              "The local XTDB repository node failed to startup within the server, likely due to some configuration issue.",
                              "Review the exception and stacktrace for additional information, correct any configuration issues, and retry."),

    /**
     * OMRS-XTDB-REPOSITORY-0009 - The XTDB repository node failed to shut down due to {0}
     */
    FAILED_REPOSITORY_SHUTDOWN("OMRS-XTDB-REPOSITORY-0009",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                               "The XTDB repository node failed to shut down due to {0}",
                               "The local XTDB repository node failed to shutdown within the server.",
                               "Review the exception and stacktrace for additional information, correct any configuration issues, and retry."),

    /**
     * OMRS-XTDB-REPOSITORY-0010 - Unable to read the configuration for the XTDB repository node due to {0}
     */
    CANNOT_READ_CONFIGURATION("OMRS-XTDB-REPOSITORY-0010",
                              AuditLogRecordSeverityLevel.EXCEPTION,
                              "Unable to read the configuration for the XTDB repository node due to {0}",
                              "The local server was unable to parse the provided configuration for the XTDB server.",
                              "Investigate the logs for additional information, and correct the configurationProperties sent to the connector."),

    /**
     * OMRS-XTDB-REPOSITORY-0011 - Unable to retrieve an instance of kind {0} with GUID {1}: {2}
     */
    FAILED_RETRIEVAL("OMRS-XTDB-REPOSITORY-0011",
                     AuditLogRecordSeverityLevel.ERROR,
                     "Unable to retrieve an instance of kind {0} with GUID {1}: {2}",
                     "The local server was unable to retrieve details of the specified instance and has therefore returned null.",
                     "Use the provided instance details to manually search your XTDB repository for the information to confirm " +
                             "that it is not present. If it is, please raise an issue on GitHub with any further details you can provide. If not, " +
                             "investigate why this retrieval was attempted and correct the calling code to not attempt to retrieve non-existent " +
                             "information."),

    /**
     * OMRS-XTDB-REPOSITORY-0012 - The specified type {0} is not mapped in this repository
     */
    UNMAPPED_TYPE("OMRS-XTDB-REPOSITORY-0012",
                  AuditLogRecordSeverityLevel.TYPES,
                  "The specified type {0} is not mapped in this repository",
                  "The local server is unable to store or retrieve information of the specified type.",
                  "Typically this should only occur types that are explicitly unknown in Egeria; however, if you see " +
                          "this message for a type that you need to use please raise an issue on GitHub with any further information " +
                          "to see whether mappings of this type can be added to the connector."),

    /**
     * OMRS-XTDB-REPOSITORY-0013 - "Found more than one property in the instanceType {0} with the name {1}: {2}
     */
    DUPLICATE_PROPERTIES("OMRS-XTDB-REPOSITORY-0013",
                         AuditLogRecordSeverityLevel.ERROR,
                         "Found more than one property in the instanceType {0} with the name {1}: {2}",
                         "The local server is unable to uniquely determine the property to use for persistence, and will use the last one in the list given.",
                         "This message indicates that there is a uniqueness issue in either the Egeria types or in the mapping " +
                                 "to those types within the XTDB repository. If you see this message, please raise a GitHub issue so that the " +
                                 "duplication can be investigated and resolved in the connector / Egeria codebase."),

    /**
     * OMRS-XTDB-REPOSITORY-0014 - The specified search criteria for matching is not mapped: {0}
     */
    UNMAPPED_MATCH_CRITERIA("OMRS-XTDB-REPOSITORY-0014",
                            AuditLogRecordSeverityLevel.ERROR,
                            "The specified search criteria for matching is not mapped: {0}",
                            "The local server is unable to use the provided matching criteria as part of its search operations.",
                            "This message indicates that there is some new match criteria in Egeria which this connector does not yet " +
                                    "support for searching. If you see this message, please raise a GitHub issue so that the " +
                                    "match criteria can be investigated and resolved in the connector."),

    /**
     * OMRS-XTDB-REPOSITORY-0015 - The specified comparison operator is not mapped: {0} 
     */
    UNKNOWN_COMPARISON_OPERATOR("OMRS-XTDB-REPOSITORY-0015",
                                AuditLogRecordSeverityLevel.ERROR,
                                "The specified comparison operator is not mapped: {0}",
                                "The local server is unable to use the provided comparison operator as part of its search operations.",
                                "This message indicates that there is some new comparison operator in Egeria which this connector does not yet " +
                                        "support for searching. If you see this message, please raise a GitHub issue so that the " +
                                        "comparison operator can be investigated and resolved in the connector."),

    /**
     * OMRS-XTDB-REPOSITORY-0016 - A search was requested for property {0} which does not match any of the type restrictions: {1}
     */
    INVALID_PROPERTY("OMRS-XTDB-REPOSITORY-0016",
                     AuditLogRecordSeverityLevel.TRACE,
                     "A search was requested for property {0} which does not match any of the type restrictions: {1}",
                     "The requested search is for a property which does not exist on any of the types for which the search is restricted, and therefore no results will be returned.",
                     "This message is provided for informational purposes, should a user expect some results to their search " +
                             "but find that there are none. Correct the calling code to either fix the property being searched, or " +
                             "change the type restrictions such that some type that actually has this property is included in the search."),

    /**
     * OMRS-XTDB-REPOSITORY-0017 - A search was requested for property {0} using an invalid comparison operator: {1}
     */
    INVALID_STRING_COMPARISON("OMRS-XTDB-REPOSITORY-0017",
                              AuditLogRecordSeverityLevel.INFO,
                              "A search was requested for property {0} using an invalid comparison operator: {1}",
                              "The requested search is for a string property, which only supports a limited set of comparison operators and specifically does not support the comparison requested.",
                              "This message is provided for informational purposes, should a user expect some particular results to their search " +
                                      "but find that there are either none or more than they expected. Correct the calling code to either fix the property being searched, or " +
                                      "change the comparison operator to one that is valid for string properties. Alternatively, raise a GitHub issue " +
                                      "explaining the comparison operator that is not supported and your expected behavior of this operator " +
                                      "when comparing strings to see if this could be added to the connector."),

    /**
     * OMRS-XTDB-REPOSITORY-0018 - Unable to serialize a value for {0} in type {1} due to: {2}
     */
    SERIALIZATION_FAILURE("OMRS-XTDB-REPOSITORY-0018",
                          AuditLogRecordSeverityLevel.EXCEPTION,
                          "Unable to serialize a value for {0} in type {1} due to: {2}",
                          "The local server was unable to serialize a value into JSON for persistence.",
                          "This exception and stacktrace indicate the reason why a value could not be serialized into JSON " +
                                  "for persistence. You will need to manually investigate the cause of the error and determine how to " +
                                  "correct the calling code to ensure values are JSON-encode-able. Should the calling code be part of " +
                                  "the connector itself, please raise an issue on GitHub with the details of the error for guidance and " +
                                  "assistance."),

    /**
     * OMRS-XTDB-REPOSITORY-0019 - Unable to deserialize a value for {0} in type {1} into {2} due to: {3}
     */
    DESERIALIZATION_FAILURE("OMRS-XTDB-REPOSITORY-0019",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "Unable to deserialize a value for {0} in type {1} into {2} due to: {3}",
                            "The local server was unable to deserialize a value from persisted JSON form.",
                            "This exception and stacktrace indicate the reason why a value could not be deserialized from JSON " +
                                    "persistence. You will need to manually investigate the cause of the error and determine how to " +
                                    "correct the calling code to ensure values are JSON-decode-able. Should the calling code be part of " +
                                    "the connector itself, please raise an issue on GitHub with the details of the error for guidance and " +
                                    "assistance."),

    /**
     * OMRS-XTDB-REPOSITORY-0020 - An unexpected runtime error occurred caused by {1}: {0}
     */
    UNEXPECTED_RUNTIME_ERROR("OMRS-XTDB-REPOSITORY-0020",
                             AuditLogRecordSeverityLevel.EXCEPTION,
                             "An unexpected runtime error occurred caused by {1}: {0}",
                             "The system was unable to complete processing of a given action due to an unexpected problem.",
                             "Check the system logs for the full stacktrace of the given exception and diagnose or report the problem."),

    /**
     * OMRS-XTDB-REPOSITORY-0021 - Unable to map the retrieved {0} instance with XTDB reference {1} into an Egeria object: {2}
     */
    MAPPING_FAILURE("OMRS-XTDB-REPOSITORY-0021",
                    AuditLogRecordSeverityLevel.ERROR,
                    "Unable to map the retrieved {0} instance with XTDB reference {1} into an Egeria object: {2}",
                    "A search result was found matching the criteria but the system is unable to map it into an Egeria object.",
                    "This message is provided for informational purposes, should a user expect some particular results to their search " +
                            "but find that there are either none or less than they expected. You will need to manually retrieve the " +
                            "XTDB document (by the provided reference) to diagnose why it cannot be mapped into an Egeria object. " +
                            "Please raise an issue on GitHub with the details of the error for guidance and assistance."),

    /**
     * OMRS-XTDB-REPOSITORY-0022 - The requested {0} enumeration with ordinal or symbolic name {1} does not exist
     */
    NON_EXISTENT_ENUM("OMRS-XTDB-REPOSITORY-0022",
                      AuditLogRecordSeverityLevel.ERROR,
                      "The requested {0} enumeration with ordinal or symbolic name {1} does not exist",
                      "An enumerated value was requested using an ordinal or symbolic name that does not actually exist, so null will be returned.",
                      "Investigate the calling code to determine why an invalid (non-existent) enumeration value is being " +
                              "requested, and correct accordingly. Should the calling code be part of the connector itself, please " +
                              "raise an issue on GitHub with the details of the error so that it can be investigated and resolved in " +
                              "the connector."),

    /**
     * OMRS-XTDB-REPOSITORY-0023 - Unable to map the property {0} as part of {1}
     */
    UNMAPPED_PROPERTY("OMRS-XTDB-REPOSITORY-0023",
                      AuditLogRecordSeverityLevel.ERROR,
                      "Unable to map the property {0} as part of {1}",
                      "A property was found that cannot be mapped as part of the specified type.",
                      "Please raise an issue on GitHub with the details of the error so that the mapping can be investigated " +
                              "and resolved in the connector."),

    /**
     * OMRS-XTDB-REPOSITORY-0024 - Unable to retrieve by GUID when the instance type has no GUID: {0}
     */
    NON_INSTANCE_RETRIEVAL("OMRS-XTDB-REPOSITORY-0024",
                           AuditLogRecordSeverityLevel.ERROR,
                           "Unable to retrieve by GUID when the instance type has no GUID: {0}",
                           "An attempt was made to retrieve an instance type which has no GUID, so null will be returned.",
                           "Investigate the calling code to determine why an instance retrieval is being attempted for a type " +
                                   "that has no GUID by which it can be retrieved. Should the calling code be part of the connector itself, " +
                                   "please raise an issue on GitHub with the details of the error so that the mapping can be investigated " +
                                   "and resolved in the connector."),

    /**
     * OMRS-XTDB-REPOSITORY-0025 - A non-string value was provided for a regex search: {0}
     */
    NO_REGEX("OMRS-XTDB-REPOSITORY-0025",
             AuditLogRecordSeverityLevel.INFO,
             "A non-string value was provided for a regex search: {0}",
             "An attempt was made to search for strings that match a non-string regex criteria, so the condition will be ignored.",
             "This message is provided for informational purposes, should a user expect some particular results to their search " +
                     "but find that there are either none or more than they expected. Correct the calling code to either fix the criteria being searched, or " +
                     "the manner in which the search is being run (to not match string properties but others). Alternatively, raise a GitHub issue " +
                     "explaining the non-string search criteria and your expected behavior of this when comparing strings as if " +
                     "the provided criteria were a regular expression to match against."),

    /**
     * OMRS-XTDB-REPOSITORY-0026 - Cannot sort by property {0} on types: {1}
     */
    NO_SORT_PROPERTY("OMRS-XTDB-REPOSITORY-0026",
                     AuditLogRecordSeverityLevel.INFO,
                     "Cannot sort by property {0} on types: {1}",
                     "An attempt was made to sort search results by property, but no valid property name by which to do the sorting was provided given the type restrictions.",
                     "This message is provided for informational purposes, should a user expect some particular sorting of results to their search " +
                             "but find that they are not sorted as expected. Correct the calling code to either provide a valid name of the property " +
                             "by which results should be sorted, change the type restrictions so that the property is valid for those types, or change " +
                             "the sorting approach to one that is not based on properties."),
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for XTDBAuditCode expects to be passed one of the enumeration rows defined in
     * XTDBAuditCode above.   For example:
     * <p>
     * XTDBAuditCode   auditCode = XTDBAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - the severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    XTDBAuditCode(String                      messageId,
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
        return "XTDBAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
