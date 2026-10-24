/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Derives the relationships in an upper layer that the lineage paths in the layer below prove.  The walker works on a
 * LineageGraph and an owner function, so it needs no repository of its own.  One walker is used for one refresh: it
 * keeps a count of the paths it abandoned for running on longer than the connector's maxLineageDepth.
 */
class LineageWalker
{
    private static final Logger log = LoggerFactory.getLogger(LineageWalker.class);

    private final int maxLineageDepth;
    private int       abandonedPaths = 0;


    /**
     * A point reached while following the lineage downstream: an element, together with what is known so far about
     * the path that reached it.  The same element reached along paths of different supply chains or families is a
     * different point, because the paths go on differently from there.
     *
     * @param elementGUID unique identifier of the element reached
     * @param iscQualifiedName information supply chain of the path - null while no relationship on it names one
     * @param pathFamily the family of relationships the path is made of - null for the shared family (see LineageBoundary)
     * @param arrivalTypeName type of the relationship the element was first reached by - null at the start
     * @param depth number of lineage relationships followed to reach it first
     */
    private record PathPoint(String elementGUID,
                             String iscQualifiedName,
                             String pathFamily,
                             String arrivalTypeName,
                             int    depth)
    {
        /**
         * Return the identity of the point - the element, the supply chain and the family.
         *
         * @return key
         */
        String key()
        {
            return elementGUID + "|" + iscQualifiedName + "|" + pathFamily;
        }
    }


    /**
     * One way a point was reached: the point the walk came from and the relationship it followed.
     *
     * @param previousKey key of the point the walk came from
     * @param edge the relationship followed
     */
    private record Arrival(String      previousKey,
                           LineageEdge edge)
    {
    }


    /**
     * An owner of the element at a point, and the supply chain of the relationship derived when the point is reached.
     *
     * @param ownerGUID unique identifier of the owner
     * @param iscQualifiedName supply chain of the derived relationship - may be null
     */
    private record OwnerMatch(String ownerGUID,
                              String iscQualifiedName)
    {
    }


    /**
     * Constructor.
     *
     * @param maxLineageDepth the number of relationships a path may follow before it is abandoned, for the
     *                        boundaries that do not set their own limit
     */
    LineageWalker(int maxLineageDepth)
    {
        this.maxLineageDepth = maxLineageDepth;
    }


    /**
     * Return the number of paths abandoned so far for running on longer than maxLineageDepth.
     *
     * @return count
     */
    int getAbandonedPaths()
    {
        return abandonedPaths;
    }


    /**
     * Derive the relationships across a boundary.  The lineage is followed downstream from each starting element, on
     * behalf of each of the starting element's owners.  Each path keeps to a single path family - fixed by the first
     * relationship out of the starting element - and a single information supply chain.  A relationship that names no
     * supply chain is treated as "supply chain unknown" rather than "no supply chain": it may appear anywhere on a
     * path, and the first relationship that does name one fixes the supply chain for the whole path.  A relationship
     * that names a different supply chain cannot join the path.  A path stops when it reaches an element that has any
     * owner - the relationship with whatever lies beyond belongs to that owner rather than this one - or when it has
     * gone on for as long as the boundary allows.
     * <p>
     * Where the path reaches an element with a different owner from the one it started for, a relationship is
     * derived between the two owners, oriented as the boundary says and tagged with the path's supply chain - null
     * if no relationship on the path names one.  Its type is the upper-layer type that the boundary maps the
     * relationship reaching the element to.  Lineage between two elements with the same owner is not a relationship
     * of the owner with itself.  For each derived relationship, the untagged relationships on every path that proves
     * it are recorded too.
     * <p>
     * Where the boundary follows an equivalence (a data set and the content it is a view over), equivalent elements are
     * treated as the same element: a walk from an element also starts from its equivalents, and an element reached is
     * owned by its equivalents' owners as well as its own.  An equivalence that names a supply chain only holds for
     * the paths of that supply chain, or of an unknown one - which it then makes known.
     *
     * @param boundary the boundary being crossed
     * @param graph the lineage of the lower layer
     * @param owners the owner function of the boundary
     * @param startElementGUIDs the lower-layer elements to start from - those without an owner are skipped
     * @return the relationships the lineage proves, with the untagged relationships on their paths
     * @throws Exception the owner function failed - reported by the caller
     */
    LineageDerivation deriveLinks(LineageBoundary    boundary,
                                  LineageGraph       graph,
                                  LineageOwners      owners,
                                  Collection<String> startElementGUIDs) throws Exception
    {
        LineageDerivation derivation = new LineageDerivation();

        for (String startElementGUID : startElementGUIDs)
        {
            for (String startOwnerGUID : owners.getOwners(startElementGUID))
            {
                this.followLineageDownstream(boundary, graph, owners, startOwnerGUID, startElementGUID, derivation);
            }
        }

        return derivation;
    }


