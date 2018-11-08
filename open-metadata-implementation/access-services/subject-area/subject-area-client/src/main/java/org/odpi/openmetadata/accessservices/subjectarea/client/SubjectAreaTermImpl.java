/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTermRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermHASARelationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.RestCaller;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides term term authoring interface for subject area experts.
 */
public class SubjectAreaTermImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermImpl.class);

    private static final String className = SubjectAreaTermImpl.class.getName();
    private static final String BASE_URL = "/users/%s/terms";
    private static final String BASE_RELATIONSHIPS_URL = "/users/%s/relationships";
    private static final String HASA = "/hasa";
    private static final String RELATED_TERM = "/relatedterm";
    private static final String SYNONYM = "/synonym";
    private static final String ANTONYM = "/antonym";
    private static final String BASE_RELATIONSHIPS_HASA_URL = BASE_RELATIONSHIPS_URL + HASA;
    private static final String BASE_RELATIONSHIPS_RELATEDTERM_URL = BASE_RELATIONSHIPS_URL + RELATED_TERM;
    private static final String BASE_RELATIONSHIPS_SYNONYM_URL = BASE_RELATIONSHIPS_URL + SYNONYM;
    private static final String BASE_RELATIONSHIPS_ANTONYM_URL = BASE_RELATIONSHIPS_URL + ANTONYM;

    /*
     * The URL of the server where OMAS is active
     */
    private String                    omasServerURL = null;

    /**
     * Default Constructor used once a connector is created.
     *
     * @param omasServerURL - unique id for the connector instance
     */
    public SubjectAreaTermImpl(String   omasServerURL)
    {
        /*
         * Save OMAS Server URL
         */
        this.omasServerURL = omasServerURL;
    }


    /**
     * Create a Term
     * @param userId  userId under which the request is performed
     * @param suppliedTerm Term to create
     * @return the created term.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws ClassificationException Error processing a classification
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public Term createTerm(String userId, Term suppliedTerm) throws
            MetadataServerUncontactableException,
            InvalidParameterException,
            UserNotAuthorizedException,
            ClassificationException,
            FunctionNotSupportedException,
            UnexpectedResponseException {
        final String methodName ="createTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        final String url = this.omasServerURL + String.format(BASE_URL,userId);
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody, url);

        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowClassificationException(methodName,restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(methodName,restResponse);
        Term term = DetectUtils.detectAndReturnTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return term;
    }

    /**
     * Get a term by guid.
     * @param userId userId under which the request is performed
     * @param guid guid of the term to get
     * @return the requested term.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public  Term getTermByGuid( String userId, String guid) throws
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            UnexpectedResponseException {
        final String methodName = "getTermByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");
        final String urlTemplate = this.omasServerURL +BASE_URL + "/%s";
        String url = String.format(urlTemplate,userId,guid);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(methodName,restResponse);
        Term term = DetectUtils.detectAndReturnTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return term;
    }

    /**
     * Replace a Term. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the term to update
     * @param suppliedTerm term to be updated
     * @return replaced term
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Term replaceTerm(String userId, String guid, Term suppliedTerm) throws
            UnexpectedResponseException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            InvalidParameterException,
            MetadataServerUncontactableException {
        final String methodName = "replaceTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }

        Term term = updateTerm(userId,guid,suppliedTerm,true);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
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
     * @param userId           userId under which the request is performed
     * @param guid             guid of the term to update
     * @param suppliedTerm term to be updated
     * @return a response which when successful contains the updated term
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Term updateTerm(String userId, String guid, Term suppliedTerm) throws UnexpectedResponseException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            InvalidParameterException,
            MetadataServerUncontactableException {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }
        Term term = updateTerm(userId,guid,suppliedTerm,false);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return term;

    }
    /**
     *  Update Term.
     *
     * If the caller has chosen to incorporate the term name in their Term Terms qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * @param userId userId under which the request is performed
     * @param guid guid of the term to update
     * @param suppliedTerm Term to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated term.
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
    private Term updateTerm(String userId,String guid,Term suppliedTerm,boolean isReplace) throws
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        final String urlTemplate = this.omasServerURL +BASE_URL +"/%s?isReplace=%b";
        String url = String.format(urlTemplate,userId,guid,isReplace);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePut(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(methodName,restResponse);

        Term term = DetectUtils.detectAndReturnTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return term;
    }


    /**
     * Delete a Term instance
     *
     * A delete (also known as a soft delete) means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the term to be deleted.
     * @return the deleted term
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws EntityNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public Term deleteTerm(String userId,String guid) throws InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnrecognizedGUIDException,
            UnexpectedResponseException,
            EntityNotDeletedException {
        final String methodName = "deleteTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        final String urlTemplate = this.omasServerURL +BASE_URL+"/%s?isPurge=false";
        String url = String.format(urlTemplate,userId,guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(methodName,restResponse);
        DetectUtils.detectAndThrowEntityNotDeletedException(methodName,restResponse);

        Term term = DetectUtils.detectAndReturnTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return term;
    }
    /**
     * Purge a Term instance
     *
     * A purge means that the term will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the term to be deleted.
     *
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws GUIDNotPurgedException a hard delete was issued but the term was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException unable to contact server
     */

    public  void purgeTerm(String userId,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException,
            UnexpectedResponseException {
        final String methodName = "purgeTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        final String urlTemplate = this.omasServerURL +BASE_URL+"/%s?isPurge=false";
        String url = String.format(urlTemplate,userId,guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowGUIDNotPurgedException(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     * Create a Term HASA Relationship is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param termHASARelationship the HASA relationship
     * @return the created term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermHASARelationship createTermHASARelationship(String userId, String termGuid, TermHASARelationship termHASARelationship)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",termguid=" + termGuid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,termGuid,"termGuid");

        final String urlTemplate = this.omasServerURL +BASE_URL+"/%s" + HASA;

        String url = String.format(urlTemplate,userId,termGuid);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termHASARelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);

        TermHASARelationship createdTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdTermHASARelationship;

    }
    /**
     * Get a Term HASA Relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to get
     * @return TermHASARelationship
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public TermHASARelationship getTermHASARelationship( String userId,String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnexpectedResponseException
    {
        final String methodName = "getTermHASARelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_HASA_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName,urlTemplate);
        TermHASARelationship gotTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotTermHASARelationship;
    }

    /**
     * Delete a Term HASA Relationship
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to delete
     * @return Deleted TermHASARelationship
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermHASARelationship deleteTermHASARelationship( String userId,String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            UnrecognizedGUIDException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException
    {
        final String methodName = "deleteTermHASARelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_HASA_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName,urlTemplate);
        TermHASARelationship termHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return termHASARelationship;
    }


    /**
     * Purge a TermHASA Relationship
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeTermHASARelationship( String userId,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "purgeTermHASARelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_HASA_URL;
        purgeRelationship(userId, guid, methodName,urlTemplate);
    }


    /**
     * Create a RelatedTermRelationship relationship
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param relatedTermRelationship the RelatedTermRelationship relationship
     * @return the created RelatedTermRelationship relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public RelatedTermRelationship createRelatedTermRelationship(String userId, String termGuid, RelatedTermRelationship relatedTermRelationship)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",termguid=" + termGuid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,termGuid,"termGuid");

        final String urlTemplate = this.omasServerURL +BASE_URL+"/%s" + RELATED_TERM;
        String url = String.format(urlTemplate,userId,termGuid);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(relatedTermRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);

        RelatedTermRelationship createdRelatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdRelatedTermRelationship;
    }
    /**
     * Get a Related Term Relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to get
     * @return RelatedTermRelationship
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public RelatedTermRelationship getRelatedTermRelationship(String userId, String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnexpectedResponseException
    {
        final String methodName = "getRelatedTermRelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName,urlTemplate);
        RelatedTermRelationship gotRelatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotRelatedTermRelationship;
    }

    /**
     * Delete a RelatedTermRelationship Relationship
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to delete
     * @return deleted RelatedTermRelationship
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public RelatedTermRelationship deleteRelatedTermRelationship(String userId, String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            UnrecognizedGUIDException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException
    {
        final String methodName = "deleteRelatedTermRelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName,urlTemplate);
        RelatedTermRelationship gotRelatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotRelatedTermRelationship;
    }


    /**
     * Purge a RelatedTermRelationship Relationship
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeRelatedTermRelationship( String userId,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "purgeRelatedTermRelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        purgeRelationship(userId, guid, methodName,urlTemplate);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param synonym the Synonym relationship
     * @return the created Synonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Synonym createSynonymRelationship(String userId, String termGuid, Synonym synonym)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "createSynonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",termguid=" + termGuid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,termGuid,"termGuid");

        final String urlTemplate = this.omasServerURL +BASE_URL+"/%s" + SYNONYM;
        String url = String.format(urlTemplate,userId,termGuid);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(synonym);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);

        Synonym createdSynonym = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdSynonym;
    }
    /**
     * Get a Synonym
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to get
     * @return Synonym
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public Synonym getSynonymRelationship( String userId,String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnexpectedResponseException
    {
        final String methodName = "getSynonymRelationship";
        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName,urlTemplate);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotSynonym;
    }

    /**
     * Delete a Synonym Relationship
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to delete
     * @return deleted Synonym
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Synonym deleteSynonymRelationship( String userId,String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UnrecognizedGUIDException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException
    {
        final String methodName = "deleteSynonymRelationship";
        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName,urlTemplate);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotSynonym;
    }


    /**
     * Purge a Synonym Relationship
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeSynonymRelationship( String userId,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "purgeSynonymRelationship";
        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        purgeRelationship(userId, guid, methodName, urlTemplate);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create an antonym relationship, which is a link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param antonym the Antonym relationship
     * @return the created antonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Antonym createAntonymRelationship(String userId, String termGuid, Antonym antonym)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "createAntonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",termguid=" + termGuid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,termGuid,"termGuid");

        final String urlTemplate = this.omasServerURL +BASE_URL+"/%s" + ANTONYM;
        String url = String.format(urlTemplate,userId,termGuid);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(antonym);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);

        Antonym createdAntonym = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdAntonym;
    }
    /**
     * Get a Antonym Relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to get
     * @return Antonym
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public Antonym getAntonymRelationship( String userId,String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnexpectedResponseException
    {
        final String methodName = "getAntonymRelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName,urlTemplate);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotAntonym;
    }

    /**
     * Delete a Antonym Relationship
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to delete
     * @return deleted Antonym
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Antonym deleteAntonymRelationship( String userId,String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            UnrecognizedGUIDException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException
    {
        final String methodName = "deleteAntonyRelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName,urlTemplate);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotAntonym;
    }


    /**
     * Purge a Antonym Relationship
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeAntonymRelationship( String userId,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "purgeAntonymRelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        purgeRelationship(userId, guid, methodName,urlTemplate);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }

    private SubjectAreaOMASAPIResponse getRelationship(String userId, String guid, String methodName,String base_url ) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, FunctionNotSupportedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = base_url + "/%s";
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        String url = String.format(urlTemplate,userId,guid);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(methodName,restResponse);
        return restResponse;
    }
    private SubjectAreaOMASAPIResponse deleteRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, FunctionNotSupportedException, RelationshipNotDeletedException, UnrecognizedGUIDException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        final String urlTemplate = base_url+"/%s?isPurge=false";
        String url = String.format(urlTemplate,userId,guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(methodName,restResponse);
        DetectUtils.detectAndThrowRelationshipNotDeletedException(methodName,restResponse);
        return restResponse;
    }
    private void purgeRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, GUIDNotPurgedException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }

        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        final String urlTemplate = base_url+"/%s?isPurge=true";
        String url = String.format(urlTemplate,userId,guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);
        DetectUtils.detectAndThrowGUIDNotPurgedException(methodName,restResponse);
        DetectUtils.detectVoid(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }

}
