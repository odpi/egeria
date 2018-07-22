/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.ObjectIdentifier;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SpineAttribute;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SpineObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode.GovernedNode;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TODO
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Term extends GovernedNode implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Term.class);
    private static final String className = Term.class.getName();

    private String glossaryName =null;
    private String summary =null;
    private String abbreviation =null;
    private String examples =null;
    private String usage =null;
    private Set<Asset> assets =null;
    private Set<String> categories = null;
    private boolean isSpineObject =false;
    private boolean isSpineAttribute =false;
    private boolean isObjectIdentifier =false;


    public Term() {
        nodeType = NodeType.Term;
    }

    public String getGlossaryName() {
        return glossaryName;
    }

    public void setGlossaryName(String glossaryName) {
        this.glossaryName = glossaryName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public boolean isSpineObject() {
        return isSpineObject;
    }

    public void setSpineObject(boolean spineObject) {
        isSpineObject = spineObject;
    }

    public boolean isSpineAttribute() {
        return isSpineAttribute;
    }

    public void setSpineAttribute(boolean spineAttribute) {
        isSpineAttribute = spineAttribute;
    }

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


        sb.append("GlossaryTerm Attributes{");
        sb.append("Summary=" +this.summary);

        sb.append("Examples=" +this.examples);
        sb.append("Abbreviation=" +this.abbreviation);
        sb.append("Usage=" +this.usage);
        sb.append("GlossaryName=" +this.glossaryName);
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

        if (glossaryName!=null ){
            sb.append(", glossaryName=");
            sb.append(glossaryName);

        }

        if (categories != null) {
            sb.append(", categories=[");
            for (String category: categories) {
                sb.append("category").append(":");
                sb.append(category);
            }
            sb.append(categories.toString());
            sb.append(" ]");
        }

        if (assets !=null) {
            for (Asset asset: assets) {
                sb.append(asset.getName()).append(":");
                sb.append(asset.toString());
            }
            sb.append(", assets=[").append(assets.toString());
            sb.append(" ],");
        }
        sb.append('}');
        return sb;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;
        Node node = (Node) o;
        if (!(node.equals((Node)o))) return false;
        if (assets != null ? !assets.equals(term.assets) : term.assets != null) return false;
        if (glossaryName != null ? !glossaryName.equals(term.glossaryName) : term.glossaryName != null) return false;
        if (summary != null ? !summary.equals(term.summary) : term.summary != null) return false;
        if (categories != null ? !categories.equals(term.categories) : term.categories != null) return false;
        return  true;
    }

    @Override
    public int hashCode() {
        int  result = ((Node)this).hashCode();
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (glossaryName != null ? glossaryName.hashCode() : 0);
        result = 31 * result + (assets != null ? assets.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        // TODO booleans
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
