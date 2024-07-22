/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The UCAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum UCAuditCode implements AuditLogMessageSet
{
    /**
     * UNITY-CATALOG-CONNECTOR-0001 - The Unity Catalog connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("UNITY-CATALOG-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Unity Catalog connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}",
                         "The connector is unable to process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * UNITY-CATALOG-CONNECTOR-0002 - The {0} connector is unable to retrieve details of any catalogs for Unity Catalog Server {1} ({2})
     */
    NO_CATALOGS( "UNITY-CATALOG-CONNECTOR-0002",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} connector is unable to retrieve details of any catalogs for Unity Catalog Server {1} ({2})",
                       "The connector terminates.",
                       "Check that this server is a Unity Catalog (UC) Server.  If it is, ensure the permissions are set up so this connector can retrieve information from the server."),

    /**
     * UNITY-CATALOG-CONNECTOR-0003 - The {0} integration connector has catalogued data set {1} ({2})
     */
    CATALOGED_DATABASE( "UNITY-CATALOG-CONNECTOR-0003",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector has catalogued data set {1} ({2})",
                  "The integration connector looks for another database.",
                  "This is an information message showing that the integration connector has found a new database."),


    /**
     * UNITY-CATALOG-CONNECTOR-0004 - he {0} integration connector is skipping data set {1} ({2}) because it is already catalogued
     */
    SKIPPING_DATABASE( "UNITY-CATALOG-CONNECTOR-0004",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector is skipping data set {1} ({2}) because it is already catalogued",
                        "The integration connector continues, looking for another database.",
                        "This is an information message showing that the integration connector is working, but does not need to do any processing on this database."),



    /**
     * UNITY-CATALOG-CONNECTOR-0005 - A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3}
     */
    CLIENT_SIDE_REST_API_ERROR( "UNITY-CATALOG-CONNECTOR-0005",
                                AuditLogRecordSeverityLevel.EXCEPTION,
                                "A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3}",
                                "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                                "Look for errors in the local server's console to understand and correct the source of the error."),

    /**
     * UNITY-CATALOG-CONNECTOR-0006 - The {0} OSS Unity Catalog Connector has been supplied with a resource connector of class {1} rather than class {2} for asset {3}
     */
    WRONG_REST_CONNECTOR("UNITY-CATALOG-CONNECTOR-0006",
                         AuditLogRecordSeverityLevel.ERROR,
                         "The {0} OSS Unity Catalog Connector has been supplied with a resource connector of class {1} rather than class {2} for asset {3}",
                         "The connector is unable to continue to profile Unity Catalog because it can not call its REST API.",
                         "Use the details from the error message to determine the class of the connector.  " +
                                 "Update the connector type associated with Unity Catalog's Connection in the metadata store."),

    /**
     * UNITY-CATALOG-CONNECTOR-0007 - The {0} OSS Unity Catalog Connector has been supplied with a friendship connector with GUID {1}
     */
    FRIENDSHIP_GUID("UNITY-CATALOG-CONNECTOR-0007",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} OSS Unity Catalog Connector has been supplied with a friendship connector with GUID {1}",
                    "The friendship connector is an integration connector that is able to synchronize the contents inside a Unity Catalog (UC) connector.  Therefore, they will cooperate to synchronize the contents of the Unity Catalog with the open metadata ecosystem.",
                    "No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of Unity Catalog"),

    /**
     * UNITY-CATALOG-CONNECTOR-0008 - The {0} OSS Unity Inside Catalog Synchronizer Connector only works with catalog targets
     */
    IGNORING_ENDPOINT("UNITY-CATALOG-CONNECTOR-0008",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} OSS Unity Inside Catalog Synchronizer Connector only works with catalog targets",
                    "The connector is ignoring the Unity Catalog (UC) server instance that are configured directly through its connection.",
                    "Update the integration connector's configuration to use catalog targets."),

    /**
     * UNITY-CATALOG-CONNECTOR-0009 - The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Unity Catalog Server Asset {3} for Unity Catalog (UC) {4}
     */
    NEW_CATALOG_TARGET("UNITY-CATALOG-CONNECTOR-0009",
                      AuditLogRecordSeverityLevel.INFO,
                      "The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Unity Catalog Server Asset {3} for Unity Catalog (UC) {4}",
                      "The connector has requested that its friendship connector starts to catalog a new Unity Catalog (UC) catalog.",
                      "Verify that the cataloguing starts the next time that the friendship connector refreshes."),

    /**
     * UNITY-CATALOG-CONNECTOR-0010 - The {0} Connector has detected a change in the identity of element {1}.  The original id was from {2} and now it is {3} in Unity Catalog (UC) {4}
     */
    IDENTITY_MISMATCH("UNITY-CATALOG-CONNECTOR-0010",
                       AuditLogRecordSeverityLevel.ACTION,
                       "The {0} Connector has detected a change in the identity of element {1}.  The original id was from {2} and now it is {3} in Unity Catalog (UC) {4}",
                       "The connector will continue to synchronize metadata with Unity Catalog (UC).  This element will not be updated in with system and will remain out of sync.",
                       "Verify that the element is being correctly synchronized.  It is possible that there are two different elements with the same name. Also investigate why this element changed in Unity Catalog when it is owned by the open metadata ecosystem.  It may be that the structure of the element was changed and the UC element was replaced to reflect the new data structure.  If this is a planned change then all is ok, if it is unexpected then take steps to repair the data source and prevent it happening again."),

    /**
     * UNITY-CATALOG-CONNECTOR-0011 - The {0} Connector has detected a change in the open metadata that controls the definition of catalog {1} but is unable to update the catalog in Unity Catalog (UC) at {2}
     */
    CATALOG_UPDATE("UNITY-CATALOG-CONNECTOR-0011",
                 AuditLogRecordSeverityLevel.ACTION,
                 "The {0} Connector has detected a change in the open metadata that controls the definition of catalog {1} but is unable to update the catalog in Unity Catalog (UC) at {2}",
                 "The connector will continue to synchronize metadata with Unity Catalog (UC).  However, this catalog's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog uses the PATCH request which is not supported by Java.",
                 "Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the table in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying data source."),

    /**
     * UNITY-CATALOG-CONNECTOR-0012 - The {0} Connector has detected a change in the open metadata that controls the definition of schema {1} but is unable to update the schema in Unity Catalog (UC) at {2}
     */
    SCHEMA_UPDATE("UNITY-CATALOG-CONNECTOR-0012",
                 AuditLogRecordSeverityLevel.ACTION,
                 "The {0} Connector has detected a change in the open metadata that controls the definition of schema {1} but is unable to update the schema in Unity Catalog (UC) at {2}",
                 "The connector will continue to synchronize metadata with Unity Catalog (UC).  However, this schema's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog uses the PATCH request which is not supported by Java.",
                 "Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the table in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying data source."),

    /**
     * UNITY-CATALOG-CONNECTOR-0013 - The {0} Connector has detected a change in the open metadata that controls the definition of table {1} but is unable to update the table in Unity Catalog (UC) at {2}
     */
    TABLE_UPDATE("UNITY-CATALOG-CONNECTOR-0013",
                 AuditLogRecordSeverityLevel.ACTION,
                 "The {0} Connector has detected a change in the open metadata that controls the definition of table {1} but is unable to update the table in Unity Catalog (UC) at {2}",
                 "The connector will continue to synchronize metadata with Unity Catalog (UC) catalog.  However, this table's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog does not support an update request.",
                 "Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the table in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying data source."),

    /**
     * UNITY-CATALOG-CONNECTOR-0014 - The {0} Connector has detected a change in the open metadata element {1} that controls the definition of volume {2} but is unable to update the volume in Unity Catalog (UC) at {3}
     */
    VOLUME_UPDATE("UNITY-CATALOG-CONNECTOR-0014",
                 AuditLogRecordSeverityLevel.ACTION,
                 "The {0} Connector has detected a change in the open metadata element {1} that controls the definition of volume {2} but is unable to update the volume in Unity Catalog (UC) at {3}",
                 "The connector will continue to synchronize metadata with Unity Catalog (UC) catalog.  However, this volume's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog does not support a full update request.",
                 "Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the volume in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying data source."),

    /**
     * UNITY-CATALOG-CONNECTOR-0015 - The {0} Connector has detected a change in the open metadata element {1} that controls the definition of function {2} but is unable to update the function in Unity Catalog (UC) at {3}
     */
    FUNCTION_UPDATE("UNITY-CATALOG-CONNECTOR-0015",
                  AuditLogRecordSeverityLevel.ACTION,
                  "The {0} Connector has detected a change in the open metadata element {1} that controls the definition of function {2} but is unable to update the function in Unity Catalog (UC) at {3}",
                  "The connector will continue to synchronize metadata with Unity Catalog (UC) catalog.  However, this function's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog does not support a full update request.",
                  "Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the volume in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying called data sources."),

    /**
     * UNITY-CATALOG-CONNECTOR-0016 - The {0} Connector has detected that the open metadata element that controls the definition of element {1} in Unity Catalog (UC) at {2} has been deleted
     */
    UC_ELEMENT_DELETE("UNITY-CATALOG-CONNECTOR-0016",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} Connector has detected that the open metadata element that controls the definition of element {1} in Unity Catalog (UC) at {2} has been deleted",
                  "The connector will delete the element in Unity Catalog (UC) catalog.",
                  "Validate that the change in the open metadata ecosystem is intended.  If it is, no additional action is required.  If the element is still required, investigate what happened to the element in the open metadata ecosystem.   It may have moved zones or metadata collections or its security controls changed, making it invisible to the connector.  Or it may have been soft-deleted which means it can be restored."),

    /**
     * UNITY-CATALOG-CONNECTOR-0017 - The {0} Connector has called method {1} that is not implemented and so is not able to synchronize element {2} with the Unity Catalog (UC) at {3}
     */
    MISSING_METHOD("UNITY-CATALOG-CONNECTOR-0017",
                  AuditLogRecordSeverityLevel.ACTION,
                  "The {0} Connector has called method {1} that is not implemented and so is not able to synchronize element {2} with the Unity Catalog (UC) at {3}",
                  "The connector will continue to synchronize metadata with Unity Catalog (UC) catalog.  However, the current element will remain unchanged and out of sync with the open metadata ecosystem.",
                  "Update the connector code to implement this method."),
    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * Constructor
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    UCAuditCode(String                      messageId,
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
        return "UCAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
