/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Validate that the unrecognized connection URL exception is properly populated and supports toString, hashCode and
 * equals.
 */
public class UnrecognizedConnectionURLExceptionTest
{
    private int       reportedHTTPCode           = 404;
    private String    reportingClassName         = "TestClassName";
    private String    reportingActionDescription = "TestActionDescription";
    private String    reportedErrorMessage       = "TestErrorMessage";
    private String    reportedSystemAction       = "TestSystemAction";
    private String    reportedUserAction         = "TestUserAction";
    private String    connectionURL              = "TestConnectionURL";
    private Throwable reportedCaughtException    = new Exception("TestReportedCaughtException");

    /**
     * Constructor
     */
    public UnrecognizedConnectionURLExceptionTest()
    {
    }


    /**
     * Test that a new exception is properly populated
     */
    @Test
    public void testNewException()
    {
        UnrecognizedConnectionURLException exception = new UnrecognizedConnectionURLException(reportedHTTPCode,
                                                                                              reportingClassName,
                                                                                              reportingActionDescription,
                                                                                              reportedErrorMessage,
                                                                                              reportedSystemAction,
                                                                                              reportedUserAction,
                                                                                              null);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertTrue(exception.getReportedCaughtException() == null);
        assertTrue(exception.getConnectionURL() == null);
    }


    /**
     * Test that a caught exception is properly wrapped
     */
    @Test
    public void testWrappingException()
    {
        UnrecognizedConnectionURLException exception = new UnrecognizedConnectionURLException(reportedHTTPCode,
                                                                                              reportingClassName,
                                                                                              reportingActionDescription,
                                                                                              reportedErrorMessage,
                                                                                              reportedSystemAction,
                                                                                              reportedUserAction,
                                                                                              reportedCaughtException,
                                                                                              connectionURL);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertFalse(exception.getReportedCaughtException().equals(null));
        assertTrue(exception.getReportedCaughtException().getMessage().equals("TestReportedCaughtException"));
        assertTrue(exception.getConnectionURL().equals(connectionURL));
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     */
    @Test
    public void testHashCode()
    {
        UnrecognizedConnectionURLException exception = new UnrecognizedConnectionURLException(reportedHTTPCode,
                                                                                              reportingClassName,
                                                                                              reportingActionDescription,
                                                                                              reportedErrorMessage,
                                                                                              reportedSystemAction,
                                                                                              reportedUserAction,
                                                                                              connectionURL);

        UnrecognizedConnectionURLException exception2 = new UnrecognizedConnectionURLException(reportedHTTPCode,
                                                                                               reportingClassName,
                                                                                               reportingActionDescription,
                                                                                               reportedErrorMessage,
                                                                                               reportedSystemAction,
                                                                                               reportedUserAction,
                                                                                               reportedCaughtException,
                                                                                               connectionURL);

        UnrecognizedConnectionURLException exception3 = new UnrecognizedConnectionURLException(reportedHTTPCode,
                                                                                               reportingClassName,
                                                                                               reportingActionDescription,
                                                                                               reportedErrorMessage,
                                                                                               reportedSystemAction,
                                                                                               reportedUserAction,
                                                                                               reportedCaughtException,
                                                                                               connectionURL);


        assertTrue(exception.hashCode() == exception.hashCode());
        assertFalse(exception.hashCode() == exception2.hashCode());

        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(reportedCaughtException));
        assertFalse(exception.equals(exception2));
        assertTrue(exception2.equals(exception3));

        assertTrue(exception.toString().contains("UnrecognizedConnectionURLException"));

        assertTrue(exception.toString().equals(exception.toString()));
        assertFalse(exception.toString().equals(exception2.toString()));

        assertTrue(exception.getConnectionURL().equals(exception2.getConnectionURL()));

    }
}
