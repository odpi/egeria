/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The FileBasedOpenMetadataArchiveStoreConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur within the
 * FileBasedOpenMetadataArchiveStoreConnector.
 * It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA. Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500: internal error</li>
 *         <li>501: not implemented </li>
 *         <li>503: Service not available</li>
 *         <li>400: invalid parameters</li>
 *         <li>401: unauthorized</li>
 *         <li>404: not found</li>
 *         <li>405: method not allowed</li>
 *         <li>409: data conflict errors, for example an item is already defined</li>
 *     </ul></li>
 *     <li>Error Message Id: to uniquely identify the message</li>
 *     <li>Error Message Text: includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction: describes the result of the error</li>
 *     <li>UserAction: describes how a user should correct the error</li>
 * </ul>
 */
public enum FileBasedOpenMetadataArchiveStoreConnectorErrorCode implements ExceptionMessageSet
{
    /**
     * FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-002 - Unable to open file {0}.  Message from {1} exception was {2}
     */
    BAD_FILE(400, "FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-002",
             "Unable to open file {0}.  Message from {1} exception was {2}",
             "The server is is unable to open an open metadata archive store.",
             "Use the information from the exception to determine the cause of the error.  For example, is the filename correct?  " +
                     "Does this runtime have permission to access the file?  Once the cause of the error is corrected, restart the caller."),
    ;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for FileBasedOpenMetadataArchiveStoreConnectorErrorCode expects to be passed one of the enumeration rows defined in
     * FileBasedOpenMetadataArchiveStoreConnectorErrorCode above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    FileBasedOpenMetadataArchiveStoreConnectorErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "FileBasedOpenMetadataArchiveStoreConnectorErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
