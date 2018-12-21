/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for glossaries.
 */
public interface SubjectAreaGlossary
{
    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     *     <li>Taxonomy to create a Taxonomy </li>
     *     <li>CanonicalGlossary to create a canonical glossary </li>
     *     <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glosary </li>
     *     <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return the created glossary.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException  the supplied guid was not recognised
     * @throws ClassificationException Error processing a classification
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public Glossary createGlossary(String serverName, String userId, Glossary suppliedGlossary) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException ;
    /**
     * Get a glossary by guid.
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to get
     * @return the requested glossary.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public  Glossary getGlossaryByGuid(String serverName, String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException ;
    /**
     * Get a Glossary by name
     *
     * Glossaries should have unique names. If repositories were not able to contact each other on the network, it is possible that glossaries of the same
     * name might be added. If this has occured this operation may not retun the glossary you are interested in. The guid of the glossary is the way to
     * uniquely identify a glossary; a get for glossary by guid can be issued to find glossaries with particular guids.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId userId under which the request is performed
     * @param name name of the glossary to get
     * @return the requested glossary.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public  Glossary getGlossaryByName(String serverName, String userId, String name) throws MetadataServerUncontactableException,
                                                                                             UserNotAuthorizedException,
                                                                                             InvalidParameterException,
                                                                                             UnexpectedResponseException ;
    /**
     * Replace a Glossary. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           userId under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @return replaced glossary
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Glossary replaceGlossary(String serverName, String userId, String guid, Glossary suppliedGlossary) throws
                                                                                                              UnexpectedResponseException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              InvalidParameterException,
                                                                                                              MetadataServerUncontactableException ;
    /**
     * Update a Glossary. This means to update the glossary with any non-null attributes from the supplied glossary.
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           userId under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Glossary updateGlossary(String serverName, String userId, String guid, Glossary suppliedGlossary) throws UnexpectedResponseException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    InvalidParameterException,
                                                                                                                    MetadataServerUncontactableException ;

    /**
     * Delete a Glossary instance
     *
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     *
     * A delete (also known as a soft delete) means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to be deleted.
     * @return the deleted glossary
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws EntityNotDeletedException a delete was issued but the glossary was not deleted.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server     */

    public Glossary deleteGlossary(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                        MetadataServerUncontactableException,
                                                                                        UserNotAuthorizedException,
                                                                                        UnrecognizedGUIDException,
                                                                                        FunctionNotSupportedException,
                                                                                        UnexpectedResponseException,
                                                                                        EntityNotDeletedException ;
    /**
     * Purge a Glossary instance
     *
     * The purge of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     *
     * A purge means that the glossary will not exist after the operation.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to be deleted.
     *
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws GUIDNotPurgedException a hard delete was issued but the glossary was not purged
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public  void purgeGlossary(String serverName, String userId,String guid) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UnrecognizedGUIDException,
                                                                                    GUIDNotPurgedException,
                                                                                    UnexpectedResponseException ;
    /**
     * Restore a Glossary
     *
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to restore
     * @return the restored glossary
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public  Glossary restoreGlossary(String serverName, String userId,String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnrecognizedGUIDException,
            FunctionNotSupportedException,
            UnexpectedResponseException;
}
