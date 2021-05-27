/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.fvt;

import org.odpi.openmetadata.accessservices.governanceengine.fvt.clientconstructors.ClientConstructorTest;
import org.odpi.openmetadata.accessservices.governanceengine.fvt.engines.CreateEngineTest;
import org.odpi.openmetadata.accessservices.governanceengine.fvt.errorhandling.InvalidParameterTest;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.FVTSuiteBase;
import org.odpi.openmetadata.http.HttpHelper;


/**
 * GovernanceEngineOMASFVTSuite provides the main program for the Governance Engine OMAS
 * Functional Verification Tests (FVTs).  It is used when running the test suite standalone
 * (ie outside of the failsafe test framework).
 */
public class GovernanceEngineOMASFVTSuite extends FVTSuiteBase
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
        results.printResults();

        results = InvalidParameterTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults();

        results = CreateEngineTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults();

        return returnCode;
    }
}
