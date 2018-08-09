/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test that all defined entities can be created, retrieved, updated and deleted.
 */
public class TestEntityLifecycle extends OpenMetadataRepositoryTestCase
{
    private static final String testUserId = "ComplianceTestUser";

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
    private static final String assertionMsg5  = " new entity has correct status.";
    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " new entity has correct type.";
    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " new entity has local metadata collection.";
    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " new entity has version greater than zero.";
    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " new entity known.";
    private static final String assertion10    = testCaseId + "-10";
    private static final String assertionMsg10 = " new entity summarized.";
    private static final String assertion11    = testCaseId + "-11";
    private static final String assertionMsg11 = " new entity retrieved.";
    private static final String assertion12    = testCaseId + "-12";
    private static final String assertionMsg12 = " new entity is unattached.";



    /**
     * Default constructor sets up superclass
     */
    TestEntityLifecycle(String   workbenchId)
    {
        super(workbenchId, testCaseId, testCaseName);
    }


    /**
     * Test that an Entity can be created, retrieved, updated and deleted.
     *
     * @param metadataCollection - access to repository
     * @param entityTypeDef - type of object to test
     * @throws Exception - something went wrong
     */
    private void verifyEntityLifecycle(OMRSMetadataCollection metadataCollection,
                                       TypeDef                entityTypeDef) throws Exception
    {
        String   testTypeName = entityTypeDef.getName();

        EntityDetail newEntity = metadataCollection.addEntity(testUserId,
                                                              entityTypeDef.getGUID(),
                                                              super.getPropertiesForInstance(entityTypeDef.getPropertiesDefinition()),
                                                              null,
                                                              InstanceStatus.ACTIVE);

        assertCondition((newEntity != null), assertion1, testTypeName + assertionMsg1);
        assertCondition(testUserId.equals(newEntity.getCreatedBy()), assertion2, testTypeName + assertionMsg2);
        assertCondition((newEntity.getCreateTime() != null), assertion3, testTypeName + assertionMsg3);
        assertCondition((newEntity.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT), assertion4, testTypeName + assertionMsg4);
        assertCondition((newEntity.getStatus() == InstanceStatus.ACTIVE), assertion5, testTypeName + assertionMsg5);

        InstanceType instanceType = newEntity.getType();

        if (instanceType != null)
        {
            assertCondition(((instanceType.getTypeDefGUID().equals(entityTypeDef.getGUID())) &&
                             (instanceType.getTypeDefName().equals(testTypeName))), assertion6, testTypeName + assertionMsg6);

        }
        else
        {
            assertCondition(false, assertion6, testTypeName + assertionMsg6);
        }

        assertCondition((newEntity.getMetadataCollectionId() != null), assertion7, testTypeName + assertionMsg7);
        assertCondition((newEntity.getVersion() > 0), assertion8, testTypeName + assertionMsg8);

        assertCondition((metadataCollection.isEntityKnown(testUserId, newEntity.getGUID()) != null), assertion9, testTypeName + assertionMsg9);
        assertCondition((metadataCollection.getEntitySummary(testUserId, newEntity.getGUID()) != null), assertion10, testTypeName + assertionMsg10);
        assertCondition((metadataCollection.getEntityDetail(testUserId, newEntity.getGUID()) != null), assertion11, testTypeName + assertionMsg11);
        assertCondition((metadataCollection.getRelationshipsForEntity(testUserId,
                                                                      newEntity.getGUID(),
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      0) == null), assertion12, testTypeName + assertionMsg12);

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

        List<TypeDef>  typeDefGallery = metadataCollection.findTypeDefsByCategory(testUserId, TypeDefCategory.ENTITY_DEF);

        if (typeDefGallery == null)
        {
            discoveredProperties.put("number of supported entity types", 0);
        }
        else
        {
            discoveredProperties.put("number of supported entity types", typeDefGallery.size());

            List<String>    supportedTypes = new ArrayList<>();

            for (TypeDef   entityTypeDef : typeDefGallery)
            {
                this.verifyEntityLifecycle(metadataCollection, entityTypeDef);
                supportedTypes.add(entityTypeDef.getName());
            }

            discoveredProperties.put("supported entities list", supportedTypes);
        }

        super.result.setSuccessMessage("Entities can be managed through their lifecycle");

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
