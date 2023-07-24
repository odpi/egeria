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
    NULL_LOCAL_SERVER_NAME(400, "CONFORMANCE-SUITE-400-001 ",
            "OMAG server has been called with a null local server name",
            "The system is unable to configure the local server.",
            "The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_USER_ID(400, "CONFORMANCE-SUITE-400-002 ",
            "OMAG server {0} has been called with a null user name (userId)",
            "The system is unable to configure the local server.",
            "The user name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    UNKNOWN_TEST_CASE_ID(400, "CONFORMANCE-SUITE-400-003 ",
            "Unable to create a report for a test case with unknown identifier {0}",
            "The system is unable to create the report.",
            "Validate the test case identifier with the messages being produced by the audit log  and the conformance suite documentation."),

    UNKNOWN_WORKBENCH_ID(400, "CONFORMANCE-SUITE-400-004 ",
            "Unable to create a report for a workbench with unknown identifier {0}",
            "The system is unable to create the report.",
            "Validate the workbench identifier with the messages being produced by the audit log and the conformance suite documentation."),

    BAD_CLIENT_CONFIG(400, "CONFORMANCE-SUITE-400-006 ",
            "OMAG server {0} has been configured with an invalid connection for the REST client; error message was {1}",
            "The system is unable to issue any REST Calls.",
            "Correct the configuration and restart the server."),

    LOCAL_REPOSITORY_MODE_NOT_SET(400, "CONFORMANCE-SUITE-400-007 ",
            "The local repository mode has not been set for OMAG server {0}",
            "The local repository mode must be enabled before the event mapper connection or repository proxy connection is set.  The system is unable to configure the local server.",
            "The local repository mode is supplied by the caller to the OMAG server. This call to enable the local repository needs to be made before the call to set the event mapper connection or repository proxy connection."),

    NULL_SERVER_CONFIG(400, "CONFORMANCE-SUITE-400-008 ",
            "The OMAG server {0} has been passed null configuration.",
            "The system is unable to initialize the local server instance.",
            "Retry the request with server configuration."),

    NULL_REPOSITORY_CONFIG(400, "CONFORMANCE-SUITE-400-009 ",
            "The OMAG server {0} has been passed a configuration document with no open metadata repository services configuration",
            "The system is unable to initialize the local server instance.",
            "Use the administration services to add the repository services configuration."),

    NULL_ACCESS_SERVICE_ADMIN_CLASS(400, "CONFORMANCE-SUITE-400-010 ",
            "The OMAG server {0} has been passed a null admin services class name for access service {1}",
            "The system is unable to initialize this access service.",
            "if the access service should be initialized then set up the appropriate admin services class name and restart the server instance."),

    BAD_ACCESS_SERVICE_ADMIN_CLASS(400, "CONFORMANCE-SUITE-400-011 ",
            "The OMAG server {0} has been passed an invalid admin services class name {1} for access service {2}",
            "The system is unable to initialize this access service.",
            "If the access service should be initialized then set up the appropriate admin services class name and restart the server instance."),

    BAD_CONFIG_FILE(400, "CONFORMANCE-SUITE-400-012 ",
            "The OMAG server {0} is not able to open its configuration file {1} due to the following error: {2}",
            "The system is unable to initialize the server.",
            "Review the error message to determine the cause of the problem."),

    BAD_MAX_PAGE_SIZE(400, "CONFORMANCE-SUITE-400-013 ",
            "The OMAG server {0} has been passed an invalid maximum page size of {1}",
            "The system has ignored this value.",
            "The maximum page size must be a number greater than zero.  Retry the request with a valid value."),

    ENTERPRISE_TOPIC_START_FAILED(400, "CONFORMANCE-SUITE-400-014 ",
            "The OMAG server {0} is unable to start the enterprise OMRS topic connector, error message was {1}",
            "The open metadata access services will not be able to receive events from the connected repositories.",
            "Review the error messages and once the source of the problem is resolved, restart the server and retry the request."),

    TOO_LATE_TO_SET_EVENT_BUS(400, "CONFORMANCE-SUITE-400-015 ",
            "The OMAG server {0} is unable to set up new event bus configuration because other services are already configured",
            "It is not possible to change the event bus configuration for this server while there are other open metadata services configured.",
            "Remove any configuration for this server's cohorts, local repository and access services, " +
                                      "and retry the request to add the event bus configuration.  " +
                                      "Then it is possible to add the configuration for the other services back " +
                                      "into the configuration document."),

    NO_EVENT_BUS_SET(400, "CONFORMANCE-SUITE-400-016 ",
            "The OMAG server {0} is unable to add open metadata services until the event bus is configured",
            "No change has occurred in this server's configuration document.",
            "Add the event bus configuration using the administration services and retry the request."),

    NULL_METADATA_COLLECTION_NAME(400, "CONFORMANCE-SUITE-400-017 ",
            "OMAG server {0} has been called with a null metadata collection name",
            "The system is unable to add this metadata collection name to the configuration document for the local server.",
            "The metadata collection name is optional.  If it is not set up then the local server name is used instead."),

    EMPTY_CONFIGURATION(400, "CONFORMANCE-SUITE-400-018 ",
            "OMAG server {0} has been called with a configuration document that has no services configured",
            "The requested server provides no function.",
            "Use the administration services to add configuration for OMAG services to the server's configuration document."),

    NULL_ACCESS_SERVICE_ROOT_URL(400, "CONFORMANCE-SUITE-400-019 ",
            "The {0} service of OMAG server {1} has been configured with a null root URL for the {2} access service",
            "The system is unable to accept this value in the configuration properties.",
            "The root URL is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_ACCESS_SERVICE_SERVER_NAME(400, "CONFORMANCE-SUITE-400-020 ",
            "OMAG server {0} has been configured with a null cohort name",
            "The system is unable to accept this value in the configuration properties.",
            "The server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_FILE_NAME(400, "CONFORMANCE-SUITE-400-021 ",
            "OMAG server {0} has been configured with a null file name for an Open Metadata Archive",
            "The system is unable to configure the local server to load this Open Metadata Archive file.",
            "The file name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can load the open metadata archive."),

    NO_ENTERPRISE_ACCESS(400, "CONFORMANCE-SUITE-400-022 ",
            "The ODPi Egeria Conformance Suite located in OMAG server {0} has been configured with no access to the enterprise repository services",
            "The system is unable to access the connectors to issue metadata requests to the technologies under test.",
            "Change the setting of the enterprise access service to ensure it is enabled."),

    UNKNOWN_PROFILE_NAME(400, "CONFORMANCE-SUITE-400-023 ",
            "Unable to create a report for a profile with unknown name {0}",
            "The system is unable to create the report.",
            "Validate the profile name with the messages being produced by the audit log and the conformance suite documentation."),

    NO_ENTERPRISE_TOPIC(500, "CONFORMANCE-SUITE-500-001 ",
            "The ODPi Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise topic connector",
            "The conformance suite is unable to receive and evaluate events from technologies under test.",
            "This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved."),

    NO_ENTERPRISE_CONNECTOR_MANAGER(500, "CONFORMANCE-SUITE-500-002 ",
            "The ODPi Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise connector manager",
            "The conformance suite is unable to issue metadata requests to the technologies under test.",
            "This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved."),

    DUPLICATE_TEST_CASE(500, "CONFORMANCE-SUITE-500-003 ",
            "The ODPi Egeria Conformance Suite testing technology {0} of type {1} has created two test cases with the same id of {2}",
            "The conformance suite is unable to process one of the test cases.",
            "This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved."),

    SERVICE_NOT_INITIALIZED(503, "CONFORMANCE-SUITE-503-003 ",
            "The conformance suite service has not been initialized for server {0} and can not support REST API call {1}",
            "The server has received a call to one of its open metadata conformance suite operations but is unable to process it because the conformance suite service is not active.",
            "If the server is supposed to have the conformance suite service activated, correct the server configuration and restart the server."),

    EXCEPTION_RESPONSE_FROM_API(503, "CONFORMANCE-SUITE-503-004 ",
            "A {0} exception was received from REST API call {1} to server {2}: error message was: {3}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received an exception response.",
            "The error message should indicate the cause of the error.  Otherwise look for errors in the remote server's audit log and console to understand and correct the source of the error.")
            ;


    private static final long   serialVersionUID = 1L;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for OMRSErrorCode expects to be passed one of the enumeration rows defined in
     * OMRSErrorCode above.   For example:
     * <br><br>
     *     OMRSErrorCode   errorCode = OMRSErrorCode.SERVER_NOT_AVAILABLE;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ConformanceSuiteErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "ApacheAtlasErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
