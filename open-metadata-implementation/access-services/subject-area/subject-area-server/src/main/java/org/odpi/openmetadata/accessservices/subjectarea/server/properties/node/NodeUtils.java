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

import org.odpi.openmetadata.accessservices.subjectarea.common.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidentiality.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Criticality.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.GovernanceClassificationStatus;

import java.util.List;

public class NodeUtils {
    public static void addGovernanceLevelsToClassifications(List<Classification> classifications, List<GovernanceLevel> governanceLevels) throws InvalidParameterException {
        for (GovernanceLevel governanceLevel:governanceLevels) {
            if ("Confidentiality".equals(governanceLevel.getClassificationName()) ) {
                Confidentiality confidentiality = new Confidentiality();

                confidentiality.setLevel(governanceLevel.getLevel());
                confidentiality.setStatus(GovernanceClassificationStatus.Proposed);
                classifications.add(confidentiality);
            }
            if ("Criticality".equals(governanceLevel.getClassificationName()) ) {
                Criticality criticality = new Criticality();
                criticality.setLevel(governanceLevel.getLevel());
                criticality.setStatus(GovernanceClassificationStatus.Proposed);

                classifications.add(criticality);
            }
            if ("Confidence".equals(governanceLevel.getClassificationName()) ) {
                Confidence confidence = new  Confidence();

                confidence.setLevel(governanceLevel.getLevel());
                confidence.setStatus(GovernanceClassificationStatus.Proposed);
                classifications.add(confidence);
            }
        }
    }
}
