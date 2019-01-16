/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.varia.NullAppender;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestLabResults;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestWorkbenchResults;
import org.odpi.openmetadata.conformance.tests.origin.OpenMetadataOriginTestWorkbench;
import org.odpi.openmetadata.conformance.tests.repository.OpenMetadataRepositoryTestWorkbench;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenMetadataTestLab provides the conformance test application for open metadata.
 * Each suite of tests is implemented in a subclass of OpenMetadataTestWorkbench
 */
public class OpenMetadataTestLab
{
    private static String  resultsFileNameLeaf = ".openmetadata.functional.testlab.results";

    private List<OpenMetadataTestWorkbench>  registeredWorkbenches;
    private String                           serverName;
    private String                           serverURLRoot;


    /**
     * Return the list of registered workbenches.
     *
     * @param serverName name of server to test.
     * @param serverURLRoot root url for the server being tested
     * @return list of workbenches.
     */
    private List<OpenMetadataTestWorkbench>  getRegisteredWorkbenches(String    serverName,
                                                                      String    serverURLRoot)
    {
        List<OpenMetadataTestWorkbench>  registeredTestWorkbenches = new ArrayList<>();

        registeredTestWorkbenches.add(new OpenMetadataOriginTestWorkbench(serverName, serverURLRoot));
        registeredTestWorkbenches.add(new OpenMetadataRepositoryTestWorkbench(serverName, serverURLRoot));

        return registeredTestWorkbenches;
    }


    /**
     * Constructor to create a test lab object that is initialized with the server to test.
     *
     * @param serverName name of server to test.
     * @param serverURLRoot location of server to test.
     */
    private OpenMetadataTestLab(String    serverName,
                                String    serverURLRoot)
    {
        this.registeredWorkbenches = getRegisteredWorkbenches(serverName, serverURLRoot);
        this.serverName = serverName;
        this.serverURLRoot = serverURLRoot;
    }


    /**
     * Request each registered workbench runs its tests.
     *
     * @return combined results from all of the workbenches.
     */
    private OpenMetadataTestLabResults runTests()
    {
        OpenMetadataTestLabResults  results = new OpenMetadataTestLabResults();

        results.setServerName(this.serverName);
        results.setServerRootURL(this.serverURLRoot);

        if (this.registeredWorkbenches != null)
        {
            List<OpenMetadataTestWorkbenchResults>  workbenchResultsList = new ArrayList<>();

            for (OpenMetadataTestWorkbench  testWorkbench : registeredWorkbenches)
            {
                workbenchResultsList.add(testWorkbench.runTests());
            }

            if (! workbenchResultsList.isEmpty())
            {
                results.setTestResultsFromWorkbenches(workbenchResultsList);
            }
        }

        return results;
    }



    /**
     * Main method handles the parameters passed and then calls the registered workbenches.
     *
     * @param args first parameter is the URL root of the server, second parameter is the
     *             server name
     */
    public static void main(String[] args)
    {
        String  serverName;
        String  serverURLRoot;
        String  resultsFileName;

        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        if ((args == null) || (args.length < 2))
        {
            System.out.println("Please specify the server's name in the first parameter and the server URL root in the second");
            System.exit(-1);
        }

        serverName = args[0];
        serverURLRoot = args[1];

        resultsFileName = serverName + resultsFileNameLeaf;

        System.out.println("===============================");
        System.out.println("Open Metadata Conformance Test  ");
        System.out.println("===============================");
        System.out.println("Conformance Report for server: " + serverName + " (" + serverURLRoot + ")");

        OpenMetadataTestLab  testLab = new OpenMetadataTestLab(serverName, serverURLRoot);

        OpenMetadataTestLabResults testLabResults = testLab.runTests();

        if (testLabResults == null)
        {
            System.out.println("The OpenMetadataTestLab failed to create any results");
            System.exit(-1);
        }

        try
        {
            File resultsStoreFile = new File(resultsFileName);
            ObjectMapper objectMapper = new ObjectMapper();
            String       jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testLabResults);

            FileUtils.writeStringToFile(resultsStoreFile, jsonString, false);
        }
        catch (Throwable  exc)
        {
            System.out.println("The OpenMetadataTestLab failed to process results " + exc.getMessage());
            System.exit(-1);
        }

        System.out.println();
        System.out.println("Number of tests: " + testLabResults.getTestCaseCount());
        System.out.println("Number of tests passed: " + testLabResults.getTestPassCount());
        System.out.println("Number of tests failed: " + testLabResults.getTestFailedCount());
        System.out.println("Number of tests skipped: " + testLabResults.getTestSkippedCount());
        System.out.println();

        if (testLabResults.getTestCaseCount() == testLabResults.getTestPassCount())
        {
            System.out.println("Congratulations, " + serverName + " server at " + serverURLRoot + " is an open metadata repository");
            System.exit(0);
        }
        else
        {
            System.out.println(serverName + " server at " + serverURLRoot + " is not yet an open metadata repository");
            System.exit(1);
        }
    }
}
