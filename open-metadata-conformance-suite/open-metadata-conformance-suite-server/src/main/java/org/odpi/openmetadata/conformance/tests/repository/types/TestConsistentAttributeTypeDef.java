/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;

public class TestConsistentAttributeTypeDef extends RepositoryConformanceTestCase
{
    private static final String rootTestCaseId = "repository-consistency-of-attribute-typedef";
    private static final String testCaseName   = "Verify consistency of type definition from event and REST API";

    private static final String assertion1     = rootTestCaseId + "-01";
    private static final String assertionMsg1  = " attribute type definition from event is consistent with API.";

    AttributeTypeDef attributeTypeDef;

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param attributeTypeDef type definition to test
     * @param eventIdentifier count of events to ensure test case ides are unique
     */
    public TestConsistentAttributeTypeDef(RepositoryConformanceWorkPad workPad,
                                          AttributeTypeDef             attributeTypeDef,
                                          int                          eventIdentifier)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getProfileId(),
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getRequirementId());

        this.attributeTypeDef = attributeTypeDef;
        this.testCaseId = rootTestCaseId + "-" + attributeTypeDef.getName() + "-" + eventIdentifier;

        this.updateTestId(rootTestCaseId, testCaseId, testCaseName);
    }


    /**
     * Dummy method needed to satisfy the superclass
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if (attributeTypeDef != null)
        {
            if (metadataCollection != null)
            {
                long start = System.currentTimeMillis();
                AttributeTypeDef restAPITypeDef = metadataCollection.getAttributeTypeDefByGUID(workPad.getLocalServerUserId(),
                                                                                               attributeTypeDef.getGUID());
                long elapsedTime = System.currentTimeMillis() - start;

                assertCondition((attributeTypeDef.equals(restAPITypeDef)),
                                assertion1,
                                attributeTypeDef.getName() + assertionMsg1,
                                super.defaultProfileId,
                                super.defaultRequirementId,
                                "getAttributeTypeDefByGUID",
                                elapsedTime);

            }

            super.setSuccessMessage(attributeTypeDef.getName() + successMessage);
        }
    }
}



