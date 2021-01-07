/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CATEGORY_ANCHOR;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_CATEGORY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_ANCHOR;
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
     *
     * @return the entity details for a glossary term based on the glossary term guid
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the entity.
     */
    public EntityDetail getGlossaryTermDetails(String userId, String glossaryTermGUID) throws InvalidParameterException, PropertyServerException,
                                                                                              UserNotAuthorizedException {
        return handlerHelper.getEntityDetails(userId, glossaryTermGUID, GLOSSARY_TERM);
    }

    /**
     * Returns the context for a Glossary Term.
     * This context contains the full description for the Schema Elements that have a Semantic Assigment to the GlossaryTerm
     *
     * @param userId       String - userId of user making request.
     * @param glossaryTerm the glossary term entity for which the context is built
     *
     * @return a map that contains the Glossary Term relationships and context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Map<String, Set<GraphContext>> buildGlossaryTermContext(String userId, EntityDetail glossaryTerm) throws OCFCheckedExceptionBase {
        String methodName = "buildGlossaryTermContext";

        String glossaryTermGUID = glossaryTerm.getGUID();
        invalidParameterHandler.validateGUID(glossaryTermGUID, GUID_PARAMETER, methodName);

        Map<String, Set<GraphContext>> context = new HashMap<>();

        List<Relationship> semanticAssignments = getSemanticAssignments(userId, glossaryTermGUID, GLOSSARY_TERM);
        context.put(AssetLineageEventType.SEMANTIC_ASSIGNMENTS_EVENT.getEventTypeName(), handlerHelper.buildContextForRelationships(userId, glossaryTerm,
                semanticAssignments));

        List<Relationship> termCategorizations = getTermCategorizations(userId, glossaryTermGUID, GLOSSARY_TERM);
        context.put(AssetLineageEventType.TERM_CATEGORIZATIONS_EVENT.getEventTypeName(), handlerHelper.buildContextForRelationships(userId, glossaryTerm,
                termCategorizations));

        List<Relationship> glossary = getTermAnchor(userId, glossaryTermGUID, GLOSSARY_TERM);
        context.put(AssetLineageEventType.TERM_ANCHOR_EVENT.getEventTypeName(), handlerHelper.buildContextForRelationships(userId, glossaryTerm, glossary));

        List<Relationship> glossariesForCategories = getGlossariesForCategories(userId, glossaryTermGUID, termCategorizations);
        context.put(AssetLineageEventType.GLOSSARY_CATEGORIES_EVENT.getEventTypeName(), handlerHelper.buildContextForRelationships(userId, glossaryTerm, glossariesForCategories));

        // handlerHelper.addLineageClassificationToContext(glossaryTerm, glossaryContext);

//        Set<EntityDetail> schemaElements = handlerHelper.getSchemaElements(userId, glossaryTerm, semanticAssignments);
//        for (EntityDetail schemaElement : schemaElements) {
//            context.put(schemaElement.getGUID(), buildSchemaElementsContext(userId, schemaElement));
//        }
        return context;
    }

    private List<Relationship> getGlossariesForCategories(String userId, String glossaryTermGUID, List<Relationship> termCategorization) throws
                                                                                                                                    OCFCheckedExceptionBase {
        List<Relationship> categories = new ArrayList<>();

        for (Relationship relationship : termCategorization) {
            if (glossaryTermGUID.equals(relationship.getEntityOneProxy().getGUID())) {
                categories.addAll(getCategoryAnchor(userId, relationship.getEntityTwoProxy().getGUID()));
            } else {
                categories.addAll(getCategoryAnchor(userId, relationship.getEntityOneProxy().getGUID()));
            }
        }
        return categories;
    }


    /**
     * Add Schema Elements entities to the Glossary Term context
     *
     * @param userId the user of user making request.
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private Set<GraphContext> buildSchemaElementsContext(String userId,
                                                         EntityDetail schemaElement) throws OCFCheckedExceptionBase {

        AssetContext schemaElementContext = assetContextHandler.getAssetContext(userId, schemaElement);
        return schemaElementContext.getGraphContexts();

    }


    /**
     * Fetch the Semantic Assignments Relationships for an entity
     *
     * @param userId         the userId of user making request.
     * @param entityTypeGUID the entity identifier
     * @param entityTypeName the entity type name
     *
     * @return the list of semantic assignments available for the given glossary term
     *
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
     *
     * @return the list of term categorization relationships available for the
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    private List<Relationship> getTermCategorizations(String userId,
                                                      String entityGUID,
                                                      String entityTypeName) throws UserNotAuthorizedException, PropertyServerException {
        return getRelationshipsByTypeGUID(userId, entityGUID, entityTypeName, TERM_CATEGORIZATION);
    }

    /**
     * Fetch the Term Anchor Relationships for an entity
     *
     * @param userId         the user Id of user making request.
     * @param entityGUID     the entity identifier
     * @param entityTypeName the entity type name
     *
     * @return the list of term categorization relationships available for the
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    private List<Relationship> getTermAnchor(String userId,
                                             String entityGUID,
                                             String entityTypeName) throws UserNotAuthorizedException, PropertyServerException {
        return getRelationshipsByTypeGUID(userId, entityGUID, entityTypeName, TERM_ANCHOR);
    }

    /**
     * Fetch the Category Anchor Relationships for a classification
     *
     * @param userId       the user Id of user making request.
     * @param categoryGUID the actegory identifier
     *
     * @return the list of term categorization relationships available for the
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    private List<Relationship> getCategoryAnchor(String userId, String categoryGUID)
            throws UserNotAuthorizedException, PropertyServerException {
        return getRelationshipsByTypeGUID(userId, categoryGUID, GLOSSARY_CATEGORY, CATEGORY_ANCHOR);
    }

    /**
     * Fetch the relationship by name for a given entity
     *
     * @param userId               the user Id of user making request.
     * @param entityGUID           the entity identifier
     * @param entityTypeName       the entity type name
     * @param relationshipTypeName the relationship type name
     *
     * @return the list of available relationship
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    private List<Relationship> getRelationshipsByTypeGUID(String userId,
                                                          String entityGUID,
                                                          String entityTypeName,
                                                          String relationshipTypeName) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getRelationshipsByTypeGUID";
        String relationshipTypeGUID = handlerHelper.getTypeByName(userId, relationshipTypeName);

        List<Relationship> relationshipsByType = repositoryHandler.getRelationshipsByType(userId,
                entityGUID,
                entityTypeName,
                relationshipTypeGUID,
                relationshipTypeName,
                methodName);

        if (CollectionUtils.isEmpty(relationshipsByType)) {
            return Collections.emptyList();
        }

        return relationshipsByType
                .stream()
                .filter(relationship -> relationship.getEntityOneProxy() != null && relationship.getEntityTwoProxy() != null)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the glossary term is involved in lineage relationships
     *
     * @param userId       the user Id of user making request.
     * @param entityDetail the glossary term entity for which the context is built
     *
     * @return true if there are lineage relationships for the glossary term
     *
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
