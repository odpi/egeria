/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.OmasObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A Node is an entity in the subject area omas that has a type {@code  NodeType}, name, qualified name and description.
 * A node may be in one or more projects.
 * <p>
 * Nodes can be connected with {@code Relationship }s to form graphs. As they may be visualised, so a node has an associated
 * icon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = Node.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Category.class),
        @JsonSubTypes.Type(value = Glossary.class),
        @JsonSubTypes.Type(value = Term.class),
        @JsonSubTypes.Type(value = Project.class)
})
public class Node implements Serializable, OmasObject {
    protected NodeType nodeType = NodeType.Unknown;
    private String name =null;
    private String qualifiedName =null;
    private SystemAttributes systemAttributes=null;
    private boolean readOnly = false;
    private Long effectiveFromTime = null;
    private Long effectiveToTime = null;
    private String description =null;
    protected List<Classification> classifications = null;
    private Set<IconSummary> icons = null;
    private Map<String,String> additionalProperties;

    /**
     * Node type
     * @return the type of the node
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
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

    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    /**
     * Description of the node
     * @return description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The Node is readOnly
     * @return whether read only
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * set whether the node is readOnly
     * @param readOnly readonly flag
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * List of associated classifications
     * @return {@code List<Classification>  }
     */
    public List<Classification> getClassifications() {
        if (classifications == null) {
            classifications = new ArrayList<>();
        }
        return classifications;
    }
    /**
     * If governance action classifications (Retention, Confidence, Confidentiality or Criticality) are supplied then remove them
     * from the classifications and add to the appropriate named field. e.g. Retention will be set in the retention field.
     *
     * @param classifications list of classifications to set on the Node.
     */
    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }
    /**
     * icon summary
     * @return icon
     */
    public Set<IconSummary> getIcons() {
        return icons;
    }

    public void setIcons(Set<IconSummary> icons) {
        this.icons = icons;
    }

    /**
     * Return the date/time that this node should start to be used (null means it can be used from creationTime).
     * @return Date the node becomes effective.
     */
    public Long getEffectiveFromTime()
    {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Long effectiveFromTime)
    {
        this.effectiveFromTime = effectiveFromTime;
    }
    /**
     * Return the date/time that this node should no longer be used.
     *
     * @return Date the node stops being effective.
     */
    public Long getEffectiveToTime()
    {
        return effectiveToTime;
    }

    public void setEffectiveToTime(Long effectiveToTime)
    {
        this.effectiveToTime = effectiveToTime;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Node{");
        if (name !=null) {
            sb.append("name='").append(name).append('\'');
        }
        if (qualifiedName!=null) {
            sb.append(", qualifiedName='").append(qualifiedName).append('\'');
        }

        if (description!=null) {
            sb.append(", description=").append(description);
        }

        if (icons != null) {
            sb.append(", icon='").append(icons).append('\'');
        }
        if (effectiveFromTime!=null) {
            sb.append(", effective from date='").append(effectiveFromTime).append('\'');
        }
        if (effectiveToTime!=null) {
            sb.append(", effective to date='").append(effectiveToTime).append('\'');
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

        Node node = (Node) o;

        if (!Objects.equals(name, node.name)) return false;
        if (!Objects.equals(qualifiedName, node.qualifiedName)) return false;
        if (!Objects.equals(description, node.description)) return false;
        if (!Objects.equals(effectiveFromTime, node.effectiveFromTime)) return false;
        if (!Objects.equals(effectiveToTime, node.effectiveToTime)) return false;
        //TODO deal with icon set properly
        return Objects.equals(icons, node.icons);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (qualifiedName != null ? qualifiedName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (effectiveFromTime !=null ? effectiveFromTime.hashCode() :0);
        result = 31 * result + (effectiveToTime !=null ? effectiveToTime.hashCode() :0);

        //TODO deal with icon set properly
        result = 31 * result + (icons != null ? icons.hashCode() : 0);
        return result;
    }

    // allow child classes to process classifications
    protected void processClassification(Classification classification) {
    }
    /**
     * Set the additional properties.
     * The additional properties are OMRS attributes that exist in the Node, due to a repository defining a Type that subclasses
     * the open types and adds additional properties.
     *
     * These additional properties should be supplied on calls for this node - or they will be lost.
     * @param  additionalProperties the additional properties
     */
    public void setAdditionalProperties(Map<String,String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     * Get the additional properties
     *
     * The additional properties are OMRS attributes that exist in the Node, due to a repository defining a Type that subclasses
     * the open types and adds additional properties.
     *
     * These additional properties should be supplied on calls for this node - or they will be lost.
     * @return the additional properties
     */
    public Map<String,String> getAdditionalProperties() {
        return this.additionalProperties;
    }
}