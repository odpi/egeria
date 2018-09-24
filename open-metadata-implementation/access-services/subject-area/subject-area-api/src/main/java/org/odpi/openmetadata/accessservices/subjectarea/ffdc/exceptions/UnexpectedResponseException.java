/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;

/**
 * The UnexpectedResponseException is thrown by the Subject Area OMAS client when it receives an unexpected response.
 * This means that the unexpectedResponseCategory was not an expected error or the expected successful response.
 */
public class UnexpectedResponseException extends SubjectAreaCheckedExceptionBase {
    private String unexpectedResponseCategory=null;
    /**
     * This is the typical constructor used for creating a unexpectedResponseCategoryNotPurgedException.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param unexpectedResponseCategory unexpected response category
     */
    public UnexpectedResponseException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, String unexpectedResponseCategory) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
        this.unexpectedResponseCategory=unexpectedResponseCategory;
    }


    /**
     * This is the constructor used for creating a unexpectedResponseCategory.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     * @param caughtError       the error that resulted in this exception.
     * @param unexpectedResponseCategory unexpected response category
     */
    public UnexpectedResponseException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, String unexpectedResponseCategory, Throwable caughtError) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
        this.unexpectedResponseCategory=unexpectedResponseCategory;
    }

    public String getunexpectedResponseCategory() {
        return unexpectedResponseCategory;
    }

}
