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
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.IRelationshipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.INodeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.Mapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.MappersFactory;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.commonservices.generichandlers.*;

import java.util.*;


/**
 * SubjectAreaProjectHandler manages Project objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public abstract class SubjectAreaHandler {
    private static final String className = SubjectAreaHandler.class.getName();

    protected final MappersFactory mappersFactory;
    protected final OpenMetadataAPIGenericHandler genericHandler;
    protected final int maxPageSize;
    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Construct the Subject Area Handler
     * needed to operate within a single server instance.
     *
     * @param genericHandler    generic handler
     * @param maxPageSize       maximum page size
     */
    public SubjectAreaHandler(OpenMetadataAPIGenericHandler genericHandler, int maxPageSize) {
        this.genericHandler =genericHandler;
        this.mappersFactory = new MappersFactory(genericHandler);
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
                                                                 SubjectAreaCheckedException {
        String guid = SubjectAreaUtils.getGlossaryGuidFromAnchor(relationship);

        EntityDetail entityDetail = genericHandler.getEntityFromRepository(userId,
                                                                           guid,
                                                                           "guid",
                                                                           OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                           null,
                                                                           null,
                                                                           false,
                                                                           false,
                                                                           null,
                                                                           restAPIName);
        GlossaryMapper glossaryMapper = mappersFactory.get(GlossaryMapper.class);
        Glossary glossary = glossaryMapper.map(entityDetail);
        // TODO sort out icons
        return SubjectAreaUtils.extractGlossarySummaryFromGlossary(glossary, relationship);
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
        EntityDetail entityDetail = genericHandler.getEntityFromRepository(userId,
                                                                           categoryGuid,
                                                                           "guid",
                                                                           OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                           null,
                                                                           null,
                                                                           false,
                                                                           false,
                                                                           null,
                                                                           restAPIName);
            CategoryMapper CategoryMapper = mappersFactory.get(CategoryMapper.class);
            Category category = CategoryMapper.map(entityDetail);
            // TODO sort out icons
            return SubjectAreaUtils.extractCategorySummaryFromCategory(category, relationship);
    }
    protected String sanitiseFindRequest(String searchCriteria, boolean exactValue, boolean ignoreCase) {
        OMRSRepositoryHelper omrsRepositoryHelper =genericHandler.getRepositoryHelper();

        if (searchCriteria != null && "".equals(searchCriteria.trim())) {
            // ignore the flags for an empty search criteria string - assume we want everything
            searchCriteria = ".*";
        } else {
            // lose any leading and trailing blanks
            searchCriteria = searchCriteria.trim();
            if (exactValue) {
                searchCriteria = omrsRepositoryHelper.getExactMatchRegex(searchCriteria, ignoreCase);
            } else {
                searchCriteria = omrsRepositoryHelper.getStartsWithRegex(searchCriteria, ignoreCase);
            }
        }

        return searchCriteria;
    }
    /**
     * Take a FindRequest and sanitise it.
     *
     * The FindRequest from the user could contain a regex expression which would cause the regex engine to loop.
     * to avoid this, we turn what the user has given us into a literal and then use the exactValue and ignoreCase flags
     * to add to the regular expression in a controlled way.
     *
     * @param findRequest supplied find request - that contains the search criteria
     * @param exactValue flag indicating that exact value mathcing should be done
     * @param ignoreCase flag indicating that case should be ignored
     * @return sanitised find request
     */
    protected FindRequest sanitiseFindRequest(FindRequest findRequest, boolean exactValue, boolean ignoreCase) {
        FindRequest sanitisedFindRequest = findRequest;
        String searchCriteria = sanitiseFindRequest(findRequest.getSearchCriteria(), exactValue, ignoreCase);
        sanitisedFindRequest.setSearchCriteria(searchCriteria);
        return sanitisedFindRequest;
    }

    protected <T extends Node>List<T> findNodes(String userId,
                                                String typeEntityName,
                                                String typeEntityGuid,
                                                FindRequest findRequest,
                                                boolean exactValue,
                                                boolean ignoreCase,
                                                Class<? extends INodeMapper<T>> mapperClass,
                                                String methodName) throws PropertyServerException,
                                                                             UserNotAuthorizedException,
                                                                             InvalidParameterException {
        List<EntityDetail> entityDetails = null;
        List<T> foundEntities = null;

        if (findRequest.getPageSize() == null) {
            findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
        }
        invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
        if (findRequest.getSearchCriteria() == null) {
              entityDetails = genericHandler.getEntitiesByType(userId,
                                                              typeEntityGuid,
                                                              typeEntityName,
                                                              findRequest.getSequencingProperty(),
                                                              false,
                                                              false,
                                                              findRequest.getStartingFrom(),
                                                              findRequest.getPageSize(),
                                                              null, // any effective date
                                                              methodName);
        } else {
            FindRequest sanitisedFindRequest = sanitiseFindRequest(findRequest, exactValue, ignoreCase);
            entityDetails = genericHandler.findEntities(userId,
                                                        sanitisedFindRequest.getSearchCriteria(),
                                                        "searchCriteria",
                                                        typeEntityGuid,
                                                        typeEntityName,
                                                        null,
                                                        null,
                                                        findRequest.getSequencingProperty(),
                                                        findRequest.getStartingFrom(),
                                                        findRequest.getPageSize(),
                                                        false,
                                                        false,
                                                        null, // any effective date
                                                        methodName);
        }
        if (entityDetails != null) {
            foundEntities = convertOmrsToOmas(entityDetails, mapperClass);
        }
        return foundEntities;
    }

    /**
     * Get All relationships for Node
     *
     * @param methodName         name of the method being called.
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid
     * @param findRequest        {@link FindRequest}
     * @param entityTypeName     typpe name of the starting entity
     * @return the relationships associated with the requested guid
     * */
    public SubjectAreaOMASAPIResponse<Relationship> getAllRelationshipsForEntity(String methodName,
                                                                                 String userId,
                                                                                 String guid,
                                                                                 FindRequest findRequest,
                                                                                 String entityTypeName)
    {
        SubjectAreaOMASAPIResponse<Relationship> response = new SubjectAreaOMASAPIResponse<>();
        try {
            if (findRequest.getPageSize() == null) {
                findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
            }
            invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
            response.addAllResults(getAllRelationshipForEntity(methodName, userId, guid, findRequest, entityTypeName));
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException | InvalidParameterException  e) {
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
     * @param entityTypeName          type name of the starting entity
     * @return {@code List<Relationship>}
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws SubjectAreaCheckedException standard exception Subject Area OMAS services
     */
    public List<Relationship> getAllRelationshipForEntity(String restAPIName,
                                                          String userId,
                                                          String entityGuid,
                                                          FindRequest findRequest,
                                                          String entityTypeName) throws SubjectAreaCheckedException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException,
                                                                                          InvalidParameterException {
        if (findRequest.getPageSize() == null) {
            findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
        }
        invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), restAPIName);
        List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationships =
                genericHandler.getAttachmentLinks(userId,
                                                  entityGuid,
                                                  "guid",
                                                  entityTypeName,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  0,
                                                  false,
                                                  false,
                                                  findRequest.getStartingFrom(),
                                                  findRequest.getPageSize(),
                                                  null, // any effective time
                                                  restAPIName);


        return getRelationshipsFromRelationships(relationships);
    }
    /**
     * Get the relationships keyed off an entity guid.
     *
     * @param restAPIName             rest API name
     * @param userId                  user identity
     * @param entityGuid              globally unique identifier
     * @param findRequest             {@link FindRequest}
     * @param entityTypeName          type name of the starting entity
     * @param attachmentRelationshipTypeGUID attachment relationship type guid
     * @param attachmentRelationshipTypeName attachment relationship type name
     * @param attachmentEntityTypeName  attached entity type name
     * @return {@code List<Relationship>}
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws SubjectAreaCheckedException standard exception Subject Area OMAS services
     */
    public List<Relationship> getRelationshipsForEntityByType(String restAPIName,
                                                              String userId,
                                                              String entityGuid,
                                                              FindRequest findRequest,
                                                              String entityTypeName,
                                                              String attachmentRelationshipTypeGUID,
                                                              String attachmentRelationshipTypeName,
                                                              String attachmentEntityTypeName) throws SubjectAreaCheckedException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      InvalidParameterException {
        if (findRequest.getPageSize() == null) {
            findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
        }
        invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), restAPIName);
        List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationships =
                genericHandler.getAttachmentLinks(userId,
                                                  entityGuid,
                                                  "guid",
                                                  entityTypeName,
                                                  attachmentRelationshipTypeGUID,
                                                  attachmentRelationshipTypeName,
                                                  null,
                                                  attachmentEntityTypeName,
                                                  0,
                                                  false,
                                                  false,
                                                  findRequest.getStartingFrom(),
                                                  findRequest.getPageSize(),
                                                  null, // any effective time
                                                  restAPIName);

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
                                                                                                    SubjectAreaCheckedException {
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
            // find by glossary by guid
            EntityDetail entityDetail = genericHandler.getEntityFromRepository(userId,
                                                                               guid,
                                                                               "guid",
                                                                               OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               false,
                                                                               null,
                                                                               methodName);

            return entityDetail.getGUID();

        } else {
            // error - glossary is mandatory
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.CREATE_WITHOUT_GLOSSARY.getMessageDefinition(methodName);
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
     * Set the effectivity to and from dates date for a Node
     * @param userId calling user
     * @param node node to update with effectivity dates if required
     * @param methodName name of the calling method
     * @param guid guid of the Bean to update
     * @param typeGUID the guid of the associated type
     * @param typeName the name of the associated type
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to update the security tags
     */
    protected void setNodeEffectivity(String userId, Node node, String methodName, String guid, String typeGUID, String typeName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Long effectiveFromLong = node.getEffectiveFromTime();
        Long effectiveToLong = node.getEffectiveToTime();
        if (effectiveFromLong != null || effectiveToLong != null) {
            Date effectiveFrom = null;
            Date effectiveTo = null;
            if (effectiveToLong != null) {
                effectiveTo = new Date(effectiveToLong);
            }
            if (effectiveFromLong != null) {
                effectiveFrom = new Date(effectiveFromLong);
            }
            genericHandler.updateBeanEffectivityDates(userId,
                                       null,
                                       null,
                                       guid,
                                       "guid",
                                       typeGUID,
                                       typeName,
                                       false,
                                       false,
                                       effectiveFrom,
                                       effectiveTo,
                                       null,
                                       methodName);
        }
    }
    /**
     * Set the effectivity to and from dates date for a Relationship
     * @param userId calling user
     * @param node node to take the effectivity dates from
     * @param methodName name of the calling method
     * @param relationshipGUID relationshipGUID of the Relationship to update
     * @param typeGUID the relationshipGUID of the associated type
     * @param typeName the name of the associated type
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to update the security tags
     */
    protected void setRelationshipEffectivity(String userId, Node node, String methodName, String relationshipGUID, String typeGUID, String typeName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Long effectiveFromLong = node.getEffectiveFromTime();
        Long effectiveToLong = node.getEffectiveToTime();
        if (effectiveFromLong != null || effectiveToLong != null) {
            Date effectiveFrom = null;
            Date effectiveTo = null;
            if (effectiveToLong != null) {
                effectiveTo = new Date(effectiveToLong);
            }
            if (effectiveFromLong != null) {
                effectiveFrom = new Date(effectiveFromLong);
            }
            genericHandler.updateRelationshipEffectivityDates(userId,
                                                      null,
                                                      null,
                                                      relationshipGUID,
                                                      "relationshipGUID",
                                                      typeName,
                                                      effectiveFrom,
                                                      effectiveTo,
                                                      false,
                                                      false,
                                                      null,
                                                      methodName);

        }
    }
}