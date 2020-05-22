/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
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
 * This interface provides term term authoring interface for subject area experts.
 */
public class SubjectAreaTermImpl extends SubjectAreaBaseImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermImpl.class);

    private static final String className = SubjectAreaTermImpl.class.getName();
    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "terms";


    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL unique id for the connector instance
     */
    public SubjectAreaTermImpl(String omasServerURL, String serverName) {
        super(omasServerURL, serverName);
    }

    /**
     * Create a Term
     *
     * @param userId       userId under which the request is performed
     * @param suppliedTerm Term to create
     * @return the created term.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws ClassificationException              Error processing a classification
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Term createTerm(String userId, Term suppliedTerm) throws
                                                             MetadataServerUncontactableException,
                                                             InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             ClassificationException,
                                                             FunctionNotSupportedException,
                                                             UnexpectedResponseException {
        final String methodName = "createTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String url = this.omasServerURL + String.format(BASE_URL, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);

        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowClassificationException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Term term = DetectUtils.detectAndReturnTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    /**
     * Get a term by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the term to get
     * @return the requested term.
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

    public Term getTermByGuid(String userId, String guid) throws
                                                          MetadataServerUncontactableException,
                                                          UserNotAuthorizedException,
                                                          InvalidParameterException,
                                                          FunctionNotSupportedException,
                                                          UnexpectedResponseException {
        final String methodName = "getTermByGuid";
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
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Term term = DetectUtils.detectAndReturnTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    /**
     * Get Term relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the term to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Term guid
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
    public List<Line> getTermRelationships(String userId, String guid,
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
        final String methodName = "getRelationships";
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
     * Replace a Term. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call
     * The GovernanceAction content if specified replaces what is on the server.
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @return replaced term
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Term replaceTerm(String userId, String guid, Term suppliedTerm) throws
                                                                           UnexpectedResponseException,
                                                                           UserNotAuthorizedException,
                                                                           FunctionNotSupportedException,
                                                                           InvalidParameterException,
                                                                           MetadataServerUncontactableException {
        final String methodName = "replaceTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        Term term = updateTerm(userId, guid, suppliedTerm, true);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    /**
     * Update a Term. This means to update the term with any non-null attributes from the supplied term.
     * <p>
     * If the caller has chosen to incorporate the term name in their Term Terms or Categories qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * If the caller has chosen to incorporate the term qualifiedName in their Term Terms or Categories qualified name, changing the qualified name of the term will cause those
     * qualified names to mismatch the Term name.
     * Status is not updated using this call.
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @return a response which when successful contains the updated term
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Term updateTerm(String userId, String guid, Term suppliedTerm) throws UnexpectedResponseException,
                                                                                 UserNotAuthorizedException,
                                                                                 FunctionNotSupportedException,
                                                                                 InvalidParameterException,
                                                                                 MetadataServerUncontactableException {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Term term = updateTerm(userId, guid, suppliedTerm, false);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;

    }


    /**
     * Delete a Term instance
     * <p>
     * A delete (also known as a soft delete) means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the term to be deleted.
     * @return the deleted term
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Term deleteTerm(String userId, String guid) throws InvalidParameterException,
                                                              MetadataServerUncontactableException,
                                                              UserNotAuthorizedException,
                                                              FunctionNotSupportedException,
                                                              UnrecognizedGUIDException,
                                                              UnexpectedResponseException,
                                                              EntityNotDeletedException {
        final String methodName = "deleteTerm";
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

        Term term = DetectUtils.detectAndReturnTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    /**
     * Purge a Term instance
     * <p>
     * A purge means that the term will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the term to be deleted.
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotPurgedException             a hard delete was issued but the term was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     */

    public void purgeTerm(String userId, String guid) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             MetadataServerUncontactableException,
                                                             UnrecognizedGUIDException,
                                                             FunctionNotSupportedException,
                                                             EntityNotPurgedException {
        final String methodName = "purgeTerm";
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
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Update Term.
     * <p>
     * If the caller has chosen to incorporate the term name in their Term Terms qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * The GovernanceAction content if specified replaces what is on the server.
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid  of the term to update
     * @param suppliedTerm Term to be updated
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated term.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    private Term updateTerm(String userId, String guid, Term suppliedTerm, boolean isReplace) throws
                                                                                              UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              FunctionNotSupportedException,
                                                                                              MetadataServerUncontactableException,
                                                                                              UnexpectedResponseException {
        final String methodName = "updateTerm";
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
            requestBody = mapper.writeValueAsString(suppliedTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePut(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);

        Term term = DetectUtils.detectAndReturnTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to restore
     * @return the restored term
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Term restoreTerm(String userId, String guid) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               MetadataServerUncontactableException,
                                                               UnrecognizedGUIDException,
                                                               FunctionNotSupportedException,
                                                               UnexpectedResponseException {
        final String methodName = "restoreTerm";
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
        Term term = DetectUtils.detectAndReturnTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    /**
     * Find Term
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Term property values (this does not include the GlossarySummary content).
     * @param asOfTime           the Terms returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Terms meeting the search Criteria
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
    public List<Term> findTerm(String userId,
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

        final String methodName = "findTerm";
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
        List<Term> terms = DetectUtils.detectAndReturnTerms(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return terms;
    }

}
