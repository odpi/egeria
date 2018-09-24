/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


/**
 * The UnrecognizedGlossaryException is thrown by the Subject Area OMAS when the unique identifier (json)
 * used to request an objects either unrecognized, or is the identifier for a different type of object.
 */
public class UnrecognizedGlossaryException extends SubjectAreaCheckedExceptionBase {
    private String json=null;
    /**
     * This is the typical constructor used for creating a UnrecognizedjsonException.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     * @param json              json is not a Glossary
     */
    public UnrecognizedGlossaryException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, String json) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
        this.json=json;
    }


    /**
     * This is the constructor used for creating a UnrecognizedjsonException that resulted from a previous error.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     * @param caughtError       the error that resulted in this exception.
     * @param json              json is not a Glossary
     */
    public UnrecognizedGlossaryException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, String json, Throwable caughtError) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
        this.json=json;
    }

    public String getJson() {
        return json;
    }

}
