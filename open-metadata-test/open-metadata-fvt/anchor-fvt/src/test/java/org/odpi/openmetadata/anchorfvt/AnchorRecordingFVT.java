/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.anchorfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.anchorfvt.AnchorFvtTestSupport.AnchoredGraph;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorSearchMatches;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Anchors classification records the anchor an element was created with, and the anchor-scoped searches find
 * an anchor's members by it.  Everything the cascades do rests on these two facts: the anchorGUID is not resolved
 * to the root of the graph, so an element can be anchored to a member, and a member's own members are found by
 * asking about that member rather than about the root.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AnchorRecordingFVT
{
    /**
     * Each element records the anchor it was created with: the schema's elements and the connection name the
     * database schema, the endpoint names the connection, and the glossary term names no other element.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void anchorGUIDIsTheAnchorSupplied() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "AnchorRecorded");

            String databaseSchemaGUID = graph.guid(AnchoredGraph.DATABASE_SCHEMA);

            for (String role : this.getDatabaseSchemaMembers())
            {
                assertEquals(databaseSchemaGUID,
                             AnchorFvtTestSupport.getAnchorGUID(openMetadataStore.getMetadataElementByGUID(graph.guid(role))),
                             role + " should be anchored to the database schema");
            }

            assertEquals(graph.guid(AnchoredGraph.CONNECTION),
                         AnchorFvtTestSupport.getAnchorGUID(openMetadataStore.getMetadataElementByGUID(graph.guid(AnchoredGraph.ENDPOINT))),
                         "The endpoint should be anchored to the connection");

            for (String ownAnchor : List.of(AnchoredGraph.DATABASE_SCHEMA, AnchoredGraph.GLOSSARY_TERM))
            {
                String anchorGUID = AnchorFvtTestSupport.getAnchorGUID(openMetadataStore.getMetadataElementByGUID(graph.guid(ownAnchor)));

                assertTrue((anchorGUID == null) || (anchorGUID.equals(graph.guid(ownAnchor))),
                           ownAnchor + " should be its own anchor, not anchored to " + anchorGUID);
            }
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * A search scoped to the database schema returns its direct members - and not the endpoint, which is anchored
     * to the connection, nor the glossary term, which is linked to a table but anchored to itself.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void anchorScopedSearchReturnsDirectMembersOnly() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "AnchorScopedSearch");

            SearchOptions searchOptions = new SearchOptions();

            searchOptions.setAnchorGUID(graph.guid(AnchoredGraph.DATABASE_SCHEMA));
            searchOptions.setPageSize(AnchorFvtTestSupport.MAX_PAGE_SIZE);

            /*
             * The search string is a literal "contains" match, not a regular expression, so the graph's own
             * qualified name marker - which every element in it carries - is what selects the whole graph.
             */
            this.checkDirectMembers(graph,
                                    openMetadataStore.findMetadataElementsWithString(graph.marker(), searchOptions),
                                    "findMetadataElementsWithString scoped to the database schema");

            AnchorSearchMatches matches = openMetadataStore.findElementsForAnchor(graph.marker(),
                                                                                  graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                                                  new SearchOptions());

            assertNotNull(matches, "findElementsForAnchor found nothing for the database schema");

            this.checkDirectMembers(graph, matches.getMatchingElements(), "findElementsForAnchor for the database schema");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * The roles that are anchored directly to the database schema.
     *
     * @return roles
     */
    private Set<String> getDatabaseSchemaMembers()
    {
        return Set.of(AnchoredGraph.SCHEMA_TYPE,
                      AnchoredGraph.TABLE1,
                      AnchoredGraph.TABLE2,
                      AnchoredGraph.COLUMN1A,
                      AnchoredGraph.COLUMN1B,
                      AnchoredGraph.COLUMN2A,
                      AnchoredGraph.CONNECTION);
    }


    /**
     * Check that a search result holds every direct member of the database schema and neither the endpoint nor
     * the glossary term.  Whether the anchor itself is included is left open: it is its own anchor.
     *
     * @param graph graph searched
     * @param results search results
     * @param search description of the search for the failure messages
     */
    private void checkDirectMembers(AnchoredGraph             graph,
                                    List<OpenMetadataElement> results,
                                    String                    search)
    {
        assertNotNull(results, search + " found nothing");

        Set<String> foundGUIDs = new HashSet<>();

        for (OpenMetadataElement element : results)
        {
            foundGUIDs.add(element.getElementGUID());
        }

        for (String role : this.getDatabaseSchemaMembers())
        {
            assertTrue(foundGUIDs.contains(graph.guid(role)), search + " did not find " + role);
        }

        for (String role : List.of(AnchoredGraph.ENDPOINT, AnchoredGraph.GLOSSARY_TERM))
        {
            assertTrue(! foundGUIDs.contains(graph.guid(role)), search + " found " + role + ", which is not anchored to the database schema");
        }
    }
}
