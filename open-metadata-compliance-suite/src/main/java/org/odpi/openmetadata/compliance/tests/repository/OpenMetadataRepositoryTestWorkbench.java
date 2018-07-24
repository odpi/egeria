/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.compliance.OpenMetadataTestCase;
import org.odpi.openmetadata.compliance.OpenMetadataTestWorkbench;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestCaseResult;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestCaseSummary;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestWorkbenchResults;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.List;


/**
 * OpenMetadataRepositoryTestWorkbench provides the workbench for testing the OMRS REST API.
 */
public class OpenMetadataRepositoryTestWorkbench extends OpenMetadataTestWorkbench
{
    private static final String workbenchId            = "repository-workbench";
    private static final String workbenchName          = "Open Metadata Repository Test Workbench";
    private static final String workbenchVersionNumber = "V0.1 SNAPSHOT";
    private static final String workbenchDocURL        = "https://odpi.github.io/egeria/open-metadata-compliance-suite/docs/" + workbenchId;


    /**
     * Constructor received the URL root for the server being tested.
     *
     * @param serverURLRoot string
     */
    public OpenMetadataRepositoryTestWorkbench(String  serverURLRoot)
    {
        super(workbenchName, workbenchVersionNumber, workbenchDocURL, serverURLRoot);
    }


    /**
     * Initialize the list of repository test cases to run.
     *
     * @return list of test cases
     */
    private List<OpenMetadataRepositoryTestCase>  getTestCases()
    {
        List<OpenMetadataRepositoryTestCase>   testCases = new ArrayList<>();

        testCases.add(new TestMetadataCollectionId(workbenchId));
        testCases.add(new TestGetTypeDef(workbenchId));

        return testCases;
    }


    /**
     * Create a connector to the repository.
     *
     * @return OMRSRepositoryConnector
     */
    private OMRSRepositoryConnector   getRepositoryConnector()
    {
        final String serverName           = "Test Server";
        final String metadataCollectionId = "DummyMetadataCollectionId";

        try
        {
            ConnectorConfigurationFactory factory = new ConnectorConfigurationFactory();

            Connection connection = factory.getDefaultLocalRepositoryRemoteConnection(serverName,
                                                                                      serverURLRoot);

            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            OMRSRepositoryConnector  repositoryConnector = (OMRSRepositoryConnector)connector;

            repositoryConnector.setMetadataCollectionId(metadataCollectionId);
            repositoryConnector.start();

            return repositoryConnector;
        }
        catch (Throwable  exc)
        {
            System.out.println("Unable to create connector " + exc.getMessage());
            return null;
        }
    }


    /**
     * Run the registered test cases and return the accumulated results.
     *
     * @return OpenMetadataWorkbenchResults bean
     */
    public OpenMetadataTestWorkbenchResults runTests()
    {
        List<OpenMetadataRepositoryTestCase>  testCases = this.getTestCases();
        OpenMetadataTestWorkbenchResults      workbenchResults = new OpenMetadataTestWorkbenchResults(this);

        if (testCases != null)
        {
            OMRSRepositoryConnector  repositoryConnector = this.getRepositoryConnector();

            if (repositoryConnector != null)
            {
                /*
                 * Executing tests only if there is a repository connector
                 */
                for (OpenMetadataRepositoryTestCase testCase : testCases)
                {
                    testCase.setConnector(repositoryConnector);
                    testCase.executeTest();
                }
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
