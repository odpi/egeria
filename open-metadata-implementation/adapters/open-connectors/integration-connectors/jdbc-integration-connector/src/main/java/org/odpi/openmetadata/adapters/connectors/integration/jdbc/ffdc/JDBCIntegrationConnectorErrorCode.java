/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The JDBCErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Kafka monitor integration connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum JDBCIntegrationConnectorErrorCode implements ExceptionMessageSet
{
    /**
     * JDBC-INTEGRATION-CONNECTOR-400-001 - Integration connector {0} has been configured without the URL to Apache Atlas
     */
    NULL_URL(400, "JDBC-INTEGRATION-CONNECTOR-400-001",
                     "Integration connector {0} has been configured without the URL to Apache Atlas",
                     "The connector is move to FAILED status and will not be called by the integration daemon until the configuration error has been corrected.",
                     "The Apache Atlas URL is configured in the integration connector's connection endpoint in the address property.  Typically it is the host name and port where Apache Atlas is listening.  The connection is either found in the Integration Daemon's configuration, or, if the Integration Daemon is configured with integration groups, in the open metadata definition of the appropriate integration group."),

    /**
     * JDBC-INTEGRATION-CONNECTOR-400-002 - Integration connector {0} has been configured with either a null userId or password for connecting to Apache Atlas
     */
    NULL_USER(400, "JDBC-INTEGRATION-CONNECTOR-400-002",
             "Integration connector {0} has been configured with either a null userId or password for connecting to Apache Atlas",
             "The connector is moved to FAILED status and will no longer be called to synchronize metadata.",
             "Update the connection information for the connector.  " +
                     "This may have been supplied through the Integration Daemon's configuration, " +
                     "or if the Integration Daemon is using integration groups, " +
                     "the connection information is stored in the open metadata ecosystem.  " +
                     "It is possible to supply the userId and password directly in the connection object or via an embedded SecretsConnector."),

    /**
     * JDBC-INTEGRATION-CONNECTOR-400-003 - Integration connector {0} has been configured without a metadataSourceQualifiedName value
     */
    NULL_ASSET_MANAGER(400, "JDBC-INTEGRATION-CONNECTOR-400-003",
              "Integration connector {0} has been configured without a metadataSourceQualifiedName value",
              "The connector uses the metadataSourceQualifiedName to identify the metadata that originated in Apache Atlas so that any updates/deletes to this metadata are reflected into the open metadata ecosystem.  Otherwise, any changes will be overridden by the values in the open metadata ecosystem. In order to ensure metadata integrity, the connector is moved to FAILED status and will no longer be called to synchronize metadata until the metadata source name has been supplied.",
              "Update the metadata source qualified name for the connector.  " +
                      "This may have been supplied through the Integration Daemon's configuration, " +
                      "or, if the Integration Daemon is using integration groups, " +
                      "the connection information is stored in the connector's RegisteredIntegrationConnector relationship in the open metadata ecosystem."),

    /**
     * JDBC-INTEGRATION-CONNECTOR-400-004 - Glossary category {0} already exists in Apache Atlas
     */
    CATEGORY_ALREADY_EXISTS(400, "JDBC-INTEGRATION-CONNECTOR-400-004",
             "Glossary category {0} already exists in Apache Atlas",
             "The connector attempts to add a numerical post-fix to the category name to ensure it has a unique name.",
             "No action is required. The connector will validate whether it has already created the category on another thread, or it will try the request with a new name."),

    /**
     * JDBC-INTEGRATION-CONNECTOR-400-005 - Glossary term {0} already exists in Apache Atlas
     */
    TERM_ALREADY_EXISTS(400, "JDBC-INTEGRATION-CONNECTOR-400-005",
                            "Glossary term {0} already exists in Apache Atlas",
                            "The connector attempts to add a numerical post-fix to the term name to ensure it has a unique name.",
                            "No action is required. The connector will validate whether it has already created the term on another thread, or it will try the request with a new name."),


    /**
     * JDBC-INTEGRATION-CONNECTOR-0036 - The {0} integration connector can not retrieve the correlation information for (1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5}
     */
    MISSING_CORRELATION(404, "JDBC-INTEGRATION-CONNECTOR-404-001",
                        "The {0} integration connector can not retrieve the correlation information for (1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5}",
                        "The correlation information that should be associated with the open metadata entity is missing and the integration connector is not able to confidently synchronize it with the Apache Atlas entity.",
                        "Review the audit log to determine if there were errors detected when the open metadata entity was created.  The simplest resolution is to delete the open metadata entity.  However, if this entity has been enhanced with many attachments and classifications then it is also possible to add the correlation information to the open metadata entity to allow the synchronization to continue."),

    /**
     * JDBC-INTEGRATION-CONNECTOR-500-001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "JDBC-INTEGRATION-CONNECTOR-500-001",
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to catalog one or more metadata elements.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * JDBC-INTEGRATION-CONNECTOR-503-001 - A client-side exception {0} was received from API call {1} to URL {2}.  The error message was {3}
     */
    CLIENT_SIDE_REST_API_ERROR(503, "JDBC-INTEGRATION-CONNECTOR-503-001",
                                       "A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3}",
                                       "The integration has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                                       "Look for errors in the local server's console to understand and correct the source of the error."),

    ;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for JDBCErrorCode expects to be passed one of the enumeration rows defined in
     * JDBCErrorCode above.   For example:
     * <br><br>
     *     JDBCErrorCode   errorCode = JDBCErrorCode.ERROR_SENDING_EVENT;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    JDBCIntegrationConnectorErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                errorMessageId,
                                                                errorMessage,
                                                                systemAction,
                                                                userAction);
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
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
        return "JDBCErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
