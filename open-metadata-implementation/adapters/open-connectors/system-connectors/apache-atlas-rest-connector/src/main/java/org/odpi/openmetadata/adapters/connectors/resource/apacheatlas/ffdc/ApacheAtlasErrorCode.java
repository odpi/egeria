/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The ApacheAtlasErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Kafka monitor Apache Atlas REST connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
     * APACHE-ATLAS-REST-CONNECTOR-400-001 - Apache Atlas REST connector {0} has been configured without the URL to Apache Atlas
     */
    NULL_URL(400, "APACHE-ATLAS-REST-CONNECTOR-400-001",
                     "Apache Atlas REST connector {0} has been configured without the URL to Apache Atlas",
                     "The connector is move to FAILED status and will not be called by the integration daemon until the configuration error has been corrected.",
                     "The Apache Atlas URL is configured in the Apache Atlas REST connector's connection endpoint in the address property.  Typically it is the host name and port where Apache Atlas is listening.  The connection is either found in the Integration Daemon's configuration, or, if the Integration Daemon is configured with integration groups, in the open metadata definition of the appropriate integration group."),

    /**
     * APACHE-ATLAS-REST-CONNECTOR-400-002 - Apache Atlas REST connector {0} has been configured with either a null userId or password for connecting to Apache Atlas
     */
    NULL_USER(400, "APACHE-ATLAS-REST-CONNECTOR-400-002",
             "Apache Atlas REST connector {0} has been configured with either a null userId or password for connecting to Apache Atlas",
             "The connector is not able to call Apache Atlas without error.",
             "Update the connection information for the connector.  " +
                     "This may have been supplied through the Integration Daemon's configuration, " +
                     "or if the Integration Daemon is using integration groups, " +
                     "the connection information is stored in the open metadata ecosystem.  " +
                     "It is possible to supply the userId and password directly in the connection object or via an embedded SecretsConnector."),


    /**
     * APACHE-ATLAS-REST-CONNECTOR-400-004 - Glossary category {0} already exists in Apache Atlas
     */
    CATEGORY_ALREADY_EXISTS(400, "APACHE-ATLAS-REST-CONNECTOR-400-004",
                            "Glossary category {0} already exists in Apache Atlas",
                            "The connector attempts to add a numerical post-fix to the category name to ensure it has a unique name.",
                            "No action is required. The connector will validate whether it has already created the category on another thread, or it will try the request with a new name."),

    /**
     * APACHE-ATLAS-REST-CONNECTOR-400-005 - Glossary term {0} already exists in Apache Atlas
     */
    TERM_ALREADY_EXISTS(400, "APACHE-ATLAS-REST-CONNECTOR-400-005",
                        "Glossary term {0} already exists in Apache Atlas",
                        "The connector attempts to add a numerical post-fix to the term name to ensure it has a unique name.",
                        "No action is required. The connector will validate whether it has already created the term on another thread, or it will try the request with a new name."),


    /**
     * APACHE-ATLAS-REST-CONNECTOR-500-001 - The {0} Apache Atlas REST connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "APACHE-ATLAS-REST-CONNECTOR-500-001",
                         "The {0} Apache Atlas REST connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to catalog one or more metadata elements.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * APACHE-ATLAS-REST-CONNECTOR-503-001 - A client-side exception {0} was received from API call {1} to URL {2}.  The error message was {3}
     */
    CLIENT_SIDE_REST_API_ERROR(503, "APACHE-ATLAS-REST-CONNECTOR-503-001",
                                       "A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3}",
                                       "The connector has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                                       "Look for errors in the local server's console to understand and correct the source of the error."),

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
