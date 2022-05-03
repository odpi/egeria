/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



/**
 * Test that all defined entities can be saved as reference copies, that the copies support all (and only) valid operations, and that the copies can be purged.
 */
public class TestSupportedEntityReferenceCopyLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-entity-reference-copy-lifecycle";
    private static final String testCaseName = "Repository entity reference copy lifecycle test case";

    /* Type */
    private static final String assertion0     = testCaseId + "-00";
    private static final String assertionMsg0  = " entity type definition matches known type  ";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " reference entity created; repository supports storage of reference copies.";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " reference entity can be retrieved as EntitySummary.";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " reference entity can be retrieved as EntityDetail.";
    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " reference entity matches the entity that was saved.";
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " reference entity has no relationships.";
    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " reference entity status cannot be updated.";
    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " reference entity properties cannot be updated.";
    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " reference entity type cannot be changed.";
    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " reference entity identity cannot be changed.";
    private static final String assertion10    = testCaseId + "-10";
    private static final String assertionMsg10 = " reference entity copy purged at TUT.";
    private static final String assertion11    = testCaseId + "-11";
    private static final String assertionMsg11 = " reference entity refresh requested by TUT.";
    private static final String assertion12    = testCaseId + "-12";
    private static final String assertionMsg12 = " reference entity refreshed.";
    private static final String assertion13    = testCaseId + "-13";
    private static final String assertionMsg13 = " refreshed reference entity matches original.";
    private static final String assertion14    = testCaseId + "-14";
    private static final String assertionMsg14 = " reference entity purged following delete at CTS.";

    private static final String assertion15    = testCaseId + "-15";
    private static final String assertionMsg15 = " master entity created.";
    private static final String assertion16    = testCaseId + "-16";
    private static final String assertionMsg16 = " reference entity created with mappingProperties.";
    private static final String assertion17    = testCaseId + "-17";
    private static final String assertionMsg17 = " reference entity retrieved with mappingProperties.";

    private static final String assertion18    = testCaseId + "-18";
    private static final String assertionMsg18 = " reference entity re-homed.";
    private static final String assertion19    = testCaseId + "-19";
    private static final String assertionMsg19 = " rehomed master entity has been retrieved.";
    private static final String assertion20    = testCaseId + "-20";
    private static final String assertionMsg20 = " rehomed master entity has correct home metadataCollectionId.";






    private final RepositoryConformanceWorkPad   workPad;
    private final String                         metadataCollectionId;
    private final EntityDef                      entityDef;
    private final String                         testTypeName;

    private final List<EntityDetail>             createdEntitiesCTS        = new ArrayList<>();  // these are all master instances
    private final List<EntityDetail>             createdEntitiesTUT        = new ArrayList<>();  // these are all master instances
    private final List<EntityDetail>             createdEntityRefCopiesTUT = new ArrayList<>();  // these are all ref copies


    /*
     * A propagation timeout is used to limit how long the testcase will wait for
     * the propagation of an OMRS instance event and consequent processing at the TUT (or CTS).
     * Each time the testcase waits it does so in a 100ms polling loop, to minimise overall delay.
     * The wait loops will wait for pollCount iterations of pollPeriod, so a pollCount of x10
     * results in a 1000ms (1s) timeout.
     *
     */
    private Integer           pollCount   = 100;
    private Integer           pollPeriod  = 100;   // milliseconds



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntityReferenceCopyLifecycle(RepositoryConformanceWorkPad workPad,
                                                     EntityDef                    entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
              RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        this.workPad              = workPad;
        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.entityDef            = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                                                    testCaseId,
                                                    testCaseName);

        /*
         * Enforce minimum pollPeriod and pollCount.
         */
        this.pollPeriod = Math.max(this.pollPeriod, 100);
        this.pollCount  = Math.max(this.pollCount, 1);

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
         * Check that the entity type matches the known type from the repository helper
         */
        OMRSRepositoryConnector cohortRepositoryConnector = null;
        OMRSRepositoryHelper repositoryHelper = null;
        if (workPad != null) {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
            repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        }

        EntityDef knownEntityDef = null;
        if (repositoryHelper != null) {
            knownEntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), entityDef.getName());
        }
        verifyCondition((entityDef.equals(knownEntityDef)),
                        assertion0,
                        testTypeName + assertionMsg0,
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());


        /*
         * This test will:
         *
         * Create an entity of the defined type in the local (CTS) repository.
         * This will cause an instance event to be flowed to the TUT, which should then (by default) save a reference copy of the instance.
         *
         * Attempt to retrieve the reference copy from the TUT.
         *
         * If this results in entity not known, assert that the TUT does not support reference copies, so set the discovered property to disabled, and abandon the remainder of the test.
         *
         * Else, if the reference copy is known - add to discovered properties and continue with the remaining test requirements.
         *
         * The following tests are run against the reference copy:
         *
         * Validate that the reference copy can be retrieved as an EntitySummary
         * Validate that the reference copy can be retrieved as an EntityDetail
         * Validate that the reference copy 'matches' the local entity.
         * Verify that the reference copy does not have any relationships.
         *
         * Verify that it is not possible to update the status of the reference copy.
         * Verify that it is not possible to update the properties of the reference copy.
         * Verify that it is not possible to re-type the reference copy.
         * Verify that it is not possible to re-identify the reference copy.
         *
         * Purge the reference copy (only) and then request a refresh and ensure that a new ref copy is created.
         *
         * Delete and purge the original local entity.
         * Because the CTS server is using local in-memory repository a soft delete must precede the purge.
         * This should flow an instance event to the TUT causing the ref copy to be purged.
         * Attempt to get the ref copy. This should fail.
         *
         * THE NEXT TEST IS PERFORMED LAST AS IT PLACES THE COHORT IN AN INVALID STATE.
         * Create another original entity, causing the creation of a reference copy in the TUT.
         * Verify that it IS possible to re-home the reference copy.
         * Delete and purge the original entity and REMOTELY delete and purge the TUT's copy of the entity (which is no longer a reference copy).
         * Note that this last part must be performed on the TUT.
         *
         */


        /*
         * This test needs a connector to the local repository - i.e. the in-memory repository running locally to the CTS server.
         */
        OMRSMetadataCollection ctsMetadataCollection = repositoryConformanceWorkPad.getLocalRepositoryConnector().getMetadataCollection();




        /*
         * Generate property values for all the type's defined properties, including inherited properties
         * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
         * thereby getting into the connector-logic beyond the property validation. It also creates an
         * entity that is logically complete - versus an instance with just the locally-defined properties.
         */

        EntityDetail newEntity = null;
        if (workPad != null) {
            newEntity = ctsMetadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                                     entityDef.getGUID(),
                                                                     super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef),
                                                                     null,
                                                                     null);
        }

        createdEntitiesCTS.add(newEntity);

        /*
         * This test does not verify the content of the entity - that is tested in the entity-lifecycle tests
         */



        /*
         * There should be a reference copy of the entity stored in the TUT
         */

        EntityDetail refEntity = null;



        /*
         * Retrieve the ref copy from the TUT - if it does not exist, assert that ref copies are not a discovered property
         * Have to be prepared to wait until event has propagated and TUT has created a reference copy of the entity.
         */
        long elapsedTime = 0;
        try {
            Integer remainingCount = this.pollCount;
            while (refEntity == null && remainingCount > 0) {

                if (workPad != null) {
                    long start = System.currentTimeMillis();
                    refEntity = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID());
                    elapsedTime = System.currentTimeMillis() - start;
                }
                Thread.sleep(this.pollPeriod);
                remainingCount--;

            }
            if (refEntity == null && remainingCount == 0) {
                if (workPad != null) {
                    ConformanceSuiteAuditCode overflow = ConformanceSuiteAuditCode.POLLING_OVERFLOW;
                    workPad.getAuditLog()
                            .logRecord(assertion1,
                                    overflow.getLogMessageId(),
                                    overflow.getSeverity(),
                                    overflow.getFormattedLogMessage(pollCount.toString(), pollPeriod.toString()),
                                    null,
                                    overflow.getSystemAction(),
                                    overflow.getUserAction());
                }
            }
        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "isEntityKnown";
            String operationDescription = "retrieve an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            if (newEntity != null) {
                parameters.put("entityGUID", newEntity.getGUID());
            }
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }



        /*
         * If this proves to be a performance problem it might be preferable to refactor the testcase to create all local
         * instances and batch the GUIDs. On completion of the batch, look for the reference copies.
         */

        if (refEntity != null) {
            /*
             * If we retrieved the reference copy of the entity - we can assert that the TUT supports reference copies.
             */

            assertCondition((true),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId(),
                            "isEntityKnown",
                            elapsedTime);


            createdEntityRefCopiesTUT.add(refEntity);


        } else {

            /*
             * Report that reference storage requirement is not supported.
             */
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

            /*
             * Terminate the test
             */
            return;

        }


        /*
         * Validate that the reference copy can be consistently retrieved from the TUT and that the retrieved reference copy 'matches' what was saved.
         */

        String retrievalOperationName = "";

        EntityDetail retrievedReferenceCopy = null;

        try {

            long start = System.currentTimeMillis();
            EntitySummary entitySummary = metadataCollection.getEntitySummary(workPad.getLocalServerUserId(), newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            retrievalOperationName = "getEntitySummary";
            verifyCondition((entitySummary != null),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                            "getEntitySummary",
                            elapsedTime);

            retrievalOperationName = "getEntityDetail";
            start = System.currentTimeMillis();
            retrievedReferenceCopy = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((retrievedReferenceCopy != null),
                            assertion3,
                            testTypeName + assertionMsg3,
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                            "getEntityDetail",
                            elapsedTime);

        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String operationDescription = "retrieve an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, retrievalOperationName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        /*
         * Verify that the retrieved reference copy matches the original entity
         */

        verifyCondition((newEntity.equals(retrievedReferenceCopy)),
                        assertion4,
                        testTypeName + assertionMsg4,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());



        /*
         * No relationships have been created so none should be returned.
         */
        try {
            if (retrievedReferenceCopy != null) {
                long start = System.currentTimeMillis();
                List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(workPad.getLocalServerUserId(),
                        retrievedReferenceCopy.getGUID(),
                        null,
                        0,
                        null,
                        null,
                        null,
                        null,
                        0);
                elapsedTime = System.currentTimeMillis() - start;
                verifyCondition((relationships == null),
                                assertion5,
                                testTypeName + assertionMsg5,
                                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId(),
                                "getRelationshipsForEntity-negative",
                                elapsedTime);
            }
        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "getRelationshipsForEntity";
            String operationDescription = "retrieve the relationships for an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            parameters.put("relationshipTypeGUID", "null");
            parameters.put("fromRelationshipElement", Integer.toString(0));
            parameters.put("limitResultsByStatus", "null");
            parameters.put("asOfTime", "null");
            parameters.put("sequencingProperty", "null");
            parameters.put("sequencingOrder", "null");
            parameters.put("pageSize", Integer.toString(0));
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        /*
         * If the entity def has any valid status values (including DELETED), attempt
         * to modify the status of the retrieved reference copy - this should fail
         */

        for (InstanceStatus validInstanceStatus : entityDef.getValidInstanceStatusList()) {

            long start = System.currentTimeMillis();
            try {

                EntityDetail updatedEntity = metadataCollection.updateEntityStatus(workPad.getLocalServerUserId(), retrievedReferenceCopy.getGUID(), validInstanceStatus);
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition((false),
                                assertion6,
                                testTypeName + assertionMsg6,
                                RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                                RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId(),
                                "updateEntityStatus-negative",
                                elapsedTime);

            } catch (InvalidParameterException e) {
                /*
                 * We are not expecting the status update to work - it should have thrown an InvalidParameterException
                 */
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition((true),
                                assertion6,
                                testTypeName + assertionMsg6,
                                RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                                RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId(),
                                "updateEntityStatus-negative",
                                elapsedTime);
            } catch (Exception exc) {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "updateEntityStatus";
                String operationDescription = "update the status of an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                if (retrievedReferenceCopy != null) {
                    parameters.put("entityGUID", retrievedReferenceCopy.getGUID());
                }
                parameters.put("newStatus", validInstanceStatus.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }

        }


        /*
         * Attempt to modify one or more property of the retrieved reference copy. This is illegal so it should fail.
         */


        if (retrievedReferenceCopy != null && (retrievedReferenceCopy.getProperties() != null) &&
                (retrievedReferenceCopy.getProperties().getInstanceProperties() != null) &&
                (!retrievedReferenceCopy.getProperties().getInstanceProperties().isEmpty())) {
            InstanceProperties minEntityProps = super.getMinPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

            long start = System.currentTimeMillis();
            try {

                EntityDetail minPropertiesEntity = metadataCollection.updateEntityProperties(workPad.getLocalServerUserId(), retrievedReferenceCopy.getGUID(), minEntityProps);
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition((false),
                        assertion7,
                        testTypeName + assertionMsg7,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId(),
                        "updateEntityProperties-negative",
                        elapsedTime);

            } catch (InvalidParameterException e) {
                /*
                 * We are not expecting the status update to work - it should have thrown an InvalidParameterException
                 */
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition((true),
                        assertion7,
                        testTypeName + assertionMsg7,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId(),
                        "updateEntityProperties-negative",
                        elapsedTime);
            } catch (Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "updateEntityProperties";
                String operationDescription = "update the properties of an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", retrievedReferenceCopy.getGUID());
                parameters.put("properties", minEntityProps.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }

        }


        /*
         * Verify that it is not possible to re-type the reference copy.
         * This test is performed using the same type as the original - the repository should not get as far as
         * even looking at the type or considering changing it. For simplicity of testcode this test therefore
         * uses the original type.
         * This test is performed against the TUT.
         */

        long start = System.currentTimeMillis();
        try {

            EntityDetail reTypedEntity = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                                                         newEntity.getGUID(),
                                                                         entityDef,
                                                                         entityDef); // see comment above about using original type
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((false),
                            assertion8,
                            testTypeName + assertionMsg8,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId(),
                            "reTypeEntity-negative",
                            elapsedTime);

        } catch (InvalidParameterException e) {

            /*
             * We are not expecting the type update to work - it should have thrown an InvalidParameterException
             */
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion8,
                            testTypeName + assertionMsg8,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId(),
                            "reTypeEntity-negative",
                            elapsedTime);
        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "reTypeEntity";
            String operationDescription = "retype an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            parameters.put("currentTypeDefSummary", entityDef.toString());
            parameters.put("newTypeDefSummary", entityDef.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }



        /*
         * Verify that it is not possible to re-identify the reference copy.
         * This test is performed using a different GUID to the original. The actual value should not be looked at
         * by the repository - it should reject the re-identify attempt prior to that.
         * This test is performed against the TUT.
         */

        String newGUID = UUID.randomUUID().toString();

        start = System.currentTimeMillis();
        try {

            EntityDetail reIdentifiedEntity = metadataCollection.reIdentifyEntity(workPad.getLocalServerUserId(),
                                                                                  entityDef.getGUID(),
                                                                                  entityDef.getName(),
                                                                                  newEntity.getGUID(),
                                                                                  newGUID);
            elapsedTime = System.currentTimeMillis() - start;

            if (reIdentifiedEntity != null)
                createdEntityRefCopiesTUT.add(reIdentifiedEntity);


            assertCondition((false),
                            assertion9,
                            testTypeName + assertionMsg9,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId(),
                            "reIdentifyEntity-negative",
                            elapsedTime);

        } catch (InvalidParameterException e) {

            /*
             * We are not expecting the identity update to work - it should have thrown an InvalidParameterException
             */
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion9,
                            testTypeName + assertionMsg9,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId(),
                            "reIdentifyEntity-negative",
                            elapsedTime);
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


        /*
         * Purge the reference copy and verify that by requesting a refresh, a new ref copy is created.
         * This test is performed against the TUT.
         */

        try {

            start = System.currentTimeMillis();
            metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(), refEntity);
            elapsedTime = System.currentTimeMillis() - start;

            /*
             * Note that the ref copy could be purged
             */
            assertCondition((true),
                            assertion10,
                            testTypeName + assertionMsg10,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId(),
                            "purgeEntityReferenceCopy",
                            elapsedTime);

        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "purgeEntityReferenceCopy";
            String operationDescription = "purge a reference copy of an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entity", refEntity.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        try {
            start = System.currentTimeMillis();
            metadataCollection.refreshEntityReferenceCopy(workPad.getLocalServerUserId(),
                                                          refEntity.getGUID(),
                                                          entityDef.getGUID(),
                                                          entityDef.getName(),
                                                          ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()));
            elapsedTime = System.currentTimeMillis() - start;

            /*
             * Note that the refresh request could be sent
             */
            assertCondition((true),
                            assertion11,
                            testTypeName + assertionMsg11,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId(),
                            "refreshEntityReferenceCopy",
                            elapsedTime);
        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "refreshEntityReferenceCopy";
            String operationDescription = "request a refresh of a reference copy of an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", refEntity.getGUID());
            parameters.put("typeDefGUID", entityDef.getGUID());
            parameters.put("typeDefName", entityDef.getName());
            parameters.put("homeMetadataCollectionId", ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()));
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        /*
         * Wait and verify that a new ref copy is created....
         */
        /*
         * There should be a reference copy of the entity stored in the TUT
         */

        EntityDetail refreshedEntityRefCopy = null;


        /*
         * Retrieve the ref copy from the TUT - if it does not exist, assert that ref copies are not a discovered property
         * Have to be prepared to wait until event has propagated and TUT has created a reference copy of the entity.
         */
        try {
            Integer remainingCount = this.pollCount;
            while (refreshedEntityRefCopy == null && remainingCount > 0) {

                refreshedEntityRefCopy = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID());
                Thread.sleep(this.pollPeriod);
                remainingCount--;
            }
            if (refreshedEntityRefCopy == null && remainingCount == 0) {
                ConformanceSuiteAuditCode overflow = ConformanceSuiteAuditCode.POLLING_OVERFLOW;
                workPad.getAuditLog()
                        .logRecord(assertion12,
                                overflow.getLogMessageId(),
                                overflow.getSeverity(),
                                overflow.getFormattedLogMessage(pollCount.toString(), pollPeriod.toString()),
                                null,
                                overflow.getSystemAction(),
                                overflow.getUserAction());
            }
        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "isEntityKnown";
            String operationDescription = "retrieve an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        /*
         * Verify that the reference copy can be retrieved form the TUT and matches the original...
         */

        try {

            start = System.currentTimeMillis();
            refreshedEntityRefCopy = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "getEntityDetail";
            String operationDescription = "retrieve an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        assertCondition((refreshedEntityRefCopy != null),
                        assertion12,
                        testTypeName + assertionMsg12,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId(),
                        "getEntityDetail",
                        elapsedTime);


        /*
         * Verify that the retrieved reference copy matches the original entity
         */

        verifyCondition((newEntity.equals(refreshedEntityRefCopy)),
                        assertion13,
                        testTypeName + assertionMsg13,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());



        /*
         * Delete (soft then hard) the CTS local entity - these operations are performed on the local (CTS) repo.
         * They should cause an OMRS instance event to flow to the TUT and for the ref copy to be purged
         */

        try {

            EntityDetail deletedEntity = ctsMetadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                            newEntity.getType().getTypeDefGUID(),
                                                                            newEntity.getType().getTypeDefName(),
                                                                            newEntity.getGUID());
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        ctsMetadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                          newEntity.getType().getTypeDefGUID(),
                                          newEntity.getType().getTypeDefName(),
                                          newEntity.getGUID());



        /*
         * Test that the reference copy has been removed from the TUT repository
         */

        /*
         * Since it may take time to propagate the purge event, retry until the entity is no longer known at the TUT.
         */

        try {
            EntityDetail survivingEntRefCopy;
            Integer remainingCount = this.pollCount;
            do {
                survivingEntRefCopy = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID());
                Thread.sleep(this.pollPeriod);
                remainingCount--;
            } while (survivingEntRefCopy != null && remainingCount > 0);
            if (survivingEntRefCopy == null && remainingCount == 0) {
                ConformanceSuiteAuditCode overflow = ConformanceSuiteAuditCode.POLLING_OVERFLOW;
                workPad.getAuditLog()
                        .logRecord(assertion14,
                                overflow.getLogMessageId(),
                                overflow.getSeverity(),
                                overflow.getFormattedLogMessage(pollCount.toString(), pollPeriod.toString()),
                                null,
                                overflow.getSystemAction(),
                                overflow.getUserAction());
            }
        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "isEntityKnown";
            String operationDescription = "retrieve an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        try {
            start = System.currentTimeMillis();
            metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((false),
                            assertion14,
                            testTypeName + assertionMsg14,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId(),
                            "getEntityDetail-negative",
                            elapsedTime);

        } catch (EntityNotKnownException exception) {
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion14,
                            testTypeName + assertionMsg14,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId(),
                            "getEntityDetail-negative",
                            elapsedTime);
        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "getEntityDetail";
            String operationDescription = "retrieve an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        /*
         * ======================================================================================================
         * The remaining tests in this test case use a different approach to creating the entity to be saved.
         * Instead of creating a master instance at the CTS server and relying on OMRS event propagation to
         * trigger the save of the reference copy at the TUT, the copy is fabricated in the test code and is
         * saved directly using the saveEntityReferenceCopy API.
         *
         * For the next test, this is because the test code needs access to mappingProperties.
         */



        /*
         * To accommodate repositories that do not support the creation of instances, wrap the creation of the entity
         * in a try..catch to check for FunctionNotSupportedException. If the connector throws this, then give up
         * on the test by setting the discovered property to disabled and returning.
         */

        EntityDetail entityWithMappingProperties;
        InstanceProperties instanceProperties = null;

        try {
            /*
             * Create an entity reference copy of the entity type.
             * To do this, a local entity is created, copied and deleted/purged. The copy is modified (so that it
             * appears to come from an unknown/defunct remote metadata collection). It is then saved as a reference copy
             */


            /*
             * Generate property values for all the type's defined properties, including inherited properties
             */

            instanceProperties = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

            start = System.currentTimeMillis();
            entityWithMappingProperties = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                                       entityDef.getGUID(),
                                                                       instanceProperties,
                                                                       null,
                                                                       null);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion15,
                            testTypeName + assertionMsg15,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "addEntity",
                            elapsedTime);

            createdEntitiesTUT.add(entityWithMappingProperties);


        } catch (FunctionNotSupportedException exception) {

            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * Report the inability to create instances and give up on the testcase....
             */

            super.addNotSupportedAssertion(assertion15,
                                           assertionMsg15,
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            return;

        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "addEntity";
            String operationDescription = "add an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("initialProperties", instanceProperties != null ? instanceProperties.toString() : "null");
            parameters.put("initialClasiifications", "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        /*
         * This test does not verify the content of the entity - that is tested in the entity-lifecycle tests
         */


        /*
         * Make a copy of the entity under a different variable name - not strictly necessary but makes things clearer - then modify it so
         * it appears to be from a remote metadata collection.
         */

        EntityDetail remoteEntityWithMappingProperties = entityWithMappingProperties;

        /*
         * Hard delete the new entity - we have no further use for it
         * If the repository under test supports soft delete, the entity must be deleted before being purged
         */

        try {
            metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                            entityWithMappingProperties.getType().getTypeDefGUID(),
                                            entityWithMappingProperties.getType().getTypeDefName(),
                                            entityWithMappingProperties.getGUID());
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "deleteEntity";
            String operationDescription = "delete an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", newEntity.getType().getTypeDefGUID());
            parameters.put("typeDefName", newEntity.getType().getTypeDefName());
            parameters.put("obsoleteEntityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        try {
            metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                           entityWithMappingProperties.getType().getTypeDefGUID(),
                                           entityWithMappingProperties.getType().getTypeDefName(),
                                           entityWithMappingProperties.getGUID());
        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "purgeEntity";
            String operationDescription = "purge an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", entityWithMappingProperties.getType().getTypeDefGUID());
            parameters.put("typeDefName", entityWithMappingProperties.getType().getTypeDefName());
            parameters.put("deletedEntityGUID", entityWithMappingProperties.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        // Beyond this point, there should be no further references to entityWithMappingProperties
        entityWithMappingProperties = null;


        /*
         * Modify the 'remote' entity so that it looks like it came from a different home repository
         */
        String REMOTE_PREFIX = "remote";

        String localEntityGUID = remoteEntityWithMappingProperties.getGUID();
        String remoteEntityGUID = REMOTE_PREFIX + localEntityGUID.substring(REMOTE_PREFIX.length());
        remoteEntityWithMappingProperties.setGUID(remoteEntityGUID);

        String localMetadataCollectionName = remoteEntityWithMappingProperties.getMetadataCollectionName();
        String remoteMetadataCollectionName = REMOTE_PREFIX + "-metadataCollection-not-" + localMetadataCollectionName;
        remoteEntityWithMappingProperties.setMetadataCollectionName(remoteMetadataCollectionName);

        String localMetadataCollectionId = remoteEntityWithMappingProperties.getMetadataCollectionId();
        String remoteMetadataCollectionId = REMOTE_PREFIX + localMetadataCollectionId.substring(REMOTE_PREFIX.length());
        remoteEntityWithMappingProperties.setMetadataCollectionId(remoteMetadataCollectionId);

        /*
         * Set mapping properties on the synthetic remote entity...
         */

        Map<String, Serializable> mappingProperties = new HashMap<>();
        mappingProperties.put("stringMappingPropertyKey", "stringMappingPropertyValue");
        mappingProperties.put("integerMappingPropertyKey", 12);

        /*
         * Save a reference copy of the 'remote' entity
         */

        try {

            start = System.currentTimeMillis();
            metadataCollection.saveEntityReferenceCopy(workPad.getLocalServerUserId(), remoteEntityWithMappingProperties);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion16,
                            testTypeName + assertionMsg16,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId(),
                            "saveEntityReferenceCopy",
                            elapsedTime);

            createdEntityRefCopiesTUT.add(remoteEntityWithMappingProperties);



            EntityDetail retrievedReferenceCopyWithMappingProperties = null;

            try {

                start = System.currentTimeMillis();
                retrievedReferenceCopyWithMappingProperties = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), remoteEntityGUID);
                elapsedTime = System.currentTimeMillis() - start;

            }
            catch (Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "getEntityDetail";
                String operationDescription = "retrieve an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", remoteEntityGUID);
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }


            assertCondition((retrievedReferenceCopyWithMappingProperties.equals(remoteEntityWithMappingProperties)),
                            assertion17,
                            assertionMsg17 + entityDef.getName(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId(),
                            "getEntityDetail",
                            elapsedTime);





            /*
             * Continuing with the tests based on a locally synthesized entity used as a reference copy, emulating
             * adoption of an instance from a virtual or defunct remote repository....
             *
             *
             * The next test verifies whether it is possible to re-home a reference copy. The rehome operation is
             * effectively a pull of the master-ship of the reference copy - i.e. it is a request made by the TUT.
             * This should not be performed on a reference copy of an entity whose master is on the CTS server - the
             * CTS server is not defunct and we also cannot delete the master instance without also triggering an event
             * that would trigger clean up of the TUT's reference copy. The bottom line is that performing a rehome on
             * the CTS's instance woudld place the cohort into an invalid state - and taht is not what we are aiming to
             * test. Therefore the rehome is performed on the locally synthesized instance used above for the
             * mappingProperties test.
             */



            /*
             * Rehome of an instance is a pull operation - i.e. it must be conducted by the TUT as the TUT holds the ref copy.
             */
            EntityDetail newMasterEntity = null;

            try {

                start = System.currentTimeMillis();
                newMasterEntity = metadataCollection.reHomeEntity(workPad.getLocalServerUserId(),
                                                                  remoteEntityGUID,
                                                                  entityDef.getGUID(),
                                                                  entityDef.getName(),
                                                                  ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()),
                                                                  metadataCollectionId,
                                                                  repositoryConformanceWorkPad.getTutRepositoryConnector().getMetadataCollectionName());
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition((true),
                                assertion18,
                                testTypeName + assertionMsg18,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId(),
                                "reHomeEntity",
                                elapsedTime);

                createdEntitiesTUT.add(newMasterEntity);

                /*
                 * Verify that the new master instance can be retrieved
                 */

                try {

                    retrievedReferenceCopyWithMappingProperties = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), remoteEntityGUID);

                } catch (Exception exc) {
                    /*
                     * We are not expecting any other exceptions from this method call. Log and fail the test.
                     */

                    String methodName = "getEntityDetail";
                    String operationDescription = "retrieve an entity of type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("entityGUID", remoteEntityGUID);
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                    throw new Exception(msg, exc);

                }

                assertCondition((retrievedReferenceCopyWithMappingProperties != null),
                                assertion19,
                                assertionMsg19 + entityDef.getName(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());

                /*
                 * Verify that the new master instance has the local metadataCollectionId
                 */
                String instanceHome;
                if (retrievedReferenceCopyWithMappingProperties != null) {
                    instanceHome = retrievedReferenceCopyWithMappingProperties.getMetadataCollectionId();
                    assertCondition((instanceHome.equals(metadataCollectionId)),
                            assertion20,
                            assertionMsg20 + entityDef.getName(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());
                }

                /*
                 * Now clean up.
                 * If the rehome worked we have a master instance locally - so we need to (optionally) delete and then (always) purge.
                 * If the rehome did not work then we have a local reference copy to purge.
                 * In both cases the operation is performed at the TUT.
                 */

                if (newMasterEntity != null) {

                    /*
                     * The rehome operation worked - perform a soft delete (optional) followed by a purge.
                     */
                    try {
                        metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                        remoteEntityWithMappingProperties.getType().getTypeDefGUID(),
                                                        remoteEntityWithMappingProperties.getType().getTypeDefName(),
                                                        remoteEntityWithMappingProperties.getGUID());
                    } catch (FunctionNotSupportedException exception) {

                        /*
                         * This is OK - we can NO OP and just proceed to purgeEntity
                         */
                    }

                    metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                                   remoteEntityWithMappingProperties.getType().getTypeDefGUID(),
                                                   remoteEntityWithMappingProperties.getType().getTypeDefName(),
                                                   remoteEntityWithMappingProperties.getGUID());
                } else {
                    /*
                     * The rehome operation did not work - the TUT is still holding a reference copy
                     */

                    /*
                     * Purge the reference copy.
                     */

                    metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(),
                                                                remoteEntityWithMappingProperties.getGUID(),
                                                                remoteEntityWithMappingProperties.getType().getTypeDefGUID(),
                                                                remoteEntityWithMappingProperties.getType().getTypeDefName(),
                                                                remoteEntityWithMappingProperties.getMetadataCollectionId());

                }


            } catch (FunctionNotSupportedException exception) {

                /*
                 * Because rehome is an optional method, this is not fatal - just record that the connector does not support rehome
                 */

                super.addNotSupportedAssertion(assertion18,
                                               assertionMsg18,
                                               RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                                               RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());

            } catch (Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "reHomeEntity";
                String operationDescription = "rehome an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", remoteEntityGUID);
                parameters.put("typeDefGUID", entityDef.getGUID());
                parameters.put("typeDefName", entityDef.getName());
                parameters.put("homeMetadataCollecitonId", ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()));
                parameters.put("newHomeMetadataCollecitonId", metadataCollectionId);
                parameters.put("newHomeMetadataCollecitonName", repositoryConformanceWorkPad.getTutRepositoryConnector().getMetadataCollectionName());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }


        }
        catch (FunctionNotSupportedException e) {

            super.addNotSupportedAssertion(assertion16,
                                           assertionMsg16,
                                           RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        }
        catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "saveEntityReferenceCopy";
            String operationDescription = "save a reference copy of an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entity", remoteEntityWithMappingProperties.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }



        super.setSuccessMessage("Reference copies of entities can be managed through their lifecycle");


    }



    /**
     * Method to clean any instance created by the test case that has not already been cleaned by the running of the test.
     *
     * @throws Exception something went wrong but there is no particular action to take.
     */
    public void cleanup() throws Exception
    {

        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        /*
         * For this testcase we created master instances at the CTS and TUT and ref copies at the TUT - three lists to clean up
         */
        if (createdEntitiesCTS != null && !createdEntitiesCTS.isEmpty()) {

            /*
             * Instances were created - clean them up.
             */

            for (EntityDetail entity : createdEntitiesCTS) {

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

        if (createdEntitiesTUT != null && !createdEntitiesTUT.isEmpty()) {

            /*
             * Instances were created - clean them up.
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

        if (createdEntityRefCopiesTUT != null && !createdEntityRefCopiesTUT.isEmpty()) {

            /*
             * Instances were created - clean them up.
             */

            for (EntityDetail entity : createdEntityRefCopiesTUT) {

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