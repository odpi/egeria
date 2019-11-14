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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Test that all defined relationships can be created, retrieved, updated and deleted.
 */
public class TestSupportedRelationshipLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-relationship-lifecycle";
    private static final String testCaseName = "Repository relationship lifecycle test case";

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

    private static final String discoveredProperty_undoSupport       = " undo support";
    private static final String discoveredProperty_softDeleteSupport = " soft delete support";

    private String                 metadataCollectionId;
    private Map<String, EntityDef> entityDefs;
    private RelationshipDef        relationshipDef;
    private String                 testTypeName;


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

        EntityDef     end1Type = entityDefs.get(relationshipDef.getEndDef1().getEntityType().getName());
        EntityDetail  end1 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end1Type);
        EntityDef     end2Type = entityDefs.get(relationshipDef.getEndDef2().getEntityType().getName());
        EntityDetail  end2 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end2Type);


        Relationship newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                          relationshipDef.getGUID(),
                                                                          super.getPropertiesForInstance(relationshipDef.getPropertiesDefinition()),
                                                                          end1.getGUID(),
                                                                          end2.getGUID(),
                                                                          null);

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

        if (instanceType != null)
        {
            verifyCondition(((instanceType.getTypeDefGUID().equals(relationshipDef.getGUID())) &&
                             (instanceType.getTypeDefName().equals(testTypeName))),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        }
        else
        {
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


        System.out.println("RELATIONSHIP LIFECYCLE TEST newRelationship GUID "+newRelationship.getGUID()+" VER "+ newRelationship.getVersion());

        /*
         * Update relationship status
         */
        long  nextVersion = newRelationship.getVersion() + 1;

        for (InstanceStatus validInstanceStatus :relationshipDef.getValidInstanceStatusList())
        {
            if (validInstanceStatus != InstanceStatus.DELETED)
            {
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

                System.out.println("RELATIONSHIP LIFECYCLE TEST updatedRelationship after status change has VER "+ updatedRelationship.getVersion());

            }
        }

        try
        {
            metadataCollection.updateRelationshipStatus(workPad.getLocalServerUserId(), newRelationship.getGUID(), InstanceStatus.DELETED);
            verifyCondition((false),
                    assertion14,
                    testTypeName + assertionMsg14,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }
        catch (StatusNotSupportedException exception)
        {
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

        if ( ( newRelationship.getProperties() != null) &&
                ( newRelationship.getProperties().getInstanceProperties() != null) &&
                (!newRelationship.getProperties().getInstanceProperties().isEmpty()))
        {
            InstanceProperties minRelationshipProps = super.getMinPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef);

            Relationship minPropertiesRelationship = metadataCollection.updateRelationshipProperties(workPad.getLocalServerUserId(),
                    newRelationship.getGUID(),
                    minRelationshipProps);

            System.out.println("RELATIONSHIP LIFECYCLE TEST minPropertiesRelationship after prop minimisation has VER "+ minPropertiesRelationship.getVersion());


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
            try
            {
                Relationship undoneRelationship = metadataCollection.undoRelationshipUpdate(workPad.getLocalServerUserId(), newRelationship.getGUID());

                super.addDiscoveredProperty(testTypeName + discoveredProperty_undoSupport,
                        "Enabled",
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

                System.out.println("RELATIONSHIP LIFECYCLE TEST undoneRelationship after prop restoration has VER "+ undoneRelationship.getVersion());


            }
            catch (FunctionNotSupportedException exception)
            {
                super.addDiscoveredProperty(testTypeName + discoveredProperty_undoSupport,
                        "Disabled",
                        RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                        RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());

            }
        }



        /*
         * Catch the current time for a later historic query test, then sleep for a second so we are sure that time has moved on
         */
        Date preDeleteDate = new Date();
        Relationship preDeleteRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());
        System.out.println("RELATIONSHIP LIFECYCLE TEST preDeleteRelationship : "+ preDeleteRelationship);
        //Thread.sleep(1000);


        /*
         * Test that the relationship can be soft deleted, and that the soft deleted relationship has a higher version.
         * Verify that the soft deleted relationship cannot be retrieved, but can be restored and that the restored relationship has
         * a valid version (higher than when it was deleted).
         * Check that the restored relationship can be retrieved.
         */


        try
        {
            Relationship deletedRelationship = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                                                     newRelationship.getType().getTypeDefGUID(),
                                                                                     newRelationship.getType().getTypeDefName(),
                                                                                     newRelationship.getGUID());
            super.addDiscoveredProperty(testTypeName + discoveredProperty_softDeleteSupport,
                                        "Enabled",
                                        RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());

            verifyCondition(((deletedRelationship != null) && (deletedRelationship.getVersion() >= nextVersion)),
                            assertion19,
                            testTypeName + assertionMsg19 + nextVersion,
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

            nextVersion = deletedRelationship.getVersion() + 1;

            System.out.println("RELATIONSHIP LIFECYCLE TEST deletedRelationship after soft delete has VER "+ deletedRelationship.getVersion());


            try
            {
                metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

                verifyCondition((false),
                                assertion20,
                                testTypeName + assertionMsg20,
                                RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());

            }
            catch (RelationshipNotKnownException exception)
            {
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

            System.out.println("RELATIONSHIP LIFECYCLE TEST restoredRelationship after restore has VER "+ restoredRelationship.getVersion());


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
        catch (FunctionNotSupportedException exception)
        {
            super.addDiscoveredProperty(testTypeName + discoveredProperty_softDeleteSupport,
                                        "Disabled",
                                        RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());
        }


        System.out.println("RELATIONSHIP LIFECYCLE TEST purge relationship ");


        metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                                             newRelationship.getType().getTypeDefGUID(),
                                             newRelationship.getType().getTypeDefName(),
                                             newRelationship.getGUID());

        System.out.println("RELATIONSHIP LIFECYCLE TEST purged relationship ");

        try
        {
            metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

            verifyCondition((false),
                            assertion24,
                            testTypeName + assertionMsg24,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }
        catch (RelationshipNotKnownException exception)
        {
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
        try
        {
            Relationship earlierRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID(), preDeleteDate);

            super.addDiscoveredProperty(testTypeName + discoveredProperty_undoSupport,
                    "Enabled",
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());

            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("RELATIONSHIP LIFECYCLE TEST historicRelationship : "+ earlierRelationship);

            /*
             * Check that the earlierRelationship is not null and that the relationship matches the copy saved at preDeleteDate.
             */
            assertCondition( ( (earlierRelationship != null)  && earlierRelationship.equals(preDeleteRelationship)),
                    assertion25,
                    testTypeName + assertionMsg25,
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());


        }
        catch (RelationshipNotKnownException exception)
        {
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

            super.addDiscoveredProperty(testTypeName + discoveredProperty_undoSupport,
                    "Disabled",
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.HISTORICAL_PROPERTY_SEARCH.getRequirementId());

        }

        super.setSuccessMessage("Relationships can be managed through their lifecycle");
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
