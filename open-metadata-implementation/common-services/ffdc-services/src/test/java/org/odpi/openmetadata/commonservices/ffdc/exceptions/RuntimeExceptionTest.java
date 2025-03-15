/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


/**
 * Validate that the CommunityProfileRuntimeException is properly populated and supports toString, hashCode and
 * equals.
 */
public class RuntimeExceptionTest
{
    private int       reportedHTTPCode = 404;
    private String    reportingClassName = "TestClassName";
    private String    reportingActionDescription = "TestActionDescription";
    private String    reportedErrorMessage = "TestErrorMessage";
    private String    reportedSystemAction = "TestSystemAction";
    private String    reportedUserAction = "TestUserAction";
    private Exception reportedCaughtException = new Exception("TestReportedCaughtException");
    private Map<String, Object>  propertyMap = new HashMap<>();

    /**
     * Constructor
     */
    public RuntimeExceptionTest()
    {
        propertyMap.put("TestPropertyName", "TestPropertyValue");
    }


    /**
     * Test that a new exception is properly populated
     */
    @SuppressWarnings(value = "deprecation")
    @Test public void testNewException()
    {
        OMAGRuntimeException exception = new OMAGRuntimeException(reportedHTTPCode,
                                                                  reportingClassName,
                                                                  reportingActionDescription,
                                                                  reportedErrorMessage,
                                                                  reportedSystemAction,
                                                                  reportedUserAction,
                                                                  null);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getReportedErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertTrue(exception.getReportedCaughtException() == null);
        assertTrue(exception.getRelatedProperties() == null);
    }


    /**
     * Test that a caught exception is properly wrapped
     */
    @SuppressWarnings(value="deprecation")
    @Test public void testWrappingException()
    {
        OMAGRuntimeException exception = new OMAGRuntimeException(reportedHTTPCode,
                                                                  reportingClassName,
                                                                  reportingActionDescription,
                                                                  reportedErrorMessage,
                                                                  reportedSystemAction,
                                                                  reportedUserAction,
                                                                  reportedCaughtException,
                                                                  new HashMap<>());

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getReportedErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertNotNull(exception.getReportedCaughtException());
        assertTrue(exception.getReportedCaughtException().getMessage().equals("TestReportedCaughtException"));
        assertTrue(exception.getRelatedProperties() == null);
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     */
    @SuppressWarnings(value="deprecation")
    @Test public void testHashCode()
    {
        OMAGRuntimeException exception = new OMAGRuntimeException(reportedHTTPCode,
                                                                  reportingClassName,
                                                                  reportingActionDescription,
                                                                  reportedErrorMessage,
                                                                  reportedSystemAction,
                                                                  reportedUserAction,
                                                                  propertyMap);

        OMAGRuntimeException exception2 = new OMAGRuntimeException(reportedHTTPCode,
                                                                   reportingClassName,
                                                                   reportingActionDescription,
                                                                   reportedErrorMessage,
                                                                   reportedSystemAction,
                                                                   reportedUserAction,
                                                                   reportedCaughtException,
                                                                   propertyMap);



        assertTrue(exception.hashCode() == exception.hashCode());
        assertFalse(exception.hashCode() == exception2.hashCode());

        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(reportedCaughtException));
        assertFalse(exception.equals(exception2));

        assertTrue(exception.toString().contains("OMAGRuntimeException"));

        assertTrue(exception.toString().equals(exception.toString()));
        assertFalse(exception.toString().equals(exception2.toString()));

        assertTrue(exception.getRelatedProperties().equals(propertyMap));
        assertTrue(exception2.getRelatedProperties().equals(propertyMap));
    }
}
