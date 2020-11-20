/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CanonicalVocabulary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Glossary object
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = Glossary.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Taxonomy.class, name = "Taxonomy"),
        @JsonSubTypes.Type(value = CanonicalGlossary.class, name = "CanonicalGlossary")
})
public class Glossary extends Node {
    public Glossary() {
        nodeType = NodeType.Glossary;
    }

    private String usage = null;
    private String language = null;

    /**
     * Guidance on the usage of this glossary content.
     * @return the usage.
     */
    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * The Natural Language used in the glossary.
     *
     * This is a String - there is a Jira raised to enhance Egeria to standardise the language values.
     * https://jira.odpi.org/browse/EGERIA-9
     * @return the natural language
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * The Governance level associated with this glossary
     */

      @Override
    /**
     * The icons associated with this glossary.
     * @return the url of the icon.
     */
    public Set<IconSummary> getIcons() {
        return super.getIcons();
    }

    @Override
    public void processClassification (Classification classification) {
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
        return this.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (usage != null ? usage.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}
