/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions;


public class InvalidEventException extends GovernanceEngineCheckedExceptionBase {

    /**
     * This is the constructor used for creating a InvalidEventException that resulted from a previous error.
     * @param reportedHTTPCode  - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     */

    public InvalidEventException(int reportedHTTPCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction) {
        super(reportedHTTPCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a InvalidEventException that resulted from a previous error.
     *
     * @param reportedHTTPCode  - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     * @param caughtError       - the error that resulted in this exception.
     */
    public InvalidEventException(int reportedHTTPCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError) {
        super(reportedHTTPCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }

}
