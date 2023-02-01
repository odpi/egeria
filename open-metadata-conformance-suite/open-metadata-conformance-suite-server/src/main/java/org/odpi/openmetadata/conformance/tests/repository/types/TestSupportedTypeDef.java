/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.List;

/**
 * Test that the type definition is properly filled out and can be retrieved in different ways.
 */
public class TestSupportedTypeDef extends RepositoryConformanceTestCase
{
    private static final String rootTestCaseId         = "repository-typedef";
    private static final String testCaseName           = "Repository type definition test case";
    private static final String testCaseSuccessMessage = " type definition is compliant";

    private static final String discoveredProperty_typeDefGUID            = " unique identifier (GUID)";
    private static final String discoveredProperty_typeDefDescription     = " description";
    private static final String discoveredProperty_typeDefCategory        = " category";
    private static final String discoveredProperty_typeDefVersion         = " version";
    private static final String discoveredProperty_typeDefDescriptionGUID = " description GUID";
    private static final String discoveredProperty_externalIdentifiers    = " external identifier mapping";

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
    private static final String assertionMsg6  = " type definition has a valid origin.";
    private static final String assertion7     = rootTestCaseId + "-07";
    private static final String assertionMsg7  = " type definition has a creator.";
    private static final String assertion8     = rootTestCaseId + "-08";
    private static final String assertionMsg8  = " type definition has a creation date.";
    private static final String assertion9     = rootTestCaseId + "-09";
    private static final String assertionMsg9  = " type definition has a valid initial status.";
    private static final String assertion10    = rootTestCaseId + "-10";
    private static final String assertionMsg10 = " type definition has a list of valid statuses.";
    private static final String assertion11    = rootTestCaseId + "-11";
    private static final String assertionMsg11 = " classification can be added to at least one entity.";
    private static final String assertion12    = rootTestCaseId + "-12";
    private static final String assertionMsg12 = " relationship type definition has two ends.";
    private static final String assertion13    = rootTestCaseId + "-13";
    private static final String assertionMsg13 = " type verified by repository.";
    private static final String assertion14    = rootTestCaseId + "-14";
    private static final String assertionMsg14 = " type retrieved from repository by name.";
    private static final String assertion15    = rootTestCaseId + "-15";
    private static final String assertionMsg15 = " same type retrieved from repository by name.";
    private static final String assertion16    = rootTestCaseId + "-16";
    private static final String assertionMsg16 = " type retrieved from repository by guid.";
    private static final String assertion17    = rootTestCaseId + "-17";
    private static final String assertionMsg17 = " same type retrieved from repository by guid.";
    private static final String assertion18    = rootTestCaseId + "-18";
    private static final String assertionMsg18 = " type found by repository by name.";
    private static final String assertion19    = rootTestCaseId + "-19";
    private static final String assertionMsg19 = " same type found by repository by name.";
    private static final String assertion20    = rootTestCaseId + "-20";
    private static final String assertionMsg20 = " type name is unique.";

    private final TypeDef typeDef;
    private final String  testCaseId;
    private final String  testTypeName;

