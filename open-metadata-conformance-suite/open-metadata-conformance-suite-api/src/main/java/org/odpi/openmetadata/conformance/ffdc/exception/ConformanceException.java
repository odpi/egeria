/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.ffdc.exception;

/**
 * Conformance exception provides a base class for exceptions that indicate there is a failure in the
 * conformance tests.
 */
public class ConformanceException extends Exception
{
    private static final long    serialVersionUID = 1L;

    /**
     * Normal constructor for a conformance exception
     *
     * @param errorMessage description of the exception
     */
    public ConformanceException(String   errorMessage)
    {
        super(errorMessage);
    }


    /**
     * Catch block constructor for a conformance exception
     *
     * @param errorMessage description of the exception
     * @param caughtException the exception
     */
    public ConformanceException(String    errorMessage,
                                Exception caughtException)
    {
        super(errorMessage, caughtException);
    }
}
