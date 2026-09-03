/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The relationship counterpart of the entity effectivity conditions test: that a repository can select
 * relationships by their effectivity dates.
 * <br>
 * It exists separately rather than as more assertions on the entity test because of the last case below,
 * which cannot be expressed with entities at all.  The condition is built from two groups of nested
 * conditions, and each group carries no property, operator or value of its own - all it does is group.  A
 * repository that evaluates such a group by testing it against every property of the instance has nothing
 * to test when the instance has no properties, and can end up excluding it.
 * <br>
 * Entities cannot demonstrate that.  Most entity types carry a unique attribute - qualifiedName on
 * everything under Referenceable - and the repository refuses to create an instance of such a type with no
 * properties at all.  Relationships are the opposite: a great many open metadata relationship types define
 * no attributes whatsoever, so a bare link with no properties is the ordinary case rather than a corner one,
 * and every one of those instances is at risk.
 * <br>
 * The condition asserted here is the one a push-down of the effectivity window would use:
 * <pre>
 *     ALL of:
 *         ANY of: effectiveFromTime IS NULL, effectiveFromTime &lt;= T
 *         ANY of: effectiveToTime   IS NULL, effectiveToTime   &gt;  T
 * </pre>
 * Getting this wrong does not throw - it silently changes which relationships a caller sees.
 */
