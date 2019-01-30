/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetlineage.exceptions;

/**
 * The PropertyServerException is thrown by the Asset Lineage OMAS when it is not able to communicate with the
 * property server.
 */
public class PropertyServerException extends AssetLineageExceptionBase {
    /**
     * This is the typical constructor used for creating a PropertyServerException.
     *
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     */
    public PropertyServerException(String className,
                                   String actionDescription,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction) {
        super(503, className, actionDescription, errorMessage, systemAction, userAction);
    }
}