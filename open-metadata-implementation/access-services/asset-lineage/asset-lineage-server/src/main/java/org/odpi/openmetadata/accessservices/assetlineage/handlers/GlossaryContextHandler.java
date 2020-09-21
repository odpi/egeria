/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_CATEGORIZATION;

/**
 * The glossary handler provide methods to provide business glossary terms for lineage.
 */
public class GlossaryContextHandler {

    private RepositoryHandler repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;
    private AssetContextHandler assetContextHandler;
    private HandlerHelper handlerHelper;

    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     */
    public GlossaryContextHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                                  RepositoryHandler repositoryHandler, AssetContextHandler assetContextHandler,
                                  Set<String> lineageClassificationTypes) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, repositoryHandler, lineageClassificationTypes);
        this.assetContextHandler = assetContextHandler;
    }

    /**
     * Returns the Glossary Term entity details based on the GlossaryTerm GUID
     *
     * @param userId           String - userId of user making request.
     * @param glossaryTermGUID the glossary term GUID
     * @return the entity details for a glossary term based on the glossary term guid
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public EntityDetail getGlossaryTermDetails(String userId, String glossaryTermGUID) throws OCFCheckedExceptionBase {
        return handlerHelper.getEntityDetails(userId, glossaryTermGUID, GLOSSARY_TERM);
    }

    /**
     * Returns the context for a Glossary Term.
     * This context contains the full description for the Schema Elements that have a Semantic Assigment to the GlossaryTerm
     *
     * @param userId       String - userId of user making request.
     * @param glossaryTerm the glossary term entity for which the context is built
     * @return a map that contains the Glossary Term relationships and context
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Map<String, Set<GraphContext>> buildGlossaryTermContext(String userId,
                                                                   EntityDetail glossaryTerm) throws OCFCheckedExceptionBase {
        String methodName = "buildGlossaryTermContext";

        String glossaryTermGUID = glossaryTerm.getGUID();
        invalidParameterHandler.validateGUID(glossaryTermGUID, GUID_PARAMETER, methodName);

        AssetContext glossaryContext = new AssetContext();

        List<Relationship> semanticAssignments = getSemanticAssignments(userId, glossaryTermGUID, GLOSSARY_TERM);
        List<Relationship> termCategorization = getTermCategorizations(userId, glossaryTermGUID, GLOSSARY_TERM);
        if (CollectionUtils.isEmpty(semanticAssignments) && CollectionUtils.isEmpty(termCategorization)) {
            return null;
        }

        handlerHelper.addLineageClassificationToContext(glossaryTerm, glossaryContext);
        addSchemaElementsContext(userId, glossaryTerm, glossaryContext, semanticAssignments);
        addGlossaryCategories(userId, glossaryTerm, glossaryContext, termCategorization);

        return glossaryContext.getNeighbors();
    }

    /**
     * Add Semantic Assignment relationships to the Glossary Term context
     *
     * @param userId              the user of user making request.
     * @param glossaryTerm        the glossary term entity for which the context is built
     * @param glossaryContext     the glossary term context
     * @param semanticAssignments list of Semantic Assignment relationships
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private void addSchemaElementsContext(String userId,
                                          EntityDetail glossaryTerm,
                                          AssetContext glossaryContext,
                                          List<Relationship> semanticAssignments) throws OCFCheckedExceptionBase {
        if (CollectionUtils.isEmpty(semanticAssignments)) {
            return;
        }

        Set<EntityDetail> schemaElements = addGlossaryTermRelationshipsToContext(userId, glossaryTerm, glossaryContext, semanticAssignments);
        for (EntityDetail schemaElement : schemaElements) {
            AssetContext schemaElementContext = assetContextHandler.getAssetContext(userId, schemaElement);
            glossaryContext.getGraphContexts().addAll(schemaElementContext.getGraphContexts());

            schemaElementContext.getNeighbors().forEach((k, v) -> mergeGraphNeighbors(glossaryContext, k, v));
        }
    }


    /**
     * Add Term Categorization relationships to the Glossary Term context
     *
     * @param userId             the user Id of user making request.
     * @param glossaryTerm       the glossary term entity for which the context is built
     * @param glossaryContext    the glossary term context
     * @param termCategorization the Term Categorization relationship list
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private void addGlossaryCategories(String userId,
                                       EntityDetail glossaryTerm,
                                       AssetContext glossaryContext,
                                       List<Relationship> termCategorization) throws OCFCheckedExceptionBase {
        if (CollectionUtils.isEmpty(termCategorization)) {
            return;
        }

        addGlossaryTermRelationshipsToContext(userId, glossaryTerm, glossaryContext, termCategorization);
    }

    private void mergeGraphNeighbors(AssetContext glossaryContext, String k, Set<GraphContext> v) {
        if (glossaryContext.getNeighbors().containsKey(k)) {
            glossaryContext.getNeighbors().get(k).addAll(v);
        } else {
            glossaryContext.getNeighbors().put(k, v);
        }
    }

    /**
     * Add semantic assignments for an asset to the Context structure
     *
     * @param userId        the userId of user making request.
     * @param assetContext  context of the asset
     * @param relationships array of the semantic assignments
     * @return a set of schema elements assigned to the Glossary Term
     */
    private Set<EntityDetail> addGlossaryTermRelationshipsToContext(String userId,
                                                                    EntityDetail glossaryTerm,
                                                                    AssetContext assetContext,
                                                                    List<Relationship> relationships) throws OCFCheckedExceptionBase {
        Set<EntityDetail> endEntityDetails = new HashSet<>();

        for (Relationship relationship : relationships) {
            EntityDetail entityDetail = handlerHelper.buildGraphEdgeByRelationship(userId, glossaryTerm, relationship, assetContext);
            endEntityDetails.add(entityDetail);
        }

        return endEntityDetails;
    }

    /**
     * Fetch the Semantic Assignments Relationships for an entity
     *
     * @param userId         the userId of user making request.
     * @param entityTypeGUID the entity identifier
     * @param entityTypeName the entity type name
     * @return the list of semantic assignments available for the given glossary term
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    private List<Relationship> getSemanticAssignments(String userId,
                                                      String entityTypeGUID,
                                                      String entityTypeName) throws UserNotAuthorizedException, PropertyServerException {
        return getRelationshipsByTypeGUID(userId, entityTypeGUID, entityTypeName, SEMANTIC_ASSIGNMENT);
    }

    /**
     * Fetch the Term Categorization Relationships for an entity
     *
     * @param userId         the user Id of user making request.
     * @param entityGUID     the entity identifier
     * @param entityTypeName the entity type name
     * @return the list of term categorization relationships available for the
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    private List<Relationship> getTermCategorizations(String userId,
                                                      String entityGUID,
                                                      String entityTypeName) throws UserNotAuthorizedException, PropertyServerException {
        return getRelationshipsByTypeGUID(userId, entityGUID, entityTypeName, TERM_CATEGORIZATION);
    }

    /**
     * Fetch the relationship by name for a given entity
     *
     * @param userId               the user Id of user making request.
     * @param entityGUID           the entity identifier
     * @param entityTypeName       the entity type name
     * @param relationshipTypeName the relationship type name
     * @return the list of available relationship
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    private List<Relationship> getRelationshipsByTypeGUID(String userId,
                                                          String entityGUID,
                                                          String entityTypeName,
                                                          String relationshipTypeName) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getRelationshipsByTypeGUID";
        String relationshipTypeGUID = handlerHelper.getTypeByName(userId, relationshipTypeName);

        return repositoryHandler.getRelationshipsByType(userId,
                entityGUID,
                entityTypeName,
                relationshipTypeGUID,
                relationshipTypeName,
                methodName);
    }

    /**
     * Checks if the glossary term is involved in lineage relationships
     *
     * @param userId       the user Id of user making request.
     * @param entityDetail the glossary term entity for which the context is built
     * @return true if there are lineage relationships for the glossary term
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    public boolean hasGlossaryTermLineageRelationships(String userId, EntityDetail entityDetail)
            throws UserNotAuthorizedException, PropertyServerException {
        String typeDefName = entityDetail.getType().getTypeDefName();
        String entityDetailGUID = entityDetail.getGUID();

        List<Relationship> semanticAssignments = getSemanticAssignments(userId, entityDetailGUID, typeDefName);
        if (CollectionUtils.isNotEmpty(semanticAssignments)) {
            return true;
        }

        return CollectionUtils.isNotEmpty(getTermCategorizations(userId, entityDetailGUID, entityDetail.getType().getTypeDefGUID()));
    }
}
