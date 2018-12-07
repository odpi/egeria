/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;


import java.util.Objects;

/**
 * The UnrecognizedGUIDException is thrown by the OMAS when the GUID passed to it is not valid.
 * It may be because the parameter is null, and invalid GUID or the GUID of something other than the expected type.
 */
public class UnrecognizedGUIDException extends AssetConsumerCheckedExceptionBase
{
    private  String   expectedTypeName;
    private  String   guid;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param expectedTypeName the expect type of the metadata element that corresponds to the guid
     * @param guid the supplied guid that is in error
     */
    public UnrecognizedGUIDException(int    httpCode,
                                     String className,
                                     String actionDescription,
                                     String errorMessage,
                                     String systemAction,
                                     String userAction,
                                     String expectedTypeName,
                                     String guid)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.expectedTypeName = expectedTypeName;
        this.guid = guid;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode - http response code to use if this exception flows over a rest call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     * @param caughtError - the error that resulted in this exception.
     * */
    public UnrecognizedGUIDException(int       httpCode,
                                     String    className,
                                     String    actionDescription,
                                     String    errorMessage,
                                     String    systemAction,
                                     String    userAction,
                                     Throwable caughtError,
                                     String    expectedTypeName,
                                     String    guid)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.expectedTypeName = expectedTypeName;
        this.guid = guid;
    }


    /**
     * Return the type name of the element expected to be retrieved.
     *
     * @return string name
     */
    public String getExpectedTypeName()
    {
        return expectedTypeName;
    }


    /**
     * Return the guid supplied on the request that failed.
     *
     * @return string identifier
     */
    public String getGUID()
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
        return "UnrecognizedGUIDException{" +
                "expectedTypeName='" + expectedTypeName + '\'' +
                ", guid='" + guid + '\'' +
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
        if (!(objectToCompare instanceof UnrecognizedGUIDException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        UnrecognizedGUIDException that = (UnrecognizedGUIDException) objectToCompare;
        return Objects.equals(getExpectedTypeName(), that.getExpectedTypeName()) &&
                Objects.equals(guid, that.guid);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getExpectedTypeName(), guid);
    }
}
