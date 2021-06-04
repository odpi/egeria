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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * Test that all defined entities can be created, reidentified and deleted
 */
public class TestSupportedRelationshipReidentify extends RepositoryConformanceTestCase
{

    private static final String testCaseId = "repository-relationship-reidentify";
    private static final String testCaseName = "Repository relationship reidentify test case";


    /* Type */

    private static final String assertion0 = testCaseId + "-00";
    private static final String assertionMsg0 = " relationship type definition matches known type  ";


    private static final String assertion101     = testCaseId + "-101";
    private static final String assertionMsg101  = " new entity created.";
    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " new relationship created.";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " new relationship retrieved.";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " relationship is reidentified.";
    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " relationship with new identity version number is ";
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " relationship no longer retrievable by previous GUID.";
    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " relationship retrievable by new GUID.";

    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " end types are supported by repository";

    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " repository supports creation of instances";

    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " repository supports reidentify of instances.";



    private RepositoryConformanceWorkPad  workPad;
    private String                        metadataCollectionId;
    private RelationshipDef               relationshipDef;
    private Map<String, EntityDef>        entityDefs;
    private String                        testTypeName;


    private List<EntityDetail>            createdEntitiesTUT       = new ArrayList<>();
    private List<Relationship>            createdRelationshipsTUT  = new ArrayList<>();


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDefs      entities to test
     * @param relationshipDef type of valid entities
     */
    public TestSupportedRelationshipReidentify(RepositoryConformanceWorkPad workPad,
                                               Map<String, EntityDef>       entityDefs,
                                               RelationshipDef              relationshipDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

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
         * Check that the relationship type matches the known type from the repository helper.
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
                            assertion7,
                            testTypeName + assertionMsg7,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }

        /*
         * The test does not iterate over all possible end types - but it must select an end type that is supported by the repository,
         * so it uses the first type in the supported list for each end.
         */

        String end1TypeName = end1SupportedTypeNames.get(0);
        String end2TypeName = end2SupportedTypeNames.get(0);

        /*
         * To accommodate repositories that do not support the creation of instances, wrap the creation of the entity
         * in a try..catch to check for FunctionNotSupportedException. If the connector throws this, then give up
         * on the test by setting the discovered property to disabled and returning.
         */


        EntityDetail entityOne;
        EntityDetail entityTwo;


        InstanceProperties properties = null;
        EntityDef entityType = null;

        try {

            /*
             * Supply all properties for the instance, including those inherited from supertypes, since they may be mandatory.
             * An alternative here would be to use getMinPropertiesForInstance, but providing all properties creates a logically
             * complete entity
             */

            entityType = entityDefs.get(end1TypeName);
            properties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityType);
            entityOne = metadataCollection.addEntity(workPad.getLocalServerUserId(), entityType.getGUID(), properties, null, null);

            // Record the created instance's GUID for later clean up.
            createdEntitiesTUT.add(entityOne);

