/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The relationship handler is initialised with a SubjectAreaRelationship, that contains the server the call should be sent to.
 * The handler exposes methods for term functionality for the glossary author view
 */
public class RelationshipHandler {
    private static final Logger log = LoggerFactory.getLogger(RelationshipHandler.class);

    private SubjectAreaRelationship subjectAreaRelationship;

    /**
     * Constructor for the RelationshipHandler
     *
     * @param subjectAreaRelationship The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for terms. This is the same as the
     *                                The SubjectAreaDefinition Open Metadata View Service (OMVS) API for terms.
     */
    public RelationshipHandler(SubjectAreaRelationship subjectAreaRelationship) {
        this.subjectAreaRelationship = subjectAreaRelationship;
    }

    /**
     * Create a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the created term HASA relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermHASARelationship createTermHASARelationship(String userId, TermHASARelationship termHASARelationship) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            MetadataServerUncontactableException,
                                                                                                                            UnexpectedResponseException,
                                                                                                                            UnrecognizedGUIDException {
        return subjectAreaRelationship.createTermHASARelationship(userId, termHASARelationship);
    }

    /**
     * Get a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to get
     * @return TermHASARelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermHASARelationship getTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UserNotAuthorizedException,
                                                                                           UnexpectedResponseException,
                                                                                           UnrecognizedGUIDException {
        return subjectAreaRelationship.getTermHASARelationship(userId, guid);
    }

    /**
     * Update a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the updated term HASA relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermHASARelationship updateTermHASARelationship(String userId, TermHASARelationship termHASARelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.updateTermHASARelationship(userId, termHASARelationship);
    }

    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the replaced term HASA relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermHASARelationship replaceTermHASARelationship(String userId, TermHASARelationship termHASARelationship) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             MetadataServerUncontactableException,
                                                                                                                             UnexpectedResponseException,
                                                                                                                             UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceTermHASARelationship(userId, termHASARelationship);
    }

    /**
     * Delete a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to delete
     * @return Deleted TermHASARelationship
     * <p>
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
    public TermHASARelationship deleteTermHASARelationship(String userId, String guid) throws
                                                                                       InvalidParameterException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UserNotAuthorizedException,
                                                                                       UnrecognizedGUIDException,
                                                                                       FunctionNotSupportedException,
                                                                                       RelationshipNotDeletedException,
                                                                                       UnexpectedResponseException {
        return subjectAreaRelationship.deleteTermHASARelationship(userId, guid);
    }

    /**
     * Purge a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermHASARelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             GUIDNotPurgedException,
                                                                             UnrecognizedGUIDException,
                                                                             MetadataServerUncontactableException,
                                                                             UnexpectedResponseException {
        subjectAreaRelationship.purgeTermHASARelationship(userId, guid);
    }

    /**
     * Restore a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * <p>
     * Restore allows the deleted has a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the has a relationship to delete
     * @return the restored has a relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermHASARelationship restoreTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UnexpectedResponseException,
                                                                                               UnrecognizedGUIDException {
        return subjectAreaRelationship.restoreTermHASARelationship(userId, guid);
    }

    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * <p>
     *
     * @param userId                  unique identifier for requesting user, under which the request is performed
     * @param relatedTermRelationship the RelatedTerm relationship
     * @return the created RelatedTerm relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public RelatedTerm createRelatedTerm(String userId, RelatedTerm relatedTermRelationship) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    MetadataServerUncontactableException,
                                                                                                    UnexpectedResponseException,
                                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.createRelatedTerm(userId, relatedTermRelationship);
    }

    /**
     * Get a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return RelatedTerm
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
        return subjectAreaRelationship.getRelatedTerm(userId, guid);
    }

    /**
     * Update a RelatedTerm Relationship.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param termRelatedTerm the RelatedTerm relationship
     * @return the updated term RelatedTerm relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public RelatedTerm updateRelatedTerm(String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.updateRelatedTerm(userId, termRelatedTerm);
    }

    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param termRelatedTerm the replacement related term relationship
     * @return ReplacementTerm replaced related Term relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public RelatedTerm replaceRelatedTerm(String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceRelatedTerm(userId, termRelatedTerm);
    }

    /**
     * Restore a Related Term relationship
     * <p>
     * Restore allows the deleted Synonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to restore
     * @return the restored related term relationship
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
        return subjectAreaRelationship.restoreRelatedTerm(userId, guid);
    }

    /**
     * Delete a RelatedTerm. A Related Term is a link between two similar Terms.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted RelatedTerm relationship
     * <p>
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
        return subjectAreaRelationship.deleteRelatedTerm(userId, guid);
    }

    /**
     * Purge a RelatedTerm. A Related Term is a link between two similar Terms.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    GUIDNotPurgedException,
                                                                    UnrecognizedGUIDException,
                                                                    MetadataServerUncontactableException,
                                                                    UnexpectedResponseException {
        subjectAreaRelationship.purgeRelatedTerm(userId, guid);
    }

    /**
     * Restore a related term relationship
     * <p>
     * Restore allows the deleted related term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to delete
     * @return the restored related term relationship
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
        return subjectAreaRelationship.restoreRelatedTerm(userId, guid);
    }


    /**
     * Create a synonym relationship. A link between glossary terms that have the same meaning.
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param synonym the Synonym relationship
     * @return the created Synonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Synonym createSynonymRelationship(String userId, Synonym synonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.createSynonymRelationship(userId, synonym);
    }

    /**
     * Get a synonym relationship. A link between glossary terms that have the same meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to get
     * @return Synonym
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
        return subjectAreaRelationship.getSynonymRelationship(userId, guid);
    }

    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param synonym the Synonym relationship
     * @return updated Synonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Synonym updateSynonymRelationship(String userId, Synonym synonym) throws InvalidParameterException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UserNotAuthorizedException,
                                                                                    UnexpectedResponseException,
                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.updateSynonymRelationship(userId, synonym);
    }

    /**
     * Replace a Synonym relationship, which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param synonym the Synonym relationship
     * @return replaced synonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Synonym replaceSynonymRelationship(String userId, Synonym synonym) throws InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceSynonymRelationship(userId, synonym);
    }

    /**
     * Delete a synonym relationship. A link between glossary terms that have the same meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the synonym relationship to delete
     * @return deleted Synonym relationship
     * <p>
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
        return subjectAreaRelationship.deleteSynonymRelationship(userId, guid);
    }

    /**
     * Purge a synonym relationship. A link between glossary terms that have the same meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            GUIDNotPurgedException,
                                                                            UnrecognizedGUIDException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException {
        subjectAreaRelationship.purgeSynonymRelationship(userId, guid);
    }

    /**
     * Restore a Synonym relationship
     * <p>
     * Restore allows the deleted Synonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to restore
     * @return the restored Synonym relationship
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
        return subjectAreaRelationship.restoreSynonymRelationship(userId, guid);
    }

    /**
     * Create a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param antonym the Antonym relationship
     * @return the created antonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Antonym createAntonymRelationship(String userId, Antonym antonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.createAntonymRelationship(userId, antonym);
    }

    /**
     * Get a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Anonym relationship to get
     * @return Antonym relationship
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
        return subjectAreaRelationship.getAntonymRelationship(userId, guid);
    }

    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param antonym the Antonym relationship
     * @return Antonym updated antonym
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Antonym updateAntonymRelationship(String userId, Antonym antonym) throws InvalidParameterException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UserNotAuthorizedException,
                                                                                    UnexpectedResponseException,
                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.updateAntonymRelationship(userId, antonym);
    }

    /**
     * Replace an Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param antonym the antonym relationship
     * @return Antonym replaced antonym
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Antonym replaceAntonymRelationship(String userId, Antonym antonym) throws InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceAntonymRelationship(userId, antonym);
    }

    /**
     * Delete a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @return deleted Antonym
     * <p>
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
        return subjectAreaRelationship.deleteAntonymRelationship(userId, guid);
    }

    /**
     * Purge a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            GUIDNotPurgedException,
                                                                            UnrecognizedGUIDException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException {
        subjectAreaRelationship.purgeAntonymRelationship(userId, guid);
    }

    /**
     * Restore a Antonym relationship
     * <p>
     * Restore allows the deleted Antonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @return the restored Antonym relationship
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
        return subjectAreaRelationship.restoreAntonymRelationship(userId, guid);
    }

    /**
     * Create a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * <p>
     *
     * @param userId      userId under which the request is performed
     * @param translation the Translation relationship
     * @return the created translation relationship
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
        return subjectAreaRelationship.createTranslationRelationship(userId, translation);
    }

    /**
     * Get a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to get
     * @return Translation
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
        return subjectAreaRelationship.getTranslationRelationship(userId, guid);
    }

    /**
     * Update a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     * @param userId      userId under which the request is performed
     * @param translation the Translation relationship
     * @return Translation updated translation
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Translation updateTranslationRelationship(String userId, Translation translation) throws InvalidParameterException,
                                                                                                    MetadataServerUncontactableException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    UnexpectedResponseException,
                                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.updateTranslationRelationship(userId, translation);
    }

    /**
     * Replace an Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     * @param userId      userId under which the request is performed
     * @param translation the translation relationship
     * @return Translation replaced translation
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Translation replaceTranslationRelationship(String userId, Translation translation) throws InvalidParameterException,
                                                                                                     MetadataServerUncontactableException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     UnexpectedResponseException,
                                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceTranslationRelationship(userId, translation);
    }

    /**
     * Delete a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * @return deleted Translation
     * <p>
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
        return subjectAreaRelationship.deleteTranslationRelationship(userId, guid);
    }

    /**
     * Purge a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                GUIDNotPurgedException,
                                                                                UnrecognizedGUIDException,
                                                                                MetadataServerUncontactableException,
                                                                                UnexpectedResponseException {
        subjectAreaRelationship.purgeTranslationRelationship(userId, guid);
    }

    /**
     * Restore a Translation relationship
     * <p>
     * Restore allows the deleted Translation relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * @return the restored Translation relationship
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
        return subjectAreaRelationship.restoreTranslationRelationship(userId, guid);
    }

    /**
     * Create a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param usedInContext the UsedInContext relationship
     * @return the created usedInContext relationship
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
        return subjectAreaRelationship.createUsedInContextRelationship(userId, usedInContext);
    }

    /**
     * Get a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to get
     * @return UsedInContext
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
        return subjectAreaRelationship.getUsedInContextRelationship(userId, guid);
    }

    /**
     * Update a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param usedInContext the UsedInContext relationship
     * @return UsedInContext updated usedInContext
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public UsedInContext updateUsedInContextRelationship(String userId, UsedInContext usedInContext) throws InvalidParameterException,
                                                                                                            MetadataServerUncontactableException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            UnexpectedResponseException,
                                                                                                            UnrecognizedGUIDException {
        return subjectAreaRelationship.updateUsedInContextRelationship(userId, usedInContext);
    }

    /**
     * Replace an UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param usedInContext the usedInContext relationship
     * @return UsedInContext replaced usedInContext
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public UsedInContext replaceUsedInContextRelationship(String userId, UsedInContext usedInContext) throws InvalidParameterException,
                                                                                                             MetadataServerUncontactableException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             UnexpectedResponseException,
                                                                                                             UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceUsedInContextRelationship(userId, usedInContext);
    }

    /**
     * Delete a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to delete
     * @return deleted UsedInContext
     * <p>
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
        return subjectAreaRelationship.deleteUsedInContextRelationship(userId, guid);
    }

    /**
     * Purge a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  GUIDNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException {
        subjectAreaRelationship.purgeUsedInContextRelationship(userId, guid);
    }

    /**
     * Restore a Used in context relationship
     * <p>
     * Restore allows the deletedUsed in context relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Used in context relationship to delete
     * @return the restored Used in context relationship
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
        return subjectAreaRelationship.restoreUsedInContextRelationship(userId, guid);
    }

    /**
     * Create a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param preferredTerm the PreferredTerm relationship
     * @return the created preferredTerm relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm createPreferredTermRelationship(String userId, PreferredTerm preferredTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.createPreferredTermRelationship(userId, preferredTerm);
    }

    /**
     * Get a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to get
     * @return PreferredTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm getPreferredTermRelationship(String userId, String guid) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.getPreferredTermRelationship(userId, guid);
    }

    /**
     * Update a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param preferredTerm the PreferredTerm relationship
     * @return PreferredTerm updated preferredTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm updatePreferredTermRelationship(String userId, PreferredTerm preferredTerm) throws InvalidParameterException,
                                                                                                            MetadataServerUncontactableException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            UnexpectedResponseException,
                                                                                                            UnrecognizedGUIDException {
        return subjectAreaRelationship.updatePreferredTermRelationship(userId, preferredTerm);
    }

    /**
     * Replace an PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param preferredTerm the preferredTerm relationship
     * @return PreferredTerm replaced preferredTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public PreferredTerm replacePreferredTermRelationship(String userId, PreferredTerm preferredTerm) throws InvalidParameterException,
                                                                                                             MetadataServerUncontactableException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             UnexpectedResponseException,
                                                                                                             UnrecognizedGUIDException {
        return subjectAreaRelationship.replacePreferredTermRelationship(userId, preferredTerm);
    }

    /**
     * Delete a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to delete
     * @return deleted PreferredTerm
     * <p>
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
        return subjectAreaRelationship.deletePreferredTermRelationship(userId, guid);
    }

    /**
     * Purge a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgePreferredTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  GUIDNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException {
        subjectAreaRelationship.purgePreferredTermRelationship(userId, guid);
    }

    /**
     * Restore a preferred term relationship
     * <p>
     * Restore allows the deletedpreferred term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the preferred term relationship to delete
     * @return the restored preferred term relationship
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
        return subjectAreaRelationship.restorePreferredTermRelationship(userId, guid);
    }

    /**
     * Create a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * <p>
     *
     * @param userId     userId under which the request is performed
     * @param validValue the ValidValue relationship
     * @return the created validValue relationship
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
        return subjectAreaRelationship.createValidValueRelationship(userId, validValue);
    }

    /**
     * Get a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to get
     * @return ValidValue
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
        return subjectAreaRelationship.getValidValueRelationship(userId, guid);
    }

    /**
     * Update a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param userId     userId under which the request is performed
     * @param validValue the ValidValue relationship
     * @return ValidValue updated validValue
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ValidValue updateValidValueRelationship(String userId, ValidValue validValue) throws InvalidParameterException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UserNotAuthorizedException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException {
        return subjectAreaRelationship.updateValidValueRelationship(userId, validValue);
    }

    /**
     * Replace an ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param userId     userId under which the request is performed
     * @param validValue the validValue relationship
     * @return ValidValue replaced validValue
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ValidValue replaceValidValueRelationship(String userId, ValidValue validValue) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceValidValueRelationship(userId, validValue);
    }

    /**
     * Delete a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to delete
     * @return deleted ValidValue
     * <p>
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
        return subjectAreaRelationship.deleteValidValueRelationship(userId, guid);
    }

    /**
     * Purge a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               GUIDNotPurgedException,
                                                                               UnrecognizedGUIDException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException {
        subjectAreaRelationship.purgeValidValueRelationship(userId, guid);
    }

    /**
     * Restore a valid value relationship
     * <p>
     * Restore allows the deletedvalid value relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the valid value relationship to delete
     * @return the restored valid value relationship
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
        return subjectAreaRelationship.restoreValidValueRelationship(userId, guid);
    }

    /**
     * Create a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param replacementTerm the ReplacementTerm relationship
     * @return the created replacementTerm relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ReplacementTerm createReplacementTermRelationship(String userId, ReplacementTerm replacementTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.createReplacementTermRelationship(userId, replacementTerm);
    }

    /**
     * Get a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to get
     * @return ReplacementTerm
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
        return subjectAreaRelationship.getReplacementTermRelationship(userId, guid);
    }

    /**
     * Update a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param replacementTerm the ReplacementTerm relationship
     * @return ReplacementTerm updated replacementTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ReplacementTerm updateReplacementTermRelationship(String userId, ReplacementTerm replacementTerm) throws InvalidParameterException,
                                                                                                                    MetadataServerUncontactableException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    UnexpectedResponseException,
                                                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.updateReplacementTermRelationship(userId, replacementTerm);
    }

    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param replacementTerm the replacementTerm relationship
     * @return ReplacementTerm replaced replacementTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ReplacementTerm replaceReplacementTermRelationship(String userId, ReplacementTerm replacementTerm) throws InvalidParameterException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceReplacementTermRelationship(userId, replacementTerm);
    }

    /**
     * Delete a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to delete
     * @return deleted ReplacementTerm
     * <p>
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
        return subjectAreaRelationship.deleteReplacementTermRelationship(userId, guid);
    }

    /**
     * Purge a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    GUIDNotPurgedException,
                                                                                    UnrecognizedGUIDException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UnexpectedResponseException {
        subjectAreaRelationship.purgeReplacementTermRelationship(userId, guid);
    }

    /**
     * Restore a replacement term relationship
     * <p>
     * Restore allows the deleted replacement term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the replacement term relationship to delete
     * @return the restored replacement term relationship
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
        return subjectAreaRelationship.restoreReplacementTermRelationship(userId, guid);
    }

    /**
     * Create a TermTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     *
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the TermTYPEDBYRelationship relationship
     * @return the created termTYPEDBYRelationship relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermTYPEDBYRelationship createTermTYPEDBYRelationship(String userId, TermTYPEDBYRelationship termTYPEDBYRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.createTermTYPEDBYRelationship(userId, termTYPEDBYRelationship);
    }

    /**
     * Get a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termTYPEDBYRelationship relationship to get
     * @return TermTYPEDBYRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermTYPEDBYRelationship getTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException {
        return subjectAreaRelationship.getTermTYPEDBYRelationship(userId, guid);
    }

    /**
     * Update a TermTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the TermTYPEDBYRelationship relationship
     * @return TermTYPEDBYRelationship updated termTYPEDBYRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermTYPEDBYRelationship updateTermTYPEDBYRelationship(String userId, TermTYPEDBYRelationship termTYPEDBYRelationship) throws InvalidParameterException,
                                                                                                                                        MetadataServerUncontactableException,
                                                                                                                                        UserNotAuthorizedException,
                                                                                                                                        UnexpectedResponseException,
                                                                                                                                        UnrecognizedGUIDException {
        return subjectAreaRelationship.updateTermTYPEDBYRelationship(userId, termTYPEDBYRelationship);
    }

    /**
     * Replace an TermTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the termTYPEDBYRelationship relationship
     * @return TermTYPEDBYRelationship replaced termTYPEDBYRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermTYPEDBYRelationship replaceTermTYPEDBYRelationship(String userId, TermTYPEDBYRelationship termTYPEDBYRelationship) throws InvalidParameterException,
                                                                                                                                         MetadataServerUncontactableException,
                                                                                                                                         UserNotAuthorizedException,
                                                                                                                                         UnexpectedResponseException,
                                                                                                                                         UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceTermTYPEDBYRelationship(userId, termTYPEDBYRelationship);
    }

    /**
     * Delete a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termTYPEDBYRelationship relationship to delete
     * @return deleted TermTYPEDBYRelationship
     * <p>
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
    public TermTYPEDBYRelationship deleteTermTYPEDBYRelationship(String userId, String guid) throws
                                                                                             InvalidParameterException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UserNotAuthorizedException,
                                                                                             UnrecognizedGUIDException,
                                                                                             FunctionNotSupportedException,
                                                                                             RelationshipNotDeletedException,
                                                                                             UnexpectedResponseException {
        return subjectAreaRelationship.deleteTermTYPEDBYRelationship(userId, guid);
    }

    /**
     * Purge a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermTYPEDBYRelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                GUIDNotPurgedException,
                                                                                UnrecognizedGUIDException,
                                                                                MetadataServerUncontactableException,
                                                                                UnexpectedResponseException {
        subjectAreaRelationship.purgeTermTYPEDBYRelationship(userId, guid);
    }

    /**
     * Restore a typed by relationship
     * <p>
     * Restore allows the deleted typed by relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the typed by relationship to delete
     * @return the restored typed by relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermTYPEDBYRelationship restoreTypedByRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException {
        return subjectAreaRelationship.restoreTypedByRelationship(userId, guid);
    }

    /**
     * Create a Isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * <p>
     *
     * @param userId userId under which the request is performed
     * @param isa    the Isa relationship
     * @return the created isa relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ISARelationship createIsaRelationship(String userId, ISARelationship isa) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.createIsaRelationship(userId, isa);
    }

    /**
     * Get a isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the isa relationship to get
     * @return Isa
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ISARelationship getIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException {
        return subjectAreaRelationship.getIsaRelationship(userId, guid);
    }

    /**
     * Update a Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param userId userId under which the request is performed
     * @param isa    the Isa relationship
     * @return Isa updated isa
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ISARelationship updateIsaRelationship(String userId, ISARelationship isa) throws InvalidParameterException,
                                                                                            MetadataServerUncontactableException,
                                                                                            UserNotAuthorizedException,
                                                                                            UnexpectedResponseException,
                                                                                            UnrecognizedGUIDException {
        return subjectAreaRelationship.updateIsaRelationship(userId, isa);
    }

    /**
     * Replace an Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param userId userId under which the request is performed
     * @param isa    the isa relationship
     * @return Isa replaced isa
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ISARelationship replaceIsaRelationship(String userId, ISARelationship isa) throws InvalidParameterException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UserNotAuthorizedException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceIsaRelationship(userId, isa);
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
    public ISARelationship deleteIsaRelationship(String userId, String guid) throws
                                                                             InvalidParameterException,
                                                                             MetadataServerUncontactableException,
                                                                             UserNotAuthorizedException,
                                                                             UnrecognizedGUIDException,
                                                                             FunctionNotSupportedException,
                                                                             RelationshipNotDeletedException,
                                                                             UnexpectedResponseException {
        return subjectAreaRelationship.deleteIsaRelationship(userId, guid);
    }

    /**
     * Purge a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Isa relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        GUIDNotPurgedException,
                                                                        UnrecognizedGUIDException,
                                                                        MetadataServerUncontactableException,
                                                                        UnexpectedResponseException {
        subjectAreaRelationship.purgeIsaRelationship(userId, guid);
    }

    /**
     * Restore an is a relationship
     * <p>
     * Restore allows the deleted is a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the is a relationship to delete
     * @return the restored is a relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ISARelationship restoreIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.restoreIsaRelationship(userId, guid);
    }

    /**
     * Create a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     *
     * <p>
     *
     * @param userId    userId under which the request is performed
     * @param isATypeOf the TermISATypeOFRelationship relationship
     * @return the created TermISATypeOFRelationship relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermISATypeOFRelationship createTermISATypeOFRelationship(String userId, TermISATypeOFRelationship isATypeOf) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.createTermISATypeOFRelationship(userId, isATypeOf);
    }

    /**
     * Get a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermISATypeOFRelationship relationship to get
     * @return TermISATypeOFRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermISATypeOFRelationship getTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                     MetadataServerUncontactableException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     UnexpectedResponseException,
                                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.getTermISATypeOFRelationship(userId, guid);
    }

    /**
     * Update a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     * @param userId    userId under which the request is performed
     * @param isATypeOf the TermISATypeOFRelationship relationship
     * @return TermISATypeOFRelationship updated TermISATypeOFRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermISATypeOFRelationship updateTermISATypeOFRelationship(String userId, TermISATypeOFRelationship isATypeOf) throws InvalidParameterException,
                                                                                                                                MetadataServerUncontactableException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                UnexpectedResponseException,
                                                                                                                                UnrecognizedGUIDException {
        return subjectAreaRelationship.updateTermISATypeOFRelationship(userId, isATypeOf);
    }

    /**
     * Replace an TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     * @param userId    userId under which the request is performed
     * @param isATypeOf the TermISATypeOFRelationship relationship
     * @return TermISATypeOFRelationship replaced TermISATypeOFRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermISATypeOFRelationship replaceTermISATypeOFRelationship(String userId, TermISATypeOFRelationship isATypeOf) throws InvalidParameterException,
                                                                                                                                 MetadataServerUncontactableException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 UnexpectedResponseException,
                                                                                                                                 UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceTermISATypeOFRelationship(userId, isATypeOf);
    }

    /**
     * Delete a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermISATypeOFRelationship relationship to delete
     * @return deleted TermISATypeOFRelationship
     * <p>
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
    public TermISATypeOFRelationship deleteTermISATypeOFRelationship(String userId, String guid) throws
                                                                                                 InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnrecognizedGUIDException,
                                                                                                 FunctionNotSupportedException,
                                                                                                 RelationshipNotDeletedException,
                                                                                                 UnexpectedResponseException {
        return subjectAreaRelationship.deleteTermISATypeOFRelationship(userId, guid);
    }

    /**
     * Purge a TermISATypeOFRelationship relationship, which is defines an inheritance relationship between two spine objects.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermISATypeOFRelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  GUIDNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException {
        subjectAreaRelationship.purgeTermISATypeOFRelationship(userId, guid);
    }

    /**
     * Restore an is a type of relationship
     * <p>
     * Restore allows the deleted is a type of relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the is a type of relationship to delete
     * @return the restored is a type of relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermISATypeOFRelationship restoreIsaTypeOfRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     MetadataServerUncontactableException,
                                                                                                     UnexpectedResponseException,
                                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.restoreIsaTypeOfRelationship(userId, guid);
    }

    /**
     * Create a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the created term categorization relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermCategorizationRelationship createTermCategorizationRelationship(String userId, TermCategorizationRelationship termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                                                    UnexpectedResponseException,
                                                                                                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.createTermCategorizationRelationship(userId, termCategorizationRelationship);
    }

    /**
     * Get a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to get
     * @return TermCategorizationRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermCategorizationRelationship getTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                               MetadataServerUncontactableException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               UnexpectedResponseException,
                                                                                                               UnrecognizedGUIDException {
        return subjectAreaRelationship.getTermCategorizationRelationship(userId, guid);
    }

    /**
     * Update a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the updated term categorization relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermCategorizationRelationship updateTermCategorizationRelationship(String userId, TermCategorizationRelationship termCategorizationRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.updateTermCategorizationRelationship(userId, termCategorizationRelationship);
    }

    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the replaced term categorization relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermCategorizationRelationship replaceTermCategorizationRelationship(String userId, TermCategorizationRelationship termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                                                     UnexpectedResponseException,
                                                                                                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceTermCategorizationRelationship(userId, termCategorizationRelationship);
    }

    /**
     * Delete a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.      * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * @return Deleted TermCategorizationRelationship
     * <p>
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
    public TermCategorizationRelationship deleteTermCategorizationRelationship(String userId, String guid) throws
                                                                                                           InvalidParameterException,
                                                                                                           MetadataServerUncontactableException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           UnrecognizedGUIDException,
                                                                                                           FunctionNotSupportedException,
                                                                                                           RelationshipNotDeletedException,
                                                                                                           UnexpectedResponseException {
        return subjectAreaRelationship.deleteTermCategorizationRelationship(userId, guid);
    }

    /**
     * Purge a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       GUIDNotPurgedException,
                                                                                       UnrecognizedGUIDException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UnexpectedResponseException {
        subjectAreaRelationship.purgeTermCategorizationRelationship(userId, guid);
    }

    /**
     * Restore a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     * Restore allows the deleted Term Categorization relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Term Categorization relationship to delete
     * @return the restored has a relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermCategorizationRelationship restoreTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   MetadataServerUncontactableException,
                                                                                                                   UnexpectedResponseException,
                                                                                                                   UnrecognizedGUIDException {
        return subjectAreaRelationship.restoreTermCategorizationRelationship(userId, guid);
    }

    /**
     * Create a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * This method does not error if the relationship ends are not spine objects or spine attributes.
     * Terms created using the Glossary author OMVS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Terms that have not been created via the Glossary Author OMVS or Subject Area OMAS or to recreate
     * the TermAnchor relationship if it has been purged.
     * <p>
     *
     * @param userId                 userId under which the request is performed
     * @param termAnchorRelationship the TermAnchor relationship
     * @return the created TermAnchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchorRelationship createTermAnchorRelationship(String userId, TermAnchorRelationship termAnchorRelationship) throws InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                    UnexpectedResponseException,
                                                                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.createTermAnchorRelationship(userId, termAnchorRelationship);
    }

    /**
     * Get a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to get
     * @return TermAnchorRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchorRelationship getTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UserNotAuthorizedException,
                                                                                               UnexpectedResponseException,
                                                                                               UnrecognizedGUIDException {
        return subjectAreaRelationship.getTermAnchorRelationship(userId, guid);
    }

    /**
     * Update a Term Anchor Relationship.
     * <p>
     *
     * @param userId                 userId under which the request is performed
     * @param termAnchorRelationship the TermAnchor relationship
     * @return the updated TermAnchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchorRelationship updateTermAnchorRelationship(String userId, TermAnchorRelationship termAnchorRelationship) throws InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                    UnexpectedResponseException,
                                                                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceTermAnchorRelationship(userId, termAnchorRelationship);
    }

    /**
     * Replace a Term Anchor Relationship.
     * <p>
     *
     * @param userId                 userId under which the request is performed
     * @param termAnchorRelationship the TermAnchor relationship
     * @return the updated TermAnchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchorRelationship replaceTermAnchorRelationship(String userId, TermAnchorRelationship termAnchorRelationship) throws InvalidParameterException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                     UnexpectedResponseException,
                                                                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceTermAnchorRelationship(userId, termAnchorRelationship);
    }

    /**
     * Delete a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * @return Deleted TermAnchorRelationship
     * <p>
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
    public TermAnchorRelationship deleteTermAnchorRelationship(String userId, String guid) throws
                                                                                           InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UserNotAuthorizedException,
                                                                                           UnrecognizedGUIDException,
                                                                                           FunctionNotSupportedException,
                                                                                           RelationshipNotDeletedException,
                                                                                           UnexpectedResponseException {
        return subjectAreaRelationship.deleteTermAnchorRelationship(userId, guid);
    }

    /**
     * Purge a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               GUIDNotPurgedException,
                                                                               UnrecognizedGUIDException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException {
        subjectAreaRelationship.purgeTermAnchorRelationship(userId, guid);
    }

    /**
     * Restore a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * <p>
     * Restore allows the deleted Term Categorization relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Term Anchor relationship to delete
     * @return the restored Term Anchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public TermAnchorRelationship restoreTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   MetadataServerUncontactableException,
                                                                                                   UnexpectedResponseException,
                                                                                                   UnrecognizedGUIDException {
        return subjectAreaRelationship.restoreTermAnchorRelationship(userId, guid);
    }

    /**
     * Create a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     * Categories created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Category. This method is to allow glossaries to be associated with Categories that have not been created via the Subject Area OMAS or to recreate
     * the CategoryAnchor relationship if it has been purged.
     *
     * @param userId                     userId under which the request is performed
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the created term categorization relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchorRelationship createCategoryAnchorRelationship(String userId, CategoryAnchorRelationship categoryAnchorRelationship) throws InvalidParameterException,
                                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                                    UnexpectedResponseException,
                                                                                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.createCategoryAnchorRelationship(userId, categoryAnchorRelationship);
    }

    /**
     * Get a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to get
     * @return CategoryAnchorRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchorRelationship getCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                       MetadataServerUncontactableException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       UnexpectedResponseException,
                                                                                                       UnrecognizedGUIDException {
        return subjectAreaRelationship.getCategoryAnchorRelationship(userId, guid);
    }

    /**
     * Update a Category Anchor Relationship.
     * <p>
     *
     * @param userId                     userId under which the request is performed
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the updated category anchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchorRelationship updateCategoryAnchorRelationship(String userId, CategoryAnchorRelationship categoryAnchorRelationship) throws InvalidParameterException,
                                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                                    UnexpectedResponseException,
                                                                                                                                                    UnrecognizedGUIDException {
        return subjectAreaRelationship.updateCategoryAnchorRelationship(userId, categoryAnchorRelationship);
    }

    /**
     * Replace a Category Anchor Relationship.
     * <p>
     *
     * @param userId                     userId under which the request is performed
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the replaced category anchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchorRelationship replaceCategoryAnchorRelationship(String userId, CategoryAnchorRelationship categoryAnchorRelationship) throws InvalidParameterException,
                                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                                     UnexpectedResponseException,
                                                                                                                                                     UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceCategoryAnchorRelationship(userId, categoryAnchorRelationship);
    }

    /**
     * Delete a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * @return Deleted CategoryAnchorRelationship
     * <p>
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
    public CategoryAnchorRelationship deleteCategoryAnchorRelationship(String userId, String guid) throws
                                                                                                   InvalidParameterException,
                                                                                                   MetadataServerUncontactableException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   UnrecognizedGUIDException,
                                                                                                   FunctionNotSupportedException,
                                                                                                   RelationshipNotDeletedException,
                                                                                                   UnexpectedResponseException {
        return subjectAreaRelationship.deleteCategoryAnchorRelationship(userId, guid);
    }

    /**
     * Purge a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   GUIDNotPurgedException,
                                                                                   UnrecognizedGUIDException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UnexpectedResponseException {
        subjectAreaRelationship.purgeCategoryAnchorRelationship(userId, guid);
    }

    /**
     * Restore a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     * <p>
     * Restore allows the deleted Category Anchor relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Category Anchor relationship to delete
     * @return the restored category anchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public CategoryAnchorRelationship restoreCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           MetadataServerUncontactableException,
                                                                                                           UnexpectedResponseException,
                                                                                                           UnrecognizedGUIDException {
        return subjectAreaRelationship.restoreCategoryAnchorRelationship(userId, guid);
    }

    /**
     * Create a ProjectScope relationship. A link between the project content and the project.
     * <p>
     *
     * @param userId       userId under which the request is performed
     * @param projectScope the ProjectScope relationship
     * @return the created ProjectScope relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScopeRelationship createProjectScopeRelationship(String userId, ProjectScopeRelationship projectScope) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        return subjectAreaRelationship.createProjectScopeRelationship(userId, projectScope);
    }

    /**
     * Get a ProjectScope relationship. A link between the project content and the project.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to get
     * @return ProjectScope
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScopeRelationship getProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                   MetadataServerUncontactableException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   UnexpectedResponseException,
                                                                                                   UnrecognizedGUIDException {
        return subjectAreaRelationship.getProjectScopeRelationship(userId, guid);
    }

    /**
     * Update a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param userId                   userId under which the request is performed
     * @param projectScopeRelationship the ProjectScope relationship
     * @return updated ProjectScope relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScopeRelationship updateProjectScopeRelationship(String userId, ProjectScopeRelationship projectScopeRelationship) throws InvalidParameterException,
                                                                                                                                            MetadataServerUncontactableException,
                                                                                                                                            UserNotAuthorizedException,
                                                                                                                                            UnexpectedResponseException,
                                                                                                                                            UnrecognizedGUIDException {
        return subjectAreaRelationship.updateProjectScopeRelationship(userId, projectScopeRelationship);
    }

    /**
     * Replace a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param userId                   userId under which the request is performed
     * @param projectScopeRelationship the ProjectScope relationship
     * @return replaced ProjectScope relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScopeRelationship replaceProjectScopeRelationship(String userId, ProjectScopeRelationship projectScopeRelationship) throws InvalidParameterException,
                                                                                                                                             MetadataServerUncontactableException,
                                                                                                                                             UserNotAuthorizedException,
                                                                                                                                             UnexpectedResponseException,
                                                                                                                                             UnrecognizedGUIDException {
        return subjectAreaRelationship.replaceProjectScopeRelationship(userId, projectScopeRelationship);
    }

    /**
     * Delete a ProjectScope relationship. A link between the project content and the project.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to delete
     * @return deleted ProjectScope
     * <p>
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
    public ProjectScopeRelationship deleteProjectScopeRelationship(String userId, String guid) throws
                                                                                               InvalidParameterException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UnrecognizedGUIDException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException,
                                                                                               RelationshipNotDeletedException,
                                                                                               UnexpectedResponseException {
        return subjectAreaRelationship.deleteProjectScopeRelationship(userId, guid);
    }


    /**
     * Purge a ProjectScope relationship. A link between the project content and the project.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws GUIDNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public void purgeProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 GUIDNotPurgedException,
                                                                                 UnrecognizedGUIDException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException {
        subjectAreaRelationship.purgeProjectScopeRelationship(userId, guid);
    }

    /**
     * Restore a ProjectScope relationship which  is a link between the project content and the project.
     * <p>
     * Restore allows the deleted ProjectScope relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to restore
     * @return the restored ProjectScope relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public ProjectScopeRelationship restoreProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       MetadataServerUncontactableException,
                                                                                                       UnexpectedResponseException,
                                                                                                       UnrecognizedGUIDException {
        return subjectAreaRelationship.restoreProjectScopeRelationship(userId, guid);
    }


    /**
     * Get a SemanticAssignment relationship,  Links a glossary term to another element such as an asset or schema element to define its meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the SemanticAssignment relationship to get
     * @return the SemanticAssignment relationship with the requested guid
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
        return subjectAreaRelationship.getSemanticAssignmentRelationship(userId, guid);
    }

}
