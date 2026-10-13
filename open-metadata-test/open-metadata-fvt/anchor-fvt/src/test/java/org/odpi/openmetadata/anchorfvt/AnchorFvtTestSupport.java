/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.anchorfvt;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Shared conventions and helpers used across the anchor-fvt test suite.
 * <br>
 * Every element this suite creates uses a qualified name starting with {@link #QUALIFIED_NAME_PREFIX}, so
 * anything left behind by an earlier run that failed partway through can be found and purged before the next
 * run starts - the PostgreSQL repository persists across runs.
 */
final class AnchorFvtTestSupport
{
    /**
     * Every qualified name this suite creates starts with this prefix.
     */
    static final String QUALIFIED_NAME_PREFIX = "anchor-fvt:";

    static final int MAX_PAGE_SIZE = ConnectorContextFactory.MAX_PAGE_SIZE;

    private static final PropertyHelper propertyHelper = new PropertyHelper();


    private AnchorFvtTestSupport()
    {
        // no instances
    }


    /**
     * The graph that most tests start from: a database schema catalogued with its tables, columns and
     * connection.  The anchorGUID recorded on each element is the one it was created with, which is how the
     * endpoint comes to be anchored to the connection rather than to the database schema.
     * <pre>
     *   databaseSchema (anchor) --Schema--&gt; schemaType --AttributeForSchema--&gt; table1, table2
     *   table1 --NestedSchemaAttribute--&gt; column1a, column1b
     *   table2 --NestedSchemaAttribute--&gt; column2a
     *   column1a --ForeignKey--&gt; column2a                 ASSOCIATION between columns of different tables
     *   table1 --LineageMapping--&gt; table2                 DESIGN_LINEAGE, which turns a delete into an archive
     *   databaseSchema --ResourceConnection--&gt; connection --ConnectToEndpoint--&gt; endpoint
     *   table1 --SemanticAssignment--&gt; glossaryTerm       the glossary term is its own anchor
     * </pre>
     * Every element is anchored to the database schema except the endpoint, which is anchored to the
     * connection, and the glossary term, which is its own anchor.
     *
     * @param marker qualified name prefix shared by every element in this graph
     * @param guids role name to GUID, in creation order
     * @param qualifiedNames role name to qualified name
     */
    record AnchoredGraph(String marker, Map<String, String> guids, Map<String, String> qualifiedNames)
    {
        static final String DATABASE_SCHEMA = "databaseSchema";
        static final String SCHEMA_TYPE     = "schemaType";
        static final String TABLE1          = "table1";
        static final String TABLE2          = "table2";
        static final String COLUMN1A        = "column1a";
        static final String COLUMN1B        = "column1b";
        static final String COLUMN2A        = "column2a";
        static final String CONNECTION      = "connection";
        static final String ENDPOINT        = "endpoint";
        static final String GLOSSARY_TERM   = "glossaryTerm";

        String guid(String role)
        {
            return guids.get(role);
        }

        /**
         * The roles of every element in the graph.
         *
         * @return role names
         */
        Set<String> roles()
        {
            return guids.keySet();
        }
    }


    /**
     * Build the graph described on {@link AnchoredGraph}.
     *
     * @param openMetadataStore store to create through
     * @param label short description of the test, included in the qualified names
     * @return the graph
     * @throws Exception problem creating the graph
     */
    static AnchoredGraph buildAnchoredGraph(OpenMetadataStore openMetadataStore,
                                            String            label) throws Exception
    {
        return buildGraphWithMarker(openMetadataStore, QUALIFIED_NAME_PREFIX + label + ":" + UUID.randomUUID() + ":");
    }


    /**
     * The placeholder that a template graph carries in every qualified name.  Copies made from the template
     * must have unique names, and a template supplies them by substitution: every element's name includes the
     * placeholder, and the create names the value to put in its place.
     */
    static final String COPY_NAME_PLACEHOLDER = "copyName";


    /**
     * Build the graph described on {@link AnchoredGraph} for use as a template.  Every qualified name holds the
     * {@link #COPY_NAME_PLACEHOLDER} placeholder, so the elements created from it are named after the value
     * supplied for it - {@code <marker>copy1:table1} - and their names say which role each one plays.
     *
     * @param openMetadataStore store to create through
     * @param label short description of the test, included in the qualified names
     * @return the template graph; its marker includes the placeholder, see {@link #getCopyMarker}
     * @throws Exception problem creating the graph
     */
    static AnchoredGraph buildTemplateGraph(OpenMetadataStore openMetadataStore,
                                            String            label) throws Exception
    {
        return buildGraphWithMarker(openMetadataStore,
                                    QUALIFIED_NAME_PREFIX + label + ":" + UUID.randomUUID() + ":~{" + COPY_NAME_PLACEHOLDER + "}~:");
    }


    /**
     * The qualified name prefix of the elements created from a template graph with the supplied copy name.
     *
     * @param template template graph
     * @param copyName value supplied for the placeholder
     * @return marker shared by every element of the copy
     */
    static String getCopyMarker(AnchoredGraph template, String copyName)
    {
        return template.marker().replace("~{" + COPY_NAME_PLACEHOLDER + "}~", copyName);
    }


    /**
     * Build the graph described on {@link AnchoredGraph} with the supplied qualified name prefix.
     *
     * @param openMetadataStore store to create through
     * @param marker qualified name prefix for every element
     * @return the graph
     * @throws Exception problem creating the graph
     */
    private static AnchoredGraph buildGraphWithMarker(OpenMetadataStore openMetadataStore,
                                                      String            marker) throws Exception
    {
        Map<String, String> guids          = new LinkedHashMap<>();
        Map<String, String> qualifiedNames = new LinkedHashMap<>();
        AnchoredGraph       graph          = new AnchoredGraph(marker, guids, qualifiedNames);

        final String anchor = AnchoredGraph.DATABASE_SCHEMA;

        create(openMetadataStore, graph, AnchoredGraph.DATABASE_SCHEMA, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
               null, null, null, true);
        create(openMetadataStore, graph, AnchoredGraph.SCHEMA_TYPE, OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName,
               anchor, AnchoredGraph.DATABASE_SCHEMA, OpenMetadataType.SCHEMA_RELATIONSHIP.typeName, true);

        for (String table : List.of(AnchoredGraph.TABLE1, AnchoredGraph.TABLE2))
        {
            create(openMetadataStore, graph, table, OpenMetadataType.RELATIONAL_TABLE.typeName,
                   anchor, AnchoredGraph.SCHEMA_TYPE, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, true);
        }

        for (String column : List.of(AnchoredGraph.COLUMN1A, AnchoredGraph.COLUMN1B))
        {
            create(openMetadataStore, graph, column, OpenMetadataType.RELATIONAL_COLUMN.typeName,
                   anchor, AnchoredGraph.TABLE1, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, true);
        }

        create(openMetadataStore, graph, AnchoredGraph.COLUMN2A, OpenMetadataType.RELATIONAL_COLUMN.typeName,
               anchor, AnchoredGraph.TABLE2, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, true);

        create(openMetadataStore, graph, AnchoredGraph.CONNECTION, OpenMetadataType.CONNECTION.typeName,
               anchor, AnchoredGraph.DATABASE_SCHEMA, OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, true);

        /*
         * Anchored to the connection, not to the database schema: an element anchored to a member of the graph.
         */
        create(openMetadataStore, graph, AnchoredGraph.ENDPOINT, OpenMetadataType.ENDPOINT.typeName,
               AnchoredGraph.CONNECTION, AnchoredGraph.CONNECTION, OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName, true);

        create(openMetadataStore, graph, AnchoredGraph.GLOSSARY_TERM, OpenMetadataType.GLOSSARY_TERM.typeName,
               null, AnchoredGraph.TABLE1, OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName, true);

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName,
                                                       graph.guid(AnchoredGraph.COLUMN1A),
                                                       graph.guid(AnchoredGraph.COLUMN2A),
                                                       null,
                                                       null,
                                                       null);

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName,
                                                       graph.guid(AnchoredGraph.TABLE1),
                                                       graph.guid(AnchoredGraph.TABLE2),
                                                       null,
                                                       null,
                                                       null);

        return graph;
    }


    /**
     * Create one element of a graph.
     *
     * @param openMetadataStore store to create through
     * @param graph graph to add it to
     * @param role role of the new element
     * @param typeName type of the new element
     * @param anchorRole role of its anchor - null for an element that is its own anchor
     * @param parentRole role of the element it is linked to - null for none
     * @param relationshipTypeName relationship to the parent
     * @param parentAtEnd1 is the parent at end 1 of that relationship?
     * @throws Exception problem creating the element
     */
    static void create(OpenMetadataStore openMetadataStore,
                       AnchoredGraph     graph,
                       String            role,
                       String            typeName,
                       String            anchorRole,
                       String            parentRole,
                       String            relationshipTypeName,
                       boolean           parentAtEnd1) throws Exception
    {
        String qualifiedName = graph.marker() + role;

        String guid = openMetadataStore.createMetadataElementInStore(typeName,
                                                                     null,
                                                                     anchorRole == null ? null : graph.guid(anchorRole),
                                                                     anchorRole == null,
                                                                     null,
                                                                     getQualifiedNameProperties(qualifiedName),
                                                                     parentRole == null ? null : graph.guid(parentRole),
                                                                     parentRole == null ? null : relationshipTypeName,
                                                                     null,
                                                                     parentAtEnd1);

        graph.guids().put(role, guid);
        graph.qualifiedNames().put(role, qualifiedName);
    }


    /**
     * Properties holding just a qualified name.
     *
     * @param qualifiedName the name
     * @return properties
     */
    static NewElementProperties getQualifiedNameProperties(String qualifiedName)
    {
        return new NewElementProperties(propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName));
    }


    /**
     * Delete options for the given method.
     *
     * @param deleteMethod how to delete
     * @param cascadedDelete may the delete cascade to dependent elements
     * @return options
     */
    static DeleteOptions getDeleteOptions(DeleteMethod deleteMethod, boolean cascadedDelete)
    {
        DeleteOptions deleteOptions = new DeleteOptions();

        deleteOptions.setDeleteMethod(deleteMethod);
        deleteOptions.setCascadedDelete(cascadedDelete);

        return deleteOptions;
    }


    /**
     * Is the element active and visible to an ordinary read?  A soft-deleted element is refused rather than
     * returned, and an archived one is hidden, so both count as not active.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID element to look for
     * @return true if an ordinary read returns it
     */
    static boolean isActive(OpenMetadataStore openMetadataStore, String elementGUID)
    {
        try
        {
            return openMetadataStore.getMetadataElementByGUID(elementGUID, new GetOptions()) != null;
        }
        catch (Exception notReadable)
        {
            return false;
        }
    }


    /**
     * The roles in a graph whose elements are still active.
     *
     * @param openMetadataStore store to read through
     * @param graph graph to check
     * @return roles of the active elements
     */
    static Set<String> getActiveRoles(OpenMetadataStore openMetadataStore, AnchoredGraph graph)
    {
        Set<String> activeRoles = new HashSet<>();

        for (String role : graph.roles())
        {
            if (isActive(openMetadataStore, graph.guid(role)))
            {
                activeRoles.add(role);
            }
        }

        return activeRoles;
    }


    /**
     * Read an element whether or not it has been archived.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID element to read
     * @return the element
     * @throws Exception it could not be read
     */
    static OpenMetadataElement getIncludingArchived(OpenMetadataStore openMetadataStore, String elementGUID) throws Exception
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForLineage(true);

        return openMetadataStore.getMetadataElementByGUID(elementGUID, getOptions);
    }


    /**
     * Return the named classification of an element.
     *
     * @param element element to look in
     * @param classificationName classification wanted
     * @return the classification, or null
     */
    static AttachedClassification getClassification(OpenMetadataElement element, String classificationName)
    {
        if ((element != null) && (element.getClassifications() != null))
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) && classificationName.equals(classification.getClassificationName()))
                {
                    return classification;
                }
            }
        }

        return null;
    }


    /**
     * The anchorGUID recorded in an element's Anchors classification.
     *
     * @param element element to look in
     * @return the anchorGUID, or null if there is none
     */
    static String getAnchorGUID(OpenMetadataElement element)
    {
        AttachedClassification anchors = getClassification(element, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

        if (anchors == null)
        {
            return null;
        }

        return propertyHelper.getStringProperty("anchor-fvt",
                                                OpenMetadataProperty.ANCHOR_GUID.name,
                                                anchors.getClassificationProperties(),
                                                "getAnchorGUID");
    }


    /**
     * The qualified name of an element.
     *
     * @param element element to look in
     * @return its qualified name
     */
    static String getQualifiedName(OpenMetadataElement element)
    {
        return propertyHelper.getStringProperty("anchor-fvt",
                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                element.getElementProperties(),
                                                "getQualifiedName");
    }


    /**
     * Return every element, in the requested statuses and archived or not, whose qualified name starts with the
     * supplied marker.
     *
     * @param openMetadataStore store to search
     * @param marker qualified name prefix
     * @param statuses statuses to include
     * @return elements found; empty if none
     * @throws Exception problem with the search
     */
    static List<OpenMetadataElement> findElements(OpenMetadataStore   openMetadataStore,
                                                  String              marker,
                                                  List<ElementStatus> statuses) throws Exception
    {
        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         marker,
                                                                         PropertyComparisonOperator.STARTS_WITH));

        List<OpenMetadataElement> elements  = new ArrayList<>();
        int                       startFrom = 0;

        while (true)
        {
            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setLimitResultsByStatus(statuses);
            queryOptions.setForLineage(true);
            queryOptions.setStartFrom(startFrom);
            queryOptions.setPageSize(MAX_PAGE_SIZE);

            List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

            if ((found == null) || (found.isEmpty()))
            {
                return elements;
            }

            elements.addAll(found);

            startFrom = startFrom + found.size();
        }
    }


    /**
     * Return the qualified names of every element, in the requested statuses and archived or not, whose qualified
     * name starts with the supplied marker.
     *
     * @param openMetadataStore store to search
     * @param marker qualified name prefix
     * @param statuses statuses to include
     * @return qualified names found; empty if none
     * @throws Exception problem with the search
     */
    static Set<String> findQualifiedNames(OpenMetadataStore   openMetadataStore,
                                          String              marker,
                                          List<ElementStatus> statuses) throws Exception
    {
        Set<String> qualifiedNames = new HashSet<>();

        for (OpenMetadataElement element : findElements(openMetadataStore, marker, statuses))
        {
            qualifiedNames.add(getQualifiedName(element));
        }

        return qualifiedNames;
    }


    /**
     * Return the lastOperationId of every element, in the requested statuses and archived or not, whose qualified
     * name starts with the supplied marker, by qualified name.
     *
     * @param openMetadataStore store to search
     * @param marker qualified name prefix
     * @param statuses statuses to include
     * @return qualified name to lastOperationId
     * @throws Exception problem with the search
     */
    static Map<String, String> findLastOperationIds(OpenMetadataStore   openMetadataStore,
                                                    String              marker,
                                                    List<ElementStatus> statuses) throws Exception
    {
        Map<String, String> operationIds = new LinkedHashMap<>();

        for (OpenMetadataElement element : findElements(openMetadataStore, marker, statuses))
        {
            operationIds.put(getQualifiedName(element), element.getVersions() == null ? null : element.getVersions().getLastOperationId());
        }

        return operationIds;
    }


    /**
     * Return the GUIDs of the relationships of one type linking an element to others at the requested end.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID starting element
     * @param relationshipTypeName relationship to follow
     * @param linkedAtEnd2 true for the relationships where the other element is at end 2
     * @return relationship GUIDs; empty if none
     * @throws Exception problem talking to the server
     */
    static Set<String> getRelationshipGUIDs(OpenMetadataStore openMetadataStore,
                                            String            elementGUID,
                                            String            relationshipTypeName,
                                            boolean           linkedAtEnd2) throws Exception
    {
        Set<String> guids = new HashSet<>();

        RelatedMetadataElementList related = openMetadataStore.getRelatedMetadataElements(elementGUID, 0, relationshipTypeName, 0, MAX_PAGE_SIZE);

        if ((related != null) && (related.getElementList() != null))
        {
            for (RelatedMetadataElement relatedElement : related.getElementList())
            {
                if ((relatedElement != null) && (relatedElement.getElementAtEnd1() != linkedAtEnd2))
                {
                    guids.add(relatedElement.getRelationshipGUID());
                }
            }
        }

        return guids;
    }


    /**
     * Remove a graph, whatever state a test left it in.  The anchor's purge cascades to its members; the
     * glossary term is its own anchor and is purged separately, and anything left over is caught by the
     * leftover cleanup at the start of the next run.
     *
     * @param openMetadataStore store to delete through
     * @param graph graph to remove
     */
    static void purgeGraph(OpenMetadataStore openMetadataStore, AnchoredGraph graph)
    {
        if (graph != null)
        {
            List<String> roots = new ArrayList<>();

            for (String role : List.of(AnchoredGraph.DATABASE_SCHEMA, AnchoredGraph.GLOSSARY_TERM))
            {
                if (graph.guid(role) != null)
                {
                    roots.add(graph.guid(role));
                }
            }

            for (String rootGUID : roots)
            {
                purgeElement(openMetadataStore, rootGUID);
            }
        }
    }


    /**
     * Permanently remove an element, regardless of the delete method the supplied client was set up
     * with.  A cleanup failure does not fail the test, but it is reported rather than discarded: a silently
     * failed purge leaves a soft-deleted element behind that confuses a later test somewhere else entirely.
     * <br><br>
     * PURGE only succeeds on an element that is already soft-deleted, so this soft-deletes the element
     * first - best-effort, since it may already be deleted - before purging it.  forLineage is set on both
     * steps so that an archived (Memento) element is found too.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     * @return true if the element was purged
     */
    static boolean purgeElement(OpenMetadataStore openMetadataStore,
                                String            elementGUID)
    {
        try
        {
            purgeElementOrFail(openMetadataStore, elementGUID);

            return true;
        }
        catch (Exception error)
        {
            System.err.println("anchor-fvt: could not purge element " + elementGUID + " - "
                                       + error.getClass().getSimpleName() + ": " + error.getMessage());

            return false;
        }
    }


    /**
     * Permanently remove an element, failing if it does not go.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     * @throws Exception the element could not be purged
     */
    static void purgeElementOrFail(OpenMetadataStore openMetadataStore,
                                   String            elementGUID) throws Exception
    {
        try
        {
            DeleteOptions softDeleteOptions = getDeleteOptions(DeleteMethod.SOFT_DELETE, true);

            softDeleteOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            /*
             * Ordinary: the element may already be deleted.  The purge below is the step that has to work.
             */
        }

        DeleteOptions purgeOptions = getDeleteOptions(DeleteMethod.PURGE, true);

        purgeOptions.setForLineage(true);

        openMetadataStore.deleteMetadataElementInStore(elementGUID, purgeOptions);
    }


    /**
     * Find and permanently purge every element whose qualified name starts with
     * {@link #QUALIFIED_NAME_PREFIX}, in any status and archived or not.  Called once, after the server is
     * activated and before any test runs, so that debris from an earlier, possibly failed, run does not
     * affect this run's assertions.
     *
     * @throws Exception problem communicating with the server - fatal, since a dirty repository would
     * invalidate the whole run
     */
    static void cleanUpLeftoverTestElements() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         QUALIFIED_NAME_PREFIX,
                                                                         PropertyComparisonOperator.STARTS_WITH));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));
        queryOptions.setPageSize(MAX_PAGE_SIZE);
        queryOptions.setForLineage(true);

        /*
         * Purging shrinks the result set, so keep re-querying from the start until nothing more comes back,
         * rather than paging through a moving target.  An anchor's cascaded purge removes its members too, so
         * some of the elements in a page may already be gone by the time they are reached - that is fine.
         */
        int purgedCount    = 0;
        int emptyPassLimit = 50;

        while (emptyPassLimit > 0)
        {
            List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

            if ((found == null) || found.isEmpty())
            {
                break;
            }

            boolean anyPurgedThisPass = false;

            for (OpenMetadataElement element : found)
            {
                if (purgeElement(openMetadataStore, element.getElementGUID()))
                {
                    purgedCount++;
                    anyPurgedThisPass = true;
                }
            }

            if (! anyPurgedThisPass)
            {
                System.err.println("anchor-fvt: leftover cleanup gave up - " + found.size()
                                           + " element(s) could not be purged");
                break;
            }

            emptyPassLimit--;
        }

        if (purgedCount > 0)
        {
            System.out.println("anchor-fvt: purged " + purgedCount + " leftover test element(s) from a previous run before starting");
        }
    }


    /**
     * Check that exactly the expected elements of a graph are Mementos with "_archivedOn_" qualified names, that
     * they all share one suffix, and that every other element keeps its name and has no Memento.
     *
     * @param openMetadataStore store to read through
     * @param graph graph to check
     * @param expectedArchived the elements that should be archived
     * @throws Exception problem talking to the server
     */
    static void checkArchivedElements(OpenMetadataStore openMetadataStore,
                                      AnchoredGraph     graph,
                                      Set<String>       expectedArchived) throws Exception
    {
        Set<String> archiveSuffixes = new HashSet<>();

        for (String role : graph.roles())
        {
            OpenMetadataElement element       = AnchorFvtTestSupport.getIncludingArchived(openMetadataStore, graph.guid(role));
            String              originalName  = graph.qualifiedNames().get(role);
            String              qualifiedName = AnchorFvtTestSupport.getQualifiedName(element);
            boolean             isMemento     = AnchorFvtTestSupport.getClassification(element, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName) != null;

            if (expectedArchived.contains(role))
            {
                assertTrue(isMemento, role + " was not classified as a Memento");
                assertTrue(qualifiedName.startsWith(originalName + "_archivedOn_"), role + " was not renamed: " + qualifiedName);

                archiveSuffixes.add(qualifiedName.substring(originalName.length()));
            }
            else
            {
                assertFalse(isMemento, role + " was classified as a Memento");
                assertEquals(originalName, qualifiedName, role + " was renamed");
            }
        }

        assertEquals(expectedArchived.isEmpty() ? 0 : 1,
                     archiveSuffixes.size(),
                     "The elements archived together carry different suffixes: " + archiveSuffixes);
    }


    /**
     * Every role in the graph except the ones named.
     *
     * @param graph graph
     * @param excluded roles to leave out
     * @return remaining roles
     */
    static Set<String> getRolesExcept(AnchoredGraph graph, Set<String> excluded)
    {
        Set<String> roles = new HashSet<>(graph.roles());

        roles.removeAll(excluded);

        return roles;
    }

    /**
     * Return the elements linked to an element by one type of relationship, at the requested end.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID starting element
     * @param relationshipTypeName relationship to follow
     * @param linkedAtEnd2 true for the elements at end 2, false for those at end 1
     * @return linked elements; empty if none
     * @throws Exception problem talking to the server
     */
    static List<OpenMetadataElement> getLinkedElements(OpenMetadataStore openMetadataStore,
                                                       String            elementGUID,
                                                       String            relationshipTypeName,
                                                       boolean           linkedAtEnd2) throws Exception
    {
        List<OpenMetadataElement> linkedElements = new ArrayList<>();

        RelatedMetadataElementList related = openMetadataStore.getRelatedMetadataElements(elementGUID, 0, relationshipTypeName, 0, MAX_PAGE_SIZE);

        if ((related != null) && (related.getElementList() != null))
        {
            for (RelatedMetadataElement relatedElement : related.getElementList())
            {
                if ((relatedElement != null) && (relatedElement.getElementAtEnd1() != linkedAtEnd2))
                {
                    linkedElements.add(relatedElement.getElement());
                }
            }
        }

        return linkedElements;
    }


    /**
     * Return the GUIDs of the elements linked to an element by one type of relationship, at the requested end.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID starting element
     * @param relationshipTypeName relationship to follow
     * @param linkedAtEnd2 true for the elements at end 2, false for those at end 1
     * @return linked element GUIDs; empty if none
     * @throws Exception problem talking to the server
     */
    static Set<String> getLinkedGUIDs(OpenMetadataStore openMetadataStore,
                                      String            elementGUID,
                                      String            relationshipTypeName,
                                      boolean           linkedAtEnd2) throws Exception
    {
        Set<String> guids = new HashSet<>();

        for (OpenMetadataElement element : getLinkedElements(openMetadataStore, elementGUID, relationshipTypeName, linkedAtEnd2))
        {
            guids.add(element.getElementGUID());
        }

        return guids;
    }


    /**
     * Find the element in a graph created from the template graph that plays each role, by walking the
     * structure the template had from the new database schema.  The copies are named after the new database
     * schema rather than after their templates, so their names do not say which role they play - the
     * structure does: table1 is the table with two columns, column1a is the column holding the foreign key.
     * The glossary term is not part of the copy - it has an anchor of its own - so it maps to the term
     * the copy is linked to.
     *
     * @param openMetadataStore store to read through
     * @param databaseSchemaGUID the new database schema
     * @return the copy, with each role's GUID and current qualified name
     * @throws Exception problem talking to the server, or a part of the structure is missing
     */
    static AnchoredGraph getCopiedGraph(OpenMetadataStore openMetadataStore,
                                        String            databaseSchemaGUID) throws Exception
    {
        Map<String, String> guids = new LinkedHashMap<>();

        guids.put(AnchoredGraph.DATABASE_SCHEMA, databaseSchemaGUID);
        guids.put(AnchoredGraph.SCHEMA_TYPE, getOnly(getLinkedGUIDs(openMetadataStore, databaseSchemaGUID, OpenMetadataType.SCHEMA_RELATIONSHIP.typeName, true), "schema type"));

        for (String tableGUID : getLinkedGUIDs(openMetadataStore, guids.get(AnchoredGraph.SCHEMA_TYPE), OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, true))
        {
            Set<String> columnGUIDs = getLinkedGUIDs(openMetadataStore, tableGUID, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, true);

            if (columnGUIDs.size() == 2)
            {
                guids.put(AnchoredGraph.TABLE1, tableGUID);

                for (String columnGUID : columnGUIDs)
                {
                    if (getLinkedGUIDs(openMetadataStore, columnGUID, OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, true).isEmpty())
                    {
                        guids.put(AnchoredGraph.COLUMN1B, columnGUID);
                    }
                    else
                    {
                        guids.put(AnchoredGraph.COLUMN1A, columnGUID);
                    }
                }
            }
            else
            {
                guids.put(AnchoredGraph.TABLE2, tableGUID);
                guids.put(AnchoredGraph.COLUMN2A, getOnly(columnGUIDs, "table2 column"));
            }
        }

        guids.put(AnchoredGraph.CONNECTION, getOnly(getLinkedGUIDs(openMetadataStore, databaseSchemaGUID, OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, true), "connection"));
        guids.put(AnchoredGraph.ENDPOINT, getOnly(getLinkedGUIDs(openMetadataStore, guids.get(AnchoredGraph.CONNECTION), OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName, true), "endpoint"));
        guids.put(AnchoredGraph.GLOSSARY_TERM, getOnly(getLinkedGUIDs(openMetadataStore, guids.get(AnchoredGraph.TABLE1), OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName, true), "glossary term"));

        Map<String, String> qualifiedNames = new LinkedHashMap<>();

        for (Map.Entry<String, String> role : guids.entrySet())
        {
            qualifiedNames.put(role.getKey(), getQualifiedName(openMetadataStore.getMetadataElementByGUID(role.getValue())));
        }

        return new AnchoredGraph(qualifiedNames.get(AnchoredGraph.DATABASE_SCHEMA), guids, qualifiedNames);
    }


    /**
     * Return the only member of a set, failing if there is not exactly one.
     *
     * @param guids the set
     * @param description what it should hold, for the failure message
     * @return its only member
     */
    private static String getOnly(Set<String> guids, String description)
    {
        assertEquals(1, guids.size(), "Expected exactly one " + description + " but found " + guids);

        return guids.iterator().next();
    }
}
