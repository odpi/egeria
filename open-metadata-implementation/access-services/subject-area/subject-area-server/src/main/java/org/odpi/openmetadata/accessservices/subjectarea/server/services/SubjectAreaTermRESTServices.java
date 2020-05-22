/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaGlossaryHandler;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaTermHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ExceptionMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The SubjectAreaTermRESTServices provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides term authoring interfaces for subject area experts.
 */

public class SubjectAreaTermRESTServices extends SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermRESTServices.class);

    private static final String className = SubjectAreaTermRESTServices.class.getName();
    static private SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();


    public static final Set<String> SUBJECT_AREA_TERM_CLASSIFICATIONS= new HashSet(Arrays.asList(
            // spine objects
            SpineObject.class.getSimpleName(),
            SpineAttribute.class.getSimpleName(),
            ObjectIdentifier.class.getSimpleName(),
            //governance actions
            Confidentiality.class.getSimpleName(),
            Confidence.class.getSimpleName(),
            Criticality.class.getSimpleName(),
            Retention.class.getSimpleName(),
            // dictionary
            AbstractConcept.class.getSimpleName(),
            ActivityDescription.class.getSimpleName(),
            DataValue.class.getSimpleName(),
            // context
            ContextDefinition.class.getSimpleName()));

    /**
     * Default constructor
     */
    public SubjectAreaTermRESTServices()
    {
        //SubjectAreaRESTServicesInstance registers this omas.
    }
    public SubjectAreaTermRESTServices(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper =oMRSAPIHelper;
    }

    /**
     * Create a Term
     * <p>
     * The name needs to be specified - as this is the main identifier for the term. The name should be unique for canonical glossaries. This API does not police the uniqueness in this case.
     * <p>
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryTerm concatinated with the the guid.
     * <p>
     * Failure to create the Terms classifications, link to its glossary or its icon, results in the create failing and the term being deleted
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId
     * @param suppliedTerm term to create
     * @return response, when successful contains the created term.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTerm(String serverName, String userId, Term suppliedTerm)
    {
        final String methodName = "createTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaTermHandler handler = instanceHandler.getSubjectAreaTermHandler(userId, serverName, methodName);
            SubjectAreaGlossaryHandler glossaryHandler = instanceHandler.getSubjectAreaGlossaryHandler(userId, serverName, methodName);
            response = handler.createTerm(glossaryHandler,userId, suppliedTerm);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }

    /**
     * Get a Term
     *
     * @param serverName  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermByGuid(String serverName, String userId, String guid)
    {
        final String methodName = "getTermByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaTermHandler handler = instanceHandler.getSubjectAreaTermHandler(userId, serverName, methodName);
            response = handler.getTermByGuid(userId, guid);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }
    /**
     * Get Term relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Term guid
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public  SubjectAreaOMASAPIResponse getTermRelationships(String serverName,
                                                            String userId,
                                                            String guid,
                                                            Date asOfTime,
                                                            Integer offset,
                                                            Integer pageSize,
                                                            org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                            String sequencingProperty
    ) {
        String methodName = "getTermRelationships";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaTermHandler handler = instanceHandler.getSubjectAreaTermHandler(userId, serverName, methodName);
            response = handler.getTermRelationships(userId,
                                                    guid,
                                                    asOfTime,
                                                    offset,
                                                    pageSize,
                                                    sequencingOrder,
                                                    sequencingProperty);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }

        return response;



    }
    /**
     * Find Term
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Term property values (this does not include the TermSummary content). When not specified, all terms are returned.
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public  SubjectAreaOMASAPIResponse findTerm( String serverName, String userId,
                                                String searchCriteria,
                                                Date asOfTime,
                                                Integer offset,
                                                Integer pageSize,
                                                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                String sequencingProperty) {

        final String methodName = "findTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaTermHandler handler = instanceHandler.getSubjectAreaTermHandler(userId, serverName, methodName);
            response = handler.findTerm(userId, searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }

        return response;
    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated. The GovernanceAction content is always replaced.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTerm(String serverName, String userId, String guid, Term suppliedTerm, boolean isReplace)
    {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaTermHandler handler = instanceHandler.getSubjectAreaTermHandler(userId, serverName, methodName);
            response = handler.updateTerm(userId, guid, suppliedTerm, isReplace);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }

    /**
     * Delete a Term instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exception responses can occur
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param guid    guid of the term to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the term was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaTermHandler handler = instanceHandler.getSubjectAreaTermHandler(userId, serverName, methodName);
            response = handler.deleteTerm(userId, guid, isPurge);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }
    /**
     * Restore a Term
     *
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the term to delete
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
    public SubjectAreaOMASAPIResponse restoreTerm(String serverName, String userId, String guid)
    {
        final String methodName = "restoreTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaTermHandler handler = instanceHandler.getSubjectAreaTermHandler(userId, serverName, methodName);
            response = handler.restoreTerm(userId, guid);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }
}
