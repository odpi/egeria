/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.fvt.utilities.exceptions;

import org.odpi.openmetadata.fvt.utilities.FVTResults;

/**
 * FVTTestFailureException is the exception thrown when a test fails.
 */
public class FVTTestFailureException extends Exception
{
    private FVTResults results;

    /**
     * Simple constructor
     *
     * @param results details of the test failure
     */
    public FVTTestFailureException(FVTResults results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "FVTTestFailureException{" +
                "results=" + results +
                '}';
    }
}
