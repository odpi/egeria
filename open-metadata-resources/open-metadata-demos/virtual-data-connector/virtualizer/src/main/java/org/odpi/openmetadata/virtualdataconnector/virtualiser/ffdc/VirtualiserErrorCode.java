/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc;

import java.text.MessageFormat;

/**
 *
 * The VirtualiserErrorCode is used to define first failure data capture (FFDC) for errors that occur within Virtualiser.
 * It is used in conjunction with all Virtualiser Exceptions, both Checked and Runtime (unchecked).
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


public enum VirtualiserErrorCode {
    NOT_VALID_QUERY(400, "VIRTUALISER-QUERY-400-001",
            "The query is not valid.",
            "Virtualiser is not able to execute the query.",
            "Please check the query."),
    ILLEGAL_ACCESS(400, "VIRTUALISER-INSTANCE-400-002",
            "The access is illegal.",
            "The method does not have access to the definition of the specified class, field, method or constructor.",
            "Please check the use of newInstance()function and offer proper JDBC driver name."),
    ILLEGAL_INSTANCE_CREATION(400, "VIRTUALISER-INSTANCE-400-003",
            "The instance creation is illegal.",
            "The specified class object using the newInstance method in class Class cannot be instantiated.",
            "Please check the use of newInstance."),
    CONNECT_FAIL(401, "VIRTUALISER-GAIAN-401-001",
            "did not get the access to the database.",
            "Virtualiser is not able to connect the database.",
            "Please check the user's valid information."),
    QUERY_EXECUTION_FAIL(401, "VIRTUALISER-GAIAN-401-002",
            "Can not retrieve the data from database",
            "Virtualiser is not able to access data from the database.",
            "Please check the user's valid information."),
    DISCONNECT_FAIL(401, "VIRTUALISER-GAIAN-401-003",
            "Can not close the connection statement and resultset properly",
            "Virtualiser is not able to shut down the connection to the database.",
            "Please the code for shut down the connection."),
    GET_DATA_FAIL(401, "VIRTUALISER-GAIAN-401-004",
            "Virtualiser can not retrieve the data from database",
            "JDBC Connector is not able to access data from the database.",
            "Please check the user's valid information (userId and password) and valid use of Virtualiser."),
    STATEMENT_CREATION_FAIL(401, "VIRTUALISER-GAIAN-401-005",
            "Can not create a statement",
            "Virtualiser is not able to create a statement based on the ConnectionDetails.",
            "Please check the use of createStatement()."),
    UPDATE_EXECUTION_FAIL(401, "VIRTUALISER-GAIAN-401-005",
            "Can not execute the update",
            "Virtualiser is not able to execute the query to update Gaian.",
            "Please check the use of executeUpdate()."),
    SEND_TOPIC_FAIL(500, "VIRTUALISER-KAFKAVIRTUALISERPRODUCER-500-001",
            "KafkaVirtualiserProducer is not able to send the message out.",
            "Virtualiser is not able to notify Information View OMAS.",
            "Please check message format and check the use of KafkaVirtualiserProducer."),
    JACKSON_PARSE_FAIL(500, "VIRTUALISER-JACKSON-500-002",
            "JACKSON is not able to trasfer Java object to json or json to Java object.",
            "Virtualiser is not able to create a proper Jave object or json string.",
            "Please check the use of JACKSON."),
    UNKNOWN_JDBC_DRIVER(500, "VIRTUALISER-JDBCDRIVER-500-003",
            "This JDBC driver is unknown.",
            "Virtualiser is not able to connect to the database without proper driver.",
            "Please check the driver name."),
    SERIALIZATION_EXCEPTION(500, "VIRTUALISER-KAFKAVIRTUALISERCONSUMER-500-004",
            "Serialization is wrong.",
            "Virtualiser is not able to serialize the object properly.",
            "Please check the use of KafkaVirtualiserConsumer."),
    INTERRUPTED_IO_EXCEPTION(500, "VIRTUALISER-KAFKAVIRTUALISERCONSUMER-500-005",
            "There is failed or interrupted I/O operations.",
            "Virtualiser is not able to continue I/O operations.",
            "Please check the use of KafkaVirtualiserConsumer."),
    JACKSON_MAPPING_FAIL(500, "VIRTUALISER-JACKSON-500-006",
            "There is failed or interrupted I/O operations.",
            "Virtualiser is not able to continue I/O operations.",
            "Please check the use of KafkaVirtualiserConsumer."),
    NO_lOGICTABLE(500, "VIRTUALISER-GAIAN-500-006",
            "There is no Logical Table created for table {0} in Gaian Node {1}.",
            "Virtualiser is not able to continue to update Gaian.",
            "Please check the data source and ensure DBA has created proper Logical Table."),
    EMPTY_GAIAN_PROPERTIES(500, "OCFDATABASECONNECTOR-PROPERTIES-500-007",
            "Properties is not set up correctly before connect to Gaian.",
            "Virtualiser is not able to form a jdbc url to connect to the database.",
            "Please check the properties file or your set up for jdbc url.");




    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;
    /**
     * The constructor for ConnectedAssetErrorCode expects to be passed one of the enumeration rows defined in
     * ConnectedAssetErrorCode above.   For example:
     *
     *     OMRSErrorCode   errorCode = OMRSErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    VirtualiserErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
}
