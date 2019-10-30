/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.responses;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.governanceservers.openlineage.model.edges.LineageEdge;
import org.odpi.openmetadata.governanceservers.openlineage.model.vertices.*;

import java.util.ArrayList;
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
@JsonSubTypes({
        @JsonSubTypes.Type(value = ColumnVertex.class, name = "LineageColumnVertex"),
        @JsonSubTypes.Type(value = TableVertex.class, name = "LineageTableVertex"),
        @JsonSubTypes.Type(value = CondensedVertex.class, name = "LineageCondensedVertex"),
        @JsonSubTypes.Type(value = ProcessVertex.class, name = "LineageProcessVertex"),
        @JsonSubTypes.Type(value = SubProcessVertex.class, name = "LineageSubProcessVertex")
}
)
public class LineageResponse extends OpenLineageAPIResponse{

    private List<LineageVertex> lineageVertices;
    private List<LineageEdge> lineageEdges;


    public LineageResponse() {
        this.lineageVertices = new ArrayList<>();
        this.lineageEdges = new ArrayList<>();
    }

    public void addVertex(LineageVertex lineageVertex){
        this.lineageVertices.add(lineageVertex);
    }

    public void addEdge(LineageEdge lineageEdge) {this.lineageEdges.add(lineageEdge);
    }

    public List<LineageVertex> getLineageVertices() {
        return lineageVertices;
    }

    public List<LineageEdge> getLineageEdges() {
        return lineageEdges;
    }
}
