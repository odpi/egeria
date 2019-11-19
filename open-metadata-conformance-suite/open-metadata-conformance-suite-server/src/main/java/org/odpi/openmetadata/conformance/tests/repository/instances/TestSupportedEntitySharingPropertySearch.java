/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;


/**
 * Test that all defined entities can be retrieved by property searches.
 *
 * This testcase does not assume that the repository under test supports the METADATA_MAINTENANCE profile -
 * i.e. it does not necessarily support the creation of instances via the OMRS API. This testcase therefore
 * does not use addEntity to create sets of test instances. Instead, the testcase relies on the repository
 * already containing instances that can be searched and retrieved.
 *
 * The testcase starts by performing a wide search (within the specified entity type) and then uses the
 * discovered properties to perform narrow subsequent searches and inspect the returned result set.
 *
 * The values of string properties are left 'raw' for the early tests and are literalised by the
 * repository helper methods in the later, findEntitiesByPropertyValue() tests. This is to ensure that
 * both styes of use operate correctly.
 *
 */

public class TestSupportedEntitySharingPropertySearch extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-entity-sharing-property-search";
    private static final String testCaseName = "Repository entity sharing property search test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " search returned results.";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " search contained expected number of results.";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " search contained expected results.";


    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " search returned results.";
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " search contained expected number of results.";
    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " search contained expected results.";


    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " search returned results.";
    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " search contained expected number of results.";
    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " search contained expected results.";

    private static final String assertion10     = testCaseId + "-10";
    private static final String assertionMsg10  = " search returned results.";
    private static final String assertion11     = testCaseId + "-11";
    private static final String assertionMsg11  = " search contained expected number of results.";
    private static final String assertion12     = testCaseId + "-12";
    private static final String assertionMsg12  = " search contained expected results.";

    private static final String assertion13     = testCaseId + "-13";
    private static final String assertionMsg13  = " search returned results.";
    private static final String assertion14     = testCaseId + "-14";
    private static final String assertionMsg14  = " search contained expected number of results.";
    private static final String assertion15     = testCaseId + "-15";
    private static final String assertionMsg15  = " search contained expected results.";





    private static final String discoveredProperty_searchSupport       = " search support";


    private String                 metadataCollectionId;
    private EntityDef              entityDef;
    private List<TypeDefAttribute> attrList;
    private String                 testTypeName;




    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntitySharingPropertySearch(RepositoryConformanceWorkPad workPad,
                                                    EntityDef                    entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                                                    testCaseId,
                                                    testCaseName);



    }


    /**
     * Default run method - throws Exception because this is a multi-phase testcase
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {

        /*
         * This test will:
         *
         * Perform a broad search for instances of the specified entity type.
         *
         */


        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();


        /*
         * Construct a list of TypeDefAttributes that are specified by the known Entity type
         */

        this.attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(), entityDef);

        List<TypeDefAttribute> uniqueAttributes = new ArrayList<>();
        List<String> definedAttributeNames = new ArrayList<>();

        if (this.attrList != null && !(this.attrList.isEmpty())) {

            /*
             * The TypeDef has defined attributes. Create a List<String> of just the attribute names.
             * Also identify any attributes that are defined as unique as these may be used for search narrowing,
             * so will be convenient to have them in a separate list.
             * This is only collated for primitives.
             */
            for (TypeDefAttribute typeDefAttribute : attrList) {
                if (typeDefAttribute.getAttributeType().getCategory() == AttributeTypeDefCategory.PRIMITIVE) {
                    definedAttributeNames.add(typeDefAttribute.getAttributeName());
                    if (typeDefAttribute.isUnique()) {
                        uniqueAttributes.add(typeDefAttribute);
                    }
                }
            }
        }


        /*
         * Perform a broad search
         *
         * This initial search uses findEntitiesByproperty() with an empty match properties object and matchCriteria ANY
         * The result can be compared to a findEntitiesBypropertyValue() with an totally wild regular expression for searchCriteria.
         *
         * This testcase does not perform general regular expression searches; they are in the corresponding advanced-search testcase.
         */



        InstanceProperties emptyMatchProperties    = new InstanceProperties();
        int                fromElement             = 0;
        int                pageSize                = 50;
        List<EntityDetail> result                  = null;
        int                instanceCount           = 0;
        boolean            pageLimited             = false;

        /* ------------------------------------------------------------------------------------- */

        /*
         *  Use emptyMatchProperties and matchCriteria ALL   - this should return all entities of the current type
         */


        result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                           entityDef.getGUID(),
                                                           emptyMatchProperties,
                                                           MatchCriteria.ALL,
                                                           fromElement,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                           pageSize);


        if (result == null) {
            /*
             * There are instances of this type reported by the repository.
             * This is not an error or failure - it could be that the repository contains no instances.
             * Quietly give up the test for this type.
             */
            return;

        }



        /*
         * Record the total instance count and the overall set of discovered entities. There may be more entities
         * that were not returned due to pageSize but that is allowed for in the tests below.
         */
        instanceCount = result.size();
        if (instanceCount == pageSize)
            pageLimited = true;

        List<String> allKnownEntityGUIDs = new ArrayList<>();
        for (EntityDetail entity : result) {
            allKnownEntityGUIDs.add(entity.getGUID());
        }


        /*
         * Construct a reverse index of entity GUIDs by property name and property value.
         * This is only performed for primitives.
         */
        Map<String, Map <Object, List<String>>>   propertyValueMap   = new HashMap<>();
        Map<String,PrimitiveDefCategory>          propertyCatMap     = new HashMap<>();


        for (TypeDefAttribute typeDefAttribute : attrList) {

            if (typeDefAttribute.getAttributeType().getCategory() == AttributeTypeDefCategory.PRIMITIVE) {

                String attrName = typeDefAttribute.getAttributeName();

                PrimitiveDef primDef = (PrimitiveDef) typeDefAttribute.getAttributeType();
                propertyCatMap.put(attrName,primDef.getPrimitiveDefCategory());

                Map<Object, List<String>> valueMap = new HashMap<>();
                propertyValueMap.put(attrName, valueMap);

                for (EntityDetail entity : result) {
                    InstanceProperties entityProperties = entity.getProperties();
                    InstancePropertyValue ipValue = entityProperties.getPropertyValue(attrName);
                    if (ipValue != null) {
                        InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                        if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                            Object primitiveValue = ipValue.valueAsObject();
                            if (valueMap.get(primitiveValue) == null) {
                                List<String> newList = new ArrayList<>();
                                valueMap.put(primitiveValue, newList);
                            }
                            List<String> entityGUIDs = valueMap.get(primitiveValue);
                            entityGUIDs.add(entity.getGUID());
                        }
                    }
                }
            }
        }



        /* ------------------------------------------------------------------------------------- */

        /*
         * Single property findEntitiesByProperty() tests - search for instances using matchProperties containing each individual primitive property.
         * Defined attributes only includes primitives.
         */

        if (!definedAttributeNames.isEmpty()) {

            for (String attributeName : definedAttributeNames) {

                Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
                Iterator<Object> possibleValueIterator = possibleValues.iterator();
                while (possibleValueIterator.hasNext()) {

                    Object value = possibleValueIterator.next();

                    InstanceProperties matchProperties = new InstanceProperties();

                    PrimitivePropertyValue ppv = new PrimitivePropertyValue();
                    ppv.setPrimitiveDefCategory(propertyCatMap.get(attributeName));
                    if (propertyCatMap.get(attributeName) == OM_PRIMITIVE_TYPE_STRING) {
                        String literalisedValue = literaliseStringProperty((String)value);
                        ppv.setPrimitiveValue(literalisedValue);
                    }
                    else {
                        ppv.setPrimitiveValue(value);
                    }



                    matchProperties.setProperty(attributeName,ppv);

                    /*
                     * Expected result size
                     */
                    int expectedEntityCount = propertyValueMap.get(attributeName).get(value).size();

                    /*
                     * Search....
                     */

                    result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                                       entityDef.getGUID(),
                                                                       matchProperties,
                                                                       MatchCriteria.ALL,
                                                                       fromElement,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                       pageSize);


                    /*
                     * The approach to checking results match expectations is as follows:
                     * The original find request (top of this testcase) returned a set of instances that
                     * are known to be in the repository. If that search hit the page limit then the
                     * instances are a partial result of what is actually in the repository. Although it
                     * is possible to sort the results on a property, there is no guarantee that the values
                     * associated with any particular property are distinct, so the resulting order is
                     * not guaranteed. If this were an OMAS this would not be a problem because the OMAS/
                     * user would mostly likely continue to search until they either find what they are
                     * looking for or exhaust the contents of the repository. Since this is an automated
                     * testcase to which we need a predictable, repeatable result, it needs to be more
                     * robust. It is not appropriate to keep looping page by page because we do not know
                     * how many matching instances the repository contains. It is reasonable (and desirable)
                     * to perform a couple of page requests (since it tests that paging is working), but
                     * this testcase will not loop exhaustively.
                     * A constant page size is assumed throughout the following.
                     * It is also assumed that instances are not being added or deleted during the course
                     * of this testcase.
                     * The original result set is used to generate the expected result from a narrower search.
                     * If the original result set returned less than the page size then we know the full
                     * set of instances in the repository and hence completely know each narrower expected set.
                     *
                     * So if original result size < page size then:
                     *  search size < expected size => fail
                     *  search size == expected size => if search contains all expected => pass
                     *                                  else search !contains all expected => fail
                     *  search size > expect size => fail (should not get more than the expectation)
                     *
                     *
                     * In contrast, if the original result set returned a full page size then the testcase needs to
                     * exercise a looser result matching policy, described below.
                     *
                     * So if original result size == page size then:
                     *  search size < expected size => fail
                     *  search size == expected size => if search contains all expected => pass
                     *                                  else search !contains all expected => check whether the unexpected instances are a valid match
                     *                                     if true => pass
                     *                                     else => fail
                     *  search size > expect size =>    check whether the unexpected instances are a valid match
                     *                                     if true => pass
                     *                                     else => fail
                     *
                     */



                    /*
                     * It is reasonable to expect a non-null result - based on the way the search properties were constructed
                     */
                    assertCondition((result != null),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                    /*
                     * Check that the expected number of entities was returned. This has to consider the effect of the original
                     * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
                     * But if the limit was hit (on the original search) then there may be additional instances in the repository
                     * that were not seen on the original search; the expected result was computed from only thos instance that WERE seen,
                     * so the expectation may be a subset of the actual.
                     * The actual instances returned
                     * may not match exactly if we hit page size because there may be additional instances that were not included in the
                     * initial set, due to the initial set being limited by pageSize; the narrower search may pull in additional
                     * entities that were not discovered previously.
                     * This next assertion is just about the size of the result set.
                     */
                    assertCondition(  (  (!pageLimited && result.size() == expectedEntityCount) || (pageLimited && result.size() >= expectedEntityCount ) ),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                    /*
                     * Check that the expected entities were all returned
                     */
                    List<String> resultGUIDs = new ArrayList<>();
                    for (EntityDetail entity : result) {
                        resultGUIDs.add(entity.getGUID());
                    }
                    List<String> expectedGUIDs = propertyValueMap.get(attributeName).get(value);




                    /*
                     * Here again, we need to be sensitive to whether the original search hit the page limit.
                     * If the original search hit the limit then we may legitimately receive additional instances in the results
                     * of a narrower search. But not if the original result set was under the page limit.
                     */

                    boolean matchingResult = true;

                    if (!pageLimited) {
                        if (!resultGUIDs.containsAll(expectedGUIDs))
                            matchingResult = false;
                    }

                    else { // pageLimited, so need to allow for and verify hitherto unseen instances

                        for (EntityDetail entity : result) {

                            if (!(expectedGUIDs.contains(entity.getGUID()))) {
                                /*
                                 * This was an extra entity that we either did not expect or that we have not seen previously.
                                 * Check it is a valid result.
                                 */
                                InstanceProperties entityProperties = entity.getProperties();
                                InstancePropertyValue ipValue = entityProperties.getPropertyValue(attributeName);
                                if (ipValue != null) {
                                    InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE) {

                                        Object primitiveValue = ipValue.valueAsObject();

                                        if (!primitiveValue.equals(value))
                                            matchingResult = false;

                                    }
                                }
                            }
                        }
                    }


                    assertCondition(matchingResult,
                            assertion3,
                            testTypeName + assertionMsg3,
                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

                }
            }
        }



        /* ------------------------------------------------------------------------------------- */

        /*
         * Dual property findEntitiesByProperty() tests - search for instances using matchProperties containing pairs of primitive properties.
         * This tests logic applied to matchCriteria ALL, ANY & NONE.
         * Defined attributes only includes primitives.
         */

        if (!definedAttributeNames.isEmpty()) {

            for (String alphaAttributeName : definedAttributeNames) {


                Set<Object> possibleAlphaValues = propertyValueMap.get(alphaAttributeName).keySet();
                Iterator<Object> possibleAlphaValueIterator = possibleAlphaValues.iterator();
                while (possibleAlphaValueIterator.hasNext()) {

                    Object alphaValue = possibleAlphaValueIterator.next();

                    PrimitivePropertyValue alphaPpv = new PrimitivePropertyValue();
                    alphaPpv.setPrimitiveDefCategory(propertyCatMap.get(alphaAttributeName));
                    if (propertyCatMap.get(alphaAttributeName) == OM_PRIMITIVE_TYPE_STRING) {
                        String literalisedValue = literaliseStringProperty((String)alphaValue);
                        alphaPpv.setPrimitiveValue(literalisedValue);
                    }
                    else {
                        alphaPpv.setPrimitiveValue(alphaValue);
                    }


                    for (String betaAttributeName : definedAttributeNames) {

                        if (!alphaAttributeName.equals(betaAttributeName)) {

                            Set<Object> possibleBetaValues = propertyValueMap.get(betaAttributeName).keySet();
                            Iterator<Object> possibleBetaValueIterator = possibleBetaValues.iterator();
                            while (possibleBetaValueIterator.hasNext()) {

                                Object betaValue = possibleBetaValueIterator.next();

                                PrimitivePropertyValue betaPpv = new PrimitivePropertyValue();
                                betaPpv.setPrimitiveDefCategory(propertyCatMap.get(betaAttributeName));
                                if (propertyCatMap.get(betaAttributeName) == OM_PRIMITIVE_TYPE_STRING) {
                                    String literalisedValue = literaliseStringProperty((String)betaValue);
                                    betaPpv.setPrimitiveValue(literalisedValue);
                                }
                                else {
                                    betaPpv.setPrimitiveValue(betaValue);
                                }


                                InstanceProperties matchProperties = new InstanceProperties();
                                matchProperties.setProperty(alphaAttributeName, alphaPpv);
                                matchProperties.setProperty(betaAttributeName, betaPpv);



                                /*
                                 * Compute expected result
                                 */
                                List<String> alphaGUIDs = propertyValueMap.get(alphaAttributeName).get(alphaValue);
                                List<String> betaGUIDs = propertyValueMap.get(betaAttributeName).get(betaValue);


                                // ----------------------------------------------------------------
                                // MATCH_CRITERIA ANY

                                MatchCriteria matchCriteria = MatchCriteria.ANY;
                                List<String> expectedGUIDs = union(alphaGUIDs, betaGUIDs);     /* MatchCriteria.ANY ==> UNION */
                                int expectedEntityCount = expectedGUIDs.size();

                                /*
                                 * Search....
                                 */

                                result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                        entityDef.getGUID(),
                                        matchProperties,
                                        matchCriteria,
                                        fromElement,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        pageSize);



                                /*
                                 * It is reasonable to expect a non-null result - based on the way the search properties were constructed
                                 */
                                assertCondition((result != null),
                                        assertion4,
                                        testTypeName + assertionMsg4,
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

                                /*
                                 * Check that the expected number of entities was returned. This has to consider the effect of the original
                                 * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
                                 * But if the limit was hit (on the original search) then there may be additional instances in the repository
                                 * that were not seen on the original search; the expected result was computed from only thos instance that WERE seen,
                                 * so the expectation may be a subset of the actual.
                                 * The actual instances returned
                                 * may not match exactly if we hit page size because there may be additional instances that were not included in the
                                 * initial set, due to the initial set being limited by pageSize; the narrower search may pull in additional
                                 * entities that were not discovered previously.
                                 * This next assertion is just about the size of the result set.
                                 */
                                assertCondition(  (  (!pageLimited && result.size() == expectedEntityCount) || (pageLimited && result.size() >= expectedEntityCount ) ),
                                        assertion5,
                                        testTypeName + assertionMsg5,
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                                /*
                                 * Check that the expected entities were all returned
                                 */
                                List<String> resultGUIDs = new ArrayList<>();
                                for (EntityDetail entity : result) {
                                    resultGUIDs.add(entity.getGUID());
                                }

                                /*
                                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                                 * of a narrower search. But not if the original result set was under the page limit.
                                 */

                                boolean matchingResult = true;

                                if (!pageLimited) {
                                    if (!resultGUIDs.containsAll(expectedGUIDs))
                                        matchingResult = false;
                                }

                                else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                    for (EntityDetail entity : result) {

                                        if (!(expectedGUIDs.contains(entity.getGUID()))) {
                                            /*
                                             * This was an extra entity that we either did not expect or that we have not seen previously.
                                             * Check it is a valid result.
                                             */

                                            InstanceProperties entityProperties = entity.getProperties();

                                            boolean alphaMatch = false;

                                            InstancePropertyValue alphaIPValue = entityProperties.getPropertyValue(alphaAttributeName);
                                            if (alphaIPValue != null) {
                                                InstancePropertyCategory ipCategory = alphaIPValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    Object primitiveValue = alphaIPValue.valueAsObject();
                                                    alphaMatch = primitiveValue.equals(alphaValue);
                                                }
                                            }

                                            boolean betaMatch = false;

                                            InstancePropertyValue betaIPValue = entityProperties.getPropertyValue(betaAttributeName);
                                            if (betaIPValue != null) {
                                                InstancePropertyCategory ipCategory = betaIPValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    Object primitiveValue = betaIPValue.valueAsObject();
                                                    betaMatch = primitiveValue.equals(betaValue);
                                                }
                                            }


                                            if (! (alphaMatch || betaMatch) )
                                                matchingResult = false;


                                        }
                                    }
                                }


                                assertCondition(matchingResult,
                                        assertion6,
                                        testTypeName + assertionMsg6,
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());







                                // ----------------------------------------------------------------
                                // REPEAT FOR MATCH_CRITERIA ALL


                                matchCriteria = MatchCriteria.ALL;
                                expectedGUIDs = intersection(alphaGUIDs, betaGUIDs);     /* MatchCriteria.ALL ==> INTERSECTION */
                                expectedEntityCount = expectedGUIDs.size();

                                /*
                                 * Search....
                                 */

                                result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                        entityDef.getGUID(),
                                        matchProperties,
                                        matchCriteria,
                                        fromElement,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        pageSize);



                                /*
                                 * In this test it is not possible to always predict (expect) a non-null result, only if expectedEntityCount > 0
                                 */
                                assertCondition((expectedEntityCount == 0 || result != null),
                                        assertion7,
                                        testTypeName + assertionMsg7,
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                                /*
                                 * Since no result is legitimate in this test, only proceed with further checks where relevant
                                 */
                                if (result != null) {


                                    /*
                                     * Check that the expected number of entities was returned. This has to consider the effect of the original
                                     * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
                                     * But if the limit was hit (on the original search) then there may be additional instances in the repository
                                     * that were not seen on the original search; the expected result was computed from only thos instance that WERE seen,
                                     * so the expectation may be a subset of the actual.
                                     * The actual instances returned
                                     * may not match exactly if we hit page size because there may be additional instances that were not included in the
                                     * initial set, due to the initial set being limited by pageSize; the narrower search may pull in additional
                                     * entities that were not discovered previously.
                                     * This next assertion is just about the size of the result set.
                                     */
                                    assertCondition(  (  (!pageLimited && result.size() == expectedEntityCount) || (pageLimited && result.size() >= expectedEntityCount ) ),
                                            assertion8,
                                            testTypeName + assertionMsg8,
                                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                                    /*
                                     * Check that the expected entities were all returned
                                     */
                                    resultGUIDs = new ArrayList<>();
                                    for (EntityDetail entity : result) {
                                        resultGUIDs.add(entity.getGUID());
                                    }

                                    /*
                                     * Here again, we need to be sensitive to whether the original search hit the page limit.
                                     * If the original search hit the limit then we may legitimately receive additional instances in the results
                                     * of a narrower search. But not if the original result set was under the page limit.
                                     */

                                    matchingResult = true;

                                    if (!pageLimited) {
                                        if (!resultGUIDs.containsAll(expectedGUIDs))
                                            matchingResult = false;
                                    }

                                    else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                        for (EntityDetail entity : result) {

                                            if (!(expectedGUIDs.contains(entity.getGUID()))) {
                                                /*
                                                 * This was an extra entity that we either did not expect or that we have not seen previously.
                                                 * Check it is a valid result.
                                                 */

                                                InstanceProperties entityProperties = entity.getProperties();

                                                boolean alphaMatch = false;

                                                InstancePropertyValue alphaIPValue = entityProperties.getPropertyValue(alphaAttributeName);
                                                if (alphaIPValue != null) {
                                                    InstancePropertyCategory ipCategory = alphaIPValue.getInstancePropertyCategory();
                                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                        Object primitiveValue = alphaIPValue.valueAsObject();
                                                        alphaMatch = primitiveValue.equals(alphaValue);
                                                    }
                                                }

                                                boolean betaMatch = false;

                                                InstancePropertyValue betaIPValue = entityProperties.getPropertyValue(betaAttributeName);
                                                if (betaIPValue != null) {
                                                    InstancePropertyCategory ipCategory = betaIPValue.getInstancePropertyCategory();
                                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                        Object primitiveValue = betaIPValue.valueAsObject();
                                                        betaMatch = primitiveValue.equals(betaValue);
                                                    }
                                                }


                                                if (! (alphaMatch && betaMatch) )
                                                    matchingResult = false;


                                            }
                                        }
                                    }


                                    assertCondition(matchingResult,
                                            assertion9,
                                            testTypeName + assertionMsg9,
                                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                                }


                                // ----------------------------------------------------------------
                                // REPEAT FOR MATCH_CRITERIA NONE


                                matchCriteria = MatchCriteria.NONE;
                                /* MatchCriteria.NONE ==> UNION COMPLEMENT */
                                expectedGUIDs = diff(allKnownEntityGUIDs, alphaGUIDs);
                                expectedGUIDs = diff(expectedGUIDs, betaGUIDs);
                                expectedEntityCount = expectedGUIDs.size();

                                /*
                                 * Search....
                                 */

                                result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                        entityDef.getGUID(),
                                        matchProperties,
                                        matchCriteria,
                                        fromElement,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        pageSize);



                                /*
                                 * In this test it is not possible to always predict (expect) a non-null result, only if expectedEntityCount > 0
                                 */
                                assertCondition((expectedEntityCount == 0 || result != null),
                                        assertion10,
                                        testTypeName + assertionMsg10,
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

                                /*
                                 * Since no result is legitimate in this test, only proceed with further checks where relevant
                                 */
                                if (result != null) {

                                    /*
                                     * Check that the expected number of entities was returned. This has to consider the effect of the original
                                     * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
                                     * But if the limit was hit (on the original search) then there may be additional instances in the repository
                                     * that were not seen on the original search; the expected result was computed from only thos instance that WERE seen,
                                     * so the expectation may be a subset of the actual.
                                     * The actual instances returned
                                     * may not match exactly if we hit page size because there may be additional instances that were not included in the
                                     * initial set, due to the initial set being limited by pageSize; the narrower search may pull in additional
                                     * entities that were not discovered previously.
                                     * This next assertion is just about the size of the result set.
                                     */
                                    assertCondition(  (  (!pageLimited && result.size() == expectedEntityCount) || (pageLimited && result.size() >= expectedEntityCount ) ),
                                            assertion11,
                                            testTypeName + assertionMsg11,
                                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                                    /*
                                     * Check that the expected entities were all returned
                                     */
                                    resultGUIDs = new ArrayList<>();
                                    for (EntityDetail entity : result) {
                                        resultGUIDs.add(entity.getGUID());
                                    }

                                    /*
                                     * Here again, we need to be sensitive to whether the original search hit the page limit.
                                     * If the original search hit the limit then we may legitmately receive additional instances in the results
                                     * of a narrower search. But not if the original result set was under the page limit.
                                     */

                                    matchingResult = true;

                                    if (!pageLimited) {
                                        if (!resultGUIDs.containsAll(expectedGUIDs))
                                            matchingResult = false;
                                    }

                                    else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                        for (EntityDetail entity : result) {

                                            if (!(expectedGUIDs.contains(entity.getGUID()))) {
                                                /*
                                                 * This was an extra entity that we either did not expect or that we have not seen previously.
                                                 * Check it is a valid result.
                                                 */

                                                InstanceProperties entityProperties = entity.getProperties();

                                                boolean alphaMatch = false;

                                                InstancePropertyValue alphaIPValue = entityProperties.getPropertyValue(alphaAttributeName);
                                                if (alphaIPValue != null) {
                                                    InstancePropertyCategory ipCategory = alphaIPValue.getInstancePropertyCategory();
                                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                        Object primitiveValue = alphaIPValue.valueAsObject();
                                                        alphaMatch = primitiveValue.equals(alphaValue);
                                                    }
                                                }

                                                boolean betaMatch = false;

                                                InstancePropertyValue betaIPValue = entityProperties.getPropertyValue(betaAttributeName);
                                                if (betaIPValue != null) {
                                                    InstancePropertyCategory ipCategory = betaIPValue.getInstancePropertyCategory();
                                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                        Object primitiveValue = betaIPValue.valueAsObject();
                                                        betaMatch = primitiveValue.equals(betaValue);
                                                    }
                                                }


                                                if (! (!alphaMatch && !betaMatch) )
                                                    matchingResult = false;


                                            }
                                        }
                                    }


                                    assertCondition(matchingResult,
                                            assertion12,
                                            testTypeName + assertionMsg12,
                                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                                }
                            }
                        }
                    }
                }
            }
        }



        /* ------------------------------------------------------------------------------------- */

        /*
         * findEntitiesByPropertyValue() tests - search for instances using searchCriteria derived from known primitive string property values.
         *
         * In these tests, any string value is literalised.
         */

        if (!definedAttributeNames.isEmpty()) {

            for (String attributeName : definedAttributeNames) {

                if (propertyCatMap.get(attributeName) == OM_PRIMITIVE_TYPE_STRING) {

                    /*
                     * This is a string attribute....
                     */

                    Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
                    Iterator<Object> possibleValueIterator = possibleValues.iterator();
                    while (possibleValueIterator.hasNext()) {

                        String stringValue = (String) (possibleValueIterator.next());


                        String literalisedValue = literaliseStringProperty(stringValue);


                        /*
                         * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
                         */
                        int expectedEntityCount = propertyValueMap.get(attributeName).get(stringValue).size();

                        /*
                         * Search....
                         */

                        result = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                                entityDef.getGUID(),
                                literalisedValue,
                                fromElement,
                                null,
                                null,
                                null,
                                null,
                                null,
                                 pageSize);


                        /*
                         * It is reasonable to expect a non-null result - based on the way the search properties were constructed
                         */
                        assertCondition((result != null),
                                assertion13,
                                testTypeName + assertionMsg13,
                                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

                        /*
                         * Check that the expected number of entities was returned. This has to consider the effect of the original
                         * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
                         * But if the limit was hit (on the original search) then there may be additional instances in the repository
                         * that were not seen on the original search; the expected result was computed from only thos instance that WERE seen,
                         * so the expectation may be a subset of the actual.
                         * The actual instances returned
                         * may not match exactly if we hit page size because there may be additional instances that were not included in the
                         * initial set, due to the initial set being limited by pageSize; the narrower search may pull in additional
                         * entities that were not discovered previously.
                         * This next assertion is just about the size of the result set.
                         */
                        assertCondition(  (  (!pageLimited && result.size() == expectedEntityCount) || (pageLimited && result.size() >= expectedEntityCount ) ),
                                assertion14,
                                testTypeName + assertionMsg14,
                                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                        /*
                         * Check that the expected entities were all returned
                         */
                        List<String> resultGUIDs = new ArrayList<>();
                        for (EntityDetail entity : result) {
                            resultGUIDs.add(entity.getGUID());
                        }
                        List<String> expectedGUIDs = propertyValueMap.get(attributeName).get(stringValue);


                        /*
                         * Here again, we need to be sensitive to whether the original search hit the page limit.
                         * If the original search hit the limit then we may legitimately receive additional instances in the results
                         * of a narrower search. But not if the original result set was under the page limit.
                         */

                        boolean matchingResult = true;

                        if (!pageLimited) {
                            if (!resultGUIDs.containsAll(expectedGUIDs))
                                matchingResult = false;
                        }

                        else { // pageLimited, so need to allow for and verify hitherto unseen instances

                            for (EntityDetail entity : result) {

                                if (!(expectedGUIDs.contains(entity.getGUID()))) {
                                    /*
                                     * This was an extra entity that we either did not expect or that we have not seen previously.
                                     * Check it is a valid result. It can have any string attribute with the same value as strValue.
                                     */
                                    boolean validEntity = false;
                                    InstanceProperties entityProperties = entity.getProperties();
                                    if (entityProperties != null) {
                                        Set<String> entityPropertyNames = entityProperties.getInstanceProperties().keySet();
                                        Iterator<String> entityPropertyNameIterator = entityPropertyNames.iterator();
                                        while (entityPropertyNameIterator.hasNext()) {
                                            String propertyName = entityPropertyNameIterator.next();
                                            InstancePropertyValue ipValue = entityProperties.getPropertyValue(attributeName);
                                            if (ipValue != null) {
                                                InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipValue;
                                                    PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                                                    if (pdCat == OM_PRIMITIVE_TYPE_STRING) {
                                                        String propertyValueAsString = (String) (ppv.getPrimitiveValue());
                                                        if (propertyValueAsString.equals(stringValue)) {
                                                            validEntity = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!validEntity)
                                        matchingResult = false;
                                }
                            }
                        }


                        assertCondition(matchingResult,
                                assertion15,
                                testTypeName + assertionMsg15,
                                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());


                    }
                }
            }
        }


        /*
         * Completion of searches - indicate success of testcase.
         */
        super.setSuccessMessage("Entities can be searched by property and property value");
    }




    /*
     * Return the union of the two lists
     */
    private List<String> union(List<String> l1, List<String> l2) {
        List<String> l1Copy;
        List<String> l2Copy;
        if (l1 != null)
            l1Copy = new ArrayList<>(l1);
        else
            l1Copy = new ArrayList<>();
        if (l2 != null)
            l2Copy = new ArrayList<>(l2);
        else
            l2Copy = new ArrayList<>();
        l2Copy.removeAll(l1);
        l1Copy.addAll(l2Copy);
        return l1Copy;
    }

    /*
     * Return the intersection of the two lists
     */
    private List<String> intersection(List<String> l1, List<String> l2) {
        if (l1 == null || l1.isEmpty() || l2 == null || l2.isEmpty())
            return new ArrayList<>();
        List<String> li = new ArrayList<>();
        for (String s : l1) {
            if (l2.contains(s)) {
                li.add(s);
            }
        }
        return li;
    }

    /*
     * Return the difference of the two lists. This means any member in the
     * first list that is NOT in the second list.
     */
    private List<String> diff(List<String> lu, List<String> ld) {
        if (lu == null)
            return new ArrayList<>();

        List<String> comp = new ArrayList<>(lu);
        if (ld != null) {
            for (String s : ld) {
                if (comp.contains(s)) {
                    comp.remove(s);
                }
            }
        }
        return comp;
    }




    /**
     * Return type def attributes for the properties defined in the TypeDef and all of its supertypes
     *
     * @param userId calling user
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     * @throws Exception problem manipulating types
     */
    protected List<TypeDefAttribute>  getAllPropertiesForTypedef(String userId, TypeDef typeDef) throws Exception
    {


        // Recursively gather all the TypeDefAttributes for the supertype hierarchy...
        List<TypeDefAttribute> allTypeDefAttributes = getPropertiesForTypeDef(userId, typeDef);


        return allTypeDefAttributes;

    }





    /*
     *  Method to literalise a string value for exact match.
     *
     */
    public String literaliseStringProperty(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        String litValue = repositoryHelper.getExactMatchRegex(value);
        return litValue;
    }



    /*
     *  Method to find all the string values and literalise them for exact match.
     *  The InstanceProperties passed in should already have been populated with the desired test values.
     */
    public InstanceProperties literaliseMatchProperties(String userId, TypeDef typeDef, InstanceProperties matchProperties)
    {

        if (matchProperties == null)
            return null;



        Map<String, InstancePropertyValue> properties = matchProperties.getInstanceProperties();


        /*
         * Get the trivial case out of the way - whatecver we were passed - pass it back
         */
        if (properties == null)
            return matchProperties;


        try {


            /*
             * Clone the match properties to a new IP object, iterate over the matchProperties and for any that are primitive string type call the repo helper and set the literalised value in the cloned properties object
             */
            InstanceProperties literalisedInstanceProperties = new InstanceProperties(matchProperties);

            OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();

            Iterator<String> propertyNames = matchProperties.getPropertyNames();

            if (propertyNames != null) {
                while (propertyNames.hasNext()) {
                    String propertyName = propertyNames.next();
                    InstancePropertyValue instancePropertyValue = matchProperties.getPropertyValue(propertyName);

                    InstancePropertyCategory ipCat = instancePropertyValue.getInstancePropertyCategory();
                    if (ipCat == InstancePropertyCategory.PRIMITIVE) {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) instancePropertyValue;
                        PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                        if (pdCat == OM_PRIMITIVE_TYPE_STRING) {
                            PrimitivePropertyValue newPpv = new PrimitivePropertyValue(ppv);
                            // Literalise the string
                            String currentValue = (String) ppv.getPrimitiveValue();
                            String litValue = repositoryHelper.getExactMatchRegex(currentValue);
                            newPpv.setPrimitiveValue(litValue);
                            literalisedInstanceProperties.setProperty(propertyName, newPpv);
                        }

                    }
                }

            }
            return literalisedInstanceProperties;

        }
        catch (Exception e) {
            return null;  // This should force an InvalidParameterException from the MDC under test.
        }

    }


}
