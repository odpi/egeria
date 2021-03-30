/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.create;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test performance of relationship creation operations.
 */
public class TestRelationshipCreation extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-creation-performance";
    private static final String TEST_CASE_NAME = "Repository relationship creation performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntities";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType instances of type: ";

    private static final String A_ADD_RELATIONSHIP     = TEST_CASE_ID + "-addRelationship";
    private static final String A_ADD_RELATIONSHIP_MSG = "Repository performs creation of instances of type: ";

    private static final String A_SAVE_RELATIONSHIP_RC     = TEST_CASE_ID + "-saveRelationshipReferenceCopy";
    private static final String A_SAVE_RELATIONSHIP_RC_MSG = "Repository performs creation of reference copies of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipCreation(PerformanceWorkPad workPad,
                                    RelationshipDef    relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_CREATION.getProfileId());

        this.relationshipDef = relationshipDef;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
                TEST_CASE_ID,
                TEST_CASE_NAME);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        int numInstances = super.getInstancesPerType();

        addRelationships(metadataCollection, numInstances);
        // TODO: addExternalRelationship(metadataCollection, numInstances);
        saveRelationshipReferenceCopies(metadataCollection, repositoryHelper, numInstances);

        super.setSuccessMessage("Relationship creation performance tests complete for: " + testTypeName);
    }

    /**
     * Attempt to add a number of new relationships.
     * @param metadataCollection through which to call addRelationship
     * @param numInstances of times to call addRelationship
     * @throws Exception on any errors
     */
    private void addRelationships(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {

        final String methodName = "addRelationship";
        InstanceProperties instProps = null;
        try {

            TypeDefLink entityOneType = relationshipDef.getEndDef1().getEntityType();
            TypeDefLink entityTwoType = relationshipDef.getEndDef2().getEntityType();

            long start = System.nanoTime();
            List<EntityDetail> entityOnes = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                    entityOneType.getGUID(),
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    numInstances);
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            assertCondition(entityOnes != null,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + entityOneType.getName(),
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntities",
                    elapsedTime);

            start = System.nanoTime();
            List<EntityDetail> entityTwos = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                    entityTwoType.getGUID(),
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    numInstances);
            elapsedTime = (System.nanoTime() - start) / 1000000;
            assertCondition(entityTwos != null,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + entityTwoType.getName(),
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntities",
                    elapsedTime);

            for (int i = 0; i < numInstances; i++) {

                EntityDetail one = entityOnes == null || entityOnes.size() <= i ? null : entityOnes.get(i);
                EntityDetail two = entityTwos == null || entityTwos.size() <= i ? null : entityTwos.get(i);

                // Can only proceed if there were sufficient existing entities to which to create relationships
                if (one != null && two != null) {
                    String entityOneGUID = one.getGUID();
                    String entityTwoGUID = two.getGUID();

                    instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef, i);
                    start = System.nanoTime();
                    Relationship result = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                            relationshipDef.getGUID(),
                            instProps,
                            entityOneGUID,
                            entityTwoGUID,
                            null);
                    elapsedTime = (System.nanoTime() - start) / 1000000;
                    performanceWorkPad.incrementRelationshipsCreated(1);

                    assertCondition(result != null,
                            A_ADD_RELATIONSHIP,
                            A_ADD_RELATIONSHIP_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_CREATION.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_ADD_RELATIONSHIP,
                    A_ADD_RELATIONSHIP_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_CREATION.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "add a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Attempt to add a number of new reference copy relationships.
     * @param metadataCollection through which to call saveRelationshipReferenceCopy
     * @param repositoryHelper used to quickly construct proper instances
     * @param numInstances of times to call saveRelationshipReferenceCopy
     * @throws Exception on any errors
     */
    private void saveRelationshipReferenceCopies(OMRSMetadataCollection metadataCollection,
                                                 OMRSRepositoryHelper repositoryHelper,
                                                 int numInstances) throws Exception
    {

        final String methodName = "saveRelationshipReferenceCopy";
        InstanceProperties instProps = null;
        try {

            for (int i = 0; i < numInstances; i++)
            {
                EntityProxy one = repositoryHelper.getNewEntityProxy(testCaseId,
                        performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        performanceWorkPad.getLocalServerUserId(),
                        relationshipDef.getEndDef1().getEntityType().getName(),
                        null,
                        null);
                EntityProxy two = repositoryHelper.getNewEntityProxy(testCaseId,
                        performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        performanceWorkPad.getLocalServerUserId(),
                        relationshipDef.getEndDef2().getEntityType().getName(),
                        null,
                        null);

                instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef, i);
                Relationship rc = repositoryHelper.getSkeletonRelationship(testCaseId,
                        performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                        performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        performanceWorkPad.getLocalServerUserId(),
                        relationshipDef.getName());
                rc.setEntityOneProxy(one);
                rc.setEntityTwoProxy(two);
                rc.setProperties(instProps);

                long start = System.nanoTime();
                metadataCollection.saveRelationshipReferenceCopy(workPad.getLocalServerUserId(), rc);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                performanceWorkPad.incrementRelationshipsCreated(1);

                assertCondition(true,
                        A_SAVE_RELATIONSHIP_RC,
                        A_SAVE_RELATIONSHIP_RC_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_CREATION.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_SAVE_RELATIONSHIP_RC,
                    A_SAVE_RELATIONSHIP_RC_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_CREATION.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "add a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
