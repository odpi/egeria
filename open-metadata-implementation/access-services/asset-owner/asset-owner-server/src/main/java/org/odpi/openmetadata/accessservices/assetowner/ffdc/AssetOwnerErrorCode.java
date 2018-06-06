/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetowner.ffdc;

import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The AssetConsumerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Asset Consumer OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
 *     <li>UserAction - describes how a AssetConsumerInterface should correct the error</li>
 * </ul>
 */
public enum AssetOwnerErrorCode
{
    SERVER_URL_NOT_SPECIFIED(400, "OMAS-ASSETCONSUMER-400-001",
            "The OMAS Server URL is null",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the AssetConsumer when it is created."),
    SERVER_URL_MALFORMED(400, "OMAS-ASSETCONSUMER-400-002",
            "The OMAS Server URL {0} is not in a recognized format",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the AssetConsumer when it is created."),
    SERVER_NOT_AVAILABLE(404, "OMAS-ASSETCONSUMER-404-001",
            "The OMAS Server {0} is not available",
            "The system is unable to connect to the OMAS Server.",
            "Check that the OMAS Server URL is correct and the OMAS Service is running.  Retry the request when the OMAS Service is available."),
    CONNECTION_NOT_FOUND(404, "OMAS-ASSETCONSUMER-404-002",
            "The requested connection {0} is not found in OMAS Server {2}",
            "The system is unable to populate the requested connection object.",
            "Check that the connection name and the OMAS Server URL is correct.  Retry the request when the connection is available in the OMAS Service"),
    ASSET_NOT_FOUND(404, "OMAS-ASSETCONSUMER-404-003",
            "The requested asset {0} is not found for connection {1}",
            "The system is unable to populate the asset properties object because the OMAS Server is not returning the asset's properties.",
            "Verify that the OMAS Service running and the connection definition in use is linked to the Asset definition in the metadata repository. Then retry the request."),
    NULL_CONNECTION_RETURNED(500, "OMAS-ASSETCONSUMER-500-001",
            "The requested connection named {0} is not returned by the OMAS Server {1}",
            "The system is unable to create a connector because the OMAS Server is not returning the Connection properties.",
            "Verify that the OMAS server running and the connection definition is correctly configured."),
    NULL_CONNECTOR_RETURNED(500, "OMAS-ASSETCONSUMER-500-002",
            "The requested connector for connection named {0} is not returned by the OMAS Server {1}",
            "The system is unable to create a connector.",
            "Verify that the OMAS server is running and the connection definition is correctly configured.")
    ;
    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = Logger.getLogger(AssetOwnerErrorCode.class);


    /**
     * The constructor for AssetConsumerErrorCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerErrorCode above.   For example:
     *
     *     AssetConsumerErrorCode   errorCode = AssetConsumerErrorCode.ASSET_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    AssetOwnerErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
     * @param params   strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== OCFErrorCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> OCFErrorCode.getMessage(%s): %s", Arrays.toString(params), result));
        }

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
}
