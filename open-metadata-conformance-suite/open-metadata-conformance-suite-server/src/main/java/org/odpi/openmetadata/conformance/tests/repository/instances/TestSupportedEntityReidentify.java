/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * Test that all defined entities can be created, reidentified and deleted
 */
public class TestSupportedEntityReidentify extends RepositoryConformanceTestCase {
    private static final String testCaseId = "repository-entity-reidentify";
    private static final String testCaseName = "Repository entity reidentify test case";

    private static final String assertion1 = testCaseId + "-01";
    private static final String assertionMsg1 = " new entity created.";

    private static final String assertion2 = testCaseId + "-02";
    private static final String assertionMsg2 = " new entity retrieved.";

    private static final String assertion3 = testCaseId + "-03";
    private static final String assertionMsg3 = " entity is reidentified.";

    private static final String assertion4 = testCaseId + "-04";
    private static final String assertionMsg4 = " entity with new identity version number is ";

    private static final String assertion5 = testCaseId + "-05";
    private static final String assertionMsg5 = " entity no longer retrievable by previous GUID.";

    private static final String assertion6 = testCaseId + "-06";
    private static final String assertionMsg6 = " entity retrievable by new GUID.";


    private static final String assertion7 = testCaseId + "-07";
    private static final String assertionMsg7 = " repository supports creation of instances.";

    private static final String assertion8 = testCaseId + "-08";
    private static final String assertionMsg8 = " repository supports reidentify of instances.";


    private final EntityDef entityDef;
    private final String testTypeName;


    private final List<EntityDetail> createdEntitiesTUT = new ArrayList<>();


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad   place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntityReidentify(RepositoryConformanceWorkPad workPad,
                                         EntityDef entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

        this.entityDef = entityDef;
        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
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

        EntityDetail newEntity;

        InstanceProperties instProps = null;

        try {


            /*
             * Generate property values for all the type's defined properties, including inherited properties
             * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
             * thereby getting into the connector-logic beyond the property validation. It also creates an
             * entity that is logically complete - versus an instance with just the locally-defined properties.
             */

            instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

            long start = System.currentTimeMillis();
            newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                     entityDef.getGUID(),
                                                     instProps,
                                                     null,
                                                     null);
            long elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion7,
                            testTypeName + assertionMsg7,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "addEntity",
                            elapsedTime);

