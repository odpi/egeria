/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test that a repository can constrain an entity search by the classifications an entity carries.
 * <br>
 * findEntities() accepts a SearchClassifications with a MatchCriteria, and it is the only way to ask the
 * repository "which entities carry this classification" - or, under NONE, "which do not".  Where it is not
 * relied upon, the code above the repository retrieves a broader set and drops the entities whose
 * classifications do not suit, which costs a larger read and, since the repository has already applied
 * paging by then, returns short pages.
 * <br>
 * This capability has a history of being wrong quietly.  The PostgreSQL query builder carries a note that
 * MatchCriteria was previously ignored here, so every named classification condition was combined the same
 * way and the search returned nothing whatever the caller asked for.  A search that returns nothing looks
 * like an empty repository rather than a broken query, which is why it survived - and why it is worth
 * asserting all three criteria rather than only the common one.
 */
public class TestSupportedEntityClassificationSearch extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-entity-classification-search";
    private static final String testCaseName = "Repository entity classification search test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " classified entities are found by their classification.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " unclassified entities are not found by that classification.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " entities without the classification are found when NONE is requested.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " page size is honoured when a classification constraint is applied.";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " the history of a classification can be retrieved.";
    private static final String assertion6    = testCaseId + "-06";
    private static final String assertionMsg6 = " an entity carrying every requested classification is found when ALL is requested.";
    private static final String assertion7    = testCaseId + "-07";
    private static final String assertionMsg7 = " an entity carrying only one of them is not found when ALL is requested.";
    private static final String assertion8    = testCaseId + "-08";
    private static final String assertionMsg8 = " an entity is no longer found by a classification once it has been removed.";

    private final RepositoryConformanceWorkPad workPad;
    private final EntityDef                    entityDef;
    private final ClassificationDef            classificationDef;
    private final List<ClassificationDef>      allClassificationDefs;
    private final String                       testTypeName;

    private final List<EntityDetail> createdEntities = new ArrayList<>();


    /**
     * Set up the test case.
     *
     * @param workPad place for parameters and results
     * @param entityDef the entity type to classify
     * @param classificationDefs the classification types available; the first is the one being tested
     */
    public TestSupportedEntityClassificationSearch(RepositoryConformanceWorkPad workPad,
                                                   EntityDef                    entityDef,
                                                   List<ClassificationDef>      classificationDefs)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

        this.workPad               = workPad;
        this.entityDef             = entityDef;
        this.allClassificationDefs = classificationDefs;
        this.classificationDef     = classificationDefs.get(0);

        this.testTypeName = this.updateTestIdByType(classificationDef.getName() + "-" + entityDef.getName(),
                                                    testCaseId,
                                                    testCaseName);
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

        EntityDetail classifiedA;
        EntityDetail classifiedB;
        EntityDetail unclassified;

        try
        {
            /*
             * Two entities carrying the classification and one without, so that each match criteria has
             * something to include and something to exclude.
             */
            classifiedA  = this.createEntity(metadataCollection);
            classifiedB  = this.createEntity(metadataCollection);
            unclassified = this.createEntity(metadataCollection);

            classifiedA = this.classify(metadataCollection, classifiedA);
            classifiedB = this.classify(metadataCollection, classifiedB);
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
            /*
             * ALL - the entities carrying the classification, and only those.  The assertion is by
             * containment rather than as an exact set because other entities of this type elsewhere in the
             * repository may legitimately carry the same classification.
             */
            long start = System.currentTimeMillis();
            List<EntityDetail> results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                                         entityDef.getGUID(),
                                                                         null,
                                                                         false,
                                                                         null,
                                                                         0,
                                                                         null,
                                                                         this.searchFor(MatchCriteria.ALL),
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         0);
            long elapsedTime = System.currentTimeMillis() - start;

            Set<String> returnedGUIDs = this.guidsOf(results);

            verifyCondition((returnedGUIDs.contains(classifiedA.getGUID()) && returnedGUIDs.contains(classifiedB.getGUID())),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);

            verifyCondition((! returnedGUIDs.contains(unclassified.getGUID())),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

            /*
             * NONE - everything except those, which is what an "omitted classification" filter is asking for.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                      entityDef.getGUID(),
                                                      null,
                                                      false,
                                                      null,
                                                      0,
                                                      null,
                                                      this.searchFor(MatchCriteria.NONE),
                                                      null,
                                                      null,
                                                      null,
                                                      0);
            elapsedTime = System.currentTimeMillis() - start;

            returnedGUIDs = this.guidsOf(results);

            boolean noneCorrect = (returnedGUIDs.contains(unclassified.getGUID())) &&
                                  (! returnedGUIDs.contains(classifiedA.getGUID())) &&
                                  (! returnedGUIDs.contains(classifiedB.getGUID()));

            verifyCondition(noneCorrect,
                            assertion3,
                            testTypeName + assertionMsg3,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);

            /*
             * Paging with the classification constraint applied.  This is the property the handler layer
             * cannot currently rely on: where the classification filtering happens after retrieval, a caller
             * asking for one entity can be handed none.
             */
            start = System.currentTimeMillis();
            List<EntityDetail> firstPage = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                                           entityDef.getGUID(),
                                                                           null,
                                                                           false,
                                                                           null,
                                                                           0,
                                                                           null,
                                                                           this.searchFor(MatchCriteria.ALL),
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           1);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(((firstPage != null) && (firstPage.size() == 1)),
                            assertion4,
                            testTypeName + assertionMsg4,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);

            /*
             * getClassificationHistory() is used above the repository to show how a classification has
             * changed, and it is the only call that reports that.  Asserting it here rather than in its own
             * test case reuses the classified entity this test has already built.
             */
            try
            {
                start = System.currentTimeMillis();
                List<Classification> classificationHistory =
                        metadataCollection.getClassificationHistory(workPad.getLocalServerUserId(),
                                                                    classifiedA.getGUID(),
                                                                    classificationDef.getName(),
                                                                    null,
                                                                    null,
                                                                    0,
                                                                    0,
                                                                    HistorySequencingOrder.BACKWARDS);
                elapsedTime = System.currentTimeMillis() - start;

                verifyCondition(((classificationHistory != null) && (! classificationHistory.isEmpty())),
                                assertion5,
                                testTypeName + assertionMsg5,
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                                "getClassificationHistory",
                                elapsedTime);
            }
            catch (FunctionNotSupportedException notSupported)
            {
                super.addNotSupportedAssertion(assertion5,
                                               assertionMsg5,
                                               RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                               RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
            }

            /*
             * ALL with more than one classification.
             * <br>
             * An entity carries each classification on a separate row, so this cannot be answered by asking
             * one row to match two names - that combination is never true and the search returns nothing.
             * The case only appears when a second classification is valid for this entity type, so it is
             * asserted when one is available rather than skipped altogether.
             */
            if (allClassificationDefs.size() > 1)
            {
                ClassificationDef secondClassificationDef = allClassificationDefs.get(1);

                EntityDetail carriesBoth = this.createEntity(metadataCollection);

                this.classify(metadataCollection, carriesBoth, classificationDef);
                this.classify(metadataCollection, carriesBoth, secondClassificationDef);

                SearchClassifications bothRequired = this.searchFor(MatchCriteria.ALL,
                                                                    classificationDef.getName(),
                                                                    secondClassificationDef.getName());

                start = System.currentTimeMillis();
                results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                          entityDef.getGUID(),
                                                          null,
                                                          false,
                                                          null,
                                                          0,
                                                          null,
                                                          bothRequired,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
                elapsedTime = System.currentTimeMillis() - start;

                returnedGUIDs = this.guidsOf(results);

                verifyCondition(returnedGUIDs.contains(carriesBoth.getGUID()),
                                assertion6,
                                testTypeName + assertionMsg6,
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                                "findEntities",
                                elapsedTime);

                /*
                 * classifiedA carries only the first classification, so requiring both must exclude it.
                 */
                verifyCondition((! returnedGUIDs.contains(classifiedA.getGUID())),
                                assertion7,
                                testTypeName + assertionMsg7,
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
            }

            /*
             * Removing a classification has to remove the entity from the results.
             * <br>
             * Worth asserting separately because a classification is removed by soft-deleting its row
             * rather than by taking it away: a search that looks only for the current version of the row,
             * without also excluding deleted ones, keeps matching a classification that is no longer there.
             * Nothing fails - the entity is simply still returned.
             */
            metadataCollection.declassifyEntity(workPad.getLocalServerUserId(),
                                                classifiedB.getGUID(),
                                                classificationDef.getName());

            start = System.currentTimeMillis();
            results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                      entityDef.getGUID(),
                                                      null,
                                                      false,
                                                      null,
                                                      0,
                                                      null,
                                                      this.searchFor(MatchCriteria.ALL),
                                                      null,
                                                      null,
                                                      null,
                                                      0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((! this.guidsOf(results).contains(classifiedB.getGUID())),
                            assertion8,
                            testTypeName + assertionMsg8,
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
        }
        finally
        {
            this.cleanUp(metadataCollection);
        }

        super.setSuccessMessage("Entities can be found by the classifications they carry");
    }


    /**
     * Build the classification search for one match criteria.
     *
     * @param matchCriteria how the condition should be applied
     * @return search classifications naming the type under test
     */
    private SearchClassifications searchFor(MatchCriteria matchCriteria)
    {
        return this.searchFor(matchCriteria, classificationDef.getName());
    }


    /**
     * Build the classification search naming one or more classifications.
     *
     * @param matchCriteria how the conditions should be combined
     * @param classificationNames the classifications to name
     * @return search classifications
     */
    private SearchClassifications searchFor(MatchCriteria matchCriteria,
                                            String...     classificationNames)
    {
        List<ClassificationCondition> conditions = new ArrayList<>();

        for (String classificationName : classificationNames)
        {
            ClassificationCondition condition = new ClassificationCondition();

            condition.setName(classificationName);

            conditions.add(condition);
        }

        SearchClassifications searchClassifications = new SearchClassifications();

        searchClassifications.setConditions(conditions);
        searchClassifications.setMatchCriteria(matchCriteria);

        return searchClassifications;
    }


    /**
     * Create an entity of the type under test, and remember it for clean up.
     *
     * @param metadataCollection repository under test
     * @return new entity
     * @throws Exception the entity could not be created
     */
    private EntityDetail createEntity(OMRSMetadataCollection metadataCollection) throws Exception
    {
        InstanceProperties entityProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

        EntityDetail newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                              entityDef.getGUID(),
                                                              entityProperties,
                                                              null,
                                                              null);
        createdEntities.add(newEntity);

        return newEntity;
    }


    /**
     * Attach the classification under test to an entity.
     *
     * @param metadataCollection repository under test
     * @param entity entity to classify
     * @return the classified entity
     * @throws Exception the entity could not be classified
     */
    private EntityDetail classify(OMRSMetadataCollection metadataCollection,
                                  EntityDetail           entity) throws Exception
    {
        return this.classify(metadataCollection, entity, classificationDef);
    }


    /**
     * Attach a named classification to an entity.
     *
     * @param metadataCollection repository under test
     * @param entity entity to classify
     * @param classificationToAttach the classification
     * @return the classified entity
     * @throws Exception the entity could not be classified
     */
    private EntityDetail classify(OMRSMetadataCollection metadataCollection,
                                  EntityDetail           entity,
                                  ClassificationDef      classificationToAttach) throws Exception
    {
        InstanceProperties classificationProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(),
                                                                                       classificationToAttach);

        return metadataCollection.classifyEntity(workPad.getLocalServerUserId(),
                                                 entity.getGUID(),
                                                 classificationToAttach.getName(),
                                                 classificationProperties);
    }


    /**
     * Return the GUIDs held in a result list.
     *
     * @param results what the repository returned
     * @return set of GUIDs
     */
    private Set<String> guidsOf(List<EntityDetail> results)
    {
        Set<String> guids = new HashSet<>();

        if (results != null)
        {
            for (EntityDetail result : results)
            {
                if (result != null)
                {
                    guids.add(result.getGUID());
                }
            }
        }

        return guids;
    }


    /**
     * Remove everything this test created.  Failures are ignored - the instances may already be gone, and a
     * problem tidying up should not be reported as a conformance failure of the repository's search.
     *
     * @param metadataCollection repository under test
     */
    private void cleanUp(OMRSMetadataCollection metadataCollection)
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
}
