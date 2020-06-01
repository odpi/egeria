/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaRelationshipHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaRESTServicesInstance {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRESTServicesInstance.class);

    public static final String PAGE_OFFSET_DEFAULT_VALUE = "0";
    public static final String PAGE_SIZE_DEFAULT_VALUE = "0";

    // The OMRSAPIHelper allows the junits to mock out the omrs layer
    protected static SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    /**
     * Default constructor
     */
    public SubjectAreaRESTServicesInstance() {
    }

    /**
     * Create a Line (relationship), which is a link between two Nodes.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName name of the rest API
     * @param userId     userId under which the request is performed
     * @param className  class name
     * @param line       line to create
     * @return response, when successful contains the created line
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.
     * </ul>
     */
    protected SubjectAreaOMASAPIResponse createLine(String serverName, String restAPIName, String userId, String className, Line line) {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.createLine(restAPIName, userId, className, line);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    /**
     * Get a Line (relationship)
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName name of the rest API
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param className  class name
     * @param guid       guid of the relationship to get
     * @return response which when successful contains the relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    protected SubjectAreaOMASAPIResponse getLine(String serverName, String restAPIName, String userId, String className, String guid) {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.getLine(restAPIName, userId, className, guid);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    /**
     * Update a relationship.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName rest api name
     * @param userId     userId under which the request is performed
     * @param className  class name
     * @param line       the relationship to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response,              when successful contains the updated Line
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    protected SubjectAreaOMASAPIResponse updateLine(String serverName, String restAPIName, String userId, String className, Line line, boolean isReplace) {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.updateLine(restAPIName, userId, className, line, isReplace);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    /**
     * Delete a Line (relationship)
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName rest API name
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param className  class name
     * @param guid       guid of the HAS A relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete, the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteLine(String serverName, String restAPIName, String userId, String className, String guid, Boolean isPurge) {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.deleteLine(restAPIName, userId, className, guid, isPurge);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    /**
     * Restore a Line (relationship).
     * <p>
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName name of the rest API
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param className  class name
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    protected SubjectAreaOMASAPIResponse restoreLine(String serverName, String restAPIName, String userId, String className, String guid) {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.restoreLine(restAPIName, userId, className, guid);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }
}