            createdEntitiesTUT.add(newEntity);

        } catch (FunctionNotSupportedException exception) {
            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * Report the inability to create instances and give up on the testcase....
             */

            super.addNotSupportedAssertion(assertion7,
                                           assertionMsg7,
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            return;
        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "addEntity";
            String operationDescription = "add an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialClasiifications", "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        assertCondition((newEntity != null),
                        assertion1,
                        testTypeName + assertionMsg1,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        /*
         * Other conditions - such as content of InstanceAuditHeader fields - are tested by Entity Lifecycle tests; so not tested here.
         */



        /*
         * Validate that the entity can be retrieved.
         */

        verifyCondition((newEntity.equals(metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID()))),
                        assertion2,
                        testTypeName + assertionMsg2,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());



        /*
         * Verify that it is possible to re-identify the entity.
         *
         * Create a new GUID and assign it to the entity.
         *
         */

        String newGUID = UUID.randomUUID().toString();

        long nextVersion = newEntity.getVersion() + 1;

        EntityDetail reIdentifiedEntity = null;

        long elapsedTime;
        try {

            long start = System.currentTimeMillis();
            reIdentifiedEntity = metadataCollection.reIdentifyEntity(workPad.getLocalServerUserId(),
                                                                     entityDef.getGUID(),
                                                                     entityDef.getName(),
                                                                     newEntity.getGUID(),
                                                                     newGUID);
            elapsedTime = System.currentTimeMillis() - start;

        } catch (FunctionNotSupportedException exception) {

            super.addNotSupportedAssertion(assertion8,
                                           assertionMsg8,
                                           RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                                           RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

            /* Give up the testcase */
            return;

        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "reIdentifyEntity";
            String operationDescription = "reidentify an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("entityGUID", newEntity.getGUID());
            parameters.put("newEntityGUID", newGUID);
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        assertCondition(true,
                        assertion8,
                        testTypeName + assertionMsg8,
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId(),
                        "reIdentifyEntity",
                        elapsedTime);

        createdEntitiesTUT.add(reIdentifiedEntity);

        verifyCondition((reIdentifiedEntity.getGUID().equals(newGUID)),
                        assertion3,
                        testTypeName + assertionMsg3,
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());


        assertCondition((reIdentifiedEntity.getVersion() >= nextVersion),
                        assertion4,
                        testTypeName + assertionMsg4 + nextVersion,
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

        /*
         * Validate that the entity can no longer be retrieved under its original GUID.
         */

        long start = System.currentTimeMillis();
        try {
            metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((false),
                            assertion5,
                            testTypeName + assertionMsg5,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId(),
                            "getEntityDetail-negative",
                            elapsedTime);

        } catch (EntityNotKnownException exception) {
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion5,
                            testTypeName + assertionMsg5,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId(),
                            "getEntityDetail-negative",
                            elapsedTime);
        }


        /*
         * Validate that the relationship can be retrieved under its new GUID.
         */

        try {
            assertCondition((reIdentifiedEntity.equals(metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newGUID))),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

        } catch (EntityNotKnownException exception) {

            assertCondition((false),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());
        }



        /*
         * Clean up the test entity.
         *
         */

        /*
         * Delete (soft then hard) the entity. This is done using the newGUID, so if the reidentify did
         * not work this will fail but that's OK.
         */

        try {

            EntityDetail deletedEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                         newEntity.getType().getTypeDefGUID(),
                                                                         newEntity.getType().getTypeDefName(),
                                                                         newGUID);
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                       newEntity.getType().getTypeDefGUID(),
                                       newEntity.getType().getTypeDefName(),
                                       newGUID);


        super.setSuccessMessage("Entities can be reidentified");

    }





    /**
     * Method to clean any instance created by the test case that has not already been cleaned by the running of the test.
     *
     * @throws Exception something went wrong but there is no particular action to take.
     */
    public void cleanup() throws Exception
    {

        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if (createdEntitiesTUT != null && !createdEntitiesTUT.isEmpty()) {

            /*
             * Instances were created - clean them up.
             * They may have already been cleaned up so be prepared to catch everything from
             * FunctionNotSupportedException to EntityNotKnownException and maybe others.
             */

            for (EntityDetail entity : createdEntitiesTUT) {

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


    /**
     * Determine if properties are as expected.
     *
     * @param firstInstanceProps is the target which must always be a non-null InstanceProperties
     * @param secondInstanceProps is the actual to be compared against first param - can be null, or empty....
     * @return match boolean
     */
    private boolean doPropertiesMatch(InstanceProperties firstInstanceProps, InstanceProperties secondInstanceProps)
    {
        boolean matchProperties = false;
        boolean noProperties = false;

        if ( (secondInstanceProps == null) ||
             (secondInstanceProps.getInstanceProperties() == null) ||
             (secondInstanceProps.getInstanceProperties().isEmpty()))
        {
            noProperties = true;
        }

        if (noProperties)
        {
            if ( (firstInstanceProps == null) ||
                 (firstInstanceProps.getInstanceProperties() == null) ||
                 (firstInstanceProps.getInstanceProperties().isEmpty()))
            {
                matchProperties = true;
            }
        }
        else
        {
            // non-empty, perform matching

            Map<String, InstancePropertyValue> secondPropertiesMap = secondInstanceProps.getInstanceProperties();
            Map<String, InstancePropertyValue> firstPropertiesMap  = firstInstanceProps.getInstanceProperties();

            boolean matchSizes = (secondPropertiesMap.size() == firstPropertiesMap.size());

            if (matchSizes)
            {
                Set<String> secondPropertiesKeySet = secondPropertiesMap.keySet();
                Set<String> firstPropertiesKeySet  = firstPropertiesMap.keySet();

                boolean matchKeys = secondPropertiesKeySet.containsAll(firstPropertiesKeySet) &&
                                    firstPropertiesKeySet.containsAll(secondPropertiesKeySet);

                if (matchKeys)
                {
                    // Assume the values match and prove it if they don't...
                    boolean matchValues = true;

                    for (String key : secondPropertiesKeySet) {
                        if (!(secondPropertiesMap.get(key).equals(firstPropertiesMap.get(key)))) {
                            matchValues = false;
                        }
                    }

                    // If all property values matched....
                    if (matchValues)
                    {
                        matchProperties = true;
                    }
                }
            }
        }

        return matchProperties;
    }
}