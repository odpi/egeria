/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * The GraphStatistics class enables the packaging and interrogation of a predictive stats
 * for a potential traversal. It does NOT contain the sub-graph - that is in a Graph
 * object.
 * The GraphStatistics includes the nodeGUID of the node on which it is centered and the depth.
 * The GraphStatistics is formed from the result of a traversal with no filtering - it includes
 * 3 maps of type information - one per category - keyed by typeName, where each entry (value)
 * contains the nodeType or LineType name and number of instances of that type in the traversal, i.e:
 *   {  typeName --> { nodeTypeName : <string> , count : <int> } }
 */
public class GraphStatistics {


    // The GraphStatistics class enables the packaging and interrogation of a predictive stats
    // for a potential traversal. It does NOT contain the sub-graph - that is in a Graph
    // object.
    // The GraphStatistics includes the nodeGUID of the node on which it is centered and the depth.
    // The GraphStatistics is formed from the result of a traversal with no filtering - it includes
    // 3 maps of type information - one per category - keyed by typeName, where each entry (value)
    // contains the nodeType or LineType name and number of instances of that type in the traversal, i.e:
    //   {  typeName --> { nodeTypeName : <string> , count : <int> } }
    //


    private String                    nodeGUID;                    // must be non-null
    private Map<String, NodeLineStats>  nodeCounts;       // a map from nodeType name to count
    private Map<String, NodeLineStats>  lineCounts;       // a map from lineType name to count
    private Integer                   depth;                       // the depth of traversal


    public GraphStatistics() {
       // No initialization yet
    }
    public GraphStatistics(String nodeGUID, int depth) {
        this.nodeGUID = nodeGUID;
        this.depth =depth;
    }

    /*
     * Getters for Jackson
     */

    public String getNodeGUID() { return nodeGUID; }

    public Map<String, NodeLineStats> getNodeCounts() { return nodeCounts; }

    public Map<String, NodeLineStats> getLineCounts() {
        return lineCounts;
    }

    public Integer getDepth() { return depth; }

    public void setNodeGUID(String nodeGUID) { this.nodeGUID = nodeGUID; }

    public void setNodeCounts(Map<String, NodeLineStats> nodeCounts) { this.nodeCounts = nodeCounts; }

    public void setLineCounts(Map<String, NodeLineStats> lineCounts) { this.lineCounts = lineCounts; }

    public void setDepth(Integer depth) { this.depth = depth; }

    @Override
    public String toString()
    {
        return "GraphStatistics{" +
                ", nodeGUID=" + nodeGUID +
                ", depth=" + depth +
                ", nodeCounts=" + nodeCounts +
                ", lineCounts=" + lineCounts +
                '}';
    }



}
