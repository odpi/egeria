/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;


import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CanonicalVocabulary.CanonicalVocabulary;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidentiality.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Criticality.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Static mapping methods to map between Glossary and the generated glossary.
 * These mapping methods map classifications and attributes that directly map to OMRS.
 *
 */
public class GlossaryMapper {
    private static final Logger log = LoggerFactory.getLogger( GlossaryMapper.class);
    private static final String className = GlossaryMapper.class.getName();

    /**
     * Map Glossary to Generated Glossary.
     * @param glossary Glossary to map to OMRS.
     * @param oMRSAPIHelper OMRSAPIHelper to use in mapping.
     * @return Glossary after mapping.
     * @throws InvalidParameterException if the glossary or helper is invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary mapGlossaryToOMRSBean(Glossary glossary, OMRSAPIHelper oMRSAPIHelper) throws InvalidParameterException {


        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary omrsBean = new org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary();

        //Set properties
        if (glossary.getSystemAttributes() !=null) {
            omrsBean.setSystemAttributes(glossary.getSystemAttributes());
        }
        omrsBean.setDescription(glossary.getDescription());
        omrsBean.setDisplayName(glossary.getName());
        omrsBean.setUsage(glossary.getUsage());
        omrsBean.setAdditionalProperties((glossary.getAdditionalProperties()));
        List<Classification> classifications = glossary.getClassifications();
        // glossary Classifications should not contain any governance classifications, but it is possible that the requester added them there by mistake.
        if (classifications==null) {
            classifications = new ArrayList<>();
        }
        for (Classification classification : classifications) {
            final String classificationName = classification.getClassificationName();
            if (classificationName.equals(new Confidentiality().getClassificationName())) {
                NodeUtils.foundGovernanceClassifications(classificationName);
            } else if (classificationName.equals(new Confidence().getClassificationName())) {
                NodeUtils.foundGovernanceClassifications(classificationName);
            } else if (classificationName.equals(new Criticality().getClassificationName())) {
                NodeUtils.foundGovernanceClassifications(classificationName);
            } else if (classificationName.equals(new Retention().getClassificationName())) {
                NodeUtils.foundGovernanceClassifications(classificationName);
            }
        }

        GovernanceActions governanceActions = glossary.getGovernanceActions();
        if (governanceActions ==null) {
            governanceActions =new GovernanceActions();
            glossary.setGovernanceActions(governanceActions);
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
        NodeType nodeType = glossary.getNodeType();
        if (nodeType !=null) {

            if (nodeType ==NodeType.CanonicalGlossary) {
                CanonicalVocabulary canonicalVocabulary = new CanonicalVocabulary();
                classifications.add(canonicalVocabulary);
            } else  if (nodeType ==NodeType.Taxonomy) {
                org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Taxonomy.Taxonomy taxonomy = new org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Taxonomy.Taxonomy();
                classifications.add(taxonomy);
            } else  if (nodeType ==NodeType.TaxonomyAndCanonicalGlossary) {
                CanonicalVocabulary canonicalVocabulary = new CanonicalVocabulary();
                classifications.add(canonicalVocabulary);
                org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Taxonomy.Taxonomy taxonomy = new org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Taxonomy.Taxonomy();
                classifications.add(taxonomy);
            }
        }

        omrsBean.setClassifications(classifications);
        return omrsBean;
    }

    /**
     * map the generated OMRS bean for glossary to the Glossary exposed in the API.
     * @param omrsBean bean to use to create Glossary entity.
     * @return Glossary created from OMRSBean.
     */
    public static Glossary mapOMRSBeantoGlossary(org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary omrsBean) {
        Glossary glossary = new Glossary();
        glossary.setClassifications(omrsBean.getClassifications());
        glossary.setDescription(omrsBean.getDescription());
        if (omrsBean.getSystemAttributes() !=null) {
            glossary.setSystemAttributes(omrsBean.getSystemAttributes());
        }
        glossary.setName(omrsBean.getDisplayName());
        glossary.setQualifiedName(omrsBean.getQualifiedName());
        glossary.setUsage(omrsBean.getUsage());
        glossary.setAdditionalProperties(omrsBean.getAdditionalProperties());
        // this call will populate the governance classifications
        glossary.setClassifications(omrsBean.getClassifications());
        return glossary;
    }
}
