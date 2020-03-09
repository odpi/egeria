/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.SuperTypesRetriever;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.*;

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
    public GlossaryHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper, RepositoryHandler repositoryHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, repositoryHandler);
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

    /**
     * Retrieves semantic assignments for an asset
     *
     * @param userId      userId
     * @param assetGuid   guid of the asset that has been created.
     * @param typeDefName the typeName of the asset.
     * @return Glossary Term retrieved from the property server
     */
    private void getGlossary(String userId, String assetGuid, String typeDefName) throws OCFCheckedExceptionBase {
        final String methodName = "getGlossary";

        String typeGuid = handlerHelper.getTypeName(userId, SEMANTIC_ASSIGNMENT);
        List<Relationship> semanticAssignments = repositoryHandler.getRelationshipsByType(userId,
                assetGuid,
                typeDefName,
                typeGuid,
                SEMANTIC_ASSIGNMENT,
                methodName);

        if (semanticAssignments == null)
            return;

        addSemanticAssignmentToContext(userId, semanticAssignments.toArray(new Relationship[0]));
    }

    /**
     * Add semantic assignments for an asset to the Context structure
     *
     * @param userId              userId
     * @param semanticAssignments array of the semantic assignments
     * @return true if semantic relationships exist, false otherwise
     */
    private void addSemanticAssignmentToContext(String userId, Relationship... semanticAssignments) throws OCFCheckedExceptionBase {
        final String methodName = "addSemanticAssignmentToContext";

        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : semanticAssignments) {

            String glossaryTermGuid = relationship.getEntityTwoProxy().getGUID();
            EntityDetail glossaryTerm = repositoryHandler.getEntityByGUID(userId,
                    glossaryTermGuid,
                    "guid",
                    GLOSSARY_TERM,
                    methodName);

            entityDetails.add(handlerHelper.buildGraphEdgeByRelationship(userId, glossaryTerm, relationship, graph, false));
        }
    }

}

