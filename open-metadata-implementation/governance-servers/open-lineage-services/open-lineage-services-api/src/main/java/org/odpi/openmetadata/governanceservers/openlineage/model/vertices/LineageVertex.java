/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model.vertices;

import com.fasterxml.jackson.annotation.*;

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
        @JsonSubTypes.Type(value = ColumnVertex.class, name = "ColumnVertex"),
        @JsonSubTypes.Type(value = TableVertex.class, name = "TableVertex"),
        @JsonSubTypes.Type(value = CondensedVertex.class, name = "CondensedVertex"),
        @JsonSubTypes.Type(value = ProcessVertex.class, name = "ProcessVertex"),
        @JsonSubTypes.Type(value = SubProcessVertex.class, name = "SubProcessVertex")
}
)
public class LineageVertex {

    protected String nodeID;
    protected String nodeType;
    protected String guid;

    public LineageVertex(String nodeID, String nodeType, String guid){
        this.nodeID = nodeID;
        this.nodeType = nodeType;
        this.guid = guid;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }
}
