/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The RepositoryGovernanceErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the RepositoryGovernance Engine Services.  It is used in conjunction with all exceptions,
 * both Checked and Runtime (unchecked).
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
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum RepositoryGovernanceErrorCode implements ExceptionMessageSet
{
    NULL_REPOSITORY_GOVERNANCE_CONTEXT(400, "OMES-REPOSITORY-GOVERNANCE-400-001",
                         "No repository governance context supplied to the repository governance service {0}",
                         "The repository governance service has no access to open metadata, the request type and request parameters.",
                         "This may be a configuration or, more likely a code error in the repository governance engine.  Look for other error messages and review the code of the repository governance service.  Once the cause is resolved, retry the repository governance request.",
                         "https://egeria-project.org/services/omes/repository-governance/overview/"),

    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */

    SERVICE_INSTANCE_FAILURE(400, "OMES-REPOSITORY-GOVERNANCE-400-008",
                             "The Repository Governance OMES are unable to initialize a new instance in server {0}; error message is {1}",
                             "The Repository Governance OMES detected an error during the start up of a specific server instance.  " +
                                     "No repository governance services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server.",
                                     "https://egeria-project.org/services/omes/repository-governance/overview/"),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the repository governance engines will operate successfully.
     */

    /*
     * Errors when running requests
     */

    INVALID_REPOSITORY_GOVERNANCE_SERVICE(400, "OMES-REPOSITORY-GOVERNANCE-400-022",
             "The repository governance service {0} linked to repository governance request type {1} can not be started.  " +
                     "The {2} exception was returned with message {3}",
             "The repository governance request is not run and an error is returned to the caller.",
             "This may be an error in the repository governance services's logic or the repository governance service may not be properly deployed or " +
                                      "there is a configuration error related to the repository governance engine.  " +
                                      "The configuration that defines the repository governance request type in the repository governance engine and links " +
                                      "it to the repository governance service is maintained in the metadata server by the RepositoryGovernance " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "repository governance service's implementation has been deployed so the Repository Governance OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the repository governance service in which case, " +
                                      "raise an issue with the author of the repository governance service to get it fixed.  Once the cause is resolved, " +
                                      "retry the repository governance request.",
                                      "https://egeria-project.org/services/omes/repository-governance/overview/"),

    UNEXPECTED_EXCEPTION(500, "OMES-REPOSITORY-GOVERNANCE-500-001",
                         "Unexpected {0} exception in repository governance service {1} of type {2} detected by method {3}.  The error message was {4}",
                         "The repository governance service failed during its operation.",
                         "This may be a configuration or a code error.  Look for other error messages and review the code of the repository governance service. " +
                                 "Once the cause is resolved, retry the repository governance request.",
                                 "https://egeria-project.org/services/omes/repository-governance/overview/"),

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
    RepositoryGovernanceErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
    RepositoryGovernanceErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
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
