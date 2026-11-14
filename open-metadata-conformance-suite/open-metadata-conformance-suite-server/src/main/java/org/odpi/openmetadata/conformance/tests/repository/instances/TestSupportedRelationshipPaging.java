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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test that a repository honours the paging contract when a caller pages through findRelationships().
 * <br>
 * The contract: the caller asks for a page by starting element and page size, and asks for the next page a page
 * size further on.  An empty list means the page was retrieved but nothing on it could be returned (for example,
 * it was filtered out), so the caller keeps going.  Null means there is nothing more, so the caller stops.
 * <br>
 * Every layer above the repository - the enterprise connector, the generic handlers, the REST APIs and the
 * clients in every language - pages on that understanding, so a repository that breaks it breaks them all, and
 * neither failure is noisy.  Returning null before the end hides the remaining relationships.  Returning an
 * empty list at the end means a caller that pages never stops.
 * <br>
 * The test creates a few relationships of the type, each between entities of its own, then pages through every
 * relationship of the type with a page size smaller than the number created, so that the instances span several
 * pages.  The results are sequenced by GUID so that each request sees the same order.  It asserts that no page is
 * larger than requested, that no relationship appears on two pages, that every relationship is reached, and that
 * the end is signalled by null.
 * <br>
 * Other relationships of the type (and its subtypes) are included in the walk.  If the repository holds more of
 * them than one search can return, the walk is not attempted rather than reporting on a partial view.  A type
 * whose end types are not among the types being tested is skipped, since its relationships cannot be created.
 */
