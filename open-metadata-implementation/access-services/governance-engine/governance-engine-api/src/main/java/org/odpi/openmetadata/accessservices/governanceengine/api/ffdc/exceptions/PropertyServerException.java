/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions;


/**
 * The UserNotAuthorizedException is thrown by the Governance Engine OMAS when a property access has problems
 */
public class PropertyServerException extends GovernanceEngineCheckedExceptionBase {
    /**
     * This is the typical constructor used for creating a UserNotAuthorizedException.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     */
    public PropertyServerException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a UserNotAuthorizedException that resulted from a previous error.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     * @param caughtError       - the error that resulted in this exception.
     */
    public PropertyServerException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
