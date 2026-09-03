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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test that a search can be narrowed to particular subtypes of the type it names - and, just as importantly,
 * that it can be narrowed to everything <em>except</em> those subtypes.
 * <br>
 * The two directions are not equally easy to get wrong in a way anyone notices.  An inclusion list that is
 * ignored returns too much, which tends to be spotted.  An exclusion list that is ignored returns exactly the
 * instances the caller asked to leave out - the inverse of the question - and nothing about the result says so.
 * That failure is invisible without a test that knows which instances should be absent, which is what this
 * test case supplies.
 * <br>
 * It matters most where a request crosses a repository boundary. The REST connector sends an exclusion to the
 * remote repository <em>and</em> applies it again locally, precisely because it cannot tell from the results
 * whether the far end honoured it.  These assertions are what establish that the two routes agree.
 * <br>
 * The test needs a subtype of the type under test that is also being tested, so that instances of both exist
 * and one can be told from the other.  Where the model offers none it does nothing rather than failing: a
 * type with no subtypes in the run has no subtype filtering to verify.
 */
public class TestSupportedEntitySubtypeSearch extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-entity-subtype-search";
    private static final String testCaseName = "Repository entity subtype search test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " a subtype inclusion list returns the subtype's instances.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " a subtype inclusion list leaves out instances of the supertype.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " a subtype exclusion list leaves out the subtype's instances.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " a subtype exclusion list returns instances of the supertype.";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " entity count agrees with the answerable part of the subtype inclusion search.";
    private static final String assertion6    = testCaseId + "-06";
    private static final String assertionMsg6 = " entity count agrees with the answerable part of the subtype exclusion search.";

    private final RepositoryConformanceWorkPad workPad;
    private final EntityDef                    entityDef;
    private final Map<String, EntityDef>       entityDefs;
    private final String                       testTypeName;

    private final List<EntityDetail> createdEntities = new ArrayList<>();


    /**
     * Set up the test case.
     *
     * @param workPad place for parameters and results
     * @param entityDef the entity type being tested
     * @param entityDefs the entity types supported by the repository under test
     */
    public TestSupportedEntitySubtypeSearch(RepositoryConformanceWorkPad workPad,
                                            EntityDef                    entityDef,
                                            Map<String, EntityDef>       entityDefs)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

        this.workPad    = workPad;
        this.entityDef  = entityDef;
        this.entityDefs = entityDefs;

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

        EntityDef subtypeDef = this.findTestableSubtype();

        if (subtypeDef == null)
        {
            /*
             * No subtype of this type is being tested, so there is no subtype filtering to verify.
             */
            return;
        }

        List<String> subtypeGUIDs = new ArrayList<>();
        subtypeGUIDs.add(subtypeDef.getGUID());

        EntityDetail superTypeInstance;
        EntityDetail subTypeInstance;

        try
        {
            superTypeInstance = this.createEntity(metadataCollection, entityDef);
            subTypeInstance   = this.createEntity(metadataCollection, subtypeDef);
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
             * Naming the subtype includes its instances and only its instances.  Both assertions are needed:
             * a repository that ignored the list entirely would pass the first on its own, because everything
             * would be returned.
             */
            long start = System.currentTimeMillis();
            List<EntityDetail> results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                                         entityDef.getGUID(),
                                                                         subtypeGUIDs,
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

            verifyCondition(this.contains(results, subTypeInstance),
                            assertion1,
                            testTypeName + assertionMsg1 + " (subtype " + subtypeDef.getName() + ")",
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);

            verifyCondition(! this.contains(results, superTypeInstance),
                            assertion2,
                            testTypeName + assertionMsg2 + " (subtype " + subtypeDef.getName() + ")",
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);

            long inclusionCount = metadataCollection.countEntities(workPad.getLocalServerUserId(),
                                                                    entityDef.getGUID(),
                                                                    subtypeGUIDs,
                                                                    false,
                                                                    null,
                                                                    0,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    0);

            verifyCondition((inclusionCount == this.countAnswerableFor(results)),
                            assertion5,
                            testTypeName + assertionMsg5 + " (counted " + inclusionCount + ", found " + this.countAnswerableFor(results) + ")",
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "countEntities",
                            elapsedTime);

            /*
             * The same list, asked the other way round.  This is the assertion that catches an exclusion that
             * was accepted and ignored: the results are then the exact opposite of what was asked for, and
             * nothing else in the result set gives that away.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                      entityDef.getGUID(),
                                                      subtypeGUIDs,
                                                      true,
                                                      null,
                                                      0,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(! this.contains(results, subTypeInstance),
                            assertion3,
                            testTypeName + assertionMsg3 + " (subtype " + subtypeDef.getName() + ")",
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);

            verifyCondition(this.contains(results, superTypeInstance),
                            assertion4,
                            testTypeName + assertionMsg4 + " (subtype " + subtypeDef.getName() + ")",
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);

            long exclusionCount = metadataCollection.countEntities(workPad.getLocalServerUserId(),
                                                                    entityDef.getGUID(),
                                                                    subtypeGUIDs,
                                                                    true,
                                                                    null,
                                                                    0,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    0);

            verifyCondition((exclusionCount == this.countAnswerableFor(results)),
                            assertion6,
                            testTypeName + assertionMsg6 + " (counted " + exclusionCount + ", found " + this.countAnswerableFor(results) + ")",
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "countEntities",
                            elapsedTime);
        }
        finally
        {
            this.cleanUp(metadataCollection);
        }

        super.setSuccessMessage("Entity subtype searches can be narrowed by inclusion and by exclusion");
    }


    /**
     * Find a subtype of the type under test that is itself being tested, so that instances of both exist and
     * a filter can tell them apart.
     *
     * @return the subtype, or null if none of this type's subtypes is in the run
     */
    private EntityDef findTestableSubtype()
    {
        List<String> subTypeNames = workPad.getEntitySubTypes(entityDef.getName());

        if (subTypeNames == null)
        {
            return null;
        }

        for (String subTypeName : subTypeNames)
        {
            EntityDef candidate = entityDefs.get(subTypeName);

            if ((candidate != null) && (! candidate.getName().equals(entityDef.getName())))
            {
                return candidate;
            }
        }

        return null;
    }


    /**
     * Create an entity of the given type, and remember it for clean up.
     *
     * @param metadataCollection repository under test
     * @param typeToCreate type of entity to create
     * @return new entity
     * @throws Exception the entity could not be created
     */
    private EntityDetail createEntity(OMRSMetadataCollection metadataCollection,
                                      EntityDef              typeToCreate) throws Exception
    {
        InstanceProperties entityProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), typeToCreate);

        EntityDetail newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                              typeToCreate.getGUID(),
                                                              entityProperties,
                                                              null,
                                                              null);
        createdEntities.add(newEntity);

        return newEntity;
    }


    /**
     * Is a particular entity in a result set?  Asserted by containment rather than as an exact set, because
     * other test cases create instances of these same types and would legitimately be returned alongside.
     *
     * @param results what the repository returned
     * @param entity the entity to look for
     * @return true if it is there
     */
    private boolean contains(List<EntityDetail> results,
                             EntityDetail       entity)
    {
        if (results == null)
        {
            return false;
        }

        Set<String> returnedGUIDs = new HashSet<>();

        for (EntityDetail result : results)
        {
            if (result != null)
            {
                returnedGUIDs.add(result.getGUID());
            }
        }

        return returnedGUIDs.contains(entity.getGUID());
    }


    /**
     * Number of a find()'s results that this repository is answerable for counting: those it homes, and those
     * it replicates on behalf of a non-cohort provenance.
     * <br><br>
     * find() and countEntities() are not asked quite the same question, and the difference is deliberate.  A
     * find is asked of one repository and answers with everything it holds that matches, reference copies
     * included, because the caller can see who answered and can deduplicate by GUID.  A count answers with a
     * number, and a federated count sums the numbers every cohort member gives back - so an instance held as a
     * reference copy by three members would be counted three times if each member counted its copies, and
     * nothing downstream can undo it because the GUIDs are gone.  So the count is compared against the part of
     * the result set this repository is answerable for, not against all of it.
     *
     * @param results what the repository returned
     * @return number of results this repository counts
     */
    private long countAnswerableFor(List<EntityDetail> results)
    {
        if (results == null)
        {
            return 0L;
        }

        String tutMetadataCollectionId = workPad.getTutMetadataCollectionId();
        long   answerableFor           = 0L;

        for (EntityDetail result : results)
        {
            if (result != null)
            {
                if ((tutMetadataCollectionId == null) ||
                    (tutMetadataCollectionId.equals(result.getMetadataCollectionId())) ||
                    (tutMetadataCollectionId.equals(result.getReplicatedBy())))
                {
                    answerableFor++;
                }
            }
        }

        return answerableFor;
    }


    /**
     * Remove everything this test created.  Failures are ignored: the instances may already be gone, and a
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

        createdEntities.clear();
    }
}
