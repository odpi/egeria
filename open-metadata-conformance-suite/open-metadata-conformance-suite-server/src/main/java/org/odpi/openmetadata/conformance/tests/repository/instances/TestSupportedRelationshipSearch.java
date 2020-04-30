/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
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

import java.text.MessageFormat;
import java.util.*;

import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;


/**
 * Test that all defined relationships can be retrieved by property searches.
 *
 * This testcase covers relationship searches using basic and advanced search criteria/values.
 * The difference is:
 *    basic     = only literal values or repo helper regexes can be used for values of string match properties or as searchCriteria
 *    advanced  = arbitrary regexes can be used for values of string match properties or as searchCriteria
 *
 */
public class TestSupportedRelationshipSearch extends RepositoryConformanceTestCase
{
    private static final String TEST_CASE_ID      = "repository-relationship-property-search";
    private static final String TEST_CASE_NAME    = "Repository relationship property search test case";

    private static final String ASSERTION_1       = TEST_CASE_ID + "-01";
    private static final String ASSERTION_MSG_1   = " relationship type matches the known type from the repository helper.";

    private static final String ASSERTION_2       = TEST_CASE_ID + "-02";
    private static final String ASSERTION_MSG_2   = " repository does not support an entity type that can be used to test relationship type ";

    private static final String ASSERTION_2_A     = TEST_CASE_ID + "-2a";
    private static final String ASSERTION_MSG_2_A = "repository supports searching for relationships of type ";

    private static final String ASSERTION_3       = TEST_CASE_ID + "-03";
    private static final String ASSERTION_MSG_3   = "repository supports creation of instances of type ";

    private static final String ASSERTION_4       = TEST_CASE_ID + "-04";
    private static final String ASSERTION_MSG_4   = "findRelationshipsByProperty found {0}/{1} expected results using parameters: {2}";

    private static final String ASSERTION_5       = TEST_CASE_ID + "-05";
    private static final String ASSERTION_MSG_5   = "findRelationshipsByProperty returned {0} unexpected results using parameters: {1}";

    private static final String ASSERTION_6       = TEST_CASE_ID + "-06";
    private static final String ASSERTION_MSG_6   = ASSERTION_MSG_4;

    private static final String ASSERTION_7       = TEST_CASE_ID + "-07";
    private static final String ASSERTION_MSG_7   = ASSERTION_MSG_5;

    private static final String ASSERTION_8       = TEST_CASE_ID + "-08";
    private static final String ASSERTION_MSG_8   = "findRelationshipsByPropertyValue found {0}/{1} expected results using parameters: {2}";

    private static final String ASSERTION_9       = TEST_CASE_ID + "-09";
    private static final String ASSERTION_MSG_9   = "findRelationshipsByPropertyValue found {0} unexpected results using parameters: {1}";

    private static final String ASSERTION_10      = TEST_CASE_ID + "-10";
    private static final String ASSERTION_MSG_10  = ASSERTION_MSG_4;

    private static final String ASSERTION_11      = TEST_CASE_ID + "-11";
    private static final String ASSERTION_MSG_11  = ASSERTION_MSG_5;

    private static final String ASSERTION_12      = TEST_CASE_ID + "-12";
    private static final String ASSERTION_MSG_12  = "findRelationshipsByPropertyValue with general regex found {0}/{1} expected results using parameters: {2}";

    private static final String ASSERTION_13      = TEST_CASE_ID + "-13";
    private static final String ASSERTION_MSG_13  = "findRelationshipsByPropertyValue with general regex found {0} unexpected results using parameters: {1}";

    private static final String ASSERTION_14      = TEST_CASE_ID + "-14";
    private static final String ASSERTION_MSG_14  = "findRelationshipsByProperty with general regex found {0}/{1} expected results using parameters: {2}";

    private static final String ASSERTION_15      = TEST_CASE_ID + "-15";
    private static final String ASSERTION_MSG_15  = "findRelationshipsByProperty with general regex found {0} unexpected results using parameters: {1}";

    private static final String ASSERTION_101     = TEST_CASE_ID + "-101";
    private static final String ASSERTION_MSG_101 = "findRelationshipsByPropertyValue supports general regular expressions: ";

    private static final String ASSERTION_102     = TEST_CASE_ID + "-102";
    private static final String ASSERTION_MSG_102 = "findRelationshipsByProperty supports general regular expressions: ";

    private static final String MISSING_EXPECTED_GUIDS = "(results missing expected GUIDs)";

    private static final String V_NULL = "null";


    private RepositoryConformanceWorkPad              workPad;
    private String                                    metadataCollectionId;
    private OMRSMetadataCollection                    metadataCollection;
    private RelationshipDef                           relationshipDef;
    private Map<String,EntityDef>                     entityDefs;
    private List<TypeDefAttribute>                    attrList;
    private String                                    testTypeName;

    private List<Relationship>                        knownInstances;
    private List<String>                              knownInstancesGUIDs;
    private List<Relationship>                        createdInstances;

    private List<EntityDetail>                        createdEntityInstances;

    private boolean                                   pageLimited;
    private int                                       pageSize;

    private List<String>                              uniqueAttributeNames;
    private List<String>                              definedAttributeNames;

    private Map<String, Map <Object, List<String>>>   propertyValueMap;
    private Map<String,PrimitiveDefCategory>          propertyCatMap;





    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDefs the entity types supported by the repository under test
     * @param relationshipDef type of valid relationships
     */
    public TestSupportedRelationshipSearch(RepositoryConformanceWorkPad   workPad,
                                           Map<String,EntityDef>          entityDefs,
                                           RelationshipDef                relationshipDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

        this.workPad               = workPad;
        this.metadataCollectionId  = workPad.getTutMetadataCollectionId();
        this.relationshipDef       = relationshipDef;
        this.entityDefs            = entityDefs;

        this.testTypeName          = this.updateTestIdByType(relationshipDef.getName(), TEST_CASE_ID, TEST_CASE_NAME);

        this.knownInstances        = null;
        this.knownInstancesGUIDs   = null;
        this.createdInstances      = null;

        this.pageLimited           = false;

        this.uniqueAttributeNames  = new ArrayList<>();
        this.definedAttributeNames = new ArrayList<>();

        this.propertyValueMap      = new HashMap<>();
        this.propertyCatMap        = new HashMap<>();

        this.pageSize              = getMaxSearchResults();




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
                        ASSERTION_1,
                        testTypeName + ASSERTION_MSG_1,
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());


        /*
         * Take a look at the attributes for the type being tested
         */

