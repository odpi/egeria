/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The relationship counterpart of the entity subtype search test: that a relationship search can be narrowed
 * to particular subtypes, and narrowed to everything <em>except</em> them.
 * <br>
 * The reason for testing the exclusion direction separately is the same one, and it is the whole point of
 * both test cases.  An inclusion list that is ignored returns too much, which gets noticed.  An exclusion list
 * that is ignored returns exactly the relationships the caller asked to leave out, and nothing about the
 * result set says so - a wrong answer that looks like a right one.  Only a test that knows which instances
 * must be absent can tell the difference.
 * <br>
 * A relationship needs entities at both ends, and a subtype may narrow what those ends accept, so this test
 * builds a separate pair of ends for each of the two relationship types rather than reusing one pair.
 */
public class TestSupportedRelationshipSubtypeSearch extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-relationship-subtype-search";
    private static final String testCaseName = "Repository relationship subtype search test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " a subtype inclusion list returns the subtype's relationships.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " a subtype inclusion list leaves out relationships of the supertype.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " a subtype exclusion list leaves out the subtype's relationships.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " a subtype exclusion list returns relationships of the supertype.";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " relationship count agrees with the subtype inclusion search.";
    private static final String assertion6    = testCaseId + "-06";
    private static final String assertionMsg6 = " relationship count agrees with the subtype exclusion search.";

    private final RepositoryConformanceWorkPad   workPad;
    private final Map<String, EntityDef>         entityDefs;
    private final RelationshipDef                relationshipDef;
    private final Map<String, RelationshipDef>   relationshipDefs;
    private final String                         testTypeName;

    private final List<EntityDetail> createdEntities      = new ArrayList<>();
    private final List<Relationship> createdRelationships = new ArrayList<>();


    /**
     * Set up the test case.
     *
     * @param workPad place for parameters and results
     * @param entityDefs the entity types supported by the repository under test
     * @param relationshipDef the relationship type being tested
     * @param relationshipDefs the relationship types supported by the repository under test
     */
    public TestSupportedRelationshipSubtypeSearch(RepositoryConformanceWorkPad workPad,
                                                  Map<String, EntityDef>       entityDefs,
                                                  RelationshipDef              relationshipDef,
                                                  Map<String, RelationshipDef> relationshipDefs)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
              RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

        this.workPad          = workPad;
        this.entityDefs       = entityDefs;
        this.relationshipDef  = relationshipDef;
        this.relationshipDefs = relationshipDefs;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(), testCaseId, testCaseName);
    }


    /**
     * Run the test.
     *
     * @throws Exception something went wrong that the conformance suite could not handle
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        RelationshipDef subtypeDef = this.findTestableSubtype();

        if (subtypeDef == null)
        {
            /*
             * No subtype of this relationship type is being tested, so there is nothing to narrow to.
             */
            return;
        }

        Relationship superTypeInstance = this.createRelationshipOfType(metadataCollection, relationshipDef);
        Relationship subTypeInstance   = this.createRelationshipOfType(metadataCollection, subtypeDef);

        if ((superTypeInstance == null) || (subTypeInstance == null))
        {
            /*
             * One of the two could not be created - most often because a supported entity type is not
             * available for one of its ends - so the comparison this test rests on cannot be made.
             */
            this.cleanUp(metadataCollection);
            return;
        }

        List<String> subtypeGUIDs = new ArrayList<>();
        subtypeGUIDs.add(subtypeDef.getGUID());

        try
        {
            long start = System.currentTimeMillis();
            List<Relationship> results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                                              relationshipDef.getGUID(),
                                                                              subtypeGUIDs,
                                                                              false,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              0,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              0);
            long elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.contains(results, subTypeInstance),
                            assertion1,
                            testTypeName + assertionMsg1 + " (subtype " + subtypeDef.getName() + ")",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            verifyCondition(! this.contains(results, superTypeInstance),
                            assertion2,
                            testTypeName + assertionMsg2 + " (subtype " + subtypeDef.getName() + ")",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            long inclusionCount = metadataCollection.countRelationships(workPad.getLocalServerUserId(),
                                                                         relationshipDef.getGUID(),
                                                                         subtypeGUIDs,
                                                                         false,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         0,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         0);

            verifyCondition((inclusionCount == this.size(results)),
                            assertion5,
                            testTypeName + assertionMsg5 + " (counted " + inclusionCount + ", found " + this.size(results) + ")",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "countRelationships",
                            elapsedTime);

            /*
             * The same list, asked the other way round - the assertion that catches an exclusion accepted and
             * ignored, because then these two results are the exact inverse of what was asked for.
             */
            start = System.currentTimeMillis();
            results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                           relationshipDef.getGUID(),
                                                           subtypeGUIDs,
                                                           true,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           0,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           0);
            elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(! this.contains(results, subTypeInstance),
                            assertion3,
                            testTypeName + assertionMsg3 + " (subtype " + subtypeDef.getName() + ")",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            verifyCondition(this.contains(results, superTypeInstance),
                            assertion4,
                            testTypeName + assertionMsg4 + " (subtype " + subtypeDef.getName() + ")",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            long exclusionCount = metadataCollection.countRelationships(workPad.getLocalServerUserId(),
                                                                         relationshipDef.getGUID(),
                                                                         subtypeGUIDs,
                                                                         true,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         0,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         0);

            verifyCondition((exclusionCount == this.size(results)),
                            assertion6,
                            testTypeName + assertionMsg6 + " (counted " + exclusionCount + ", found " + this.size(results) + ")",
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "countRelationships",
                            elapsedTime);
        }
        finally
        {
            this.cleanUp(metadataCollection);
        }

        super.setSuccessMessage("Relationship subtype searches can be narrowed by inclusion and by exclusion");
    }


    /**
     * Find a subtype of the relationship type under test that is itself being tested.
     *
     * @return the subtype, or null if none of this type's subtypes is in the run
     */
    private RelationshipDef findTestableSubtype()
    {
        List<String> subTypeNames = workPad.getRelationshipSubTypes(relationshipDef.getName());

        if (subTypeNames == null)
        {
            return null;
        }

        for (String subTypeName : subTypeNames)
        {
            RelationshipDef candidate = relationshipDefs.get(subTypeName);

            if ((candidate != null) && (! candidate.getName().equals(relationshipDef.getName())))
            {
                return candidate;
            }
        }

        return null;
    }


    /**
     * Create a relationship of the given type, with entities of its own end types.
     *
     * @param metadataCollection repository under test
     * @param typeToCreate relationship type to create
     * @return the new relationship, or null if its ends could not be built
     */
    private Relationship createRelationshipOfType(OMRSMetadataCollection metadataCollection,
                                                  RelationshipDef        typeToCreate)
    {
        String end1TypeName = this.getSupportedEndTypeName(typeToCreate.getEndDef1().getEntityType().getName());
        String end2TypeName = this.getSupportedEndTypeName(typeToCreate.getEndDef2().getEntityType().getName());

        if ((end1TypeName == null) || (end2TypeName == null))
        {
            return null;
        }

        try
        {
            EntityDetail end1 = this.createEntity(metadataCollection, end1TypeName);
            EntityDetail end2 = this.createEntity(metadataCollection, end2TypeName);

            InstanceProperties relationshipProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), typeToCreate);

            Relationship newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                              typeToCreate.getGUID(),
                                                                              relationshipProperties,
                                                                              end1.getGUID(),
                                                                              end2.getGUID(),
                                                                              null);
            createdRelationships.add(newRelationship);

            return newRelationship;
        }
        catch (FunctionNotSupportedException exception)
        {
            return null;
        }
        catch (Exception error)
        {
            return null;
        }
    }


    /**
     * Return a type that this repository supports for one end of a relationship - the type the end definition
     * names, or one of its subtypes if the named type itself is not being tested.
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
     * Is a particular relationship in a result set?  Asserted by containment rather than as an exact set,
     * because other test cases create instances of these same types.
     *
     * @param results what the repository returned
     * @param relationship the relationship to look for
     * @return true if it is there
     */
    private boolean contains(List<Relationship> results,
                             Relationship       relationship)
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

        return returnedGUIDs.contains(relationship.getGUID());
    }


    /**
     * Size of a result set, treating null as none.
     *
     * @param results what the repository returned
     * @return number of results
     */
    private long size(List<Relationship> results)
    {
        return (results == null) ? 0L : results.size();
    }


    /**
     * Remove everything this test created - relationships first, since an entity cannot be purged while a
     * relationship still reaches it.  Failures are ignored: a problem tidying up should not be reported as a
     * conformance failure of the repository's search.
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

        createdRelationships.clear();
        createdEntities.clear();
    }
}
