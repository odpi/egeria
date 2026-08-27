/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Test that a repository records the provenance of an instance created on behalf of an external source.
 * <br>
 * addExternalEntity() is how content packs and external catalogues get their metadata into a repository, and
 * the provenance it records is what later stops a local caller modifying that metadata.  If the external
 * source identifiers are not stored, or the provenance comes back as LOCAL_COHORT, then instances that
 * belong to somebody else become editable here - a failure that only shows up as metadata quietly diverging
 * from its source of truth.
 */
public class TestSupportedExternalInstances extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-external-instances";
    private static final String testCaseName = "Repository external instances test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " external entity can be created.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " external entity records its external provenance.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " external entity records the external source identifiers.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " external entity keeps its provenance when retrieved again.";

    private final RepositoryConformanceWorkPad workPad;
    private final EntityDef                    entityDef;
    private final String                       testTypeName;

    private final List<EntityDetail> createdEntities = new ArrayList<>();


    /**
     * Set up the test case.
     *
     * @param workPad place for parameters and results
     * @param entityDef the entity type being created externally
     */
    public TestSupportedExternalInstances(RepositoryConformanceWorkPad workPad,
                                          EntityDef                    entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getProfileId(),
              RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getRequirementId());

        this.workPad   = workPad;
        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(), testCaseId, testCaseName);
    }


    /**
     * Run the test.
     *
     * @throws Exception something went wrong that the conformance suite could not handle
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        String externalSourceGUID = UUID.randomUUID().toString();
        String externalSourceName = "conformance-suite-external-source";

        EntityDetail externalEntity;

        try
        {
            InstanceProperties entityProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

            long start = System.currentTimeMillis();
            externalEntity = metadataCollection.addExternalEntity(workPad.getLocalServerUserId(),
                                                                  entityDef.getGUID(),
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  entityProperties,
                                                                  null,
                                                                  null);
            long elapsedTime = System.currentTimeMillis() - start;

            createdEntities.add(externalEntity);

            verifyCondition((externalEntity != null),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getRequirementId(),
                            "addExternalEntity",
                            elapsedTime);
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getProfileId(),
                                           RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getRequirementId());
            return;
        }

        try
        {
            verifyCondition((externalEntity.getInstanceProvenanceType() == InstanceProvenanceType.EXTERNAL_SOURCE),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getRequirementId());

            verifyCondition((externalSourceGUID.equals(externalEntity.getMetadataCollectionId())),
                            assertion3,
                            testTypeName + assertionMsg3,
                            RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getRequirementId());

            /*
             * Provenance has to survive storage, not just be echoed back from the create.
             */
            long start = System.currentTimeMillis();
            EntityDetail retrievedEntity = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(),
                                                                              externalEntity.getGUID());
            long elapsedTime = System.currentTimeMillis() - start;

            boolean provenanceKept = (retrievedEntity != null) &&
                                     (retrievedEntity.getInstanceProvenanceType() == InstanceProvenanceType.EXTERNAL_SOURCE) &&
                                     (externalSourceGUID.equals(retrievedEntity.getMetadataCollectionId()));

            verifyCondition(provenanceKept,
                            assertion4,
                            testTypeName + assertionMsg4,
                            RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.STORE_EXTERNAL_ENTITIES.getRequirementId(),
                            "getEntityDetail",
                            elapsedTime);
        }
        finally
        {
            for (EntityDetail entity : createdEntities)
            {
                try
                {
                    metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(), entity);
                }
                catch (Exception ignored)
                {
                    /* best effort - fall through to the ordinary delete below */
                }

                try
                {
                    metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                    entity.getType().getTypeDefGUID(),
                                                    entity.getType().getTypeDefName(),
                                                    entity.getGUID());

                    metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                                   entity.getType().getTypeDefGUID(),
                                                   entity.getType().getTypeDefName(),
                                                   entity.getGUID());
                }
                catch (Exception ignored)
                {
                    /* best effort */
                }
            }
        }

        super.setSuccessMessage("External instances keep the provenance of the source that created them");
    }
}
