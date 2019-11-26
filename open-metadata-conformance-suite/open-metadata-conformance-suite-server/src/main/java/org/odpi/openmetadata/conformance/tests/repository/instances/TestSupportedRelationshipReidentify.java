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
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;

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


    private static final String discoveredProperty_reidentifySupport = " reidentify support";



    private String            metadataCollectionId;
    private RelationshipDef   relationshipDef;
    private String            testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid entities
     */
    public TestSupportedRelationshipReidentify(RepositoryConformanceWorkPad workPad,
                                               RelationshipDef              relationshipDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.relationshipDef = relationshipDef;

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
         * Create the local entities.
         */
        String     endOneEntityDefGUID = relationshipDef.getEndDef1().getEntityType().getGUID();
        String     endTwoEntityDefGUID = relationshipDef.getEndDef2().getEntityType().getGUID();
        EntityDef  endOneEntityDef = (EntityDef)metadataCollection.getTypeDefByGUID(workPad.getLocalServerUserId(),endOneEntityDefGUID);
        EntityDef  endTwoEntityDef = (EntityDef)metadataCollection.getTypeDefByGUID(workPad.getLocalServerUserId(),endTwoEntityDefGUID);

        EntityDetail entityOne = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                              endOneEntityDef.getGUID(),
                                                              super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), endOneEntityDef),
                                                              null,
                                                              null);

        EntityDetail entityTwo = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                              endTwoEntityDef.getGUID(),
                                                              super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), endTwoEntityDef),
                                                             null,
                                                             null);

        /*
         * Generate property values for all the type's defined properties, including inherited properties
         * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
         * thereby getting into the connector-logic beyond the property validation. It also creates a
         * relationship that is logically complete - versus an instance with just the locally-defined properties.
         */

        Relationship newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                              relationshipDef.getGUID(),
                                                              super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef),
                                                              entityOne.getGUID(),
                                                              entityTwo.getGUID(),null);

        assertCondition((newRelationship != null),
                        assertion1,
                        testTypeName + assertionMsg1,
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());

       /*
        * Other conditions - such as content of InstanceAuditHeader fields - are tested by Relationship Lifecycle tests; so not tested here.
        */



        /*
         * Validate that the relationship can be retrieved.
         */

        verifyCondition((newRelationship.equals(metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID()))),
                        assertion2,
                        testTypeName + assertionMsg2,
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getProfileId(),
                        RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_IDENTIFIER.getRequirementId());



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

            super.addDiscoveredProperty(testTypeName + discoveredProperty_reidentifySupport,
                    "Enabled",
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

            super.addDiscoveredProperty(testTypeName + discoveredProperty_reidentifySupport,
                    "Disabled",
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
