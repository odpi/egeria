/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.jsonld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JsonLdGraph holds the nodes read from a JSON-LD document.  The nested nodes of the document are flattened so that
 * every node can be found through its identifier, and the descriptions of a node that appear in different parts of
 * the document are merged.
 */
public class JsonLdGraph
{
    private final Map<String, JsonLdNode> nodes               = new LinkedHashMap<>();
    private final Set<String>             unresolvedContexts  = new LinkedHashSet<>();
    private       int                     blankNodeCount      = 0;


    /**
     * Default constructor
     */
    public JsonLdGraph()
    {
    }


    /**
     * Return the node with the requested identifier, creating it if it is not already known.
     *
     * @param id identifier of the node
     * @return node
     */
    public JsonLdNode getOrCreateNode(String id)
    {
        return nodes.computeIfAbsent(id, JsonLdNode::new);
    }


    /**
     * Return the node with the requested identifier.
     *
     * @param id identifier of the node
     * @return node or null if it is not known
     */
    public JsonLdNode getNode(String id)
    {
        return (id == null) ? null : nodes.get(id);
    }


    /**
     * Return all the nodes in the order that they were first seen in the document.
     *
     * @return collection of nodes
     */
    public Collection<JsonLdNode> getNodes()
    {
        return nodes.values();
    }


    /**
     * Return the nodes of the requested type in the order that they were first seen in the document.
     *
     * @param typeIRI IRI of the type
     * @return list of nodes
     */
    public List<JsonLdNode> getNodesOfType(String typeIRI)
    {
        List<JsonLdNode> results = new ArrayList<>();

        for (JsonLdNode node : nodes.values())
        {
            if (node.hasType(typeIRI))
            {
                results.add(node);
            }
        }

        return results;
    }


    /**
     * Return a new blank node identifier.
     *
     * @return identifier
     */
    public String newBlankNodeId()
    {
        blankNodeCount++;

        return JsonLdNode.BLANK_NODE_PREFIX + "egeria-b" + blankNodeCount;
    }


    /**
     * Record that the document referred to a context that was not embedded in it.
     *
     * @param contextIRI location of the context
     */
    public void addUnresolvedContext(String contextIRI)
    {
        unresolvedContexts.add(contextIRI);
    }


    /**
     * Return the locations of the contexts that the document referred to but did not embed.  These contexts are not
     * retrieved, so terms that only they define are resolved using the standard names of the DCAT vocabulary.
     *
     * @return set of IRIs
     */
    public Set<String> getUnresolvedContexts()
    {
        return unresolvedContexts;
    }
}
