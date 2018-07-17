/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.compliance.OpenMetadataTestCase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * OpenMetadataTestCase is the superclass for an open metadata compliance test.  It manages the
 * test environment and reporting.
 */
public abstract class OpenMetadataRepositoryTestCase extends OpenMetadataTestCase
{
    private static final  String   assertion1 =  "repository-test-case-base-01";
    private static final  String   assertionMsg1 = "Connector supplied to test case.";
    private static final  String   assertion2 = "repository-test-case-base-02";
    private static final  String   assertionMsg2 = "Metadata collection for connector supplied to test case.";

    protected OMRSRepositoryConnector connector = null;


    /**
     * Constructor passes the standard descriptive information to the superclass.
     *
     * @param workbenchId - identifier of the workbench used to build the documentation URL.
     * @param testCaseId - id of the test case
     * @param testCaseName - name of the test case
     */
    protected OpenMetadataRepositoryTestCase(String workbenchId,
                                             String testCaseId,
                                             String testCaseName)
    {
        super(workbenchId, testCaseId, testCaseName);
    }


    /**
     * Set up the connector to the repository that is being tested.
     *
     * @param connector initialized and started OMRSRepositoryConnector object
     */
    public void setConnector(OMRSRepositoryConnector connector)
    {
        this.connector = connector;
    }


    /**
     * Return the metadata collection used to call the repository.
     *
     * @return OMRSMetadataCollection object
     * @throws Exception if the connector is not properly set up.
     */
    protected OMRSMetadataCollection getMetadataCollection() throws Exception
    {
        OMRSMetadataCollection metadataCollection = null;

        if (connector != null)
        {
            metadataCollection = connector.getMetadataCollection();
        }

        /*
         * The assertions are issued only if there is a problem so that the test mechanism is not
         * cluttering up the results with successful assertions.
         * Errors at this point are likely to be errors in the test framework.
         */
        if (metadataCollection == null)
        {
            assertCondition((connector != null), assertion1, assertionMsg1);
            assertCondition((false), assertion2, assertionMsg2);
        }

        return metadataCollection;
    }
}
