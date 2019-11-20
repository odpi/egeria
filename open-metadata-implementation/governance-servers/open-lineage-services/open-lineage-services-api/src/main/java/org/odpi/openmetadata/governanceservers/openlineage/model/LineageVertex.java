/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

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
public class LineageVertex {

    private String nodeID;
    private String nodeType;
    private String displayName;
    private String guid;
    private Map<String, String> attributes;

    public LineageVertex(){}

    public LineageVertex(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getGuid() {
        return guid;
    }

    public String getNodeID() {
        return nodeID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
