/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The AssetConsumerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Asset Consumer OMAS.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum AssetConsumerErrorCode implements ExceptionMessageSet
{
    BAD_OUT_TOPIC_CONNECTION(400, "OMAS-ASSET-CONSUMER-400-001",
             "The Asset Consumer Open Metadata Access Service (OMAS) has been passed an invalid connection for publishing events.  The connection was {0}.  The resulting exception of {1} included the following message: {2}",
             "The access service has not been passed valid configuration for its out topic connection.",
             "Correct the server configuration and restart the server."),
    OMRS_NOT_INITIALIZED(404, "OMAS-ASSET-CONSUMER-404-002",
             "The open metadata repository services are not initialized for the {0} operation",
             "The system is unable to connect to an open metadata repository.",
             "Check that the server where the Asset Consumer OMAS is running initialized correctly.  " +
                                 "Correct any errors discovered and retry the request when the open metadata services are available."),
    CONNECTION_NOT_FOUND(404, "OMAS-ASSET-CONSUMER-404-005",
            "The requested connection {0} is not found in OMAG Server {1}, optional error message {2}",
            "The system is unable to populate the requested connection object because is is not found in the cohort.",
            "Check that the connection name is correct and the caller is connecting to the correct server.  Retry the request when the connection " +
                                 "is available in the cohort."),
    PROXY_CONNECTION_FOUND(404, "OMAS-ASSET-CONSUMER-404-006",
            "Only an entity proxy for requested connection {0} is found in the open metadata server {1}, error message was: {2}",
            "The system is unable to populate the requested connection object because the object stored is only a stub from a relationship.",
            "Check that the connection name is correct.  Check that all the servers in the cohort are running. Retry the request when the " +
                                   "connection is available in the cohort."),
    ASSET_NOT_FOUND(404, "OMAS-ASSET-CONSUMER-404-007",
            "A connected asset is not found for connection {0}",
            "The system is unable to populate the connected asset properties because none of the open metadata repositories are returning an asset linked to this connection.",
            "Verify that the OMAS Service is running and the connection definition in use is linked to the Asset definition in one of the metadata " +
                            "repositories. Then retry the request."),
    MULTIPLE_ASSETS_FOUND(404, "OMAS-ASSET-CONSUMER-404-008",
            "Multiple assets are connected to connection {0}",
            "The system is unable to populate the connected asset properties because the open metadata repositories have many links to assets defined for this connection.  The service is unsure which one to use.",
            "Investigate why multiple assets are connected to this connection.  If the related connector is able to serve up many assets then create a virtual asset to cover its collection of assets and link it to the connection. Then link the assets currently linked to this connection to the virtual asset instead. Then retry the request."),
    UNKNOWN_ASSET(404, "OMAS-ASSET-CONSUMER-404-009",
            "The asset with unique identifier {0} and expected type of {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5}",
            "The system is unable to update information associated with the asset because none of the connected open metadata repositories recognize the asset's unique identifier.",
            "The unique identifier of the asset is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request."),

    NULL_LISTENER(400, "OMAS-ASSET-CONSUMER-400-017",
            "A null topic listener has been passed by user {0} on method {1}",
            "There is a coding error in the caller to the Asset Consumer OMAS.",
            "Correct the caller logic and retry the request."),

    UNABLE_TO_SEND_EVENT(500, "OMAS-ASSET-CONSUMER-500-004",
            "An unexpected exception occurred when sending an event through connector {0} to the Asset Consumer OMAS out topic.  The failing " +
                                 "event was {1}, the exception was {2} with message {3}",
            "The access service has issued a call to publish an event on its Out Topic and it failed.",
            "Look for errors in the event bus to understand why this is failing.  When the event bus is operating correctly, event will" +
                                 " begin to be published again.  In the meantime, events are being lost."),

    NULL_CONNECTOR_RETURNED(500, "OMAS-ASSET-CONSUMER-500-006",
           "The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service" +
                                    " running in OMAG Server {2} at {3}",
           "The system is unable to create a connector which means some of its services will not work.",
           "This problem is likely to be caused by an incorrect connection object.  Check the settings on the Connection" +
                                    "and correct if necessary.  If the connection is correct, contact the Egeria community for help."),

    WRONG_TYPE_OF_CONNECTOR(500, "OMAS-ASSET-CONSUMER-500-007",
           "The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is " +
                                    "not of the required type. It should be an instance of {4}",
           "The system is unable to create the required connector which means some of its services will not work.",
           "Verify that the OMAG server is running and the OMAS service is correctly configured."),

    ;

    private static final long    serialVersionUID = 1L;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for AssetConsumerErrorCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerErrorCode above.   For example:
     *
     *     AssetConsumerErrorCode   errorCode = AssetConsumerErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    AssetConsumerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "AssetConsumerErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
