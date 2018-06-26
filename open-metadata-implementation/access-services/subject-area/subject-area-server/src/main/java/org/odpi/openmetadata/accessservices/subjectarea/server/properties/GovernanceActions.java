
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
package org.odpi.openmetadata.accessservices.subjectarea.server.properties;


        import com.fasterxml.jackson.annotation.JsonAutoDetect;
        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
        import com.fasterxml.jackson.databind.annotation.JsonSerialize;

        import java.io.Serializable;
        import java.util.List;
        import java.util.Map;
        import java.util.Objects;

        import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
        import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

        import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidentiality.Confidentiality;
        import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence.Confidence;
        import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
        import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Criticality.Criticality;
/**
 * TODO
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  GovernanceActions implements Serializable {
    private Confidentiality confidentiality = null;
    private Confidence confidence =null;
    private Retention retention =null;
    private Criticality criticality=null;
    /**
     * TODO
     */
    public Confidentiality getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(Confidentiality confidentiality) {
        this.confidentiality = confidentiality;
    }
    /**
     * TODO
     */
    public Confidence getConfidence() {
        return confidence;
    }

    public void setConfidence(Confidence confidence) {
        this.confidence = confidence;
    }
    /**
     * TODO
     */
    public Retention getRetention() {
        return retention;
    }

    public void setRetention(Retention retention) {
        this.retention = retention;
    }
    /**
     * TODO
     */
    public Criticality getCriticality() {
        return criticality;
    }

    public void setCriticality(Criticality criticality) {
        this.criticality = criticality;
    }
}

