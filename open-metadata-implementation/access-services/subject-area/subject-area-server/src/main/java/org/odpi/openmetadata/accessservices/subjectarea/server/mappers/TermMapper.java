/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidentiality.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Criticality.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeUtils;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Static mapping methods to map between the Term and the generated OMRSBean for GlossaryTerm.
 */
public class TermMapper {
    private static final Logger log = LoggerFactory.getLogger( TermMapper.class);
    private static final String className = TermMapper.class.getName();

    /**
     * map Node to GlossaryTerm local attributes
     * @param term
     * @return
     * @throws InvalidParameterException
     */
    static public GlossaryTerm mapTermToOMRSBean(Term term) throws InvalidParameterException {

        GlossaryTerm omrsBean = new GlossaryTerm();
        //Set properties
        if (term.getSystemAttributes() !=null) {
            omrsBean.setSystemAttributes(term.getSystemAttributes());
        }
        omrsBean.setQualifiedName(term.getQualifiedName());
        omrsBean.setDescription(term.getDescription());
        omrsBean.setDisplayName(term.getName());
        omrsBean.setSummary(term.getSummary());
        omrsBean.setGlossaryName(term.getGlossaryName());
        // map classifications
        List<Classification> classifications = term.getClassifications();
        if (classifications==null) {
            classifications = new ArrayList<>();
        }
        for (Classification classification : classifications) {
            final String classificationName = classification.getClassificationName();
            if (classificationName.equals(new Confidentiality().getClassificationName())) {
                NodeUtils.foundGovernanceClassifications(classificationName);
            } else    if (classificationName.equals(new Confidence().getClassificationName())) {
                NodeUtils.foundGovernanceClassifications(classificationName);
            } else    if (classificationName.equals(new Criticality().getClassificationName())) {
                NodeUtils. foundGovernanceClassifications(classificationName);
            } else    if (classificationName.equals(new Retention().getClassificationName())) {
                NodeUtils.foundGovernanceClassifications(classificationName);
            }
        }
        GovernanceActions governanceActions = term.getGovernanceActions();
        if (governanceActions ==null) {
            governanceActions =new GovernanceActions();
            term.setGovernanceActions(governanceActions);
        }
        if (governanceActions.getRetention() !=null) {
            classifications.add(governanceActions.getRetention());
        }
        if (governanceActions.getConfidence() !=null) {
            classifications.add(governanceActions.getConfidence());
        }
        if (governanceActions.getConfidentiality() !=null) {
            classifications.add(governanceActions.getConfidentiality());
        }
        if (governanceActions.getCriticality() !=null) {
            classifications.add(governanceActions.getCriticality());
        }

        omrsBean.setClassifications(classifications);
        return omrsBean;
    }

    public static Term mapOMRSBeantoTerm(GlossaryTerm omrsBean) {
        Term term = new Term();
        term.setClassifications(omrsBean.getClassifications());
        term.setDescription(omrsBean.getDescription());

        if (omrsBean.getSystemAttributes() !=null) {
            term.setSystemAttributes(omrsBean.getSystemAttributes());
        }
        term.setName(omrsBean.getDisplayName());
        term.setSummary(omrsBean.getSummary());
        term.setQualifiedName(omrsBean.getQualifiedName());

        term.setClassifications(omrsBean.getClassifications());


        return term;
    }
}
