/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CanonicalVocabulary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Taxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.CanonicalGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.CanonicalTaxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapping methods to map between Glossary (or a subtype of Glossary) and EntityDetail.
 * These mapping methods map classifications and attributes that directly map to OMRS.
 */
@SubjectAreaMapper
public class GlossaryMapper extends EntityDetailMapper<Glossary> {
    private static final Logger log = LoggerFactory.getLogger(GlossaryMapper.class);
    private static final String className = GlossaryMapper.class.getName();

    public GlossaryMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map EntityDetail to Glossary or a sub type of Glossary
     *
     * @param entityDetail the supplied EntityDetail
     * @return Glossary the equivalent Glossary to the supplied entityDetail.
     */
    @Override
    public Glossary map(EntityDetail entityDetail) {
        // construct the right type of node.
        Glossary glossary = new Glossary();
        List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> omrsClassifications = entityDetail.getClassifications();
        if (omrsClassifications != null) {
            for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification : omrsClassifications) {
                boolean isTaxonomy = false;
                boolean iscanonicalvocabulary = false;

                if (repositoryHelper.isTypeOf(  genericHandler.getServiceName(), OpenMetadataAPIMapper.TAXONOMY_CLASSIFICATION_TYPE_NAME, omrsClassification.getName())) {
                    isTaxonomy = true;
                }
                if (repositoryHelper.isTypeOf(genericHandler.getServiceName(), OpenMetadataAPIMapper.CANONICAL_VOCAB_CLASSIFICATION_TYPE_NAME, omrsClassification.getName())) {
                    iscanonicalvocabulary = true;
                }
                if (iscanonicalvocabulary && isTaxonomy) {
                    glossary = new CanonicalTaxonomy();
                } else if (iscanonicalvocabulary) {
                    glossary = new CanonicalGlossary();
                } else if (isTaxonomy) {
                    glossary = new org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Taxonomy();
                }
            }
        }
        mapEntityDetailToNode(glossary, entityDetail);
        return glossary;
    }

    @Override
    public EntityDetail map(Glossary node) throws InvalidParameterException {
        return super.toEntityDetail(node);
    }

    @Override
    protected List<Classification> getInlinedClassifications(Glossary node) {
        List<Classification> inlinedClassifications = new ArrayList<>();
        if (node.getNodeType() == NodeType.TaxonomyAndCanonicalGlossary) {
            CanonicalTaxonomy canonicalTaxonomy = (CanonicalTaxonomy) node;

            Taxonomy taxonomyClassification = new Taxonomy();
            taxonomyClassification.setOrganizingPrinciple(canonicalTaxonomy.getOrganizingPrinciple());
            inlinedClassifications.add(taxonomyClassification);
            CanonicalVocabulary canonicalVocabulary = new CanonicalVocabulary();
            canonicalVocabulary.setScope(canonicalTaxonomy.getScope());
            inlinedClassifications.add(canonicalVocabulary);
            // add to inlined classifications
        } else if (node.getNodeType() == NodeType.Taxonomy) {
            org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Taxonomy taxonomy = (org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Taxonomy) node;

            Taxonomy taxonomyClassification = new Taxonomy();
            taxonomyClassification.setOrganizingPrinciple(taxonomy.getOrganizingPrinciple());
            inlinedClassifications.add(taxonomyClassification);

        } else if (node.getNodeType() == NodeType.CanonicalGlossary) {
            CanonicalGlossary canonicalGlossary = (CanonicalGlossary) node;
            CanonicalVocabulary canonicalVocabulary = new CanonicalVocabulary();
            canonicalVocabulary.setScope(canonicalGlossary.getScope());
            inlinedClassifications.add(canonicalVocabulary);
        }
        return inlinedClassifications;
    }

    /**
     * Map a primitive omrs property to the glossary object.
     *
     * @param glossary     the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Node, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToNode(Glossary glossary, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = true;
        if (propertyName.equals( OpenMetadataAPIMapper.LANGUAGE_PROPERTY_NAME)) {
            glossary.setLanguage(stringValue);
        } else if (propertyName.equals(OpenMetadataAPIMapper.USAGE_PROPERTY_NAME)) {
            glossary.setUsage(stringValue);
        } else {
            foundProperty = false;
        }
        return foundProperty;
    }

    /**
     * Map the supplied Node to omrs InstanceProperties.
     *
     * @param node               supplied node
     * @param instanceProperties equivalent instance properties to the Node
     */
    @Override
    protected void mapNodeToInstanceProperties(Glossary node, InstanceProperties instanceProperties) {
        Glossary glossary = (Glossary) node;
        if (glossary.getLanguage() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, glossary.getLanguage(), OpenMetadataAPIMapper.LANGUAGE_PROPERTY_NAME);
        }
        if (glossary.getUsage() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, glossary.getUsage(), OpenMetadataAPIMapper.USAGE_PROPERTY_NAME);
        }
        if (node.getName() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, node.getName(), OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);
        }
    }

    @Override
    protected boolean updateNodeWithClassification(Glossary glossary, Classification omasClassification) {
        boolean handled = false;

        final String classificationName = omasClassification.getClassificationName();
        //TODO do additional properties for classification subtypes.

        NodeType existingNodeType = glossary.getNodeType();
        if (existingNodeType == null) {
            existingNodeType = NodeType.Glossary;
        }
        if (existingNodeType == NodeType.Glossary && repositoryHelper.isTypeOf(genericHandler.getServiceName(), OpenMetadataAPIMapper.TAXONOMY_CLASSIFICATION_TYPE_NAME, classificationName)) {
            glossary.setNodeType(NodeType.Taxonomy);
            handled = true;
        } else if (existingNodeType == NodeType.CanonicalGlossary && repositoryHelper.isTypeOf(genericHandler.getServiceName(), OpenMetadataAPIMapper.TAXONOMY_CLASSIFICATION_TYPE_NAME, classificationName)) {
            glossary.setNodeType(NodeType.TaxonomyAndCanonicalGlossary);
            handled = true;
        } else if (existingNodeType == NodeType.Glossary && repositoryHelper.isTypeOf(genericHandler.getServiceName(), OpenMetadataAPIMapper.CANONICAL_VOCAB_CLASSIFICATION_TYPE_NAME, classificationName)) {
            glossary.setNodeType(NodeType.CanonicalGlossary);
            handled = true;
        } else if (existingNodeType == NodeType.Taxonomy && repositoryHelper.isTypeOf(genericHandler.getServiceName(), OpenMetadataAPIMapper.CANONICAL_VOCAB_CLASSIFICATION_TYPE_NAME, classificationName)) {
            glossary.setNodeType(NodeType.TaxonomyAndCanonicalGlossary);
            handled = true;
        } else {
            glossary.setNodeType(existingNodeType);
            handled = true;
        }
        return handled;
    }

    @Override
    public String getTypeName() {
        return OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME;
    }
}
