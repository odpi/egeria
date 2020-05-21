/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * The term handler is initialised with a SubjectAreaTerm, that contains the server the call should be sent to.
 * The handler exposes methods for term functionality for the glossary author view
 */
public class TermHandler
{
    private static final Logger log = LoggerFactory.getLogger(TermHandler.class);

    private SubjectAreaTerm subjectAreaTerm;

    /**
     * Constructor for the TermHandler
     * @param subjectAreaTerm The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for terms. This is the same as the
     *                           The SubjectAreaDefinition Open Metadata View Service (OMVS) API for terms.
     */
    public TermHandler(SubjectAreaTerm subjectAreaTerm) {
      this.subjectAreaTerm =subjectAreaTerm;
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
    public Term createTerm(String userId, Term suppliedTerm) throws MetadataServerUncontactableException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaTerm.createTerm(userId, suppliedTerm);
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
    public Term getTermByGuid(String userId, String guid) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaTerm.getTermByGuid(userId, guid);
    }
    /**
     * Find Term
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Term property values (this does not include the TermSummary content).
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Terms meeting the search Criteria
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
    public List<Term> findTerm(String userId, String searchCriteria, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaTerm.findTerm(userId, searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }
    /**
     * Replace a Term. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     * The GovernanceAction content if specified replaces what is on the server.
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
    public Term replaceTerm(String userId, String guid, Term suppliedTerm) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaTerm.replaceTerm(userId, guid, suppliedTerm);
    }
    /**
     * Update a Term. This means to update the term with any non-null attributes from the supplied term.
     * <p>
     * Status is not updated using this call.
     * The GovernanceAction content if specified replaces what is on the server.
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
    public Term updateTerm(String userId, String guid, Term suppliedTerm) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaTerm.updateTerm(userId, guid, suppliedTerm);
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
    public Term deleteTerm(String userId, String guid) throws  MetadataServerUncontactableException,UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, EntityNotDeletedException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaTerm.deleteTerm(userId,guid);
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
     * @throws EntityNotPurgedException a hard delete was issued but the term was not purged
     * @throws UnrecognizedGUIDException            the supplied userId was not recognised
     * @throws FunctionNotSupportedException   Function not supported
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server     */
    public void purgeTerm(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, EntityNotPurgedException, UnexpectedResponseException, FunctionNotSupportedException, InvalidParameterException, UserNotAuthorizedException {
        subjectAreaTerm.purgeTerm(userId,guid);
    }
    /**
     * Restore a Term
     *
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the term to restore
     * @return the restored term
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Term restoreTerm(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaTerm.restoreTerm(userId, guid);
    }
    /**
     * Get Term relationships
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Term guid
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
    public List<Line> getTermRelationships(String userId,
                                    String guid,
                                    Date asOfTime,
                                    int offset,
                                    int pageSize,
                                    SequencingOrder sequencingOrder,
                                    String sequencingProperty) throws
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            UnexpectedResponseException {
        return subjectAreaTerm.getTermRelationships(userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);

    }

}
