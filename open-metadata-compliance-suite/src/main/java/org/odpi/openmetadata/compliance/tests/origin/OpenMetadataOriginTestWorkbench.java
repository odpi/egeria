/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.origin;


import org.odpi.openmetadata.compliance.OpenMetadataTestCase;
import org.odpi.openmetadata.compliance.OpenMetadataTestWorkbench;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestCaseResult;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestCaseSummary;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestWorkbenchResults;

import java.util.ArrayList;
import java.util.List;


/**
 * OpenMetadataOriginTestWorkbench provides the workbench for testing the Origin API of an open metadata repository.
 */
public class OpenMetadataOriginTestWorkbench extends OpenMetadataTestWorkbench
{
    private static final String workbenchId            = "origin-workbench";
    private static final String workbenchName          = "Open Metadata Origin Test Workbench";
    private static final String workbenchVersionNumber = "V0.1 SNAPSHOT";
    private static final String workbenchDocURL        = "https://odpi.github.io/egeria/open-metadata-compliance-suite/docs/" + workbenchId;


    /**
     * Constructor received the URL root for the server being tested.
     *
     * @param serverURLRoot string
     */
    public OpenMetadataOriginTestWorkbench(String  serverURLRoot)
    {
        super(workbenchName, workbenchVersionNumber, workbenchDocURL, serverURLRoot);
    }


    /**
     * Initialize the list of repository test cases to run.
     *
     * @return list of test cases
     */
    private List<OpenMetadataOriginTestCase>  getTestCases()
    {
        List<OpenMetadataOriginTestCase>   testCases = new ArrayList<>();

        testCases.add(new TestOpenMetadataOrigin(workbenchId));

        return testCases;
    }


    /**
     * Run the registered test cases and return the accumulated results.
     *
     * @return OpenMetadataWorkbenchResults bean
     */
    public OpenMetadataTestWorkbenchResults runTests()
    {
        List<OpenMetadataOriginTestCase>  testCases = this.getTestCases();
        OpenMetadataTestWorkbenchResults      workbenchResults = new OpenMetadataTestWorkbenchResults(this);

        if (testCases != null)
        {
            for (OpenMetadataOriginTestCase testCase : testCases)
            {
                testCase.setServerURLRoot(serverURLRoot);
                testCase.executeTest();
            }

            List<OpenMetadataTestCaseResult>  passedTestCases  = new ArrayList<>();
            List<OpenMetadataTestCaseResult>  failedTestCases  = new ArrayList<>();
            List<OpenMetadataTestCaseSummary> skippedTestCases = new ArrayList<>();

            /*
             * Executing tests only if there is a repository connector
             */
            for (OpenMetadataTestCase testCase : testCases)
            {
                if (testCase.isTestRan())
                {
                    if (testCase.isTestPassed())
                    {
                        passedTestCases.add(testCase.getResult());
                    }
                    else
                    {
                        failedTestCases.add(testCase.getResult());
                    }
                }
                else
                {
                    skippedTestCases.add(testCase.getSummary());
                }
            }

            if (! passedTestCases.isEmpty())
            {
                workbenchResults.setPassedTestCases(passedTestCases);
            }
            if (! failedTestCases.isEmpty())
            {
                workbenchResults.setFailedTestCases(failedTestCases);
            }
            if (! skippedTestCases.isEmpty())
            {
                workbenchResults.setSkippedTestCases(skippedTestCases);
            }
        }

        return workbenchResults;
    }
}
