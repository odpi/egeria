/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.RestCaller;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides glossary category  authoring interfaces for subject area experts.
 */
public class SubjectAreaCategoryImpl extends SubjectAreaBaseImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaCategoryImpl.class);

    private static final String className = SubjectAreaCategoryImpl.class.getName();

    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "categories";

    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL unique id for the connector instance
     */
    public SubjectAreaCategoryImpl(String omasServerURL, String serverName) {
        super(omasServerURL, serverName);
    }

    /**
     * Create a Category
     *
     * @param userId           userId under which the request is performed
     * @param suppliedCategory Category
     * @return the created category.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws ClassificationException              Error processing a classification
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Category createCategory(String userId, Category suppliedCategory) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "createCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String url = this.omasServerURL + String.format(BASE_URL, serverName, userId);
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedCategory);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);

        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowClassificationException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Category category = DetectUtils.detectAndReturnCategory(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return category;
    }

    /**
     * Get a category by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the category to get
     * @return the requested category.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Category getCategoryByGuid(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "getCategoryByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s";
        String url = String.format(urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Category category = DetectUtils.detectAndReturnCategory(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return category;
    }

    /**
     * Get Category relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the category to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Category userId
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public List<Line> getCategoryRelationships(String userId, String guid,
                                               Date asOfTime,
                                               int offset,
                                               int pageSize,
                                               org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                               String sequencingProperty) throws
                                                                          UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          FunctionNotSupportedException,
                                                                          UnexpectedResponseException,
                                                                          MetadataServerUncontactableException {
        final String methodName = "getCategoryRelationships";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        List<Line> relationships = getRelationships(BASE_URL, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relationships;
    }

    /**
     * Find Category
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Category property values (this does not include the GlossarySummary content).
     * @param asOfTime           the categories returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Categories meeting the search Criteria
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public List<Category> findCategory(String userId,
                                       String searchCriteria,
                                       Date asOfTime,
                                       int offset,
                                       int pageSize,
                                       org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                       String sequencingProperty) throws
                                                                  MetadataServerUncontactableException,
                                                                  UserNotAuthorizedException,
                                                                  InvalidParameterException,
                                                                  FunctionNotSupportedException,
                                                                  UnexpectedResponseException {

        final String methodName = "findCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String urlTemplate = this.omasServerURL + BASE_URL;
        String url = String.format(urlTemplate, serverName, userId);

        if (sequencingOrder == null) {
            sequencingOrder = SequencingOrder.ANY;
        }
        StringBuffer queryStringSB = new StringBuffer();
        QueryUtils.addCharacterToQuery(queryStringSB);
        queryStringSB.append("sequencingOrder=" + sequencingOrder);
        if (asOfTime != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("asOfTime=" + asOfTime);
        }
        if (searchCriteria != null) {
            // encode the string

            encodeQueryProperty("searchCriteria", searchCriteria, methodName, queryStringSB);
        }
        if (offset != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("offset=" + offset);
        }
        if (pageSize != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("pageSize=" + pageSize);
        }
        if (sequencingProperty != null) {
            // encode the string
            encodeQueryProperty("sequencingProperty", sequencingProperty, methodName, queryStringSB);
        }
        if (queryStringSB.length() > 0) {
            url = url + queryStringSB.toString();
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        List<Category> categories = DetectUtils.detectAndReturnCategories(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return categories;
    }

    /**
     * Replace a Category. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory category to be updated
     * @return replaced category
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Category replaceCategory(String userId, String guid, Category suppliedCategory) throws
                                                                                           UnexpectedResponseException,
                                                                                           UserNotAuthorizedException,
                                                                                           FunctionNotSupportedException,
                                                                                           InvalidParameterException,
                                                                                           MetadataServerUncontactableException {
        final String methodName = "replaceCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        Category category = updateCategory(userId, guid, suppliedCategory, true);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return category;
    }

    /**
     * Update a Category. This means to update the category with any non-null attributes from the supplied category.
     * <p>
     * If the caller has chosen to incorporate the category name in their Category Categorys or Categories qualified name, renaming the category will cause those
     * qualified names to mismatch the Category name.
     * If the caller has chosen to incorporate the category qualifiedName in their Category Categorys or Categories qualified name, changing the qualified name of the category will cause those
     * qualified names to mismatch the Category name.
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory category to be updated
     * @return a response which when successful contains the updated category
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Category updateCategory(String userId, String guid, Category suppliedCategory) throws UnexpectedResponseException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 FunctionNotSupportedException,
                                                                                                 InvalidParameterException,
                                                                                                 MetadataServerUncontactableException {
        final String methodName = "updateCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Category category = updateCategory(userId, guid, suppliedCategory, false);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return category;

    }

    /**
     * Delete a Category instance
     * <p>
     * A delete (also known as a soft delete) means that the category instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the category to be deleted.
     * @return the deleted category
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotDeletedException            a delete was issued but the category was not deleted.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Category deleteCategory(String userId, String guid) throws InvalidParameterException,
                                                                      MetadataServerUncontactableException,
                                                                      UserNotAuthorizedException,
                                                                      FunctionNotSupportedException,
                                                                      UnexpectedResponseException,
                                                                      EntityNotDeletedException {
        final String methodName = "deleteCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isPurge=false";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        DetectUtils.detectAndThrowEntityNotDeletedException(restResponse);

        Category category = DetectUtils.detectAndReturnCategory(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return category;
    }

    /**
     * Purge a Category instance
     * <p>
     * A purge means that the category will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the category to be deleted.
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotPurgedException             a hard delete was issued but the category was not purged
     * @throws FunctionNotSupportedException        Function not supported.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     */

    public void purgeCategory(String userId, String guid) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 MetadataServerUncontactableException,
                                                                 EntityNotPurgedException,
                                                                 FunctionNotSupportedException {
        final String methodName = "purgeCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isPurge=true";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowEntityNotPurgedException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a Category
     * <p>
     * Restore allows the deleted Category to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to restore
     * @return the restored category
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Category restoreCategory(String userId, String guid) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       MetadataServerUncontactableException,
                                                                       UnrecognizedGUIDException,
                                                                       FunctionNotSupportedException,
                                                                       UnexpectedResponseException {
        final String methodName = "restoreCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePostNoBody(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Category category = DetectUtils.detectAndReturnCategory(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return category;
    }


    /**
     * Create a SubjectAreaDefinition
     *
     * @param userId                        userId under which the request is performed
     * @param suppliedSubjectAreaDefinition SubjectAreaDefinition
     * @return the created subjectAreaDefinition.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws ClassificationException              Error processing a classification
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public SubjectAreaDefinition createSubjectAreaDefinition(String userId, SubjectAreaDefinition suppliedSubjectAreaDefinition) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "createSubjectAreaDefinition";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String url = this.omasServerURL + String.format(BASE_URL, serverName, userId);
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedSubjectAreaDefinition);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);

        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowClassificationException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        SubjectAreaDefinition subjectAreaDefinition = DetectUtils.detectAndReturnSubjectAreaDefinition(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return subjectAreaDefinition;
    }

    /**
     * Get a subjectAreaDefinition by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the subjectAreaDefinition to get
     * @return the requested subjectAreaDefinition.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public SubjectAreaDefinition getSubjectAreaDefinitionByGuid(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "getSubjectAreaDefinitionByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s";
        String url = String.format(urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        SubjectAreaDefinition subjectAreaDefinition = DetectUtils.detectAndReturnSubjectAreaDefinition(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return subjectAreaDefinition;
    }

    /**
     * Replace a SubjectAreaDefinition. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     *
     * @param userId                        userId under which the request is performed
     * @param guid                          guid of the subjectAreaDefinition to update
     * @param suppliedSubjectAreaDefinition subjectAreaDefinition to be updated
     * @return replaced subjectAreaDefinition
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public SubjectAreaDefinition replaceSubjectAreaDefinition(String userId, String guid, SubjectAreaDefinition suppliedSubjectAreaDefinition) throws
                                                                                                                                               UnexpectedResponseException,
                                                                                                                                               UserNotAuthorizedException,
                                                                                                                                               InvalidParameterException,
                                                                                                                                               MetadataServerUncontactableException {
        final String methodName = "replaceSubjectAreaDefinition";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        SubjectAreaDefinition subjectAreaDefinition = updateSubjectAreaDefinition(userId, guid, suppliedSubjectAreaDefinition, true);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return subjectAreaDefinition;
    }

    /**
     * Update a SubjectAreaDefinition. This means to update the subjectAreaDefinition with any non-null attributes from the supplied subjectAreaDefinition.
     * <p>
     * If the caller has chosen to incorporate the subjectAreaDefinition name in their SubjectAreaDefinition SubjectAreaDefinitions or Categories qualified name, renaming the subjectAreaDefinition will cause those
     * qualified names to mismatch the SubjectAreaDefinition name.
     * If the caller has chosen to incorporate the subjectAreaDefinition qualifiedName in their SubjectAreaDefinition SubjectAreaDefinitions or Categories qualified name, changing the qualified name of the subjectAreaDefinition will cause those
     * qualified names to mismatch the SubjectAreaDefinition name.
     * Status is not updated using this call.
     *
     * @param userId                        userId under which the request is performed
     * @param guid                          guid of the subjectAreaDefinition to update
     * @param suppliedSubjectAreaDefinition subjectAreaDefinition to be updated
     * @return a response which when successful contains the updated subjectAreaDefinition
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public SubjectAreaDefinition updateSubjectAreaDefinition(String userId, String guid, SubjectAreaDefinition suppliedSubjectAreaDefinition) throws UnexpectedResponseException,
                                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                                     InvalidParameterException,
                                                                                                                                                     MetadataServerUncontactableException {
        final String methodName = "updateSubjectAreaDefinition";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaDefinition subjectAreaDefinition = updateSubjectAreaDefinition(userId, guid, suppliedSubjectAreaDefinition, false);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return subjectAreaDefinition;

    }


    /**
     * Delete a SubjectAreaDefinition instance
     * <p>
     * A delete (also known as a soft delete) means that the subjectAreaDefinition instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the subjectAreaDefinition to be deleted.
     * @return the deleted subjectAreaDefinition
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotDeletedException            a delete was issued but the subjectAreaDefinition was not deleted.
     * @throws MetadataServerUncontactableException unable to contact server
     */

    public SubjectAreaDefinition deleteSubjectAreaDefinition(String userId, String guid) throws InvalidParameterException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UserNotAuthorizedException,
                                                                                                FunctionNotSupportedException,
                                                                                                UnexpectedResponseException,
                                                                                                EntityNotDeletedException {
        final String methodName = "deleteSubjectAreaDefinition";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isPurge=false";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        DetectUtils.detectAndThrowEntityNotDeletedException(restResponse);

        SubjectAreaDefinition subjectAreaDefinition = DetectUtils.detectAndReturnSubjectAreaDefinition(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return subjectAreaDefinition;
    }

    /**
     * Purge a SubjectAreaDefinition instance
     * <p>
     * A purge means that the subjectAreaDefinition will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the subjectAreaDefinition to be deleted.
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotPurgedException             a hard delete was issued but the subjectAreaDefinition was not purged
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public void purgeSubjectAreaDefinition(String userId, String guid) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              MetadataServerUncontactableException,
                                                                              EntityNotPurgedException,
                                                                              UnexpectedResponseException {
        final String methodName = "purgeSubjectAreaDefinition";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isPurge=false";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowEntityNotPurgedException(restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a SubjectAreaDefinition
     * <p>
     * Restore allows the deleted SubjectArea to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the subject area definition to restore
     * @return the restored subject area definition
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public SubjectAreaDefinition restoreSubjectAreaDefinition(String userId, String guid) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UnrecognizedGUIDException,
                                                                                                 FunctionNotSupportedException,
                                                                                                 UnexpectedResponseException {
        final String methodName = "restoreSubjectArea";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePostNoBody(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        SubjectAreaDefinition subjectArea = DetectUtils.detectAndReturnSubjectAreaDefinition(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return subjectArea;
    }

    /*
     *  Update Category.
     *
     *
     * @param userId userId under which the request is performed
     * @param userId guid of the category to update
     * @param suppliedCategory Category to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated category.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported
     * @throws InvalidParameterException one of the parameters is null or invalid
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    private Category updateCategory(String userId, String guid, Category suppliedCategory, boolean isReplace) throws UserNotAuthorizedException,
                                                                                                                     InvalidParameterException,
                                                                                                                     FunctionNotSupportedException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UnexpectedResponseException {
        final String methodName = "updateCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isReplace=%b";
        String url = String.format(urlTemplate, serverName, userId, guid, isReplace);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedCategory);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePut(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);

        Category category = DetectUtils.detectAndReturnCategory(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return category;
    }

    /**
     * Update SubjectAreaDefinition.
     *
     * @param userId                        userId under which the request is performed
     * @param guid                          guid of the subjectAreaDefinition to update
     * @param suppliedSubjectAreaDefinition SubjectAreaDefinition to be updated
     * @param isReplace                     flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated subjectAreaDefinition.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    private SubjectAreaDefinition updateSubjectAreaDefinition(String userId, String guid, SubjectAreaDefinition suppliedSubjectAreaDefinition, boolean isReplace) throws UserNotAuthorizedException,
                                                                                                                                                                         InvalidParameterException,
                                                                                                                                                                         MetadataServerUncontactableException,
                                                                                                                                                                         UnexpectedResponseException {
        final String methodName = "updateSubjectAreaDefinition";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isReplace=%b";
        String url = String.format(urlTemplate, serverName, userId, guid, isReplace);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedSubjectAreaDefinition);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePut(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);

        SubjectAreaDefinition subjectAreaDefinition = DetectUtils.detectAndReturnSubjectAreaDefinition(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return subjectAreaDefinition;
    }
}
