/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.exception;

/**
 * The ClassificationNotFoundException is thrown by the Asset Catalog OMAS when the classification passed on a request
 * is not found in the repository
 */
public class ClassificationNotFoundException extends AssetCatalogException {

    /**
     * This is the typical constructor used for creating a ClassificationNotFoundException.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     */
    public ClassificationNotFoundException(int httpCode,
                                           String className,
                                           String actionDescription,
                                           String errorMessage,
                                           String systemAction,
                                           String userAction) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }

    /**
     * This is the constructor used for creating a ClassificationNotFoundException that resulted from a previous error.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     * @param caughtError       the error that resulted in this exception.
     */
    public ClassificationNotFoundException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
