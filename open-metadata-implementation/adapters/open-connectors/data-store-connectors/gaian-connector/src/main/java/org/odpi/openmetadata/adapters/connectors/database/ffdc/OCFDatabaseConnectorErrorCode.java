/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.adapters.connectors.database.ffdc;

import java.text.MessageFormat;

/**
 *
 * The OCFDatabaseConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur within the JDBC Connector
 * It is used in conjunction with all OCF Database Connector Exceptions, both Checked and Runtime (unchecked).
 *
 * The 3 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>401 - unauthorized or unauthenticated</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */


public enum OCFDatabaseConnectorErrorCode
{
    NOT_VALID_QUERY(400, "OCF-DATABASE-CONNECTOR-QUERY-400-001 ",
            "In {0}, the query is not valid.",
            "OCF Database Connector is not able to execute the query.",
            "Please check the query."),
    ILLEGAL_ACCESS(400, "OCF-DATABASE-CONNECTOR-INSTANCE-400-002 ",
            "{0} is not have legal access to the code it needs",
            "The method does not have access to the definition of the specified class, field, method or constructor.",
            "Please check the use of newInstance."),
    ILLEGAL_INSTANCE_CREATION(400, "OCF-DATABASE-CONNECTOR-INSTANCE-400-003 ",
            "In {0}, the instance creation is illegal.",
            "The specified class object using the newInstance method in class Class cannot be instantiated.",
            "Please check the use of newInstance."),
    CONNECT_FAIL(401, "OCF-DATABASE-CONNECTOR-GAIAN-401-001 ",
            "{0} does not get the access to the database.",
            "OCF Database Connector is not able to connect the database.",
            "Please check the user id."),
    QUERY_EXECUTION_FAIL(401, "OCF-DATABASE-CONNECTOR-GAIAN-401-002 ",
            "{0} can not retrieve the data from database",
            "OCF Database Connector is not able to access data from the database.",
            "Please check the query and the connection to Gaian."),
    DISCONNECT_FAIL(401, "OCF-DATABASE-CONNECTOR-GAIAN-401-003 ",
            "{0} can not close the connection, statement and result set properly",
            "OCF Database Connector is not able to shut down the connection to the database.",
            "Please check the code for shutting down the connection."),
    UNKNOWN_JDBC_DRIVER(500, "OCF-DATABASE-CONNECTOR-JDBC-DRIVER-500-001 ",
            "In {0}, this JDBC driver is unknown.",
            "OCF Database Connector is not able to connect to the database without proper driver.",
            "Please check the driver name.");



    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;


    /**
     * The constructor for OCFDatabaseConnectorErrorCode expects to be passed one of the enumeration rows defined in
     * OCFDatabaseConnectorErrorCode above.   For example:
     *
     *     OMRSErrorCode   errorCode = OCFDatabaseConnectorErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    OCFDatabaseConnectorErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

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
        return "OCFDatabaseConnectorErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
