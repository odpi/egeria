/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.viewservices.subjectarea.services;


import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * The SubjectAreaViewTermRESTServices provides the org.odpi.openmetadata.viewservices.subjectarea.services implementation of the SubjectArea Open Metadata
 * View Service (OMVS). This interface provides view term authoring interfaces for subject area experts.
 */

public class SubjectAreaViewTermRESTServices extends BaseSubjectAreaView {

    private static String className = SubjectAreaViewTermRESTServices.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);

    /**
     * Default constructor
     */
    public SubjectAreaViewTermRESTServices() {

    }

    /**
     * Create a Term. There are specializations of terms that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Term in the supplied term.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalTerm to create a canonical term </li>
     * <li>TaxonomyAndCanonicalTerm to create a term that is both a taxonomy and a canonical glosary </li>
     * <li>Term to create a term that is not a taxonomy or a canonical term</li>
     * </ul>
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param suppliedTerm Term to create
     * @return response, when successful contains the created term.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse createTerm(String serverName, String userId, Term suppliedTerm) {
        SubjectAreaOMASAPIResponse response;

        try {
            Term term = instanceHandler.createTerm(serverName, userId, suppliedTerm);
            TermResponse termResponse = new TermResponse();
            termResponse.setTerm(term);
            response = termResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }

    /**
     * Get a term.
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTerm(String serverName, String userId, String guid) {
        SubjectAreaOMASAPIResponse response;

        try {
            Term term = instanceHandler.getTermByGuid(serverName, userId, guid);
            TermResponse termResponse = new TermResponse();
            termResponse.setTerm(term);
            response = termResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }

        return response;
    }

    /**
     * Find Term
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param searchCriteria     String expression matching Term property values .
     * @param asOfTime           the terms returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse findTerm(
            String serverName,
            String userId,
            Date asOfTime,
            String searchCriteria,
            Integer offset,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty
    ) {
        SubjectAreaOMASAPIResponse response = null;

        try {
            if (offset == null) {
                offset = new Integer(0);
            }
            if (pageSize == null) {
                pageSize = new Integer(0);
            }
            List<Term> terms = instanceHandler.findTerm(
                    serverName,
                    userId,
                    searchCriteria,
                    asOfTime,
                    offset,
                    pageSize,
                    sequencingOrder,
                    sequencingProperty);
            TermsResponse termsResponse = new TermsResponse();
            termsResponse.setTerms(terms);
            response = termsResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }

    /**
     * Get Term relationships
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid               guid of the term to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the term relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getTermRelationships(
            String serverName,
            String userId,
            String guid,
            Date asOfTime,
            Integer offset,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty


    ) {

        SubjectAreaOMASAPIResponse response;

        try {
            List<Line> lines =instanceHandler.getTermRelationships(serverName, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
            LinesResponse linesResponse = new LinesResponse();
            linesResponse.setLines(lines);
            response = linesResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;

    }

    /**
     * Update a Term
     * <p>
     * If the caller has chosen to incorporate the term name in their Term Terms or Categories qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * If the caller has chosen to incorporate the term qualifiedName in their Term Terms or Categories qualified name, changing the qualified name of the term will cause those
     * qualified names to mismatch the Term name.
     * Status is not updated using this call.
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the term to update
     * @param term   term to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse updateTerm(
            String serverName,
            String userId,
            String guid,
            Term term,
            Boolean isReplace
    ) {
        SubjectAreaOMASAPIResponse response = null;

        try {
            Term updatedTerm;
            if (isReplace == null) {
                isReplace = false;
            }
            if (isReplace) {
                updatedTerm = instanceHandler.replaceTerm(serverName, userId, guid, term);
            } else {
                updatedTerm = instanceHandler.updateTerm(serverName, userId, guid, term);
            }
            TermResponse termResponse = new TermResponse();
            termResponse.setTerm(updatedTerm);
            response = termResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }

    /**
     * Delete a Term instance
     * <p>
     * The deletion of a term is only allowed if there is no term content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the term to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the term was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTerm(
            String serverName,
            String userId,
            String guid,
            Boolean isPurge
    ) {
        SubjectAreaOMASAPIResponse response = null;

        try {
            if (isPurge == null) {
                // default to soft delete if isPurge is not specified.
                isPurge = false;
            }

            if (isPurge) {
                instanceHandler.purgeTerm(serverName, userId, guid);
                response = new VoidResponse();
            } else {
                Term term = instanceHandler.deleteTerm(serverName, userId, guid);
                TermResponse termResponse = new TermResponse();
                termResponse.setTerm(term);
                response = termResponse;
            }
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the term to restore
     * @return response which when successful contains the restored term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTerm(
            String serverName,
            String userId,
            String guid) {
        SubjectAreaOMASAPIResponse response;

        try {
            Term term = instanceHandler.restoreTerm(serverName, userId, guid);
            TermResponse termResponse = new TermResponse();
            termResponse.setTerm(term);
            response = termResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }
}
