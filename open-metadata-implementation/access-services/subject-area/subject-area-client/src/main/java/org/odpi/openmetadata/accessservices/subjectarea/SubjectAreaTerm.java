/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for terms.
 */
public interface SubjectAreaTerm
{

    /**
     * Create a Term
     * @param userId unique identifier for requesting user, under which the request is performed
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

    public Term createTerm(String userId, Term suppliedTerm)
            throws MetadataServerUncontactableException,UnexpectedResponseException,
            InvalidParameterException,
            UserNotAuthorizedException,
            ClassificationException,
            FunctionNotSupportedException,
            UnexpectedResponseException ;

    /**
     * Get a term by guid.
     * @param userId unique identifier for requesting user, under which the request is performed
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

    public  Term getTermByGuid( String userId, String guid)
            throws MetadataServerUncontactableException,UnexpectedResponseException,
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            UnexpectedResponseException;

    /**
     * Replace a Term. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
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
            MetadataServerUncontactableException;
    /**
     * Update a Term. This means to update the term with any non-null attributes from the supplied term.
     * <p>
     * If the caller has chosen to incorporate the term name in their Term Terms or Categories qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * If the caller has chosen to incorporate the term qualifiedName in their Term Terms or Categories qualified name, changing the qualified name of the term will cause those
     * qualified names to mismatch the Term name.
     * Status is not updated using this call.
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
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
            MetadataServerUncontactableException;

    /**
     * Delete a Term instance
     *
     * A delete (also known as a soft delete) means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid guid of the term to be deleted.
     * @return the deleted term
     * @param userId userId under which the request is performed
     * @param guid guid of the term to be deleted.
     * @return the deleted term
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public Term deleteTerm(String userId,String guid) throws InvalidParameterException,
            MetadataServerUncontactableException,UnexpectedResponseException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            EntityNotDeletedException,
            UnrecognizedGUIDException;
    /**
     * Purge a Term instance
     *
     * A purge means that the term will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid guid of the term to be deleted.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public  void purgeTerm(String userId,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,UnexpectedResponseException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException;
    /**
     * Create a Term HASA Relationship which is the relationship between a spine object and a spine attribute.
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
            MetadataServerUncontactableException,UnexpectedResponseException,
            UnexpectedResponseException;
    /**
     * Get a TermHASARelationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to get
     * @return TermHASARelationship
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public TermHASARelationship getTermHASARelationship( String userId,String guid) throws InvalidParameterException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnexpectedResponseException;
    /**
     * Delete a Term HASA Relationship
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to delete
     * @return response which when successful contains the Antonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException            a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public TermHASARelationship deleteTermHASARelationship( String userId,String guid) throws InvalidParameterException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException,
            UnrecognizedGUIDException;
    /**
     * Purge a Term HASA Relationship
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to delete
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeTermHASARelationship( String userId,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            GUIDNotPurgedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException;
    /**
     * Create a RelatedTermRelationship. A Related Term is a link between two similar Terms.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param relatedTermRelationship  the related term relationship
     * @return the created re;lated term relationship
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
            MetadataServerUncontactableException,UnexpectedResponseException,
            UnexpectedResponseException;
    /**
     * Get a RelatedTermRelationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to get
     * @return RelatedTermRelationship
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.

     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public RelatedTermRelationship getRelatedTermRelationship(String userId, String guid)  throws   InvalidParameterException,
            MetadataServerUncontactableException,UnexpectedResponseException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnexpectedResponseException;
    /**
     * Delete a RelatedTermRelationship relationship
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to delete
     * @return the deleted synonmym
     * when not successful the following Exception responses can occur
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public RelatedTermRelationship deleteRelatedTermRelationship(String userId, String guid)  throws InvalidParameterException,
            MetadataServerUncontactableException,UnexpectedResponseException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException, UnrecognizedGUIDException;
    /**
     * Purge a RelatedTermRelationship relationship
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTermRelationship relationship to delete
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeRelatedTermRelationship( String userId,String guid) throws InvalidParameterException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UserNotAuthorizedException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException;

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
            MetadataServerUncontactableException,UnexpectedResponseException,
            UnexpectedResponseException;
    /**
     * Get a Synonym
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to get
     * @return Synonym
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public Synonym getSynonymRelationship( String userId,String guid)  throws   InvalidParameterException,
            MetadataServerUncontactableException,UnexpectedResponseException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnexpectedResponseException;
    /**
     * Delete a Synonym relationship
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * @return the deleted synonmym
     * when not successful the following Exception responses can occur
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException            a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Synonym deleteSynonymRelationship( String userId,String guid)  throws InvalidParameterException,
    MetadataServerUncontactableException,UnexpectedResponseException,
    UserNotAuthorizedException,
    FunctionNotSupportedException,
    RelationshipNotDeletedException,
    UnexpectedResponseException,
    UnrecognizedGUIDException;
    /**
     * Purge a Synonym relationship
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeSynonymRelationship( String userId,String guid) throws InvalidParameterException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UserNotAuthorizedException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException;
    /**
     *  Create an antonym relationship, which is a link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param antonym the Antonym relationship
     * @return the created antonym
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
            MetadataServerUncontactableException,UnexpectedResponseException,
            UnexpectedResponseException;
    /**
     * Get a antonym relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to get
     * @return Antonym
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.

     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    public Antonym getAntonymRelationship( String userId,String guid) throws InvalidParameterException, MetadataServerUncontactableException,UnexpectedResponseException, UserNotAuthorizedException, FunctionNotSupportedException, UnexpectedResponseException;
    /**
     * Delete a Antonym relationship
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @return response which when successful contains the Antonym relationship with the requested guid
     * Exceptions returned by the server
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Antonym deleteAntonymRelationship( String userId,String guid) throws  InvalidParameterException,
    MetadataServerUncontactableException,UnexpectedResponseException,
    UserNotAuthorizedException,
    FunctionNotSupportedException,
    UnrecognizedGUIDException,
    RelationshipNotDeletedException,
    UnexpectedResponseException;
    /**
     * Purge a Antonym relationship
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeAntonymRelationship( String userId,String guid) throws InvalidParameterException,
            MetadataServerUncontactableException,UnexpectedResponseException,
            UserNotAuthorizedException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException,
            UnexpectedResponseException;

}