/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;


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
     * Map Glossary to OMRS bean Glossary.
     * @param glossary the API glossary object
     * @return omrsBean the omrs bean equivalent to the glossary
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary mapGlossaryToOMRSBean(Glossary glossary) throws InvalidParameterException {


        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary omrsBean = new org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary();

        //Set properties
        if (glossary.getSystemAttributes() !=null) {
            omrsBean.setSystemAttributes(glossary.getSystemAttributes());
        }
        omrsBean.setEffectiveFromTime(glossary.getEffectiveFromTime());
        omrsBean.setEffectiveToTime(glossary.getEffectiveToTime());
        omrsBean.setDescription(glossary.getDescription());
        omrsBean.setDisplayName(glossary.getName());
        omrsBean.setUsage(glossary.getUsage());
        omrsBean.setQualifiedName(glossary.getQualifiedName());
        omrsBean.setAdditionalProperties((glossary.getAdditionalProperties()));
        List<Classification> classifications = glossary.getClassifications();
        if (classifications==null) {
            classifications = new ArrayList<>();
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
     * @param omrsBean supplied omrs bean representation of Glossary
     * @return Glossary the API Glossary object
     */
    public static Glossary mapOMRSBeantoGlossary(org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary omrsBean) {
        Glossary glossary = new Glossary();
        glossary.setClassifications(omrsBean.getClassifications());
        glossary.setDescription(omrsBean.getDescription());
        if (omrsBean.getSystemAttributes() !=null) {
            glossary.setSystemAttributes(omrsBean.getSystemAttributes());
        }
        glossary.setEffectiveFromTime(omrsBean.getEffectiveFromTime());
        glossary.setEffectiveToTime((omrsBean.getEffectiveToTime()));
        glossary.setName(omrsBean.getDisplayName());
        glossary.setQualifiedName(omrsBean.getQualifiedName());
        glossary.setUsage(omrsBean.getUsage());
        glossary.setAdditionalProperties(omrsBean.getAdditionalProperties());
        // this call will populate the governance classifications
        glossary.setClassifications(omrsBean.getClassifications());
        return glossary;
    }
}
