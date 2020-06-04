/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectArea OMAS.
 * This interface provides term authoring interface for subject area experts.
 */
public class SubjectAreaTermImpl extends SubjectAreaBaseImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermImpl.class);

    private static final String className = SubjectAreaTermImpl.class.getName();
    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "terms";

    /**
     * Constructor for no authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException there is a problem creating the client-side components to issue any
     *                                                                                    REST API calls.
     */
    public SubjectAreaTermImpl(String serverName, String serverPlatformURLRoot) throws
                                                                                org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    @Override
    public Term createTerm(String userId, Term suppliedTerm) throws
                                                             MetadataServerUncontactableException,
                                                             InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             ClassificationException,
                                                             FunctionNotSupportedException,
                                                             UnexpectedResponseException, PropertyServerException {
        final String methodName = "createTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String urlTemplate = BASE_URL + "/%s";
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, urlTemplate, suppliedTerm);
        Term term = DetectUtils.detectAndReturnTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    @Override
    public Term getTermByGuid(String userId, String guid) throws MetadataServerUncontactableException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException, PropertyServerException {
        final String methodName = "getCategoryByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s";
        SubjectAreaOMASAPIResponse response = getByIdRESTCall(userId, guid, methodName, urlTemplate);
        Term term = DetectUtils.detectAndReturnTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    @Override
    public List<Line> getTermRelationships(String userId, String guid,
                                           Date asOfTime,
                                           int offset,
                                           int pageSize,
                                           org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                           String sequencingProperty) throws
                                                                      UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      FunctionNotSupportedException,
                                                                      UnexpectedResponseException,
                                                                      MetadataServerUncontactableException,
                                                                      PropertyServerException {
        final String methodName = "getRelationships";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        List<Line> relationships = getRelationships(BASE_URL, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relationships;
    }

    @Override
    public Term replaceTerm(String userId, String guid, Term suppliedTerm) throws
                                                                           UnexpectedResponseException,
                                                                           UserNotAuthorizedException,
                                                                           FunctionNotSupportedException,
                                                                           InvalidParameterException,
                                                                           MetadataServerUncontactableException,
                                                                           PropertyServerException {
        final String methodName = "replaceTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        Term term = updateTerm(userId, guid, suppliedTerm, true);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    @Override
    public Term updateTerm(String userId, String guid, Term suppliedTerm) throws UnexpectedResponseException,
                                                                                 UserNotAuthorizedException,
                                                                                 FunctionNotSupportedException,
                                                                                 InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 PropertyServerException {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Term term = updateTerm(userId, guid, suppliedTerm, false);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;

    }

    @Override
    public Term deleteTerm(String userId, String guid) throws InvalidParameterException,
                                                              MetadataServerUncontactableException,
                                                              UserNotAuthorizedException,
                                                              FunctionNotSupportedException,
                                                              UnrecognizedGUIDException,
                                                              UnexpectedResponseException,
                                                              EntityNotDeletedException, PropertyServerException {
        final String methodName = "deleteTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s?isPurge=false";
        SubjectAreaOMASAPIResponse response = deleteEntityRESTCall(userId, guid, methodName, urlTemplate);
        Term term = DetectUtils.detectAndReturnTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    @Override
    public void purgeTerm(String userId, String guid) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             MetadataServerUncontactableException,
                                                             UnrecognizedGUIDException,
                                                             FunctionNotSupportedException,
                                                             EntityNotPurgedException,
                                                             PropertyServerException {
        final String methodName = "purgeTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s?isPurge=true";
        purgeEntityRESTCall(userId, guid, methodName, urlTemplate);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Update Term.
     * <p>
     * If the caller has chosen to incorporate the term name in their Term Terms qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * The GovernanceAction content if specified replaces what is on the server.
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid  of the term to update
     * @param suppliedTerm Term to be updated
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated term.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    private Term updateTerm(String userId, String guid, Term suppliedTerm, boolean isReplace) throws
                                                                                              UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              FunctionNotSupportedException,
                                                                                              MetadataServerUncontactableException,
                                                                                              UnexpectedResponseException,
                                                                                              PropertyServerException {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s?isReplace=%b";
        SubjectAreaOMASAPIResponse response = putRESTCall(userId, guid, isReplace, methodName, urlTemplate, suppliedTerm);

        Term term = DetectUtils.detectAndReturnTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to restore
     * @return the restored term
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Term restoreTerm(String userId, String guid) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               MetadataServerUncontactableException,
                                                               UnrecognizedGUIDException,
                                                               FunctionNotSupportedException,
                                                               UnexpectedResponseException,
                                                               PropertyServerException {
        final String methodName = "restoreTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s";
        SubjectAreaOMASAPIResponse response = restoreRESTCall(userId, methodName, urlTemplate, null);
        Term term = DetectUtils.detectAndReturnTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return term;
    }

    /**
     * Find Term
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Term property values (this does not include the GlossarySummary content).
     * @param asOfTime           the Terms returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Terms meeting the search Criteria
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public List<Term> findTerm(String userId,
                               String searchCriteria,
                               Date asOfTime,
                               int offset,
                               int pageSize,
                               org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                               String sequencingProperty) throws
                                                          MetadataServerUncontactableException,
                                                          UserNotAuthorizedException,
                                                          InvalidParameterException,
                                                          FunctionNotSupportedException,
                                                          UnexpectedResponseException,
                                                          PropertyServerException {

        final String methodName = "findTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = findRESTCall(userId,
                                                           methodName,
                                                           BASE_URL,
                                                           searchCriteria,
                                                           asOfTime,
                                                           offset,
                                                           pageSize,
                                                           sequencingOrder,
                                                           sequencingProperty);
        List<Term> terms = DetectUtils.detectAndReturnTerms(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return terms;
    }

}
