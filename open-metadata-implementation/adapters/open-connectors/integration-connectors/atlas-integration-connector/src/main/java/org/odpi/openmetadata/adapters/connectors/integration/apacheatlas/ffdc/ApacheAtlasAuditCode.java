/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The ApacheAtlasAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum ApacheAtlasAuditCode implements AuditLogMessageSet
{
    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0001 - The {0} integration connector has been initialized to publish all glossary terms to the Apache Atlas server at URL {1}
     */
    CONNECTOR_CONFIGURATION_ALL_EGERIA_GLOSSARIES("APACHE-ATLAS-INTEGRATION-CONNECTOR-0001",
                                                  OMRSAuditLogRecordSeverity.INFO,
                                                  "The {0} integration connector has been initialized to publish all glossary terms to the Apache Atlas server at URL {1}",
                                                  "The connector is designed to publish changes to all active glossary terms to equivalent Apache Atlas glossaries.",
                                                  "No specific action is required.  This message is to confirm the configuration for the integration connector allows all open metadata glossaries to be published to Apache Atlas."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0002 - The {0} integration connector has been initialized to publish glossary terms from glossary {2} in the Apache Atlas server at URL {1} to the open metadata ecosystem
     */
    CONNECTOR_CONFIGURATION_SPECIFIC_EGERIA_GLOSSARIES("APACHE-ATLAS-INTEGRATION-CONNECTOR-0002",
                                                       OMRSAuditLogRecordSeverity.INFO,
                                                       "The {0} integration connector has been initialized to publish glossary terms from glossary {2} in the Apache Atlas server at URL {1} to the open metadata ecosystem",
                                                       "The connector is designed to publish changes to all active glossary terms from the named glossary to an equivalent glossary on Apache Atlas.",
                                                       "No specific action is required.  This message is to confirm the configuration for the integration connector limits which open metadata glossaries are to be published to Apache Atlas."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0003 - The {0} integration connector has been initialized to publish all glossary terms from the Apache Atlas server at URL {1} to the open metadata ecosystem
     */
    CONNECTOR_CONFIGURATION_ALL_ATLAS_GLOSSARIES("APACHE-ATLAS-INTEGRATION-CONNECTOR-0003",
                                                  OMRSAuditLogRecordSeverity.INFO,
                                                  "The {0} integration connector has been initialized to publish all glossary terms from the Apache Atlas server at URL {1} to the open metadata ecosystem",
                                                  "The connector is designed to publish changes to all glossary terms from the Apache Atlas glossaries to Egeria.",
                                                  "No specific action is required.  This message is to confirm the configuration for the integration connector allows all Atlas glossaries to be published to the open metadata ecosystem."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0004 - The {0} integration connector has been initialized to publish glossary terms from glossary {2} to the Apache Atlas server at URL {1}
     */
    CONNECTOR_CONFIGURATION_SPECIFIC_ATLAS_GLOSSARIES("APACHE-ATLAS-INTEGRATION-CONNECTOR-0004",
                                                       OMRSAuditLogRecordSeverity.INFO,
                                                       "The {0} integration connector has been initialized to publish glossary terms from glossary {2} to the Apache Atlas server at URL {1}",
                                                       "The connector is designed to publish changes to all active glossary terms from the name glossary to an equivalent glossary on Apache Atlas.",
                                                       "No specific action is required.  This message is to confirm the configuration for the integration connector limits which Apache Atlas glossaries are to be published to the open metadata ecosystem."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0005 - The {0} integration connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4}
     */
    BAD_CONFIGURATION("APACHE-ATLAS-INTEGRATION-CONNECTOR-0005",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The {0} integration connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4}",
                          "The exception is passed back to the Catalog Integrator OMIS in the integration daemon that is hosting " +
                                  "this connector to enable it to perform error handling.  More messages are likely to follow describing the " +
                                  "error handling that was performed.  These can help to determine how to recover from this error",
                          "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                  "exception stack trace to determine why the connector is not able to access the event broker and resolve that issue.  " +
                                  "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                  "connector in the integration daemon once the original cause of the error has been corrected."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0006 - The {0} integration connector is unable to retrieve the requested {1} glossary from the open metadata ecosystem
     */
    UNABLE_TO_RETRIEVE_EGERIA_GLOSSARY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0006",
                                       OMRSAuditLogRecordSeverity.INFO,
                                       "The {0} integration connector is unable to retrieve the requested {1} glossary from the open metadata ecosystem",
                                       "Synchronization of the requested glossary is skipped until the requested glossary has been created.",
                                       "Check that the configured glossary name is correct.  Check that the failure to retrieve the glossary is expected."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0007 - The {0} integration connector us unable to retrieve requested {1} glossary from Apache Atlas
     */
    UNABLE_TO_RETRIEVE_ATLAS_GLOSSARY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0007",
                                       OMRSAuditLogRecordSeverity.INFO,
                                       "The {0} integration connector us unable to retrieve requested {1} glossary from Apache Atlas",
                                       "Synchronization of the requested Apache Atlas glossary is skipped until the requested glossary has been created.",
                                       "Check that the configured glossary name is correct.  Check that the failure to retrieve the glossary from Apache Atlas is expected."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0008 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("APACHE-ATLAS-INTEGRATION-CONNECTOR-0008",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to catalog one or more metadata elements.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0009 - The {0} integration connector has stopped its monitoring of Apache Atlas at {1} and is shutting down
     */
    CONNECTOR_STOPPING("APACHE-ATLAS-INTEGRATION-CONNECTOR-0009",
                       OMRSAuditLogRecordSeverity.INFO,
                       "The {0} integration connector has stopped its monitoring of Apache Atlas at {1} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0010 - The open metadata glossary {0} equivalent for Apache Atlas glossary {1} is missing
     */
    EGERIA_GLOSSARY_DELETED("APACHE-ATLAS-INTEGRATION-CONNECTOR-0010",
                            OMRSAuditLogRecordSeverity.INFO,
                            "The open metadata glossary {0} equivalent for Apache Atlas glossary {1} is missing; removing Apache Atlas copy",
                            "An open metadata glossary has been deleted.  It has been copied to Apache Atlas in the past.  The Atlas glossary needs to be deleted too.",
                            "This is not necessarily an error, unless the open metadata glossary should not have been deleted."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0011 - The open metadata glossary term {0} equivalent for Apache Atlas glossary term {1} is missing
     */
    EGERIA_GLOSSARY_TERM_DELETED("APACHE-ATLAS-INTEGRATION-CONNECTOR-0011",
                                 OMRSAuditLogRecordSeverity.INFO,
                                 "The open metadata glossary term {0} equivalent for Apache Atlas glossary term {1} is missing; removing Apache Atlas copy",
                                 "An open metadata glossary term has been deleted.  It has been copied to Apache Atlas in the past.  The Atlas glossary term needs to be deleted too.",
                                 "This is not necessarily an error, unless the open metadata glossary term should not have been deleted."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0012 - The open metadata glossary category {0} equivalent for Apache Atlas glossary category {1} is missing
     */
    EGERIA_GLOSSARY_CATEGORY_DELETED("APACHE-ATLAS-INTEGRATION-CONNECTOR-0012",
                                     OMRSAuditLogRecordSeverity.INFO,
                                     "The open metadata glossary category {0} equivalent for Apache Atlas glossary category {1} is missing; removing Apache Atlas copy",
                                     "An open metadata glossary category has been deleted.  It has been copied to Apache Atlas in the past.  The Atlas glossary category needs to be deleted too.",
                                     "This is not necessarily an error, unless the open metadata glossary category should not have been deleted."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0013 - The equivalent Apache Atlas GUID for {0} open metadata element {1} is not stored as an external identifier
     */
    EGERIA_GUID_MISSING("APACHE-ATLAS-INTEGRATION-CONNECTOR-0013",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "The equivalent Apache Atlas GUID for {0} open metadata element {1} is not stored as an external identifier",
                               "The open metadata element is marked as originating from Apache Atlas.  The unique identifier (GUID) of the original Apache Atlas element is not stored in the open metadata element as an external identifier which means it can not be resynchronized with Apache Atlas.",
                               "This error occurs if the external identifier has been removed from the open metadata element.  To enable synchronization again, either delete the open metadata element and allow it to be recreated in the next refresh scan, or determine the correct Apache Atlas GUID and store it as an external identifier in the open metadata element."),


    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0014 - The open metadata glossary {0} for equivalent Apache Atlas glossary {1} has been unilaterally deleted; connector {2} is putting it back
     */
    REPLACING_EGERIA_GLOSSARY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0014",
                        OMRSAuditLogRecordSeverity.ERROR,
                        "The open metadata glossary {0} for equivalent Apache Atlas glossary {1} has been unilaterally deleted; connector {2} is putting it back",
                        "The open metadata glossary can not be retrieved.  This glossary is owned by Apache Atlas.  The connector is creating a new copy of the Apache Atlas glossary in the open metadata ecosystem.",
                        "Open metadata glossary elements that are copies from Apache Atlas should not be unilaterally removed.  Investigate why this element is missing from the open metadata ecosystem and make changes so it can not happen again."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0015 - The open metadata glossary term {0} for equivalent Apache Atlas glossary term {1} has been unilaterally deleted; connector {2} is putting it back
     */
    REPLACING_EGERIA_GLOSSARY_TERM("APACHE-ATLAS-INTEGRATION-CONNECTOR-0015",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "The open metadata glossary term {0} for equivalent Apache Atlas glossary term {1} has been unilaterally deleted; connector {2} is putting it back",
                              "The open metadata glossary term can not be retrieved.  This glossary term is owned by Apache Atlas.  The connector is creating a new copy of the Apache Atlas glossary term in the open metadata ecosystem.",
                              "Open metadata glossary terms that are copies from Apache Atlas should not be unilaterally removed.  Investigate why this element is missing from the open metadata ecosystem and make changes so it can not happen again."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0016 - The open metadata glossary category {0} for equivalent Apache Atlas glossary category {1} has been unilaterally deleted; connector {2} is putting it back
     */
    REPLACING_EGERIA_GLOSSARY_CATEGORY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0016",
                                   OMRSAuditLogRecordSeverity.ERROR,
                                   "The open metadata glossary category {0} for equivalent Apache Atlas glossary category {1} has been unilaterally deleted; connector {2} is putting it back",
                                   "The open metadata glossary category can not be retrieved.  This glossary category is owned by Apache Atlas.  The connector is creating a new copy of the Apache Atlas glossary category in the open metadata ecosystem.",
                                   "Open metadata glossary categories that are copies from Apache Atlas should not be unilaterally removed.  Investigate why this element is missing from the open metadata ecosystem and make changes so it can not happen again."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0020 - The {0} integration connector is copying the Apache Atlas glossary called {1} into the {2} ({3}) open metadata glossary
     */
    SYNC_ATLAS_GLOSSARY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0020",
                      OMRSAuditLogRecordSeverity.INFO,
                     "The {0} integration connector is copying the Apache Atlas glossary called {1} into the {2} ({3}) open metadata glossary",
                     "The connector will ensure that the content of the glossary in the open metadata ecosystem is the same as the glossary stored in Apache Atlas.",
                     "No action is required.  This message is to record that the connector is preforming a sweep of the Atlas glossary" +
                             " to ensure it is correctly represented in the open metadata ecosystem."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0021 - The {0} integration connector is copying the Apache Atlas glossary term called {1} into the {2} ({3}) open metadata glossary term
     */
    SYNC_ATLAS_GLOSSARY_TERM("APACHE-ATLAS-INTEGRATION-CONNECTOR-0021",
                        OMRSAuditLogRecordSeverity.INFO,
                        "The {0} integration connector is copying the Apache Atlas glossary term called {1} into the {2} ({3}) open metadata glossary term",
                        "The connector will ensure that the content of the glossary term in the open metadata ecosystem is the same as the glossary term stored in Apache Atlas.",
                        "No action is required.  This message is to record that the connector is preforming a sweep of the Atlas glossary" +
                                " to ensure its terms are correctly represented in the open metadata ecosystem."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0022 - The {0} integration connector is copying the Apache Atlas glossary category called {1} into the {2} ({3}) open metadata glossary category
     */
    SYNC_ATLAS_GLOSSARY_CATEGORY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0022",
                             OMRSAuditLogRecordSeverity.INFO,
                             "The {0} integration connector is copying the Apache Atlas glossary category called {1} into the {2} ({3}) open metadata glossary category",
                             "The connector will ensure that the content of the glossary category in the open metadata ecosystem is the same as the glossary category stored in Apache Atlas.",
                             "No action is required.  This message is to record that the connector is preforming a sweep of the Atlas glossary" +
                                     " to ensure its categories are correctly represented in the open metadata ecosystem."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0025 - The {0} integration connector is copying the open metadata glossary {1} ({2}) into the Apache Atlas glossary called {3}
     */
    SYNC_EGERIA_GLOSSARY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0025",
                        OMRSAuditLogRecordSeverity.INFO,
                        "The {0} integration connector is copying the open metadata glossary {1} ({2}) into the Apache Atlas glossary called {3}",
                        "The connector will ensure that the content of the glossary in Apache Atlas is the same as the glossary stored in the open metadata ecosystem.",
                        "No action is required.  This message is to record that the connector is preforming a sweep of the open metadata glossary" +
                                " to ensure it is correctly represented in Apache Atlas."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0026 - The {0} integration connector is copying the open metadata glossary term {1} ({2}) into the Apache Atlas glossary term called {3}
     */
    SYNC_EGERIA_GLOSSARY_TERM("APACHE-ATLAS-INTEGRATION-CONNECTOR-0026",
                         OMRSAuditLogRecordSeverity.INFO,
                         "The {0} integration connector is copying the open metadata glossary term {1} ({2}) into the Apache Atlas glossary term called {3}",
                         "The connector will ensure that the content of the glossary term in Apache Atlas is the same as the glossary term stored in the open metadata ecosystem.",
                         "No action is required.  This message is to record that the connector is preforming a sweep of the open metadata glossary" +
                                 " to ensure its terms correctly represented in Apache Atlas."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0027 - The {0} integration connector is copying the open metadata glossary category {1} ({2}) into the Apache Atlas glossary category called {3}
     */
    SYNC_EGERIA_GLOSSARY_CATEGORY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0027",
                         OMRSAuditLogRecordSeverity.INFO,
                         "The {0} integration connector is copying the open metadata glossary category {1} ({2}) into the Apache Atlas glossary category called {3}",
                         "The connector will ensure that the content of the glossary category in Apache Atlas is the same as the glossary category stored in the open metadata ecosystem.",
                         "No action is required.  This message is to record that the connector is preforming a sweep of the open metadata glossary" +
                                 " to ensure its categories correctly represented in Apache Atlas."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0031 - A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3}
     */
    CLIENT_SIDE_REST_API_ERROR( "APACHE-ATLAS-INTEGRATION-CONNECTOR-0031",
                                OMRSAuditLogRecordSeverity.EXCEPTION,
                                "A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3}",
                                "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                                "Look for errors in the local server's console to understand and correct the source of the error."),


    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0032 - The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}
     */
    UNABLE_TO_REGISTER_LISTENER("APACHE-ATLAS-INTEGRATION-CONNECTOR-0032",
                      OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}",
                                  "The connector continues to scan and synchronize metadata as configured.  Without the listener, updates to open metadata elements with only be synchronized to Apache Atlas during a refresh scan.",
                                  "The likely cause of this error is that the Asset Manager OMAS in the metadata access server used by the integration daemon is not configured to support topics.  This can be changed by reconfiguring the metadata access server to support topics.  A less likely cause is that the metadata access server has stopped running"),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0033 - Glossary category {0} already exists in Apache Atlas; trying a different name
     */
    CATEGORY_ALREADY_EXISTS("APACHE-ATLAS-INTEGRATION-CONNECTOR-0033",
                            OMRSAuditLogRecordSeverity.INFO,
                            "Glossary category {0} already exists in Apache Atlas; trying a different name",
                            "The connector attempts to add a numerical post-fix to the category name to ensure it has a unique name.",
                            "No action is required. The connector will validate whether it has already created the category on another thread, or it will try the request with a new name."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0034 - Glossary term {0} already exists in Apache Atlas; trying a different name
     */
    TERM_ALREADY_EXISTS("APACHE-ATLAS-INTEGRATION-CONNECTOR-0034",
                        OMRSAuditLogRecordSeverity.INFO,
                        "Glossary term {0} already exists in Apache Atlas; trying a different name",
                        "The connector attempts to add a numerical post-fix to the term name to ensure it has a unique name.",
                        "No action is required. The connector will validate whether it has already created the term on another thread, or it will try the request with a new name."),


    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0035 - The {0} integration connector is calling the {1} integration module
     */
    SYNC_INTEGRATION_MODULE("APACHE-ATLAS-INTEGRATION-CONNECTOR-0035",
                             OMRSAuditLogRecordSeverity.INFO,
                             "The {0} integration connector is calling the {1} integration module",
                             "The connector is calling one of its registered integration modules to refresh the metadata it is responsible for.",
                             "No action is required.  This message is to record that the connector is working it way through the registered integration modules.  If an error occurs this message helps to identify which module experienced the error."),


    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0036 - The {0} integration connector can not retrieve the correlation information for {1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5}
     */
    MISSING_CORRELATION("APACHE-ATLAS-INTEGRATION-CONNECTOR-0036",
                            OMRSAuditLogRecordSeverity.ERROR,
                            "The {0} integration connector can not retrieve the correlation information for {1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5}",
                            "The correlation information that should be associated with the open metadata entity is missing and the integration connector is not able to confidently synchronize it with the Apache Atlas entity.",
                            "Review the audit log to determine if there were errors detected when the open metadata entity was created.  The simplest resolution is to delete the open metadata entity.  However, if this entity has been enhanced with many attachments and classifications then it is also possible to add the correlation information to the open metadata entity to allow the synchronization to continue."),


    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0040 - The integration connector {0} created open metadata {1} entity {2} match Apache Atlas {3} entity {4}
     */
    CREATING_EGERIA_ENTITY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0040",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The integration connector {0} created open metadata {1} entity {2} match Apache Atlas {3} entity {4}",
                           "The connector is has created the open metadata entity with information from the Apache Atlas entity.",
                           "No action is required. The connector working to ensure the open metadata ecosystem can store metadata from Apache Atlas."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0041 - The integration connector {0} created Apache Atlas {1} entity {2} match open metadata {3} entity {4}
     */
    CREATING_ATLAS_ENTITY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0041",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The integration connector {0} created Apache Atlas {1} entity {2} match open metadata {3} entity {4}",
                           "The connector is has created the Apache Atlas entity with information from the open metadata entity.",
                           "No action is required. The connector working to ensure Apache Atlas can store metadata from the open metadata ecosystem."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0042 - The integration connector {0} is synchronizing Apache Atlas {1} entity {2} to {3} open metadata entity {4}
     */
    UPDATING_EGERIA_ENTITY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0042",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The integration connector {0} is synchronizing Apache Atlas {1} entity {2} to {3} open metadata entity {4}",
                           "The connector is updating the open metadata entity with information from the Apache Atlas entity.",
                           "No action is required. The connector working to keep the Open metadata entity consistent with its Apache Atlas equivalent."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0043 - The integration connector {0} is synchronizing open metadata {1} entity {2} to the {3} Apache Atlas entity {4}
     */
    UPDATING_ATLAS_ENTITY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0043",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The integration connector {0} is synchronizing open metadata {1} entity {2} to the {3} Apache Atlas entity {4}",
                           "The connector is updating the Apache Atlas entity with information from the open metadata entity.",
                           "No action is required. The connector working to keep the Apache Atlas entity consistent with the open metadata one."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0044 - The integration connector {0} is deleting {1} open metadata entity {2} since Apache Atlas entity {3} has been removed
     */
    DELETING_EGERIA_ENTITY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0044",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The integration connector {0} is deleting {1} open metadata entity {2} since Apache Atlas entity {3} has been removed",
                           "The connector is deleting the open metadata entity because the Apache Atlas entity where its content is sourced from has gone.",
                           "No action is required. The connector is working to keep the two systems consistent."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0045 - The integration connector {0} is deleting the {1} Apache Atlas entity {2} since the open metadata entity {3} has been removed
     */
    DELETING_ATLAS_ENTITY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0045",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The integration connector {0} is deleting the {1} Apache Atlas entity {2} since the open metadata entity {3} has been removed",
                           "The connector is deleting the Apache Atlas entity because the open metadata entity where its content is sourced from has gone.",
                           "No action is required. The connector is working to keep the entities in the two systems consistent."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0046 - The integration connector {0} is replacing {1} open metadata entity {2} for Apache Atlas entity {3} since the open metadata entity has been unilaterally removed
     */
    REPLACING_EGERIA_ENTITY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0046",
                            OMRSAuditLogRecordSeverity.ERROR,
                            "The integration connector {0} is replacing {1} open metadata entity {2} for Apache Atlas entity {3} since the open metadata entity has been unilaterally removed",
                            "The connector is creating a new open metadata entity to represent the Apache Atlas entity in the open metadata ecosystem.  This is because the entity originated in Apache Atlas and this is the proper place to delete the entity.",
                            "Investigate why the connector can not retrieve the original open metadata entity.  Has it been deleted, archived or moved to a governance zone that is not visible to this connector?  Make changes to ensure the open metadata entities synchronized from Apache Atlas are only maintained by this connector."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0047 - The integration connector {0} is replacing Apache Atlas {1} entity {2} for open metadata entity {3} since the Apache Atlas entity has been unilaterally removed
     */
    REPLACING_ATLAS_ENTITY("APACHE-ATLAS-INTEGRATION-CONNECTOR-0047",
                            OMRSAuditLogRecordSeverity.ERROR,
                            "The integration connector {0} is replacing Apache Atlas {1} entity {2} for open metadata entity {3} since the Apache Atlas entity has been unilaterally removed",
                            "The connector is creating a new Apache Atlas entity to represent the open metadata entity from the open metadata ecosystem.  This is because the entity originated in the open metadata ecosystem and this is the proper place to delete the entity.",
                            "Investigate why the connector can not retrieve the original Apache Atlas.   Make changes to ensure the Apache Atlas entities synchronized from the open metadata ecosystem are only maintained by this connector."),


    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-0048 - The integration connector {0} is adding a DataFlow lineage relationship from {1} open metadata entity {2} to {3} open metadata entity {4}
     */
    ADDING_LINEAGE("APACHE-ATLAS-INTEGRATION-CONNECTOR-0048",
                     OMRSAuditLogRecordSeverity.INFO,
                     "The integration connector {0} is adding a DataFlow lineage relationship from {1} open metadata entity {2} to {3} open metadata entity {4}",
                     "The connector is creating a new lineage relationship around a process base on a similar relationship in Apache Atlas.",
                     "No action is required. The connector is working to keep the two systems view of lineage consistent."),

    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for ApacheAtlasAuditCode expects to be passed one of the enumeration rows defined in
     * ApacheAtlasAuditCode above.   For example:
     * <br>
     *     ApacheAtlasAuditCode   auditCode = ApacheAtlasAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    ApacheAtlasAuditCode(String                     messageId,
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
        return "ApacheAtlasAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
