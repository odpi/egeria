/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageEdge;
import org.odpi.openmetadata.governanceservers.openlineage.responses.OpenLineageAPIResponse;

import java.util.List;

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
public class LineageVerticesAndEdges  {

    private List<LineageVertex> lineageVertices;
    private List<LineageEdge> lineageEdges;

    public LineageVerticesAndEdges(List<LineageVertex> lineageVertices, List<LineageEdge> lineageEdges) {
        this.lineageVertices = lineageVertices;
        this.lineageEdges = lineageEdges;
    }

    public void setLineageVertices(List<LineageVertex> lineageVertices) {
        this.lineageVertices = lineageVertices;
    }

    public void setLineageEdges(List<LineageEdge> lineageEdges) {
        this.lineageEdges = lineageEdges;
    }

    public List<LineageVertex> getLineageVertices() {
        return lineageVertices;
    }

    public List<LineageEdge> getLineageEdges() {
        return lineageEdges;
    }
}
