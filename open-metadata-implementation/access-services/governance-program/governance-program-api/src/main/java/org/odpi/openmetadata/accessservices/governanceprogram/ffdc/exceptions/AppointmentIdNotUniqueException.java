/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The AppointmentIdNotUniqueException is thrown by the OMAS when more than one governance officer record is returned
 * for an appointment id.
 */
public class AppointmentIdNotUniqueException extends GovernanceProgramCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private List<EntityDetail> duplicatePosts;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param duplicatePosts list of profiles for the same employee number
     */
    public AppointmentIdNotUniqueException(ExceptionMessageDefinition messageDefinition,
                                           String                     className,
                                           String                     actionDescription,
                                           List<EntityDetail>         duplicatePosts)
    {
        super(messageDefinition, className, actionDescription);
        this.duplicatePosts = duplicatePosts;
    }


    /**
     * This is the typical constructor used for creating an exception.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param duplicatePosts list of profiles for the same employee number
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public AppointmentIdNotUniqueException(ExceptionMessageDefinition       messageDefinition,
                                           String                           className,
                                           String                           actionDescription,
                                           List<EntityDetail>               duplicatePosts,
                                           Map<String, Object>              relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
        this.duplicatePosts = duplicatePosts;
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param duplicatePosts list of profiles for the same employee number
     */
    public AppointmentIdNotUniqueException(ExceptionMessageDefinition       messageDefinition,
                                           String                           className,
                                           String                           actionDescription,
                                           Throwable                        caughtError,
                                           List<EntityDetail>               duplicatePosts)
    {
        super(messageDefinition, className, actionDescription, caughtError);
        this.duplicatePosts = duplicatePosts;
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param duplicatePosts list of profiles for the same employee number
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public AppointmentIdNotUniqueException(ExceptionMessageDefinition       messageDefinition,
                                           String                           className,
                                           String                           actionDescription,
                                           Throwable                        caughtError,
                                           List<EntityDetail>               duplicatePosts,
                                           Map<String, Object>              relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
        this.duplicatePosts = duplicatePosts;
    }


    /**
     * This is the constructor used when receiving an exception from a remote server.  The values are
     * stored directly in the response object and are passed explicitly to the new exception.
     * Notice that the technical aspects of the exception - such as class name creating the exception
     * are local values so that the implementation of the server is not exposed.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param errorMessageId unique identifier for the message
     * @param errorMessageParameters parameters that were inserted in the message
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtErrorClassName   previous error causing this exception
     * @param duplicatePosts list of profiles for the same employee number
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public AppointmentIdNotUniqueException(int                              httpCode,
                                           String                           className,
                                           String                           actionDescription,
                                           String                           errorMessage,
                                           String                           errorMessageId,
                                           String[]                         errorMessageParameters,
                                           String                           systemAction,
                                           String                           userAction,
                                           String                           caughtErrorClassName,
                                           List<EntityDetail>               duplicatePosts,
                                           Map<String, Object>              relatedProperties)
    {
        super(httpCode,
              className,
              actionDescription,
              errorMessage,
              errorMessageId,
              errorMessageParameters,
              systemAction,
              userAction,
              caughtErrorClassName,
              relatedProperties);

        this.duplicatePosts = duplicatePosts;
    }


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
    @Deprecated
    public AppointmentIdNotUniqueException(int                   httpCode,
                                           String                className,
                                           String                actionDescription,
                                           String                errorMessage,
                                           String                systemAction,
                                           String                userAction,
                                           List<EntityDetail>    duplicatePosts)
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
    @Deprecated
    public AppointmentIdNotUniqueException(int                   httpCode,
                                           String                className,
                                           String                actionDescription,
                                           String                errorMessage,
                                           String                systemAction,
                                           String                userAction,
                                           Throwable             caughtError,
                                           List<EntityDetail>    duplicatePosts)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.duplicatePosts = duplicatePosts;
    }


    /**
     * Return the list of personal profiles that have a duplicate name.
     *
     * @return list of profiles
     */
    public List<EntityDetail> getDuplicatePosts()
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
        return "AppointmentIdNotUniqueException{" +
                "duplicatePosts=" + duplicatePosts +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", reportedErrorMessage='" + getReportedErrorMessage() + '\'' +
                ", reportedErrorMessageId='" + getReportedErrorMessageId() + '\'' +
                ", reportedErrorMessageParameters=" + Arrays.toString(getReportedErrorMessageParameters()) +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                ", reportedCaughtExceptionClassName='" + getReportedCaughtExceptionClassName() + '\'' +
                ", relatedProperties=" + getRelatedProperties() +
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
        if (!(objectToCompare instanceof AppointmentIdNotUniqueException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AppointmentIdNotUniqueException that = (AppointmentIdNotUniqueException) objectToCompare;
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
