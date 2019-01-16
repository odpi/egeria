/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;


import java.util.Objects;

/**
 * The NoConnectedAssetException is thrown by the Asset Consumer OMAS when there is no asset linked to
 * the supplied connection object.
 */
public class NoConnectedAssetException extends AssetConsumerCheckedExceptionBase
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
    public NoConnectedAssetException(int    httpCode,
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
    public NoConnectedAssetException(int       httpCode,
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
        return "NoConnectedAssetException{" +
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
        if (!(objectToCompare instanceof NoConnectedAssetException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        NoConnectedAssetException that = (NoConnectedAssetException) objectToCompare;
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
