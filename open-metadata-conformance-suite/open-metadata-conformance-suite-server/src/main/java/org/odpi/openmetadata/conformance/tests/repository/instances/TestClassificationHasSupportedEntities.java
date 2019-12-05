/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Test that all defined classifications can be linked to at least one of the supported entities.
 */
public class TestClassificationHasSupportedEntities extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-classification-entities";
    private static final String testCaseName = "Repository classification has supported entities test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " classification can attach to at least one supported entity.";

    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " supported for classification.";

    private static final String successMessage = " has at least one supported entity";

    private Map<String, EntityDef> entityDefs;
    private ClassificationDef      classificationDef;

    private List<EntityDef>        supportedEntityDefsForClassification = new ArrayList<>();

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDefs list of valid entities
     * @param classificationDef list of valid classifications
     */
    public TestClassificationHasSupportedEntities(RepositoryConformanceWorkPad workPad,
                                                  Map<String, EntityDef>       entityDefs,
                                                  ClassificationDef            classificationDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
              RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId());

        this.classificationDef = classificationDef;
        this.entityDefs = entityDefs;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {

        /*
         * Verify that the supplied TypeDef is valid and update the testId
         */
        String  testTypeName = this.updateTestIdByType(classificationDef.getName(),
                                                       testCaseId,
                                                       testCaseName);

        /*
         * The classificationDef includes the list of entity types that it can be attached to.
         * This list is used to drive a loop of tests to attach the classification to each
         * entity type.
         */
        List<TypeDefLink>   classifiableEntities = classificationDef.getValidEntityDefs();
        if (classifiableEntities != null)
        {
            for (TypeDefLink   typeDefLink : classifiableEntities)
            {
                EntityDef testEntityDef = this.getEntityDef(entityDefs, typeDefLink);

                /*
                 * Ignore entities that are not supported by the local repository.
                 */
                if (testEntityDef != null)
                {
                    supportedEntityDefsForClassification.add(testEntityDef);

                    assertCondition((true),
                            assertion2,
                            testTypeName + assertionMsg2 + classificationDef.getName(),
                            RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                            RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());

                }
                else
                {

                    super.addNotSupportedAssertion(assertion2,
                            testTypeName + assertionMsg2 + classificationDef.getName(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

                }
            }
        }


        /*
         * Verify that there are entities to connect this classification to.
         * It the repository does not support any of the valid entity types for this classification.
         * it should not be supporting this classification.
         */
        assertCondition((! supportedEntityDefsForClassification.isEmpty()),
                        assertion1,
                        testTypeName + assertionMsg1,
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());

        super.setSuccessMessage(classificationDef.getName() + successMessage);
    }


    /**
     * Return the list of supported entity types for this classification.
     *
     * @return list of entity defs
     */
    public List<EntityDef> getSupportedEntityDefsForClassification()
    {
        return  supportedEntityDefsForClassification;
    }
}
