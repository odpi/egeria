/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.datafolder.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The DataFolderConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Data Folder Connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum DataFolderConnectorErrorCode implements ExceptionMessageSet
{
    /**
     * DATA-FOLDER-CONNECTOR-400-001 - The folder name is null in the Connection object {0}
     */
    FOLDER_NOT_SPECIFIED(400, "DATA-FOLDER-CONNECTOR-400-001",
            "The folder name is null in the Connection object {0}",
            "The connector is unable to open the folder because the name of the folder is not passed in the Connection object.",
            "The name of the folder should be set up in the address property of the connection's Endpoint object."),

    /**
     * DATA-FOLDER-CONNECTOR-400-002 - The folder {0} given in Connection object {1} is a file
     */
    FILE_NOT_DIRECTORY(400, "DATA-FOLDER-CONNECTOR-400-002",
            "The folder {0} given in Connection object {1} is a file",
            "The connector is unable to work with a file.",
            "Ensure a valid folder name is passed in the address property in the Endpoint object of the Connection object."),

    /**
     * DATA-FOLDER-CONNECTOR-400-003 - The folder {0} given in Connection object {1} is not readable
     */
    FOLDER_NOT_READABLE(400, "DATA-FOLDER-CONNECTOR-400-003",
            "The folder {0} given in Connection object {1} is not readable",
            "The connector is unable to open the folder because it does not have permission to read the file.",
            "Ensure a readable folder name is passed in the address property in the Endpoint object of the Connection object."),

    /**
     * DATA-FOLDER-CONNECTOR-404-001 - The folder named {0} in the Connection object {1} does not exist
     */
    FOLDER_NOT_FOUND(404, "DATA-FOLDER-CONNECTOR-404-001",
             "The folder named {0} in the Connection object {1} does not exist",
             "The connector is unable to open the folder because it does not exist.",
             "Add the name of an existing folder to the address property of the connection's Endpoint object."),

    /**
     * DATA-FOLDER-CONNECTOR-500-001 - The connector received an unexpected security exception when reading the file named {0}; the error message was: {1}
     */
    UNEXPECTED_SECURITY_EXCEPTION(500, "DATA-FOLDER-CONNECTOR-500-001",
             "The connector received an unexpected security exception when reading the file named {0}; the error message was: {1}",
             "The connector is unable to process the folder.",
             "Use details from the error message to determine the cause of the security exception and retry the request once it is resolved."),

    /**
     * DATA-FOLDER-CONNECTOR-500-002 - The connector received an unexpected IO exception when reading the folder named {0}; the error message was: {1}
     */
    UNEXPECTED_IO_EXCEPTION(500, "DATA-FOLDER-CONNECTOR-500-002",
             "The connector received an unexpected IO exception when reading the folder named {0}; the error message was: {1}",
             "The connector is unable to process the data in the folder.",
             "Use details from the error message to determine the cause of the error and retry the request once it is resolved.");

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
    DataFolderConnectorErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
