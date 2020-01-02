/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
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
 * Test that all defined relationships can be retrieved by property searches.
 *
 * This testcase does not assume that the repository under test supports the METADATA_MAINTENANCE profile -
 * i.e. it does not necessarily support the creation of instances via the OMRS API. This testcase therefore
 * does not use addRelationship to create sets of test instances. Instead, the testcase relies on the repository
 * already containing instances that can be searched and retrieved.
 *
 * The testcase starts by performing a wide search (within the specified relationship type) and then uses the
 * discovered properties to perform narrow subsequent searches and inspect the returned result set.
 *
 * The values of string properties are left 'raw' for the early tests and are literalised by the
 * repository helper methods in the later, findEntitiesByPropertyValue() tests. This is to ensure that
 * both styes of use operate correctly.
 *
 */

public class TestSupportedRelationshipSharingPropertySearch extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-relationship-sharing-property-search";
    private static final String testCaseName = "Repository relationship sharing property search test case";

    /* Type */

    private static final String assertion0 = testCaseId + "-00";
    private static final String assertionMsg0 = " relationship type definition matches known type  ";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " repository supports optional relationship search functions.";

    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " positive search contained expected number of results.";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " positive search contained expected results.";

    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " negative search returned results.";
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " negative search contained expected number of results.";
    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " negative search contained expected results.";

    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " dual property ANY search returned results.";
    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " dual property ANY search contained expected number of results.";
    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " dual property ANY search contained expected results.";

    private static final String assertion10     = testCaseId + "-10";
    private static final String assertionMsg10  = " dual property ALL search returned results.";
    private static final String assertion11     = testCaseId + "-11";
    private static final String assertionMsg11  = " dual property ALL search contained expected number of results.";
    private static final String assertion12     = testCaseId + "-12";
    private static final String assertionMsg12  = " dual property ALL search contained expected results.";

    private static final String assertion13     = testCaseId + "-13";
    private static final String assertionMsg13  = " dual property NONE  search returned results.";
    private static final String assertion14     = testCaseId + "-14";
    private static final String assertionMsg14  = " dual property NONE  search contained expected number of results.";
    private static final String assertion15     = testCaseId + "-15";
    private static final String assertionMsg15  = " dual property NONE  search contained expected results.";

    private static final String assertion16     = testCaseId + "-16";
    private static final String assertionMsg16  = " exact search returned results.";
    private static final String assertion17     = testCaseId + "-17";
    private static final String assertionMsg17  = " exact search contained expected number of results.";
    private static final String assertion18     = testCaseId + "-18";
    private static final String assertionMsg18  = " exact search contained expected results.";

    private static final String assertion19     = testCaseId + "-19";
    private static final String assertionMsg19  = " prefix search returned results.";
    private static final String assertion20     = testCaseId + "-20";
    private static final String assertionMsg20  = " prefix search contained expected number of results.";
    private static final String assertion21     = testCaseId + "-21";
    private static final String assertionMsg21  = " prefix search contained expected results.";

    private static final String assertion22     = testCaseId + "-22";
    private static final String assertionMsg22  = " suffix search returned results.";
    private static final String assertion23     = testCaseId + "-23";
    private static final String assertionMsg23  = " suffix search contained expected number of results.";
    private static final String assertion24     = testCaseId + "-24";
    private static final String assertionMsg24  = " suffix search contained expected results.";

    private static final String assertion25     = testCaseId + "-25";
    private static final String assertionMsg25  = " contains search returned results.";
    private static final String assertion26     = testCaseId + "-26";
    private static final String assertionMsg26  = " contains search contained expected number of results.";
    private static final String assertion27     = testCaseId + "-27";
    private static final String assertionMsg27  = " contains search contained expected results.";





    private RepositoryConformanceWorkPad  workPad;
    private String                        metadataCollectionId;
    private RelationshipDef               relationshipDef;
    private Map<String, EntityDef>        entityDefs;
    private List<TypeDefAttribute>        attrList;
    private String                        testTypeName;

    private boolean                       multiSetTest = false;
    private String                        firstStringAttributeName = null;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDefs      entities to test
     * @param relationshipDef type of valid relationships
     */
    public TestSupportedRelationshipSharingPropertySearch(RepositoryConformanceWorkPad workPad,
                                                          Map<String, EntityDef>       entityDefs,
                                                          RelationshipDef              relationshipDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        this.workPad               = workPad;
        this.metadataCollectionId  = workPad.getTutMetadataCollectionId();
        this.relationshipDef       = relationshipDef;
        this.entityDefs            = entityDefs;
        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(), testCaseId, testCaseName);

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
         * Perform a broad search for instances of the specified relationship type.
         *
         */


        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();



        /*
         * Check that the relationship type matches the known type from the repository helper.
         *
         * The entity types used by the ends are not verified on this test - they are verified in the supported entity tests
         */
        OMRSRepositoryConnector cohortRepositoryConnector = null;
        OMRSRepositoryHelper repositoryHelper = null;
        if (workPad != null) {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
            repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        }

        RelationshipDef knownRelationshipDef = (RelationshipDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), relationshipDef.getName());
        verifyCondition((relationshipDef.equals(knownRelationshipDef)),
                assertion0,
                testTypeName + assertionMsg0,
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());




        /*
         * Construct a list of TypeDefAttributes that are specified by the known Relationship type
         */

        this.attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(), relationshipDef);

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
         * This initial search uses findRelationshipsByproperty() with an empty match properties object and matchCriteria ANY
         * The result can be compared to a findRelationshipsBypropertyValue() with an totally wild regular expression for searchCriteria.
         *
         * This testcase does not perform general regular expression searches; they are in the corresponding advanced-search testcase.
         */



        InstanceProperties emptyMatchProperties    = new InstanceProperties();
        int                fromElement             = 0;
        int                pageSize                = getMaxSearchResults();
        List<Relationship> result                  = null;
        int                instanceCount           = 0;
        boolean            pageLimited             = false;

        /* ------------------------------------------------------------------------------------- */

        /*
         *  Use emptyMatchProperties and matchCriteria ANY   - this should return all relationships of the current type
         */


        try {
            result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                    relationshipDef.getGUID(),
                                                                    emptyMatchProperties,
                                                                    MatchCriteria.ANY,
                                                                    fromElement,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    pageSize);

        }
        catch (FunctionNotSupportedException exception) {

            /*
             * If running against a repository/connector that does not support relationship searches
             * report that the optional cpability is not supported and give up on the test.
             */

            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

            return;

        }

        if (result == null) {
            /*
             * There are no instances of this type reported by the repository.
             * This is not an error or failure - it could be that the repository contains no instances.
             * Quietly give up the test for this type.
             */
            return;

        }


        /*
         * Record the total instance count and the overall set of discovered relationships. There may be more relationships
         * that were not returned due to pageSize but that is allowed for in the tests below.
         */
        instanceCount = result.size();
        if (instanceCount == pageSize)
            pageLimited = true;

        List<String> allKnownRelationshipGUIDs = new ArrayList<>();
        for (Relationship relationship : result) {
            allKnownRelationshipGUIDs.add(relationship.getGUID());
        }



        /*
         * Construct a reverse index of relationship GUIDs by property name and property value.
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

                for (Relationship relationship : result) {
                    InstanceProperties relationshipProperties = relationship.getProperties();
                    if (relationshipProperties != null) {
                        InstancePropertyValue ipValue = relationshipProperties.getPropertyValue(attrName);
                        if (ipValue != null) {
                            InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                            if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                Object primitiveValue = ipValue.valueAsObject();
                                if (valueMap.get(primitiveValue) == null) {
                                    List<String> newList = new ArrayList<>();
                                    valueMap.put(primitiveValue, newList);
                                }
                                List<String> relationshipGUIDs = valueMap.get(primitiveValue);
                                relationshipGUIDs.add(relationship.getGUID());
                            }
                        }
                    }
                }
            }
        }



        /* ------------------------------------------------------------------------------------- */

        /*
         * Single property findRelationshipsByProperty() tests - search for instances using matchProperties containing each individual primitive property.
         * Defined attributes only includes primitives.
         */

        if (!definedAttributeNames.isEmpty()) {

            for (String attributeName : definedAttributeNames) {

                Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
                Iterator<Object> possibleValueIterator = possibleValues.iterator();
                /*
                 * Perform a search using the first discovered value for the property
                 */
                if (possibleValueIterator.hasNext()) {

                    Object value = possibleValueIterator.next();

                    InstanceProperties matchProperties = new InstanceProperties();

                    PrimitivePropertyValue ppv = new PrimitivePropertyValue();
                    ppv.setPrimitiveDefCategory(propertyCatMap.get(attributeName));
                    if (propertyCatMap.get(attributeName) == OM_PRIMITIVE_TYPE_STRING) {
                        String literalisedValue = literaliseStringProperty((String) value);
                        ppv.setPrimitiveValue(literalisedValue);
                    } else {
                        ppv.setPrimitiveValue(value);
                    }

                    ppv.setPrimitiveValue(value);
                    matchProperties.setProperty(attributeName, ppv);

                    /*
                     * Expected result size
                     */
                    int expectedRelationshipCount = propertyValueMap.get(attributeName).get(value).size();

                    /*
                     * Search....
                     */

                    try {

                        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                                relationshipDef.getGUID(),
                                                                                matchProperties,
                                                                                MatchCriteria.ALL,
                                                                                fromElement,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                pageSize);
                    }
                    catch (FunctionNotSupportedException exception) {

                        /*
                         * If running against a repository/connector that does not support relationship searches
                         * report that the optional cpability is not supported and give up on the test.
                         */

                        super.addNotSupportedAssertion(assertion1,
                                                       assertionMsg1,
                                                       RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                       RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                        return;

                    }

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
                     * Check that the expected number of relationships was returned. This has to consider the effect of the original
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
                    int resultCount = result == null ? 0 : result.size();
                    assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                                    assertion2,
                                    testTypeName + assertionMsg2,
                                    RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                    RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


                    /*
                     * Check that the expected relationships were all returned
                     */
                    if (result != null) {
                        List<String> resultGUIDs = new ArrayList<>();
                        for (Relationship relationship : result) {
                            resultGUIDs.add(relationship.getGUID());
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
                        } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                            for (Relationship relationship : result) {

                                if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                    /*
                                     * This was an extra entity that we either did not expect or that we have not seen previously.
                                     * Check it is a valid result.
                                     */
                                    InstanceProperties relationshipProperties = relationship.getProperties();
                                    if (relationshipProperties != null) {
                                        InstancePropertyValue ipValue = relationshipProperties.getPropertyValue(attributeName);
                                        if (ipValue != null) {
                                            InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                            if (ipCategory == InstancePropertyCategory.PRIMITIVE) {

                                                Object primitiveValue = ipValue.valueAsObject();

                                                /*
                                                 * Check for inequality and fail the match if unequal.
                                                 * This is because, even for strings, we used an exact match literalised property value
                                                 * and match criteria was ALL - so a relationship with an unequal property is not a valid result.
                                                 */
                                                if (!primitiveValue.equals(value))
                                                    matchingResult = false;

                                            }
                                        }
                                    }
                                }
                            }
                        }


                        assertCondition(matchingResult,
                                        assertion3,
                                        testTypeName + assertionMsg3,
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());
                    }

                    // ----------------------------------------------------
                    // REPEAT SEARCH WITH MATCHCRITERIA NONE

                    /*
                     * Second search with MatchCriteria.NONE
                     */

                    /*
                     * Expected result size
                     */
                    int numRelationshipsWithValue = propertyValueMap.get(attributeName).get(value).size();
                    expectedRelationshipCount = instanceCount - numRelationshipsWithValue;

                    /*
                     * Search....
                     */

                    try {
                        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                                relationshipDef.getGUID(),
                                                                                matchProperties,
                                                                                MatchCriteria.NONE,
                                                                                fromElement,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                pageSize);
                    }
                    catch (FunctionNotSupportedException exception) {

                        /*
                         * If running against a repository/connector that does not support relationship searches
                         * report that the optional cpability is not supported and give up on the test.
                         */

                        super.addNotSupportedAssertion(assertion1,
                                                       assertionMsg1,
                                                       RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                       RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                        return;

                    }

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
                     * Check that the expected number of relationships was returned. This has to consider the effect of the original
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
                    resultCount = result == null ? 0 : result.size();
                    assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                                    assertion5,
                                    testTypeName + assertionMsg5,
                                    RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                    RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


                    /*
                     * Check that the expected relationships were all returned
                     */
                    if (result != null) {
                        List<String> resultGUIDs = new ArrayList<>();
                        for (Relationship relationship : result) {
                            resultGUIDs.add(relationship.getGUID());
                        }
                        List<String> expectedGUIDs = diff(allKnownRelationshipGUIDs, propertyValueMap.get(attributeName).get(value));


                        /*
                         * Here again, we need to be sensitive to whether the original search hit the page limit.
                         * If the original search hit the limit then we may legitimately receive additional instances in the results
                         * of a narrower search. But not if the original result set was under the page limit.
                         */

                        boolean matchingResult = true;

                        if (!pageLimited) {
                            if (!resultGUIDs.containsAll(expectedGUIDs))
                                matchingResult = false;
                        } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                            for (Relationship relationship : result) {

                                if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                    /*
                                     * This was an extra entity that we either did not expect or that we have not seen previously.
                                     * Check it is a valid result.
                                     */
                                    InstanceProperties relationshipProperties = relationship.getProperties();
                                    if (relationshipProperties != null) {
                                        InstancePropertyValue ipValue = relationshipProperties.getPropertyValue(attributeName);
                                        if (ipValue != null) {
                                            InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                            if (ipCategory == InstancePropertyCategory.PRIMITIVE) {

                                                Object primitiveValue = ipValue.valueAsObject();

                                                /*
                                                 * Check for equality and fail the match if equal.
                                                 * This is because, even for strings, we used an exact match literalised property value
                                                 * and match criteria was NONE - so a relationship with an equal property is not a valid result.
                                                 */
                                                if (primitiveValue.equals(value))
                                                    matchingResult = false;

                                            }
                                        }
                                    }
                                }
                            }
                        }


                        assertCondition(matchingResult,
                                        assertion6,
                                        testTypeName + assertionMsg6,
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

                    }
                }
            }
        }



        /* ------------------------------------------------------------------------------------- */

        /*
         * Dual property findRelationshipsByProperty() tests - search for instances using matchProperties containing pairs of primitive properties.
         * This tests logic applied to matchCriteria ALL, ANY & NONE.
         * Defined attributes only includes primitives.
         */

        if (!definedAttributeNames.isEmpty()) {


            /*
             * Pick one pair of properties for dual property tests - if there are less than two properties skip
             */

            Set<String> propertyNameSet = propertyValueMap.keySet();
            if (propertyNameSet.size() >= 2) {

                String alphaAttributeName = null;
                String betaAttributeName = null;

                for (String attributeName : definedAttributeNames) {

                    Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
                    Iterator<Object> possibleValueIterator = possibleValues.iterator();
                    /*
                     * Perform a search using the first discovered value for the property
                     */
                    if (possibleValueIterator.hasNext()) {

                        alphaAttributeName = attributeName;
                        break;

                    }
                }
                for (String attributeName : definedAttributeNames) {

                    if (!attributeName.equals(alphaAttributeName)) {

                        Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
                        Iterator<Object> possibleValueIterator = possibleValues.iterator();
                        /*
                         * Perform a search using the first discovered value for the property
                         */
                        if (possibleValueIterator.hasNext()) {

                            betaAttributeName = attributeName;
                            break;

                        }
                    }
                }
                if (alphaAttributeName != null && betaAttributeName != null) {

                    PrimitivePropertyValue alphaPpv = null;
                    PrimitivePropertyValue betaPpv = null;
                    Object alphaValue = null;
                    Object betaValue = null;

                    /*
                     * Select first available value for alphaAttribute
                     */
                    Set<Object> possibleAlphaValues = propertyValueMap.get(alphaAttributeName).keySet();
                    Iterator<Object> possibleAlphaValueIterator = possibleAlphaValues.iterator();

                    if (possibleAlphaValueIterator.hasNext()) {

                        alphaValue = possibleAlphaValueIterator.next();

                        alphaPpv = new PrimitivePropertyValue();
                        alphaPpv.setPrimitiveDefCategory(propertyCatMap.get(alphaAttributeName));
                        if (propertyCatMap.get(alphaAttributeName) == OM_PRIMITIVE_TYPE_STRING) {
                            String literalisedValue = literaliseStringProperty((String) alphaValue);
                            alphaPpv.setPrimitiveValue(literalisedValue);
                        } else {
                            alphaPpv.setPrimitiveValue(alphaValue);
                        }
                    }
                    /*
                     * Select first available value for betaAttribute
                     */
                    Set<Object> possibleBetaValues = propertyValueMap.get(betaAttributeName).keySet();
                    Iterator<Object> possibleBetaValueIterator = possibleBetaValues.iterator();

                    if (possibleBetaValueIterator.hasNext()) {

                        betaValue = possibleBetaValueIterator.next();


                        betaPpv = new PrimitivePropertyValue();
                        betaPpv.setPrimitiveDefCategory(propertyCatMap.get(betaAttributeName));
                        if (propertyCatMap.get(betaAttributeName) == OM_PRIMITIVE_TYPE_STRING) {
                            String literalisedValue = literaliseStringProperty((String) betaValue);
                            betaPpv.setPrimitiveValue(literalisedValue);
                        } else {
                            betaPpv.setPrimitiveValue(betaValue);
                        }

                    }

                    if (alphaPpv != null && alphaValue != null && betaPpv != null && betaValue != null) {


                        InstanceProperties matchProperties = new InstanceProperties();
                        matchProperties.setProperty(alphaAttributeName, alphaPpv);
                        matchProperties.setProperty(betaAttributeName, betaPpv);



                        /*
                         * Compute expected result
                         */
                        List<String> alphaGUIDs = propertyValueMap.get(alphaAttributeName).get(alphaValue);
                        List<String> betaGUIDs = propertyValueMap.get(betaAttributeName).get(betaValue);


                        MatchCriteria matchCriteria = MatchCriteria.ANY;
                        List<String> expectedGUIDs = union(alphaGUIDs, betaGUIDs);     /* MatchCriteria.ANY ==> UNION */
                        int expectedRelationshipCount = expectedGUIDs.size();

                        /*
                         * Search....
                         */

                        try {
                            result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                                    relationshipDef.getGUID(),
                                                                                    matchProperties,
                                                                                    matchCriteria,
                                                                                    fromElement,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    pageSize);
                        }
                        catch (FunctionNotSupportedException exception) {

                            /*
                             * If running against a repository/connector that does not support relationship searches
                             * report that the optional cpability is not supported and give up on the test.
                             */

                            super.addNotSupportedAssertion(assertion1,
                                                           assertionMsg1,
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                            return;

                        }


                        /*
                         * Check that the expected number of instances was returned. This has to consider the effect of the original
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
                        int resultCount = result == null ? 0 : result.size();
                        assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                                        assertion8,
                                        testTypeName + assertionMsg8,
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


                        /*
                         * Check that the expected relationships were all returned
                         */
                        if (result != null) {
                            List<String> resultGUIDs = new ArrayList<>();
                            for (Relationship relationship : result) {
                                resultGUIDs.add(relationship.getGUID());
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
                            } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                for (Relationship relationship : result) {

                                    if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                        /*
                                         * This was an extra relationship that we either did not expect or that we have not seen previously.
                                         * Check it is a valid result.
                                         */

                                        InstanceProperties relationshipProperties = relationship.getProperties();

                                        boolean alphaMatch = false;

                                        if (relationshipProperties != null) {

                                            InstancePropertyValue alphaIPValue = relationshipProperties.getPropertyValue(alphaAttributeName);
                                            if (alphaIPValue != null) {
                                                InstancePropertyCategory ipCategory = alphaIPValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    Object primitiveValue = alphaIPValue.valueAsObject();
                                                    alphaMatch = primitiveValue.equals(alphaValue);
                                                }
                                            }

                                        }

                                        boolean betaMatch = false;

                                        if (relationshipProperties != null) {

                                            InstancePropertyValue betaIPValue = relationshipProperties.getPropertyValue(betaAttributeName);
                                            if (betaIPValue != null) {
                                                InstancePropertyCategory ipCategory = betaIPValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    Object primitiveValue = betaIPValue.valueAsObject();
                                                    betaMatch = primitiveValue.equals(betaValue);
                                                }
                                            }

                                        }


                                        if (!(alphaMatch || betaMatch))
                                            matchingResult = false;


                                    }
                                }
                            }


                            assertCondition(matchingResult,
                                            assertion9,
                                            testTypeName + assertionMsg9,
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());
                        }

                        // ----------------------------------------------------------------
                        // REPEAT FOR MATCH_CRITERIA ALL


                        matchCriteria = MatchCriteria.ALL;
                        expectedGUIDs = intersection(alphaGUIDs, betaGUIDs);     /* MatchCriteria.ALL ==> INTERSECTION */
                        expectedRelationshipCount = expectedGUIDs.size();

                        /*
                         * Search....
                         */

                        try {
                            result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                                    relationshipDef.getGUID(),
                                                                                    matchProperties,
                                                                                    matchCriteria,
                                                                                    fromElement,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    pageSize);

                        }
                        catch (FunctionNotSupportedException exception) {

                            /*
                             * If running against a repository/connector that does not support relationship searches
                             * report that the optional cpability is not supported and give up on the test.
                             */

                            super.addNotSupportedAssertion(assertion1,
                                                           assertionMsg1,
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                            return;

                        }




                        /*
                         * Check that the expected number of instances was returned. This has to consider the effect of the original
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
                        resultCount = result == null ? 0 : result.size();
                        assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                                        assertion11,
                                        testTypeName + assertionMsg11,
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


                        /*
                         * Check that the expected relationships were all returned
                         */
                        if (result != null) {
                            List<String> resultGUIDs = new ArrayList<>();
                            for (Relationship relationship : result) {
                                resultGUIDs.add(relationship.getGUID());
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
                            } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                for (Relationship relationship : result) {

                                    if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                        /*
                                         * This was an extra relationship that we either did not expect or that we have not seen previously.
                                         * Check it is a valid result.
                                         */

                                        InstanceProperties relationshipProperties = relationship.getProperties();

                                        boolean alphaMatch = false;

                                        if (relationshipProperties != null) {

                                            InstancePropertyValue alphaIPValue = relationshipProperties.getPropertyValue(alphaAttributeName);
                                            if (alphaIPValue != null) {
                                                InstancePropertyCategory ipCategory = alphaIPValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    Object primitiveValue = alphaIPValue.valueAsObject();
                                                    alphaMatch = primitiveValue.equals(alphaValue);
                                                }
                                            }

                                        }

                                        boolean betaMatch = false;

                                        if (relationshipProperties != null) {

                                            InstancePropertyValue betaIPValue = relationshipProperties.getPropertyValue(betaAttributeName);
                                            if (betaIPValue != null) {
                                                InstancePropertyCategory ipCategory = betaIPValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    Object primitiveValue = betaIPValue.valueAsObject();
                                                    betaMatch = primitiveValue.equals(betaValue);
                                                }
                                            }
                                        }

                                        if (!(alphaMatch && betaMatch))
                                            matchingResult = false;


                                    }
                                }
                            }


                            assertCondition(matchingResult,
                                            assertion12,
                                            testTypeName + assertionMsg12,
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());
                        }


                        // ----------------------------------------------------------------
                        // REPEAT FOR MATCH_CRITERIA NONE


                        matchCriteria = MatchCriteria.NONE;
                        /* MatchCriteria.NONE ==> UNION COMPLEMENT */
                        expectedGUIDs = diff(allKnownRelationshipGUIDs, alphaGUIDs);
                        expectedGUIDs = diff(expectedGUIDs, betaGUIDs);
                        expectedRelationshipCount = expectedGUIDs.size();

                        /*
                         * Search....
                         */

                        try {
                            result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                                    relationshipDef.getGUID(),
                                                                                    matchProperties,
                                                                                    matchCriteria,
                                                                                    fromElement,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    pageSize);
                        }
                        catch (FunctionNotSupportedException exception) {

                            /*
                             * If running against a repository/connector that does not support relationship searches
                             * report that the optional cpability is not supported and give up on the test.
                             */

                            super.addNotSupportedAssertion(assertion1,
                                                           assertionMsg1,
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                            return;

                        }



                        /*
                         * Check that the expected number of instances was returned. This has to consider the effect of the original
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
                        resultCount = result == null ? 0 : result.size();
                        assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                                        assertion14,
                                        testTypeName + assertionMsg14,
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


                        /*
                         * Check that the expected relationships were all returned
                         */
                        if (result != null) {
                            List<String> resultGUIDs = new ArrayList<>();
                            for (Relationship relationship : result) {
                                resultGUIDs.add(relationship.getGUID());
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
                            } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                for (Relationship relationship : result) {

                                    if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                        /*
                                         * This was an extra relationship that we either did not expect or that we have not seen previously.
                                         * Check it is a valid result.
                                         */

                                        InstanceProperties relationshipProperties = relationship.getProperties();

                                        boolean alphaMatch = false;

                                        if (relationshipProperties != null) {

                                            InstancePropertyValue alphaIPValue = relationshipProperties.getPropertyValue(alphaAttributeName);
                                            if (alphaIPValue != null) {
                                                InstancePropertyCategory ipCategory = alphaIPValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    Object primitiveValue = alphaIPValue.valueAsObject();
                                                    alphaMatch = primitiveValue.equals(alphaValue);
                                                }
                                            }
                                        }

                                        boolean betaMatch = false;

                                        if (relationshipProperties != null) {

                                            InstancePropertyValue betaIPValue = relationshipProperties.getPropertyValue(betaAttributeName);
                                            if (betaIPValue != null) {
                                                InstancePropertyCategory ipCategory = betaIPValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    Object primitiveValue = betaIPValue.valueAsObject();
                                                    betaMatch = primitiveValue.equals(betaValue);
                                                }
                                            }
                                        }


                                        if (!(!alphaMatch && !betaMatch))
                                            matchingResult = false;


                                    }
                                }
                            }


                            assertCondition(matchingResult,
                                            assertion15,
                                            testTypeName + assertionMsg15,
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());
                        }
                    }
                }
            }
        }



        /* ------------------------------------------------------------------------------------- */

        /*
         * findRelationshipsByPropertyValue() tests - search for instances using searchCriteria derived from known primitive string property values.
         *
         * In these tests, any string value is literalised.
         */

        if (!definedAttributeNames.isEmpty()) {

            String attributeName = null;

            for (String testAttributeName : definedAttributeNames) {

                if (propertyCatMap.get(testAttributeName) == OM_PRIMITIVE_TYPE_STRING) {

                    /*
                     * This is a string attribute....
                     */
                    attributeName = testAttributeName;
                    break;
                }
            }

            if (attributeName != null) {

                /*
                 * The given attribute (only) is tested for exact, prefix, suffix and contains matches for each of the values already seen.
                 * All these searches should return at least some instances in the result
                 */

                Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
                Iterator<Object> possibleValueIterator = possibleValues.iterator();
                while (possibleValueIterator.hasNext()) {

                    String stringValue = (String) (possibleValueIterator.next());

                    /*
                     * EXACT MATCH
                     */

                    String literalisedValue = literaliseStringPropertyExact(stringValue);


                    /*
                     * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
                     * Find all the values (regardless of attributeName) in the map that are an exact match to the search value
                     */
                    int expectedRelationshipCount = 0;
                    List<String> expectedGUIDs = new ArrayList<>();
                    Set<String> propertyNamesSet = propertyValueMap.keySet();
                    Iterator<String> propertyNamesSetIterator = propertyNamesSet.iterator();
                    while (propertyNamesSetIterator.hasNext()) {
                        String propName = propertyNamesSetIterator.next();
                        if (propertyCatMap.get(propName) == OM_PRIMITIVE_TYPE_STRING) {
                            Map<Object,List<String>> propValues = propertyValueMap.get(propName);
                            Set<Object> propertyValuesSet = propValues.keySet();
                            Iterator<Object> propertyValuesSetIterator = propertyValuesSet.iterator();
                            while (propertyValuesSetIterator.hasNext()) {
                                String knownStringValue = (String)(propertyValuesSetIterator.next());
                                /* EXACT MATCH */
                                if (stringValue.equals(knownStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    expectedRelationshipCount = expectedGUIDs.size();

                    /*
                     * Search....
                     */

                    try {
                        result = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                                                                                     relationshipDef.getGUID(),
                                                                                     literalisedValue,
                                                                                     fromElement,
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     pageSize);

                    }
                    catch (FunctionNotSupportedException exception) {

                        /*
                         * If running against a repository/connector that does not support relationship searches
                         * report that the optional cpability is not supported and give up on the test.
                         */

                        super.addNotSupportedAssertion(assertion1,
                                                       assertionMsg1,
                                                       RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                       RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                        return;

                    }



                    /*
                     * Check that the expected number of instances was returned. This has to consider the effect of the original
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
                    int resultCount = result == null ? 0 : result.size();
                    assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                            assertion17,
                            testTypeName + assertionMsg17,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());


                    /*
                     * Check that the expected relationships were all returned
                     */
                    if (result != null) {
                        List<String> resultGUIDs = new ArrayList<>();
                        for (Relationship relationship : result) {
                            resultGUIDs.add(relationship.getGUID());
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
                        } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                            for (Relationship relationship : result) {

                                if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                    /*
                                     * This was an extra relationship that we either did not expect or that we have not seen previously.
                                     * Check it is a valid result. It can have any string attribute with the same value as strValue.
                                     */
                                    boolean validRelationship = false;
                                    InstanceProperties relationshipProperties = relationship.getProperties();
                                    if (relationshipProperties != null) {
                                        Set<String> relationshipPropertyNames = relationshipProperties.getInstanceProperties().keySet();
                                        Iterator<String> relationshipPropertyNameIterator = relationshipPropertyNames.iterator();
                                        while (relationshipPropertyNameIterator.hasNext()) {
                                            String propertyName = relationshipPropertyNameIterator.next();
                                            InstancePropertyValue ipValue = relationshipProperties.getPropertyValue(attributeName);
                                            if (ipValue != null) {
                                                InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                                if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipValue;
                                                    PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                                                    if (pdCat == OM_PRIMITIVE_TYPE_STRING) {
                                                        String propertyValueAsString = (String) (ppv.getPrimitiveValue());
                                                        if (propertyValueAsString.equals(stringValue)) {
                                                            validRelationship = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!validRelationship)
                                        matchingResult = false;
                                }
                            }
                        }


                        assertCondition(matchingResult,
                                        assertion18,
                                        testTypeName + assertionMsg18,
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

                    }
                    /*
                     * PREFIX MATCH - only feasible if string value has at least 2 chars.
                     */


                    int stringValueLength = stringValue.length();
                    if (stringValueLength >= 2) {
                        int truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                        String truncatedStringValue = stringValue.substring(0, truncatedLength);

                        literalisedValue = literaliseStringPropertyStartsWith(truncatedStringValue);

                        /*
                         * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
                         * Find all the values (regardless of attributeName) in the map that are an exact match to the search value
                         */
                        expectedRelationshipCount = 0;
                        expectedGUIDs = new ArrayList<>();
                        propertyNamesSet = propertyValueMap.keySet();
                        propertyNamesSetIterator = propertyNamesSet.iterator();
                        while (propertyNamesSetIterator.hasNext()) {
                            String propName = propertyNamesSetIterator.next();
                            if (propertyCatMap.get(propName) == OM_PRIMITIVE_TYPE_STRING) {
                                Map<Object, List<String>> propValues = propertyValueMap.get(propName);
                                Set<Object> propertyValuesSet = propValues.keySet();
                                Iterator<Object> propertyValuesSetIterator = propertyValuesSet.iterator();
                                while (propertyValuesSetIterator.hasNext()) {
                                    String knownStringValue = (String) (propertyValuesSetIterator.next());
                                    /* PREFIX MATCH */
                                    if (knownStringValue.startsWith(truncatedStringValue)) {
                                        for (String matchGUID : propValues.get(knownStringValue)) {
                                            if (!expectedGUIDs.contains(matchGUID)) {
                                                expectedGUIDs.add(matchGUID);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        expectedRelationshipCount = expectedGUIDs.size();

                        /*
                         * Search....
                         */

                        try {
                            result = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                                                                                         relationshipDef.getGUID(),
                                                                                         literalisedValue,
                                                                                         fromElement,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         pageSize);
                        }
                        catch (FunctionNotSupportedException exception) {

                            /*
                             * If running against a repository/connector that does not support relationship searches
                             * report that the optional cpability is not supported and give up on the test.
                             */

                            super.addNotSupportedAssertion(assertion1,
                                                           assertionMsg1,
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                            return;

                        }



                        /*
                         * Check that the expected number of instances was returned. This has to consider the effect of the original
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
                        resultCount = result == null ? 0 : result.size();
                        assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                                        assertion20,
                                        testTypeName + assertionMsg20,
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());


                        /*
                         * Check that the expected relationships were all returned
                         */
                        if (result != null) {
                            List<String> resultGUIDs = new ArrayList<>();
                            for (Relationship relationship : result) {
                                resultGUIDs.add(relationship.getGUID());
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
                            } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                for (Relationship relationship : result) {

                                    if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                        /*
                                         * This was an extra relationship that we either did not expect or that we have not seen previously.
                                         * Check it is a valid result. It can have any string attribute with the same value as strValue.
                                         */
                                        boolean validRelationship = false;
                                        InstanceProperties relationshipProperties = relationship.getProperties();
                                        if (relationshipProperties != null) {
                                            Set<String> relationshipPropertyNames = relationshipProperties.getInstanceProperties().keySet();
                                            Iterator<String> relationshipPropertyNameIterator = relationshipPropertyNames.iterator();
                                            while (relationshipPropertyNameIterator.hasNext()) {
                                                String propertyName = relationshipPropertyNameIterator.next();
                                                InstancePropertyValue ipValue = relationshipProperties.getPropertyValue(attributeName);
                                                if (ipValue != null) {
                                                    InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipValue;
                                                        PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                                                        if (pdCat == OM_PRIMITIVE_TYPE_STRING) {
                                                            String propertyValueAsString = (String) (ppv.getPrimitiveValue());
                                                            if (propertyValueAsString.startsWith(truncatedStringValue)) {
                                                                validRelationship = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (!validRelationship)
                                            matchingResult = false;
                                    }
                                }
                            }


                            assertCondition(matchingResult,
                                            assertion21,
                                            testTypeName + assertionMsg21,
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

                        }
                    }



                    /*
                     * SUFFIX MATCH - only feasible if string value has at least 2 chars.
                     */

                    if (stringValueLength >= 2) {
                        int truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                        String truncatedStringValue = stringValue.substring(stringValueLength - truncatedLength, stringValueLength);

                        literalisedValue = literaliseStringPropertyEndsWith(truncatedStringValue);

                        /*
                         * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
                         * Find all the values (regardless of attributeName) in the map that are an exact match to the search value
                         */
                        expectedRelationshipCount = 0;
                        expectedGUIDs = new ArrayList<>();
                        propertyNamesSet = propertyValueMap.keySet();
                        propertyNamesSetIterator = propertyNamesSet.iterator();
                        while (propertyNamesSetIterator.hasNext()) {
                            String propName = propertyNamesSetIterator.next();
                            if (propertyCatMap.get(propName) == OM_PRIMITIVE_TYPE_STRING) {
                                Map<Object,List<String>> propValues = propertyValueMap.get(propName);
                                Set<Object> propertyValuesSet = propValues.keySet();
                                Iterator<Object> propertyValuesSetIterator = propertyValuesSet.iterator();
                                while (propertyValuesSetIterator.hasNext()) {
                                    String knownStringValue = (String)(propertyValuesSetIterator.next());
                                    /* SUFFIX MATCH */
                                    if (knownStringValue.endsWith(truncatedStringValue)) {
                                        for (String matchGUID : propValues.get(knownStringValue)) {
                                            if (!expectedGUIDs.contains(matchGUID)) {
                                                expectedGUIDs.add(matchGUID);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        expectedRelationshipCount = expectedGUIDs.size();

                        /*
                         * Search....
                         */

                        try {
                            result = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                                                                                         relationshipDef.getGUID(),
                                                                                         literalisedValue,
                                                                                         fromElement,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         pageSize);
                        }
                        catch (FunctionNotSupportedException exception) {

                            /*
                             * If running against a repository/connector that does not support relationship searches
                             * report that the optional cpability is not supported and give up on the test.
                             */

                            super.addNotSupportedAssertion(assertion1,
                                                           assertionMsg1,
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                            return;

                        }



                        /*
                         * Check that the expected number of instances was returned. This has to consider the effect of the original
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
                        resultCount = result == null ? 0 : result.size();
                        assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                                assertion23,
                                testTypeName + assertionMsg23,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());


                        /*
                         * Check that the expected relationships were all returned
                         */
                        if (result != null) {
                            List<String> resultGUIDs = new ArrayList<>();
                            for (Relationship relationship : result) {
                                resultGUIDs.add(relationship.getGUID());
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
                            } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                for (Relationship relationship : result) {

                                    if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                        /*
                                         * This was an extra relationship that we either did not expect or that we have not seen previously.
                                         * Check it is a valid result. It can have any string attribute with the same value as strValue.
                                         */
                                        boolean validRelationship = false;
                                        InstanceProperties relationshipProperties = relationship.getProperties();
                                        if (relationshipProperties != null) {
                                            Set<String> relationshipPropertyNames = relationshipProperties.getInstanceProperties().keySet();
                                            Iterator<String> relationshipPropertyNameIterator = relationshipPropertyNames.iterator();
                                            while (relationshipPropertyNameIterator.hasNext()) {
                                                String propertyName = relationshipPropertyNameIterator.next();
                                                InstancePropertyValue ipValue = relationshipProperties.getPropertyValue(attributeName);
                                                if (ipValue != null) {
                                                    InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipValue;
                                                        PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                                                        if (pdCat == OM_PRIMITIVE_TYPE_STRING) {
                                                            String propertyValueAsString = (String) (ppv.getPrimitiveValue());
                                                            if (propertyValueAsString.endsWith(truncatedStringValue)) {
                                                                validRelationship = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (!validRelationship)
                                            matchingResult = false;
                                    }
                                }
                            }


                            assertCondition(matchingResult,
                                            assertion24,
                                            testTypeName + assertionMsg24,
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

                        }
                    }

                    /*
                     * CONTAINS MATCH - only feasible if string value has at least 3 chars.
                     */


                    if (stringValueLength >= 3) {
                        int truncatedLength = (int) (Math.floor(stringValueLength / 2.0));
                        int diff = stringValueLength - truncatedLength;
                        int halfDiff = diff / 2;
                        String truncatedStringValue = stringValue.substring(halfDiff, stringValueLength - halfDiff);

                        literalisedValue = literaliseStringPropertyContains(truncatedStringValue);


                        /*
                         * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
                         * Find all the values (regardless of attributeName) in the map that are an exact match to the search value
                         */
                        expectedRelationshipCount = 0;
                        expectedGUIDs = new ArrayList<>();
                        propertyNamesSet = propertyValueMap.keySet();
                        propertyNamesSetIterator = propertyNamesSet.iterator();
                        while (propertyNamesSetIterator.hasNext()) {
                            String propName = propertyNamesSetIterator.next();
                            if (propertyCatMap.get(propName) == OM_PRIMITIVE_TYPE_STRING) {
                                Map<Object,List<String>> propValues = propertyValueMap.get(propName);
                                Set<Object> propertyValuesSet = propValues.keySet();
                                Iterator<Object> propertyValuesSetIterator = propertyValuesSet.iterator();
                                while (propertyValuesSetIterator.hasNext()) {
                                    String knownStringValue = (String)(propertyValuesSetIterator.next());
                                    /* CONTAINS MATCH */
                                    if (knownStringValue.contains(truncatedStringValue)) {
                                        for (String matchGUID : propValues.get(knownStringValue)) {
                                            if (!expectedGUIDs.contains(matchGUID)) {
                                                expectedGUIDs.add(matchGUID);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        expectedRelationshipCount = expectedGUIDs.size();

                        /*
                         * Search....
                         */

                        try {
                            result = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                                                                                         relationshipDef.getGUID(),
                                                                                         literalisedValue,
                                                                                         fromElement,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         pageSize);

                        }
                        catch (FunctionNotSupportedException exception) {

                            /*
                             * If running against a repository/connector that does not support relationship searches
                             * report that the optional cpability is not supported and give up on the test.
                             */

                            super.addNotSupportedAssertion(assertion1,
                                                           assertionMsg1,
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

                            return;

                        }


                        /*
                         * Check that the expected number of instances was returned. This has to consider the effect of the original
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
                        resultCount = result == null ? 0 : result.size();
                        assertCondition(((!pageLimited && resultCount == expectedRelationshipCount) || (pageLimited && resultCount >= expectedRelationshipCount)),
                                assertion26,
                                testTypeName + assertionMsg26,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());


                        /*
                         * Check that the expected relationships were all returned
                         */
                        if (result != null) {
                            List<String> resultGUIDs = new ArrayList<>();
                            for (Relationship relationship : result) {
                                resultGUIDs.add(relationship.getGUID());
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
                            } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                                for (Relationship relationship : result) {

                                    if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                        /*
                                         * This was an extra relationship that we either did not expect or that we have not seen previously.
                                         * Check it is a valid result. It can have any string attribute with the same value as strValue.
                                         */
                                        boolean validRelationship = false;
                                        InstanceProperties relationshipProperties = relationship.getProperties();
                                        if (relationshipProperties != null) {
                                            Set<String> relationshipPropertyNames = relationshipProperties.getInstanceProperties().keySet();
                                            Iterator<String> relationshipPropertyNameIterator = relationshipPropertyNames.iterator();
                                            while (relationshipPropertyNameIterator.hasNext()) {
                                                String propertyName = relationshipPropertyNameIterator.next();
                                                InstancePropertyValue ipValue = relationshipProperties.getPropertyValue(attributeName);
                                                if (ipValue != null) {
                                                    InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE) {
                                                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipValue;
                                                        PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                                                        if (pdCat == OM_PRIMITIVE_TYPE_STRING) {
                                                            String propertyValueAsString = (String) (ppv.getPrimitiveValue());
                                                            if (propertyValueAsString.contains(truncatedStringValue)) {
                                                                validRelationship = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (!validRelationship)
                                            matchingResult = false;
                                    }
                                }
                            }


                            assertCondition(matchingResult,
                                            assertion27,
                                            testTypeName + assertionMsg27,
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

                        }
                    }
                }
            }
        }





        /*
         * Completion of searches - indicate success of testcase.
         */
        super.setSuccessMessage("Relationships can be searched by property and property value");
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
     *  Method to literalise a string value for exact match.
     *
     */
    public String literaliseStringPropertyExact(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        String litValue = repositoryHelper.getExactMatchRegex(value);
        return litValue;
    }

    /*
     *  Method to literalise a string value for prefix match.
     *
     */
    public String literaliseStringPropertyStartsWith(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        String litValue = repositoryHelper.getStartsWithRegex(value);
        return litValue;
    }

    /*
     *  Method to literalise a string value for suffix match.
     *
     */
    public String literaliseStringPropertyEndsWith(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        String litValue = repositoryHelper.getEndsWithRegex(value);
        return litValue;
    }

    /*
     *  Method to literalise a string value for suffix match.
     *
     */
    public String literaliseStringPropertyContains(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        String litValue = repositoryHelper.getContainsRegex(value);
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
