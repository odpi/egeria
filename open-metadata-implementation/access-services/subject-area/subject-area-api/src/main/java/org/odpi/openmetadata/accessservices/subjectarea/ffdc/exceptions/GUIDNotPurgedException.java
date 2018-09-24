/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


/**
 * The UIDNotDeletedException is thrown by the Subject Area OMAS when a unique identifier (guid)
 * used to purge an object - did not result in the object being purged.
 */
public class GUIDNotPurgedException extends SubjectAreaCheckedExceptionBase {
    private String guid=null;
    /**
     * This is the typical constructor used for creating a GUIDNotPurgedException.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param guid               guid of the entity that was not purged
     */
    public GUIDNotPurgedException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, String guid) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
        this.guid=guid;
    }


    /**
     * This is the constructor used for creating a GUIDNotPurgedException that resulted from a previous error.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param caughtError        the error that resulted in this exception.
     * @param guid               guid of the entity that was not purged
     */
    public GUIDNotPurgedException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, String guid, Throwable caughtError) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
        this.guid=guid;
    }

    public String getGuid() {
        return guid;
    }

}
