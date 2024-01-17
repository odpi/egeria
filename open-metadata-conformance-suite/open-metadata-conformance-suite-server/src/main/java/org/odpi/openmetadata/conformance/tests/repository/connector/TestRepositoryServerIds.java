/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.connector;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * TestRepositoryServerIds tests server ids
 */
public class TestRepositoryServerIds extends RepositoryConformanceTestCase
{
    private static final  String testCaseId   = "repository-server-ids";
    private static final  String testCaseName = "Repository server identifiers test case";

    private static final  String assertion1    = testCaseId + "-01";
    private static final  String assertionMsg1 = "Repository connector retrieved from cohort registration.";
    private static final  String assertion2    = testCaseId + "-02";
    private static final  String assertionMsg2 = "Retrieved helper object for building TypeDefs and metadata instances from repository connector.";
    private static final  String assertion3    = testCaseId + "-03";
    private static final  String assertionMsg3 = "Retrieved validator object to check the validity of TypeDefs and metadata instances from repository connector.";
    private static final  String assertion4    = testCaseId + "-04";
    private static final  String assertionMsg4 = "Retrieved correct local user Id from repository connector.";
    private static final  String assertion5    = testCaseId + "-05";
    private static final  String assertionMsg5 = "Retrieved correct max page size from repository connector.";

    private static final  String repositoryNamePropertyName = "repository name";
    private static final  String serverNamePropertyName = "server name";
    private static final  String serverTypePropertyName = "server type";
    private static final  String organizationNamePropertyName = "organization name";

    private static final  String successMessage = "Repository connector properties retrieved";


    /**
     * Typical constructor sets up superclass
     *
     * @param workPad place for parameters and results
     */
    public TestRepositoryServerIds(RepositoryConformanceWorkPad   workPad)
    {
        super(workPad,
              testCaseId,
              testCaseName,
              RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getProfileId(),
              RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getRequirementId());
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSRepositoryConnector  repositoryConnector = super.cohortRepositoryConnector;

        assertCondition((repositoryConnector != null),
                        assertion1,
                        assertionMsg1,
                        RepositoryConformanceProfileRequirement.COHORT_REGISTRATION.getProfileId(),
                        RepositoryConformanceProfileRequirement.COHORT_REGISTRATION.getRequirementId());

        if (repositoryConnector != null)
        {
            verifyCondition((repositoryConnector.getRepositoryHelper() != null),
                            assertion2,
                            assertionMsg2,
                            defaultProfileId,
                            defaultRequirementId);

            verifyCondition((repositoryConnector.getRepositoryValidator() != null),
                            assertion3,
                            assertionMsg3,
                            defaultProfileId,
                            defaultRequirementId);

            verifyCondition(workPad.getLocalServerUserId().equals(repositoryConnector.getServerUserId()),
                            assertion4,
                            assertionMsg4,
                            defaultProfileId,
                            defaultRequirementId);

            verifyCondition((workPad.getMaxPageSize() == repositoryConnector.getMaxPageSize()),
                            assertion5,
                            assertionMsg5,
                            defaultProfileId,
                            defaultRequirementId);

            addDiscoveredProperty(repositoryNamePropertyName,
                                  repositoryConnector.getRepositoryName(),
                                  defaultProfileId,
                                  defaultRequirementId);

            addDiscoveredProperty(serverNamePropertyName,
                                  repositoryConnector.getServerName(),
                                  defaultProfileId,
                                  defaultRequirementId);

            addDiscoveredProperty(serverTypePropertyName,
                                  repositoryConnector.getServerType(),
                                  defaultProfileId,
                                  defaultRequirementId);

            addDiscoveredProperty(organizationNamePropertyName,
                                  repositoryConnector.getOrganizationName(),
                                  defaultProfileId,
                                  defaultRequirementId);
        }

        super.setSuccessMessage(successMessage);
    }
}
