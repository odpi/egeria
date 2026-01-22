/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serveroperations.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The ServerOpsErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMAG Server
 * It is used in conjunction with OMAG Exceptions, both Checked and Runtime (unchecked).
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>501 - not implemented </li>
 *         <li>503 - service not available</li>
 *         <li>400 - invalid parameters</li>
 *         <li>401 - unauthorized</li>
 *         <li>404 - not found</li>
 *         <li>405 - method not allowed</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum ServerOpsErrorCode implements ExceptionMessageSet
{
    /**
     * SERVER-OPS-400-011 - The OMAG server {0} has been passed a null admin services class name for access service {1}
     */
    NULL_ACCESS_SERVICE_ADMIN_CLASS(400, "SERVER-OPS-400-011",
            "The OMAG server {0} has been passed a null admin services class name for access service {1}",
            "The system cannot initialize this access service. The server failed to start.",
            "If the access service should be initialized then set up the appropriate admin services class name " +
                                            "in the access service's configuration and restart the server instance. Otherwise, " +
                                            "remove the configuration for this access service and restart the server."),

    /**
     * SERVER-OPS-400-012 - The OMAG server {0} has been passed an invalid admin services class name {1} for access service {2}
     */
    BAD_ACCESS_SERVICE_ADMIN_CLASS(400, "SERVER-OPS-400-012",
            "The OMAG server {0} has been passed an invalid admin services class name {1} for access service {2}",
            "The system cannot initialize this access service and the server failed to start.",
            "The configuration document for the serve needs to be fixed before the server will restart.  " +
                                           "If the access service should be initialized then update its configuration and" +
                                           "ensure ist admin class name is set to the name of a Java Class that implements AccessServiceAdmin. " +
                                           "Otherwise delete the configuration for this access service.  " +
                                           "Once the configuration document is updated, restart the server."),

    /**
     * SERVER-OPS-400-014 - The OMAG server {0} has been passed an invalid maximum page size of {1}
     */
    BAD_MAX_PAGE_SIZE(400, "SERVER-OPS-400-014",
            "The OMAG server {0} has been passed an invalid maximum page size of {1}",
            "The server failed to start.",
            "The maximum page size sets an upper limit on how many results a caller can request on a paged REST API call.  " +
                              "If it is set to zero then it means there is no limit, a positive number is the maximum paging size allowed.  " +
                              "Set the maximum page size in the configuration document to an appropriate value and restart the server."),

    /**
     * SERVER-OPS-400-015 - The OMAG server {0} cannot start the {1} enterprise OMRS topic connector, {2} exception with error message {3} occurred
     */
    ENTERPRISE_TOPIC_START_FAILED(400, "SERVER-OPS-400-015",
            "The OMAG server {0} cannot start the {1} enterprise OMRS topic connector, {2} exception with error message {3} occurred",
            "The open metadata access services will not be able to receive events from the connected repositories.",
            "Review the error messages and once the source of the problem is resolved, restart the server and retry the request."),

    /**
     * SERVER-OPS-400-017 - The OMAG server {0} cannot add open metadata services until the event bus is configured
     */
    NO_EVENT_BUS_SET(400, "SERVER-OPS-400-017",
            "The OMAG server {0} cannot add open metadata services until the event bus is configured",
            "No change has occurred in this server's configuration document.",
            "Add the event bus configuration using the administration services and retry the request."),

    /**
     * SERVER-OPS-400-019 - OMAG server {0} has been called with a configuration document that has no services configured
     */
    EMPTY_CONFIGURATION(400, "SERVER-OPS-400-019",
            "OMAG server {0} has been called with a configuration document that has no services configured",
            "The requested server provides no function.",
            "Use the administration services to add configuration for OMAG services to the server's configuration document."),

    /**
     * SERVER-OPS-400-029 - The View Server {0} has been passed a null admin services class name for view service {1}
     */
    NULL_VIEW_SERVICE_ADMIN_CLASS(400, "SERVER-OPS-400-029",
            "The View Server {0} has been passed a null admin services class name for view service {1}",
            "The system cannot initialize this view service since it has no admin class to call.",
            "If the view service should be initialized then set up the appropriate view service admin class name and restart the View Server."),

    /**
     * SERVER-OPS-400-030 - The View Server {0} has been passed an invalid admin services class name {1} for view service {2}
     */
    BAD_VIEW_SERVICE_ADMIN_CLASS(400, "SERVER-OPS-400-030",
            "The View Server {0} has been passed an invalid admin services class name {1} for view service {2}",
            "The system cannot initialize this view service since it can not find the view's admin class.",
            "If the view service should be initialized then ensure that the view service's admin class is specified correctly and available on " +
                                         "the class path.  Then restart the View Server."),


    /**
     * SERVER-OPS-400-050 - The OMAG Server {0} has been passed configuration which contains the following obsolete section {1}
     */
    OLD_CONFIGURATION(400, "SERVER-OPS-400-050",
                                    "The OMAG Server {0} has been passed configuration which contains the following obsolete section {1}",
                                    "The system cannot initialize this server since it no longer supports the requested subsystem.",
                                    "Use information in the admin guide to update the configuration to request the replacement service " +
                                            "and restart the server."),

    /**
     * SERVER-OPS-500-001 - Method {1} for OMAG server {0} returned an unexpected {2} exception with message {3}
     */
    UNEXPECTED_EXCEPTION(500, "SERVER-OPS-500-001",
            "Method {1} for OMAG server {0} returned an unexpected exception of {2} with message {3}",
            "The system cannot work with the OMAG server.  No change was made to the server's configuration document.",
            "This is likely to be either a configuration, operational or logic error. Look for other errors.  Validate the request.  If you are stuck, raise an issue."),


    /**
     * SERVER-OPS-500-004 - The {0} service detected an unexpected {1} exception with message {2} during initialization
     */
    UNEXPECTED_INITIALIZATION_EXCEPTION(500, "SERVER-OPS-500-004",
            "The {0} service detected an unexpected {1} exception with message {2} during initialization",
            "The system cannot start the service in the OMAG server.",
            "This is likely to be either an operational or logic error. Look for other errors in the audit log.  Validate the request.  " +
                                                "If you are stuck, raise an issue."),

    /**
     * SERVER-OPS-503-001 - A client-side exception was received from API call {0} to OMAG Server Platform at {1}.  The error message was {2}
     */
    CLIENT_SIDE_REST_API_ERROR(503, "SERVER-OPS-503-001",
            "A client-side exception was received from API call {0} to OMAG Server Platform at {1}.  The error message was {2}",
            "The server has issued a call to the open metadata admin service REST API in a remote server and has received an exception from the " +
                                       "local client libraries.",
            "Look for errors in the local client's console to understand and correct the source of the error.")

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
    ServerOpsErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
