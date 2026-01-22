/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The AtlasDiscoveryErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Apache Atlas REST connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum AtlasSurveyErrorCode implements ExceptionMessageSet
{
    /**
     * APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-001 - The {0} Apache Atlas Discovery Connector has been supplied with a resource connector of class {1} rather than class {2} for asset {3}
     */
    WRONG_REST_CONNECTOR(400, "APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-001",
                     "The {0} Apache Atlas Survey Action Connector has been supplied with a resource connector of class {1} rather than class {2} for asset {3}",
                     "The connector cannot continue to profile Apache Atlas because it can not call its REST API.",
                         "Use the details from the error message to determine the correct class of the connector to use.  It should be specified in the connector type of an embedded connection as part of the failing connector's Connection.  When the connection information has been corrected, restart the failing connector."),


    /**
     * APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-002 - The root schema type for Apache Atlas Software Server {0} is of type {1} rather than {2}. Apache Atlas Survey Connector {3} is not able to continue with its schema analysis.  The existing schema type properties are {4}
     */
    WRONG_ROOT_SCHEMA_TYPE(400, "APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-002",
                           "The root schema type for Apache Atlas Software Server {0} is of type {1} rather than {2}. Apache Atlas Survey Connector {3} is not able to continue with its schema analysis.  The existing schema type properties are {4}",
                           "The connector cannot continue to define the schema for the Apache Atlas Server based on its defined types because it cannot understand the existing root schema type.",
                           "Use the details from the error message to determine the origin and reason for the existing schema type.  If it is correct then disable the schema analysis of this survey action service.  It the existing root schema type should not be present, then delete it, and re-run the failed survey action service."),

    /**
     * APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-003 - The asset universe for Apache Atlas Software Server is null. Apache Atlas Survey Connector {0} is not able to continue with its schema analysis
     */
    MISSING_ASSET_UNIVERSE(400, "APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-003",
                           "The asset universe for Apache Atlas Software Server is null. Apache Atlas Survey Connector {0} is not able to continue with its schema analysis",
                           "The connector cannot continue to define the schema for the Apache Atlas Server based on its defined types because it cannot access the existing root schema type from the asset universe because it is null.",
                           "Use the details from the error message to determine the asset universe being null.  Correct the error and re-run the failed survey action service."),

    /**
     * APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-500-001 - The {0} Apache Atlas Survey Action connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-500-001",
                         "The {0} Apache Atlas Survey Action connector received an unexpected {1} exception during method {2}; the error message was: {3}",
                         "The connector cannot continue to survey the Apache Atlas Server.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),


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
    AtlasSurveyErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
