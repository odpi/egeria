/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.fvt;

import org.odpi.openmetadata.accessservices.digitalarchitecture.fvt.clientconstructors.ClientConstructorTest;
import org.odpi.openmetadata.accessservices.digitalarchitecture.fvt.errorhandling.InvalidParameterTest;
import org.odpi.openmetadata.accessservices.digitalarchitecture.fvt.validvalues.CreateValidValuesSetTest;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.FVTSuiteBase;
import org.odpi.openmetadata.http.HttpHelper;


/**
 * DigitalArchitectureOMASFVT provides the main program for the Digital Architecture OMAS
 * Functional Verification Tests (FVTs).
 */
public class DigitalArchitectureOMASFVTSuite extends FVTSuiteBase
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

        results = CreateValidValuesSetTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults();

        return returnCode;
    }
}
