/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
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
 * This interface provides glossary authoring interface for subject area experts.
 */
public class SubjectAreaGlossaryImpl extends SubjectAreaBaseImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGlossaryImpl.class);

    private static final String className = SubjectAreaGlossaryImpl.class.getName();

    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "glossaries";


    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL unique id for the connector instance
     */
    public SubjectAreaGlossaryImpl(String omasServerURL, String serverName) {
        super(omasServerURL, serverName);
    }


    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Glossaries with the same name can be confusing. Best practise is to createGlossaries that have unique names.
     * This Create call does not police that glossary names are unique. So it is possible to create Glossaries with the same name as each other.
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return the created glossary.
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

    public Glossary createGlossary(String userId, Glossary suppliedGlossary) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "createGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String url = this.omasServerURL + String.format(BASE_URL, serverName, userId);
        InputValidator.validateNodeType(className, methodName, suppliedGlossary.getNodeType(), NodeType.Glossary, NodeType.Taxonomy, NodeType.TaxonomyAndCanonicalGlossary, NodeType.CanonicalGlossary);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedGlossary);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);

        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowClassificationException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, restResponse);
        // that the returned nodeType matches the requested one
        if (suppliedGlossary.getNodeType() != null && !suppliedGlossary.getNodeType().equals(glossary.getNodeType())) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.UNEXPECTED_NODETYPE;

            throw new InvalidParameterException(
                    errorCode.getMessageDefinition(),
                    className,
                    methodName,
                    "NodeType",
                    suppliedGlossary.getNodeType().name()

            );
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    /**
     * Get a glossary by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the glossary to get
     * @return the requested glossary.
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

    public Glossary getGlossaryByGuid(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "getGlossaryByGuid";
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
        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    /**
     * Get Glossary relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the glossary to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Glossary guid
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
    public List<Line> getGlossaryRelationships(String userId, String guid,
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
        final String methodName = "getGlossaryRelationships";
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
     * Find Glossary
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Glossary properties. If not specified then all glossaries are returned.
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Glossaries meeting the search Criteria
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
    public List<Glossary> findGlossary(String userId,
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

        final String methodName = "findGlossary";
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
        List<Glossary> glossaries = DetectUtils.detectAndReturnGlossaries(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossaries;
    }

    /**
     * Replace a Glossary. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @return replaced glossary
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Glossary replaceGlossary(String userId, String guid, Glossary suppliedGlossary) throws
                                                                                           UnexpectedResponseException,
                                                                                           UserNotAuthorizedException,
                                                                                           InvalidParameterException,
                                                                                           MetadataServerUncontactableException {
        final String methodName = "replaceGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Glossary glossary = updateGlossary(userId, guid, suppliedGlossary, true);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    /**
     * Update a Glossary. This means to update the glossary with any non-null attributes from the supplied glossary.
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Glossary updateGlossary(String userId, String guid, Glossary suppliedGlossary) throws UnexpectedResponseException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 MetadataServerUncontactableException {
        final String methodName = "updateGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Glossary glossary = updateGlossary(userId, guid, suppliedGlossary, false);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;

    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * A delete (also known as a soft delete) means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the glossary to be deleted.
     * @return the deleted glossary
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotDeletedException            a delete was issued but the glossary was not deleted.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Glossary deleteGlossary(String userId, String guid) throws InvalidParameterException,
                                                                      MetadataServerUncontactableException,
                                                                      UserNotAuthorizedException,
                                                                      UnrecognizedGUIDException,
                                                                      FunctionNotSupportedException,
                                                                      UnexpectedResponseException,
                                                                      EntityNotDeletedException {
        final String methodName = "deleteGlossary";
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
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        DetectUtils.detectAndThrowEntityNotDeletedException(restResponse);

        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    /**
     * Purge a Glossary instance
     * <p>
     * The purge of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * A purge means that the glossary will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the glossary to be deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotPurgedException             a hard delete was issued but the glossary was not purged
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public void purgeGlossary(String userId, String guid) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 MetadataServerUncontactableException,
                                                                 UnrecognizedGUIDException,
                                                                 EntityNotPurgedException,
                                                                 UnexpectedResponseException,
                                                                 FunctionNotSupportedException {
        final String methodName = "purgeGlossary";
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
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowEntityNotPurgedException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        DetectUtils.detectVoid(className, methodName, restResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /*
     *  Update Glossary.
     *
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     *
     * @param userId userId under which the request is performed
     * @param userIdguid of the glossary to update
     * @param suppliedGlossary Glossary to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated glossary.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    private Glossary updateGlossary(String userId, String guid, Glossary suppliedGlossary, boolean isReplace) throws
                                                                                                              UserNotAuthorizedException,
                                                                                                              InvalidParameterException,
                                                                                                              MetadataServerUncontactableException,
                                                                                                              UnexpectedResponseException {
        final String methodName = "updateGlossary";
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
            requestBody = mapper.writeValueAsString(suppliedGlossary);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePut(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);

        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the glossary to restore
     * @return the restored glossary
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Glossary restoreGlossary(String userId, String guid) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       MetadataServerUncontactableException,
                                                                       UnrecognizedGUIDException,
                                                                       FunctionNotSupportedException,
                                                                       UnexpectedResponseException {
        final String methodName = "restoreGlossary";
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

        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }
}
