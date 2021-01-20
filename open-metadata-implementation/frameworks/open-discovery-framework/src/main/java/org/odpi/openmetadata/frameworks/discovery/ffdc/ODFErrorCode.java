/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The ODF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * ODF Discovery Services.  It is used in conjunction with all ODF Exceptions, both Checked and Runtime (unchecked).
 *
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
public enum ODFErrorCode implements ExceptionMessageSet
{
    NULL_DISCOVERY_CONTEXT(400, "ODF-DISCOVERY-SERVICE-400-001 ",
            "No discovery context supplied to the discovery service {0}",
            "The discovery service is not able to determine which asset to analyze.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery service.  Once the cause is resolved, retry the discovery request."),
    NO_EMBEDDED_DISCOVERY_SERVICES(400, "ODF-DISCOVERY-SERVICE-400-002 ",
            "No embedded discovery services supplied to the discovery pipeline {0}",
            "The discovery pipeline is not able to discovery which discovery services to run.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery service.  Once the cause is resolved, retry the discovery request."),
    INVALID_EMBEDDED_DISCOVERY_SERVICE(400, "ODF-DISCOVERY-SERVICE-400-003 ",
            "No embedded discovery services supplied to the discovery pipeline {0}",
            "The discovery pipeline is not able to discovery which discovery services to run.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery service.  Once the cause is resolved, retry the discovery request."),
    INVALID_DISCOVERY_SERVICE_CONNECTION(400, "ODF-DISCOVERY-SERVICE-400-004 ",
            "The discovery engine {0} is not able to create the discovery service for asset type {1} and is therefore not able to discover asset {2}.  Error message was {3}.  The connection was {4}",
            "The discovery engine is not able to create a discovery service to analyze an asset because the connection information associated with the discovery service is not valid.",
            "The connection is stored with the discovery service definition in the open metadata repository used by the discovery engine.  Use the error message to correct the connection properties.  Once the connection is corrected is resolved, retry the discovery request."),
    INVALID_DISCOVERY_SERVICE_CONNECTOR(400, "ODF-DISCOVERY-SERVICE-400-005 ",
            "Invalid discovery service for asset type {0}.  Discovery engine {1} is not able to analyze asset {2}",
            "The discovery service is not functioning correctly.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery service.  Once the cause is resolved, retry the discovery request."),
    INVALID_ASSET_CONNECTION(400, "ODF-DISCOVERY-SERVICE-400-006 ",
            "The connection for asset {0} is no valid.  Error message was {1}.  The connection was {2}",
            "The discovery engine is not able to create a connector to the asset to allow a discovery service to access its contents.",
            "The connection is stored with the asset definition in the open metadata repository used by the discovery engine.  Use the error message to correct the connection properties.  Once the connection is corrected is resolved, retry the discovery request."),
    INVALID_ASSET_CONNECTOR(400, "ODF-DISCOVERY-SERVICE-400-007 ",
            "Invalid connector for asset {0}.  Error message was {1}.  The connection was {2}",
            "The discovery service is not able to analyze the asset.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the connector.  Once the cause is resolved, retry the discovery request."),

    UNEXPECTED_EXCEPTION(500, "ODF-DISCOVERY-SERVICE-500-001 ",
            "Unexpected exception in discovery service {0} of type {1} detected by method {2}.  The error message was {3}",
            "The discovery service failed during its operation.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery service.  Once the cause is resolved, retry the discovery request.");
    ;

    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for ODFErrorCode expects to be passed one of the enumeration rows defined in
     * ODFErrorCode above.   For example:
     *
     *     ODFErrorCode   errorCode = ODFErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ODFErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "ODFErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}