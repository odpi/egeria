/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The BasicFilesIntegrationConnectorsErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Basic File Connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum BasicFilesIntegrationConnectorsErrorCode implements ExceptionMessageSet
{
    FILES_LOCATION_NOT_SPECIFIED(400, "BASIC-FILES-INTEGRATION-CONNECTORS-400-001",
            "The name of the directory (folder) identifying where the files to be catalogued are located is null in the Connection object {0}",
            "The connector is unable to monitor the directory for files because the name of the directory is not passed in the Connection object.",
            "The name of the directory should be set up in the address property of the connection's Endpoint object.  Correct this in the configuration " +
                    "for this connector in the Files Integration integration service configuration which is part of the configuration of the " +
                    "Integration Daemon OMAG server where this connector is running."),
    FILES_LOCATION_NOT_DIRECTORY(400, "BASIC-FILES-INTEGRATION-CONNECTORS-400-002",
            "The file location {0} is not a directory",
            "The connector is unable to work with this location since it is not a directory (folder).",
            "Ensure a valid directory name is passed in the address property in the Endpoint object of the Connection object.  " +
                    "This connection object is part of he Files Integration integration service configuration which is part of the configuration " +
                    "of the Integration Daemon OMAG server where this connector is running."),
    FILES_LOCATION_NOT_READABLE(400, "BASIC-FILES-INTEGRATION-CONNECTORS-400-003",
            "The directory (folder) {0} is not readable",
            "The connector is unable to open the file because it does not have sufficient permission.",
            "Ensure the name of a readable file is passed in the address property in the Endpoint object of the Connection object."),
    UNEXPECTED_EXC_RETRIEVING_FOLDER(400,"BASIC-FILES-INTEGRATION-CONNECTORS-400-004",
            "An unexpected {0} exception was returned to the {1} integration connector by the Files Integrator OMIS {2} " +
                    "method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5}",
            "The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.",
            "Use the message in the nested exception to determine the root cause of the error. Once this is " +
                    "resolved, follow the instructions in the messages produced by the integration daemon to restart the connector."),
    UNEXPECTED_EXC_DATA_FILE_UPDATE(400,"BASIC-FILES-INTEGRATION-CONNECTORS-400-005",
            "An unexpected {0} exception was returned to the {1} integration connector when it tried to update the " +
                    "DataFile in the metadata repositories for file {2}.  The error message was {3}",
            "The exception is logged and the integration connector continues to synchronize metadata.  " +
                    "This file is not catalogued at this time but may succeed later.",
            "Use the message in the unexpected exception to determine the root cause of the error and fix it."),
    FILES_LOCATION_NOT_FOUND(404, "BASIC-FILES-INTEGRATION-CONNECTORS-404-001",
             "The directory named {0} does not exist",
             "The connector is unable to locate the file it has been asked to work with.",
             "Ensure that the name of the file in the address property of the connection's Endpoint object matches the location of the file " +
                           "that the connector is to access."),
    UNEXPECTED_SECURITY_EXCEPTION(500, "BASIC-FILES-INTEGRATION-CONNECTORS-500-001",
             "The connector received an unexpected security exception when reading the file named {0}; the error message was: {1}",
             "The connector is unable to access the file.",
             "Use details from the error message to determine the cause of the error and retry the request once it is resolved."),
    UNEXPECTED_IO_EXCEPTION(500, "BASIC-FILES-INTEGRATION-CONNECTORS-500-002",
             "The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}",
             "The connector is unable to process the file.",
             "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),
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
    BasicFilesIntegrationConnectorsErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
