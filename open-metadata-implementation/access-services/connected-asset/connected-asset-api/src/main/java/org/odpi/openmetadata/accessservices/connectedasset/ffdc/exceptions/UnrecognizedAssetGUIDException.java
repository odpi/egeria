/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions;


import java.util.Objects;

/**
 * The UnrecognizedConnectionGUIDException is thrown by the Connected Asset OMAS when the unique identifier (guid)
 * used to request a connection object from the property server is either unrecognized, or is the identifier
 * for a different type of object.
 */
public class UnrecognizedAssetGUIDException extends ConnectedAssetCheckedExceptionBase
{
    private String assetGUID;

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
    public UnrecognizedAssetGUIDException(int    httpCode,
                                          String className,
                                          String actionDescription,
                                          String errorMessage,
                                          String systemAction,
                                          String userAction,
                                          String assetGUID)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.assetGUID = assetGUID;
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
     * @param assetGUID guid in error
     */
    public UnrecognizedAssetGUIDException(int       httpCode,
                                          String    className,
                                          String    actionDescription,
                                          String    errorMessage,
                                          String    systemAction,
                                          String    userAction,
                                          Throwable caughtError,
                                          String assetGUID)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.assetGUID = assetGUID;

    }


    /**
     * Return the GUID in error
     *
     * @return string identifier
     */
    public String getAssetGUID()
    {
        return assetGUID;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "UnrecognizedAssetGUIDException{" +
                "assetGUID='" + assetGUID + '\'' +
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
        if (!(objectToCompare instanceof UnrecognizedAssetGUIDException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        UnrecognizedAssetGUIDException that = (UnrecognizedAssetGUIDException) objectToCompare;
        return Objects.equals(getAssetGUID(), that.getAssetGUID());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAssetGUID());
    }
}
