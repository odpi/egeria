/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.ffdc;

/**
 * CaughtException is used when an unexpected exception occurs during a test.
 */
public class CaughtException extends ComplianceException
{
    Throwable    caughtException;

    /**
     * The constructor captures the context of the exception.
     *
     * @param errorMessage description of where exception occurred
     * @param caughtException exception that was caught.
     */
    public CaughtException(String errorMessage, Throwable caughtException)
    {
        super(errorMessage);

        this.caughtException = caughtException;
    }


    /**
     * Return the caught exception.
     *
     * @return exception object
     */
    public Throwable getCaughtException()
    {
        return caughtException;
    }
}
