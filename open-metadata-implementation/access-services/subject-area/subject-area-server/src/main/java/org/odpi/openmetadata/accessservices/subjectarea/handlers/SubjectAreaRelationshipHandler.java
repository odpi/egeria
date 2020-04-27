/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.InvalidParameterExceptionResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineBundle;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineBundleFactory;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ResponseFactory;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServicesInstance;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRelationshipRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.StringTokenizer;


/**
 * TheSubjectAreaTermRESTServices provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides relationship authoring interfaces for subject area experts.
 */

public class SubjectAreaRelationshipHandler extends SubjectAreaHandler {

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRelationshipHandler.class);


    /**
     * Construct the Subject Area Relationship Handler
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param oMRSAPIHelper           omrs API helper
     * @param errorHandler            handler for repository service errors
     */
    public SubjectAreaRelationshipHandler(String serviceName,
                                          String serverName,
                                          InvalidParameterHandler invalidParameterHandler,
                                          OMRSRepositoryHelper repositoryHelper,
                                          RepositoryHandler repositoryHandler,
                                          OMRSAPIHelper oMRSAPIHelper,
                                          RepositoryErrorHandler errorHandler) {
        super(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler, oMRSAPIHelper, errorHandler);
    }
    // nothing to change for relationships
    @Override
    protected SubjectAreaOMASAPIResponse getResponse(SubjectAreaOMASAPIResponse response) {
        return response;
    }
    /**
     * Create a Line (relationship), which is a link between two Nodes.
     * <p>
     *
     * @param userId     userId under which the request is performed
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
    public SubjectAreaOMASAPIResponse createLine(String restAPIName, String userId, String className, Line line) {
        String methodName = "createLine";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse response = null;
        ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
        ILineBundle bundle = factory.getInstance(className);
        ILineMapper mapper = bundle.getMapper();

        try {
            Relationship omrsRelationship = mapper.mapLineToRelationship(line);
            response = oMRSAPIHelper.callOMRSAddRelationship(restAPIName, userId, omrsRelationship);
            if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                Relationship createdOMRSRelationship = ((RelationshipResponse) response).getRelationship();
                Line createdLine = mapper.mapRelationshipToLine(createdOMRSRelationship);
                response = new ResponseFactory().getInstance(className, createdLine);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }


        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;

    }
    /**
     * Get a Line (relationship)
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
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
    public SubjectAreaOMASAPIResponse getLine(String restAPIName, String userId, String className, String guid) {

        String methodName = "getLine";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
        ILineBundle bundle = factory.getInstance(className);
        ILineMapper mapper = bundle.getMapper();

        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, guid);
            if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                Relationship createdOMRSRelationship = ((RelationshipResponse) response).getRelationship();
                Line gotLine = mapper.mapRelationshipToLine(createdOMRSRelationship);
                response = new ResponseFactory().getInstance(className, gotLine);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }


        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a relationship.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param line                    the relationship to update
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public SubjectAreaOMASAPIResponse updateLine(String restAPIName, String userId, String className, Line line, boolean isReplace) {
        String methodName = "updateLine";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",className=" + className, ",isReplace=" + isReplace);
        }

        SubjectAreaOMASAPIResponse response = null;
        ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
        ILineBundle bundle = factory.getInstance(className);
        ILineMapper mapper = bundle.getMapper();

        try {
            String relationshipGuid = line.getGuid();
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "termGuid");
            response = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, relationshipGuid);
            if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                Relationship originalRelationship = ((RelationshipResponse) response).getRelationship();
                Relationship relationshipToUpdate = mapper.mapLineToRelationship(line);

                if (isReplace) {
                    // use the relationship as supplied
                } else {
                    if (relationshipToUpdate.getProperties() != null && relationshipToUpdate.getProperties().getPropertyCount() > 0) {
                        Map<String, InstancePropertyValue> updateInstanceProperties = relationshipToUpdate.getProperties().getInstanceProperties();
                        if (originalRelationship.getProperties() != null) {
                            Map<String, InstancePropertyValue> orgInstanceProperties = originalRelationship.getProperties().getInstanceProperties();

                            // if there a property that already exists but is not in the update properties then make sure that value is not overwritten by including it in this update request.
                            for (String orgPropertyName : orgInstanceProperties.keySet()) {
                                if (!updateInstanceProperties.keySet().contains(orgPropertyName)) {
                                    // make sure the original value is not lost.
                                    updateInstanceProperties.put(orgPropertyName, orgInstanceProperties.get(orgPropertyName));
                                }
                            }
                        }
                        InstanceProperties instancePropertiesToUpdate = new InstanceProperties();
                        instancePropertiesToUpdate.setInstanceProperties(updateInstanceProperties);
                        // copy over with effectivity dates - honour what is in the request. So null means we lose any effectivity time that are set.
                        instancePropertiesToUpdate.setEffectiveFromTime(line.getEffectiveFromTime());
                        instancePropertiesToUpdate.setEffectiveToTime(line.getEffectiveToTime());
                        relationshipToUpdate.setProperties(instancePropertiesToUpdate);
                    }
                }
                if (relationshipToUpdate.getProperties() == null || relationshipToUpdate.getProperties().getPropertyCount() == 0) {
                    // nothing to update.
                    // TODO may need to change this logic if effectivity updates can be made through this call.
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.LINE_UPDATE_ATTEMPTED_WITH_NO_PROPERTIES;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
                    log.error(errorMessage);
                    response = new InvalidParameterExceptionResponse(
                            new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction())
                    );
                } else {
                    response = oMRSAPIHelper.callOMRSUpdateRelationship(restAPIName, userId, relationshipToUpdate);
                    if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                        Relationship updatedOmrsRelationship = ((RelationshipResponse) response).getRelationship();
                        Line updatedLine = mapper.mapRelationshipToLine(updatedOmrsRelationship);
                        response = new ResponseFactory().getInstance(className, updatedLine);
                    }
                }
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    /**
     * Delete a Line (relationship)
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
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
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteLine(String restAPIName, String userId, String className, String guid, Boolean isPurge) {

        String methodName = "deleteLine";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid + ",isPurge=" + isPurge);
        }
        SubjectAreaOMASAPIResponse response = null;
        OMRSRepositoryHelper repositoryHelper = oMRSAPIHelper.getOMRSRepositoryHelper();
        ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
        ILineBundle bundle = factory.getInstance(className);
        ILineMapper mapper = bundle.getMapper();

        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            StringTokenizer st = new StringTokenizer(className, ".");
            String typeName = mapper.getTypeName();
            String source = oMRSAPIHelper.getServiceName();
            String typeGuid = repositoryHelper.getTypeDefByName(source, typeName).getGUID();
            if (isPurge) {
                response = oMRSAPIHelper.callOMRSPurgeRelationship(restAPIName, userId, typeGuid, typeName, guid);
            } else {
                response = oMRSAPIHelper.callOMRSDeleteRelationship(restAPIName, userId, typeGuid, typeName, guid);
                if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                    Relationship omrsRelationship = ((RelationshipResponse) response).getRelationship();
                    Line deletedLine = mapper.mapRelationshipToLine(omrsRelationship);
                    response = new ResponseFactory().getInstance(className, deletedLine);
                }
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    /**
     * Restore a Line (relationship).
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
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
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreLine(String restAPIName, String userId, String className, String guid) {

        String methodName = "deleteLine";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
        ILineBundle bundle = factory.getInstance(className);
        ILineMapper mapper = bundle.getMapper();
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = this.oMRSAPIHelper.callOMRSRestoreRelationship(restAPIName, userId, guid);
            if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                Relationship omrsRelationship = ((RelationshipResponse) response).getRelationship();
                Line restoredLine = mapper.mapRelationshipToLine(omrsRelationship);
                response = new ResponseFactory().getInstance(className, restoredLine);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
}