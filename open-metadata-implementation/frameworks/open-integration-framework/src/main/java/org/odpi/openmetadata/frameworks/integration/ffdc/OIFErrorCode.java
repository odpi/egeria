/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The OIFErrorCode is used to define the message content for the OMRS Audit Log.
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
public enum OIFErrorCode implements ExceptionMessageSet
{
    /**
     * OIF-CONNECTOR-400-001 - The integration connector {0} has been configured to have its own thread to issue blocking calls but has not implemented the engage() method
     */
    ENGAGE_IMPLEMENTATION_MISSING(400,"OIF-CONNECTOR-400-001",
                    "The integration connector {0} has been configured to have its own thread to issue blocking calls but has not " +
                                          "implemented the engage() method",
                    "The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  " +
                                          "It called the engage() method on this thread.  However, the default implementation of the " +
                                          "engage() method has been invoked suggesting that either the dedicated thread is not needed or " +
                                          "there is an error in the implementation of the connector.  The integration daemon " +
                                          "will terminate the thread once the engage() method returns.",
                    "If the connector does not need to issue blocking calls update the configuration to remove the need for the " +
                                          "dedicated thread.  Otherwise update the integration connector's implementation to override " +
                                          "the default engage() method implementation."),

    /**
     * OIF-CONNECTOR-400-002 - Catalog target {0} is of type {1} but the {2} connector only supports the following type(s): {3}
     */
    INVALID_CATALOG_TARGET_TYPE(400, "OIF-CONNECTOR-400-002",
                                "Catalog target {0} is of type {1} but the {2} connector only supports the following type(s): {3}",
                                "The connector skips the catalog target.",
                                "The caller has requested a connector work with the wrong type of element.  It should be reconfigured with the correct type of element and rerun."),

    /**
     * OIF-CONNECTOR-400-003 - Catalog target {0} has a connection that is missing property {1} and connector {2} is unable to proceed
     */
    INVALID_CATALOG_TARGET_CONNECTION(400, "OIF-CONNECTOR-400-003",
                                "Catalog target {0} has a connection that is missing property {1} and connector {2} is unable to proceed",
                                "The connector stops processing the catalog target.",
                                "The caller has requested a connector work with a catalog target that has a connection that has missing information.  Correct the set up of the connection."),

    /**
     * OIF-CONNECTOR-400-004 - Catalog target {0} has a connector of type {1} but the {2} connector only supports the following type(s) of connector: {3}
     */
    INVALID_CONNECTOR_TYPE(400, "OIF-CONNECTOR-400-004",
                                "Catalog target {0} has a connector of type {1} but the {2} connector only supports the following type(s) of connector: {3}",
                                "The connector skips the catalog target because it is not able to communicate with the third party technology.",
                                "The caller has requested a connector work with the wrong type of connector to the third party technology.  It should be reconfigured with the correct type of connector and rerun."),


    /**
     * OIF-CONNECTOR-500-001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "OIF-CONNECTOR-500-001",
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to catalog one or more metadata elements.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * OIF-CONNECTOR-500-003 - The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}
     */
    BAD_OM_VALUE(500, "OIF-CONNECTOR-500-003",
                 "The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}",
                 "The connector throws an exception to indicate that it should not continue.",
                 "Check the templates that where used to create the element to be sure they include the correct value.  If the template is correct then check the element's history to discover which processing cleared the value, since this is where the problem was introduced.  Now decide if the test in the connector is incorrect, or the ."),

    /**
     * OIF-CONNECTOR-500-004 - The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}
     */
    BAD_OM_PROPERTY_TYPE(500, "OIF-CONNECTOR-500-004",
                         "The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}",
                         "The connector throws an exception to indicate that it should not continue since something has gone very wrong with the connector or Open Metadata Framework since the connector is expecting an element of a certain type, with bean properties that match that type, but the OMF has returned something different.",
                         "Check that the type of element is that which was expected.  If not, this may be a connector logic problem, or another process may have created additional metadata that has confused the connector. Compare the element retrieved through the OMF with its stored value.  If the stored value is correct, then the problem is in the OMF converters.  If the stored value is not correct then look at the element's history to understand which processing caused the problem."),

    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OIFErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "ErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
