/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class"
)
public class LineageVerticesAndEdges implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<LineageVertex> lineageVertices;
    private Set<LineageEdge>   lineageEdges;

    public LineageVerticesAndEdges(){}

    public LineageVerticesAndEdges(Set<LineageVertex> lineageVertices, Set<LineageEdge> lineageEdges) {
        this.lineageVertices = lineageVertices;
        this.lineageEdges = lineageEdges;
    }

    public void setLineageVertices(Set<LineageVertex> lineageVertices) {
        this.lineageVertices = lineageVertices;
    }

    public void setLineageEdges(Set<LineageEdge> lineageEdges) {
        this.lineageEdges = lineageEdges;
    }

    public Set<LineageVertex> getLineageVertices() {
        return lineageVertices;
    }

    public Set<LineageEdge> getLineageEdges() {
        return lineageEdges;
    }

    @Override
    public String toString() {
        return "LineageVerticesAndEdges{" +
                "lineageVertices=" + lineageVertices +
                ", lineageEdges=" + lineageEdges +
                '}';
    }
}
