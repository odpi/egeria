/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.base.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The DesignModelArchiveErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMRS
 * It is used in conjunction with all OMRS Exceptions, both Checked and Runtime (unchecked).
 *
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
public enum DesignModelArchiveErrorCode
{
    ARCHIVE_UNAVAILABLE(500, "OMRS-DESIGN-MODEL-ARCHIVE-500-001 ",
            "The archive builder failed to return an archive.",
            "There is an internal error in the archive building process.",
            "Raise a Github issue to get this fixed.")

    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(
            DesignModelArchiveErrorCode.class);


    /**
     * The constructor for DesignModelArchiveErrorCode expects to be passed one of the enumeration rows defined in
     * DesignModelArchiveErrorCode above.   For example:
     *
     *     DesignModelArchiveErrorCode   errorCode = DesignModelArchiveErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  error code to use over REST calls
     * @param newErrorMessageId  unique Id for the message
     * @param newErrorMessage  text for the message
     * @param newSystemAction  description of the action taken by the system when the error condition happened
     * @param newUserAction  instructions for resolving the error
     */
    DesignModelArchiveErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
    {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }


    public int getHTTPErrorCode()
    {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId()
    {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params  strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== DesignModelArchiveErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> DesignModelArchiveErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction()
    {
        return userAction;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "DesignModelArchiveErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
