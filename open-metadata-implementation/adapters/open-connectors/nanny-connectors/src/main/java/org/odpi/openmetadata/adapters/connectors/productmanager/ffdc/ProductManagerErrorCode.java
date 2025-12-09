/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The ProductManagerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
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
public enum ProductManagerErrorCode implements ExceptionMessageSet
{
    /**
     * JACQUARD-HARVESTER-400-001 - Integration connector {0} has been configured without the URL to the database
     */
    NULL_URL(400, "JACQUARD-HARVESTER-400-001",
                     "Integration connector {0} has been configured without the URL to the database",
                     "The connector is move to FAILED status and will not be called by the integration daemon until the configuration error has been corrected.",
                     "The the database URL is configured in the integration connector's connection endpoint in the address property.  Typically it is the host name and port where the database is listening.  The connection is either found in the Integration Daemon's configuration, or, if the Integration Daemon is configured with integration groups, in the open metadata definition of the appropriate integration group."),

    /**
     * JACQUARD-HARVESTER-400-002 - Integration connector {0} has been configured without a secrets connector
     */
    NO_SECRETS(400, "JACQUARD-HARVESTER-400-002",
              "Integration connector {0} has been configured without a secrets connector",
              "The connector is moved to FAILED status and will no longer be called to build open metadata products.",
              "Update the connection information for the connector to include an embedded SecretsConnector configured to point to the secret store/collection for use by the digital products when they are harvesting open metadata.  " +
                      "The connection information is stored in the open metadata ecosystem. By default this is seeded from JacquardHarvesterContentPack.omarchive." +
                      "It is possible to supply the ."),


    /**
     * JACQUARD-HARVESTER-500-001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "JACQUARD-HARVESTER-500-001",
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to catalog one or more metadata elements.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    ;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for ProductManagerErrorCode expects to be passed one of the enumeration rows defined in
     * ProductManagerErrorCode above.   For example:
     * <br><br>
     *     ProductManagerErrorCode   errorCode = ProductManagerErrorCode.ERROR_SENDING_EVENT;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ProductManagerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "ProductManagerErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
