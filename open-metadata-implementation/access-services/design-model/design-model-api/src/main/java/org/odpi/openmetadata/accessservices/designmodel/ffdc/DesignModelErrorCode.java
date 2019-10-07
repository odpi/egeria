/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.designmodel.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The DesignModelErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Design Model OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum DesignModelErrorCode
{
    BAD_OUT_TOPIC_CONNECTION(400, "OMAS-DESIGN-MODEL-400-001 ",
             "The Design Model Open Metadata Access Service (OMAS) has been passed an invalid connection for publishing events.  The connection was {0}.  The resulting exception of {1} included the following message: {2}",
             "The access service has not been passed valid configuration for its out topic connection.",
             "Correct the configuration and restart the service."),
    OMRS_NOT_INITIALIZED(404, "OMAS-DESIGN-MODEL-404-002 ",
                         "The open metadata repository services are not initialized for the {0} operation",
                         "The system is unable to connect to an open metadata repository.",
                         "Check that the server where the Design Model OMAS is running initialized correctly.  " +
                                 "Correct any errors discovered and retry the request when the open metadata services are available."),
    CONNECTION_NOT_FOUND(404, "OMAS-DESIGN-MODEL-404-005 ",
            "The requested connection {0} is not found in OMAS Server {1}, optional error message {2}",
            "The system is unable to populate the requested connection object.",
            "Check that the connection name and the OMAS Server URL are correct.  Retry the request when the connection is available in the OMAS Service"),
    PROXY_CONNECTION_FOUND(404, "OMAS-DESIGN-MODEL-404-006 ",
            "Only an entity proxy for requested connection {0} is found in the open metadata server {1}, error message was: {2}",
            "The system is unable to populate the requested connection object.",
            "Check that the connection name and the OMAS Server URL are correct.  Retry the request when the connection is available in the OMAS Service"),
    ASSET_NOT_FOUND(404, "OMAS-DESIGN-MODEL-404-007 ",
            "A connected asset is not found for connection {0}",
            "The system is unable to populate the connected asset properties because none of the open metadata repositories are returning an asset linked to this connection.",
            "Verify that the OMAS Service running and the connection definition in use is linked to the Asset definition in the metadata repository. Then retry the request."),
    MULTIPLE_ASSETS_FOUND(404, "OMAS-DESIGN-MODEL-404-008 ",
            "Multiple assets are connected to connection {0}",
            "The system is unable to populate the connected asset properties because the open metadata repositories have many links to assets defined for this connection.  The service is unsure which one to use.",
            "Investigate why multiple assets are connected to this connection.  If the related connector is able to serve up many assets then create a virtual asset to cover its collection of assets and link it to the connection. Then link the assets currently linked to this connection to the virtual asset instead. Then retry the request."),
    UNKNOWN_ASSET(404, "OMAS-DESIGN-MODEL-404-009 ",
            "The asset with unique identifier {0} and expected type of {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5}",
            "The system is unable to update information associated with the asset because none of the connected open metadata repositories recognize the asset's unique identifier.",
            "The unique identifier of the asset is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request."),
     ;


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(DesignModelErrorCode.class);


    /**
     * The constructor for DesignModelErrorCode expects to be passed one of the enumeration rows defined in
     * DesignModelErrorCode above.   For example:
     *
     *     DesignModelErrorCode   errorCode = DesignModelErrorCode.ASSET_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    DesignModelErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
     * @param params - strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== DesignModelErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> DesignModelErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "DesignModelErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