    /**
     * Follow the lineage downstream from one element on behalf of one of its owners.  This is a breadth-first walk
     * over points (an element with the supply chain and family of the path that reached it).  Every way a point is
     * reached is remembered, so that once the walk is over the untagged relationships on all the paths to each
     * owned element can be collected.
     *
     * @param boundary the boundary being crossed
     * @param graph the lineage of the lower layer
     * @param owners the owner function of the boundary
     * @param startOwnerGUID the owner the walk is on behalf of
     * @param startElementGUID the element the walk starts from
     * @param derivation the relationships found so far - added to
     * @throws Exception the owner function failed - reported by the caller
     */
    private void followLineageDownstream(LineageBoundary   boundary,
                                         LineageGraph      graph,
                                         LineageOwners     owners,
                                         String            startOwnerGUID,
                                         String            startElementGUID,
                                         LineageDerivation derivation) throws Exception
    {
        int                         maxSteps      = (boundary.maxSteps > 0) ? boundary.maxSteps : maxLineageDepth;
        Deque<PathPoint>            pointsToVisit = new ArrayDeque<>();
        Map<String, List<Arrival>>  arrivals      = new HashMap<>();
        Map<PathPoint, List<OwnerMatch>> ownersReached = new HashMap<>();

        List<PathPoint> starts = new ArrayList<>();

        starts.add(new PathPoint(startElementGUID, null, null, null, 0));

        if (boundary.equivalenceRelationshipTypeName != null)
        {
            /*
             * The walk also starts from the elements equivalent to the starting element - such as the content of a
             * data set - for the supply chain of the equivalence.
             */
            for (LineageGraph.LineageEquivalence equivalent : graph.getEquivalents(startElementGUID))
            {
                starts.add(new PathPoint(equivalent.elementGUID(), equivalent.iscQualifiedName(), null, null, 0));
            }
        }

        for (PathPoint start : starts)
        {
            if (! arrivals.containsKey(start.key()))
            {
                arrivals.put(start.key(), new ArrayList<>());

                this.queueDownstreamNeighbours(boundary, graph, start, true, arrivals, pointsToVisit);
            }
        }

        while (! pointsToVisit.isEmpty())
        {
            PathPoint        point       = pointsToVisit.poll();
            List<OwnerMatch> pointOwners = this.getOwners(boundary, graph, owners, point);

            if (! pointOwners.isEmpty())
            {
                ownersReached.put(point, pointOwners);

                continue;
            }

            if (point.depth() >= maxSteps)
            {
                /*
                 * A boundary with its own limit stops its paths there by design; only a path cut short by the
                 * configured depth is a path that might have proved something.
                 */
                if (boundary.maxSteps == 0)
                {
                    abandonedPaths++;

                    if (log.isDebugEnabled())
                    {
                        log.debug("Abandoning lineage path from " + startElementGUID + " at " + point.elementGUID() +
                                          " for information supply chain " + point.iscQualifiedName() + " after " + point.depth() + " steps");
                    }
                }

                continue;
            }

            this.queueDownstreamNeighbours(boundary, graph, point, false, arrivals, pointsToVisit);
        }

        /*
         * The walk is over, so every way of reaching each owned element is known.
         */
        for (Map.Entry<PathPoint, List<OwnerMatch>> reached : ownersReached.entrySet())
        {
            PathPoint        point         = reached.getKey();
            String           upperTypeName = boundary.getUpperTypeName(point.arrivalTypeName());
            Set<LineageEdge> untaggedEdges = null;

            for (OwnerMatch ownerReached : reached.getValue())
            {
                if (! ownerReached.ownerGUID().equals(startOwnerGUID))
                {
                    LinkKey link;

                    if (boundary.consumerAtEnd1)
                    {
                        link = new LinkKey(ownerReached.ownerGUID(), startOwnerGUID, ownerReached.iscQualifiedName());
                    }
                    else
                    {
                        link = new LinkKey(startOwnerGUID, ownerReached.ownerGUID(), ownerReached.iscQualifiedName());
                    }

                    if (untaggedEdges == null)
                    {
                        untaggedEdges = this.getUntaggedEdges(point.key(), arrivals);
                    }

                    derivation.addLink(upperTypeName, link);
                    derivation.addUntaggedEdges(upperTypeName, link, untaggedEdges);
                }
            }
        }
    }


