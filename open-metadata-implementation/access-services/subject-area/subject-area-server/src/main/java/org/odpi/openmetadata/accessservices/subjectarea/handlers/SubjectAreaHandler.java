/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.OmasObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.INodeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.Mapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.MappersFactory;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.*;


/**
 * SubjectAreaProjectHandler manages Project objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public abstract class SubjectAreaHandler {
    private static final String className = SubjectAreaHandler.class.getName();

    protected static final String GLOSSARY_TYPE_NAME = "Glossary";
    protected static final String CATEGORY_TYPE_NAME = "GlossaryCategory";
    protected static final String TERM_TYPE_NAME = "GlossaryTerm";
    protected static final String PROJECT_TYPE_NAME = "Project";

    protected static final String TERM_ANCHOR_RELATIONSHIP_NAME = "TermAnchor";
    protected static final String PROJECT_SCOPE_RELATIONSHIP_NAME = "ProjectScope";
    protected static final String CATEGORY_ANCHOR_RELATIONSHIP_NAME = "CategoryAnchor";
    protected static final String CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME ="CategoryHierarchyLink";

    protected final MappersFactory mappersFactory;
    protected final OMRSAPIHelper oMRSAPIHelper;

    /**
     * Construct the Subject Area Project Handler
     * needed to operate within a single server instance.
     *
     * @param oMRSAPIHelper           omrs API helper
     */
    public SubjectAreaHandler(OMRSAPIHelper oMRSAPIHelper) {
        this.oMRSAPIHelper = oMRSAPIHelper;
        this.mappersFactory = new MappersFactory(oMRSAPIHelper);
    }

    /**
     * Get glossary summary
     * @param restAPIName rest API Name
     * @param userId userid under which to issue to the get of the related media
     * @param line glossary relationship {@link TermAnchor} or {@link CategoryAnchor}
     * @return Glossary summary
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    GlossarySummary getGlossarySummary(String restAPIName,
                                       String userId,
                                       Line line) throws UserNotAuthorizedException,
                                                         PropertyServerException,
                                                         InvalidParameterException,
                                                         SubjectAreaCheckedException
    {
        String guid = SubjectAreaUtils.getGlossaryGuidFromAnchor(line);
        Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid, GLOSSARY_TYPE_NAME, restAPIName);
        if (entityDetail.isPresent()) {
            GlossaryMapper glossaryMapper = mappersFactory.get(GlossaryMapper.class);
            Glossary glossary = glossaryMapper.map(entityDetail.get());
            // TODO sort out icons
            return SubjectAreaUtils.extractGlossarySummaryFromGlossary(glossary, line);
        }

        return null;
    }

    protected <T extends Node>List<T> findEntities(String userId,
                                                   String typeEntityName,
                                                   FindRequest findRequest,
                                                   Class<? extends INodeMapper<T>> mapperClass,
                                                   String methodName) throws SubjectAreaCheckedException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        List<EntityDetail> entityDetails = null;
        List<T> foundEntities = null;
        if (findRequest.getSearchCriteria() == null) {
            entityDetails = oMRSAPIHelper.getEntitiesByType(methodName, userId, typeEntityName, findRequest);
        } else {
            entityDetails = oMRSAPIHelper.findEntitiesByPropertyValue(methodName, userId, typeEntityName, findRequest);
        }
        if (entityDetails != null) {
            INodeMapper<T> iNodeMapper = mappersFactory.get(mapperClass);
            foundEntities = new ArrayList<>();
            for (EntityDetail entityDetail : entityDetails) {
                foundEntities.add(iNodeMapper.map(entityDetail));
            }
        }
        return foundEntities;
    }

    /**
     * Get All relationships for entity
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested guid
     * */
    public SubjectAreaOMASAPIResponse<Line> getAllRelationshipsForEntity(String methodName,
                                                                         String userId,
                                                                         String guid,
                                                                         FindRequest findRequest)
    {
        SubjectAreaOMASAPIResponse<Line> response = new SubjectAreaOMASAPIResponse<>();
        try {
            response.addAllResults(getAllLineForEntity(methodName, userId, guid, findRequest));
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get the relationships keyed off an entity guid.
     *
     * @param restAPIName             rest API name
     * @param userId                  user identity
     * @param entityGuid              globally unique identifier
     * @param findRequest             {@link FindRequest}
     * @return {@code List<Line>}
     */
    public List<Line> getAllLineForEntity(String restAPIName,
                                           String userId,
                                           String entityGuid,
                                           FindRequest findRequest) throws SubjectAreaCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException {
        List<Relationship> relationships = oMRSAPIHelper.getAllRelationshipsForEntity(restAPIName, userId, entityGuid, findRequest);
        return getLinesFromRelationships(relationships);
    }

    public List<Line> getLinesFromRelationships(Collection<Relationship> relationships) {
        return convertOrmsToOmas(relationships, ILineMapper.class);
    }

    public List<Node> getNodesFromEntityDetails(Collection<EntityDetail> entityDetails){
        return convertOrmsToOmas(entityDetails, INodeMapper.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <R extends InstanceHeader, T extends OmasObject>List<T>
    convertOrmsToOmas(Collection<R> list, Class<? extends Mapper> mapperInterface)
    {
        List<T> result =  new ArrayList<>();
        if (list != null) {
            Map<String, Mapper<R, T>> cache = new HashMap<>();
            for (R entityDetail : list) {
                String typeDefName = entityDetail.getType().getTypeDefName();
                if (cache.containsKey(typeDefName)) {
                    Mapper<R, T> mapper = cache.get(typeDefName);
                    result.add(mapper.map(entityDetail));
                } else {
                    Set<Class<?>> allMapperClasses = mappersFactory.getAllMapperClasses();
                    for (Class<?> mapperClass : allMapperClasses) {
                        if (mapperInterface.isAssignableFrom(mapperClass)) {
                            Mapper<R, T> mapper =  mapperInterface.cast(mappersFactory.get(mapperClass));
                            if (mapper.getTypeName().equals(typeDefName)) {
                                cache.put(typeDefName, mapper);
                                result.add(mapper.map(entityDetail));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * This method validated for creation.
     *
     * @param userId           userId under which the request is performed
     * @param methodName       method making the call
     * @param suppliedGlossary glossary to validate against.
     * @return SubjectAreaOMASAPIResponse this response is of type ResponseCategory.Category.Glossary if successful, otherwise there is an error response.
     */
    protected String validateGlossarySummaryDuringCreation(String userId,
                                                           String methodName,
                                                           GlossarySummary suppliedGlossary) throws UserNotAuthorizedException,
                                                                                                    PropertyServerException,
                                                                                                    InvalidParameterException,
                                                                                                    SubjectAreaCheckedException
    {
        /*
         * There needs to be an associated glossary supplied
         * The glossary could be of NodeType Glossary, Taxonomy , Canonical glossary or canonical and taxonomy.
         * The Glossary summary contains 4 identifying fields. We only require one of these fields to be supplied.
         * If more than one is supplied then we look for a glossary matching the supplied userId then matching the name.
         * Note if a relationship userId is supplied - then we reject this request - as the relationship cannot exist before one of its ends exists.
         */

        if (suppliedGlossary != null) {
            String guid = suppliedGlossary.getGuid();
            String relationshipGuid = suppliedGlossary.getRelationshipguid();
            if (relationshipGuid != null) {
                // glossary relationship cannot exist before the Term exists.
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.CREATE_WITH_GLOSSARY_RELATIONSHIP.getMessageDefinition();
                throw new InvalidParameterException(
                        messageDefinition,
                        className,
                        methodName,
                        "glossary",
                        null);
            }
            // find by guid
            Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid, GLOSSARY_TYPE_NAME, methodName);
            if (entityDetail.isPresent()) {
                return entityDetail.get().getGUID();
            } else {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.CREATE_WITHOUT_GLOSSARY.getMessageDefinition();
                throw new InvalidParameterException(
                        messageDefinition,
                        className,
                        methodName,
                        "glossary",
                        null);
            }
        } else {
            // error - glossary is mandatory
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.CREATE_WITHOUT_GLOSSARY.getMessageDefinition();
            throw new InvalidParameterException(
                    messageDefinition,
                    className,
                    methodName,
                    "glossary",
                    null);
        }
    }
}