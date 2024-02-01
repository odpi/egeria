/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.catalog.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The CatalogIntegratorErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Integration Services.  It is used in conjunction with all exceptions,
 * both Checked and Runtime (unchecked).
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
public enum CatalogIntegratorErrorCode implements ExceptionMessageSet
{
    /**
     * OMIS-CATALOG-INTEGRATOR-400-001 - Integration connector {0} is not of the correct type to run in the {1} integration service.  It must inherit from {2}
     */
    INVALID_CONNECTOR(400,"OMIS-CATALOG-INTEGRATOR-400-001",
                  "Integration connector {0} is not of the correct type to run in the {1} integration service.  It must inherit from {2}",
                  "The integration service fails to start and this in turn causes the integration daemon to fail.",
                  "The connector was configured through the administration calls for the integration service." +
                              "Either move it to an appropriate integration service or update the connector implementation " +
                              "to inherit from the correct class."),

    /**
     * OMIS-CATALOG-INTEGRATOR-400-002 - The {0} has been disabled by the configuration for the {1} integration service
     */
    DISABLED_EXCHANGE_SERVICE(400,"OMIS-CATALOG-INTEGRATOR-400-002",
                      "The {0} has been disabled by the configuration for the {1} integration service",
                      "The integration service's context is unable to return the client interface to this service.",
                      "The exchange service was disabled through the administration calls for the integration service." +
                              "Either change the configuration of the integration service or change the connector to skip the part of the " +
                              "synchronization that uses this exchange service since the organization does not want this type of metadata synchronized."),

    /**
     * OMIS-CATALOG-INTEGRATOR-400-003 - The permitted synchronization direction of {0} does not allow connector {1} to issue {2} requests on behalf of asset manager {3}
     */
    NOT_PERMITTED_SYNCHRONIZATION(400,"OMIS-CATALOG-INTEGRATOR-400-003",
                              "The permitted synchronization direction of {0} does not allow connector {1} to issue {2} requests on behalf of asset manager {3}",
                              "The request is not issued and an exception is returned to the caller.",
                              "The request was disabled through the administration calls for the integration connector." +
                                      "Either change the configuration of the integration service or change the connector to skip the part of the " +
                                      "synchronization that uses this request since the organization does not want this type of metadata synchronized."),

    /**
     * OMIS-CATALOG-INTEGRATOR-400-004 - The {0} Open Metadata Integration Service (OMIS) has been passed an invalid value of {1} in the {2} property.  The resulting exception of {3} included the following message: {4}
     */
    BAD_CONFIG_PROPERTIES(400, "OMIS-CATALOG-INTEGRATOR-400-004",
                          "The {0} Open Metadata Integration Service (OMIS) has been passed an invalid value of {1} in the {2} property.  The resulting exception of {3} included the following message: {4}",
                          "The access service has not been passed valid configuration .",
                          "Correct the value of the failing configuration property and restart the server."),




    /**
     * OMIS-CATALOG-INTEGRATOR-400-006 - Integration connector {0} has passed a null element to {1}
     */
    NULL_ELEMENT_PASSED(400,"OMIS-CATALOG-INTEGRATOR-400-006",
                        "Integration connector {0} has passed a null element to {1} in parameter {2}",
                        "The integration connector called the method with a null element. This means it has a logic error because the element should not be null.",
                        "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                                "from the integration daemon and its partner metadata server. Look at the logic and path through the code and correct the error."),

    /**
     * OMIS-CATALOG-INTEGRATOR-500-001 - Integration connector {0} has a null context
     */
    NULL_CONTEXT(400,"OMIS-CATALOG-INTEGRATOR-500-001",
                 "Integration connector {0} has a null context",
                 "The integration connector is running but does not have a context.  This is a timing issue in the integration daemon.",
                 "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                         "from the integration daemon and its partner metadata server.  Then contact the Egeria community to get help."),
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
    CatalogIntegratorErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
