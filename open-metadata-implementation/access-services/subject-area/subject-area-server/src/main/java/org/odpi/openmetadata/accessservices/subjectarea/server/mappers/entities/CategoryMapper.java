/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapping methods to map between Category (or a subtype of Category) and EntityDetail.
 * These mapping methods map classifications and attributes that directly map to OMRS.
 *
 */
@SubjectAreaMapper
public class CategoryMapper extends EntityDetailMapper<Category> {
    private static final Logger log = LoggerFactory.getLogger(CategoryMapper.class);
    private static final String className = CategoryMapper.class.getName();
    public static final String GLOSSARY_CATEGORY = "GlossaryCategory";

    public CategoryMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map EntityDetail to Category or a sub type of Category
     * @param entityDetail the supplied EntityDetail
     * @return Category the equivalent Category to the supplied entityDetail.
     */
    @Override
    public Category map(EntityDetail entityDetail) {
            Category category = new Category();
            if (entityDetail.getClassifications()!=null) {
                Set<String> classificationNames = entityDetail.getClassifications().stream().map(x -> x.getName()).collect(Collectors.toSet());
                if (classificationNames.contains("SubjectArea")) {
                    // construct the right object so that that is is serialised appropriately as a SubjectArea Definition on the response.
                    category = new SubjectAreaDefinition();
                }
            }
            mapEntityDetailToNode(category,entityDetail);
            return category;
    }

    @Override
    public EntityDetail map(Category node) throws InvalidParameterException {
        return super.toEntityDetail(node);
    }

    /**
     * Map the supplied Node to omrs InstanceProperties.
     * @param node supplied node
     * @param instanceProperties equivalent instance properties to the Node
     */
    @Override
    protected void mapNodeToInstanceProperties(Category node, InstanceProperties instanceProperties) {
        if (node.getName()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, node.getName(), "displayName");
        }
    }

    @Override
    protected boolean updateNodeWithClassification(Category category, Classification omasClassification) {
        boolean handled = false;
        final String classificationName = omasClassification.getClassificationName();
        //TODO do additional properties for classification subtypes.
        NodeType nodeType = NodeType.Category;
        if (repositoryHelper.isTypeOf(genericHandler.getServiceName(),"SubjectArea",classificationName)) {
            nodeType =NodeType.SubjectAreaDefinition;
            handled =true;
        }
        category.setNodeType(nodeType);
        return handled;
    }
    /**
     * A Classification either exists in the classifications associated with a node or as an inlined attribute (these are properties / attributes of a node that correspond to OMRS Classifications)
     * @param node supplied term
     * @return inlined classifications.
     */
    @Override
    protected List<Classification> getInlinedClassifications(Category node) {
        List<Classification> inlinedClassifications = new ArrayList<>();

        if (node.getNodeType()== NodeType.SubjectAreaDefinition) {
            SubjectArea subjectArea = new SubjectArea();
            subjectArea.setName(node.getName());
            inlinedClassifications.add(subjectArea);
        }

        return inlinedClassifications;
    }

    @Override
    public String getTypeName(){
        return GLOSSARY_CATEGORY;
    }
}


