/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term;

import com.fasterxml.jackson.annotation.*;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.ObjectIdentifier;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SpineAttribute;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SpineObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode.GovernedNode;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A Term in the Subject Area OMAS is a semantic description of something, such as a concept, object, asset, technology, role or group.
 * A Term lives in a Glossary.
 * Terms can be categorised, which gives them context. Categories are hierarchical.
 * Terms can be grouped into projects, for particular groups of people to work with.
 * Terms can be related to other terms and can form ontologies.
 * Terms can be classified.
 * Terms can be semantically assigned to assets.
 *
 * {@literal A term can be a spine object, spine attribute and/or an object identifier. This allows terms to be grouped into objects that have attributes and identity. }
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = Term.class,
        visible = true
)
@JsonSubTypes({ @JsonSubTypes.Type(value = Activity.class, name = "Activity") })
public class Term extends GovernedNode implements Serializable {
    private String summary =null;
    private String abbreviation =null;
    private String examples =null;
    private String usage =null;

    private GlossarySummary glossary =null;
    private List<CategorySummary> categories = null;
    @JsonProperty(value="spineObject")
    private boolean spineObject =false;
    @JsonProperty(value="spineAttribute")
    private boolean spineAttribute =false;
    @JsonProperty(value="objectIdentifier")
    private boolean objectIdentifier =false;

    public Term() {
        nodeType = NodeType.Term;
    }

    /**
     * The associated Glossary
     * @return Glossary summary
     */
    public GlossarySummary getGlossary() {
        return glossary;
    }

    public void setGlossary(GlossarySummary glossary) {
        this.glossary = glossary;
    }

    /**
     * The short description of the glossary term.
     * @return String
     */
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * How this glossary term is abbreviated.
     * @return String
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Examples of this glossary term in use.
     * @return String
     */
    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    /**
     * Further guidance on the use of this glossary term.
     * @return String
     */
    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * Identifies a glossary term that describes a type of spine object.
     * @return <code>true</code> if spine object, false otherwise.
     */
    public boolean isSpineObject() {
        return spineObject;
    }

    public void setSpineObject(boolean spineObject) {
        this.spineObject = spineObject;
    }

    /**
     * Identifies a glossary term that describes an attribute of a spine object.
      * @return <code>true</code> if spine object, false otherwise.
     */
    public boolean isSpineAttribute() {
        return spineAttribute;
    }

    public void setSpineAttribute(boolean spineAttribute) {
        this.spineAttribute = spineAttribute;
    }

    /**
     * Identifies a glossary term that describes an attribute that can be used to identify an instance.
     * @return <code>true</code> if term is an identifier, false otherwise.
     */
    public boolean isObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(boolean objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }
    /**
     * The Categories that categorize this Term
     * @return Category Summaries
     */
    public List<CategorySummary> getCategories() {
        return categories;
    }

    public void setCategories(List<CategorySummary> categories) {
        this.categories = categories;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("term=");
        sb.append(super.toString(sb));
        sb.append("Term Attributes{");
        sb.append("Summary=").append(this.summary);

        sb.append("Examples=").append(this.examples);
        sb.append("Abbreviation=").append(this.abbreviation);
        sb.append("Usage=").append(this.usage);
        sb.append("Glossary").append(this.glossary);
        if (this.categories != null && this.categories.size() >0) {
            sb.append("Categories: [");
            for (CategorySummary categorySummary : this.categories) {
                sb.append("Category=" + categorySummary);
            }
            sb.append("]");
        }
        sb.append(getGovernanceClassifications());
        sb.append(", SpineInformation=[");

        if (spineObject) {
            sb.append("SpineObject ");
        }
        if (spineAttribute) {
            sb.append("SpineAttribute ");
        }
        if (objectIdentifier) {
            sb.append("SpineIdentifier");
        }
        sb.append("]");

        if (glossary!=null ){
            sb.append(", glossary:").append(glossary.toString());

        }
        sb.append('}');
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Term term = (Term) o;

        return spineObject == term.spineObject &&
                spineAttribute == term.spineAttribute &&
                objectIdentifier == term.objectIdentifier &&
                Objects.equals(categories,term.categories) &&
                Objects.equals(summary, term.summary) &&
                Objects.equals(abbreviation, term.abbreviation) &&
                Objects.equals(usage, term.usage) &&
                Objects.equals(getGovernanceClassifications(), term.getGovernanceClassifications()) &&
                Objects.equals(glossary, term.glossary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), summary, usage, abbreviation, glossary, spineObject, spineAttribute, objectIdentifier, categories, getGovernanceClassifications());
    }

    protected void processClassification(Classification classification) {
        String classificationName = new SpineObject().getClassificationName();
        if (classification.getClassificationName().equals(classificationName)) {
            this.spineObject = true;
        }
        classificationName = new SpineAttribute().getClassificationName();
        if (classification.getClassificationName().equals(classificationName)) {
            this.spineAttribute = true;
        }
        classificationName = new ObjectIdentifier().getClassificationName();
        if (classification.getClassificationName().equals(classificationName)) {
            this.objectIdentifier = true;
        }
    }
}