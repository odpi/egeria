/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.varia.NullAppender;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestLabResults;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestWorkbenchResults;
import org.odpi.openmetadata.compliance.tests.origin.OpenMetadataOriginTestWorkbench;
import org.odpi.openmetadata.compliance.tests.repository.OpenMetadataRepositoryTestWorkbench;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenMetadataTestLab provides the compliance test application for open metadata.
 * Each suite of tests is implemented in a subclass of OpenMetadataTestWorkbench
 */
public class OpenMetadataTestLab
{
    private List<OpenMetadataTestWorkbench>  registeredWorkbenches;
    private String                           serverURLRoot;


    /**
     * Return the list of registered workbenches.
     *
     * @param serverURLRoot root url for the server being tested
     * @return list of workbenches.
     */
    private List<OpenMetadataTestWorkbench>  getRegisteredWorkbenches(String    serverURLRoot)
    {
        List<OpenMetadataTestWorkbench>  registeredTestWorkbenches = new ArrayList<>();

        registeredTestWorkbenches.add(new OpenMetadataOriginTestWorkbench(serverURLRoot));
        registeredTestWorkbenches.add(new OpenMetadataRepositoryTestWorkbench(serverURLRoot));

        return registeredTestWorkbenches;
    }


    /**
     * Constructor to create a test lab object that is initialized with the server to test.
     *
     * @param serverURLRoot server to test.
     */
    private OpenMetadataTestLab(String    serverURLRoot)
    {
        this.registeredWorkbenches = getRegisteredWorkbenches(serverURLRoot);
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
     * @param args - first parameter is the URL root of the server.
     */
    public static void main(String[] args)
    {
        String  serverURLRoot;
        String  resultsFileName = "openmetadata.functional.testlab.results";

        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        if ((args == null) || (args.length == 0))
        {
            System.out.println("Please specific the server's URL root in the first parameter");
            System.exit(-1);
        }

        serverURLRoot = args[0];

        System.out.println("===============================");
        System.out.println("Open Metadata Compliance Test  ");
        System.out.println("===============================");
        System.out.println("Compliance Report for server: " + serverURLRoot);

        OpenMetadataTestLab  testLab = new OpenMetadataTestLab(serverURLRoot);

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
            System.out.println("Congratulations, server at " + serverURLRoot + " is an open metadata repository");
            System.exit(0);
        }
        else
        {
            System.out.println("Server at " + serverURLRoot + " is not yet an open metadata repository");
            System.exit(1);
        }
    }
}
