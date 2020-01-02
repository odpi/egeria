/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
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
 */
public class TestSupportedRelationshipPropertyAdvancedSearch extends RepositoryConformanceTestCase {

    private static final String testCaseId = "repository-relationship-property-advanced-search";
    private static final String testCaseName = "Repository relationship property advanced search test case";

    /* Type */
    private static final String assertion0 = testCaseId + "-00";
    private static final String assertionMsg0 = " relationship type definition matches known type  ";


    /* Test 1 */
    private static final String assertion1 = testCaseId + "-01";
    private static final String assertionMsg1 = " search returned results.";
    private static final String assertion2 = testCaseId + "-02";
    private static final String assertionMsg2 = " search contained all expected results.";
    private static final String assertion3 = testCaseId + "-03";
    private static final String assertionMsg3 = " search contained only valid results.";

    /* Test 2 */
    private static final String assertion4 = testCaseId + "-04";
    private static final String assertionMsg4 = " search contained only valid results.";

    /* Test 3 */
    private static final String assertion5 = testCaseId + "-05";
    private static final String assertionMsg5 = " search returned results.";
    private static final String assertion6 = testCaseId + "-06";
    private static final String assertionMsg6 = " search contained all expected results.";
    private static final String assertion7 = testCaseId + "-07";
    private static final String assertionMsg7 = " search contained only valid results.";

    /* Test 4 */
    private static final String assertion8 = testCaseId + "-08";
    private static final String assertionMsg8 = " search returned results.";
    private static final String assertion9 = testCaseId + "-09";
    private static final String assertionMsg9 = " search contained all expected results.";
    private static final String assertion10 = testCaseId + "-10";
    private static final String assertionMsg10 = " search contained only valid results.";

    /* Test 5 */
    private static final String assertion11 = testCaseId + "-11";
    private static final String assertionMsg11 = " search returned results.";
    private static final String assertion12 = testCaseId + "-12";
    private static final String assertionMsg12 = " search contained all expected results.";
    private static final String assertion13 = testCaseId + "-13";
    private static final String assertionMsg13 = " search contained only valid results.";

    /* Test 6 */
    private static final String assertion14 = testCaseId + "-14";
    private static final String assertionMsg14 = " search returned results.";
    private static final String assertion15 = testCaseId + "-15";
    private static final String assertionMsg15 = " search contained all expected results.";
    private static final String assertion16 = testCaseId + "-16";
    private static final String assertionMsg16 = " search contained no unexpected results.";

    /* Test 7 */
    private static final String assertion17 = testCaseId + "-17";
    private static final String assertionMsg17 = " value search returned results.";
    private static final String assertion18 = testCaseId + "-18";
    private static final String assertionMsg18 = " value search contained all expected results.";
    private static final String assertion19 = testCaseId + "-19";
    private static final String assertionMsg19 = " value search contained no unexpected results.";

    /* Test 8 */
    private static final String assertion20 = testCaseId + "-20";
    private static final String assertionMsg20 = " value search returned results.";
    private static final String assertion21 = testCaseId + "-21";
    private static final String assertionMsg21 = " value search contained all expected results.";
    private static final String assertion22 = testCaseId + "-22";
    private static final String assertionMsg22 = " value search contained no unexpected results.";

    private static final String assertion23     = testCaseId + "-23";
    private static final String assertionMsg23  = " repository supports creation of instances.";



    private static final String discoveredProperty_searchSupport = " advanced search support";


    private RepositoryConformanceWorkPad workPad;
    private String metadataCollectionId;
    private Map<String, EntityDef> entityDefs;
    private RelationshipDef relationshipDef;
    private List<TypeDefAttribute> attrList;

    private String testTypeName;


    private boolean multiSetTest = false;
    private String firstStringAttributeName = null;


    private InstanceProperties instProps0;
    private InstanceProperties instProps1;
    private InstanceProperties instProps2;

    private InstanceProperties literalisedInstanceProperties0;
    private InstanceProperties literalisedInstanceProperties1;
    private InstanceProperties literalisedInstanceProperties2;


