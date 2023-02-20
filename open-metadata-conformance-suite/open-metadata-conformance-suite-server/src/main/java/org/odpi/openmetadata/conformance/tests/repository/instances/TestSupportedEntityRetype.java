/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.ffdc.exception.AssertionFailureException;
import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Test that all defined entities can be created, retyped and deleted
 */
public class TestSupportedEntityRetype extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-entity-retype";
    private static final String testCaseName = "Repository entity retype test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " new entity created.";

    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " new entity retrieved.";

    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " entity is retyped.";

    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " retyped entity has expected type.";

    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " retyped entity has expected properties.";

    private static final String assertion6     = testCaseId + "-06";
    private static final String assertionMsg6  = " retyped entity version number is ";

    private static final String assertion7     = testCaseId + "-07";
    private static final String assertionMsg7  = " retyped entity can be retrieved.";

    private static final String assertion8     = testCaseId + "-08";
    private static final String assertionMsg8  = " retyped entity has expected type.";

    private static final String assertion9     = testCaseId + "-09";
    private static final String assertionMsg9  = " retyped entity has expected properties.";

    private static final String assertion10     = testCaseId + "-10";
    private static final String assertionMsg10  = " retyped entity can be retrieved.";

    private static final String assertion11     = testCaseId + "-11";
    private static final String assertionMsg11  = " retyped entity has expected type.";

    private static final String assertion12     = testCaseId + "-12";
    private static final String assertionMsg12  = " retyped entity has expected properties.";

    private static final String assertion13     = testCaseId + "-13";
    private static final String assertionMsg13  = " retyped entity version number is ";

    private static final String assertion14     = testCaseId + "-14";
    private static final String assertionMsg14  = " retyped entity can be retrieved.";

    private static final String assertion15     = testCaseId + "-15";
    private static final String assertionMsg15  = " retyped entity has expected type.";

    private static final String assertion16     = testCaseId + "-16";
    private static final String assertionMsg16  = " retyped entity has expected properties.";


    private static final String assertion17     = testCaseId + "-17";
    private static final String assertionMsg17  = " repository supports creation of instances.";

    private static final String assertion18     = testCaseId + "-18";
    private static final String assertionMsg18  = " repository supports retype of instances of type ";



    private String            metadataCollectionId;
    private EntityDef         entityDef;
    private String            testTypeName;

    private List<EntityDetail>  createdEntities = new ArrayList<>();


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntityRetype(RepositoryConformanceWorkPad workPad,
                                     EntityDef               entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
              RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                                                    testCaseId,
                                                    testCaseName);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        /*
         * To accommodate repositories that do not support the creation of instances, wrap the creation of the entity
         * in a try..catch to check for FunctionNotSupportedException. If the connector throws this, then give up
         * on the test by setting the discovered property to disabled and returning.
         */

        EntityDetail newEntity;
        InstanceProperties instanceProperties = null;

        long start;
        long elapsedTime;
        try
        {
            /*
             * Generate property values for all the type's defined properties, including inherited properties
             * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
             * thereby getting into the connector-logic beyond the property validation. It also creates an
             * entity that is logically complete - versus an instance with just the locally-defined properties.
             */

            instanceProperties = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

            start = System.currentTimeMillis();
            newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                     entityDef.getGUID(),
                                                     instanceProperties,
                                                    null,
                                                    null);
            elapsedTime = System.currentTimeMillis() - start;

            assertCondition((true),
                            assertion17,
                            testTypeName + assertionMsg17,
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId(),
                            "addEntity",
                            elapsedTime);

            createdEntities.add(newEntity);

        }
        catch (FunctionNotSupportedException exception)
        {

            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * Report the inability to create instances and give up on the testcase....
             */

            super.addNotSupportedAssertion(assertion17,
                                           assertionMsg17,
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            return;
        }
        catch (AssertionFailureException exception)
        {
            /*
             * Re throw this exception, so it is not masked by Exception (below).
             */
            throw exception;
        }
        catch(Exception exc)
        {
            /*
             * We are not expecting any other exceptions from this method call. Log and fail the test.
             */

            String methodName = "addEntity";
            String operationDescription = "add an entity of type " + entityDef.getName();
            Map<String,String> parameters = new HashMap<>();
            parameters.put("typeGUID"                , entityDef.getGUID());
            parameters.put("initialProperties"       , instanceProperties!=null?instanceProperties.toString():"null");
            parameters.put("initialClassifications"  , "null");
            parameters.put("initialStatus"           , "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

            throw new Exception( msg , exc );
        }

        assertCondition((newEntity != null),
                        assertion1,
                        testTypeName + assertionMsg1,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        /*
         * Other conditions - such as content of InstanceAuditHeader fields - are tested by Entity Lifecycle tests; so not tested here.
         */



        /*
         * Validate that the entity can be retrieved.
         */

        verifyCondition((newEntity.equals(metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID()))),
                        assertion2,
                        testTypeName + assertionMsg2,
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                        RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());



        /*
         * Verify that it is possible to retype the entity.
         *
         * This test works as follows:
         * We have an instance of the type. If the type has any subtypes the entity instance will be re-typed to ech of the subtypes.
         * This is the simplest implementation of the test as all properties of the original instance will be valid in the sub-type.
         * If the entity type has no subtypes then the test is ignored.
         *
         */





        /*
         * Find out whether the entity type has any subtypes.
         */
        List<String> subTypeNames = repositoryConformanceWorkPad.getEntitySubTypes(entityDef.getName());

        if (subTypeNames == null)
        {
            /*
             * No subtypes - ignore this type
             */
            return;

        }
        else
        {
            /*
             * This type has subtypes - retype the entity instance to each subtype and back again.
             */
            for (String subTypeName : subTypeNames)
            {

                /*
                 * Re-type the entity instance to this subtype.
                 */

                long nextVersion = newEntity.getVersion() + 1;

                EntityDetail subTypedEntity = null;

                TypeDef subTypeDef = metadataCollection.getTypeDefByName(workPad.getLocalServerUserId(), subTypeName);

                try
                {

                    start = System.currentTimeMillis();
                    subTypedEntity = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                                                     newEntity.getGUID(),
                                                                     entityDef,
                                                                     subTypeDef);
                    elapsedTime = System.currentTimeMillis() - start;
                }
                catch (FunctionNotSupportedException exception)
                {
                    super.addNotSupportedAssertion(assertion18,
                                                   testTypeName + assertionMsg18 + subTypeName,
                                                   RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                                   RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

                    /* Give up the rest of the testcase */
                    return;

                }
                catch (Exception exc)
                {
                    /*
                     * We are not expecting any other exceptions from this method call. Log and fail the test.
                     */

                    String methodName = "reTypeEntity";
                    String operationDescription = "retype an entity of type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("currentTypeDefSummary", entityDef.toString());
                    parameters.put("newTypeDefSummary", subTypeDef.toString());
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                    throw new Exception(msg, exc);
                }

                assertCondition(true,
                                assertion18,
                                testTypeName + assertionMsg18 + subTypeName,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId(),
                                "reTypeEntity",
                                elapsedTime);

                createdEntities.add(subTypedEntity);

                /*
                 * Check that the retyped entity was returned
                 */

                assertCondition((subTypedEntity != null),
                                assertion3,
                                testTypeName + assertionMsg3,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

                /*
                 * Check that the retyped entity has the correct type
                 */

                assertCondition((subTypedEntity.getType() != null) && (subTypedEntity.getType().getTypeDefName().equals(subTypeName)),
                                assertion4,
                                testTypeName + assertionMsg4,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

                /*
                 * Check that the retyped entity properties match the original
                 */

                assertCondition( this.doPropertiesMatch(instanceProperties, subTypedEntity.getProperties() ),
                                assertion5,
                                testTypeName + assertionMsg5,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

                /*
                 * Check that the retyped entity has a higher version than previously
                 */

                assertCondition(((subTypedEntity.getVersion() >= nextVersion)),
                                assertion6,
                                testTypeName + assertionMsg6 + nextVersion,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());


                /*
                 * Verify that the instance can be retrieved from the store and its type and properties match those for the instance returned by the reType method
                 */

                start = System.currentTimeMillis();
                EntityDetail retrievedSubTypedEntity = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());
                elapsedTime = System.currentTimeMillis() - start;

                verifyCondition((retrievedSubTypedEntity != null),
                                assertion7,
                                testTypeName + assertionMsg7,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId(),
                                "getEntityDetail",
                                elapsedTime);

                assertCondition((subTypedEntity.getType() != null) && (subTypedEntity.getType().getTypeDefName().equals(subTypeName)),
                                assertion8,
                                testTypeName + assertionMsg8,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());


                assertCondition( this.doPropertiesMatch(instanceProperties, subTypedEntity.getProperties()),
                                assertion9,
                                testTypeName + assertionMsg9,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());



                /*
                 * Re-type the entity instance from this subtype back to its original type.
                 */

                nextVersion = newEntity.getVersion() + 1;

                EntityDetail superTypedEntity = null;

                try
                {

                    start = System.currentTimeMillis();
                    superTypedEntity = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                                                       newEntity.getGUID(),
                                                                       subTypeDef,
                                                                       entityDef);
                    elapsedTime = System.currentTimeMillis() - start;
                }
                catch (FunctionNotSupportedException exception)
                {
                    super.addNotSupportedAssertion(assertion18,
                                                   subTypeName + assertionMsg18 + testTypeName,
                                                   RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                                   RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

                    return;

                }
                catch (Exception exc)
                {
                    /*
                     * We are not expecting any other exceptions from this method call. Log and fail the test.
                     */
                    String methodName = "reTypeEntity";
                    String operationDescription = "retype an entity of type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("currentTypeDefSummary", subTypeDef.toString());
                    parameters.put("newTypeDefSummary", entityDef.toString());
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                    throw new Exception(msg, exc);
                }

                /*
                 * Check that the retyped entity was returned
                 */

                assertCondition((superTypedEntity != null),
                                assertion10,
                                testTypeName + assertionMsg10,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId(),
                                "reTypeEntity",
                                elapsedTime);

                /*
                 * Check that the retyped entity has the correct type
                 */

                assertCondition((superTypedEntity.getType() != null) && (superTypedEntity.getType().equals(newEntity.getType())),
                                assertion11,
                                testTypeName + assertionMsg11,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

                /*
                 * Check that the retyped entity properties match the original
                 */

                assertCondition( this.doPropertiesMatch(instanceProperties, superTypedEntity.getProperties()),
                                assertion12,
                                testTypeName + assertionMsg12,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

                /*
                 * Check that the retyped entity has a higher version than previously
                 */

                assertCondition(((superTypedEntity.getVersion() >= nextVersion)),
                                assertion13,
                                testTypeName + assertionMsg13 + nextVersion,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());

                /*
                 * Verify that the instance can be retrieved from the store and that it has the correct type and properties
                 */

                start = System.currentTimeMillis();
                EntityDetail retrievedSuperTypedEntity = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), newEntity.getGUID());
                elapsedTime = System.currentTimeMillis() - start;

                verifyCondition((retrievedSuperTypedEntity != null),
                                assertion14,
                                testTypeName + assertionMsg14,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId(),
                                "getEntityDetail",
                                elapsedTime);

                /*
                 * Verify that the instance can be retrieved from the store and its type name and properties match those for the original instance
                 */

                assertCondition((retrievedSuperTypedEntity.getType() != null) && (retrievedSuperTypedEntity.getType().getTypeDefName().equals(entityDef.getName())),
                                assertion15,
                                testTypeName + assertionMsg15,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());


                assertCondition(this.doPropertiesMatch(instanceProperties, retrievedSuperTypedEntity.getProperties()),
                                assertion16,
                                testTypeName + assertionMsg16,
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getProfileId(),
                                RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_TYPE.getRequirementId());
            }
        }

        /*
         * Clean up the test entity.
         *
         */

        /*
         * Delete (soft then hard) the entity. This is done using the newGUID, so if the reidentify did
         * not work this will fail but that's OK.
         */

        try
        {
            EntityDetail deletedEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                                         newEntity.getType().getTypeDefGUID(),
                                                                         newEntity.getType().getTypeDefName(),
                                                                         newEntity.getGUID());
        }
        catch (FunctionNotSupportedException exception)
        {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                       newEntity.getType().getTypeDefGUID(),
                                       newEntity.getType().getTypeDefName(),
                                       newEntity.getGUID());

        super.setSuccessMessage("Entities can be retyped");
    }





    /**
     * Method to clean any instance created by the test case that has not already been cleaned by the running of the test.
     *
     * @throws Exception something went wrong but there is no particular action to take.
     */
    public void cleanup() throws Exception
    {

        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if (createdEntities != null && !createdEntities.isEmpty()) {

            /*
             * Instances were created - clean them up.
             * They may have already been cleaned up so be prepared to catch everything from
             * FunctionNotSupportedException to EntityNotKnownException and maybe others.
             */

            for (EntityDetail entity : createdEntities) {

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

            if ( (firstInstanceProps == null) ||
                 (firstInstanceProps.getInstanceProperties() == null) ||
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

}
