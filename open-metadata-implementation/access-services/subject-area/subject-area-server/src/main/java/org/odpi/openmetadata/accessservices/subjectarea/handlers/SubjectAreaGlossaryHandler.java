/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.EntityNotDeletedException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.*;


/**
 * SubjectAreaGlossaryHandler manages Glossary objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class SubjectAreaGlossaryHandler extends SubjectAreaHandler {
    private static final String className = SubjectAreaGlossaryHandler.class.getName();

    /**
     * Construct the Subject Area Glossary Handler
     * needed to operate within a single server instance.
     *
     * @param oMRSAPIHelper omrs API helper
     * @param maxPageSize   maximum page size
     */
    public SubjectAreaGlossaryHandler(OMRSAPIHelper oMRSAPIHelper, int maxPageSize) {
        super(oMRSAPIHelper, maxPageSize);
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
                GlossaryMapper glossaryMapper = mappersFactory.get(GlossaryMapper.class);
                EntityDetail glossaryEntityDetail = glossaryMapper.map(suppliedGlossary);
                String entityDetailGuid = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, glossaryEntityDetail);
                response = getGlossaryByGuid(userId, entityDetailGuid);
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
            Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid, GLOSSARY_TYPE_NAME, methodName);
            entityDetail.ifPresent(entity -> {
                GlossaryMapper glossaryMapper = mappersFactory.get(GlossaryMapper.class);
                response.addResult(glossaryMapper.map(entity));
            });
        } catch (InvalidParameterException | SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Find Glossary
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param findRequest        {@link FindRequest}
     * @return A list of Glossaries meeting the search Criteria
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> findGlossary(String userId, FindRequest findRequest) {
        final String methodName = "findGlossary";
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();

        // If no search criteria is supplied then we return all glossaries, this should not be too many.
        try {
            List<Glossary> foundGlossaries = findEntities(userId, GLOSSARY_TYPE_NAME, findRequest, GlossaryMapper.class, methodName);
            if (foundGlossaries != null) {
                response.addAllResults(foundGlossaries);
            } else {
                return response;
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get Glossary relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested Glossary guid
     * <p>
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Line> getGlossaryRelationships(String userId, String guid, FindRequest findRequest) {
        String methodName = "getGlossaryRelationships";
        return getAllRelationshipsForEntity(methodName, userId, guid, findRequest);
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

            response = getGlossaryByGuid(userId, guid);
            if (response.head().isPresent()) {
                Glossary currentGlossary = response.head().get();
                if (isReplace)
                    replaceAttributes(currentGlossary, suppliedGlossary);
                else
                    updateAttributes(currentGlossary, suppliedGlossary);

                Date termFromTime = suppliedGlossary.getEffectiveFromTime();
                Date termToTime = suppliedGlossary.getEffectiveToTime();
                currentGlossary.setEffectiveFromTime(termFromTime);
                currentGlossary.setEffectiveToTime(termToTime);

                GlossaryMapper glossaryMapper = mappersFactory.get(GlossaryMapper.class);
                EntityDetail entityDetail = glossaryMapper.map(currentGlossary);
                final String glossaryGuid = entityDetail.getGUID();
                oMRSAPIHelper.callOMRSUpdateEntity(methodName, userId, entityDetail);
                response = getGlossaryByGuid(userId, glossaryGuid);
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    private void replaceAttributes(Glossary currentGlossary, Glossary newGlossary) {
        currentGlossary.setName(newGlossary.getName());
        currentGlossary.setQualifiedName(newGlossary.getQualifiedName());
        currentGlossary.setDescription(newGlossary.getDescription());
        currentGlossary.setUsage(newGlossary.getUsage());
        currentGlossary.setAdditionalProperties(newGlossary.getAdditionalProperties());
    }

    private void updateAttributes(Glossary oldGlossary, Glossary newGlossary) {
        if (newGlossary.getName() != null) {
            oldGlossary.setName(newGlossary.getName());
        }
        if (newGlossary.getQualifiedName() != null) {
            oldGlossary.setQualifiedName(newGlossary.getQualifiedName());
        }
        if (newGlossary.getDescription() != null) {
            oldGlossary.setDescription(newGlossary.getDescription());
        }
        if (newGlossary.getUsage() != null) {
            oldGlossary.setUsage(newGlossary.getUsage());
        }
        if (newGlossary.getAdditionalProperties() != null) {
            oldGlossary.setAdditionalProperties(newGlossary.getAdditionalProperties());
        }
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param userId  unique identifier for requesting user, under which the request is performed
     * @param guid    guid of the glossary to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * <li> EntityNotPurgedException             a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Glossary> deleteGlossary(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteGlossary";
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, GLOSSARY_TYPE_NAME, guid);
            } else {
                // if this is a not a purge then attempt to get terms and categories, as we should not delete if there are any
                List<String> relationshipTypeNames = Arrays.asList(TERM_ANCHOR_RELATIONSHIP_NAME, CATEGORY_ANCHOR_RELATIONSHIP_NAME);
                if (oMRSAPIHelper.isEmptyContent(relationshipTypeNames, userId, guid, GLOSSARY_TYPE_NAME, methodName)) {
                    oMRSAPIHelper.callOMRSDeleteEntity(methodName, userId, GLOSSARY_TYPE_NAME, guid);
                } else {
                    throw new EntityNotDeletedException(SubjectAreaErrorCode.GLOSSARY_CONTENT_PREVENTED_DELETE.getMessageDefinition(guid),
                            className,
                            methodName,
                            guid);
                }
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
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
            this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
            response = getGlossaryByGuid(userId, guid);
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get terms that are owned by this glossary.  The server has a maximum page size defined, the number of terms returned is limited by that maximum page size.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get terms
     * @param termHandler Term handler
     * @param startingFrom return results starting from element
     * @param maxPageSize maximum number of elements to be returned
     * @return A list of terms owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    public SubjectAreaOMASAPIResponse<Term> getTerms(String userId, String guid, SubjectAreaTermHandler termHandler, Integer startingFrom, int maxPageSize) {
        final String methodName = "getTerms";
        SubjectAreaOMASAPIResponse<Term>  response = getRelatedNodesForEnd1(methodName, userId, guid, TERM_ANCHOR_RELATIONSHIP_NAME, TermMapper.class, startingFrom, maxPageSize);
        List<Term> allTerms = new ArrayList<>();
        // the terms we get back from the mappers only map the parts from the entity. They do not set the glossary.
        if (response.getRelatedHTTPCode() == 200 && response.results() !=null && response.results().size() >0) {
            for (Term mappedCategory: response.results()) {
                SubjectAreaOMASAPIResponse<Term> termResponse = termHandler.getTermByGuid(userId, mappedCategory.getSystemAttributes().getGUID());
                if (termResponse.getRelatedHTTPCode() == 200) {
                    allTerms.add(termResponse.results().get(0));
                } else {
                    response = termResponse;
                    break;
                }
            }
        }
        if (response.getRelatedHTTPCode() == 200) {
            response = new SubjectAreaOMASAPIResponse<>();
            response.addAllResults(allTerms);
        }
        return response;
    }

    /**
     * Get the Categories owned by this glossary.  The server has a maximum page size defined, the number of categories returned is limited by that maximum page size.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get terms
     * @param onlyTop      when only the top categories (those categories without parents) are returned.
     * @param categoryHandler category handler
     * @param startingFrom  starting from
     * @param maxPageSize maximum number of elements to be returned
     * @return A list of categories owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    public SubjectAreaOMASAPIResponse<Category> getCategories(String userId, String guid, Boolean onlyTop, SubjectAreaCategoryHandler categoryHandler, Integer startingFrom, Integer maxPageSize) {
        final String methodName = "getCategories";

        SubjectAreaOMASAPIResponse<Category> response = getCategoriesWithPaging(userId, guid, categoryHandler, startingFrom, maxPageSize, methodName);
        if (onlyTop && response.getRelatedHTTPCode() == 200) {
            // if we got the categories successfully and we have some and only top categories then filter out the top categories from the bigger list of categories.
            // if we get a pages worth of results back and not all of them are top categories we need to get more categories to fill out the page of results.
            int from = 0;
            boolean keepGettingCategories = true;
            List<Category> topCategories = new ArrayList<>();
            while (keepGettingCategories) {
                List<Category> allCategories = response.results();
                if (allCategories != null && allCategories.size() > 0) {
                    response = new SubjectAreaOMASAPIResponse<>();
                    for (Category category : allCategories) {
                        if (category.getParentCategory() == null) {
                            topCategories.add(category);
                        }
                    }
                }
                if (response.results() == null || response.results().size() == 0) {
                    keepGettingCategories = false;
                } else if (allCategories.size() > 0 && response.results() != null && response.results().size() == maxPageSize) {
                    // in this case we have found top categories, but we got a page full of categories back to there may be more top categories we need to find to fill up the requested page, or there is no more data
                    from = from + maxPageSize;
                    response = getCategoriesWithPaging(userId, guid, categoryHandler, startingFrom, maxPageSize, methodName);
                    if (response.getRelatedHTTPCode() != 200) {
                        keepGettingCategories = false;
                        topCategories = new ArrayList<>();
                    }
                }
            }
            // clear the response
            response = new SubjectAreaOMASAPIResponse<>();
            // and add all the top categories
            response.addAllResults(topCategories);
        }
        return response;
    }

    /**
     * Get categories with paging.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get terms
     * @param categoryHandler category handler
     * @param startingFrom starting element
     * @param pageSize maximum elements returned on the request
     * @param methodName REst API
     * @return Categories  response
     */
    private SubjectAreaOMASAPIResponse<Category> getCategoriesWithPaging(String userId, String guid, SubjectAreaCategoryHandler categoryHandler, Integer startingFrom, Integer pageSize, String methodName) {
        SubjectAreaOMASAPIResponse<Category>  response = getRelatedNodesForEnd1(methodName, userId, guid, CATEGORY_ANCHOR_RELATIONSHIP_NAME, CategoryMapper.class, startingFrom, pageSize);
        List<Category> allCategories = new ArrayList<>();
        // the categories we get back from the mappers only map the parts from the entity. They do not set the parentCategory or the anchor.
        if (response.getRelatedHTTPCode() == 200 && response.results() !=null && response.results().size() >0) {
            for (Category mappedCategory: response.results()) {
                SubjectAreaOMASAPIResponse<Category> categoryResponse = categoryHandler.getCategoryByGuid(userId, mappedCategory.getSystemAttributes().getGUID());
                if (categoryResponse.getRelatedHTTPCode() == 200) {
                    allCategories.add(categoryResponse.results().get(0));
                } else {
                    response = categoryResponse;
                    break;
                }
            }
        }
        if (response.getRelatedHTTPCode() == 200) {
            response = new SubjectAreaOMASAPIResponse<>();
            response.addAllResults(allCategories);
        }
        return response;
    }
}