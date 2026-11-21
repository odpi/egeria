/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.AssessmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.DecisionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.OutcomeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.guids;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.newElement;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.search;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.template;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.updateElement;

/**
 * GovernanceOfficerDecisionsFVT covers the Governance Officer OMVS requests for decisions (model 0018): the
 * lifecycle of a Decision, the Outcome relationship to the elements that result from it, and the Assessment
 * relationship from the elements that were assessed to make it.
 * <br>
 * Outcome is uni-link, so it is attached and detached using the unique identifiers of its two ends.  Assessment
 * is multi-link: each attach creates another relationship between the same two elements, and the update and
 * detach requests work with the relationship's own unique identifier.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GovernanceOfficerDecisionsFVT
{
    private static final ViewServiceClient governanceOfficer = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_OFFICER);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static final List<String> decisionsToDelete = new ArrayList<>();

    private static String decisionGUID = null;
    private static String subjectGUID  = null;
    private static String outcomeGUID  = null;


    /**
     * Create the element that is assessed and the element that is the outcome of the decision.
     */
    @BeforeAll
    static void createElements()
    {
        subjectGUID = createServer("assessed-server", "Assessed server");
        outcomeGUID = createServer("replacement-server", "Replacement server");
    }


    /**
     * A decision is created, read back, found by name and by search string, updated, and copied from a template.
     */
    @Test
    @Order(1)
    void decisionLifecycle()
    {
        Date startTime      = new Date(1800000000000L);
        Date nextReviewTime = new Date(1900000000000L);
        Date expiryTime     = new Date(2000000000000L);

        DecisionProperties properties = new DecisionProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Decision", "replace-the-server"));
        properties.setDisplayName("Replace the server");
        properties.setSummary("The server is to be replaced");
        properties.setScope("Data centre");
        properties.setUsage("Use when planning the data centre refresh");
        properties.setImportance("high");
        properties.setDisposition("proposed");
        properties.setImplications(List.of("Budget is needed", "Downtime must be scheduled"));
        properties.setNotes("The server is out of support");
        properties.setStartTime(startTime);
        properties.setNextReviewTime(nextReviewTime);
        properties.setExpiryTime(expiryTime);

        decisionGUID = governanceOfficer.create("/decisions", newElement(properties));
        decisionsToDelete.add(decisionGUID);

        OpenMetadataRootElement decision = decision(decisionGUID);

        assertEquals(OpenMetadataType.DECISION.typeName, decision.getElementHeader().getType().getTypeName());

        DecisionProperties retrieved = assertInstanceOf(DecisionProperties.class, decision.getProperties());

        assertEquals("The server is to be replaced", retrieved.getSummary());
        assertEquals("Data centre", retrieved.getScope());
        assertEquals("Use when planning the data centre refresh", retrieved.getUsage());
        assertEquals("high", retrieved.getImportance());
        assertEquals("proposed", retrieved.getDisposition());
        assertEquals(List.of("Budget is needed", "Downtime must be scheduled"), retrieved.getImplications());
        assertEquals("The server is out of support", retrieved.getNotes());
        assertEquals(startTime, retrieved.getStartTime());
        assertEquals(nextReviewTime, retrieved.getNextReviewTime());
        assertEquals(expiryTime, retrieved.getExpiryTime());

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(decisionGUID), List.copyOf(guids(governanceOfficer.elements("/decisions/by-name", byName))));
        assertTrue(guids(governanceOfficer.elements("/decisions/by-search-string", search())).contains(decisionGUID));

        Date lastReviewTime = new Date(1850000000000L);

        DecisionProperties update = new DecisionProperties();

        update.setDisposition("approved");
        update.setLastReviewTime(lastReviewTime);

        assertTrue(governanceOfficer.post("/decisions/" + decisionGUID + "/update", updateElement(update), BooleanResponse.class).getFlag(),
                   "the update did not change the decision");

        DecisionProperties updated = assertInstanceOf(DecisionProperties.class, decision(decisionGUID).getProperties());

        assertEquals("approved", updated.getDisposition());
        assertEquals(lastReviewTime, updated.getLastReviewTime());
        assertEquals("Replace the server", updated.getDisplayName(), "a merge update should keep the display name");
        assertEquals(expiryTime, updated.getExpiryTime(), "a merge update should keep the expiry time");

        DecisionProperties replacement = new DecisionProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Decision", "replace-the-server-copy"));

        String copyGUID = governanceOfficer.create("/decisions/from-template", template(decisionGUID, replacement));
        decisionsToDelete.add(copyGUID);

        assertNotEquals(decisionGUID, copyGUID);

        OpenMetadataRootElement copy = decision(copyGUID);

        assertEquals(OpenMetadataType.DECISION.typeName, copy.getElementHeader().getType().getTypeName());

        DecisionProperties copied = assertInstanceOf(DecisionProperties.class, copy.getProperties());

        assertEquals(replacement.getQualifiedName(), copied.getQualifiedName());
        assertEquals("approved", copied.getDisposition(), "the copy should take the template's disposition");
    }


    /**
     * The replacement server is an outcome of the decision.  The Outcome relationship and its properties are
     * visible from both ends until it is detached.
     */
    @Test
    @Order(2)
    void outcome()
    {
        String typeName = OpenMetadataType.OUTCOME_RELATIONSHIP.typeName;

        OutcomeProperties properties = new OutcomeProperties();

        properties.setLabel("replacement");
        properties.setDescription("The server that replaces the old one");
        properties.setImportance("high");
        properties.setDisposition("ordered");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        governanceOfficer.call("/decisions/" + decisionGUID + "/outcomes/" + outcomeGUID + "/attach", link);

        RelatedMetadataElementSummary fromDecision = RelatedElementChecks.relationship(decision(decisionGUID), typeName, outcomeGUID);

        assertNotNull(fromDecision, "the decision does not show its outcome");
        assertTrue(decision(decisionGUID).getDecisionOutcomes().stream().anyMatch(summary -> outcomeGUID.equals(summary.getRelatedElement().getElementHeader().getGUID())),
                   "the outcome is not in the decision's decisionOutcomes");

        OutcomeProperties retrieved = assertInstanceOf(OutcomeProperties.class, fromDecision.getRelationshipProperties());

        assertEquals("replacement", retrieved.getLabel());
        assertEquals("The server that replaces the old one", retrieved.getDescription());
        assertEquals("high", retrieved.getImportance());
        assertEquals("ordered", retrieved.getDisposition());

        assertTrue(asset(outcomeGUID).getAssociatedDecisions().stream().anyMatch(summary -> decisionGUID.equals(summary.getRelatedElement().getElementHeader().getGUID())),
                   "the decision is not in the outcome's associatedDecisions");

        governanceOfficer.call("/decisions/" + decisionGUID + "/outcomes/" + outcomeGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(decision(decisionGUID), typeName, outcomeGUID);
        assertNotLinked(asset(outcomeGUID), typeName, decisionGUID);
    }


    /**
     * The old server is assessed twice for the same decision.  Assessment is multi-link, so each attach returns
     * a new relationship.  One is updated through its relationship GUID without touching the other, and each is
     * detached on its own.
     */
    @Test
    @Order(3)
    void assessments()
    {
        String typeName = OpenMetadataType.ASSESSMENT_RELATIONSHIP.typeName;
        Date   firstTime  = new Date(1700000000000L);
        Date   secondTime = new Date(1750000000000L);
        Date   expiryTime = new Date(2000000000000L);

        String attachPath = "/elements/" + subjectGUID + "/assessments/" + decisionGUID + "/attach";

        AssessmentProperties first = new AssessmentProperties();

        first.setLabel("hardware review");
        first.setDescription("Review of the hardware's support status");
        first.setAssessor("garygeeke");
        first.setAssessorTypeName(OpenMetadataType.USER_IDENTITY.typeName);
        first.setAssessorPropertyName("userId");
        first.setAssessmentTime(firstTime);
        first.setExpiryTime(expiryTime);
        first.setNotes("Out of support since last year");
        first.setAdditionalProperties(Map.of("ticket", "INC-1"));

        NewRelationshipRequestBody firstLink = new NewRelationshipRequestBody();

        firstLink.setProperties(first);

        String firstGUID = governanceOfficer.create(attachPath, firstLink);

        AssessmentProperties second = new AssessmentProperties();

        second.setLabel("capacity review");
        second.setAssessmentTime(secondTime);

        NewRelationshipRequestBody secondLink = new NewRelationshipRequestBody();

        secondLink.setProperties(second);

        String secondGUID = governanceOfficer.create(attachPath, secondLink);

        assertNotEquals(firstGUID, secondGUID, "a multi-link attach should create a new relationship each time");

        assertEquals(Set.of(firstGUID, secondGUID), relationshipGUIDs(decision(decisionGUID).getInputsToDecision(), subjectGUID));
        assertEquals(Set.of(firstGUID, secondGUID), relationshipGUIDs(asset(subjectGUID).getAssessmentDecisions(), decisionGUID));

        AssessmentProperties retrieved = assertInstanceOf(AssessmentProperties.class, relationshipProperties(asset(subjectGUID), firstGUID));

        assertEquals("hardware review", retrieved.getLabel());
        assertEquals("Review of the hardware's support status", retrieved.getDescription());
        assertEquals("garygeeke", retrieved.getAssessor());
        assertEquals(OpenMetadataType.USER_IDENTITY.typeName, retrieved.getAssessorTypeName());
        assertEquals("userId", retrieved.getAssessorPropertyName());
        assertEquals(firstTime, retrieved.getAssessmentTime());
        assertEquals(expiryTime, retrieved.getExpiryTime());
        assertEquals("Out of support since last year", retrieved.getNotes());
        assertEquals(Map.of("ticket", "INC-1"), retrieved.getAdditionalProperties());

        AssessmentProperties update = new AssessmentProperties();

        update.setNotes("Support contract cannot be extended");

        UpdateRelationshipRequestBody updateBody = new UpdateRelationshipRequestBody();

        updateBody.setMergeUpdate(true);
        updateBody.setProperties(update);

        governanceOfficer.call("/assessments/" + firstGUID + "/update", updateBody);

        AssessmentProperties updated = assertInstanceOf(AssessmentProperties.class, relationshipProperties(asset(subjectGUID), firstGUID));

        assertEquals("Support contract cannot be extended", updated.getNotes());
        assertEquals("hardware review", updated.getLabel(), "a merge update should keep the label");
        assertEquals(firstTime, updated.getAssessmentTime(), "a merge update should keep the assessment time");

        AssessmentProperties untouched = assertInstanceOf(AssessmentProperties.class, relationshipProperties(asset(subjectGUID), secondGUID));

        assertEquals("capacity review", untouched.getLabel(), "updating one assessment should not change the other");
        assertEquals(secondTime, untouched.getAssessmentTime());

        governanceOfficer.call("/assessments/" + firstGUID + "/detach", new DeleteRelationshipRequestBody());

        assertEquals(Set.of(secondGUID), relationshipGUIDs(asset(subjectGUID).getAssessmentDecisions(), decisionGUID),
                     "detaching one assessment should leave the other in place");

        governanceOfficer.call("/assessments/" + secondGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(asset(subjectGUID), typeName, decisionGUID);
        assertNotLinked(decision(decisionGUID), typeName, subjectGUID);
    }


    /**
     * Delete everything this class created.
     */
    @AfterAll
    static void deleteElements()
    {
        for (String guid : decisionsToDelete)
        {
            governanceOfficer.call("/decisions/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(governanceOfficer, "/decisions/" + guid + "/retrieve");
        }

        for (String guid : new String[]{subjectGUID, outcomeGUID})
        {
            if (guid != null)
            {
                assetMaker.call("/assets/" + guid + "/delete", new DeleteElementRequestBody());
                ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + guid + "/retrieve");
            }
        }
    }


    private static String createServer(String name,
                                       String displayName)
    {
        SoftwareServerProperties properties = new SoftwareServerProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SoftwareServer", name));
        properties.setDisplayName(displayName);

        return assetMaker.create("/assets", newElement(properties));
    }


    /**
     * Return the unique identifiers of the relationships in the list that link to the element with the GUID.
     */
    private static Set<String> relationshipGUIDs(List<RelatedMetadataElementSummary> summaries,
                                                 String                              relatedGUID)
    {
        Set<String> relationshipGUIDs = new HashSet<>();

        if (summaries != null)
        {
            for (RelatedMetadataElementSummary summary : summaries)
            {
                if (relatedGUID.equals(summary.getRelatedElement().getElementHeader().getGUID()))
                {
                    relationshipGUIDs.add(summary.getRelationshipHeader().getGUID());
                }
            }
        }

        return relationshipGUIDs;
    }


    /**
     * Return the properties of the relationship with the given GUID, found from one of its ends.
     */
    private static RelationshipBeanProperties relationshipProperties(OpenMetadataRootElement element,
                                                                     String                  relationshipGUID)
    {
        for (RelatedMetadataElementSummary summary : ActorGovernanceFvtSupport.relationships(element))
        {
            if ((summary.getRelationshipHeader() != null) && relationshipGUID.equals(summary.getRelationshipHeader().getGUID()))
            {
                assertNotNull(summary.getRelationshipProperties(), "relationship " + relationshipGUID + " has no properties");

                return summary.getRelationshipProperties();
            }
        }

        throw new AssertionError(element.getElementHeader().getGUID() + " has no relationship " + relationshipGUID);
    }


    private static OpenMetadataRootElement decision(String guid)
    {
        OpenMetadataRootElement element = governanceOfficer.element("/decisions/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "decision " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement asset(String guid)
    {
        OpenMetadataRootElement element = assetMaker.element("/assets/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "asset " + guid + " is missing");

        return element;
    }
}
