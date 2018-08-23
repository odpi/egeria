/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test that the type definition is properly filled out and can be retrieved in different ways.
 */
public class TestSupportedTypeDef extends OpenMetadataRepositoryTestCase
{
    private static final  String testUserId = "ComplianceTestUser";

    private static final String testCaseId             = "repository-typedef";
    private static final String testCaseName           = "Repository type definition test case";
    private static final String testCaseSuccessMessage = " type definition is compliant";

    private static final String discoveredProperty_typeDefGUID            = " unique identifier (GUID)";
    private static final String discoveredProperty_typeDefDescription     = " description";
    private static final String discoveredProperty_typeDefCategory        = " category";
    private static final String discoveredProperty_typeDefVersion         = " version";
    private static final String discoveredProperty_typeDefDescriptionGUID = " description GUID";
    private static final String discoveredProperty_externalIdentifiers    = " external identifier mapping";

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
    private static final String assertionMsg6  = " type definition has a valid origin.";
    private static final String assertion7     = "-07";
    private static final String assertionMsg7  = " type definition has a creator.";
    private static final String assertion8     = "-08";
    private static final String assertionMsg8  = " type definition has a creation date.";
    private static final String assertion9     = "-09";
    private static final String assertionMsg9  = " type definition has a valid initial status.";
    private static final String assertion10    = "-10";
    private static final String assertionMsg10 = " type definition has a list of valid statuses.";
    private static final String assertion11    = "-11";
    private static final String assertionMsg11 = " classification can be added to at least one entity.";
    private static final String assertion12    = "-12";
    private static final String assertionMsg12 = " relationship type definition has two ends.";
    private static final String assertion13    = "-13";
    private static final String assertionMsg13 = " type verified by repository.";
    private static final String assertion14    = "-14";
    private static final String assertionMsg14 = " type retrieved from repository by name.";
    private static final String assertion15    = "-15";
    private static final String assertionMsg15 = " same type retrieved from repository by name.";
    private static final String assertion16    = "-16";
    private static final String assertionMsg16 = " type retrieved from repository by guid.";
    private static final String assertion17    = "-17";
    private static final String assertionMsg17 = " same type retrieved from repository by guid.";
    private static final String assertion18    = "-18";
    private static final String assertionMsg18 = " type found by repository by name.";
    private static final String assertion19    = "-19";
    private static final String assertionMsg19 = " same type found by repository by name.";
    private static final String assertion20    = "-20";
    private static final String assertionMsg20 = " type name is unique.";

    private TypeDef typeDef;


