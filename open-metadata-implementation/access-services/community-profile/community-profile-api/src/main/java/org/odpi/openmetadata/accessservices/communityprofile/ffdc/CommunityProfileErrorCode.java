/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

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
public enum CommunityProfileErrorCode
{
    NO_OTHER_IDENTITY(400, "OMAS-COMMUNITY-PROFILE-400-001 ",
            "The user identity {0} is the only identity assigned to profile {1} and therefore it can not be deleted",
            "No action was taken.",
            "If this user identity needs to be deleted, either make sure another identity has been added to the profile, or delete the profile first."),
    QUALIFIED_NAME_NOT_UNIQUE(400, "OMAS-COMMUNITY-PROFILE-400-002 ",
            "The qualified name passed in the parameter {1} of the {2} operation is not unique.",
            "The system is unable to create the requested object because the qualified name is not unique.",
            "Correct the qualified name passed on the call so it is a unique value."),
    UNKNOWN_IDENTITY(404, "OMAS-COMMUNITY-PROFILE-404-001 ",
            "The user identity {0} is not known",
            "No action was taken.",
            "Check that the user identity value is correct.  If it is correct then add it to the repository. Try the request again with a valid value."),
    OMRS_NOT_INITIALIZED(500, "OMAS-COMMUNITY-PROFILE-500-001 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Community Profile OMAS is running has initialized correctly.  " +
                "Correct any errors discovered and retry the request when the open metadata services are available."),
    UNABLE_TO_CREATE_USER_IDENTITY(500, "OMAS-COMMUNITY-PROFILE-500-002 ",
            "Unable to create new user identity object for user id {0}",
            "The system returned a null from the addEntity request.",
            "Verify that the OMAS server running and their are no errors on the server side."),
    UNABLE_TO_CREATE_CONTRIBUTION_RECORD(500, "OMAS-COMMUNITY-PROFILE-500-003 ",
            "Method {0} for server {1} is unable to create new contribution record for profile with identifier of {2} supporting person with qualified name of {3}",
            "The system returned a null from the addEntity request.",
            "Verify that the OMAS server running and their are no errors on the server side."),
    NO_IDENTITY_FOR_PROFILE(500, "OMAS-COMMUNITY-PROFILE-500-004 ",
            "Profile {0} does not have an associated user identity",
            "The system returned a PropertyServerException rather than executing the request.  The profile is not usable without a user identity",
            "Use the Community Profile OMAS API to either delete this profile or add a user identity to it."),
    ;


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(CommunityProfileErrorCode.class);


    /**
     * The constructor for CommunityProfileErrorCode expects to be passed one of the enumeration rows defined in
     * CommunityProfileErrorCode above.   For example:
     *
     *     CommunityProfileErrorCode   errorCode = CommunityProfileErrorCode.PROFILE_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    CommunityProfileErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
    {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }


    public int getHTTPErrorCode()
    {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId()
    {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== CommunityProfileErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> CommunityProfileErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction()
    {
        return userAction;
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
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
