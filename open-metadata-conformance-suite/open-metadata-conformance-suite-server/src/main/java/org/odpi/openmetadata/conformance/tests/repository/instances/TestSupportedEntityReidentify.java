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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * Test that all defined entities can be created, reidentified and deleted
 */
public class TestSupportedEntityReidentify extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-entity-reidentify";
    private static final String testCaseName = "Repository entity reidentify test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " new entity created.";

    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " new entity retrieved.";

    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " entity is reidentified.";

    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " entity with new identity version number is ";

    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " entity no longer retrievable by previous GUID.";

    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " entity retrievable by new GUID.";


    private static final String discoveredProperty_reidentifySupport = " reidentify support";



    private String            metadataCollectionId;
    private EntityDef         entityDef;
    private String            testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntityReidentify(RepositoryConformanceWorkPad workPad,
                                         EntityDef               entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

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
                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

        /*
         * Other conditions - such as content of InstanceAuditHeader fields - are tested by Entity Lifecycle tests; so not tested here.
         */



        /*
         * Validate that the entity can be retrieved.
         */

        verifyCondition((newEntity.equals(metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID()))),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());



        /*
         * Verify that it is possible to re-identify the entity.
         *
         * Create a new GUID and assign it to the entity.
         *
         */

        String newGUID = UUID.randomUUID().toString();

        long nextVersion = newEntity.getVersion() + 1;

        EntityDetail reIdentifiedEntity = null;

        try {


            reIdentifiedEntity = metadataCollection.reIdentifyEntity(workPad.getLocalServerUserId(),
                    entityDef.getGUID(),
                    entityDef.getName(),
                    newEntity.getGUID(),
                    newGUID);

            super.addDiscoveredProperty(testTypeName + discoveredProperty_reidentifySupport,
                    "Enabled",
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

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
        } catch (FunctionNotSupportedException exception) {

            super.addDiscoveredProperty(testTypeName + discoveredProperty_reidentifySupport,
                    "Disabled",
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());
        }


        /*
         * Validate that the entity can no longer be retrieved under its original GUID.
         */

        try {
            metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());

            assertCondition((false),
                    assertion5,
                    testTypeName + assertionMsg5,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());
        } catch (EntityNotKnownException exception) {
            assertCondition((true),
                    assertion5,
                    testTypeName + assertionMsg5,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());
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
