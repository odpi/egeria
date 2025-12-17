/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.ffdc.exception.AssertionFailureException;
import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Test that all defined entities can be created, retrieved, updated and deleted.
 */
public class TestSupportedEntityLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-entity-lifecycle";
    private static final String testCaseName = "Repository entity lifecycle test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " new entity created.";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " new entity has createdBy user.";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " new entity has creation time.";
    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " new entity has correct provenance type.";
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " new entity has correct initial status.";
    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " new entity has correct type.";
    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " new entity has local metadata collection.";
    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " new entity has version greater than zero.";
    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " new entity is known.";
    private static final String assertion10    = testCaseId + "-10";
    private static final String assertionMsg10 = " new entity summarized.";
    private static final String assertion11    = testCaseId + "-11";
    private static final String assertionMsg11 = " new entity retrieved.";
    private static final String assertion12    = testCaseId + "-12";
    private static final String assertionMsg12 = " new entity is unattached.";
    private static final String assertion13    = testCaseId + "-13";
    private static final String assertionMsg13 = " entity status updated.";
    private static final String assertion14    = testCaseId + "-14";
    private static final String assertionMsg14 = " entity new status is ";
    private static final String assertion15    = testCaseId + "-15";
    private static final String assertionMsg15 = " entity with new status version number is ";
    private static final String assertion16    = testCaseId + "-16";
    private static final String assertionMsg16 = " entity can not be set to DELETED status.";
    private static final String assertion17    = testCaseId + "-17";
    private static final String assertionMsg17 = " entity properties cleared to min.";
    private static final String assertion18    = testCaseId + "-18";
    private static final String assertionMsg18 = " entity with min properties version number is ";
    private static final String assertion19    = testCaseId + "-19";
    private static final String assertionMsg19 = " entity has properties restored.";
    private static final String assertion20    = testCaseId + "-20";
    private static final String assertionMsg20 = " entity after undo version number is ";
    private static final String assertion21    = testCaseId + "-21";
    private static final String assertionMsg21 = " entity deleted version number is ";
    private static final String assertion22    = testCaseId + "-22";
    private static final String assertionMsg22 = " entity no longer retrievable after delete.";
    private static final String assertion23    = testCaseId + "-23";
    private static final String assertionMsg23 = " entity restored ";
    private static final String assertion24    = testCaseId + "-24";
    private static final String assertionMsg24 = " entity restored version number is ";
    private static final String assertion25    = testCaseId + "-25";
    private static final String assertionMsg25 = " entity retrieved following restore ";
    private static final String assertion26    = testCaseId + "-26";
    private static final String assertionMsg26 = " entity purged.";
    private static final String assertion27    = testCaseId + "-27";
    private static final String assertionMsg27 = " historical retrieval returned correct version of entity ";
    private static final String assertion28    = testCaseId + "-28";
    private static final String assertionMsg28 = " repository supports creation of instances ";
    private static final String assertion29    = testCaseId + "-29";
    private static final String assertionMsg29 = " repository supports undo of operations ";
    private static final String assertion30    = testCaseId + "-30";
    private static final String assertionMsg30 = " repository supports soft delete ";
    private static final String assertion31    = testCaseId + "-31";
    private static final String assertionMsg31 = " repository supports historic retrieval ";
    private static final String assertion32    = testCaseId + "-32";
    private static final String assertionMsg32 = " entity versions NOT returned before create time ";
    private static final String assertion33    = testCaseId + "-33";
    private static final String assertionMsg33 = " entity versions returned after create ";
    private static final String assertion34    = testCaseId + "-34";
    private static final String assertionMsg34 = " entity versions returned after update ";
    private static final String assertion35    = testCaseId + "-35";
    private static final String assertionMsg35 = " entity version is returned while deleted ";
    private static final String assertion36    = testCaseId + "-36";
    private static final String assertionMsg36 = " entity versions returned after restore ";
    private static final String assertion37    = testCaseId + "-37";
    private static final String assertionMsg37 = " entity versions returned after update  ";
    private static final String assertion38    = testCaseId + "-38";
    private static final String assertionMsg38 = " entity versions NOT returned after purge ";
    private static final String assertion39    = testCaseId + "-39";
    private static final String assertionMsg39 = " entity is not known after purge ";
    private static final String assertion40    = testCaseId + "-40";
    private static final String assertionMsg40 = " entity versions are ordered BACKWARDS as requested ";
    private static final String assertion41    = testCaseId + "-41";
    private static final String assertionMsg41 = " entity versions are ordered FORWARDS as requested ";
    private static final String assertion43    = testCaseId + "-43";
    private static final String assertionMsg43 = " entity is known when deleted ";


    private final String              metadataCollectionId;
    private final EntityDef           entityDef;
    private final String              testTypeName;

    private final List<EntityDetail>  createdEntities = new ArrayList<>();



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntityLifecycle(RepositoryConformanceWorkPad workPad,
                                        EntityDef                    entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
              RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
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
         * in a try...catch to check for FunctionNotSupportedException. If the connector throws this, then give up
         * on the test by setting the discovered property to disabled and returning.
         */

        EntityDetail newEntity;
        Date         beforeEntityCreateTime = new Date();

        InstanceProperties instProps = null;

        long elapsedTime;
        long start;
        try
        {
            /*
             * Generate property values for all the type's defined properties, including inherited properties
             * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
             * thereby getting into the connector-logic beyond the property validation. It also creates an
             * entity that is logically complete - versus an instance with just the locally-defined properties.
             */
            start = System.currentTimeMillis();

            instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

            newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                     entityDef.getGUID(),
                                                     super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef),
                                                     null,
                                                     null);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion28,
                            testTypeName + assertionMsg28,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "addEntity",
                            elapsedTime);

            // Record the created instance for later clean up.
            createdEntities.add(newEntity);
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

            super.addNotSupportedAssertion(assertion28,
                                           assertionMsg28,
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
            String operationDescription = "add an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialClassifications", "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        assertCondition((newEntity != null),
                        assertion1,
                        testTypeName + assertionMsg1,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        verifyCondition(workPad.getLocalServerUserId().equals(newEntity.getCreatedBy()),
                        assertion2,
                        testTypeName + assertionMsg2,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        verifyCondition((newEntity.getCreateTime() != null),
                        assertion3,
                        testTypeName + assertionMsg3,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        verifyCondition((newEntity.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT),
                        assertion4,
                        testTypeName + assertionMsg4,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        verifyCondition((newEntity.getStatus() == entityDef.getInitialStatus()),
                        assertion5,
                        testTypeName + assertionMsg5,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        InstanceType instanceType = newEntity.getType();

        if (instanceType != null)
        {
            verifyCondition(((instanceType.getTypeDefGUID().equals(entityDef.getGUID())) &&
                                    (instanceType.getTypeDefName().equals(testTypeName))),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        }
        else
        {
            verifyCondition(false,
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        }

        /*
         * The metadata collection should be set up and consistent
         */
        verifyCondition(((newEntity.getMetadataCollectionId() != null) && newEntity.getMetadataCollectionId().equals(this.metadataCollectionId)),
                        assertion7,
                        testTypeName + assertionMsg7,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        /*
         * The version should be set up and greater than zero
         */
        verifyCondition((newEntity.getVersion() > 0),
                        assertion8,
                        testTypeName + assertionMsg8,
                        RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                        RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

        String retrievalOperationName = "getEntityDetailHistory";

        /*===============
         * Validate that the entity version can not be retrieved for a time before the entity is created
         */
        start = System.currentTimeMillis();
        try
        {
            metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                      newEntity.getGUID(),
                                                      null,
                                                      beforeEntityCreateTime,
                                                      0,
                                                      workPad.getMaxPageSize(),
                                                      HistorySequencingOrder.FORWARDS);
            elapsedTime = System.currentTimeMillis() - start;

            /*
             * This is a fail because EntityNotKnownException should be thrown.
             */
            verifyCondition(false,
                            assertion32,
                            testTypeName + assertionMsg32,
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                            retrievalOperationName,
                            elapsedTime);
        }
        catch (EntityNotKnownException exception)
        {
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(true,
                            assertion32,
                            testTypeName + assertionMsg32,
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                            retrievalOperationName,
                            elapsedTime);
        }
        catch (FunctionNotSupportedException exception)
        {
            /*
             * If running against a repository/connector that does not support history catch FunctionNotSupportedException and give up the test.
             * Report the inability to create instances and give up on the testcase....
             */
            super.addNotSupportedAssertion(assertion32,
                                           assertionMsg32,
                                           RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                           RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId());

            return;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */
            String operationDescription = " retrieve version history before creation of an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, retrievalOperationName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }


        /*================
         * Validate that the entity can be consistently retrieved.
         */
        try
        {
            start = System.currentTimeMillis();
            EntityDetail knownEntity = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            retrievalOperationName = "isEntityKnown";
            verifyObjectsAreEqual(newEntity,
                                  knownEntity,
                                  assertion9,
                                  testTypeName + assertionMsg9,
                                  RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                                  RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                                  retrievalOperationName,
                                  elapsedTime);


            start = System.currentTimeMillis();
            EntitySummary entitySummary = metadataCollection.getEntitySummary(workPad.getLocalServerUserId(), newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            retrievalOperationName = "getEntitySummary";
            verifyCondition((entitySummary != null),
                            assertion10,
                            testTypeName + assertionMsg10,
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                            retrievalOperationName,
                            elapsedTime);

            start = System.currentTimeMillis();
            EntityDetail entityDetail = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            retrievalOperationName = "getEntityDetail";
            verifyObjectsAreEqual(newEntity,
                                  entityDetail,
                                  assertion11,
                                  testTypeName + assertionMsg11,
                                  RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                                  RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                                  retrievalOperationName,
                                  elapsedTime);
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */
            String operationDescription = "retrieve an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, retrievalOperationName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }


        /*===============
         * Validate that the entity version can be retrieved after the entity is created
         */
        start = System.currentTimeMillis();
        try
        {
            List<EntityDetail> entityDetails = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                                                         newEntity.getGUID(),
                                                                                         null,
                                                                                         null,
                                                                                         0,
                                                                                         workPad.getMaxPageSize(),
                                                                                         HistorySequencingOrder.FORWARDS);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition(entityDetails != null,
                            assertion33,
                            testTypeName + assertionMsg33,
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                            retrievalOperationName,
                            elapsedTime);

            assertCondition(entityDetails.size() == 1,
                            assertion33,
                            testTypeName + assertionMsg33,
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                            retrievalOperationName,
                            elapsedTime);

            verifyObjectsAreEqual(newEntity,
                                  entityDetails.get(0),
                                  assertion33,
                                  testTypeName + assertionMsg33,
                                  RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                  RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                  retrievalOperationName,
                                  elapsedTime);
        }
        catch (EntityNotKnownException exception)
        {
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition(false,
                            assertion33,
                            testTypeName + assertionMsg33,
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                            retrievalOperationName,
                            elapsedTime);
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Keep going
             */
        }
        catch (FunctionNotSupportedException exception)
        {
            /*
             * If running against a repository/connector that does not support history catch FunctionNotSupportedException and give up the test.
             * Report the inability to create instances and give up on the testcase....
             */
            super.addNotSupportedAssertion(assertion33,
                                           assertionMsg33,
                                           RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                           RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId());

            return;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */
            String operationDescription = " retrieve version history of an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, retrievalOperationName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        /*=============
         * No relationships have been created so none should be returned for this entity.
         */
        try
        {
            start = System.currentTimeMillis();
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(workPad.getLocalServerUserId(),
                                                                                            newEntity.getGUID(),
                                                                                            null,
                                                                                            0,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            0);
            elapsedTime = System.currentTimeMillis() - start;
            verifyCondition((relationships == null),
                            assertion12,
                            testTypeName + assertionMsg12,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "getRelationshipsForEntity-negative",
                            elapsedTime);
        }
        catch (Exception exc)
        {
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




        /*============
         * Modify the entity such that it has the minimum set of properties possible. If any properties are defined as
         * mandatory (based on their cardinality) then provide them - in order to exercise the connector more fully.
         * All optional properties are removed.
         */
        long nextVersion = newEntity.getVersion();

        if ((newEntity.getProperties() != null) &&
                (newEntity.getProperties().getInstanceProperties() != null) &&
                (!newEntity.getProperties().getInstanceProperties().isEmpty()))
        {
            InstanceProperties minEntityProps = super.getMinPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);
            EntityDetail minPropertiesEntity;

            try
            {
                start = System.currentTimeMillis();
                minPropertiesEntity = metadataCollection.updateEntityProperties(workPad.getLocalServerUserId(),
                                                                                newEntity.getGUID(),
                                                                                minEntityProps);
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "updateEntityProperties";
                String operationDescription = "update the properties of an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", newEntity.getGUID());
                parameters.put("properties", minEntityProps.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            /*
             * Check that the returned entity has the desired properties.
             * Even when there are no properties the minEntityProps will be a (non-null) InstanceProperties - containing
             * a property map, which may be empty.
             * The returned EntityDetail may contain a null if there are no properties (i.e. no InstanceProperties object), but
             * also tolerate an InstanceProperties with no map or an empty map.
             */

            verifyCondition(((minPropertiesEntity != null) && doPropertiesMatch(minEntityProps, minPropertiesEntity.getProperties())),
                            assertion17,
                            testTypeName + assertionMsg17,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "updateEntityProperties",
                            elapsedTime);

            /*
             * Check that the returned entity has the new version number...
             */
            verifyCondition(((minPropertiesEntity != null) && (minPropertiesEntity.getVersion() >= nextVersion)),
                            assertion18,
                            testTypeName + assertionMsg18 + nextVersion,
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

            /*
             * Verify that entity versions can be retrieved following an update
             */
            try
            {
                start = System.currentTimeMillis();
                List<EntityDetail> entityDetailHistory = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                                                                   newEntity.getGUID(),
                                                                                                   null,
                                                                                                   null,
                                                                                                   0,
                                                                                                   workPad.getMaxPageSize(),
                                                                                                   HistorySequencingOrder.BACKWARDS);
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition(entityDetailHistory != null,
                                assertion34,
                                testTypeName + assertionMsg34,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyCondition(entityDetailHistory.size() == nextVersion,
                                assertion34,
                                testTypeName + assertionMsg34,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyObjectsAreEqual(entityDetailHistory.get(0),
                                      minPropertiesEntity,
                                      assertion34,
                                      testTypeName + assertionMsg34,
                                      RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                      RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                      "getEntityDetailHistory",
                                      elapsedTime);

                start = System.currentTimeMillis();
                entityDetailHistory = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                                                newEntity.getGUID(),
                                                                                null,
                                                                                null,
                                                                                0,
                                                                                workPad.getMaxPageSize(),
                                                                                HistorySequencingOrder.FORWARDS);
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition(entityDetailHistory != null,
                                assertion36,
                                testTypeName + assertionMsg36,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyCondition(entityDetailHistory.size() == nextVersion,
                                assertion36,
                                testTypeName + assertionMsg36,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyObjectsAreEqual(entityDetailHistory.get(0),
                                      newEntity,
                                      assertion41,
                                      testTypeName + assertionMsg41,
                                      RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                      RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                      "getEntityDetailHistory",
                                      elapsedTime);
            }
            catch (AssertionFailureException exception)
            {
                /*
                 * Keep going
                 */
            }
            catch (FunctionNotSupportedException exception)
            {
                super.addNotSupportedAssertion(assertion34,
                                               assertionMsg34,
                                               RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                               RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId());

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */
                String methodName = "getEntityDetailHistory";
                String operationDescription = "retrieve an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", newEntity.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            /*============
             * Test the ability (or not) to undo the changes just made
             */
            nextVersion = minPropertiesEntity.getVersion() + 1;
            EntityDetail undoneEntity;

            try
            {
                start = System.currentTimeMillis();
                undoneEntity = metadataCollection.undoEntityUpdate(workPad.getLocalServerUserId(), newEntity.getGUID());
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition(true,
                                assertion29,
                                testTypeName + assertionMsg29,
                                RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                                RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId(),
                                "undoEntityUpdate",
                                elapsedTime);

                assertCondition(((undoneEntity != null) && (undoneEntity.getProperties() != null)),
                                assertion19,
                                testTypeName + assertionMsg19,
                                RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                                RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());

                assertCondition(((undoneEntity != null) && (undoneEntity.getVersion() >= nextVersion)),
                                assertion20,
                                testTypeName + assertionMsg20 + nextVersion,
                                RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_UNDO.getProfileId(),
                                RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_UNDO.getRequirementId());


                /*
                 * Verify that entity versions can be retrieved following undo
                 */
                try
                {
                    start = System.currentTimeMillis();
                    List<EntityDetail> entityDetailHistory = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                                                                       newEntity.getGUID(),
                                                                                                       null,
                                                                                                       null,
                                                                                                       0,
                                                                                                       workPad.getMaxPageSize(),
                                                                                                       HistorySequencingOrder.BACKWARDS);
                    elapsedTime = System.currentTimeMillis() - start;
                    assertCondition(entityDetailHistory != null,
                                    assertion37,
                                    testTypeName + assertionMsg37,
                                    RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                    RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                    "getEntityDetailHistory",
                                    elapsedTime);

                    verifyCondition(entityDetailHistory.size() == nextVersion,
                                    assertion37,
                                    testTypeName + assertionMsg37,
                                    RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                    RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                    "getEntityDetailHistory",
                                    elapsedTime);

                    verifyObjectsAreEqual(entityDetailHistory.get(0),
                                          undoneEntity,
                                          assertion40,
                                          testTypeName + assertionMsg40,
                                          RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                          RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                          "getEntityDetailHistory",
                                          elapsedTime);

                    start = System.currentTimeMillis();
                    entityDetailHistory = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                                                    newEntity.getGUID(),
                                                                                    null,
                                                                                    null,
                                                                                    0,
                                                                                    workPad.getMaxPageSize(),
                                                                                    HistorySequencingOrder.FORWARDS);
                    elapsedTime = System.currentTimeMillis() - start;
                    assertCondition(entityDetailHistory != null,
                                    assertion37,
                                    testTypeName + assertionMsg37,
                                    RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                    RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                    "getEntityDetailHistory",
                                    elapsedTime);

                    verifyCondition(entityDetailHistory.size() == nextVersion,
                                    assertion37,
                                    testTypeName + assertionMsg37,
                                    RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                    RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                    "getEntityDetailHistory",
                                    elapsedTime);

                    verifyObjectsAreEqual(entityDetailHistory.get(0),
                                          newEntity,
                                          assertion41,
                                          testTypeName + assertionMsg41,
                                          RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                          RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                          "getEntityDetailHistory",
                                          elapsedTime);
                }
                catch (AssertionFailureException exception)
                {
                    throw exception;
                }
                catch (FunctionNotSupportedException exception)
                {
                    super.addNotSupportedAssertion(assertion34,
                                                   assertionMsg34,
                                                   RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                                   RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId());

                }
                catch (Exception exc)
                {
                    /*
                     * We are not expecting any other exceptions from this method call. Log and fail the test.
                     */
                    String methodName = "getEntityDetailHistory";
                    String operationDescription = "retrieve an entity of type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("entityGUID", newEntity.getGUID());
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                    throw new Exception(msg, exc);
                }

                nextVersion = undoneEntity.getVersion() + 1;
            }
            catch (FunctionNotSupportedException exception)
            {
                super.addNotSupportedAssertion(assertion29,
                                               assertionMsg29,
                                               RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                                               RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "undoEntityUpdate";
                String operationDescription = "undo the update of an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", newEntity.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }
        }


        /*
         * Catch the current time for a later historic query test, then sleep for a second, so we are sure that time has moved on
         */
        Date preDeleteDate = new Date();


        /*=========================================================
         * Test that the entity can be soft-deleted, that the soft-deleted entity has a higher version.
         * Verify that the soft deleted entity cannot be retrieved, but can be restored and that the restored entity has
         * a valid version (higher than when it was deleted).
         * Check that the restored entity can be retrieved.
         */

        EntityDetail deletedEntity;

        try
        {
            start = System.currentTimeMillis();
            deletedEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                            newEntity.getType().getTypeDefGUID(),
                                                            newEntity.getType().getTypeDefName(),
                                                            newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition(true,
                            assertion30,
                            testTypeName + assertionMsg30,
                            RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                            RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId(),
                            "deleteEntity",
                            elapsedTime);

            assertCondition(((deletedEntity != null) && (deletedEntity.getVersion() >= nextVersion)),
                            assertion21,
                            testTypeName + assertionMsg21 + nextVersion,
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

            Date preRestoreDate = new Date();

            /*
             * Verify that an entity cannot be retrieved when deleted
             */
            try
            {
                start = System.currentTimeMillis();
                metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition((false),
                                assertion22,
                                testTypeName + assertionMsg22,
                                RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                                RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                                "getEntityDetail-negative",
                                elapsedTime);
            }
            catch (EntityNotKnownException exception)
            {
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition((true),
                                assertion22,
                                testTypeName + assertionMsg22,
                                RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                                RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                                "getEntityDetail-negative",
                                elapsedTime);
            }
            catch (Exception exc)
            {
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
             * Verify that the entity is known following delete
             */
            try
            {
                start = System.currentTimeMillis();
                EntityDetail knownEntity = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), deletedEntity.getGUID());
                elapsedTime = System.currentTimeMillis() - start;
                verifyObjectsAreEqual(deletedEntity,
                                      knownEntity,
                                      assertion43,
                                      testTypeName + assertionMsg43,
                                      RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                                      RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                                      "isEntityKnown",
                                      elapsedTime);
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */
                String methodName = "isEntityKnown";
                String operationDescription = " known a deleted entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", deletedEntity.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            /*
             * Verify that the deleted version of the entity can be retrieved.
             */
            try
            {
                start = System.currentTimeMillis();
                List<EntityDetail> entityVersions = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                                                              newEntity.getGUID(),
                                                                                              null,
                                                                                              null,
                                                                                              0,
                                                                                              workPad.getMaxPageSize(),
                                                                                              HistorySequencingOrder.BACKWARDS);
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition(entityVersions != null,
                                assertion35,
                                testTypeName + assertionMsg35,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyCondition(! entityVersions.isEmpty(),
                                assertion35,
                                testTypeName + assertionMsg35,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyCondition((entityVersions.get(0).getStatus() == InstanceStatus.DELETED),
                                assertion35,
                                testTypeName + assertionMsg35,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);
            }
            catch (AssertionFailureException exception)
            {
                /*
                 * Keep going
                 */
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "getEntityDetailHistory";
                String operationDescription = "retrieve an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", newEntity.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            /*=========================================
             * Verify that an entity can be restored
             */

            /*
             * Performing the restore should advance the version number again
             */
            nextVersion = deletedEntity.getVersion() + 1;

            EntityDetail restoredEntity;
            try
            {
                start = System.currentTimeMillis();
                restoredEntity = metadataCollection.restoreEntity(workPad.getLocalServerUserId(), newEntity.getGUID());
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */
                String methodName = "restoreEntity";
                String operationDescription = "restore a soft-deleted entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", newEntity.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            assertCondition((restoredEntity != null),
                            assertion23,
                            testTypeName + assertionMsg23,
                            RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getProfileId(),
                            RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getRequirementId(),
                            "restoreEntity",
                            elapsedTime);

            assertCondition((restoredEntity.getVersion() >= nextVersion),
                            assertion24,
                            testTypeName + assertionMsg24 + nextVersion,
                            RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_RESTORE.getProfileId(),
                            RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_RESTORE.getRequirementId());

            /*
             * Verify that entity can be retrieved following restore
             */
            try
            {
                start = System.currentTimeMillis();
                EntityDetail entityDetail = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), restoredEntity.getGUID());
                elapsedTime = System.currentTimeMillis() - start;
                verifyObjectsAreEqual(restoredEntity,
                                      entityDetail,
                                      assertion25,
                                      testTypeName + assertionMsg25,
                                      RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                                      RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                                      "getEntityDetail",
                                      elapsedTime);
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */
                String methodName = "getEntityDetail";
                String operationDescription = "retrieve an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", restoredEntity.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            /*
             * Verify that entity versions can be retrieved following restore
             */
            try
            {
                start = System.currentTimeMillis();
                List<EntityDetail> entityDetailHistory = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                                                                   restoredEntity.getGUID(),
                                                                                                   null,
                                                                                                   null,
                                                                                                   0,
                                                                                                   workPad.getMaxPageSize(),
                                                                                                   HistorySequencingOrder.BACKWARDS);
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition(entityDetailHistory != null,
                                assertion36,
                                testTypeName + assertionMsg36,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyCondition(entityDetailHistory.size() == nextVersion,
                                assertion36,
                                testTypeName + assertionMsg36,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyObjectsAreEqual(entityDetailHistory.get(0),
                                      restoredEntity,
                                      assertion40,
                                      testTypeName + assertionMsg40,
                                      RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                      RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                      "getEntityDetailHistory",
                                      elapsedTime);

                start = System.currentTimeMillis();
                entityDetailHistory = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                                                restoredEntity.getGUID(),
                                                                                null,
                                                                                null,
                                                                                0,
                                                                                workPad.getMaxPageSize(),
                                                                                HistorySequencingOrder.FORWARDS);
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition(entityDetailHistory != null,
                                assertion36,
                                testTypeName + assertionMsg36,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyCondition(entityDetailHistory.size() == nextVersion,
                                assertion36,
                                testTypeName + assertionMsg36,
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                "getEntityDetailHistory",
                                elapsedTime);

                verifyObjectsAreEqual(entityDetailHistory.get(0),
                                      newEntity,
                                      assertion41,
                                      testTypeName + assertionMsg41,
                                      RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                      RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                                      "getEntityDetailHistory",
                                      elapsedTime);
            }
            catch (AssertionFailureException exception)
            {
                /*
                 * Keep going
                 */
            }
            catch (FunctionNotSupportedException exception)
            {
                super.addNotSupportedAssertion(assertion31,
                                               assertionMsg31,
                                               RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                               RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId());

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */
                String methodName = "getEntityDetailHistory";
                String operationDescription = "retrieve an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", restoredEntity.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            /*
             * Verify that historical query for the time when it was deleted does not return the entity
             */
            try
            {
                start = System.currentTimeMillis();
                metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID(), deletedEntity.getUpdateTime());

                assertCondition(false,
                                assertion31,
                                testTypeName + assertionMsg31,
                                RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());
            }
            catch (EntityNotKnownException exception)
            {
                /*
                 * Even if it supports historical retrieval, the repository should not return the entity at this time.
                 */
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition((true),
                                assertion27,
                                testTypeName + assertionMsg27,
                                RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId(),
                                "getEntityDetail-when deleted",
                                elapsedTime);

            }
            catch (AssertionFailureException exception)
            {
                /*
                 * Keep going
                 */
            }
            catch (FunctionNotSupportedException exception)
            {
                super.addNotSupportedAssertion(assertion31,
                                               assertionMsg31,
                                               RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */
                String methodName = "getEntityDetail";
                String operationDescription = "retrieve a historical copy of a purged entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID"   , newEntity.getGUID());
                parameters.put("asOfTime"     , deletedEntity.getUpdateTime().toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            /*=====================================
             * Now soft-delete the entity - this time for real
             */
            try
            {
                deletedEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                newEntity.getType().getTypeDefGUID(),
                                                                newEntity.getType().getTypeDefName(),
                                                                newEntity.getGUID());
            }
            catch (FunctionNotSupportedException exception)
            {
                super.addNotSupportedAssertion(assertion30,
                                               assertionMsg30,
                                               RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                               RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());

                /* OK to continue - soft delete is optional */

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */
                final String methodName = "deleteEntity";
                String operationDescription = "delete an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", newEntity.getType().getTypeDefGUID());
                parameters.put("typeDefName", newEntity.getType().getTypeDefName());
                parameters.put("obsoleteEntityGUID", newEntity.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Keep going.
             */
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion30,
                                           assertionMsg30,
                                           RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());
        }
        catch (Exception exc)
        {
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

        /*=======================================
         * Test that an entity can be purged.
         */
        try
        {
            start = System.currentTimeMillis();
            metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                           newEntity.getType().getTypeDefGUID(),
                                           newEntity.getType().getTypeDefName(),
                                           newEntity.getGUID());
            elapsedTime = System.currentTimeMillis() - start;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */
            String methodName = "purgeEntity";
            String operationDescription = "purge an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", newEntity.getType().getTypeDefGUID());
            parameters.put("typeDefName", newEntity.getType().getTypeDefName());
            parameters.put("deletedEntityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        /*
         * Check an entity can not be retrieved after purge
         */
        try
        {
            metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());

            assertCondition((false),
                            assertion26,
                            testTypeName + assertionMsg26,
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                            "getEntityDetail",
                            elapsedTime);
        }
        catch (EntityNotKnownException exception)
        {
            assertCondition((true),
                            assertion26,
                            testTypeName + assertionMsg26,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "getEntityDetail",
                            null);
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Keep going.
             */
        }
        catch (Exception exc)
        {
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
         * Check that an entity is no longer known after the purge.
         */
        try
        {
            EntityDetail entityDetail = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID());

            assertCondition((entityDetail == null),
                            assertion39,
                            testTypeName + assertionMsg39,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "isEntityKnown",
                            null);
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Keep going.
             */
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "isEntityKnown";
            String operationDescription = "retrieve an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        /*
         * Perform a historic get of the entity - this should not return the entity since it has now been [deleted and] purged
         * The time for the query is the time set just before the delete operation above.
         */
        try
        {
            start = System.currentTimeMillis();
            metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID(), preDeleteDate);

            assertCondition(false,
                            assertion31,
                            testTypeName + assertionMsg31,
                            RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());
        }
        catch (EntityNotKnownException exception)
        {
            /*
             * Even if it supports historical retrieval, the repository should not return any version of a purged entity,
             * as the entity and all of its history should have been purged. Therefore, this exception being thrown
             * indicates success -- so we do not need to handle it any further.
             */
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                    assertion27,
                    testTypeName + assertionMsg27,
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId(),
                    "getEntityDetail-purged",
                    elapsedTime);

        }
        catch (AssertionFailureException exception)
        {
            /*
             * Keep going.
             */
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion31,
                                           assertionMsg31,
                                           RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());

        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */
            String methodName = "getEntityDetail";
            String operationDescription = "retrieve a historical copy of a purged entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID"   , newEntity.getGUID());
            parameters.put("asOfTime"     , preDeleteDate.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        /*
         * Perform a get entity history - this should not return any of the versions because the entity has now been purged
         */
        try
        {
            start = System.currentTimeMillis();
            metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                                                      newEntity.getGUID(),
                                                      null,
                                                      null,
                                                      0,
                                                      workPad.getMaxPageSize(),
                                                      HistorySequencingOrder.BACKWARDS);

            assertCondition(false,
                            assertion38,
                            testTypeName + assertionMsg38,
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId());
        }
        catch (EntityNotKnownException exception)
        {
            /*
             * Even if it supports historical retrieval, the repository should not return any version of a purged entity,
             * as the entity and all of its history should have been purged. Therefore, this exception being thrown
             * indicates success -- so we do not need to handle it any further.
             */
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion38,
                            testTypeName + assertionMsg38,
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                            RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId(),
                            "getEntityDetailHistory-purged",
                            elapsedTime);

        }
        catch (AssertionFailureException exception)
        {
            /*
             * Keep going.
             */
            throw exception;
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion38,
                                           assertionMsg38,
                                           RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getProfileId(),
                                           RepositoryConformanceProfileRequirement.GET_ENTITY_VERSIONS.getRequirementId());

        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */
            String methodName = "getEntityDetailHistory";
            String operationDescription = "retrieve a historical versions of a purged entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", newEntity.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        super.setSuccessMessage("Entities can be managed through their lifecycle");
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
             * They may have already been cleaned up so be prepared to catch everything from
             * FunctionNotSupportedException to EntityNotKnownException and maybe others.
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



    /*** Determine if properties are as expected.
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
            if ((firstInstanceProps.getInstanceProperties() == null) ||
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

                    for (String key : secondPropertiesKeySet)
                    {
                        if (! (secondPropertiesMap.get(key).equals(firstPropertiesMap.get(key))))
                        {
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
