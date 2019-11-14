/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;

import java.util.UUID;



/**
 * Test that all defined entities can be saved as reference copies, that the copies support all (and only) valid operations, and that the copies can be purged.
 */
public class TestSupportedEntityReferenceCopyLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-entity-reference-copy-lifecycle";
    private static final String testCaseName = "Repository entity reference copy lifecycle test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " reference entity is known.";
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
    private static final String assertion10     = testCaseId + "-10";
    private static final String assertionMsg10 = " reference entity purged.";
    private static final String assertion11    = testCaseId + "-11";
    private static final String assertionMsg11 = " reference entity saved.";
    private static final String assertion12    = testCaseId + "-12";
    private static final String assertionMsg12 = " reference entity re-homed.";

    private static final String discoveredProperty_referenceCopySupport = " reference copy support";


    private String            metadataCollectionId;
    private EntityDef         entityDef;
    private String            testTypeName;
    private OMRSMetadataCollection localMetadataCollection;


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

        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.entityDef = entityDef;

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
         * If this does not work there may be a timing issue - i.e. the ref copy may not have been created if the OMRS event has not
         * propagated in time. If this happens refactor the testcase to create all local instances and batch GUIDs. Then on completion
         * of the batch, look for the reference copies.
         *
         */

        if (refEntity != null) {
            /*
             * If we retrieved the reference copy of the entity - we can assert that the TUT supports reference copies.
             */
            super.addDiscoveredProperty(testTypeName + discoveredProperty_referenceCopySupport,
                    "Enabled",
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

            verifyCondition((true),
                    assertion1,
                    testTypeName + assertionMsg1,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        } else {

            /*
             * Disable the discovered property, with evidence that reference storage requirement is not supported.
             */
            super.addDiscoveredProperty(testTypeName + discoveredProperty_referenceCopySupport,
                    "Disabled",
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

            } catch (InvalidParameterException e) {
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

            } catch (InvalidParameterException e) {
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
                    assertion10,
                    testTypeName + assertionMsg10,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());

        } catch (EntityNotKnownException exception) {

            assertCondition((true),
                    assertion10,
                    testTypeName + assertionMsg10,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());
        }



        /*
         * Verify that it IS possible to re-home a reference copy.
         *
         * ===============================================================================================
         * |    NOTE THAT ALTHOUGH THIS OPERATION IS POSSIBLE, THE ONLY WAY TO TEST IT IS TO ISSUE       |
         * |    THE RE-HOME REQUEST BEFORE THE ORIGINAL ENTITY IS DELETED. THIS IS NOT A NORMAL          |
         * |    STATE FOR THE INSTANCE - IF THE ENTITY IS RE-HOMED THERE WILL BE TWO MASTERS             |
         * |    IN THE COHORT.                                                                           |
         * |    DO NOT ATTEMPT ANY FURTHER TESTS ON THE ENTITY OR REFERENCE COPY AFTER THIS, OTHER       |
         * |    THAN THE DELETE OF THE ORIGINAL ENTITY AND REMOTE PURGE OF THE REFERENCE COPY.           |
         * ===============================================================================================
         */

        EntityDetail masterEntity = ctsMetadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                                    entityDef.getGUID(),
                                                                    super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef),
                                                                    null,
                                                                    null);



        /*
         * Retrieve the ref copy from the TUT.
         * Have to be prepared to wait until event has propagated and TUT has created a reference copy of the entity.
         */

        EntityDetail refCopyEntity = null;
        remainingCount = this.pollCount;
        while (refCopyEntity == null && remainingCount>0 ) {
            refCopyEntity = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), masterEntity.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        }

        verifyCondition((refCopyEntity != null),
                assertion11,
                testTypeName + assertionMsg11,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());
        /*
         * Rehome of an instance is a pull operation - i.e. it must be conducted by the TUT as the TUT holds the ref copy.
         */

        try {
            EntityDetail newMasterEntity = metadataCollection.reHomeEntity(workPad.getLocalServerUserId(),
                                                                              masterEntity.getGUID(),
                                                                              entityDef.getGUID(),
                                                                              entityDef.getName(),
                                                                              ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()),
                                                                              metadataCollectionId,
                                                                              repositoryConformanceWorkPad.getTutRepositoryConnector().getMetadataCollectionName());


            assertCondition((true),
                    assertion12,
                    testTypeName + assertionMsg12,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());

            /*
             * Now clean up. This is not a purge of the ref copy - the test will delete and purge the CTS masterEntity below (
             * i.e. regardless of whether the rehome worked or not). That should clean up any lingering ref copy at the TUT.
             * But if the rehome worked we must remotely delete and purge the TUT instance - as it thinks it is no longer a ref copy.
             * Note that these operations are issued to the TUT.
             */



            try {
                EntityDetail deletedMasterEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                                   masterEntity.getType().getTypeDefGUID(),
                                                                                   masterEntity.getType().getTypeDefName(),
                                                                                   masterEntity.getGUID());
            }
            catch (FunctionNotSupportedException exception) {

                /*
                 * This is OK - we can NO OP and just proceed to purgeEntity
                 */
            }

            metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                           masterEntity.getType().getTypeDefGUID(),
                                           masterEntity.getType().getTypeDefName(),
                                           masterEntity.getGUID());


        }
        catch (InvalidParameterException e)
        {

            /*
             * We are expecting the rehome to work.
             */

            assertCondition((false),
                    assertion11,
                    testTypeName + assertionMsg11,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());
        }

        /*
         * Now clean up the CTS instance. i.e. delete and purge the CTS masterEntity.
         */

        /*
         * Delete (soft then hard) the CTS local entity - these operations are performed on the local (CTS) repo.
         * They will cause an OMRS instance event to flow to the TUT and for the ref copy to be purged, but since
         * we put the cohort into a multi-master state the TUT is likely to experience a state violation. That's
         * why this test is at the end.
         * Note that these operations are issued to the CTS.
         */

        try {
            EntityDetail deletedMasterEntity = ctsMetadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                                  masterEntity.getType().getTypeDefGUID(),
                                                                                  masterEntity.getType().getTypeDefName(),
                                                                                  masterEntity.getGUID());
        }
        catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        ctsMetadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                          masterEntity.getType().getTypeDefGUID(),
                                          masterEntity.getType().getTypeDefName(),
                                          masterEntity.getGUID());






        super.setSuccessMessage("Reference copies of entities can be managed through their lifecycle");


    }
}
