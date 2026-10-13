/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.anchorfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.anchorfvt.AnchorFvtTestSupport.AnchoredGraph;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.HistoricalQueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Restore reverses the command that removed an element - a soft delete or an archive - and the whole of it: every
 * instance one command writes carries the same lastOperationId, so restore finds exactly what that command
 * changed, whatever anchoring and the cascade option made it take.  Restore can be asked for on any element the
 * command removed.  An element removed by a separate, earlier command stays removed, and so does a relationship to
 * an element outside the restored graph that has since been deleted.  A purged element can not be restored.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AnchorRestoreFVT
{
    /**
     * Every link in the graph, as source role, relationship type and target role at end 2.
     */
    private static final List<String[]> LINKS = List.of(new String[]{AnchoredGraph.DATABASE_SCHEMA, OpenMetadataType.SCHEMA_RELATIONSHIP.typeName},
                                                        new String[]{AnchoredGraph.SCHEMA_TYPE, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName},
                                                        new String[]{AnchoredGraph.TABLE1, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName},
                                                        new String[]{AnchoredGraph.TABLE2, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName},
                                                        new String[]{AnchoredGraph.COLUMN1A, OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName},
                                                        new String[]{AnchoredGraph.TABLE1, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName},
                                                        new String[]{AnchoredGraph.DATABASE_SCHEMA, OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName},
                                                        new String[]{AnchoredGraph.CONNECTION, OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName},
                                                        new String[]{AnchoredGraph.TABLE1, OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName});


    /**
     * A cascaded delete of the database schema is one operation: every element it removes carries the same
     * lastOperationId on its deleted version.  The deleted versions are read from each element's history once it
     * has been restored - while they are deleted, a member whose anchor is deleted is not returned by a read.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void cascadedDeleteStampsOneOperationId() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "DeleteOperationId");

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));
            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA), new MetadataSourceOptions());

            Map<String, String> operationIds = new java.util.LinkedHashMap<>();

            for (String role : AnchorFvtTestSupport.getRolesExcept(graph, Set.of(AnchoredGraph.GLOSSARY_TERM)))
            {
                List<OpenMetadataElement> history = openMetadataStore.getMetadataElementHistory(graph.guid(role), new HistoricalQueryOptions());

                assertNotNull(history, role + " has no history");

                for (OpenMetadataElement version : history)
                {
                    if (version.getStatus() == ElementStatus.DELETED)
                    {
                        operationIds.put(role, version.getVersions().getLastOperationId());
                    }
                }

                assertTrue(operationIds.containsKey(role), role + " has no deleted version in its history");
            }

            assertEquals(1, new HashSet<>(operationIds.values()).size(), "The delete wrote more than one lastOperationId: " + operationIds);
            assertNotNull(operationIds.values().iterator().next(), "The delete wrote no lastOperationId");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Restoring the database schema after a cascaded delete brings back every element and every link, including
     * the endpoint anchored to the connection and the link to the glossary term outside the graph.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreAfterCascadedDeleteBringsBackTheWholeGraph() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreAnchor");

            Set<String> links = this.getLinks(openMetadataStore, graph);

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));
            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA), new MetadataSourceOptions());

            assertEquals(graph.roles(), AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph), "Restore should bring back every element");
            assertEquals(links, this.getLinks(openMetadataStore, graph), "Restore should bring back every link");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Restore can be asked for on any element the delete removed - here a column - and it reverses the whole
     * delete, not just that column.  Restoring part of the graph would leave its members anchored to a database
     * schema that is still gone.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreOfAnyRemovedElementReversesTheWholeDelete() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreFromMember");

            Set<String> links = this.getLinks(openMetadataStore, graph);

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));
            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.COLUMN2A), new MetadataSourceOptions());

            assertEquals(graph.roles(), AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph), "Restore should bring back every element");
            assertEquals(links, this.getLinks(openMetadataStore, graph), "Restore should bring back every link");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * A column deleted on its own before the database schema was deleted was removed by a different command, so
     * restoring the database schema leaves it deleted.  Restoring the column afterwards brings it back under its
     * table.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreLeavesAnEarlierSeparateDeleteInPlace() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreSeparateDelete");

            Set<String> links = this.getLinks(openMetadataStore, graph);

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.COLUMN1B),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, false));
            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));
            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA), new MetadataSourceOptions());

            assertEquals(AnchorFvtTestSupport.getRolesExcept(graph, Set.of(AnchoredGraph.COLUMN1B)),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "Restoring the database schema should leave the separately deleted column deleted");

            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.COLUMN1B), new MetadataSourceOptions());

            assertEquals(graph.roles(), AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph), "Restoring the column should bring it back");
            assertEquals(links, this.getLinks(openMetadataStore, graph), "Restoring the column should bring back its link to its table");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Restoring a table deleted on its own brings back the table, its columns and their links - to the schema
     * type above it, to the column its foreign key points at, to the table its lineage mapping points at and to the
     * glossary term - all of which are still there.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreOfDeletedMemberBringsBackItsLinks() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreMember");

            Set<String> links = this.getLinks(openMetadataStore, graph);

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.TABLE1),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, false));
            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.TABLE1), new MetadataSourceOptions());

            assertEquals(graph.roles(), AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph), "Restore should bring back the table and its columns");
            assertEquals(links, this.getLinks(openMetadataStore, graph), "Restore should bring back every link the delete removed");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * A link to an element outside the restored graph comes back only if that element is still there.  The
     * glossary term is deleted separately after the database schema, so table1's link to it stays deleted.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreSkipsLinksToElementsSinceDeleted() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreOutsideLink");

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));
            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.GLOSSARY_TERM),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, false));
            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA), new MetadataSourceOptions());

            assertEquals(AnchorFvtTestSupport.getRolesExcept(graph, Set.of(AnchoredGraph.GLOSSARY_TERM)),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "Restore should bring back the graph and leave the glossary term deleted");
            assertTrue(AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, graph.guid(AnchoredGraph.TABLE1), OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName, true).isEmpty(),
                       "table1's link to the deleted glossary term should stay deleted");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Restoring the database schema after an archive takes the Memento classification off every element the
     * archive classified and gives each its original qualified name back.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreAfterArchiveOfAnchorReversesTheArchive() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreArchive");

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.ARCHIVE, false));
            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA), new MetadataSourceOptions());

            assertEquals(graph.roles(), AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph), "Restore should make every element visible again");
            AnchorFvtTestSupport.checkArchivedElements(openMetadataStore, graph, Set.of());
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * An ordinary delete of a table with lineage is an archive; restoring the table reverses that archive - the
     * table and its columns - and leaves the rest of the graph as it was.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreAfterLookForLineageArchiveOfMember() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreMemberArchive");

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.TABLE1),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.LOOK_FOR_LINEAGE, false));
            openMetadataStore.restoreMetadataElementInStore(graph.guid(AnchoredGraph.COLUMN1A), new MetadataSourceOptions());

            assertEquals(graph.roles(), AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph), "Restore should make every element visible again");
            AnchorFvtTestSupport.checkArchivedElements(openMetadataStore, graph, Set.of());
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * A relationship deleted on its own can be restored on its own.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreOfDeletedRelationship() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreRelationship");

            String foreignKeyGUID = AnchorFvtTestSupport.getRelationshipGUIDs(openMetadataStore,
                                                                              graph.guid(AnchoredGraph.COLUMN1A),
                                                                              OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName,
                                                                              true).iterator().next();

            openMetadataStore.deleteRelationshipInStore(foreignKeyGUID, AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, false));

            assertTrue(AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, graph.guid(AnchoredGraph.COLUMN1A), OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, true).isEmpty(),
                       "The foreign key should be deleted");

            openMetadataStore.restoreRelationshipInStore(foreignKeyGUID, new MetadataSourceOptions());

            assertEquals(Set.of(graph.guid(AnchoredGraph.COLUMN2A)),
                         AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, graph.guid(AnchoredGraph.COLUMN1A), OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, true),
                         "The foreign key should be restored");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * There is nothing to restore for an active element, and a purged element is gone for good.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void restoreOfActiveOrPurgedElementIsRefused() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            final AnchoredGraph builtGraph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RestoreRefused");

            graph = builtGraph;

            assertThrows(Exception.class,
                         () -> openMetadataStore.restoreMetadataElementInStore(builtGraph.guid(AnchoredGraph.TABLE1), new MetadataSourceOptions()),
                         "Restoring an active element should be refused");

            AnchorFvtTestSupport.purgeElementOrFail(openMetadataStore, builtGraph.guid(AnchoredGraph.DATABASE_SCHEMA));

            assertThrows(Exception.class,
                         () -> openMetadataStore.restoreMetadataElementInStore(builtGraph.guid(AnchoredGraph.DATABASE_SCHEMA), new MetadataSourceOptions()),
                         "Restoring a purged element should be refused");
            assertFalse(AnchorFvtTestSupport.isActive(openMetadataStore, builtGraph.guid(AnchoredGraph.TABLE1)),
                        "The purged graph should still be gone");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Return every link in the graph that is active now, as "source -type-> target".  A target outside the graph
     * is named by its GUID.
     *
     * @param openMetadataStore store to read through
     * @param graph graph
     * @return links
     * @throws Exception problem talking to the server
     */
    private Set<String> getLinks(OpenMetadataStore openMetadataStore, AnchoredGraph graph) throws Exception
    {
        Set<String> links = new HashSet<>();

        for (String[] link : LINKS)
        {
            if (AnchorFvtTestSupport.isActive(openMetadataStore, graph.guid(link[0])))
            {
                for (String targetGUID : AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, graph.guid(link[0]), link[1], true))
                {
                    String targetRole = targetGUID;

                    for (String role : graph.roles())
                    {
                        if (graph.guid(role).equals(targetGUID))
                        {
                            targetRole = role;
                        }
                    }

                    links.add(link[0] + " -" + link[1] + "-> " + targetRole);
                }
            }
        }

        return links;
    }
}
