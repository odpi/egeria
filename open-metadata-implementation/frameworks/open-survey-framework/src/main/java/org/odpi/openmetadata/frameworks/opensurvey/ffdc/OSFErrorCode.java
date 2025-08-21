/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The OSF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * OSF Discovery Services.  It is used in conjunction with all OSF Exceptions, both Checked and Runtime (unchecked).
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
public enum OSFErrorCode implements ExceptionMessageSet
{
    /**
     * OPEN-SURVEY-400-001 - No survey context supplied to the survey action service {0}
     */
    NULL_SURVEY_CONTEXT(400, "OPEN-SURVEY-400-001",
            "No survey context supplied to the survey action service {0}",
            "The survey action service is not able to determine which asset to analyze.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the survey action service.  Once the cause is resolved, retry the survey action request."),

    /**
     * OPEN-SURVEY-400-002 - No embedded survey action services supplied to the survey action pipeline {0}
     */
    NO_EMBEDDED_SURVEY_ACTION_SERVICES(400, "OPEN-SURVEY-400-002",
            "No embedded survey action services supplied to the survey action pipeline {0}",
            "The survey action pipeline is not able to survey action which survey action services to run.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the survey action pipeline service.  Once the cause is resolved, retry the survey action request."),

    /**
     * OPEN-SURVEY-400-003 - No embedded survey action services supplied to the survey action pipeline {0}
     */
    INVALID_EMBEDDED_SURVEY_ACTION_SERVICE(400, "OPEN-SURVEY-400-003",
            "No embedded survey action services supplied to the survey action pipeline {0}",
            "The survey action pipeline is not able to discover which survey action services to run.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the survey action pipeline service or the associated open survey action engine.  Once the cause is resolved, retry the survey action request."),



    /**
     * OPEN-SURVEY-400-005 - Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3}
     */
    INVALID_ASSET_TYPE(400, "OPEN-SURVEY-400-005",
                       "Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3}",
                       "The survey action service terminates.",
                       "The caller has requested a governance request type that is incompatible with the type of the " +
                               "asset that has been supplied.  This problem could be resolved by issuing the survey request with " +
                               "a governance request type that is compatible with the asset, or changing the survey action service " +
                               "associated with the governance request type to one that supports this type of asset."),


    /**
     * OPEN-SURVEY-400-006 - The {0} Survey Acton Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3}
     */
    WRONG_TYPE_OF_CONNECTOR(400, "OPEN-SURVEY-400-006",
                            "The {0} Survey Acton Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3}",
                            "The survey is unable to continue since it is unable to work with the supplied connector.",
                            "Use the details from the error message to determine the class of the connector.  " +
                                    "Update the connector type associated with its Connection in the metadata store."),

    /**
     * OPEN-SURVEY-400-007 - The {0} Survey Acton Service has been supplied with asset {1} which does not have a schema attached
     */
    NO_SCHEMA(400, "OPEN-SURVEY-400-007",
                            "The {0} Survey Acton Service has been supplied with asset {1} which does not have a schema attached",
                            "The survey is unable to continue since it is unable to assess whether the data stored in the associated resource matches the desired schema.",
                            "Update the asset to include the desired schema and re-run this survey.  If you want to discover the asset's schema then use a different survey service."),

    /**
     * OPEN-SURVEY-400-008 - The {0} Survey Acton Service has been supplied with asset {1} which does not have any schema attributes attached
     */
    NO_SCHEMA_ATTRIBUTES(400,"OPEN-SURVEY-400-008",
                         "The {0} Survey Acton Service has been supplied with asset {1} which does not have any schema attributes attached",
                         "The survey is unable to continue since it is unable to assess whether the data stored in the associated resource matches the desired schema because there are no schema attributes attached to the root schema.",
                         "Update the asset to include the desired schema attributes and re-run this survey.  If you want to discover the asset's schema then use a different survey service."),



    /**
     * OPEN-SURVEY-500-001 - Unexpected exception in survey action service {0} of type {1} detected by method {2}.  The error message was {3}
     */
    UNEXPECTED_EXCEPTION(500, "OPEN-SURVEY-500-001",
                         "Unexpected exception in survey action service {0} of type {1} detected by method {2}.  The error message was {3}",
                         "The survey action service failed during its operation.",
                         "This may be a configuration or a code error.  Look for other error messages and review the code of the survey action service.  Once the cause is resolved, retry the survey action request."),

    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;

    /**
     * The constructor for OSFErrorCode expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OSFErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "OSFErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}