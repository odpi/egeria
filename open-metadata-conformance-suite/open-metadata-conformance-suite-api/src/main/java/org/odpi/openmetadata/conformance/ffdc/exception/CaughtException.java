/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.ffdc.exception;

/**
 * CaughtException is used when an unexpected exception occurs during a test.
 */
public class CaughtException extends ConformanceException
{
    private static final long    serialVersionUID = 1L;

    private final Exception    caughtException;

    /**
     * The constructor captures the context of the exception.
     *
     * @param errorMessage description of where exception occurred
     * @param caughtException exception that was caught.
     */
    public CaughtException(String errorMessage, Exception caughtException)
    {
        super(errorMessage);

        this.caughtException = caughtException;
    }


    /**
     * Return the caught exception.
     *
     * @return exception object
     */
    public Exception getCaughtException()
    {
        return caughtException;
    }
}
