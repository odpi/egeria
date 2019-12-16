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
 * Test that all defined entities can be retrieved by property searches
 */
public class TestSupportedRelationshipPropertySearch extends RepositoryConformanceTestCase {

    private static final String testCaseId = "repository-relationship-property-search";
    private static final String testCaseName = "Repository relationship property search test case";


    /* Type */

    private static final String assertion0 = testCaseId + "-00";
    private static final String assertionMsg0 = " relationship type definition matches known type  ";


    /* Assertions for multi set tests */

    /* Test 1 */
    private static final String assertion1 = testCaseId + "-01";
    private static final String assertionMsg1 = " search returned results.";
    private static final String assertion2 = testCaseId + "-02";
    private static final String assertionMsg2 = " search contained all expected results.";
    private static final String assertion3 = testCaseId + "-03";
    private static final String assertionMsg3 = " search contained only valid results.";

    /* Test 2 */
    private static final String assertion4 = testCaseId + "-04";
    private static final String assertionMsg4 = " search returned results.";
    private static final String assertion5 = testCaseId + "-05";
    private static final String assertionMsg5 = " search contained all expected results.";
    private static final String assertion6 = testCaseId + "-06";
    private static final String assertionMsg6 = " search contained only valid results.";

    /* Test 3 */
    private static final String assertion7 = testCaseId + "-07";
    private static final String assertionMsg7 = " search returned results.";
    private static final String assertion8 = testCaseId + "-08";
    private static final String assertionMsg8 = " search contained all expected results.";
    private static final String assertion9 = testCaseId + "-09";
    private static final String assertionMsg9 = " search contained only valid results.";

    /* Test 4 */
    private static final String assertion10 = testCaseId + "-10";
    private static final String assertionMsg10 = " search returned results.";
    private static final String assertion11 = testCaseId + "-11";
    private static final String assertionMsg11 = " search contained all expected results.";
    private static final String assertion12 = testCaseId + "-12";
    private static final String assertionMsg12 = " search contained only valid results.";

    /* Test 5 */
    private static final String assertion13 = testCaseId + "-13";
    private static final String assertionMsg13 = " search returned results.";
    private static final String assertion14 = testCaseId + "-14";
    private static final String assertionMsg14 = " search contained all expected results.";
    private static final String assertion15 = testCaseId + "-15";
    private static final String assertionMsg15 = " search contained only valid results.";

    /* Test 6 */
    private static final String assertion16 = testCaseId + "-16";
    private static final String assertionMsg16 = " search returned results.";
    private static final String assertion17 = testCaseId + "-17";
    private static final String assertionMsg17 = " search contained all expected results.";
    private static final String assertion18 = testCaseId + "-18";
    private static final String assertionMsg18 = " search contained only valid results.";

    /* Test 7 */
    private static final String assertion19     = testCaseId + "-19";
    private static final String assertionMsg19  = " search without filtering returned results.";
    private static final String assertion20     = testCaseId + "-20";
    private static final String assertionMsg20  = " search with filtering return expected number of results.";
    /* Assertions for single set tests */

    private static final String assertion21     = testCaseId + "-21";
    private static final String assertionMsg21  = " value search returned results.";
    private static final String assertion22     = testCaseId + "-22";
    private static final String assertionMsg22  = " value search contained all expected results.";
    private static final String assertion23     = testCaseId + "-23";
    private static final String assertionMsg23  = " value search contained only valid results.";

    private static final String assertion24     = testCaseId + "-24";
    private static final String assertionMsg24  = " value search returned results.";
    private static final String assertion25     = testCaseId + "-25";
    private static final String assertionMsg25  = " value search contained all expected results.";
    private static final String assertion26     = testCaseId + "-26";
    private static final String assertionMsg26  = " value search contained only valid results.";

    private static final String assertion27     = testCaseId + "-27";
    private static final String assertionMsg27  = " search returned results.";
    private static final String assertion28     = testCaseId + "-28";
    private static final String assertionMsg28  = " search contained all expected results.";
    private static final String assertion29     = testCaseId + "-29";
    private static final String assertionMsg29  = " search contained only valid results.";

