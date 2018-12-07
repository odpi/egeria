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
public class SubjectAreaRelationshipImpl implements SubjectAreaRelationship
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRelationshipImpl.class);

    private static final String className = SubjectAreaRelationshipImpl.class.getName();
    private static final String BASE_RELATIONSHIPS_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL +"relationships";
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
    private static final String SEMANTIC_ASSIGNMENT = "/semantic-assignments";

    // urls to use when creating types of relationships
    private static final String BASE_RELATIONSHIPS_HASA_URL = BASE_RELATIONSHIPS_URL + HASA;
    private static final String BASE_RELATIONSHIPS_RELATEDTERM_URL = BASE_RELATIONSHIPS_URL + RELATED_TERM;
    private static final String BASE_RELATIONSHIPS_SYNONYM_URL = BASE_RELATIONSHIPS_URL + SYNONYM;
    private static final String BASE_RELATIONSHIPS_ANTONYM_URL = BASE_RELATIONSHIPS_URL + ANTONYM;
    private static final String BASE_RELATIONSHIPS_TRANSLATION_URL = BASE_RELATIONSHIPS_URL +TRANSLATION;
    private static final String BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL = BASE_RELATIONSHIPS_URL +USED_IN_CONTEXT;
    private static final String BASE_RELATIONSHIPS_PREFERRED_TERM_URL = BASE_RELATIONSHIPS_URL +PREFERRED_TERM;
    private static final String BASE_RELATIONSHIPS_VALID_VALUE_URL = BASE_RELATIONSHIPS_URL +VALID_VALUE;
    private static final String BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL = BASE_RELATIONSHIPS_URL +REPLACEMENT_TERM;
    private static final String BASE_RELATIONSHIPS_TYPED_BY_URL = BASE_RELATIONSHIPS_URL +TYPED_BY;
    private static final String BASE_RELATIONSHIPS_IS_A_URL = BASE_RELATIONSHIPS_URL +IS_A;
    private static final String BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL = BASE_RELATIONSHIPS_URL +IS_A_TYPE_OF;
    private static final String BASE_RELATIONSHIPS_SEMANTIC_ASSIGNMENT_URL = BASE_RELATIONSHIPS_URL +SEMANTIC_ASSIGNMENT;

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
    public SubjectAreaRelationshipImpl(String   omasServerURL, String serverName)
    {
        /*
         * Save OMAS Server URL
         */
        this.omasServerURL = omasServerURL;
        this.serverName = serverName;
    }

    /**
     * Create a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the created term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermHASARelationship createTermHASARelationship(String serverName, String userId, TermHASARelationship termHASARelationship) throws InvalidParameterException,
                                                                                                                                               UserNotAuthorizedException,
                                                                                                                                               MetadataServerUncontactableException,
                                                                                                                                               UnexpectedResponseException,
                                                                                                                                               UnrecognizedGUIDException
    {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_HASA_URL;
        String url = String.format(urlTemplate,serverName,userId);
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
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        TermHASARelationship createdTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdTermHASARelationship;

    }
    /**
     * Get a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to get
     * @return TermHASARelationship
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public TermHASARelationship getTermHASARelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UserNotAuthorizedException,
                                                                                           UnexpectedResponseException,
                                                                                           UnrecognizedGUIDException
    {
        final String methodName = "getTermHASARelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_HASA_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,urlTemplate);
        TermHASARelationship gotTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotTermHASARelationship;
    }
    /**
     * Update a Term HASA Relationship.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the created term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermHASARelationship updateTermHASARelationship(String serverName, String userId, TermHASARelationship termHASARelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "updateTermHASARelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termHASARelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_HASA_URL,requestBody,false);
        TermHASARelationship updatedTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedTermHASARelationship;
    }
    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the created term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermHASARelationship replaceTermHASARelationship(String serverName, String userId, TermHASARelationship termHASARelationship) throws InvalidParameterException,
                                                                                                                                                UserNotAuthorizedException,
                                                                                                                                                MetadataServerUncontactableException,
                                                                                                                                                UnexpectedResponseException,
                                                                                                                                                UnrecognizedGUIDException
    {
        final String methodName = "replaceTermHASARelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termHASARelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_HASA_URL,requestBody,true);
        TermHASARelationship updatedTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedTermHASARelationship;
    }

    /**
     * Delete a Term HASA Relationship. A relationship between a spine object and a spine attribute.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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
    public TermHASARelationship deleteTermHASARelationship(String serverName, String userId,String guid) throws
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
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,urlTemplate);
        TermHASARelationship termHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return termHASARelationship;
    }


    /**
     * Purge a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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
    public void purgeTermHASARelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             GUIDNotPurgedException,
                                                                             UnrecognizedGUIDException,
                                                                             MetadataServerUncontactableException,
                                                                             UnexpectedResponseException
    {
        final String methodName = "purgeTermHASARelationship";
        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_HASA_URL;
        purgeRelationship(serverName, userId, guid, methodName,urlTemplate);
    }


    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param relatedTermRelationship the RelatedTerm relationship
     * @return the created RelatedTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public RelatedTerm createRelatedTerm(String serverName, String userId, RelatedTerm relatedTermRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        final String urlTemplate = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        String url = String.format(urlTemplate,serverName,userId);
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
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        RelatedTerm createdRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdRelatedTerm;
    }
    /**
     * Get a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return RelatedTerm
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public RelatedTerm getRelatedTerm(String serverName, String userId, String guid) throws InvalidParameterException,
                                                                                            MetadataServerUncontactableException,
                                                                                            UserNotAuthorizedException,
                                                                                            UnexpectedResponseException,
                                                                                            UnrecognizedGUIDException
    {
        final String methodName = "getRelatedTerm";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotRelatedTerm;
    }
    /**
     * Update a RelatedTerm Relationship.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termRelatedTerm the RelatedTerm relationship
     * @return the created term RelatedTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public RelatedTerm updateRelatedTerm(String serverName, String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "updateRelatedTerm";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termRelatedTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL,requestBody,false);
        RelatedTerm relatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return relatedTermRelationship;
    }
    /**
     * Replace a RelatedTerm Relationship.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termRelatedTerm the RelatedTerm relationship
     * @return the created term RelatedTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public RelatedTerm replaceRelatedTerm(String serverName, String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                MetadataServerUncontactableException,
                                                                                                                UnexpectedResponseException,
                                                                                                                UnrecognizedGUIDException
    {
        final String methodName = "updateTermRelatedRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termRelatedTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL,requestBody,true);
        RelatedTerm relatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return relatedTermRelationship;
    }
    /**
     * Delete a RelatedTerm. A Related Term is a link between two similar Terms.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted RelatedTerm
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
    public RelatedTerm deleteRelatedTerm(String serverName, String userId, String guid) throws
                                                                                        InvalidParameterException,
                                                                                        MetadataServerUncontactableException,
                                                                                        UserNotAuthorizedException,
                                                                                        UnrecognizedGUIDException,
                                                                                        FunctionNotSupportedException,
                                                                                        RelationshipNotDeletedException,
                                                                                        UnexpectedResponseException
    {
        final String methodName = "deleteRelatedTerm";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotRelatedTerm;
    }


    /**
     * Purge a RelatedTerm. A Related Term is a link between two similar Terms.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
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
    public void purgeRelatedTerm(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    GUIDNotPurgedException,
                                                                    UnrecognizedGUIDException,
                                                                    MetadataServerUncontactableException,
                                                                    UnexpectedResponseException
    {
        final String methodName = "purgeRelatedTerm";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a synonym relationship. A link between glossary terms that have the same meaning.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param synonym the Synonym relationship
     * @return the created Synonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Synonym createSynonymRelationship(String serverName, String userId, Synonym synonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "createSynonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        String url = String.format(urlTemplate,serverName,userId);
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
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        Synonym createdSynonym = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdSynonym;
    }
    /**
     *  Get a synonym relationship. A link between glossary terms that have the same meaning.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return Synonym
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public Synonym getSynonymRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                             MetadataServerUncontactableException,
                                                                             UserNotAuthorizedException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException
    {
        final String methodName = "getSynonymRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotSynonym;
    }
    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param synonymRelationship the Synonym relationship
     * @return updated Synonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Synonym updateSynonymRelationship(String serverName, String userId, Synonym synonymRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_SYNONYM_URL,requestBody,false);
        Synonym updatedSynonymRelationship = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedSynonymRelationship;
    }
    /**
     * Replace a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param synonymRelationship the Synonym relationship
     * @return replaced synonym relationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Synonym replaceSynonymRelationship(String serverName, String userId, Synonym synonymRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_SYNONYM_URL,requestBody,true);
        Synonym updatedSynonymRelationship = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedSynonymRelationship;
    }

    /**
     *  Delete a synonym relationship. A link between glossary terms that have the same meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
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
    public Synonym deleteSynonymRelationship(String serverName, String userId,String guid) throws
                                                                         InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UnrecognizedGUIDException,
                                                                         UserNotAuthorizedException,
                                                                         FunctionNotSupportedException,
                                                                         RelationshipNotDeletedException,
                                                                         UnexpectedResponseException
    {
        final String methodName = "deleteSynonymRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotSynonym;
    }


    /**
     *  Purge a synonym relationship. A link between glossary terms that have the same meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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
    public void purgeSynonymRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            GUIDNotPurgedException,
                                                                            UnrecognizedGUIDException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException
    {
        final String methodName = "purgeSynonymRelationship";
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        purgeRelationship(serverName, userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param antonym the Antonym relationship
     * @return the created antonym relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Antonym createAntonymRelationship(String serverName, String userId, Antonym antonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "createAntonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_ANTONYM_URL;
        String url = String.format(urlTemplate,serverName,userId);
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
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        Antonym createdAntonym = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdAntonym;
    }
    /**
     *  Get a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return Antonym
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public Antonym getAntonymRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                             MetadataServerUncontactableException, UserNotAuthorizedException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException
    {
        final String methodName = "getAntonymRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotAntonym;
    }
    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param antonymRelationship the Antonym relationship
     * @return  Antonym updated antonym
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public Antonym updateAntonymRelationship(String serverName, String userId, Antonym antonymRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL,requestBody,false);
        Antonym updatedAntonymRelationship = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedAntonymRelationship;
    }
    /**
     * Replace an Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param antonymRelationship the antonym relationship
     * @return  Antonym replaced antonym
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Antonym replaceAntonymRelationship(String serverName, String userId, Antonym antonymRelationship)  throws InvalidParameterException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException{
        final String methodName = "updateAntonymRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(antonymRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL,requestBody,true);
        Antonym updatedAntonymRelationship = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedAntonymRelationship;
    }

    /**
     *  Delete a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
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
    public Antonym deleteAntonymRelationship(String serverName, String userId,String guid) throws
                                                                         InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UserNotAuthorizedException,
                                                                         UnrecognizedGUIDException,
                                                                         FunctionNotSupportedException,
                                                                         RelationshipNotDeletedException,
                                                                         UnexpectedResponseException
    {
        final String methodName = "deleteAntonymRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotAntonym;
    }


    /**
     *  Purge a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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
    public void purgeAntonymRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            GUIDNotPurgedException,
                                                                            UnrecognizedGUIDException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException
    {
        final String methodName = "purgeAntonymRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }

    /**
     *  Create a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param translation the Translation relationship
     * @return the created translation relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Translation createTranslationRelationship(String serverName, String userId, Translation translation) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       MetadataServerUncontactableException,
                                                                                                                       UnexpectedResponseException,
                                                                                                                       UnrecognizedGUIDException
    {
        final String methodName = "createTranslationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TRANSLATION_URL;
        String url = String.format(urlTemplate,serverName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(translation);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        Translation createdTranslation = DetectUtils.detectAndReturnTranslation(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdTranslation;
    }
    /**
     *  Get a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return Translation
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public Translation getTranslationRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException
    {
        final String methodName = "getTranslationRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_TRANSLATION_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        Translation gotTranslation = DetectUtils.detectAndReturnTranslation(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotTranslation;
    }
    /**
     * Update a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param translationRelationship the Translation relationship
     * @return  Translation updated translation
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public Translation updateTranslationRelationship(String serverName, String userId, Translation translationRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_TRANSLATION_URL,requestBody,false);
        Translation updatedTranslationRelationship = DetectUtils.detectAndReturnTranslation(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedTranslationRelationship;
    }
    /**
     * Replace an Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param translationRelationship the translation relationship
     * @return  Translation replaced translation
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Translation replaceTranslationRelationship(String serverName, String userId, Translation translationRelationship)  throws InvalidParameterException,
                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     UnexpectedResponseException,
                                                                                                                                     UnrecognizedGUIDException{
        final String methodName = "updateTranslationRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(translationRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_TRANSLATION_URL,requestBody,true);
        Translation updatedTranslationRelationship = DetectUtils.detectAndReturnTranslation(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedTranslationRelationship;
    }

    /**
     *  Delete a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted Translation
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
    public Translation deleteTranslationRelationship(String serverName, String userId,String guid) throws
                                                                                 InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnrecognizedGUIDException,
                                                                                 FunctionNotSupportedException,
                                                                                 RelationshipNotDeletedException,
                                                                                 UnexpectedResponseException
    {
        final String methodName = "deleteTranslationRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_TRANSLATION_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        Translation gotTranslation = DetectUtils.detectAndReturnTranslation(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotTranslation;
    }


    /**
     *  Purge a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
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
    public void purgeTranslationRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                GUIDNotPurgedException,
                                                                                UnrecognizedGUIDException,
                                                                                MetadataServerUncontactableException,
                                                                                UnexpectedResponseException
    {
        final String methodName = "purgeTranslationRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_TRANSLATION_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param usedInContext the UsedInContext relationship
     * @return the created usedInContext relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public UsedInContext createUsedInContextRelationship(String serverName, String userId, UsedInContext usedInContext) throws InvalidParameterException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               MetadataServerUncontactableException,
                                                                                                                               UnexpectedResponseException,
                                                                                                                               UnrecognizedGUIDException
    {
        final String methodName = "createUsedInContextRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        String url = String.format(urlTemplate,serverName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(usedInContext);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        UsedInContext createdUsedInContext = DetectUtils.detectAndReturnUsedInContext(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdUsedInContext;
    }
    /**
     *  Get a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return UsedInContext
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public UsedInContext getUsedInContextRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UserNotAuthorizedException,
                                                                                         UnexpectedResponseException,
                                                                                         UnrecognizedGUIDException
    {
        final String methodName = "getUsedInContextRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        UsedInContext gotUsedInContext = DetectUtils.detectAndReturnUsedInContext(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotUsedInContext;
    }
    /**
     * Update a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param usedInContextRelationship the UsedInContext relationship
     * @return  UsedInContext updated usedInContext
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public UsedInContext updateUsedInContextRelationship(String serverName, String userId, UsedInContext usedInContextRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL,requestBody,false);
        UsedInContext updatedUsedInContextRelationship = DetectUtils.detectAndReturnUsedInContext(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedUsedInContextRelationship;
    }
    /**
     * Replace an UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param usedInContextRelationship the usedInContext relationship
     * @return  UsedInContext replaced usedInContext
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public UsedInContext replaceUsedInContextRelationship(String serverName, String userId, UsedInContext usedInContextRelationship)  throws InvalidParameterException,
                                                                                                                                             MetadataServerUncontactableException,
                                                                                                                                             UserNotAuthorizedException,
                                                                                                                                             UnexpectedResponseException,
                                                                                                                                             UnrecognizedGUIDException{
        final String methodName = "updateUsedInContextRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(usedInContextRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL,requestBody,true);
        UsedInContext updatedUsedInContextRelationship = DetectUtils.detectAndReturnUsedInContext(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedUsedInContextRelationship;
    }

    /**
     *  Delete a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted UsedInContext
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
    public UsedInContext deleteUsedInContextRelationship(String serverName, String userId,String guid) throws
                                                                                     InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnrecognizedGUIDException,
                                                                                     FunctionNotSupportedException,
                                                                                     RelationshipNotDeletedException,
                                                                                     UnexpectedResponseException
    {
        final String methodName = "deleteUsedInContextRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        UsedInContext gotUsedInContext = DetectUtils.detectAndReturnUsedInContext(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotUsedInContext;
    }


    /**
     *  Purge a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to delete
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
    public void purgeUsedInContextRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  GUIDNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException
    {
        final String methodName = "purgeUsedInContextRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param preferredTerm the PreferredTerm relationship
     * @return the created preferredTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public PreferredTerm createPreferredTermRelationship(String serverName, String userId, PreferredTerm preferredTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "createPreferredTermRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        String url = String.format(urlTemplate,serverName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(preferredTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        PreferredTerm createdPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdPreferredTerm;
    }
    /**
     *  Get a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return PreferredTerm
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public PreferredTerm getPreferredTermRelationship(String serverName, String userId,String guid) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "getPreferredTermRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        PreferredTerm gotPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotPreferredTerm;
    }
    /**
     * Update a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param preferredTermRelationship the PreferredTerm relationship
     * @return  PreferredTerm updated preferredTerm
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public PreferredTerm updatePreferredTermRelationship(String serverName, String userId, PreferredTerm preferredTermRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_PREFERRED_TERM_URL,requestBody,false);
        PreferredTerm updatedPreferredTermRelationship = DetectUtils.detectAndReturnPreferredTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedPreferredTermRelationship;
    }
    /**
     * Replace an PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param preferredTermRelationship the preferredTerm relationship
     * @return  PreferredTerm replaced preferredTerm
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public PreferredTerm replacePreferredTermRelationship(String serverName, String userId, PreferredTerm preferredTermRelationship)  throws InvalidParameterException,
                                                                                                                                             MetadataServerUncontactableException,
                                                                                                                                             UserNotAuthorizedException,
                                                                                                                                             UnexpectedResponseException,
                                                                                                                                             UnrecognizedGUIDException{
        final String methodName = "updatePreferredTermRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(preferredTermRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_PREFERRED_TERM_URL,requestBody,true);
        PreferredTerm updatedPreferredTermRelationship = DetectUtils.detectAndReturnPreferredTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedPreferredTermRelationship;
    }

    /**
     *  Delete a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted PreferredTerm
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
    public PreferredTerm deletePreferredTermRelationship(String serverName, String userId,String guid) throws
                                                                                     InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnrecognizedGUIDException,
                                                                                     FunctionNotSupportedException,
                                                                                     RelationshipNotDeletedException,
                                                                                     UnexpectedResponseException
    {
        final String methodName = "deletePreferredTermRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        PreferredTerm gotPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotPreferredTerm;
    }


    /**
     *  Purge a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to delete
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
    public void purgePreferredTermRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  GUIDNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException
    {
        final String methodName = "purgePreferredTermRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param validValue the ValidValue relationship
     * @return the created validValue relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public ValidValue createValidValueRelationship(String serverName, String userId, ValidValue validValue) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   MetadataServerUncontactableException,
                                                                                                                   UnexpectedResponseException,
                                                                                                                   UnrecognizedGUIDException
    {
        final String methodName = "createValidValueRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_VALID_VALUE_URL;
        String url = String.format(urlTemplate,serverName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(validValue);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        ValidValue createdValidValue = DetectUtils.detectAndReturnValidValue(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdValidValue;
    }
    /**
     *  Get a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return ValidValue
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public ValidValue getValidValueRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UserNotAuthorizedException,
                                                                                   UnexpectedResponseException,
                                                                                   UnrecognizedGUIDException
    {
        final String methodName = "getValidValueRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_VALID_VALUE_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        ValidValue gotValidValue = DetectUtils.detectAndReturnValidValue(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotValidValue;
    }
    /**
     * Update a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param validValueRelationship the ValidValue relationship
     * @return  ValidValue updated validValue
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public ValidValue updateValidValueRelationship(String serverName, String userId, ValidValue validValueRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_VALID_VALUE_URL,requestBody,false);
        ValidValue updatedValidValueRelationship = DetectUtils.detectAndReturnValidValue(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedValidValueRelationship;
    }
    /**
     * Replace an ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param validValueRelationship the validValue relationship
     * @return  ValidValue replaced validValue
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public ValidValue replaceValidValueRelationship(String serverName, String userId, ValidValue validValueRelationship)  throws InvalidParameterException,
                                                                                                                                 MetadataServerUncontactableException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 UnexpectedResponseException,
                                                                                                                                 UnrecognizedGUIDException{
        final String methodName = "updateValidValueRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(validValueRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_VALID_VALUE_URL,requestBody,true);
        ValidValue updatedValidValueRelationship = DetectUtils.detectAndReturnValidValue(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedValidValueRelationship;
    }

    /**
     *  Delete a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted ValidValue
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
    public ValidValue deleteValidValueRelationship(String serverName, String userId,String guid) throws
                                                                               InvalidParameterException,
                                                                               MetadataServerUncontactableException,
                                                                               UserNotAuthorizedException,
                                                                               UnrecognizedGUIDException,
                                                                               FunctionNotSupportedException,
                                                                               RelationshipNotDeletedException,
                                                                               UnexpectedResponseException
    {
        final String methodName = "deleteValidValueRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_VALID_VALUE_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        ValidValue gotValidValue = DetectUtils.detectAndReturnValidValue(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotValidValue;
    }


    /**
     *  Purge a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to delete
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
    public void purgeValidValueRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               GUIDNotPurgedException,
                                                                               UnrecognizedGUIDException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException
    {
        final String methodName = "purgeValidValueRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_VALID_VALUE_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param replacementTerm the ReplacementTerm relationship
     * @return the created replacementTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public ReplacementTerm createReplacementTermRelationship(String serverName, String userId, ReplacementTerm replacementTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "createReplacementTermRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        String url = String.format(urlTemplate,serverName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(replacementTerm);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        ReplacementTerm createdReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdReplacementTerm;
    }
    /**
     *  Get a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return ReplacementTerm
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public ReplacementTerm getReplacementTermRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UserNotAuthorizedException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException
    {
        final String methodName = "getReplacementTermRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        ReplacementTerm gotReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotReplacementTerm;
    }
    /**
     * Update a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param replacementTermRelationship the ReplacementTerm relationship
     * @return  ReplacementTerm updated replacementTerm
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public ReplacementTerm updateReplacementTermRelationship(String serverName, String userId, ReplacementTerm replacementTermRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL,requestBody,false);
        ReplacementTerm updatedReplacementTermRelationship = DetectUtils.detectAndReturnReplacementTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedReplacementTermRelationship;
    }
    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param replacementTermRelationship the replacementTerm relationship
     * @return  ReplacementTerm replaced replacementTerm
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public ReplacementTerm replaceReplacementTermRelationship(String serverName, String userId, ReplacementTerm replacementTermRelationship)  throws InvalidParameterException,
                                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                                     UnexpectedResponseException,
                                                                                                                                                     UnrecognizedGUIDException{
        final String methodName = "updateReplacementTermRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(replacementTermRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL,requestBody,true);
        ReplacementTerm updatedReplacementTermRelationship = DetectUtils.detectAndReturnReplacementTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedReplacementTermRelationship;
    }

    /**
     *  Delete a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted ReplacementTerm
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
    public ReplacementTerm deleteReplacementTermRelationship(String serverName, String userId,String guid) throws
                                                                                         InvalidParameterException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UserNotAuthorizedException,
                                                                                         UnrecognizedGUIDException,
                                                                                         FunctionNotSupportedException,
                                                                                         RelationshipNotDeletedException,
                                                                                         UnexpectedResponseException
    {
        final String methodName = "deleteReplacementTermRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        ReplacementTerm gotReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotReplacementTerm;
    }


    /**
     *  Purge a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to delete
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
    public void purgeReplacementTermRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    GUIDNotPurgedException,
                                                                                    UnrecognizedGUIDException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UnexpectedResponseException
    {
        final String methodName = "purgeReplacementTermRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a TermTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationship the TermTYPEDBYRelationship relationship
     * @return the created termTYPEDBYRelationship relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermTYPEDBYRelationship createTermTYPEDBYRelationshipRelationship(String serverName, String userId, TermTYPEDBYRelationship termTYPEDBYRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "createTermTYPEDBYRelationshipRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_TYPED_BY_URL;
        String url = String.format(urlTemplate,serverName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(termTYPEDBYRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        TermTYPEDBYRelationship createdTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdTermTYPEDBYRelationship;
    }
    /**
     *  Get a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return TermTYPEDBYRelationship
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public TermTYPEDBYRelationship getTermTYPEDBYRelationshipRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                                             MetadataServerUncontactableException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             UnexpectedResponseException,
                                                                                                             UnrecognizedGUIDException
    {
        final String methodName = "getTermTYPEDBYRelationshipRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_TYPED_BY_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        TermTYPEDBYRelationship gotTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotTermTYPEDBYRelationship;
    }
    /**
     * Update a TermTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationshipRelationship the TermTYPEDBYRelationship relationship
     * @return  TermTYPEDBYRelationship updated termTYPEDBYRelationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public TermTYPEDBYRelationship updateTermTYPEDBYRelationshipRelationship(String serverName, String userId, TermTYPEDBYRelationship termTYPEDBYRelationshipRelationship)  throws InvalidParameterException,
                                                                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                                                                    UnexpectedResponseException,
                                                                                                                                                                                    UnrecognizedGUIDException {
        final String methodName = "updateTermTYPEDBYRelationshipRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termTYPEDBYRelationshipRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_TYPED_BY_URL,requestBody,false);
        TermTYPEDBYRelationship updatedTermTYPEDBYRelationshipRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedTermTYPEDBYRelationshipRelationship;
    }
    /**
     * Replace an TermTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationshipRelationship the termTYPEDBYRelationship relationship
     * @return  TermTYPEDBYRelationship replaced termTYPEDBYRelationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermTYPEDBYRelationship replaceTermTYPEDBYRelationshipRelationship(String serverName, String userId, TermTYPEDBYRelationship termTYPEDBYRelationshipRelationship)  throws InvalidParameterException,
                                                                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                                                                     UnexpectedResponseException,
                                                                                                                                                                                     UnrecognizedGUIDException{
        final String methodName = "updateTermTYPEDBYRelationshipRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(termTYPEDBYRelationshipRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_TYPED_BY_URL,requestBody,true);
        TermTYPEDBYRelationship updatedTermTYPEDBYRelationshipRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedTermTYPEDBYRelationshipRelationship;
    }

    /**
     *  Delete a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted TermTYPEDBYRelationship
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
    public TermTYPEDBYRelationship deleteTermTYPEDBYRelationshipRelationship(String serverName, String userId,String guid) throws
                                                                                                         InvalidParameterException,
                                                                                                         MetadataServerUncontactableException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         UnrecognizedGUIDException,
                                                                                                         FunctionNotSupportedException,
                                                                                                         RelationshipNotDeletedException,
                                                                                                         UnexpectedResponseException
    {
        final String methodName = "deleteTermTYPEDBYRelationshipRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_TYPED_BY_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        TermTYPEDBYRelationship gotTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotTermTYPEDBYRelationship;
    }


    /**
     *  Purge a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermTYPEDBYRelationship relationship to delete
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
    public void purgeTermTYPEDBYRelationshipRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            GUIDNotPurgedException,
                                                                                            UnrecognizedGUIDException,
                                                                                            MetadataServerUncontactableException,
                                                                                            UnexpectedResponseException
    {
        final String methodName = "purgeTermTYPEDBYRelationshipRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_TYPED_BY_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a Isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param isa the Isa relationship
     * @return the created isa relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public ISARelationship createIsaRelationship(String serverName, String userId, ISARelationship isa) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "createIsaRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_URL;
        String url = String.format(urlTemplate,serverName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(isa);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        ISARelationship createdIsa = DetectUtils.detectAndReturnISARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdIsa;
    }
    /**
     *  Get a isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return Isa
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public ISARelationship getIsaRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException
    {
        final String methodName = "getIsaRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_IS_A_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        ISARelationship gotIsa = DetectUtils.detectAndReturnISARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotIsa;
    }
    /**
     * Update a Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param isaRelationship the Isa relationship
     * @return  Isa updated isa
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public ISARelationship updateIsaRelationship(String serverName, String userId, ISARelationship isaRelationship)  throws InvalidParameterException,
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
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_IS_A_URL,requestBody,false);
        ISARelationship updatedIsaRelationship = DetectUtils.detectAndReturnISARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedIsaRelationship;
    }
    /**
     * Replace an Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param isaRelationship the isa relationship
     * @return  Isa replaced isa
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public ISARelationship replaceIsaRelationship(String serverName, String userId, ISARelationship isaRelationship)  throws InvalidParameterException,
                                                                                                                             MetadataServerUncontactableException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             UnexpectedResponseException,
                                                                                                                             UnrecognizedGUIDException{
        final String methodName = "updateIsaRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(isaRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_IS_A_URL,requestBody,true);
        ISARelationship updatedIsaRelationship = DetectUtils.detectAndReturnISARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedIsaRelationship;
    }

    /**
     *  Delete a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted Isa
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
    public ISARelationship deleteIsaRelationship(String serverName, String userId,String guid) throws
                                                                             InvalidParameterException,
                                                                             MetadataServerUncontactableException,
                                                                             UserNotAuthorizedException,
                                                                             UnrecognizedGUIDException,
                                                                             FunctionNotSupportedException,
                                                                             RelationshipNotDeletedException,
                                                                             UnexpectedResponseException
    {
        final String methodName = "deleteIsaRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_IS_A_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        ISARelationship gotIsa = DetectUtils.detectAndReturnISARelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotIsa;
    }


    /**
     *  Purge a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Isa relationship to delete
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
    public void purgeIsaRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        GUIDNotPurgedException,
                                                                        UnrecognizedGUIDException,
                                                                        MetadataServerUncontactableException,
                                                                        UnexpectedResponseException
    {
        final String methodName = "purgeIsaRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_IS_A_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param TermISATypeOFRelationship the TermISATypeOFRelationship relationship
     * @return the created TermISATypeOFRelationship relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermISATypeOFRelationship createTermISATypeOFRelationshipRelationship(String serverName, String userId, TermISATypeOFRelationship TermISATypeOFRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "createTermISATypeOFRelationshipRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        String urlTemplate = this.omasServerURL + BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        String url = String.format(urlTemplate,serverName,userId);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(TermISATypeOFRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);

        TermISATypeOFRelationship createdTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return createdTermISATypeOFRelationship;
    }
    /**
     *  Get a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return TermISATypeOFRelationship
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public TermISATypeOFRelationship getTermISATypeOFRelationshipRelationship(String serverName, String userId,String guid) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "getTermISATypeOFRelationshipRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(serverName, userId, guid, methodName,url);
        TermISATypeOFRelationship gotTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotTermISATypeOFRelationship;
    }
    /**
     * Update a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param TermISATypeOFRelationshipRelationship the TermISATypeOFRelationship relationship
     * @return  TermISATypeOFRelationship updated TermISATypeOFRelationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    public TermISATypeOFRelationship updateTermISATypeOFRelationshipRelationship(String serverName, String userId, TermISATypeOFRelationship TermISATypeOFRelationshipRelationship)  throws InvalidParameterException,
                                                                                                                                                                                            MetadataServerUncontactableException,
                                                                                                                                                                                            UserNotAuthorizedException,
                                                                                                                                                                                            UnexpectedResponseException,
                                                                                                                                                                                            UnrecognizedGUIDException {
        final String methodName = "updateTermISATypeOFRelationshipRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(TermISATypeOFRelationshipRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL,requestBody,false);
        TermISATypeOFRelationship updatedTermISATypeOFRelationshipRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedTermISATypeOFRelationshipRelationship;
    }
    /**
     * Replace an TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param TermISATypeOFRelationshipRelationship the TermISATypeOFRelationship relationship
     * @return  TermISATypeOFRelationship replaced TermISATypeOFRelationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermISATypeOFRelationship replaceTermISATypeOFRelationshipRelationship(String serverName, String userId, TermISATypeOFRelationship TermISATypeOFRelationshipRelationship)  throws InvalidParameterException,
                                                                                                                                                                                             MetadataServerUncontactableException,
                                                                                                                                                                                             UserNotAuthorizedException,
                                                                                                                                                                                             UnexpectedResponseException,
                                                                                                                                                                                             UnrecognizedGUIDException{
        final String methodName = "updateTermISATypeOFRelationshipRelationship";
        String requestBody = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestBody = mapper.writeValueAsString(TermISATypeOFRelationshipRelationship);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className,methodName,error);
        }
        SubjectAreaOMASAPIResponse restResponse = updateRelationship(serverName, userId,this.omasServerURL +BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL,requestBody,true);
        TermISATypeOFRelationship updatedTermISATypeOFRelationshipRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return updatedTermISATypeOFRelationshipRelationship;
    }

    /**
     *  Delete a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted TermISATypeOFRelationship
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
    public TermISATypeOFRelationship deleteTermISATypeOFRelationshipRelationship(String serverName, String userId,String guid) throws
                                                                                                             InvalidParameterException,
                                                                                                             MetadataServerUncontactableException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             UnrecognizedGUIDException,
                                                                                                             FunctionNotSupportedException,
                                                                                                             RelationshipNotDeletedException,
                                                                                                             UnexpectedResponseException
    {
        final String methodName = "deleteTermISATypeOFRelationshipRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(serverName, userId, guid, methodName,url);
        TermISATypeOFRelationship gotTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotTermISATypeOFRelationship;
    }


    /**
     *  Purge a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermISATypeOFRelationship relationship to delete
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
    public void purgeTermISATypeOFRelationshipRelationship(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              GUIDNotPurgedException,
                                                                                              UnrecognizedGUIDException,
                                                                                              MetadataServerUncontactableException,
                                                                                              UnexpectedResponseException
    {
        final String methodName = "purgeTermISATypeOFRelationshipRelationship";
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        purgeRelationship(serverName, userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }


    private SubjectAreaOMASAPIResponse getRelationship(String serverName, String userId, String guid, String methodName,String base_url ) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnrecognizedGUIDException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = base_url + "/%s";
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        String url = String.format(urlTemplate,serverName,userId,guid);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);
        return restResponse;
    }
    private SubjectAreaOMASAPIResponse deleteRelationship(String serverName, String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, FunctionNotSupportedException, RelationshipNotDeletedException, UnrecognizedGUIDException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        final String urlTemplate = base_url+"/%s?isPurge=false";
        String url = String.format(urlTemplate,serverName,userId,guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse); DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(methodName,restResponse);
        DetectUtils.detectAndThrowRelationshipNotDeletedException(methodName,restResponse);
        return restResponse;
    }
    private void purgeRelationship(String serverName, String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, GUIDNotPurgedException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }

        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        final String urlTemplate = base_url+"/%s?isPurge=true";
        String url = String.format(urlTemplate,serverName,userId,guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse); DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);
        DetectUtils.detectAndThrowGUIDNotPurgedException(methodName,restResponse);
        DetectUtils.detectVoid(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Update Relationship.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId userId under which the request is performed
     * @param baseUrl baseUrl to build the rest call on
     * @param requestBody requestBody String representation of the relationship
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated term.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    private SubjectAreaOMASAPIResponse updateRelationship(String serverName, String userId,String baseUrl,String requestBody,boolean isReplace) throws UserNotAuthorizedException, InvalidParameterException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    {
        final String methodName = "updateRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        final String urlTemplate = baseUrl +"?isReplace=%b";
        String url = String.format(urlTemplate,serverName,userId,isReplace);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePut(className,methodName,requestBody,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(methodName,restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(methodName,restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return restResponse;
    }
}
