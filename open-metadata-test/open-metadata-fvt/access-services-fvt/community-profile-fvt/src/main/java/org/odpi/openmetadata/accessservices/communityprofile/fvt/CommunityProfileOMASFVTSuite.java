/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.fvt;

import org.odpi.openmetadata.accessservices.communityprofile.fvt.clientconstructors.ClientConstructorTest;
import org.odpi.openmetadata.accessservices.communityprofile.fvt.profiles.CreateProfileTest;
import org.odpi.openmetadata.accessservices.communityprofile.fvt.errorhandling.InvalidParameterTest;
import org.odpi.openmetadata.accessservices.communityprofile.fvt.todos.ToDosTest;
import org.odpi.openmetadata.accessservices.communityprofile.fvt.validvalues.CreateValidValuesSetTest;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.FVTSuiteBase;
import org.odpi.openmetadata.http.HttpHelper;

import java.io.IOException;

import static java.lang.System.exit;


/**
 * CommunityProfileOMASFVTSuite provides the main program for the ProjectProperties Profile OMAS
 * Functional Verification Tests (FVTs).  It is used when running the test suite standalone
 * (ie outside the failsafe test framework).
 */
public class CommunityProfileOMASFVTSuite extends FVTSuiteBase
{
    /**
     * Run the FVT Suite.
     *
     * @param args user input
     */
    public static void main(String[] args)
    {
        int exitCode;

        try
        {
            String url = getUrl(args);
            String serverName = getServerName(args);
            String userId = getUserId(args);

            CommunityProfileOMASFVTSuite fvtSuite = new CommunityProfileOMASFVTSuite();

            exitCode = fvtSuite.performFVT(serverName, url, userId);
        }
        catch (IOException error)
        {
            System.out.println("Error getting user input");
            error.printStackTrace();
            exitCode = -99;
        }

        exit(exitCode);
    }


    /**
     * Run all the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return combined results of running test
     */
    @Override
    public int performFVT(String serverName,
                          String serverPlatformRootURL,
                          String userId)
    {
        HttpHelper.noStrictSSL();

        int returnCode = 0;

        FVTResults results;

        results = CreateValidValuesSetTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults(serverName);
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

        results = CreateProfileTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults(serverName);

        results = ToDosTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults(serverName);

        return returnCode;
    }
}
