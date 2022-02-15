/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.fvt;

import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.clientconstructors.ClientConstructorTest;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.errorhandling.InvalidParameterTest;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.imports.ImportsTest;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.synchronization.SynchronizationTest;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.FVTSuiteBase;
import org.odpi.openmetadata.http.HttpHelper;


/**
 * AnalyticsModelingOMASFVTSuite provides the main program for the Analytics Modeling OMAS
 * Functional Verification Tests (FVTs).  It is used when running the test suite standalone
 * (ie outside of the failsafe test framework).
 */
public class AnalyticsModelingOMASFVTSuite extends FVTSuiteBase
{
    /**
     * Run all of the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return combined results of running test
     */
    @Override
    protected int performFVT(String   serverName,
                             String   serverPlatformRootURL,
                             String   userId)
    {
        HttpHelper.noStrictSSL();

        int returnCode = 0;

        FVTResults results;

        results = ClientConstructorTest.performFVT(serverName, serverPlatformRootURL);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults(serverName);

        results = InvalidParameterTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults(serverName);

        results = ImportsTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults(serverName);

        results = SynchronizationTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults(serverName);

        return returnCode;
    }
}
