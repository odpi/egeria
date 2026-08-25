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
                                            "remove the configuration for this access service and restart the server.",
                                            "https://egeria-project.org/services/server-operations/"),

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
                                           "Once the configuration document is updated, restart the server.",
                                           "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-400-014 - The OMAG server {0} has been passed an invalid maximum page size of {1}
     */
    BAD_MAX_PAGE_SIZE(400, "SERVER-OPS-400-014",
            "The OMAG server {0} has been passed an invalid maximum page size of {1}",
            "The server failed to start.",
            "The maximum page size sets an upper limit on how many results a caller can request on a paged REST API call.  " +
                              "If it is set to zero then it means there is no limit, a positive number is the maximum paging size allowed.  " +
                              "Set the maximum page size in the configuration document to an appropriate value and restart the server.",
                              "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-400-015 - The OMAG server {0} cannot start the {1} enterprise OMRS topic connector, {2} exception with error message {3} occurred
     */
    ENTERPRISE_TOPIC_START_FAILED(400, "SERVER-OPS-400-015",
            "The OMAG server {0} cannot start the {1} enterprise OMRS topic connector, {2} exception with error message {3} occurred",
            "The open metadata access services will not be able to receive events from the connected repositories.",
            "Review the error messages and once the source of the problem is resolved, restart the server and retry the request.",
            "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-400-029 - The View Server {0} has been passed a null admin services class name for view service {1}
     */
    NULL_VIEW_SERVICE_ADMIN_CLASS(400, "SERVER-OPS-400-029",
            "The View Server {0} has been passed a null admin services class name for view service {1}",
            "The system cannot initialize this view service since it has no admin class to call.",
            "If the view service should be initialized then set up the appropriate view service admin class name and restart the View Server.",
            "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-400-030 - The View Server {0} has been passed an invalid admin services class name {1} for view service {2}
     */
    BAD_VIEW_SERVICE_ADMIN_CLASS(400, "SERVER-OPS-400-030",
            "The View Server {0} has been passed an invalid admin services class name {1} for view service {2}",
            "The system cannot initialize this view service since it can not find the view's admin class.",
            "If the view service should be initialized then ensure that the view service's admin class is specified correctly and available on " +
                                         "the class path.  Then restart the View Server.",
                                         "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-500-004 - The {0} service detected an unexpected {1} exception with message {2} during initialization
     */
    UNEXPECTED_INITIALIZATION_EXCEPTION(500, "SERVER-OPS-500-004",
            "The {0} service detected an unexpected {1} exception with message {2} during initialization",
            "The system cannot start the service in the OMAG server.",
            "This is likely to be either an operational or logic error. Look for other errors in the audit log.  Validate the request.  " +
                                                "If you are stuck, raise an issue.",
                                                "https://egeria-project.org/services/server-operations/"),

    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;
    private final String url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ServerOpsErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, null);
    }


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    ServerOpsErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                              userAction,
                                              url);
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
                                                                                      userAction,
                                                                                      url);

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
                       ", url='" + url + '\'' +
                       '}';
    }
}
