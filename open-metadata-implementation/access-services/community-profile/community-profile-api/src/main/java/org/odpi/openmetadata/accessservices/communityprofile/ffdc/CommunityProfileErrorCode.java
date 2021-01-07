/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The CommunityProfileErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Community Profile OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 *
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
public enum CommunityProfileErrorCode implements ExceptionMessageSet
{
    NO_OTHER_IDENTITY(400, "OMAS-COMMUNITY-PROFILE-400-001",
            "The user identity {0} is the only identity assigned to profile {1} and therefore it can not be deleted",
            "The delete request fails.",
            "If this user identity needs to be deleted, either make sure another identity has been added to the profile, or delete the profile first."),
    QUALIFIED_NAME_NOT_UNIQUE(400, "OMAS-COMMUNITY-PROFILE-400-002",
            "The qualified name passed in the parameter {1} of the {2} operation is not unique.",
            "The system is unable to create the requested object because the qualified name is not unique.",
            "Correct the qualified name passed on the call so it is a unique value."),
    UNKNOWN_IDENTITY(404, "OMAS-COMMUNITY-PROFILE-404-001",
            "The user identity {0} is not known",
            "No action was taken on behalf of the user.",
            "Check that the user identity value is correct.  If it is correct then add it to the repository. Try the request again with a valid value."),
    OMRS_NOT_INITIALIZED(500, "OMAS-COMMUNITY-PROFILE-500-001",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Community Profile OMAS is running has initialized correctly.  " +
                "Correct any errors discovered and retry the request when the open metadata services are available."),
    UNABLE_TO_CREATE_USER_IDENTITY(500, "OMAS-COMMUNITY-PROFILE-500-002",
            "Unable to create new user identity object for user id {0}",
            "The system returned a null from the request to add the user identity object.",
            "Verify that the OMAS server running and there are no errors associated with the new user identity request on the server side."),
    UNABLE_TO_CREATE_CONTRIBUTION_RECORD(500, "OMAS-COMMUNITY-PROFILE-500-003",
            "Method {0} for server {1} is unable to create new contribution record for profile with identifier of {2} supporting person with qualified name of {3}",
            "The system returned a null from the request to create the contribution record.",
            "Verify that the OMAG server running and there are no errors associated with the new contribution record request on the server side."),
    NO_IDENTITY_FOR_PROFILE(500, "OMAS-COMMUNITY-PROFILE-500-004",
            "Profile {0} does not have an associated user identity",
            "The system returned a PropertyServerException rather than executing the request.  The profile is not usable without a user identity.",
            "Use the Community Profile OMAS API to either delete this profile or add a user identity to it."),
    PARSE_EVENT_ERROR(500, "OMAS-COMMUNITY-PROFILE-500-005",
            "Unable to publish the {0} event due to exception {1}.  The error message from the exception was {2}, the event contents was {3}",
            "The system detected an exception whilst parsing an event into a JSON String prior to publishing it.",
            "Investigate and correct the source of the error.  Once fixed, events will be published."),
    ;


    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for CommunityProfileErrorCode expects to be passed one of the enumeration rows defined in
     * CommunityProfileErrorCode above.   For example:
     *
     *     CommunityProfileErrorCode   errorCode = CommunityProfileErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    CommunityProfileErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "CommunityProfileErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
