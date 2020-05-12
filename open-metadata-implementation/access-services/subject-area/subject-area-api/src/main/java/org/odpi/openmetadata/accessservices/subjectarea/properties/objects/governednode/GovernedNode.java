/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
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
 * A governed Node is a node that can have associated governance actions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernedNode extends Node implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(GovernedNode.class);
    private static final String className = GovernedNode.class.getName();

    private GovernanceActions governanceActions = null;


    public GovernanceActions getGovernanceActions() {
        return governanceActions;
    }

    public void setGovernanceActions(GovernanceActions governanceActions) {
        this.governanceActions = governanceActions;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Node{");
        sb.append("node='").append(super.toString(sb)).append('\'');

        if (governanceActions!=null) {
            sb.append(",goveranceActions=").append(governanceActions);
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
        return Objects.equals(governanceActions, that.governanceActions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), governanceActions);
    }

    /**
     * If governance action classifications (Retention, Confidence, Confidentiality or Criticality) are supplied then remove them
     * from the classifications and add to the appropriate named field. e.g. Retention will be set in the retention field.
     *
     * @param classifications the list of classifications to set on the GovernedNode.
     */
    public void setClassifications(List<Classification> classifications) {
        if (classifications!=null && classifications.size() >0) {
            List<Classification> newClassifications = new ArrayList<>();
            if (this.governanceActions ==null) {
                this.governanceActions = new GovernanceActions();
            }

            for (Classification classification : classifications) {
                if (classification.getClassificationName().equals(new Confidentiality().getClassificationName())) {
                    this.governanceActions.setConfidentiality((Confidentiality) classification);
                } else if (classification.getClassificationName().equals(new Confidence().getClassificationName())) {
                    this.governanceActions.setConfidence((Confidence) classification);
                } else if (classification.getClassificationName().equals(new Criticality().getClassificationName())) {
                    this.governanceActions.setCriticality((Criticality) classification);
                } else if (classification.getClassificationName().equals(new Retention().getClassificationName())) {
                    this.governanceActions.setRetention((Retention) classification);
                } else {
                    newClassifications.add(classification);
                }
                processClassification(classification);
            }
            this.classifications=newClassifications;
        }
    }
}
