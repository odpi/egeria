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
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A governed Node is a node that can have associated governance actions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
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

        GovernedNode governedNode = (GovernedNode) o;
        Node node = (Node) o;
        if (!node.equals((Node)this)) return false;

        if (governanceActions != null ? !governanceActions.equals(governedNode.governanceActions) : governedNode.governanceActions != null)
            return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
            result = 31 * result + (governanceActions != null ? governanceActions.hashCode() : 0);
        return result;
    }

    /**
     * If governance action classifications (Retention, Confidence, Confidentiality or Criticality) are supplied then remove them
     * from the classifications and add to the appropriate named field. e.g. Retention will be set in the retention field.
     *
     * @param classifications
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
