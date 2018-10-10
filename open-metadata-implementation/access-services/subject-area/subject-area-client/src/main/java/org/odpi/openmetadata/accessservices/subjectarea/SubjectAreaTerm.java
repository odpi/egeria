/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

/**
 * The SubjectArea Open Metadata Access Service (OMAS) API for terms.
 */
public interface SubjectAreaTerm
{

    /**
     * Create a Term
     * @param userid unique identifier for requesting user, under which the request is performed
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

    public Term createTerm(String userid, Term suppliedTerm)
            throws MetadataServerUncontactableException,
            InvalidParameterException,
            UserNotAuthorizedException,
            ClassificationException,
            FunctionNotSupportedException,
            UnexpectedResponseException ;

    /**
     * Get a term by guid.
     * @param userid unique identifier for requesting user, under which the request is performed
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

    public  Term getTermByGuid( String userid, String guid)
            throws MetadataServerUncontactableException,
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            UnexpectedResponseException;

    /**
     * Replace a Term. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     *
     * @param userid           unique identifier for requesting user, under which the request is performed
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
    public Term replaceTerm(String userid, String guid, Term suppliedTerm) throws
            UnexpectedResponseException,
            UserNotAuthorizedException,
            UnrecognizedNameException,
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
     * @param userid           unique identifier for requesting user, under which the request is performed
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
    public Term updateTerm(String userid, String guid, Term suppliedTerm) throws UnexpectedResponseException,
            UserNotAuthorizedException,
            UnrecognizedNameException,
            FunctionNotSupportedException,
            InvalidParameterException,
            MetadataServerUncontactableException;

    /**
     * Delete a Term instance
     *
     * A delete (also known as a soft delete) means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userid unique identifier for requesting user, under which the request is performed
     * @param guid guid of the term to be deleted.
     * @return the deleted term
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws EntityNotDeletedException a delete was issued but the term was not deleted.
     * @throws MetadataServerUncontactableException unable to contact server
     */

    public Term deleteTerm(String userid,String guid) throws InvalidParameterException,
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            FunctionNotSupportedException,
            UnexpectedResponseException,
            EntityNotDeletedException;
    /**
     * Purge a Term instance
     *
     * A purge means that the term will not exist after the operation.
     *
     * @param userid unique identifier for requesting user, under which the request is performed
     * @param guid guid of the term to be deleted.
     *
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws GUIDNotPurgedException a hard delete was issued but the term was not purged
     * @throws MetadataServerUncontactableException unable to contact server
     */
    public  void purgeTerm(String userid,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            GUIDNotPurgedException,
            UnexpectedResponseException;
}
