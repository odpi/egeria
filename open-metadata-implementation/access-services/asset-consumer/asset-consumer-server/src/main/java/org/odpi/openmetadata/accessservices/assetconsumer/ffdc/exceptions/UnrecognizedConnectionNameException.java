/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;

/**
 * The UnrecognizedConnectionNameException is thrown by the Asset Consumer OMAS when a connection name used
 * to request a Connection or Connector is not recognized and so the Asset Consumer OMAS is not able to determine
 * which connection to use.
 */
public class UnrecognizedConnectionNameException extends AssetConsumerCheckedExceptionBase
{
    /**
     * This is the typical constructor used for creating a UnrecognizedConnectionNameException.
     *
     * @param httpCode - http response code to use if this exception flows over a rest call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     */
    public UnrecognizedConnectionNameException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a UnrecognizedConnectionNameException that resulted from a previous error.
     *
     * @param httpCode - http response code to use if this exception flows over a rest call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     * @param caughtError - the error that resulted in this exception.
     */
    public UnrecognizedConnectionNameException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
