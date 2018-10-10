/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;


import java.util.Objects;

/**
 * The NotAnAssetException is thrown by the Asset Consumer OMAS when the unique identifier (guid)
 * used to identify an asset object from the property server is either unrecognized, or is the identifier
 * for a different type of object.
 */
public class NotAnAssetException extends AssetConsumerCheckedExceptionBase
{
    private String guid;

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
    public NotAnAssetException(int    httpCode,
                               String className,
                               String actionDescription,
                               String errorMessage,
                               String systemAction,
                               String userAction,
                               String guid)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.guid = guid;
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
     * @param guid guid in error
     */
    public NotAnAssetException(int       httpCode,
                               String    className,
                               String    actionDescription,
                               String    errorMessage,
                               String    systemAction,
                               String    userAction,
                               Throwable caughtError,
                               String    guid)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.guid = guid;

    }


    /**
     * Return the GUID in error
     *
     * @return string identifier
     */
    public String getGuid()
    {
        return guid;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "NotAnAssetException{" +
                "guid='" + guid + '\'' +
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
        if (!(objectToCompare instanceof NotAnAssetException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        NotAnAssetException that = (NotAnAssetException) objectToCompare;
        return Objects.equals(getGuid(), that.getGuid());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getGuid());
    }
}
