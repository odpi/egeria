/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.InstanceGraphResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.GraphResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph.LineTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph.NodeTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGraphRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServicesInstance;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaGraphHandler extends SubjectAreaHandler {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGraphHandler.class);
    private static final String className = SubjectAreaGraphHandler.class.getName();

    /**
     * Construct the Subject Area Graph Handler
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param oMRSAPIHelper           omrs API helper
     * @param errorHandler            handler for repository service errors
     */
    public SubjectAreaGraphHandler(String serviceName,
                                   String serverName,
                                   InvalidParameterHandler invalidParameterHandler,
                                   OMRSRepositoryHelper repositoryHelper,
                                   RepositoryHandler repositoryHandler,
                                   OMRSAPIHelper oMRSAPIHelper,
                                   RepositoryErrorHandler errorHandler) {
        super(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler, oMRSAPIHelper, errorHandler);
    }

    @Override
    protected SubjectAreaOMASAPIResponse getResponse(SubjectAreaOMASAPIResponse response) {
        return null;
    }


    /**
     * Get the graph of nodes and Lines radiating out from a node.
     * <p>
     * Return the nodes and Lines that radiate out from the supplied node (identified by a GUID).
     * The results are scoped by types of Lines, types of nodes and classifications as well as level.
     *
     * @param userId        userId under which the request is performed
     * @param guid          the starting point of the query.
     * @param nodeFilterStr Comma separated list of node names to include in the query results.  Null means include
     *                      all entities found, irrespective of their type.
     * @param lineFilterStr comma separated list of line names to include in the query results.  Null means include
     *                      all Lines found, irrespective of their type.
     * @param asOfTime      Requests a historical query of the relationships for the entity.  Null means return the
     *                      present values.
     * @param statusFilter  By default only active instances are returned. Specify ALL to see all instance in any status.
     * @param level         the number of the Lines (relationships) out from the starting node that the query will traverse to
     *                      gather results. If not specified then it defaults to 3.
     * @return A graph of nodeTypes.
     *
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getGraph(
                                               String userId,
                                               String guid,
                                               Date asOfTime,
                                               String nodeFilterStr,
                                               String lineFilterStr,
                                               StatusFilter statusFilter,   // may need to extend this for controlled terms
                                               Integer level) {

        final String methodName = "getGraph";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaGraphRESTServices graphRESTServices = new SubjectAreaGraphRESTServices();
        graphRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        //set of entity type guids so we do not have duplicates
        Set<String> entityTypeGUIDs = new HashSet();
        Set<NodeType> nodeFilter = new HashSet<>();
        Set<LineType> lineFilter = new HashSet<>();
        // if there was no NodeFilter supplied then limit to the the NodeType values, so we only get the types that this omas is interested in.
        if (nodeFilterStr == null) {
            nodeFilter = new HashSet();
            for (NodeType nodeType : NodeType.values()) {
                if (nodeType != NodeType.Unknown) {
                    nodeFilter.add(nodeType);
                }
            }
        } else {
            if (nodeFilterStr.contains(",")) {
                StringTokenizer tokenizer = new StringTokenizer(nodeFilterStr, ",");
                while (tokenizer.hasMoreElements()) {
                    String nodeTypeName = (String) tokenizer.nextElement();
                    for (NodeType nodeType : NodeType.values()) {
                        if (nodeType.name().equals(nodeTypeName)) {
                            nodeFilter.add(nodeType);
                        }
                    }
                }
            } else {
                for (NodeType nodeType : NodeType.values()) {
                    if (nodeType.name().equals(nodeFilterStr)) {
                        nodeFilter.add(nodeType);
                    }
                }
            }
        }
        try {
            for (NodeType nodeType : nodeFilter) {
                String entityTypeGUID = NodeTypeMapper.mapNodeTypeToEntityTypeGuid(nodeType);
                entityTypeGUIDs.add(entityTypeGUID);
            }
            // if there was no Line filter supplied then limit to the the LineType values, so we only get the types that this omas is interested in.
            if (lineFilterStr == null) {
                for (LineType lineType : LineType.values()) {
                    if (lineType != LineType.Unknown) {
                        lineFilter.add(lineType);
                    }
                }
            } else {
                if (lineFilterStr.contains(",")) {
                    StringTokenizer tokenizer = new StringTokenizer(lineFilterStr, ",");
                    while (tokenizer.hasMoreElements()) {
                        String lineTypeName = (String) tokenizer.nextElement();
                        for (LineType lineType : LineType.values()) {
                            if (lineType.name().equals(lineTypeName)) {
                                lineFilter.add(lineType);
                            }
                        }
                    }
                } else {
                    for (LineType lineType : LineType.values()) {
                        if (lineType.name().equals(lineFilterStr)) {
                            lineFilter.add(lineType);
                        }
                    }
                }
            }
            OMRSRepositoryHelper oMRSRepositoryHelper = oMRSAPIHelper.getOMRSRepositoryHelper();
            List<String> relationshipTypeGUIDs = new ArrayList<>();
            for (LineType lineType : lineFilter) {
                String relationshipTypeGUID = LineTypeMapper.mapLineTypeToRelationshipTypeGuid(lineType);
                relationshipTypeGUIDs.add(relationshipTypeGUID);
            }

            List<String> entityTypeGUIDList = null;
            if (!entityTypeGUIDs.isEmpty()) {
                entityTypeGUIDList = new ArrayList(entityTypeGUIDs);
            }
            List<InstanceStatus> requestedInstanceStatus = null;
            requestedInstanceStatus = new ArrayList<>();
            if (statusFilter == null || statusFilter == StatusFilter.ACTIVE) {
                requestedInstanceStatus.add(SubjectAreaUtils.convertStatusToInstanceStatus(Status.ACTIVE));
            } else {
                // request all status instances.
                for (Status omasStatus : Status.values()) {
                    requestedInstanceStatus.add(SubjectAreaUtils.convertStatusToInstanceStatus(omasStatus));
                }
            }
            if (level == null) {
                level = 3;
            }

            response = oMRSAPIHelper.callGetEntityNeighbourhood(methodName,
                                                                userId,
                                                                guid,
                                                                entityTypeGUIDList,
                                                                relationshipTypeGUIDs,
                                                                requestedInstanceStatus,
                                                                null,
                                                                asOfTime,
                                                                level);
            if (response.getResponseCategory() == ResponseCategory.OmrsInstanceGraph) {
                InstanceGraphResponse instanceGraphResponse = (InstanceGraphResponse) response;
                InstanceGraph instanceGraph = instanceGraphResponse.getInstanceGraph();
                Graph graph = new Graph();
                Set<Node> nodes = null;
                Set<Line> lines = null;

                List<EntityDetail> entities = instanceGraph.getEntities();
                List<Relationship> relationships = instanceGraph.getRelationships();

                if (entities != null && !entities.isEmpty()) {
                    nodes = new HashSet<>();
                    for (EntityDetail entity : entities) {
                        Node node = new Node();
                        InstanceStatus instanceStatus = entity.getStatus();
                        Status omas_status = SubjectAreaUtils.convertInstanceStatusToStatus(instanceStatus);
                        SystemAttributes systemAttributes = new SystemAttributes();
                        systemAttributes.setCreatedBy(entity.getCreatedBy());
                        systemAttributes.setStatus(omas_status);
                        systemAttributes.setCreatedBy(entity.getCreatedBy());
                        systemAttributes.setUpdatedBy(entity.getUpdatedBy());
                        systemAttributes.setCreateTime(entity.getCreateTime());
                        systemAttributes.setUpdateTime(entity.getUpdateTime());
                        systemAttributes.setVersion(entity.getVersion());
                        systemAttributes.setGUID(entity.getGUID());
                        node.setSystemAttributes(systemAttributes);
                        InstanceProperties entityProperties = entity.getProperties();
                        if (entityProperties != null) {
                            node.setEffectiveFromTime(entity.getProperties().getEffectiveFromTime());
                            node.setEffectiveToTime(entity.getProperties().getEffectiveToTime());
                            Iterator omrsPropertyIterator = entityProperties.getPropertyNames();

                            while (omrsPropertyIterator.hasNext()) {
                                String name = (String) omrsPropertyIterator.next();
                                InstancePropertyValue value = entityProperties.getPropertyValue(name);
                                // supplied guid matches the expected type
                                if (value.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE) {
                                    PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                                    Object actualValue = primitivePropertyValue.getPrimitiveValue();
                                    if (name.equals("displayName")) {
                                        node.setName((String) actualValue);
                                    }
                                    if (name.equals("name")) {
                                        node.setName((String) actualValue);
                                    }
                                    if (name.equals("description")) {
                                        node.setDescription((String) actualValue);
                                    }
                                    if (name.equals("qualifiedName")) {
                                        node.setQualifiedName((String) actualValue);
                                    }
                                }
                                break;
                            }
                        }   // end while
                        // change any subtypes into the NodeType value. for example subtypes of Category have NodeType Category
                        setNodeTypeInNode(methodName, oMRSRepositoryHelper, entity, node);
                        nodes.add(node);
                    }
                }
                if (relationships != null && !relationships.isEmpty()) {
                    lines = new HashSet<>();
                    for (Relationship relationship : relationships) {
                        Line line = new Line(relationship);
                        line.setLineType(LineType.Unknown);
                        String typeDefName = relationship.getType().getTypeDefName();
                        for (LineType lineTypeValue : LineType.values()) {
                            if (lineTypeValue.name().equals(typeDefName)) {
                                line.setLineType(lineTypeValue);
                            }
                        }
                        lines.add(line);
                    }
                }
                if (nodes != null) {
                    graph.setNodes(nodes);
                }
                if (lines != null) {
                    graph.setLines(lines);
                }
                response = new GraphResponse(graph);
            } // end of if after getEntityNeighbourhood call
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }

    /**
     * Set the NodeType in the node based on the entity contents
     *
     * @param methodName           - method name for logging
     * @param oMRSRepositoryHelper helper to allow us to issue isTypeOf call
     * @param entity               EntityDetail - that contains classifications and type used to determine the NodeType
     * @param node                 the node to update
     */
    private void setNodeTypeInNode(String methodName, OMRSRepositoryHelper oMRSRepositoryHelper, EntityDetail entity, Node node) {
        String typeDefName = entity.getType().getTypeDefName();

        String nodeType = null;
        if (oMRSRepositoryHelper.isTypeOf(methodName, typeDefName, "GlossaryTerm")) {
            nodeType = "Term";
        }
        if (oMRSRepositoryHelper.isTypeOf(methodName, typeDefName, "GlossaryCategory")) {
            nodeType = "Category";
        }
        if (oMRSRepositoryHelper.isTypeOf(methodName, typeDefName, "Glossary")) {
            nodeType = "Glossary";
        }
        if (oMRSRepositoryHelper.isTypeOf(methodName, typeDefName, "Asset")) {
            nodeType = "Asset";
        }
        if (oMRSRepositoryHelper.isTypeOf(methodName, typeDefName, "Project")) {
            nodeType = "Project";
        }
        List<Classification> classifications = entity.getClassifications();
        Set<String> classificationNames = null;
        if (classifications != null && !classifications.isEmpty()) {
            classificationNames = classifications.stream().map(x -> x.getName()).collect(Collectors.toSet());
        }
        /*
         * the nodeType variable needs to be changed for certain classifications.
         */
        for (NodeType nodeTypeValue : NodeType.values()) {
            if (nodeTypeValue.name().equals(nodeType)) {
                if (classificationNames != null) {
                    if (nodeType.equals("Category") && classificationNames.contains("SubjectArea")) {
                        node.setNodeType(NodeType.SubjectAreaDefinition);
                    } else if (nodeType.equals("Glossary")) {
                        if (classificationNames.contains("Taxonomy") && classificationNames.contains("CanonicalGlossary")) {
                            node.setNodeType(NodeType.TaxonomyAndCanonicalGlossary);
                        } else if (classificationNames.contains("Taxonomy")) {
                            node.setNodeType(NodeType.Taxonomy);
                        } else if (classificationNames.contains("CanonicalGlossary")) {
                            node.setNodeType(NodeType.CanonicalGlossary);
                        }
                    } else if (typeDefName.equals("Term") && classificationNames.contains("Activity")) {
                        node.setNodeType(NodeType.Activity);
                    } else if (typeDefName.equals("Project") && classificationNames.contains("GlossaryProject")) {
                        node.setNodeType(NodeType.GlossaryProject);
                    }
                }
                if (node.getNodeType() == NodeType.Unknown) {
                    node.setNodeType(nodeTypeValue);
                }
            }
        }
    }
}
