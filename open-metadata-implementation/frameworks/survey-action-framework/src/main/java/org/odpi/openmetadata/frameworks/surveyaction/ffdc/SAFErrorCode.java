/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The SAF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * SAF Discovery Services.  It is used in conjunction with all SAF Exceptions, both Checked and Runtime (unchecked).
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum SAFErrorCode implements ExceptionMessageSet
{
    /**
     * SAF-SURVEY-ACTION-SERVICE-400-001 - No discovery context supplied to the discovery service {0}
     */
    NULL_SURVEY_CONTEXT(400, "SAF-SURVEY-ACTION-SERVICE-400-001",
            "No discovery context supplied to the discovery service {0}",
            "The discovery service is not able to determine which asset to analyze.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery service.  Once the cause is resolved, retry the discovery request."),

    /**
     * SAF-SURVEY-ACTION-SERVICE-400-002 - No embedded discovery services supplied to the discovery pipeline {0}
     */
    NO_EMBEDDED_SURVEY_ACTION_SERVICES(400, "SAF-SURVEY-ACTION-SERVICE-400-002",
            "No embedded discovery services supplied to the discovery pipeline {0}",
            "The discovery pipeline is not able to discovery which discovery services to run.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery pipeline service.  Once the cause is resolved, retry the discovery request."),

    /**
     * SAF-SURVEY-ACTION-SERVICE-400-003 - No embedded discovery services supplied to the discovery pipeline {0}
     */
    INVALID_EMBEDDED_SURVEY_ACTION_SERVICE(400, "SAF-SURVEY-ACTION-SERVICE-400-003",
            "No embedded discovery services supplied to the discovery pipeline {0}",
            "The discovery pipeline is not able to discover which discovery services to run.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery pipeline service or the associated open discovery engine.  Once the cause is resolved, retry the discovery request."),

    /**
     * SAF-SURVEY-ACTION-SERVICE-500-001 - Unexpected exception in discovery service {0} of type {1} detected by method {2}.  The error message was {3}
     */
    UNEXPECTED_EXCEPTION(500, "SAF-SURVEY-ACTION-SERVICE-500-001",
            "Unexpected exception in discovery service {0} of type {1} detected by method {2}.  The error message was {3}",
            "The discovery service failed during its operation.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery service.  Once the cause is resolved, retry the discovery request."),
    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;

    /**
     * The constructor for SAFErrorCode expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    SAFErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "SAFErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}