/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

/**
 * TestConsistentTypeDef tests type defs
 */
public class TestConsistentTypeDef extends RepositoryConformanceTestCase
{
    private static final String rootTestCaseId = "repository-consistency-of-typedef";
    private static final String testCaseName   = "Verify consistency of type definition from event and REST API";

    private static final String assertion1     = "-01";
    private static final String assertionMsg1  = " type definition from event is consistent with API.";

    private static final String successMessage  = "Type definition event successfully processed";

    private TypeDef  typeDef;
    private String   testCaseId;

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param typeDef type definition to test
     * @param eventIdentifier event count to ensure test case ids are consistent
     */
    public TestConsistentTypeDef(RepositoryConformanceWorkPad workPad,
                                 TypeDef                      typeDef,
                                 int                          eventIdentifier)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getProfileId(),
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getRequirementId());

        this.typeDef = typeDef;
        this.testCaseId = rootTestCaseId + "-" + typeDef.getName() + "-" + Integer.toString(eventIdentifier);

        this.updateTestId(rootTestCaseId, testCaseId, testCaseName);
    }


    /**
     * Build up the assertion name.
     *
     * @param assertionId unique identifier
     * @return assertion name
     */
    private String getAssertionName(String   assertionId)
    {
        return testCaseId + "-" + assertionId;
    }



    /**
     * Dummy method needed to satisfy the superclass
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if (typeDef != null)
        {
            if (metadataCollection != null)
            {
                long start = System.currentTimeMillis();
                TypeDef restAPITypeDef = metadataCollection.getTypeDefByGUID(workPad.getLocalServerUserId(),
                                                                             typeDef.getGUID());
                long elapsedTime = System.currentTimeMillis() - start;

                /*
                 * A full equality test is too strong because the type may be at a different version, in which case numeric version field will differ.
                 * Test full equality only if the version is identical.
                 */
                assertCondition(( (typeDef.getVersion() != restAPITypeDef.getVersion()) || typeDef.equals(restAPITypeDef) ),
                                getAssertionName(assertion1),
                                typeDef.getName() + assertionMsg1,
                                super.defaultProfileId,
                                super.defaultRequirementId,
                                "getTypeDefByGUID",
                                elapsedTime);

            }

            super.setSuccessMessage(typeDef.getName() + successMessage);
        }
    }
}
