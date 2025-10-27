/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.io.Serial;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * The InvalidParameterException is thrown by the connector when a parameter is null or an invalid value.
 */
public class InvalidParameterException extends OMFCheckedExceptionBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Parameter in error.
     */
    private   String  parameterName;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param parameterName name of the invalid parameter if known
     */
    public InvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                     String                     className,
                                     String                     actionDescription,
                                     String                     parameterName)
    {
        super(messageDefinition, className, actionDescription);

        this.parameterName = parameterName;
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param parameterName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public InvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                     String                     className,
                                     String                     actionDescription,
                                     String                     parameterName,
                                     Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);

        this.parameterName = parameterName;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the error that resulted in this exception.
     * @param parameterName name of the invalid parameter if known
     */
    public InvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                     String                     className,
                                     String                     actionDescription,
                                     Exception                  caughtError,
                                     String                     parameterName)
    {
        super(messageDefinition, className, actionDescription, caughtError);

        this.parameterName = parameterName;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the error that resulted in this exception.
     * @param parameterName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public InvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                     String                     className,
                                     String                     actionDescription,
                                     Exception                  caughtError,
                                     String                     parameterName,
                                     Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);

        this.parameterName = parameterName;
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
     * @param parameterName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public InvalidParameterException(int                 httpCode,
                                     String              className,
                                     String              actionDescription,
                                     String              errorMessage,
                                     String              errorMessageId,
                                     String[]            errorMessageParameters,
                                     String              systemAction,
                                     String              userAction,
                                     String              caughtErrorClassName,
                                     String              parameterName,
                                     Map<String, Object> relatedProperties)
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

        this.parameterName = parameterName;
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param errorMessage message for the exception
     * @param template   object to copy
     */
    public InvalidParameterException(String                    errorMessage,
                                     InvalidParameterException template)
    {
        super(errorMessage, template);

        if (template != null)
        {
            this.parameterName = template.getParameterName();
        }
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param template   object to copy
     */
    public InvalidParameterException(InvalidParameterException template)
    {
        super(template);

        if (template != null)
        {
            this.parameterName = template.getParameterName();
        }
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param errorMessage message for the exception
     * @param template   object to copy
     * @param parameterName name of invalid parameter
     */
    public InvalidParameterException(String                  errorMessage,
                                     OMFCheckedExceptionBase template,
                                     String                  parameterName)
    {
        super(errorMessage, template);

        this.parameterName = parameterName;
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param template   object to copy
     * @param parameterName name of invalid parameter
     */
    public InvalidParameterException(OMFCheckedExceptionBase template,
                                     String                  parameterName)
    {
        super(template);

        this.parameterName = parameterName;
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
     * @param parameterName name of the invalid parameter if known
     */
    public InvalidParameterException(int    httpCode,
                                     String className,
                                     String actionDescription,
                                     String errorMessage,
                                     String systemAction,
                                     String userAction,
                                     String parameterName)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.parameterName = parameterName;
    }



    /**
     * Return the invalid parameter's name, if known.
     *
     * @return string name
     */
    public String getParameterName()
    {
        return parameterName;
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
                "parameterName='" + parameterName + '\'' +
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
        if (!(objectToCompare instanceof InvalidParameterException that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getParameterName(), that.getParameterName());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getParameterName());
    }
}
