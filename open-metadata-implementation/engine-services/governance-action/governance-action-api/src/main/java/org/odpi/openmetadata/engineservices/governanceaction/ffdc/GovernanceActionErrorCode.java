/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The GovernanceActionErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Governance Action Engine Services.  It is used in conjunction with all exceptions,
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
public enum GovernanceActionErrorCode implements ExceptionMessageSet
{
    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */

    SERVICE_INSTANCE_FAILURE(400, "OMES-GOVERNANCE-ACTION-400-008",
                             "The Governance Action OMES are unable to initialize a new instance in server {0}; error message is {1}",
                             "The Governance Action OMES detected an error during the start up of a specific server instance.  " +
                                     "No governance action services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server.",
                                     "https://egeria-project.org/services/omes/governance-action/overview/"),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the governance action engines will operate successfully.
     */

    /*
     * Errors when running requests
     */

    INVALID_GOVERNANCE_ACTION_SERVICE(400, "OMES-GOVERNANCE-ACTION-400-022",
             "The governance action service {0} linked to governance action request type {1} can not be started.  " +
                     "The {2} exception was returned with message {3}",
             "The governance action request is not run and an error is returned to the caller.",
             "This may be an error in the governance action services's logic or the governance action service may not be properly deployed or " +
                                      "there is a configuration error related to the governance action engine.  " +
                                      "The configuration that defines the governance action request type in the governance action engine and links " +
                                      "it to the governance action service is maintained in the metadata server by the Governance Action " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "governance action service's implementation has been deployed so the Governance Action OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the governance action service in which case, " +
                                      "raise an issue with the author of the governance action service to get it fixed.  Once the cause is resolved, " +
                                      "retry the governance action request.",
                                      "https://egeria-project.org/services/omes/governance-action/overview/"),

    UNKNOWN_GOVERNANCE_ACTION_SERVICE( 400, "OMES-GOVERNANCE-ACTION-400-030",
                                       "The governance action service {0} linked to request type {1} can not be started because the Governance Action OMES does not support the {2} type of " +
                                               "governance action service.",
                                       "The governance action request is not run and an error is returned to the caller.  Subsequent requests to this governance action service will also fail.",
                                       "This version of the Governance Engine OMES does not support this type of governance action service.  " +
                                               "It is likely that you need a future version of Egeria or another platform that supports this type of governance action service.",
                                               "https://egeria-project.org/services/omes/governance-action/overview/"),

    NOT_GOVERNANCE_ACTION_SERVICE( 400, "OMES-GOVERNANCE-ACTION-400-031",
                                   "The governance action service {0} linked to request type {1} can not be started because it is not a governance action service.  " +
                                           "Its class is {2} rather than a subclass of {3}",
                                   "The governance action request is not run and an error is returned to the caller.  Subsequent calls to this service will fail in the same way",
                                   "Correct the configuration for the Governance Action OMES to only include valid governance action service implementations.",
                                   "https://egeria-project.org/services/omes/governance-action/overview/"),

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
    GovernanceActionErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
    GovernanceActionErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
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
