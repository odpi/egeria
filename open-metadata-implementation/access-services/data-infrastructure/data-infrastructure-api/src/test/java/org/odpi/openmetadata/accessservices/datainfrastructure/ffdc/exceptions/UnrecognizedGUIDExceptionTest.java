/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.datainfrastructure.ffdc.exceptions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Validate that the unrecognized guid exception is properly populated and supports toString, hashCode and
 * equals.
 */
public class UnrecognizedGUIDExceptionTest
{
    private int       reportedHTTPCode           = 404;
    private String    reportingClassName         = "TestClassName";
    private String    reportingActionDescription = "TestActionDescription";
    private String    reportedErrorMessage       = "TestErrorMessage";
    private String    reportedSystemAction       = "TestSystemAction";
    private String    reportedUserAction         = "TestUserAction";
    private String    requestedGUID              = "TestGUID";
    private String    expectedTypeName           = "TestTypeName";
    private Throwable reportedCaughtException    = new Exception("TestReportedCaughtException");

    /**
     * Constructor
     */
    public UnrecognizedGUIDExceptionTest()
    {
    }


    /**
     * Test that a new exception is properly populated
     */
    @Test
    public void testNewException()
    {
        UnrecognizedGUIDException exception = new UnrecognizedGUIDException(reportedHTTPCode,
                                                                            reportingClassName,
                                                                            reportingActionDescription,
                                                                            reportedErrorMessage,
                                                                            reportedSystemAction,
                                                                            reportedUserAction,
                                                                            null,
                                                                            null);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertTrue(exception.getReportedCaughtException() == null);
        assertTrue(exception.getGUID() == null);
        assertTrue(exception.getExpectedTypeName() == null);
    }


    /**
     * Test that a caught exception is properly wrapped
     */
    @Test
    public void testWrappingException()
    {
        UnrecognizedGUIDException exception = new UnrecognizedGUIDException(reportedHTTPCode,
                                                                            reportingClassName,
                                                                            reportingActionDescription,
                                                                            reportedErrorMessage,
                                                                            reportedSystemAction,
                                                                            reportedUserAction,
                                                                            reportedCaughtException,
                                                                            expectedTypeName,
                                                                            requestedGUID);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertFalse(exception.getReportedCaughtException().equals(null));
        assertTrue(exception.getReportedCaughtException().getMessage().equals("TestReportedCaughtException"));
        assertTrue(exception.getGUID().equals(requestedGUID));
        assertTrue(exception.getExpectedTypeName().equals(expectedTypeName));
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     */
    @Test
    public void testHashCode()
    {
        UnrecognizedGUIDException exception = new UnrecognizedGUIDException(reportedHTTPCode,
                                                                            reportingClassName,
                                                                            reportingActionDescription,
                                                                            reportedErrorMessage,
                                                                            reportedSystemAction,
                                                                            reportedUserAction,
                                                                            expectedTypeName,
                                                                            requestedGUID);

        UnrecognizedGUIDException exception2 = new UnrecognizedGUIDException(reportedHTTPCode,
                                                                             reportingClassName,
                                                                             reportingActionDescription,
                                                                             reportedErrorMessage,
                                                                             reportedSystemAction,
                                                                             reportedUserAction,
                                                                             reportedCaughtException,
                                                                             expectedTypeName,
                                                                             requestedGUID);

        UnrecognizedGUIDException exception3 = new UnrecognizedGUIDException(reportedHTTPCode,
                                                                             reportingClassName,
                                                                             reportingActionDescription,
                                                                             reportedErrorMessage,
                                                                             reportedSystemAction,
                                                                             reportedUserAction,
                                                                             reportedCaughtException,
                                                                             expectedTypeName,
                                                                             requestedGUID);


        assertTrue(exception.hashCode() == exception.hashCode());
        assertFalse(exception.hashCode() == exception2.hashCode());

        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(reportedCaughtException));
        assertFalse(exception.equals(exception2));
        assertTrue(exception2.equals(exception3));

        assertTrue(exception.toString().contains("UnrecognizedGUIDException"));

        assertTrue(exception.toString().equals(exception.toString()));
        assertFalse(exception.toString().equals(exception2.toString()));

        assertTrue(exception.getGUID().equals(exception2.getGUID()));
        assertTrue(exception.getExpectedTypeName().equals(exception2.getExpectedTypeName()));

    }
}
