/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.List;


/**
 * Test that all defined classifications can be created, retrieved, updated and deleted.
 * This test is similar to the TestSupportedClassificationLifecycle test except it operates on entity reference copies.
 */
public class TestSupportedReferenceCopyClassificationLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-reference-copy-classification-lifecycle";
    private static final String testCaseName = "Repository reference copy classification lifecycle test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = "No classifications attached to new entity reference copy of type ";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " entity reference copy returned when classification added.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " classification added to entity reference copy of type ";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " classification properties added to entity reference copy of type ";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " classification removed from entity reference copy of type ";


    private static final String assertion6    = testCaseId + "-06";
    private static final String assertionMsg6 = " repository supports creation of instances.";


    private static final String discoveredProperty_referenceCopySupport = " reference copy support";


    private EntityDef         testEntityDef;
    private ClassificationDef classificationDef;
    private String            testTypeName;

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param testEntityDef type of entity to attach classification to
     * @param classificationDef list of valid classifications
     */
    public TestSupportedReferenceCopyClassificationLifecycle(RepositoryConformanceWorkPad workPad,
                                                             EntityDef                    testEntityDef,
                                                             ClassificationDef            classificationDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
              RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        this.classificationDef = classificationDef;
        this.testEntityDef = testEntityDef;

        this.testTypeName = this.updateTestIdByType(classificationDef.getName() + "-" + testEntityDef.getName(),
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

        try {
            /*
             * Create an entity reference copy of the entity type.
             * To do this, a local entity is created, copied and deleted/purged. The copy is modified (to look remote)
             * and is saved as a reference copy
             */


            /*
             * Generate property values for all the type's defined properties, including inherited properties
             * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
             * thereby getting into the connector-logic beyond the property validation. It also creates an
             * entity that is logically complete - versus an instance with just the locally-defined properties.
             */

            newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                    testEntityDef.getGUID(),
                    super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), testEntityDef),
                    null,
                    null);

            assertCondition((true),
                    assertion6,
                    testTypeName + assertionMsg6,
                    RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());


        }
        catch (FunctionNotSupportedException exception) {

            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * Report the inability to create instances and give up on the testcase....
             */

            super.addNotSupportedAssertion(assertion6,
                    assertionMsg6,
                    RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            return;
        }

        /*
         * This test does not verify the content of the entity - that is tested in the entity-lifecycle tests
         */


        /*
         * Make a copy of the entity under a different variable name - not strictly necessary but makes things clearer - then modify it so
         * it appears to be from a remote metadata collection.
         */

        EntityDetail remoteEntity = newEntity;

        /*
         * Hard delete the new entity - we have no further use for it
         * If the repository under test supports soft delete, the entity must be deleted before being purged
         */

        try {
            EntityDetail deletedEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                    newEntity.getType().getTypeDefGUID(),
                    newEntity.getType().getTypeDefName(),
                    newEntity.getGUID());
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                newEntity.getType().getTypeDefGUID(),
                newEntity.getType().getTypeDefName(),
                newEntity.getGUID());

        // Beyond this point, there should be no further references to newEntity
        newEntity = null;


        /*
         * Modify the 'remote' entity so that it looks like it came from a different home repository
         */
        String REMOTE_PREFIX = "remote";

        String localEntityGUID = remoteEntity.getGUID();
        String remoteEntityGUID = REMOTE_PREFIX + localEntityGUID.substring(REMOTE_PREFIX.length());
        remoteEntity.setGUID(remoteEntityGUID);

        String localMetadataCollectionName = remoteEntity.getMetadataCollectionName();
        String remoteMetadataCollectionName = REMOTE_PREFIX + "-metadataCollection-not-" + localMetadataCollectionName;
        remoteEntity.setMetadataCollectionName(remoteMetadataCollectionName);

        String localMetadataCollectionId = remoteEntity.getMetadataCollectionId();
        String remoteMetadataCollectionId = REMOTE_PREFIX + localMetadataCollectionId.substring(REMOTE_PREFIX.length());
        remoteEntity.setMetadataCollectionId(remoteMetadataCollectionId);

        /*
         * There are no conditions to assert or verify yet.
         */


        /*
         * Save a reference copy of the 'remote' entity
         */

        try {

            metadataCollection.saveEntityReferenceCopy(workPad.getLocalServerUserId(), remoteEntity);

            super.addDiscoveredProperty(testTypeName + discoveredProperty_referenceCopySupport,
                    "Enabled",
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


            EntityDetail retrievedReferenceCopy = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), remoteEntityGUID);

            assertCondition((retrievedReferenceCopy.getClassifications() == null),
                    assertion1,
                    assertionMsg1 + testEntityDef.getName(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


            EntityDetail classifiedEntity = metadataCollection.classifyEntity(workPad.getLocalServerUserId(),
                    remoteEntity.getGUID(),
                    classificationDef.getName(),
                    null);

            assertCondition((classifiedEntity != null),
                    assertion2,
                    testEntityDef.getName() + assertionMsg2,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

            Classification initialClassification = null;

            List<Classification> classifications = classifiedEntity.getClassifications();

            if ((classifications != null) && (classifications.size() == 1)) {
                initialClassification = classifications.get(0);
            }

            assertCondition(((initialClassification != null) &&
                            (classificationDef.getName().equals(initialClassification.getName()) &&
                                    (initialClassification.getProperties() == null))),
                    assertion3,
                    testTypeName + assertionMsg3 + testEntityDef.getName(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


            InstanceProperties classificationProperties = this.getPropertiesForInstance(classificationDef.getPropertiesDefinition());
            if (classificationProperties != null) {
                EntityDetail reclassifiedEntity = metadataCollection.updateEntityClassification(workPad.getLocalServerUserId(),
                        remoteEntity.getGUID(),
                        classificationDef.getName(),
                        classificationProperties);

                Classification updatedClassification = null;
                classifications = reclassifiedEntity.getClassifications();

                if ((classifications != null) && (classifications.size() == 1)) {
                    updatedClassification = classifications.get(0);
                }

                assertCondition(((updatedClassification != null) &&
                                (classificationDef.getName().equals(initialClassification.getName()) &&
                                        (updatedClassification.getProperties() != null))),
                        assertion4,
                        testTypeName + assertionMsg4 + testEntityDef.getName(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());
            }

            EntityDetail declassifiedEntity = metadataCollection.declassifyEntity(workPad.getLocalServerUserId(),
                    remoteEntity.getGUID(),
                    classificationDef.getName());

            assertCondition(((declassifiedEntity != null) && (declassifiedEntity.getClassifications() == null)),
                    assertion5,
                    testTypeName + assertionMsg5 + testEntityDef.getName(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());



            /*
             * Purge the reference copy - this is just to clean up rather than part of the test.
             */

            metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(),
                    remoteEntity.getGUID(),
                    remoteEntity.getType().getTypeDefGUID(),
                    remoteEntity.getType().getTypeDefName(),
                    remoteEntity.getMetadataCollectionId());


        }
        catch (FunctionNotSupportedException e) {

            /*
             * Disable the discovered property, with evidence that reference copy storage requirement is not supported.
             */
            super.addDiscoveredProperty(testTypeName + discoveredProperty_referenceCopySupport,
                    "Disabled",
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        }
        /*
         * If any other exception was thrown, let it go; this method will throw it and fail the whole test
         */


        super.setSuccessMessage("Classifications on entity reference copies can be managed through their lifecycle");
    }

    /**
     * Method to clean any instance created by the test case.
     *
     * @throws Exception something went wrong with the test.
     */
    public void cleanup() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        /*
         * Find any entities of the given type def and delete them....
         */

        int fromElement = 0;
        int pageSize = 50; // chunk size - loop below will repeatedly get chunks
        int resultSize = 0;

        do {

            InstanceProperties emptyMatchProperties = new InstanceProperties();


            List<EntityDetail> entities = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                    testEntityDef.getGUID(),
                    emptyMatchProperties,
                    MatchCriteria.ANY,
                    fromElement,
                    null,
                    null,
                    null,
                    null,
                    null,
                    pageSize);


            if (entities == null) {
                /*
                 * There are no instances of this type reported by the repository.
                 */
                return;

            }

            /*
             * Report how many entities were left behind at the end of the test run
             */

            resultSize = entities.size();

            System.out.println("At completion of testcase "+testTypeName+", there were " + entities.size() + " entities found");

            for (EntityDetail entity : entities) {

                /*
                 * Try soft delete (ok if it fails) and purge.
                 */

                try {
                    EntityDetail deletedEntity = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                            entity.getType().getTypeDefGUID(),
                            entity.getType().getTypeDefName(),
                            entity.getGUID());

                } catch (FunctionNotSupportedException exception) {
                    /* OK - had to try soft; continue to purge */
                }

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                        entity.getType().getTypeDefGUID(),
                        entity.getType().getTypeDefName(),
                        entity.getGUID());

                System.out.println("Entity wth GUID " + entity.getGUID() + " removed");

            }
        } while (resultSize >= pageSize);

    }

}
