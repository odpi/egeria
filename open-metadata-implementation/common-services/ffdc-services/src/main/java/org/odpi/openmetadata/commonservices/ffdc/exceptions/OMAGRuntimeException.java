/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;

import java.io.Serial;
import java.util.Arrays;
import java.util.Map;

/**
 * OMAGRuntimeException is used for all runtime exceptions generated by one of the Open Metadata and Governance
 * (OMAG) Services.
 */
public class OMAGRuntimeException extends OMFRuntimeException
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an OMAGRuntimeException.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public OMAGRuntimeException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor used for creating an OMAGRuntimeException.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OMAGRuntimeException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This is the constructor used for creating an OMAGRuntimeException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public OMAGRuntimeException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Exception                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating an OMAGRuntimeException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OMAGRuntimeException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Exception                  caughtError,
                                Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    @Deprecated
    public OMAGRuntimeException(int                 httpCode,
                                String              className,
                                String              actionDescription,
                                String              errorMessage,
                                String              systemAction,
                                String              userAction,
                                Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, relatedProperties);
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been
     * caught.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param caughtError        previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    @Deprecated
    public OMAGRuntimeException(int                 httpCode,
                                String              className,
                                String              actionDescription,
                                String              errorMessage,
                                String              systemAction,
                                String              userAction,
                                Exception           caughtError,
                                Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, relatedProperties);
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "OMAGRuntimeException{" +
                "reportedHTTPCode=" + getReportedHTTPCode() +
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
}
