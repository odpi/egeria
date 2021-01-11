/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The BasicFileConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Basic File Connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum BasicFileConnectorErrorCode implements ExceptionMessageSet
{
    FILE_NOT_SPECIFIED(400, "BASIC-FILE-CONNECTOR-400-001",
            "The file name is null in the Connection object {0}",
            "The connector is unable to open the file because the name of the file is not passed in the Connection object.",
            "The name of the file should be set up in the address property of the connection's Endpoint object."),
    DIRECTORY_SPECIFIED(400, "BASIC-FILE-CONNECTOR-400-002",
            "The file {0} given in Connection object {1} is a directory",
            "The connector is unable to work with a directory.",
            "Ensure a valid file name is passed in the address property in the Endpoint object of the Connection object."),
    FILE_NOT_READABLE(400, "BASIC-FILE-CONNECTOR-400-003",
            "The file {0} given in Connection object {1} is not readable",
            "The connector is unable to open the file because it does not have sufficient permission.",
            "Ensure the name of a readable file is passed in the address property in the Endpoint object of the Connection object."),
    FILE_NOT_FOUND(404, "BASIC-FILE-CONNECTOR-404-001",
             "The file named {0} in the Connection object {1} does not exist",
             "The connector is unable to locate the file it has been asked to work with.",
             "Ensure that the name of the file in the address property of the connection's Endpoint object matches the location of the file " +
                           "that the connector is to access."),
    UNEXPECTED_SECURITY_EXCEPTION(500, "BASIC-FILE-CONNECTOR-500-001",
             "The connector received an unexpected security exception when reading the file named {0}; the error message was: {1}",
             "The connector is unable to access the file.",
             "Use details from the error message to determine the cause of the error and retry the request once it is resolved."),
    UNEXPECTED_IO_EXCEPTION(500, "BASIC-FILE-CONNECTOR-500-002",
             "The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}",
             "The connector is unable to process the file.",
             "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.");


    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for BasicFileConnectorErrorCode expects to be passed one of the enumeration rows defined in
     * BasicFileConnectorErrorCode above.   For example:
     *
     *     BasicFileConnectorErrorCode   errorCode = BasicFileConnectorErrorCode.ERROR_SENDING_EVENT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    BasicFileConnectorErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "BasicFileConnectorErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
