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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;




/**
 * Test that all defined entities can be retrieved by property searches.
 */
public class TestSupportedEntityPropertyAdvancedSearch extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-entity-property-advanced-search";
    private static final String testCaseName = "Repository entity property advanced search test case";

    /* Type */
    private static final String assertion0 = testCaseId + "-00";
    private static final String assertionMsg0 = " entity type definition matches known type  ";



    /* Test 1 */
    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " search returned results.";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " search contained all expected results.";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " search contained only valid results.";

    /* Test 2 */
    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " search contained only valid results.";

    /* Test 3 */
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " search returned results.";
    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " search contained all expected results.";
    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " search contained only valid results.";

    /* Test 4 */
    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " search returned results.";
    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " search contained all expected results.";
    private static final String assertion10    = testCaseId + "-10";
    private static final String assertionMsg10 = " search contained only valid results.";

    /* Test 5 */
    private static final String assertion11     = testCaseId + "-11";
    private static final String assertionMsg11  = " search returned results.";
    private static final String assertion12     = testCaseId + "-12";
    private static final String assertionMsg12  = " search contained all expected results.";
    private static final String assertion13     = testCaseId + "-13";
    private static final String assertionMsg13  = " search contained only valid results.";

    /* Test 6 */
    private static final String assertion14     = testCaseId + "-14";
    private static final String assertionMsg14  = " search returned results.";
    private static final String assertion15     = testCaseId + "-15";
    private static final String assertionMsg15 = " search contained all expected results.";
    private static final String assertion16     = testCaseId + "-16";
    private static final String assertionMsg16  = " search contained only valid results.";

    /* Test 7 */
    private static final String assertion17     = testCaseId + "-17";
    private static final String assertionMsg17  = " value search returned results.";
    private static final String assertion18     = testCaseId + "-18";
    private static final String assertionMsg18  = " value search contained all expected results.";
    private static final String assertion19     = testCaseId + "-19";
    private static final String assertionMsg19  = " value search contained only valid results.";

    /* Test 8 */
    private static final String assertion20     = testCaseId + "-20";
    private static final String assertionMsg20  = " value search returned results.";
    private static final String assertion21     = testCaseId + "-21";
    private static final String assertionMsg21 = " value search contained all expected results.";
    private static final String assertion22     = testCaseId + "-22";
    private static final String assertionMsg22  = " value search contained only valid results.";


    private static final String assertion23     = testCaseId + "-23";
    private static final String assertionMsg23  = " repository supports creation of instances.";

    private static final String discoveredProperty_searchSupport       = " advanced search support";


    private RepositoryConformanceWorkPad workPad;
    private String            metadataCollectionId;
    private EntityDef         entityDef;
    private List<TypeDefAttribute> attrList;
    private String            testTypeName;


    private boolean           multiSetTest = false;
    private String            firstStringAttributeName = null;

    private InstanceProperties instProps0;
    private InstanceProperties instProps1;
    private InstanceProperties instProps2;

    List<EntityDetail> entitySet_0 = null;
    List<EntityDetail> entitySet_1 = null;
    List<EntityDetail> entitySet_2 = null;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntityPropertyAdvancedSearch(RepositoryConformanceWorkPad workPad,
                                                     EntityDef               entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        this.workPad = workPad;
        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                                                    testCaseId,
                                                    testCaseName);

        this.instProps0 = null;
        this.instProps1 = null;
        this.instProps2 = null;

        this.entitySet_0 = null;
        this.entitySet_1 = null;
        this.entitySet_2 = null;


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
         * Create a set of entities of the defined type in the TUT repository, with different/overlapping property values.
         * The entity is not classified - for find by classification tests see the corresponding testcase (TestSupportedEntityClassificationSearch)
         * It will then conduct a series of searches against the repository - some of which should retrieve the entity, others should not.
         *
         * The following searches are performed:
         *   Find By Instance (Match) Properties
         *   Find By Property Value
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

        EntityDef knownEntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), entityDef.getName());
        verifyCondition((entityDef.equals(knownEntityDef)),
                assertion0,
                testTypeName + assertionMsg0,
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());



        this.attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(),entityDef);

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
         *   There are three sets of entities (named 0, 1 & 2).
         *   There will be two entities per set, to ensure multiple 'hits'
         *   The sets of entities and their property values are as follows:
         *
         *   Entity set 0:   base values for all properties, formed as <property-name>.<set> - e.g. qualifiedName.0
         *   Entity set 1:   distinct from e0 but overlaps with e2
         *   Entity set 2:   distinct from e0 but overlaps with e1, on the first property
         *
         * Searches:
         *
         * These tests are only run for types with at least one String property; the first String property is searched using a Regex.
         *
         * findEntitiesByProperty()
         *   1. Use instanceProperties with <propertyName> of first String property, MatchCriteria set to ALL - this should return all entities in sets 0, 1 & 2
         *   2. Similar to test 1 but with MatchCriteria set to NONE - this should not return any return entities in sets 0, 1 or 2
         *   3. Use instanceProperties with <propertyName>\.[0] of first String property - this should return all and only entities in set 0
         *   4. Similar to test 3 but with MatchCriteria set to NONE - this should not return any return entities in sets 1 & 2
         *   5. Use instanceProperties with <propertyName>\.[^12] of first String property - this should return all and only entities in set 0
         *   6. Similar to test 5 but with MatchCriteria set to NONE - this should not return any return entities in sets 1 & 2
         *
         * findEntitiesByPropertyValue()
         *   7. Use searchCriteria regex that will match first string property in set 0 - this should return all and only entities in set 0
         *   8. Use searchCriteria regex that will match first string property in sets 1 and 2 - this should return all and only entities in set 1 and 2
         *
         */


        switch (phase) {

            case SEED:
                this.createInstances( metadataCollection );
                break;
            case EXECUTE:
                this.performFinds( metadataCollection );
                break;
            case CLEAN:
                this.cleanInstances( metadataCollection );
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
         * instProps0 is always distinct.
         * instProps1 and instProps2 are distinct apart from first prop - for which they have the same value.
         *
         *  Generate property values for all the type's defined properties, including inherited properties
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
             * Create two entities for each set.
             */

            this.entitySet_0 = new ArrayList<>();

            entitySet_0.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps0, null, null));
            entitySet_0.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps0, null, null));

            if (this.multiSetTest) {

                this.entitySet_1 = new ArrayList<>();

                entitySet_1.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps1, null, null));
                entitySet_1.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps1, null, null));

                this.entitySet_2 = new ArrayList<>();

                entitySet_2.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps2, null, null));
                entitySet_2.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps2, null, null));
            }


            repositoryConformanceWorkPad.addEntityInstanceSets(entityDef.getName(), this.entitySet_0, this.entitySet_1, this.entitySet_2);

            assertCondition((true),
                    assertion23,
                    testTypeName + assertionMsg23,
                    RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

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

            super.addNotSupportedAssertion(assertion23,
                    assertionMsg23,
                    RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());


            return;
        }


    }

    private void cleanInstances(OMRSMetadataCollection metadataCollection) throws Exception
    {

        List<EntityDetail> workpad_set0 = repositoryConformanceWorkPad.getEntityInstanceSet(entityDef.getName(), 0);

        if (workpad_set0 != null) {

            /*
             * Instances were created for set0; so may also have been created for other sets (if multi-set). Clean up all instance sets
             */


            /*
             * Clean up all entities created by this testcase
             */

            repositoryConformanceWorkPad.removeEntityInstanceSets(entityDef.getName());


            for (EntityDetail entity : entitySet_0) {

                try {
                    metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                            entity.getType().getTypeDefGUID(),
                            entity.getType().getTypeDefName(),
                            entity.getGUID());
                } catch (FunctionNotSupportedException exception) {
                    // NO OP - can proceed to purge
                }

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                        entity.getType().getTypeDefGUID(),
                        entity.getType().getTypeDefName(),
                        entity.getGUID());
            }

            if (this.multiSetTest) {

                for (EntityDetail entity : entitySet_1) {

                    try {
                        metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                entity.getType().getTypeDefGUID(),
                                entity.getType().getTypeDefName(),
                                entity.getGUID());
                    } catch (FunctionNotSupportedException exception) {
                        // NO OP - can proceed to purge
                    }

                    metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                            entity.getType().getTypeDefGUID(),
                            entity.getType().getTypeDefName(),
                            entity.getGUID());
                }

                for (EntityDetail entity : entitySet_2) {

                    try {
                        metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                entity.getType().getTypeDefGUID(),
                                entity.getType().getTypeDefName(),
                                entity.getGUID());
                    } catch (FunctionNotSupportedException exception) {
                        // NO OP - can proceed to purge
                    }

                    metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                            entity.getType().getTypeDefGUID(),
                            entity.getType().getTypeDefName(),
                            entity.getGUID());
                }
            }
        }
    }

    private void performFinds(OMRSMetadataCollection metadataCollection) throws Exception
    {

        List<EntityDetail> workpad_set0 = repositoryConformanceWorkPad.getEntityInstanceSet(entityDef.getName(), 0);

        if (workpad_set0 != null) {

            /*
             * Instances were created for set0; so may also have been created for other sets (if multi-set). Run the tests
             */
            if (this.multiSetTest)
                this.performFindsMultiSet(metadataCollection);
            else {
                /*
                 * If not multiSet t is because there are no string properties in the type - you cannot do a regex test in this case.
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
        List<EntityDetail> result          = null;

        boolean contamination;


        /* ------------------------------------------------------------------------------------- */


        String regex = null;

        /*
         *  Test 1.  Use instanceProperties with <propertyName> of first String property - matchCriteria ALL
         *  Regular expression is set to any string that includes the property name.
         *  This should return all entities in sets 0, 1 & 2
         */


        regex = ".*"+this.firstStringAttributeName+".*";

        matchProperties = new InstanceProperties();
        matchProperties = this.addStringPropertyToInstance(matchProperties, this.firstStringAttributeName, regex);
        matchCriteria = MatchCriteria.ALL;
        fromElement = 0;


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
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion1,
                testTypeName + assertionMsg1,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all entities in sets 0, 1 & 2 of the current type and its subtypes
         */

        List<EntityDetail> expectedResult = new ArrayList<>();
        expectedResult.addAll(entitySet_0);
        expectedResult.addAll(entitySet_1);
        expectedResult.addAll(entitySet_2);
        List<String> subTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityDef.getName());
        if (subTypeNames != null) {
            for (String subTypeName : subTypeNames) {
                List<EntityDetail> subTypeInstances;
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 0);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 1);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 2);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
            }
        }

        assertCondition((result.containsAll(expectedResult)),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (EntityDetail ent : result) {
                if (!(expectedResult.contains(ent))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(ent.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion3,
                testTypeName + assertionMsg3,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());




        /* ------------------------------------------------------------------------------------- */


        /*
         *
         *  Test 2.  Use instanceProperties with <propertyName> of first String property - and MatchCriteria NONE
         *  Regular expression is still set to any string that includes the property name.
         *  The result set should be empty.
         */

        matchCriteria = MatchCriteria.NONE;


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
                0);



        /*
         * Not expecting any results but verify that any extra instances in the result are valid
         */

        contamination = false;

        expectedResult = new ArrayList<>();  // deliberate empty list


        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (EntityDetail ent : result) {
                if (!(expectedResult.contains(ent))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(ent.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion4,
                testTypeName + assertionMsg4,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());




        /* ------------------------------------------------------------------------------------- */


        /*
         *  Test 3. Use instanceProperties with <propertyName>\.[0] of first String property - matchCriteria ALL
         *  Regular expression is set to explicitly match the value generated for set 0 only.
         *  This should return all and only entities in set 0
         */

        regex = this.firstStringAttributeName+"\\.[0]";

        matchProperties = new InstanceProperties();
        matchProperties = this.addStringPropertyToInstance(matchProperties, this.firstStringAttributeName, regex);
        matchCriteria = MatchCriteria.ALL;
        fromElement = 0;


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
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion5,
                testTypeName + assertionMsg5,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 entities of the current type and its subtypes
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(entitySet_0);

        subTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityDef.getName());
        if (subTypeNames != null) {
            for (String subTypeName : subTypeNames) {
                List<EntityDetail> subTypeInstances;
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 0);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
            }
        }

        assertCondition((result.containsAll(expectedResult)),

                assertion6,
                testTypeName + assertionMsg6,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (EntityDetail ent : result) {
                if (!(expectedResult.contains(ent))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(ent.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }



        assertCondition((contamination == false),
                assertion7,
                testTypeName + assertionMsg7,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *
         *  Test 4. Use instanceProperties with <propertyName>\.[0] of first String property - matchCriteria NONE
         *  Regular expression is still set to explicitly match the value generated for set 0 only.
         *  This should return sets 1 & 2 only.
         */


        matchCriteria = MatchCriteria.NONE;


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
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion8,
                testTypeName + assertionMsg8,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_1 and set_2 entities of the current type and its subtypes
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(entitySet_1);
        expectedResult.addAll(entitySet_2);
        subTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityDef.getName());
        if (subTypeNames != null) {
            for (String subTypeName : subTypeNames) {
                List<EntityDetail> subTypeInstances;
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 1);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 2);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
            }
        }

        assertCondition((result.containsAll(expectedResult)),
                assertion9,
                testTypeName + assertionMsg9,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (EntityDetail ent : result) {
                if (!(expectedResult.contains(ent))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(ent.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }


        assertCondition((contamination == false),
                assertion10,
                testTypeName + assertionMsg10,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */


        /*
         *  Test 5. Use instanceProperties with <propertyName>\.[^12] of first String property - matchCriteria ALL
         *  Regular expression is set to explicitly match any set values but NOT the values generated for sets 1 & 2.
         *  This should return all and only entities in set 0
         */

        regex = this.firstStringAttributeName+"\\.[^12]";

        matchProperties = new InstanceProperties();
        matchProperties = this.addStringPropertyToInstance(matchProperties, this.firstStringAttributeName, regex);  // deliberately using name as value too
        matchCriteria = MatchCriteria.ALL;
        fromElement = 0;


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
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion11,
                testTypeName + assertionMsg11,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 entities of the current type and its subtypes
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(entitySet_0);
        subTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityDef.getName());
        if (subTypeNames != null) {
            for (String subTypeName : subTypeNames) {
                List<EntityDetail> subTypeInstances;
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 0);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
            }
        }

        assertCondition((result.containsAll(expectedResult)),
                assertion12,
                testTypeName + assertionMsg12,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;


        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (EntityDetail ent : result) {
                if (!(expectedResult.contains(ent))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(ent.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }



        assertCondition((contamination == false),
                assertion13,
                testTypeName + assertionMsg13,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 6. Use instanceProperties with <propertyName>\.[^12] of first String property - matchCriteria NONE
         *  Regular expression is still set to explicitly match any set values but NOT the values generated for sets 1 & 2.
         *  This should return all and only entities in sets 1 & 2 only.
         */


        matchCriteria = MatchCriteria.NONE;

        matchProperties = new InstanceProperties();
        matchProperties = this.addStringPropertyToInstance(matchProperties, this.firstStringAttributeName, regex);  // deliberately using name as value too
        fromElement = 0;



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
                0);


        /*
         * Verify that the result of the above search is not empty
         */

        assertCondition((result != null),
                assertion14,
                testTypeName + assertionMsg14,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_1 and set_2 entities of the current type and its subtypes
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(entitySet_1);
        expectedResult.addAll(entitySet_2);
        subTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityDef.getName());
        if (subTypeNames != null) {
            for (String subTypeName : subTypeNames) {
                List<EntityDetail> subTypeInstances;
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 1);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 2);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
            }
        }

        assertCondition((result.containsAll(expectedResult)),
                assertion15,
                testTypeName + assertionMsg15,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (EntityDetail ent : result) {
                if (!(expectedResult.contains(ent))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(ent.getProperties(), firstStringAttributeName, regex, matchCriteria);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion16,
                testTypeName + assertionMsg16,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 7. Use searchCriteria regex that will match first string property in set 0 - this should return all and only entities in set 0
         *
         */

        regex = this.firstStringAttributeName+"\\.0";
        String searchCriteria = regex;
        fromElement = 0;


        result = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                entityDef.getGUID(),
                searchCriteria,
                fromElement,
                null,
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
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 entities of the current type and its subtypes
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(entitySet_0);
        subTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityDef.getName());
        if (subTypeNames != null) {
            for (String subTypeName : subTypeNames) {
                List<EntityDetail> subTypeInstances;
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 0);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
            }
        }

        assertCondition((result.containsAll(expectedResult)),
                assertion18,
                testTypeName + assertionMsg18,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (EntityDetail ent : result) {
                if (!(expectedResult.contains(ent))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(ent.getProperties(), firstStringAttributeName, regex, MatchCriteria.ALL);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion19,
                testTypeName + assertionMsg19,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());


        /* ------------------------------------------------------------------------------------- */

        /*
         *  Test 8. Use searchCriteria regex that will match the overlapping property in sets 1 ad 2 - this should return all entities in sets 1 and 2
         *
         */

        regex = this.firstStringAttributeName+"\\.1";
        searchCriteria = regex;
        fromElement = 0;


        result = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                entityDef.getGUID(),
                searchCriteria,
                fromElement,
                null,
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
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_1 and set_2 entities of the current type and its subtypes
         */

        expectedResult = new ArrayList<>();
        expectedResult.addAll(entitySet_1);
        expectedResult.addAll(entitySet_2);
        subTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityDef.getName());
        if (subTypeNames != null) {
            for (String subTypeName : subTypeNames) {
                List<EntityDetail> subTypeInstances;
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 1);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
                subTypeInstances = repositoryConformanceWorkPad.getEntityInstanceSet(subTypeName, 2);
                if (subTypeInstances != null)
                    expectedResult.addAll(subTypeInstances);
            }
        }

        assertCondition((result.containsAll(expectedResult)),
                assertion21,
                testTypeName + assertionMsg21,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());


        /*
         * Verify that any extra instances in the result are valid
         */

        contamination = false;

        if (result != null && (result.size() > expectedResult.size())) {
            /*
             * There are additional results.
             */
            for (EntityDetail ent : result) {
                if (!(expectedResult.contains(ent))) {
                    /*
                     * This instance is not a member of the expected result set, so check that it is a viable result
                     */
                    boolean match = this.doInstancePropertiesSatisfyMatchCriteria(ent.getProperties(), firstStringAttributeName, regex, MatchCriteria.ALL);
                    if (!match)
                        contamination = true;

                }
            }
        }

        assertCondition((contamination == false),
                assertion22,
                testTypeName + assertionMsg22,
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());





        super.setSuccessMessage("Entities can be searched by property and property value");
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
