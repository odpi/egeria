/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NeighborhoodHistoricalFindRequest is used by the Subject Area OMAS to specify information for graph find calls.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NeighborhoodHistoricalFindRequest implements Serializable {
    private Date asOfTime = null;
    private Set<NodeType> nodeFilter = null;
    private Set<LineType> lineFilter = null;
    private StatusFilter statusFilter = StatusFilter.ACTIVE;
    private int level = 3;

    /**
     * Default constructor */
    public NeighborhoodHistoricalFindRequest() {

    }

    public Date getAsOfTime() {
        return asOfTime;
    }

    public void setAsOfTime(Date asOfTime) {
        this.asOfTime = asOfTime;
    }

    public Set<NodeType> getNodeFilter() {
        if(nodeFilter == null) {
            nodeFilter = new HashSet<>();
        }

        return nodeFilter;
    }

    public void setNodeFilter(Set<NodeType> nodeFilter) {
        this.nodeFilter = nodeFilter;
    }

    public Set<LineType> getLineFilter() {
        if (lineFilter == null) {
            lineFilter =  new HashSet<>();
        }

        return lineFilter;
    }

    public void setLineFilter(Set<LineType> lineFilter) {
        this.lineFilter = lineFilter;
    }

    public StatusFilter getStatusFilter() {
        if(statusFilter == null) {
            statusFilter = StatusFilter.ACTIVE;
        }

        return statusFilter;
    }

    public void setStatusFilter(StatusFilter statusFilter) {
        this.statusFilter = statusFilter;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeighborhoodHistoricalFindRequest that = (NeighborhoodHistoricalFindRequest) o;
        return level == that.level &&
                Objects.equals(asOfTime, that.asOfTime) &&
                Objects.equals(nodeFilter, that.nodeFilter) &&
                Objects.equals(lineFilter, that.lineFilter) &&
                statusFilter == that.statusFilter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(asOfTime, nodeFilter, lineFilter, statusFilter, level);
    }
}
