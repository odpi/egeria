/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The SurveyServiceErrorCode is used to define first failure data capture (FFDC) for errors that occur
 * when running Survey Action Services.  It is
 * used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum SurveyServiceErrorCode implements ExceptionMessageSet
{
    /**
     * SURVEY-ACTION-SERVICE-400-001 - Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3}
     */
    INVALID_ASSET_TYPE(400, "SURVEY-ACTION-SERVICE-400-001",
             "Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3}",
             "The survey action service terminates.",
             "The caller has requested a governance request type that is incompatible with the type of the " +
                               "asset that has been supplied.  This problem could be resolved by issuing the survey request with " +
                               "a governance request type that is compatible with the asset, or changing the survey action service " +
                               "associated with the governance request type to one that supports this type of asset."),

    /**
     * SURVEY-ACTION-SERVICE-400-002 - Asset {0} has a root schema of type {1} but survey action service {2} only supports the following root schema type(s): {3}
     */
    INVALID_ROOT_SCHEMA_TYPE(400, "SURVEY-ACTION-SERVICE-400-002",
                       "Asset {0} has a root schema of type {1} but survey action service {2} only supports the following root schema type(s): {3}",
                       "The survey action service terminates because it can not proceed.",
                       "The caller has requested a governance request type that is unable to process a root schema for an asset because its type is unsupported." +
                                     "  This problem could be resolved by issuing the survey request with " +
                               "a governance request type that is compatible with the asset's schema, or changing the survey action service " +
                               "associated with the governance request type to one that supports this type of schema."),

    /**
     * SURVEY-ACTION-SERVICE-400-003 - {0} asset {1} describes a resource called {2} which is of type {3} but survey action service {4} only supports the following type(s) of resources: {5}
     */
    INVALID_RESOURCE(400, "SURVEY-ACTION-SERVICE-400-003",
                             "{0} asset {1} describes a resource called {2} which is of type {3} but survey action service {4} only supports the following type(s) of resources: {5}",
                             "The survey action service terminates because it does not know how to process this type of resource.",
                             "There is a mismatch between the asset in the open metadata catalog and the resource that it represents. Update the asset in the asset catalog so that it is matched with more appropriate services."),

    /**
     * SURVEY-ACTION-SERVICE-400-004 - {0} asset {1} describes a resource called {2} does not exist
     */
    NO_RESOURCE(400, "SURVEY-ACTION-SERVICE-400-004",
                     "{0} asset {1} describes a resource called {2} that does not exist",
                     "The survey action service terminates because it does not have access to the resource.",
                     "Ensure the resource is correctly identified in the asset. Rerun this request when the resource is created."),

    /**
     * SURVEY-ACTION-SERVICE-500-001 - No information about the asset {0} has been returned from the asset store for survey action service {1}
     */
    NO_ASSET(500, "SURVEY-ACTION-SERVICE-500-001",
            "No information about the asset {0} has been returned from the asset store for survey action service {1}",
            "The survey action service terminates without running any automated survey function.",
            "This is an unexpected condition because if the metadata server was unavailable, an exception would have been caught."),

    /**
     * SURVEY-ACTION-SERVICE-500-002 - No type name is available for the asset passed to survey action service {0}.  The full asset contents are: {1}
     */
    NO_ASSET_TYPE(500, "SURVEY-ACTION-SERVICE-500-002",
             "No type name is available for the asset passed to survey action service {0}.  The full asset contents are: {1}",
             "The survey action service terminates without running the requested automated survey function.",
             "This is an unexpected condition because this value should always be returned with an asset."),
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
    SurveyServiceErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
