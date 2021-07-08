/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.IRelationshipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
;
import java.util.Date;



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
        String methodName = "createRelationship";
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

           String guid =  genericHandler.linkElementToElement(
                                 userId,
                                 null,
                                 null,
                                 omrsRelationship.getEntityOneProxy().getGUID(),
                                 "end1.guid",
                                 omrsRelationship.getEntityOneProxy().getType().getTypeDefName(),
                                 omrsRelationship.getEntityTwoProxy().getGUID(),
                                 "end2.guid",
                                 omrsRelationship.getEntityTwoProxy().getType().getTypeDefName(),
                                 omrsRelationship.getType().getTypeDefGUID(),
                                 omrsRelationship.getType().getTypeDefName(),
                                 instanceProperties,
                                 methodName);
           response = getRelationship(methodName, userId, clazz, guid);

        } catch (UserNotAuthorizedException | PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
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
            String typeDefName = mapper.getTypeName();
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship oMRSRelationship =
                    genericHandler.getRepositoryHandler().getRelationshipByGUID(userId, guid,
                                                                        "guid",
                                                                       typeDefName,
                                                                       restAPIName);

                R omasRelationship = mapper.map(oMRSRelationship);
                response.addResult(omasRelationship);

        } catch (UserNotAuthorizedException | PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
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
            response = getRelationship(methodName, userId, clazz, relationshipGuid);
            R storedOMASRelationship = response.results().get(0);
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship storedRelationship = mapper.map(storedOMASRelationship);

            checkRelationshipReadOnly(methodName, storedRelationship, "update");

            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationshipToUpdate = mapper.map(relationship);
            relationshipToUpdate.setGUID(relationshipGuid);

            if (relationshipToUpdate.getProperties() != null || relationshipToUpdate.getProperties().getPropertyCount() == 0) {
                // nothing to update.
//                    // TODO may need to change this logic if effectivity updates can be made through this call.
//                    ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.RELATIONSHIP_UPDATE_ATTEMPTED_WITH_NO_PROPERTIES.getMessageDefinition();
//                    throw new InvalidParameterException(messageDefinition, className, restAPIName, "properties", null);
            } else {
                genericHandler.updateRelationshipProperties(userId,
                                                            null,  // local cohort.
                                                            null,
                                                            relationshipGuid,
                                                            "guid",
                                                            mapper.getTypeName(),
                                                            !isReplace,
                                                            relationshipToUpdate.getProperties(),
                                                            methodName);
            }
            Date requestedEffectiveFrom = relationship.getEffectiveFromTime() == null ? null : new Date(relationship.getEffectiveFromTime());
            Date requestedEffectiveTo = relationship.getEffectiveToTime() == null ? null : new Date(relationship.getEffectiveToTime());
            Date storedEffectiveFrom = storedRelationship.getProperties().getEffectiveFromTime();
            Date storedEffectiveTo = storedRelationship.getProperties().getEffectiveToTime();

            if (!isReplace) {
                // do not change the effectivity dates if null and not replacing
                if (requestedEffectiveFrom == null && storedEffectiveFrom != null) {
                    requestedEffectiveFrom = storedEffectiveFrom;
                }
                if (requestedEffectiveTo == null && storedEffectiveTo != null) {
                    requestedEffectiveTo = storedEffectiveTo;
                }
            }

            genericHandler.updateRelationshipEffectivityDates(userId,
                                                              null,  // local cohort.
                                                              null,
                                                              relationshipGuid,
                                                              "guid",
                                                              mapper.getTypeName(),
                                                              requestedEffectiveFrom,
                                                              requestedEffectiveTo,
                                                              methodName);

            response = getRelationship(restAPIName, userId, clazz, relationshipGuid);

        } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Delete a relationship
     *
     * @param <R>         {@link Relationship} type of object for response
     * @param restAPIName rest API name
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param clazz       mapper Class
     * @param guid        guid of the relationship to delete
     * @return response for a soft delete, the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * </ul>
     */
    public <R extends Relationship> SubjectAreaOMASAPIResponse<R> deleteRelationship(String restAPIName,
                                                                                     String userId,
                                                                                     Class<? extends IRelationshipMapper<R>> clazz,
                                                                                     String guid) {
        final String methodName = "deleteRelationship";
        SubjectAreaOMASAPIResponse<R> response = new SubjectAreaOMASAPIResponse<>();

        try {
            IRelationshipMapper<R> mapper = mappersFactory.get(clazz);
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship oMRSRelationship =
                    genericHandler.getRepositoryHandler().getRelationshipByGUID(userId, guid,
                                                                                "guid",
                                                                                mapper.getTypeName(),
                                                                                restAPIName);

            checkRelationshipReadOnly(methodName, oMRSRelationship, "delete");

            genericHandler.unlinkElementFromElement(userId,
                                                    false,
                                                    null,
                                                    null,
                                                    oMRSRelationship.getEntityOneProxy().getGUID(),
                                                    "end1,guid",
                                                    oMRSRelationship.getEntityOneProxy().getType().getTypeDefName(),
                                                    oMRSRelationship.getEntityTwoProxy().getGUID(),
                                                    "end2,guid",
                                                    oMRSRelationship.getEntityTwoProxy().getType().getTypeDefGUID(),
                                                    oMRSRelationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                    oMRSRelationship.getType().getTypeDefName(),
                                                    oMRSRelationship,
                                                    methodName);
        } catch (UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
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