/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.glossaryauthor.handlers.GlossaryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;

/**
 * The GlossaryAuthorViewGlossaryRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the GlossaryAuthor Open Metadata
 * View Service (OMVS). This interface provides view glossary authoring interfaces for subject area experts.
 */

public class GlossaryAuthorViewGlossaryRESTServices extends BaseGlossaryAuthorView {

    private static String className = GlossaryAuthorViewGlossaryRESTServices.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);

    /**
     * Default constructor
     */
    public GlossaryAuthorViewGlossaryRESTServices() {

    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalGlossary to create a canonical glossary </li>
     * <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glosary </li>
     * <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     *
     * @param serverName name of the local UI server.
     * @param userId     user identifier
     * @param suppliedGlossary Glossary to create
     * @return response, when successful contains the created glossary.
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

    public SubjectAreaOMASAPIResponse createGlossary(String serverName, String userId, Glossary suppliedGlossary) {
        final String methodName = "createGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied glossary - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryHandler handler = instanceHandler.getGlossaryHandler(serverName, userId, methodName);
            Glossary createdGlossary = handler.createGlossary(userId,
                    suppliedGlossary);
            response = new GlossaryResponse(createdGlossary);
        }  catch (Throwable error) {
            response =  getResponseForError(error, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a glossary.
     *
     * @param serverName name of the local UI server.
     * @param userId     user identifier
     * @param guid       guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
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

    public SubjectAreaOMASAPIResponse getGlossary(String serverName, String userId, String guid) {
        final String methodName = "getGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryHandler handler = instanceHandler.getGlossaryHandler(serverName, userId, methodName);
            Glossary obtainedGlossary = handler.getGlossaryByGuid(userId,
                    guid);
            response = new GlossaryResponse(obtainedGlossary);
        }  catch (Throwable error) {
            response =  getResponseForError(error, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Find Glossary
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param searchCriteria     String expression matching Glossary property values .
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of glossaries meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse findGlossary(
            String serverName,
            String userId,
            Date asOfTime,
            String searchCriteria,
            Integer offset,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty
    ) {
        final String methodName = "findGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryHandler handler = instanceHandler.getGlossaryHandler(serverName, userId, methodName);
            if (offset == null) {
                offset = new Integer(0);
            }
            if (pageSize == null) {
                pageSize = new Integer(0);
            }
            List<Glossary> glossaries = handler.findGlossary(
                    userId,
                    searchCriteria,
                    asOfTime,
                    offset,
                    pageSize,
                    sequencingOrder,
                    sequencingProperty);
            GlossariesResponse glossariesResponse = new GlossariesResponse();
            glossariesResponse.setGlossaries(glossaries);
            response = glossariesResponse;
        }  catch (Throwable error) {
            response =  getResponseForError(error, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get Glossary relationships
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid               guid of the glossary to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the glossary relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getGlossaryRelationships(
            String serverName,
            String userId,
            String guid,
            Date asOfTime,
            Integer offset,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty


    ) {
        final String methodName = "getGlossaryRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryHandler handler = instanceHandler.getGlossaryHandler(serverName, userId, methodName);
            List<Line> lines =  handler.getGlossaryRelationships(userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
            LinesResponse linesResponse = new LinesResponse();
            linesResponse.setLines(lines);
            response = linesResponse;
        }  catch (Throwable error) {
            response =  getResponseForError(error, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a Glossary
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the glossary to update
     * @param glossary   glossary to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse updateGlossary(
            String serverName,
            String userId,
            String guid,
            Glossary glossary,
            Boolean isReplace
    ) {
        final String methodName = "updateGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied glossary - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryHandler handler = instanceHandler.getGlossaryHandler(serverName, userId, methodName);
            Glossary updatedGlossary;
            if (isReplace == null) {
                isReplace = false;
            }
            if (isReplace) {
                updatedGlossary = handler.replaceGlossary(userId, guid, glossary);
            } else {
                updatedGlossary = handler.updateGlossary(userId, guid, glossary);
            }
            GlossaryResponse glossaryResponse = new GlossaryResponse();
            glossaryResponse.setGlossary(updatedGlossary);
            response = glossaryResponse;
        }  catch (Throwable error) {
            response =  getResponseForError(error, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the glossary to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteGlossary(
            String serverName,
            String userId,
            String guid,
            Boolean isPurge
    ) {

        final String methodName = "deleteGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied glossary - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryHandler handler = instanceHandler.getGlossaryHandler(serverName, userId, methodName);
            if (isPurge == null) {
                // default to soft delete if isPurge is not specified.
                isPurge = false;
            }

            if (isPurge) {
                handler.purgeGlossary(userId, guid);
                response = new VoidResponse();
            } else {
                Glossary glossary = handler.deleteGlossary(userId, guid);
                GlossaryResponse glossaryResponse = new GlossaryResponse();
                glossaryResponse.setGlossary(glossary);
                response = glossaryResponse;
            }
        }  catch (Throwable error) {
            response =  getResponseForError(error, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the glossary to restore
     * @return response which when successful contains the restored glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreGlossary(
            String serverName,
            String userId,
            String guid) {
        final String methodName = "restoreGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied glossary - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryHandler handler = instanceHandler.getGlossaryHandler(serverName, userId, methodName);
            Glossary glossary = handler.restoreGlossary(userId, guid);
            GlossaryResponse glossaryResponse = new GlossaryResponse();
            glossaryResponse.setGlossary(glossary);
            response = glossaryResponse;
        }  catch (Throwable error) {
            response =  getResponseForError(error, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
