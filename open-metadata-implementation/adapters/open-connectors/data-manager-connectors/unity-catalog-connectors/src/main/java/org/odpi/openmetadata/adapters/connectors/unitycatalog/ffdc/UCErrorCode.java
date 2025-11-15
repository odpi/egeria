/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The UCErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
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
public enum UCErrorCode implements ExceptionMessageSet
{
    /**
     * UNITY-CATALOG-CONNECTOR-400-001 - Connection {0} has been configured without the URL to the Unity Catalog (UC)
     */
    NULL_URL(400, "UNITY-CATALOG-CONNECTOR-400-001",
                     "Connection {0} has been configured without the URL to the Unity Catalog (UC)",
                     "The connector is unable to start because the endpoint of its connection has a null address property.",
                     "Update the connection's endpoint to include the connection string needed to connect to the desired database."),

    /**
     * UNITY-CATALOG-CONNECTOR-400-002 - The {0} Unity Catalog Connector has not been supplied with a {1} property value
     */
    MISSING_PROPERTY_NAME(400, "UNITY-CATALOG-CONNECTOR-400-002",
                         "The {0} Unity Catalog Connector has not been supplied with a {1} property value",
                         "The connector is unable to continue because it is not sure which elements to work on.",
                         "Add this property to either the connector's configuration properties (or if it is a governance service, to the request parameters) and retry the request."),


    /**
     * UNITY-CATALOG-CONNECTOR-500-001 - The {0} Unity Catalog connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "UNITY-CATALOG-CONNECTOR-500-001",
                         "The {0} Unity Catalog (UC) connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * UNITY-CATALOG-CONNECTOR-500-002 - The {0} Unity Catalog (UC) governance service has detected an invalid technology type in code that runs after the technology type has been validated
     */
    LOGIC_ERROR(500, "UNITY-CATALOG-CONNECTOR-500-002",
                "The {0} Unity Catalog (UC) governance service has detected an invalid technology type in code that runs after the technology type has been validated",
                "The connector ends with an exception.",
                "The code in the service needs to be fixed to ensure the list of valid unity catalog resources is consistent throughout."),

    /**
     * UNITY-CATALOG-CONNECTOR-500-003 - The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}
     */
    BAD_OM_VALUE(500, "UNITY-CATALOG-CONNECTOR-500-003",
                 "The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}",
                 "The connector throws an exception to indicate that it should not continue.",
                 "Check the templates that where used to create the element to be sure they include the correct value.  If the template is correct then check the element's history to discover which processing cleared the value, since this is where the problem was introduced.  Now decide if the test in the connector is incorrect, or the ."),

    /**
     * UNITY-CATALOG-CONNECTOR-500-004 - The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}
     */
    BAD_OM_PROPERTY_TYPE(500, "UNITY-CATALOG-CONNECTOR-500-004",
                         "The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}",
                         "The connector throws an exception to indicate that it should not continue since something has gone very wrong with the connector or Open Metadata Framework since the connector is expecting an element of a certain type, with bean properties that match that type, but the OMF has returned something different.",
                         "Check that the type of element is that which was expected.  If not, this may be a connector logic problem, or another process may have created additional metadata that has confused the connector. Compare the element retrieved through the OMF with its stored value.  If the stored value is correct, then the problem is in the OMF converters.  If the stored value is not correct then look at the element's history to understand which processing caused the problem."),


    /**
     * UNITY-CATALOG-CONNECTOR-503-001 - A client-side exception {0} was received from API call {1} to URL {2}.  The error message was {3}
     */
    CLIENT_SIDE_REST_API_ERROR(503, "UNITY-CATALOG-CONNECTOR-503-001",
                               "A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3}",
                               "The connector has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                               "Look for errors in the local server's console to understand and correct the source of the error."),
    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * Constructor
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    UCErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return new ExceptionMessageDefinition(httpErrorCode,
                                              errorMessageId,
                                              errorMessage,
                                              systemAction,
                                              userAction);
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
        ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                                      errorMessageId,
                                                                                      errorMessage,
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
        return "UCErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
