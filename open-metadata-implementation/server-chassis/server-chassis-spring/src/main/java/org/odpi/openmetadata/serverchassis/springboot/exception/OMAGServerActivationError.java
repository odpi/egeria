/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Custom exception definition used for managing known server start-up error scenarios.
 * The application cannot recover from this error.
 */
public class OMAGServerActivationError extends Exception {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * Constructs error object by describing the event with message.
     * @param errorMessage text message describing the error
     */
    public OMAGServerActivationError(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Constructs error object by describing the event with message and providing the original cause of the error.
     * @param errorMessage text message describing the error
     * @param cause the error cause as instance of java.lang.Throwable
     */
    public OMAGServerActivationError(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