    /**
     * Default constructor sets up superclass
     *
     * @param workbenchId identifier for calling workbench
     * @param typeDef type to test
     */
    TestSupportedTypeDef(String   workbenchId,
                         TypeDef  typeDef)
    {
        super(workbenchId, testCaseId, testCaseName);
        this.typeDef = typeDef;
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
        String            testTypeName = typeDef.getName();

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

        String guid = typeDef.getGUID();
        assertCondition((guid != null), getAssertionName(assertion2, testTypeName), testTypeName + assertionMsg2);

        long  versionNumber = typeDef.getVersion();
        assertCondition((versionNumber != 0), getAssertionName(assertion3, testTypeName), testTypeName + assertionMsg3);

        String versionName = typeDef.getVersionName();
        assertCondition((versionName != null), getAssertionName(assertion4, testTypeName), testTypeName + assertionMsg4);

        TypeDefCategory category = typeDef.getCategory();
        assertCondition(((category != null) && (category != TypeDefCategory.UNKNOWN_DEF)), getAssertionName(assertion5, testTypeName), testTypeName + assertionMsg5);

        String categoryName = category.getName();

        String origin = typeDef.getOrigin();
        assertCondition((origin != null), getAssertionName(assertion6, testTypeName), testTypeName + assertionMsg6);

        assertCondition(((typeDef.getCreatedBy() != null) && ((typeDef.getVersion() == 1) || (typeDef.getUpdatedBy() != null))),
                        getAssertionName(assertion7, testTypeName), testTypeName + assertionMsg7);
        assertCondition(((typeDef.getCreateTime() != null) && ((typeDef.getVersion() == 1) || (typeDef.getUpdateTime() != null))),
                        getAssertionName(assertion8, testTypeName), testTypeName + assertionMsg8);
        assertCondition((typeDef.getValidInstanceStatusList() != null), getAssertionName(assertion10, testTypeName), testTypeName + assertionMsg10);
        assertCondition(((typeDef.getInitialStatus() != null) &&
                         (typeDef.getValidInstanceStatusList().contains(typeDef.getInitialStatus()))),
                        getAssertionName(assertion9, testTypeName),
                        testTypeName + assertionMsg9);

        switch (category)
        {
            case ENTITY_DEF:
                break;

            case CLASSIFICATION_DEF:
                ClassificationDef  classificationDef = (ClassificationDef)typeDef;
                assertCondition((classificationDef.getValidEntityDefs() != null), getAssertionName(assertion11, testTypeName), testTypeName + assertionMsg11);
                break;

            case RELATIONSHIP_DEF:
                RelationshipDef relationshipDef = (RelationshipDef)typeDef;
                assertCondition(((relationshipDef.getEndDef1() != null) && (relationshipDef.getEndDef2() != null)), getAssertionName(assertion12, testTypeName), testTypeName + assertionMsg12);
                break;
        }

        /*
         * Verify that the repository confirms it supports this TypeDef
         */
        assertCondition(metadataCollection.verifyTypeDef(testUserId, typeDef),
                        getAssertionName(assertion13, testTypeName),
                        testTypeName + assertionMsg13);

        /*
         * Retrieve the TypeDef by name and confirm the result is consistent.
         */
        TypeDef   resultObject = metadataCollection.getTypeDefByName(testUserId, typeDef.getName());

        assertCondition((resultObject != null),
                        getAssertionName(assertion14, testTypeName),
                        testTypeName + assertionMsg14);

        assertCondition(typeDef.equals(resultObject),
                        getAssertionName(assertion15, testTypeName),
                        testTypeName + assertionMsg15);

        /*
         * Retrieve the TypeDef by GUID and confirm the result is consistent.
         */
        resultObject = metadataCollection.getTypeDefByGUID(testUserId, typeDef.getGUID());

        assertCondition((resultObject != null),
                        getAssertionName(assertion16, testTypeName),
                        testTypeName + assertionMsg16);

        assertCondition(typeDef.equals(resultObject),
                        getAssertionName(assertion17, testTypeName),
                        testTypeName + assertionMsg17);

        /*
         * Find the TypeDef by name and confirm the result is consistent.
         */
        TypeDefGallery resultGallery = metadataCollection.findTypesByName(testUserId, typeDef.getName());
        List<TypeDef>  resultList    = resultGallery.getTypeDefs();

        assertCondition((resultList != null),
                        getAssertionName(assertion18, testTypeName),
                        testTypeName + assertionMsg18);

        assertCondition(resultList.contains(typeDef),
                        getAssertionName(assertion19, testTypeName),
                        testTypeName + assertionMsg19);

        assertCondition(((resultGallery.getAttributeTypeDefs() == null) && (resultList.size() == 1)),
                        getAssertionName(assertion20, testTypeName),
                        testTypeName + assertionMsg20);

        /*
         * Capture success and discovered properties.
         */
        super.result.setSuccessMessage(testTypeName + testCaseSuccessMessage);

        discoveredProperties.put(testTypeName + discoveredProperty_typeDefGUID, guid);
        discoveredProperties.put(testTypeName + discoveredProperty_typeDefCategory, categoryName);
        discoveredProperties.put(testTypeName + discoveredProperty_typeDefVersion, versionName + "." + versionNumber);

        if (typeDef.getDescription() != null)
        {
            discoveredProperties.put(testTypeName + discoveredProperty_typeDefDescription, typeDef.getDescription());
        }

        if (typeDef.getDescriptionGUID() != null)
        {
            discoveredProperties.put(testTypeName + discoveredProperty_typeDefDescriptionGUID, typeDef.getDescriptionGUID());
        }

        if (typeDef.getExternalStandardMappings() != null)
        {
            discoveredProperties.put(testTypeName + discoveredProperty_externalIdentifiers, typeDef.getExternalStandardMappings());
        }

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
