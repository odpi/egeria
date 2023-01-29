/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.connector;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

public class TestMetadataCollectionId extends RepositoryConformanceTestCase
{
    private static final  String testCaseId   = "repository-metadata-collection";
    private static final  String testCaseName = "Repository metadata collection identifiers test case";

    private static final  String assertion1    = testCaseId + "-01";
    private static final  String assertionMsg1 = "Metadata collection id retrieved from cohort registration.";
    private static final  String assertion2    = testCaseId + "-02";
    private static final  String assertionMsg2 = "Metadata collection id retrieved from cohort repository connector.";
    private static final  String assertion3    = testCaseId + "-03";
    private static final  String assertionMsg3 = "Metadata collection id retrieved from cohort repository connector's metadata collection.";
    private static final  String assertion4    = testCaseId + "-04";
    private static final  String assertionMsg4 = "Metadata collection id retrieved from cohort repository connector matches registration.";
    private static final  String assertion5    = testCaseId + "-05";
    private static final  String assertionMsg5 = "Metadata collection id retrieved from cohort repository connector's metadata collection matches registration.";

    private static final  String metadataCollectionIdPropertyName   = "metadata collection id";
    private static final  String metadataCollectionNamePropertyName = "metadata collection name";

    private static final  String successMessage = "Metadata collection identifiers returned consistently";


    /**
     * Typical constructor sets up superclass
     *
     * @param workPad place for parameters and results
     */
    public TestMetadataCollectionId(RepositoryConformanceWorkPad   workPad)
    {
        super(workPad,
              testCaseId,
              testCaseName,
              RepositoryConformanceProfileRequirement.METADATA_COLLECTION_ID.getProfileId(),
              RepositoryConformanceProfileRequirement.METADATA_COLLECTION_ID.getRequirementId());
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {

        String registrationMetadataCollectionId = repositoryConformanceWorkPad.getTutMetadataCollectionId();

        verifyCondition((registrationMetadataCollectionId != null),
                        assertion1,
                        assertionMsg1,
                        defaultProfileId,
                        defaultRequirementId);

        OMRSRepositoryConnector repositoryConnector       = super.getRepositoryConnector();
        String                  localMetadataCollectionId = repositoryConnector.getMetadataCollectionId();

        verifyCondition((localMetadataCollectionId != null),
                        assertion2,
                        assertionMsg2,
                        defaultProfileId,
                        defaultRequirementId);

        verifyCondition((repositoryConformanceWorkPad.getTutMetadataCollectionId().equals(localMetadataCollectionId)),
                        assertion4,
                        assertionMsg4,
                        defaultProfileId,
                        defaultRequirementId);

        OMRSMetadataCollection  metadataCollection     = super.getMetadataCollection();
        String                  mcMetadataCollectionId = null;

        /*
         * This is the first attempt to make a remote call to the technology under test.
         */
        int retryCount = 0;
        do
        {
            try
            {
                mcMetadataCollectionId = metadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId());
            }
            catch (RepositoryErrorException serverNotRunningException)
            {
                Thread.sleep(1000);
            }

            retryCount++;

        } while ((retryCount < 20) && (mcMetadataCollectionId == null));

        verifyCondition((mcMetadataCollectionId != null),
                        assertion3,
                        assertionMsg3,
                        defaultProfileId,
                        defaultRequirementId);

        verifyCondition((repositoryConformanceWorkPad.getTutMetadataCollectionId().equals(mcMetadataCollectionId)),
                        assertion5,
                        assertionMsg5,
                        defaultProfileId,
                        defaultRequirementId);

        addDiscoveredProperty(metadataCollectionIdPropertyName,
                              mcMetadataCollectionId,
                              defaultProfileId,
                              defaultRequirementId);

        addDiscoveredProperty(metadataCollectionNamePropertyName,
                              (repositoryConnector.getMetadataCollectionName()),
                              defaultProfileId,
                              defaultRequirementId);

        super.setSuccessMessage(successMessage);
    }
}
