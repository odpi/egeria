/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.INodeMapper;
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
public class TermMapper extends EntityDetailMapper implements INodeMapper{
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
     * @throws InvalidParameterException the entity with this guid is not of the right type
     */
    public Term mapEntityDetailToNode(EntityDetail entityDetail) throws InvalidParameterException {
        String methodName = "mapEntityDetailToNode";

        String entityTypeName = entityDetail.getType().getTypeDefName();
        if (repositoryHelper.isTypeOf(omrsapiHelper.getServiceName(),entityTypeName, GLOSSARY_TERM)) {
            Term term = new Term();
            mapEntityDetailToNode(term,entityDetail);
            return term;
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.MAPPER_ENTITY_GUID_TYPE_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityDetail.getGUID(), entityTypeName, "GlossaryTerm");
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
        }
    }

    /**
     * Map a primitive omrs property to the term object.
     * @param node the term to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Node, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToNode(Node node, String propertyName, Object value) {
        String stringValue = (String) value;
        Term term = (Term) node;
        boolean foundProperty = true;
        if (propertyName.equals("summary")) {
            term.setSummary(stringValue);
        } else if (propertyName.equals("abbreviation")) {
            term.setAbbreviation(stringValue);
        } else if (propertyName.equals("examples")) {
            term.setExamples(stringValue);
        } else if (propertyName.equals("usage")) {
            term.setUsage(stringValue);
        } else {
            foundProperty =false;
        }
        return foundProperty;
    }
    /**
     * Map the supplied Node to omrs InstanceProperties.
     * @param node supplied node
     * @param instanceProperties equivalent instance properties to the Node
     */
    @Override
    protected void mapNodeToInstanceProperties(Node node, InstanceProperties instanceProperties) {
        Term term = (Term)node;
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
        if (node.getName()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, node.getName(), "displayName");
        }
    }

    @Override
    protected boolean updateNodeWithClassification(Node node, org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification) {
        Term term = (Term) node;
        boolean handled = false;
        GovernanceActions governanceActions = term.getGovernanceActions();
        if (governanceActions ==null) {
            governanceActions = new GovernanceActions();
        }
        final String classificationName = omasClassification.getClassificationName();

        String sourceName = omrsapiHelper.getServiceName();
        //TODO do additional properties for classification subtypes.
        if (repositoryHelper.isTypeOf(sourceName,classificationName,"Confidentiality")) {
            governanceActions.setConfidentiality((org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidentiality) omasClassification);
            term.setGovernanceActions(governanceActions);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"Confidence")) {
            governanceActions.setConfidence((org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidence) omasClassification);
            term.setGovernanceActions(governanceActions);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"Criticality")) {
            governanceActions.setCriticality((org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Criticality) omasClassification);
            term.setGovernanceActions(governanceActions);
            handled =true;
        } else if (repositoryHelper.isTypeOf(sourceName,classificationName,"Retention")) {
            governanceActions.setRetention((org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention) omasClassification);
            term.setGovernanceActions(governanceActions);
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
     * @param node supplied term
     * @return inlined classifications.
     */
    @Override
    protected List<Classification> getInlinedClassifications(Node node) {
        List<Classification> inlinedClassifications = new ArrayList<>();
        Term term = (Term) node;
        GovernanceActions governanceActions = term.getGovernanceActions();
        if (governanceActions !=null) {
            Criticality criticality = governanceActions.getCriticality();
            Confidence confidence = governanceActions.getConfidence();
            Confidentiality confidentiality =governanceActions.getConfidentiality();
            Retention retention = governanceActions.getRetention();
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
    protected String getTypeName(){
        return GLOSSARY_TERM;
    }
}