    private static final String assertion30     = testCaseId + "-30";
    private static final String assertionMsg30  = " search returned results.";
    private static final String assertion31     = testCaseId + "-31";
    private static final String assertionMsg31  = " search contained all expected results.";
    private static final String assertion32     = testCaseId + "-32";
    private static final String assertionMsg32  = " search contained only valid results.";

    private static final String assertion33     = testCaseId + "-33";
    private static final String assertionMsg33  = " search return only valid results.";


    private static final String assertion34     = testCaseId + "-34";
    private static final String assertionMsg34  = " repository supports creation of instances.";

    private static final String discoveredProperty_searchSupport = " search support";

    private RepositoryConformanceWorkPad workPad;
    private String metadataCollectionId;
    private Map<String, EntityDef> entityDefs;
    private RelationshipDef relationshipDef;
    private List<TypeDefAttribute> attrList;
    private String testTypeName;

    private boolean multiSetTest                    = false;
    private String  firstStringAttributeName        = null;
    private boolean typeHasTestedInstanceProperties = true; // assumed true until CREATE phase is performed


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
     * @param relationshipDef type of valid relationships
     */
    public TestSupportedRelationshipPropertySearch(RepositoryConformanceWorkPad workPad,
                                                   Map<String, EntityDef> entityDefs,
                                                   RelationshipDef relationshipDef)
    {
        super(workPad,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

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
     * Default run method - throws Exception because this is a multi-phase testcase
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {

        throw new Exception("This is a multi--phase testcase; please invoke it with a phase parameter");
    }

    /**
     * Method implemented by the actual test case. This overloads the default implementation in OpenMetadatatestCase
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
         * The searches in this testcase are all searches using match properties and all strings are treated as exact match.
         * There are more details on each type of test in the relevant section below.
         *
         */


        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();



        /*
         * Check that the relationship type matches the known type from the repository helper
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

        String end1TypeDefName = relationshipDef.getEndDef1().getEntityType().getName();
        EntityDef end1EntityDef = entityDefs.get(end1TypeDefName);
        EntityDef knownEnd1EntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), end1EntityDef.getName());
        verifyCondition((end1EntityDef.equals(knownEnd1EntityDef)),
                assertion0,
                testTypeName + assertionMsg0,
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());

        String end2TypeDefName = relationshipDef.getEndDef2().getEntityType().getName();
        EntityDef end2EntityDef = entityDefs.get(end2TypeDefName);
        EntityDef knownEnd2EntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), end2EntityDef.getName());
        verifyCondition((end2EntityDef.equals(knownEnd2EntityDef)),
                assertion0,
                testTypeName + assertionMsg0,
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());




        this.attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(), relationshipDef);

        if (this.attrList == null || this.attrList.isEmpty() || this.typeHasTestedInstanceProperties == false) {

            /*
             * If the TypeDef has NO attributes then it is not possible to perform a matchProperties find on instances of that type.
             * The MetadataCollection API does not accept null match properties.
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
         * MatchProperties with all properties and matchCriteria == ALL. This tests that every property is correctly compared.
         * In the following tests, all String values are passed by the repository helper's exact match helper method
         *
         * findEntitiesByProperty()
         *   1. Use instanceProperties for set 0 & matchCriteria ALL   - this should return all and only relationships in set 0
         *   2. Use instanceProperties for set 0 & matchCriteria ANY   - this should return all and only relationships in set 0
         *   3. Use instanceProperties for set 0 & matchCriteria NONE  - this should return all and only relationships in sets 1 & 2
         *   4. Use instanceProperties for set 1 & matchCriteria ALL  - this should return all and only relationships in set 1
         *   5. Use instanceProperties for set 1 & matchCriteria ANY  - this should return all and only relationships in sets 1 & 2
         *   6. Use instanceProperties for set 1 & matchCriteria NONE  - this should return all and only relationships in set 0
         *   7. Similar to test 1 (above) but tests typeGUID filtering - compares the number of instances returned
         *
         * findEntitiesByPropertyValue()
         *   8. Use searchCriteria that will match first string property in set 0 - this should return all and only relationships in set 0
         *   9. Use searchCriteria that will match first string property in sets 1 and 2 - this should return all and only relationships in set 1 and 2
         *
         * This testcase does not perform regular expression searches; they are in the corresponding advanced-search testcase.
         *
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

        List<TypeDefAttribute> attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(), relationshipDef);


        /*
         * There are attributes for this type - this was verified in the run() method
         */

        this.firstStringAttributeName = this.typeDefHasAtLeastOneStringProperty(attrList);
        this.multiSetTest = this.firstStringAttributeName != null;

        /*
         * Create the instance properties for each entity set. There is a chance that the type does not have any properties that
         * are used in the test (i.e. set by populatenstanceProperties(). This can be tested by checking if the populated instance
         * properties is null. In that case, set typeHasTestedInstanceProperties and no further find testing will be performed on
         * this type. If populatenstanceProperties() is extended in future this predicate may succeed - it will not need to be modified.
         */

        this.instProps0 = populateInstanceProperties(attrList, "0");

        if (this.instProps0 == null) {
            /*
             * populateinstanceProperties() did not generate any test properties- either because the type has none or those it has are not
             * supported by populateinstanceProperties().
             */
            this.typeHasTestedInstanceProperties = false;
            return;
        }



        if (this.multiSetTest) {
            this.instProps1 = populateInstanceProperties(attrList, "1");
            this.instProps2 = populateInstanceProperties(attrList, "2");
        }



        /*
         * Prepare literalised versions of instance properties for use as match properties in the subsequent searches
         */

        this.literalisedInstanceProperties0 = this.literaliseMatchProperties(workPad.getLocalServerUserId(), relationshipDef, this.instProps0);
        if (this.multiSetTest) {
            this.literalisedInstanceProperties1 = this.literaliseMatchProperties(workPad.getLocalServerUserId(), relationshipDef, this.instProps1);
            this.literalisedInstanceProperties2 = this.literaliseMatchProperties(workPad.getLocalServerUserId(), relationshipDef, this.instProps2);
        }


        /*
         * We cannot be sure that the repository under test supports metadata maintenance, so need to try and back off.
         */

        try {

            /*
             * Create two relationships for each set. Always create set 0
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
                    assertion34,
                    testTypeName + assertionMsg34,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        }
        catch (FunctionNotSupportedException exception) {

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

            super.addNotSupportedAssertion(assertion34,
                    assertionMsg34,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());


            return;
        }


    }

    private void populateRelationshipSet(OMRSMetadataCollection metadataCollection,
                                         List<Relationship> relationshipSet,
                                         InstanceProperties instanceProps) throws Exception
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
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());
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
        EntityDef    end1Type;
        EntityDetail end1;
        EntityDef    end2Type;
        EntityDetail end2;


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

            repositoryConformanceWorkPad.removeRelationshipInstanceSets(relationshipDef.getName());


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
                 * Delete the relationship and then both end entities.
                 * Deleting either entity first would delete the relationship, but
                 * this sequence is a little more orderly.
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
            else
                this.performFindsSingleSet(metadataCollection);
        }
    }


    private void performFindsMultiSet(OMRSMetadataCollection metadataCollection) throws Exception
    {


        /*
         * Do not perform existence assertions and content validations - these are tested in lifecycle tests.
         */

        InstanceProperties matchProperties = null;
        MatchCriteria matchCriteria = null;
        int fromElement = 0;
        List<Relationship> result = null;

        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 1. Use instanceProperties for set 0 & matchCriteria ALL   - this should return all and only relationships in set 0
         *  Since this is a nonregex test we need to use a literalised string for each string value
         */

        matchProperties = literalisedInstanceProperties0;
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
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_0)),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that any extra instances in the result are valid
         */

        boolean contamination = false;


        List<Relationship> expectedResult = new ArrayList<>();
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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), matchProperties, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }


        assertCondition((contamination == false),
                assertion3,
                testTypeName + assertionMsg3,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());







        /* ------------------------------------------------------------------------------------- */

        /*
         *
         *  Test 2. Use instanceProperties for set 0 & matchCriteria ANY   - this should return all and only relationships in set 0
         *
         */

        matchProperties = literalisedInstanceProperties0;
        matchCriteria = MatchCriteria.ANY;
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
                assertion4,
                testTypeName + assertionMsg4,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_0)),
                assertion5,
                testTypeName + assertionMsg5,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), matchProperties, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion6,
                testTypeName + assertionMsg6,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *
         * Test 3.  Use instanceProperties for set 0 & matchCriteria NONE  - this should return all and only relationships in sets 1 & 2
         */

        matchProperties = literalisedInstanceProperties0;
        matchCriteria = MatchCriteria.NONE;
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
                assertion7,
                testTypeName + assertionMsg7,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_1 and set_2 relationships
         */

        assertCondition((result.containsAll(relationshipSet_1) && result.containsAll(relationshipSet_2)),
                assertion8,
                testTypeName + assertionMsg8,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), this.instProps0, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion9,
                testTypeName + assertionMsg9,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */
        /*
         *  Test 4. Use instanceProperties for set 1 & matchCriteria ALL  - this should return all relationships in set 1
         *  If there is only one string property in the instance properties then it will also match the relationships in set 2
         */

        matchProperties = literalisedInstanceProperties1;
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
                assertion10,
                testTypeName + assertionMsg10,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_1 relationships (and set_2 if only one property exists, since they will overlap on that property)
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(relationshipSet_1);
        if (this.attrList.size() == 1)
            expectedResult.addAll(relationshipSet_2);

        assertCondition((result.containsAll(expectedResult)),
                assertion11,
                testTypeName + assertionMsg11,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), this.instProps1, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }



        assertCondition((contamination == false),
                assertion12,
                testTypeName + assertionMsg12,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 5. Use instanceProperties for set 1 & matchCriteria ANY  - this should return all and only relationships in sets 1 & 2
         */

        matchProperties = literalisedInstanceProperties1;
        matchCriteria = MatchCriteria.ANY;
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
                assertion13,
                testTypeName + assertionMsg13,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_1 and sdet_2 relationships
         */

        assertCondition((result.containsAll(relationshipSet_1) && result.containsAll(relationshipSet_2)),
                assertion14,
                testTypeName + assertionMsg14,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), this.instProps1, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }



        assertCondition((contamination == false),
                assertion15,
                testTypeName + assertionMsg15,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 6. Use instanceProperties for set 1 & matchCriteria NONE  - this should return all and only relationships in set 0
         */


        matchProperties = literalisedInstanceProperties1;
        matchCriteria = MatchCriteria.NONE;
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
                assertion16,
                testTypeName + assertionMsg16,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_0)),
                assertion17,
                testTypeName + assertionMsg17,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), this.instProps1, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion18,
                testTypeName + assertionMsg18,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());



        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 7. Type Filtering.
         *  Use instanceProperties for set 0 & matchCriteria ALL  and do not specify a type GUID. This should return at least the relationships for this type (and subtypes) in set 0
         *  Count how many result relationships are of the current relationship type or its subtypes and then repeat the test with the current type GUID.
         *  Ensure the counts match.
         *
         *  This test does not verify that the content of the result matches what would be expected - that is tested in other tests above. This test is concerned with type filtering.
         */

        matchProperties = literalisedInstanceProperties0;
        matchCriteria = MatchCriteria.ALL;
        fromElement = 0;

        /*
         * Perform the search without type filtering
         */
        result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                null,
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                0);


        int relationshipCount = 0;

        if (result != null) {
            /*
             * Count the relationships that are of current type...
             */
            String relationshipTypeName = relationshipDef.getName();

            for (Relationship relationship : result) {
                String typeName = relationship.getType().getTypeDefName();
                if (typeName.equals(relationshipTypeName)) {
                    relationshipCount++;
                }
            }
        }

        /*
         * Repeat the search being specific about type
         */
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
         * Verify that the result is not empty (it should always return entities) and that the size of the result matches the count above.
         */

        assertCondition((result != null),
                assertion19,
                testTypeName + assertionMsg19,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        assertCondition((result.size() == relationshipCount),
                assertion20,
                testTypeName + assertionMsg20,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());



        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 8. Use searchCriteria set to value of first string property for set 0 - this should return all and only relationships in set 0
         */

        /*
         * For consistency and maintainability, the search value is derived using the same utilities that the value generator uses.
         */
        String searchCriteria = null;
        String stringPropName = null;
        if (this.attrList != null || !(this.attrList.isEmpty())) {

            /*
             * Only run the test if the TypeDef has attributes (even inherited ones), otherwise it is not possible to perform a find by property value on instances of that type.
             */

            stringPropName = typeDefHasAtLeastOneStringProperty(attrList);
            PrimitivePropertyValue stringPropPPV = getPrimitivePropertyValue(stringPropName, new PrimitiveDef(OM_PRIMITIVE_TYPE_STRING), "0");
            String stringValue = (String) stringPropPPV.getPrimitiveValue();
            searchCriteria = this.literaliseStringProperty(stringValue);

            fromElement = 0;

            /*
             * Since this is a non-regex test we need to ask the repo helper to provide a literalised string for each of our string values
             */


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
                    assertion21,
                    testTypeName + assertionMsg21,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

            /*
             * Verify that the result of the above search includes all set_0 relationships of the current type
             */

            expectedResult = new ArrayList<>();
            expectedResult.addAll(relationshipSet_0);

            assertCondition((result.containsAll(expectedResult)),
                    assertion22,
                    testTypeName + assertionMsg22,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

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
                         * This instance is not a member of the expected result set, so check that it is a viable result.
                         * To do this we set up a matchProperties with just the string parameter from above.
                         */
                        InstanceProperties verifyProperties = addStringPropertyToInstance(null, stringPropName, searchCriteria);
                        boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), verifyProperties, MatchCriteria.ALL);
                        if (!match)
                            contamination = true;

                    }
                }
            }


            assertCondition((contamination == false),
                    assertion23,
                    testTypeName + assertionMsg23,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());
        }


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 9. Use searchCriteria set to value of overlapping string property for sets 1 & 2 - this should return entities in set 1 and 2
         */

        /*
         * For consistency and maintainability, the search value is derived using the same utilities that the value generator uses.
         */
        searchCriteria = null;
        stringPropName = null;
        if (this.attrList != null || !(this.attrList.isEmpty())) {

            /*
             * Only run the test if the TypeDef has attributes (even inherited ones), otherwise it is not possible to perform a find by property value on instances of that type.
             */

            stringPropName = typeDefHasAtLeastOneStringProperty(attrList);
            PrimitivePropertyValue stringPropPPV = getPrimitivePropertyValue(stringPropName, new PrimitiveDef(OM_PRIMITIVE_TYPE_STRING), "1");
            String stringValue = (String) stringPropPPV.getPrimitiveValue();
            searchCriteria = this.literaliseStringProperty(stringValue);

            fromElement = 0;

            /*
             * Since this is a non-regex test we need to ask the repo helper to provide a literalised string for each of our string values
             */


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
                    assertion24,
                    testTypeName + assertionMsg24,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

            /*
             * Verify that the result of the above search includes all set_1 and set_2 relationships of the current type
             */

            expectedResult = new ArrayList<>();
            expectedResult.addAll(relationshipSet_1);
            expectedResult.addAll(relationshipSet_2);

            assertCondition((result.containsAll(expectedResult)),
                    assertion25,
                    testTypeName + assertionMsg25,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

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
                         * This instance is not a member of the expected result set, so check that it is a viable result.
                         * To do this we set up a matchProperties with just the string parameter from above.
                         */
                        InstanceProperties verifyProperties = addStringPropertyToInstance(null, stringPropName, searchCriteria);
                        boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), verifyProperties, MatchCriteria.ALL);
                        if (!match)
                            contamination = true;

                    }
                }
            }


            assertCondition((contamination == false),
                    assertion26,
                    testTypeName + assertionMsg26,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());
        }



        /*
         * Completion of searches - indicate success of testcase.
         */
        super.setSuccessMessage("Relationships can be searched by property and property value");
    }


    private void performFindsSingleSet(OMRSMetadataCollection metadataCollection) throws Exception
    {


        /*
         * Do not perform existence assertions and content validations - these are tested in lifecycle tests.
         */

        InstanceProperties matchProperties = null;
        MatchCriteria matchCriteria = null;
        int fromElement = 0;
        List<Relationship> result = null;

        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 1. Use instanceProperties for set 0 & matchCriteria ALL   - this should return all and only relationships in set 0
         *  Since this is a nonregex test we need to use a literalised string for each string value
         */

        matchProperties = literalisedInstanceProperties0;
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
                assertion27,
                testTypeName + assertionMsg27,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_0)),
                assertion28,
                testTypeName + assertionMsg28,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that any extra instances in the result are valid
         */

        boolean contamination = false;

        List<Relationship> expectedResult = new ArrayList<>();
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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), this.instProps0, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion29,
                testTypeName + assertionMsg29,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());








        /* ------------------------------------------------------------------------------------- */

        /*
         *
         *  Test 2. Use instanceProperties for set 0 & matchCriteria ANY   - this should return all and only relationships in set 0
         *
         */

        matchProperties = literalisedInstanceProperties0;
        matchCriteria = MatchCriteria.ANY;
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
                assertion30,
                testTypeName + assertionMsg30,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 relationships
         */

        assertCondition((result.containsAll(relationshipSet_0)),
                assertion31,
                testTypeName + assertionMsg31,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), this.instProps0, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion32,
                testTypeName + assertionMsg32,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());




        /* ------------------------------------------------------------------------------------- */

        /*
         *
         * Test 3.  Use instanceProperties for set 0 & matchCriteria NONE  - this should return all and only relationships in sets 1 & 2
         */

        matchProperties = literalisedInstanceProperties0;
        matchCriteria = MatchCriteria.NONE;
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
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(rel.getProperties(), this.instProps0, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion33,
                testTypeName + assertionMsg33,
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());



        /*
         * Completion of searches - indicate success of testcase.
         */
        super.setSuccessMessage("Relationships can be searched by property and property value");
    }




    /**
     * Determine if properties are as expected.
     *
     * @param firstInstanceProps  is the target which must always be a non-null InstanceProperties
     * @param secondInstanceProps is the actual to be compared against first param - can be null, or empty....
     * @return match boolean
     */
    private boolean doPropertiesMatch(InstanceProperties firstInstanceProps, InstanceProperties secondInstanceProps)
    {
        boolean matchProperties = false;
        boolean noProperties = false;

        if ((secondInstanceProps == null) ||
                (secondInstanceProps.getInstanceProperties() == null) ||
                (secondInstanceProps.getInstanceProperties().isEmpty())) {
            noProperties = true;
        }

        if (noProperties) {
            if ((firstInstanceProps.getInstanceProperties() == null) ||
                    (firstInstanceProps.getInstanceProperties().isEmpty())) {
                matchProperties = true;
            }
        } else {
            // non-empty, perform matching

            Map<String, InstancePropertyValue> secondPropertiesMap = secondInstanceProps.getInstanceProperties();
            Map<String, InstancePropertyValue> firstPropertiesMap = firstInstanceProps.getInstanceProperties();

            boolean matchSizes = (secondPropertiesMap.size() == firstPropertiesMap.size());

            if (matchSizes) {
                Set<String> secondPropertiesKeySet = secondPropertiesMap.keySet();
                Set<String> firstPropertiesKeySet = firstPropertiesMap.keySet();

                boolean matchKeys = secondPropertiesKeySet.containsAll(firstPropertiesKeySet) &&
                        firstPropertiesKeySet.containsAll(secondPropertiesKeySet);

                if (matchKeys) {
                    // Assume the values match and prove it if they don't...
                    boolean matchValues = true;

                    Iterator<String> secondPropertiesKeyIterator = secondPropertiesKeySet.iterator();
                    while (secondPropertiesKeyIterator.hasNext()) {
                        String key = secondPropertiesKeyIterator.next();
                        if (!(secondPropertiesMap.get(key).equals(firstPropertiesMap.get(key)))) {
                            matchValues = false;
                        }
                    }

                    // If all property values matched....
                    if (matchValues) {
                        matchProperties = true;
                    }
                }
            }
        }

        return matchProperties;
    }


    /*
     * Method used to inspect whether an instance satisfies the natch criteria used in a search
     * If there are no matchProperties then the instance is deemed to match (since matchProperties act as a filter).
     * Return true if the instance matches.
     */

    private boolean doInstancePropertiesSatisfyMatchCriteria(InstanceProperties instProps, InstanceProperties matchProps, MatchCriteria matchCriteria)
    {
        boolean match = false;

        if (matchProps == null || matchProps.getInstanceProperties() == null || matchProps.getInstanceProperties().isEmpty()) {
            return true;
        }

        /*
         * Already asserted that mp is a non-empty map
         */
        Map<String,InstancePropertyValue> ip = instProps.getInstanceProperties();
        Map<String,InstancePropertyValue> mp = matchProps.getInstanceProperties();
        Set<String> mpNameSet = mp.keySet();
        Iterator<String> mpNameIterator = mpNameSet.iterator();

        switch (matchCriteria) {

            case ALL:
                /*
                 * Assume true unless proven otherwise
                 */
                match = true;
                while (mpNameIterator.hasNext()) {
                    String mpName = mpNameIterator.next();

                    InstancePropertyValue mpValue = mp.get(mpName);
                    InstancePropertyValue ipValue = ip.get(mpName);

                    /*
                     * If either or both values are null assert that they do not match. If both are non-null compare them
                     */
                    if (mpValue == null)
                        match = false;
                    else if (ipValue == null)
                        match = false;
                    else if (!mpValue.equals(ipValue))
                        match = false;
                }
                break;

            case ANY:
                /*
                 * Assume false unless proven otherwise
                 */
                match = false;
                while (mpNameIterator.hasNext()) {
                    String mpName = mpNameIterator.next();

                    InstancePropertyValue mpValue = mp.get(mpName);
                    InstancePropertyValue ipValue = ip.get(mpName);

                    /*
                     * If either or both values are null assert that they do not match. If both are non-null compare them
                     */
                    if (mpValue == null)
                        match = false;
                    else if (ipValue == null)
                        match = false;
                    else if (mpValue.equals(ipValue))
                        match = true;
                }
                break;

            case NONE:
                /*
                 * Assume true unless proven otherwise
                 */
                match = true;
                while (mpNameIterator.hasNext()) {
                    String mpName = mpNameIterator.next();

                    InstancePropertyValue mpValue = mp.get(mpName);
                    InstancePropertyValue ipValue = ip.get(mpName);

                    /*
                     * If either or both values are null assert that they do not match. If both are non-null compare them
                     */
                    if (mpValue == null)
                        match = false;
                    else if (ipValue == null)
                        match = false;
                    else if (mpValue.equals(ipValue))
                        match = false;
                }
                break;
        }
        return match;
    }




    /**
     * Determine if properties are mismatched - i.e. if there are any clashing properties.
     *
     * If either or both InstanceProperties object is null or empty there is no clash
     * If both a re non-null and non-empty then any properties with the same key must have matching values.
     *
     * @param firstInstanceProps is the target which must always be a non-null InstanceProperties
     * @param secondInstanceProps is the actual to be compared against first param - can be null, or empty....
     * @return match boolean
     */
    private boolean doPropertiesClash(InstanceProperties firstInstanceProps, InstanceProperties secondInstanceProps)
    {
        boolean clash = false;


        if ( (firstInstanceProps == null) ||
                (firstInstanceProps.getInstanceProperties() == null) ||
                (firstInstanceProps.getInstanceProperties().isEmpty()))
        {
            return false;
        }

        if ( (secondInstanceProps == null) ||
                (secondInstanceProps.getInstanceProperties() == null) ||
                (secondInstanceProps.getInstanceProperties().isEmpty()))
        {
            return false;
        }

        /*
         * Both InstanceProperties objects are non-null and have properties...perform matching
         */


        Map<String, InstancePropertyValue> firstPropertiesMap  = firstInstanceProps.getInstanceProperties();

        Map<String, InstancePropertyValue> secondPropertiesMap = secondInstanceProps.getInstanceProperties();

        Set<String> firstPropertiesKeySet  = firstPropertiesMap.keySet();
        Iterator<String> firstPropertiesKeyIterator = firstPropertiesKeySet.iterator();

        while (firstPropertiesKeyIterator.hasNext()) {
            /*
             * See whether secondInstancePropertirs has a value under this key
             */

            String key = firstPropertiesKeyIterator.next();
            InstancePropertyValue value1 = firstPropertiesMap.get(key);
            InstancePropertyValue value2 = secondPropertiesMap.get(key);

            if (value2 != null && !(value1.equals(value2))) {
                return true;
            }
        }

        return clash;

    }

    /**
     * Return type def attributes for the properties defined in the TypeDef and all of its supertypes
     *
     * @param userId  calling user
     * @param typeDef the definition of the type
     * @return properties for an instance of this type
     * @throws Exception problem manipulating types
     */
    protected List<TypeDefAttribute> getAllPropertiesForTypedef(String userId, TypeDef typeDef) throws Exception
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
    protected InstanceProperties populateInstanceProperties(List<TypeDefAttribute> attrList, String setName)
    {

        /*
         * Get the trivial case out of the way
         */
        if (attrList == null) {
            return null;
        }

        InstanceProperties properties = null;

        Map<String, InstancePropertyValue> propertyMap = new HashMap<>();



        for (TypeDefAttribute typeDefAttribute : attrList) {

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

            switch (category) {
                case PRIMITIVE:
                    PrimitiveDef primitiveDef = (PrimitiveDef) attributeType;
                    propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef, modifier));
                    break;
            }
        }

        if (!propertyMap.isEmpty()) {
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
    private PrimitivePropertyValue getPrimitivePropertyValue(String propertyName,
                                                             PrimitiveDef propertyType,
                                                             String modifier)
    {
        String strModifier = modifier;
        Integer intModifier = Integer.parseInt(modifier);

        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(propertyType.getPrimitiveDefCategory());

        switch (propertyType.getPrimitiveDefCategory()) {
            case OM_PRIMITIVE_TYPE_STRING:
                propertyValue.setPrimitiveValue(propertyName + "." + strModifier);
                break;
            case OM_PRIMITIVE_TYPE_DATE:
                propertyValue.setPrimitiveValue(100L + new Long(strModifier));
                break;
            case OM_PRIMITIVE_TYPE_INT:
                propertyValue.setPrimitiveValue(42 + intModifier);
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
                propertyValue.setPrimitiveValue(new Long(2000 * intModifier));
                break;
            case OM_PRIMITIVE_TYPE_FLOAT:
                propertyValue.setPrimitiveValue(new Float(3.14159 * intModifier));
                break;
            case OM_PRIMITIVE_TYPE_DOUBLE:
                propertyValue.setPrimitiveValue(new Double(1000000 * intModifier));
                break;
            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                propertyValue.setPrimitiveValue(new Double(1000000 * intModifier));
                break;
            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                propertyValue.setPrimitiveValue(new Double(1000000 * intModifier));
                break;
            case OM_PRIMITIVE_TYPE_UNKNOWN:
                break;
        }

        return propertyValue;
    }


    public InstanceProperties addStringPropertyToInstance(InstanceProperties properties,
                                                          String propertyName,
                                                          String propertyValue)
    {
        InstanceProperties resultingProperties;

        if (propertyValue != null) {

            if (properties == null) {

                resultingProperties = new InstanceProperties();
            } else {
                resultingProperties = properties;
            }


            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

            primitivePropertyValue.setPrimitiveDefCategory(OM_PRIMITIVE_TYPE_STRING);
            primitivePropertyValue.setPrimitiveValue(propertyValue);
            primitivePropertyValue.setTypeName(OM_PRIMITIVE_TYPE_STRING.getName());
            primitivePropertyValue.setTypeGUID(OM_PRIMITIVE_TYPE_STRING.getGUID());

            resultingProperties.setProperty(propertyName, primitivePropertyValue);

            return resultingProperties;
        } else {
            return properties;
        }
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

        } catch (Exception e) {
            return null;  // This should force an InvalidParameterException from the MDC under test.
        }

    }

    /*
     * Method to check whether the typeDef has at least one attribute of type String, and if so return the attribute name.
     * If there is no String attribute in the type return null.
     */

    private String typeDefHasAtLeastOneStringProperty(List<TypeDefAttribute> attrList)
    {


        for (TypeDefAttribute typeDefAttribute : attrList) {
            String attributeName = typeDefAttribute.getAttributeName();
            AttributeTypeDef attributeType = typeDefAttribute.getAttributeType();
            AttributeTypeDefCategory category = attributeType.getCategory();

            switch (category) {
                case PRIMITIVE:
                    PrimitiveDef primitiveDef = (PrimitiveDef) attributeType;
                    PrimitiveDefCategory pdCat = primitiveDef.getPrimitiveDefCategory();
                    if (pdCat == OM_PRIMITIVE_TYPE_STRING)
                        return attributeName;
                    break;
            }
        }

        return null;

    }

}
