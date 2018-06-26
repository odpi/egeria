/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;

/**
 * The UnrecognizednNameException is thrown by the Subject Area OMAS when a name used
 * to request an object is not recognized and so the Subject Area OMAS is not able to determine
 * which object to use.
 */
public class UnrecognizednNameException extends SubjectAreaCheckedExceptionBase {
    /**
     * This is the typical constructor used for creating a UnrecognizedNameException.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     */
    public UnrecognizednNameException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a UnrecognizedNameException that resulted from a previous error.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     * @param caughtError       - the error that resulted in this exception.
     */
    public UnrecognizednNameException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
