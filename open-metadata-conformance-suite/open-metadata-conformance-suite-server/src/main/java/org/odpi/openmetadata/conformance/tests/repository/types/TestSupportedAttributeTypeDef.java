/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.List;

/**
 * Test that the attribute type definition is properly filled out and can be retrieved in different ways.
 */
public class TestSupportedAttributeTypeDef extends RepositoryConformanceTestCase
{
    private static final String rootTestCaseId         = "repository-attribute-typedef";
    private static final String testCaseName           = "Repository attribute type definition test case";
    private static final String testCaseSuccessMessage = " attribute type definition is compliant";

    private static final String discoveredProperty_attributeTypeDefGUID            = " unique identifier (GUID)";
    private static final String discoveredProperty_attributeTypeDefDescription     = " description";
    private static final String discoveredProperty_attributeTypeDefCategory        = " category";
    private static final String discoveredProperty_attributeTypeDefVersion         = " version";
    private static final String discoveredProperty_attributeTypeDefDescriptionGUID = " description GUID";

    private static final String assertion1     = rootTestCaseId + "-01";
    private static final String assertionMsg1  = " type definition has a name.";
    private static final String assertion2     = rootTestCaseId + "-02";
    private static final String assertionMsg2  = " type definition has a guid.";
    private static final String assertion3     = rootTestCaseId + "-03";
    private static final String assertionMsg3  = " type definition has a version number.";
    private static final String assertion4     = rootTestCaseId + "-04";
    private static final String assertionMsg4  = " type definition has a version name.";
    private static final String assertion5     = rootTestCaseId + "-05";
    private static final String assertionMsg5  = " type definition has a valid category.";
    private static final String assertion6     = rootTestCaseId + "-06";
    private static final String assertionMsg6  = " primitive type definition has a valid category.";
    private static final String assertion7     = rootTestCaseId + "-07";
    private static final String assertionMsg7  = " collection type definition has a valid category.";
    private static final String assertion8     = rootTestCaseId + "-08";
    private static final String assertionMsg8  = " collection type definition has a valid argument count and element types.";
    private static final String assertion9     = rootTestCaseId + "-09";
    private static final String assertionMsg9  = " enum type definition has elements.";
    private static final String assertion10    = rootTestCaseId + "-10";
    private static final String assertionMsg10 = " type verified by repository.";
    private static final String assertion11    = rootTestCaseId + "-11";
    private static final String assertionMsg11 = " type retrieved from repository by name.";
    private static final String assertion12    = rootTestCaseId + "-12";
    private static final String assertionMsg12 = " same type retrieved from repository by name.";
    private static final String assertion13    = rootTestCaseId + "-13";
    private static final String assertionMsg13 = " type retrieved from repository by guid.";
    private static final String assertion14    = rootTestCaseId + "-14";
    private static final String assertionMsg14 = " same type retrieved from repository by guid.";

