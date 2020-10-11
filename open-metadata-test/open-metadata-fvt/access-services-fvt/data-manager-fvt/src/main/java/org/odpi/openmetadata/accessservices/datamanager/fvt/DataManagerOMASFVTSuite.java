/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.fvt;

import org.odpi.openmetadata.accessservices.datamanager.fvt.clientconstructors.ClientConstructorTest;
import org.odpi.openmetadata.accessservices.datamanager.fvt.databases.CreateDatabaseTest;
import org.odpi.openmetadata.accessservices.datamanager.fvt.errorhandling.InvalidParameterTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.fvt.utilities.FVTResults;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import static java.lang.System.exit;

/**
 * FVTSuite provides the main program for the Data Manager OMAS
 * Functional Verification Tests (FVTs).  It is used when running the test suite standalone
 * (ie outside of the failsafe test framework).
 */
public class FVTSuite
{
    public static void main(String[] args)
    {
        int exitCode;

        try
        {
            String url = getUrl(args);
            String serverName = getServerName(args);
            String userId = getUserId(args);

            exitCode = performFVT(url, serverName, userId);
        }
        catch (IOException e1)
        {
            System.out.println("Error getting user input");
            exitCode = -99;
        }

        exit(exitCode);
    }


    /**
     * Run all of the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return combined results of running test
     */
    private static int performFVT(String   serverName,
                                  String   serverPlatformRootURL,
                                  String   userId)
    {
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

        results = CreateDatabaseTest.performFVT(serverName, serverPlatformRootURL, userId);
        if (! results.isSuccessful())
        {
            returnCode --;
        }
        results.printResults();

        return returnCode;
    }


    /**
     * This method gets the serverName that the FVT suite will use to issue calls to the server.
     * If arguments are supplied then the second parameter is used as a serverName.
     * If no serverName is supplied then prompt the user to enter a valid serverName, enter means to use the default serverName.
     *
     * @param args arguments supplied
     * @return the serverName to use on the calls to the server
     * @throws IOException IO exception occurred while getting input from the user.
     */
    private static String getServerName(String[] args) throws IOException
    {
        String name;
        if (args.length > 1)
        {
            name = args[1];
        }
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a OMAG Server Name. Press enter to get the default (" + FVTConstants.DEFAULT_SERVER_NAME + ")) :");
            name = br.readLine();
            if (name.equals(""))
            {
                name = FVTConstants.DEFAULT_SERVER_NAME;
            }

        }

        return name;
    }


    /**
     * This method gets the userId that the FVT suite will use to issue calls to the server.
     * If arguments are supplied then the third parameter is used as a userId.
     * If no userId is supplied then prompt the user to enter a valid userId, enter means to use the default userId.
     *
     * @param args arguments supplied
     * @return the userId to use on the calls to the server
     * @throws IOException IO exception occurred while getting input from the user.
     */
    private static String getUserId(String[] args) throws IOException
    {
        String userId;
        if (args.length > 2)
        {
            userId = args[2];
        }
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a userId. Press enter to get the default (" + FVTConstants.USERID + ")) :");
            userId = br.readLine();
            if (userId.equals(""))
            {
                userId = FVTConstants.USERID;
            }

        }

        return userId;
    }


    /**
     * This method gets the url that the FVT suite will use to issue calls to the server.
     * If arguments are supplied then the first parameter is used as a url.
     * If no url is supplied then prompt the user to enter a valid url, enter means to use the default url.
     *
     * @param args arguments supplied
     * @return the url to use on the calls to the server
     * @throws IOException IO exception occurred while getting input from the user.
     */
    private static String getUrl(String[] args) throws IOException
    {
        String url;

        if (args.length > 0)
        {
            url = args[0];
        }
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a URL. Press enter to get the default (" + FVTConstants.DEFAULT_URL + ".)) :");
            url = br.readLine();
            if (url.equals(""))
            {
                url = FVTConstants.DEFAULT_URL;
            }
        }
        return url;
    }
}
