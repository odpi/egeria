/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


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
public enum GovernanceProgramErrorCode implements ExceptionMessageSet
{
    NULL_ENUM(400, "OMAS-GOVERNANCE-PROGRAM-400-010",
            "The enumeration value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this enumeration value.",
            "Correct the code in the caller to provide the enumeration value and retry the request."),

    OMRS_NOT_INITIALIZED(404, "OMAS-GOVERNANCE-PROGRAM-404-002",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server where the Governance Program OMAS is running completed initialization successfully.  " +
                    "Correct any errors discovered and restart the server."),

    OMRS_NOT_AVAILABLE(404, "OMAS-GOVERNANCE-PROGRAM-404-003",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata repository.",
            "Review the start up messages for the server where the Governance Program OMAS is running.  " +
                    "Correct any errors discovered and restart the server."),

    GOVERNANCE_OFFICER_NOT_FOUND_BY_APPOINTMENT_ID(404, "OMAS-GOVERNANCE-PROGRAM-404-011",
            "The governance program OMAS is not able to retrieve a governance officer record with an appointment id of {0}",
            "The governance officer record  with the requested id is not stored in the property server.",
            "Check that the appointment id is correct and the property server(s) supporting the governance program is/are running."),

    GOVERNANCE_OFFICER_NOT_FOUND_BY_NAME(404, "OMAS-GOVERNANCE-PROGRAM-404-012",
            "The governance program OMAS is not able to retrieve a governance officer record with an employee number of {0}",
            "The governance officer record with the requested name is not stored in the property server.",
            "Check that the name is correct and the property server(s) supporting the governance program is/are running."),

    DUPLICATE_GOVERNANCE_OFFICER_FOR_APPOINTMENT_ID(404, "OMAS-GOVERNANCE-PROGRAM-404-013",
            "The governance program OMAS has retrieved multiple governance officer records with an appointment id of {0}",
            "Multiple governance officer records are linked to a governance appointment in the property server.",
             "Details of the duplicate records are stored in the exception.  Use them to locate and correct some or all of the records so the employee number is a unique field."),

    GOVERNANCE_OFFICER_NOT_DELETED(404, "OMAS-GOVERNANCE-PROGRAM-404-014",
            "The governance program OMAS is not able to delete a governance officer record with an guid of {0} since the supplied appointmentId of {1} and " +
                    "governance domain of {2} does not match what is stored for this governance officer",
            "The governance officer record is not deletable with these parameters.",
            "Reissue the delete call with a guid, appointment id and governance domain that matches the stored information."),

    MULTIPLE_INCUMBENTS_FOR_GOVERNANCE_OFFICER(404, "OMAS-GOVERNANCE-PROGRAM-404-016",
            "Governance officer for domain {0} with guid {1}, appointmentId {2} and context {3} currently has {4} people assigned",
            "The position is crowded.",
            "Information about the people appointed is included in the accompanying exception.  Use this information to correct the appointments by relieving all but one of the people assigned"),

    FREE_ROLE_FOR_APPOINTMENT_FAILED(404, "OMAS-GOVERNANCE-PROGRAM-404-017",
            "Unable to relieve incumbent governance officer; error message was {0}",
            "The new appointment can not proceed.",
            "Use the information in the message to resolve the problem and retry the appointment request."),

    NO_PROFILE_FOR_GOVERNANCE_POST(404, "OMAS-GOVERNANCE-PROGRAM-404-018",
            "Governance officer {0} has a null profile for governance posting {1}",
            "The new appointment can not proceed because the information stored about a related governance posting is invalid.",
            "Create a profile for the governance officer and retry the appointment request."),

    PROFILE_NOT_LINKED_TO_GOVERNANCE_OFFICER(404, "OMAS-GOVERNANCE-PROGRAM-404-019",
            "Personal profile {0} is not currently appointed to governance officer post {1}",
            "The requested personal profile can not be relieved from a governance posting to the governance officer role because this profile is not currently associated with the role.",
            "Validate that the personal profile unique identifier (guid) is correct."),

    INVALID_GOVERNANCE_POST_RELATIONSHIP(404, "OMAS-GOVERNANCE-PROGRAM-404-020",
            "The Governance Program OMAS has discovered an invalid Governance Post Relationship: {0}",
            "There is a logic error in the Governance Program OMAS or one of the repositories that stores metadata.",
            "Use the content of the relationship to determine if the relationship is malformed and to what extent.  If the relationship if properly formed " +
                                                 "then the issue is in the Governance Program OMAS.  " +
                                                 "If the relationship is incomplete then use the metadata collection id to determine which " +
                                                 "repository is in error.  Raise an Egeria JIRA and report your findings."),
    ;



    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for GovernanceProgramErrorCode expects to be passed one of the enumeration rows defined in
     * GovernanceProgramErrorCode above.   For example:
     *
     *     GovernanceProgramErrorCode   errorCode = GovernanceProgramErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    GovernanceProgramErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "GovernanceProgramErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
