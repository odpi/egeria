/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.ffdc.exception.AssertionFailureException;
import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Test that all defined relationships can be created, retrieved, updated and deleted.
 */
public class TestSupportedRelationshipLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-relationship-lifecycle";
    private static final String testCaseName = "Repository relationship lifecycle test case";


    private static final String assertion0    = testCaseId + "-00";
    private static final String assertionMsg0 = " relationship type definition matches known type  ";

    private static final String assertion101     = testCaseId + "-101";
    private static final String assertionMsg101  = " new entity created.";
    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " new relationship created.";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " new relationship has createdBy user.";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " new relationship has creation time.";
    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " new relationship has correct provenance type.";
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " new relationship has correct initial status.";
    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " new relationship has correct type.";
    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " new relationship has local metadata collection.";
    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " new relationship has version greater than zero.";
    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " new relationship is known.";
    private static final String assertion10    = testCaseId + "-10";
    private static final String assertionMsg10 = " new relationship retrieved.";
    private static final String assertion11    = testCaseId + "-11";
    private static final String assertionMsg11 = " relationship status updated.";
    private static final String assertion12    = testCaseId + "-12";
    private static final String assertionMsg12 = " relationship new status is ";
    private static final String assertion13    = testCaseId + "-13";
    private static final String assertionMsg13 = " relationship with new status version number is ";
    private static final String assertion14    = testCaseId + "-14";
    private static final String assertionMsg14 = " relationship can not be set to DELETED status.";
    private static final String assertion15    = testCaseId + "-15";
    private static final String assertionMsg15 = " relationship properties cleared to min.";
    private static final String assertion16    = testCaseId + "-16";
    private static final String assertionMsg16 = " relationship with min properties version number is ";
    private static final String assertion17    = testCaseId + "-17";
    private static final String assertionMsg17 = " relationship has properties restored.";
    private static final String assertion18    = testCaseId + "-18";
    private static final String assertionMsg18 = " relationship after undo version number is ";
    private static final String assertion19    = testCaseId + "-19";
    private static final String assertionMsg19 = " relationship deleted version number is ";
    private static final String assertion20    = testCaseId + "-20";
    private static final String assertionMsg20 = " relationship no longer retrievable after delete.";
    private static final String assertion21    = testCaseId + "-21";
    private static final String assertionMsg21 = " relationship restored ";
    private static final String assertion22    = testCaseId + "-22";
    private static final String assertionMsg22 = " relationship restored version number is ";
    private static final String assertion23    = testCaseId + "-23";
    private static final String assertionMsg23 = " relationship retrieved following restore ";
    private static final String assertion24    = testCaseId + "-24";
    private static final String assertionMsg24 = " relationship purged.";
    private static final String assertion25    = testCaseId + "-25";
    private static final String assertionMsg25 = " historical retrieval returned correct version relationship ";

    private static final String assertion27    = testCaseId + "-27";
    private static final String assertionMsg27 = " relationship end types are supported by repository  ";

    private static final String assertion28    = testCaseId + "-28";
    private static final String assertionMsg28 = " repository supports creation of instances ";

    private static final String assertion29    = testCaseId + "-29";
    private static final String assertionMsg29 = " repository supports undo of operations ";

    private static final String assertion30    = testCaseId + "-30";
    private static final String assertionMsg30 = " repository supports soft delete ";

    private static final String assertion31    = testCaseId + "-31";
    private static final String assertionMsg31 = " repository supports historic retrieval ";



    private final RepositoryConformanceWorkPad  workPad;
    private final String                        metadataCollectionId;
    private final Map<String, EntityDef>        entityDefs;
    private final RelationshipDef               relationshipDef;
    private final String                        testTypeName;

    private final List<EntityDetail>            createdEntities       = new ArrayList<>();
    private final List<Relationship>            createdRelationships  = new ArrayList<>();


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDefs types of valid entities
     * @param relationshipDef type of relationship to test
     */

    public TestSupportedRelationshipLifecycle(RepositoryConformanceWorkPad workPad,
                                              Map<String, EntityDef>  entityDefs,
                                              RelationshipDef         relationshipDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
              RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        this.workPad = workPad;
        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.relationshipDef = relationshipDef;
        this.entityDefs = entityDefs;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
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
         * Check that the relationship type and end entity types match the known types from the repository helper.
         *
         * The entity types used by the ends are not verified on this test - they are verified in the supported entity tests
         */

        OMRSRepositoryConnector cohortRepositoryConnector = null;
        OMRSRepositoryHelper repositoryHelper = null;
        if (workPad != null) {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
            repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        }

        RelationshipDef knownRelationshipDef = (RelationshipDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), relationshipDef.getName());
        verifyCondition((relationshipDef.equals(knownRelationshipDef)),
                        assertion0,
                        testTypeName + assertionMsg0,
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());



        /*
         * In this testcase the repository is believed to support the relationship type defined by
         * relationshipDef - but may not support all of the entity inheritance hierarchy - it may only
         * support a subset of entity types. So although the relationship type may have end definitions
         * each specifying a given entity type - the repository may only support certain sub-types of the
         * specified type. This is OK, and the testcase needs to only try to use entity types that are
         * supported by the repository being tested. To do this it needs to start with the specified
         * end type, e.g. Referenceable, and walk down the hierarchy looking for each subtype that
         * is supported by the repository (i.e. is in the entityDefs map). The test is run for
         * each combination of end1Type and end2Type - but only for types that are within the
         * active set for this repository.
         */

        String end1DefName = relationshipDef.getEndDef1().getEntityType().getName();
        List<String> end1DefTypeNames = new ArrayList<>();
        end1DefTypeNames.add(end1DefName);
        if (this.workPad.getEntitySubTypes(end1DefName) != null) {
            end1DefTypeNames.addAll(this.workPad.getEntitySubTypes(end1DefName));
        }


        String end2DefName = relationshipDef.getEndDef2().getEntityType().getName();
        List<String> end2DefTypeNames = new ArrayList<>();
        end2DefTypeNames.add(end2DefName);
        if (this.workPad.getEntitySubTypes(end2DefName) != null) {
            end2DefTypeNames.addAll(this.workPad.getEntitySubTypes(end2DefName));
        }

        /*
         * Filter the possible types to only include types that are supported by the repository
         */

        List<String> end1SupportedTypeNames = new ArrayList<>();
        for (String end1TypeName : end1DefTypeNames) {
            if (entityDefs.get(end1TypeName) != null)
                end1SupportedTypeNames.add(end1TypeName);
        }

        List<String> end2SupportedTypeNames = new ArrayList<>();
        for (String end2TypeName : end2DefTypeNames) {
            if (entityDefs.get(end2TypeName) != null)
                end2SupportedTypeNames.add(end2TypeName);
        }

        /*
         * Check that neither list is empty
         */
        if (end1SupportedTypeNames.isEmpty() || end2SupportedTypeNames.isEmpty()) {

            /*
             * There are no supported types for at least one of the ends - the repository cannot test this relationship type.
             */
            assertCondition((false),
                            assertion27,
                            testTypeName + assertionMsg27,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }

        /*
         * It is not practical to iterate over all combinations of feasible (supported) end types as it takes too long to run.
         * For now, this test verifies relationship operation over a limited set of end types. The limitation is extreme in
         * that it ONLY takes the first available type for each end. This is undesirable for two reasons - one is that it
         * provides less test coverage; the other is that the types chosen depend on the order in the lists and this could
         * vary, making results non-repeatable. For now though, it seems these limitations are necessary.
         *
         * A full permutation across end types would use the following nested loops...
         *  for (String end1TypeName : end1SupportedTypeNames) {
         *     for (String end2TypeName : end2SupportedTypeNames) {
         *          test logic as below...
         *      }
         *  }
         */


        String end1TypeName = end1SupportedTypeNames.get(0);
        String end2TypeName = end2SupportedTypeNames.get(0);


        /*
         * To accommodate repositories that do not support the creation of instances, wrap the creation of the entity
         * in a try..catch to check for FunctionNotSupportedException. If the connector throws this, then give up
         * on the test by setting the discovered property to disabled and returning.
         */

        EntityDetail end1 = null;
        EntityDetail end2 = null;
        EntityDef entityType = null;
        InstanceProperties entityInstanceProperties = null;

        try {

            entityType = entityDefs.get(end1TypeName);
            entityInstanceProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityType);
            end1 = metadataCollection.addEntity(workPad.getLocalServerUserId(), entityType.getGUID(), entityInstanceProperties, null, null);
            createdEntities.add(end1);

            entityType = entityDefs.get(end2TypeName);
            entityInstanceProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityType);
            end2 = metadataCollection.addEntity(workPad.getLocalServerUserId(), entityType.getGUID(), entityInstanceProperties, null, null);
            createdEntities.add(end2);

        } catch (FunctionNotSupportedException exception) {

            /*
             * The repository does not support creation of entity instances; we need to report and fail the test
             *
             */

            super.addNotSupportedAssertion(assertion101,
                                           assertionMsg101,
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            return;

        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "addEntity";
            String operationDescription = "add an entity of type " + (entityType != null ? entityType.getName() : "null");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityType != null ? entityType.getGUID() : "null");
            parameters.put("initialProperties", entityInstanceProperties != null ? entityInstanceProperties.toString() : "null");
            parameters.put("initialClasiifications", "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        Relationship newRelationship = null;
        InstanceProperties relationshipInstanceProperties = null;

        long start;
        long elapsedTime;
        try {

            start = System.currentTimeMillis();
            newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                 relationshipDef.getGUID(),
                                                                 super.getPropertiesForInstance(relationshipDef.getPropertiesDefinition()),
                                                                 end1.getGUID(),
                                                                 end2.getGUID(),
                                                                 null);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion28,
                            testTypeName + assertionMsg28,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                            "addRelationship",
                            elapsedTime);

            createdRelationships.add(newRelationship);


        } catch (AssertionFailureException exception) {

            throw exception;

        } catch (FunctionNotSupportedException exception) {

            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * Report the inability to create instances and give up on the testcase....
             */

            super.addNotSupportedAssertion(assertion28,
                                           assertionMsg28,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

            return;


        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "addRelationship";
            String operationDescription = "add a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            parameters.put("end1 entityGUID", end1.getGUID());
            parameters.put("end2 entityGUID", end2.getGUID());
            parameters.put("initialProperties", relationshipInstanceProperties != null ? relationshipInstanceProperties.toString() : "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        assertCondition((newRelationship != null),
                        assertion1,
                        testTypeName + assertionMsg1,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());


        verifyCondition(workPad.getLocalServerUserId().equals(newRelationship.getCreatedBy()),
                        assertion2,
                        testTypeName + assertionMsg2,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        verifyCondition((newRelationship.getCreateTime() != null),
                        assertion3,
                        testTypeName + assertionMsg3,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        verifyCondition((newRelationship.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT),
                        assertion4,
                        testTypeName + assertionMsg4,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        verifyCondition((newRelationship.getStatus() == relationshipDef.getInitialStatus()),
                        assertion5,
                        testTypeName + assertionMsg5,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        InstanceType instanceType = newRelationship.getType();

        if (instanceType != null) {
            verifyCondition(((instanceType.getTypeDefGUID().equals(relationshipDef.getGUID())) &&
                                    (instanceType.getTypeDefName().equals(testTypeName))),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        } else {
            verifyCondition(false,
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }

        /*
         * The metadata collection should be set up and consistent
         */
        verifyCondition(((newRelationship.getMetadataCollectionId() != null) && newRelationship.getMetadataCollectionId().equals(this.metadataCollectionId)),
                        assertion7,
                        testTypeName + assertionMsg7,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        /*
         * The version should be set up and greater than zero
         */
        verifyCondition((newRelationship.getVersion() > 0),
                        assertion8,
                        testTypeName + assertionMsg8,
                        RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                        RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

        /*
         * Validate that the relationship can be consistently retrieved.
         */

        String retrievalOperationName = "";

        try {

            start = System.currentTimeMillis();
            Relationship knownRelationship = metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(), newRelationship.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            retrievalOperationName = "isRelationshipKnown";
            verifyCondition((newRelationship.equals(knownRelationship)),
                            assertion9,
                            testTypeName + assertionMsg9,
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                            "isRelationshipKnown",
                            elapsedTime);

            start = System.currentTimeMillis();
            Relationship retrievedRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            retrievalOperationName = "getRelationship";
            verifyCondition((newRelationship.equals(retrievedRelationship)),
                            assertion10,
                            testTypeName + assertionMsg10,
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                            RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId(),
                            "getRelationship",
                            elapsedTime);

        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String operationDescription = "retrieve a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("relationshipGUID", newRelationship.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, retrievalOperationName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        /*
         * Update relationship status
         */
        long nextVersion = newRelationship.getVersion() + 1;

        for (InstanceStatus validInstanceStatus : relationshipDef.getValidInstanceStatusList()) {
            if (validInstanceStatus != InstanceStatus.DELETED) {

                Relationship updatedRelationship = null;

                try {

                    start = System.currentTimeMillis();
                    updatedRelationship = metadataCollection.updateRelationshipStatus(workPad.getLocalServerUserId(), newRelationship.getGUID(), validInstanceStatus);
                    elapsedTime = System.currentTimeMillis() - start;

                } catch (Exception exc) {
                    /*
                     * We are not expecting any exceptions from this method call. Log and fail the test.
                     */

                    String methodName = "updateRelationshipStatus";
                    String operationDescription = "update the status of a relationship of type " + relationshipDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("relationshipGUID", newRelationship.getGUID());
                    parameters.put("newStatus", validInstanceStatus.toString());
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                    throw new Exception(msg, exc);

                }


                assertCondition((updatedRelationship != null),
                                assertion11,
                                testTypeName + assertionMsg11,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                                "updateRelationshipStatus",
                                elapsedTime);

                assertCondition((updatedRelationship.getStatus() == validInstanceStatus),
                                assertion12,
                                testTypeName + assertionMsg12 + validInstanceStatus.getName(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                assertCondition((updatedRelationship.getVersion() >= nextVersion),
                                assertion13,
                                testTypeName + assertionMsg13 + nextVersion,
                                RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                                RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

                nextVersion = updatedRelationship.getVersion() + 1;


            }
        }

        try {

            start = System.currentTimeMillis();
            metadataCollection.updateRelationshipStatus(workPad.getLocalServerUserId(), newRelationship.getGUID(), InstanceStatus.DELETED);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((false),
                            assertion14,
                            testTypeName + assertionMsg14,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                            "updateRelationshipStatus-negative",
                            elapsedTime);

        } catch (StatusNotSupportedException exception) {

            elapsedTime = System.currentTimeMillis() - start;
            verifyCondition((true),
                            assertion14,
                            testTypeName + assertionMsg14,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                            "updateRelationshipStatus-negative",
                            elapsedTime);

        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "updateRelationshipStatus";
            String operationDescription = "update the status of a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("relationshipGUID", newRelationship.getGUID());
            parameters.put("newStatus", InstanceStatus.DELETED.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }



        /*
         * Modify the relationship such that it has the minimum set of properties possible. If any properties are defined as
         * mandatory (based on their cardinality) then provide them - in order to exercise the connector more fully.
         * All optional properties are removed.
         */

        if ((newRelationship.getProperties() != null) &&
                (newRelationship.getProperties().getInstanceProperties() != null) &&
                (!newRelationship.getProperties().getInstanceProperties().isEmpty())) {

            InstanceProperties minRelationshipProps = super.getMinPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef);
            Relationship minPropertiesRelationship = null;

            try {
                start = System.currentTimeMillis();
                minPropertiesRelationship = metadataCollection.updateRelationshipProperties(workPad.getLocalServerUserId(),
                                                                                            newRelationship.getGUID(),
                                                                                            minRelationshipProps);
                elapsedTime = System.currentTimeMillis() - start;
            } catch (Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "updateRelationshipProperties";
                String operationDescription = "update the properties of a relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("relationshipGUID", newRelationship.getGUID());
                parameters.put("properties", minRelationshipProps.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }


            /*
             * Check that the returned relationship has the desired properties.
             * Even when there are no properties the minRelationshipProps will be a (non-null) InstanceProperties - containing
             * a property map, which may be empty.
             * The returned Relationship may contain a null if there are no properties (i.e. no InstanceProperties object), but
             * also tolerate an InstanceProperties with no map or an empty map.
             */

            verifyCondition(((minPropertiesRelationship != null) && doPropertiesMatch(minRelationshipProps, minPropertiesRelationship.getProperties())),
                            assertion15,
                            testTypeName + assertionMsg15,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                            "updateRelationshipProperties",
                            elapsedTime);

            /*
             * Check that the returned relationship has the new version number...
             */
            verifyCondition(((minPropertiesRelationship != null) && (minPropertiesRelationship.getVersion() >= nextVersion)),
                            assertion16,
                            testTypeName + assertionMsg16 + nextVersion,
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

            nextVersion = minPropertiesRelationship.getVersion() + 1;

            /*
             * Test the ability (or not) to undo the changes just made
             */

            Relationship undoneRelationship = null;

            try {

                start = System.currentTimeMillis();
                undoneRelationship = metadataCollection.undoRelationshipUpdate(workPad.getLocalServerUserId(), newRelationship.getGUID());
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition(true,
                                assertion29,
                                testTypeName + assertionMsg29,
                                RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                                RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId(),
                                "undoRelationshipUpdate",
                                elapsedTime);

                verifyCondition(((undoneRelationship != null) && (undoneRelationship.getProperties() != null)),
                                assertion17,
                                testTypeName + assertionMsg17,
                                RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                                RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());

                verifyCondition(((undoneRelationship != null) && (undoneRelationship.getVersion() >= nextVersion)),
                                assertion18,
                                testTypeName + assertionMsg18 + nextVersion,
                                RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_UNDO.getProfileId(),
                                RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_UNDO.getRequirementId());

                nextVersion = undoneRelationship.getVersion() + 1;


            } catch (AssertionFailureException exception) {

                throw exception;

            } catch (FunctionNotSupportedException exception) {

                super.addNotSupportedAssertion(assertion29,
                                               assertionMsg29,
                                               RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                                               RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());

            } catch (Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "undoRelationshipUpdate";
                String operationDescription = "undo the update of a relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", newRelationship.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }

        }



        /*
         * Catch the current time for a later historic query test, then sleep for a second so we are sure that time has moved on
         */
        Date preDeleteDate = new Date();

        Relationship preDeleteRelationship = null;

        try {

            preDeleteRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "getRelationship";
            String operationDescription = "retrieve a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("relationshipGUID", newRelationship.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        /*
         * Test that the relationship can be soft-deleted, and that the soft deleted relationship has a higher version.
         * Verify that the soft deleted relationship cannot be retrieved, but can be restored and that the restored relationship has
         * a valid version (higher than when it was deleted).
         * Check that the restored relationship can be retrieved.
         */

        Relationship deletedRelationship = null;

        try {

            start = System.currentTimeMillis();
            deletedRelationship = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                                        newRelationship.getType().getTypeDefGUID(),
                                                                        newRelationship.getType().getTypeDefName(),
                                                                        newRelationship.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition(true,
                            assertion30,
                            testTypeName + assertionMsg30,
                            RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                            RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId(),
                            "deleteRelationship",
                            elapsedTime);

            verifyCondition(((deletedRelationship != null) && (deletedRelationship.getVersion() >= nextVersion)),
                            assertion19,
                            testTypeName + assertionMsg19 + nextVersion,
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

            nextVersion = deletedRelationship.getVersion() + 1;

            try {

                start = System.currentTimeMillis();
                metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());
                elapsedTime = System.currentTimeMillis() - start;

                verifyCondition((false),
                                assertion20,
                                testTypeName + assertionMsg20,
                                RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId(),
                                "getRelationship-negative",
                                elapsedTime);

            } catch (RelationshipNotKnownException exception) {

                elapsedTime = System.currentTimeMillis() - start;
                verifyCondition((true),
                                assertion20,
                                testTypeName + assertionMsg20,
                                RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId(),
                                "getRelationship-negative",
                                elapsedTime);

            } catch (Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "getRelationship";
                String operationDescription = "retrieve a relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("relationshipGUID", newRelationship.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }

            /*
             * Performing the restore should advance the version number again
             */
            nextVersion = deletedRelationship.getVersion() + 1;

            Relationship restoredRelationship = null;

            try {

                start = System.currentTimeMillis();
                restoredRelationship = metadataCollection.restoreRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());
                elapsedTime = System.currentTimeMillis() - start;

            } catch (Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "restoreRelationship";
                String operationDescription = "restore a soft-deleted relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("relationshipGUID", newRelationship.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }

            verifyCondition((restoredRelationship != null),
                            assertion21,
                            testTypeName + assertionMsg21,
                            RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getProfileId(),
                            RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getRequirementId(),
                            "restoreRelationship",
                            elapsedTime);

            verifyCondition((restoredRelationship.getVersion() >= nextVersion),
                            assertion22,
                            testTypeName + assertionMsg22 + nextVersion,
                            RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_RESTORE.getProfileId(),
                            RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_RESTORE.getRequirementId());


            /*
             * Verify that relationship can be retrieved following restore
             */

            try {

                start = System.currentTimeMillis();
                Relationship knownRelationship = metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(), restoredRelationship.getGUID());
                elapsedTime = System.currentTimeMillis() - start;
                verifyCondition((restoredRelationship.equals(knownRelationship)),
                                assertion23,
                                testTypeName + assertionMsg23,
                                RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getRequirementId(),
                                "isRelationshipKnown",
                                elapsedTime);

            }
            catch (Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "isRelationshipKnown";
                String operationDescription = "retrieve a relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("relationshipGUID", restoredRelationship.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }


        } catch (FunctionNotSupportedException exception) {

            super.addNotSupportedAssertion(assertion30,
                                           assertionMsg30,
                                           RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());

        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "deleteRelationship";
            String operationDescription = "delete a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", newRelationship.getType().getTypeDefGUID());
            parameters.put("typeDefName", newRelationship.getType().getTypeDefName());
            parameters.put("obsoleteRelationshipGUID", newRelationship.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }






        /*
         * Now get rid of the relationship - this time for real
         */

        try {
            metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                  newRelationship.getType().getTypeDefGUID(),
                                                  newRelationship.getType().getTypeDefName(),
                                                  newRelationship.getGUID());
        }
        catch (FunctionNotSupportedException exception) {

            super.addNotSupportedAssertion(assertion30,
                                           assertionMsg30,
                                           RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());

        } catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "deleteRelationship";
            String operationDescription = "delete a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", newRelationship.getType().getTypeDefGUID());
            parameters.put("typeDefName", newRelationship.getType().getTypeDefName());
            parameters.put("obsoleteRelationshipGUID", newRelationship.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        try {

            start = System.currentTimeMillis();
            metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                                                 newRelationship.getType().getTypeDefGUID(),
                                                 newRelationship.getType().getTypeDefName(),
                                                 newRelationship.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

        }
        catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "purgeRelationship";
            String operationDescription = "purge a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", newRelationship.getType().getTypeDefGUID());
            parameters.put("typeDefName", newRelationship.getType().getTypeDefName());
            parameters.put("deletedRelationshipGUID", newRelationship.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        try {

            metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

            verifyCondition((false),
                            assertion24,
                            testTypeName + assertionMsg24,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                            "purgeRelationship",
                            elapsedTime);

        } catch (RelationshipNotKnownException exception) {

            verifyCondition((true),
                            assertion24,
                            testTypeName + assertionMsg24,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                            "purgeRelationship",
                            elapsedTime);
        }
        catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "getRelationship";
            String operationDescription = "retrieve a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("relationshipGUID", newRelationship.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        /*
         * Perform a historic get of the relationship - this should not return the relationship since it has now been [deleted and] purged
         * The time for the query is the time set just before the delete operation above.
         */
        try {
            start = System.currentTimeMillis();
            Relationship earlierRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID(), preDeleteDate);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition(true,
                            assertion31,
                            testTypeName + assertionMsg31,
                            RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());


            /*
             * Check that the earlierRelationship is null (really it should be the RelationshipNotKnownException below that handles)
             */
            assertCondition((earlierRelationship == null),
                            assertion25,
                            testTypeName + assertionMsg25,
                            RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId(),
                            "getRelationship-negative",
                            elapsedTime);


        } catch (AssertionFailureException exception) {

            throw exception;

        } catch (RelationshipNotKnownException exception) {
            /*
             * Even if it supports historical retrieval, the repository should not return any version of a purged relationship,
             * as the relationship and all of its history should have been purged. Therefore this exception being thrown
             * indicates success -- so we do not need to handle it any further.
             */
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                    assertion25,
                    testTypeName + assertionMsg25,
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId(),
                    "getRelationship-negative",
                    elapsedTime);

        } catch (FunctionNotSupportedException exception) {

            super.addNotSupportedAssertion(assertion31,
                                           assertionMsg31,
                                           RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());

        }
        catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "getRelationshipDetail";
            String operationDescription = "retrieve a historical copy of a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("relationshipGUID"   , newRelationship.getGUID());
            parameters.put("asOfTime"     , preDeleteDate.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        /*
         * Relationship was purged - clean up the entities.....
         *
         */


        try {

            /*
             * Delete both end entities.
             * Deleting either entity first would delete the relationship, but it has already been deleted as part of the test
             * this sequence is a little more orderly.
             */


            metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                            end1.getType().getTypeDefGUID(),
                                            end1.getType().getTypeDefName(),
                                            end1.getGUID());

            metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                            end2.getType().getTypeDefGUID(),
                                            end2.getType().getTypeDefName(),
                                            end2.getGUID());


        } catch (FunctionNotSupportedException exception) {
            // NO OP - can proceed to purge
        }


        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                       end1.getType().getTypeDefGUID(),
                                       end1.getType().getTypeDefName(),
                                       end1.getGUID());

        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                       end2.getType().getTypeDefGUID(),
                                       end2.getType().getTypeDefName(),
                                       end2.getGUID());


        super.setSuccessMessage("Relationships can be managed through their lifecycle");
    }





    /**
     * Method to clean any instance created by the test case that has not already been cleaned by the running of the test.
     *
     * @throws Exception something went wrong but there is no particular action to take.
     */
    public void cleanup() throws Exception
    {

        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if (createdRelationships != null && !createdRelationships.isEmpty()) {

            /*
             * Instances were created - clean them up.
             */

            for (Relationship relationship : createdRelationships) {

                try
                {
                    metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                    relationship.getType().getTypeDefGUID(),
                                                    relationship.getType().getTypeDefName(),
                                                    relationship.getGUID());
                }
                catch (FunctionNotSupportedException exception)
                {
                    // NO OP - can proceed to purge
                }
                catch (RelationshipNotKnownException exception)
                {
                    // Relationship already cleaned up - nothing more to do here.
                    continue;
                }

                // If relationship is known then (whether delete was supported or not) issue purge
                metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                                               relationship.getType().getTypeDefGUID(),
                                               relationship.getType().getTypeDefName(),
                                               relationship.getGUID());
            }
        }



        if (createdEntities != null && !createdEntities.isEmpty()) {

            /*
             * Instances were created - clean them up.
             */

            for (EntityDetail entity : createdEntities) {

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
