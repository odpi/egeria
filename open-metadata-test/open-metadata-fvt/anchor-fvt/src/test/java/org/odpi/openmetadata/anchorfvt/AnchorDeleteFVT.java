/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.anchorfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.anchorfvt.AnchorFvtTestSupport.AnchoredGraph;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Deleting, archiving or purging an anchor takes its whole anchored graph - including elements anchored to one of
 * its members rather than to the anchor itself - and nothing that has an anchor of its own.  The members are found
 * by a query on the Anchors classification, so these tests also check that the query finds every member, however
 * many there are, on both repositories.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AnchorDeleteFVT
{
    /**
     * A cascaded soft delete of the database schema takes every element of its graph, the endpoint anchored to
     * the connection included.  The glossary term is its own anchor and stays.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void cascadedDeleteOfAnchorTakesWholeGraph() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "DeleteAnchor");

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));

            assertEquals(Set.of(AnchoredGraph.GLOSSARY_TERM),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "Deleting the database schema should leave only the glossary term");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Without cascade, a database schema that still has tables may not be deleted - and refusing must leave the
     * graph as it was.  The members are found and deleted by query before the anchor itself is checked, so a
     * check made only at that point finds the schema already emptied and lets the delete through.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void nonCascadedDeleteOfNonEmptyDatabaseSchemaIsRefusedAndChangesNothing() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            final AnchoredGraph builtGraph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "RefuseDeleteAnchor");

            graph = builtGraph;

            assertThrows(Exception.class,
                         () -> openMetadataStore.deleteMetadataElementInStore(builtGraph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                                              AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, false)),
                         "Deleting a database schema with tables and no cascade should be refused");

            assertEquals(graph.roles(),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "A refused delete should leave every element in place");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Archiving the database schema archives every element of its graph - each one classified as a Memento and
     * renamed with the same "_archivedOn_" suffix, not just the anchor - and leaves the glossary term alone.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void archiveOfAnchorArchivesAndRenamesWholeGraph() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "ArchiveAnchor");

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.ARCHIVE, false));

            Set<String> archived = AnchorFvtTestSupport.getRolesExcept(graph, Set.of(AnchoredGraph.GLOSSARY_TERM));

            assertEquals(Set.of(AnchoredGraph.GLOSSARY_TERM),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "Archiving the database schema should leave only the glossary term active");

            AnchorFvtTestSupport.checkArchivedElements(openMetadataStore, graph, archived);
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * A cascaded purge of the database schema removes every element of its graph for good - nothing is left even
     * to a search that includes deleted and archived elements - except the glossary term.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void cascadedPurgeOfAnchorRemovesWholeGraph() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "PurgeAnchor");

            AnchorFvtTestSupport.purgeElementOrFail(openMetadataStore, graph.guid(AnchoredGraph.DATABASE_SCHEMA));

            assertEquals(Set.of(graph.qualifiedNames().get(AnchoredGraph.GLOSSARY_TERM)),
                         AnchorFvtTestSupport.findQualifiedNames(openMetadataStore,
                                                                 graph.marker(),
                                                                 List.of(ElementStatus.ACTIVE, ElementStatus.DELETED)),
                         "Purging the database schema should leave only the glossary term");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * The members of an anchor are found a page at a time.  A database schema with more tables than one page holds
     * is deleted completely - none of the tables beyond the first page is left behind, anchored to an element that
     * no longer exists.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void deleteOfAnchorWithMoreThanOnePageOfMembersTakesThemAll() throws Exception
    {
        final int tableCount = (AnchorFvtTestSupport.MAX_PAGE_SIZE * 3) / 2;

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = new AnchoredGraph(AnchorFvtTestSupport.QUALIFIED_NAME_PREFIX + "ManyMembers:" + UUID.randomUUID() + ":",
                                                                new LinkedHashMap<>(),
                                                                new LinkedHashMap<>());

        try
        {
            AnchorFvtTestSupport.create(openMetadataStore, graph, AnchoredGraph.DATABASE_SCHEMA, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                        null, null, null, true);
            AnchorFvtTestSupport.create(openMetadataStore, graph, AnchoredGraph.SCHEMA_TYPE, OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName,
                                        AnchoredGraph.DATABASE_SCHEMA, AnchoredGraph.DATABASE_SCHEMA, OpenMetadataType.SCHEMA_RELATIONSHIP.typeName, true);

            for (int table = 0; table < tableCount; table++)
            {
                AnchorFvtTestSupport.create(openMetadataStore, graph, "table" + table, OpenMetadataType.RELATIONAL_TABLE.typeName,
                                            AnchoredGraph.DATABASE_SCHEMA, AnchoredGraph.SCHEMA_TYPE, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, true);
            }

            openMetadataStore.deleteMetadataElementInStore(graph.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));

            assertEquals(Set.of(),
                         AnchorFvtTestSupport.findQualifiedNames(openMetadataStore, graph.marker(), List.of(ElementStatus.ACTIVE)),
                         "Every one of the " + tableCount + " tables should have been deleted with the database schema");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * A cascaded delete of a host takes the platforms deployed on it.  The cascade used to delete the host itself
     * a second time instead - recursing until the server's stack ran out - and never reached the platform.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void cascadedDeleteOfInfrastructureTakesDeployedElements() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = new AnchoredGraph(AnchorFvtTestSupport.QUALIFIED_NAME_PREFIX + "Infrastructure:" + UUID.randomUUID() + ":",
                                                                new LinkedHashMap<>(),
                                                                new LinkedHashMap<>());

        try
        {
            AnchorFvtTestSupport.create(openMetadataStore, graph, "host", OpenMetadataType.HOST.typeName,
                                        null, null, null, true);
            AnchorFvtTestSupport.create(openMetadataStore, graph, "platform", OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                        null, "host", OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName, false);

            openMetadataStore.deleteMetadataElementInStore(graph.guid("host"),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));

            assertEquals(Set.of(),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "Deleting the host should take the platform deployed on it");
        }
        finally
        {
            for (String guid : graph.guids().values())
            {
                AnchorFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    /**
     * A collection's members go with it on a cascaded delete, whatever they are anchored to - the members of a
     * collection are part of it by design.  Without cascade, a collection with members may not be deleted.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void collectionMembersGoOnlyWithCascadedDelete() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = new AnchoredGraph(AnchorFvtTestSupport.QUALIFIED_NAME_PREFIX + "Collection:" + UUID.randomUUID() + ":",
                                                                new LinkedHashMap<>(),
                                                                new LinkedHashMap<>());

        try
        {
            AnchorFvtTestSupport.create(openMetadataStore, graph, "collection", OpenMetadataType.COLLECTION.typeName,
                                        null, null, null, true);
            AnchorFvtTestSupport.create(openMetadataStore, graph, "member", OpenMetadataType.COLLECTION.typeName,
                                        null, "collection", OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, true);

            assertThrows(Exception.class,
                         () -> openMetadataStore.deleteMetadataElementInStore(graph.guid("collection"),
                                                                              AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, false)),
                         "Deleting a collection with members and no cascade should be refused");

            assertEquals(Set.of("collection", "member"),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph),
                         "A refused delete should leave the collection and its member in place");

            openMetadataStore.deleteMetadataElementInStore(graph.guid("collection"),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));

            assertTrue(AnchorFvtTestSupport.getActiveRoles(openMetadataStore, graph).isEmpty(),
                       "A cascaded delete of the collection should take its member");
        }
        finally
        {
            for (String guid : graph.guids().values())
            {
                AnchorFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }
}
