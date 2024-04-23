/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.EntityNotDeletedException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * SubjectAreaGlossaryHandler manages Glossary objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class SubjectAreaGlossaryHandler extends SubjectAreaHandler {
    private static Class<SubjectAreaGlossaryHandler> clazz = SubjectAreaGlossaryHandler.class;
    String className = clazz.getName();
    private static final Logger log = LoggerFactory.getLogger(clazz);


    /**
     * Construct the Subject Area Glossary Handler
     * needed to operate within a single server instance.
     *
     * @param genericHandler    generic handler
     * @param maxPageSize       maximum page size
     */
    public SubjectAreaGlossaryHandler(OpenMetadataAPIGenericHandler genericHandler, int maxPageSize) {
        super(genericHandler, maxPageSize);
    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Glossaries with the same name can be confusing. Best practise is to createGlossaries that have unique names.
     * This Create call does not police that glossary names are unique. So it is possible to create Glossaries with the same name as each other.
     *
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalGlossary to create a canonical glossary </li>
     * <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glossary </li>
     * <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> createGlossary(String userId, Glossary suppliedGlossary) {
        final String methodName = "createGlossary";
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            InputValidator.validateNodeType(className, methodName, suppliedGlossary.getNodeType(), NodeType.Glossary, NodeType.Taxonomy, NodeType.TaxonomyAndCanonicalGlossary, NodeType.CanonicalGlossary);
            final String suppliedGlossaryName = suppliedGlossary.getName();

            // need to check we have a name
            if (suppliedGlossaryName == null || suppliedGlossaryName.equals("")) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_CREATE_WITHOUT_NAME.getMessageDefinition();
                throw new InvalidParameterException(messageDefinition, className, methodName, "name");
            } else {
                setUniqueQualifiedNameIfBlank(suppliedGlossary);

                Date effectiveFrom = null;
                Date effectiveTo = null;

                if (suppliedGlossary.getEffectiveFromTime() != null) {
                    effectiveFrom = new Date(suppliedGlossary.getEffectiveFromTime());
                }
                if (suppliedGlossary.getEffectiveToTime() != null) {
                    effectiveTo = new Date(suppliedGlossary.getEffectiveToTime());
                }

                GlossaryBuilder builder = new GlossaryBuilder(suppliedGlossary.getQualifiedName(),
                                                              suppliedGlossary.getName(),
                                                              suppliedGlossary.getDescription(),
                                                              suppliedGlossary.getLanguage(),
                                                              suppliedGlossary.getUsage(),
                                                              genericHandler.getRepositoryHelper(),
                                                              genericHandler.getServiceName(),
                                                              genericHandler.getServerName());

                builder.setEffectivityDates(effectiveFrom, effectiveTo);

                String guid = genericHandler.createBeanInRepository(userId,
                                                                    null,
                                                                    null,
                                                                    OpenMetadataType.GLOSSARY_TYPE_GUID,
                                                                    OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                    builder,
                                                                    null,
                                                                    methodName);

                // set classifications if required
                if (suppliedGlossary.getNodeType() == NodeType.Taxonomy || suppliedGlossary.getNodeType() == NodeType.TaxonomyAndCanonicalGlossary) {

                    genericHandler.setClassificationInRepository(userId,
                                                                 null,
                                                                 null,
                                                                 guid,
                                                                 "guid",
                                                                 OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                 OpenMetadataType.TAXONOMY_CLASSIFICATION_TYPE_GUID,
                                                                 OpenMetadataType.TAXONOMY_CLASSIFICATION_TYPE_NAME,
                                                                 null,  // TODO properties
                                                                 false,
                                                                 false,
                                                                 false,
                                                                 null,
                                                                 methodName);
                }
                if (suppliedGlossary.getNodeType() == NodeType.CanonicalGlossary || suppliedGlossary.getNodeType() == NodeType.TaxonomyAndCanonicalGlossary) {
                    genericHandler.setClassificationInRepository(userId,
                                                                 null,
                                                                 null,
                                                                 guid,
                                                                 "guid",
                                                                 OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                 OpenMetadataType.CANONICAL_VOCAB_CLASSIFICATION_TYPE_GUID,
                                                                 OpenMetadataType.CANONICAL_VOCAB_CLASSIFICATION_TYPE_NAME,
                                                                 null,  // TODO properties
                                                                 false,
                                                                 false,
                                                                 false,
                                                                 null,
                                                                 methodName);
                }




                response = getGlossaryByGuid(userId, guid);
            }
        } catch (PropertyServerException | UserNotAuthorizedException | SubjectAreaCheckedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }



    /**
     * Get a glossary by guid.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> getGlossaryByGuid(String userId, String guid) {
        final String methodName = "getGlossaryByGuid";
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
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
            GlossaryMapper glossaryMapper = mappersFactory.get(GlossaryMapper.class);
            response.addResult(glossaryMapper.map(entityDetail));
        } catch (InvalidParameterException | SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Find Glossary
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param findRequest {@link FindRequest}
     * @param exactValue  a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase  a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @return A list of Glossaries meeting the search Criteria
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> findGlossary(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) {
        final String methodName = "findGlossary";
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();

        // If no search criteria is supplied then we return all glossaries, this should not be too many.
        try {
            List<Glossary> foundGlossaries = findNodes(userId, OpenMetadataType.GLOSSARY_TYPE_NAME, OpenMetadataType.GLOSSARY_TYPE_GUID, findRequest, exactValue, ignoreCase, GlossaryMapper.class, methodName);
            if (foundGlossaries != null) {
                response.addAllResults(foundGlossaries);
            } else {
                return response;
            }
        } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get Glossary relationships
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        guid
     * @param findRequest {@link FindRequest}
     * @return the relationships associated with the requested Glossary guid
     * <p>
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Relationship> getGlossaryRelationships(String userId, String guid, FindRequest findRequest) {
        String methodName = "getGlossaryRelationships";
        return getAllRelationshipsForEntity(methodName, userId, guid, findRequest, OpenMetadataType.GLOSSARY_TYPE_NAME );
    }

    /**
     * Update a Glossary
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> updateGlossary(String userId, String guid, Glossary suppliedGlossary, boolean isReplace) {
        final String methodName = "updateGlossary";
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            InputValidator.validateNodeType(className, methodName, suppliedGlossary.getNodeType(), NodeType.Glossary, NodeType.Taxonomy, NodeType.TaxonomyAndCanonicalGlossary, NodeType.CanonicalGlossary);

            Date effectiveFrom = null;
            Date effectiveTo = null;

            if (suppliedGlossary.getEffectiveFromTime() != null) {
                effectiveFrom = new Date(suppliedGlossary.getEffectiveFromTime());
            }
            if (suppliedGlossary.getEffectiveToTime() != null) {
                effectiveTo = new Date(suppliedGlossary.getEffectiveToTime());
            }

            GlossaryBuilder builder = new GlossaryBuilder(suppliedGlossary.getQualifiedName(),
                                                          suppliedGlossary.getName(),
                                                          suppliedGlossary.getDescription(),
                                                          suppliedGlossary.getLanguage(),
                                                          suppliedGlossary.getUsage(),
                                                          genericHandler.getRepositoryHelper(),
                                                          genericHandler.getServiceName(),
                                                          genericHandler.getServerName());

            builder.setEffectivityDates(effectiveFrom, effectiveTo);

            genericHandler.updateBeanInRepository(userId,
                                                  null,
                                                  null,
                                                  guid,
                                                  "guid",
                                                  OpenMetadataType.GLOSSARY_TYPE_GUID,
                                                  OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                  false,
                                                  false,
                                                  builder.getInstanceProperties(methodName),
                                                  !isReplace,
                                                  null,
                                                  methodName);
            response = getGlossaryByGuid(userId, guid);

        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     *
     * @param userId  unique identifier for requesting user, under which the request is performed
     * @param guid    guid of the glossary to be deleted.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> deleteGlossary(String userId, String guid) {
        final String methodName = "deleteGlossary";
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        boolean issueDelete = false;
        try {
            // if this is a not a purge then check there are no relationships before deleting,
            // otherwise the deletion could remove all anchored entities.
            if (genericHandler.isBeanIsolated(userId,
                                              guid,
                                              OpenMetadataType.GLOSSARY_TYPE_NAME,
                                              false,
                                              false,
                                              null,
                                              methodName)) {

                issueDelete = true;
            } else {
                throw new EntityNotDeletedException(SubjectAreaErrorCode.GLOSSARY_CONTENT_PREVENTED_DELETE.getMessageDefinition(guid),
                                                    className,
                                                    methodName,
                                                    guid);
            }

            if (issueDelete) {
                genericHandler.deleteBeanInRepository(userId,
                                                      null,
                                                      null,
                                                      guid,
                                                      "guid",
                                                      OpenMetadataType.GLOSSARY_TYPE_GUID,    // true for sub types
                                                      OpenMetadataType.GLOSSARY_TYPE_NAME,    // true for sub types
                                                      null,
                                                      null,
                                                      false,
                                                      false,
                                                      null,
                                                      methodName);
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the glossary to restore
     * @return response which when successful contains the restored glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> restoreGlossary(String userId, String guid) {
        final String methodName = "restoreGlossary";
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            genericHandler.getRepositoryHandler().restoreEntity(userId,
                                                                null,
                                                                null,
                                                                guid,
                                                                methodName);
            response = getGlossaryByGuid(userId, guid);
        } catch (UserNotAuthorizedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get terms that are owned by this glossary.  The server has a maximum page size defined, the number of terms returned is limited by that maximum page size.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        guid of the category to get terms
     * @param findRequest {@link FindRequest}
     * @param exactValue  a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase  a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @return A list of terms owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> getTerms(String userId, String guid, SubjectAreaTermHandler termHandler, FindRequest findRequest, boolean exactValue, boolean ignoreCase) {
        final String methodName = "getTerms";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        Integer pageSize = findRequest.getPageSize();
        Integer requestedStartingFrom = findRequest.getStartingFrom();
        String searchCriteria = findRequest.getSearchCriteria();
        if (pageSize == null) {
            pageSize = maxPageSize;
        }
        if (requestedStartingFrom == null) {
            requestedStartingFrom = 0;
        }
        SubjectAreaOMASAPIResponse<Glossary> thisGlossaryResponse = getGlossaryByGuid(userId, guid);
        if (thisGlossaryResponse.getRelatedHTTPCode() == 200) {
            try {
                Set<String>   specificMatchPropertyNames = new HashSet();

                // specify the names of string attributes for this type that we want to match against
                specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.DESCRIPTION.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.SUMMARY.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.EXAMPLES.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.ABBREVIATION.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.USAGE.name);

                List<EntityDetail> entities = genericHandler.getAttachedFilteredEntities(userId,
                                                                                         guid,
                                                                                         "guid",
                                                                                         OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                                         OpenMetadataType.TERM_ANCHOR.typeName,
                                                                                         OpenMetadataType.TERM_ANCHOR.typeGUID,
                                                                                         2,      // get only the children
                                                                                         specificMatchPropertyNames,
                                                                                         searchCriteria,
                                                                                         requestedStartingFrom,
                                                                                         !exactValue,
                                                                                         ignoreCase,
                                                                                         pageSize,
                                                                                         methodName);

                Set<Term> terms = new HashSet<>();
                if (entities != null) {
                    for (EntityDetail entity : entities) {
                        SubjectAreaOMASAPIResponse<Term> termResponse = termHandler.getTermByGuid(userId, entity.getGUID());
                        if (termResponse.getRelatedHTTPCode() == 200) {
                            terms.add(termResponse.results().get(0));
                        } else {
                            response = termResponse;
                            break;
                        }
                    }
                    if (response.getRelatedHTTPCode() == 200) {
                        response.addAllResults(terms);
                    }
                }

            } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
                response.setExceptionInfo(e, className);
            }
        }
        return response;
    }

    /**
     * Get the Categories owned by this glossary.  The server has a maximum page size defined, the number of categories returned is limited by that maximum page size.
     *
     * @param userId          unique identifier for requesting user, under which the request is performed
     * @param guid            guid of the category to get terms
     * @param findRequest     {@link FindRequest}
     * @param exactValue      a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase      a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param onlyTop         when only the top categories (those categories without parents) are returned.
     * @param categoryHandler category handler is supplied, so that we can obtain  categories containing the parentCategory field,
     *                        which we need to test as part of the onlyTop processing
     * @return A list of categories owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> getCategories(String userId, String guid, FindRequest findRequest, boolean exactValue, boolean ignoreCase, Boolean onlyTop, SubjectAreaCategoryHandler categoryHandler) {
        final String methodName = "getCategories";
        if (log.isDebugEnabled()) {
            String searchCriteria = "not set";
            int startingFrom = 0;
            if (findRequest != null) {
                searchCriteria = findRequest.getSearchCriteria();
                startingFrom = findRequest.getStartingFrom();
            }

            log.debug("==> " + methodName + ",userId=" + userId + ",guid=" + guid +",searchCriteria="+searchCriteria+",startingFrom="+startingFrom);
        }
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        Integer pageSize = findRequest.getPageSize();
        Integer requestedStartingFrom = findRequest.getStartingFrom();
        String searchCriteria = findRequest.getSearchCriteria();
        if (pageSize == null) {
            pageSize = maxPageSize;
        }
        if (requestedStartingFrom == null) {
            requestedStartingFrom = 0;
        }
        SubjectAreaOMASAPIResponse<Glossary> thisGlossaryResponse = getGlossaryByGuid(userId, guid);
        if (thisGlossaryResponse.getRelatedHTTPCode() == 200) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug(methodName + ": got glossary guid " + guid );
                }

                Set<String>   specificMatchPropertyNames = new HashSet();

                specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.DESCRIPTION.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);

                String parentToCheckTypeGUID = null;
                String parentToCheckTypeName = null;
                if (onlyTop) {
                    parentToCheckTypeGUID = OpenMetadataType.CATEGORY_HIERARCHY_TYPE_GUID;
                    parentToCheckTypeName = OpenMetadataType.CATEGORY_HIERARCHY_TYPE_NAME;
                    if (log.isDebugEnabled()) {
                        log.debug("parentToCheckTypeGUID="+parentToCheckTypeGUID+",parentToCheckTypeName=" + parentToCheckTypeName);
                    }
                }


                List<EntityDetail> entities = genericHandler.getAttachedFilteredEntities(userId,
                                                                                         guid,
                                                                                         "guid",
                                                                                         OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                                         OpenMetadataType.CATEGORY_ANCHOR_TYPE_NAME,
                                                                                         OpenMetadataType.CATEGORY_ANCHOR_TYPE_GUID,
                                                                                         2,      // get only the category end
                                                                                         parentToCheckTypeName,  //set for onlyTop
                                                                                         parentToCheckTypeGUID,  //set for onlyTop
                                                                                         true, // CategoryHierarchyLink end1 is the parent.
                                                                                         specificMatchPropertyNames,
                                                                                         searchCriteria,
                                                                                         requestedStartingFrom,
                                                                                         !exactValue,
                                                                                         ignoreCase,
                                                                                         pageSize,
                                                                                         false,
                                                                                         false,
                                                                                         null,
                                                                                         methodName);
                Set<Category> categories = new HashSet<>();
                if(entities != null)
                {
                    for (EntityDetail entity : entities)
                    {
                        SubjectAreaOMASAPIResponse<Category> categoryResponse = categoryHandler.getCategoryByGuid(userId, entity.getGUID());
                        if (categoryResponse.getRelatedHTTPCode() == 200)
                        {
                            categories.add(categoryResponse.results().get(0));
                        } else
                        {
                            response = categoryResponse;
                            break;
                        }
                    }
                    if (response.getRelatedHTTPCode() == 200)
                    {
                        response.addAllResults(categories);
                    }
                }

            } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
                response.setExceptionInfo(e, className);
            }
        }
        return response;
    }

}