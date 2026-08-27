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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.EndMatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test that a repository can constrain a relationship search by the entities at the ends of the relationship.
 * <br>
 * findRelationships() accepts end1EntityGUIDs, end2EntityGUIDs and an EndMatchCriteria, and this is the only
 * way to ask "which relationships connect these two elements" in one call.  Where it is not relied upon, the
 * code above the repository has to retrieve a broader set and discard the rest in memory - which costs a
 * larger read than the question needs, and, because the repository has already applied paging by the time
 * the discarding happens, returns short pages while matches remain unseen.
 * <br>
 * The test builds a small deliberate graph rather than reusing whatever instances exist, because the
 * assertions are about exact result sets:
 * <br>
 * <pre>
 *     end1A ---- r1 ----&gt; end2A
 *     end1A ---- r2 ----&gt; end2B
 *     end1B ---- r3 ----&gt; end2A
 *     end1B ---- r4 ----&gt; end2B
 * </pre>
 * That is enough to tell each constraint apart: end 1 alone selects r1 and r2, end 2 alone selects r1 and r3,
 * and the two together select r1 or all three depending on the match criteria.
 */
public class TestSupportedRelationshipEndCriteria extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-relationship-end-criteria";
    private static final String testCaseName = "Repository relationship end criteria test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " relationships can be found by the entity at end 1.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " relationships can be found by the entity at end 2.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " both ends together select only the relationship joining them.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " either end selects every relationship touching one of them.";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " page size is honoured when an end constraint is applied.";
    private static final String assertion6    = testCaseId + "-06";
    private static final String assertionMsg6 = " paging through an end-constrained search returns each relationship once.";
    private static final String assertion7    = testCaseId + "-07";
    private static final String assertionMsg7 = " neither end returns the relationships that touch neither of them.";
    private static final String assertion8    = testCaseId + "-08";
    private static final String assertionMsg8 = " relationship count agrees with the end-constrained search.";
    private static final String assertion9    = testCaseId + "-09";
    private static final String assertionMsg9 = " external relationship records its external provenance.";
    private static final String assertion10    = testCaseId + "-10";
    private static final String assertionMsg10 = " an end type the entity does belong to leaves the end guids selecting the same relationships.";
    private static final String assertion11    = testCaseId + "-11";
    private static final String assertionMsg11 = " an end type the entity does not belong to excludes the relationship.";
    private static final String assertion12    = testCaseId + "-12";
    private static final String assertionMsg12 = " an end 1 type on its own, with no end 1 guids, constrains the search.";
    private static final String assertion13    = testCaseId + "-13";
    private static final String assertionMsg13 = " an end 2 type on its own, with no end 2 guids, constrains the search.";
    private static final String assertion14    = testCaseId + "-14";
    private static final String assertionMsg14 = " an unrelated end type on its own excludes every relationship of this type.";
    private static final String assertion15    = testCaseId + "-15";
    private static final String assertionMsg15 = " relationship count honours an end type given without end guids.";
    private static final String assertion16    = testCaseId + "-16";
    private static final String assertionMsg16 = " guids and types on both ends together select the relationship joining them.";
    private static final String assertion17    = testCaseId + "-17";
    private static final String assertionMsg17 = " neither end, with only one end constrained, excludes just that end's relationships.";
    private static final String assertion18    = testCaseId + "-18";
    private static final String assertionMsg18 = " either end, with only one end constrained, selects just that end's relationships.";

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
     * @param relationshipDef the relationship type being tested
     */
    public TestSupportedRelationshipEndCriteria(RepositoryConformanceWorkPad workPad,
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
             * The repository does not support a type for one of the ends, so it cannot hold a relationship of
             * this type at all.  That is not a failure of end criteria support.
             */
            return;
        }

        EntityDetail end1A;
        EntityDetail end1B;
        EntityDetail end2A;
        EntityDetail end2B;

        Relationship r1;
        Relationship r2;
        Relationship r3;
        Relationship r4;

        try
        {
            end1A = this.createEntity(metadataCollection, end1TypeName);
            end1B = this.createEntity(metadataCollection, end1TypeName);
            end2A = this.createEntity(metadataCollection, end2TypeName);
            end2B = this.createEntity(metadataCollection, end2TypeName);

            r1 = this.createRelationship(metadataCollection, end1A, end2A);
            r2 = this.createRelationship(metadataCollection, end1A, end2B);
            r3 = this.createRelationship(metadataCollection, end1B, end2A);
            r4 = this.createRelationship(metadataCollection, end1B, end2B);
        }
        catch (FunctionNotSupportedException exception)
        {
            /*
             * The repository does not support the creation of instances, so there is nothing to search for.
             */
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());
            return;
        }

        try
        {
            /*
             * End 1 on its own.
             */
            long start = System.currentTimeMillis();
            List<Relationship> results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                                             relationshipDef.getGUID(),
                                                                             null,
                                                                             this.guids(end1A),
                                                                             null,
                                                                             EndMatchCriteria.BOTH,
                                                                             null,
                                                                             0,
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             0);
            long elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.sameRelationships(results, r1, r2),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * End 2 on its own.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          null,
                                                          this.guids(end2A),
                                                          EndMatchCriteria.BOTH,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.sameRelationships(results, r1, r3),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * Both ends together - the question the handlers actually ask: which relationships join these two?
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          this.guids(end1A),
                                                          this.guids(end2A),
                                                          EndMatchCriteria.BOTH,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.sameRelationships(results, r1),
                            assertion3,
                            testTypeName + assertionMsg3,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * Either end.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          this.guids(end1A),
                                                          this.guids(end2A),
                                                          EndMatchCriteria.ANY,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.sameRelationships(results, r1, r2, r3),
                            assertion4,
                            testTypeName + assertionMsg4,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * Paging with the end constraint applied.
             * <br>
             * This is the property the code above the repository cannot currently rely on.  When the end
             * filtering is done after the repository has paged, a caller asking for one relationship can be
             * handed none, and paging through the results either skips or repeats them.  Asking for one at a
             * time from a set of two is the smallest case that shows it.
             */
            start = System.currentTimeMillis();
            List<Relationship> firstPage = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                                               relationshipDef.getGUID(),
                                                                               null,
                                                                               this.guids(end1A),
                                                                               null,
                                                                               EndMatchCriteria.BOTH,
                                                                               null,
                                                                               0,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               1);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(((firstPage != null) && (firstPage.size() == 1)),
                            assertion5,
                            testTypeName + assertionMsg5,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            List<Relationship> secondPage = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                                                relationshipDef.getGUID(),
                                                                                null,
                                                                                this.guids(end1A),
                                                                                null,
                                                                                EndMatchCriteria.BOTH,
                                                                                null,
                                                                                1,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                1);

            boolean pagedCleanly = false;

            if ((firstPage != null) && (firstPage.size() == 1) && (secondPage != null) && (secondPage.size() == 1))
            {
                Set<String> pagedGUIDs = new HashSet<>();

                pagedGUIDs.add(firstPage.get(0).getGUID());
                pagedGUIDs.add(secondPage.get(0).getGUID());

                Set<String> expectedGUIDs = new HashSet<>();

                expectedGUIDs.add(r1.getGUID());
                expectedGUIDs.add(r2.getGUID());

                pagedCleanly = pagedGUIDs.equals(expectedGUIDs);
            }

            verifyCondition(pagedCleanly,
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

            /*
             * Neither end.
             * <br>
             * NONE returns everything except what matches - the negation of ANY - so this asserts by
             * containment rather than as an exact set: other relationships of this type may exist in the
             * repository and would legitimately be returned alongside r4.  What must hold is that the three
             * relationships touching a constrained entity are absent, and the one touching neither is present.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          this.guids(end1A),
                                                          this.guids(end2A),
                                                          EndMatchCriteria.NONE,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            Set<String> returnedGUIDs = new HashSet<>();

            if (results != null)
            {
                for (Relationship result : results)
                {
                    if (result != null)
                    {
                        returnedGUIDs.add(result.getGUID());
                    }
                }
            }

            boolean neitherEndCorrect = (returnedGUIDs.contains(r4.getGUID())) &&
                                        (! returnedGUIDs.contains(r1.getGUID())) &&
                                        (! returnedGUIDs.contains(r2.getGUID())) &&
                                        (! returnedGUIDs.contains(r3.getGUID()));

            verifyCondition(neitherEndCorrect,
                            assertion7,
                            testTypeName + assertionMsg7,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * countRelationships() takes the same criteria as findRelationships(), and the code above the
             * repository uses it to decide whether there is anything to fetch.  A count that disagrees with
             * the search is acted upon rather than noticed, so the two are asserted against each other on
             * criteria whose answer this test knows: the two relationships starting at end1A.
             */
            start = System.currentTimeMillis();
            long count = metadataCollection.countRelationships(workPad.getLocalServerUserId(),
                                                               relationshipDef.getGUID(),
                                                               null,
                                                               false,
                                                               this.guids(end1A),
                                                               null,
                                                               null,
                                                               null,
                                                               EndMatchCriteria.BOTH,
                                                               null,
                                                               0,
                                                               null,
                                                               null,
                                                               null,
                                                               null,
                                                               0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((count == 2),
                            assertion8,
                            testTypeName + assertionMsg8 + " (counted " + count + ", expected 2)",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "countRelationships",
                            elapsedTime);

            /*
             * The end constraints also accept the type of the entity that has to be at each end.  The type may
             * be given alongside the end guids, to narrow them, or on its own with the guids left null, which is
             * the only way to ask for "every relationship of this type that starts at an entity of that type"
             * without first fetching the entities.
             */
            String end1TypeGUID = entityDefs.get(end1TypeName).getGUID();
            String end2TypeGUID = entityDefs.get(end2TypeName).getGUID();

            /*
             * A type the end entity does belong to must not change the answer the guids already gave.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          false,
                                                          this.guids(end1A),
                                                          end1TypeGUID,
                                                          null,
                                                          null,
                                                          EndMatchCriteria.BOTH,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.sameRelationships(results, r1, r2),
                            assertion10,
                            testTypeName + assertionMsg10,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * An end type on its own, with the guids for that end left null, has to be honoured.  The other end
             * is pinned to a guid so that the expected result is exactly known whatever else the repository holds.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          false,
                                                          null,
                                                          end1TypeGUID,
                                                          this.guids(end2A),
                                                          null,
                                                          EndMatchCriteria.BOTH,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.sameRelationships(results, r1, r3),
                            assertion12,
                            testTypeName + assertionMsg12,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * The same again for end 2, so that neither end is left resting on the other's implementation.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          false,
                                                          this.guids(end1A),
                                                          null,
                                                          null,
                                                          end2TypeGUID,
                                                          EndMatchCriteria.BOTH,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.sameRelationships(results, r1, r2),
                            assertion13,
                            testTypeName + assertionMsg13,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * Every end constraint at once - a guid and a type on each end.  Each of the four has been
             * asserted on above in smaller combinations, so what this adds is that they compose: a
             * repository that builds its query by joining the parts together has to join exactly these
             * four, and getting the joining wrong is not visible until all four are present.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          false,
                                                          this.guids(end1A),
                                                          end1TypeGUID,
                                                          this.guids(end2A),
                                                          end2TypeGUID,
                                                          EndMatchCriteria.BOTH,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.sameRelationships(results, r1),
                            assertion16,
                            testTypeName + assertionMsg16,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * ANY and NONE with only one end constrained.
             * <br>
             * An end that carries no criteria has not been asked about, and an end that has not been asked
             * about takes no part in the decision.  Under BOTH there is no way to tell that apart from an
             * end that matches everything, which is why it is asserted here instead: if an unasked end
             * counted as a match, ANY would return every relationship of this type and NONE would return
             * none of them.
             * <br>
             * Both are asserted by containment, not as exact sets - other relationships of this type may
             * exist in the repository and would legitimately be returned alongside these.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          false,
                                                          this.guids(end1A),
                                                          null,
                                                          null,
                                                          null,
                                                          EndMatchCriteria.NONE,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((this.noneOfTheseRelationships(results, r1, r2)) && (this.allOfTheseRelationships(results, r3, r4)),
                            assertion17,
                            testTypeName + assertionMsg17,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          false,
                                                          this.guids(end1A),
                                                          null,
                                                          null,
                                                          null,
                                                          EndMatchCriteria.ANY,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((this.sameRelationships(results, r1, r2)),
                            assertion18,
                            testTypeName + assertionMsg18,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            /*
             * countRelationships() takes the end type too, and the code above the repository trusts it in the
             * same way it trusts the count on any other criteria.
             */
            start = System.currentTimeMillis();
            count = metadataCollection.countRelationships(workPad.getLocalServerUserId(),
                                                          relationshipDef.getGUID(),
                                                          null,
                                                          false,
                                                          null,
                                                          end1TypeGUID,
                                                          this.guids(end2A),
                                                          null,
                                                          EndMatchCriteria.BOTH,
                                                          null,
                                                          0,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition((count == 2),
                            assertion15,
                            testTypeName + assertionMsg15 + " (counted " + count + ", expected 2)",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "countRelationships",
                            elapsedTime);

            /*
             * A type the end entity does not belong to has to exclude the relationship.  Without this, a
             * repository that accepted the parameter and ignored it would pass every assertion above.  The
             * unrelated type is chosen so that no relationship of this type can hold one at end 1, which is
             * what makes the two searches below safe to assert on.
             */
            String unrelatedTypeName = this.findUnrelatedEntityType(end1TypeName,
                                                                    relationshipDef.getEndDef1().getEntityType().getName());

            if (unrelatedTypeName != null)
            {
                String unrelatedTypeGUID = entityDefs.get(unrelatedTypeName).getGUID();

                start = System.currentTimeMillis();
                results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                              relationshipDef.getGUID(),
                                                              null,
                                                              false,
                                                              this.guids(end1A),
                                                              unrelatedTypeGUID,
                                                              null,
                                                              null,
                                                              EndMatchCriteria.BOTH,
                                                              null,
                                                              0,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              0);
                elapsedTime = System.currentTimeMillis() - start;

                verifyCondition(this.sameRelationships(results),
                                assertion11,
                                testTypeName + assertionMsg11,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                                "findRelationships",
                                elapsedTime);

                /*
                 * The same type on its own, with no guids anywhere to bound the search, must still exclude
                 * every relationship this test created.  Other instances may exist, so this asserts that none
                 * of the four are returned rather than that nothing at all is.
                 */
                start = System.currentTimeMillis();
                results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                              relationshipDef.getGUID(),
                                                              null,
                                                              false,
                                                              null,
                                                              unrelatedTypeGUID,
                                                              null,
                                                              null,
                                                              EndMatchCriteria.BOTH,
                                                              null,
                                                              0,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              0);
                elapsedTime = System.currentTimeMillis() - start;

                verifyCondition(this.noneOfTheseRelationships(results, r1, r2, r3, r4),
                                assertion14,
                                testTypeName + assertionMsg14,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                                "findRelationships",
                                elapsedTime);
            }

            /*
             * A relationship created on behalf of an external source has to keep that provenance - it is what
             * later stops a local caller modifying metadata that belongs to somebody else.
             */
            String externalSourceGUID = java.util.UUID.randomUUID().toString();

            try
            {
                InstanceProperties externalProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(),
                                                                                         relationshipDef);

                start = System.currentTimeMillis();
                Relationship externalRelationship = metadataCollection.addExternalRelationship(workPad.getLocalServerUserId(),
                                                                                               relationshipDef.getGUID(),
                                                                                               externalSourceGUID,
                                                                                               "conformance-suite-external-source",
                                                                                               externalProperties,
                                                                                               end1B.getGUID(),
                                                                                               end2B.getGUID(),
                                                                                               null);
                elapsedTime = System.currentTimeMillis() - start;

                createdRelationships.add(externalRelationship);

                boolean provenanceRecorded = (externalRelationship != null) &&
                                             (externalRelationship.getInstanceProvenanceType() == InstanceProvenanceType.EXTERNAL_SOURCE) &&
                                             (externalSourceGUID.equals(externalRelationship.getMetadataCollectionId()));

                verifyCondition(provenanceRecorded,
                                assertion9,
                                testTypeName + assertionMsg9,
                                RepositoryConformanceProfileRequirement.STORE_EXTERNAL_RELATIONSHIPS.getProfileId(),
                                RepositoryConformanceProfileRequirement.STORE_EXTERNAL_RELATIONSHIPS.getRequirementId(),
                                "addExternalRelationship",
                                elapsedTime);
            }
            catch (FunctionNotSupportedException notSupported)
            {
                super.addNotSupportedAssertion(assertion9,
                                               assertionMsg9,
                                               RepositoryConformanceProfileRequirement.STORE_EXTERNAL_RELATIONSHIPS.getProfileId(),
                                               RepositoryConformanceProfileRequirement.STORE_EXTERNAL_RELATIONSHIPS.getRequirementId());
            }
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());
        }
        finally
        {
            this.cleanUp(metadataCollection);
        }

        super.setSuccessMessage("Relationships can be found by the entities at their ends");
    }


    /**
     * Return the first subtype of the given end type that the repository actually supports, or null if it
     * supports none of them.
     *
     * @param endDefTypeName type named by the relationship's end definition
     * @return supported type name or null
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
     * Create a relationship between two entities, and remember it for clean up.
     *
     * @param metadataCollection repository under test
     * @param end1 entity at end 1
     * @param end2 entity at end 2
     * @return new relationship
     * @throws Exception the relationship could not be created
     */
    private Relationship createRelationship(OMRSMetadataCollection metadataCollection,
                                            EntityDetail           end1,
                                            EntityDetail           end2) throws Exception
    {
        InstanceProperties relationshipProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef);

        Relationship newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                          relationshipDef.getGUID(),
                                                                          relationshipProperties,
                                                                          end1.getGUID(),
                                                                          end2.getGUID(),
                                                                          null);
        createdRelationships.add(newRelationship);

        return newRelationship;
    }


    /**
     * Find an entity type that an entity of the supplied type cannot be an instance of, so that it can be used
     * to show that an end type constraint is actually applied rather than accepted and ignored.  The candidate
     * has to be unrelated to the instance type in both directions - a supertype would still match, and a
     * subtype would make the search legitimately empty for an uninteresting reason.  It also has to be
     * unrelated to the type the relationship's end definition names, so that no relationship of this type
     * created by any other test can hold one at that end either.
     *
     * @param instanceTypeName type of the entity this test put at the end
     * @param endDefTypeName type the relationship's end definition names
     * @return name of an unrelated entity type, or null if this repository does not support one
     */
    private String findUnrelatedEntityType(String instanceTypeName,
                                           String endDefTypeName)
    {
        for (String candidateTypeName : entityDefs.keySet())
        {
            if ((candidateTypeName.equals(instanceTypeName)) || (candidateTypeName.equals(endDefTypeName)))
            {
                continue;
            }

            if ((this.isSubTypeOf(candidateTypeName, instanceTypeName)) ||
                (this.isSubTypeOf(instanceTypeName, candidateTypeName)) ||
                (this.isSubTypeOf(candidateTypeName, endDefTypeName)))
            {
                continue;
            }

            return candidateTypeName;
        }

        return null;
    }


    /**
     * Is the candidate type one of the supplied type's subtypes?
     *
     * @param candidateTypeName type that may be a subtype
     * @param superTypeName type that may be its supertype
     * @return true if it is
     */
    private boolean isSubTypeOf(String candidateTypeName,
                                String superTypeName)
    {
        List<String> subTypeNames = workPad.getEntitySubTypes(superTypeName);

        return (subTypeNames != null) && (subTypeNames.contains(candidateTypeName));
    }


    /**
     * Return a single entity's GUID as the list the search parameters take.
     *
     * @param entity entity to constrain on
     * @return list holding that entity's GUID
     */
    private List<String> guids(EntityDetail entity)
    {
        List<String> guidList = new ArrayList<>();

        guidList.add(entity.getGUID());

        return guidList;
    }


    /**
     * Compare a result set against the relationships it should hold - as a set, since the search says nothing
     * about the order they come back in, and no more and no less, since that is the whole point of the
     * constraint.
     *
     * @param results what the repository returned
     * @param expectedRelationships what it should have returned
     * @return true if the two hold the same relationships
     */
    private boolean sameRelationships(List<Relationship> results,
                                      Relationship...    expectedRelationships)
    {
        Set<String> expectedGUIDs = new HashSet<>();

        for (Relationship expectedRelationship : expectedRelationships)
        {
            expectedGUIDs.add(expectedRelationship.getGUID());
        }

        Set<String> returnedGUIDs = new HashSet<>();

        if (results != null)
        {
            for (Relationship result : results)
            {
                if (result != null)
                {
                    returnedGUIDs.add(result.getGUID());
                }
            }
        }

        return returnedGUIDs.equals(expectedGUIDs);
    }


    /**
     * Check that all of the supplied relationships appear in a result set.  The companion to
     * noneOfTheseRelationships(), for the same reason: the search is not bounded to this test's own
     * instances, so what can be asserted is that these are present, not that nothing else is.
     *
     * @param results what the repository returned
     * @param wantedRelationships the relationships that must be in it
     * @return true if all of them are present
     */
    private boolean allOfTheseRelationships(List<Relationship> results,
                                            Relationship...    wantedRelationships)
    {
        if (results == null)
        {
            return false;
        }

        Set<String> returnedGUIDs = new HashSet<>();

        for (Relationship result : results)
        {
            if (result != null)
            {
                returnedGUIDs.add(result.getGUID());
            }
        }

        for (Relationship wantedRelationship : wantedRelationships)
        {
            if (! returnedGUIDs.contains(wantedRelationship.getGUID()))
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Check that none of the supplied relationships appear in a result set.  This is the weaker companion to
     * sameRelationships(): it is used where the search is not bounded to this test's own instances, so the
     * repository may legitimately return other things, but must not return these.
     *
     * @param results what the repository returned
     * @param unwantedRelationships the relationships that must not be in it
     * @return true if none of them are present
     */
    private boolean noneOfTheseRelationships(List<Relationship> results,
                                             Relationship...    unwantedRelationships)
    {
        if (results == null)
        {
            return true;
        }

        Set<String> returnedGUIDs = new HashSet<>();

        for (Relationship result : results)
        {
            if (result != null)
            {
                returnedGUIDs.add(result.getGUID());
            }
        }

        for (Relationship unwantedRelationship : unwantedRelationships)
        {
            if (returnedGUIDs.contains(unwantedRelationship.getGUID()))
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Remove everything this test created.  Failures are ignored: the instances may already be gone, and a
     * problem tidying up should not be reported as a conformance failure of the repository's search.
     *
     * @param metadataCollection repository under test
     */
    private void cleanUp(OMRSMetadataCollection metadataCollection)
    {
        for (Relationship relationship : createdRelationships)
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
