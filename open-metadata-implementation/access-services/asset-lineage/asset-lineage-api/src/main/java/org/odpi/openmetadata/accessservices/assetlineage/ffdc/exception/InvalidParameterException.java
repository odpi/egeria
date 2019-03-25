/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception;

/**
 * The InvalidParameterException is thrown by the Asset Lineage OMAS when a parameter passed on a request is
 * invalid and the action can not be performed without it.
 */
public class InvalidParameterException extends AssetLineageException {

    /**
     * This is the typical constructor used for creating a InvalidParameterException.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     */
    public InvalidParameterException(int httpCode, String className, String actionDescription,
                                     String errorMessage, String systemAction, String userAction) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }
}