            entityType = entityDefs.get(end2TypeName);
            properties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityType);
            entityTwo = metadataCollection.addEntity(workPad.getLocalServerUserId(), entityType.getGUID(), properties, null, null);

            // Record the created instance's GUID for later clean up.
            createdEntitiesTUT.add(entityTwo);

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
            parameters.put("initialProperties", properties != null ? properties.toString() : "null");
            parameters.put("initialClasiifications", "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        Relationship newRelationship = null;
        InstanceProperties instanceProps = null;

        long start;
        long elapsedTime;
        try {

            instanceProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef);

            start = System.currentTimeMillis();
            newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(), relationshipDef.getGUID(), instanceProps, entityOne.getGUID(), entityTwo.getGUID(), null);
            elapsedTime = System.currentTimeMillis() - start;

            // Record the created instance's GUID for later clean up.
            createdRelationshipsTUT.add(newRelationship);

        } catch (FunctionNotSupportedException exception) {

            /*
             * The repository does not support creation of entity instances; we need to report and fail the test
             *
             */

            super.addNotSupportedAssertion(assertion2,
                                           assertionMsg2,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

            /* Give up on the rest of the testcase */
            return;


        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "addRelationship";
            String operationDescription = "add a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            parameters.put("end1 entityGUID", entityOne.getGUID());
            parameters.put("end2 entityGUID", entityTwo.getGUID());
            parameters.put("initialProperties", instanceProps != null ? instanceProps.toString() : "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        assertCondition((true),
                        assertion8,
                        testTypeName + assertionMsg8,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                        "addRelationship",
                        elapsedTime);

        createdRelationshipsTUT.add(newRelationship);


        assertCondition((newRelationship != null),
                        assertion1,
                        testTypeName + assertionMsg1,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        /*
         * Other conditions - such as content of InstanceAuditHeader fields - are tested by Relationship Lifecycle tests; so not tested here.
         */



        /*
         * Validate that the relationship can be retrieved.
         */

        start = System.currentTimeMillis();
        Relationship retrievedRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());
        elapsedTime = System.currentTimeMillis() - start;
        verifyCondition((newRelationship.equals(retrievedRelationship)),
                        assertion2,
                        testTypeName + assertionMsg2,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                        "getRelationship",
                        elapsedTime);



        /*
         * Verify that it is possible to re-identify the relationship.
         *
         * Create a new GUID and assign it to the relationship.
         *
         */

        String newGUID = UUID.randomUUID().toString();

        long nextVersion = newRelationship.getVersion() + 1;

        Relationship reIdentifiedRelationship = null;

        try {

            start = System.currentTimeMillis();
            reIdentifiedRelationship = metadataCollection.reIdentifyRelationship(workPad.getLocalServerUserId(),
                                                                                 relationshipDef.getGUID(),
                                                                                 relationshipDef.getName(),
                                                                                 newRelationship.getGUID(),
                                                                                 newGUID);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition(true,
                            assertion9,
                            testTypeName + assertionMsg9,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId(),
                            "reIdentifyRelationship",
                            elapsedTime);

            createdRelationshipsTUT.add(reIdentifiedRelationship);

            verifyCondition((reIdentifiedRelationship.getGUID().equals(newGUID)),
                            assertion3,
                            testTypeName + assertionMsg3,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());


            assertCondition((reIdentifiedRelationship.getVersion() >= nextVersion),
                            assertion4,
                            testTypeName + assertionMsg4 + nextVersion,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

        } catch (FunctionNotSupportedException exception) {

            super.addNotSupportedAssertion(assertion9,
                                           assertionMsg9,
                                           RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                                           RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

            /* Give up on the rest of the testcase */
            return;

        }
        catch (Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "reIdentifyRelationship";
            String operationDescription = "reidentify a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID"                , relationshipDef.getGUID());
            parameters.put("relationshipGUID"        , newRelationship.getGUID());
            parameters.put("newRelationshipGUID"     , newGUID);
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }


        /*
         * Validate that the relationship can no longer be retrieved under its original GUID.
         */

        try {
            start = System.currentTimeMillis();
            metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((false),
                            assertion5,
                            testTypeName + assertionMsg5,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId(),
                            "getRelationship-negative",
                            elapsedTime);
        } catch (RelationshipNotKnownException exception) {
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion5,
                            testTypeName + assertionMsg5,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId(),
                            "getRelationship-negative",
                            elapsedTime);
        }

        /*
         * Validate that the relationship can be retrieved under its new GUID.
         */

        try {
            start = System.currentTimeMillis();
            Relationship theRelationship = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newGUID);
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((reIdentifiedRelationship.equals(theRelationship)),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId(),
                            "getRelationship",
                            elapsedTime);

        } catch (RelationshipNotKnownException exception) {
            assertCondition((false),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                            RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());
        }


        /*
         * Clean up the test relationship.
         *
         */

        /*
         * Delete (soft then hard) the entity. This is done using the newGUID, so if the reidentify did
         * not work this will fail but that's OK.
         */

        try {
            Relationship deletedRelationship = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                                                     newRelationship.getType().getTypeDefGUID(),
                                                                                     newRelationship.getType().getTypeDefName(),
                                                                                     newGUID);
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeERelationship
             */
        }

        metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                                             newRelationship.getType().getTypeDefGUID(),
                                             newRelationship.getType().getTypeDefName(),
                                             newGUID);


        /*
         * Tidy up the remaining local test objects by purging the local entities
         * If the repository under test supports soft delete, each entity must be deleted before being purged
         * This should also clean up the corresponding ref copies of the test entities at the TUT.
         * These operations are to the CTS
         */

        try {
            EntityDetail deletedEntityOne = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                            entityOne.getType().getTypeDefGUID(),
                                                                            entityOne.getType().getTypeDefName(),
                                                                            entityOne.getGUID());

            EntityDetail deletedEntityTwo = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                            entityTwo.getType().getTypeDefGUID(),
                                                                            entityTwo.getType().getTypeDefName(),
                                                                            entityTwo.getGUID());
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }


        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                       entityOne.getType().getTypeDefGUID(),
                                       entityOne.getType().getTypeDefName(),
                                       entityOne.getGUID());

        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                       entityTwo.getType().getTypeDefGUID(),
                                       entityTwo.getType().getTypeDefName(),
                                       entityTwo.getGUID());

        super.setSuccessMessage("Relationships can be reidentified");
    }




    /**
     * Method to clean any instance created by the test case that has not already been cleaned by the running of the test.
     *
     * @throws Exception something went wrong but there is no particular action to take.
     */
    public void cleanup() throws Exception
    {

        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if (createdRelationshipsTUT != null && !createdRelationshipsTUT.isEmpty()) {

            /*
             * Instances were created - clean them up.
             */

            for (Relationship relationship : createdRelationshipsTUT) {

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
