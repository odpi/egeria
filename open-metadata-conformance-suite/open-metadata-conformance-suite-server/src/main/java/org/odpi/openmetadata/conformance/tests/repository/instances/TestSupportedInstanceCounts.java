/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Test that a repository's instance counts agree with what it returns from the equivalent search.
 * <br>
 * countEntities() and countRelationships() take the same criteria as their find() counterparts, and the code
 * above the repository uses them to answer "how many" without pulling the instances back.  A count that
 * disagrees with the search is worse than no count at all: it is used to size pages and to decide whether
 * there is anything to fetch, so a wrong answer is acted upon rather than noticed.
 * <br>
 * The assertion is that the count is at least the number of instances this test created, and matches the
 * instances find() reports for the same criteria that this repository is answerable for counting - those it
 * homes, and those it replicates on behalf of a non-cohort provenance.  It is not an absolute number because
 * the repository legitimately holds other instances of the type, and it is not the whole of what find()
 * returns because find() also returns reference copies of instances another member homes; those are counted
 * by that member, so counting them here too would double them in a federated count.
 */
public class TestSupportedInstanceCounts extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-instance-counts";
    private static final String testCaseName = "Repository instance counts test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " entity count includes the entities just created.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " entity count agrees with the entities found that it is answerable for.";

    private final RepositoryConformanceWorkPad workPad;
    private final EntityDef                    entityDef;
    private final String                       testTypeName;

    private final List<EntityDetail> createdEntities = new ArrayList<>();

    private static final int INSTANCE_COUNT = 3;


    /**
     * Set up the test case.
     *
     * @param workPad place for parameters and results
     * @param entityDef the entity type being counted
     */
    public TestSupportedInstanceCounts(RepositoryConformanceWorkPad workPad,
                                       EntityDef                    entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

        this.workPad   = workPad;
        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(), testCaseId, testCaseName);
    }


    /**
     * Run the test.
     *
     * @throws Exception something went wrong that the conformance suite could not handle
     */
    @Override
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        try
        {
            for (int i = 0; i < INSTANCE_COUNT; i++)
            {
                InstanceProperties entityProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

                createdEntities.add(metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                                  entityDef.getGUID(),
                                                                  entityProperties,
                                                                  null,
                                                                  null));
            }
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
            return;
        }

        try
        {
            long start = System.currentTimeMillis();
            long count = metadataCollection.countEntities(workPad.getLocalServerUserId(),
                                                          entityDef.getGUID(),
                                                          null,
                                                          false,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            long elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((count >= INSTANCE_COUNT),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "countEntities",
                            elapsedTime);

            /*
             * The count and the search have to agree - but they are not asked quite the same question, and the
             * difference is deliberate.
             *
             * A find is asked of one repository and answers with everything it holds that matches, reference
             * copies included: the caller can see which repository answered and can deduplicate by GUID.  A
             * count answers with a number, and a federated count is the sum of the numbers every cohort member
             * gives back - so an instance held as a reference copy by three members would be counted three
             * times if each member counted its copies, and nothing downstream can undo that because the GUIDs
             * are gone.  A repository therefore counts only what it is answerable for: what it homes, and what
             * it replicates on behalf of a non-cohort provenance.
             *
             * So the number counted is compared against the instances found that this repository homes or
             * replicates, not against everything it returned.
             */
            List<EntityDetail> found = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                                        entityDef.getGUID(),
                                                                        null,
                                                                        false,
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0);

            int foundCount = 0;

            if (found != null)
            {
                String tutMetadataCollectionId = workPad.getTutMetadataCollectionId();

                for (EntityDetail foundEntity : found)
                {
                    if (foundEntity != null)
                    {
                        if ((tutMetadataCollectionId == null) ||
                            (tutMetadataCollectionId.equals(foundEntity.getMetadataCollectionId())) ||
                            (tutMetadataCollectionId.equals(foundEntity.getReplicatedBy())))
                        {
                            foundCount++;
                        }
                    }
                }
            }

            /*
             * find() is limited by the page size the server allows, so the two can only be compared when the
             * search was not truncated.
             */
            if (foundCount < super.getMaxSearchResults())
            {
                verifyCondition((count == foundCount),
                                assertion2,
                                testTypeName + assertionMsg2 + " (counted " + count + ", found " + foundCount + ")",
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
            }
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
        }
        finally
        {
            for (EntityDetail entity : createdEntities)
            {
                try
                {
                    metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                    entity.getType().getTypeDefGUID(),
                                                    entity.getType().getTypeDefName(),
                                                    entity.getGUID());
                }
                catch (Exception ignored)
                {
                    /* best effort */
                }

                try
                {
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

        super.setSuccessMessage("Instance counts agree with the equivalent search");
    }
}
