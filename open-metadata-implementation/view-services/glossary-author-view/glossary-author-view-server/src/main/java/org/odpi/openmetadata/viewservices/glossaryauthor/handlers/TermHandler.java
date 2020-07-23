/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.client.entities.terms.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The term handler is initialised with a SubjectAreaTerm, that contains the server the call should be sent to.
 * The handler exposes methods for term functionality for the glossary author view
 */
public class TermHandler {
    private SubjectAreaTerm subjectAreaTerm;

    /**
     * Constructor for the TermHandler
     *
     * @param subjectAreaTerm The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for terms. This is the same as the
     *                        The SubjectAreaDefinition Open Metadata View Service (OMVS) API for terms.
     */
    public TermHandler(SubjectAreaTerm subjectAreaTerm) {
        this.subjectAreaTerm = subjectAreaTerm;
    }

    /**
     * Create a Term
     *
     * @param userId       userId under which the request is performed
     * @param suppliedTerm Term to create
     * @return the created term.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Term createTerm(String userId, Term suppliedTerm) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return subjectAreaTerm.term().create(userId, suppliedTerm);
    }

    /**
     * Get a term by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the term to get
     * @return the requested term.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Term getTermByGuid(String userId, String guid) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException {
        return subjectAreaTerm.term().getByGUID(userId, guid);
    }

    /**
     * Find Term
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param findRequest        {@link FindRequest}
     * @return A list of Terms meeting the search Criteria
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public List<Term> findTerm(String userId, FindRequest findRequest) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaTerm.term().find(userId, findRequest);
    }

    /**
     * Replace a Term. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     * The GovernanceAction content if specified replaces what is on the server.
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @return replaced term
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Term replaceTerm(String userId, String guid, Term suppliedTerm) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException {
        return subjectAreaTerm.term().replace(userId, guid, suppliedTerm);
    }

    /**
     * Update a Term. This means to update the term with any non-null attributes from the supplied term.
     * <p>
     * Status is not updated using this call.
     * The GovernanceAction content if specified replaces what is on the server.
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @return a response which when successful contains the updated term
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Term updateTerm(String userId, String guid, Term suppliedTerm) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException {
        return subjectAreaTerm.term().update(userId, guid, suppliedTerm);
    }

    /**
     * Delete a Term instance
     * <p>
     * A delete (also known as a soft delete) means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the term to be deleted.
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public void deleteTerm(String userId, String guid) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException {
        subjectAreaTerm.term().delete(userId, guid);
    }

    /**
     * Purge a Term instance
     * <p>
     * A purge means that the term will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the term to be deleted.
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public void purgeTerm(String userId, String guid) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException {
        subjectAreaTerm.term().purge(userId, guid);
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to restore
     * @return the restored term
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Term restoreTerm(String userId, String guid) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException {
        return subjectAreaTerm.term().restore(userId, guid);
    }

    /**
     * Get Term relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the term to get
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested Term guid
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server exception
     */
    public List<Line> getTermRelationships(String userId, String guid, FindRequest findRequest) throws UserNotAuthorizedException, InvalidParameterException, PropertyServerException {
        return subjectAreaTerm.term().getRelationships(userId, guid, findRequest);

    }
}