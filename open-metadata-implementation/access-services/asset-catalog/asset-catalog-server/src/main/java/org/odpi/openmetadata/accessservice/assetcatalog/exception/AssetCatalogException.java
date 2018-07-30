/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.exception;

import lombok.Getter;

/**
 * AssetCatalogException provides a checked exception for reporting errors found when using
 * the Asset Catalog OMAS services.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or power AssetConsumerInterface.  However, there may be the odd bug that surfaces here.
 * The AssetCatalogErrorCode can be used with this exception to populate it with standard messages.
 * The aim is to be able to uniquely identify the cause and remedy for the error.
 */
@Getter
public class AssetCatalogException extends Exception {

    /**
     * The HTTP response code to use with this exception.
     */
    private int reportedHTTPCode;

    /**
     * The class that created this exception.
     */
    private String reportingClassName;

    /**
     * The type of request that the class was performing when the condition occurred that resulted in this
     * exception.
     */
    private String reportingActionDescription;

    /**
     * A formatted short description of the cause of the condition that resulted in this exception.
     */
    private String reportedErrorMessage;

    /**
     * A description of the action that the system took as a result of the error condition.
     */
    private String reportedSystemAction;

    /**
     * A description of the action necessary to correct the error.
     */
    private String reportedUserAction;

    /**
     * An exception that was caught and wrapped by this exception.  If a null is returned, then this exception is
     * newly created and not the result of a previous exception.
     */
    private Throwable reportedCaughtException;


    /**
     * This is the typical constructor used for creating a ConnectionCheckedException.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     */
    public AssetCatalogException(int httpCode,
                                 String className,
                                 String actionDescription,
                                 String errorMessage,
                                 String systemAction,
                                 String userAction) {
        super(errorMessage);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
    }


    /**
     * This is the  constructor used for creating a ConnectionCheckedException that resulted from a previous error.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     * @param caughtError       - the error that resulted in this exception.
     */
    public AssetCatalogException(int httpCode,
                                 String className,
                                 String actionDescription,
                                 String errorMessage,
                                 String systemAction,
                                 String userAction,
                                 Throwable caughtError) {
        super(errorMessage, caughtError);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
    }
}
