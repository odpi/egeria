/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Validate that the user not authorized exception is properly populated and supports toString, hashCode and
 * equals.
 */
public class UserNotAuthorizedExceptionTest
{
    private String    reportingClassName         = "TestClassName";
    private String    reportingActionDescription = "TestActionDescription";
    private String    userId                     = "TestUserId";
    private Throwable reportedCaughtException    = new Exception("TestReportedCaughtException");

    /**
     * Constructor
     */
    public UserNotAuthorizedExceptionTest()
    {
    }


    /**
     * Test that a new exception is properly populated
     */
    @Test public void testNewException()
    {
        UserNotAuthorizedException exception = new UserNotAuthorizedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName"),
                                                                              reportingClassName,
                                                                              reportingActionDescription,
                                                                              null);

        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getReportedCaughtException() == null);
        assertTrue(exception.getUserId() == null);
    }


    /**
     * Test that a caught exception is properly wrapped
     */
    @Test public void testWrappingException()
    {
        UserNotAuthorizedException exception = new UserNotAuthorizedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName"),
                                                                              reportingClassName,
                                                                              reportingActionDescription,
                                                                              reportedCaughtException,
                                                                              userId);

        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertFalse(exception.getReportedCaughtException().equals(null));
        assertTrue(exception.getUserId().equals(userId));
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     */
    @Test public void testHashCode()
    {
        UserNotAuthorizedException exception = new UserNotAuthorizedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName"),
                                                                              reportingClassName,
                                                                              reportingActionDescription,
                                                                              userId);

        UserNotAuthorizedException exception2 = new UserNotAuthorizedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName"),
                                                                               reportingClassName,
                                                                               reportingActionDescription,
                                                                               reportedCaughtException,
                                                                               userId);

        UserNotAuthorizedException exception3 = new UserNotAuthorizedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName"),
                                                                               reportingClassName,
                                                                               reportingActionDescription,
                                                                               reportedCaughtException,
                                                                               userId);


        assertTrue(exception.hashCode() == exception.hashCode());
        assertFalse(exception.hashCode() == exception2.hashCode());

        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(reportedCaughtException));
        assertFalse(exception.equals(exception2));
        assertTrue(exception2.equals(exception3));

        assertTrue(exception.toString().contains("UserNotAuthorizedException"));

        assertTrue(exception.toString().equals(exception.toString()));
        assertFalse(exception.toString().equals(exception2.toString()));

        assertTrue(exception.getUserId().equals(exception2.getUserId()));
    }
}
