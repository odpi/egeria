/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Test that a repository honours the paging contract when a caller pages through findEntities().
 * <br>
 * The contract: the caller asks for a page by starting element and page size, and asks for the next page a page
 * size further on.  An empty list means the page was retrieved but nothing on it could be returned (for example,
 * it was filtered out), so the caller keeps going.  Null means there is nothing more, so the caller stops.
 * <br>
 * Every layer above the repository - the enterprise connector, the generic handlers, the REST APIs and the
 * clients in every language - pages on that understanding, so a repository that breaks it breaks them all, and
 * neither failure is noisy.  Returning null before the end hides the remaining instances: a "find by name"
 * reports not found although a larger page would have found it.  Returning an empty list at the end means a
 * caller that pages never stops.
 * <br>
 * The test creates a few entities of the type, then pages through every entity of the type with a page size
 * smaller than the number created, so that the instances span several pages.  The results are sequenced by GUID
 * so that each request sees the same order.  It asserts that no page is larger than requested, that no instance
 * appears on two pages, that every instance is reached, and that the end is signalled by null.
 * <br>
 * Other instances of the type (and its subtypes) are included in the walk.  If the repository holds more of them
 * than one search can return, the walk is not attempted rather than reporting on a partial view.
 */
public class TestSupportedEntityPaging extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-entity-paging";
    private static final String testCaseName = "Repository entity paging test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " no page of entities holds more than the page size requested.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " no entity is returned on more than one page.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " paging reaches every entity that a single search returns.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " the end of the entities is signalled by null, not by an empty list.";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " a page requested beyond the last entity is null.";

    /*
     * More instances than fit on one page, so the walk crosses page boundaries, but few enough that running the
     * test for every type stays cheap.
     */
    private static final int INSTANCE_COUNT = 3;
    private static final int PAGE_SIZE      = 2;

    private final RepositoryConformanceWorkPad workPad;
    private final EntityDef                    entityDef;
    private final String                       testTypeName;

    private final List<EntityDetail> createdEntities = new ArrayList<>();


    /**
     * Set up the test case.
     *
     * @param workPad place for parameters and results
     * @param entityDef the entity type being paged through
     */
    public TestSupportedEntityPaging(RepositoryConformanceWorkPad workPad,
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
            this.cleanUp(metadataCollection);
            return;
        }
        catch (Exception error)
        {
            this.cleanUp(metadataCollection);
            throw error;
        }

        try
        {
            /*
             * One search for everything, to know how many instances the walk should reach.
             */
            int                searchLimit   = super.getMaxSearchResults();
            List<EntityDetail> allEntities   = this.findPage(metadataCollection, 0, searchLimit);
            int                expectedCount = (allEntities == null) ? 0 : allEntities.size();

            if ((searchLimit > 0) && (expectedCount >= searchLimit))
            {
                super.setSuccessMessage("Too many instances of the type to page through them all; paging not tested");
                return;
            }

            PagedSearchWalk walk = new PagedSearchWalk((startFrom, pageSize) -> this.findPage(metadataCollection, startFrom, pageSize),
                                                       PAGE_SIZE,
                                                       Math.max(expectedCount, INSTANCE_COUNT));

            verifyCondition((! walk.isPageTooLarge()),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            walk.getElapsedTime());

            verifyCondition((! walk.isDuplicateReturned()),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

            boolean reachedAll = (walk.getInstanceCount() == expectedCount);

            for (EntityDetail createdEntity : createdEntities)
            {
                if (! walk.reached(createdEntity.getGUID()))
                {
                    reachedAll = false;
                }
            }

            verifyCondition(reachedAll,
                            assertion3,
                            testTypeName + assertionMsg3 + " (single search returned " + expectedCount +
                                    ", paging reached " + walk.getInstanceCount() + ")",
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

            verifyCondition(walk.isEndedWithNull(),
                            assertion4,
                            testTypeName + assertionMsg4 + " (" + walk.getPagesRequested() + " pages requested)",
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

            long start = System.currentTimeMillis();
            List<EntityDetail> beyondTheEnd = this.findPage(metadataCollection, walk.getNextStartFrom() + (10 * PAGE_SIZE), PAGE_SIZE);
            long elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((beyondTheEnd == null),
                            assertion5,
                            testTypeName + assertionMsg5,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
            return;
        }
        finally
        {
            this.cleanUp(metadataCollection);
        }

        super.setSuccessMessage("Paging through entities follows the paging contract");
    }


    /**
     * Retrieve one page of the entities of the type under test, sequenced by GUID so that every page sees the
     * same order.
     *
     * @param metadataCollection repository under test
     * @param startFrom position of the first entity on the page
     * @param pageSize maximum number of entities on the page
     * @return the page
     * @throws Exception problem calling the repository
     */
    private List<EntityDetail> findPage(OMRSMetadataCollection metadataCollection,
                                        int                    startFrom,
                                        int                    pageSize) throws Exception
    {
        return metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                               entityDef.getGUID(),
                                               null,
                                               false,
                                               null,
                                               startFrom,
                                               null,
                                               null,
                                               null,
                                               null,
                                               SequencingOrder.GUID,
                                               pageSize);
    }


    /**
     * Remove the entities this test created.  Failures are ignored: a problem tidying up should not be reported
     * as a conformance failure of the repository's paging.
     *
     * @param metadataCollection repository under test
     */
    private void cleanUp(OMRSMetadataCollection metadataCollection)
    {
        for (EntityDetail entity : createdEntities)
        {
            if (entity != null)
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

        createdEntities.clear();
    }
}
