/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.OmasObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.IRelationshipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.INodeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.Mapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.MappersFactory;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;

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
    protected static final String TERM_CATEGORIZATION_RELATIONSHIP_NAME = "TermCategorization";
    protected static final String PROJECT_SCOPE_RELATIONSHIP_NAME = "ProjectScope";
    protected static final String CATEGORY_ANCHOR_RELATIONSHIP_NAME = "CategoryAnchor";
    protected static final String CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME ="CategoryHierarchyLink";

    protected final MappersFactory mappersFactory;
    protected final OMRSAPIHelper oMRSAPIHelper;
    protected final int maxPageSize;
    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Construct the Subject Area Project Handler
     * needed to operate within a single server instance.
     *
     * @param oMRSAPIHelper           omrs API helper
     * @param maxPageSize             maximum page size
     */
    public SubjectAreaHandler(OMRSAPIHelper oMRSAPIHelper, int maxPageSize) {
        this.oMRSAPIHelper = oMRSAPIHelper;
        this.mappersFactory = new MappersFactory(oMRSAPIHelper);
        this.maxPageSize = maxPageSize;
        invalidParameterHandler.setMaxPagingSize(this.maxPageSize);
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

    /**
     * Get glossary summary
     * @param restAPIName rest API Name
     * @param userId userid under which to issue to the get of the related media
     * @param relationship glossary relationship {@link TermAnchor} or {@link CategoryAnchor}
     * @return Glossary summary
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the Glossary.
     */
    GlossarySummary getGlossarySummary(String restAPIName,
                                       String userId,
                                       Relationship relationship) throws UserNotAuthorizedException,
                                                                 PropertyServerException,
                                                                 InvalidParameterException,
                                                                 SubjectAreaCheckedException
    {
        String guid = SubjectAreaUtils.getGlossaryGuidFromAnchor(relationship);
        Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid, GLOSSARY_TYPE_NAME, restAPIName);
        if (entityDetail.isPresent()) {
            GlossaryMapper glossaryMapper = mappersFactory.get(GlossaryMapper.class);
            Glossary glossary = glossaryMapper.map(entityDetail.get());
            // TODO sort out icons
            return SubjectAreaUtils.extractGlossarySummaryFromGlossary(glossary, relationship);
        }

        return null;
    }
    /**
     * Get category summary
     * @param restAPIName rest API Name
     * @param userId userid under which to issue to the get of the related media
     * @param relationship category relationship {@link TermAnchor} or {@link CategoryAnchor}
     * @return category summary
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the Category.
     */
    CategorySummary getCategorySummary(String restAPIName,
                                       String userId,
                                       Relationship relationship) throws UserNotAuthorizedException,
                                                                 PropertyServerException,
                                                                 InvalidParameterException,
                                                                 SubjectAreaCheckedException
    {
        String categoryGuid = relationship.getEnd1().getNodeGuid();
        Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, categoryGuid, CATEGORY_TYPE_NAME, restAPIName);
        if (entityDetail.isPresent()) {
            CategoryMapper CategoryMapper = mappersFactory.get(CategoryMapper.class);
            Category category = CategoryMapper.map(entityDetail.get());
            // TODO sort out icons
            return SubjectAreaUtils.extractCategorySummaryFromCategory(category, relationship);
        }

        return null;
    }

    protected <T extends Node>List<T> findEntities(String userId,
                                                   String typeEntityName,
                                                   FindRequest findRequest,
                                                   Class<? extends INodeMapper<T>> mapperClass,
                                                   String methodName) throws SubjectAreaCheckedException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        List<EntityDetail> entityDetails = null;
        List<T> foundEntities = null;

        if (findRequest.getPageSize() == null) {
            findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
        }
        invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
        if (findRequest.getSearchCriteria() == null) {
            entityDetails = oMRSAPIHelper.getEntitiesByType(methodName, userId, typeEntityName, findRequest);
        } else {
            entityDetails = oMRSAPIHelper.findEntitiesByPropertyValue(methodName, userId, typeEntityName, findRequest);
        }
        if (entityDetails != null) {
            foundEntities = convertOmrsToOmas(entityDetails, mapperClass);
        }
        return foundEntities;
    }

    /**
     * Get the related nodes from end 1 of a given type of relationship
     * @param methodName           name of the method being called.
     * @param userId               unique identifier for requesting user, under which the request is performed
     * @param guid                 guid
     * @param relationshipTypeName relationship type name
     * @param mapperClass          mapper class used to get the type name of the Node to return
     * @param startingFrom         retrieve items starting from this location
     * @param pageSize             maximum size of the returned items
     * @return response containing the the related Nodes if there are any
     */
    public <T extends Node> SubjectAreaOMASAPIResponse<T> getRelatedNodesForEnd1(String methodName,
                                                                                 String userId,
                                                                                 String guid,
                                                                                 String relationshipTypeName,
                                                                                 Class<? extends INodeMapper<T>> mapperClass,
                                                                                 Integer startingFrom,
                                                                                 Integer pageSize) {
        SubjectAreaOMASAPIResponse<T> response = new SubjectAreaOMASAPIResponse<>();

        try {
            if (pageSize == null) {
               pageSize = maxPageSize;
            }
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            final INodeMapper<T> mapper = mappersFactory.get(mapperClass);
            List<EntityDetail> entityDetails= oMRSAPIHelper.callGetEntitiesForRelationshipEnd1(
                        methodName, userId, guid, mapper.getTypeName(), relationshipTypeName, startingFrom, pageSize);
            if (entityDetails != null) {
                for (EntityDetail entityDetail : entityDetails) {
                    response.addResult(mapper.map(entityDetail));
                }
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException | org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }
    /**
     * Get the related nodes from end 2 of a given type of relationship
     * @param methodName           name of the method being called.
     * @param userId               unique identifier for requesting user, under which the request is performed
     * @param guid                 guid
     * @param relationshipTypeName relationship type name
     * @param mapperClass          mapper class used to get the type name of the Node to return
     * @param startingFrom         retrieve items starting from this location
     * @param pageSize             maximum size of the returned items
     * @return response containing the the related Nodes if there are any
     */
    public <T extends Node> SubjectAreaOMASAPIResponse<T> getRelatedNodesForEnd2(String methodName,
                                                                                 String userId,
                                                                                 String guid,
                                                                                 String relationshipTypeName,
                                                                                 Class<? extends INodeMapper<T>> mapperClass,
                                                                                 Integer startingFrom,
                                                                                 Integer pageSize) {
        SubjectAreaOMASAPIResponse<T> response = new SubjectAreaOMASAPIResponse<>();

        try {
            if (pageSize == null) {
                pageSize = maxPageSize;
            }
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            final INodeMapper<T> mapper = mappersFactory.get(mapperClass);
            List<EntityDetail> entityDetails = oMRSAPIHelper.callGetEntitiesForRelationshipEnd2(
                   methodName, userId, guid, mapper.getTypeName(), relationshipTypeName, startingFrom, pageSize);
            if (entityDetails != null) {
                for (EntityDetail entityDetail : entityDetails) {
                    response.addResult(mapper.map(entityDetail));
                }
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException | org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get All relationships for Node
     *
     * @param methodName         name of the method being called.
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested guid
     * */
    public SubjectAreaOMASAPIResponse<Relationship> getAllRelationshipsForEntity(String methodName,
                                                                                 String userId,
                                                                                 String guid,
                                                                                 FindRequest findRequest)
    {
        SubjectAreaOMASAPIResponse<Relationship> response = new SubjectAreaOMASAPIResponse<>();
        try {
            if (findRequest.getPageSize() == null) {
                findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
            }
            invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
            response.addAllResults(getAllRelationshipForEntity(methodName, userId, guid, findRequest));
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException | org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException  e) {
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
     * @return {@code List<Relationship>}
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws SubjectAreaCheckedException standard exception Subject Area OMAS services
     */
    public List<Relationship> getAllRelationshipForEntity(String restAPIName,
                                                  String userId,
                                                  String entityGuid,
                                                  FindRequest findRequest) throws SubjectAreaCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        if (findRequest.getPageSize() == null) {
            findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
        }
        invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), restAPIName);
        List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationships = oMRSAPIHelper.getAllRelationshipsForEntity(restAPIName, userId, entityGuid, findRequest);
        return getRelationshipsFromRelationships(relationships);
    }

    public List<Relationship> getRelationshipsFromRelationships(Collection<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationships) {
        return convertOmrsToOmas(relationships, IRelationshipMapper.class);
    }

    public List<Node> getNodesFromEntityDetails(Collection<EntityDetail> entityDetails){
        return convertOmrsToOmas(entityDetails, INodeMapper.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <R extends InstanceHeader, T extends OmasObject>List<T>
    convertOmrsToOmas(Collection<R> list, Class<? extends Mapper> mapperInterface)
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
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws SubjectAreaCheckedException standard exception Subject Area OMAS services
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
    /**
     * Set unique qualifiedName into the supplied Node. The qualified name needs to be unique and is supplied on an addnEtity omrs call.
     * Prior to the add, we do not know the guid of the entity. We do not want to add an entity, then immediately update it; as these changes to
     * entity identity would be propagated across the cohort. So we set the qualified name by concatinating the supplied seed, an @ symbol and a newly generated UUID.
     *
     * The assumption is that this method is supplied a node that contains a name.
     *
     * @param node Node to set the unique qualified name into
     */
    protected void setUniqueQualifiedNameIfBlank(Node node) {
        String qualifiedName = node.getQualifiedName();
        if (qualifiedName == null || qualifiedName.trim().equals("")) {
            node.setQualifiedName(node.getName() + "@" + UUID.randomUUID().toString());
        }
    }
    /**
     * return whether the Category matches the search criteria
     *
     * @param category       category to use for match
     * @param searchCriteria criteria to use for match
     * @return boolean indicating whether the category matches the search criteria
     */
    protected boolean categoryMatchSearchCriteria(Category category, String searchCriteria) {
        boolean isMatch = false;
        if (searchCriteria == null) return true;
        final String name = category.getName();
        final String description = category.getDescription();
        final String qualifiedName = category.getQualifiedName();

        if (name != null && name.matches(searchCriteria)) {
            isMatch = true;
        }
        if (description != null && description.matches(searchCriteria)) {
            isMatch = true;
        }
        if (qualifiedName != null && qualifiedName.matches(searchCriteria)) {
            isMatch = true;
        }
        return isMatch;
    }
    /**
     * return whether the Term matches the search criteria
     *
     * @param term           term to use for match
     * @param searchCriteria criteria to use for match
     * @return boolean indicating whether the term matches the search criteria
     */
    protected boolean termMatchSearchCriteria(Term term, String searchCriteria) {
        if (searchCriteria == null) return true;
        boolean isMatch = false;
        final String name = term.getName();
        final String description = term.getDescription();
        final String qualifiedName = term.getQualifiedName();
        final String abbreviation = term.getAbbreviation();
        final String examples = term.getExamples();
        final String usage = term.getUsage();

        if (name != null && name.matches(searchCriteria)) {
            isMatch = true;
        }
        if (description != null && description.matches(searchCriteria)) {
            isMatch = true;
        }
        if (qualifiedName != null && qualifiedName.matches(searchCriteria)) {
            isMatch = true;
        }
        if (abbreviation != null && abbreviation.matches(searchCriteria)) {
            isMatch = true;
        }
        if (examples != null && examples.matches(searchCriteria)) {
            isMatch = true;
        }
        if (usage != null && usage.matches(searchCriteria)) {
            isMatch = true;
        }
        return isMatch;
    }

    /**
     * Check whether the node is readonly and throw and exception if it is
     * @param methodName calling methodName
     * @param node node to check
     * @param operation operation being attempted
     * @throws PropertyServerException exception thrown when the node is readonly
     */
    protected void checkReadOnly(String methodName, Node node, String operation ) throws PropertyServerException {
        if (node.isReadOnly()) {
            // reject
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.MODIFICATION_OPERATION_ATTEMPTED_ON_READ_ONLY_NODE.getMessageDefinition(operation, node.getNodeType().toString());
            throw new PropertyServerException(messageDefinition, className , methodName);
        }
    }
    /**
     * Check whether the relationship is readonly and throw and exception if it is
     * @param methodName calling methodName
     * @param relationship relationship to check
     * @param operation operation being attempted
     * @throws PropertyServerException exception thrown when the relationship is readonly
     */
    protected void checkRelationshipReadOnly(String methodName, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship, String operation ) throws PropertyServerException {
        if (relationship.getInstanceProvenanceType() != InstanceProvenanceType.LOCAL_COHORT) {
            // reject
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.MODIFICATION_OPERATION_ATTEMPTED_ON_READ_ONLY_RELATIONSHIP.getMessageDefinition(operation, relationship.getType().getTypeDefName());
            throw new PropertyServerException(messageDefinition, className , methodName);
        }
    }
}