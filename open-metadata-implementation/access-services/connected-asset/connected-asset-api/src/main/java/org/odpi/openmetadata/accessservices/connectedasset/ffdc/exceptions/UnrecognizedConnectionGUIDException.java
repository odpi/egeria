/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions;


import java.util.Objects;

/**
 * The UnrecognizedConnectionGUIDException is thrown by the Connected Asset OMAS when the unique identifier (guid)
 * used to request a connection object from the property server is either unrecognized, or is the identifier
 * for a different type of object.
 */
public class UnrecognizedConnectionGUIDException extends ConnectedAssetCheckedExceptionBase
{
    private String connectionGUID;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     */
    public UnrecognizedConnectionGUIDException(int    httpCode,
                                               String className,
                                               String actionDescription,
                                               String errorMessage,
                                               String systemAction,
                                               String userAction,
                                               String connectionGUID)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.connectionGUID = connectionGUID;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param caughtError the error that resulted in this exception.
     * @param connectionGUID guid in error
     */
    public UnrecognizedConnectionGUIDException(int       httpCode,
                                               String    className,
                                               String    actionDescription,
                                               String    errorMessage,
                                               String    systemAction,
                                               String    userAction,
                                               Throwable caughtError,
                                               String    connectionGUID)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.connectionGUID = connectionGUID;

    }


    /**
     * Return the GUID in error
     *
     * @return string identifier
     */
    public String getConnectionGUID()
    {
        return connectionGUID;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "UnrecognizedConnectionGUIDException{" +
                "connectionGUID='" + connectionGUID + '\'' +
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
        if (!(objectToCompare instanceof UnrecognizedConnectionGUIDException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        UnrecognizedConnectionGUIDException that = (UnrecognizedConnectionGUIDException) objectToCompare;
        return Objects.equals(getConnectionGUID(), that.getConnectionGUID());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getConnectionGUID());
    }
}
