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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;

import java.util.ArrayList;
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



    private RepositoryConformanceWorkPad workPad;
    private String            metadataCollectionId;
    private RelationshipDef   relationshipDef;
    private Map<String, EntityDef>        entityDefs;
    private String            testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDefs      types of valid entities
     * @param relationshipDef type of relationship
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
         * Check that the relationship type matches the known type from the repository helper
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
        Relationship newRelationship;

        try {

            /*
             * Create the local entities.
             */
            EntityDef end1Type = entityDefs.get(end1TypeName);
            entityOne = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end1Type);
            EntityDef end2Type = entityDefs.get(end2TypeName);
            entityTwo = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end2Type);

            /*
             * Generate property values for all the type's defined properties, including inherited properties
             * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
             * thereby getting into the connector-logic beyond the property validation. It also creates a
             * relationship that is logically complete - versus an instance with just the locally-defined properties.
             */

            newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                 relationshipDef.getGUID(),
                                                                 super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef),
                                                                 entityOne.getGUID(),
                                                                 entityTwo.getGUID(),
                                                                null);


            assertCondition((true),
                    assertion8,
                    testTypeName + assertionMsg8,
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

            super.addNotSupportedAssertion(assertion8,
                    assertionMsg8,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

            return;
        }

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

        verifyCondition((newRelationship.equals(metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID()))),
                        assertion2,
                        testTypeName + assertionMsg2,
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());



        /*
         * Verify that it is possible to re-identify the relationship.
         *
         * Create a new GUID and assign it to the relationship.
         *
         */

        String newGUID = UUID.randomUUID().toString();

        long  nextVersion = newRelationship.getVersion() + 1;

        Relationship reIdentifiedRelationship = null;

        try {


            reIdentifiedRelationship = metadataCollection.reIdentifyRelationship(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    relationshipDef.getName(),
                    newRelationship.getGUID(),
                    newGUID);

            assertCondition(true,
                    assertion9,
                    testTypeName + assertionMsg9,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

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
        }

        catch (FunctionNotSupportedException exception)
        {

            super.addNotSupportedAssertion(assertion9,
                    assertionMsg9,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());
        }


        /*
         * Validate that the relationship can no longer be retrieved under its original GUID.
         */

        try
        {
            metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

            assertCondition((false),
                    assertion5,
                    testTypeName + assertionMsg5,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());
        }
        catch (RelationshipNotKnownException exception)
        {
            assertCondition((true),
                    assertion5,
                    testTypeName + assertionMsg5,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());
        }

        /*
         * Validate that the relationship can be retrieved under its new GUID.
         */

        try
        {
            metadataCollection.getRelationship(workPad.getLocalServerUserId(), newGUID);

            assertCondition((reIdentifiedRelationship.equals(metadataCollection.getRelationship(workPad.getLocalServerUserId(), newGUID))),
                    assertion6,
                    testTypeName + assertionMsg6,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

        }
        catch (RelationshipNotKnownException exception)
        {
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
        }
        catch (FunctionNotSupportedException exception)
        {

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
        }
        catch (FunctionNotSupportedException exception) {

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
