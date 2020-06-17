/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

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
     * @param oMRSAPIHelper           omrs API helper
     */
    public SubjectAreaRelationshipHandler(OMRSAPIHelper oMRSAPIHelper) {
        super(oMRSAPIHelper);
    }

    /**
     * Create a Line (relationship), which is a link between two Nodes.
     * <p>
     *
     * @param restAPIName rest API name
     * @param userId      userId under which the request is performed
     * @param clazz       mapper Class
     * @param line        line to create
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
    public <L extends Line>SubjectAreaOMASAPIResponse2<L> createLine(String restAPIName,
                                                                     String userId,
                                                                     Class<? extends ILineMapper<L>> clazz,
                                                                     L line)
    {
        SubjectAreaOMASAPIResponse2<L> response = new SubjectAreaOMASAPIResponse2<>();
        try {
            ILineMapper<L> mapper = mappersFactory.get(clazz);
            Relationship omrsRelationship = mapper.map(line);
            Optional<Relationship> createdOMRSRelationship = oMRSAPIHelper.callOMRSAddRelationship(restAPIName, userId, omrsRelationship);
            if (createdOMRSRelationship.isPresent()) {
                L createdLine = mapper.map(createdOMRSRelationship.get());
                response.addResult(createdLine);
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get a Line (relationship)
     *
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
    public <L extends Line>SubjectAreaOMASAPIResponse2<L> getLine(String restAPIName,
                                                                  String userId,
                                                                  Class<? extends ILineMapper<L>> clazz,
                                                                  String guid)
    {
        SubjectAreaOMASAPIResponse2<L> response = new SubjectAreaOMASAPIResponse2<>();
        try {
            ILineMapper<L> mapper = mappersFactory.get(clazz);
            Optional<Relationship> gotRelationship = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, guid);
            if (gotRelationship.isPresent()) {
                L gotLine = mapper.map(gotRelationship.get());
                response.addResult(gotLine);
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
     * @param restAPIName rest API name
     * @param userId      userId under which the request is performed
     * @param clazz       mapper Class
     * @param line        the relationship to update
     * @param isReplace   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public <L extends Line>SubjectAreaOMASAPIResponse2<L> updateLine(String restAPIName,
                                                                     String userId,
                                                                     Class<? extends ILineMapper<L>> clazz,
                                                                     L line,
                                                                     Boolean isReplace)
    {
        SubjectAreaOMASAPIResponse2<L> response = new SubjectAreaOMASAPIResponse2<>();

        try {
            ILineMapper<L> mapper = mappersFactory.get(clazz);
            Optional<Relationship> gotRelationship = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, line.getGuid());
            if (gotRelationship.isPresent()) {
                Relationship originalRelationship = gotRelationship.get();
                Relationship relationshipToUpdate = mapper.map(line);

                if (relationshipToUpdate.getProperties() == null || relationshipToUpdate.getProperties().getPropertyCount() == 0) {
                    // nothing to update.
                    // TODO may need to change this logic if effectivity updates can be made through this call.
                    ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.LINE_UPDATE_ATTEMPTED_WITH_NO_PROPERTIES.getMessageDefinition();
                    throw new InvalidParameterException(messageDefinition, className, restAPIName, "properties", null);
                }

                if (!isReplace) {
                    // copy over with effectivity dates - honour what is in the request. So null means we lose any effectivity time that are set.
                    InstanceProperties instanceProperties = updateProperties(originalRelationship, relationshipToUpdate);
                    instanceProperties.setEffectiveFromTime(line.getEffectiveFromTime());
                    instanceProperties.setEffectiveToTime(line.getEffectiveToTime());
                    relationshipToUpdate.setProperties(instanceProperties);
                }
                oMRSAPIHelper.callOMRSUpdateRelationship(restAPIName, userId, relationshipToUpdate);
                response = getLine(restAPIName, userId, clazz, line.getGuid());
            }
        } catch (PropertyServerException | UserNotAuthorizedException | SubjectAreaCheckedException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    private InstanceProperties updateProperties(Relationship originalRelationship, Relationship relationshipToUpdate) {
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
     * Delete a Line (relationship)
     *
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
    public <L extends Line>SubjectAreaOMASAPIResponse2<L> deleteLine(String restAPIName,
                                                                     String userId,
                                                                     Class<? extends ILineMapper<L>> clazz,
                                                                     String guid,
                                                                     Boolean isPurge) {
        SubjectAreaOMASAPIResponse2<L> response = new SubjectAreaOMASAPIResponse2<>();

        try {
            ILineMapper<L> mapper = mappersFactory.get(clazz);
            String typeGuid = mapper.getTypeDefGuid();
            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeRelationship(restAPIName, userId, typeGuid, mapper.getTypeName(), guid);
            } else {
                oMRSAPIHelper.callOMRSDeleteRelationship(restAPIName, userId, typeGuid, mapper.getTypeName(), guid);
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Restore a Line (relationship).
     * <p>
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
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
    public <L extends Line>SubjectAreaOMASAPIResponse2<L>  restoreLine(String restAPIName,
                                                                       String userId,
                                                                       Class<? extends ILineMapper<L>> clazz,
                                                                       String guid)
    {
        SubjectAreaOMASAPIResponse2<L> response = new SubjectAreaOMASAPIResponse2<>();
        try {
            oMRSAPIHelper.callOMRSRestoreRelationship(restAPIName, userId, guid);
            response = getLine(restAPIName, userId, clazz, guid);
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }
}