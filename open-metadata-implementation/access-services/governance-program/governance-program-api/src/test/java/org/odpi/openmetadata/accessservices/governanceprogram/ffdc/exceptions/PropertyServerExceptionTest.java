/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Validate that the property server exception is properly populated and supports toString, hashCode and
 * equals.
 */
public class PropertyServerExceptionTest
{
    private int       reportedHTTPCode           = 404;
    private String    reportingClassName         = "TestClassName";
    private String    reportingActionDescription = "TestActionDescription";
    private String    reportedErrorMessage       = "TestErrorMessage";
    private String    reportedSystemAction       = "TestSystemAction";
    private String    reportedUserAction         = "TestUserAction";
    private Throwable reportedCaughtException    = new Exception("TestReportedCaughtException");

    /**
     * Constructor
     */
    public PropertyServerExceptionTest()
    {
    }


    /**
     * Test that a new exception is properly populated
     */
    @Test public void testNewException()
    {
        PropertyServerException exception = new PropertyServerException(reportedHTTPCode,
                                                                        reportingClassName,
                                                                        reportingActionDescription,
                                                                        reportedErrorMessage,
                                                                        reportedSystemAction,
                                                                        reportedUserAction);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertTrue(exception.getReportedCaughtException() == null);
    }


    /**
     * Test that a caught exception is properly wrapped
     */
    @Test public void testWrappingException()
    {
        PropertyServerException exception = new PropertyServerException(reportedHTTPCode,
                                                                        reportingClassName,
                                                                        reportingActionDescription,
                                                                        reportedErrorMessage,
                                                                        reportedSystemAction,
                                                                        reportedUserAction,
                                                                        reportedCaughtException);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertFalse(exception.getReportedCaughtException().equals(null));
        assertTrue(exception.getReportedCaughtException().getMessage().equals("TestReportedCaughtException"));
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     */
    @Test public void testHashCode()
    {
        PropertyServerException exception = new PropertyServerException(reportedHTTPCode,
                                                                        reportingClassName,
                                                                        reportingActionDescription,
                                                                        reportedErrorMessage,
                                                                        reportedSystemAction,
                                                                        reportedUserAction);

        PropertyServerException exception2 = new PropertyServerException(reportedHTTPCode,
                                                                         reportingClassName,
                                                                         reportingActionDescription,
                                                                         reportedErrorMessage,
                                                                         reportedSystemAction,
                                                                         reportedUserAction,
                                                                         reportedCaughtException);

        PropertyServerException exception3 = new PropertyServerException(reportedHTTPCode,
                                                                         reportingClassName,
                                                                         reportingActionDescription,
                                                                         reportedErrorMessage,
                                                                         reportedSystemAction,
                                                                         reportedUserAction,
                                                                         reportedCaughtException);


        assertTrue(exception.hashCode() == exception.hashCode());
        assertFalse(exception.hashCode() == exception2.hashCode());

        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(reportedCaughtException));
        assertFalse(exception.equals(exception2));
        assertTrue(exception2.equals(exception3));

        assertTrue(exception.toString().contains("PropertyServerException"));

        assertTrue(exception.toString().equals(exception.toString()));
        assertFalse(exception.toString().equals(exception2.toString()));
    }
}
