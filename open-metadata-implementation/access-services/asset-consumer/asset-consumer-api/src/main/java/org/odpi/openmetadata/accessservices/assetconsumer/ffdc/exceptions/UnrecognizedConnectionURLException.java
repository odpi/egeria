/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;

import java.util.Objects;

/**
 * The UnrecognizedConnectionURLException is thrown by the Asset Consumer OMAS when a connection URL used
 * to request a Connection or Connector is not recognized and so the Asset Consumer OMAS is not able to determine
 * which connection to use.
 */
public class UnrecognizedConnectionURLException extends AssetConsumerCheckedExceptionBase
{
    private String   connectionURL;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param connectionURL url for the connection
     */
    public UnrecognizedConnectionURLException(int    httpCode,
                                              String className,
                                              String actionDescription,
                                              String errorMessage,
                                              String systemAction,
                                              String userAction,
                                              String connectionURL)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.connectionURL = connectionURL;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     * @param caughtError  the error that resulted in this exception.
     * @param connectionURL url for the connection
     */
    public UnrecognizedConnectionURLException(int       httpCode,
                                              String    className,
                                              String    actionDescription,
                                              String    errorMessage,
                                              String    systemAction,
                                              String    userAction,
                                              Throwable caughtError,
                                              String    connectionURL)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.connectionURL = connectionURL;
    }


    /**
     * Return the invalid connection URL
     *
     * @return url
     */
    public String getConnectionURL()
    {
        return connectionURL;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "UnrecognizedConnectionURLException{" +
                "connectionURL='" + connectionURL + '\'' +
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
        if (!(objectToCompare instanceof UnrecognizedConnectionURLException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        UnrecognizedConnectionURLException that = (UnrecognizedConnectionURLException) objectToCompare;
        return Objects.equals(getConnectionURL(), that.getConnectionURL());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getConnectionURL());
    }
}
