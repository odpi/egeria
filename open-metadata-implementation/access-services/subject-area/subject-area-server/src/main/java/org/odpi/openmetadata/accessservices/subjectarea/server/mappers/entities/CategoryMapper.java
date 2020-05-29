/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.INodeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
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
public class CategoryMapper extends EntityDetailMapper implements INodeMapper {
    private static final Logger log = LoggerFactory.getLogger(CategoryMapper.class);
    private static final String className = CategoryMapper.class.getName();
    public static final String GLOSSARY_CATEGORY = "GlossaryCategory";

    public CategoryMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map EntityDetail to Category or a sub type of Category
     * @param entityDetail the supplied EntityDetail
     * @return Category the equivalent Category to the supplied entityDetail.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    public Category mapEntityDetailToNode(EntityDetail entityDetail) throws InvalidParameterException {
        String methodName = "mapEntityDetailToNode";
        String entityTypeName = entityDetail.getType().getTypeDefName();
        if (repositoryHelper.isTypeOf(omrsapiHelper.getServiceName(), entityTypeName, GLOSSARY_CATEGORY)) {
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
        } else {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.MAPPER_ENTITY_GUID_TYPE_ERROR.getMessageDefinition();
            messageDefinition.setMessageParameters(entityDetail.getGUID(), entityTypeName, GLOSSARY_CATEGORY);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                methodName,
                                                "Node Type",
                                                null);
        }
    }
    /**
     * Map the supplied Node to omrs InstanceProperties.
     * @param node supplied node
     * @param instanceProperties equivalent instance properties to the Node
     */
    @Override
    protected void mapNodeToInstanceProperties(Node node, InstanceProperties instanceProperties) {
        if (node.getName()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, node.getName(), "displayName");
        }
    }

    @Override
    protected boolean updateNodeWithClassification(Node node, Classification omasClassification) {
        Category category = (Category) node;
        boolean handled = false;
        final String classificationName = omasClassification.getClassificationName();
        //TODO do additional properties for classification subtypes.
        NodeType nodeType = NodeType.Category;
        if (repositoryHelper.isTypeOf(omrsapiHelper.getServiceName(),"SubjectArea",classificationName)) {
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
    protected List<Classification> getInlinedClassifications(Node node) {
        List<Classification> inlinedClassifications = new ArrayList<>();

        if (node.getNodeType()== NodeType.SubjectAreaDefinition) {
            SubjectArea subjectArea = new SubjectArea();
            subjectArea.setName(node.getName());
            inlinedClassifications.add(subjectArea);
        }

        return inlinedClassifications;
    }
    @Override
    protected String getTypeName(){
        return GLOSSARY_CATEGORY;
    }
}