    /**
     * Return the owners of the element at a point: its own owners, and - where the boundary follows an equivalence -
     * the owners of its equivalent elements, for the equivalences whose supply chain fits the path's.  An owner found
     * through an equivalence that names a supply chain makes the supply chain of a path of unknown supply chain known.
     *
     * @param boundary the boundary being crossed
     * @param graph the lineage of the lower layer, with its equivalences
     * @param owners the owner function of the boundary
     * @param point the point reached
     * @return owners, each with the supply chain of the relationship derived for it - empty if there are none
     * @throws Exception the owner function failed - reported by the caller
     */
    private List<OwnerMatch> getOwners(LineageBoundary boundary,
                                       LineageGraph    graph,
                                       LineageOwners   owners,
                                       PathPoint       point) throws Exception
    {
        List<OwnerMatch> ownerMatches = new ArrayList<>();
        Set<String>      ownersFound  = new HashSet<>();

        for (String ownerGUID : owners.getOwners(point.elementGUID()))
        {
            if (ownersFound.add(ownerGUID))
            {
                ownerMatches.add(new OwnerMatch(ownerGUID, point.iscQualifiedName()));
            }
        }

        if (boundary.equivalenceRelationshipTypeName != null)
        {
            for (LineageGraph.LineageEquivalence equivalent : graph.getEquivalents(point.elementGUID()))
            {
                if ((point.iscQualifiedName() != null) && (equivalent.iscQualifiedName() != null) &&
                        (! point.iscQualifiedName().equals(equivalent.iscQualifiedName())))
                {
                    continue;
                }

                String supplyChain = (point.iscQualifiedName() != null) ? point.iscQualifiedName() : equivalent.iscQualifiedName();

                for (String ownerGUID : owners.getOwners(equivalent.elementGUID()))
                {
                    if (ownersFound.add(ownerGUID))
                    {
                        ownerMatches.add(new OwnerMatch(ownerGUID, supplyChain));
                    }
                }
            }
        }

        return ownerMatches;
    }


    /**
     * Add the points immediately downstream of a point to the points still to visit, and remember how each was
     * reached.  A relationship can extend the path when it is of the path's family (any family at the start of the
     * walk) and names the path's supply chain, or no supply chain, or the path's supply chain is still unknown.
     *
     * @param boundary the boundary being crossed, which gives the path family of each relationship
     * @param graph the lineage of the lower layer
     * @param point the point whose downstream neighbours are wanted
     * @param startOfPath true at the start of a walk, when the first relationship fixes the family of the path
     * @param arrivals the ways each point has been reached, by point key - added to
     * @param pointsToVisit queue to add to
     */
    private void queueDownstreamNeighbours(LineageBoundary            boundary,
                                           LineageGraph               graph,
                                           PathPoint                  point,
                                           boolean                    startOfPath,
                                           Map<String, List<Arrival>> arrivals,
                                           Deque<PathPoint>           pointsToVisit)
    {
        for (LineageEdge edge : graph.getDownstreamEdges(point.elementGUID()))
        {
            String edgeFamily = boundary.getPathFamily(edge.relationshipTypeName());

            if ((! startOfPath) && (! Objects.equals(point.pathFamily(), edgeFamily)))
            {
                continue;
            }

            if ((point.iscQualifiedName() != null) && (edge.iscQualifiedName() != null) &&
                    (! point.iscQualifiedName().equals(edge.iscQualifiedName())))
            {
                continue;
            }

            String pathSupplyChain = (point.iscQualifiedName() != null) ? point.iscQualifiedName() : edge.iscQualifiedName();

            PathPoint next = new PathPoint(edge.consumerGUID(), pathSupplyChain, edgeFamily, edge.relationshipTypeName(), point.depth() + 1);

            /*
             * A point reached before is not visited again - which is also what stops a cycle in the lineage being
             * followed for ever - but the new way of reaching it is remembered.
             */
            List<Arrival> nextArrivals = arrivals.get(next.key());

            if (nextArrivals == null)
            {
                nextArrivals = new ArrayList<>();
                arrivals.put(next.key(), nextArrivals);
                pointsToVisit.add(next);
            }

            nextArrivals.add(new Arrival(point.key(), edge));
        }
    }


    /**
     * Collect the untagged relationships on every path from the start of the walk to a point, by working back through
     * the ways each point was reached.
     *
     * @param pointKey key of the point
     * @param arrivals the ways each point was reached, by point key
     * @return the untagged relationships
     */
    private Set<LineageEdge> getUntaggedEdges(String                     pointKey,
                                              Map<String, List<Arrival>> arrivals)
    {
        Set<LineageEdge> untaggedEdges = new HashSet<>();
        Set<String>      visited       = new HashSet<>();
        Deque<String>    pointsToVisit = new ArrayDeque<>();

        pointsToVisit.add(pointKey);

        while (! pointsToVisit.isEmpty())
        {
            String key = pointsToVisit.poll();

            if (visited.add(key))
            {
                for (Arrival arrival : arrivals.getOrDefault(key, List.of()))
                {
                    if (arrival.edge().iscQualifiedName() == null)
                    {
                        untaggedEdges.add(arrival.edge());
                    }

                    pointsToVisit.add(arrival.previousKey());
                }
            }
        }

        return untaggedEdges;
    }
}
