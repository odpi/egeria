/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The ApacheAtlasErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
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
public enum ApacheAtlasErrorCode implements ExceptionMessageSet
{
    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-400-001 - Integration connector {0} has been configured without the URL to Apache Atlas
     */
    NULL_URL(400, "APACHE-ATLAS-INTEGRATION-CONNECTOR-400-001",
                     "Integration connector {0} has been configured without the URL to Apache Atlas",
                     "The connector is move to FAILED status and will not be called by the integration daemon until the configuration error has been corrected.",
                     "The Apache Atlas URL is configured in the integration connector's connection endpoint in the address property.  Typically it is the host name and port where Apache Atlas is listening.  The connection is either found in the Integration Daemon's configuration, or, if the Integration Daemon is configured with integration groups, in the open metadata definition of the appropriate integration group."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-400-002 - Integration connector {0} has been configured with either a null userId or password for connecting to Apache Atlas
     */
    NULL_USER(400, "APACHE-ATLAS-INTEGRATION-CONNECTOR-400-002",
             "Integration connector {0} has been configured with either a null userId or password for connecting to Apache Atlas",
             "The connector is moved to FAILED status and will no longer be called to synchronize metadata.",
             "Update the connection information for the connector.  " +
                     "This may have been supplied through the Integration Daemon's configuration, " +
                     "or if the Integration Daemon is using integration groups, " +
                     "the connection information is stored in the open metadata ecosystem.  " +
                     "It is possible to supply the userId and password directly in the connection object or via an embedded SecretsConnector."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-400-003 - Integration connector {0} has been configured without a metadataSourceQualifiedName value
     */
    NULL_ASSET_MANAGER(400, "APACHE-ATLAS-INTEGRATION-CONNECTOR-400-003",
              "Integration connector {0} has been configured without a metadataSourceQualifiedName value",
              "The connector uses the metadataSourceQualifiedName to identify the metadata that originated in Apache Atlas so that any updates/deletes to this metadata are reflected into the open metadata ecosystem.  Otherwise, any changes will be overridden by the values in the open metadata ecosystem. In order to ensure metadata integrity, the connector is moved to FAILED status and will no longer be called to synchronize metadata until the metadata source name has been supplied.",
              "Update the metadata source qualified name for the connector.  " +
                      "This may have been supplied through the Integration Daemon's configuration, " +
                      "or, if the Integration Daemon is using integration groups, " +
                      "the connection information is stored in the connector's RegisteredIntegrationConnector relationship in the open metadata ecosystem."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-400-004 - Integration connector {0} is unable to create an Apache Atlas REST Connector
     */
    NULL_ATLAS_CLIENT(400, "APACHE-ATLAS-INTEGRATION-CONNECTOR-400-004",
                       "Integration connector {0} is unable to create an Apache Atlas REST Connector",
                       "The connector uses the Apache Atlas REST Connector to make REST calls to Apache Atlas. The connector is moved to FAILED status and will no longer be called to synchronize metadata until the problem creating the Apache Atlas REST Connector is resolved.",
                       "Ensure that the jar file for the Apache Atlas REST Connector is in the class path of the platform."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-404-001 - The {0} integration connector can not retrieve the correlation information for {1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5}
     */
    MISSING_CORRELATION(404, "APACHE-ATLAS-INTEGRATION-CONNECTOR-404-001",
                        "The {0} integration connector can not retrieve the correlation information for {1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5}",
                        "The correlation information that should be associated with the open metadata entity is missing and the integration connector is not able to confidently synchronize it with the Apache Atlas entity.",
                        "Review the audit log to determine if there were errors detected when the open metadata entity was created.  The simplest resolution is to delete the open metadata entity.  However, if this entity has been enhanced with many attachments and classifications then it is also possible to add the correlation information to the open metadata entity to allow the synchronization to continue."),

    /**
     * APACHE-ATLAS-INTEGRATION-CONNECTOR-500-001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "APACHE-ATLAS-INTEGRATION-CONNECTOR-500-001",
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to catalog one or more metadata elements.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),
   ;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for ApacheAtlasErrorCode expects to be passed one of the enumeration rows defined in
     * ApacheAtlasErrorCode above.   For example:
     * <br><br>
     *     ApacheAtlasErrorCode   errorCode = ApacheAtlasErrorCode.ERROR_SENDING_EVENT;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ApacheAtlasErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "ApacheAtlasErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
