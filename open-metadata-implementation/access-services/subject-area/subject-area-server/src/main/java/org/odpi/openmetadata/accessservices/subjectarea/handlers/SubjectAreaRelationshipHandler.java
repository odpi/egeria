/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.IRelationshipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * TheSubjectAreaTermRESTServices provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides relationship authoring interfaces for subject area experts.
 */

public class SubjectAreaRelationshipHandler extends SubjectAreaHandler {
    private static final String className = SubjectAreaRelationshipHandler.class.getName();

    /**
     * Construct the Subject Area Relationship Handler
     * needed to operate within a single server instance.
     *
     * @param oMRSAPIHelper omrs API helper
     * @param maxPageSize   maximum page size
     */
    public SubjectAreaRelationshipHandler(OMRSAPIHelper oMRSAPIHelper, int maxPageSize) {
        super(oMRSAPIHelper, maxPageSize);
    }

    /**
     * Create a relationship, which is a link between two Nodes.
     * <p>
     *
     * @param <R>          {@link Relationship} type of object for response
     * @param restAPIName  rest API name
     * @param userId       userId under which the request is performed
     * @param clazz        mapper Class
     * @param relationship relationship to create
     * @return response, when successful contains the created relationship
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
    public <R extends Relationship> SubjectAreaOMASAPIResponse<R> createRelationship(String restAPIName,
                                                                                     String userId,
                                                                                     Class<? extends IRelationshipMapper<R>> clazz,
                                                                                     R relationship) {
        SubjectAreaOMASAPIResponse<R> response = new SubjectAreaOMASAPIResponse<>();
        try {
            IRelationshipMapper<R> mapper = mappersFactory.get(clazz);
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship omrsRelationship = mapper.map(relationship);
            InstanceProperties instanceProperties = omrsRelationship.getProperties();
            if (instanceProperties == null) {
                instanceProperties = new InstanceProperties();
            }
            Date effectiveFromtime = instanceProperties.getEffectiveFromTime();
            if (effectiveFromtime == null) {
                // if not supplied set the effective from Date to now.
                instanceProperties.setEffectiveFromTime(new Date());
                omrsRelationship.setProperties(instanceProperties);
            }

            Optional<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> createdOMRSRelationship = oMRSAPIHelper.callOMRSAddRelationship(restAPIName, userId, omrsRelationship);
            if (createdOMRSRelationship.isPresent()) {
                R createdrelationship = mapper.map(createdOMRSRelationship.get());
                response.addResult(createdrelationship);
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get a relationship (relationship)
     *
     * @param <R>         {@link Relationship} type of object for response
     * @param restAPIName rest API name
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param clazz       mapper Class
     * @param guid        guid of the relationship to get
     * @return response which when successful contains the relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    public <R extends Relationship> SubjectAreaOMASAPIResponse<R> getRelationship(String restAPIName,
                                                                                  String userId,
                                                                                  Class<? extends IRelationshipMapper<R>> clazz,
                                                                                  String guid) {
        SubjectAreaOMASAPIResponse<R> response = new SubjectAreaOMASAPIResponse<>();
        try {
            IRelationshipMapper<R> mapper = mappersFactory.get(clazz);
            Optional<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> gotOMRSRelationship = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, guid);
            if (gotOMRSRelationship.isPresent()) {
                R omasRelationship = mapper.map(gotOMRSRelationship.get());
                response.addResult(omasRelationship);
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Update a relationship.
     * <p>
     *
     * @param <R>              {@link Relationship} type of object for response
     * @param restAPIName      rest API name
     * @param userId           userId under which the request is performed
     * @param relationshipGuid unique identifier of the relationship
     * @param clazz            mapper Class
     * @param relationship     the relationship to update
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response,              when successful contains the updated relationship
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
    public <R extends Relationship> SubjectAreaOMASAPIResponse<R> updateRelationship(String restAPIName,
                                                                                     String userId,
                                                                                     String relationshipGuid,
                                                                                     Class<? extends IRelationshipMapper<R>> clazz,
                                                                                     R relationship,
                                                                                     Boolean isReplace) {
        final String methodName = "updateRelationship";
        SubjectAreaOMASAPIResponse<R> response = new SubjectAreaOMASAPIResponse<>();

        try {
            IRelationshipMapper<R> mapper = mappersFactory.get(clazz);
            Optional<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> gotRelationship = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, relationshipGuid);
            if (gotRelationship.isPresent()) {
                checkRelationshipReadOnly(methodName, gotRelationship.get(), "update");

                org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship originalRelationship = gotRelationship.get();
                org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationshipToUpdate = mapper.map(relationship);
                relationshipToUpdate.setGUID(relationshipGuid);

                if (relationshipToUpdate.getProperties() == null || relationshipToUpdate.getProperties().getPropertyCount() == 0) {
                    // nothing to update.
                    // TODO may need to change this logic if effectivity updates can be made through this call.
                    ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.RELATIONSHIP_UPDATE_ATTEMPTED_WITH_NO_PROPERTIES.getMessageDefinition();
                    throw new InvalidParameterException(messageDefinition, className, restAPIName, "properties", null);
                }

                if (!isReplace) {
                    // copy over with effectivity dates - honour what is in the request. So null means we lose any effectivity time that are set.
                    InstanceProperties instanceProperties = updateProperties(originalRelationship, relationshipToUpdate);
                    if (relationship.getEffectiveFromTime() != null) {
                        instanceProperties.setEffectiveFromTime(new Date(relationship.getEffectiveFromTime()));
                    }
                    if (relationship.getEffectiveToTime() != null) {
                        instanceProperties.setEffectiveToTime(new Date(relationship.getEffectiveToTime()));
                    }
                    relationshipToUpdate.setProperties(instanceProperties);
                }
                oMRSAPIHelper.callOMRSUpdateRelationship(restAPIName, userId, relationshipToUpdate);
                response = getRelationship(restAPIName, userId, clazz, relationshipGuid);
            }
        } catch (PropertyServerException | UserNotAuthorizedException | SubjectAreaCheckedException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    private InstanceProperties updateProperties(org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship originalRelationship, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationshipToUpdate) {
            Map<String, InstancePropertyValue> updateInstanceProperties = relationshipToUpdate.getProperties().getInstanceProperties();
            if (originalRelationship.getProperties() != null) {
                Map<String, InstancePropertyValue> orgInstanceProperties = originalRelationship.getProperties().getInstanceProperties();

                // if there a property that already exists but is not in the update properties
                // then make sure that value is not overwritten by including it in this update request.
                for (String orgPropertyName : orgInstanceProperties.keySet()) {
                    if (!updateInstanceProperties.containsKey(orgPropertyName)) {
                        // make sure the original value is not lost.
                        updateInstanceProperties.put(orgPropertyName, orgInstanceProperties.get(orgPropertyName));
                    }
                }
            }
            InstanceProperties instancePropertiesToUpdate = new InstanceProperties();
            instancePropertiesToUpdate.setInstanceProperties(updateInstanceProperties);

            return instancePropertiesToUpdate;
        }

    /**
     * Delete a relationship
     *
     * @param <R>         {@link Relationship} type of object for response
     * @param restAPIName rest API name
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param clazz       mapper Class
     * @param guid        guid of the HAS A relationship to delete
     * @param isPurge     true indicates a hard delete, false is a soft delete.
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
    public <R extends Relationship> SubjectAreaOMASAPIResponse<R> deleteRelationship(String restAPIName,
                                                                                     String userId,
                                                                                     Class<? extends IRelationshipMapper<R>> clazz,
                                                                                     String guid,
                                                                                     Boolean isPurge) {
        final String methodName = "deleteRelationship";
        SubjectAreaOMASAPIResponse<R> response = new SubjectAreaOMASAPIResponse<>();

        try {
            IRelationshipMapper<R> mapper = mappersFactory.get(clazz);
            String typeGuid = mapper.getTypeDefGuid();
            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeRelationship(restAPIName, userId, typeGuid, mapper.getTypeName(), guid);
            } else {
                Optional<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> gotRelationship = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, guid);
                if (gotRelationship.isPresent()) {
                    checkRelationshipReadOnly(methodName, gotRelationship.get(), "delete");
                }
                oMRSAPIHelper.callOMRSDeleteRelationship(restAPIName, userId, typeGuid, mapper.getTypeName(), guid);
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Restore a relationship.
     * <p>
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param <R>         {@link Relationship} type of object for response
     * @param restAPIName rest API name
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param clazz       mapper Class
     * @param guid        guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException             a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public <R extends Relationship> SubjectAreaOMASAPIResponse<R> restoreRelationship(String restAPIName,
                                                                                      String userId,
                                                                                      Class<? extends IRelationshipMapper<R>> clazz,
                                                                                      String guid) {
        SubjectAreaOMASAPIResponse<R> response = new SubjectAreaOMASAPIResponse<>();
        try {
            oMRSAPIHelper.callOMRSRestoreRelationship(restAPIName, userId, guid);
            response = getRelationship(restAPIName, userId, clazz, guid);
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }
}