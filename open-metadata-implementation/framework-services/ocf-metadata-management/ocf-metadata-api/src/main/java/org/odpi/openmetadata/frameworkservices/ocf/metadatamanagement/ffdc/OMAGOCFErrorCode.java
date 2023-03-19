/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The OMAGOCFErrorCode is used to define first failure data capture (FFDC) for errors that occur when
 * working with OCF Beans.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA   Typically the numbers used are:</li>
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
public enum OMAGOCFErrorCode implements ExceptionMessageSet
{
    NULL_CONNECTION_PARAMETER(400, "CONNECTED-ASSET-SERVICES-400-001",
                              "The connection value passed on the {0} parameter of the {1} operation is null",
                              "The system is unable to process the request without this value.",
                              "Correct the code in the caller to provide the name."),

    NO_ASSET_PROPERTIES(400, "CONNECTED-ASSET-SERVICES-400-002",
                        "The request for the properties of asset {0} failed with the following message returned: {1}",
                        "The system is unable to process the request.",
                        "Use the information in the message to understand the nature of the problem and once it is resolved, retry the request."),

    NULL_CLASSIFICATION_NAME(400, "CONNECTED-ASSET-SERVICES-400-003",
                             "Service {0} is unable to process one of the classifications supplied on the {1} call because the classification name is null",
                             "The system is unable to create a new instance because the classification might be important.",
                             "Correct the list of classifications passed with this request."),

    BAD_CLASSIFICATION_PROPERTIES(400, "CONNECTED-ASSET-SERVICES-400-004",
                                  "Service {0} is unable to process the properties supplied with classification {1}.  The associated error message was: {2}",
                                  "The system is unable to create a new instance with invalid properties in any of the classifications.",
                                  "Correct the classification parameters passed with this request."),

    INTEGRATOR_NOT_RETURNED(400, "CONNECTED-ASSET-SERVICES-400-005",
                                  "Service {0} is unable to process the properties supplied to method {1} because the requested owner {2} ({3}) is " +
                                          "not a recognized software server capability.  The associated error message is: {4}",
                                  "The system is unable to create a new instance in the metadata repository with an invalid integrator specified as" +
                                    " the owner.",
                                  "Ensure the request includes the unique identifiers for a valid software server capability entity to represent " +
                                    "the integrator and retry the request."),

    BAD_INTEGRATOR_NAME(400, "CONNECTED-ASSET-SERVICES-400-006",
                        "Service {0} is unable to process the properties supplied to method {1} because the unique name {2} given for the " +
                                "requested owner does not match the unique name of {3} returned in software server capability {4}",
                        "The system is unable to create a new instance with an invalid integrator specified as the owner.",
                        "Retry the request with a matching the unique identifier and name for a valid software server capability entity to " +
                                "represent the owner of the new instance."),

    OMRS_NOT_INITIALIZED(404, "CONNECTED-ASSET-SERVICES-404-001",
                         "The open metadata repository services are not initialized for the {0} operation",
                         "The system is unable to connect to an open metadata repository.",
                         "Check that the server where the Open Connector Framework metadata services are running is initialized correctly.  " +
                                 "Correct any errors discovered and retry the request when the open metadata services are available."),

    NULL_CONNECTOR_RETURNED(500, "CONNECTED-ASSET-SERVICES-500-001",
                            "The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service" +
                                    " running in OMAG Server {2} at {3}",
                            "The system is unable to create a connector which means some of its services will not work.",
                            "This problem is likely to be caused by an incorrect connection object.  Check the settings on the Connection" +
                                    "and correct if necessary.  If the connection is correct, contact the Egeria community for help."),

    WRONG_TYPE_OF_CONNECTOR(500, "CONNECTED-ASSET-SERVICES-500-002",
                            "The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is " +
                                    "not of the required type. It should be an instance of {4}",
                            "The system is unable to create the required connector which means some of its services will not work.",
                            "Verify that the OMAG server is running and the OMAS service is correctly configured."),

    ;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for OMAGOCFErrorCode expects to be passed one of the enumeration rows defined in
     * OMAGOCFErrorCode above.   For example:
     *
     *     OMAGOCFErrorCode   errorCode = OMAGOCFErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OMAGOCFErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
}
