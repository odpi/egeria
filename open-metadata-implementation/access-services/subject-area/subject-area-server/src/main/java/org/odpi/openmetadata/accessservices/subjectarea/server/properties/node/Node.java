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
package org.odpi.openmetadata.accessservices.subjectarea.server.properties.node;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.odpi.openmetadata.accessservices.subjectarea.common.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidentiality.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Criticality.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.ConfidenceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.ConfidentialityLevel;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.CriticalityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TODO
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Node implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.Node.class);
    private static final String className = org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.Node.class.getName();
    protected NodeType nodeType = NodeType.Unknown;

    private Set<String> projects = null;
    private String name =null;
    private String qualifiedName =null;
    private SystemAttributes systemAttributes=null;
    private Map<String,String> additionalProperties;
    private String description =null;
    private List<org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel> governanceLevels = null;
    private Retention retention = null;

    private String icon = null;

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public Set<String> getProjects() {
        return projects;
    }

    public void setProjects(Set<String> projects) {
        this.projects = projects;
    }

    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel> getGovernanceLevels() {
        return governanceLevels;
    }

    public void setGovernanceLevels(List<org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel> governanceLevels) {
        this.governanceLevels = governanceLevels;
    }

    public Retention getRetention() {
        return retention;
    }

    public void setRetention(Retention retention) {
        this.retention = retention;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
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
        if (governanceLevels !=null) {
            sb.append(", Governance levels=[");
            for (org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel governanceClassification: governanceLevels) {
                sb.append(governanceClassification.getClassificationName()).append(":");
                sb.append(governanceLevels.toString());
            }
            sb.append(governanceLevels.toString());
            sb.append(" ]");
        }
        if (retention !=null) {
            sb.append(", retention='").append(retention.toString()).append('\'');

        }


        if (icon != null) {
            sb.append(", icon='").append(icon).append('\'');
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

        org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.Node node = (org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.Node) o;

        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (qualifiedName != null ? !qualifiedName.equals(node.qualifiedName) : node.qualifiedName != null)
            return false;
        if (description != null ? !description.equals(node.description) : node.description != null) return false;
        if (governanceLevels != null ? !governanceLevels.equals(node.governanceLevels) : node.governanceLevels != null)
            return false;
        if (retention != null ? !retention.equals(node.retention) : node.retention != null) return false;
        return  (icon != null ? !icon.equals(node.icon) : node.icon != null)== false;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (qualifiedName != null ? qualifiedName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (governanceLevels != null ? governanceLevels.hashCode() : 0);
        result = 31 * result + (retention != null ? retention.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }

    public void setClassifications(List<Classification> classifications) {
        if (classifications!=null && classifications.size() >0) {
            List<org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel> governanceLevels = new ArrayList<>();
            for (Classification classification : classifications) {
                String classificationName = new Confidentiality().getClassificationName();

                if (classification.getClassificationName().equals(classificationName)) {
                    org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel governanceClassification = new org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel();
                    governanceClassification.setClassificationName(classificationName);
                    Confidentiality confidentiality = (Confidentiality) classification;
                    ConfidentialityLevel confidentialityLevel = confidentiality.getLevel();
                    GovernanceClassificationLevel level = new GovernanceClassificationLevel();
                    level.setLevel(confidentialityLevel.ordinal());
                    level.setName(confidentialityLevel.name());
                    governanceClassification.setLevel(level);
                    //TODO other attributes?
                    governanceLevels.add(governanceClassification);
                }
                classificationName = new Confidence().getClassificationName();
                if (classification.getClassificationName().equals(classificationName)) {
                    org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel governanceClassification = new org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel();
                    governanceClassification.setClassificationName(classificationName);
                    Confidence confidence = (Confidence) classification;
                    ConfidenceLevel confidenceLevel = confidence.getLevel();
                    GovernanceClassificationLevel level = new GovernanceClassificationLevel();
                    level.setLevel(confidenceLevel.ordinal());
                    level.setName(confidenceLevel.name());
                    governanceClassification.setLevel(level);
                    //TODO other attributes?
                    governanceLevels.add(governanceClassification);
                }
                classificationName = new Criticality().getClassificationName();
                if (classification.getClassificationName().equals(classificationName)) {
                    org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel governanceClassification = new org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel();
                    governanceClassification.setClassificationName(classificationName);
                    Criticality criticality = (Criticality) classification;
                    CriticalityLevel criticalityLevel = criticality.getLevel();
                    GovernanceClassificationLevel level = new GovernanceClassificationLevel();
                    level.setLevel(criticalityLevel.ordinal());
                    level.setName(criticalityLevel.name());
                    governanceClassification.setLevel(level);
                    //TODO other attributes?
                    governanceLevels.add(governanceClassification);
                }
                classificationName = new Retention().getClassificationName();
                if (classification.getClassificationName().equals(classificationName)) {
                    Retention retention = (Retention) classification;
                    this.setRetention(retention);
                }
                processClassification(classification);
            }
            this.setGovernanceLevels(governanceLevels);
        }
    }
    // allow child classes to process classifications
    protected void processClassification(Classification classification) {
    }

}
