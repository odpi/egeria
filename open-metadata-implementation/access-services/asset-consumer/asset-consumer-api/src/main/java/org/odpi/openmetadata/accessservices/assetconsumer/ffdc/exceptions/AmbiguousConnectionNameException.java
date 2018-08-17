/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;


import java.util.Objects;

/**
 * The AmbiguousConnectionNameException is thrown by OMAS when a connection name used (typically the
 * displayName) to request a connector instance is not unique and so the OMAS is not able to determine
 * which connection to use.
 */
public class AmbiguousConnectionNameException extends AssetConsumerCheckedExceptionBase
{
    private String   connectionName;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param connectionName name of the connection passed on the request
     */
    public AmbiguousConnectionNameException(int    httpCode,
                                            String className,
                                            String actionDescription,
                                            String errorMessage,
                                            String systemAction,
                                            String userAction,
                                            String connectionName)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.connectionName = connectionName;
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
     * @param caughtError  the error that resulted in this exception.
     * */
    public AmbiguousConnectionNameException(int       httpCode,
                                            String    className,
                                            String    actionDescription,
                                            String    errorMessage,
                                            String    systemAction,
                                            String    userAction,
                                            Throwable caughtError,
                                            String    connectionName)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.connectionName = connectionName;
    }


    /**
     * Return the connection name in error.
     *
     * @return string name
     */
    public String getConnectionName()
    {
        return connectionName;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "AmbiguousConnectionNameException{" +
                "connectionName='" + connectionName + '\'' +
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
        if (!(objectToCompare instanceof AmbiguousConnectionNameException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AmbiguousConnectionNameException that = (AmbiguousConnectionNameException) objectToCompare;
        return Objects.equals(getConnectionName(), that.getConnectionName());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getConnectionName());
    }
}
