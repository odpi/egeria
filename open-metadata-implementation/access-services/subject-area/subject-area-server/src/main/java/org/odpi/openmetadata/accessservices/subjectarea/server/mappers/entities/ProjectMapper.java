/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.GlossaryProject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.INodeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Mapping methods to map between Project (or a subtype of Project) and EntityDetail.
 * These mapping methods map classifications and attributes that directly map to OMRS.
 *
 */
public class ProjectMapper extends EntityDetailMapper implements INodeMapper {
    private static final Logger log = LoggerFactory.getLogger( ProjectMapper.class);
    private static final String className = ProjectMapper.class.getName();
    public static final String PROJECT = "Project";
    public static final String GLOSSARY_PROJECT = "GlossaryProject";


    public ProjectMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map EntityDetail to Project or a sub type of Project
     * @param entityDetail the supplied EntityDetail
     * @return Project the equivalent Project to the supplied entityDetail.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    public org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project mapEntityDetailToNode(EntityDetail entityDetail) throws InvalidParameterException{
        String methodName = "mapEntityDetailToNode";
        String entityTypeName = entityDetail.getType().getTypeDefName();
        if (repositoryHelper.isTypeOf(omrsapiHelper.getServiceName(),entityTypeName, PROJECT)) {
            // construct the right type of node.
            Project project  = new Project();
            List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> omrsClassifications = entityDetail.getClassifications();
            if (omrsClassifications !=null) {
                for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification : omrsClassifications) {

                    if (repositoryHelper.isTypeOf(omrsapiHelper.getServiceName(), GLOSSARY_PROJECT, omrsClassification.getName())) {
                        project = new GlossaryProject();
                    } else {
                        project = new Project();
                    }
                }
            }
            mapEntityDetailToNode(project,entityDetail);
            return project;
        } else {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.MAPPER_ENTITY_GUID_TYPE_ERROR.getMessageDefinition();
            messageDefinition.setMessageParameters(entityDetail.getGUID(), entityTypeName, PROJECT);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                methodName,
                                                "Node Type",
                                                null);
        }
    }
    @Override
    protected List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> getInlinedClassifications(Node node) {
        List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> inlinedClassifications = new ArrayList<>();
        if (node.getNodeType() == NodeType.GlossaryProject) {
            org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.GlossaryProject glossaryProjectClassification =new org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.GlossaryProject();
            // add to inlined classifications
            inlinedClassifications.add(glossaryProjectClassification);
        }
        return inlinedClassifications;
    }

    /**
     * Map a primitive omrs property to the Project object.
     * @param node the Project to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Node, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToNode(Node node, String propertyName, Object value) {


        Project project = (Project) node;
        boolean foundProperty = true;
        if (propertyName.equals("startDate")) {
            Date dateValue = (Date) value;
            project.setStartDate(dateValue);
        } else if (propertyName.equals("plannedEndDate")) {
            Date dateValue = (Date)value;
            project.setPlannedEndDate(dateValue);
        } else if (propertyName.equals("status")) {
            String stringValue = (String) value;
            project.setStatus(stringValue);
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
        Project project = (Project)node;
        if (project.getStatus()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, project.getStatus(), "status");
        }
        if (project.getStartDate()!=null) {
            SubjectAreaUtils.setDatePropertyInInstanceProperties(instanceProperties, project.getStartDate(), "startDate");
        }
        if (project.getPlannedEndDate()!=null) {
            SubjectAreaUtils.setDatePropertyInInstanceProperties(instanceProperties, project.getPlannedEndDate(), "plannedEndDate");
        }
    }

    @Override
    protected boolean updateNodeWithClassification(Node node, org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification) {
        Project project = (Project) node;
        boolean handled = false;

        final String classificationName = omasClassification.getClassificationName();
        //TODO do additional properties for classification subtypes.

        NodeType existingNodeType = project.getNodeType();
        if (existingNodeType==null) {
            existingNodeType=NodeType.Project;
        }
        String sourceName = omrsapiHelper.getServiceName();
        if (existingNodeType == NodeType.Project && repositoryHelper.isTypeOf(sourceName, GLOSSARY_PROJECT,classificationName)) {
            project.setNodeType(NodeType.GlossaryProject);
            handled=true;
        } else {
            project.setNodeType(existingNodeType);
            handled=true;
        }
        return handled;
    }
    @Override
    protected String getTypeName(){
        return PROJECT;
    }
}