    /**
     * Typical constructor sets up superclass
     *
     * @param workPad place for parameters and results
     * @param typeDef type to test
     * @param source source of test case execution request
     * @param defaultProfileId most of the results will be recorded in this profile
     * @param defaultRequirementId most of the results will be recorded under this requirement
     */
    public TestSupportedTypeDef(RepositoryConformanceWorkPad workPad,
                                TypeDef                      typeDef,
                                String                       source,
                                Integer                      defaultProfileId,
                                Integer                      defaultRequirementId)
    {
        super(workPad,
              defaultProfileId,
              defaultRequirementId);

        testTypeName = typeDef.getName();
        testCaseId = rootTestCaseId + "-" + testTypeName + "-" + source;
        super.updateTestId(rootTestCaseId, testCaseId, testCaseName);

        this.typeDef = typeDef;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        assertCondition((typeDef.getName() != null),
                        assertion1,
                        testTypeName + assertionMsg1,
                        super.defaultProfileId,
                        super.defaultRequirementId);


        String guid = typeDef.getGUID();

        verifyCondition((guid != null),
                        assertion2,
                        testTypeName + assertionMsg2,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        long  versionNumber = typeDef.getVersion();

        verifyCondition((versionNumber != 0),
                        assertion3,
                        testTypeName + assertionMsg3,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        String versionName = typeDef.getVersionName();

        verifyCondition((versionName != null),
                        assertion4,
                        testTypeName + assertionMsg4,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        TypeDefCategory category = typeDef.getCategory();

        verifyCondition(((category != null) && (category != TypeDefCategory.UNKNOWN_DEF)),
                        assertion5,
                        testTypeName + assertionMsg5,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        String categoryName;

        if (category != null)
        {
            categoryName = category.getName();
        }
        else
        {
            categoryName = "<Unknown>";
        }

        String origin = typeDef.getOrigin();
        verifyCondition((origin != null),
                        assertion6,
                        testTypeName + assertionMsg6,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        verifyCondition(((typeDef.getCreatedBy() != null) && ((typeDef.getVersion() == 1) || (typeDef.getUpdatedBy() != null))),
                        assertion7,
                        testTypeName + assertionMsg7,
                        super.defaultProfileId,
                        super.defaultRequirementId);
        verifyCondition(((typeDef.getCreateTime() != null) && ((typeDef.getVersion() == 1) || (typeDef.getUpdateTime() != null))),
                        assertion8,
                        testTypeName + assertionMsg8,
                        super.defaultProfileId,
                        super.defaultRequirementId);
        verifyCondition((typeDef.getValidInstanceStatusList() != null),
                        assertion10,
                        testTypeName + assertionMsg10,
                        super.defaultProfileId,
                        super.defaultRequirementId);
        verifyCondition(((typeDef.getInitialStatus() != null) &&
                         (typeDef.getValidInstanceStatusList().contains(typeDef.getInitialStatus()))),
                        assertion9,
                        testTypeName + assertionMsg9,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        if (category != null)
        {
            switch (category)
            {
                case ENTITY_DEF:
                    break;

                case CLASSIFICATION_DEF:
                    ClassificationDef  classificationDef = (ClassificationDef)typeDef;
                    assertCondition((classificationDef.getValidEntityDefs() != null),
                                    assertion11,
                                    testTypeName + assertionMsg11,
                                    super.defaultProfileId,
                                    super.defaultRequirementId);
                    break;

                case RELATIONSHIP_DEF:
                    RelationshipDef relationshipDef = (RelationshipDef)typeDef;
                    assertCondition(((relationshipDef.getEndDef1() != null) && (relationshipDef.getEndDef2() != null)),
                                    assertion12,
                                    testTypeName + assertionMsg12,
                                    super.defaultProfileId,
                                    super.defaultRequirementId);
                    break;
            }
        }

        /*
         * Verify that the repository confirms it supports this TypeDef
         */
        long start = System.currentTimeMillis();
        boolean verified = metadataCollection.verifyTypeDef(workPad.getLocalServerUserId(), typeDef);
        long elapsedTime = System.currentTimeMillis() - start;
        assertCondition(verified,
                        assertion13,
                        testTypeName + assertionMsg13,
                        super.defaultProfileId,
                        super.defaultRequirementId,
                        "verifyTypeDef",
                        elapsedTime);

        /*
         * Retrieve the TypeDef by name and confirm the result is consistent.
         */
        start = System.currentTimeMillis();
        TypeDef   resultObject = metadataCollection.getTypeDefByName(workPad.getLocalServerUserId(), typeDef.getName());
        elapsedTime = System.currentTimeMillis() - start;

        verifyCondition((resultObject != null),
                        assertion14,
                        testTypeName + assertionMsg14,
                        super.defaultProfileId,
                        super.defaultRequirementId,
                        "getTypeDefByName",
                        elapsedTime);

        verifyCondition(( (typeDef.getVersion() != resultObject.getVersion()) || typeDef.equals(resultObject) ),
                        assertion15,
                        testTypeName + assertionMsg15,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        /*
         * Retrieve the TypeDef by GUID and confirm the result is consistent.
         */
        start = System.currentTimeMillis();
        resultObject = metadataCollection.getTypeDefByGUID(workPad.getLocalServerUserId(), typeDef.getGUID());
        elapsedTime = System.currentTimeMillis() - start;

        verifyCondition((resultObject != null),
                        assertion16,
                        testTypeName + assertionMsg16,
                        super.defaultProfileId,
                        super.defaultRequirementId,
                        "getTypeDefByGUID",
                        elapsedTime);

        verifyCondition(( (typeDef.getVersion() != resultObject.getVersion()) || typeDef.equals(resultObject) ),
                        assertion17,
                        testTypeName + assertionMsg17,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        /*
         * Find the TypeDef by name and confirm the result is consistent.
         */
        start = System.currentTimeMillis();
        TypeDefGallery resultGallery = metadataCollection.findTypesByName(workPad.getLocalServerUserId(), typeDef.getName());
        elapsedTime = System.currentTimeMillis() - start;
        List<TypeDef>  resultList    = resultGallery.getTypeDefs();

        verifyCondition((resultList != null),
                        assertion18,
                        testTypeName + assertionMsg18,
                        super.defaultProfileId,
                        super.defaultRequirementId,
                        "findTypesByName",
                        elapsedTime);

        /*
         * Look in the result list for a typedef with the same name, and verify that it matches if it claims to be at the same version.
         */
        TypeDef typeDefFromGallery = null;
        if (resultList != null && !resultList.isEmpty())
        {
            String typeDefName = typeDef.getName();
            for (TypeDef td : resultList)
            {
                if (td.getName().equals(typeDefName))
                {
                    typeDefFromGallery = td;
                    break;
                }
            }
        }
        verifyCondition(((resultList != null) && (!resultList.isEmpty()) && (typeDefFromGallery!= null) && ( (typeDef.getVersion() != resultObject.getVersion()) || typeDef.equals(resultObject) )),
                        assertion19,
                        testTypeName + assertionMsg19,
                        super.defaultProfileId,
                        super.defaultRequirementId);

        verifyCondition(((resultGallery.getAttributeTypeDefs() == null) && (resultList != null) && (resultList.size() == 1)),
                        assertion20,
                        testTypeName + assertionMsg20,
                        super.defaultProfileId,
                        super.defaultRequirementId);



        super.addDiscoveredProperty(testTypeName + discoveredProperty_typeDefGUID,
                                    guid,
                                    super.defaultProfileId,
                                    super.defaultRequirementId);
        super.addDiscoveredProperty(testTypeName + discoveredProperty_typeDefCategory,
                                    categoryName,
                                    super.defaultProfileId,
                                    super.defaultRequirementId);
        super.addDiscoveredProperty(testTypeName + discoveredProperty_typeDefVersion,
                                    versionName + "." + versionNumber,
                                    super.defaultProfileId,
                                    super.defaultRequirementId);

        if (typeDef.getDescription() != null)
        {
            super.addDiscoveredProperty(testTypeName + discoveredProperty_typeDefDescription,
                                        typeDef.getDescription(),
                                        super.defaultProfileId,
                                        super.defaultRequirementId);
        }

        if (typeDef.getDescriptionGUID() != null)
        {
            super.addDiscoveredProperty(testTypeName + discoveredProperty_typeDefDescriptionGUID,
                                        typeDef.getDescriptionGUID(),
                                        super.defaultProfileId,
                                        super.defaultRequirementId);
        }

        if (typeDef.getExternalStandardMappings() != null)
        {
            super.addDiscoveredProperty(testTypeName + discoveredProperty_externalIdentifiers,
                                        typeDef.getExternalStandardMappings(),
                                        super.defaultProfileId,
                                        super.defaultRequirementId);
        }

        /*
         * Capture success and discovered properties.
         */
        super.setSuccessMessage(testTypeName + testCaseSuccessMessage);
    }
}
