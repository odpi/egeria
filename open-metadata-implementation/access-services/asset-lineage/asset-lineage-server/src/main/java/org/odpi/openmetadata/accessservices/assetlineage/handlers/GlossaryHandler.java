/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.SuperTypesRetriever;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.COMPLEX_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SCHEMA_ELEMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SEMANTIC_ASSIGNMENT;

/**
 * The glossary handler provide methods to provide business glossary terms for lineage.
 */
public class GlossaryHandler {

    private RepositoryHandler repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;
    private AssetContext graph = new AssetContext();
    private HandlerHelper handlerHelper;

    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     */
    public GlossaryHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                           RepositoryHandler repositoryHandler, List<String> lineageClassificationTypes) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, repositoryHandler, lineageClassificationTypes);
    }

    /**
     * Returns the glossary term object corresponding to the supplied asset that can possibly have a glossary Term.
     *
     * @param assetGuid    guid of the asset that has been created
     * @param userId       String - userId of user making request.
     * @param assetContext the asset context
     * @return Glossary Term retrieved from the repository, null if not semantic assignment to the asset
     * @throws InvalidParameterException the invalid parameter exception
     */
    public Map<String, Set<GraphContext>> getGlossaryTerm(String assetGuid,
                                                          String userId,
                                                          AssetContext assetContext,
                                                          SuperTypesRetriever superTypesRetriever) throws OCFCheckedExceptionBase {

        String methodName = "getGlossaryTerm";

        invalidParameterHandler.validateGUID(assetGuid, GUID_PARAMETER, methodName);

        graph = assetContext;

        Set<LineageEntity> vertices = assetContext.getVertices();
        vertices = vertices.stream().filter(vertex -> superTypesRetriever.getSuperTypes(vertex.getTypeDefName()).contains(SCHEMA_ELEMENT) &&
                !superTypesRetriever.getSuperTypes(vertex.getTypeDefName()).contains(COMPLEX_SCHEMA_TYPE)).collect(Collectors.toSet());

        for (LineageEntity vertex : vertices)
            getGlossary(userId, vertex.getGuid(), vertex.getTypeDefName());

        return graph.getNeighbors();
    }

    public Map<String, Set<GraphContext>> buildGlossaryTermContext(String userId, EntityDetail glossaryTerm) throws OCFCheckedExceptionBase {
        String methodName = "buildGlossaryTermContext";
        invalidParameterHandler.validateGUID(glossaryTerm.getGUID(), GUID_PARAMETER, methodName);

        AssetContext glossaryContext = new AssetContext();

        List<Relationship> semanticAssignments = getSemanticAssignments(userId, glossaryTerm.getGUID(), glossaryTerm.getType().getTypeDefName(), methodName);
        if (CollectionUtils.isEmpty(semanticAssignments)) {
            return null;
        }

        addSemanticAssignmentToContext(userId, glossaryTerm, glossaryContext, semanticAssignments.toArray(new Relationship[0]));
        handlerHelper.addLineageClassificationToContext(glossaryTerm, glossaryContext);

        return glossaryContext.getNeighbors();
    }

    /**
     * Retrieves semantic assignments for a Glossary Term
     *
     * @param userId           userId
     * @param glossaryTermGUID guid of the glossary term
     * @param typeDefName      the typeName of the asset.
     */
    private void getGlossary(String userId, String glossaryTermGUID, String typeDefName) throws OCFCheckedExceptionBase {
        final String methodName = "getGlossary";

        EntityDetail glossaryTerm = repositoryHandler.getEntityByGUID(userId, glossaryTermGUID, "guid", typeDefName, methodName);
        handlerHelper.addLineageClassificationToContext(glossaryTerm, graph);

        List<Relationship> semanticAssignments = getSemanticAssignments(userId, glossaryTermGUID, typeDefName, methodName);
        if (semanticAssignments == null) return;

        addSemanticAssignmentToContext(userId, glossaryTerm, graph, semanticAssignments.toArray(new Relationship[0]));
    }

    /**
     * Add semantic assignments for an asset to the Context structure
     *
     * @param userId              userId
     * @param assetContext        context of the asset
     * @param semanticAssignments array of the semantic assignments
     */
    private void addSemanticAssignmentToContext(String userId, EntityDetail glossaryTerm, AssetContext assetContext, Relationship... semanticAssignments) throws OCFCheckedExceptionBase {
        for (Relationship relationship : semanticAssignments) {
            handlerHelper.buildGraphEdgeByRelationship(userId, glossaryTerm, relationship, assetContext);
        }
    }

    private List<Relationship> getSemanticAssignments(String userId, String glossaryTermGUID, String typeDefName, String methodName) throws UserNotAuthorizedException, PropertyServerException {
        String typeGuid = handlerHelper.getTypeByName(userId, SEMANTIC_ASSIGNMENT);

        return repositoryHandler.getRelationshipsByType(userId,
                glossaryTermGUID,
                typeDefName,
                typeGuid,
                SEMANTIC_ASSIGNMENT,
                methodName);
    }
}
