/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A SummaryNode represents a node in the subject area omas that has a type {@code  NodeType}, relationship type, name and icon.
 * A Summary Node is used when a Node needs to hold a reference to another node. It contains only fields useful for identification.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class NodeSummary implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(NodeSummary.class);
    private static final String className = NodeSummary.class.getName();
    protected NodeType type = NodeType.Unknown;
    protected String relationshipType = null;
    private String name = null;
    private String qualifiedName = null;
    private String icon = null;
    private String guid = null;
    private String relationshipguid = null;

    /**
     * Type of the other end of this relationship
     * @return the type
     */
    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    /**
     * the type of the relationship
     * @return relationship type
     */
    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * The name of the node
     * @return name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The qualified name of the node.
     * @return qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * icon url
     * @return url of icon
     */
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * A unique identifier for a node
     * @return guid
     */
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * The unique identifer of the associated Line (relationship)
     * @return relationship guid
     */
    public String getRelationshipguid() {
        return relationshipguid;
    }

    public void setRelationshipguid(String relationshipguid) {
        this.relationshipguid = relationshipguid;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Nodesummary{");
        if (name != null) {
            sb.append("name='").append(name).append('\'');
        }
        if (qualifiedName != null) {
            sb.append(", qualifiedName='").append(qualifiedName).append('\'');
        }
        if (icon != null) {
            sb.append(", icon='").append(icon).append('\'');
        }
        if (guid != null) {
            sb.append(", guid='").append(guid).append('\'');
        }

        sb.append('}');

        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeSummary node = (NodeSummary) o;

        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (qualifiedName != null ? !qualifiedName.equals(node.qualifiedName) : node.qualifiedName != null)
            return false;
        return (icon != null ? !icon.equals(node.icon) : node.icon != null) == false;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (qualifiedName != null ? qualifiedName.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }

    // allow child classes to process classifications
    protected void processClassification(Classification classification) {
    }

}
