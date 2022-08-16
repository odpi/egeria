/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * The FileException is thrown by the connector when it is not possible to retrieve the requested file.
 */
public class FileException extends ConnectorCheckedException
{
    private static final long    serialVersionUID = 1L;

    private   String fileName;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param fileName name of the invalid parameter if known
     */
    public FileException(ExceptionMessageDefinition messageDefinition,
                         String                     className,
                         String                     actionDescription,
                         String                     fileName)
    {
        super(messageDefinition, className, actionDescription);

        this.fileName = fileName;
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param fileName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public FileException(ExceptionMessageDefinition messageDefinition,
                         String                     className,
                         String                     actionDescription,
                         String                     fileName,
                         Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);

        this.fileName = fileName;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the error that resulted in this exception.
     * @param fileName name of the invalid parameter if known
     */
    public FileException(ExceptionMessageDefinition messageDefinition,
                         String                     className,
                         String                     actionDescription,
                         Exception                  caughtError,
                         String                     fileName)
    {
        super(messageDefinition, className, actionDescription, caughtError);

        this.fileName = fileName;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the error that resulted in this exception.
     * @param fileName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public FileException(ExceptionMessageDefinition messageDefinition,
                         String                     className,
                         String                     actionDescription,
                         Exception                  caughtError,
                         String                     fileName,
                         Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);

        this.fileName = fileName;
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
     * @param fileName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public FileException(int                 httpCode,
                         String              className,
                         String              actionDescription,
                         String              errorMessage,
                         String              errorMessageId,
                         String[]            errorMessageParameters,
                         String              systemAction,
                         String              userAction,
                         String              caughtErrorClassName,
                         String              fileName,
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

        this.fileName = fileName;
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
     * @param fileName name of the invalid parameter if known
     */
    @Deprecated
    public FileException(int    httpCode,
                         String className,
                         String actionDescription,
                         String errorMessage,
                         String systemAction,
                         String userAction,
                         String fileName)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.fileName = fileName;
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
     * @param fileName name of the invalid parameter if known
     */
    @Deprecated
    public FileException(int       httpCode,
                         String    className,
                         String    actionDescription,
                         String    errorMessage,
                         String    systemAction,
                         String    userAction,
                         Exception caughtError,
                         String    fileName)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.fileName = fileName;
    }


    /**
     * Return the invalid parameter's name, if known.
     *
     * @return string name
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "FileException{" +
                "fileName='" + fileName + '\'' +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", errorMessage='" + getReportedErrorMessage() + '\'' +
                ", reportedErrorMessageId='" + getReportedErrorMessageId() + '\'' +
                ", reportedErrorMessageParameters=" + Arrays.toString(getReportedErrorMessageParameters()) +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
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
        if (!(objectToCompare instanceof FileException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        FileException that = (FileException) objectToCompare;
        return Objects.equals(getFileName(), that.getFileName());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getFileName());
    }
}
