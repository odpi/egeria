/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

public class GaianAuthorizationException extends Exception{
    private static final long serialVersionUID = 1L;
    public GaianAuthorizationException(String message) { super(message); }
    public GaianAuthorizationException(String message, Throwable exception) {
        super(message, exception);
    }

    public GaianAuthorizationException(String message, Throwable exception, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, exception, enableSuppression, writableStackTrace);
    }

}
