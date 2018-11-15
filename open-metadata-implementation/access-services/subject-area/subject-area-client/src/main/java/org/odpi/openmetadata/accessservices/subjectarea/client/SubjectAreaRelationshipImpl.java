/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
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


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides relationship authoring interface for subject area experts.
 */
public class SubjectAreaRelationshipImpl implements SubjectAreaRelationship
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRelationshipImpl.class);

    private static final String className = SubjectAreaRelationshipImpl.class.getName();
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
    public SubjectAreaRelationshipImpl(String   omasServerURL)
    {
        /*
         * Save OMAS Server URL
         */
        this.omasServerURL = omasServerURL;
    }

    /**
     * Create a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param userId               userId under which the request is performed
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
    public TermHASARelationship createTermHASARelationship(String userId, TermHASARelationship termHASARelationship)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_URL+"/%s" + HASA;
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
     * Get a Term HASA Relationship. A relationship between a spine object and a spine attribute.
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
     * Delete a Term HASA Relationship. A relationship between a spine object and a spine attribute.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
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
     * Purge a Term HASA Relationship. A relationship between a spine object and a spine attribute.
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
     * Create a RelatedTermRelationship. A Related Term is a link between two similar Terms.
     *
     * <p>
     *
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

    public RelatedTermRelationship createRelatedTermRelationship(String userId, RelatedTermRelationship relatedTermRelationship)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        final String url = this.omasServerURL +BASE_RELATIONSHIPS_URL+"/%s" + RELATED_TERM;
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
     * Get a RelatedTermRelationship. A Related Term is a link between two similar Terms.
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
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName,url);
        RelatedTermRelationship gotRelatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotRelatedTermRelationship;
    }

    /**
     * Delete a RelatedTermRelationship. A Related Term is a link between two similar Terms.
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
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName,url);
        RelatedTermRelationship gotRelatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotRelatedTermRelationship;
    }


    /**
     * Purge a RelatedTermRelationship. A Related Term is a link between two similar Terms.
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
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_RELATEDTERM_URL;
        purgeRelationship(userId, guid, methodName,url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a synonym relationship. A link between glossary terms that have the same meaning.
     * <p>
     *
     * @param userId               userId under which the request is performed
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
    public Synonym createSynonymRelationship(String userId, Synonym synonym)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "createSynonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        final String url = this.omasServerURL +BASE_RELATIONSHIPS_URL+"/%s" + SYNONYM;
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
     *  Get a synonym relationship. A link between glossary terms that have the same meaning.
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
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName,url);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotSynonym;
    }

    /**
     *  Delete a synonym relationship. A link between glossary terms that have the same meaning.
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
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName,url);
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
        String url = this.omasServerURL + BASE_RELATIONSHIPS_SYNONYM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
    }
    /**
     *  Create a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
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
    public Antonym createAntonymRelationship(String userId, Antonym antonym)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    {
        final String methodName = "createAntonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);

        final String url = this.omasServerURL +BASE_RELATIONSHIPS_URL+"/%s" + ANTONYM;
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
     *  Get a antonym relationship. A link between glossary terms that have the opposite meaning.
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
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = getRelationship(userId, guid, methodName,url);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(methodName,restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return gotAntonym;
    }

    /**
     *  Delete a antonym relationship. A link between glossary terms that have the opposite meaning.
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
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse restResponse = deleteRelationship(userId, guid, methodName,url);
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
        final String url = this.omasServerURL +BASE_RELATIONSHIPS_ANTONYM_URL;
        purgeRelationship(userId, guid, methodName,url);
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
