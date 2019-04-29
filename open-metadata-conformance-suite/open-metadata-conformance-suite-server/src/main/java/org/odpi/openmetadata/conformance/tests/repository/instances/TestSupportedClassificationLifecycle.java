/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;

import java.util.List;


/**
 * Test that all defined classifications can be created, retrieved, updated and deleted.
 */
public class TestSupportedClassificationLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-classification-lifecycle";
    private static final String testCaseName = "Repository classification lifecycle test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = "No classifications attached to new entity of type ";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " entity returned when classification added.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " classification added to entity of type ";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " classification properties added to entity of type ";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " classification removed from entity of type ";

    private EntityDef         testEntityDef;
    private ClassificationDef classificationDef;
    private String            testTypeName;

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param testEntityDef type of entity to attach classification to
     * @param classificationDef list of valid classifications
     */
    public TestSupportedClassificationLifecycle(RepositoryConformanceWorkPad workPad,
                                                EntityDef                    testEntityDef,
                                                ClassificationDef            classificationDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
              RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId());

        this.classificationDef = classificationDef;
        this.testEntityDef = testEntityDef;

        this.testTypeName = this.updateTestIdByType(classificationDef.getName() + "-" + testEntityDef.getName(),
                                                    testCaseId,
                                                    testCaseName);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        EntityDetail testEntity = addEntityToRepository(testTypeName, metadataCollection, testEntityDef);

        assertCondition((testEntity.getClassifications() == null),
                        assertion1,
                         assertionMsg1 + testEntityDef.getName(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId());

        EntityDetail classifiedEntity = metadataCollection.classifyEntity(workPad.getLocalServerUserId(),
                                                                          testEntity.getGUID(),
                                                                          classificationDef.getName(),
                                                                          null);

        assertCondition((classifiedEntity != null),
                        assertion2,
                        testEntityDef.getName() + assertionMsg2,
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId());

        Classification initialClassification = null;

        List<Classification>  classifications = classifiedEntity.getClassifications();

        if ((classifications != null) && (classifications.size() == 1))
        {
            initialClassification = classifications.get(0);
        }

        assertCondition(((initialClassification != null) &&
                         (classificationDef.getName().equals(initialClassification.getName()) &&
                          (initialClassification.getProperties() == null))),
                        assertion3,
                        testTypeName + assertionMsg3 + testEntityDef.getName(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId());


        InstanceProperties  classificationProperties = this.getPropertiesForInstance(classificationDef.getPropertiesDefinition());
        if (classificationProperties != null)
        {
            EntityDetail reclassifiedEntity = metadataCollection.updateEntityClassification(workPad.getLocalServerUserId(),
                                                                                            testEntity.getGUID(),
                                                                                            classificationDef.getName(),
                                                                                            classificationProperties);

            Classification updatedClassification = null;
            classifications = reclassifiedEntity.getClassifications();

            if ((classifications != null) && (classifications.size() == 1))
            {
                updatedClassification = classifications.get(0);
            }

            assertCondition(((updatedClassification != null) &&
                                    (classificationDef.getName().equals(initialClassification.getName()) &&
                                            (updatedClassification.getProperties() != null))),
                            assertion4,
                            testTypeName + assertionMsg4 + testEntityDef.getName(),
                            RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId());
        }

        EntityDetail  declassifiedEntity = metadataCollection.declassifyEntity(workPad.getLocalServerUserId(),
                                                                               testEntity.getGUID(),
                                                                               classificationDef.getName());

        assertCondition(((declassifiedEntity != null) && (declassifiedEntity.getClassifications() == null)),
                        assertion5,
                        testTypeName + assertionMsg5 + testEntityDef.getName(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId());

        super.setSuccessMessage("Classifications can be managed through their lifecycle");
    }
}
