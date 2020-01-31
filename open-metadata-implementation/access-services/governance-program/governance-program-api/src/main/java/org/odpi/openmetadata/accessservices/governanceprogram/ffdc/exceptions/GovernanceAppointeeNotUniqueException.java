/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions;


import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficerAppointee;

import java.util.List;
import java.util.Objects;

/**
 * The GovernanceAppointeeNotUniqueException is thrown by the OMAS when more than one person has a current appointment
 * to a governance officer post.
 */
public class GovernanceAppointeeNotUniqueException extends GovernanceProgramCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private List<GovernanceOfficerAppointee> duplicatePosts;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param duplicatePosts list of profiles for the same employee number
     */
    public GovernanceAppointeeNotUniqueException(int                                 httpCode,
                                                 String                              className,
                                                 String                              actionDescription,
                                                 String                              errorMessage,
                                                 String                              systemAction,
                                                 String                              userAction,
                                                 List<GovernanceOfficerAppointee>    duplicatePosts)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.duplicatePosts = duplicatePosts;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     * @param caughtError the error that resulted in this exception.
     * @param duplicatePosts list of profiles for the same employee number
     */
    public GovernanceAppointeeNotUniqueException(int                                 httpCode,
                                                 String                              className,
                                                 String                              actionDescription,
                                                 String                              errorMessage,
                                                 String                              systemAction,
                                                 String                              userAction,
                                                 Throwable                           caughtError,
                                                 List<GovernanceOfficerAppointee>    duplicatePosts)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.duplicatePosts = duplicatePosts;
    }


    /**
     * Return the list of personal profiles that have a duplicate name.
     *
     * @return list of profiles
     */
    public List<GovernanceOfficerAppointee> getDuplicatePosts()
    {
        return duplicatePosts;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "InvalidParameterException{" +
                "duplicatePosts='" + duplicatePosts + '\'' +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof GovernanceAppointeeNotUniqueException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceAppointeeNotUniqueException that = (GovernanceAppointeeNotUniqueException) objectToCompare;
        return Objects.equals(getDuplicatePosts(), that.getDuplicatePosts());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDuplicatePosts());
    }
}
