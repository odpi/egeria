/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.RestCaller;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides term term authoring interface for subject area experts.
 */
public class SubjectAreaTermImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermImpl.class);

    private static final String className = SubjectAreaTermImpl.class.getName();
    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL +"terms";

    /*
     * The URL of the server where OMAS is active
     */
    private String                    omasServerURL = null;
    /*
     * serverName is a name that picks out a specific named running instance on the server.
     */
    private String serverName;


    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL unique id for the connector instance
     */
    public SubjectAreaTermImpl(String   omasServerURL, String serverName)
    {
        /*
         * Save OMAS Server URL
         */
        this.omasServerURL = omasServerURL;
        this.serverName = serverName;
    }


    /**
     * Create a Term
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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

    public Term createTerm(String serverName, String userId, Term suppliedTerm) throws
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
        final String url = this.omasServerURL + String.format(BASE_URL,serverName,userId);
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
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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

    public  Term getTermByGuid(String serverName, String userId, String guid) throws
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
        String url = String.format(urlTemplate,serverName,userId,guid);
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
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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
    public Term replaceTerm(String serverName, String userId, String guid, Term suppliedTerm) throws
                                                                                              UnexpectedResponseException,
                                                                                              UserNotAuthorizedException,
                                                                                              FunctionNotSupportedException,
                                                                                              InvalidParameterException,
                                                                                              MetadataServerUncontactableException {
        final String methodName = "replaceTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }

        Term term = updateTerm(serverName,userId,guid,suppliedTerm,true);
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
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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
    public Term updateTerm(String serverName, String userId, String guid, Term suppliedTerm) throws UnexpectedResponseException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    InvalidParameterException,
                                                                                                    MetadataServerUncontactableException {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }
        Term term = updateTerm(serverName,userId,guid,suppliedTerm,false);
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
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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

    public Term deleteTerm(String serverName, String userId,String guid) throws InvalidParameterException,
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
        String url = String.format(urlTemplate,serverName,userId,guid);

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
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId userId under which the request is performed
     * @param guid guid of the term to be deleted.
     *
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws GUIDNotPurgedException a hard delete was issued but the term was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server     */

    public  void purgeTerm(String serverName, String userId,String guid) throws InvalidParameterException,
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
        String url = String.format(urlTemplate,serverName,userId,guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowGUIDNotPurgedException(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /*
   *  Update Term.
   *
   * If the caller has chosen to incorporate the term name in their Term Terms qualified name, renaming the term will cause those
   * qualified names to mismatch the Term name.
   * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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
    private Term updateTerm(String serverName, String userId,String guid,Term suppliedTerm,boolean isReplace) throws
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
        String url = String.format(urlTemplate,serverName,userId,guid,isReplace);
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

}
