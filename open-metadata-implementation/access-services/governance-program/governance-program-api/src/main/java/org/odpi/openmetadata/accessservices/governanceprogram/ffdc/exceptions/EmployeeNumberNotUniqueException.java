/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;
import java.util.Objects;

/**
 * The EmployeeNumberNotUniqueException is thrown by the OMAS when more than one personal details record is returned
 * for an employee number.
 */
public class EmployeeNumberNotUniqueException extends GovernanceProgramCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private List<EntityDetail> duplicateProfiles;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param duplicateProfiles list of profiles for the same employee number
     */
    public EmployeeNumberNotUniqueException(int                   httpCode,
                                            String                className,
                                            String                actionDescription,
                                            String                errorMessage,
                                            String                systemAction,
                                            String                userAction,
                                            List<EntityDetail>    duplicateProfiles)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.duplicateProfiles = duplicateProfiles;
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
     * @param duplicateProfiles list of profiles for the same employee number
     */
    public EmployeeNumberNotUniqueException(int                   httpCode,
                                            String                className,
                                            String                actionDescription,
                                            String                errorMessage,
                                            String                systemAction,
                                            String                userAction,
                                            Throwable             caughtError,
                                            List<EntityDetail>    duplicateProfiles)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.duplicateProfiles = duplicateProfiles;
    }


    /**
     * Return the list of personal profiles that have a duplicate name.
     *
     * @return list of profiles
     */
    public List<EntityDetail> getDuplicateProfiles()
    {
        return duplicateProfiles;
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
                "duplicateProfiles='" + duplicateProfiles + '\'' +
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
        if (!(objectToCompare instanceof EmployeeNumberNotUniqueException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EmployeeNumberNotUniqueException that = (EmployeeNumberNotUniqueException) objectToCompare;
        return Objects.equals(getDuplicateProfiles(), that.getDuplicateProfiles());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDuplicateProfiles());
    }
}