    private List<Relationship> relationshipSet_0 = null;
    private List<Relationship> relationshipSet_1 = null;
    private List<Relationship> relationshipSet_2 = null;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad         place for parameters and results
     * @param entityDefs      entities to test
     * @param relationshipDef type of valid relationships
     */
    public TestSupportedRelationshipPropertyAdvancedSearch(RepositoryConformanceWorkPad workPad,
                                                           Map<String, EntityDef>       entityDefs,
                                                           RelationshipDef              relationshipDef)
    {
        super(workPad,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        this.workPad = workPad;
        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.entityDefs = entityDefs;
        this.relationshipDef = relationshipDef;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
                testCaseId,
                testCaseName);

        this.instProps0 = null;
        this.instProps1 = null;
        this.instProps2 = null;

        this.literalisedInstanceProperties0 = null;
        this.literalisedInstanceProperties1 = null;
        this.literalisedInstanceProperties2 = null;

        this.relationshipSet_0 = null;
        this.relationshipSet_1 = null;
        this.relationshipSet_2 = null;


    }


    /**
     * Default run method - throws Exception because this is a multi-phase test case
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        throw new Exception("This is a multi--phase test case; please invoke it with a phase parameter");
    }


    /**
     * Method implemented by the actual test case. This overloads the default implementation in OpenMetadataTestCase
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run(TestPhase phase) throws Exception
    {

        /*
         * This test will:
         *
         * Create a set of relationships of the defined type in the TUT repository, with different/overlapping property values.
         *
         * It will then conduct a series of searches against the repository - some of which should retrieve relationships, others should not.
         *
         * The searches in this test case are all searches using match properties and all strings are treated as exact match.
         * There are more details on each type of test in the relevant section below.
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



        this.attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(), relationshipDef);

        if (this.attrList == null || this.attrList.isEmpty()) {
            /*
             * There are no attributes for this type so a MatchProperties search is not possible.
             */
            return;
        }


        /*
         * Find By Instance (Match) Properties
         *
         *   Since this test uses MatchProperties there have to be some attributes defined on the type. If not, the test returns.
         *   Provided the type has at least one attribute the test can proceed.
         *
         *   There are three sets of relationships (named 0, 1 & 2).
         *   There will be two relationships per set, to ensure multiple 'hits'
         *   The sets of relationships and their property values are as follows:
         *
         *   Entity set 0:   base values for all properties, formed as <property-name>.<set> - e.g. description.0
         *   Entity set 1:   distinct from e0 but overlaps with e2
         *   Entity set 2:   distinct from e0 but overlaps with e1, on the first property
         *
         *
         * instProps0 is always distinct.
         * instProps1 and instProps2 are distinct apart from first prop - for which they have the same value.
         *
         *
         * Searches:
         *
         * These tests are only run for types with at least one String property; the first String property is searched using a Regex.
         *
         * findRelationshipsByProperty()
         *   1. Use instanceProperties with <propertyName> of first String property, MatchCriteria set to ALL - this should return all entities in sets 0, 1 & 2
         *   2. Similar to test 1 but with MatchCriteria set to NONE - this should not return any return entities in sets 0, 1 or 2
         *   3. Use instanceProperties with <propertyName>\.[0] of first String property - this should return all and only entities in set 0
         *   4. Similar to test 3 but with MatchCriteria set to NONE - this should not return any return entities in sets 1 & 2
         *   5. Use instanceProperties with <propertyName>\.[^12] of first String property - this should return all and only entities in set 0
         *   6. Similar to test 5 but with MatchCriteria set to NONE - this should not return any return entities in sets 1 & 2
         *
         * findRelationshipsByPropertyValue()
         *   7. Use searchCriteria regex that will match first string property in set 0 - this should return all and only relationships in set 0
         *   8. Use searchCriteria regex that will match first string property in sets 1 and 2 - this should return all and only relationships in set 1 and 2
         *
         */


        switch (phase) {

            case CREATE:
                this.createInstances(metadataCollection);
                break;
            case EXECUTE:
                this.performFinds(metadataCollection);
                break;
            case CLEAN:
                this.cleanInstances(metadataCollection);
                break;
        }
    }


    private void createInstances(OMRSMetadataCollection metadataCollection) throws Exception
    {

        /*
         *  Generate property values for all the type's defined properties, including inherited properties
         */
        this.firstStringAttributeName = this.typeDefHasAtLeastOneStringProperty(attrList);
        this.multiSetTest = this.firstStringAttributeName != null;


        /*
         *  Generate property values for all the type's defined properties, including inherited properties
         */



        /*
         * Create the instance properties for each relationship set.
         */


        this.instProps0 = populateInstanceProperties(attrList, "0");
        if (this.multiSetTest) {
            this.instProps1 = populateInstanceProperties(attrList, "1");
            this.instProps2 = populateInstanceProperties(attrList, "2");
        }

        /*
         * We cannot be sure that the repository under test supports metadata maintenance, so need to try and back off.
         */

        try {


            /*
             * Create two relationships for each set.
             */

            this.relationshipSet_0 = new ArrayList<>();
            this.populateRelationshipSet(metadataCollection, this.relationshipSet_0, this.instProps0);

            if (this.multiSetTest) {

                this.relationshipSet_1 = new ArrayList<>();
                this.populateRelationshipSet(metadataCollection, this.relationshipSet_1, this.instProps1);

                this.relationshipSet_2 = new ArrayList<>();
                this.populateRelationshipSet(metadataCollection, this.relationshipSet_2, this.instProps2);

            }

            repositoryConformanceWorkPad.addRelationshipInstanceSets(relationshipDef.getName(), this.relationshipSet_0, this.relationshipSet_1, this.relationshipSet_2);

            assertCondition((true),
                    assertion23,
                    testTypeName + assertionMsg23,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        } catch (FunctionNotSupportedException exception) {

            /*
             * If the repository does not support metadata maintenance, the workpad will not have recorded any instances.
             * The absence of instance is checked in the remaining phases (EXECUTE and CLEAN).
             */

            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * Report the inability to create instances and give up on the testcase....
             */

            super.addNotSupportedAssertion(assertion23,
                    assertionMsg23,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());


            return;
        }

    }

    private void populateRelationshipSet(OMRSMetadataCollection metadataCollection,
                                         List<Relationship>     relationshipSet,
                                         InstanceProperties     instanceProps) throws Exception
    {

        /*
         * In this testcase the repository is believed to support the relationship type defined by
         * relationshipDef - but may not support all of the entity inheritance hierarchy - it may only
         * support a subset of entity types. So although the relationship type may have end definitions
         * each specifying a given entity type - the repository may only support certain sub-types of the
         * specified type. This is OK, and the testcase needs to only try to use entity types that are
         * supported by the repository being tested. To do this it needs to start with the specified
         * end type, e.g. Referenceable, and walk down the hierarchy looking for each subtype that
         * is supported by the repository (i.e. is in the entityDefs map). The test is run for
         * each combination of end1Type and end2Type - but only for types that are within the
         * active set for this repository.
         */

        String end1DefName = relationshipDef.getEndDef1().getEntityType().getName();
        List<String> end1DefTypeNames = new ArrayList<>();
        end1DefTypeNames.add(end1DefName);
        if (this.workPad.getEntitySubTypes(end1DefName) != null) {
            end1DefTypeNames.addAll(this.workPad.getEntitySubTypes(end1DefName));
        }


        String end2DefName = relationshipDef.getEndDef2().getEntityType().getName();
        List<String> end2DefTypeNames = new ArrayList<>();
        end2DefTypeNames.add(end2DefName);
        if (this.workPad.getEntitySubTypes(end2DefName) != null) {
            end2DefTypeNames.addAll(this.workPad.getEntitySubTypes(end2DefName));
        }

        /*
         * Filter the possible types to only include types that are supported by the repository
         */

        List<String> end1SupportedTypeNames = new ArrayList<>();
        for (String end1TypeName : end1DefTypeNames) {
            if (entityDefs.get(end1TypeName) != null)
                end1SupportedTypeNames.add(end1TypeName);
        }

        List<String> end2SupportedTypeNames = new ArrayList<>();
        for (String end2TypeName : end2DefTypeNames) {
            if (entityDefs.get(end2TypeName) != null)
                end2SupportedTypeNames.add(end2TypeName);
        }

        /*
         * Check that neither list is empty
         */
        if (end1SupportedTypeNames.isEmpty() || end2SupportedTypeNames.isEmpty()) {

            /*
             * There are no supported types for at least one of the ends - the repository cannot test this relationship type.
             */
            assertCondition((false),
                    assertion12,
                    testTypeName + assertionMsg12,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());
        }

        /*
         * The test does not iterate over all possible end types - but it must select an end type that is supported by the repository,
         * so it uses the first type in the supported list for each end.
         */

        String end1TypeName = end1SupportedTypeNames.get(0);
        String end2TypeName = end2SupportedTypeNames.get(0);


        /*
         * Local variables for entity creation - entities need to be cleaned up when relationships are deleted.
         */
        EntityDef     end1Type;
        EntityDetail  end1;
        EntityDef     end2Type;
        EntityDetail  end2;


        end1Type = entityDefs.get(end1TypeName);
        end1 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end1Type);
        end2Type = entityDefs.get(end2TypeName);
        end2 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end2Type);

        relationshipSet.add(metadataCollection.addRelationship(workPad.getLocalServerUserId(), relationshipDef.getGUID(), instanceProps, end1.getGUID(), end2.getGUID(), null));

        end1Type = entityDefs.get(end1TypeName);
        end1 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end1Type);
        end2Type = entityDefs.get(end2TypeName);
        end2 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end2Type);

        relationshipSet.add(metadataCollection.addRelationship(workPad.getLocalServerUserId(), relationshipDef.getGUID(), instanceProps, end1.getGUID(), end2.getGUID(), null));


    }

    private void cleanInstances(OMRSMetadataCollection metadataCollection) throws Exception
    {

        List<Relationship> workpad_set0 = repositoryConformanceWorkPad.getRelationshipInstanceSet(relationshipDef.getName(), 0);

        if (workpad_set0 != null) {

            /*
             * Instances were created for set0; so may also have been created for other sets (if multi-set). Clean up all instance sets
             */
            /*
             * Clean up all relationships (and their associated entities) created by this testcase
             */

            this.cleanRelationshipSet(metadataCollection, relationshipSet_0);
            if (this.multiSetTest) {
                this.cleanRelationshipSet(metadataCollection, relationshipSet_1);
                this.cleanRelationshipSet(metadataCollection, relationshipSet_2);
            }

        }
    }


    private void cleanRelationshipSet(OMRSMetadataCollection metadataCollection, List<Relationship> relationshipSet) throws Exception
    {
        /*
         * Local variables for end2
         */
        EntityProxy end1;
        EntityProxy end2;


        for (Relationship relationship : relationshipSet) {

            end1 = relationship.getEntityOneProxy();
            end2 = relationship.getEntityTwoProxy();


            try {

                /*
                 * Delete both end entities and the relationship
                 */
                metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                        relationship.getType().getTypeDefGUID(),
                        relationship.getType().getTypeDefName(),
                        relationship.getGUID());

                metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                        end1.getType().getTypeDefGUID(),
                        end1.getType().getTypeDefName(),
                        end1.getGUID());

                metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                        end2.getType().getTypeDefGUID(),
                        end2.getType().getTypeDefName(),
                        end2.getGUID());



            } catch (FunctionNotSupportedException exception) {
                // NO OP - can proceed to purge
            }

            metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                    relationship.getType().getTypeDefGUID(),
                    relationship.getType().getTypeDefName(),
                    relationship.getGUID());

            metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                    end1.getType().getTypeDefGUID(),
                    end1.getType().getTypeDefName(),
                    end1.getGUID());

            metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                    end2.getType().getTypeDefGUID(),
                    end2.getType().getTypeDefName(),
                    end2.getGUID());


        }
    }

    private void performFinds(OMRSMetadataCollection metadataCollection) throws Exception
    {

        List<Relationship> workpad_set0 = repositoryConformanceWorkPad.getRelationshipInstanceSet(relationshipDef.getName(), 0);

        if (workpad_set0 != null) {

            /*
             * Instances were created for set0; so may also have been created for other sets (if multi-set). Run the tests
             */

            if (this.multiSetTest)
                this.performFindsMultiSet(metadataCollection);
            else {
                /*
                 * If not multiSet it is because there is no string property - so no possibility of performing a regex test
                 */
                return;
            }
        }
    }


    private void performFindsMultiSet(OMRSMetadataCollection metadataCollection) throws Exception
    {


        /*
         * Do not perform existence assertions and content validations - these are tested in lifecycle tests.
         */

        InstanceProperties matchProperties = null;
        MatchCriteria matchCriteria        = null;
        int fromElement                    = 0;
        List<Relationship> result          = null;



        /* ------------------------------------------------------------------------------------- */


        String regex = null;

        /*
         *  Test 1.  Use instanceProperties with <propertyName> of first String property - matchCriteria ALL
         *  Regex is set to any string containing the property name which should match all sets.
         *  This should return all relationships in sets 0, 1 & 2
         */

        regex = ".*" + this.firstStringAttributeName + ".*";

        matchProperties = new InstanceProperties();
        matchProperties = this.addStringPropertyToInstance(matchProperties, this.firstStringAttributeName, regex);
        matchCriteria = MatchCriteria.ALL;
        fromElement = 0;


        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion1,
                testTypeName + assertionMsg1,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all relationships in sets 0, 1 & 2
         */

        assertCondition((result.containsAll(relationshipSet_0) && result.containsAll(relationshipSet_1) && result.containsAll(relationshipSet_2)),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        boolean contamination = false;

        List<Relationship> expectedResult = new ArrayList<>();
        expectedResult.addAll(relationshipSet_0);
        expectedResult.addAll(relationshipSet_1);
        expectedResult.addAll(relationshipSet_2);

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (Relationship rel : result) {
                if (!(expectedResult.contains(rel))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion3,
                testTypeName + assertionMsg3,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());




        /* ------------------------------------------------------------------------------------- */


        /*
         *
         *  Test 2.  Use instanceProperties with <propertyName> of first String property - and MatchCriteria NONE
         *  Regex is set to any string containing the property name which should match all sets.
         *  The result set should be empty.
         */

        matchCriteria = MatchCriteria.NONE;


        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                 0);



        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        expectedResult = new ArrayList<>(); // deliberately empty list


        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (Relationship rel : result) {
                if (!(expectedResult.contains(rel))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion4,
                testTypeName + assertionMsg4,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());



        /* ------------------------------------------------------------------------------------- */


        /*
         *  Test 3. Use instanceProperties with <propertyName>\.[0] of first String property - matchCriteria ALL
         *  Regex is set to any string containing the property name used for set 0 only.
         *  This should return all and only relationships in set 0
         */

        regex = this.firstStringAttributeName+"\\.[0]";

        matchProperties = new InstanceProperties();
        matchProperties = this.addStringPropertyToInstance(matchProperties, this.firstStringAttributeName, regex);  // deliberately using name as value too
        matchCriteria = MatchCriteria.ALL;
        fromElement = 0;


        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion5,
                testTypeName + assertionMsg5,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_0)),
                assertion6,
                testTypeName + assertionMsg6,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        expectedResult = new ArrayList<>();
        expectedResult.addAll(relationshipSet_0);


        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (Relationship rel : result) {
                if (!(expectedResult.contains(rel))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }


        assertCondition((contamination == false),
                assertion7,
                testTypeName + assertionMsg7,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *
         *  Test 4. Use instanceProperties with <propertyName>\.[0] of first String property - matchCriteria NONE
         *  Regex is set to any string containing the property name used for set 0 only.
         *  This should return sets 1 & 2 only.
         */


        matchCriteria = MatchCriteria.NONE;


        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                 0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion8,
                testTypeName + assertionMsg8,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_1) && result.containsAll(relationshipSet_2)),
                assertion9,
                testTypeName + assertionMsg9,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        expectedResult = new ArrayList<>();
        expectedResult.addAll(relationshipSet_1);
        expectedResult.addAll(relationshipSet_2);

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (Relationship rel : result) {
                if (!(expectedResult.contains(rel))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }


        assertCondition((contamination == false),
                assertion10,
                testTypeName + assertionMsg10,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */


        /*
         *  Test 5. Use instanceProperties with <propertyName>\.[^12] of first String property - matchCriteria ALL
         *  Regex is set to any string containing the property name apart from those used for sets 1 & 2.
         *  This should return all and only relationships in set 0
         */

        regex = this.firstStringAttributeName+"\\.[^12]";

        matchProperties = new InstanceProperties();
        matchProperties = this.addStringPropertyToInstance(matchProperties, this.firstStringAttributeName, regex);  // deliberately using name as value too
        matchCriteria = MatchCriteria.ALL;
        fromElement = 0;


        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion11,
                testTypeName + assertionMsg11,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_0)),
                assertion12,
                testTypeName + assertionMsg12,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        expectedResult = new ArrayList<>();
        expectedResult.addAll(relationshipSet_0);


        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (Relationship rel : result) {
                if (!(expectedResult.contains(rel))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }


        assertCondition((contamination == false),
                assertion13,
                testTypeName + assertionMsg13,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 6. Use instanceProperties with <propertyName>\.[^12] of first String property - matchCriteria NONE
         *  Regex is set to any string containing the property name apart from those used for sets 1 & 2.
         *  This should return all and only relationships in sets 1 & 2 only.
         */

        matchCriteria = MatchCriteria.NONE;


        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion14,
                testTypeName + assertionMsg14,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_1) && result.containsAll(relationshipSet_2)),
                assertion15,
                testTypeName + assertionMsg15,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        expectedResult = new ArrayList<>();
        expectedResult.addAll(relationshipSet_1);
        expectedResult.addAll(relationshipSet_2);

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (Relationship rel : result) {
                if (!(expectedResult.contains(rel))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }


        assertCondition((contamination == false),
                assertion16,
                testTypeName + assertionMsg16,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 7. Use searchCriteria regex that will match first string property in set 0 - this should return all and only relationships in set 0
         *
         */

        regex = this.firstStringAttributeName+"\\.0";
        String searchCriteria = regex;
        fromElement = 0;


        result = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                searchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion17,
                testTypeName + assertionMsg17,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships of the current type
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(relationshipSet_0);


        assertCondition((result.containsAll(expectedResult)),
                assertion18,
                testTypeName + assertionMsg18,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (Relationship rel : result) {
                if (!(expectedResult.contains(rel))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), firstStringAttributeName, regex, MatchCriteria.ALL);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion19,
                testTypeName + assertionMsg19,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 8. Use searchCriteria regex that will match the overlapping property in sets 1 ad 2 - this should return all relationships in sets 1 and 2
         *
         */

        regex = this.firstStringAttributeName+"\\.1";
        searchCriteria = regex;
        fromElement = 0;


        result = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                searchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion20,
                testTypeName + assertionMsg20,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_1 and set_2 relationships of the current type
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(relationshipSet_1);
        expectedResult.addAll(relationshipSet_2);


        assertCondition((result.containsAll(expectedResult)),
                assertion21,
                testTypeName + assertionMsg21,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (Relationship rel : result) {
                if (!(expectedResult.contains(rel))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), firstStringAttributeName, regex, MatchCriteria.ALL);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion22,
                testTypeName + assertionMsg22,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getRequirementId());





        /*
         * Completion of searches - indicate success of testcase.
         */
        super.setSuccessMessage("Relationships can be searched by property and property value");
    }




    /**
     * Determine if properties are as expected.
     *
     * @param firstInstanceProps is the target which must always be a non-null InstanceProperties
     * @param secondInstanceProps is the actual to be compared against first param - can be null, or empty....
     * @return match boolean
     */
    private boolean doPropertiesMatch(InstanceProperties firstInstanceProps, InstanceProperties secondInstanceProps)
    {
        boolean matchProperties = false;
        boolean noProperties = false;

        if ( (secondInstanceProps == null) ||
             (secondInstanceProps.getInstanceProperties() == null) ||
             (secondInstanceProps.getInstanceProperties().isEmpty()))
        {
            noProperties = true;
        }

        if (noProperties)
        {
            if ((firstInstanceProps.getInstanceProperties() == null) ||
                (firstInstanceProps.getInstanceProperties().isEmpty()))
            {
                matchProperties = true;
            }
        }
        else
        {
            // non-empty, perform matching

            Map<String, InstancePropertyValue> secondPropertiesMap = secondInstanceProps.getInstanceProperties();
            Map<String, InstancePropertyValue> firstPropertiesMap  = firstInstanceProps.getInstanceProperties();

            boolean matchSizes = (secondPropertiesMap.size() == firstPropertiesMap.size());

            if (matchSizes)
            {
                Set<String> secondPropertiesKeySet = secondPropertiesMap.keySet();
                Set<String> firstPropertiesKeySet  = firstPropertiesMap.keySet();

                boolean matchKeys = secondPropertiesKeySet.containsAll(firstPropertiesKeySet) &&
                                    firstPropertiesKeySet.containsAll(secondPropertiesKeySet);

                if (matchKeys)
                {
                    // Assume the values match and prove it if they don't...
                    boolean matchValues = true;

                    Iterator<String> secondPropertiesKeyIterator = secondPropertiesKeySet.iterator();
                    while (secondPropertiesKeyIterator.hasNext())
                    {
                        String key = secondPropertiesKeyIterator.next();
                        if (!(secondPropertiesMap.get(key).equals(firstPropertiesMap.get(key))))
                        {
                            matchValues = false;
                        }
                    }

                    // If all property values matched....
                    if (matchValues)
                    {
                        matchProperties = true;
                    }
                }
            }
        }

        return matchProperties;
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
     * This method will populate an instance properties object - it does not populate core properties.
     * attrList is a list of all the TypeDefAttributes for the TypeDef to be instantiated.
     * modifier is an instance modifier - some properties will be customised per instance, others not.
     */
    protected InstanceProperties populateInstanceProperties(List<TypeDefAttribute> attrList , String setName)
    {

        /*
         * Get the trivial case out of the way
         */
        if (attrList == null) {
            return null;
        }

        InstanceProperties properties = null;

        Map<String, InstancePropertyValue> propertyMap = new HashMap<>();


        for (TypeDefAttribute typeDefAttribute : attrList)
        {
            String                   attributeName = typeDefAttribute.getAttributeName();
            AttributeTypeDef         attributeType = typeDefAttribute.getAttributeType();
            AttributeTypeDefCategory category      = attributeType.getCategory();

            String modifier = setName;

            if (this.multiSetTest) {
                if (attributeName.equals(this.firstStringAttributeName)) {
                    // This is the first String property - so for any sets other than set "0" it should match set "1"
                    if (!setName.equals("0")) {
                        modifier = "1";
                    }
                }
            }

            switch(category)
            {
                case PRIMITIVE:
                    PrimitiveDef primitiveDef = (PrimitiveDef)attributeType;
                    propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef, modifier));
                    break;
            }
        }

        if (! propertyMap.isEmpty())
        {
            properties = new InstanceProperties();
            properties.setInstanceProperties(propertyMap);
        }


        return properties;

    }


    /**
     * Create a primitive property value for the requested property. This is called both during create population and
     * during generation of match properties for searches. It can also be used for result validation.
     *
     * @param propertyName name of the property
     * @param propertyType type of the property
     * @return PrimitivePropertyValue object
     */
    private PrimitivePropertyValue getPrimitivePropertyValue(String        propertyName,
                                                             PrimitiveDef  propertyType,
                                                             String        modifier)
    {
        String  strModifier = modifier;
        Integer intModifier = Integer.parseInt(modifier);

        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(propertyType.getPrimitiveDefCategory());

        switch (propertyType.getPrimitiveDefCategory())
        {
            case OM_PRIMITIVE_TYPE_STRING:
                propertyValue.setPrimitiveValue(propertyName + "." + strModifier);
                break;
            case OM_PRIMITIVE_TYPE_DATE:
                propertyValue.setPrimitiveValue(100L+new Long(strModifier));
                break;
            case OM_PRIMITIVE_TYPE_INT:
                propertyValue.setPrimitiveValue(42+intModifier);
                break;
            case OM_PRIMITIVE_TYPE_BOOLEAN:
                propertyValue.setPrimitiveValue(modifier.equals("0")?false:true);
                break;
            case OM_PRIMITIVE_TYPE_SHORT:
                propertyValue.setPrimitiveValue(new Short(strModifier));
                break;
            case OM_PRIMITIVE_TYPE_BYTE:
                propertyValue.setPrimitiveValue(new Byte(strModifier));
                break;
            case OM_PRIMITIVE_TYPE_CHAR:
                propertyValue.setPrimitiveValue(strModifier.charAt(0));
                break;
            case OM_PRIMITIVE_TYPE_LONG:
                propertyValue.setPrimitiveValue(new Long(2000*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_FLOAT:
                propertyValue.setPrimitiveValue(new Float(3.14159*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_DOUBLE:
                propertyValue.setPrimitiveValue(new Double(1000000*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                propertyValue.setPrimitiveValue(new Double(1000000*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                propertyValue.setPrimitiveValue(new Double(1000000*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_UNKNOWN:
                break;
        }

        return propertyValue;
    }


    public InstanceProperties addStringPropertyToInstance(InstanceProperties properties,
                                                          String             propertyName,
                                                          String             propertyValue)
    {
        InstanceProperties  resultingProperties;

        if (propertyValue != null)
        {

            if (properties == null)
            {

                resultingProperties = new InstanceProperties();
            }
            else
            {
                resultingProperties = properties;
            }


            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            primitivePropertyValue.setPrimitiveValue(propertyValue);
            primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
            primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

            resultingProperties.setProperty(propertyName, primitivePropertyValue);

            return resultingProperties;
        }
        else
        {
            return properties;
        }
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
                        if (pdCat == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING) {
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


    /*
     * Method to check whether the typeDef has at least one attribute of type String, and if so return the attribute name.
     * If there is no String attribute in the type return null.
     */

    private String typeDefHasAtLeastOneStringProperty(List<TypeDefAttribute> attrList)
    {


        for (TypeDefAttribute typeDefAttribute : attrList)
        {
            String                   attributeName = typeDefAttribute.getAttributeName();
            AttributeTypeDef         attributeType = typeDefAttribute.getAttributeType();
            AttributeTypeDefCategory category      = attributeType.getCategory();

            switch(category)
            {
                case PRIMITIVE:
                    PrimitiveDef primitiveDef = (PrimitiveDef)attributeType;
                    PrimitiveDefCategory pdCat = primitiveDef.getPrimitiveDefCategory();
                    if (pdCat == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                        return attributeName;
                    break;
            }
        }

        return null;

    }


    private boolean doInstancePropertiesSatisfyMatchCriteria(InstanceProperties instProps, String attributeName, String regex, MatchCriteria matchCriteria) {

        boolean match = false;

        /*
         * Check that the named attribute in instProps satisfies the regex...
         */
        if (instProps != null) {
            InstancePropertyValue ipValue = instProps.getPropertyValue(attributeName);
            InstancePropertyCategory ipCat = ipValue.getInstancePropertyCategory();
            if (ipCat == InstancePropertyCategory.PRIMITIVE) {
                PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) ipValue;
                if (primitivePropertyValue.getPrimitiveDefCategory() == OM_PRIMITIVE_TYPE_STRING) {
                    String stringProperty = (String) primitivePropertyValue.getPrimitiveValue();
                    if (stringProperty != null) {
                        if (stringProperty.matches(regex)) {
                            match = true;
                        }
                    }
                }
            }
        }

        if (matchCriteria == MatchCriteria.ALL || matchCriteria == MatchCriteria.ANY)
            return match;
        else
            return !match;

    }

}
