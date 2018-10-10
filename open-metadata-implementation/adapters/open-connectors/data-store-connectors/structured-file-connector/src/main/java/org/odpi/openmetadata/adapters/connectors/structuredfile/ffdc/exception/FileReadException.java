/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.structuredfile.ffdc.exception;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.Objects;

/**
 * The FileReadException is thrown by the connector when it is not possible to retrieve the requested
 * record.
 */
public class FileReadException extends ConnectorCheckedException
{
    private   String fileName;

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
    public FileReadException(int    httpCode,
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
    public FileReadException(int       httpCode,
                             String    className,
                             String    actionDescription,
                             String    errorMessage,
                             String    systemAction,
                             String    userAction,
                             Throwable caughtError,
                             String fileName)
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
        return "FileReadException{" +
                "fileName='" + fileName + '\'' +
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
        if (!(objectToCompare instanceof FileReadException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        FileReadException that = (FileReadException) objectToCompare;
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
