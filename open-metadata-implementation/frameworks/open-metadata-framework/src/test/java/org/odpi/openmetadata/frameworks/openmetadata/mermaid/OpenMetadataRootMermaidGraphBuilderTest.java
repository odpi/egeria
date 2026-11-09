/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OpenMetadataRelationshipPurpose;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * OpenMetadataRootMermaidGraphBuilderTest covers how the relationships of an OpenMetadataRootElement are split
 * between its three flowcharts by the purpose declared for each relationship type.
 * <br><br>
 * The context graph (mermaidGraph) must leave out Action and Activity relationships, at every depth, so that it
 * does not grow as governance actions and activity records accumulate.  Those relationships are drawn in
 * actionMermaidGraph and activityMermaidGraph instead, each of which shows only the relationships of its own
 * purpose that are attached to the root element - plus whatever is nested below them.
 */
public class OpenMetadataRootMermaidGraphBuilderTest
{
    private static final int MAX_NODES = 50;


    /**
     * For every relationship purpose, a relationship of a type declaring that purpose lands in exactly one graph:
     * the action graph for Action, the activity graph for Activity, and the context graph for everything else.
     */
    @Test
    public void eachPurposeLandsInExactlyTheRightGraph()
    {
        List<RelatedMetadataElementSummary> relationships = new ArrayList<>();

        for (OpenMetadataRelationshipPurpose purpose : OpenMetadataRelationshipPurpose.values())
        {
            relationships.add(related(relationshipTypeWithPurpose(purpose).typeName, "Target " + purpose.name()));
        }

        OpenMetadataRootElement rootElement = rootElement();
        rootElement.setCollectionMembers(relationships);

        String context  = build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.CONTEXT);
        String action   = build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.ACTION);
        String activity = build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.ACTIVITY);

        assertNotNull(context);
        assertNotNull(action);
        assertNotNull(activity);

        for (OpenMetadataRelationshipPurpose purpose : OpenMetadataRelationshipPurpose.values())
        {
            String target = "Target " + purpose.name();

            boolean inContext  = context.contains(target);
            boolean inAction   = action.contains(target);
            boolean inActivity = activity.contains(target);

            assertEquals(inContext, (purpose != OpenMetadataRelationshipPurpose.ACTION) && (purpose != OpenMetadataRelationshipPurpose.ACTIVITY),
                         purpose + " in context graph:\n" + context);
            assertEquals(inAction, purpose == OpenMetadataRelationshipPurpose.ACTION,
                         purpose + " in action graph:\n" + action);
            assertEquals(inActivity, purpose == OpenMetadataRelationshipPurpose.ACTIVITY,
                         purpose + " in activity graph:\n" + activity);
        }

        assertTrue(action.contains("Root Element"), "the root element anchors the action graph:\n" + action);
        assertTrue(activity.contains("Root Element"), "the root element anchors the activity graph:\n" + activity);
        assertTrue(action.contains("title: Asset Actions - Root Element"), action);
        assertTrue(activity.contains("title: Asset Activity - Root Element"), activity);
    }


    /**
     * The context graph leaves out Action and Activity relationships found among nested elements and side links,
     * not just those attached directly to the root element.
     */
    @Test
    public void contextGraphExcludesNestedActionsAndActivity()
    {
        RelatedMetadataHierarchySummary member = hierarchy(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, "Member");

        member.setNestedElements(Arrays.asList(related(OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName, "Nested Action"),
                                               related(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, "Nested Member")));
        member.setSideLinks(List.of(related(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName, "Side Activity")));

        OpenMetadataRootElement rootElement = rootElement();
        rootElement.setCollectionMembers(List.of(member));

        String context = build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.CONTEXT);

        assertNotNull(context);
        assertTrue(context.contains("Member"), context);
        assertTrue(context.contains("Nested Member"), context);
        assertFalse(context.contains("Nested Action"), context);
        assertFalse(context.contains("Side Activity"), context);

        /*
         * Neither graph picks up actions or activity that hang off a structural member - only those
         * attached to the root element itself.
         */
        assertNull(build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.ACTION));
        assertNull(build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.ACTIVITY));
    }


    /**
     * Elements nested below an action relationship are drawn in the action graph whatever their own purpose.
     */
    @Test
    public void actionGraphShowsElementsNestedBelowAnAction()
    {
        RelatedMetadataHierarchySummary action = hierarchy(OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName, "Monitor");

        action.setNestedElements(List.of(related(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName, "Below Monitor")));

        OpenMetadataRootElement rootElement = rootElement();
        rootElement.setMonitoredResources(List.of(action));

        String actionGraph = build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.ACTION);

        assertNotNull(actionGraph);
        assertTrue(actionGraph.contains("Monitor"), actionGraph);
        assertTrue(actionGraph.contains("Below Monitor"), actionGraph);

        assertNull(build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.CONTEXT),
                   "a root element with only action relationships has no context graph");
        assertNull(build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.ACTIVITY),
                   "activity nested below an action belongs to the action graph");
    }


    /**
     * A relationship type with no declared purpose of its own (for example, one from an extension archive)
     * takes the purpose of its nearest super type.  One with no known purpose at all stays in the context graph.
     */
    @Test
    public void purposeIsInheritedFromSuperTypes()
    {
        ElementType extensionType = type("MyActionTargetExtension", OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName);

        assertEquals(MermaidGraphBuilderBase.getRelationshipPurpose(extensionType),
                     OpenMetadataType.ACTION_TARGET_RELATIONSHIP.relationshipPurpose);
        assertNull(MermaidGraphBuilderBase.getRelationshipPurpose(type("UnknownRelationship")));

        RelatedMetadataElementSummary extension = related(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName, "Extension Target");
        extension.getRelationshipHeader().setType(extensionType);

        OpenMetadataRootElement rootElement = rootElement();
        rootElement.setCollectionMembers(List.of(extension, related("UnknownRelationship", "Unknown Target")));

        String context  = build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.CONTEXT);
        String activity = build(rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope.ACTIVITY);

        assertNotNull(context);
        assertNotNull(activity);
        assertTrue(context.contains("Unknown Target"), context);
        assertFalse(context.contains("Extension Target"), context);
        assertTrue(activity.contains("Extension Target"), activity);
        assertFalse(activity.contains("Unknown Target"), activity);
    }


    /**
     * The "plus N items" node counts only the relationships that are drawn in this graph.
     */
    @Test
    public void moreNodeCountsOnlyFilteredRelationships()
    {
        List<RelatedMetadataElementSummary> relationships = new ArrayList<>();

        for (int i = 0; i < 4; i++)
        {
            relationships.add(related(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, "Member " + i));
            relationships.add(related(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName, "Impacted " + i));
        }

        OpenMetadataRootElement rootElement = rootElement();
        rootElement.setCollectionMembers(relationships);

        String context = new OpenMetadataRootMermaidGraphBuilder(rootElement, 2).getMermaidGraph();

        assertNotNull(context);
        assertTrue(context.contains("plus 2 Items"), context);
        assertFalse(context.contains("Impacted"), context);
    }


    /**
     * The new graphs are carried by the copy constructor and serialized under their own JSON names, and
     * AssetGraph (which used to declare its own actionMermaidGraph) still exposes a single actionMermaidGraph.
     */
    @Test
    public void newGraphsSurviveCopyAndJson() throws Exception
    {
        OpenMetadataRootElement rootElement = rootElement();

        rootElement.setMermaidGraph("context");
        rootElement.setActionMermaidGraph("action");
        rootElement.setActivityMermaidGraph("activity");

        OpenMetadataRootElement copy = new OpenMetadataRootElement(rootElement);

        assertEquals(copy, rootElement);
        assertEquals(copy.hashCode(), rootElement.hashCode());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode     json         = objectMapper.valueToTree(rootElement);

        assertEquals(json.get("mermaidGraph").asText(), "context");
        assertEquals(json.get("actionMermaidGraph").asText(), "action");
        assertEquals(json.get("activityMermaidGraph").asText(), "activity");

        OpenMetadataRootElement readBack = objectMapper.readValue(objectMapper.writeValueAsString(rootElement), OpenMetadataRootElement.class);

        assertEquals(readBack.getActionMermaidGraph(), "action");
        assertEquals(readBack.getActivityMermaidGraph(), "activity");

        AssetGraph assetGraph = new AssetGraph(rootElement);

        assetGraph.setActionMermaidGraph("asset graph actions");

        String assetGraphJson = objectMapper.writeValueAsString(assetGraph);

        assertEquals(objectMapper.readTree(assetGraphJson).get("actionMermaidGraph").asText(), "asset graph actions");
        assertEquals(objectMapper.readValue(assetGraphJson, AssetGraph.class).getActionMermaidGraph(), "asset graph actions");
        assertEquals(new AssetGraph(assetGraph).getActivityMermaidGraph(), "activity");
    }


    /*
     * Fixtures and helpers
     */

    private String build(OpenMetadataRootElement rootElement, OpenMetadataRootMermaidGraphBuilder.GraphScope graphScope)
    {
        return new OpenMetadataRootMermaidGraphBuilder(rootElement, MAX_NODES, graphScope).getMermaidGraph();
    }


    private OpenMetadataType relationshipTypeWithPurpose(OpenMetadataRelationshipPurpose purpose)
    {
        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            if (openMetadataType.relationshipPurpose == purpose)
            {
                return openMetadataType;
            }
        }

        throw new AssertionError("No relationship type declares purpose " + purpose);
    }


    private OpenMetadataRootElement rootElement()
    {
        ElementHeader header = new ElementHeader();

        header.setGUID("root-guid");
        header.setType(type("Asset", "Referenceable", "OpenMetadataRoot"));

        OpenMetadataRootElement rootElement = new OpenMetadataRootElement();

        rootElement.setElementHeader(header);
        rootElement.setProperties(properties("Root Element"));

        return rootElement;
    }


    private RelatedMetadataElementSummary related(String relationshipTypeName, String displayName)
    {
        return fill(new RelatedMetadataElementSummary(), relationshipTypeName, displayName);
    }


    private RelatedMetadataHierarchySummary hierarchy(String relationshipTypeName, String displayName)
    {
        return fill(new RelatedMetadataHierarchySummary(), relationshipTypeName, displayName);
    }


    private <T extends RelatedMetadataElementSummary> T fill(T related, String relationshipTypeName, String displayName)
    {
        String guid = displayName.replace(' ', '-');

        ElementHeader elementHeader = new ElementHeader();

        elementHeader.setGUID(guid);
        elementHeader.setType(type("Referenceable", "OpenMetadataRoot"));

        MetadataElementSummary summary = new MetadataElementSummary();

        summary.setElementHeader(elementHeader);
        summary.setProperties(properties(displayName));

        ElementHeader relationshipHeader = new ElementHeader();

        relationshipHeader.setGUID("rel-" + guid);
        relationshipHeader.setType(type(relationshipTypeName));

        related.setRelationshipHeader(relationshipHeader);
        related.setRelatedElement(summary);

        return related;
    }


    private ReferenceableProperties properties(String displayName)
    {
        ReferenceableProperties properties = new ReferenceableProperties();

        properties.setDisplayName(displayName);
        properties.setQualifiedName(displayName);

        return properties;
    }


    private ElementType type(String name, String... superTypes)
    {
        ElementType elementType = new ElementType();

        elementType.setTypeName(name);
        elementType.setSuperTypeNames(Arrays.asList(superTypes));

        return elementType;
    }
}
