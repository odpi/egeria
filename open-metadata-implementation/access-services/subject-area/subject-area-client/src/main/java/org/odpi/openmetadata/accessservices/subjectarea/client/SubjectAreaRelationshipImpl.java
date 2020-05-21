/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.RestCaller;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides relationship authoring interface for subject area experts.
 */
public class SubjectAreaRelationshipImpl extends SubjectAreaBaseImpl implements SubjectAreaRelationship {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRelationshipImpl.class);

    private static final String className = SubjectAreaRelationshipImpl.class.getName();
    private static final String BASE_RELATIONSHIPS_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "relationships";
    private static final String HASA = "/has-as";
    private static final String RELATED_TERM = "/related-terms";
    private static final String SYNONYM = "/synonyms";
    private static final String ANTONYM = "/antonyms";
    private static final String TRANSLATION = "/translations";
    private static final String USED_IN_CONTEXT = "/used-in-contexts";
    private static final String PREFERRED_TERM = "/preferred-terms";
    private static final String VALID_VALUE = "/valid-values";
    private static final String REPLACEMENT_TERM = "/replacement-terms";
    private static final String TYPED_BY = "/typed-bys";
    private static final String IS_A = "/is-as";
    private static final String IS_A_TYPE_OF = "/is-a-type-ofs";
    private static final String TERM_CATEGORIZATION = "/term-categorizations";
    private static final String SEMANTIC_ASSIGNMENT = "/semantic-assignments";
    private static final String TERM_ANCHOR = "/term-anchor";
    private static final String CATEGORY_ANCHOR = "/category-anchor";
    private static final String PROJECT_SCOPE = "/project-scopes";

    // urls to use when creating types of relationships
    private static final String BASE_RELATIONSHIPS_HASA_URL = BASE_RELATIONSHIPS_URL + HASA;
    private static final String BASE_RELATIONSHIPS_RELATEDTERM_URL = BASE_RELATIONSHIPS_URL + RELATED_TERM;
    private static final String BASE_RELATIONSHIPS_SYNONYM_URL = BASE_RELATIONSHIPS_URL + SYNONYM;
    private static final String BASE_RELATIONSHIPS_ANTONYM_URL = BASE_RELATIONSHIPS_URL + ANTONYM;
    private static final String BASE_RELATIONSHIPS_TRANSLATION_URL = BASE_RELATIONSHIPS_URL + TRANSLATION;
    private static final String BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL = BASE_RELATIONSHIPS_URL + USED_IN_CONTEXT;
    private static final String BASE_RELATIONSHIPS_PREFERRED_TERM_URL = BASE_RELATIONSHIPS_URL + PREFERRED_TERM;
    private static final String BASE_RELATIONSHIPS_VALID_VALUE_URL = BASE_RELATIONSHIPS_URL + VALID_VALUE;
    private static final String BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL = BASE_RELATIONSHIPS_URL + REPLACEMENT_TERM;
    private static final String BASE_RELATIONSHIPS_TYPED_BY_URL = BASE_RELATIONSHIPS_URL + TYPED_BY;
    private static final String BASE_RELATIONSHIPS_IS_A_URL = BASE_RELATIONSHIPS_URL + IS_A;
    private static final String BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL = BASE_RELATIONSHIPS_URL + IS_A_TYPE_OF;
    private static final String BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL = BASE_RELATIONSHIPS_URL + TERM_CATEGORIZATION;
    private static final String BASE_RELATIONSHIPS_SEMANTIC_ASSIGNMENT_URL = BASE_RELATIONSHIPS_URL + SEMANTIC_ASSIGNMENT;
    private static final String BASE_RELATIONSHIPS_TERM_ANCHOR_URL = BASE_RELATIONSHIPS_URL + TERM_ANCHOR;
    private static final String BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL = BASE_RELATIONSHIPS_URL + CATEGORY_ANCHOR;
    private static final String BASE_RELATIONSHIPS_PROJECT_SCOPE_URL = BASE_RELATIONSHIPS_URL + PROJECT_SCOPE;

    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL unique id for the connector instance
     */
    public SubjectAreaRelationshipImpl(String omasServerURL, String serverName) {
        super(omasServerURL, serverName);
    }

    /**
     * Create a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the created term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Hasa createTermHASARelationship(String userId, Hasa termHASARelationship) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            MetadataServerUncontactableException,
                                                                                            UnexpectedResponseException,
                                                                                            UnrecognizedGUIDException {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_HASA_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termHASARelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        Hasa createdTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermHASARelationship;

    }

    /**
     * Get a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to get
     * @return Hasa
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Hasa getTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                           MetadataServerUncontactableException,
                                                                           UserNotAuthorizedException,
                                                                           UnexpectedResponseException,
                                                                           UnrecognizedGUIDException {
        final String methodName = "getTermHASARelationship";
        final String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_HASA_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, urlTemplate);
        Hasa gotTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermHASARelationship;
    }

    /**
     * Update a Term HASA Relationship.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the updated term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Hasa updateTermHASARelationship(String userId, Hasa termHASARelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "updateTermHASARelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termHASARelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_HASA_URL, requestBody, false);
        Hasa updatedTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermHASARelationship;
    }

    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the replaced term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Hasa replaceTermHASARelationship(String userId, Hasa termHASARelationship) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException {
        final String methodName = "replaceTermHASARelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termHASARelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_HASA_URL, requestBody, true);
        Hasa updatedTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermHASARelationship;
    }

    /**
     * Delete a Term HASA Relationship. A relationship between a spine object and a spine attribute.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to delete
     * @return Deleted Hasa
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Hasa deleteTermHASARelationship(String userId, String guid) throws
                                                                       InvalidParameterException,
                                                                       MetadataServerUncontactableException,
                                                                       UserNotAuthorizedException,
                                                                       UnrecognizedGUIDException,
                                                                       FunctionNotSupportedException,
                                                                       RelationshipNotDeletedException,
                                                                       UnexpectedResponseException {
        final String methodName = "deleteTermHASARelationship";
        final String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_HASA_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, urlTemplate);
        Hasa termHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return termHASARelationship;
    }


    /**
     * Purge a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             UnrecognizedGUIDException,
                                                                             MetadataServerUncontactableException,
                                                                             UnexpectedResponseException,
                                                                             RelationshipNotPurgedException {
        final String methodName = "purgeTermHASARelationship";
        final String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_HASA_URL;
        purgeRelationship(userId, guid, methodName, urlTemplate);
    }

    /**
     * Restore a has a relationship
     * <p>
     * Restore allows the deleted has a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the has a relationship to delete
     * @return response which when successful contains the restored has a relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Hasa restoreTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException,
                                                                               UnrecognizedGUIDException {
        final String methodName = "restoreTermHASARelationship";
        final String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_HASA_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, urlTemplate);
        Hasa termHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return termHASARelationship;
    }

    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * <p>
     *
     * @param userId                  unique identifier for requesting user, under which the request is performed
     * @param relatedTermRelationship the RelatedTerm relationship
     * @return the created RelatedTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public RelatedTerm createRelatedTerm(String userId, RelatedTerm relatedTermRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createTermRelatedARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        final String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_RELATEDTERM_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(relatedTermRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        RelatedTerm createdRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdRelatedTerm;
    }

    /**
     * Get a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return RelatedTerm
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public RelatedTerm getRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UserNotAuthorizedException,
                                                                         UnexpectedResponseException,
                                                                         UnrecognizedGUIDException {
        final String methodName = "getRelatedTerm";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotRelatedTerm;
    }

    /**
     * Update a RelatedTerm Relationship.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param termRelatedTerm the RelatedTerm relationship
     * @return the updated term RelatedTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public RelatedTerm updateRelatedTerm(String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "updateRelatedTerm";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termRelatedTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_RELATEDTERM_URL, requestBody, false);
        RelatedTerm relatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relatedTermRelationship;
    }

    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param termRelatedTerm the replacement related term relationship
     * @return ReplacementTerm replaced related Term relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public RelatedTerm replaceRelatedTerm(String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "replaceRelatedTerm";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termRelatedTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_RELATEDTERM_URL, requestBody, true);
        RelatedTerm relatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relatedTermRelationship;
    }

    /**
     * Restore a Related Term relationship
     * <p>
     * Restore allows the deleted related term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to restore
     * @return response which when successful contains the restored related term relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public RelatedTerm restoreRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             MetadataServerUncontactableException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException {
        final String methodName = "restoreRelatedTerm";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotRelatedTerm;
    }

    /**
     * Delete a RelatedTerm. A Related Term is a link between two similar Terms.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted RelatedTerm
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public RelatedTerm deleteRelatedTerm(String userId, String guid) throws
                                                                     InvalidParameterException,
                                                                     MetadataServerUncontactableException,
                                                                     UserNotAuthorizedException,
                                                                     UnrecognizedGUIDException,
                                                                     FunctionNotSupportedException,
                                                                     RelationshipNotDeletedException,
                                                                     UnexpectedResponseException {
        final String methodName = "deleteRelatedTerm";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotRelatedTerm;
    }


    /**
     * Purge a RelatedTerm. A Related Term is a link between two similar Terms.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    UnrecognizedGUIDException,
                                                                    MetadataServerUncontactableException,
                                                                    UnexpectedResponseException,
                                                                    RelationshipNotPurgedException {
        final String methodName = "purgeRelatedTerm";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_RELATEDTERM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a related term relationship
     * <p>
     * Restore allows the deleted related term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to delete
     * @return response which when successful contains the restored related term relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public RelatedTerm restoreRelatedTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UnexpectedResponseException,
                                                                                         UnrecognizedGUIDException {
        final String methodName = "restoreRelatedTerm";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotRelatedTerm;
    }

    /**
     * Create a synonym relationship. A link between glossary terms that have the same meaning.
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param synonym the Synonym relationship
     * @return the created Synonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Synonym createSynonymRelationship(String userId, Synonym synonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createSynonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(synonym);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        Synonym createdSynonym = DetectUtils.detectAndReturnSynonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdSynonym;
    }

    /**
     * Get a synonym relationship. A link between glossary terms that have the same meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return Synonym
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Synonym getSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                             MetadataServerUncontactableException,
                                                                             UserNotAuthorizedException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException {
        final String methodName = "getSynonymRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotSynonym;
    }

    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param userId              userId under which the request is performed
     * @param synonymRelationship the Synonym relationship
     * @return updated Synonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Synonym updateSynonymRelationship(String userId, Synonym synonymRelationship) throws InvalidParameterException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UserNotAuthorizedException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException {
        final String methodName = "updateSynonymRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(synonymRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL, requestBody, false);
        Synonym updatedSynonymRelationship = DetectUtils.detectAndReturnSynonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedSynonymRelationship;
    }

    /**
     * Replace a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param userId              userId under which the request is performed
     * @param synonymRelationship the Synonym relationship
     * @return replaced synonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Synonym replaceSynonymRelationship(String userId, Synonym synonymRelationship) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException {
        final String methodName = "updateSynonymRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(synonymRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL, requestBody, true);
        Synonym updatedSynonymRelationship = DetectUtils.detectAndReturnSynonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedSynonymRelationship;
    }

    /**
     * Delete a synonym relationship. A link between glossary terms that have the same meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted Synonym
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Synonym deleteSynonymRelationship(String userId, String guid) throws
                                                                         InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UnrecognizedGUIDException,
                                                                         UserNotAuthorizedException,
                                                                         FunctionNotSupportedException,
                                                                         RelationshipNotDeletedException,
                                                                         UnexpectedResponseException {
        final String methodName = "deleteSynonymRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotSynonym;
    }


    /**
     * Purge a synonym relationship. A link between glossary terms that have the same meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            UnrecognizedGUIDException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException,
                                                                            RelationshipNotPurgedException {
        final String methodName = "purgeSynonymRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a Synonym relationship
     * <p>
     * Restore allows the deleted Synonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to restore
     * @return response which when successful contains the restored Synonym relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Synonym restoreSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException {
        final String methodName = "restoreSynonymRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotSynonym;
    }

    /**
     * Create a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param antonym the Antonym relationship
     * @return the created antonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Antonym createAntonymRelationship(String userId, Antonym antonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createAntonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_ANTONYM_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(antonym);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        Antonym createdAntonym = DetectUtils.detectAndReturnAntonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdAntonym;
    }

    /**
     * Get a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return Antonym
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Antonym getAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                             MetadataServerUncontactableException, UserNotAuthorizedException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException {
        final String methodName = "getAntonymRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotAntonym;
    }

    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param userId              userId under which the request is performed
     * @param antonymRelationship the Antonym relationship
     * @return Antonym updated antonym
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Antonym updateAntonymRelationship(String userId, Antonym antonymRelationship) throws InvalidParameterException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UserNotAuthorizedException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException {
        final String methodName = "updateAntonymRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(antonymRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_ANTONYM_URL, requestBody, false);
        Antonym updatedAntonymRelationship = DetectUtils.detectAndReturnAntonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedAntonymRelationship;
    }

    /**
     * Replace an Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param userId              userId under which the request is performed
     * @param antonymRelationship the antonym relationship
     * @return Antonym replaced antonym
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Antonym replaceAntonymRelationship(String userId, Antonym antonymRelationship) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException {
        final String methodName = "updateAntonymRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(antonymRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_ANTONYM_URL, requestBody, true);
        Antonym updatedAntonymRelationship = DetectUtils.detectAndReturnAntonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedAntonymRelationship;
    }

    /**
     * Delete a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted Antonym
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Antonym deleteAntonymRelationship(String userId, String guid) throws
                                                                         InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UserNotAuthorizedException,
                                                                         UnrecognizedGUIDException,
                                                                         FunctionNotSupportedException,
                                                                         RelationshipNotDeletedException,
                                                                         UnexpectedResponseException {
        final String methodName = "deleteAntonymRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotAntonym;
    }


    /**
     * Purge a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            UnrecognizedGUIDException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException,
                                                                            RelationshipNotPurgedException {
        final String methodName = "purgeAntonymRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_ANTONYM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a Antonym relationship
     * <p>
     * Restore allows the deleted Antonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @return response which when successful contains the restored Synonym relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Antonym restoreAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException {
        final String methodName = "restoreAntonymRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotAntonym;
    }

    /**
     * Create a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * <p>
     *
     * @param userId      userId under which the request is performed
     * @param translation the Translation relationship
     * @return the created translation relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Translation createTranslationRelationship(String userId, Translation translation) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    MetadataServerUncontactableException,
                                                                                                    UnexpectedResponseException,
                                                                                                    UnrecognizedGUIDException {
        final String methodName = "createTranslationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TRANSLATION_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(translation);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        Translation createdTranslation = DetectUtils.detectAndReturnTranslation(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTranslation;
    }

    /**
     * Get a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return Translation
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Translation getTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException {
        final String methodName = "getTranslationRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TRANSLATION_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        Translation gotTranslation = DetectUtils.detectAndReturnTranslation(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTranslation;
    }

    /**
     * Update a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param translationRelationship the Translation relationship
     * @return Translation updated translation
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Translation updateTranslationRelationship(String userId, Translation translationRelationship) throws InvalidParameterException,
                                                                                                                MetadataServerUncontactableException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                UnexpectedResponseException,
                                                                                                                UnrecognizedGUIDException {
        final String methodName = "updateTranslationRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(translationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_TRANSLATION_URL, requestBody, false);
        Translation updatedTranslationRelationship = DetectUtils.detectAndReturnTranslation(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTranslationRelationship;
    }

    /**
     * Replace an Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param translationRelationship the translation relationship
     * @return Translation replaced translation
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Translation replaceTranslationRelationship(String userId, Translation translationRelationship) throws InvalidParameterException,
                                                                                                                 MetadataServerUncontactableException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 UnexpectedResponseException,
                                                                                                                 UnrecognizedGUIDException {
        final String methodName = "updateTranslationRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(translationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_TRANSLATION_URL, requestBody, true);
        Translation updatedTranslationRelationship = DetectUtils.detectAndReturnTranslation(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTranslationRelationship;
    }

    /**
     * Delete a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted Translation
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Translation deleteTranslationRelationship(String userId, String guid) throws
                                                                                 InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnrecognizedGUIDException,
                                                                                 FunctionNotSupportedException,
                                                                                 RelationshipNotDeletedException,
                                                                                 UnexpectedResponseException {
        final String methodName = "deleteTranslationRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TRANSLATION_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        Translation gotTranslation = DetectUtils.detectAndReturnTranslation(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTranslation;
    }

    /**
     * Purge a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                RelationshipNotPurgedException,
                                                                                UnrecognizedGUIDException,
                                                                                MetadataServerUncontactableException,
                                                                                UnexpectedResponseException {
        final String methodName = "purgeTranslationRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TRANSLATION_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a Translation relationship
     * <p>
     * Restore allows the deleted Translation relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * @return response which when successful contains the restored Translation relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Translation restoreTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UnexpectedResponseException,
                                                                                         UnrecognizedGUIDException {
        final String methodName = "restoreTranslationRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TRANSLATION_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        Translation gotTranslation = DetectUtils.detectAndReturnTranslation(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTranslation;
    }

    /**
     * Create a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param usedInContext the UsedInContext relationship
     * @return the created usedInContext relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public UsedInContext createUsedInContextRelationship(String userId, UsedInContext usedInContext) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            MetadataServerUncontactableException,
                                                                                                            UnexpectedResponseException,
                                                                                                            UnrecognizedGUIDException {
        final String methodName = "createUsedInContextRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(usedInContext);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        UsedInContext createdUsedInContext = DetectUtils.detectAndReturnUsedInContext(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdUsedInContext;
    }

    /**
     * Get a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return UsedInContext
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public UsedInContext getUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UserNotAuthorizedException,
                                                                                         UnexpectedResponseException,
                                                                                         UnrecognizedGUIDException {
        final String methodName = "getUsedInContextRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        UsedInContext gotUsedInContext = DetectUtils.detectAndReturnUsedInContext(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotUsedInContext;
    }

    /**
     * Update a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     * @param userId                    userId under which the request is performed
     * @param usedInContextRelationship the UsedInContext relationship
     * @return UsedInContext updated usedInContext
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public UsedInContext updateUsedInContextRelationship(String userId, UsedInContext usedInContextRelationship) throws InvalidParameterException,
                                                                                                                        MetadataServerUncontactableException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        UnexpectedResponseException,
                                                                                                                        UnrecognizedGUIDException {
        final String methodName = "updateUsedInContextRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(usedInContextRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL, requestBody, false);
        UsedInContext updatedUsedInContextRelationship = DetectUtils.detectAndReturnUsedInContext(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedUsedInContextRelationship;
    }

    /**
     * Replace an UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     * @param userId                    userId under which the request is performed
     * @param usedInContextRelationship the usedInContext relationship
     * @return UsedInContext replaced usedInContext
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public UsedInContext replaceUsedInContextRelationship(String userId, UsedInContext usedInContextRelationship) throws InvalidParameterException,
                                                                                                                         MetadataServerUncontactableException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         UnexpectedResponseException,
                                                                                                                         UnrecognizedGUIDException {
        final String methodName = "updateUsedInContextRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(usedInContextRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL, requestBody, true);
        UsedInContext updatedUsedInContextRelationship = DetectUtils.detectAndReturnUsedInContext(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedUsedInContextRelationship;
    }

    /**
     * Delete a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted UsedInContext
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public UsedInContext deleteUsedInContextRelationship(String userId, String guid) throws
                                                                                     InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnrecognizedGUIDException,
                                                                                     FunctionNotSupportedException,
                                                                                     RelationshipNotDeletedException,
                                                                                     UnexpectedResponseException {
        final String methodName = "deleteUsedInContextRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        UsedInContext gotUsedInContext = DetectUtils.detectAndReturnUsedInContext(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotUsedInContext;
    }


    /**
     * Purge a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  RelationshipNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException {
        final String methodName = "purgeUsedInContextRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a Used in context relationship
     * <p>
     * Restore allows the deletedUsed in context relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Used in context relationship to delete
     * @return response which when successful contains the restored Used in context relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public UsedInContext restoreUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException {
        final String methodName = "restoreUsedInContextRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        UsedInContext gotUsedInContext = DetectUtils.detectAndReturnUsedInContext(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotUsedInContext;
    }

    /**
     * Create a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param preferredTerm the PreferredTerm relationship
     * @return the created preferredTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm createPreferredTermRelationship(String userId, PreferredTerm preferredTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createPreferredTermRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(preferredTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        PreferredTerm createdPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdPreferredTerm;
    }

    /**
     * Get a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return PreferredTerm
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm getPreferredTermRelationship(String userId, String guid) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "getPreferredTermRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        PreferredTerm gotPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotPreferredTerm;
    }

    /**
     * Update a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     * @param userId                    userId under which the request is performed
     * @param preferredTermRelationship the PreferredTerm relationship
     * @return PreferredTerm updated preferredTerm
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm updatePreferredTermRelationship(String userId, PreferredTerm preferredTermRelationship) throws InvalidParameterException,
                                                                                                                        MetadataServerUncontactableException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        UnexpectedResponseException,
                                                                                                                        UnrecognizedGUIDException {
        final String methodName = "updatePreferredTermRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(preferredTermRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_PREFERRED_TERM_URL, requestBody, false);
        PreferredTerm updatedPreferredTermRelationship = DetectUtils.detectAndReturnPreferredTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedPreferredTermRelationship;
    }

    /**
     * Replace an PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     * @param userId                    userId under which the request is performed
     * @param preferredTermRelationship the preferredTerm relationship
     * @return PreferredTerm replaced preferredTerm
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm replacePreferredTermRelationship(String userId, PreferredTerm preferredTermRelationship) throws InvalidParameterException,
                                                                                                                         MetadataServerUncontactableException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         UnexpectedResponseException,
                                                                                                                         UnrecognizedGUIDException {
        final String methodName = "updatePreferredTermRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(preferredTermRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_PREFERRED_TERM_URL, requestBody, true);
        PreferredTerm updatedPreferredTermRelationship = DetectUtils.detectAndReturnPreferredTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedPreferredTermRelationship;
    }

    /**
     * Delete a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted PreferredTerm
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm deletePreferredTermRelationship(String userId, String guid) throws
                                                                                     InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnrecognizedGUIDException,
                                                                                     FunctionNotSupportedException,
                                                                                     RelationshipNotDeletedException,
                                                                                     UnexpectedResponseException {
        final String methodName = "deletePreferredTermRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        PreferredTerm gotPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotPreferredTerm;
    }

    /**
     * Purge a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgePreferredTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  RelationshipNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException {
        final String methodName = "purgePreferredTermRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a preferred term relationship
     * <p>
     * Restore allows the deletedpreferred term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the preferred term relationship to delete
     * @return response which when successful contains the restored preferred term relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public PreferredTerm restorePreferredTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException {
        final String methodName = "restorePreferredTermRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        PreferredTerm gotPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotPreferredTerm;
    }

    /**
     * Create a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * <p>
     *
     * @param userId     userId under which the request is performed
     * @param validValue the ValidValue relationship
     * @return the created validValue relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ValidValue createValidValueRelationship(String userId, ValidValue validValue) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException {
        final String methodName = "createValidValueRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_VALID_VALUE_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(validValue);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        ValidValue createdValidValue = DetectUtils.detectAndReturnValidValue(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdValidValue;
    }

    /**
     * Get a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return ValidValue
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ValidValue getValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UserNotAuthorizedException,
                                                                                   UnexpectedResponseException,
                                                                                   UnrecognizedGUIDException {
        final String methodName = "getValidValueRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_VALID_VALUE_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        ValidValue gotValidValue = DetectUtils.detectAndReturnValidValue(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotValidValue;
    }

    /**
     * Update a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param userId                 userId under which the request is performed
     * @param validValueRelationship the ValidValue relationship
     * @return ValidValue updated validValue
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ValidValue updateValidValueRelationship(String userId, ValidValue validValueRelationship) throws InvalidParameterException,
                                                                                                            MetadataServerUncontactableException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            UnexpectedResponseException,
                                                                                                            UnrecognizedGUIDException {
        final String methodName = "updateValidValueRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(validValueRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_VALID_VALUE_URL, requestBody, false);
        ValidValue updatedValidValueRelationship = DetectUtils.detectAndReturnValidValue(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedValidValueRelationship;
    }

    /**
     * Replace an ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param userId                 userId under which the request is performed
     * @param validValueRelationship the validValue relationship
     * @return ValidValue replaced validValue
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ValidValue replaceValidValueRelationship(String userId, ValidValue validValueRelationship) throws InvalidParameterException,
                                                                                                             MetadataServerUncontactableException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             UnexpectedResponseException,
                                                                                                             UnrecognizedGUIDException {
        final String methodName = "updateValidValueRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(validValueRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_VALID_VALUE_URL, requestBody, true);
        ValidValue updatedValidValueRelationship = DetectUtils.detectAndReturnValidValue(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedValidValueRelationship;
    }

    /**
     * Delete a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted ValidValue
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ValidValue deleteValidValueRelationship(String userId, String guid) throws
                                                                               InvalidParameterException,
                                                                               MetadataServerUncontactableException,
                                                                               UserNotAuthorizedException,
                                                                               UnrecognizedGUIDException,
                                                                               FunctionNotSupportedException,
                                                                               RelationshipNotDeletedException,
                                                                               UnexpectedResponseException {
        final String methodName = "deleteValidValueRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_VALID_VALUE_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        ValidValue gotValidValue = DetectUtils.detectAndReturnValidValue(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotValidValue;
    }

    /**
     * Purge a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               RelationshipNotPurgedException,
                                                                               UnrecognizedGUIDException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException {
        final String methodName = "purgeValidValueRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_VALID_VALUE_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a valid value relationship
     * <p>
     * Restore allows the deletedvalid value relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the valid value relationship to delete
     * @return response which when successful contains the restored valid value relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public ValidValue restoreValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UnexpectedResponseException,
                                                                                       UnrecognizedGUIDException {
        final String methodName = "restoreValidValueRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_VALID_VALUE_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        ValidValue gotValidValue = DetectUtils.detectAndReturnValidValue(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotValidValue;
    }

    /**
     * Create a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param replacementTerm the ReplacementTerm relationship
     * @return the created replacementTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ReplacementTerm createReplacementTermRelationship(String userId, ReplacementTerm replacementTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createReplacementTermRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(replacementTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        ReplacementTerm createdReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdReplacementTerm;
    }

    /**
     * Get a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return ReplacementTerm
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ReplacementTerm getReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UserNotAuthorizedException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException {
        final String methodName = "getReplacementTermRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        ReplacementTerm gotReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotReplacementTerm;
    }

    /**
     * Update a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId                      userId under which the request is performed
     * @param replacementTermRelationship the ReplacementTerm relationship
     * @return ReplacementTerm updated replacementTerm
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ReplacementTerm updateReplacementTermRelationship(String userId, ReplacementTerm replacementTermRelationship) throws InvalidParameterException,
                                                                                                                                MetadataServerUncontactableException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                UnexpectedResponseException,
                                                                                                                                UnrecognizedGUIDException {
        final String methodName = "updateReplacementTermRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(replacementTermRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL, requestBody, false);
        ReplacementTerm updatedReplacementTermRelationship = DetectUtils.detectAndReturnReplacementTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedReplacementTermRelationship;
    }

    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId                      userId under which the request is performed
     * @param replacementTermRelationship the replacementTerm relationship
     * @return ReplacementTerm replaced replacementTerm
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ReplacementTerm replaceReplacementTermRelationship(String userId, ReplacementTerm replacementTermRelationship) throws InvalidParameterException,
                                                                                                                                 MetadataServerUncontactableException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 UnexpectedResponseException,
                                                                                                                                 UnrecognizedGUIDException {
        final String methodName = "updateReplacementTermRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(replacementTermRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL, requestBody, true);
        ReplacementTerm updatedReplacementTermRelationship = DetectUtils.detectAndReturnReplacementTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedReplacementTermRelationship;
    }

    /**
     * Delete a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted ReplacementTerm
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ReplacementTerm deleteReplacementTermRelationship(String userId, String guid) throws
                                                                                         InvalidParameterException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UserNotAuthorizedException,
                                                                                         UnrecognizedGUIDException,
                                                                                         FunctionNotSupportedException,
                                                                                         RelationshipNotDeletedException,
                                                                                         UnexpectedResponseException {
        final String methodName = "deleteReplacementTermRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        ReplacementTerm gotReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotReplacementTerm;
    }


    /**
     * Purge a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    RelationshipNotPurgedException,
                                                                                    UnrecognizedGUIDException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UnexpectedResponseException {
        final String methodName = "purgeReplacementTermRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a replacement term relationship
     * <p>
     * Restore allows the deleted replacement term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the replacement term relationship to delete
     * @return response which when successful contains the restored replacement term relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public ReplacementTerm restoreReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException {
        final String methodName = "restoreReplacementTermRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        ReplacementTerm gotReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotReplacementTerm;
    }

    /**
     * Create a TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     *
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the TypedBy relationship
     * @return the created termTYPEDBYRelationship relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TypedBy createTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createTermTYPEDBYRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TYPED_BY_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termTYPEDBYRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        TypedBy createdTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermTYPEDBYRelationship;
    }

    /**
     * Get a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termTYPEDBY relationship to get
     * @return TypedBy
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TypedBy getTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException {
        final String methodName = "getTermTYPEDBYRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TYPED_BY_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        TypedBy gotTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermTYPEDBYRelationship;
    }

    /**
     * Update a TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the TypedBy relationship
     * @return TypedBy updated termTYPEDBYRelationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TypedBy updateTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException,
                                                                                                        MetadataServerUncontactableException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        UnexpectedResponseException,
                                                                                                        UnrecognizedGUIDException {
        final String methodName = "updateTermTYPEDBYRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termTYPEDBYRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_TYPED_BY_URL, requestBody, false);
        TypedBy updatedTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermTYPEDBYRelationship;
    }

    /**
     * Replace an TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the termTYPEDBYRelationship relationship
     * @return TypedBy replaced termTYPEDBYRelationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TypedBy replaceTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException,
                                                                                                         MetadataServerUncontactableException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         UnexpectedResponseException,
                                                                                                         UnrecognizedGUIDException {
        final String methodName = "updateTermTYPEDBYRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termTYPEDBYRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_TYPED_BY_URL, requestBody, true);
        TypedBy updatedTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermTYPEDBYRelationship;
    }

    /**
     * Delete a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the is a type of relationship to delete
     * @return deleted TypedBy
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TypedBy deleteTermTYPEDBYRelationship(String userId, String guid) throws
                                                                             InvalidParameterException,
                                                                             MetadataServerUncontactableException,
                                                                             UserNotAuthorizedException,
                                                                             UnrecognizedGUIDException,
                                                                             FunctionNotSupportedException,
                                                                             RelationshipNotDeletedException,
                                                                             UnexpectedResponseException {
        final String methodName = "deleteTermTYPEDBYRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TYPED_BY_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        TypedBy gotTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermTYPEDBYRelationship;
    }

    /**
     * Purge a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TypedBy relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                RelationshipNotPurgedException,
                                                                                UnrecognizedGUIDException,
                                                                                MetadataServerUncontactableException,
                                                                                UnexpectedResponseException {
        final String methodName = "purgeTermTYPEDBYRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TYPED_BY_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a typed by relationship
     * <p>
     * Restore allows the deleted typed by relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the typed by relationship to delete
     * @return response which when successful contains the restored typed by relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public TypedBy restoreTypedByRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException {
        final String methodName = "restoreeTermTYPEDBYRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TYPED_BY_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        TypedBy gotTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermTYPEDBYRelationship;
    }

    /**
     * Create a Isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * <p>
     *
     * @param userId userId under which the request is performed
     * @param isa    the Isa relationship
     * @return the created isa relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Isa createIsaRelationship(String userId, Isa isa) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createIsaRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(isa);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        Isa createdIsa = DetectUtils.detectAndReturnISARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdIsa;
    }

    /**
     * Get a isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the isa relationship to get
     * @return Isa
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Isa getIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                     MetadataServerUncontactableException,
                                                                     UserNotAuthorizedException,
                                                                     UnexpectedResponseException,
                                                                     UnrecognizedGUIDException {
        final String methodName = "getIsaRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        Isa gotIsa = DetectUtils.detectAndReturnISARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotIsa;
    }

    /**
     * Update a Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param isaRelationship the Isa relationship
     * @return Isa updated isa
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Isa updateIsaRelationship(String userId, Isa isaRelationship) throws InvalidParameterException,
                                                                                MetadataServerUncontactableException,
                                                                                UserNotAuthorizedException,
                                                                                UnexpectedResponseException,
                                                                                UnrecognizedGUIDException {
        final String methodName = "updateIsaRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(isaRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_IS_A_URL, requestBody, false);
        Isa updatedIsaRelationship = DetectUtils.detectAndReturnISARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedIsaRelationship;
    }

    /**
     * Replace an Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param isaRelationship the isa relationship
     * @return Isa replaced isa
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Isa replaceIsaRelationship(String userId, Isa isaRelationship) throws InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException {
        final String methodName = "updateIsaRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(isaRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_IS_A_URL, requestBody, true);
        Isa updatedIsaRelationship = DetectUtils.detectAndReturnISARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedIsaRelationship;
    }

    /**
     * Delete a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the isa relationship to delete
     * @return deleted Isa
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Isa deleteIsaRelationship(String userId, String guid) throws
                                                                 InvalidParameterException,
                                                                 MetadataServerUncontactableException,
                                                                 UserNotAuthorizedException,
                                                                 UnrecognizedGUIDException,
                                                                 FunctionNotSupportedException,
                                                                 RelationshipNotDeletedException,
                                                                 UnexpectedResponseException {
        final String methodName = "deleteIsaRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        Isa gotIsa = DetectUtils.detectAndReturnISARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotIsa;
    }


    /**
     * Purge a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Isa relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        RelationshipNotPurgedException,
                                                                        UnrecognizedGUIDException,
                                                                        MetadataServerUncontactableException,
                                                                        UnexpectedResponseException {
        final String methodName = "purgeIsaRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore an is a relationship
     * <p>
     * Restore allows the deleted is a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the is a relationship to delete
     * @return response which when successful contains the restored is a relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Isa restoreIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         MetadataServerUncontactableException,
                                                                         UnexpectedResponseException,
                                                                         UnrecognizedGUIDException {
        final String methodName = "restoreIsaRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        Isa gotIsa = DetectUtils.detectAndReturnISARelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotIsa;
    }

    /**
     * Create a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     *
     * <p>
     *
     * @param userId                    userId under which the request is performed
     * @param TermISATypeOFRelationship the IsaTypeOf relationship
     * @return the created IsaTypeOf relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public IsaTypeOf createTermISATypeOFRelationship(String userId, IsaTypeOf TermISATypeOFRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createTermISATypeOFRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(TermISATypeOFRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        IsaTypeOf createdTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermISATypeOFRelationship;
    }

    /**
     * Get a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship to get
     * @return IsaTypeOf
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public IsaTypeOf getTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "getTermISATypeOFRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        IsaTypeOf gotTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermISATypeOFRelationship;
    }

    /**
     * Update a isATypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     * @param userId    userId under which the request is performed
     * @param isATypeOf the isATypeOf relationship
     * @return isATypeOf updated isATypeOf
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public IsaTypeOf updateTermISATypeOFRelationship(String userId, IsaTypeOf isATypeOf) throws InvalidParameterException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UserNotAuthorizedException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException {
        final String methodName = "updateTermISATypeOFRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(isATypeOf);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL, requestBody, false);
        IsaTypeOf updatedTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermISATypeOFRelationship;
    }

    /**
     * Replace an isATypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     * @param userId    userId under which the request is performed
     * @param isATypeOf the isATypeOf relationship
     * @return isATypeOf replaced isATypeOf
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public IsaTypeOf replaceTermISATypeOFRelationship(String userId, IsaTypeOf isATypeOf) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException {
        final String methodName = "updateTermISATypeOFRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(isATypeOf);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL, requestBody, true);
        IsaTypeOf updatedTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermISATypeOFRelationship;
    }

    /**
     * Delete a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the is a type of relationship to delete
     * @return deleted IsaTypeOf
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public IsaTypeOf deleteTermISATypeOFRelationship(String userId, String guid) throws
                                                                                 InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnrecognizedGUIDException,
                                                                                 FunctionNotSupportedException,
                                                                                 RelationshipNotDeletedException,
                                                                                 UnexpectedResponseException {
        final String methodName = "deleteTermISATypeOFRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        IsaTypeOf gotTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermISATypeOFRelationship;
    }

    /**
     * Purge a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  RelationshipNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException {
        final String methodName = "purgeTermISATypeOFRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore an is a type of relationship
     * <p>
     * Restore allows the deleted is a type of relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the is a type of relationship to delete
     * @return response which when successful contains the restored is a type of relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public IsaTypeOf restoreIsaTypeOfRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException {
        final String methodName = "restoreTermISATypeOFRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        IsaTypeOf gotTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermISATypeOFRelationship;
    }

    private SubjectAreaOMASAPIResponse getRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnrecognizedGUIDException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = base_url + "/%s";
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        String url = String.format(urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        return restResponse;
    }

    private SubjectAreaOMASAPIResponse deleteRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, FunctionNotSupportedException, RelationshipNotDeletedException, UnrecognizedGUIDException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = base_url + "/%s?isPurge=false";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        DetectUtils.detectAndThrowRelationshipNotDeletedException(restResponse);
        return restResponse;
    }

    private SubjectAreaOMASAPIResponse restoreRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnrecognizedGUIDException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = base_url + "/%s";
        String url = String.format(urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePostNoBody(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        return restResponse;
    }

    private void purgeRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, RelationshipNotPurgedException, UnexpectedResponseException, UnrecognizedGUIDException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = base_url + "/%s?isPurge=true";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowRelationshipNotPurgedException(restResponse);
        DetectUtils.detectVoid(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Create a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the created term categorization relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Categorization createTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                    UnexpectedResponseException,
                                                                                                                                    UnrecognizedGUIDException {

        final String methodName = "createTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termCategorizationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        Categorization createdTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermCategorizationRelationship;

    }

    /**
     * Get a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to get
     * @return TermCategorizationRelationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Categorization getTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UserNotAuthorizedException,
                                                                                               UnexpectedResponseException,
                                                                                               UnrecognizedGUIDException {
        final String methodName = "getTermCategorizationRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        Categorization gotTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermCategorizationRelationship;
    }

    /**
     * Update a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the updated term categorization relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Categorization updateTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "updateTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termCategorizationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL, requestBody, false);
        Categorization updatedTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermCategorizationRelationship;
    }

    /**
     * Replace a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the replaced term categorization relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Categorization replaceTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                     UnexpectedResponseException,
                                                                                                                                     UnrecognizedGUIDException {
        final String methodName = "replaceTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termCategorizationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL, requestBody, true);
        Categorization updatedTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermCategorizationRelationship;
    }

    /**
     * Delete a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.      * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * @return Deleted TermCategorizationRelationship
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Categorization deleteTermCategorizationRelationship(String userId, String guid) throws
                                                                                           InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UserNotAuthorizedException,
                                                                                           UnrecognizedGUIDException,
                                                                                           FunctionNotSupportedException,
                                                                                           RelationshipNotDeletedException,
                                                                                           UnexpectedResponseException {
        final String methodName = "deleteTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        Categorization gotTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermCategorizationRelationship;
    }

    /**
     * Purge a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       RelationshipNotPurgedException,
                                                                                       UnrecognizedGUIDException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UnexpectedResponseException {
        final String methodName = "purgeTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     * Restore allows the deleted has a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the has a relationship to delete
     * @return response which when successful contains the restored has a relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Categorization restoreTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   MetadataServerUncontactableException,
                                                                                                   UnexpectedResponseException,
                                                                                                   UnrecognizedGUIDException {
        final String methodName = "restoreTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        Categorization gotTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermCategorizationRelationship;
    }

    /**
     * Create a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * This method does not error if the relationship ends are not spine objects or spine attributes.
     * Terms created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Terms that have not been created via the Subject Area OMAS.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term anchor relationship
     * @return the created term anchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchor createTermAnchorRelationship(String userId, TermAnchor termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    MetadataServerUncontactableException,
                                                                                                                    UnexpectedResponseException,
                                                                                                                    UnrecognizedGUIDException {

        final String methodName = "createTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termCategorizationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        TermAnchor createdTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermAnchorRelationship;

    }

    /**
     * Get a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to get
     * @return TermAnchorRelationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchor getTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UserNotAuthorizedException,
                                                                                   UnexpectedResponseException,
                                                                                   UnrecognizedGUIDException {
        final String methodName = "getTermAnchorRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        TermAnchor gotTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermAnchorRelationship;
    }

    /**
     * Replace a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term anchor relationship
     * @return the replaced term anchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchor replaceTermAnchorRelationship(String userId, TermAnchor termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException {
        final String methodName = "replaceTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termCategorizationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_TERM_ANCHOR_URL, requestBody, true);
        TermAnchor updatedTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermAnchorRelationship;
    }

    /**
     * Delete a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.      * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * @return Deleted TermAnchorRelationship
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchor deleteTermAnchorRelationship(String userId, String guid) throws
                                                                               InvalidParameterException,
                                                                               MetadataServerUncontactableException,
                                                                               UserNotAuthorizedException,
                                                                               UnrecognizedGUIDException,
                                                                               FunctionNotSupportedException,
                                                                               RelationshipNotDeletedException,
                                                                               UnexpectedResponseException {
        final String methodName = "deleteTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        TermAnchor gotTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermAnchorRelationship;
    }

    /**
     * Purge a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               RelationshipNotPurgedException,
                                                                               UnrecognizedGUIDException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException {
        final String methodName = "purgeTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by Glossaries.
     * <p>
     * Restore allows the deleted has a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Term Anchor relationship to delete
     * @return response which when successful contains the restored has a relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public TermAnchor restoreTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UnexpectedResponseException,
                                                                                       UnrecognizedGUIDException {
        final String methodName = "restoreTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        TermAnchor gotTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermAnchorRelationship;
    }

    /**
     * Create a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categories to be owned by a glossary.
     * Categories created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Category. This method is to allow glossaries to be associated with Categories that have not been created via the Subject Area OMAS.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the category anchor relationship
     * @return the created category anchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchor createCategoryAnchorRelationship(String userId, CategoryAnchor termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                MetadataServerUncontactableException,
                                                                                                                                UnexpectedResponseException,
                                                                                                                                UnrecognizedGUIDException {

        final String methodName = "createCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termCategorizationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        CategoryAnchor createdCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdCategoryAnchorRelationship;

    }

    /**
     * Get a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categories to be owned by a glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to get
     * @return CategoryAnchorRelationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchor getCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UserNotAuthorizedException,
                                                                                           UnexpectedResponseException,
                                                                                           UnrecognizedGUIDException {
        final String methodName = "getCategoryAnchorRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        CategoryAnchor gotCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotCategoryAnchorRelationship;
    }

    /**
     * Update a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categories to be owned by a glossary.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the Category Anchor relationship
     * @return the updated Category Anchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchor updateCategoryAnchorRelationship(String userId, CategoryAnchor termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                MetadataServerUncontactableException,
                                                                                                                                UnexpectedResponseException,
                                                                                                                                UnrecognizedGUIDException {
        final String methodName = "updateCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termCategorizationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL, requestBody, false);
        CategoryAnchor updatedCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedCategoryAnchorRelationship;
    }

    /**
     * Replace a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categories to be owned by a glossary.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the Category Anchor relationship
     * @return the replaced Category Anchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchor replaceCategoryAnchorRelationship(String userId, CategoryAnchor termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 MetadataServerUncontactableException,
                                                                                                                                 UnexpectedResponseException,
                                                                                                                                 UnrecognizedGUIDException {
        final String methodName = "replaceCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termCategorizationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL, requestBody, true);
        CategoryAnchor updatedCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedCategoryAnchorRelationship;
    }

    /**
     * Delete a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categories to be owned by a glossary.      * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * @return Deleted CategoryAnchorRelationship
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchor deleteCategoryAnchorRelationship(String userId, String guid) throws
                                                                                       InvalidParameterException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UserNotAuthorizedException,
                                                                                       UnrecognizedGUIDException,
                                                                                       FunctionNotSupportedException,
                                                                                       RelationshipNotDeletedException,
                                                                                       UnexpectedResponseException {
        final String methodName = "deleteCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        CategoryAnchor gotCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotCategoryAnchorRelationship;
    }

    /**
     * Purge a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categories to be owned by a glossary.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   RelationshipNotPurgedException,
                                                                                   UnrecognizedGUIDException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UnexpectedResponseException {
        final String methodName = "purgeCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categories to be owned by a glossary.
     * <p>
     * Restore allows the deleted has a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the has a relationship to delete
     * @return response which when successful contains the restored has a relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public CategoryAnchor restoreCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UnexpectedResponseException,
                                                                                               UnrecognizedGUIDException {
        final String methodName = "restoreCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        CategoryAnchor gotCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotCategoryAnchorRelationship;
    }

    /**
     * Create a ProjectScope relationship. A link between the project content and the project.
     * <p>
     *
     * @param userId       userId under which the request is performed
     * @param projectScope the ProjectScope relationship
     * @return the created ProjectScope relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScope createProjectScopeRelationship(String userId, ProjectScope projectScope) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "createProjectScopeRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_PROJECT_SCOPE_URL;
        String url = String.format(urlTemplate, serverName, userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(projectScope);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);

        ProjectScope createdProjectScope = DetectUtils.detectAndReturnProjectScope(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdProjectScope;
    }

    /**
     * Get a ProjectScope relationship. A link between the project content and the project.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return ProjectScope
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScope getProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UserNotAuthorizedException,
                                                                                       UnexpectedResponseException,
                                                                                       UnrecognizedGUIDException {
        final String methodName = "getProjectScopeRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_PROJECT_SCOPE_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        ProjectScope gotProjectScope = DetectUtils.detectAndReturnProjectScope(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotProjectScope;
    }

    /**
     * Update a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param userId                   userId under which the request is performed
     * @param projectScopeRelationship the ProjectScope relationship
     * @return updated ProjectScope relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScope updateProjectScopeRelationship(String userId, ProjectScope projectScopeRelationship) throws InvalidParameterException,
                                                                                                                    MetadataServerUncontactableException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    UnexpectedResponseException,
                                                                                                                    UnrecognizedGUIDException {
        final String methodName = "updateProjectScopeRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(projectScopeRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_PROJECT_SCOPE_URL, requestBody, false);
        ProjectScope updatedProjectScopeRelationship = DetectUtils.detectAndReturnProjectScope(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedProjectScopeRelationship;
    }

    /**
     * Replace a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param userId                   userId under which the request is performed
     * @param projectScopeRelationship the ProjectScope relationship
     * @return replaced ProjectScope relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScope replaceProjectScopeRelationship(String userId, ProjectScope projectScopeRelationship) throws InvalidParameterException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException {
        final String methodName = "updateProjectScopeRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(projectScopeRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(userId, this.omasServerURL + BASE_RELATIONSHIPS_PROJECT_SCOPE_URL, requestBody, true);
        ProjectScope updatedProjectScopeRelationship = DetectUtils.detectAndReturnProjectScope(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedProjectScopeRelationship;
    }

    /**
     * Delete a ProjectScope relationship. A link between the project content and the project.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted ProjectScope
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScope deleteProjectScopeRelationship(String userId, String guid) throws
                                                                                   InvalidParameterException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UnrecognizedGUIDException,
                                                                                   UserNotAuthorizedException,
                                                                                   FunctionNotSupportedException,
                                                                                   RelationshipNotDeletedException,
                                                                                   UnexpectedResponseException {
        final String methodName = "deleteProjectScopeRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_PROJECT_SCOPE_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName, url);
        ProjectScope gotProjectScope = DetectUtils.detectAndReturnProjectScope(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotProjectScope;
    }


    /**
     * Purge a ProjectScope relationship. A link between the project content and the project.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to delete
     *               when not successful the following Exception responses can occur
     *               Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 RelationshipNotPurgedException,
                                                                                 UnrecognizedGUIDException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException {
        final String methodName = "purgeProjectScopeRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_PROJECT_SCOPE_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Restore a ProjectScope relationship which  is a link between the project content and the project.
     * <p>
     * Restore allows the deleted ProjectScope relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to restore
     * @return response which when successful contains the restored ProjectScope relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScope restoreProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UnexpectedResponseException,
                                                                                           UnrecognizedGUIDException {
        final String methodName = "restoreProjectScopeRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_PROJECT_SCOPE_URL;
        SubjectAreaOMASAPIResponse restResponse = restoreRelationship(userId, guid, methodName, url);
        ProjectScope gotProjectScope = DetectUtils.detectAndReturnProjectScope(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotProjectScope;
    }

    /**
     * Get a SemanticAssignment relationship,  Links a glossary term to another element such as an asset or schema element to define its meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the SemanticAssignment relationship to get
     * @return the SemanticAssignment relationship with the requested guid
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public SemanticAssignment getSemanticAssignmentRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                   MetadataServerUncontactableException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   UnexpectedResponseException,
                                                                                                   UnrecognizedGUIDException {
        final String methodName = "getSemanticAssignmentRelationship";
        final String url = this.omasServerURL + BASE_RELATIONSHIPS_SEMANTIC_ASSIGNMENT_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName, url);
        SemanticAssignment gotSemanticAssignmentRelationship = DetectUtils.detectAndReturnSemanticAssignmentRelationship(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotSemanticAssignmentRelationship;
    }

    /**
     * Update Relationship.
     *
     * @param userId      userId under which the request is performed
     * @param baseUrl     omasServerUrl to build the rest call on
     * @param requestBody requestBody String representation of the relationship
     * @param isReplace   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated term.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    private SubjectAreaOMASAPIResponse updateRelationship(String userId, String baseUrl, String requestBody, boolean isReplace) throws UserNotAuthorizedException, InvalidParameterException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        final String methodName = "updateRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String urlTemplate = baseUrl + "?isReplace=%b";
        String url = String.format(urlTemplate, serverName, userId, isReplace);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePut(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return restResponse;
    }
}
