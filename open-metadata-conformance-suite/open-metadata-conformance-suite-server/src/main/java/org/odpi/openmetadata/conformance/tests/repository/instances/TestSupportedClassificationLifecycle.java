/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.ffdc.exception.AssertionFailureException;
import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private static final String assertion6    = testCaseId + "-6";
    private static final String assertionMsg6 = " repository supports creation of instances ";


    private final EntityDef            testEntityDef;
    private final ClassificationDef    classificationDef;
    private final String               testTypeName;

    private final List<EntityDetail>   createdEntities = new ArrayList<>();



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

        /*
         * To accommodate repositories that do not support the creation of instances, wrap the creation of the entity
         * in a try..catch to check for FunctionNotSupportedException. If the connector throws this, then give up
         * on the test by setting the discovered property to disabled and returning.
         */

        EntityDetail       testEntity;
        InstanceProperties instProps = null;

        long elapsedTime;
        try
        {
            /*
             * Generate property values for all the type's defined properties, including inherited properties
             * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
             * thereby getting into the connector-logic beyond the property validation. It also creates an
             * entity that is logically complete - versus an instance with just the locally-defined properties.
             */

            instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), testEntityDef);

            long start = System.currentTimeMillis();
            testEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                     testEntityDef.getGUID(),
                                                     instProps,
                                                     null,
                                                     null);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "addEntity",
                            elapsedTime);

            // Record the created instance for later clean up.
            createdEntities.add(testEntity);
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch (FunctionNotSupportedException exception)
        {

            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * Report the inability to create instances and give up on the testcase....
             */

            super.addNotSupportedAssertion(assertion6,
                                           assertionMsg6,
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            return;

        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */
            String methodName = "addEntity";
            String operationDescription = "add an entity of type " + testEntityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", testEntityDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialClassifications", "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        assertCondition((testEntity.getClassifications() == null),
                        assertion1,
                         assertionMsg1 + testEntityDef.getName(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId());



        EntityDetail classifiedEntity = null;
        try
        {
            long start = System.currentTimeMillis();
            classifiedEntity = metadataCollection.classifyEntity(workPad.getLocalServerUserId(),
                                                                              testEntity.getGUID(),
                                                                              classificationDef.getName(),
                                                                              null);
            elapsedTime = System.currentTimeMillis() - start;

        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "classifyEntity";
            String operationDescription = "classify an entity of type " + testEntityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", testEntity.getGUID());
            parameters.put("classificationName", classificationDef.getName());
            parameters.put("classificationProperties", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        assertCondition((classifiedEntity != null),
                        assertion2,
                        testEntityDef.getName() + assertionMsg2,
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId(),
                        "classifyEntity",
                        elapsedTime);

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
            EntityDetail reclassifiedEntity = null;

            try
            {
                long start = System.currentTimeMillis();
                reclassifiedEntity = metadataCollection.updateEntityClassification(workPad.getLocalServerUserId(),
                                                                                   testEntity.getGUID(),
                                                                                   classificationDef.getName(),
                                                                                   classificationProperties);
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "updateEntityClassification";
                String operationDescription = "update the classification of an entity of type " + testEntityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", testEntity.getGUID());
                parameters.put("classificationName", classificationDef.getName());
                parameters.put("classificationProperties", classificationProperties.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

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
                            RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId(),
                            "updateEntityClassification",
                            elapsedTime);
        }

        EntityDetail declassifiedEntity = null;

        try
        {

            long start = System.currentTimeMillis();
            declassifiedEntity = metadataCollection.declassifyEntity(workPad.getLocalServerUserId(),
                                                                                  testEntity.getGUID(),
                                                                                  classificationDef.getName());
            elapsedTime = System.currentTimeMillis() - start;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "declassifyEntity";
            String operationDescription = "declassify an entity of type " + testEntityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", testEntity.getGUID());
            parameters.put("classificationName", classificationDef.getName());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        assertCondition(((declassifiedEntity != null) && (declassifiedEntity.getClassifications() == null)),
                        assertion5,
                        testTypeName + assertionMsg5 + testEntityDef.getName(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.CLASSIFICATION_LIFECYCLE.getRequirementId(),
                        "declassifyEntity",
                        elapsedTime);

        super.setSuccessMessage("Classifications can be managed through their lifecycle");
    }


    /**
     * Method to clean any instance created by the test case that has not already been cleaned by the running of the test.
     *
     * @throws Exception something went wrong but there is no particular action to take.
     */
    public void cleanup() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if (createdEntities != null && !createdEntities.isEmpty())
        {
            /*
             * Instances were created - clean them up.
             */

            for (EntityDetail entity : createdEntities)
            {
                try
                {
                    metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                    entity.getType().getTypeDefGUID(),
                                                    entity.getType().getTypeDefName(),
                                                    entity.getGUID());
                }
                catch (FunctionNotSupportedException exception)
                {
                    // NO OP - can proceed to purge
                }
                catch (EntityNotKnownException exception)
                {
                    // Entity already cleaned up - nothing more to do here.
                    continue;
                }

                // If entity is known then (whether delete was supported or not) issue purge
                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                               entity.getType().getTypeDefGUID(),
                                               entity.getType().getTypeDefName(),
                                               entity.getGUID());
            }
        }
    }

}
