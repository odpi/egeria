/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.Objects;

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
public class LineageEdge implements Serializable {

    private static final long serialVersionUID = 1L;

    public LineageEdge(){}

    protected Object id;
    protected String edgeType;
    protected String sourceNodeID;
    protected String destinationNodeID;

    public LineageEdge(Object id, String edgeType, String sourceNodeID, String destinationNodeID) {
        this.id = id;
        this.edgeType = edgeType;
        this.sourceNodeID = sourceNodeID;
        this.destinationNodeID = destinationNodeID;
    }

    public Object getId(){
        return id;
    }
    public String getEdgeType() {
        return edgeType;
    }
    public String getSourceNodeID() {
        return sourceNodeID;
    }
    public String getDestinationNodeID() {
        return destinationNodeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageEdge that = (LineageEdge) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(edgeType, that.edgeType) &&
                Objects.equals(sourceNodeID, that.sourceNodeID) &&
                Objects.equals(destinationNodeID, that.destinationNodeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, edgeType, sourceNodeID, destinationNodeID);
    }
}
