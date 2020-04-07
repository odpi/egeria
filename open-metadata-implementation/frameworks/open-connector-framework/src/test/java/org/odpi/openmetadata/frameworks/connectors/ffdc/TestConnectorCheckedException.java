/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Validate that the ConnectorCheckedException is properly populate and supports toString, hashCode and
 * equals.
 */
public class TestConnectorCheckedException
{
    /*
     * These default values are only seen if this exception is initialized using one of its superclass constructors.
     */
    private int       reportedHTTPCode = 400;
    private String    reportingClassName = "TestClassName";
    private String    reportingActionDescription = "TestActionDescription";
    private Throwable reportedCaughtException = new Exception("TestReportedCaughtException");

    /**
     * Constructor
     */
    public TestConnectorCheckedException()
    {
    }


    /**
     * Test that a new exception is properly populated
     */
    @Test public void testNewException()
    {
        ConnectorCheckedException exception = new ConnectorCheckedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName", "entityGUID", "entityType"),
                                                                            reportingClassName,
                                                                            reportingActionDescription);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getReportedCaughtException() == null);
    }


    /**
     * Test that a caught exception is properly wrapped
     */
    @Test public void testWrappingException()
    {
        ConnectorCheckedException exception = new ConnectorCheckedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName", "entityGUID", "entityType"),
                                                                            reportingClassName,
                                                                            reportingActionDescription,
                                                                            reportedCaughtException);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertFalse(exception.getReportedCaughtException().equals(null));
        assertTrue(exception.getReportedCaughtException().getMessage().equals("TestReportedCaughtException"));
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     */
    @Test public void testHashCode()
    {
        ConnectorCheckedException exception = new ConnectorCheckedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName", "entityGUID", "entityType"),
                                                                            reportingClassName,
                                                                            reportingActionDescription,
                                                                            reportedCaughtException);

        ConnectorCheckedException exception2 = new ConnectorCheckedException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName", "entityGUID", "entityType"),
                                                                             reportingClassName,
                                                                             reportingActionDescription,
                                                                             reportedCaughtException);



        assertTrue(exception.hashCode() == exception2.hashCode());

        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(reportedCaughtException));
        assertTrue(exception.equals(exception2));

        assertTrue(exception.toString().equals(exception2.toString()));
    }
}
