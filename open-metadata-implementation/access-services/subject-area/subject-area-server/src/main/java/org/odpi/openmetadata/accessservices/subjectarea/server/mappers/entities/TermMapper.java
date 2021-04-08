/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceClassifications;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Static mapping methods to map between the Term and the generated OMRSBean for GlossaryTerm.
 */
@SubjectAreaMapper
public class TermMapper extends EntityDetailMapper<Term> {
    private static final Logger log = LoggerFactory.getLogger( TermMapper.class);
    private static final String className = TermMapper.class.getName();
    public static final String GLOSSARY_TERM = "GlossaryTerm";

    public TermMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map (convert) EntityDetail to Term or a sub type of Term
     * @param entityDetail the supplied EntityDetail
     * @return Term the equivalent Term to the supplied entityDetail.
     */
    public Term map(EntityDetail entityDetail) {
            Term term = new Term();
            mapEntityDetailToNode(term,entityDetail);
            return term;
    }

    @Override
    public EntityDetail map(Term node) {
        return toEntityDetail(node);
    }

    /**
     * Map a primitive omrs property to the term object.
     * @param term the term to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Node, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToNode(Term term, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = true;
        if (propertyName.equals("summary")) {
            term.setSummary(stringValue);
        } else if (propertyName.equals("abbreviation")) {
            term.setAbbreviation(stringValue);
        } else if (propertyName.equals("examples")) {
            term.setExamples(stringValue);
        } else if (propertyName.equals("usage")) {
            term.setUsage(stringValue);
        } else if (propertyName.equals("summary")) {
            term.setSummary(stringValue);
        } else {
            foundProperty =false;
        }
        return foundProperty;
    }
    /**
     * Map the supplied Node to omrs InstanceProperties.
     * @param term supplied node
     * @param instanceProperties equivalent instance properties to the Node
     */
    @Override
    protected void mapNodeToInstanceProperties(Term term, InstanceProperties instanceProperties) {
        if (term.getSummary()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, term.getSummary(), "summary");
        }
        if (term.getAbbreviation()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, term.getAbbreviation(), "abbreviation");
        }
        if (term.getExamples()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, term.getExamples(), "examples");
        }
        if (term.getUsage()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, term.getUsage(), "usage");
        }
        if (term.getSummary()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, term.getSummary(), "summary");
        }
        if (term.getName()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, term.getName(), "displayName");
        }
    }

    @Override
    protected boolean updateNodeWithClassification(Term term, Classification omasClassification) {
        boolean handled = false;
        GovernanceClassifications governanceClassifications = term.getGovernanceClassifications();
        if (governanceClassifications ==null) {
            governanceClassifications = new GovernanceClassifications();
        }
        final String classificationName = omasClassification.getClassificationName();

        String sourceName = omrsapiHelper.getServiceName();
        //TODO do additional properties for classification subtypes.
        if (repositoryHelper.isTypeOf(sourceName,classificationName,"Confidentiality")) {
            governanceClassifications.setConfidentiality((Confidentiality) omasClassification);
            term.setGovernanceClassifications(governanceClassifications);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"Confidence")) {
            governanceClassifications.setConfidence((Confidence) omasClassification);
            term.setGovernanceClassifications(governanceClassifications);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"Criticality")) {
            governanceClassifications.setCriticality((Criticality) omasClassification);
            term.setGovernanceClassifications(governanceClassifications);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"Retention")) {
            governanceClassifications.setRetention((Retention) omasClassification);
            term.setGovernanceClassifications(governanceClassifications);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"SpineObject")) {
            term.setSpineObject(true);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"SpineAttribute")) {
            term.setSpineAttribute(true);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"ObjectIdentifier")) {
            term.setObjectIdentifier(true);
            handled =true;
        }

        // TODO activity
        return handled;
    }
    /**
     * A Classification either exists in the classifications associated with a node or as an inlined attribute (these are properties / attributes of a node that correspond to OMRS Classifications)
     * @param term supplied term
     * @return inlined classifications.
     */
    @Override
    protected List<Classification> getInlinedClassifications(Term term) {
        List<Classification> inlinedClassifications = new ArrayList<>();
        GovernanceClassifications governanceClassifications = term.getGovernanceClassifications();
        if (governanceClassifications !=null) {
            Criticality criticality = governanceClassifications.getCriticality();
            Confidence confidence = governanceClassifications.getConfidence();
            Confidentiality confidentiality =governanceClassifications.getConfidentiality();
            Retention retention = governanceClassifications.getRetention();
            if (criticality != null) {
                inlinedClassifications.add(criticality);
            }
            if (confidence!=null) {
                inlinedClassifications.add(confidence);
            }
            if (confidentiality!=null) {
                inlinedClassifications.add(confidentiality);
            }
            if (retention!=null) {
                inlinedClassifications.add(retention);
            }
        }
        //TODO do additional properties for classification subtypes.
        if (term.isSpineObject()) {
            inlinedClassifications.add(new SpineObject());
        }
        if ( term.isSpineAttribute()) {
            inlinedClassifications.add(new SpineAttribute());
        }
        if (term.isObjectIdentifier()) {
            inlinedClassifications.add(new ObjectIdentifier());
        }
        return inlinedClassifications;
    }
    @Override
    public String getTypeName(){
        return GLOSSARY_TERM;
    }
}
