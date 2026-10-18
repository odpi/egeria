/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

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
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventEvidenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventForTimelineEffectsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TimeKeeperFVT covers every request of the Time Keeper OMVS: the lifecycle of context events, the five
 * relationships a context event takes part in (dependent and related context events, evidence, impacts and
 * timeline effects), and the retrieval requests.  Each attach is checked from the other end of the relationship
 * using the Metadata Expert's generic relationship query, and each detach is checked the same way.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimeKeeperFVT
{
    private static final ViewServiceClient timeKeeper = new ViewServiceClient(ViewServiceDescription.TIME_KEEPER);

    private static final String DESCRIPTION         = "Context event created by " + TimeKeeperFVT.class.getSimpleName();
    private static final String UPDATED_DESCRIPTION = "Context event updated by " + TimeKeeperFVT.class.getSimpleName();

    private static String outageQualifiedName = null;
    private static String outageGUID          = null;
    private static String rootCauseGUID       = null;
    private static String evidenceGUID        = null;
    private static String copyGUID            = null;


    /**
     * Create a context event and read it back with its properties.
     */
    @Test
    @Order(1)
    void createAndRetrieveContextEvent()
    {
        outageQualifiedName = ViewServicesFvtTestSupport.qualifiedName("ContextEvent", "outage");
        outageGUID          = createContextEvent(outageQualifiedName, "Network outage");
        rootCauseGUID       = createContextEvent(ViewServicesFvtTestSupport.qualifiedName("ContextEvent", "root-cause"), "Router failure");
        evidenceGUID        = createContextEvent(ViewServicesFvtTestSupport.qualifiedName("ContextEvent", "evidence"), "Monitoring alert");

        ContextEventProperties properties = retrieve(outageGUID);

        assertEquals(outageQualifiedName, properties.getQualifiedName());
        assertEquals("Network outage", properties.getDisplayName());
        assertEquals(DESCRIPTION, properties.getDescription());
        assertEquals("unavailable", properties.getEventEffect());
        assertEquals(30L, properties.getPlannedDuration());
    }


    /**
     * A merge update changes only the properties it supplies.
     */
    @Test
    @Order(2)
    void updateContextEvent()
    {
        ContextEventProperties properties = new ContextEventProperties();

        properties.setDescription(UPDATED_DESCRIPTION);
        properties.setActualDuration(45L);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        timeKeeper.post("/context-events/" + outageGUID + "/update", requestBody, BooleanResponse.class);

        ContextEventProperties updated = retrieve(outageGUID);

        assertEquals(UPDATED_DESCRIPTION, updated.getDescription());
        assertEquals(45L, updated.getActualDuration());
        assertEquals("Network outage", updated.getDisplayName(), "a merge update lost the display name");
        assertEquals(outageQualifiedName, updated.getQualifiedName());
    }


    /**
     * A context event created from a template copies the template's properties, with the replacement
     * properties overriding them.
     */
    @Test
    @Order(3)
    void createContextEventFromTemplate()
    {
        String copyQualifiedName = ViewServicesFvtTestSupport.qualifiedName("ContextEvent", "copy");

        ContextEventProperties replacementProperties = new ContextEventProperties();

        replacementProperties.setQualifiedName(copyQualifiedName);
        replacementProperties.setDisplayName("Copied outage");

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(outageGUID);
        requestBody.setReplacementProperties(replacementProperties);

        copyGUID = timeKeeper.create("/context-events/from-template", requestBody);

        assertNotEquals(outageGUID, copyGUID);

        ContextEventProperties copy = retrieve(copyGUID);

        assertEquals(copyQualifiedName, copy.getQualifiedName());
        assertEquals("Copied outage", copy.getDisplayName());
        assertEquals("unavailable", copy.getEventEffect(), "the template's eventEffect was not copied");
    }


    /**
     * Retrieve by name finds exactly the event with that qualified name; a search finds all of this run's
     * events.
     */
    @Test
    @Order(4)
    void findContextEvents()
    {
        FilterRequestBody filterRequestBody = new FilterRequestBody();

        filterRequestBody.setFilter(outageQualifiedName);

        List<OpenMetadataRootElement> byName = timeKeeper.elements("/context-events/by-name", filterRequestBody);

        assertEquals(1, byName.size(), "by-name should return just the outage event");
        assertEquals(outageGUID, byName.get(0).getElementHeader().getGUID());

        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::ContextEvent::");

        List<String> found = timeKeeper.elements("/context-events/by-search-string", searchRequestBody)
                                       .stream().map(element -> element.getElementHeader().getGUID()).toList();

        assertTrue(found.containsAll(List.of(outageGUID, rootCauseGUID, evidenceGUID, copyGUID)),
                   "by-search-string missed some of this run's context events: " + found);
    }


    /**
     * Each of the context event relationships can be attached, is visible from the other end, and can be
     * detached.
     */
    @Test
    @Order(5)
    void attachAndDetachRelationships()
    {
        DependentContextEventProperties dependentProperties = new DependentContextEventProperties();
        dependentProperties.setLabel("caused by");

        attachThenDetach("/context-events/" + rootCauseGUID + "/dependent-context-events/" + outageGUID,
                         dependentProperties,
                         rootCauseGUID,
                         outageGUID,
                         OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName);

        RelatedContextEventProperties relatedProperties = new RelatedContextEventProperties();
        relatedProperties.setConfidence(80);

        attachThenDetach("/context-events/" + outageGUID + "/related-context-events/" + copyGUID,
                         relatedProperties,
                         outageGUID,
                         copyGUID,
                         OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName);

        ContextEventEvidenceProperties evidenceProperties = new ContextEventEvidenceProperties();
        evidenceProperties.setDescription("Alert raised at the start of the outage");

        attachThenDetach("/context-events/" + outageGUID + "/evidence/" + evidenceGUID,
                         evidenceProperties,
                         outageGUID,
                         evidenceGUID,
                         OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP.typeName);

        ContextEventImpactProperties impactProperties = new ContextEventImpactProperties();
        impactProperties.setSeverityLevel(3);

        attachThenDetach("/context-events/" + outageGUID + "/impacts/" + evidenceGUID,
                         impactProperties,
                         outageGUID,
                         evidenceGUID,
                         OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName);

        /*
         * The timeline effect requests differ: the attach path says "context-events-describing-timeline-effects"
         * and the detach path "context-events-describing-timeline-effect".
         */
        ContextEventForTimelineEffectsProperties timelineProperties = new ContextEventForTimelineEffectsProperties();
        timelineProperties.setLabel("explains the gap");

        NewRelationshipRequestBody attachRequestBody = new NewRelationshipRequestBody();
        attachRequestBody.setProperties(timelineProperties);

        timeKeeper.call("/elements/" + evidenceGUID + "/context-events-describing-timeline-effects/" + outageGUID + "/attach", attachRequestBody);

        String timelineType = OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP.typeName;

        assertTrue(MetadataExpertProbe.linkedGUIDs(outageGUID, timelineType).contains(evidenceGUID),
                   "attach did not create a " + timelineType + " relationship");

        timeKeeper.call("/elements/" + evidenceGUID + "/context-events-describing-timeline-effect/" + outageGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(MetadataExpertProbe.linkedGUIDs(outageGUID, timelineType).contains(evidenceGUID),
                    "detach did not remove the " + timelineType + " relationship");
    }


    /**
     * Delete every context event this test created.
     */
    @Test
    @Order(6)
    void deleteContextEvents()
    {
        for (String guid : List.of(copyGUID, evidenceGUID, rootCauseGUID, outageGUID))
        {
            timeKeeper.call("/context-events/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(timeKeeper, "/context-events/" + guid + "/retrieve");
        }
    }


    private static String createContextEvent(String qualifiedName,
                                             String displayName)
    {
        ContextEventProperties properties = new ContextEventProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(displayName);
        properties.setDescription(DESCRIPTION);
        properties.setEventEffect("unavailable");
        properties.setPlannedStartDate(new Date());
        properties.setPlannedDuration(30L);

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return timeKeeper.create("/context-events", requestBody);
    }


    private static ContextEventProperties retrieve(String contextEventGUID)
    {
        OpenMetadataRootElement element = timeKeeper.element("/context-events/" + contextEventGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "context event " + contextEventGUID + " is missing");
        assertEquals(contextEventGUID, element.getElementHeader().getGUID());
        assertTrue(element.getProperties() instanceof ContextEventProperties,
                   "retrieve returned " + element.getProperties());

        return (ContextEventProperties) element.getProperties();
    }


    private static void attachThenDetach(String                 pathPrefix,
                                         RelationshipProperties properties,
                                         String                 end1GUID,
                                         String                 end2GUID,
                                         String                 relationshipTypeName)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        timeKeeper.call(pathPrefix + "/attach", requestBody);

        assertTrue(MetadataExpertProbe.linkedGUIDs(end1GUID, relationshipTypeName).contains(end2GUID),
                   pathPrefix + "/attach did not create a " + relationshipTypeName + " relationship");
        assertTrue(MetadataExpertProbe.linkedGUIDs(end2GUID, relationshipTypeName).contains(end1GUID),
                   pathPrefix + "/attach: the " + relationshipTypeName + " relationship is not visible from the other end");

        timeKeeper.call(pathPrefix + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(MetadataExpertProbe.linkedGUIDs(end1GUID, relationshipTypeName).contains(end2GUID),
                    pathPrefix + "/detach did not remove the " + relationshipTypeName + " relationship");
    }
}
