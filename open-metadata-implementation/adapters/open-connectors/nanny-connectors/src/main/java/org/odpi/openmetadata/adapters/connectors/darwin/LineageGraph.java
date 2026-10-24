/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The lineage relationships of one layer, held in memory and indexed by supplier so that the walk downstream from an
 * element needs no further retrievals.  The graph is loaded once per refresh from a sweep of each relationship type
 * the layer uses, which is far cheaper than asking the repository for the neighbours of every element a walk reaches -
 * the same elements are reached from many starting points.  The graph also holds the equivalences between elements
 * of the layer - a data set and the content it is a view over (DataSetContent) - which a walk treats as the same
 * element.
 */
class LineageGraph
{
    private final Map<String, List<LineageEdge>>        edgesBySupplier       = new HashMap<>();
    private final Map<String, List<LineageEquivalence>> equivalencesByElement = new HashMap<>();


    /**
     * An element that is equivalent to another, for the paths of one information supply chain.
     *
     * @param elementGUID unique identifier of the equivalent element
     * @param iscQualifiedName supply chain the equivalence holds for - null for any supply chain
     */
    record LineageEquivalence(String elementGUID,
                              String iscQualifiedName)
    {
    }


    /**
     * Add a lineage relationship to the graph.
     *
     * @param edge relationship, oriented from supplier to consumer
     */
    void addEdge(LineageEdge edge)
    {
        edgesBySupplier.computeIfAbsent(edge.supplierGUID(), guid -> new ArrayList<>()).add(edge);
    }


    /**
     * Record that two elements are equivalent - a data set and its content.  The equivalence works both ways.
     *
     * @param elementGUID one element
     * @param otherElementGUID the other element
     * @param iscQualifiedName supply chain the equivalence holds for - null for any supply chain
     */
    void addEquivalence(String elementGUID,
                        String otherElementGUID,
                        String iscQualifiedName)
    {
        if ((elementGUID != null) && (otherElementGUID != null) && (! elementGUID.equals(otherElementGUID)))
        {
            equivalencesByElement.computeIfAbsent(elementGUID, guid -> new ArrayList<>()).add(new LineageEquivalence(otherElementGUID, iscQualifiedName));
            equivalencesByElement.computeIfAbsent(otherElementGUID, guid -> new ArrayList<>()).add(new LineageEquivalence(elementGUID, iscQualifiedName));
        }
    }


    /**
     * Return the elements equivalent to an element.
     *
     * @param elementGUID unique identifier of the element
     * @return equivalent elements - empty if there are none
     */
    List<LineageEquivalence> getEquivalents(String elementGUID)
    {
        List<LineageEquivalence> equivalents = equivalencesByElement.get(elementGUID);

        if (equivalents == null)
        {
            return List.of();
        }

        return equivalents;
    }


    /**
     * Return the lineage relationships that carry data away from an element.
     *
     * @param elementGUID unique identifier of the element
     * @return relationships - empty if there are none
     */
    List<LineageEdge> getDownstreamEdges(String elementGUID)
    {
        List<LineageEdge> edges = edgesBySupplier.get(elementGUID);

        if (edges == null)
        {
            return List.of();
        }

        return edges;
    }


    /**
     * Return the elements that supply data to at least one other element.  These are the only elements a walk
     * downstream can start from.
     *
     * @return unique identifiers of the suppliers
     */
    Set<String> getSupplierGUIDs()
    {
        return edgesBySupplier.keySet();
    }
}
