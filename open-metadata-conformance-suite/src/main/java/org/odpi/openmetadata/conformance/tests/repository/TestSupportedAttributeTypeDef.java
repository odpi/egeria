/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test that the attribute type definition is properly filled out and can be retrieved in different ways.
 */
public class TestSupportedAttributeTypeDef extends OpenMetadataRepositoryTestCase
{
    private static final String testUserId = "ConformanceTestUser";

    private static final String testCaseId             = "repository-attribute-typedef";
    private static final String testCaseName           = "Repository attribute type definition test case";
    private static final String testCaseSuccessMessage = " attribute type definition is compliant";

    private static final String discoveredProperty_attributeTypeDefGUID            = " unique identifier (GUID)";
    private static final String discoveredProperty_attributeTypeDefDescription     = " description";
    private static final String discoveredProperty_attributeTypeDefCategory        = " category";
    private static final String discoveredProperty_attributeTypeDefVersion         = " version";
    private static final String discoveredProperty_attributeTypeDefDescriptionGUID = " description GUID";

    private static final String assertion1     = "-01";
    private static final String assertionMsg1  = " type definition has a name.";
    private static final String assertion2     = "-02";
    private static final String assertionMsg2  = " type definition has a guid.";
    private static final String assertion3     = "-03";
    private static final String assertionMsg3  = " type definition has a version number.";
    private static final String assertion4     = "-04";
    private static final String assertionMsg4  = " type definition has a version name.";
    private static final String assertion5     = "-05";
    private static final String assertionMsg5  = " type definition has a valid category.";
    private static final String assertion6     = "-06";
    private static final String assertionMsg6  = " primitive type definition has a valid category.";
    private static final String assertion7     = "-07";
    private static final String assertionMsg7  = " collection type definition has a valid category.";
    private static final String assertion8     = "-08";
    private static final String assertionMsg8  = " collection type definition has a valid argument count and element types.";
    private static final String assertion9     = "-09";
    private static final String assertionMsg9  = " enum type definition has elements.";
    private static final String assertion10    = "-10";
    private static final String assertionMsg10 = " type verified by repository.";
    private static final String assertion11    = "-11";
    private static final String assertionMsg11 = " type retrieved from repository by name.";
    private static final String assertion12    = "-12";
    private static final String assertionMsg12 = " same type retrieved from repository by name.";
    private static final String assertion13    = "-13";
    private static final String assertionMsg13 = " type retrieved from repository by guid.";
    private static final String assertion14    = "-14";
    private static final String assertionMsg14 = " same type retrieved from repository by guid.";

    private AttributeTypeDef  attributeTypeDef;


    /**
     * Constructor sets up superclass and the attribute type definition to test
     *
     * @param workbenchId - identifier of the calling workbench for the results
     * @param attributeTypeDef - AttributeTypeDef object - this was retrieved on an earlier call.
     */
    TestSupportedAttributeTypeDef(String            workbenchId,
                                  AttributeTypeDef  attributeTypeDef)
    {
        super(workbenchId, testCaseId, testCaseName);
        this.attributeTypeDef = attributeTypeDef;

    }



