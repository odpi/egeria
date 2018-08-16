/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.HashMap;
import java.util.Map;

public class TestMetadataCollectionId extends OpenMetadataRepositoryTestCase
{
    private static final  String testCaseId = "repository-metadata-collection";
    private static final  String testCaseName = "Repository metadata collection id test case";

    private static final  String assertion1    = testCaseId + "-01";
    private static final  String assertionMsg1 = "Metadata collection id retrieved from repository.";
    private static final  String assertion2    = testCaseId + "-02";
    private static final  String assertionMsg2 = "Consistent metadata collection id retrieved from repository.";

    private String metadataCollectionId = null;


    /**
     * Default constructor sets up superclass
     */
    public TestMetadataCollectionId(String   workbenchId)
    {
        super(workbenchId, testCaseId, testCaseName);
    }


    /**
     * Return the discovered metadata collection Id.
     *
     * @return String guid
     */
    public String getMetadataCollectionId()
    {
        return metadataCollectionId;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        try
        {
            metadataCollectionId = metadataCollection.getMetadataCollectionId();
        }
        catch (RepositoryErrorException  exception)
        {
            String tokens[] = exception.getErrorMessage().split(" on its REST API after it registered with the cohort");

            if (tokens.length > 0)
            {
                String frontOfMessageTokens[] = tokens[0].split("returned a metadata collection identifier of ");

                if (frontOfMessageTokens.length > 1)
                {
                    metadataCollectionId = frontOfMessageTokens[1];
                    super.connector.setMetadataCollectionId(metadataCollectionId);
                    metadataCollection = connector.getMetadataCollection();
                }
            }
        }

        assertCondition((metadataCollectionId != null), assertion1, assertionMsg1);

        assertCondition((metadataCollectionId.equals(metadataCollection.getMetadataCollectionId())),
                        assertion2,
                        assertionMsg2);

        super.result.setSuccessMessage("Metadata collection id working consistently");

        Map<String, Object>  discoveredProperties = new HashMap<>();
        discoveredProperties.put("metadata collection id", metadataCollectionId);

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
