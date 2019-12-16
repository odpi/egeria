/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
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
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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



    private RepositoryConformanceWorkPad  workPad;
    private String                        metadataCollectionId;
    private Map<String, EntityDef>        entityDefs;
    private RelationshipDef               relationshipDef;
    private String                        testTypeName;


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
         * Check that the relationship type and end entity types match the known types from the repository helper
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

        String end1TypeDefName = relationshipDef.getEndDef1().getEntityType().getName();
        EntityDef end1EntityDef = entityDefs.get(end1TypeDefName);
        EntityDef knownEnd1EntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), end1EntityDef.getName());
        verifyCondition((end1EntityDef.equals(knownEnd1EntityDef)),
                assertion0,
                testTypeName + assertionMsg0,
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());

        String end2TypeDefName = relationshipDef.getEndDef2().getEntityType().getName();
        EntityDef end2EntityDef = entityDefs.get(end2TypeDefName);
        EntityDef knownEnd2EntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), end2EntityDef.getName());
        verifyCondition((end2EntityDef.equals(knownEnd2EntityDef)),
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


        EntityDetail end1;
        EntityDetail end2;
        Relationship newRelationship;

        try {


            EntityDef end1Type = entityDefs.get(end1TypeName);
            end1 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end1Type);
            EntityDef end2Type = entityDefs.get(end2TypeName);
            end2 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end2Type);


            newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    super.getPropertiesForInstance(relationshipDef.getPropertiesDefinition()),
                    end1.getGUID(),
                    end2.getGUID(),
                    null);

            assertCondition((true),
                    assertion28,
                    testTypeName + assertionMsg28,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        }
        catch (FunctionNotSupportedException exception) {

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
        verifyCondition((newRelationship.equals(metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(),
                newRelationship.getGUID()))),
                assertion9,
                testTypeName + assertionMsg9,
                RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId());

        verifyCondition((newRelationship.equals(metadataCollection.getRelationship(workPad.getLocalServerUserId(),
                newRelationship.getGUID()))),
                assertion10,
                testTypeName + assertionMsg10,
                RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getProfileId(),
                RepositoryConformanceProfileRequirement.METADATA_INSTANCE_ACCESS.getRequirementId());



        /*
         * Update relationship status
         */
        long nextVersion = newRelationship.getVersion() + 1;

        for (InstanceStatus validInstanceStatus : relationshipDef.getValidInstanceStatusList()) {
            if (validInstanceStatus != InstanceStatus.DELETED) {
                Relationship updatedRelationship = metadataCollection.updateRelationshipStatus(workPad.getLocalServerUserId(), newRelationship.getGUID(), validInstanceStatus);

                assertCondition((updatedRelationship != null),
                        assertion11,
                        testTypeName + assertionMsg11,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

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
            metadataCollection.updateRelationshipStatus(workPad.getLocalServerUserId(), newRelationship.getGUID(), InstanceStatus.DELETED);
            verifyCondition((false),
                    assertion14,
                    testTypeName + assertionMsg14,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        } catch (StatusNotSupportedException exception) {
            verifyCondition((true),
                    assertion14,
                    testTypeName + assertionMsg14,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
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

            Relationship minPropertiesRelationship = metadataCollection.updateRelationshipProperties(workPad.getLocalServerUserId(),
                    newRelationship.getGUID(),
                    minRelationshipProps);


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
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

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
            try {
                Relationship undoneRelationship = metadataCollection.undoRelationshipUpdate(workPad.getLocalServerUserId(), newRelationship.getGUID());

                assertCondition(true,
                        assertion29,
                        testTypeName + assertionMsg29,
                        RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                        RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());


                assertCondition(((undoneRelationship != null) && (undoneRelationship.getProperties() != null)),
                        assertion17,
                        testTypeName + assertionMsg17,
                        RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                        RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());

                assertCondition(((undoneRelationship != null) && (undoneRelationship.getVersion() >= nextVersion)),
                        assertion18,
                        testTypeName + assertionMsg18 + nextVersion,
                        RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_UNDO.getProfileId(),
                        RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_UNDO.getRequirementId());

                nextVersion = undoneRelationship.getVersion() + 1;


            }
            catch (FunctionNotSupportedException exception) {

                super.addNotSupportedAssertion(assertion29,
                        assertionMsg29,
                        RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                        RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());

            }
        }



        /*
         * Catch the current time for a later historic query test, then sleep for a second so we are sure that time has moved on
         */
        Date preDeleteDate = new Date();
        Relationship preDeleteRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());


        /*
         * Test that the relationship can be soft deleted, and that the soft deleted relationship has a higher version.
         * Verify that the soft deleted relationship cannot be retrieved, but can be restored and that the restored relationship has
         * a valid version (higher than when it was deleted).
         * Check that the restored relationship can be retrieved.
         */


        try {

            Relationship deletedRelationship = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                    newRelationship.getType().getTypeDefGUID(),
                    newRelationship.getType().getTypeDefName(),
                    newRelationship.getGUID());

            assertCondition(true,
                    assertion30,
                    testTypeName + assertionMsg30,
                    RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                    RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());

            verifyCondition(((deletedRelationship != null) && (deletedRelationship.getVersion() >= nextVersion)),
                    assertion19,
                    testTypeName + assertionMsg19 + nextVersion,
                    RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                    RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

            nextVersion = deletedRelationship.getVersion() + 1;


            try {
                metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

                verifyCondition((false),
                        assertion20,
                        testTypeName + assertionMsg20,
                        RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                        RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());

            }
            catch (RelationshipNotKnownException exception) {
                verifyCondition((true),
                        assertion20,
                        testTypeName + assertionMsg20,
                        RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                        RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());
            }


            Relationship restoredRelationship = metadataCollection.restoreRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

            verifyCondition((restoredRelationship != null),
                    assertion21,
                    testTypeName + assertionMsg21,
                    RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getProfileId(),
                    RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getRequirementId());

            verifyCondition((restoredRelationship.getVersion() >= nextVersion),
                    assertion22,
                    testTypeName + assertionMsg22 + nextVersion,
                    RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_RESTORE.getProfileId(),
                    RepositoryConformanceProfileRequirement.NEW_VERSION_NUMBER_ON_RESTORE.getRequirementId());


            /*
             * Verify that relationship can be retrieved following restore
             */
            verifyCondition((restoredRelationship.equals(metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(), restoredRelationship.getGUID()))),
                    assertion23,
                    testTypeName + assertionMsg23,
                    RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getProfileId(),
                    RepositoryConformanceProfileRequirement.UNDELETE_INSTANCE.getRequirementId());

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
        }


        metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                newRelationship.getType().getTypeDefGUID(),
                newRelationship.getType().getTypeDefName(),
                newRelationship.getGUID());

        try {

            metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

            verifyCondition((false),
                    assertion24,
                    testTypeName + assertionMsg24,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }
        catch (RelationshipNotKnownException exception) {

            verifyCondition((true),
                    assertion24,
                    testTypeName + assertionMsg24,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }


        /*
         * Perform a historic get of the relationship - this should return the relationship even though it has now been [deleted and] purged
         * The time for the query is the time set just before the delete operation above.
         */
        try {
            Relationship earlierRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID(), preDeleteDate);

            assertCondition(true,
                    assertion31,
                    testTypeName + assertionMsg31,
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());


            /*
             * Check that the earlierRelationship is not null and that the relationship matches the copy saved at preDeleteDate.
             */
            assertCondition(((earlierRelationship != null) && earlierRelationship.equals(preDeleteRelationship)),
                    assertion25,
                    testTypeName + assertionMsg25,
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());


        }
        catch (RelationshipNotKnownException exception) {
            /*
             * If it supports historical retrieval, the repository should have returned the relationship, hence fail the test
             */
            assertCondition((false),
                    assertion25,
                    testTypeName + assertionMsg25,
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());

        }
        catch (FunctionNotSupportedException exception) {

            super.addNotSupportedAssertion(assertion31,
                    assertionMsg31,
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());

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
     * Method to clean any instance created by the test case.
     *
     * @throws Exception something went wrong with the test.
     */
    public void cleanup() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        /*
         * Find any relationships of the given type def and delete them....
         */

        int fromElement = 0;
        int pageSize = 50; // chunk size - loop below will repeatedly get chunks
        int resultSize = 0;

        do {


            InstanceProperties emptyMatchProperties = new InstanceProperties();


            List<Relationship> relationships = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    emptyMatchProperties,
                    MatchCriteria.ANY,
                    fromElement,
                    null,
                    null,
                    null,
                    null,
                    pageSize);


            if (relationships == null) {
                /*
                 * There are no instances of this type reported by the repository.
                 */
                return;

            }

            /*
             * Report how many relationships were left behind at the end of the test run
             */

            System.out.println("At completion of testcase "+testTypeName+", there were " + relationships.size() + " relationships found");

            for (Relationship relationship : relationships) {


                /*
                 * Local variables for end2
                 */
                EntityProxy end1;
                EntityProxy end2;


                end1 = relationship.getEntityOneProxy();
                end2 = relationship.getEntityTwoProxy();


                try {

                    /*
                     * Delete the relationship and then both end entities.
                     * Deleting either entity first would delete the relationship, but
                     * this sequence is a little more orderly.
                     */

                    metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                            relationship.getType().getTypeDefGUID(),
                            relationship.getType().getTypeDefName(),
                            relationship.getGUID());


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

                metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                        relationship.getType().getTypeDefGUID(),
                        relationship.getType().getTypeDefName(),
                        relationship.getGUID());

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                        end1.getType().getTypeDefGUID(),
                        end1.getType().getTypeDefName(),
                        end1.getGUID());

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                        end2.getType().getTypeDefGUID(),
                        end2.getType().getTypeDefName(),
                        end2.getGUID());


                System.out.println("Relationship wth GUID " + relationship.getGUID() + " removed");
            }

        } while (resultSize >= pageSize);

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

                    Iterator<String> secondPropertiesKeyIterator = secondPropertiesKeySet.iterator();
                    while (secondPropertiesKeyIterator.hasNext())
                    {
                        String key = secondPropertiesKeyIterator.next();
                        if (!(secondPropertiesMap.get(key).equals(firstPropertiesMap.get(key))))
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