    private AttributeTypeDef  attributeTypeDef;
    private String            testCaseId;
    private String            testTypeName;

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param attributeTypeDef AttributeTypeDef object - this was retrieved on an earlier call.
     * @param source where did the type definition come from
     * @param defaultProfileId identifier of the profile that this test case is focused on
     * @param defaultRequirementId identifier of the requirement within the profile that this test case is focused on
     */
    public TestSupportedAttributeTypeDef(RepositoryConformanceWorkPad workPad,
                                         AttributeTypeDef             attributeTypeDef,
                                         String                       source,
                                         Integer                      defaultProfileId,
                                         Integer                      defaultRequirementId)
    {
        super(workPad,
              defaultProfileId,
              defaultRequirementId);

        testTypeName = attributeTypeDef.getName();
        if (source == null)
        {
            testCaseId = rootTestCaseId + "-" + testTypeName;
        }
        else
        {
            testCaseId = rootTestCaseId + "-" + testTypeName + "-" + source;
        }
        super.updateTestId(rootTestCaseId, testCaseId, testCaseName);

        this.attributeTypeDef = attributeTypeDef;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        assertCondition((attributeTypeDef.getName() != null),
                        assertion1,
                        testTypeName + assertionMsg1,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        String guid = attributeTypeDef.getGUID();
        assertCondition((guid != null),
                        assertion2,
                        testTypeName + assertionMsg2,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        long  versionNumber = attributeTypeDef.getVersion();

        assertCondition((versionNumber != 0),
                        assertion3,
                        testTypeName + assertionMsg3,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        String versionName = attributeTypeDef.getVersionName();
        assertCondition((versionName != null),
                        assertion4,
                        testTypeName + assertionMsg4,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        AttributeTypeDefCategory  category = attributeTypeDef.getCategory();
        assertCondition(((category != null) && (category != AttributeTypeDefCategory.UNKNOWN_DEF)),
                        assertion5,
                        testTypeName + assertionMsg5,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        String categoryName = category.getName();

        switch (category)
        {
            case PRIMITIVE:
                PrimitiveDef  primitiveDef = (PrimitiveDef)attributeTypeDef;
                PrimitiveDefCategory primitiveDefCategory = primitiveDef.getPrimitiveDefCategory();
                assertCondition((primitiveDefCategory != null),
                                assertion6,
                                testTypeName + assertionMsg6,
                                super.defaultProfileId,
                                super.defaultRequirementId);
                categoryName = categoryName + "<" + primitiveDefCategory.getName() + ">";
                break;

            case COLLECTION:
                CollectionDef collectionDef = (CollectionDef)attributeTypeDef;
                CollectionDefCategory collectionDefCategory = collectionDef.getCollectionDefCategory();
                assertCondition(((collectionDefCategory != null) && (collectionDefCategory != CollectionDefCategory.OM_COLLECTION_UNKNOWN)),
                                assertion7,
                                testTypeName + assertionMsg7,
                                super.defaultProfileId,
                                super.defaultRequirementId);

                int                        collectionArgCount      = collectionDef.getArgumentCount();
                List<PrimitiveDefCategory> collectionArgs = collectionDef.getArgumentTypes();
                assertCondition(((collectionArgCount > 0) &&
                                 (collectionArgs != null) &&
                                 (collectionArgCount == collectionArgs.size())),
                                assertion8,
                                testTypeName + assertionMsg8,
                                super.defaultProfileId,
                                super.defaultRequirementId);
                categoryName = categoryName + "<" + collectionDefCategory.getName() + ">";
                break;

            case ENUM_DEF:
                EnumDef   enumDef = (EnumDef)attributeTypeDef;
                List<EnumElementDef> enumElementDefs = enumDef.getElementDefs();
                assertCondition(((enumElementDefs != null) && (!enumElementDefs.isEmpty())),
                                assertion9,
                                testTypeName + assertionMsg9,
                                super.defaultProfileId,
                                super.defaultRequirementId);
                break;
        }


        /*
         * Verify that the repository confirms it supports this AttributeTypeDef
         */
        long start = System.currentTimeMillis();
        boolean verified = metadataCollection.verifyAttributeTypeDef(workPad.getLocalServerUserId(), attributeTypeDef);
        long elapsedTime = System.currentTimeMillis() - start;

        assertCondition(verified,
                        assertion10,
                        testTypeName + assertionMsg10,
                        super.defaultProfileId,
                        super.defaultRequirementId,
                        "verifyAttributeTypeDef",
                        elapsedTime);

        /*
         * Retrieve the AttributeTypeDef by name and confirm the result is consistent.
         */
        start = System.currentTimeMillis();
        AttributeTypeDef   resultObject = metadataCollection.getAttributeTypeDefByName(workPad.getLocalServerUserId(), attributeTypeDef.getName());
        elapsedTime = System.currentTimeMillis() - start;

        assertCondition((resultObject != null),
                        assertion11,
                        testTypeName + assertionMsg11,
                        super.defaultProfileId,
                        super.defaultRequirementId,
                        "getAttributeTypeDefByName",
                        elapsedTime);

        assertCondition(attributeTypeDef.equals(resultObject),
                        assertion12,
                        testTypeName + assertionMsg12,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        /*
         * Retrieve the AttributeTypeDef by GUID and confirm the result is consistent.
         */
        start = System.currentTimeMillis();
        resultObject = metadataCollection.getAttributeTypeDefByGUID(workPad.getLocalServerUserId(), attributeTypeDef.getGUID());
        elapsedTime = System.currentTimeMillis() - start;

        assertCondition((resultObject != null),
                        assertion13,
                        testTypeName + assertionMsg13,
                        super.defaultProfileId,
                        super.defaultRequirementId,
                        "getAttributeTypeDefByGUID",
                        elapsedTime);

        assertCondition(attributeTypeDef.equals(resultObject),
                        assertion14,
                        testTypeName + assertionMsg14,
                        super.defaultProfileId,
                        super.defaultRequirementId);


        super.addDiscoveredProperty(testTypeName + discoveredProperty_attributeTypeDefGUID,
                                    guid,
                                    super.defaultProfileId,
                                    super.defaultRequirementId);
        super.addDiscoveredProperty(testTypeName + discoveredProperty_attributeTypeDefCategory,
                                    categoryName,
                                    super.defaultProfileId,
                                    super.defaultRequirementId);
        super.addDiscoveredProperty(testTypeName + discoveredProperty_attributeTypeDefVersion,
                                    versionName + "." + versionNumber,
                                    super.defaultProfileId,
                                    super.defaultRequirementId);

        if (attributeTypeDef.getDescription() != null)
        {
            super.addDiscoveredProperty(testTypeName + discoveredProperty_attributeTypeDefDescription,
                                        attributeTypeDef.getDescription(),
                                        super.defaultProfileId,
                                        super.defaultRequirementId);
        }

        if (attributeTypeDef.getDescriptionGUID() != null)
        {
            super.addDiscoveredProperty(testTypeName + discoveredProperty_attributeTypeDefDescriptionGUID,
                                        attributeTypeDef.getDescriptionGUID(),
                                        super.defaultProfileId,
                                        super.defaultRequirementId);
        }

        super.setSuccessMessage(testTypeName + testCaseSuccessMessage);
    }
}
