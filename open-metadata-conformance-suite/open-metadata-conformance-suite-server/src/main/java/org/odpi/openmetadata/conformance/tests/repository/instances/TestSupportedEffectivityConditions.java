/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test that a repository can select instances by their effectivity dates.
 * <br>
 * The code above the repository asks "which instances are in effect at this moment" constantly, and today it
 * answers that question in Java, after retrieval, by testing each instance's effectiveFromTime and
 * effectiveToTime.  Both repositories hold those two values as searchable header properties, so the question
 * can be asked of the repository instead - but only if the null cases behave, because an open-ended
 * effectivity is the normal case, not the exception.
 * <br>
 * The condition asserted here is the one such a push-down would use:
 * <pre>
 *     ALL of:
 *         ANY of: effectiveFromTime IS NULL, effectiveFromTime &lt;= T
 *         ANY of: effectiveToTime   IS NULL, effectiveToTime   &gt;  T
 * </pre>
 * Getting the null handling wrong here does not throw - it silently changes which elements a caller sees,
 * which is why this needs certifying before anything relies on it.
 */
public class TestSupportedEffectivityConditions extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-effectivity-conditions";
    private static final String testCaseName = "Repository effectivity conditions test case";

    private static final String assertion1    = testCaseId + "-01";
    private static final String assertionMsg1 = " an instance with no effectivity dates is in effect.";
    private static final String assertion2    = testCaseId + "-02";
    private static final String assertionMsg2 = " an instance whose effectivity has not started is not in effect.";
    private static final String assertion3    = testCaseId + "-03";
    private static final String assertionMsg3 = " an instance whose effectivity has ended is not in effect.";
    private static final String assertion4    = testCaseId + "-04";
    private static final String assertionMsg4 = " an instance within its effectivity window is in effect.";

    private static final long ONE_HOUR = 60L * 60L * 1000L;

    private final RepositoryConformanceWorkPad workPad;
    private final EntityDef                    entityDef;
    private final String                       testTypeName;

    private final List<EntityDetail> createdEntities = new ArrayList<>();


    /**
     * Set up the test case.
     *
     * @param workPad place for parameters and results
     * @param entityDef the entity type being tested
     */
    public TestSupportedEffectivityConditions(RepositoryConformanceWorkPad workPad,
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
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        Date now       = new Date();
        Date anHourAgo = new Date(now.getTime() - ONE_HOUR);
        Date inAnHour  = new Date(now.getTime() + ONE_HOUR);

        EntityDetail openEnded;
        EntityDetail notYetStarted;
        EntityDetail alreadyEnded;
        EntityDetail currentlyInEffect;

        try
        {
            openEnded         = this.createEntity(metadataCollection, null, null);
            notYetStarted     = this.createEntity(metadataCollection, inAnHour, null);
            alreadyEnded      = this.createEntity(metadataCollection, null, anHourAgo);
            currentlyInEffect = this.createEntity(metadataCollection, anHourAgo, inAnHour);
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
            List<EntityDetail> results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                                                                          entityDef.getGUID(),
                                                                          null,
                                                                          false,
                                                                          this.inEffectAt(now),
                                                                          0,
                                                                          null,
                                                                          null,
                                                                          null,
                                                                          null,
                                                                          null,
                                                                          0);
            long elapsedTime = System.currentTimeMillis() - start;

            Set<String> returnedGUIDs = new HashSet<>();

            if (results != null)
            {
                for (EntityDetail result : results)
                {
                    if (result != null)
                    {
                        returnedGUIDs.add(result.getGUID());
                    }
                }
            }

            verifyCondition(returnedGUIDs.contains(openEnded.getGUID()),
                            assertion1,
                            testTypeName + assertionMsg1,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId(),
                            "findEntities",
                            elapsedTime);

            verifyCondition((! returnedGUIDs.contains(notYetStarted.getGUID())),
                            assertion2,
                            testTypeName + assertionMsg2,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

            verifyCondition((! returnedGUIDs.contains(alreadyEnded.getGUID())),
                            assertion3,
                            testTypeName + assertionMsg3,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());

            verifyCondition(returnedGUIDs.contains(currentlyInEffect.getGUID()),
                            assertion4,
                            testTypeName + assertionMsg4,
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getProfileId(),
                            RepositoryConformanceProfileRequirement.ENTITY_CONDITION_SEARCH.getRequirementId());
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

        super.setSuccessMessage("Instances can be selected by their effectivity dates");
    }


    /**
     * Build the search that asks for instances in effect at a given moment.
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

        List<PropertyCondition> conditions = new ArrayList<>();

        conditions.add(unset);
        conditions.add(compared);

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ANY);

        return searchProperties;
    }


    /**
     * Wrap a group of conditions so it can sit inside another group.
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
     * Create an entity with the given effectivity window, and remember it for clean up.
     *
     * @param metadataCollection repository under test
     * @param effectiveFrom start of the window, or null for open ended
     * @param effectiveTo end of the window, or null for open ended
     * @return new entity
     * @throws Exception the entity could not be created
     */
    private EntityDetail createEntity(OMRSMetadataCollection metadataCollection,
                                      Date                   effectiveFrom,
                                      Date                   effectiveTo) throws Exception
    {
        InstanceProperties entityProperties = this.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef);

        entityProperties.setEffectiveFromTime(effectiveFrom);
        entityProperties.setEffectiveToTime(effectiveTo);

        EntityDetail newEntity = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                                                              entityDef.getGUID(),
                                                              entityProperties,
                                                              null,
                                                              null);
        createdEntities.add(newEntity);

        return newEntity;
    }


    /**
     * Remove everything this test created.  Failures are ignored - a problem tidying up should not be
     * reported as a conformance failure of the repository's search.
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
