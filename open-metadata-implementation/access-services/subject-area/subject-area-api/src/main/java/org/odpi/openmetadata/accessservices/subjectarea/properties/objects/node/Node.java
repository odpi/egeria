/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.ProjectSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A Node is an entity in the subject area omas that has a type {@code  NodeType}, name, qualified name and description.
 * A node may be in one or more projects.
 * <p>
 * Nodes can be connected with {@code Line }s to form graphs. As they may be visualised, so a node has an associated
 * icon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Node implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Node.class);
    private static final String className = Node.class.getName();
    protected NodeType nodeType = NodeType.Unknown;

    private Set<ProjectSummary> projects = null;
    private String name =null;
    private String qualifiedName =null;
    private SystemAttributes systemAttributes=null;
    private Date effectiveFromTime = null;
    private Date effectiveToTime = null;
    private Map<String,String> additionalProperties;
    private String description =null;
    protected List<Classification> classifications = null;
    private Set<IconSummary> icons = null;

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

    /**
     * get the projects
     * @return associated projects
     */
    public Set<ProjectSummary> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectSummary> projects) {
        this.projects = projects;
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
     * List of associated classifications
     * @return {@code List<Classification>  }
     */
    public List<Classification> getClassifications() {
        return classifications;
    }
    /**
     * If governance action classifications (Retention, Confidence, Confidentiality or Criticality) are supplied then remove them
     * from the classifications and add to the appropriate named field. e.g. Retention will be set in the retention field.
     *
     * @param classifications the list of classifications to set on the GovernedNode.
     */
    public void setClassifications(List<Classification> classifications) {
        this.classifications=classifications;
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

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     * Return the date/time that this node should start to be used (null means it can be used from creationTime).
     * @return Date the node becomes effective.
     */
    public Date getEffectiveFromTime()
    {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Date effectiveFromTime)
    {
        this.effectiveFromTime = effectiveFromTime;
    }
    /**
     * Return the date/time that this node should no longer be used.
     *
     * @return Date the node stops being effective.
     */
    public Date getEffectiveToTime()
    {
        return effectiveToTime;
    }

    public void setEffectiveToTime(Date effectiveToTime)
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
            sb.append(", descripion=").append(description);
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

        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (qualifiedName != null ? !qualifiedName.equals(node.qualifiedName) : node.qualifiedName != null)
            return false;
        if (description != null ? !description.equals(node.description) : node.description != null) return false;
        if (effectiveFromTime != null ?!effectiveFromTime.equals(node.effectiveFromTime) : node.effectiveFromTime !=null) return false;
        if (effectiveToTime != null ?!effectiveToTime.equals(node.effectiveToTime) : node.effectiveToTime !=null) return false;
        //TODO deal with icon set properly
        return  (icons != null ? !icons.equals(node.icons) : node.icons != null)== false;

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

}
