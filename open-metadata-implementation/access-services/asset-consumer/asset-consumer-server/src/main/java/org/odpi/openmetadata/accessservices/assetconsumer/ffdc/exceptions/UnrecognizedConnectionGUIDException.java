/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;


/**
 * The UnrecognizedConnectionGUIDException is thrown by the Asset Consumer OMAS when the unique identifier (guid)
 * used to request a connection object from the property server is either unrecognized, or is the identifier
 * for a different type of object.
 */
public class UnrecognizedConnectionGUIDException extends AssetConsumerCheckedExceptionBase
{
    /**
     * This is the typical constructor used for creating a UnrecognizedConnectionGUIDException.
     *
     * @param httpCode - http response code to use if this exception flows over a rest call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     */
    public UnrecognizedConnectionGUIDException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a UnrecognizedConnectionGUIDException that resulted from a previous error.
     *
     * @param httpCode - http response code to use if this exception flows over a rest call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     * @param caughtError - the error that resulted in this exception.
     */
    public UnrecognizedConnectionGUIDException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
