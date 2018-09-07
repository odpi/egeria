/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The GovernanceProgramErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Governance Program OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum GovernanceProgramErrorCode
{
    SERVER_URL_NOT_SPECIFIED(400, "OMAS-GOVERNANCE-PROGRAM-400-001 ",
            "The OMAS Server URL is null",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the GovernanceProgram when it is created."),
    SERVER_URL_MALFORMED(400, "OMAS-GOVERNANCE-PROGRAM-400-002 ",
            "The OMAS Server URL {0} is not in a recognized format",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the GovernanceProgram when it is created."),
    NULL_USER_ID(400, "OMAS-GOVERNANCE-PROGRAM-400-003 ",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),
    NULL_GUID(400, "OMAS-GOVERNANCE-PROGRAM-400-004 ",
            "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a guid.",
            "Correct the code in the caller to provide the guid."),
    NULL_NAME(400, "OMAS-GOVERNANCE-PROGRAM-400-005 ",
            "The name passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a name.",
            "Correct the code in the caller to provide the name."),
    USER_NOT_AUTHORIZED(400, "OMAS-GOVERNANCE-PROGRAM-400-008 ",
            "User {0} is not authorized to issue the {1} request for open metadata access service {3} on server {4}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    PROPERTY_SERVER_ERROR(400, "OMAS-GOVERNANCE-PROGRAM-400-009 ",
            "An unexpected error was returned by the property server during {1} request for open metadata access service {2} on server {3}; message was {0}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    NULL_ENUM(400, "OMAS-GOVERNANCE-PROGRAM-400-010 ",
            "The enumeration value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this enumeration value.",
            "Correct the code in the caller to provide the name."),
    SERVER_NOT_AVAILABLE(404, "OMAS-GOVERNANCE-PROGRAM-404-001 ",
            "The OMAS Service {0} is not available",
            "The system is unable to connect to the OMAS Server.",
            "Check that the OMAS Server URL is correct and the OMAS Service is running.  Retry the request when the OMAS Service is available."),
    OMRS_NOT_INITIALIZED(404, "OMAS-GOVERNANCE-PROGRAM-404-002 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server where the Governance Program OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    OMRS_NOT_AVAILABLE(404, "OMAS-GOVERNANCE-PROGRAM-404-003 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata repository.",
            "Check that the server where the Governance Program OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    NO_METADATA_COLLECTION(404, "OMAS-GOVERNANCE-PROGRAM-404-004 ",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),
    INSTANCE_NOT_FOUND_BY_GUID(404, "OMAS-GOVERNANCE-PROGRAM-404-005 ",
            "The governance program OMAS {0} method is not able to retrieve a {1} record with a unique identifier of {2} for userId {3} from server {4}",
            "The record is not stored in the property server.",
            "Check that the unique identifier is correct and the property server(s) supporting the governance program is/are running."),
    PERSONAL_DETAILS_NOT_FOUND_BY_EMP_ID(404, "OMAS-GOVERNANCE-PROGRAM-404-006 ",
            "The governance program OMAS is not able to retrieve a personal details record with an employee number of {0}",
            "The personal details record is not stored in the property server.",
            "Check that the employee number is correct and the property server(s) supporting the governance program is/are running."),
    PERSONAL_DETAILS_NOT_FOUND_BY_NAME(404, "OMAS-GOVERNANCE-PROGRAM-404-007 ",
            "The governance program OMAS is not able to retrieve a personal details record with an employee number of {0}",
            "The personal details record is not stored in the property server.",
            "Check that the name is correct and the property server(s) supporting the governance program is/are running."),
    DUPLICATE_PERSONAL_DETAILS_FOR_EMP_ID(404, "OMAS-GOVERNANCE-PROGRAM-404-008 ",
            "The governance program OMAS has retrieved multiple personal details records with an employee number of {0}",
            "Multiple personal details records are stored in the property server.",
            "Details of the duplicate records are stored in the exception.  Use them to locate and correct some or all of the records so the employee number is a unique field."),
    PERSONAL_DETAILS_NOT_DELETED(404, "OMAS-GOVERNANCE-PROGRAM-404-009 ",
            "The governance program OMAS is not able to delete a personal details record with an guid of {0}; the error message was {1}",
            "The personal details record is not deletable.",
            "Check that the guid is correct and the property server(s) supporting the governance program is/are running."),
    INSTANCE_WRONG_TYPE_FOR_GUID(404, "OMAS-GOVERNANCE-PROGRAM-404-010 ",
            "The governance program OMAS {0} method has retrieved a record for unique identifier (guid) {1} which is of type {2} rather than type {3)",
            "The governance officer record is not stored in the property server.",
            "Check that the unique identifier is correct and the property server(s) supporting the governance program is/are running."),
    GOVERNANCE_OFFICER_NOT_FOUND_BY_APPOINTMENT_ID(404, "OMAS-GOVERNANCE-PROGRAM-404-011 ",
            "The governance program OMAS is not able to retrieve a governance officer record with an appointment id of {0}",
            "The governance officer record is not stored in the property server.",
            "Check that the appointment id is correct and the property server(s) supporting the governance program is/are running."),
    GOVERNANCE_OFFICER_NOT_FOUND_BY_NAME(404, "OMAS-GOVERNANCE-PROGRAM-404-012 ",
            "The governance program OMAS is not able to retrieve a governance officer record with an employee number of {0}",
            "The governance officer record is not stored in the property server.",
            "Check that the name is correct and the property server(s) supporting the governance program is/are running."),
    DUPLICATE_GOVERNANCE_OFFICER_FOR_APPOINTMENT_ID(404, "OMAS-GOVERNANCE-PROGRAM-404-013 ",
            "The governance program OMAS has retrieved multiple governance officer records with an appointment id of {0}",
            "Multiple governance officer records are stored in the property server.",
             "Details of the duplicate records are stored in the exception.  Use them to locate and correct some or all of the records so the employee number is a unique field."),
    GOVERNANCE_OFFICER_NOT_DELETED(404, "OMAS-GOVERNANCE-PROGRAM-404-014 ",
            "The governance program OMAS is not able to delete a governance officer record with an guid of {0} since the supplied appointmentId of {1} and " +
                    "governance domain of {2} does not match what is stored for this governance officer",
            "The governance officer record is not deletable with these parameters.",
            "Reissue the delete call with a guid, appointment id and governance domain that matches the stored information."),
    INSTANCE_NOT_DELETED(404, "OMAS-GOVERNANCE-PROGRAM-404-015 ",
            "The governance program OMAS is not able to delete an instance of type {0} with an guid of {1}; the error message was {2}",
            "The instance is not deletable.",
            "Check that the guid and type information are correct and the property server(s) supporting the governance program is/are running."),
    MULTIPLE_INCUMBENTS_FOR_GOVERNANCE_OFFICER(404, "OMAS-GOVERNANCE-PROGRAM-404-016 ",
            "Governance officer for domain {0} with guid {1}, appointmentId {2} and context {3} currently has {4} people assigned",
            "The position is crowded.",
            "Information about the people appointed is included in the accompanying exception.  Use this information to correct the appointments by relieving all but one of the people assigned"),
    FREE_ROLE_FOR_APPOINTMENT_FAILED(404, "OMAS-GOVERNANCE-PROGRAM-404-017 ",
            "Unable to relieve incumbent governance officer; error message was {0}",
            "The new appointment can not proceed.",
            "Use the information in the message to resolve the problem and retry the appointment request."),
    NO_PROFILE_FOR_GOVERNANCE_POST(404, "OMAS-GOVERNANCE-PROGRAM-404-018 ",
            "Governance officer {0} has a null profile for governance posting {1}",
            "The new appointment can not proceed because the information stored about a related governance posting is invalid.",
            "Use the information in the message to resolve the problem and retry the appointment request."),
    PROFILE_NOT_LINKED_TO_GOVERNANCE_OFFICER(404, "OMAS-GOVERNANCE-PROGRAM-404-019 ",
            "Personal profile {0} is not currently appointed to governance officer post {1}",
            "The requested personal profile can not be relieved from a governance posting to the governance officer role because this profile is not currently associated with the role.",
            "Validate that the personal profile unique identifier (guid) is correct."),
    INVALID_GOVERNANCE_POST_RELATIONSHIP(404, "OMAS-GOVERNANCE-PROGRAM-404-020 ",
            "The Governance Program OMAS has discovered an invalid Governance Post Relationship: {0}",
            "There is a logic error in the Governance Program OMAS or one of the repositories that stores metadata.",
            "Use the content of the relationship to determine if the relationship is malformed and to what extent.  If the relationship if properly formed " +
                                                 "then the issue is in the Governance Program OMAS.  " +
                                                 "If the relationship is incomplete then use the metadata collection id to determine which " +
                                                 "repository is in error.  Raise an Egeria JIRA and report your findings."),
    NULL_RESPONSE_FROM_API(503, "OMAS-GOVERNANCE-PROGRAM-503-001 ",
            "A null response was received from REST API call {0} to server {1}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
            "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),
    CLIENT_SIDE_REST_API_ERROR(503, "OMAS-GOVERNANCE-PROGRAM-503-002 ",
            "A client-side exception was received from API call {0} to repository {1}.  The error message was {2}",
            "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
            "Look for errors in the local server's console to understand and correct the source of the error."),
    SERVICE_NOT_INITIALIZED(503, "OMAS-GOVERNANCE-PROGRAM-503-003 ",
            "The access service has not been initialized and can not support REST API call {0}",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server.")
    ;


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(GovernanceProgramErrorCode.class);


    /**
     * The constructor for GovernanceProgramErrorCode expects to be passed one of the enumeration rows defined in
     * GovernanceProgramErrorCode above.   For example:
     *
     *     GovernanceProgramErrorCode   errorCode = GovernanceProgramErrorCode.SERVER_URL_NOT_SPECIFIED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    GovernanceProgramErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
     * @param params   strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== OCFErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> OCFErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "GovernanceProgramErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