public class TestSupportedRelationshipPaging extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-relationship-paging";
    private static final String testCaseName = "Repository relationship paging test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " no page of relationships holds more than the page size requested.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " no relationship is returned on more than one page.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " paging reaches every relationship that a single search returns.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " the end of the relationships is signalled by null, not by an empty list.";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " a page requested beyond the last relationship is null.";

    /*
     * More instances than fit on one page, so the walk crosses page boundaries, but few enough that running the
     * test for every type stays cheap.
     */
    private static final int INSTANCE_COUNT = 3;
    private static final int PAGE_SIZE      = 2;

    private final RepositoryConformanceWorkPad workPad;
    private final Map<String, EntityDef>       entityDefs;
    private final RelationshipDef              relationshipDef;
    private final String                       testTypeName;

    private final List<EntityDetail> createdEntities      = new ArrayList<>();
    private final List<Relationship> createdRelationships = new ArrayList<>();


    /**
     * Set up the test case.
     *
     * @param workPad place for parameters and results
     * @param entityDefs the entity types supported by the repository under test
     * @param relationshipDef the relationship type being paged through
     */
    public TestSupportedRelationshipPaging(RepositoryConformanceWorkPad workPad,
                                           Map<String, EntityDef>       entityDefs,
                                           RelationshipDef              relationshipDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

        this.workPad         = workPad;
        this.entityDefs      = entityDefs;
        this.relationshipDef = relationshipDef;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(), testCaseId, testCaseName);
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

        String end1TypeName = this.getSupportedEndTypeName(relationshipDef.getEndDef1().getEntityType().getName());
        String end2TypeName = this.getSupportedEndTypeName(relationshipDef.getEndDef2().getEntityType().getName());

        if ((end1TypeName == null) || (end2TypeName == null))
        {
            /*
             * The ends this relationship type needs are not among the types being tested, so there is nothing
             * to assert for it.
             */
            return;
        }

        try
        {
            for (int i = 0; i < INSTANCE_COUNT; i++)
            {
                this.createRelationship(metadataCollection, end1TypeName, end2TypeName);
            }
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());
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
             * One search for everything, to know how many relationships the walk should reach.
             */
            int                searchLimit      = super.getMaxSearchResults();
            List<Relationship> allRelationships = this.findPage(metadataCollection, 0, searchLimit);
            int                expectedCount    = (allRelationships == null) ? 0 : allRelationships.size();

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
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            walk.getElapsedTime());

            verifyCondition((! walk.isDuplicateReturned()),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

            boolean reachedAll = (walk.getInstanceCount() == expectedCount);

            for (Relationship createdRelationship : createdRelationships)
            {
                if (! walk.reached(createdRelationship.getGUID()))
                {
                    reachedAll = false;
                }
            }

            verifyCondition(reachedAll,
                            assertion3,
                            testTypeName + assertionMsg3 + " (single search returned " + expectedCount +
                                    ", paging reached " + walk.getInstanceCount() + ")",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

            verifyCondition(walk.isEndedWithNull(),
                            assertion4,
                            testTypeName + assertionMsg4 + " (" + walk.getPagesRequested() + " pages requested)",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

            long start = System.currentTimeMillis();
            List<Relationship> beyondTheEnd = this.findPage(metadataCollection, walk.getNextStartFrom() + (10 * PAGE_SIZE), PAGE_SIZE);
            long elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((beyondTheEnd == null),
                            assertion5,
                            testTypeName + assertionMsg5,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());
            return;
        }
        finally
        {
            this.cleanUp(metadataCollection);
        }

        super.setSuccessMessage("Paging through relationships follows the paging contract");
    }


    /**
     * Retrieve one page of the relationships of the type under test, sequenced by GUID so that every page sees
     * the same order.
     *
     * @param metadataCollection repository under test
     * @param startFrom position of the first relationship on the page
     * @param pageSize maximum number of relationships on the page
     * @return the page
     * @throws Exception problem calling the repository
     */
    private List<Relationship> findPage(OMRSMetadataCollection metadataCollection,
                                        int                    startFrom,
                                        int                    pageSize) throws Exception
    {
        return metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                    relationshipDef.getGUID(),
                                                    null,
                                                    false,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    startFrom,
                                                    null,
                                                    null,
                                                    null,
                                                    SequencingOrder.GUID,
                                                    pageSize);
    }


    /**
     * Create a relationship of the type under test between two new entities, and remember all three for clean
     * up.  Each relationship has entities of its own so that no end's multiplicity is exceeded.
     *
     * @param metadataCollection repository under test
     * @param end1TypeName type of entity for end 1
     * @param end2TypeName type of entity for end 2
     * @throws Exception the relationship could not be created
     */
    private void createRelationship(OMRSMetadataCollection metadataCollection,
                                    String                 end1TypeName,
                                    String                 end2TypeName) throws Exception
    {
        EntityDetail end1 = this.createEntity(metadataCollection, end1TypeName);
        EntityDetail end2 = this.createEntity(metadataCollection, end2TypeName);

        InstanceProperties relationshipProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef);

        createdRelationships.add(metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                    relationshipDef.getGUID(),
                                                                    relationshipProperties,
                                                                    end1.getGUID(),
                                                                    end2.getGUID(),
                                                                    null));
    }


    /**
     * Return a type that this repository supports for one end of the relationship - the type the end
     * definition names, or one of its subtypes if the named type itself is not being tested.
     *
     * @param endDefTypeName type named by the relationship's end definition
     * @return a supported type name, or null if none is available
     */
    private String getSupportedEndTypeName(String endDefTypeName)
    {
        List<String> candidateTypeNames = new ArrayList<>();

        candidateTypeNames.add(endDefTypeName);

        if (workPad.getEntitySubTypes(endDefTypeName) != null)
        {
            candidateTypeNames.addAll(workPad.getEntitySubTypes(endDefTypeName));
        }

        for (String candidateTypeName : candidateTypeNames)
        {
            if (entityDefs.get(candidateTypeName) != null)
            {
                return candidateTypeName;
            }
        }

        return null;
    }


    /**
     * Create an entity to act as one end of a relationship, and remember it for clean up.
     *
     * @param metadataCollection repository under test
     * @param entityTypeName type of entity to create
     * @return new entity
     * @throws Exception the entity could not be created
     */
    private EntityDetail createEntity(OMRSMetadataCollection metadataCollection,
                                      String                 entityTypeName) throws Exception
    {
        EntityDef          entityType       = entityDefs.get(entityTypeName);
        InstanceProperties entityProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityType);

        EntityDetail newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                              entityType.getGUID(),
                                                              entityProperties,
                                                              null,
                                                              null);
        createdEntities.add(newEntity);

        return newEntity;
    }


    /**
     * Remove everything this test created - relationships first, since an entity cannot be purged while a
     * relationship still reaches it.  Failures are ignored: a problem tidying up should not be reported as a
     * conformance failure of the repository's paging.
     *
     * @param metadataCollection repository under test
     */
    private void cleanUp(OMRSMetadataCollection metadataCollection)
    {
        for (Relationship relationship : createdRelationships)
        {
            if (relationship != null)
            {
                try
                {
                    metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                                                          relationship.getType().getTypeDefGUID(),
                                                          relationship.getType().getTypeDefName(),
                                                          relationship.getGUID());
                }
                catch (Exception ignored)
                {
                    /* best effort */
                }

                try
                {
                    metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                                                         relationship.getType().getTypeDefGUID(),
                                                         relationship.getType().getTypeDefName(),
                                                         relationship.getGUID());
                }
                catch (Exception ignored)
                {
                    /* best effort */
                }
            }
        }

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

        createdRelationships.clear();
        createdEntities.clear();
    }
}
