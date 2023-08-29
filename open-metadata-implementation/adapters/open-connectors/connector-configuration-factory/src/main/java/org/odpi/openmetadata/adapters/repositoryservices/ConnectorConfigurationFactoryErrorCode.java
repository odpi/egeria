/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The ConnectorConfigurationFactoryErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the ConnectorConfigurationFactory.  It is used in conjunction with all Exceptions, both Checked and Runtime (unchecked).
 * <br><br>
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum ConnectorConfigurationFactoryErrorCode implements ExceptionMessageSet
{
    /**
     * CONNECTOR-CONFIGURATION-FACTORY-400-001 Invalid Connector Provider class {0}; class loader exception was {1} with message {2}
     */
    INVALID_CONNECTOR_PROVIDER(400, "CONNECTOR-CONFIGURATION-FACTORY-400-001 ",
            "Invalid Connector Provider class {0}; class loader exception was {1} with message {2}",
            "The system is unable to create the requested connector type because the Connector Provider's class is failing to initialize in the JVM" +
                                       ".  This has resulted in an exception in the class loader.",
            "Update the configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of " +
                                       "the connection's connectorType. Then retry the request.");


    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for ConnectorConfigurationFactoryErrorCode expects to be passed one of the enumeration rows defined in
     * ConnectorConfigurationFactoryErrorCode above.   For example:
     * <br><br>
     *     ConnectorConfigurationFactoryErrorCode   errorCode = ConnectorConfigurationFactoryErrorCode.ERROR_SENDING_EVENT;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ConnectorConfigurationFactoryErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "ConnectorConfigurationFactoryErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}