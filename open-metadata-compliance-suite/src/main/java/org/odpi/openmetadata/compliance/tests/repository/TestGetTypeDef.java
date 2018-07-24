/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGetTypeDef extends OpenMetadataRepositoryTestCase
{
    private static final  String testUserId = "ComplianceTestUser";

    private static final  String testCaseId = "repository-get-typedef";
    private static final  String testCaseName = "Repository get type definition test case";

    private static final  String assertion1    = testCaseId + "-01";
    private static final  String assertionMsg1 = " type verified by repository.";
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
    TestGetTypeDef(String   workbenchId)
    {
        super(workbenchId, testCaseId, testCaseName);
    }


    /**
     * Test that TypeDef can be retrieved in different ways.
     *
     * @param metadataCollection - access to repository
     * @param testObject - object to test
     * @throws Exception - something went wrong
     */
    private void verifyAttributeTypeDef(OMRSMetadataCollection metadataCollection,
                                        AttributeTypeDef       testObject) throws Exception
    {
        String   testTypeName = testObject.getName();

        assertCondition(metadataCollection.verifyAttributeTypeDef(testUserId, testObject),
                        assertion1,
                        testTypeName + assertionMsg1);

        AttributeTypeDef   resultObject = metadataCollection.getAttributeTypeDefByName(testUserId, testObject.getName());

        assertCondition((resultObject != null),
                        assertion2,
                        testTypeName + assertionMsg2);

        assertCondition(testObject.equals(resultObject),
                        assertion3,
                        testTypeName + assertionMsg3);

        resultObject = metadataCollection.getAttributeTypeDefByGUID(testUserId, testObject.getGUID());

        assertCondition((resultObject != null),
                        assertion4,
                        testTypeName + assertionMsg4);

        assertCondition(testObject.equals(resultObject),
                        assertion5,
                        testTypeName + assertionMsg5);
    }

    /**
     * Test that TypeDef can be retrieved in different ways.
     *
     * @param metadataCollection - access to repository
     * @param testObject - object to test
     * @throws Exception - something went wrong
     */
    private void verifyTypeDef(OMRSMetadataCollection metadataCollection,
                               TypeDef                testObject) throws Exception
    {
        String   testTypeName = testObject.getName();

        assertCondition(metadataCollection.verifyTypeDef(testUserId, testObject),
                        assertion1,
                        testTypeName + assertionMsg1);

        TypeDef   resultObject = metadataCollection.getTypeDefByName(testUserId, testObject.getName());

        assertCondition((resultObject != null),
                        assertion2,
                        testTypeName + assertionMsg2);

        assertCondition(testObject.equals(resultObject),
                        assertion3,
                        testTypeName + assertionMsg3);

        resultObject = metadataCollection.getTypeDefByGUID(testUserId, testObject.getGUID());

        assertCondition((resultObject != null),
                        assertion4,
                        testTypeName + assertionMsg4);

        assertCondition(testObject.equals(resultObject),
                        assertion5,
                        testTypeName + assertionMsg5);
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

        TypeDefGallery  typeDefGallery = metadataCollection.getAllTypes("testUser");

        if (typeDefGallery == null)
        {
            discoveredProperties.put("number of supported AttributeTypeDefs", 0);
            discoveredProperties.put("number of supported TypeDefs", 0);
        }
        else
        {
            List<AttributeTypeDef> attributeTypeDefs = typeDefGallery.getAttributeTypeDefs();
            List<TypeDef>          typeDefs = typeDefGallery.getTypeDefs();

            if (attributeTypeDefs == null)
            {
                discoveredProperties.put("number of supported AttributeTypeDefs", 0);
            }
            else
            {
                discoveredProperties.put("number of supported AttributeTypeDefs", attributeTypeDefs.size());

                List<String>    supportedTypes = new ArrayList<>();

                for (AttributeTypeDef   attributeTypeDef : attributeTypeDefs)
                {
                    this.verifyAttributeTypeDef(metadataCollection, attributeTypeDef);
                    supportedTypes.add(attributeTypeDef.getName());
                }

                discoveredProperties.put("supported AttributeTypeDef list", supportedTypes);
            }

            if (typeDefs == null)
            {
                discoveredProperties.put("number of supported TypeDefs", 0);
            }
            else
            {
                discoveredProperties.put("number of supported TypeDefs", typeDefs.size());

                List<String>    supportedTypes = new ArrayList<>();

                for (TypeDef   typeDef : typeDefs)
                {
                    this.verifyTypeDef(metadataCollection, typeDef);
                    supportedTypes.add(typeDef.getName());
                }

                discoveredProperties.put("supported TypeDef list", supportedTypes);
            }
        }

        super.result.setSuccessMessage("Types can be retrieved from the repository consistently");

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