public class TestSupportedRelationshipEffectivityConditions extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-relationship-effectivity-conditions";
    private static final String testCaseName = "Repository relationship effectivity conditions test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " a relationship with no effectivity dates is in effect.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " a relationship whose effectivity has not started is not in effect.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " a relationship whose effectivity has ended is not in effect.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " a relationship within its effectivity window is in effect.";
    private static final String assertion5    = testCaseId + "-05";
    private static final String assertionMsg5 = " a relationship with no properties of its own is in effect.";

    private static final long ONE_HOUR = 60L * 60L * 1000L;

    private final RepositoryConformanceWorkPad workPad;
    private final Map<String, EntityDef>       entityDefs;
    private final RelationshipDef              relationshipDef;
    private final String                       testTypeName;

    private final List<EntityDetail> createdEntities      = new ArrayList<>();
    private final List<Relationship> createdRelationships = new ArrayList<>();


    /**
     * Set up the test.
     *
     * @param workPad working area for the conformance run
     * @param entityDefs the entity types supported by the repository under test
     * @param relationshipDef the relationship type being tested
     */
    public TestSupportedRelationshipEffectivityConditions(RepositoryConformanceWorkPad workPad,
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

        Date now       = new Date();
        Date anHourAgo = new Date(now.getTime() - ONE_HOUR);
        Date inAnHour  = new Date(now.getTime() + ONE_HOUR);

        Relationship openEnded;
        Relationship notYetStarted;
        Relationship alreadyEnded;
        Relationship currentlyInEffect;
        Relationship withoutProperties;

        try
        {
            openEnded         = this.createRelationship(metadataCollection, null, null, true);
            notYetStarted     = this.createRelationship(metadataCollection, inAnHour, null, true);
            alreadyEnded      = this.createRelationship(metadataCollection, null, anHourAgo, true);
            currentlyInEffect = this.createRelationship(metadataCollection, anHourAgo, inAnHour, true);
            withoutProperties = this.createRelationship(metadataCollection, null, null, false);
        }
        catch (FunctionNotSupportedException exception)
        {
            super.addNotSupportedAssertion(assertion1,
                                           assertionMsg1,
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                                           RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());
            return;
        }

        if (openEnded == null)
        {
            /*
             * The ends this relationship type needs are not among the types being tested, so there is
             * nothing to assert for it.
             */
            return;
        }

        try
        {
            long start = System.currentTimeMillis();
            List<Relationship> results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                                                                              relationshipDef.getGUID(),
                                                                              null,
                                                                              false,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              this.inEffectAt(now),
                                                                              0,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              0);
            long elapsedTime = System.currentTimeMillis() - start;

            verifyCondition(this.contains(results, openEnded),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId(),
                            "findRelationships",
                            elapsedTime);

            verifyCondition((! this.contains(results, notYetStarted)),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

            verifyCondition((! this.contains(results, alreadyEnded)),
                            assertion3,
                            testTypeName + assertionMsg3,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

            verifyCondition(this.contains(results, currentlyInEffect),
                            assertion4,
                            testTypeName + assertionMsg4,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());

            /*
             * Only asserted for types that define no attributes - for any other type the repository is
             * entitled to refuse an instance with no properties, and createRelationship returns null.
             */
            if (withoutProperties != null)
            {
                verifyCondition(this.contains(results, withoutProperties),
                                assertion5,
                                testTypeName + assertionMsg5,
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getProfileId(),
                                RepositoryConformanceProfileRequirement.RELATIONSHIP_CONDITION_SEARCH.getRequirementId());
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

        super.setSuccessMessage("Relationships can be selected by their effectivity dates");
    }


    /**
     * Create a relationship of the type under test with the given effectivity window, and remember it for
     * clean up.
     *
     * @param metadataCollection repository under test
     * @param effectiveFrom start of the window, or null for open ended
     * @param effectiveTo end of the window, or null for open ended
     * @param withProperties should the relationship carry the type's properties?
     * @return the new relationship, or null if it could not be built
     * @throws Exception the relationship could not be created
     */
    private Relationship createRelationship(OMRSMetadataCollection metadataCollection,
                                            Date                   effectiveFrom,
                                            Date                   effectiveTo,
                                            boolean                withProperties) throws Exception
    {
        String end1TypeName = this.getSupportedEndTypeName(relationshipDef.getEndDef1().getEntityType().getName());
        String end2TypeName = this.getSupportedEndTypeName(relationshipDef.getEndDef2().getEntityType().getName());

        if ((end1TypeName == null) || (end2TypeName == null))
        {
            return null;
        }

        EntityDetail end1 = this.createEntity(metadataCollection, end1TypeName);
        EntityDetail end2 = this.createEntity(metadataCollection, end2TypeName);

        InstanceProperties relationshipProperties;

        if (withProperties)
        {
            relationshipProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef);

            if (relationshipProperties == null)
            {
                relationshipProperties = new InstanceProperties();
            }
        }
        else
        {
            relationshipProperties = new InstanceProperties();
        }

        relationshipProperties.setEffectiveFromTime(effectiveFrom);
        relationshipProperties.setEffectiveToTime(effectiveTo);

        try
        {
            Relationship newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                              relationshipDef.getGUID(),
                                                                              relationshipProperties,
                                                                              end1.getGUID(),
                                                                              end2.getGUID(),
                                                                              null);
            createdRelationships.add(newRelationship);

            return newRelationship;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException error)
        {
            if (withProperties)
            {
                throw error;
            }

            /*
             * This type has a unique attribute, so an instance of it cannot have no properties.  There is
             * nothing to assert about the property-less case for this type.
             */
            return null;
        }
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
     * Build the search that asks for relationships in effect at a given moment.
     *
     * @param effectiveTime moment of interest
     * @return search properties expressing the effectivity window
     */
    private SearchProperties inEffectAt(Date effectiveTime)
    {
        List<PropertyCondition> topLevelConditions = new ArrayList<>();

        topLevelConditions.add(this.nested(this.anyOf(OpenMetadataProperty.EFFECTIVE_FROM_TIME.name,
                                                      PropertyComparisonOperator.LTE,
                                                      effectiveTime)));
        topLevelConditions.add(this.nested(this.anyOf(OpenMetadataProperty.EFFECTIVE_TO_TIME.name,
                                                      PropertyComparisonOperator.GT,
                                                      effectiveTime)));

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(topLevelConditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        return searchProperties;
    }


    /**
     * Return the "unset, or on the right side of the moment" group for one end of the window.  An unset date
     * is the open-ended case and has to be treated as satisfying the condition.
     *
     * @param propertyName header property holding the date
     * @param operator how a set date should compare to the moment
     * @param effectiveTime moment of interest
     * @return search properties matching either an unset date or a date that compares as required
     */
    private SearchProperties anyOf(String                     propertyName,
                                   PropertyComparisonOperator operator,
                                   Date                       effectiveTime)
    {
        PropertyCondition unset = new PropertyCondition();

        unset.setProperty(propertyName);
        unset.setOperator(PropertyComparisonOperator.IS_NULL);

        PrimitivePropertyValue dateValue = new PrimitivePropertyValue();

        dateValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        dateValue.setPrimitiveValue(effectiveTime.getTime());

        PropertyCondition compared = new PropertyCondition();

        compared.setProperty(propertyName);
        compared.setOperator(operator);
        compared.setValue(dateValue);

        List<PropertyCondition> eitherWay = new ArrayList<>();

        eitherWay.add(unset);
        eitherWay.add(compared);

        SearchProperties nestedConditions = new SearchProperties();

        nestedConditions.setConditions(eitherWay);
        nestedConditions.setMatchCriteria(MatchCriteria.ANY);

        return nestedConditions;
    }


    /**
     * Wrap a group of conditions as a single condition of the level above.  The wrapper carries no property,
     * operator or value of its own - grouping is all it does.
     *
     * @param nestedConditions the group
     * @return condition holding the group
     */
    private PropertyCondition nested(SearchProperties nestedConditions)
    {
        PropertyCondition condition = new PropertyCondition();

        condition.setNestedConditions(nestedConditions);

        return condition;
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
        if ((results == null) || (relationship == null))
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
    }
}
