/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.anchorfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.anchorfvt.AnchorFvtTestSupport.AnchoredGraph;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Deleting or archiving an element that is anchored to something else takes that element and what lies downstream
 * of it - the elements at end 2 of its STRUCTURAL and ATTACHMENT relationships, and anything anchored to it - and
 * nothing else.  Its siblings, its anchor, the elements it is linked to for any other relationship purpose (a foreign
 * key, a lineage mapping, an assignment) and elements with an anchor of their own all survive.
 * <br><br>
 * The anchorGUID of an anchored graph's members names the anchor, so a table's columns and the other tables look
 * the same; only relationship direction separates them.  Archiving used to follow relationships in both directions
 * and so took the whole graph, anchor included - and since a delete of an element with lineage becomes an archive by
 * default, that is what deleting a table with lineage did.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AnchoredMemberDeleteFVT
{
    private static final Set<String> TABLE1_AND_COLUMNS = Set.of(AnchoredGraph.TABLE1,
                                                                 AnchoredGraph.COLUMN1A,
                                                                 AnchoredGraph.COLUMN1B);

    private static final Set<String> CONNECTION_AND_ENDPOINT = Set.of(AnchoredGraph.CONNECTION,
                                                                      AnchoredGraph.ENDPOINT);


    /**
     * A soft delete of a table takes the table and its columns.  The other table, the column its foreign key
     * points at, the schema, the connection and the glossary term all stay.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void softDeleteOfMemberTakesOnlyDownstreamElements() throws Exception
    {
        this.checkDelete("SoftDeleteMember", AnchoredGraph.TABLE1, DeleteMethod.SOFT_DELETE, false, TABLE1_AND_COLUMNS);
    }


    /**
     * A cascaded soft delete of a table is no wider.  The cascade option governs the type-specific clean up of
     * an element's dependents; it is not licence to climb out of the element's own part of the graph.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void cascadedSoftDeleteOfMemberTakesOnlyDownstreamElements() throws Exception
    {
        this.checkDelete("CascadeDeleteMember", AnchoredGraph.TABLE1, DeleteMethod.SOFT_DELETE, true, TABLE1_AND_COLUMNS);
    }


    /**
     * The delete reported from the field: an ordinary delete (LOOK_FOR_LINEAGE, the default) of a table that has a
     * lineage mapping is turned into an archive, and the archive used to take every table, the schema and the
     * database schema with it.  Now it archives the table and its columns, and each of those - and only those - is
     * renamed with the same archive suffix.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void lookForLineageDeleteOfMemberArchivesOnlyDownstreamElements() throws Exception
    {
        this.checkArchive("ArchiveMember", AnchoredGraph.TABLE1, DeleteMethod.LOOK_FOR_LINEAGE, TABLE1_AND_COLUMNS);
    }


    /**
     * Deleting the connection takes its endpoint, which is anchored to the connection rather than to the database
     * schema - an element anchored to a member of the graph belongs to that member.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void deleteOfMemberTakesElementsAnchoredToIt() throws Exception
    {
        this.checkDelete("DeleteConnection", AnchoredGraph.CONNECTION, DeleteMethod.SOFT_DELETE, false, CONNECTION_AND_ENDPOINT);
    }


    /**
     * Archiving the connection archives its endpoint too, for the same reason.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void archiveOfMemberTakesElementsAnchoredToIt() throws Exception
    {
        this.checkArchive("ArchiveConnection", AnchoredGraph.CONNECTION, DeleteMethod.ARCHIVE, CONNECTION_AND_ENDPOINT);
    }


    /**
     * Deleting the column that a foreign key points at removes only that column.  The table holding the foreign
     * key's other column is untouched, as is the column itself.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void deleteOfPeerTargetTakesOnlyThatElement() throws Exception
    {
        this.checkDelete("DeletePeerTarget", AnchoredGraph.COLUMN2A, DeleteMethod.SOFT_DELETE, false, Set.of(AnchoredGraph.COLUMN2A));
    }


    /**
     * A member linked to another member of the same graph by a relationship that is not part of the graph's
     * structure keeps it.  Here the first table has an AssignmentScope (ASSIGNMENT) to the second table, so the
     * second table is at end 2 and shares the anchor - but it is assigned to the first table, not part of it.
     * Before relationship purposes, only a fixed list of peer relationships was skipped and a link such as this one
     * took the other element with it: that is how deleting product manager roles removed the links that made them
     * subscription owners.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void deleteOfMemberLeavesElementsAssignedToIt() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "DeleteAssigner");

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                           graph.guid(AnchoredGraph.TABLE1),
                                                           graph.guid(AnchoredGraph.TABLE2),
                                                           null,
                                                           null,
                                                           null);

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.TABLE1),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, false));

            assertEquals(AnchorFvtTestSupport.getRolesExcept(graph, TABLE1_AND_COLUMNS),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "Deleting " + AnchoredGraph.TABLE1 + " should leave the table assigned to it");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Build a graph, delete one element and check exactly which elements are no longer active.
     *
     * @param label test label for the qualified names
     * @param deletedRole element to delete
     * @param deleteMethod how to delete it
     * @param cascadedDelete may the delete cascade
     * @param expectedGone the elements that should go
     * @throws Exception problem talking to the server
     */
    private void checkDelete(String       label,
                             String       deletedRole,
                             DeleteMethod deleteMethod,
                             boolean      cascadedDelete,
                             Set<String>  expectedGone) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, label);

            openMetadataStore.deleteMetadataElementInStore(graph.guid(deletedRole),
                                                           AnchorFvtTestSupport.getDeleteOptions(deleteMethod, cascadedDelete));

            assertEquals(AnchorFvtTestSupport.getRolesExcept(graph, expectedGone),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "Deleting " + deletedRole + " should leave everything but " + expectedGone);
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Build a graph, archive one element and check that exactly the expected elements are archived - classified
     * as Mementos and renamed, all with the same suffix - and that the rest are untouched.
     *
     * @param label test label for the qualified names
     * @param archivedRole element to archive
     * @param deleteMethod ARCHIVE, or LOOK_FOR_LINEAGE for an element with lineage
     * @param expectedArchived the elements that should be archived
     * @throws Exception problem talking to the server
     */
    private void checkArchive(String       label,
                              String       archivedRole,
                              DeleteMethod deleteMethod,
                              Set<String>  expectedArchived) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, label);

            openMetadataStore.deleteMetadataElementInStore(graph.guid(archivedRole),
                                                           AnchorFvtTestSupport.getDeleteOptions(deleteMethod, false));

            assertEquals(AnchorFvtTestSupport.getRolesExcept(graph, expectedArchived),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "Archiving " + archivedRole + " should leave everything but " + expectedArchived);

            AnchorFvtTestSupport.checkArchivedElements(openMetadataStore, graph, expectedArchived);
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }
}
