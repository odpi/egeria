/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CanonicalVocabulary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Glossary object
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = Project.class,
        visible = true
)
@JsonSubTypes({ @JsonSubTypes.Type(value = GlossaryProject.class, name = "GlossaryProject") })
public class Project extends Node{
    public Project() {
        nodeType = NodeType.Project;
    }

    private String identifier = null;
    private Date startDate = null;
    private Date plannedEndDate = null;
    private String status = null;


    public String getIdentifier() { return identifier; }

    public void setIdentifier(String identifier) { this.identifier = identifier;}

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * The icons associated with this glossary.
     * @return the url of the icon.
     */
    @Override
    public Set<IconSummary> getIcons() {
        return super.getIcons();
    }

    @Override
    public void processClassification (Classification classification) {
        //TODO
        if (classification.getClassificationName().equals("Taxonomy")) {
            if (nodeType == NodeType.CanonicalGlossary || nodeType == NodeType.TaxonomyAndCanonicalGlossary) {
                super.setNodeType(NodeType.TaxonomyAndCanonicalGlossary);
            } else {
                super.setNodeType(NodeType.Taxonomy);
            }
        } else if (classification.getClassificationName().equals(new CanonicalVocabulary().getClassificationName())) {
            if (nodeType == NodeType.Taxonomy || nodeType == NodeType.TaxonomyAndCanonicalGlossary) {
                super.setNodeType(NodeType.TaxonomyAndCanonicalGlossary);
            } else {
                super.setNodeType(NodeType.CanonicalGlossary);
            }
        }
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("Glossary=");
        sb.append(super.toString(sb));

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;
        if (!(this.equals(o))) return false;
        if (!Objects.equals(identifier, project.identifier)) return false;
        if (!Objects.equals(startDate, project.startDate)) return false;
        if (!Objects.equals(plannedEndDate, project.plannedEndDate)) return false;
        return Objects.equals(status, project.status);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (plannedEndDate != null ? plannedEndDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
