/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The result of deriving the relationships across one boundary: the relationships the lineage beneath proves, by
 * upper-layer relationship type, and - for each one - the lower-layer relationships on the paths that prove it that
 * name no information supply chain.  Those are the relationships that can be given a supply chain once the supply
 * chain of the path is known, either from a tagged relationship on the path or from the relationship it proves.
 */
class LineageDerivation
{
    private final Map<String, Set<LinkKey>>                   linksByType         = new HashMap<>();
    private final Map<String, Map<LinkKey, Set<LineageEdge>>> untaggedEdgesByLink = new HashMap<>();


    /**
     * Record a derived relationship.
     *
     * @param upperTypeName type of the derived relationship
     * @param link the derived relationship
     */
    void addLink(String  upperTypeName,
                 LinkKey link)
    {
        linksByType.computeIfAbsent(upperTypeName, typeName -> new HashSet<>()).add(link);
    }


    /**
     * Record the untagged lower-layer relationships on the paths that prove a derived relationship.
     *
     * @param upperTypeName type of the derived relationship
     * @param link the derived relationship
     * @param untaggedEdges untagged relationships on its paths
     */
    void addUntaggedEdges(String           upperTypeName,
                          LinkKey          link,
                          Set<LineageEdge> untaggedEdges)
    {
        if (! untaggedEdges.isEmpty())
        {
            untaggedEdgesByLink.computeIfAbsent(upperTypeName, typeName -> new HashMap<>())
                               .computeIfAbsent(link, key -> new HashSet<>())
                               .addAll(untaggedEdges);
        }
    }


    /**
     * Return the upper-layer relationship types for which something was derived.
     *
     * @return type names
     */
    Set<String> getLinkTypeNames()
    {
        return linksByType.keySet();
    }


    /**
     * Return the derived relationships of a type.
     *
     * @param upperTypeName type of relationship
     * @return derived relationships - empty if there are none
     */
    Set<LinkKey> getLinks(String upperTypeName)
    {
        return linksByType.getOrDefault(upperTypeName, Set.of());
    }


    /**
     * Return the untagged lower-layer relationships on the paths that prove a derived relationship.
     *
     * @param upperTypeName type of the derived relationship
     * @param link the derived relationship
     * @return untagged relationships - empty if there are none
     */
    Set<LineageEdge> getUntaggedEdges(String  upperTypeName,
                                      LinkKey link)
    {
        Map<LinkKey, Set<LineageEdge>> untaggedEdges = untaggedEdgesByLink.get(upperTypeName);

        if (untaggedEdges == null)
        {
            return Set.of();
        }

        return untaggedEdges.getOrDefault(link, Set.of());
    }
}