        this.attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(), relationshipDef);


        if (this.attrList != null && !(this.attrList.isEmpty())) {

            /*
             * If the TypeDef has NO attributes then it is not possible to perform a matchProperties or searchCriteria find on instances of that type.
             * The MetadataCollection API does not accept null match properties and a searchCritera search on something with no values would be pointless.
             */

            /*
             * The TypeDef has defined attributes. Create a List<String> of just the attribute names.
             * Also identify any attributes that are defined as unique as they need to be created distinct, and
             * may be used for search narrowing.
             * This is only collated for primitives.
             */
            for (TypeDefAttribute typeDefAttribute : attrList) {
                if (typeDefAttribute.getAttributeType().getCategory() == AttributeTypeDefCategory.PRIMITIVE) {
                    definedAttributeNames.add(typeDefAttribute.getAttributeName());
                    if (typeDefAttribute.isUnique()) {
                        uniqueAttributeNames.add(typeDefAttribute.getAttributeName());
                    }
                }
            }
        }


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
         * This is a 3-phase testcase. Each phase should be called in the obvious order.
         *
         * Phase 1: Looks for exisyting instances; if found then they are recorded - in the next phase the test will be run against those instances.
         *          If there were no existing instances this phase will try to create a representative set of instances (based on the attributes
         *          defined for the type under test. It will strive to achieve variety and depth. Again the instances are recorded.
         *          If there were no existing instances and the repository connector does not support creation (i.e. is read-only) then the test
         *          is abandoned so it should result in UNKNOWN_STATUS.
         *
         * Phase 2: Based on the recorded instances from phase 1, this phase executes the actual tests.
         *
         * Phase 3: This is the clean up phase - if any instances were created they are cleaned up.
         *
         * This test case does not classify relationships or search by classification - they are tested in the accompanying TestSupportedClassificationLifecycle
         * and TestSupportedClassificationSearch testcases.
         *
         *
         * The following searches are performed:
         *   Find By Instance (Match) Properties - with match property values using repo helper regexes for mandatory METADATA_SHARING profile
         *                                       - with arbitrary regexes (not produced by repo helper) for optional RELATIONSHIP_ADVANCED_SEARCH profile
         *   Find By Property Value              - with searchCriteria using repo helper regexes for mandatory METADATA_SHARING profile and
         *                                       - with arbitrary regexes (not produced by repo helper) for optional RELATIONSHIP_ADVANCED_SEARCH profile
         *
         *
         */


        this.metadataCollection = super.getMetadataCollection();

        switch (phase) {

            case SEED:
                this.seedInstances();
                break;
            case EXECUTE:
                this.performFinds();
                break;
            case CLEAN:
                this.cleanInstances();
                break;
        }
    }


    private void seedInstances() throws Exception
    {


        /*
         * Perform an initial discovery search against the repository....
         *
         * This initial search uses findRelationshipsByProperty() with an empty match properties object. The purpose of this
         * is to retrieve up to a page-worth of instances of the type being tested. These instances are then recorded and
         * analysed in order to predict the expected results from the actual test searches during the EXECUTE phase.
         */


        /*
         *  Use emptyMatchProperties and matchCriteria ALL   - this should return up to pageSize relationships of the current type
         */

        InstanceProperties emptyMatchProperties    = new InstanceProperties();



        try {
            knownInstances = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                            relationshipDef.getGUID(),
                                                                            emptyMatchProperties,
                                                                            MatchCriteria.ALL,
                                                                            0,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            pageSize);
        }
        catch (FunctionNotSupportedException exception) {

            /*
             * If the repository does not support search for relationships, it's OK because it's an optional profile
             * Report it as unsupported and back off.
             */

            super.addNotSupportedAssertion(ASSERTION_2_A,
                                           ASSERTION_MSG_2_A + relationshipDef.getName(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


            return;

        }
        catch(Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "findRelationshipsByProperty";
            String operationDescription = "find relationships of type " + relationshipDef.getName();
            Map<String,String> parameters = getParameters(relationshipDef.getGUID(), emptyMatchProperties, MatchCriteria.ALL);
            String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception( msg , exc );

        }




        if (knownInstances == null) {

            /*
             * There are no instances of this type reported by the repository.
             *
             * Attempt to create a generated set of test instances. If that succeeds record what is created (so that it can be
             * cleaned up again at the end of the test).
             * Also set result to the created instances, so this will be used in the tests. If it is not possible to create instances then
             * the test is abandoned as no assertions can be made (the result for this test will be UNKNOWN_STATUS).
             *
             * Want to come out of this with List<Relationship> result - or having quietly given up....
             */


            /*
             * We cannot be sure that the repository under test supports metadata maintenance, so need to try and back off.
             */


            knownInstances = new ArrayList<>();
            createdInstances = new ArrayList<>();
            createdEntityInstances = new ArrayList<>();

            /*
             * Create two pages worth of instances. Why two pages? Because it will allow us to test pagesize on searches.
             * The test verification will allow for the fact that a search may be pageLimited, in the same way that
             * the initial discovery search may have been page limited.
             */

            int numInstancesToCreate = 2 * pageSize;

            /*
             * For each instance, set all primitive properties.
             * Any unique properties must be set to distinct (unique) values. For this use the property name and instance count.
             * For other (non-unique) properties, set half of them to a common value, and the other half to unique values. This
             * can be achieved by testing if instance count is odd/even - odd => distinct; even => use common value.
             */

            for (int instanceCount = 0; instanceCount < numInstancesToCreate; instanceCount++) {


                Relationship newRelationship = createRelationship(relationshipDef,
                                                                  super.generatePropertiesForInstance(workPad.getLocalServerUserId(),
                                                                                                      attrList,
                                                                                                      instanceCount));

                if (newRelationship != null) {
                    /*
                     * If createRelationship came back non-null then any created instances (two
                     * entities and one relationship) will have been recorded for result prediction
                     * and cleanup).
                     *
                     */

                    /*
                     * We succeeded in creating instances - record the fact
                     */
                    assertCondition((true),
                                    ASSERTION_3,
                                    testTypeName + ASSERTION_MSG_3,
                                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());


                } else {
                    /*
                     * If relationship returned was null, test case must fail. Any exception processing
                     * has already been handled in createRelationship() - e.g FuncitonNotSupportedException.
                     * Worse (unexpected) exceptions will have already been wrapped and thrown.
                     * Test case can give up quietly.
                     */
                    return;
                }
            }
        }


        if (knownInstances == null || knownInstances.isEmpty()) {

            /*
             * Something bad has happened - if there were no instances and we failed to create any instances we should
             * have abandoned in the catch block above - belt and braces check but it shows there is no point continuing.
             */

            return;
        }


        /*
         * The data to test against is in 'knownInstances' - perform preliminary analysis.
         */


        /*
         * Record the total instance count and the overall set of discovered or created relationships. There may be more than
         * pageSize relationships (we know this to be true in the created case). The test assertions below allow for the fact
         * that hitherto unseen relationships may be returned.
         */

        if (knownInstances.size() >= pageSize)
            pageLimited = true;

        knownInstancesGUIDs = new ArrayList<>();
        for (Relationship relationship : knownInstances) {
            knownInstancesGUIDs.add(relationship.getGUID());
        }


        /*
         * Construct a reverse index of relationship GUIDs by property name and property value.
         * This is only performed for primitives.
         */

        for (TypeDefAttribute typeDefAttribute : attrList) {

            if (typeDefAttribute.getAttributeType().getCategory() == AttributeTypeDefCategory.PRIMITIVE) {

                String attrName = typeDefAttribute.getAttributeName();

                PrimitiveDef primDef = (PrimitiveDef) typeDefAttribute.getAttributeType();
                propertyCatMap.put(attrName,primDef.getPrimitiveDefCategory());

                Map<Object, List<String>> valueMap = new HashMap<>();
                propertyValueMap.put(attrName, valueMap);

                for (Relationship relationship : knownInstances) {
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


    }

    private Relationship createRelationship(RelationshipDef relationshipDef, InstanceProperties instanceProps) throws Exception {

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
                            ASSERTION_2,
                             ASSERTION_MSG_2 + testTypeName,
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

        EntityDetail end1;
        EntityDetail end2;

        InstanceProperties properties = null;
        EntityDef entityType = null;

        try {

            /*
             * Supply all properties for the instance, including those inherited from supertypes, since they may be mandatory.
             * An alternative here would be to use getMinPropertiesForInstance, but providing all properties creates a logically
             * complete entity
             */

            entityType = entityDefs.get(end1TypeName);
            properties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityType);
            end1 = metadataCollection.addEntity(workPad.getLocalServerUserId(), entityType.getGUID(), properties, null, null );

            // Record the created instance's GUID for later clean up.
            createdEntityInstances.add(end1);

            entityType = entityDefs.get(end2TypeName);
            properties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityType);
            end2 = metadataCollection.addEntity(workPad.getLocalServerUserId(), entityType.getGUID(), properties, null, null );

            // Record the created instance's GUID for later clean up.
            createdEntityInstances.add(end2);

        }
        catch (FunctionNotSupportedException exception) {

            /*
             * The repository does not support creation of entity instances; we need to report and fail the test
             *
             */

            super.addNotSupportedAssertion(ASSERTION_2,
                                           ASSERTION_MSG_2,
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            return null;
        }
        catch(Exception exc) {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "addEntity";
            String operationDescription = "add an entity of type " + (entityType!=null?entityType.getName():"null");
            Map<String,String> parameters = new HashMap<>();
            parameters.put("typeGUID"                , entityType!=null?entityType.getGUID():"null");
            parameters.put("initialProperties"       , properties!=null?properties.toString():"null");
            parameters.put("initialClassifications"  , "null");
            parameters.put("initialStatus"           , "null");
            String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception( msg , exc );

        }

        Relationship retRelationship = null;

        try {

            retRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(), relationshipDef.getGUID(), instanceProps, end1.getGUID(), end2.getGUID(), null);

            // Record the relationship instance for future result prediction and verification.
            knownInstances.add(retRelationship);
            // Record the created instance's GUID for later clean up.
            createdInstances.add(retRelationship);

        }
        catch (FunctionNotSupportedException exception) {

            /*
             * The repository does not support creation of entity instances; we need to report and fail the test
             *
             */

            super.addNotSupportedAssertion(ASSERTION_2,
                                           ASSERTION_MSG_2,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());


        }
        catch(Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "addRelationship";
            String operationDescription = "add a relationship of type " + relationshipDef.getName();
            Map<String,String> parameters = new HashMap<>();
            parameters.put("typeGUID"                , relationshipDef.getGUID());
            parameters.put("end1 entityGUID"         , end1.getGUID());
            parameters.put("end2 entityGUID"         , end2.getGUID());
            parameters.put("initialProperties"       , instanceProps!=null?instanceProps.toString():"null");
            parameters.put("initialStatus"           , "null");
            String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception( msg , exc );

        }


        return retRelationship;


    }

    /*
     * Clean up all relationships created by this testcase
     */
    private void cleanInstances() throws Exception
    {

        if (createdInstances != null && !createdInstances.isEmpty()) {

            /*
             * Instances were created - clean them up.
             */

            for (Relationship relationship : createdInstances) {
                try {

                    metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                    relationship.getType().getTypeDefGUID(),
                                                    relationship.getType().getTypeDefName(),
                                                    relationship.getGUID());

                } catch (FunctionNotSupportedException exception) {
                    // NO OP - can proceed to purge
                }

                metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                                               relationship.getType().getTypeDefGUID(),
                                               relationship.getType().getTypeDefName(),
                                               relationship.getGUID());
            }
        }
    }






    private void performFinds() throws Exception
    {
        /*
         * This test does not perform content validation of returned instances - these are tested in the lifecycle tests.
         */

        if (knownInstances != null && !knownInstances.isEmpty()) {

            /*
             * There are instances of the type being tested in the repository.
             * If this is not the case, the test will drop through and report an UNKNOWN_STATUS.
             */


            if (!definedAttributeNames.isEmpty()) {

                /*
                 * The type has at least one attribute that we can test against.
                 * If this is not the case, the test will drop through and report an UNKNOWN_STATUS.
                 *
                 * Perform single property findRelationshipsByProperty() tests - these take each attribute in turn and for each attribute,
                 * take one known value to search for instances using matchProperties containing the individual primitive property.
                 * The test is repeated for MatchCriteria ALL and NONE.
                 * This is only done for primitives.
                 */


                for (String attributeName : definedAttributeNames) {

                    performMatchPropertiesTestForAttribute(attributeName, MatchCriteria.ALL);

                    performMatchPropertiesTestForAttribute(attributeName, MatchCriteria.NONE);

                }

                /*
                 * Perform dual property findRelationshipsByProperty() tests -
                 *
                 * Dual property findRelationshipsByProperty() tests - these take a pair of attributes and using just one known value
                 * for each attribute, they exercise the different settings of MatchCriteria. They search for instances using a
                 * matchProperties object containing the pair of primitive properties. This tests matchCriteria ALL, ANY & NONE.
                 * Defined attributes only includes primitives.
                 */

                /*
                 * Pick one pair of properties for dual property tests - if there are less than two properties skip this test
                 */

                List<String> attributePair = pickAttributePair();
                if (attributePair != null && attributePair.size() == 2) {

                    String alphaAttributeName = attributePair.get(0);
                    String betaAttributeName = attributePair.get(1);

                    if (alphaAttributeName != null && betaAttributeName != null) {

                        performMatchPropertiesTestForAttributePair(alphaAttributeName, betaAttributeName, MatchCriteria.ANY);

                        performMatchPropertiesTestForAttributePair(alphaAttributeName, betaAttributeName, MatchCriteria.ALL);

                        performMatchPropertiesTestForAttributePair(alphaAttributeName, betaAttributeName, MatchCriteria.NONE);


                    }
                }



                /*
                 * Perform searchCriteria-based findByPropertyValue tests. One string attribute is selected and tested for each of its
                 * known values with all of EXACT, PREFIX, SUFFIX and CONTAINS matching. These are mandatory profile tests, so all string
                 * values are literalised using the repo helper methods.
                 */

                /*
                 * Look for a suitable (string) attribute...
                 */
                String stringAttributeName = null;

                for (String testAttributeName : definedAttributeNames) {

                    if (propertyCatMap.get(testAttributeName) == OM_PRIMITIVE_TYPE_STRING) {

                        /*
                         * This is a string attribute....
                         */
                        stringAttributeName = testAttributeName;
                        break;
                    }
                }

                if (stringAttributeName != null) {

                    performSearchCriteriaTests(stringAttributeName, RegexMatchType.Exact);

                    performSearchCriteriaTests(stringAttributeName, RegexMatchType.Prefix);

                    performSearchCriteriaTests(stringAttributeName, RegexMatchType.Suffix);

                    performSearchCriteriaTests(stringAttributeName, RegexMatchType.Contains);

                }


                /*
                 * Perform type filtering tests
                 */
                for (String attributeName : definedAttributeNames) {

                    performTypeFilteringTests(attributeName);

                }


                /*
                 * Perform generalised regex tests - these are part of the RELATIONSHIP_ADVANCED_SEARCH profile
                 */

                /*
                 * Look for a suitable (string) attribute...
                 */
                stringAttributeName = null;

                for (String testAttributeName : definedAttributeNames) {

                    if (propertyCatMap.get(testAttributeName) == OM_PRIMITIVE_TYPE_STRING) {

                        /*
                         * This is a string attribute....
                         */
                        stringAttributeName = testAttributeName;
                        break;
                    }
                }

                if (stringAttributeName != null) {

                    performAdvancedSearchTests(stringAttributeName, RegexMatchType.Exact);
                    performAdvancedSearchTests(stringAttributeName, RegexMatchType.Prefix);
                    performAdvancedSearchTests(stringAttributeName, RegexMatchType.Suffix);
                    performAdvancedSearchTests(stringAttributeName, RegexMatchType.Contains);
                }

                /*
                 * Completion of searches - indicate success of testcase.
                 */
                super.setSuccessMessage("Relationships can be searched by property and property value");

            }
        }
    }





    private List<String> pickAttributePair() {

        List<String> returnList = null;

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
                returnList = new ArrayList<>();
                returnList.add(alphaAttributeName);
                returnList.add(betaAttributeName);
            }
        }
        return returnList;

    }



    private void performMatchPropertiesTestForAttribute(String attributeName, MatchCriteria matchCriteria) throws Exception {

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


            matchProperties.setProperty(attributeName, ppv);


            /*
             * Formulate expected result
             */
            List<String> relationshipsWithValue = propertyValueMap.get(attributeName).get(value);
            List<String> expectedGUIDs = null;

            switch (matchCriteria) {
                case ALL:
                case ANY:
                    /* This is a single property test, so ANY and ALL are equivalent */
                    expectedGUIDs = relationshipsWithValue;
                    break;
                case NONE:
                    expectedGUIDs = diff(knownInstancesGUIDs, relationshipsWithValue);
                    break;
                default:
                    /* Invalid matchCriteria value passed */
                    return;
            }
            int expectedRelationshipCount = expectedGUIDs.size();
            // In the case where the instances were created, expected may exceed pageSize.

            /*
             * Search....
             */
            List<Relationship> result = null;

            Map<String,String> parameters = getParameters(relationshipDef.getGUID(), matchProperties, matchCriteria);

            try {

                result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                        relationshipDef.getGUID(),
                                                                        matchProperties,
                                                                        matchCriteria,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        pageSize);
            }
            catch (FunctionNotSupportedException exception) {

                /*
                 * The repository does not support relationship searches; we need to report and stop
                 *
                 */

                super.addNotSupportedAssertion(ASSERTION_101,
                                               ASSERTION_MSG_101,
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


            }
            catch(Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findRelationshipsByProperty";
                String operationDescription = "find relationships of type " + relationshipDef.getName();
                String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }

            /*
             * The approach to checking results match expectations is as follows:
             * The original disckvery request (top of this testcase) returned a set of instances that
             * are known to be in the repository. If that search hit the page limit then the
             * instances may be a partial result of what is actually in the repository. Although it
             * is possible to sort the results on a property, there is no guarantee that the values
             * associated with any particular property are distinct, so the resulting order is
             * not guaranteed. If this were an OMAS this would not be a problem because the OMAS/
             * user would mostly likely continue to search until they either find what they are
             * looking for or exhaust the contents of the repository. Since this is an automated
             * testcase for which we need a predictable, repeatable result, it needs to be more
             * robust. It is not appropriate to keep looping page by page because we do not know
             * how many matching instances the repository contains. It is preferable to perform
             * a limited search (in this case one page) rather than loop exhaustively.
             *
             * A constant page size is assumed throughout the following.
             * It is also assumed that instances are not being added or deleted during the course
             * of this testcase.
             *
             * The original result set is filtered to generate the result we expect to get from a narrower
             * search. If the original result set returned less than the page size then we know the full
             * set of instances in the repository and hence completely know each narrower expected set.
             * This case (of complete knowledge) can be summarised in pseudo code as:
             *   if original result size < page size then:
             *     result size < expected size => fail
             *     result size == expected size => if search contains all expected => pass
             *                                     else search !contains all expected => fail
             *      search size > expect size => fail (should not get more than the expectation)
             *
             *
             * In contrast, if the original result set returned a full page size then the testcase needs to
             * exercise a looser result matching policy. This case (of incomplete knowledge) can be summarised in
             * psudo code as:
             *
             *   if original result size == page size then:
             *     search size < expected size => fail
             *     search size == expected size => if search contains all expected => pass
             *                                     else search !contains all expected => check whether the unexpected instances are a valid match
             *                                       if true => pass
             *                                       else => fail
             *     search size > expect size =>    check whether the unexpected instances are a valid match
             *                                       if true => pass
             *                                       else => fail
             *
             * With the above in mind....
             *
             * Check that the expected number of relationships was returned. This has to consider the effect of the original
             * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
             * But if the limit was hit (on the original search) then there may be additional instances in the repository
             * that were not seen on the original search; the expected result was computed from only those instance that WERE seen,
             * so the expectation may be a subset of the actual. If we hit page size there may be additional instances that were
             * not included in the initial set, due to the initial set being limited by pageSize; the narrower search may
             * pull in additional relationships that were not discovered previously.
             *
             */


            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedRelationshipCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedRelationshipCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedRelationshipCount <  pageSize && resultCount >= expectedRelationshipCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(ASSERTION_MSG_4, resultCount, expectedRelationshipCount, parameters);
            assertCondition((acceptable_result_size),
                            ASSERTION_4,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


            /*
             * If there were any result, check that all expected relationships were returned and (in the pageLimited case) that any
             * additional relationships were valid results for the search.
             */
            if (resultCount > 0) {

                List<String> resultGUIDs = new ArrayList<>();
                for (Relationship relationship : result) {
                    resultGUIDs.add(relationship.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether there are (or may be) more relationships than the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited) {

                    if (!resultGUIDs.containsAll(expectedGUIDs))
                        unexpectedResult = MISSING_EXPECTED_GUIDS;

                } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                    for (Relationship relationship : result) {

                        if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                            /*
                             * This was an extra relationship that we either did not expect or that we have not seen previously.
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
                                         * and match criteria was ALL - so an relationship with an unequal property is not a valid result.
                                         */
                                        switch (matchCriteria) {
                                            case ALL:
                                            case ANY:
                                                /* This is a single property test, so ANY and ALL are equivalent */
                                                if (!primitiveValue.equals(value))
                                                    unexpectedResult = "('" + primitiveValue.toString() + "' for guid=" + relationship.getGUID() + ")";
                                                break;
                                            case NONE:
                                                if (primitiveValue.equals(value))
                                                    unexpectedResult = "('" + primitiveValue.toString() + "' for guid=" + relationship.getGUID() + ")";
                                                break;
                                            default:
                                                /* Invalid matchCriteria value passed */
                                                return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                assertionMessage = MessageFormat.format(ASSERTION_MSG_5, unexpectedResult, parameters);
                assertCondition(unexpectedResult.equals("0"),
                                ASSERTION_5,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());
            }

        }
    }


    private void performMatchPropertiesTestForAttributePair(String alphaAttributeName, String betaAttributeName, MatchCriteria matchCriteria) throws Exception  {


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
             * Formulate expected result
             */

            List<String> relationshipsWithAlphaValue = propertyValueMap.get(alphaAttributeName).get(alphaValue);
            List<String> relationshipsWithBetaValue  = propertyValueMap.get(betaAttributeName).get(betaValue);
            List<String> expectedGUIDs = null;

            switch (matchCriteria) {
                case ALL:
                    /* MatchCriteria.ALL ==> INTERSECTION */
                    expectedGUIDs = intersection(relationshipsWithAlphaValue, relationshipsWithBetaValue);
                    break;
                case ANY:
                    /* MatchCriteria.ANY ==> UNION */
                    expectedGUIDs = union(relationshipsWithAlphaValue, relationshipsWithBetaValue);
                    break;
                case NONE:
                    /* MatchCriteria.NONE ==> UNION COMPLEMENT */
                    expectedGUIDs = diff(knownInstancesGUIDs, relationshipsWithAlphaValue);
                    expectedGUIDs = diff(expectedGUIDs, relationshipsWithBetaValue);
                    break;
                default:
                    /* Invalid matchCriteria value passed */
                    return;
            }
            int expectedRelationshipCount = expectedGUIDs.size();



            /*
             * Search....
             */

            List<Relationship> result = null;

            Map<String,String> parameters = getParameters(relationshipDef.getGUID(), matchProperties, matchCriteria);

            try {
                result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                        relationshipDef.getGUID(),
                                                                        matchProperties,
                                                                        matchCriteria,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        pageSize);

            }
            catch (FunctionNotSupportedException exception) {

                /*
                 * The repository does not support relationship searches; we need to report and stop
                 *
                 */

                super.addNotSupportedAssertion(ASSERTION_101,
                                               ASSERTION_MSG_101,
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


            }
            catch(Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findRelationshipsByProperty";
                String operationDescription = "find relationships of type " + relationshipDef.getName();
                String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }

            /*
             * Check that the expected number of relationships was returned. This has to consider the effect of the original
             * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
             * But if the limit was hit (on the original search) then there may be additional instances in the repository
             * that were not seen on the original search; the expected result was computed from only thos instance that WERE seen,
             * so the expectation may be a subset of the actual.
             * The actual instances returned
             * may not match exactly if we hit page size because there may be additional instances that were not included in the
             * initial set, due to the initial set being limited by pageSize; the narrower search may pull in additional
             * relationships that were not discovered previously.
             * This next assertion is just about the size of the result set.
             */

            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();

            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedRelationshipCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedRelationshipCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedRelationshipCount <  pageSize && resultCount >= expectedRelationshipCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(ASSERTION_MSG_6, resultCount, expectedRelationshipCount, parameters);
            assertCondition((acceptable_result_size),
                            ASSERTION_6,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());



            /*
             * If there were any result, check that all expected relationships were returned and (in the pageLimited case) that any
             * additional relationships were valid results for the search.
             */
            if (resultCount > 0) {
                List<String> resultGUIDs = new ArrayList<>();
                for (Relationship relationship : result) {
                    resultGUIDs.add(relationship.getGUID());
                }

                /*
                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";
                String alpha = "";
                String beta = "";

                if (!pageLimited) {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                        unexpectedResult = MISSING_EXPECTED_GUIDS;
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
                                        alpha = primitiveValue.toString();
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
                                        beta = primitiveValue.toString();
                                    }
                                }
                            }


                            switch (matchCriteria) {
                                case ALL:
                                    if (!(alphaMatch && betaMatch))
                                        unexpectedResult = "(" + alphaAttributeName + "='" + alpha + "', " + betaAttributeName + "='" + beta + "' for guid=" + relationship.getGUID() + ")";
                                    break;
                                case ANY:
                                    if (!(alphaMatch || betaMatch))
                                        unexpectedResult = "(" + alphaAttributeName + "='" + alpha + "', " + betaAttributeName + "='" + beta + "' for guid=" + relationship.getGUID() + ")";
                                    break;
                                case NONE:
                                    if (!(!alphaMatch && !betaMatch))
                                        unexpectedResult = "(" + alphaAttributeName + "='" + alpha + "', " + betaAttributeName + "='" + beta + "' for guid=" + relationship.getGUID() + ")";
                                    break;
                                default:
                                    /* Invalid matchCriteria value passed */
                                    return;
                            }
                        }
                    }
                }

                assertionMessage = MessageFormat.format(ASSERTION_MSG_7, unexpectedResult, parameters);
                assertCondition(unexpectedResult.equals("0"),
                                ASSERTION_7,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());
            }

        }
    }


    private enum RegexMatchType {
        Exact,
        Prefix,
        Suffix,
        Contains
    }



    private void performSearchCriteriaTests(String attributeName, RegexMatchType matchType) throws Exception {

        /*
         * The given attribute is tested for exact, prefix, suffix and contains matches for each of the values already seen.
         * All these searches should return at least some instances in the result
         */

        Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
        Iterator<Object> possibleValueIterator = possibleValues.iterator();

        while (possibleValueIterator.hasNext()) {

            String stringValue = (String) (possibleValueIterator.next());
            String truncatedStringValue = null;
            String literalisedValue = null;
            int stringValueLength;
            int truncatedLength;

            switch (matchType) {

                case Exact:
                    /* EXACT MATCH */
                    literalisedValue = literaliseStringPropertyExact(stringValue);
                    break;
                case Prefix:
                    /* PREFIX MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 2) {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                    truncatedStringValue = stringValue.substring(0, truncatedLength);
                    literalisedValue = literaliseStringPropertyStartsWith(truncatedStringValue);
                    break;
                case Suffix:
                    /* SUFFIX MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 2) {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                    truncatedStringValue = stringValue.substring(stringValueLength-truncatedLength, stringValueLength);
                    literalisedValue = literaliseStringPropertyEndsWith(truncatedStringValue);
                    break;
                case Contains:
                    /* CONTAINS MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 3) {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.floor(stringValueLength / 2.0));
                    int diff = stringValueLength - truncatedLength;
                    int halfDiff = diff/2;
                    truncatedStringValue = stringValue.substring(halfDiff, stringValueLength-halfDiff);
                    literalisedValue = literaliseStringPropertyContains(truncatedStringValue);
                    break;
            }




            /*
             * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
             * Find all the values (regardless of attributeName) in the map that are an exact match to the search value
             * Care needed to detect relationships that are matched by more than one property - to avoid duplication it's
             * important to check that the relationship was not already included in the expected set.
             */
            int expectedRelationshipCount = 0;
            List<String> expectedGUIDs = new ArrayList<>();
            Set<String> propertyNamesSet = propertyValueMap.keySet();
            Iterator<String> propertyNamesSetIterator = propertyNamesSet.iterator();
            while (propertyNamesSetIterator.hasNext()) {
                String propName = propertyNamesSetIterator.next();
                if (propertyCatMap.get(propName) == OM_PRIMITIVE_TYPE_STRING) {
                    Map<Object, List<String>> propValues = propertyValueMap.get(propName);
                    Set<Object> propertyValuesSet = propValues.keySet();
                    Iterator<Object> propertyValuesSetIterator = propertyValuesSet.iterator();
                    while (propertyValuesSetIterator.hasNext()) {
                        String knownStringValue = (String) (propertyValuesSetIterator.next());

                        switch (matchType) {

                            case Exact:
                                /* EXACT MATCH */
                                if (stringValue.equals(knownStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Prefix:
                                /* PREFIX MATCH */
                                if (knownStringValue.startsWith(truncatedStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Suffix:
                                /* SUFFIX MATCH */
                                if (knownStringValue.endsWith(truncatedStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Contains:
                                /* CONTAINS MATCH */
                                if (knownStringValue.contains(truncatedStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }
            expectedRelationshipCount = expectedGUIDs.size();


            /*
             * Search....
             */

            List<Relationship> result = null;

            Map<String,String> parameters = getParameters(relationshipDef.getGUID(), literalisedValue);

            try {
                result = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                                                                    relationshipDef.getGUID(),
                                                                    literalisedValue,
                                                                    0,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    pageSize);
            }
            catch (FunctionNotSupportedException exception) {

                /*
                 * The repository does not support relationship searches; we need to report and stop
                 *
                 */

                super.addNotSupportedAssertion(ASSERTION_101,
                                               ASSERTION_MSG_101,
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());


            }
            catch(Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findRelationshipsByPropertyValue";
                String operationDescription = "find relationships of type " + relationshipDef.getName();
                String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }



            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedRelationshipCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedRelationshipCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedRelationshipCount <  pageSize && resultCount >= expectedRelationshipCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(ASSERTION_MSG_8, resultCount, expectedRelationshipCount, parameters);
            assertCondition((acceptable_result_size),
                            ASSERTION_8,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());


            /*
             * If there were any results, check that all expected relationships were returned and (in the pageLimited case) that any
             * additional relationships were valid results for the search.
             */
            if (resultCount > 0) {

                List<String> resultGUIDs = new ArrayList<>();
                for (Relationship relationship : result) {
                    resultGUIDs.add(relationship.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited) {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                        unexpectedResult = MISSING_EXPECTED_GUIDS;
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

                                                switch (matchType) {
                                                    case Exact:
                                                        /* EXACT MATCH */
                                                        if (propertyValueAsString.equals(stringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Prefix:
                                                        /* PREFIX MATCH */
                                                        if (propertyValueAsString.startsWith(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Suffix:
                                                        /* SUFFIX MATCH */
                                                        if (propertyValueAsString.endsWith(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Contains:
                                                        /* CONTAINS MATCH */
                                                        if (propertyValueAsString.contains(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            if (!validRelationship)
                                unexpectedResult = "(guid=" + relationship.getGUID() + ")";
                        }
                    }
                }

                assertionMessage = MessageFormat.format(ASSERTION_MSG_9, unexpectedResult, parameters);
                assertCondition(unexpectedResult.equals("0"),
                                ASSERTION_9,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());
            }

        }
    }


    /**
     * Return type def attributes for the properties defined in the TypeDef and all of its supertypes
     *
     * @param userId calling user
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     */
    protected List<TypeDefAttribute>  getAllPropertiesForTypedef(String userId, TypeDef typeDef)
    {


        // Recursively gather all the TypeDefAttributes for the supertype hierarchy...
        List<TypeDefAttribute> allTypeDefAttributes = getPropertiesForTypeDef(userId, typeDef);


        return allTypeDefAttributes;

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


    private void performTypeFilteringTests(String attributeName) throws Exception
    {
        /*
         *  This test does not verify that the content of the result matches what would be expected - that is tested in other tests above. This test is concerned with type filtering.
         */

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


            matchProperties.setProperty(attributeName, ppv);


            boolean wildSearchPageLimited = false;

            /*
             * Perform the search without type filtering
             */
            List<Relationship> result = null;

            try {
                result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                        null,
                                                                        matchProperties,
                                                                        MatchCriteria.ALL,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        pageSize);

            }
            catch (FunctionNotSupportedException exception) {

                /*
                 * The repository does not support relationship searches; we need to report and stop
                 *
                 */

                super.addNotSupportedAssertion(ASSERTION_101,
                                               ASSERTION_MSG_101,
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


            }
            catch(Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findRelationshipsByProperty";
                String operationDescription = "find relationships with no type filter";
                Map<String,String> parameters = getParameters(null, matchProperties, MatchCriteria.ALL);
                String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }


            List<String> expectedGUIDs;

            if (result != null) {

                if (result.size() == pageSize) {
                    /*
                     * Need to note whether this wild search hit the page limit - if so then there may be more
                     * relationships that we have not seen; pull the expected list together (below) and the expected
                     * count, and tolerate and verify any additional relationships that are returned in the more
                     * type-specific search below.
                     */
                    wildSearchPageLimited = true;
                }
                /*
                 * Count the relationships that are of current type
                 */
                String relationshipTypeName = relationshipDef.getName();
                List<String> countableTypeNames = new ArrayList<>();
                countableTypeNames.add(relationshipTypeName);

                expectedGUIDs = new ArrayList<>();

                for (Relationship relationship : result) {
                    String typeName = relationship.getType().getTypeDefName();
                    if (countableTypeNames.contains(typeName)) {
                        expectedGUIDs.add(relationship.getGUID());
                    }
                }

                int expectedRelationshipCount = expectedGUIDs.size();


                /*
                 * Repeat the search being specific about type
                 */

                result = null;

                Map<String,String> parameters = getParameters(relationshipDef.getGUID(), matchProperties, MatchCriteria.ALL);

                try {
                    result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                            relationshipDef.getGUID(),
                                                                            matchProperties,
                                                                            MatchCriteria.ALL,
                                                                            0,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            pageSize);
                }
                catch (FunctionNotSupportedException exception) {

                    /*
                     * The repository does not support relationship searches; we need to report and stop
                     *
                     */

                    super.addNotSupportedAssertion(ASSERTION_101,
                                                   ASSERTION_MSG_101,
                                                   RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                                   RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


                }
                catch(Exception exc) {
                    /*
                     * We are not expecting any other exceptions from this method call. Log and fail the test.
                     */

                    String methodName = "findRelationshipsByProperty";
                    String operationDescription = "find relationships of type " + relationshipDef.getName();
                    String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                    throw new Exception( msg , exc );

                }


                /*
                 * We need to check that we got (at least) the expected number of results - which could include zero.
                 */
                int resultCount = result == null ? 0 : result.size();
                /*
                 * If the broader wild query hit the page limit then we should have been able to exactly predict the expected result.
                 * In addition the result size should be no more than a page.
                 */
                boolean unlimited_case = !wildSearchPageLimited && resultCount == expectedRelationshipCount;
                /*
                 * If the broader wild query hit the page limit then we have to tolerate hitherto unseen instances in the results.
                 * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
                 * So in that latter case we need to accept Min().
                 */
                boolean limited_large_case = wildSearchPageLimited && expectedRelationshipCount >= pageSize && resultCount == pageSize;
                boolean limited_small_case = wildSearchPageLimited && expectedRelationshipCount < pageSize && resultCount >= expectedRelationshipCount;
                boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

                String assertionMessage = MessageFormat.format(ASSERTION_MSG_10, resultCount, expectedRelationshipCount, parameters);
                assertCondition((acceptable_result_size),
                                ASSERTION_10,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());


                /*
                 * If there were any result, check that all expected relationships were returned and (in the pageLimited case) that any
                 * additional relationships were valid results for the search.
                 */
                if (resultCount > 0) {

                    List<String> resultGUIDs = new ArrayList<>();
                    for (Relationship relationship : result) {
                        resultGUIDs.add(relationship.getGUID());
                    }


                    /*
                     * Here again, we need to be sensitive to whether there are (or may be) more relationships than the page limit.
                     * If the original search hit the limit then we may legitimately receive additional instances in the results
                     * of a narrower search. But not if the original result set was under the page limit.
                     */

                    String unexpectedResult = "0";

                    if (!pageLimited) {

                        if (!resultGUIDs.containsAll(expectedGUIDs))
                            unexpectedResult = MISSING_EXPECTED_GUIDS;

                    } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                        for (Relationship relationship : result) {

                            if (!(expectedGUIDs.contains(relationship.getGUID()))) {
                                /*
                                 * This was an extra relationship that we either did not expect or that we have not seen previously.
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
                                             * This is because we used an exact match literalised property value
                                             * and match criteria was ALL - so an relationship with an unequal property
                                             * is not a valid result.
                                             */

                                            if (!primitiveValue.equals(value))
                                                unexpectedResult = "('" + primitiveValue.toString() + "' for guid=" + relationship.getGUID() + ")";


                                        }
                                    }
                                }
                            }
                        }
                    }

                    assertionMessage = MessageFormat.format(ASSERTION_MSG_11, unexpectedResult, parameters);
                    assertCondition(unexpectedResult.equals("0"),
                                    ASSERTION_11,
                                    assertionMessage,
                                    RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                    RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());
                }
            }
        }
    }




    /*
     * This method tests ability to handle arbitrary regular epresseions.
     * This method includes both searchCriteria based findRelationshipByPropertyValue tests and matchProperty based findRelationshipByProperty tests
     */
    private void performAdvancedSearchTests(String attributeName, RegexMatchType matchType) throws Exception {

        /*
         * The given attribute is tested for exact, prefix, suffix and contains matches for each of the values already seen.
         * All these searches should return at least some instances in the result; some may match more than a page full.
         */

        Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
        Iterator<Object> possibleValueIterator = possibleValues.iterator();

        while (possibleValueIterator.hasNext()) {

            String stringValue = (String) (possibleValueIterator.next());
            String truncatedStringValue = null;
            String regexValue = null;
            int stringValueLength;
            int truncatedLength;

            switch (matchType) {

                case Exact:
                    /* EXACT MATCH */
                    regexValue = stringValue;
                    break;
                case Prefix:
                    /* PREFIX MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 2) {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                    truncatedStringValue = stringValue.substring(0, truncatedLength);
                    regexValue = truncatedStringValue + ".*";
                    break;
                case Suffix:
                    /* SUFFIX MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 2) {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                    truncatedStringValue = stringValue.substring(stringValueLength - truncatedLength, stringValueLength);
                    regexValue = ".*" + truncatedStringValue;
                    break;
                case Contains:
                    /* CONTAINS MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 3) {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.floor(stringValueLength / 2.0));
                    int diff = stringValueLength - truncatedLength;
                    int halfDiff = diff / 2;
                    truncatedStringValue = stringValue.substring(halfDiff, stringValueLength - halfDiff);
                    regexValue = ".*" + truncatedStringValue + ".*";
                    break;
            }




            /*
             * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
             * Find all the values (regardless of attributeName) in the map that are an exact match to the search value
             * Care needed to detect relationships that are matched by more than one property - to avoid duplication it's
             * important to check that the relationship was not already included in the expected set.
             */
            int expectedRelationshipCount = 0;
            List<String> expectedGUIDs = new ArrayList<>();
            Set<String> propertyNamesSet = propertyValueMap.keySet();
            Iterator<String> propertyNamesSetIterator = propertyNamesSet.iterator();
            while (propertyNamesSetIterator.hasNext()) {
                String propName = propertyNamesSetIterator.next();
                if (propertyCatMap.get(propName) == OM_PRIMITIVE_TYPE_STRING) {
                    Map<Object, List<String>> propValues = propertyValueMap.get(propName);
                    Set<Object> propertyValuesSet = propValues.keySet();
                    Iterator<Object> propertyValuesSetIterator = propertyValuesSet.iterator();
                    while (propertyValuesSetIterator.hasNext()) {
                        String knownStringValue = (String) (propertyValuesSetIterator.next());

                        switch (matchType) {

                            case Exact:
                                /* EXACT MATCH */
                                if (stringValue.equals(knownStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Prefix:
                                /* PREFIX MATCH */
                                if (knownStringValue.startsWith(truncatedStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Suffix:
                                /* SUFFIX MATCH */
                                if (knownStringValue.endsWith(truncatedStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Contains:
                                /* CONTAINS MATCH */
                                if (knownStringValue.contains(truncatedStringValue)) {
                                    for (String matchGUID : propValues.get(knownStringValue)) {
                                        if (!expectedGUIDs.contains(matchGUID)) {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }
            expectedRelationshipCount = expectedGUIDs.size();


            /*
             * Test search using findRelationshipsByPropertyValue
             */

            List<Relationship> result = null;

            Map<String,String> parameters = getParameters(relationshipDef.getGUID(), regexValue);

            try {
                result = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                                                                    relationshipDef.getGUID(),
                                                                    regexValue,
                                                                    0,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    pageSize);
            }
            catch (FunctionNotSupportedException exc) {

                /*
                 * This may be because the repository either :
                 *  - does not support search for relationships (io is optional), or
                 *  - des not support general regular expressions.
                 * Either way we are going to have to fail this test - but we want to provide human-interpretable output n the test results.
                 * To do this, we record the exception message in the assertion message.
                 * The requirement/profile used here is the more specific  - i.e. not supporting general regexes. This is because the ability to
                 * search relationships will have been tested and validated by an earlier test, so it will be found anyway on inspection of the
                 * test results. The actual nature of the failure should be clear from the exception message.
                 */

                super.addNotSupportedAssertion(ASSERTION_101,
                                               ASSERTION_MSG_101 + relationshipDef.getName() + ": " + exc.getMessage(),
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_VALUE_SEARCH.getRequirementId());

                return;

            }
            catch(Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findRelationshipsByPropertyValue";
                String operationDescription = "find relationships with general regex for type " + relationshipDef.getName();
                String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }


            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedRelationshipCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedRelationshipCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedRelationshipCount < pageSize && resultCount >= expectedRelationshipCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(ASSERTION_MSG_12, resultCount, expectedRelationshipCount, parameters);
            assertCondition((acceptable_result_size),
                            ASSERTION_12,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getRequirementId());


            /*
             * If there were any results, check that all expected relationships were returned and (in the pageLimited case) that any
             * additional relationships were valid results for the search.
             */
            if (resultCount > 0) {

                List<String> resultGUIDs = new ArrayList<>();
                for (Relationship relationship : result) {
                    resultGUIDs.add(relationship.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited) {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                        unexpectedResult = MISSING_EXPECTED_GUIDS;
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

                                                switch (matchType) {
                                                    case Exact:
                                                        /* EXACT MATCH */
                                                        if (propertyValueAsString.equals(stringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Prefix:
                                                        /* PREFIX MATCH */
                                                        if (propertyValueAsString.startsWith(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Suffix:
                                                        /* SUFFIX MATCH */
                                                        if (propertyValueAsString.endsWith(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Contains:
                                                        /* CONTAINS MATCH */
                                                        if (propertyValueAsString.contains(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            if (!validRelationship)
                                unexpectedResult = "(guid=" + relationship.getGUID() + ")";
                        }
                    }
                }

                assertionMessage = MessageFormat.format(ASSERTION_MSG_13, unexpectedResult, parameters);
                assertCondition(unexpectedResult.equals("0"),
                                ASSERTION_13,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_VALUE_SEARCH.getRequirementId());
            }




            /*
             * Repeat the same search using findRelationshipsByProperty and a MatchProperties object
             */


            InstanceProperties matchProperties = new InstanceProperties();

            PrimitivePropertyValue mppv = new PrimitivePropertyValue();
            mppv.setPrimitiveDefCategory(propertyCatMap.get(attributeName));
            mppv.setPrimitiveValue(regexValue);
            matchProperties.setProperty(attributeName, mppv);


            result = null;

            parameters = getParameters(relationshipDef.getGUID(), matchProperties, MatchCriteria.ALL);

            try {

                result = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                                                                        relationshipDef.getGUID(),
                                                                        matchProperties,
                                                                        MatchCriteria.ALL,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        pageSize);

            }
            catch (FunctionNotSupportedException exc) {

                /*
                 * This may be because the repository either :
                 *  - does not support search for relationships (io is optional), or
                 *  - des not support general regular expressions.
                 * Either way we are going to have to fail this test - but we want to provide human-interpretable output n the test results.
                 * To do this, we record the exception message in the assertion message.
                 * The requirement/profile used here is the more specific  - i.e. not supporting general regexes. This is because the ability to
                 * search relationships will have been tested and validated by an earlier test, so it will be found anyway on inspection of the
                 * test results. The actual nature of the failure should be clear from the exception message.
                 */

                super.addNotSupportedAssertion(ASSERTION_102,
                                               ASSERTION_MSG_102 + relationshipDef.getName() + ": " + exc.getMessage(),
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.RELATIONSHIP_PROPERTY_SEARCH.getRequirementId());

                return;

            }
            catch(Exception exc) {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findRelationshipsByProperty";
                String operationDescription = "find relationships of type " + relationshipDef.getName();
                String msg = this.buildExceptionMessage(TEST_CASE_ID, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }

            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition the result size should be no more than a page.
             */
            unlimited_case = !pageLimited && resultCount == expectedRelationshipCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            limited_large_case = pageLimited && expectedRelationshipCount >= pageSize && resultCount == pageSize;
            limited_small_case = pageLimited && expectedRelationshipCount < pageSize && resultCount >= expectedRelationshipCount;
            acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            assertionMessage = MessageFormat.format(ASSERTION_MSG_14, resultCount, expectedRelationshipCount, parameters);
            assertCondition((acceptable_result_size),
                            ASSERTION_14,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());


            /*
             * If there were any results, check that all expected relationships were returned and (in the pageLimited case) that any
             * additional relationships were valid results for the search.
             */
            if (resultCount > 0) {

                List<String> resultGUIDs = new ArrayList<>();
                for (Relationship relationship : result) {
                    resultGUIDs.add(relationship.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited) {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                        unexpectedResult = MISSING_EXPECTED_GUIDS;
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

                                                switch (matchType) {
                                                    case Exact:
                                                        /* EXACT MATCH */
                                                        if (propertyValueAsString.equals(stringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Prefix:
                                                        /* PREFIX MATCH */
                                                        if (propertyValueAsString.startsWith(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Suffix:
                                                        /* SUFFIX MATCH */
                                                        if (propertyValueAsString.endsWith(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                    case Contains:
                                                        /* CONTAINS MATCH */
                                                        if (propertyValueAsString.contains(truncatedStringValue)) {
                                                            validRelationship = true;
                                                        }
                                                        break;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            if (!validRelationship)
                                unexpectedResult = "(guid=" + relationship.getGUID() + ")";
                        }
                    }
                }

                assertionMessage = MessageFormat.format(ASSERTION_MSG_15, unexpectedResult, parameters);
                assertCondition(unexpectedResult.equals("0"),
                                ASSERTION_15,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_ADVANCED_PROPERTY_SEARCH.getRequirementId());
            }
        }

    }

    private Map<String,String> getParameters(String relationshipTypeGuid,
                                             String searchCriteria) {
        Map<String,String> parameters = new TreeMap<>();
        parameters.put("relationshipTypeGUID", relationshipTypeGuid == null ? V_NULL : relationshipTypeGuid);
        parameters.put("searchCriteria", searchCriteria == null ? V_NULL : searchCriteria);
        parameters.put("fromRelationshipElement", Integer.toString(0));
        parameters.put("limitResultsByStatus", V_NULL);
        parameters.put("asOfTime", V_NULL);
        parameters.put("sequencingProperty", V_NULL);
        parameters.put("sequencingOrder", V_NULL);
        parameters.put("pageSize", Integer.toString(pageSize));
        return parameters;
    }

    private Map<String,String> getParameters(String relationshipTypeGuid,
                                             InstanceProperties matchProperties,
                                             MatchCriteria matchCriteria) {
        Map<String,String> parameters = new TreeMap<>();
        parameters.put("relationshipTypeGUID", relationshipTypeGuid == null ? V_NULL : relationshipTypeGuid);
        parameters.put("matchProperties", matchProperties == null ? V_NULL : matchProperties.toString());
        parameters.put("matchCriteria", matchCriteria == null ? V_NULL : matchCriteria.getName());
        parameters.put("fromRelationshipElement", Integer.toString(0));
        parameters.put("limitResultsByStatus", V_NULL);
        parameters.put("asOfTime", V_NULL);
        parameters.put("sequencingProperty", V_NULL);
        parameters.put("sequencingOrder", V_NULL);
        parameters.put("pageSize", Integer.toString(pageSize));
        return parameters;
    }

}
