/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedAssetGUIDException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Validate that the unrecognized asset guid exception is properly populated and supports toString, hashCode and
 * equals.
 */
public class NoConnectedAssetExceptionTest
{
    private int       reportedHTTPCode           = 404;
    private String    reportingClassName         = "TestClassName";
    private String    reportingActionDescription = "TestActionDescription";
    private String    reportedErrorMessage       = "TestErrorMessage";
    private String    reportedSystemAction       = "TestSystemAction";
    private String    reportedUserAction         = "TestUserAction";
    private String    assetGUID                  = "TestAssetGUID";
    private Throwable reportedCaughtException    = new Exception("TestReportedCaughtException");

    /**
     * Constructor
     */
    public NoConnectedAssetExceptionTest()
    {
    }


    /**
     * Test that a new exception is properly populated
     */
    @Test
    public void testNewException()
    {
        UnrecognizedAssetGUIDException exception = new UnrecognizedAssetGUIDException(reportedHTTPCode,
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
        assertTrue(exception.getAssetGUID() == null);
    }


    /**
     * Test that a caught exception is properly wrapped
     */
    @Test
    public void testWrappingException()
    {
        UnrecognizedAssetGUIDException exception = new UnrecognizedAssetGUIDException(reportedHTTPCode,
                                                                                                reportingClassName,
                                                                                                reportingActionDescription,
                                                                                                reportedErrorMessage,
                                                                                                reportedSystemAction,
                                                                                                reportedUserAction,
                                                                                                reportedCaughtException,
                                                                                      assetGUID);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertFalse(exception.getReportedCaughtException().equals(null));
        assertTrue(exception.getReportedCaughtException().getMessage().equals("TestReportedCaughtException"));
        assertTrue(exception.getAssetGUID().equals(assetGUID));
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     */
    @Test
    public void testHashCode()
    {
        UnrecognizedAssetGUIDException exception = new UnrecognizedAssetGUIDException(reportedHTTPCode,
                                                                                                reportingClassName,
                                                                                                reportingActionDescription,
                                                                                                reportedErrorMessage,
                                                                                                reportedSystemAction,
                                                                                                reportedUserAction,
                                                                                      assetGUID);

        UnrecognizedAssetGUIDException exception2 = new UnrecognizedAssetGUIDException(reportedHTTPCode,
                                                                                                 reportingClassName,
                                                                                                 reportingActionDescription,
                                                                                                 reportedErrorMessage,
                                                                                                 reportedSystemAction,
                                                                                                 reportedUserAction,
                                                                                                 reportedCaughtException,
                                                                                       assetGUID);

        UnrecognizedAssetGUIDException exception3 = new UnrecognizedAssetGUIDException(reportedHTTPCode,
                                                                                                 reportingClassName,
                                                                                                 reportingActionDescription,
                                                                                                 reportedErrorMessage,
                                                                                                 reportedSystemAction,
                                                                                                 reportedUserAction,
                                                                                                 reportedCaughtException,
                                                                                       assetGUID);


        assertTrue(exception.hashCode() == exception.hashCode());
        assertFalse(exception.hashCode() == exception2.hashCode());

        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(reportedCaughtException));
        assertFalse(exception.equals(exception2));
        assertTrue(exception2.equals(exception3));

        assertTrue(exception.toString().contains("UnrecognizedAssetGUIDException"));

        assertTrue(exception.toString().equals(exception.toString()));
        assertFalse(exception.toString().equals(exception2.toString()));

        assertTrue(exception.getAssetGUID().equals(exception2.getAssetGUID()));

    }
}
