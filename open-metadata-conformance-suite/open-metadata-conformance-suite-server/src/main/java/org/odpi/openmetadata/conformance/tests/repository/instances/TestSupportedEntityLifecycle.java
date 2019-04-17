/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;

import java.util.Iterator;
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
    private static final String assertionMsg23 = " entity restored version number is ";
    private static final String assertion24    = testCaseId + "-24";
    private static final String assertionMsg24 = " entity purged.";

    private static final String discoveredProperty_undoSupport = " undo support";
    private static final String discoveredProperty_softDeleteSupport = " soft delete support";


    private String            metadataCollectionId;
    private EntityDef         entityDef;
    private String            testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntityLifecycle(RepositoryConformanceWorkPad workPad,
                                        EntityDef               entityDef)
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
         * Generate property values for all the type's defined properties, including inherited properties
         * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
         * thereby getting into the connector-logic beyond the property validation. It also creates an
         * entity that is logically complete - versus an instance with just the locally-defined properties.
         */

        EntityDetail newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                              entityDef.getGUID(),
                                                              super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef),
                                                              null,
                                                              null);

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

        verifyCondition((newEntity.getVersion() > 0),
                        assertion8,
                        testTypeName + assertionMsg8,
                        RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                        RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

        /*
         * Validate that the entity can be consistently retrieved.
         */
        verifyCondition((newEntity.equals(metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), newEntity.getGUID()))),
                        assertion9,
                        testTypeName + assertionMsg9,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        verifyCondition((metadataCollection.getEntitySummary(workPad.getLocalServerUserId(), newEntity.getGUID()) != null),
                        assertion10,
                        testTypeName + assertionMsg10,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        verifyCondition((newEntity.equals(metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID()))),
                        assertion11,
                        testTypeName + assertionMsg11,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        /*
         * No relationships have been created so none should be returned.
         */
        verifyCondition((metadataCollection.getRelationshipsForEntity(workPad.getLocalServerUserId(),
                                                                      newEntity.getGUID(),
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      0) == null),
                        assertion12,
                        testTypeName + assertionMsg12,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());


        /*
         * Update entity status
         */
        long  nextVersion = newEntity.getVersion() + 1;
        for (InstanceStatus validInstanceStatus : entityDef.getValidInstanceStatusList())
        {
            if (validInstanceStatus != InstanceStatus.DELETED)
            {
                EntityDetail updatedEntity = metadataCollection.updateEntityStatus(workPad.getLocalServerUserId(), newEntity.getGUID(), validInstanceStatus);

                assertCondition((updatedEntity != null),
                                assertion13,
                                testTypeName + assertionMsg13,
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
                assertCondition((updatedEntity.getStatus() == validInstanceStatus),
                                assertion14,
                                testTypeName + assertionMsg14 + validInstanceStatus.getName(),
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
                assertCondition((updatedEntity.getVersion() >= nextVersion),
                                assertion15,
                                testTypeName + assertionMsg15 + nextVersion,
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
                nextVersion = updatedEntity.getVersion() + 1;
            }
        }

        try
        {
            metadataCollection.updateEntityStatus(workPad.getLocalServerUserId(), newEntity.getGUID(), InstanceStatus.DELETED);
            verifyCondition((false),
                            assertion16,
                            testTypeName + assertionMsg16,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        }
        catch (StatusNotSupportedException exception)
        {
            verifyCondition((true),
                            assertion16,
                            testTypeName + assertionMsg16,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        }

        /*
         * Modify the entity such that it has the minimum set of properties possible. If any properties are defined as
         * mandatory (based on their cardinality) then provide them - in order to exercise the connector more fully.
         * All optional properties are removed.
         */

        if ( ( newEntity.getProperties() != null) &&
             ( newEntity.getProperties().getInstanceProperties() != null) &&
             (!newEntity.getProperties().getInstanceProperties().isEmpty()))
        {
            InstanceProperties minEntityProps = super.getMinPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

            EntityDetail minPropertiesEntity = metadataCollection.updateEntityProperties(workPad.getLocalServerUserId(),
                                                                                         newEntity.getGUID(),
                                                                                         minEntityProps);


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
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            /*
             * Check that the returned entity has the new version number...
             */
            verifyCondition(((minPropertiesEntity != null) && (minPropertiesEntity.getVersion() >= nextVersion)),
                            assertion18,
                           testTypeName + assertionMsg18 + nextVersion,
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getProfileId(),
                            RepositoryConformanceProfileRequirement.INSTANCE_VERSIONING.getRequirementId());

            nextVersion = minPropertiesEntity.getVersion() + 1;

            /*
             * Test the ability (or not) to undo the changes just made
             */
            try
            {
                EntityDetail undoneEntity = metadataCollection.undoEntityUpdate(workPad.getLocalServerUserId(), newEntity.getGUID());

                super.addDiscoveredProperty(testTypeName + discoveredProperty_undoSupport,
                                            "Enabled",
                                            RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getProfileId(),
                                            RepositoryConformanceProfileRequirement.RETURN_PREVIOUS_VERSION.getRequirementId());

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

                nextVersion = undoneEntity.getVersion() + 1;
            }
            catch (FunctionNotSupportedException exception)
            {
                super.addDiscoveredProperty(testTypeName + discoveredProperty_undoSupport,
                                            "Disabled",
                                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            }
        }

        try
        {
            EntityDetail deletedEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                         newEntity.getType().getTypeDefGUID(),
                                                                         newEntity.getType().getTypeDefName(),
                                                                         newEntity.getGUID());
            super.addDiscoveredProperty(testTypeName + discoveredProperty_softDeleteSupport,
                                        "Enabled",
                                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            assertCondition(((deletedEntity != null) && (deletedEntity.getVersion() >= nextVersion)),
                            assertion21,
                            testTypeName + assertionMsg21 + nextVersion,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            nextVersion = deletedEntity.getVersion() + 1;

            try
            {
                metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());

                assertCondition((false),
                                assertion22,
                                testTypeName + assertionMsg22,
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
            }
            catch (EntityNotKnownException exception)
            {
                assertCondition((true),
                                assertion22,
                                testTypeName + assertionMsg22,
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
            }

            EntityDetail restoredEntity = metadataCollection.restoreEntity(workPad.getLocalServerUserId(),
                                                                           newEntity.getGUID());

            assertCondition(((restoredEntity != null) && (restoredEntity.getVersion() >= nextVersion)),
                            assertion23,
                            testTypeName + assertionMsg23 + nextVersion,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                            newEntity.getType().getTypeDefGUID(),
                                            newEntity.getType().getTypeDefName(),
                                            newEntity.getGUID());
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addDiscoveredProperty(testTypeName + discoveredProperty_softDeleteSupport,
                                        "Disabled",
                                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        }

        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                       newEntity.getType().getTypeDefGUID(),
                                       newEntity.getType().getTypeDefName(),
                                       newEntity.getGUID());

        try
        {
            metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());

            assertCondition((false),
                            assertion24,
                            testTypeName + assertionMsg24,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        }
        catch (EntityNotKnownException exception)
        {
            assertCondition((true),
                            assertion24,
                            testTypeName + assertionMsg24,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());
        }

        super.setSuccessMessage("Entities can be managed through their lifecycle");
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
