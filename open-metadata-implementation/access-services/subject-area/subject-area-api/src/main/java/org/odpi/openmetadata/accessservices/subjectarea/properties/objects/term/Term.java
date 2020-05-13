/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term;

import com.fasterxml.jackson.annotation.*;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.ObjectIdentifier;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SpineAttribute;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SpineObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode.GovernedNode;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

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
        include = JsonTypeInfo.As.PROPERTY,
        property = "class",
        defaultImpl = Term.class
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Term.class, name = "Term")
        })
public class Term extends GovernedNode implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Term.class);
    private static final String className = Term.class.getName();

    private String summary =null;
    private String abbreviation =null;
    private String examples =null;
    private String usage =null;

    private GlossarySummary glossary =null;
    @JsonProperty(value="isSpineObject")
    private boolean isSpineObject =false;
    @JsonProperty(value="isSpineAttribute")
    private boolean isSpineAttribute =false;
    @JsonProperty(value="isObjectIdentifier")
    private boolean isObjectIdentifier =false;

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
        return isSpineObject;
    }

    public void setSpineObject(boolean spineObject) {
        isSpineObject = spineObject;
    }

    /**
     * Identifies a glossary term that describes an attribute of a spine object.
      * @return <code>true</code> if spine object, false otherwise.
     */
    public boolean isSpineAttribute() {
        return isSpineAttribute;
    }

    public void setSpineAttribute(boolean spineAttribute) {
        isSpineAttribute = spineAttribute;
    }

    /**
     * Identifies a glossary term that describes an attribute that can be used to identify an instance.
     * @return <code>true</code> if term is an identifier, false otherwise.
     */
    public boolean isObjectIdentifier() {
        return isObjectIdentifier;
    }

    public void setObjectIdentifier(boolean objectIdentifier) {
        isObjectIdentifier = objectIdentifier;
    }
    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("term=");
        sb.append(super.toString(sb));
        sb.append("Term Attributes{");
        sb.append("Summary=" +this.summary);

        sb.append("Examples=" +this.examples);
        sb.append("Abbreviation=" +this.abbreviation);
        sb.append("Usage=" +this.usage);
        sb.append("Glossary" +this.glossary);
        sb.append(", SpineInformation=[");

        if (isSpineObject) {
            sb.append("SpineObject ");
        }
        if (isSpineAttribute) {
            sb.append("SpineAttribute ");
        }
        if (isObjectIdentifier) {
            sb.append("SpineIdentifier");
        }
        sb.append("]");

        if (glossary!=null ){
            sb.append(", glossary:" + glossary.toString());

        }
        sb.append('}');
        return sb;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;
        if (!(this.equals((Node)o))) return false;
        if (glossary != null ? !glossary.equals(term.glossary) : term.glossary != null) return false;
        if (summary != null ? !summary.equals(term.summary) : term.summary != null) return false;
        return  true;
    }

    @Override
    public int hashCode() {
        int  result = super.hashCode();
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (glossary!= null ? glossary.hashCode() : 0);
        // not including assets, terms and categories in hashcode as they are relationships
        if (this.isSpineAttribute) {
            result = 31 * result + "isSpineAttribute".hashCode();
        }
        if (this.isSpineObject) {
            result = 31 * result + "isSpineObject".hashCode();
        }
        if (this.isObjectIdentifier) {
            result = 31 * result + "isObjectIdentifier".hashCode();
        }
        return result;
    }

    protected void processClassification(Classification classification) {
        String classificationName = new SpineObject().getClassificationName();
        if (classification.getClassificationName().equals(classificationName)) {
            this.isSpineObject = true;
        }
        classificationName = new SpineAttribute().getClassificationName();
        if (classification.getClassificationName().equals(classificationName)) {
            this.isSpineAttribute = true;
        }
        classificationName = new ObjectIdentifier().getClassificationName();
        if (classification.getClassificationName().equals(classificationName)) {
            this.isObjectIdentifier = true;
        }
    }
}