    /**
     * Build up the assertion name.
     *
     * @param assertionId unique identifier
     * @param testTypeName name of type being tested
     * @return assertion name
     */
    private String getAssertionName(String   assertionId,
                                    String   testTypeName)
    {
        return testCaseId + "-" + testTypeName + assertionId;
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

        /*
         * Verify that the supplied AttributeTypeDef is valid
         */
        String            testTypeName = attributeTypeDef.getName();

        if (testTypeName == null)
        {
            testTypeName = "<null>";
            super.testCaseId = testCaseId + "-" + testTypeName;
            super.result.setTestCaseId(super.testCaseId);
            assertCondition((false), getAssertionName(assertion1, testTypeName), testTypeName + assertionMsg1);
        }
        else
        {
            super.testCaseId = testCaseId + "-" + testTypeName;
            super.result.setTestCaseId(super.testCaseId);
            assertCondition((true), getAssertionName(assertion1, testTypeName), testTypeName + assertionMsg1);
        }

        String guid = attributeTypeDef.getGUID();
        assertCondition((guid != null), getAssertionName(assertion2, testTypeName), testTypeName + assertionMsg2);

        long  versionNumber = attributeTypeDef.getVersion();
        assertCondition((versionNumber != 0), getAssertionName(assertion3, testTypeName), testTypeName + assertionMsg3);

        String versionName = attributeTypeDef.getVersionName();
        assertCondition((versionName != null), getAssertionName(assertion4, testTypeName), testTypeName + assertionMsg4);

        AttributeTypeDefCategory  category = attributeTypeDef.getCategory();
        assertCondition(((category != null) && (category != AttributeTypeDefCategory.UNKNOWN_DEF)), getAssertionName(assertion5, testTypeName), testTypeName + assertionMsg5);

        String categoryName = category.getName();

        switch (category)
        {
            case PRIMITIVE:
                PrimitiveDef  primitiveDef = (PrimitiveDef)attributeTypeDef;
                PrimitiveDefCategory primitiveDefCategory = primitiveDef.getPrimitiveDefCategory();
                assertCondition((primitiveDefCategory != null),
                                getAssertionName(assertion6, testTypeName),
                                testTypeName + assertionMsg6);
                categoryName = categoryName + "<" + primitiveDefCategory.getName() + ">";
                break;

            case COLLECTION:
                CollectionDef collectionDef = (CollectionDef)attributeTypeDef;
                CollectionDefCategory collectionDefCategory = collectionDef.getCollectionDefCategory();
                assertCondition(((collectionDefCategory != null) && (collectionDefCategory != CollectionDefCategory.OM_COLLECTION_UNKNOWN)),
                                getAssertionName(assertion7, testTypeName),
                                testTypeName + assertionMsg7);

                int                        collectionArgCount      = collectionDef.getArgumentCount();
                List<PrimitiveDefCategory> collectionArgs = collectionDef.getArgumentTypes();
                assertCondition(((collectionArgCount > 0) &&
                                 (collectionArgs != null) &&
                                 (collectionArgCount == collectionArgs.size())),
                                getAssertionName(assertion8, testTypeName),
                                testTypeName + assertionMsg8);
                categoryName = categoryName + "<" + collectionDefCategory.getName() + ">";
                break;

            case ENUM_DEF:
                EnumDef   enumDef = (EnumDef)attributeTypeDef;
                List<EnumElementDef> enumElementDefs = enumDef.getElementDefs();
                assertCondition(((enumElementDefs != null) && (!enumElementDefs.isEmpty())),
                                getAssertionName(assertion9, testTypeName),
                                testTypeName + assertionMsg9);
                break;
        }


        /*
         * Verify that the repository confirms it supports this AttributeTypeDef
         */
        assertCondition(metadataCollection.verifyAttributeTypeDef(testUserId, attributeTypeDef),
                        getAssertionName(assertion10, testTypeName),
                        testTypeName + assertionMsg10);

        /*
         * Retrieve the AttributeTypeDef by name and confirm the result is consistent.
         */
        AttributeTypeDef   resultObject = metadataCollection.getAttributeTypeDefByName(testUserId, attributeTypeDef.getName());

        assertCondition((resultObject != null),
                        getAssertionName(assertion11, testTypeName),
                        testTypeName + assertionMsg11);

        assertCondition(attributeTypeDef.equals(resultObject),
                        getAssertionName(assertion12, testTypeName),
                        testTypeName + assertionMsg12);

        /*
         * Retrieve the AttributeTypeDef by GUID and confirm the result is consistent.
         */
        resultObject = metadataCollection.getAttributeTypeDefByGUID(testUserId, attributeTypeDef.getGUID());

        assertCondition((resultObject != null),
                        getAssertionName(assertion13, testTypeName),
                        testTypeName + assertionMsg13);

        assertCondition(attributeTypeDef.equals(resultObject),
                        getAssertionName(assertion14, testTypeName),
                        testTypeName + assertionMsg14);

        super.result.setSuccessMessage(testTypeName + testCaseSuccessMessage);

        discoveredProperties.put(testTypeName + discoveredProperty_attributeTypeDefGUID, guid);
        discoveredProperties.put(testTypeName + discoveredProperty_attributeTypeDefCategory, categoryName);
        discoveredProperties.put(testTypeName + discoveredProperty_attributeTypeDefVersion, versionName + "." + versionNumber);

        if (attributeTypeDef.getDescription() != null)
        {
            discoveredProperties.put(testTypeName + discoveredProperty_attributeTypeDefDescription, attributeTypeDef.getDescription());
        }

        if (attributeTypeDef.getDescriptionGUID() != null)
        {
            discoveredProperties.put(testTypeName + discoveredProperty_attributeTypeDefDescriptionGUID, attributeTypeDef.getDescriptionGUID());
        }

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
