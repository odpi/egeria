/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaGraphHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ExceptionMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaGraphRESTServices extends SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGraphRESTServices.class);
    private static final String className = SubjectAreaGraphRESTServices.class.getName();
    static private SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();


    /**
     * Default constructor
     */
    public SubjectAreaGraphRESTServices() {}
    public SubjectAreaGraphRESTServices(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper =oMRSAPIHelper;
    }


    /**
     * Get the graph of nodes and Lines radiating out from a node.
     *
     * Return the nodes and Lines that radiate out from the supplied node (identified by a GUID).
     * The results are scoped by types of Lines, types of nodes and classifications as well as level.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param guid the starting point of the query.
     * @param nodeFilterStr Comma separated list of node names to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param lineFilterStr comma separated list of line names to include in the query results.  Null means include
     *                                all Lines found, irrespective of their type.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param statusFilter By default only active instances are returned. Specify ALL to see all instance in any status.
     * @param level the number of the Lines (relationships) out from the starting node that the query will traverse to
     *              gather results. If not specified then it defaults to 3.
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
    public  SubjectAreaOMASAPIResponse getGraph(String serverName,
                                                String userId,
                                                String guid,
                                                Date asOfTime,
                                                String nodeFilterStr,
                                                String lineFilterStr,
                                                StatusFilter statusFilter,   // may need to extend this for controlled terms
                                                Integer level ) {

        final String methodName = "getGraph";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaGraphHandler handler = instanceHandler.getSubjectAreaGraphHandler(userId, serverName, methodName);
            response = handler.getGraph(
                                        userId,
                                        guid,
                                        asOfTime,
                                        nodeFilterStr,
                                        lineFilterStr,
                                        statusFilter,   // may need to extend this for controlled terms
                                        level);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }

    /**
     * Set the NodeType in the node based on the entity contents
     * @param methodName - method name for logging
     * @param oMRSRepositoryHelper helper to allow us to issue isTypeOf call
     * @param entity EntityDetail - that contains classifications and type used to determine the NodeType
     * @param node the node to update
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
            classificationNames = classifications.stream().map(Classification::getName).collect(Collectors.toSet());
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
