/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test that all defined classifications can be created, retrieved, updated and deleted.
 */
public class TestSupportedClassificationLifecycle extends OpenMetadataRepositoryTestCase
{
    private static final String testUserId = "ConformanceTestUser";

    private static final String testCaseId = "repository-classification-lifecycle";
    private static final String testCaseName = "Repository classification lifecycle test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " classification can attach to a supported entity.";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " classification added to entity of type ";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " classification properties added to entity of type ";
    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " classification removed from entity of type ";
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " classification added to all identified entities";


    private String                 metadataCollectionId;
    private Map<String, EntityDef> entityDefs;
    private ClassificationDef      classificationDef;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     */
    TestSupportedClassificationLifecycle(String                  workbenchId,
                                         String                  metadataCollectionId,
                                         Map<String, EntityDef>  entityDefs,
                                         ClassificationDef       classificationDef)
    {
        super(workbenchId, testCaseId, testCaseName);
        this.metadataCollectionId = metadataCollectionId;
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
        Map<String, Object>    discoveredProperties = new HashMap<>();
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();
        List<EntityDef>        testEntityDefs = new ArrayList<>();

        String   testTypeName = classificationDef.getName();

        List<TypeDefLink>   classifiableEntities = classificationDef.getValidEntityDefs();
        if (classifiableEntities != null)
        {
            for (TypeDefLink   typeDefLink : classifiableEntities)
            {
                EntityDef testEntityDef = this.getEntityDef(entityDefs, typeDefLink);

                if (testEntityDef != null)
                {
                    testEntityDefs.add(testEntityDef);
                }
            }
        }

        assertCondition((! testEntityDefs.isEmpty()), assertion1, testTypeName + assertionMsg1);

        List<EntityDetail>   classifiedEntities = new ArrayList<>();
        for (EntityDef  testEntityDef : testEntityDefs)
        {
            EntityDetail testEntity = addEntityToRepository(testTypeName, metadataCollection, testEntityDef);
            EntityDetail classifiedEntity = metadataCollection.classifyEntity(testUserId,
                                                                              testEntity.getGUID(),
                                                                              classificationDef.getName(),
                                                                              null);
            if (classifiedEntity != null)
            {
                classifiedEntities.add(classifiedEntity);
            }

            Classification initialClassification = null;

            List<Classification>  classifications = classifiedEntity.getClassifications();

            if ((classifications != null) && (classifications.size() == 1))
            {
                initialClassification = classifications.get(0);
            }

            assertCondition(((initialClassification != null) &&
                                    (classificationDef.getName().equals(initialClassification.getName()) &&
                                    (initialClassification.getProperties() == null))),
                            assertion2,
                            testTypeName + assertionMsg2 + testEntityDef.getName());


            InstanceProperties  classificationProperties = this.getPropertiesForInstance(classificationDef.getPropertiesDefinition());
            if (classificationProperties != null)
            {
                EntityDetail reclassifiedEntity = metadataCollection.updateEntityClassification(testUserId,
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
                                assertion3,
                                testTypeName + assertionMsg3 + testEntityDef.getName());
            }

            EntityDetail  declassifiedEntity = metadataCollection.declassifyEntity(testUserId,
                                                                                   testEntity.getGUID(),
                                                                                   classificationDef.getName());

            assertCondition(((declassifiedEntity != null) && (declassifiedEntity.getClassifications() == null)),
                            assertion4,
                            testTypeName + assertionMsg4 + testEntityDef.getName());
        }

        assertCondition((testEntityDefs.size() == classifiableEntities.size()), assertion5, testTypeName + assertionMsg5);


        super.result.setSuccessMessage("Classifications can be managed through their lifecycle");

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
