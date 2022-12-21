/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CATEGORY_ANCHOR;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_CATEGORY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_ANCHOR;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_CATEGORIZATION;

/**
 * The Glossary Context Handler provides methods to build graph context for glossary terms.
 */
public class GlossaryContextHandler {

    private final InvalidParameterHandler invalidParameterHandler;
    private final HandlerHelper handlerHelper;
    private final AssetContextHandler assetContextHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for invalid parameters
     * @param handlerHelper           the helper handler
     */
    public GlossaryContextHandler(InvalidParameterHandler invalidParameterHandler,  AssetContextHandler assetContextHandler,
                                  HandlerHelper handlerHelper) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.handlerHelper = handlerHelper;
        this.assetContextHandler = assetContextHandler;
    }

    /**
     * Returns the Glossary Term entity details based on the GlossaryTerm GUID
     *
     * @param userId           the unique identifier for the user
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
     * Builds the context for a Glossary Term.
     * This context contains the full description for the Schema Elements that have a Semantic Assigment to the GlossaryTerm
     *
     * @param userId       the unique identifier for the user
     * @param glossaryTerm the glossary term entity for which the context is built
     *
     * @return a map that contains the Glossary Term relationships and context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Multimap<String, RelationshipsContext> buildGlossaryTermContext(String userId, EntityDetail glossaryTerm) throws OCFCheckedExceptionBase {
        String methodName = "buildGlossaryTermContext";

        String glossaryTermGUID = glossaryTerm.getGUID();
        invalidParameterHandler.validateGUID(glossaryTermGUID, GUID_PARAMETER, methodName);

        List<Relationship> semanticAssignments = getSemanticAssignments(userId, glossaryTermGUID, GLOSSARY_TERM);
        List<Relationship> termCategorizations = getTermCategorizations(userId, glossaryTermGUID, GLOSSARY_TERM);
        List<Relationship> glossaries = getTermAnchors(userId, glossaryTermGUID);

        Multimap<String, RelationshipsContext> context = ArrayListMultimap.create();
        if (Stream.of(semanticAssignments, termCategorizations, glossaries).allMatch(CollectionUtils::isEmpty)) {
            return context;
        }

        context.put(AssetLineageEventType.SEMANTIC_ASSIGNMENTS_EVENT.getEventTypeName(), handlerHelper.buildContextForRelationships(userId,
                glossaryTermGUID, semanticAssignments));
        context.put(AssetLineageEventType.TERM_CATEGORIZATIONS_EVENT.getEventTypeName(), handlerHelper.buildContextForRelationships(userId,
                glossaryTermGUID, termCategorizations));
        context.put(AssetLineageEventType.TERM_ANCHORS_EVENT.getEventTypeName(), handlerHelper.buildContextForRelationships(userId,
                glossaryTermGUID, glossaries));

        List<Relationship> glossariesForCategories = getGlossariesForCategories(userId, glossaryTermGUID, termCategorizations);
        context.put(AssetLineageEventType.CATEGORY_ANCHORS_EVENT.getEventTypeName(), handlerHelper.buildContextForRelationships(userId,
                glossaryTermGUID, glossariesForCategories));


        context.put(AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT.getEventTypeName(),
                handlerHelper.buildContextForLineageClassifications(glossaryTerm));

        Set<EntityDetail> schemaElementsAttached = getSchemaElementsAttached(userId, glossaryTerm);
        for (EntityDetail schemaElement : schemaElementsAttached) {
            context.putAll(Multimaps.forMap(assetContextHandler.buildSchemaElementContext(userId, schemaElement)));
        }

        return context;
    }

    /**
     * Fetch the Glossary Categories for a list of Term Categorizations of a Glossary Term
     *
     * @param userId             the unique identifier for the user
     * @param glossaryTermGUID   the glossary term GUID
     * @param termCategorization the list of term categorizations to
     *
     * @return a list of Category Anchor Relationships for the Term Categorizations
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
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
     * Fetch the Semantic Assignments Relationships for a Glossary Term
     *
     * @param userId         the unique identifier for the user
     * @param entityGUID     the entity identifier
     * @param entityTypeName the entity type name
     *
     * @return the list of semantic assignments available for the given glossary term
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private List<Relationship> getSemanticAssignments(String userId, String entityGUID, String entityTypeName) throws OCFCheckedExceptionBase {
        return handlerHelper.getRelationshipsByType(userId, entityGUID, SEMANTIC_ASSIGNMENT, entityTypeName);
    }

    /**
     * Fetch the Term Categorization Relationships for a Glossary Term
     *
     * @param userId         the unique identifier for the user
     * @param entityGUID     the entity identifier
     * @param entityTypeName the entity type name
     *
     * @return the list of term categorization relationships available for the
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private List<Relationship> getTermCategorizations(String userId, String entityGUID, String entityTypeName) throws OCFCheckedExceptionBase {
        return handlerHelper.getRelationshipsByType(userId, entityGUID, TERM_CATEGORIZATION, entityTypeName);
    }

    /**
     * Fetch the Term Anchor Relationships for an entity
     *
     * @param userId     the unique identifier for the user
     * @param entityGUID the entity identifier
     *
     * @return the list of term categorization relationships available for the
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private List<Relationship> getTermAnchors(String userId, String entityGUID) throws OCFCheckedExceptionBase {
        return handlerHelper.getRelationshipsByType(userId, entityGUID, TERM_ANCHOR, GLOSSARY_TERM);
    }

    /**
     * Fetch the Category Anchor Relationships for a Glossary Category
     *
     * @param userId       the unique identifier for the user
     * @param categoryGUID the category identifier
     *
     * @return the list of term categorization relationships available for the glossary category
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    private List<Relationship> getCategoryAnchor(String userId, String categoryGUID) throws OCFCheckedExceptionBase {
        return handlerHelper.getRelationshipsByType(userId, categoryGUID, CATEGORY_ANCHOR, GLOSSARY_CATEGORY);
    }

    /**
     * Checks if the glossary term is involved in lineage relationships
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the glossary term entity for which the context is built
     *
     * @return true if there are lineage relationships for the glossary term
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public boolean hasGlossaryTermLineageRelationships(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        String typeDefName = entityDetail.getType().getTypeDefName();
        String entityDetailGUID = entityDetail.getGUID();

        List<Relationship> semanticAssignments = getSemanticAssignments(userId, entityDetailGUID, typeDefName);
        if (CollectionUtils.isNotEmpty(semanticAssignments)) {
            return true;
        }

        return CollectionUtils.isNotEmpty(getTermCategorizations(userId, entityDetailGUID, entityDetail.getType().getTypeDefName()));
    }

    /**
     * Gets the list of Schema Elements attached to a Glossary Term by Semantic Assignments Relationships
     *
     * @param userId       the unique identifier for the user
     * @param glossaryTerm the glossary term for which the context is built
     *
     * @return the list of Schema Elements attached to a Glossary Term by Semantic Assignments Relationships
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private Set<EntityDetail> getSchemaElementsAttached(String userId, EntityDetail glossaryTerm) throws OCFCheckedExceptionBase {
        List<Relationship> semanticAssignments = getSemanticAssignments(userId, glossaryTerm.getGUID(), GLOSSARY_TERM);

        Set<EntityDetail> schemaElements = new HashSet<>();
        for (Relationship semanticAssignment : semanticAssignments) {
            schemaElements.add(handlerHelper.getEntityAtTheEnd(userId, glossaryTerm.getGUID(), semanticAssignment));
        }
        return schemaElements;
    }
}
