/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The ConformanceSuiteErrorCode is used to define first failure data capture (FFDC) for errors that occur within the
 * Open Metadata Conformance Suite
 * It is used in conjunction with conformance suite exceptions, both Checked and Runtime (unchecked).
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>501 - not implemented </li>
 *         <li>503 - Service not available</li>
 *         <li>400 - invalid parameters</li>
 *         <li>401 - unauthorized</li>
 *         <li>404 - not found</li>
 *         <li>405 - method not allowed</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum ConformanceSuiteErrorCode implements ExceptionMessageSet
{
    /**
     * CONFORMANCE-SUITE-400-001 - OMAG server has been called with a null local server name
     */
    NULL_LOCAL_SERVER_NAME(400, "CONFORMANCE-SUITE-400-001",
            "OMAG server has been called with a null local server name",
            "The system cannot configure the local server.",
            "The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    /**
     * CONFORMANCE-SUITE-400-002 - OMAG server {0} has been called with a null username (userId)
     */
    NULL_USER_ID(400, "CONFORMANCE-SUITE-400-002",
            "OMAG server {0} has been called with a null username (userId)",
            "The system cannot configure the local server.",
            "The user name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    /**
     * CONFORMANCE-SUITE-400-003 - Unable to create a report for a test case with unknown identifier {0}
     */
    UNKNOWN_TEST_CASE_ID(400, "CONFORMANCE-SUITE-400-003",
            "Unable to create a report for a test case with unknown identifier {0}",
            "The system cannot create the report.",
            "Validate the test case identifier with the messages being produced by the audit log  and the conformance suite documentation."),

    /**
     * CONFORMANCE-SUITE-400-004 - Unable to create a report for a workbench with unknown identifier {0}
     */
    UNKNOWN_WORKBENCH_ID(400, "CONFORMANCE-SUITE-400-004",
            "Unable to create a report for a workbench with unknown identifier {0}",
            "The system cannot create the report.",
            "Validate the workbench identifier with the messages being produced by the audit log and the conformance suite documentation."),

    /**
     * CONFORMANCE-SUITE-400-022 - The Egeria Conformance Suite located in OMAG server {0} has been configured with no access to the enterprise repository services
     */
    NO_ENTERPRISE_ACCESS(400, "CONFORMANCE-SUITE-400-022",
            "The Egeria Conformance Suite located in OMAG server {0} has been configured with no access to the enterprise repository services",
            "The system cannot access the connectors to issue metadata requests to the technologies under test.",
            "Change the setting of the enterprise access service to ensure it is enabled."),

    /**
     * CONFORMANCE-SUITE-400-023 - Unable to create a report for a profile with unknown name {0}
     */
    UNKNOWN_PROFILE_NAME(400, "CONFORMANCE-SUITE-400-023",
            "Unable to create a report for a profile with unknown name {0}",
            "The system cannot create the report.",
            "Validate the profile name with the messages being produced by the audit log and the conformance suite documentation."),

    /**
     * CONFORMANCE-SUITE-500-001 - The Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise topic connector
     */
    NO_ENTERPRISE_TOPIC(500, "CONFORMANCE-SUITE-500-001",
            "The Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise topic connector",
            "The conformance suite cannot receive and evaluate events from technologies under test.",
            "This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved."),

    /**
     * CONFORMANCE-SUITE-500-002 - The Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise connector manager
     */
    NO_ENTERPRISE_CONNECTOR_MANAGER(500, "CONFORMANCE-SUITE-500-002",
            "The Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise connector manager",
            "The conformance suite cannot issue metadata requests to the technologies under test.",
            "This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved."),

    /**
     * CONFORMANCE-SUITE-500-003 - The Egeria Conformance Suite testing technology {0} of type {1} has created two test cases with the same id of {2}
     */
    DUPLICATE_TEST_CASE(500, "CONFORMANCE-SUITE-500-003",
            "The Egeria Conformance Suite testing technology {0} of type {1} has created two test cases with the same id of {2}",
            "The conformance suite cannot process one of the test cases.",
            "This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved."),

    /**
     * CONFORMANCE-SUITE-503-003 - The conformance suite service has not been initialized for server {0} and can not support REST API call {1}
     */
    SERVICE_NOT_INITIALIZED(503, "CONFORMANCE-SUITE-503-003",
            "The conformance suite service has not been initialized for server {0} and can not support REST API call {1}",
            "The server has received a call to one of its open metadata conformance suite operations but cannot process it because the conformance suite service is not active.",
            "If the server is supposed to have the conformance suite service activated, correct the server configuration and restart the server."),

    ;


    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ConformanceSuiteErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "ErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
