/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions;


/**
 * The UnrecognizedAssetGUIDException is thrown by the ConnectedAsset OMAS when the AssetGUID passed to it is not valid.
 * It may be because the parameter is null, and invalid GUID or the GUID of something other than an Asset.
 */
public class UnrecognizedAssetGUIDException extends ConnectedAssetCheckedExceptionBase
{
    /**
     * This is the typical constructor used for creating a UnrecognizedAssetGUIDException.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     */
    public UnrecognizedAssetGUIDException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a UnrecognizedAssetGUIDException that resulted from a previous error.
     *
     * @param httpCode - http response code to use if this exception flows over a rest call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     * @param caughtError - the error that resulted in this exception.
     * */
    public UnrecognizedAssetGUIDException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
