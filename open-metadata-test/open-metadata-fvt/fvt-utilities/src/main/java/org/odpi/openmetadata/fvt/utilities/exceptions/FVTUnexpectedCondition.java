/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.fvt.utilities.exceptions;


import java.io.Serial;

/**
 * FVTUnexpectedCondition is an exception used by the FVT test cases to indicate that an unexpected
 * condition has occurred.
 */
public class FVTUnexpectedCondition extends Exception
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Name of test case
     */
    private final String    testCaseName;

    /**
     * Description of test activity
     */
    private final String    activityDescription;

    /**
     * Any exception that occurred
     */
    private Throwable caughtException     = null;

    /**
     * Constructor for an unexpected exception.
     *
     * @param testCaseName test case
     * @param activityDescription what was the action?
     * @param caughtException unexpected exception
     */
    public FVTUnexpectedCondition(String testCaseName, String activityDescription, Throwable caughtException)
    {
        super(activityDescription, caughtException);

        this.testCaseName        = testCaseName;
        this.activityDescription = activityDescription;
        this.caughtException     = caughtException;
    }


    /**
     * Constructor when an expected exception does not occur.
     *
     * @param testCaseName test case
     * @param activityDescription what was the action?
     */
    public FVTUnexpectedCondition(String testCaseName, String activityDescription)
    {
        super(activityDescription);

        this.testCaseName        = testCaseName;
        this.activityDescription = activityDescription;
    }


    /**
     * Retrieve the name of the test case that failed.
     *
     * @return test case name
     */
    public String getTestCaseName()
    {
        return testCaseName;
    }


    /**
     * Retrieve the name of the activity that failed.
     *
     * @return description
     */
    public String getActivityDescription()
    {
        return activityDescription;
    }


    /**
     * Retrieve any unexpected exception.
     *
     * @return caught exception
     */
    public Throwable getCaughtException()
    {
        return caughtException;
    }


    @Override
    public String toString()
    {
        return "FVTUnexpectedCondition{" +
                "testCaseName='" + testCaseName + '\'' +
                ", errorDescription='" + activityDescription + '\'' +
                ", caughtException=" + caughtException +
                '}';
    }
}
