/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The ReferenceDataErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Reference Data Connectors.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum TabularDataErrorCode implements ExceptionMessageSet
{
    /**
     * REFERENCE-DATA-CONNECTORS-400-001 - Reference Data Connector {0} has been configured without the URL to the OMAG Server Platform
     */
    NULL_URL(400, "REFERENCE-DATA-CONNECTORS-400-001",
                     "Reference Data Connector {0} has been configured without the URL to the OMAG Server Platform",
                     "The connector is unable to contact the OMAG Infrastructure.",
                     "The Platform URL Root is configured in the connector's connection endpoint in the address property.  Typically it is the host name and port where the OMAG Server Platform is running."),


    /**
     * REFERENCE-DATA-CONNECTORS-400-002 - Reference Data Connector {0} has been configured without the name of the OMAG Server to call
     */
    NULL_SERVER_NAME(400, "REFERENCE-DATA-CONNECTORS-400-002",
             "Reference Data Connector {0} has been configured without the name of the OMAG Server to call",
             "The connector is unable to contact the OMAG Server.",
             "The server's name is configured in the connector's connection additionalProperties in the serverName property."),

    /**
     * REFERENCE-DATA-CONNECTORS-400-003 - Reference Data Connector {0} has been configured without a userId needed to call the OMAG Server Platform.
     */
    NULL_CLIENT_USER_ID(400, "REFERENCE-DATA-CONNECTORS-400-003",
                     "Reference Data Connector {0} has been configured without a userId needed to call the OMAG Server Platform.",
                     "The connector is not authorized to contact the OMAG Server Platform.",
                     "The default userId is configured in the connector's connection userId property.  The parent connector may also supply a userId with the setEnvironment() call."),

    /**
     * REFERENCE-DATA-CONNECTORS-400-004 - Reference Data Connector {0} has no data at record {1}.  The data set size is {2}
     */
    NULL_RECORD(400, "REFERENCE-DATA-CONNECTORS-400-004",
                        "Reference Data Connector {0} has no data at record {1}.  The data set size is {2}",
                        "The connector is not able to return data at the requested record number.",
                        "Request data from the connector with record numbers from '0' to the 'data set size minus 1'."),

    /**
     * REFERENCE-DATA-CONNECTORS-400-005 - Reference Data Connector {0} has been passed {1} columns on the {2} method for record number {3}.  The data set width is {4}
     */
    WRONG_NUMBER_OF_COLUMNS(400, "REFERENCE-DATA-CONNECTORS-400-005",
                "Reference Data Connector {0} has been passed {1} columns on the {2} method for record number {3}.  The data set width is {4}",
                "The connector is not able to store data at the requested record number because the caller has not passed the correct number of column values.",
                "The column definitions can be extracted using the getColumnDefinitions().  They show the columns and the types of values that should be stored in them."),


    /**
     * REFERENCE-DATA-CONNECTORS-500-001 - The {0} Reference Data Connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "REFERENCE-DATA-CONNECTORS-500-001",
                         "The {0} Reference Data Connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to continue with the request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * REFERENCE-DATA-CONNECTORS-500-002 - The {0} Reference Data Connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    INVALID_ELEMENT(500, "REFERENCE-DATA-CONNECTORS-500-002",
                         "The {0} Reference Data Connector received an invalid element from the metadata store: {1}",
                         "The connector is unable to continue to work with this element.",
                         "Use the details from the element and any other related error messages to determine the cause of the error and retry the request once this is resolved."),

    /**
     * REFERENCE-DATA-CONNECTORS-500-003 - The {0} Reference Data Connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNMAPPED_COLUMN(500, "REFERENCE-DATA-CONNECTORS-500-003",
                    "The {0} Reference Data Connector is unable to map column {1}",
                    "The connector is unable to continue to work with this element.",
                    "Use the details from the element and any other related error messages to determine the cause of the error and retry the request once this is resolved."),


    /**
     * REFERENCE-DATA-CONNECTORS-500-004 - Product definition is null for connector {0} in method {1}
     */
    NULL_PRODUCT_DEFINITION(500, "REFERENCE-DATA-CONNECTORS-500-004",
                    "Product definition is null for connector {0} in method {1}",
                    "The connector is unable to continue to work without the product definition.",
                    "This is a timing issue.  The connector should set up a valid product definition either in its constructor or start() method.  The calls to retrieve the table name/description should occur after start()."),

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
    TabularDataErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
