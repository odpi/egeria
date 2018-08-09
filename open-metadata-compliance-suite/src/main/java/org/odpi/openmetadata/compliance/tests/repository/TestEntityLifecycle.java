/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
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
    private static final  String testUserId = "ComplianceTestUser";

    private static final  String testCaseId = "repository-entity-lifecycle";
    private static final  String testCaseName = "Repository entity lifecycle test case";

    private static final  String assertion1    = testCaseId + "-01";
    private static final  String assertionMsg1 = " new entity created.";
    private static final  String assertion2    = testCaseId + "-02";
    private static final  String assertionMsg2 = " type retrieved from repository by name.";
    private static final  String assertion3    = testCaseId + "-03";
    private static final  String assertionMsg3 = " same type retrieved from repository by name.";
    private static final  String assertion4    = testCaseId + "-04";
    private static final  String assertionMsg4 = " type retrieved from repository by guid.";
    private static final  String assertion5    = testCaseId + "-05";
    private static final  String assertionMsg5 = " same type retrieved from repository by guid.";


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

        assertCondition((newEntity != null),
                        assertion1,
                        testTypeName + assertionMsg1);
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
