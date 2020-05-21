/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for relationships.
 */
public interface SubjectAreaRelationship
{
    /**
     * Create a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the created term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    Hasa createTermHASARelationship(String userId, Hasa termHASARelationship) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException
    ;
    /**
     * Get a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to get
     * @return Hasa
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
    Hasa getTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                    MetadataServerUncontactableException,
                                                                    UserNotAuthorizedException,
                                                                    UnexpectedResponseException,
                                                                    UnrecognizedGUIDException
    ;
    /**
     * Update a Term HASA Relationship.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the updated term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    Hasa updateTermHASARelationship(String userId, Hasa termHASARelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the replaced term HASA relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    Hasa replaceTermHASARelationship(String userId, Hasa termHASARelationship) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      MetadataServerUncontactableException,
                                                                                      UnexpectedResponseException,
                                                                                      UnrecognizedGUIDException
    ;
    /**
     * Delete a Term HASA Relationship. A relationship between a spine object and a spine attribute.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to delete
     * @return Deleted Hasa
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
    Hasa deleteTermHASARelationship(String userId, String guid) throws
                                                                                                         InvalidParameterException,
                                                                                                         MetadataServerUncontactableException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         UnrecognizedGUIDException,
                                                                                                         FunctionNotSupportedException,
                                                                                                         RelationshipNotDeletedException,
                                                                                                         UnexpectedResponseException
    ;
    /**
     * Purge a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException       a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      UnrecognizedGUIDException,
                                                                      MetadataServerUncontactableException,
                                                                      UnexpectedResponseException,
                                                                      RelationshipNotPurgedException
    ;
    /**
     * Restore a has a relationship
     *
     * Restore allows the deleted has a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the has a relationship to delete
     * @return response which when successful contains the restored has a relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    Hasa restoreTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        MetadataServerUncontactableException,
                                                                        UnexpectedResponseException,
                                                                        UnrecognizedGUIDException
    ;
    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * <p>
     *
     *
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

    RelatedTerm createRelatedTerm(String userId, RelatedTerm relatedTermRelationship) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;
    /**
     * Get a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     *
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
    RelatedTerm getRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                                                            MetadataServerUncontactableException,
                                                                                            UserNotAuthorizedException,
                                                                                            UnexpectedResponseException,
                                                                                            UnrecognizedGUIDException
    ;
    /**
     * Update a RelatedTerm Relationship.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termRelatedTerm the RelatedTerm relationship
     * @return the updated term RelatedTerm relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    RelatedTerm updateRelatedTerm(String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termRelatedTerm     the replacement related term relationship
     * @return  ReplacementTerm replaced related Term relationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    RelatedTerm replaceRelatedTerm(String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException;

    /**
     * Restore a Related Term relationship
     *
     * Restore allows the deleted Synonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the related term relationship to restore
     * @return response which when successful contains the restored related term relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    RelatedTerm restoreRelatedTerm(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
        ;
    /**
     * Delete a RelatedTerm. A Related Term is a link between two similar Terms.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    RelatedTerm deleteRelatedTerm(String userId, String guid) throws
                                                                                        InvalidParameterException,
                                                                                        MetadataServerUncontactableException,
                                                                                        UserNotAuthorizedException,
                                                                                        UnrecognizedGUIDException,
                                                                                        FunctionNotSupportedException,
                                                                                        RelationshipNotDeletedException,
                                                                                        UnexpectedResponseException
    ;
    /**
     * Purge a RelatedTerm. A Related Term is a link between two similar Terms.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             RelationshipNotPurgedException,
                                                             UnrecognizedGUIDException,
                                                             MetadataServerUncontactableException,
                                                             UnexpectedResponseException, RelationshipNotPurgedException, RelationshipNotPurgedException
    ;
    /**
     * Restore a related term relationship
     *
     * Restore allows the deleted related term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the related term relationship to delete
     * @return response which when successful contains the restored related term relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    RelatedTerm restoreRelatedTermRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;


    /**
     *  Create a synonym relationship. A link between glossary terms that have the same meaning.
     * <p>
     *
     *
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
    Synonym createSynonymRelationship(String userId, Synonym synonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     *  Get a synonym relationship. A link between glossary terms that have the same meaning.
     *
     *
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
    Synonym getSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UserNotAuthorizedException,
                                                                                               UnexpectedResponseException,
                                                                                               UnrecognizedGUIDException
    ;
    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     *
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
    Synonym updateSynonymRelationship(String userId, Synonym synonymRelationship)  throws InvalidParameterException,
                                                                                                                    MetadataServerUncontactableException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    UnexpectedResponseException,
                                                                                                                    UnrecognizedGUIDException ;
    /**
     * Replace a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     *
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
    Synonym replaceSynonymRelationship(String userId, Synonym synonymRelationship)  throws InvalidParameterException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException ;
    /**
     *  Delete a synonym relationship. A link between glossary terms that have the same meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    Synonym deleteSynonymRelationship(String userId, String guid) throws
                                                                                           InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UnrecognizedGUIDException,
                                                                                           UserNotAuthorizedException,
                                                                                           FunctionNotSupportedException,
                                                                                           RelationshipNotDeletedException,
                                                                                           UnexpectedResponseException
    ;
    /**
     *  Purge a synonym relationship. A link between glossary terms that have the same meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     RelationshipNotPurgedException,
                                                                     UnrecognizedGUIDException,
                                                                     MetadataServerUncontactableException,
                                                                     UnexpectedResponseException, RelationshipNotPurgedException
    ;
    /**
     * Restore a Synonym relationship
     *
     * Restore allows the deleted Synonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Synonym relationship to delete
     * @return response which when successful contains the restored Synonym relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    Synonym restoreSynonymRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;
    /**
     *  Create a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     *
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
    Antonym createAntonymRelationship(String userId, Antonym antonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     *  Get a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     *
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
    Antonym getAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                               MetadataServerUncontactableException, UserNotAuthorizedException,
                                                                                               UnexpectedResponseException,
                                                                                               UnrecognizedGUIDException
    ;
    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     *
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
    Antonym updateAntonymRelationship(String userId, Antonym antonymRelationship)  throws InvalidParameterException,
                                                                                                                    MetadataServerUncontactableException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    UnexpectedResponseException,
                                                                                                                    UnrecognizedGUIDException ;
    /**
     * Replace an Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     *
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
    Antonym replaceAntonymRelationship(String userId, Antonym antonymRelationship)  throws InvalidParameterException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException;
    /**
     *  Delete a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    Antonym deleteAntonymRelationship(String userId, String guid) throws
                                                                                           InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UserNotAuthorizedException,
                                                                                           UnrecognizedGUIDException,
                                                                                           FunctionNotSupportedException,
                                                                                           RelationshipNotDeletedException,
                                                                                           UnexpectedResponseException
    ;
    /**
     *  Purge a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     RelationshipNotPurgedException,
                                                                     UnrecognizedGUIDException,
                                                                     MetadataServerUncontactableException,
                                                                     UnexpectedResponseException, RelationshipNotPurgedException
    ;
    /**
     * Restore a Antonym relationship
     *
     * Restore allows the deleted Antonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Antonym relationship to delete
     * @return response which when successful contains the restored Antonym relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    Antonym restoreAntonymRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;
    /**
     *  Create a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * <p>
     *
     *
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
    Translation createTranslationRelationship(String userId, Translation translation) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       MetadataServerUncontactableException,
                                                                                                                       UnexpectedResponseException,
                                                                                                                       UnrecognizedGUIDException
    ;
    /**
     *  Get a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     *
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
    Translation getTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                       MetadataServerUncontactableException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       UnexpectedResponseException,
                                                                                                       UnrecognizedGUIDException
    ;
    /**
     * Update a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     *
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
    Translation updateTranslationRelationship(String userId, Translation translationRelationship)  throws InvalidParameterException,
                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    UnexpectedResponseException,
                                                                                                                                    UnrecognizedGUIDException ;
    /**
     * Replace an Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     *
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
    Translation replaceTranslationRelationship(String userId, Translation translationRelationship)  throws InvalidParameterException,
                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     UnexpectedResponseException,
                                                                                                                                     UnrecognizedGUIDException;
    /**
     *  Delete a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    Translation deleteTranslationRelationship(String userId, String guid) throws
                                                                                                   InvalidParameterException,
                                                                                                   MetadataServerUncontactableException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   UnrecognizedGUIDException,
                                                                                                   FunctionNotSupportedException,
                                                                                                   RelationshipNotDeletedException,
                                                                                                   UnexpectedResponseException
    ;
    /**
     *  Purge a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         RelationshipNotPurgedException,
                                                                         UnrecognizedGUIDException,
                                                                         MetadataServerUncontactableException,
                                                                         UnexpectedResponseException, RelationshipNotPurgedException
    ;
    /**
     * Restore a Translation relationship
     *
     * Restore allows the deleted Translation relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Translation relationship to delete
     * @return response which when successful contains the restored Translation relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    Translation restoreTranslationRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;
    /**
     *  Create a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * <p>
     *
     *
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
    UsedInContext createUsedInContextRelationship(String userId, UsedInContext usedInContext) throws InvalidParameterException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               MetadataServerUncontactableException,
                                                                                                                               UnexpectedResponseException,
                                                                                                                               UnrecognizedGUIDException
    ;
    /**
     *  Get a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     *
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
    UsedInContext getUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                           MetadataServerUncontactableException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           UnexpectedResponseException,
                                                                                                           UnrecognizedGUIDException
    ;
    /**
     * Update a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     *
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
    UsedInContext updateUsedInContextRelationship(String userId, UsedInContext usedInContextRelationship)  throws InvalidParameterException,
                                                                                                                                            MetadataServerUncontactableException,
                                                                                                                                            UserNotAuthorizedException,
                                                                                                                                            UnexpectedResponseException,
                                                                                                                                            UnrecognizedGUIDException ;
    /**
     * Replace an UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     *
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
    UsedInContext replaceUsedInContextRelationship(String userId, UsedInContext usedInContextRelationship)  throws InvalidParameterException,
                                                                                                                                             MetadataServerUncontactableException,
                                                                                                                                             UserNotAuthorizedException,
                                                                                                                                             UnexpectedResponseException,
                                                                                                                                             UnrecognizedGUIDException;
    /**
     *  Delete a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    UsedInContext deleteUsedInContextRelationship(String userId, String guid) throws
                                                                                                       InvalidParameterException,
                                                                                                       MetadataServerUncontactableException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       UnrecognizedGUIDException,
                                                                                                       FunctionNotSupportedException,
                                                                                                       RelationshipNotDeletedException,
                                                                                                       UnexpectedResponseException
    ;
    /**
     *  Purge a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    RelationshipNotPurgedException,
                                                                                                    UnrecognizedGUIDException,
                                                                                                    MetadataServerUncontactableException,
                                                                                                    UnexpectedResponseException
    ;
    /**
     * Restore a Used in context relationship
     *
     * Restore allows the deletedUsed in context relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Used in context relationship to delete
     * @return response which when successful contains the restored Used in context relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    UsedInContext restoreUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;
    /**
     *  Create a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * <p>
     *
     *
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
    PreferredTerm createPreferredTermRelationship(String userId, PreferredTerm preferredTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     *  Get a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     *
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
    PreferredTerm getPreferredTermRelationship(String userId, String guid) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     * Update a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     *
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
    PreferredTerm updatePreferredTermRelationship(String userId, PreferredTerm preferredTermRelationship)  throws InvalidParameterException,
                                                                                                                                            MetadataServerUncontactableException,
                                                                                                                                            UserNotAuthorizedException,
                                                                                                                                            UnexpectedResponseException,
                                                                                                                                            UnrecognizedGUIDException ;
    /**
     * Replace an PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     *
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
    PreferredTerm replacePreferredTermRelationship(String userId, PreferredTerm preferredTermRelationship)  throws InvalidParameterException,
                                                                                                                                             MetadataServerUncontactableException,
                                                                                                                                             UserNotAuthorizedException,
                                                                                                                                             UnexpectedResponseException,
                                                                                                                                             UnrecognizedGUIDException;
    /**
     *  Delete a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    PreferredTerm deletePreferredTermRelationship(String userId, String guid) throws
                                                                                                       InvalidParameterException,
                                                                                                       MetadataServerUncontactableException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       UnrecognizedGUIDException,
                                                                                                       FunctionNotSupportedException,
                                                                                                       RelationshipNotDeletedException,
                                                                                                       UnexpectedResponseException
    ;
    /**
     *  Purge a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgePreferredTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    RelationshipNotPurgedException,
                                                                                                    UnrecognizedGUIDException,
                                                                                                    MetadataServerUncontactableException,
                                                                                                    UnexpectedResponseException
    ;
    /**
     * Restore a preferred term relationship
     *
     * Restore allows the deletedpreferred term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the preferred term relationship to delete
     * @return response which when successful contains the restored preferred term relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    PreferredTerm restorePreferredTermRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;
    /**
     *  Create a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * <p>
     *
     *
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
    ValidValue createValidValueRelationship(String userId, ValidValue validValue) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   MetadataServerUncontactableException,
                                                                                                                   UnexpectedResponseException,
                                                                                                                   UnrecognizedGUIDException
    ;
    /**
     *  Get a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     *
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
    ValidValue getValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                     MetadataServerUncontactableException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     UnexpectedResponseException,
                                                                                                     UnrecognizedGUIDException
    ;
    /**
     * Update a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     *
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
    ValidValue updateValidValueRelationship(String userId, ValidValue validValueRelationship)  throws InvalidParameterException,
                                                                                                                                MetadataServerUncontactableException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                UnexpectedResponseException,
                                                                                                                                UnrecognizedGUIDException ;
    /**
     * Replace an ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     *
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
    ValidValue replaceValidValueRelationship(String userId, ValidValue validValueRelationship)  throws InvalidParameterException,
                                                                                                                                 MetadataServerUncontactableException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 UnexpectedResponseException,
                                                                                                                                 UnrecognizedGUIDException;
    /**
     *  Delete a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    ValidValue deleteValidValueRelationship(String userId, String guid) throws
                                                                                                 InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnrecognizedGUIDException,
                                                                                                 FunctionNotSupportedException,
                                                                                                 RelationshipNotDeletedException,
                                                                                                 UnexpectedResponseException
    ;
    /**
     *  Purge a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 RelationshipNotPurgedException,
                                                                                                 UnrecognizedGUIDException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UnexpectedResponseException
    ;
    /**
     * Restore a valid value relationship
     *
     * Restore allows the deletedvalid value relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the valid value relationship to delete
     * @return response which when successful contains the restored valid value relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    ValidValue restoreValidValueRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;
    /**
     *  Create a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * <p>
     *
     *
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
    ReplacementTerm createReplacementTermRelationship(String userId, ReplacementTerm replacementTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     *  Get a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     *
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
    ReplacementTerm getReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                               MetadataServerUncontactableException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               UnexpectedResponseException,
                                                                                                               UnrecognizedGUIDException
    ;
    /**
     * Update a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     *
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
    ReplacementTerm updateReplacementTermRelationship(String userId, ReplacementTerm replacementTermRelationship)  throws InvalidParameterException,
                                                                                                                                                    MetadataServerUncontactableException,
                                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                                    UnexpectedResponseException,
                                                                                                                                                    UnrecognizedGUIDException ;
    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     *
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
    ReplacementTerm replaceReplacementTermRelationship(String userId, ReplacementTerm replacementTermRelationship)  throws InvalidParameterException,
                                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                                     UnexpectedResponseException,
                                                                                                                                                     UnrecognizedGUIDException;
    /**
     *  Delete a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    ReplacementTerm deleteReplacementTermRelationship(String userId, String guid) throws
                                                                                                           InvalidParameterException,
                                                                                                           MetadataServerUncontactableException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           UnrecognizedGUIDException,
                                                                                                           FunctionNotSupportedException,
                                                                                                           RelationshipNotDeletedException,
                                                                                                           UnexpectedResponseException
    ;
    /**
     *  Purge a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      RelationshipNotPurgedException,
                                                                                                      UnrecognizedGUIDException,
                                                                                                      MetadataServerUncontactableException,
                                                                                                      UnexpectedResponseException
    ;
    /**
     * Restore a replacement term relationship
     *
     * Restore allows the deleted replacement term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the replacement term relationship to delete
     * @return response which when successful contains the restored replacement term relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    ReplacementTerm restoreReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;
    /**
     *  Create a TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     *
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationship the TypedBy relationship
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
    TypedBy createTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     *  Get a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return TypedBy
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
    TypedBy getTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException,
                                                                          MetadataServerUncontactableException,
                                                                          UserNotAuthorizedException,
                                                                          UnexpectedResponseException,
                                                                          UnrecognizedGUIDException
    ;
    /**
     * Update a TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationship the TypedBy relationship
     * @return  TypedBy updated termTYPEDBYRelationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server

     */
    TypedBy updateTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException ;
    /**
     * Replace an TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationship the termTYPEDBYRelationship relationship
     * @return  TypedBy replaced termTYPEDBYRelationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    TypedBy replaceTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException,
                                                                                                  MetadataServerUncontactableException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  UnexpectedResponseException,
                                                                                                  UnrecognizedGUIDException;
    /**
     *  Delete a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted TypedBy
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
    TypedBy deleteTermTYPEDBYRelationship(String userId, String guid) throws
                                                                                                                           InvalidParameterException,
                                                                                                                           MetadataServerUncontactableException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           UnrecognizedGUIDException,
                                                                                                                           FunctionNotSupportedException,
                                                                                                                           RelationshipNotDeletedException,
                                                                                                                           UnexpectedResponseException
    ;
    /**
     *  Purge a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TypedBy relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              RelationshipNotPurgedException,
                                                                                                              UnrecognizedGUIDException,
                                                                                                              MetadataServerUncontactableException,
                                                                                                              UnexpectedResponseException
    ;
    /**
     * Restore a typed by relationship
     *
     * Restore allows the deleted typed by relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the typed by relationship to delete
     * @return response which when successful contains the restored typed by relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    TypedBy restoreTypedByRelationship(String userId, String guid) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          MetadataServerUncontactableException,
                                                                          UnexpectedResponseException,
                                                                          UnrecognizedGUIDException
    ;
    /**
     *  Create a Isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * <p>
     *
     *
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
    Isa createIsaRelationship(String userId, Isa isa) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     *  Get a isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     *
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
    Isa getIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                              MetadataServerUncontactableException,
                                                              UserNotAuthorizedException,
                                                              UnexpectedResponseException,
                                                              UnrecognizedGUIDException
    ;
    /**
     * Update a Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     *
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
    Isa updateIsaRelationship(String userId, Isa isaRelationship) throws InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UserNotAuthorizedException,
                                                                         UnexpectedResponseException,
                                                                         UnrecognizedGUIDException ;
    /**
     * Replace an Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     *
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
    Isa replaceIsaRelationship(String userId, Isa isaRelationship) throws InvalidParameterException,
                                                                          MetadataServerUncontactableException,
                                                                          UserNotAuthorizedException,
                                                                          UnexpectedResponseException,
                                                                          UnrecognizedGUIDException;
    /**
     *  Delete a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
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
    Isa deleteIsaRelationship(String userId, String guid) throws
                                                                                               InvalidParameterException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UserNotAuthorizedException,
                                                                                               UnrecognizedGUIDException,
                                                                                               FunctionNotSupportedException,
                                                                                               RelationshipNotDeletedException,
                                                                                               UnexpectedResponseException
    ;
    /**
     *  Purge a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Isa relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          RelationshipNotPurgedException,
                                                                                          UnrecognizedGUIDException,
                                                                                          MetadataServerUncontactableException,
                                                                                          UnexpectedResponseException
    ;
    /**
     * Restore an is a relationship
     *
     * Restore allows the deleted is a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the is a relationship to delete
     * @return response which when successful contains the restored is a relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    Isa restoreIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  MetadataServerUncontactableException,
                                                                  UnexpectedResponseException,
                                                                  UnrecognizedGUIDException
    ;
    /**
     *  Create a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     *
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param TermISATypeOFRelationship the IsaTypeOf relationship
     * @return the created IsaTypeOf relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    IsaTypeOf createTermISATypeOFRelationship(String userId, IsaTypeOf TermISATypeOFRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     *  Get a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return IsaTypeOf
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
    IsaTypeOf getTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException,
                                                                              MetadataServerUncontactableException,
                                                                              UserNotAuthorizedException,
                                                                              UnexpectedResponseException,
                                                                              UnrecognizedGUIDException
    ;
    /**
     * Update a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param isATypeOf            the IsaTypeOf relationship
     * @return  IsaTypeOf updated IsaTypeOf
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    IsaTypeOf updateTermISATypeOFRelationship(String userId, IsaTypeOf isATypeOf) throws InvalidParameterException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UserNotAuthorizedException,
                                                                                         UnexpectedResponseException,
                                                                                         UnrecognizedGUIDException ;
    /**
     * Replace an isATypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param isATypeOf the isATypeOf relationship
     * @return  isATypeOf replaced isATypeOf
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    IsaTypeOf replaceTermISATypeOFRelationship(String userId, IsaTypeOf isATypeOf) throws InvalidParameterException,
                                                                                          MetadataServerUncontactableException,
                                                                                          UserNotAuthorizedException,
                                                                                          UnexpectedResponseException,
                                                                                          UnrecognizedGUIDException;
    /**
     *  Delete a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted IsaTypeOf
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
    IsaTypeOf deleteTermISATypeOFRelationship(String userId, String guid) throws
                                                                                                                               InvalidParameterException,
                                                                                                                               MetadataServerUncontactableException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               UnrecognizedGUIDException,
                                                                                                                               FunctionNotSupportedException,
                                                                                                                               RelationshipNotDeletedException,
                                                                                                                               UnexpectedResponseException
    ;
    /**
     *  Purge a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                RelationshipNotPurgedException,
                                                                                                                UnrecognizedGUIDException,
                                                                                                                MetadataServerUncontactableException,
                                                                                                                UnexpectedResponseException
    ;
    /**
     * Restore an is a type of relationship
     *
     * Restore allows the deleted is a type of relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the is a type of relationship to delete
     * @return response which when successful contains the restored is a type of relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    IsaTypeOf restoreIsaTypeOfRelationship(String userId, String guid) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              MetadataServerUncontactableException,
                                                                              UnexpectedResponseException,
                                                                              UnrecognizedGUIDException
    ;

    /**
     * Create a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized. 
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the created term categorization relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    Categorization createTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             MetadataServerUncontactableException,
                                                                                                                             UnexpectedResponseException,
                                                                                                                             UnrecognizedGUIDException
    ;
    /**
     * Get a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to get
     * @return TermCategorizationRelationship
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
    Categorization getTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                        MetadataServerUncontactableException,
                                                                                        UserNotAuthorizedException,
                                                                                        UnexpectedResponseException,
                                                                                        UnrecognizedGUIDException
    ;
    /**
     * Update a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the updated term categorization relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    Categorization updateTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
    ;
    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the replaced term categorization relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    Categorization replaceTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              MetadataServerUncontactableException,
                                                                                                                              UnexpectedResponseException,
                                                                                                                              UnrecognizedGUIDException
    ;
    /**
     * Delete a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.      * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * @return Deleted TermCategorizationRelationship
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
    Categorization deleteTermCategorizationRelationship(String userId, String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            UnrecognizedGUIDException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException
    ;
    /**
     * Purge a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            RelationshipNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    ;
    /**
     * Restore a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * Restore allows the deleted Term Categorization relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Term Categorization relationship to delete
     * @return response which when successful contains the restored has a relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    Categorization restoreTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            MetadataServerUncontactableException,
                                                                                            UnexpectedResponseException,
                                                                                            UnrecognizedGUIDException
    ;

    /**
     * Create a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * This method does not error if the relationship ends are not spine objects or spine attributes.
     * Terms created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Terms that have not been created via the Subject Area OMAS or to recreate
     * the TermAnchor relationship if it has been purged.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termAnchorRelationship the TermAnchor relationship
     * @return the created TermAnchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    TermAnchor createTermAnchorRelationship(String userId, TermAnchor termAnchorRelationship) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     MetadataServerUncontactableException,
                                                                                                     UnexpectedResponseException,
                                                                                                     UnrecognizedGUIDException
    ;
    /**
     * Get a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to get
     * @return TermAnchorRelationship
     * Exceptions returned by the server
     * @throws  UserNotAuthorizedException          the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     *
     */
    TermAnchor getTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                            MetadataServerUncontactableException,
                                                                            UserNotAuthorizedException,
                                                                            UnexpectedResponseException,
                                                                            UnrecognizedGUIDException
    ;

    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param termAnchorRelationship the TermAnchor relationship
     * @return the updated TermAnchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    TermAnchor replaceTermAnchorRelationship(String userId, TermAnchor termAnchorRelationship) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      MetadataServerUncontactableException,
                                                                                                      UnexpectedResponseException,
                                                                                                      UnrecognizedGUIDException
    ;
    /**
     * Delete a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * @return Deleted TermAnchorRelationship
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
    TermAnchor deleteTermAnchorRelationship(String userId, String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            UnrecognizedGUIDException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException
    ;
    /**
     * Purge a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * A purge means that the relationship will not exist after the operation.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            RelationshipNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    ;
    /**
     * Restore a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     *
     * Restore allows the deleted Term Categorization relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Term Anchor relationship to delete
     * @return response which when successful contains the restored Term Anchor relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    TermAnchor restoreTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                MetadataServerUncontactableException,
                                                                                UnexpectedResponseException,
                                                                                UnrecognizedGUIDException
    ;

    /**
     * Create a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     * Categories created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Category. This method is to allow glossaries to be associated with Categories that have not been created via the Subject Area OMAS or to recreate
     * the CategoryAnchor relationship if it has been purged.
     *
     * @param userId               userId under which the request is performed
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the created term categorization relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    CategoryAnchor createCategoryAnchorRelationship(String userId, CategoryAnchor categoryAnchorRelationship) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException
    ;
    /**
     * Get a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to get
     * @return CategoryAnchorRelationship
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
    CategoryAnchor getCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UserNotAuthorizedException,
                                                                                    UnexpectedResponseException,
                                                                                    UnrecognizedGUIDException
    ;
    /**
     *Update a Category Anchor Relationship.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the updated category anchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    CategoryAnchor updateCategoryAnchorRelationship(String userId, CategoryAnchor categoryAnchorRelationship) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException
    ;

    /**
     * Replace a Category Anchor Relationship.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the replaced category anchor relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    CategoryAnchor replaceCategoryAnchorRelationship(String userId, CategoryAnchor categoryAnchorRelationship) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      MetadataServerUncontactableException,
                                                                                                                      UnexpectedResponseException,
                                                                                                                      UnrecognizedGUIDException
    ;
    /**
     * Delete a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * @return Deleted CategoryAnchorRelationship
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
    CategoryAnchor deleteCategoryAnchorRelationship(String userId, String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            UnrecognizedGUIDException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException
    ;
    /**
     * Purge a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    void purgeCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            RelationshipNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    ;
    /**
     * Restore a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     *
     * Restore allows the deleted Category Anchor relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Category Anchor relationship to delete
     * @return response which when successful contains the restored category anchor relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    CategoryAnchor restoreCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        MetadataServerUncontactableException,
                                                                                        UnexpectedResponseException,
                                                                                        UnrecognizedGUIDException
    ;
    /**
     *  Create a ProjectScope relationship. A link between the project content and the project.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param projectScope the ProjectScope relationship
     * @return the created ProjectScope relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    ProjectScope createProjectScopeRelationship(String userId, ProjectScope projectScope) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException
   ;
    /**
     *  Get a ProjectScope relationship. A link between the project content and the project.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return ProjectScope
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
    ProjectScope getProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                MetadataServerUncontactableException,
                                                                                UserNotAuthorizedException,
                                                                                UnexpectedResponseException,
                                                                                UnrecognizedGUIDException
  ;
    /**
     * Update a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param projectScopeRelationship the ProjectScope relationship
     * @return updated ProjectScope relationship
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    ProjectScope updateProjectScopeRelationship(String userId, ProjectScope projectScopeRelationship) throws InvalidParameterException,
                                                                                                             MetadataServerUncontactableException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             UnexpectedResponseException,
                                                                                                             UnrecognizedGUIDException
        ;
    /**
     * Replace a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     *
     * @param userId               userId under which the request is performed
     * @param projectScopeRelationship the ProjectScope relationship
     * @return replaced ProjectScope relationship
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    ProjectScope replaceProjectScopeRelationship(String userId, ProjectScope projectScopeRelationship) throws InvalidParameterException,
                                                                                                              MetadataServerUncontactableException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              UnexpectedResponseException,
                                                                                                              UnrecognizedGUIDException
;

    /**
     *  Delete a ProjectScope relationship. A link between the project content and the project.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @return deleted ProjectScope
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
    ProjectScope deleteProjectScopeRelationship(String userId, String guid) throws
            InvalidParameterException,
            MetadataServerUncontactableException,
            UnrecognizedGUIDException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            RelationshipNotDeletedException,
            UnexpectedResponseException
   ;


    /**
     *  Purge a ProjectScope relationship. A link between the project content and the project.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to delete
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws RelationshipNotPurgedException               a hard delete was issued but the relationship was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
     void purgeProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            RelationshipNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            UnexpectedResponseException
    ;
    /**
     * Restore a ProjectScope relationship which  is a link between the project content and the project.
     *
     * Restore allows the deleted ProjectScope relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ProjectScope relationship to restore
     * @return response which when successful contains the restored ProjectScope relationship
     * when not successful the following Exception responses can occur
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
     ProjectScope restoreProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException
   ;


    
    /**
     * Get a SemanticAssignment relationship,  Links a glossary term to another element such as an asset or schema element to define its meaning.
     *
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the SemanticAssignment relationship to get
     * @return  the SemanticAssignment relationship with the requested guid
     *  Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    SemanticAssignment getSemanticAssignmentRelationship(String userId,String guid)  throws InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            UnexpectedResponseException,
            UnrecognizedGUIDException
    ;

}