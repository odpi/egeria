/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.ffdc.exception.AssertionFailureException;
import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryTimeoutException;

import java.text.MessageFormat;
import java.util.*;

import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria.ALL;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria.ANY;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;


/**
 * Test that all defined entities can be retrieved by property searches.
 *
 * This testcase covers Entity searches using basic and advanced search criteria/values.
 * The difference is:
 *    basic     = only literal values or repo helper regexes can be used for values of string match properties or as searchCriteria
 *    advanced  = arbitrary regexes can be used for values of string match properties or as searchCriteria
 *
 */
public class TestSupportedEntitySearch extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-entity-property-search";
    private static final String testCaseName = "Repository entity property search test case";

    private static final String assertion101FEBP        = testCaseId + "-101FEBP";
    private static final String assertionMsg_101FEBP    = "findEntitiesByProperty is supported: ";

    private static final String assertion101FEBPV       = testCaseId + "-101FEBPV";
    private static final String assertionMsg_101FEBPV   = "findEntitiesByPropertyValue is supported: ";

    private static final String assertion101FE          = testCaseId + "-101FE";
    private static final String assertionMsg_101FE      = "findEntities using SearchProperties is supported: ";

    private static final String assertion101FEBPGEN     = testCaseId + "-101FEBPGEN";
    private static final String assertionMsg_101FEBPGEN = "findEntitiesByProperty supports general regular expressions: ";

    private static final String assertion101FEBPVGEN     = testCaseId + "-101FEBPVGEN";
    private static final String assertionMsg_101FEBPVGEN = "findEntitiesByPropertyValue supports general regular expressions: ";


    private static final String assertion1       = testCaseId + "-01";
    private static final String assertionMsg_1   = " entity type matches the known type from the repository helper.";

    private static final String assertion2       = testCaseId + "-2";
    private static final String assertionMsg_2   = "repository supports creation of instances of type ";

    private static final String assertion3       = testCaseId + "-03";
    private static final String assertionMsg_3   = "findEntitiesByProperty found {0}/{1} expected results using parameters: {2}";

    private static final String assertion4       = testCaseId + "-04";
    private static final String assertionMsg_4   = "findEntitiesByProperty returned {0} unexpected results using parameters: {1}";

    private static final String assertion5       = testCaseId + "-05";
    private static final String assertionMsg_5   = assertionMsg_3;

    private static final String assertion6       = testCaseId + "-06";
    private static final String assertionMsg_6   = assertionMsg_4;

    private static final String assertion7       = testCaseId + "-07";
    private static final String assertionMsg_7   = "findEntitiesByPropertyValue found {0}/{1} expected results using parameters: {2}";

    private static final String assertion8       = testCaseId + "-08";
    private static final String assertionMsg_8   = "findEntitiesByPropertyValue returned {0} unexpected results using parameters: {1}";

    private static final String assertion9       = testCaseId + "-09";
    private static final String assertionMsg_9   = assertionMsg_3;

    private static final String assertion10      = testCaseId + "-10";
    private static final String assertionMsg_10  = assertionMsg_4;

    private static final String assertion11      = testCaseId + "-11";
    private static final String assertionMsg_11  = "findEntitiesByPropertyValue with general regex found {0}/{1} expected results using parameters: {2}";

    private static final String assertion12      = testCaseId + "-12";
    private static final String assertionMsg_12  = "findEntitiesByPropertyValue with general regex returned {0} unexpected results using parameters: {1}";

    private static final String assertion13      = testCaseId + "-13";
    private static final String assertionMsg_13  = "findEntitiesByProperty with general regex found {0}/{1} expected results using parameters: {2}";

    private static final String assertion14      = testCaseId + "-14";
    private static final String assertionMsg_14  = "findEntitiesByProperty with general regex returned {0} unexpected results using parameters: {1}";

    private static final String assertion103     = testCaseId + "-103";
    private static final String assertionMsg_103 = "findEntities using SearchProperties with general regex found {0}/{1} expected results using parameters: {2}";

    private static final String assertion104     = testCaseId + "-104";
    private static final String assertionMsg_104 = "findEntities using SearchProperties with general regex returned {0} unexpected results using parameters: {1}";

    private static final String assertion105       = testCaseId + "-105";
    private static final String assertionMsg_105   = "findEntitiesByProperty with null match parameters found {0}/{1} expected results using parameters: {2}";

    private static final String assertion106       = testCaseId + "-106";
    private static final String assertionMsg_106   = "findEntitiesByProperty with null match parameters returned {0} unexpected results using parameters: {1}";

    private static final String assertion107       = testCaseId + "-107";
    private static final String assertionMsg_107   = "findEntities with null match parameters found {0}/{1} expected results using parameters: {2}";

    private static final String assertion108       = testCaseId + "-108";
    private static final String assertionMsg_108   = "findEntities with null match parameters returned {0} unexpected results using parameters: {1}";

    private static final String RESULTS_MISSING_EXPECTED_GUIDS = "(results missing expected GUIDs)";

    private static final String V_NULL = "null";

    private RepositoryConformanceWorkPad              workPad;
    private OMRSMetadataCollection                    metadataCollection;
    private EntityDef                                 entityDef;
    private List<TypeDefAttribute>                    attrList;
    private String                                    testTypeName;

    private List<EntityDetail>                        knownInstances;
    private List<String>                              knownInstancesGUIDs;
    private List<EntityDetail>                        createdInstances;
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
     * @param entityDef type of valid entities
     */
    public TestSupportedEntitySearch(RepositoryConformanceWorkPad workPad,
                                     EntityDef                    entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());

        this.workPad = workPad;
        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(), testCaseId, testCaseName);

        this.knownInstances = null;
        this.knownInstancesGUIDs = null;
        this.createdInstances = null;

        this.pageLimited = false;

        this.uniqueAttributeNames = new ArrayList<>();
        this.definedAttributeNames = new ArrayList<>();

        this.propertyValueMap = new HashMap<>();
        this.propertyCatMap = new HashMap<>();

        this.pageSize = getMaxSearchResults();


        /*
         * Check that the entity type matches the known type from the repository helper
         */
        OMRSRepositoryConnector cohortRepositoryConnector = null;
        OMRSRepositoryHelper repositoryHelper = null;
        cohortRepositoryConnector = workPad.getTutRepositoryConnector();
        repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();

        EntityDef knownEntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), entityDef.getName());
        verifyCondition((entityDef.equals(knownEntityDef)),
                        assertion1,
                        testTypeName + assertionMsg_1,
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());


        /*
         * Take a look at the attributes for the type being tested
         *
         * This should only return current type attributes, excluding any that are deprecated.
         */

        this.attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(), entityDef);

        if ( (this.attrList != null) && !(this.attrList.isEmpty()) )
        {

            /*
             * If the TypeDef has NO attributes then it is not possible to perform a matchProperties or searchCriteria find on instances of that type.
             * The MetadataCollection API does not accept null match properties and a searchCriteria search on something with no values would be pointless.
             */

            /*
             * The TypeDef has defined attributes. Create a List<String> of just the attribute names.
             * Also identify any attributes that are defined as unique as they need to be created distinct, and
             * may be used for search narrowing.
             * This is only collated for primitives.
             */
            for (TypeDefAttribute typeDefAttribute : attrList)
            {
                if (typeDefAttribute.getAttributeType().getCategory() == AttributeTypeDefCategory.PRIMITIVE)
                {
                    definedAttributeNames.add(typeDefAttribute.getAttributeName());
                    if (typeDefAttribute.isUnique())
                    {
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
         * This test case does not classify entities or search by classification - they are tested in the accompanying TestSupportedClassificationLifecycle
         * and TestSupportedClassificationSearch testcases.
         *
         *
         * The following searches are performed:
         *   Find By Instance (Match) Properties - with match property values using repo helper regexes for mandatory METADATA_SHARING profile
         *                                       - with arbitrary regexes (not produced by the repo helper) for optional ENTITY_ADVANCED_SEARCH profile
         *   Find By Property Value              - with searchCriteria using repo helper regexes for mandatory METADATA_SHARING profile
         *                                       - with arbitrary regexes (not produced by the repo helper) for optional ENTITY_ADVANCED_SEARCH profile
         *
         *
         */

        this.metadataCollection = super.getMetadataCollection();

        switch (phase)
        {
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
         * This initial search uses findEntitiesByProperty() with an empty match properties object. The purpose of this
         * is to retrieve up to a page-worth of instances of the type being tested. These instances are then recorded and
         * analysed in order to predict the expected results from the actual test searches during the EXECUTE phase.
         */




        /*
         *  Use emptyMatchProperties and matchCriteria ALL   - this should return up to pageSize entities of the current type
         */

        InstanceProperties emptyMatchProperties = new InstanceProperties();

        try
        {

            knownInstances = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                                       entityDef.getGUID(),
                                                                       emptyMatchProperties,
                                                                       MatchCriteria.ALL,
                                                                       0,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       pageSize);
        }
        catch(Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "findEntitiesByProperty";
            String operationDescription = "find entities of type " + entityDef.getName();
            Map<String,String> parameters = getParameters(entityDef.getGUID(), emptyMatchProperties, MatchCriteria.ALL);
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception( msg , exc );

        }


        if (knownInstances == null)
        {

            /*
             * There are no instances of this type reported by the repository.
             *
             * Attempt to create a generated set of test instances. If that succeeds record what is created (so that it can be
             * cleaned up again at the end of the test).
             * Also set result to the created instances, so this will be used in the tests. If it is not possible to create instances then
             * the test is abandoned as no assertions can be made (the result for this test will be UNKNOWN_STATUS).
             *
             * Want to come out of this with List<EntityDetail> result - or having quietly given up....
             */


            /*
             * We cannot be sure that the repository under test supports metadata maintenance, so need to try and back off.
             */

            InstanceProperties instProps = null;

            try
            {
                knownInstances = new ArrayList<>();
                createdInstances = new ArrayList<>();
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

                // We will simply average the results given there is only a single assertion (not instance-by-instance)
                long totalElapsedTime = 0;
                for (int instanceCount = 0 ; instanceCount < numInstancesToCreate ; instanceCount++ )
                {
                    instProps = super.generatePropertiesForInstance(workPad.getLocalServerUserId(), attrList, instanceCount);

                    long start = System.currentTimeMillis();
                    EntityDetail newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                                          entityDef.getGUID(),
                                                                          instProps,
                                                                          null,
                                                                          null);
                    long finish = System.currentTimeMillis();
                    totalElapsedTime += (finish - start);

                    // Record the created instance for result prediction and verification
                    knownInstances.add(newEntity);

                    // Record the created instance's GUID for later clean up.
                    createdInstances.add(newEntity);

                }

                /*
                 * We succeeded in creating instances - record the fact
                 */
                assertCondition((true),
                                assertion2,
                                 assertionMsg_2 + testTypeName,
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                                "addEntity",
                                totalElapsedTime / numInstancesToCreate);

            }
            catch (AssertionFailureException exception)
            {
                throw exception;
            }
            catch (FunctionNotSupportedException exception)
            {


                /*
                 * If the repository does not support metadata maintenance, then the test is not going to be interesting.
                 * The workpad will not have recorded any instances and this absence of instance is checked in the remaining phases.
                 * The EXECUTE phase will quietly give up if there are no recorded instances
                 * The CLEAN phase will clean up any recorded instances (and do nothing if there are no instances).
                 */

                /*
                 * If running against a read-only repository/connector that cannot add instances of the necessary type(s)
                 * catch FunctionNotSupportedException and give up the test.
                 *
                 * Report the inability to create instances and give up on the testcase....
                 */

                super.addNotSupportedAssertion(assertion2,
                                               assertionMsg_2 + testTypeName,
                                               RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                               RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());


                return;
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "addEntity";
                String operationDescription = "add an entity of type " + entityDef.getName();
                Map<String,String> parameters = new HashMap<>();
                parameters.put("typeGUID"                , entityDef.getGUID());
                parameters.put("initialProperties"       , instProps!=null?instProps.toString():"null");
                parameters.put("initialClassifications"  , "null");
                parameters.put("initialStatus"           , "null");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }

        }


        if (knownInstances == null || knownInstances.isEmpty())
        {

            /*
             * Something bad has happened - if there were no instances, and we failed to create any instances we should
             * have abandoned in the catch block above - belt and braces check, but it shows there is no point continuing.
             */

            return;
        }


        /*
         * The data to test against is in 'knownInstances' - perform preliminary analysis.
         */


        /*
         * Record the total instance count and the overall set of discovered or created entities. There may be more than
         * pageSize entities (we know this to be true in the created case). The test assertions below allow for the fact
         * that hitherto unseen entities may be returned.
         */

        if (knownInstances.size() >= pageSize)
        {
            pageLimited = true;
        }

        knownInstancesGUIDs = new ArrayList<>();
        for (EntityDetail entity : knownInstances)
        {
            knownInstancesGUIDs.add(entity.getGUID());
        }


        /*
         * Construct a reverse index of entity GUIDs by property name and property value.
         * This is only performed for primitives.
         */

        for (TypeDefAttribute typeDefAttribute : attrList)
        {
            if (typeDefAttribute.getAttributeType().getCategory() == AttributeTypeDefCategory.PRIMITIVE)
            {
                String attrName = typeDefAttribute.getAttributeName();

                PrimitiveDef primDef = (PrimitiveDef) typeDefAttribute.getAttributeType();
                propertyCatMap.put(attrName,primDef.getPrimitiveDefCategory());

                Map<Object, List<String>> valueMap = new HashMap<>();
                propertyValueMap.put(attrName, valueMap);

                for (EntityDetail entity : knownInstances)
                {
                    InstanceProperties entityProperties = entity.getProperties();
                    if (entityProperties != null)
                    {
                        InstancePropertyValue ipValue = entityProperties.getPropertyValue(attrName);
                        if (ipValue != null) {
                            InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                            if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                            {
                                Object primitiveValue = ipValue.valueAsObject();
                                if (valueMap.get(primitiveValue) == null)
                                {
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
        }
    }


    /*
     * Clean up all entities created by this testcase
     */
    private void cleanInstances() throws Exception
    {

        if (createdInstances != null && !createdInstances.isEmpty())
        {
            /*
             * Instances were created - clean them up.
             */

            for (EntityDetail entity : createdInstances)
            {
                try
                {

                    metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                    entity.getType().getTypeDefGUID(),
                                                    entity.getType().getTypeDefName(),
                                                    entity.getGUID());

                }
                catch (FunctionNotSupportedException exception)
                {
                    // NO OP - can proceed to purge
                }

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                               entity.getType().getTypeDefGUID(),
                                               entity.getType().getTypeDefName(),
                                               entity.getGUID());
            }
        }
    }






    private void performFinds() throws Exception
    {
        /*
         * This test does not perform content validation of returned instances - these are tested in the lifecycle tests.
         */

        if (knownInstances != null && !knownInstances.isEmpty())
        {

            /*
             * There are instances of the type being tested in the repository.
             * If this is not the case, the test will drop through and report an UNKNOWN_STATUS.
             */


            if (!definedAttributeNames.isEmpty())
            {

                /*
                 * The type has at least one attribute that we can test against.
                 * If this is not the case, the test will drop through and report an UNKNOWN_STATUS.
                 *
                 * Perform single property findEntitiesByProperty() tests - these take each attribute in turn and for each attribute,
                 * take one known value to search for instances using matchProperties containing the individual primitive property.
                 * The test is repeated for MatchCriteria ALL and NONE.
                 * This is only done for primitives.
                 */


                for (String attributeName : definedAttributeNames)
                {

                    performMatchPropertiesTestForAttribute(attributeName, MatchCriteria.ALL);

                    performMatchPropertiesTestForAttribute(attributeName, MatchCriteria.NONE);

                    performSearchPropertiesTestForAttribute(attributeName);

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
                if (attributePair != null && attributePair.size() == 2)
                {

                    String alphaAttributeName = attributePair.get(0);
                    String betaAttributeName = attributePair.get(1);

                    if (alphaAttributeName != null && betaAttributeName != null)
                    {

                        performMatchPropertiesTestForAttributePair(alphaAttributeName, betaAttributeName, ANY);

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

                for (String testAttributeName : definedAttributeNames)
                {

                    if (propertyCatMap.get(testAttributeName) == OM_PRIMITIVE_TYPE_STRING)
                    {

                        /*
                         * This is a string attribute....
                         */
                        stringAttributeName = testAttributeName;
                        break;
                    }
                }

                if (stringAttributeName != null)
                {

                    performSearchCriteriaTests(stringAttributeName, RegexMatchType.Exact);

                    performSearchCriteriaTests(stringAttributeName, RegexMatchType.Prefix);

                    performSearchCriteriaTests(stringAttributeName, RegexMatchType.Suffix);

                    performSearchCriteriaTests(stringAttributeName, RegexMatchType.Contains);

                }


                /*
                 * Perform type filtering tests
                 */
                for (String attributeName : definedAttributeNames)
                {

                    performTypeFilteringTests(attributeName);

                }


                /*
                 * Perform generalised regex tests - these are part of the ENTITY_ADVANCED_SEARCH profile
                 */

                /*
                 * Look for a suitable (string) attribute...
                 */
                stringAttributeName = null;

                for (String testAttributeName : definedAttributeNames)
                {

                    if (propertyCatMap.get(testAttributeName) == OM_PRIMITIVE_TYPE_STRING)
                    {

                        /*
                         * This is a string attribute....
                         */
                        stringAttributeName = testAttributeName;
                        break;
                    }
                }

                if (stringAttributeName != null)
                {

                    performAdvancedSearchTests(stringAttributeName, RegexMatchType.Exact);
                    performAdvancedSearchTests(stringAttributeName, RegexMatchType.Prefix);
                    performAdvancedSearchTests(stringAttributeName, RegexMatchType.Suffix);
                    performAdvancedSearchTests(stringAttributeName, RegexMatchType.Contains);
                }


                /*
                 * Completion of searches - indicate success of testcase.
                 */
                super.setSuccessMessage("Entities can be searched by property and property value");
            }


            /*
             * Run finds for this type, with no matchProperties or searchPreopties, using
             * both findEntitiesByProperty and findENtities. (findEntitiesByPropertyValue is not
             * included because searchCriteria is mandatory).
             */
            performNonPropertySearchTests();
        }
    }





    private List<String> pickAttributePair()
    {

        List<String> returnList = null;

        Set<String> propertyNameSet = propertyValueMap.keySet();
        if (propertyNameSet.size() >= 2)
        {
            String alphaAttributeName = null;
            String betaAttributeName = null;

            for (String attributeName : definedAttributeNames)
            {
                Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
                Iterator<Object> possibleValueIterator = possibleValues.iterator();
                /*
                 * Perform a search using the first discovered value for the property
                 */
                if (possibleValueIterator.hasNext())
                {
                    alphaAttributeName = attributeName;
                    break;
                }
            }

            for (String attributeName : definedAttributeNames)
            {
                if (!attributeName.equals(alphaAttributeName))
                {
                    Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
                    Iterator<Object> possibleValueIterator = possibleValues.iterator();
                    /*
                     * Perform a search using the first discovered value for the property
                     */
                    if (possibleValueIterator.hasNext())
                    {
                        betaAttributeName = attributeName;
                        break;

                    }
                }
            }

            if (alphaAttributeName != null && betaAttributeName != null)
            {
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
        if (possibleValueIterator.hasNext())
        {

            Object value = possibleValueIterator.next();

            InstanceProperties matchProperties = new InstanceProperties();

            PrimitivePropertyValue ppv = new PrimitivePropertyValue();
            ppv.setPrimitiveDefCategory(propertyCatMap.get(attributeName));
            if (propertyCatMap.get(attributeName) == OM_PRIMITIVE_TYPE_STRING)
            {
                String literalisedValue = literaliseStringProperty((String) value);
                ppv.setPrimitiveValue(literalisedValue);
            }
            else
            {
                ppv.setPrimitiveValue(value);
            }


            matchProperties.setProperty(attributeName, ppv);


            /*
             * Formulate expected result
             */
            List<String> entitiesWithValue = propertyValueMap.get(attributeName).get(value);
            List<String> expectedGUIDs = null;

            switch (matchCriteria)
            {
                case ALL:
                case ANY:
                    /* This is a single property test, so ANY and ALL are equivalent */
                    expectedGUIDs = entitiesWithValue;
                    break;
                case NONE:
                    expectedGUIDs = diff(knownInstancesGUIDs, entitiesWithValue);
                    break;
                default:
                    /* Invalid matchCriteria value passed */
                    return;
            }

            int expectedEntityCount = expectedGUIDs.size();
            // In the case where the instances were created, expected may exceed pageSize.

            /*
             * Search....
             */

            List<EntityDetail> result;

            Map<String,String> parameters = getParameters(entityDef.getGUID(), matchProperties, matchCriteria);

            long elapsedTime;
            try
            {
                long start = System.currentTimeMillis();
                result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                                   entityDef.getGUID(),
                                                                   matchProperties,
                                                                   matchCriteria,
                                                                   0,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   pageSize);
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (FunctionNotSupportedException exc)
            {

                /*
                 * Because the above test only exercises one optional function (advanced regex support)
                 * we can assert that it is that function that is not supported.
                 */

                super.addNotSupportedAssertion(assertion101FEBP,
                                               assertionMsg_101FEBP + exc.getMessage(),
                                               RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());

                return;

            }
            catch (RepositoryTimeoutException exc)
            {

                /*
                 * Such a query may simply timeout, in which case we do not have enough information
                 * to know whether this optional function is supported or not.
                 */
                super.addDiscoveredProperty("query timeouts",
                                            true,
                                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
                return;

            }
            catch(Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findEntitiesByProperty";
                String operationDescription = "find entities of type " + entityDef.getName();
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

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
             * Check that the expected number of entities was returned. This has to consider the effect of the original
             * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
             * But if the limit was hit (on the original search) then there may be additional instances in the repository
             * that were not seen on the original search; the expected result was computed from only those instance that WERE seen,
             * so the expectation may be a subset of the actual. If we hit page size there may be additional instances that were
             * not included in the initial set, due to the initial set being limited by pageSize; the narrower search may
             * pull in additional entities that were not discovered previously.
             *
             */


            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition, the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedEntityCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedEntityCount <  pageSize && resultCount >= expectedEntityCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(assertionMsg_3, resultCount, expectedEntityCount, parameters);
            assertCondition((acceptable_result_size),
                            assertion3,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId(),
                            "findEntitiesByProperty",
                            elapsedTime);


            /*
             * If there were any result, check that all expected entities were returned and (in the pageLimited case) that any
             * additional entities were valid results for the search.
             */
            if (resultCount > 0)
            {

                List<String> resultGUIDs = new ArrayList<>();
                for (EntityDetail entity : result)
                {
                    resultGUIDs.add(entity.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether there are (or may be) more entities than the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited)
                {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                    {
                        unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;
                    }

                }
                else
                { // pageLimited, so need to allow for and verify hitherto unseen instances

                    for (EntityDetail entity : result)
                    {
                        if (!(expectedGUIDs.contains(entity.getGUID())))
                        {
                            /*
                             * This was an extra entity that we either did not expect or that we have not seen previously.
                             * Check it is a valid result.
                             */
                            InstanceProperties entityProperties = entity.getProperties();
                            if (entityProperties != null)
                            {
                                InstancePropertyValue ipValue = entityProperties.getPropertyValue(attributeName);
                                if (ipValue != null)
                                {
                                    InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                                    {

                                        Object primitiveValue = ipValue.valueAsObject();

                                        /*
                                         * Check for inequality and fail the match if unequal.
                                         * This is because, even for strings, we used an exact match literalised property value
                                         * and match criteria was ALL - so an entity with an unequal property is not a valid result.
                                         */
                                        switch (matchCriteria)
                                        {
                                            case ALL:
                                            case ANY:
                                                /* This is a single property test, so ANY and ALL are equivalent */
                                                if (!primitiveValue.equals(value))
                                                {
                                                    unexpectedResult = "('" + primitiveValue + "' for guid=" + entity.getGUID() + ")";
                                                }
                                                break;
                                            case NONE:
                                                if (primitiveValue.equals(value)) {
                                                    unexpectedResult = "('" + primitiveValue + "' for guid=" + entity.getGUID() + ")";
                                                }
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

                assertionMessage = MessageFormat.format(assertionMsg_4, unexpectedResult, parameters.toString());
                assertCondition(unexpectedResult.equals("0"),
                                assertion4,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
            }
        }
    }


    private void performMatchPropertiesTestForAttributePair(String        alphaAttributeName,
                                                            String        betaAttributeName,
                                                            MatchCriteria matchCriteria) throws Exception
    {
        PrimitivePropertyValue alphaPpv = null;
        PrimitivePropertyValue betaPpv = null;

        Object alphaValue = null;
        Object betaValue = null;

        /*
         * Select first available value for alphaAttribute
         */
        Set<Object> possibleAlphaValues = propertyValueMap.get(alphaAttributeName).keySet();
        Iterator<Object> possibleAlphaValueIterator = possibleAlphaValues.iterator();

        if (possibleAlphaValueIterator.hasNext())
        {
            alphaValue = possibleAlphaValueIterator.next();

            alphaPpv = new PrimitivePropertyValue();
            alphaPpv.setPrimitiveDefCategory(propertyCatMap.get(alphaAttributeName));
            if (propertyCatMap.get(alphaAttributeName) == OM_PRIMITIVE_TYPE_STRING)
            {
                String literalisedValue = literaliseStringProperty((String) alphaValue);
                alphaPpv.setPrimitiveValue(literalisedValue);
            }
            else
            {
                alphaPpv.setPrimitiveValue(alphaValue);
            }
        }

        /*
         * Select first available value for betaAttribute
         */
        Set<Object> possibleBetaValues = propertyValueMap.get(betaAttributeName).keySet();
        Iterator<Object> possibleBetaValueIterator = possibleBetaValues.iterator();

        if (possibleBetaValueIterator.hasNext())
        {
            betaValue = possibleBetaValueIterator.next();

            betaPpv = new PrimitivePropertyValue();
            betaPpv.setPrimitiveDefCategory(propertyCatMap.get(betaAttributeName));
            if (propertyCatMap.get(betaAttributeName) == OM_PRIMITIVE_TYPE_STRING)
            {
                String literalisedValue = literaliseStringProperty((String) betaValue);
                betaPpv.setPrimitiveValue(literalisedValue);
            }
            else
            {
                betaPpv.setPrimitiveValue(betaValue);
            }

        }

        if (alphaPpv != null && alphaValue != null && betaPpv != null && betaValue != null)
        {
            InstanceProperties matchProperties = new InstanceProperties();
            matchProperties.setProperty(alphaAttributeName, alphaPpv);
            matchProperties.setProperty(betaAttributeName, betaPpv);


            /*
             * Formulate expected result
             */

            List<String> entitiesWithAlphaValue = propertyValueMap.get(alphaAttributeName).get(alphaValue);
            List<String> entitiesWithBetaValue = propertyValueMap.get(betaAttributeName).get(betaValue);
            List<String> expectedGUIDs = null;

            switch (matchCriteria)
            {
                case ALL:
                    /* MatchCriteria.ALL ==> INTERSECTION */
                    expectedGUIDs = intersection(entitiesWithAlphaValue, entitiesWithBetaValue);
                    break;
                case ANY:
                    /* MatchCriteria.ANY ==> UNION */
                    expectedGUIDs = union(entitiesWithAlphaValue, entitiesWithBetaValue);
                    break;
                case NONE:
                    /* MatchCriteria.NONE ==> UNION COMPLEMENT */
                    expectedGUIDs = diff(knownInstancesGUIDs, entitiesWithAlphaValue);
                    expectedGUIDs = diff(expectedGUIDs, entitiesWithBetaValue);
                    break;
                default:
                    /* Invalid matchCriteria value passed */
                    return;
            }

            int expectedEntityCount = expectedGUIDs.size();



            /*
             * Search....
             */

            List<EntityDetail> result;

            Map<String,String> parameters = getParameters(entityDef.getGUID(), matchProperties, matchCriteria);

            long elapsedTime;
            try
            {
                long start = System.currentTimeMillis();
                result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                                   entityDef.getGUID(),
                                                                   matchProperties,
                                                                   matchCriteria,
                                                                   0,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   pageSize);
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (FunctionNotSupportedException exc)
            {

                /*
                 * Because the above test only exercises one optional function (advanced regex support)
                 * we can assert that it is that function that is not supported.
                 */

                super.addNotSupportedAssertion(assertion101FEBP,
                                               assertionMsg_101FEBP + exc.getMessage(),
                                               RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());

                return;

            }
            catch (RepositoryTimeoutException exc)
            {

                /*
                 * Such a query may simply timeout, in which case we do not have enough information
                 * to know whether this optional function is supported or not.
                 */
                super.addDiscoveredProperty("query timeouts",
                                            true,
                                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
                return;

            }
            catch(Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findEntitiesByProperty";
                String operationDescription = "find entities of type " + entityDef.getName();
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }


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

            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();

            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition, the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedEntityCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedEntityCount <  pageSize && resultCount >= expectedEntityCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(assertionMsg_5, resultCount, expectedEntityCount, parameters);
            assertCondition((acceptable_result_size),
                            assertion5,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId(),
                            "findEntitiesByProperty",
                            elapsedTime);



            /*
             * If there were any result, check that all expected entities were returned and (in the pageLimited case) that any
             * additional entities were valid results for the search.
             */
            if (resultCount > 0)
            {
                List<String> resultGUIDs = new ArrayList<>();
                for (EntityDetail entity : result)
                {
                    resultGUIDs.add(entity.getGUID());
                }

                /*
                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";
                String alpha = "";
                String beta = "";

                if (!pageLimited)
                {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                    {
                        unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;
                    }
                }
                else
                { // pageLimited, so need to allow for and verify hitherto unseen instances

                    for (EntityDetail entity : result)
                    {
                        if (!(expectedGUIDs.contains(entity.getGUID())))
                        {
                            /*
                             * This was an extra entity that we either did not expect or that we have not seen previously.
                             * Check it is a valid result.
                             */

                            InstanceProperties entityProperties = entity.getProperties();

                            boolean alphaMatch = false;

                            if (entityProperties != null)
                            {

                                InstancePropertyValue alphaIPValue = entityProperties.getPropertyValue(alphaAttributeName);
                                if (alphaIPValue != null)
                                {
                                    InstancePropertyCategory ipCategory = alphaIPValue.getInstancePropertyCategory();
                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                                    {
                                        Object primitiveValue = alphaIPValue.valueAsObject();
                                        alphaMatch = primitiveValue.equals(alphaValue);
                                        alpha = primitiveValue.toString();
                                    }
                                }
                            }

                            boolean betaMatch = false;

                            if (entityProperties != null)
                            {

                                InstancePropertyValue betaIPValue = entityProperties.getPropertyValue(betaAttributeName);
                                if (betaIPValue != null)
                                {
                                    InstancePropertyCategory ipCategory = betaIPValue.getInstancePropertyCategory();
                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                                    {
                                        Object primitiveValue = betaIPValue.valueAsObject();
                                        betaMatch = primitiveValue.equals(betaValue);
                                        beta = primitiveValue.toString();
                                    }
                                }
                            }


                            switch (matchCriteria)
                            {
                                case ALL:
                                    if (!(alphaMatch && betaMatch))
                                        unexpectedResult = "(" + alphaAttributeName + "='" + alpha + "', " + betaAttributeName + "='" + beta + "' for guid=" + entity.getGUID() + ")";
                                    break;
                                case ANY:
                                    if (!(alphaMatch || betaMatch))
                                        unexpectedResult = "(" + alphaAttributeName + "='" + alpha + "', " + betaAttributeName + "='" + beta + "' for guid=" + entity.getGUID() + ")";
                                    break;
                                case NONE:
                                    if (!(!alphaMatch && !betaMatch))
                                        unexpectedResult = "(" + alphaAttributeName + "='" + alpha + "', " + betaAttributeName + "='" + beta + "' for guid=" + entity.getGUID() + ")";
                                    break;
                                default:
                                    /* Invalid matchCriteria value passed */
                                    return;
                            }
                        }
                    }
                }

                assertionMessage = MessageFormat.format(assertionMsg_6, unexpectedResult, parameters.toString());
                assertCondition(unexpectedResult.equals("0"),
                                assertion6,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
            }
        }
    }


    private enum RegexMatchType
    {
        Exact,
        Prefix,
        Suffix,
        Contains
    }



    private void performSearchCriteriaTests(String attributeName, RegexMatchType matchType) throws Exception
    {

        /*
         * The given attribute is tested for exact, prefix, suffix and contains matches for each of the values already seen.
         * All these searches should return at least some instances in the result
         */

        Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();

        for (Object possibleValue : possibleValues)
        {

            String stringValue = (String) possibleValue;
            String truncatedStringValue = null;
            String literalisedValue = null;
            int stringValueLength;
            int truncatedLength;

            switch (matchType)
            {

                case Exact:
                    /* EXACT MATCH */
                    literalisedValue = literaliseStringPropertyExact(stringValue);
                    break;
                case Prefix:
                    /* PREFIX MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 2)
                    {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                    truncatedStringValue = stringValue.substring(0, truncatedLength);
                    literalisedValue = literaliseStringPropertyStartsWith(truncatedStringValue);
                    break;
                case Suffix:
                    /* SUFFIX MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 2)
                    {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                    truncatedStringValue = stringValue.substring(stringValueLength - truncatedLength, stringValueLength);
                    literalisedValue = literaliseStringPropertyEndsWith(truncatedStringValue);
                    break;
                case Contains:
                    /* CONTAINS MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 3)
                    {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.floor(stringValueLength / 2.0));
                    int diff = stringValueLength - truncatedLength;
                    int halfDiff = diff / 2;
                    truncatedStringValue = stringValue.substring(halfDiff, stringValueLength - halfDiff);
                    literalisedValue = literaliseStringPropertyContains(truncatedStringValue);
                    break;
            }




            /*
             * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
             * Find all the values (regardless of attributeName) in the map that are an exact match to the search value
             * Care needed to detect entities that are matched by more than one property - to avoid duplication it's
             * important to check that the entity was not already included in the expected set.
             */
            int expectedEntityCount = 0;
            List<String> expectedGUIDs = new ArrayList<>();
            Set<String> propertyNamesSet = propertyValueMap.keySet();
            for (String propName : propertyNamesSet)
            {
                if (propertyCatMap.get(propName) == OM_PRIMITIVE_TYPE_STRING)
                {
                    Map<Object, List<String>> propValues = propertyValueMap.get(propName);
                    Set<Object> propertyValuesSet = propValues.keySet();
                    Iterator<Object> propertyValuesSetIterator = propertyValuesSet.iterator();
                    while (propertyValuesSetIterator.hasNext())
                    {
                        String knownStringValue = (String) (propertyValuesSetIterator.next());

                        switch (matchType)
                        {
                            case Exact:
                                /* EXACT MATCH */
                                if (stringValue.equals(knownStringValue))
                                {
                                    for (String matchGUID : propValues.get(knownStringValue))
                                    {
                                        if (!expectedGUIDs.contains(matchGUID))
                                        {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Prefix:
                                /* PREFIX MATCH */
                                if (knownStringValue.startsWith(truncatedStringValue))
                                {
                                    for (String matchGUID : propValues.get(knownStringValue))
                                    {
                                        if (!expectedGUIDs.contains(matchGUID))
                                        {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Suffix:
                                /* SUFFIX MATCH */
                                if (knownStringValue.endsWith(truncatedStringValue))
                                {
                                    for (String matchGUID : propValues.get(knownStringValue))
                                    {
                                        if (!expectedGUIDs.contains(matchGUID))
                                        {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Contains:
                                /* CONTAINS MATCH */
                                if (knownStringValue.contains(truncatedStringValue))
                                {
                                    for (String matchGUID : propValues.get(knownStringValue))
                                    {
                                        if (!expectedGUIDs.contains(matchGUID))
                                        {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }
            expectedEntityCount = expectedGUIDs.size();


            /*
             * Search....
             */

            List<EntityDetail> result;

            Map<String, String> parameters = getParameters(entityDef.getGUID(), literalisedValue);

            long elapsedTime;
            try
            {

                long start = System.currentTimeMillis();
                result = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                                                                        entityDef.getGUID(),
                                                                        literalisedValue,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        pageSize);
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (FunctionNotSupportedException exc)
            {

                /*
                 * Because the above test only exercises one optional function (advanced regex support)
                 * we can assert that it is that function that is not supported.
                 */

                super.addNotSupportedAssertion(assertion101FEBPV,
                                               assertionMsg_101FEBPV + exc.getMessage(),
                                               RepositoryConformanceProfileRequirement.ENTITY_VALUE_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.ENTITY_VALUE_SEARCH.getRequirementId());

                return;

            }
            catch (RepositoryTimeoutException exc) {

                /*
                 * Such a query may simply timeout, in which case we do not have enough information
                 * to know whether this optional function is supported or not.
                 */
                super.addDiscoveredProperty("query timeouts",
                                            true,
                                            RepositoryConformanceProfileRequirement.ENTITY_VALUE_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.ENTITY_VALUE_SEARCH.getRequirementId());
                return;

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findEntitiesByPropertyValue";
                String operationDescription = "find entities using repository helper regex for type " + entityDef.getName();
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }

            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedEntityCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedEntityCount < pageSize && resultCount >= expectedEntityCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(assertionMsg_7, resultCount, expectedEntityCount, parameters);
            assertCondition((acceptable_result_size),
                    assertion7,
                    assertionMessage,
                    RepositoryConformanceProfileRequirement.ENTITY_VALUE_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_VALUE_SEARCH.getRequirementId(),
                    "findEntitiesByPropertyValue",
                    elapsedTime);


            /*
             * If there were any results, check that all expected entities were returned and (in the pageLimited case) that any
             * additional entities were valid results for the search.
             */
            if (resultCount > 0)
            {

                List<String> resultGUIDs = new ArrayList<>();
                for (EntityDetail entity : result)
                {
                    resultGUIDs.add(entity.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited)
                {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                    {
                        unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;
                    }
                }
                else
                { // pageLimited, so need to allow for and verify hitherto unseen instances

                    for (EntityDetail entity : result)
                    {

                        if (!(expectedGUIDs.contains(entity.getGUID())))
                        {
                            /*
                             * This was an extra entity that we either did not expect or that we have not seen previously.
                             * Check it is a valid result. It can have any string attribute with the same value as strValue.
                             */
                            boolean validEntity = false;
                            InstanceProperties entityProperties = entity.getProperties();
                            if (entityProperties != null)
                            {
                                Set<String> entityPropertyNames = entityProperties.getInstanceProperties().keySet();
                                for (String propertyName : entityPropertyNames)
                                {
                                    InstancePropertyValue ipValue = entityProperties.getPropertyValue(attributeName);
                                    if (ipValue != null)
                                    {
                                        InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                        if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                                        {
                                            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipValue;
                                            PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                                            if (pdCat == OM_PRIMITIVE_TYPE_STRING)
                                            {
                                                String propertyValueAsString = (String) (ppv.getPrimitiveValue());

                                                switch (matchType) {
                                                    case Exact:
                                                        /* EXACT MATCH */
                                                        if (propertyValueAsString.equals(stringValue))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Prefix:
                                                        /* PREFIX MATCH */
                                                        if (propertyValueAsString.startsWith(truncatedStringValue))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Suffix:
                                                        /* SUFFIX MATCH */
                                                        if (propertyValueAsString.endsWith(truncatedStringValue))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Contains:
                                                        /* CONTAINS MATCH */
                                                        if (propertyValueAsString.contains(truncatedStringValue))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            if (!validEntity)
                            {
                                unexpectedResult = "(guid=" + entity.getGUID() + ")";
                            }
                        }
                    }
                }

                assertionMessage = MessageFormat.format(assertionMsg_8, unexpectedResult, parameters.toString());
                assertCondition(unexpectedResult.equals("0"),
                        assertion8,
                        assertionMessage,
                        RepositoryConformanceProfileRequirement.ENTITY_VALUE_SEARCH.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_VALUE_SEARCH.getRequirementId());
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
        return getPropertiesForTypeDef(userId, typeDef);
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


        try
        {
            /*
             * Clone the match properties to a new IP object, iterate over the matchProperties and for any that are primitive string type call the repo helper and set the literalised value in the cloned properties object
             */
            InstanceProperties literalisedInstanceProperties = new InstanceProperties(matchProperties);

            OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();

            Iterator<String> propertyNames = matchProperties.getPropertyNames();

            if (propertyNames != null)
            {
                while (propertyNames.hasNext())
                {
                    String propertyName = propertyNames.next();
                    InstancePropertyValue instancePropertyValue = matchProperties.getPropertyValue(propertyName);

                    InstancePropertyCategory ipCat = instancePropertyValue.getInstancePropertyCategory();
                    if (ipCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) instancePropertyValue;
                        PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                        if (pdCat == OM_PRIMITIVE_TYPE_STRING)
                        {
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
        catch (Exception e)
        {
            return null;  // This should force an InvalidParameterException from the MDC under test.
        }

    }


    /*
     * Return the union of the two lists
     */
    private List<String> union(List<String> l1, List<String> l2)
    {
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
    private List<String> intersection(List<String> l1, List<String> l2)
    {
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
    private List<String> diff(List<String> lu, List<String> ld)
    {
        if (lu == null)
            return new ArrayList<>();

        List<String> comp = new ArrayList<>(lu);
        if (ld != null)
        {
            for (String s : ld)
            {
                comp.remove(s);
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
        return repositoryHelper.getExactMatchRegex(value);
    }

    /*
     *  Method to literalise a string value for exact match.
     *
     */
    public String literaliseStringPropertyExact(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        return repositoryHelper.getExactMatchRegex(value);
    }

    /*
     *  Method to literalise a string value for prefix match.
     *
     */
    public String literaliseStringPropertyStartsWith(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        return repositoryHelper.getStartsWithRegex(value);
    }

    /*
     *  Method to literalise a string value for suffix match.
     *
     */
    public String literaliseStringPropertyEndsWith(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        return repositoryHelper.getEndsWithRegex(value);
    }

    /*
     *  Method to literalise a string value for suffix match.
     *
     */
    public String literaliseStringPropertyContains(String value)
    {
        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        return repositoryHelper.getContainsRegex(value);
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
        if (possibleValueIterator.hasNext())
        {

            Object value = possibleValueIterator.next();

            InstanceProperties matchProperties = new InstanceProperties();

            PrimitivePropertyValue ppv = new PrimitivePropertyValue();
            ppv.setPrimitiveDefCategory(propertyCatMap.get(attributeName));
            if (propertyCatMap.get(attributeName) == OM_PRIMITIVE_TYPE_STRING)
            {
                String literalisedValue = literaliseStringProperty((String) value);
                ppv.setPrimitiveValue(literalisedValue);
            }
            else
            {
                ppv.setPrimitiveValue(value);
            }


            matchProperties.setProperty(attributeName, ppv);


            boolean wildSearchPageLimited = false;

            /*
             * Perform the search without type filtering
             */
            List<EntityDetail> result;

            try
            {
                result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                                   null,
                                                                   matchProperties,
                                                                   MatchCriteria.ALL,
                                                                   0,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   pageSize);
            }
            catch (FunctionNotSupportedException exc)
            {

                /*
                 * Because the above test only exercises one optional function (advanced regex support)
                 * we can assert that it is that function that is not supported.
                 */

                super.addNotSupportedAssertion(assertion101FEBP,
                                               assertionMsg_101FEBP + exc.getMessage(),
                                               RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());

                return;

            }
            catch (RepositoryTimeoutException exc)
            {

                /*
                 * Such a query may simply timeout, in which case we do not have enough information
                 * to know whether this optional function is supported or not.
                 */
                super.addDiscoveredProperty("query timeouts",
                                            true,
                                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
                return;

            }
            catch(Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findEntitiesByProperty";
                String operationDescription = "find entities with no type filter ";
                Map<String,String> parameters = getParameters(null, matchProperties, MatchCriteria.ALL);
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }


            List<String> expectedGUIDs;

            if (result != null)
            {
                if (result.size() == pageSize)
                {
                    /*
                     * Need to note whether this wild search hit the page limit - if so then there may be more
                     * entities that we have not seen; pull the expected list together (below) and the expected
                     * count, and tolerate and verify any additional entities that are returned in the more
                     * type-specific search below.
                     */
                    wildSearchPageLimited = true;
                }
                /*
                 * Count the entities that are of current type or subtypes...
                 */
                String entityTypeName = entityDef.getName();
                List<String> countableTypeNames = new ArrayList<>();
                List<String> countableSubTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityTypeName);
                if (countableSubTypeNames != null)
                {
                    countableTypeNames.addAll(countableSubTypeNames);
                }
                countableTypeNames.add(entityTypeName);

                expectedGUIDs = new ArrayList<>();

                for (EntityDetail entityDetail : result)
                {
                    String typeName = entityDetail.getType().getTypeDefName();
                    if (countableTypeNames.contains(typeName))
                    {
                        expectedGUIDs.add(entityDetail.getGUID());
                    }
                }

                int expectedEntityCount = expectedGUIDs.size();


                /*
                 * Repeat the search being specific about type
                 */

                Map<String,String> parameters = getParameters(entityDef.getGUID(), matchProperties, MatchCriteria.ALL);

                long elapsedTime;
                try
                {

                    long start = System.currentTimeMillis();
                    result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                                       entityDef.getGUID(),
                                                                       matchProperties,
                                                                       MatchCriteria.ALL,
                                                                       0,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       pageSize);
                    elapsedTime = System.currentTimeMillis() - start;
                }
                catch (FunctionNotSupportedException exc)
                {

                    /*
                     * Because the above test only exercises one optional function (advanced regex support)
                     * we can assert that it is that function that is not supported.
                     */

                    super.addNotSupportedAssertion(assertion101FEBP,
                                                   assertionMsg_101FEBP + exc.getMessage(),
                                                   RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                                   RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());

                    return;

                }
                catch (RepositoryTimeoutException exc)
                {

                    /*
                     * Such a query may simply timeout, in which case we do not have enough information
                     * to know whether this optional function is supported or not.
                     */
                    super.addDiscoveredProperty("query timeouts",
                                                true,
                                                RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                                RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
                    return;

                }
                catch(Exception exc)
                {
                    /*
                     * We are not expecting any exceptions from this method call. Log and fail the test.
                     */

                    String methodName = "findEntitiesByProperty";
                    String operationDescription = "find entities of type " + entityDef.getName();
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

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
                boolean unlimited_case = !wildSearchPageLimited && resultCount == expectedEntityCount;
                /*
                 * If the broader wild query hit the page limit then we have to tolerate hitherto unseen instances in the results.
                 * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
                 * So in that latter case we need to accept Min().
                 */
                boolean limited_large_case = wildSearchPageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
                boolean limited_small_case = wildSearchPageLimited && expectedEntityCount < pageSize && resultCount >= expectedEntityCount;
                boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

                String assertionMessage = MessageFormat.format(assertionMsg_9, resultCount, expectedEntityCount, parameters);
                assertCondition((acceptable_result_size),
                                assertion9,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId(),
                                "findEntitiesByProperty",
                                elapsedTime);


                /*
                 * If there were any result, check that all expected entities were returned and (in the pageLimited case) that any
                 * additional entities were valid results for the search.
                 */
                if (resultCount > 0)
                {
                    List<String> resultGUIDs = new ArrayList<>();
                    for (EntityDetail entity : result)
                    {
                        resultGUIDs.add(entity.getGUID());
                    }


                    /*
                     * Here again, we need to be sensitive to whether there are (or may be) more entities than the page limit.
                     * If the original search hit the limit then we may legitimately receive additional instances in the results
                     * of a narrower search. But not if the original result set was under the page limit.
                     */

                    String unexpectedResult = "0";

                    if (!pageLimited)
                    {
                        if (!resultGUIDs.containsAll(expectedGUIDs))
                        {
                            unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;
                        }

                    }
                    else
                    { // pageLimited, so need to allow for and verify hitherto unseen instances

                        for (EntityDetail entity : result)
                        {

                            if (!(expectedGUIDs.contains(entity.getGUID())))
                            {
                                /*
                                 * This was an extra entity that we either did not expect or that we have not seen previously.
                                 * Check it is a valid result.
                                 */
                                InstanceProperties entityProperties = entity.getProperties();
                                if (entityProperties != null)
                                {
                                    InstancePropertyValue ipValue = entityProperties.getPropertyValue(attributeName);
                                    if (ipValue != null)
                                    {
                                        InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                        if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                                        {
                                            Object primitiveValue = ipValue.valueAsObject();

                                            /*
                                             * Check for inequality and fail the match if unequal.
                                             * This is because we used an exact match literalised property value
                                             * and match criteria was ALL - so an entity with an unequal property
                                             * is not a valid result.
                                             */

                                            if (!primitiveValue.equals(value))
                                            {
                                                unexpectedResult = "('" + primitiveValue.toString() + "' for guid=" + entity.getGUID() + ")";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    assertionMessage = MessageFormat.format(assertionMsg_10, unexpectedResult, parameters.toString());
                    assertCondition(unexpectedResult.equals("0"),
                                    assertion10,
                                    assertionMessage,
                                    RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                    RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
                }
            }
        }
    }




    /*
     *  This method tests ability to handle arbitrary regular expressions.
     *  This method includes both searchCriteria based findEntitiesByPropertyValue tests and matchProperty based findEntitiesByProperty tests
     */
    private void performAdvancedSearchTests(String attributeName, RegexMatchType matchType) throws Exception {

        /*
         * The given attribute is tested for exact, prefix, suffix and contains matches for each of the values already seen.
         * All these searches should return at least some instances in the result; some may match more than a page full.
         */

        Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();

        for (Object possibleValue : possibleValues)
        {
            String stringValue = (String) possibleValue;
            String truncatedStringValue = null;
            String regexValue = null;
            int stringValueLength;
            int truncatedLength;

            switch (matchType)
            {
                case Exact:
                    /* EXACT MATCH */
                    stringValue = escapeRegexSpecials(stringValue);
                    regexValue = stringValue;
                    break;
                case Prefix:
                    /* PREFIX MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 2)
                    {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                    truncatedStringValue = stringValue.substring(0, truncatedLength);
                    truncatedStringValue = escapeRegexSpecials(truncatedStringValue);
                    regexValue = truncatedStringValue + ".*";
                    break;
                case Suffix:
                    /* SUFFIX MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 2)
                    {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.ceil(stringValueLength / 2.0));
                    truncatedStringValue = stringValue.substring(stringValueLength - truncatedLength, stringValueLength);
                    truncatedStringValue = escapeRegexSpecials(truncatedStringValue);
                    regexValue = ".*" + truncatedStringValue;
                    break;
                case Contains:
                    /* CONTAINS MATCH */
                    stringValueLength = stringValue.length();
                    if (stringValueLength < 3)
                    {
                        return; /* not a long enough string to perform a meaningful test */
                    }
                    truncatedLength = (int) (Math.floor(stringValueLength / 2.0));
                    int diff = stringValueLength - truncatedLength;
                    int halfDiff = diff / 2;
                    truncatedStringValue = stringValue.substring(halfDiff, stringValueLength - halfDiff);
                    truncatedStringValue = escapeRegexSpecials(truncatedStringValue);
                    regexValue = ".*" + truncatedStringValue + ".*";
                    break;
            }




            /*
             * Expected result size - this really is a minimum expectation - other instances' properties may match, if so they will be validated retrospectively
             * Find all the values (regardless of attributeName) in the map that are an exact match to the search value
             * Care needed to detect entities that are matched by more than one property - to avoid duplication it's
             * important to check that the entity was not already included in the expected set.
             */
            int expectedEntityCount = 0;
            List<String> expectedGUIDs = new ArrayList<>();
            Set<String> propertyNamesSet = propertyValueMap.keySet();
            for (String propName : propertyNamesSet)
            {
                if (propertyCatMap.get(propName) == OM_PRIMITIVE_TYPE_STRING)
                {
                    Map<Object, List<String>> propValues = propertyValueMap.get(propName);
                    Set<Object> propertyValuesSet = propValues.keySet();
                    for (Object o : propertyValuesSet) {
                        String knownStringValue = (String) o;

                        switch (matchType)
                        {
                            case Exact:
                                /* EXACT MATCH */
                                if (knownStringValue.matches(stringValue))
                                {
                                    for (String matchGUID : propValues.get(knownStringValue))
                                    {
                                        if (!expectedGUIDs.contains(matchGUID))
                                        {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Prefix:
                                /* PREFIX MATCH */
                                if (knownStringValue.matches(truncatedStringValue + ".*"))
                                {
                                    for (String matchGUID : propValues.get(knownStringValue))
                                    {
                                        if (!expectedGUIDs.contains(matchGUID))
                                        {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Suffix:
                                /* SUFFIX MATCH */
                                if (knownStringValue.matches(".*" + truncatedStringValue))
                                {
                                    for (String matchGUID : propValues.get(knownStringValue))
                                    {
                                        if (!expectedGUIDs.contains(matchGUID))
                                        {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                            case Contains:
                                /* CONTAINS MATCH */
                                if (knownStringValue.matches(".*" + truncatedStringValue + ".*"))
                                {
                                    for (String matchGUID : propValues.get(knownStringValue))
                                    {
                                        if (!expectedGUIDs.contains(matchGUID))
                                        {
                                            expectedGUIDs.add(matchGUID);
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }
            expectedEntityCount = expectedGUIDs.size();


            /*
             * Test search using findEntitiesByPropertyValue
             */

            List<EntityDetail> result;

            Map<String, String> parameters = getParameters(entityDef.getGUID(), regexValue);

            long elapsedTime;
            try
            {

                long start = System.currentTimeMillis();
                result = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                                                                        entityDef.getGUID(),
                                                                        regexValue,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        pageSize);
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (FunctionNotSupportedException exc)
            {

                /*
                 * Because the above test only exercises one optional function (advanced regex support)
                 * we can assert that it is that function that is not supported.
                 */

                super.addNotSupportedAssertion(assertion101FEBPVGEN,
                        assertionMsg_101FEBPVGEN + exc.getMessage(),
                        RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());

                return;

            }
            catch (RepositoryTimeoutException exc)
            {

                /*
                 * Such a query may simply timeout, in which case we do not have enough information
                 * to know whether this optional function is supported or not.
                 */
                super.addDiscoveredProperty("query timeouts",
                                            true,
                                            RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());
                return;

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findEntitiesByPropertyValue";
                String operationDescription = "find entities using general regex for type " + entityDef.getName();
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }


            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition, the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedEntityCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedEntityCount < pageSize && resultCount >= expectedEntityCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(assertionMsg_11, resultCount, expectedEntityCount, parameters);
            assertCondition((acceptable_result_size),
                    assertion11,
                    assertionMessage,
                    RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId(),
                    "findEntitiesByPropertyValue",
                    elapsedTime);


            /*
             * If there were any results, check that all expected entities were returned and (in the pageLimited case) that any
             * additional entities were valid results for the search.
             */
            if (resultCount > 0)
            {

                List<String> resultGUIDs = new ArrayList<>();
                for (EntityDetail entity : result)
                {
                    resultGUIDs.add(entity.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited)
                {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                    {
                        unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;
                    }
                }
                else
                { // pageLimited, so need to allow for and verify hitherto unseen instances

                    for (EntityDetail entity : result)
                    {
                        if (!(expectedGUIDs.contains(entity.getGUID())))
                        {
                            /*
                             * This was an extra entity that we either did not expect or that we have not seen previously.
                             * Check it is a valid result. It can have any string attribute with the same value as strValue.
                             */
                            boolean validEntity = false;
                            InstanceProperties entityProperties = entity.getProperties();
                            if (entityProperties != null)
                            {
                                Set<String> entityPropertyNames = entityProperties.getInstanceProperties().keySet();
                                for (String propertyName : entityPropertyNames)
                                {
                                    InstancePropertyValue ipValue = entityProperties.getPropertyValue(attributeName);
                                    if (ipValue != null)
                                    {
                                        InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                        if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                                        {
                                            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipValue;
                                            PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                                            if (pdCat == OM_PRIMITIVE_TYPE_STRING)
                                            {
                                                String propertyValueAsString = (String) (ppv.getPrimitiveValue());

                                                switch (matchType)
                                                {
                                                    case Exact:
                                                        /* EXACT MATCH */
                                                        if (propertyValueAsString.matches(stringValue))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Prefix:
                                                        /* PREFIX MATCH */
                                                        if (propertyValueAsString.matches(truncatedStringValue + ".*"))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Suffix:
                                                        /* SUFFIX MATCH */
                                                        if (propertyValueAsString.matches(".*" + truncatedStringValue))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Contains:
                                                        /* CONTAINS MATCH */
                                                        if (propertyValueAsString.matches(".*" + truncatedStringValue + ".*"))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            if (!validEntity)
                            {
                                unexpectedResult = "(guid=" + entity.getGUID() + ")";
                            }
                        }
                    }
                }

                assertionMessage = MessageFormat.format(assertionMsg_12, unexpectedResult, parameters);
                assertCondition(unexpectedResult.equals("0"),
                        assertion12,
                        assertionMessage,
                        RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_VALUE_SEARCH.getRequirementId());
            }




            /*
             * Repeat the same search using findEntitiesByProperty and a MatchProperties object
             */


            InstanceProperties matchProperties = new InstanceProperties();

            PrimitivePropertyValue mppv = new PrimitivePropertyValue();
            mppv.setPrimitiveDefCategory(propertyCatMap.get(attributeName));
            mppv.setPrimitiveValue(regexValue);
            matchProperties.setProperty(attributeName, mppv);

            parameters = getParameters(entityDef.getGUID(), matchProperties, MatchCriteria.ALL);

            try
            {
                long start = System.currentTimeMillis();
                result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                                   entityDef.getGUID(),
                                                                   matchProperties,
                                                                   MatchCriteria.ALL,
                                                                   0,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   pageSize);
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (FunctionNotSupportedException exc)
            {

                /*
                 * Because the above test only exercises one optional function (advanced regex support)
                 * we can assert that it is that function that is not supported.
                 */

                super.addNotSupportedAssertion(assertion101FEBPGEN,
                        assertionMsg_101FEBPGEN + exc.getMessage(),
                        RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());

                return;

            }
            catch (RepositoryTimeoutException exc)
            {

                /*
                 * Such a query may simply timeout, in which case we do not have enough information
                 * to know whether this optional function is supported or not.
                 */
                super.addDiscoveredProperty("query timeouts",
                                            true,
                                            RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());
                return;

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findEntitiesByProperty";
                String operationDescription = "find entities using general regex for type " + entityDef.getName();
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }


            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition, the result size should be no more than a page.
             */
            unlimited_case = !pageLimited && resultCount == expectedEntityCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            limited_large_case = pageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
            limited_small_case = pageLimited && expectedEntityCount < pageSize && resultCount >= expectedEntityCount;
            acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            assertionMessage = MessageFormat.format(assertionMsg_13, resultCount, expectedEntityCount, parameters);
            assertCondition((acceptable_result_size),
                    assertion13,
                    assertionMessage,
                    RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId(),
                    "findEntitiesByProperty",
                    elapsedTime);


            /*
             * If there were any results, check that all expected entities were returned and (in the pageLimited case) that any
             * additional entities were valid results for the search.
             */
            if (resultCount > 0)
            {
                List<String> resultGUIDs = new ArrayList<>();
                for (EntityDetail entity : result)
                {
                    resultGUIDs.add(entity.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether the original search hit the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited) {
                    if (!resultGUIDs.containsAll(expectedGUIDs))
                        unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;
                } else { // pageLimited, so need to allow for and verify hitherto unseen instances

                    for (EntityDetail entity : result)
                    {

                        if (!(expectedGUIDs.contains(entity.getGUID())))
                        {
                            /*
                             * This was an extra entity that we either did not expect or that we have not seen previously.
                             * Check it is a valid result. It can have any string attribute with the same value as strValue.
                             */
                            boolean validEntity = false;
                            InstanceProperties entityProperties = entity.getProperties();
                            if (entityProperties != null)
                            {
                                Set<String> entityPropertyNames = entityProperties.getInstanceProperties().keySet();
                                for (String propertyName : entityPropertyNames)
                                {
                                    InstancePropertyValue ipValue = entityProperties.getPropertyValue(attributeName);
                                    if (ipValue != null) {
                                        InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                        if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                                        {
                                            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipValue;
                                            PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                                            if (pdCat == OM_PRIMITIVE_TYPE_STRING)
                                            {
                                                String propertyValueAsString = (String) (ppv.getPrimitiveValue());

                                                switch (matchType)
                                                {
                                                    case Exact:
                                                        /* EXACT MATCH */
                                                        if (propertyValueAsString.matches(stringValue))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Prefix:
                                                        /* PREFIX MATCH */
                                                        if (propertyValueAsString.matches(truncatedStringValue + ".*"))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Suffix:
                                                        /* SUFFIX MATCH */
                                                        if (propertyValueAsString.matches(".*" + truncatedStringValue))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                    case Contains:
                                                        /* CONTAINS MATCH */
                                                        if (propertyValueAsString.matches(".*" + truncatedStringValue + ".*"))
                                                        {
                                                            validEntity = true;
                                                        }
                                                        break;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            if (!validEntity)
                            {
                                unexpectedResult = "(guid=" + entity.getGUID() + ")";
                            }
                        }
                    }
                }

                assertionMessage = MessageFormat.format(assertionMsg_14, unexpectedResult, parameters);
                assertCondition(unexpectedResult.equals("0"),
                        assertion14,
                        assertionMessage,
                        RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_ADVANCED_PROPERTY_SEARCH.getRequirementId());
            }
        }

    }

    private Map<String,String> getParameters(String entityTypeGuid,
                                             String searchCriteria) {
        Map<String,String> parameters = new TreeMap<>();
        parameters.put("entityTypeGUID", entityTypeGuid == null ? V_NULL : entityTypeGuid);
        parameters.put("searchCriteria", searchCriteria == null ? V_NULL : searchCriteria);
        parameters.put("fromEntityElement", Integer.toString(0));
        parameters.put("limitResultsByStatus", V_NULL);
        parameters.put("limitResultsByClassification", V_NULL);
        parameters.put("asOfTime", V_NULL);
        parameters.put("sequencingProperty", V_NULL);
        parameters.put("sequencingOrder", V_NULL);
        parameters.put("pageSize", Integer.toString(pageSize));
        return parameters;
    }

    private Map<String,String> getParameters(String entityTypeGuid,
                                             InstanceProperties matchProperties,
                                             MatchCriteria matchCriteria) {
        Map<String,String> parameters = new TreeMap<>();
        parameters.put("entityTypeGUID", entityTypeGuid == null ? V_NULL : entityTypeGuid);
        parameters.put("matchProperties", matchProperties == null ? V_NULL : matchProperties.toString());
        parameters.put("matchCriteria", matchCriteria == null ? V_NULL : matchCriteria.getName());
        parameters.put("fromEntityElement", Integer.toString(0));
        parameters.put("limitResultsByStatus", V_NULL);
        parameters.put("limitResultsByClassification", V_NULL);
        parameters.put("asOfTime", V_NULL);
        parameters.put("sequencingProperty", V_NULL);
        parameters.put("sequencingOrder", V_NULL);
        parameters.put("pageSize", Integer.toString(pageSize));
        return parameters;
    }

    /*
     * This method will escape any characters that are regex specials - i.e. have significance in a regex.
     * This is so that the regex (yet to be constructed) will be certain not to contain any characters that
     * could render the regex invalid.
     */
    private String escapeRegexSpecials(String inputString)
    {

        StringBuilder outputStringBldr = new StringBuilder();

        // Process chars
        for (int i = 0; i < inputString.length(); i++)
        {
            Character c = inputString.charAt(i);
            /*
             * No need to escape a '-' char as it is only significant if inside '[]' brackets, and these will be escaped,
             * so the '-' character has no special meaning
             */

            /*
             * Handle special chars - disjoint from alphas
             */
            switch (c) {
                case '.':
                case '[':
                case ']':
                case '^':
                case '*':
                case '(':
                case ')':
                case '$':
                case '{':
                case '}':
                case '|':
                case '+':
                case '?':
                case '\\':  // single backslash escaped for Java
                    outputStringBldr.append('\\').append(c);
                    continue;  // process the next character
            }
            /* You only reach this point if the character was not already intercepted and escaped.
             * The character is not special, so just append it...
             */
            outputStringBldr.append(c);
        }

        return outputStringBldr.toString();
    }


    /*
     * performSearchPropertiesTestForAttribute
     *
     * This set of test uses one attribute to construct a SearchProperties hierarchy
     * with a fixed structure and set of MatchCriteria and operators. This is to test
     * that the repository implements the findEntities method and can handle both
     * linear and nested SearchProperties objects and a variety of operators and
     * match criteria. It would not be sensible to try to test with a lot of different
     * permutations - there would simply be too many and the test would take too long
     * to run and not be particularly more useful.
     *
     * To maintain separation of test cases and profiles there is no use of the LIKE
     * operator as that is specific to String primitives and is part of the Advanced
     * profile which tests regex handling.
     *
     * The attribute will have come from the definedAttributes set and hence is a
     * primitive.
     *
     * The SearchProperties object will always have the following 'shape':
     *
     *
     * 	"matchProperties" : {            SP1
     *      "matchCriteria" : "ALL",
     *		"conditions" : [  propCons1
     *          {         PC1
     *              "property" : <attributeName>,
     *              "operator" : "EQ",
     *              "value"    : <attributeValue>
     *          },
     *          {        PC2
     *              "property" : <attributeName>,
     *              "operator" : "NEQ",
     *              "value"    : <modifiedAttributeValue>
     *          },
     *          {         PC3
     *              "nestedConditions" : {      SP2
     *                  "matchCriteria":"ANY",
     *                  "conditions" : [   propCons2
     *                      {         PC4
     *                          "property" : <attributeName>,
     *                          "operator" : "EQ",
     *                          "value"    : <attributeValue>
     *                      },
     *                      {       PC5
     *                          "property" : <attributeName>,
     *                          "operator" : "NEQ",
     *                          "value"    : <attributeValue>
     *                      }
     *                  ]
     *              }
     *          }
     *       ]
     *   }
     *  This should always deliver a TRUE result.
     */

    private void performSearchPropertiesTestForAttribute(String attributeName) throws Exception
    {
        Set<Object> possibleValues = propertyValueMap.get(attributeName).keySet();
        Iterator<Object> possibleValueIterator = possibleValues.iterator();

        /*
         * Perform a search using the first discovered value for the property
         */
        if (possibleValueIterator.hasNext())
        {

            Object value = possibleValueIterator.next();

            /*
             * Construct a SearchProperties object using this attribute and value.
             */


            /*
             * Need a 'good' PrimitiveTypePropertyValue to use in the search properties.
             */

            PrimitivePropertyValue ppvGood = new PrimitivePropertyValue();
            ppvGood.setPrimitiveDefCategory(propertyCatMap.get(attributeName));
            ppvGood.setPrimitiveValue(value);

            /*
             * Also need a 'bad' value for negative conditions
             */

            PrimitivePropertyValue ppvBad = getWrongPrimitivePropertyValue(propertyCatMap.get(attributeName), value);
            ppvBad.setPrimitiveDefCategory(propertyCatMap.get(attributeName));

            // Level 2 (lower level)
            SearchProperties searchProperties2 = new SearchProperties();

            List<PropertyCondition> propertyConditions2 = new ArrayList<>();

            PropertyCondition propCondition4 = new PropertyCondition();
            propCondition4.setProperty(attributeName);
            propCondition4.setOperator(PropertyComparisonOperator.EQ);
            propCondition4.setValue(ppvGood);
            propertyConditions2.add(propCondition4);

            PropertyCondition propCondition5 = new PropertyCondition();
            propCondition5.setProperty(attributeName);
            propCondition5.setOperator(PropertyComparisonOperator.NEQ);
            propCondition5.setValue(ppvGood);
            propertyConditions2.add(propCondition5);

            searchProperties2.setConditions(propertyConditions2);
            searchProperties2.setMatchCriteria(ANY);

            // Level 1 (higher level)
            SearchProperties searchProperties1 = new SearchProperties();

            List<PropertyCondition> propertyConditions1 = new ArrayList<>();

            PropertyCondition propCondition1 = new PropertyCondition();
            propCondition1.setProperty(attributeName);
            propCondition1.setOperator(PropertyComparisonOperator.EQ);
            propCondition1.setValue(ppvGood);
            propertyConditions1.add(propCondition1);

            PropertyCondition propCondition2 = new PropertyCondition();
            propCondition2.setProperty(attributeName);
            propCondition2.setOperator(PropertyComparisonOperator.NEQ);
            propCondition2.setValue(ppvBad);
            propertyConditions1.add(propCondition2);

            PropertyCondition propCondition3 = new PropertyCondition();
            propCondition3.setNestedConditions(searchProperties2);
            propertyConditions1.add(propCondition3);

            searchProperties1.setConditions(propertyConditions1);
            searchProperties1.setMatchCriteria(ALL);

            /*
             * searchProperties1 should now be the full query as illustrated above.
             */


            /*
             * Formulate the expected result - all entities with the value should match
             */
            List<String> entitiesWithValue = propertyValueMap.get(attributeName).get(value);
            List<String> expectedGUIDs = entitiesWithValue;
            int expectedEntityCount = expectedGUIDs.size();
            // In the case where the instances were created, expected may exceed pageSize.

            /*
             * Search....
             */

            List<EntityDetail> result;

            Map<String,String> parameters = getParameters(entityDef.getGUID(), searchProperties1.toString());

            long elapsedTime;
            try
            {

                long start = System.currentTimeMillis();
                result = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                         entityDef.getGUID(),
                                                         null,  // no subtype GUID filtering
                                                         searchProperties1,
                                                         0,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         pageSize);
                elapsedTime = System.currentTimeMillis() - start;
            }
            catch (FunctionNotSupportedException exc)
            {

                /*
                 * Because the above test only exercises one optional function (findEntities using SearchProperties)
                 * we can assert that it is that function that is not supported.
                 */

                super.addNotSupportedAssertion(assertion101FE,
                                               assertionMsg_101FE + exc.getMessage(),
                                               RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

                return;

            }
            catch (RepositoryTimeoutException exc)
            {

                /*
                 * Such a query may simply timeout, in which case we do not have enough information
                 * to know whether this optional function is supported or not.
                 */
                super.addDiscoveredProperty("query timeouts",
                                            true,
                                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
                return;

            }
            catch(Exception exc)
            {
                /*
                 * We are not expecting any exceptions from this method call. Log and fail the test.
                 */

                String methodName = "findEntities";
                String operationDescription = "find entities of type " + entityDef.getName();
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception( msg , exc );

            }

            /*
             * The approach to checking results match expectations is as follows:
             * The original discovery request (top of this testcase) returned a set of instances that
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
             * pseudocode as:
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
             * Check that the expected number of entities was returned. This has to consider the effect of the original
             * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
             * But if the limit was hit (on the original search) then there may be additional instances in the repository
             * that were not seen on the original search; the expected result was computed from only those instance that WERE seen,
             * so the expectation may be a subset of the actual. If we hit page size there may be additional instances that were
             * not included in the initial set, due to the initial set being limited by pageSize; the narrower search may
             * pull in additional entities that were not discovered previously.
             *
             */


            /*
             * We need to check that we got (at least) the expected number of results - which could include zero.
             */
            int resultCount = result == null ? 0 : result.size();
            /*
             * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
             * In addition, the result size should be no more than a page.
             */
            boolean unlimited_case = !pageLimited && resultCount == expectedEntityCount;
            /*
             * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
             * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
             * So in that latter case we need to accept Min().
             */
            boolean limited_large_case = pageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
            boolean limited_small_case = pageLimited && expectedEntityCount <  pageSize && resultCount >= expectedEntityCount;
            boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

            String assertionMessage = MessageFormat.format(assertionMsg_103, resultCount, expectedEntityCount, parameters);
            assertCondition((acceptable_result_size),
                            assertion103,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);


            /*
             * If there were any result, check that all expected entities were returned and (in the pageLimited case) that any
             * additional entities were valid results for the search.
             */
            if (resultCount > 0) {

                List<String> resultGUIDs = new ArrayList<>();
                for (EntityDetail entity : result) {
                    resultGUIDs.add(entity.getGUID());
                }


                /*
                 * Here again, we need to be sensitive to whether there are (or may be) more entities than the page limit.
                 * If the original search hit the limit then we may legitimately receive additional instances in the results
                 * of a narrower search. But not if the original result set was under the page limit.
                 */

                String unexpectedResult = "0";

                if (!pageLimited)
                {

                    if (!resultGUIDs.containsAll(expectedGUIDs))
                        unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;

                }
                else
                { // pageLimited, so need to allow for and verify hitherto unseen instances

                    for (EntityDetail entity : result)
                    {

                        if (!(expectedGUIDs.contains(entity.getGUID())))
                        {
                            /*
                             * This was an extra entity that we either did not expect or that we have not seen previously.
                             * Check it is a valid result.
                             */
                            InstanceProperties entityProperties = entity.getProperties();
                            if (entityProperties != null)
                            {
                                InstancePropertyValue ipValue = entityProperties.getPropertyValue(attributeName);
                                if (ipValue != null)
                                {
                                    InstancePropertyCategory ipCategory = ipValue.getInstancePropertyCategory();
                                    if (ipCategory == InstancePropertyCategory.PRIMITIVE)
                                    {
                                        Object primitiveValue = ipValue.valueAsObject();

                                        /*
                                         * Check for inequality and fail the match if unequal.
                                         * This is because, even for strings, we used an exact match literalised property value
                                         * and match criteria was ALL - so an entity with an unequal property is not a valid result.
                                         */
                                        if (!primitiveValue.equals(value))
                                        {
                                            unexpectedResult = "('" + primitiveValue.toString() + "' for guid=" + entity.getGUID() + ")";
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                assertionMessage = MessageFormat.format(assertionMsg_104, unexpectedResult, parameters.toString());
                assertCondition(unexpectedResult.equals("0"),
                                assertion104,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
            }

        }
    }

    /*
     * performNonPropertySearchTests
     *
     * This test calls findEntitiesByProperty and findEntities with no match properties or match criteria.
     * Although this is similar to the discovery test at the start of this testcase, it differs by passing
     * a null for matchProperties or searchProperties, rather than an empty object.
     * This is what happens under the covers when someone calls getEntitiesByType() for example.
     *
     * The expected return set should be ANY of the instances created for this type.
     */
    private void performNonPropertySearchTests() throws Exception
    {
        performFindEntitiesByPropertyWithNullParams();

        performFindEntitiesWithNullParams();
    }

    /*
     * performFindEntitiesByPropertyWithNullParams
     *
     * This test calls findEntitiesByProperty and findEntities with no match properties or match criteria.
     * Although this is similar to the discovery test at the start of this testcase, it differs by passing
     * a null for matchProperties or searchProperties, rather than an empty object.
     * This is what happens under the covers when someone calls getEntitiesByType() for example.
     *
     * The expected return set should be ANY of the instances created for this type.
     */
    private void performFindEntitiesByPropertyWithNullParams() throws Exception
    {

        /*
         * Use knownInstances and knownInstancesGUIDs to validate results
         */

        int expectedEntityCount = knownInstances.size();
        List<String> expectedGUIDs = knownInstancesGUIDs;

        /*
         * Search....
         */

        List<EntityDetail> result;

        Map<String, String> parameters = getParameters(entityDef.getGUID(), null, null);

        long elapsedTime;
        try
        {

            long start = System.currentTimeMillis();
            result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                               entityDef.getGUID(),
                                                               null,
                                                               null,
                                                               0,
                                                               null,
                                                               null,
                                                               null,
                                                               null,
                                                               null,
                                                               pageSize);
            elapsedTime = System.currentTimeMillis() - start;
        }
        catch (FunctionNotSupportedException exc)
        {

            /*
             * Because the above test only exercises one optional function (findEntities using SearchProperties)
             * we can assert that it is that function that is not supported.
             */

            super.addNotSupportedAssertion(assertion101FEBP,
                                           assertionMsg_101FEBP + exc.getMessage(),
                                           RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());

            return;

        }
        catch (RepositoryTimeoutException exc)
        {

            /*
             * Such a query may simply timeout, in which case we do not have enough information
             * to know whether this optional function is supported or not.
             */
            super.addDiscoveredProperty("query timeouts",
                                        true,
                                        RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
            return;

        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "findEntitiesByProperty";
            String operationDescription = "find entities of type " + entityDef.getName();
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        /*
         * The approach to checking results match expectations is as follows:
         * The original discovery request (top of this testcase) returned a set of instances that
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
         * This case (of complete knowledge) can be summarised in pseudocode as:
         *   if original result size < page size then:
         *     result size < expected size => fail
         *     result size == expected size => if search contains all expected => pass
         *                                     else search !contains all expected => fail
         *      search size > expect size => fail (should not get more than the expectation)
         *
         *
         * In contrast, if the original result set returned a full page size then the testcase needs to
         * exercise a looser result matching policy. This case (of incomplete knowledge) can be summarised in
         * pseudocode as:
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
         * Check that the expected number of entities was returned. This has to consider the effect of the original
         * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
         * But if the limit was hit (on the original search) then there may be additional instances in the repository
         * that were not seen on the original search; the expected result was computed from only those instance that WERE seen,
         * so the expectation may be a subset of the actual. If we hit page size there may be additional instances that were
         * not included in the initial set, due to the initial set being limited by pageSize; the narrower search may
         * pull in additional entities that were not discovered previously.
         *
         */


        /*
         * We need to check that we got (at least) the expected number of results - which could include zero.
         */
        int resultCount = result == null ? 0 : result.size();
        /*
         * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
         * In addition, the result size should be no more than a page.
         */
        boolean unlimited_case = !pageLimited && resultCount == expectedEntityCount;
        /*
         * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
         * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
         * So in that latter case we need to accept Min().
         */
        boolean limited_large_case = pageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
        boolean limited_small_case = pageLimited && expectedEntityCount < pageSize && resultCount >= expectedEntityCount;
        boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

        String assertionMessage = MessageFormat.format(assertionMsg_105, resultCount, expectedEntityCount, parameters);
        assertCondition((acceptable_result_size),
                        assertion105,
                        assertionMessage,
                        RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId(),
                        "findEntitiesByProperty",
                        elapsedTime);


        /*
         * If there were any result, check that all expected entities were returned and (in the pageLimited case) that any
         * additional entities were valid results for the search.
         */
        if (resultCount > 0)
        {

            List<String> resultGUIDs = new ArrayList<>();
            for (EntityDetail entity : result)
            {
                resultGUIDs.add(entity.getGUID());
            }


            /*
             * Here again, we need to be sensitive to whether there are (or may be) more entities than the page limit.
             * If the original search hit the limit then we may legitimately receive additional instances in the results
             * of a narrower search. But not if the original result set was under the page limit.
             */

            String unexpectedResult = "0";

            if (!pageLimited)
            {
                if (!resultGUIDs.containsAll(expectedGUIDs))
                {
                    unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;
                }
            }
            else
            { // pageLimited, so need to allow for and verify hitherto unseen instances

                for (EntityDetail entity : result)
                {

                    if (!(expectedGUIDs.contains(entity.getGUID())))
                    {
                        /*
                         * This was an extra entity that we either did not expect or that we have not seen previously.
                         * Check it is a valid result. This really just boils down to checking the type
                         */
                        InstanceType instanceType = entity.getType();
                        if (instanceType != null)
                        {
                            String instanceTypeName = instanceType.getTypeDefName();
                            if (instanceTypeName != null)
                            {
                                cohortRepositoryConnector = workPad.getTutRepositoryConnector();
                                OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
                                if (!repositoryHelper.isTypeOf(workPad.getTutServerName(), instanceTypeName, entityDef.getName()))
                                {
                                    unexpectedResult = "(Inappropriate type '" + instanceTypeName+ "' returned for type filter '" + entityDef.getName() + "')";
                                }
                            }
                        }
                    }
                }
            }

            assertionMessage = MessageFormat.format(assertionMsg_106, unexpectedResult, parameters.toString());
            assertCondition(unexpectedResult.equals("0"),
                            assertion106,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_PROPERTY_SEARCH.getRequirementId());
        }

    }



    private void performFindEntitiesWithNullParams() throws Exception
    {

        /*
         * Use knownInstances and knownInstancesGUIDs to validate results
         */

        int expectedEntityCount = knownInstances.size();
        List<String> expectedGUIDs = knownInstancesGUIDs;

        /*
         * Search....
         */

        List<EntityDetail> result;

        Map<String, String> parameters = getParameters(entityDef.getGUID(), null, null);

        long elapsedTime;
        try
        {

            long start = System.currentTimeMillis();
            result = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                     entityDef.getGUID(),
                                                     null,  // no subtype GUID filtering
                                                     null,
                                                     0,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     pageSize);
            elapsedTime = System.currentTimeMillis() - start;
        }
        catch (FunctionNotSupportedException exc)
        {

            /*
             * Because the above test only exercises one optional function (findEntities using SearchProperties)
             * we can assert that it is that function that is not supported.
             */

            super.addNotSupportedAssertion(assertion101FE,
                                           assertionMsg_101FE + exc.getMessage(),
                                           RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

            return;

        }
        catch (RepositoryTimeoutException exc)
        {

            /*
             * Such a query may simply timeout, in which case we do not have enough information
             * to know whether this optional function is supported or not.
             */
            super.addDiscoveredProperty("query timeouts",
                                        true,
                                        RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                        RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
            return;

        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "findEntities";
            String operationDescription = "find entities of type " + entityDef.getName();
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        /*
         * The approach to checking results match expectations is as follows:
         * The original discovery request (top of this testcase) returned a set of instances that
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
         * Check that the expected number of entities was returned. This has to consider the effect of the original
         * search hitting the page limit. If the limit was not hit then the result size should match the expected size exactly.
         * But if the limit was hit (on the original search) then there may be additional instances in the repository
         * that were not seen on the original search; the expected result was computed from only those instance that WERE seen,
         * so the expectation may be a subset of the actual. If we hit page size there may be additional instances that were
         * not included in the initial set, due to the initial set being limited by pageSize; the narrower search may
         * pull in additional entities that were not discovered previously.
         *
         */


        /*
         * We need to check that we got (at least) the expected number of results - which could include zero.
         */
        int resultCount = result == null ? 0 : result.size();
        /*
         * If the original discovery query was not pageLimited then we should have been able to exactly predict the expected result.
         * In addition, the result size should be no more than a page.
         */
        boolean unlimited_case = !pageLimited && resultCount == expectedEntityCount;
        /*
         * If the original discovery query was pageLimited then we have to tolerate hitherto unseen instances in the results.
         * If the most recent query hit the pageSize limit then we have to accept that we got less than we might have 'expected'.
         * So in that latter case we need to accept Min().
         */
        boolean limited_large_case = pageLimited && expectedEntityCount >= pageSize && resultCount == pageSize;
        boolean limited_small_case = pageLimited && expectedEntityCount < pageSize && resultCount >= expectedEntityCount;
        boolean acceptable_result_size = unlimited_case || limited_large_case || limited_small_case;

        String assertionMessage = MessageFormat.format(assertionMsg_107, resultCount, expectedEntityCount, parameters);
        assertCondition((acceptable_result_size),
                        assertion107,
                        assertionMessage,
                        RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                        "findEntities",
                        elapsedTime);


        /*
         * If there were any result, check that all expected entities were returned and (in the pageLimited case) that any
         * additional entities were valid results for the search.
         */
        if (resultCount > 0)
        {
            List<String> resultGUIDs = new ArrayList<>();
            for (EntityDetail entity : result)
            {
                resultGUIDs.add(entity.getGUID());
            }

            /*
             * Here again, we need to be sensitive to whether there are (or may be) more entities than the page limit.
             * If the original search hit the limit then we may legitimately receive additional instances in the results
             * of a narrower search. But not if the original result set was under the page limit.
             */

            String unexpectedResult = "0";

            if (!pageLimited)
            {
                if (!resultGUIDs.containsAll(expectedGUIDs))
                {
                    unexpectedResult = RESULTS_MISSING_EXPECTED_GUIDS;
                }
            }
            else
            { // pageLimited, so need to allow for and verify hitherto unseen instances

                for (EntityDetail entity : result)
                {

                    if (!(expectedGUIDs.contains(entity.getGUID())))
                    {
                        /*
                         * This was an extra entity that we either did not expect or that we have not seen previously.
                         * Check it is a valid result. This really just boils doen to checking the type
                         */
                        InstanceType instanceType = entity.getType();
                        if (instanceType != null)
                        {
                            String instanceTypeName = instanceType.getTypeDefName();
                            if (instanceTypeName != null)
                            {
                                cohortRepositoryConnector = workPad.getTutRepositoryConnector();
                                OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
                                if (!repositoryHelper.isTypeOf(workPad.getTutServerName(), instanceTypeName, entityDef.getName()))
                                {
                                    unexpectedResult = "(Inappropriate type '" + instanceTypeName+ "' returned for type filter '" + entityDef.getName() + "')";
                                }
                            }
                        }
                    }
                }
            }

            assertionMessage = MessageFormat.format(assertionMsg_108, unexpectedResult, parameters.toString());
            assertCondition(unexpectedResult.equals("0"),
                            assertion108,
                            assertionMessage,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
        }

    }


    /**
     * Create a primitive property value for the requested property that has value different to
     * anything the seed generator would have created. This is to create a value that should fail
     * any comparisons.
     *
     * @param primDefCat   the primitive category of the property
     * @param primValue    the value of the actual property
     * @return PrimitiveTypePropertyValue object
     */
    private PrimitivePropertyValue getWrongPrimitivePropertyValue(PrimitiveDefCategory  primDefCat,
                                                                  Object                primValue)
    {

        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(primDefCat);


        switch (primDefCat)
        {
            case OM_PRIMITIVE_TYPE_STRING:
                    propertyValue.setPrimitiveValue("InvalidValue");
                break;
            case OM_PRIMITIVE_TYPE_DATE:
                Date date = new Date();                        // Date and Time now - should be different to actual
                Long timestamp = date.getTime();
                propertyValue.setPrimitiveValue(timestamp);    // Dates are stored as Long values
                break;
            case OM_PRIMITIVE_TYPE_INT:
                propertyValue.setPrimitiveValue((int)primValue + 13);
                break;
            case OM_PRIMITIVE_TYPE_BOOLEAN:
                propertyValue.setPrimitiveValue( ! ((boolean)primValue) );
                break;
            case OM_PRIMITIVE_TYPE_SHORT:
                propertyValue.setPrimitiveValue((short)primValue + 13);
                break;
            case OM_PRIMITIVE_TYPE_BYTE:
                propertyValue.setPrimitiveValue((byte)primValue + 13);
                break;
            case OM_PRIMITIVE_TYPE_CHAR:
                propertyValue.setPrimitiveValue((char)primValue + 13);
                break;
            case OM_PRIMITIVE_TYPE_LONG:
                propertyValue.setPrimitiveValue((long)primValue + 13);
                break;
            case OM_PRIMITIVE_TYPE_FLOAT:
                propertyValue.setPrimitiveValue((float)primValue + 13);
                break;
            case OM_PRIMITIVE_TYPE_DOUBLE:
            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                propertyValue.setPrimitiveValue((double)primValue + 13);
                break;
            case OM_PRIMITIVE_TYPE_UNKNOWN:
                break;
        }

        return propertyValue;
    }
}