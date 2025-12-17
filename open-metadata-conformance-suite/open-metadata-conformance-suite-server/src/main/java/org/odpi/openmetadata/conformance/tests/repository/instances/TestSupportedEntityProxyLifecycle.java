/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.ffdc.exception.AssertionFailureException;
import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Test that all defined entities can be stored and retrieved as proxies, that the proxies support all (and only) valid operations, and that they can be deleted/purged.
 */
public class TestSupportedEntityProxyLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-entity-proxy-lifecycle";
    private static final String testCaseName = "Repository entity proxy lifecycle test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " repository does not support entity types required for ends of relationship of type ";

    private static final String assertion2     = testCaseId + "-2";
    private static final String assertionMsg2  = " repository does not support creation of entity proxies for type ";

    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " repository supports retrieval of entity proxy as an EntitySummary for type ";

    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " repository disallows retrieval of entity proxy as an EntityDetail for type ";

    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " repository disallows isEntityKnown for entity proxy for type ";

    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " repository does not support creation of relationship instance for type ";

    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " repository supports soft delete of relationship instance for type ";

    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " repository supports delete of entity proxy of type ";


    private static final String assertion10     = testCaseId + "-10";
    private static final String assertionMsg10  = " entity proxy properties cannot be updated ";

    private static final String assertion11     = testCaseId + "-11";
    private static final String assertionMsg11  = " entity proxy type cannot be updated ";

    private static final String assertion12     = testCaseId + "-12";
    private static final String assertionMsg12  = " entity proxy identity cannot be updated ";


    private final List<EntityDetail>             createdEntitiesCTS        = new ArrayList<>();  // these are all master instances
    private final List<EntityDetail>             createdEntitiesTUT        = new ArrayList<>();  // these are all master instances
    private final List<EntityDetail>             createdEntityRefCopiesTUT = new ArrayList<>();  // these are all ref copies
    private final List<EntityProxy>              createdEntityProxiesTUT   = new ArrayList<>();  // these are all proxy instances
    private final List<Relationship>             createdRelationshipsTUT   = new ArrayList<>();  // these are all master instances



    /*
     * This testcase:
     *
     * Accepts a RelationshipDef and the set of EntityDefs supported by the TUT.
     * This allows the creation of a relationship and a pair of entities (one for each end of the relationship).
     *
     * 1. Creates an entity E1 in the CTS server
     * 1a. The master instance of E1 is created in the CTS server's repository
     * 1b. A NEW_ENTITY_EVENT is flowed to the TUT for E1
     * 1c. The TUT should create a reference copy of E1 - we are not interested in the ref copy, but we need to be aware of it and purge it.
     *
     * 2. The E1ref copy is purged from the TUT
     *
     * 3. A proxy of E1 is created at the TUT
     * 3a. The entity E1 is retrieved from the CTS
     * 3b. The entity instance is transformed (in memory) into a proxy
     * 3c. The proxy E1' is installed in the TUT by a REST call to the TUT's metadataCollection addEntityProxy method
     * 3d. The TUT creates an E1' proxy.
     *
     * 4. All the above is repeated for a second entity. This avoids requiring that the TUT supports creation of locally mastered entities or reference copies.
     * It would be sufficient to support the ENTITY_PROXIES profile if the TUT only supports the creation of proxies of entities mastered elsewhere (in this test at the CTS)
     *
     * 5. Perform retrieval tests on the E1' proxy:
     * 5a. The proxy should be able to be retrieved using getEntitySummary()
     * 5b. The proxy should NOT be able to be retrieved using getEntityDetail()
     * 5c. The proxy should NOT be able to be retrieved using isEntityKnown()
     *
     *
     * 6. Create a relationship between E1' (proxy) and E2' (proxy)
     * 6a. Create the relationship at the TUT and verify it succeeds
     * 6b. Delete and purge the relationship
     *
     * 7. Delete E1 (master)
     * 7a. The master instance of E1 is deleted at the CTS
     * 7b. This will flow a DELETED_ENTITY event to the TUT, which should (soft) delete the E1' proxy - this tests that proxies are handled effectively like ref copies
     * 7c. The master instance of E1 is purged at the CTS
     * 7d. This will flow a PYRGED_ENTITY event to the TUT, which should purge the E1' entity proxy - this tests that proxies are handled effectively like ref copies
     * 7e. Verify that the E1' proxy has been removed from the TUT
     *
     * 8. Repeat step 7 for E2.
     */





    private final RepositoryConformanceWorkPad   workPad;
    private final Map<String, EntityDef>         entityDefs;
    private final RelationshipDef                relationshipDef;
    private final String                         testTypeName;


    /*
     * A propagation timeout is used to limit how long the testcase will wait for
     * the propagation of an OMRS instance event and consequent processing at the TUT (or CTS).
     * Each time the testcase waits it does so in a 100ms polling loop, to minimise overall delay.
     * The wait loops will wait for pollCount iterations of pollPeriod, so a pollCount of x10
     * results in a 1000ms (1s) timeout.
     *
     */
    private Integer           pollCount   = 100;
    private Integer           pollPeriod  = 100;   // milliseconds



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDefs types of valid entities
     * @param relationshipDef type of relationship to test
     */
    public TestSupportedEntityProxyLifecycle(RepositoryConformanceWorkPad workPad,
                                             Map<String, EntityDef>       entityDefs,
                                             RelationshipDef              relationshipDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getProfileId(),
              RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getRequirementId());

        this.workPad              = workPad;
        this.relationshipDef      = relationshipDef;
        this.entityDefs           = entityDefs;


        /*
         * Although this is test case is concerned with proxies - it is desirable to have a relationship to test with.
         * The testcase is named after the relationship type.
         */
        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
                                                    testCaseId,
                                                    testCaseName);

        /*
         * Enforce minimum pollPeriod and pollCount.
         */
        this.pollPeriod = Math.max(this.pollPeriod, 100);
        this.pollCount  = Math.max(this.pollCount, 1);

    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {

        /*
         * This test case does not perform entity or relationship type validation - this is tested in other test cases.
         */

        /*
         * Get access to the repositoryHelper - we'll need it in a while....
         */
        OMRSRepositoryHelper repositoryHelper = null;
        if (workPad != null) {
            OMRSRepositoryConnector cohortRepositoryConnector = workPad.getTutRepositoryConnector();
            repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
            if (repositoryHelper == null) {
                /*
                 * Critical failure - give up
                 */
                return;
            }
        }

        /*
         * Get the TUT metadataCollection
         */

        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        /*
         * This test needs a connector to the local repository - i.e. the in-memory repository running locally to the CTS server.
         */
        OMRSMetadataCollection ctsMetadataCollection = repositoryConformanceWorkPad.getLocalRepositoryConnector().getMetadataCollection();

        String ctsMetadataCollectionName = repositoryConformanceWorkPad.getLocalRepositoryConnector().getMetadataCollectionName();
        if (ctsMetadataCollectionName == null) {
            ctsMetadataCollectionName = "dummyMetadataCollectionName";
        }


        /*
         * Find all the entity types that we could use at the ends of the requested relationship type
         *
         * In this testcase the repository is believed to support the relationship type defined by
         * relationshipDef - but may not support all the entity inheritance hierarchy - it may only
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
        if (this.workPad != null && this.workPad.getEntitySubTypes(end1DefName) != null) {
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
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }

        /*
         * It is not practical to iterate over all combinations of feasible (supported) end types as it takes too long to run.
         * For now, this test verifies relationship operation over a limited set of end types. The limitation is extreme in
         * that it ONLY takes the first available type for each end. This is undesirable for two reasons - one is that it
         * provides less test coverage; the other is that the types chosen depend on the order in the lists and this could
         * vary, making results non-repeatable. For now though, it seems these limitations are necessary.
         *
         * A full permutation across end types would use the following nested loops...
         *  for (String end1TypeName : end1SupportedTypeNames) {
         *     for (String end2TypeName : end2SupportedTypeNames) {
         *          test logic as below...
         *      }
         *  }
         */


        String end1TypeName = end1SupportedTypeNames.get(0);
        String end2TypeName = end2SupportedTypeNames.get(0);



        /*
         * Create E1 at the CTS
         */


        EntityDef end1Type = entityDefs.get(end1TypeName);


        /*
         * Generate property values for all the type's defined properties, including inherited properties
         * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
         * thereby getting into the connector-logic beyond the property validation. It also creates an
         * entity that is logically complete - versus an instance with just the locally-defined properties.
         */

        EntityDetail entity1 = ctsMetadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                               end1Type.getGUID(),
                                                               super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), end1Type),
                                                               null,
                                                               null);

        createdEntitiesCTS.add(entity1);

        /*
         * This test does not verify the content of the entity - that is tested in the entity-lifecycle tests
         */



        /*
         * A reference copy of the entity should be created in the TUT
         */

        EntityDetail entity1Ref = null;



        /*
         * Retrieve the ref copy from the TUT - if it does not exist, assert that ref copies are not a discovered property
         * Have to be prepared to wait until event has propagated and TUT has created a reference copy of the entity.
         */
        try
        {
            Integer remainingCount = this.pollCount;
            while (entity1Ref == null && remainingCount > 0)
            {

                entity1Ref = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), entity1.getGUID());
                Thread.sleep(this.pollPeriod);
                remainingCount--;

            }
            if (entity1Ref == null && remainingCount == 0)
            {
                workPad.getAuditLog()
                        .logMessage(assertion2,
                                    ConformanceSuiteAuditCode.POLLING_OVERFLOW.getMessageDefinition(pollCount.toString(), pollPeriod.toString()));
            }
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "isEntityKnown";
            String operationDescription = "retrieve an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", entity1.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        createdEntityRefCopiesTUT.add(entity1Ref);

        /*
         * This test needs to eliminate the reference copy - so that a proxy of E1 can be created at the TUT.
         */

        if (entity1Ref != null)
        {

            /*
             * If we retrieved the reference copy of the entity - we must purge it.
             *
             * If there is no reference copy it may be because the TUT does not support ref copies - that's OK, we can continue with the test of proxy support
             */

            try
            {

                metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(), entity1Ref);

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "purgeEntityReferenceCopy";
                String operationDescription = "purge a reference copy of an entity of type " + end1Type.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", entity1Ref.getType().getTypeDefGUID());
                parameters.put("typeDefName", entity1Ref.getType().getTypeDefName());
                parameters.put("deletedEntityGUID", entity1Ref.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);

            }

        }


        /*
         * Retrieve the master instance of entity1 from the CTS and fashion a proxy from it.
         */

        EntityDetail retrievedEntity1 = ctsMetadataCollection.getEntityDetail(workPad.getLocalServerUserId(), entity1.getGUID());
        EntityProxy entity1Proxy;

        if (retrievedEntity1 != null && repositoryHelper != null) {
            entity1Proxy = repositoryHelper.getNewEntityProxy(ctsMetadataCollectionName, retrievedEntity1);
        } else {
            /*
             * If we cannot retrieve the entity (detail) from the CTS repository then we are in deep trouble.
             * The CTS is (most probably) using the in-memory repository which absolutely should support retrieval.
             * If this is not working do not pursue the test any further. By returning we will report UNKNOWN_STATUS
             * for the proxies profile for the repository actually being tested - which is a sensible outcome.
             */

            return;
        }



        /*
         * Ask the TUT to add the entity proxy to its repository
         */

        long elapsedTime;
        try
        {
            long start = System.currentTimeMillis();
            metadataCollection.addEntityProxy(workPad.getLocalServerUserId(), entity1Proxy);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion2,
                            assertionMsg2 + end1TypeName,
                            RepositoryConformanceProfileRequirement.STORE_ENTITY_PROXIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.STORE_ENTITY_PROXIES.getRequirementId(),
                            "addEntityProxy",
                            elapsedTime);

            createdEntityProxiesTUT.add(entity1Proxy);

        }
        catch (FunctionNotSupportedException exception)
        {

            /*
             * Report that the TUT does not support the creation of proxies - and do not pursue the test any further
             */
            super.addNotSupportedAssertion(assertion2,
                                           assertionMsg2 + end1TypeName,
                                           RepositoryConformanceProfileRequirement.STORE_ENTITY_PROXIES.getProfileId(),
                                           RepositoryConformanceProfileRequirement.STORE_ENTITY_PROXIES.getRequirementId());

            return;
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "addEntityProxy";
            String operationDescription = "add a proxy instance for an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityProxy", entity1Proxy.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        /*
         * Create E2 at the CTS
         */

        EntityDef end2Type = entityDefs.get(end2TypeName);

        /*
         * Generate property values for all the type's defined properties, including inherited properties
         * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
         * thereby getting into the connector-logic beyond the property validation. It also creates an
         * entity that is logically complete - versus an instance with just the locally-defined properties.
         */

        EntityDetail entity2 = ctsMetadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                               end2Type.getGUID(),
                                                               super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), end2Type),
                                                               null,
                                                               null);
        createdEntitiesCTS.add(entity2);

        /*
         * This test does not verify the content of the entity - that is tested in the entity-lifecycle tests
         */



        /*
         * A reference copy of the entity should be created in the TUT
         */

        EntityDetail entity2Ref = null;



        /*
         * Retrieve the ref copy from the TUT - if it does not exist, assert that ref copies are not a discovered property
         * Have to be prepared to wait until event has propagated and TUT has created a reference copy of the entity.
         */
        try
        {
            Integer remainingCount = this.pollCount;
            while (entity2Ref == null && remainingCount > 0)
            {
                entity2Ref = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), entity2.getGUID());
                Thread.sleep(this.pollPeriod);
                remainingCount--;

            }

            if (entity2Ref == null && remainingCount == 0)
            {
                workPad.getAuditLog()
                        .logMessage(assertion2,
                                   ConformanceSuiteAuditCode.POLLING_OVERFLOW.getMessageDefinition(pollCount.toString(), pollPeriod.toString()));
            }
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */
            String methodName = "isEntityKnown";
            String operationDescription = "retrieve an entity of type " + end2Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", entity2.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        createdEntityRefCopiesTUT.add(entity2Ref);

        /*
         * This test needs to eliminate the reference copy - so that a proxy of E2 can be created at the TUT.
         */

        if (entity2Ref != null)
        {

            /*
             * If we retrieved the reference copy of the entity - we must purge it.
             *
             * If there is no reference copy it may be because the TUT does not support ref copies - that's OK, we can continue with the test of proxy support
             */

            try
            {
                metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(), entity2Ref);
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "purgeEntityReferenceCopy";
                String operationDescription = "purge a reference copy an entity of type " + end2Type.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", entity2Ref.getType().getTypeDefGUID());
                parameters.put("typeDefName", entity2Ref.getType().getTypeDefName());
                parameters.put("deletedEntityGUID", entity2Ref.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

        }



        /*
         * Retrieve the master instance of entity2 from the CTS and fashion a proxy from it.
         */

        EntityDetail retrievedEntity2 = ctsMetadataCollection.getEntityDetail(workPad.getLocalServerUserId(), entity2.getGUID());
        EntityProxy entity2Proxy;

        if (retrievedEntity2 != null) {

            entity2Proxy = repositoryHelper.getNewEntityProxy(ctsMetadataCollectionName, retrievedEntity2);

        } else {
            /*
             * If we cannot retrieve the entity (detail) from the CTS repository then we are in deep trouble.
             * The CTS is (most probably) using the in-memory repository which absolutely should support retrieval.
             * If this is not working do not pursue the test any further. By returning we will report UNKNOWN_STATUS
             * for the proxies profile for the repository actually being tested - which is a sensible outcome.
             */

            return;
        }



        /*
         * Ask the TUT to add the E2' entity proxy to its repository
         */

        try {
            long start = System.currentTimeMillis();
            metadataCollection.addEntityProxy(workPad.getLocalServerUserId(), entity2Proxy);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion2,
                            assertionMsg2 + end2TypeName,
                            RepositoryConformanceProfileRequirement.STORE_ENTITY_PROXIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.STORE_ENTITY_PROXIES.getRequirementId(),
                            "addEntityProxy",
                            elapsedTime);

            createdEntityProxiesTUT.add(entity2Proxy);

        } catch (FunctionNotSupportedException excpetion) {

            /*
             * Report that the TUT does not support the creation of proxies - and do not pursue the test any further
             */
            super.addNotSupportedAssertion(assertion2,
                                           assertionMsg2 + end2TypeName,
                                           RepositoryConformanceProfileRequirement.STORE_ENTITY_PROXIES.getProfileId(),
                                           RepositoryConformanceProfileRequirement.STORE_ENTITY_PROXIES.getRequirementId());

            return;

        } catch (Exception exc) {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "addEntityProxy";
            String operationDescription = "add a proxy instance for an entity of type " + end2Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityProxy", entity2Proxy.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }



        /*
         * Perform retrieval tests on the E1 proxy (only - these tests are not repeated for E2 proxy).
         */

        /*
         * It SHOULD be possible to retrieve the proxy as an EntitySummary
         */

        EntitySummary retrievedEntity1Summary = null;
        try
        {

            long start = System.currentTimeMillis();
            retrievedEntity1Summary = metadataCollection.getEntitySummary(workPad.getLocalServerUserId(), entity1.getGUID());
            elapsedTime = System.currentTimeMillis() - start;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */
            String methodName = "getEntitySummary";
            String operationDescription = "retrieve an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", entity1.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        assertCondition((retrievedEntity1Summary != null),
                        assertion3,
                        assertionMsg3 + end1TypeName,
                        RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getProfileId(),
                        RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getRequirementId(),
                        "getEntitySummary",
                        elapsedTime);


        /*
         * It SHOULD NOT be possible to retrieve the proxy as an EntityDetail - this should throw EntityProxyOnlyException
         */

        long start = System.currentTimeMillis();
        try
        {
            EntityDetail retrievedEntity1Detail = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), entity1.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((false),
                            assertion4,
                            assertionMsg4 + end1TypeName,
                            RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getRequirementId(),
                            "getEntityDetail-negative",
                            elapsedTime);
        }
        catch (EntityProxyOnlyException exception)
        {
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion4,
                            assertionMsg4 + end1TypeName,
                            RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getRequirementId(),
                            "getEntityDetail-negative",
                            elapsedTime);
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "getEntityDetail";
            String operationDescription = "retrieve an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", entity1.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);

        }

        /*
         * It SHOULD NOT be possible to retrieve the proxy using isEntityKnown - this should return null
         */

        try
        {
            start = System.currentTimeMillis();
            EntityDetail retrievedEntity1Detail = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), entity1.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((retrievedEntity1Detail == null),
                            assertion5,
                            assertionMsg5 + end1TypeName,
                            RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.RETRIEVE_ENTITY_PROXIES.getRequirementId(),
                            "isEntityKnown-negative",
                            elapsedTime);

        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "isEntityKnown";
            String operationDescription = "retrieve an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", entity1.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        /*
         * Verify that operations that should not be valid on a proxy are correctly disallowed.
         * These tests are all performed on the E1 proxy.
         *
         * It SHOULD NOT be possible to perform any of the following operations.
         */

        /*
         * Attempt to modify one or more property of the entity proxy. This is illegal so it should fail.
         */


        if ((entity1.getProperties() != null) &&
                (entity1.getProperties().getInstanceProperties() != null) &&
                (!entity1.getProperties().getInstanceProperties().isEmpty())) {
            InstanceProperties minEntityProps = super.getMinPropertiesForInstance(workPad.getLocalServerUserId(), end1Type);

            try
            {
                start = System.currentTimeMillis();
                EntityDetail minPropertiesEntity = metadataCollection.updateEntityProperties(workPad.getLocalServerUserId(), entity1.getGUID(), minEntityProps);
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition((false),
                                assertion10,
                                testTypeName + assertionMsg10,
                                RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getRequirementId(),
                                "updateEntityProperties-negative",
                                elapsedTime);

            }
            catch (InvalidParameterException e)
            {
                /*
                 * We are not expecting the status update to work - it should have thrown an InvalidParameterException
                 */
                elapsedTime = System.currentTimeMillis() - start;
                assertCondition((true),
                                assertion10,
                                testTypeName + assertionMsg10,
                                RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getRequirementId(),
                                "updateEntityProperties-negative",
                                elapsedTime);
            }
            catch (AssertionFailureException exception)
            {
                /*
                 * Re throw this exception, so it is not masked by Exception (below).
                 */
                throw exception;
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "updateEntityProperties";
                String operationDescription = "update the properties of an entity of type " + end1Type.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityGUID", entity1.getGUID());
                parameters.put("properties", minEntityProps.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }
        }


        /*
         * Verify that it is not possible to re-type the entity proxy.
         * This test is performed using the same type as the original - the repository should not get as far as
         * even looking at the type or considering changing it. For simplicity of testcode this test therefore
         * uses the original type.
         * This test is performed against the TUT.
         */

        try
        {
            start = System.currentTimeMillis();
            EntityDetail reTypedEntity = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                                                         entity1.getGUID(),
                                                                         end1Type,
                                                                         end1Type); // see comment above about using original type
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((false),
                            assertion11,
                            testTypeName + assertionMsg11,
                            RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getRequirementId(),
                            "reTypeEntity-negative",
                            elapsedTime);

        }
        catch (InvalidParameterException e)
        {

            /*
             * We are not expecting the type update to work - it should have thrown an InvalidParameterException
             */

            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion11,
                            testTypeName + assertionMsg11,
                            RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getRequirementId(),
                            "reTypeEntity-negative",
                            elapsedTime);
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "reTypeEntity";
            String operationDescription = "retype an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", entity1.getGUID());
            parameters.put("currentTypeDefSummary", end1Type.toString());
            parameters.put("newTypeDefSummary", end1Type.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        /*
         * Verify that it is not possible to re-identify the entity proxy.
         * This test is performed using a different GUID to the original. The actual value should not be looked at
         * by the repository - it should reject the re-identify attempt prior to that.
         * This test is performed against the TUT.
         */

        String newGUID = UUID.randomUUID().toString();

        try
        {

            start = System.currentTimeMillis();
            EntityDetail reIdentifiedEntity = metadataCollection.reIdentifyEntity(workPad.getLocalServerUserId(),
                                                                                  end1Type.getGUID(),
                                                                                  end1Type.getName(),
                                                                                  entity1.getGUID(),
                                                                                  newGUID);
            elapsedTime = System.currentTimeMillis() - start;

            if (reIdentifiedEntity != null)
            {
                createdEntitiesTUT.add(reIdentifiedEntity);
            }

            assertCondition((false),
                            assertion12,
                            testTypeName + assertionMsg12,
                            RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getRequirementId(),
                            "reIdentifyEntity-negative",
                            elapsedTime);

        }
        catch (InvalidParameterException e)
        {
            /*
             * We are not expecting the identity update to work - it should have thrown an InvalidParameterException
             */

            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                            assertion12,
                            testTypeName + assertionMsg12,
                            RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_PROXY_LOCKING.getRequirementId(),
                            "reIdentifyEntity-negative",
                            elapsedTime);
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "reIdentifyEntity";
            String operationDescription = "reidentify an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", end1Type.getGUID());
            parameters.put("entityGUID", entity1.getGUID());
            parameters.put("newEntityGUID", newGUID);
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }


        /*
         * Create a relationship between the proxy and the local entity, entity2
         */

        Relationship newRelationship = null;
        InstanceProperties instanceProps = null;

        try
        {
            instanceProps = super.getPropertiesForInstance(relationshipDef.getPropertiesDefinition());

            start = System.currentTimeMillis();
            newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                 relationshipDef.getGUID(),
                                                                 instanceProps,
                                                                 entity1.getGUID(),
                                                                 entity2.getGUID(),
                                                                 null);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion6,
                            assertionMsg6 + testTypeName,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId(),
                            "addRelationship",
                            elapsedTime);

            createdRelationshipsTUT.add(newRelationship);

        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch (FunctionNotSupportedException exception)
        {

            /*
             * If running against a read-only repository/connector that cannot add
             * a relationship catch FunctionNotSupportedException and skip this part of the test, but continue with deletion side and clean up operations
             *
             * Report the inability to create instances.
             */

            super.addNotSupportedAssertion(assertion6,
                                           assertionMsg6 + testTypeName,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

            /* Worth carrying on with whatever else in the test we can achieve - so don't return here */

        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "addRelationship";
            String operationDescription = "add a relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            parameters.put("end1 entityGUID", entity1.getGUID());
            parameters.put("end2 entityGUID", entity2.getGUID());
            parameters.put("initialProperties", instanceProps != null ? instanceProps.toString() : "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }


        if (newRelationship != null)
        {
            /*
             * We have no further use for the relationship - so delete it and purge it.
             */
            try
            {
                start = System.currentTimeMillis();
                Relationship deletedRelationship = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                                                         newRelationship.getType().getTypeDefGUID(),
                                                                                         newRelationship.getType().getTypeDefName(),
                                                                                         newRelationship.getGUID());
                elapsedTime = System.currentTimeMillis() - start;

                assertCondition(true,
                                assertion7,
                                assertionMsg7 + testTypeName,
                                RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId(),
                                "deleteRelationship",
                                elapsedTime);


            }
            catch (FunctionNotSupportedException exception)
            {

                super.addNotSupportedAssertion(assertion7,
                                               assertionMsg7 + testTypeName,
                                               RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getProfileId(),
                                               RepositoryConformanceProfileRequirement.SOFT_DELETE_INSTANCE.getRequirementId());

                /* Soft delete is optional so carry on */

            }
            catch (AssertionFailureException exception)
            {
                /*
                 * Re throw this exception, so it is not masked by Exception (below).
                 */
                throw exception;
            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */
                String methodName = "deleteRelationship";
                String operationDescription = "delete a relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", newRelationship.getType().getTypeDefGUID());
                parameters.put("typeDefName", newRelationship.getType().getTypeDefName());
                parameters.put("obsoleteRelationshipGUID", newRelationship.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }

            try
            {
                metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                                                     newRelationship.getType().getTypeDefGUID(),
                                                     newRelationship.getType().getTypeDefName(),
                                                     newRelationship.getGUID());

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "purgeRelationship";
                String operationDescription = "purge a relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", newRelationship.getType().getTypeDefGUID());
                parameters.put("typeDefName", newRelationship.getType().getTypeDefName());
                parameters.put("deletedRelationshipGUID", newRelationship.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }
        }

        /*
         * Delete (soft then hard) each of the CTS local entities - these operations are performed on the local (CTS) repo.
         * They should cause an OMRS instance event to flow to the TUT and for each entity proxy to be purged
         */

        /*
         * Delete and purge E1 - this is not to test the operation of the repository with regard to the entity
         * lifecycle - that is tested elsewhere. This is just to trigger clean up of the proxy at the TUT.
         */

        try
        {
            EntityDetail deletedEntity = ctsMetadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                            entity1.getType().getTypeDefGUID(),
                                                                            entity1.getType().getTypeDefName(),
                                                                            entity1.getGUID());
        }
        catch (FunctionNotSupportedException exception)
        {

            /*
             * This would admittedly be surprising as the CTS is most likely using the in-memory repo, but in case
             * it is ever used with a repository that does not support soft deletes, then this is OK - we can NO OP and
             * just proceed to purgeEntity
             */
        }


        /*
         * Purge the master instance of E1
         */

        ctsMetadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                          entity1.getType().getTypeDefGUID(),
                                          entity1.getType().getTypeDefName(),
                                          entity1.getGUID());


        /*
         * Test that the entity proxy has been removed from the TUT repository
         */

        /*
         * Since it may take time to propagate the purge event, retry until the entity is no longer known at the TUT.
         * Note that you should get an EntityNotKnownException from getEntitySummary - it is not like isEntityKnown that returns a null.
         * Retry the whole try catch up to pollCount times.
         */

        try
        {

            EntitySummary survivingEnt1Proxy;
            Integer remainingCount = this.pollCount;
            do
            {
                try
                {
                    survivingEnt1Proxy = metadataCollection.getEntitySummary(workPad.getLocalServerUserId(), entity1.getGUID());
                }
                catch (EntityNotKnownException exception)
                {
                    survivingEnt1Proxy = null;
                }
                Thread.sleep(this.pollPeriod);
                remainingCount--;
            } while (survivingEnt1Proxy != null && remainingCount > 0);

            if (entity1Ref == null && remainingCount == 0)
            {
                workPad.getAuditLog()
                        .logMessage(assertion8,
                                    ConformanceSuiteAuditCode.POLLING_OVERFLOW.getMessageDefinition(pollCount.toString(), pollPeriod.toString()));
            }
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */

            String methodName = "getEntitySummary";
            String operationDescription = "retrieve an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", entity1.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }

        /*
         * By now we have given this enough retries that it is either gone or is deemed to have failed....make one or other assertion
         */

        try
        {
            start = System.currentTimeMillis();
            metadataCollection.getEntitySummary(workPad.getLocalServerUserId(), entity1.getGUID());
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((false),
                    assertion8,
                    assertionMsg8 + end1TypeName,
                    RepositoryConformanceProfileRequirement.ENTITY_PROXY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_PROXY_DELETE.getRequirementId(),
                    "getEntitySummary-negative",
                    elapsedTime);

        }
        catch (EntityNotKnownException exception)
        {
            elapsedTime = System.currentTimeMillis() - start;
            assertCondition((true),
                    assertion8,
                    assertionMsg8 + end1TypeName,
                    RepositoryConformanceProfileRequirement.ENTITY_PROXY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_PROXY_DELETE.getRequirementId(),
                    "getEntitySummary-negative",
                    elapsedTime);
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch (Exception exc)
        {
            /*
             * We are not expecting any exceptions from this method call. Log and fail the test.
             */
            String methodName = "getEntitySummary";
            String operationDescription = "retrieve an entity of type " + end1Type.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("entityGUID", entity1.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception(msg, exc);
        }


        /*
         * Delete and purge E2
         */
        try
        {
            EntityDetail deletedEntity = ctsMetadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                            entity2.getType().getTypeDefGUID(),
                                                                            entity2.getType().getTypeDefName(),
                                                                            entity2.getGUID());

        }
        catch (FunctionNotSupportedException exception)
        {
            /*
             * This would admittedly be surprising as the CTS is most likely using the in-memory repo, but in case
             * it is ever used with a repository that does not support soft deletes, then this is OK - we can NO OP and
             * just proceed to purgeEntity
             */
        }

        /*
         * Purge the master instance of E1
         */
        ctsMetadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                          entity2.getType().getTypeDefGUID(),
                                          entity2.getType().getTypeDefName(),
                                          entity2.getGUID());

        super.setSuccessMessage("Entity proxies can be managed through their lifecycle");
    }


    /**
     * Method to clean any instance created by the test case that has not already been cleaned by the running of the test.
     *
     * @throws Exception something went wrong but there is no particular action to take.
     */
    public void cleanup() throws Exception
    {

        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        /*
         * For this testcase we created master instances at the CTS and TUT and ref copies and proxies at the TUT. Also
         * we created relationships at the TUT - five lists to clean up but you cannot directly delete/purge the proxy - only by
         * removing the master at the CTS.
         *   createdEntitiesCTS
         *   createdEntitiesTUT
         *   createdEntityRefCopiesTUT
         *   createdEntityProxiesTUT    x cannot be cleaned directly via API
         *   createdRelationshipsTUT
         */

        if (createdRelationshipsTUT != null && !createdRelationshipsTUT.isEmpty())
        {

            /*
             * Instances were created - clean them up.
             */

            for (Relationship relationship : createdRelationshipsTUT)
            {
                try
                {
                    metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                          relationship.getType().getTypeDefGUID(),
                                                          relationship.getType().getTypeDefName(),
                                                          relationship.getGUID());
                }
                catch (FunctionNotSupportedException exception)
                {
                    // NO OP - can proceed to purge
                }
                catch (RelationshipNotKnownException exception)
                {
                    // Relationship already cleaned up - nothing more to do here.
                    continue;
                }

                // If relationship is known then (whether delete was supported or not) issue purge
                metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                                                     relationship.getType().getTypeDefGUID(),
                                                     relationship.getType().getTypeDefName(),
                                                     relationship.getGUID());
            }
        }

        if (createdEntitiesCTS != null && !createdEntitiesCTS.isEmpty())
        {
            /*
             * Instances were created - clean them up.
             */
            for (EntityDetail entity : createdEntitiesCTS)
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
                catch (EntityNotKnownException exception)
                {
                    // Entity already cleaned up - nothing more to do here.
                    continue;
                }

                // If entity is known then (whether delete was supported or not) issue purge
                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                               entity.getType().getTypeDefGUID(),
                                               entity.getType().getTypeDefName(),
                                               entity.getGUID());
            }
        }

        if (createdEntitiesTUT != null && !createdEntitiesTUT.isEmpty())
        {

            /*
             * Instances were created - clean them up.
             */

            for (EntityDetail entity : createdEntitiesTUT)
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
                catch (EntityNotKnownException exception)
                {
                    // Entity already cleaned up - nothing more to do here.
                    continue;
                }

                // If entity is known then (whether delete was supported or not) issue purge
                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                               entity.getType().getTypeDefGUID(),
                                               entity.getType().getTypeDefName(),
                                               entity.getGUID());
            }
        }

        if (createdEntityRefCopiesTUT != null && !createdEntityRefCopiesTUT.isEmpty())
        {

            /*
             * Instances were created - clean them up.
             */

            for (EntityDetail entity : createdEntityRefCopiesTUT)
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
                catch (EntityNotKnownException exception)
                {
                    // Entity already cleaned up - nothing more to do here.
                    continue;
                }

                // If entity is known then (whether delete was supported or not) issue purge
                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                               entity.getType().getTypeDefGUID(),
                                               entity.getType().getTypeDefName(),
                                               entity.getGUID());
            }
        }
    }
}