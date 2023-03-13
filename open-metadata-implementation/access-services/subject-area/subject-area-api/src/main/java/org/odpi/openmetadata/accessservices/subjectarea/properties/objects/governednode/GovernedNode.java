/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceClassifications;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A governed Node is a node that can have associated governance classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernedNode extends Node implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(GovernedNode.class);
    private static final String className = GovernedNode.class.getName();

    private GovernanceClassifications governanceClassifications = null;


    public GovernanceClassifications getGovernanceClassifications() {
        return governanceClassifications;
    }

    public void setGovernanceClassifications(GovernanceClassifications governanceClassifications) {
        this.governanceClassifications = governanceClassifications;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Node{");
        sb.append("node='").append(super.toString(sb)).append('\'');

        if (governanceClassifications !=null) {
            sb.append(",governanceClassifications=").append(governanceClassifications);
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
        if (!super.equals(o)) return false;
        GovernedNode that = (GovernedNode) o;
        return Objects.equals(governanceClassifications, that.governanceClassifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), governanceClassifications);
    }

    /**
     * If governance action classifications (Retention, Confidence, Confidentiality or Criticality) are supplied then remove them
     * from the classifications and add to the appropriate named field. e.g. Retention will be set in the retention field.
     *
     * @param classifications the list of classifications to set on the GovernedNode.
     */
    @JsonIgnore
    public void setClassifications(List<Classification> classifications) {
        if (CollectionUtils.isNotEmpty(classifications)) {
            List<Classification> newClassifications = new ArrayList<>();
            if (this.governanceClassifications ==null) {
                this.governanceClassifications = new GovernanceClassifications();
            }

            for (Classification classification : classifications) {
                if (classification.getClassificationName().equals(new Confidentiality().getClassificationName())) {
                    this.governanceClassifications.setConfidentiality((Confidentiality) classification);
                } else if (classification.getClassificationName().equals(new Confidence().getClassificationName())) {
                    this.governanceClassifications.setConfidence((Confidence) classification);
                } else if (classification.getClassificationName().equals(new Criticality().getClassificationName())) {
                    this.governanceClassifications.setCriticality((Criticality) classification);
                } else if (classification.getClassificationName().equals(new Retention().getClassificationName())) {
                    this.governanceClassifications.setRetention((Retention) classification);
                } else {
                    newClassifications.add(classification);
                }
                processClassification(classification);
            }
            this.classifications=newClassifications;
        }
    }
}
