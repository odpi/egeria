/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;

/**
 * The PropertyServerException is thrown by the Information View OMAS when it is not able to communicate with the
 * property server.
 */
public class PropertyServerException extends InformationViewExceptionBase {

    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating a PropertyServerException.
     *
     * @param className         name of class reporting error
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     */
    public PropertyServerException(String className,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction) {
        super(503, className, errorMessage, systemAction, userAction, null);
    }
}