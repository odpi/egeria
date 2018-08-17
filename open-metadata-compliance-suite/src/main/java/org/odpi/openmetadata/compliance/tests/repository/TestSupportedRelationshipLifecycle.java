/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;

import java.util.HashMap;
import java.util.Map;


/**
 * Test that all defined relationships can be created, retrieved, updated and deleted.
 */
public class TestSupportedRelationshipLifecycle extends OpenMetadataRepositoryTestCase
{
    private static final String testUserId = "ComplianceTestUser";

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



    private String                 metadataCollectionId;
    private Map<String, EntityDef> entityDefs;
    private RelationshipDef        relationshipDef;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     */
    TestSupportedRelationshipLifecycle(String                  workbenchId,
                                       String                  metadataCollectionId,
                                       Map<String, EntityDef>  entityDefs,
                                       RelationshipDef         relationshipDef)
    {
        super(workbenchId, testCaseId, testCaseName);
        this.metadataCollectionId = metadataCollectionId;
        this.relationshipDef = relationshipDef;
        this.entityDefs = entityDefs;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        Map<String, Object>    discoveredProperties = new HashMap<>();
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();


        String   testTypeName = relationshipDef.getName();

        EntityDef     end1Type = entityDefs.get(relationshipDef.getEndDef1().getEntityType().getName());
        EntityDetail  end1 = this.addEntityToRepository(testUserId, metadataCollection, end1Type);
        EntityDef     end2Type = entityDefs.get(relationshipDef.getEndDef2().getEntityType().getName());
        EntityDetail  end2 = this.addEntityToRepository(testUserId, metadataCollection, end2Type);


        Relationship newRelationship = metadataCollection.addRelationship(testUserId,
                                                                          relationshipDef.getGUID(),
                                                                          super.getPropertiesForInstance(relationshipDef.getPropertiesDefinition()),
                                                                          end1.getGUID(),
                                                                          end2.getGUID(),
                                                                          null);

        assertCondition((newRelationship != null), assertion1, testTypeName + assertionMsg1);
        assertCondition(testUserId.equals(newRelationship.getCreatedBy()), assertion2, testTypeName + assertionMsg2);
        assertCondition((newRelationship.getCreateTime() != null), assertion3, testTypeName + assertionMsg3);
        assertCondition((newRelationship.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT), assertion4, testTypeName + assertionMsg4);
        assertCondition((newRelationship.getStatus() == relationshipDef.getInitialStatus()), assertion5, testTypeName + assertionMsg5);

        InstanceType instanceType = newRelationship.getType();

        if (instanceType != null)
        {
            assertCondition(((instanceType.getTypeDefGUID().equals(relationshipDef.getGUID())) &&
                    (instanceType.getTypeDefName().equals(testTypeName))), assertion6, testTypeName + assertionMsg6);

        }
        else
        {
            assertCondition(false, assertion6, testTypeName + assertionMsg6);
        }

        /*
         * The metadata collection should be set up and consistently
         */
        assertCondition(((newRelationship.getMetadataCollectionId() != null) && newRelationship.getMetadataCollectionId().equals(this.metadataCollectionId)),
                        assertion7, testTypeName + assertionMsg7);

        assertCondition((newRelationship.getVersion() > 0), assertion8, testTypeName + assertionMsg8);

        /*
         * Validate that the relationship can be consistently retrieved.
         */
        assertCondition((newRelationship.equals(metadataCollection.isRelationshipKnown(testUserId, newRelationship.getGUID()))), assertion9, testTypeName + assertionMsg9);
        assertCondition((newRelationship.equals(metadataCollection.getRelationship(testUserId, newRelationship.getGUID()))),
                        assertion10, testTypeName + assertionMsg10);

        super.result.setSuccessMessage("Relationships can be managed through their lifecycle");

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
