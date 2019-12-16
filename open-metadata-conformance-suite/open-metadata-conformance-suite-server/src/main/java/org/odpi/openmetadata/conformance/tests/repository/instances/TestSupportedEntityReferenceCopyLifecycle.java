/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;

import java.io.Serializable;
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






    private RepositoryConformanceWorkPad   workPad;
    private String                         metadataCollectionId;
    private EntityDef                      entityDef;
    private String                         testTypeName;
    private OMRSMetadataCollection         localMetadataCollection;


    /*
     * A propagation timeout is used to limit how long the testcase will wait for
     * the propagation of an OMRS instance event and consequent processing at the TUT (or CTS).
     * Each time the testcase waits it does so in a 100ms polling loop, to minimise overall delay.
     * The wait loops will wait for pollCount iterations of pollPeriod, so a pollCount of x10
     * results in a 1000ms (1s) timeout.
     *
     */
    private Integer           pollCount   = 50;
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

        EntityDef knownEntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), entityDef.getName());
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

        EntityDetail newEntity = ctsMetadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                                 entityDef.getGUID(),
                                                                 super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef),
                                                                null,
                                                                null);

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
        Integer remainingCount = this.pollCount;
        while (refEntity == null && remainingCount>0 ) {

            refEntity = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;

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
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


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

        verifyCondition((metadataCollection.getEntitySummary(workPad.getLocalServerUserId(), newEntity.getGUID()) != null),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


        EntityDetail retrievedReferenceCopy = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());

        assertCondition((retrievedReferenceCopy != null),
                assertion3,
                testTypeName + assertionMsg3,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


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
        verifyCondition((metadataCollection.getRelationshipsForEntity(workPad.getLocalServerUserId(),
                retrievedReferenceCopy.getGUID(),
                null,
                0,
                null,
                null,
                null,
                null,
                0) == null),
                assertion5,
                testTypeName + assertionMsg5,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());



        /*
         * If the entity def has any valid status values (including DELETED), attempt
         * to modify the status of the retrieved reference copy - this should fail
         */

        for (InstanceStatus validInstanceStatus : entityDef.getValidInstanceStatusList()) {

            try {

                EntityDetail updatedEntity = metadataCollection.updateEntityStatus(workPad.getLocalServerUserId(), retrievedReferenceCopy.getGUID(), validInstanceStatus);

                assertCondition((false),
                        assertion6,
                        testTypeName + assertionMsg6,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());

            }
            catch (InvalidParameterException e) {
                /*
                 * We are not expecting the status update to work - it should have thrown an InvalidParameterException
                 */

                assertCondition((true),
                        assertion6,
                        testTypeName + assertionMsg6,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());
            }

        }


        /*
         * Attempt to modify one or more property of the retrieved reference copy. This is illegal so it should fail.
         */


        if ((retrievedReferenceCopy.getProperties() != null) &&
                (retrievedReferenceCopy.getProperties().getInstanceProperties() != null) &&
                (!retrievedReferenceCopy.getProperties().getInstanceProperties().isEmpty())) {
            InstanceProperties minEntityProps = super.getMinPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

            try {

                EntityDetail minPropertiesEntity = metadataCollection.updateEntityProperties(workPad.getLocalServerUserId(), retrievedReferenceCopy.getGUID(), minEntityProps);

                assertCondition((false),
                        assertion7,
                        testTypeName + assertionMsg7,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());

            }
            catch (InvalidParameterException e) {
                /*
                 * We are not expecting the status update to work - it should have thrown an InvalidParameterException
                 */

                assertCondition((true),
                        assertion7,
                        testTypeName + assertionMsg7,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());
            }

        }


        /*
         * Verify that it is not possible to re-type the reference copy.
         * This test is performed using the same type as the original - the repository should not get as far as
         * even looking at the type or considering changing it. For simplicity of testcode this test therefore
         * uses the original type.
         * This test is performed against the TUT.
         */

        try {

            EntityDetail reTypedEntity = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                                                         newEntity.getGUID(),
                                                                         entityDef,
                                                                         entityDef); // see comment above about using original type

            assertCondition((false),
                    assertion8,
                    testTypeName + assertionMsg8,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());

        }
        catch (InvalidParameterException e)
        {

            /*
             * We are not expecting the type update to work - it should have thrown an InvalidParameterException
             */

            assertCondition((true),
                    assertion8,
                    testTypeName + assertionMsg8,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());
        }



        /*
         * Verify that it is not possible to re-identify the reference copy.
         * This test is performed using a different GUID to the original. The actual value should not be looked at
         * by the repository - it should reject the re-identify attempt prior to that.
         * This test is performed against the TUT.
         */

        try {

            EntityDetail reIdentifiedEntity = metadataCollection.reIdentifyEntity(workPad.getLocalServerUserId(),
                                                                                  entityDef.getGUID(),
                                                                                  entityDef.getName(),
                                                                                  newEntity.getGUID(),
                                                                                  UUID.randomUUID().toString());


            assertCondition((false),
                    assertion9,
                    testTypeName + assertionMsg9,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());

        }
        catch (InvalidParameterException e)
        {

            /*
             * We are not expecting the identity update to work - it should have thrown an InvalidParameterException
             */

            assertCondition((true),
                    assertion9,
                    testTypeName + assertionMsg9,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());
        }


        /*
         * Purge the reference copy and verify that by requesting a refresh, a new ref copy is created.
         * This test is performed against the TUT.
         */

        try {

            metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(), refEntity);

            /*
             * Note that the ref copy could be purged
             */
            assertCondition((true),
                    assertion10,
                    testTypeName + assertionMsg10,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());

        }
        catch (Exception e)
        {

            /*
             * If for any reason the ref copy could not be purged, fail the test
             */

            assertCondition((false),
                    assertion10,
                    testTypeName + assertionMsg10,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());
        }


        try {
            metadataCollection.refreshEntityReferenceCopy(workPad.getLocalServerUserId(),
                                                          refEntity.getGUID(),
                                                          entityDef.getGUID(),
                                                          entityDef.getName(),
                                                          ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()));

            /*
             * Note that the refresh request could be sent
             */
            assertCondition((true),
                    assertion11,
                    testTypeName + assertionMsg11,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());
        }
        catch (Exception e)
        {

            /*
             * If for any reason the refresh request failed, fail the test
             */

            assertCondition((false),
                    assertion11,
                    testTypeName + assertionMsg11,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());
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
        remainingCount = this.pollCount;
        while (refreshedEntityRefCopy == null && remainingCount>0 ) {

            refreshedEntityRefCopy = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        }


        /*
         * Verify that the reference copy can be retrieved form the TUT and matches the original...
         */
        refreshedEntityRefCopy = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());

        assertCondition((refreshedEntityRefCopy != null),
                assertion12,
                testTypeName + assertionMsg12,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


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

        EntityDetail survivingEntRefCopy;
        remainingCount = this.pollCount;
        do {
            survivingEntRefCopy = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        } while (survivingEntRefCopy != null && remainingCount>0 );


        try {
            metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());

            assertCondition((false),
                    assertion14,
                    testTypeName + assertionMsg14,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());

        }
        catch (EntityNotKnownException exception) {

            assertCondition((true),
                    assertion14,
                    testTypeName + assertionMsg14,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());
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

        try {
            /*
             * Create an entity reference copy of the entity type.
             * To do this, a local entity is created, copied and deleted/purged. The copy is modified (so that it
             * appears to come from an unknown/defunct remote metadata collection). It is then saved as a reference copy
             */


            /*
             * Generate property values for all the type's defined properties, including inherited properties
             */

            entityWithMappingProperties = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                                       entityDef.getGUID(),
                                                                       super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef),
                                                                       null,
                                                                       null);

            assertCondition((true),
                            assertion15,
                           testTypeName + assertionMsg15,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());


        }
        catch (FunctionNotSupportedException exception) {

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
        }
        catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                       entityWithMappingProperties.getType().getTypeDefGUID(),
                                       entityWithMappingProperties.getType().getTypeDefName(),
                                       entityWithMappingProperties.getGUID());

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
        mappingProperties.put("integerMappingPropertyKey", new Integer(12));

        /*
         * Save a reference copy of the 'remote' entity
         */

        try {

            metadataCollection.saveEntityReferenceCopy(workPad.getLocalServerUserId(), remoteEntityWithMappingProperties);


            assertCondition((true),
                            assertion16,
                            testTypeName + assertionMsg16,
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


            EntityDetail retrievedReferenceCopyWithMappingProperties = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), remoteEntityGUID);

            assertCondition((retrievedReferenceCopyWithMappingProperties.equals(remoteEntityWithMappingProperties)),
                            assertion17,
                            assertionMsg17 + entityDef.getName(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                            RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());






        }
        catch (FunctionNotSupportedException e) {

            super.addNotSupportedAssertion(assertion16,
                                           assertionMsg16,
                                           RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        }



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
            newMasterEntity = metadataCollection.reHomeEntity(workPad.getLocalServerUserId(),
                                                              remoteEntityGUID,
                                                              entityDef.getGUID(),
                                                              entityDef.getName(),
                                                              ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()),
                                                              metadataCollectionId,
                                                              repositoryConformanceWorkPad.getTutRepositoryConnector().getMetadataCollectionName());


            assertCondition((true),
                            assertion18,
                           testTypeName + assertionMsg18,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());

            /*
             * Verify that the new master instance can be retrieved
             */
            EntityDetail retrievedReferenceCopyWithMappingProperties = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), remoteEntityGUID);

            assertCondition((retrievedReferenceCopyWithMappingProperties != null),
                            assertion19,
                           assertionMsg19 + entityDef.getName(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());

            /*
             * Verify that the new master instance has the local metadataCollectionId
             */
            String instanceHomne = retrievedReferenceCopyWithMappingProperties.getMetadataCollectionId();
            assertCondition((instanceHomne.equals(metadataCollectionId)),
                            assertion20,
                           assertionMsg20 + entityDef.getName(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());


        }
        catch (FunctionNotSupportedException exception) {

            /*
             * Because rehome is an optional method, this is not fatal - just record that the connector does not support rehome
             */

            super.addNotSupportedAssertion(assertion18,
                                           assertionMsg18,
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
            }
            catch (FunctionNotSupportedException exception) {

                /*
                 * This is OK - we can NO OP and just proceed to purgeEntity
                 */
            }

            metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                           remoteEntityWithMappingProperties.getType().getTypeDefGUID(),
                                           remoteEntityWithMappingProperties.getType().getTypeDefName(),
                                           remoteEntityWithMappingProperties.getGUID());
        }
        else {
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




        super.setSuccessMessage("Reference copies of entities can be managed through their lifecycle");


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
         * Find any entities of the given type def and delete them....
         */

        int fromElement = 0;
        int pageSize = 50; // chunk size - loop below will repeatedly get chunks
        int resultSize = 0;

        do {

            InstanceProperties emptyMatchProperties = new InstanceProperties();


            List<EntityDetail> entities = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                    entityDef.getGUID(),
                    emptyMatchProperties,
                    MatchCriteria.ANY,
                    fromElement,
                    null,
                    null,
                    null,
                    null,
                    null,
                    pageSize);


            if (entities == null) {
                /*
                 * There are no instances of this type reported by the repository.
                 */
                return;

            }

            /*
             * Report how many entities were left behind at the end of the test run
             */

            resultSize = entities.size();

            System.out.println("At completion of testcase "+testTypeName+", there were " + entities.size() + " entities found");

            for (EntityDetail entity : entities) {

                /*
                 * Try soft delete (ok if it fails) and purge.
                 */

                try {
                    EntityDetail deletedEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                            entity.getType().getTypeDefGUID(),
                            entity.getType().getTypeDefName(),
                            entity.getGUID());

                } catch (FunctionNotSupportedException exception) {
                    /* OK - had to try soft; continue to purge */
                }

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                        entity.getType().getTypeDefGUID(),
                        entity.getType().getTypeDefName(),
                        entity.getGUID());

                System.out.println("Entity wth GUID " + entity.getGUID() + " removed");

            }
        } while (resultSize >= pageSize);

    }
}
