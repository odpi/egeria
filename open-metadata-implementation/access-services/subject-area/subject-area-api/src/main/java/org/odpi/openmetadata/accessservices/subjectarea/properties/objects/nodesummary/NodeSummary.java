/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */

package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

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
    protected NodeType type = NodeType.Unknown;
    protected String relationshipType = null;
    private String name = null;
    private String qualifiedName = null;
    private Set<IconSummary> icons = null;
    private String guid = null;
    private Long fromEffectivityTime = null;
    private Long toEffectivityTime = null;
    private String relationshipguid = null;
    private Long fromRelationshipEffectivityTime = null;
    private Long toRelationshipEffectivityTime = null;

    /**
     * Type of the other end of this relationship
     *
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
     *
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
     *
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
     *
     * @return qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * icons
     *
     * @return set of icon summaries
     */
    public Set<IconSummary> getIcons() {
        return icons;
    }

    public void setIcons(Set<IconSummary> icons) {
        this.icons = icons;
    }

    /**
     * A unique identifier for a node
     *
     * @return guid
     */
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * get the date effective from which this node is effective
     *
     * @return Date date effective from which this node is effective
     */
    public Long getFromEffectivityTime() {
        return fromEffectivityTime;
    }

    /**
     * set the date from which this node is effective
     *
     * @param fromEffectivityTime date from which this node is effective
     */
    public void setFromEffectivityTime(Long fromEffectivityTime) {
        this.fromEffectivityTime = fromEffectivityTime;
    }

    /**
     * get the date to which this node is effective
     *
     * @return Date to which this node is effective
     */

    public Long getToEffectivityTime() {
        return toEffectivityTime;
    }

    /**
     * set the date to which this node is effective
     *
     * @param toEffectivityTime date to which this node is effective
     */
    public void setToEffectivityTime(Long toEffectivityTime) {
        this.toEffectivityTime = toEffectivityTime;
    }

    /**
     * The unique identifier of the associated relationship
     *
     * @return relationship guid
     */
    public String getRelationshipguid() {
        return relationshipguid;
    }

    /**
     * The set the unique identifier of the associated relationship
     *
     * @param relationshipguid relationship guid
     */
    public void setRelationshipguid(String relationshipguid) {
        this.relationshipguid = relationshipguid;
    }

    /**
     * get when date from which the relationship is effective
     *
     * @return Date date from which the relationship is effective
     */
    public Long getFromRelationshipEffectivityTime() {
        return fromRelationshipEffectivityTime;
    }

    /**
     * set date from which the relationship is effective
     *
     * @param fromRelationshipEffectivityTime date from which the relationship is effective
     */
    public void setFromRelationshipEffectivityTime(Long fromRelationshipEffectivityTime) {
        this.fromRelationshipEffectivityTime = fromRelationshipEffectivityTime;
    }

    /**
     * get date from which the relationship is effective
     *
     * @return date from which the relationship is effective
     */
    public Long getToRelationshipEffectivityTime() {
        return toRelationshipEffectivityTime;
    }

    /**
     * set date to which the relationship is effective
     *
     * @param toRelationshipEffectivityTime date to which the relationship is effective
     */
    public void setToRelationshipEffectivityTime(Long toRelationshipEffectivityTime) {
        this.toRelationshipEffectivityTime = toRelationshipEffectivityTime;
    }

    public String toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("NodeSummary{");
        if (name != null) {
            sb.append("name='").append(name).append('\'');
        }
        if (qualifiedName != null) {
            sb.append(", qualifiedName='").append(qualifiedName).append('\'');
        }
//TODO Icons
        if (guid != null) {
            sb.append(", guid='").append(guid).append('\'');
        }
        if (fromEffectivityTime != null) {
            sb.append(", fromEffectivityTime='").append(fromEffectivityTime).append('\'');
        }
        if (toEffectivityTime != null) {
            sb.append(", toEffectivityTime='").append(toEffectivityTime).append('\'');
        }

        sb.append('}');

        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(new StringBuilder());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeSummary node = (NodeSummary) o;

        if (!Objects.equals(name, node.name)) return false;
        if (!Objects.equals(fromEffectivityTime, node.fromEffectivityTime)) return false;
        if (!Objects.equals(toEffectivityTime, node.toEffectivityTime)) return false;
        //TODO Icons
        return Objects.equals(qualifiedName, node.qualifiedName);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (qualifiedName != null ? qualifiedName.hashCode() : 0);
        result = 31 * result + (fromEffectivityTime != null ? fromEffectivityTime.hashCode() : 0);
        result = 31 * result + (toEffectivityTime != null ? toEffectivityTime.hashCode() : 0);
//        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        //TODO Icons
        return result;
    }

    // allow child classes to process classifications
    protected void processClassification(Classification classification) {
    }
}
