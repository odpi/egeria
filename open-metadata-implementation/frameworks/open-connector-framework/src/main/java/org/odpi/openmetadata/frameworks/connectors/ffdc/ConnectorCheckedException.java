/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.ffdc;


import org.apache.log4j.Logger;

/**
 * ConnectorCheckedException provides a checked exception for reporting errors found when using OCF connectors.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or power user.  However, there may be the odd bug that surfaces here. The OCFErrorCode can be used with
 * this exception to populate it with standard messages.  Otherwise messages defined uniquely for a
 * ConnectorProvider/Connector implementation can be used.  The aim is to be able to uniquely identify the cause
 * and remedy for the error.
 */
public class ConnectorCheckedException extends OCFCheckedExceptionBase
{
    private static final Logger log = Logger.getLogger(ConnectorCheckedException.class);

    /**
     * This is the typical constructor used for creating a ConnectorCheckedException.
     *
     * @param httpCode http response code to use if this exception flows over a REST call
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     */
    public ConnectorCheckedException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        if (log.isDebugEnabled())
        {
            log.debug(httpCode + ", " + className + ", " + actionDescription);
        }
    }


    /**
     * This is the constructor used for creating a ConnectorCheckedException in response to a previous exception.
     *
     * @param httpCode http response code to use if this exception flows over a REST call
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     */
    public ConnectorCheckedException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        if (log.isDebugEnabled())
        {
            log.debug(httpCode + ", " + className + ", " + actionDescription + ", " + caughtError.toString());
        }
    }
}