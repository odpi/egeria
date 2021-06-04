/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This is a facade around the OMRS API. It transforms the OMRS Exceptions into OMAS exceptions
 */
public class OMRSAPIHelper {

    private static final Logger log = LoggerFactory.getLogger(OMRSAPIHelper.class);
    private final String serviceName;
    private final String serverName;
    private OMRSRepositoryHelper omrsRepositoryHelper;
    private RepositoryHandler repositoryHandler;

    /**
     * @param serviceName            name of the consuming service
     * @param serverName             name of this server instance
     * @param repositoryHelper       helper used by the converters
     * @param repositoryHandler      issues common calls to the open metadata repository to retrieve and store metadata
     */
    public OMRSAPIHelper(
            String serviceName,
            String serverName,
            RepositoryHandler repositoryHandler,
            OMRSRepositoryHelper repositoryHelper

    ) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.repositoryHandler = repositoryHandler;
        this.omrsRepositoryHelper = repositoryHelper;
    }

    /**
     * Get the service name - ths is used for logging
     *
     * @return service name
     */
    public String getServiceName() {
        return this.serviceName;
    }

    public OMRSRepositoryHelper getOMRSRepositoryHelper() {
        return this.omrsRepositoryHelper;
    }

    public RepositoryHandler getRepositoryHandler() { return this.repositoryHandler; }

    public String callOMRSAddEntity(String restAPIName,
                                    String userId,
                                    EntityDetail entityDetail) throws SubjectAreaCheckedException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        String methodName = "callOMRSAddEntity";
        try {
           return getRepositoryHandler().createEntity(
                   userId,
                   entityDetail.getType().getTypeDefGUID(),
                   entityDetail.getType().getTypeDefName(),
                   null,
                   null,
                   entityDetail.getProperties(),
                   entityDetail.getClassifications(),
                   InstanceStatus.ACTIVE,
                   restAPIName
           );
        } catch (UserNotAuthorizedException | PropertyServerException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
        return null;
    }

    public Optional<EntityDetail> callOMRSGetEntityByGuid(String userId,
                                                          String entityGUID,
                                                          String entityTypeName,
                                                          String restAPIName) throws SubjectAreaCheckedException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException,
                                                                                     InvalidParameterException
    {
        String methodName = "callOMRSGetEntityByGuid";
        try {
            return Optional.ofNullable(getRepositoryHandler()
                            .getEntityByGUID(userId,
                                    entityGUID,
                                    "guid",
                                    entityTypeName,
                                    restAPIName)
            );
        } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }

        return Optional.empty();
    }

    public List<EntityDetail> callFindEntitiesByPropertyValue(String userId,
                                                              String entityTypeGUID,
                                                              String searchCriteria,
                                                              int fromEntityElement,
                                                              int pageSize,
                                                              Date asOfTime,
                                                              String sequencingProperty,
                                                              SequencingOrder sequencingOrder,
                                                              String restAPIName) throws UserNotAuthorizedException,
                                                                                         PropertyServerException,
                                                                                         SubjectAreaCheckedException
    {
        String methodName = "callFindEntitiesByPropertyValue";
        showMethodNameIfDebugEnabled(methodName);

        try {
            return getRepositoryHandler()
                    .getEntitiesByPropertyValue(
                            userId,
                            entityTypeGUID,
                            searchCriteria,
                            fromEntityElement,
                            pageSize,
                            asOfTime,
                            sequencingProperty,
                            sequencingOrder,
                            restAPIName
                    );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }

        return null;
    }

    public List<EntityDetail> callGetEntitiesByType(String userId,
                                                            String entityTypeGUID,
                                                            Date asOfTime,
                                                            int fromEntityElement,
                                                            int pageSize,
                                                            String sequencingProperty,
                                                            SequencingOrder sequencingOrder,
                                                            String restAPIName) throws UserNotAuthorizedException,
                                                                                        PropertyServerException,
                                                                                        SubjectAreaCheckedException
    {
        String methodName = "callGetEntitiesByType";
        showMethodNameIfDebugEnabled(methodName);

        try {
            return getRepositoryHandler()
                    .getEntitiesByType(
                            userId,
                            entityTypeGUID,
                            fromEntityElement,
                            pageSize,
                            asOfTime,
                            sequencingProperty,
                            sequencingOrder,
                            restAPIName
                    );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }

        return null;
    }


    public Optional<EntityDetail> callOMRSUpdateEntity(String restAPIName,
                                                       String userId,
                                                       EntityDetail entityDetail) throws UserNotAuthorizedException,
                                                                                         PropertyServerException,
                                                                                         SubjectAreaCheckedException
    {
        String methodName = "callOMRSUpdateEntityProperties";
        showMethodNameIfDebugEnabled(methodName);

        try {
           return Optional.ofNullable(
                   getRepositoryHandler().updateEntityProperties(
                    userId,
                    entityDetail.getGUID(),
                    entityDetail.getType().getTypeDefGUID(),
                    entityDetail.getType().getTypeDefName(),
                    entityDetail.getProperties(),
                    restAPIName)
           );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
        return Optional.empty();
    }

    public void callOMRSDeleteEntity(String restAPIName,
                                     String userId,
                                     String entityTypeName,
                                     String obsoleteGuid) throws UserNotAuthorizedException,
                                                                 PropertyServerException,
                                                                 SubjectAreaCheckedException
    {
        String methodName = "callOMRSDeleteEntity";
        showMethodNameIfDebugEnabled(methodName);

        try {
            final String entityTypeGuid = getTypeDefGUID(restAPIName, entityTypeName);
            getRepositoryHandler().removeIsolatedEntity(
                    userId,
                    obsoleteGuid,
                    entityTypeGuid,
                    entityTypeName,
                    restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public void callOMRSPurgeEntity(String restAPIName,
                                    String userId,
                                    String entityTypeName,
                                    String obsoleteGuid) throws UserNotAuthorizedException,
                                                                PropertyServerException,
                                                                SubjectAreaCheckedException
    {
        String methodName = "callOMRSPurgeEntity";
        showMethodNameIfDebugEnabled(methodName);
        try {
            final String entityTypeGuid = getTypeDefGUID(restAPIName, entityTypeName);
            getRepositoryHandler().purgeEntity(
                    userId,
                    obsoleteGuid,
                    entityTypeGuid,
                    entityTypeName,
                    restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public void callOMRSRestoreEntity(String restAPIName,
                                      String userId,
                                      String deletedEntityGUID) throws UserNotAuthorizedException,
                                                                        PropertyServerException,
                                                                        SubjectAreaCheckedException
    {
        String methodName = "callOMRSRestoreEntity";
        showMethodNameIfDebugEnabled(methodName);

        try {
            getRepositoryHandler().restoreEntity(userId, deletedEntityGUID, restAPIName);
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public void callOMRSClassifyEntity(String restAPIName,
                                       String userId,
                                       String entityGUID,
                                       Classification classification) throws UserNotAuthorizedException,
                                                                             PropertyServerException,
                                                                             SubjectAreaCheckedException
    {
        String methodName = "callOMRSClassifyEntity";
        try {
            InstanceType type = classification.getType();
            String typeDefGUID = null;

            if (type != null) {
                typeDefGUID = type.getTypeDefGUID();
            }
            this.callOMRSClassifyEntity(
                    restAPIName,
                    userId,
                    entityGUID,
                    typeDefGUID,
                    classification.getName(),
                    classification.getProperties()
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public void callOMRSClassifyEntity(String restAPIName,
                                       String userId,
                                       String entityGUID,
                                       String classificationTypeGUID,
                                       String classificationName,
                                       InstanceProperties instanceProperties) throws UserNotAuthorizedException,
                                                                                     PropertyServerException,
                                                                                     SubjectAreaCheckedException
    {
        String methodName = "callOMRSClassifyEntity";
        showMethodNameIfDebugEnabled(methodName);

        try {
            getRepositoryHandler().classifyEntity(
                    userId,
                    entityGUID,
                    classificationName,
                    instanceProperties,
                    restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public void callOMRSUpdateClassification(String restAPIName,
                                             String userId,
                                             String entityGUID,
                                             Classification existingClassification,
                                             InstanceProperties newProperties) throws UserNotAuthorizedException,
                                                                                      PropertyServerException,
                                                                                      SubjectAreaCheckedException {
        String methodName = "callOMRSUpdateClassification";
        try {
            InstanceType type = existingClassification.getType();
            String typeDefGUID = null;
            String typeDefName = null;

            if (type != null) {
                typeDefGUID = type.getTypeDefGUID();
                typeDefName = type.getTypeDefName();
            }
            getRepositoryHandler().reclassifyEntity(userId,
                                                    null,
                                                    null,
                                                    entityGUID,
                                                    typeDefGUID,
                                                    typeDefName,
                                                    existingClassification,
                                                    newProperties,
                                                    restAPIName);
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Exception error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public void callOMRSDeClassifyEntity(String restAPIName,
                                         String userId,
                                         String entityGUID,
                                         String classificationName) throws UserNotAuthorizedException,
                                                                           PropertyServerException,
                                                                           SubjectAreaCheckedException
    {
        String methodName = "callOMRSDeClassifyEntity";
        showMethodNameIfDebugEnabled(methodName);

        try {
            getRepositoryHandler().declassifyEntity(
                    userId,
                    entityGUID,
                    classificationName,
                    null,
                    restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public Optional<Relationship> callOMRSAddRelationship(String restAPIName,
                                        String userId,
                                        Relationship relationship) throws UserNotAuthorizedException,
                                                                          PropertyServerException,
                                                                          SubjectAreaCheckedException
    {
        String methodName = "callOMRSAddRelationship";
        showMethodNameIfDebugEnabled(methodName);

        try {
           return Optional.ofNullable(getRepositoryHandler()
                   .createRelationship(
                           userId,
                           relationship.getType().getTypeDefGUID(),
                           null,
                           null,
                           relationship.getEntityOneProxy().getGUID(),
                           relationship.getEntityTwoProxy().getGUID(),
                           relationship.getProperties(),
                           restAPIName)
           );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
        return Optional.empty();
    }

    public Optional<Relationship> callOMRSGetRelationshipByGuid(String restAPIName,
                                                                String userId,
                                                                String relationshipGUID) throws SubjectAreaCheckedException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        String methodName = "callOMRSGetRelationshipByGuid";
        showMethodNameIfDebugEnabled(methodName);

        try {
            return Optional.ofNullable(
                    getRepositoryHandler()
                            .getRelationshipByGUID(
                                    userId,
                                    relationshipGUID,
                                    restAPIName)
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }

        return Optional.empty();
    }

    public void callOMRSUpdateRelationship(String restAPIName,
                                           String userId,
                                           Relationship relationship) throws UserNotAuthorizedException,
                                                                             PropertyServerException,
                                                                             SubjectAreaCheckedException
    {
        String methodName = "callOMRSUpdateRelationship";
        showMethodNameIfDebugEnabled(methodName);

        try {
            getRepositoryHandler().updateRelationshipProperties(
                    userId,
                    relationship.getGUID(),
                    relationship.getProperties(),
                    restAPIName
            );
            if (relationship.getStatus() != null) {
                getRepositoryHandler().updateRelationshipStatus(
                        userId,
                        relationship.getGUID(),
                        relationship.getStatus(),
                        restAPIName
                );
            }
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public void callOMRSDeleteRelationship(String restAPIName,
                                           String userId,
                                           String typeGuid,
                                           String typeName,
                                           String guid) throws UserNotAuthorizedException,
                                                               PropertyServerException,
                                                               SubjectAreaCheckedException
    {
        String methodName = "callOMRSDeleteRelationship";
        showMethodNameIfDebugEnabled(methodName);

        try {
            getRepositoryHandler().removeRelationship(
                    userId,
                    typeGuid,
                    typeName,
                    guid,
                    restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public void callOMRSRestoreRelationship(String restAPIName,
                                            String userId,
                                            String guid) throws UserNotAuthorizedException,
                                                                PropertyServerException,
                                                                SubjectAreaCheckedException
    {
        String methodName = "callOMRSRestoreRelationship";
        showMethodNameIfDebugEnabled(methodName);

        try {
            getRepositoryHandler().restoreRelationship(
                    userId,
                    guid,
                    restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }

    }

    public void callOMRSPurgeRelationship(String restAPIName,
                                          String userId,
                                          String typeGuid,
                                          String typeName,
                                          String guid) throws UserNotAuthorizedException,
                                                              PropertyServerException,
                                                              SubjectAreaCheckedException
    {

        String methodName = "callOMRSPurgeRelationship";
        showMethodNameIfDebugEnabled(methodName);

        try {
            getRepositoryHandler().purgeRelationship(
                    userId,
                    typeGuid,
                    typeName,
                    guid,
                    restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
    }

    public List<Relationship> getRelationshipsByType(String userId,
                                                     String entityGuid,
                                                     String entityTypeName,
                                                     String relationshipTypeName,
                                                     String methodName) throws UserNotAuthorizedException,
                                                                               PropertyServerException,
                                                                               SubjectAreaCheckedException
    {
        try {
            String relationshipTypeGuid = this.getTypeDefGUID(methodName, relationshipTypeName);
            return getRepositoryHandler()
                    .getRelationshipsByType(
                            userId,
                            entityGuid,
                            entityTypeName,
                            relationshipTypeGuid,
                            relationshipTypeName,
                            methodName
                    );
        } catch (UserNotAuthorizedException | PropertyServerException e) {
            throw e;
        }  catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }
        return null;
    }

    public List<Relationship> callGetRelationshipsForEntity(String restAPIName,
                                                            String userId,
                                                            String entityGUID,
                                                            String relationshipTypeGuid,
                                                            int fromRelationshipElement,
                                                            int pageSize) throws PropertyServerException,
                                                                                 SubjectAreaCheckedException,
                                                                                 UserNotAuthorizedException
    {
        return this.callGetRelationshipsForEntity(
                restAPIName,
                userId,
                entityGUID,
                relationshipTypeGuid,
                fromRelationshipElement,
                pageSize,
                null,
                null,
                null);
    }

    public List<Relationship> getAllRelationshipsForEntity(String restAPIName,
                                                           String userId,
                                                           String entityGUID,
                                                           FindRequest findRequest) throws PropertyServerException,
                                                                                           SubjectAreaCheckedException,
                                                                                           UserNotAuthorizedException
    {
        return this.callGetRelationshipsForEntity(
                restAPIName,
                userId,
                entityGUID,
                null,
                findRequest.getStartingFrom(),
                findRequest.getPageSize(),
                findRequest.getAsOfTime(),
                findRequest.getSequencingProperty(),
                findRequest.getSequencingOrder());
    }

    public List<Relationship> callGetRelationshipsForEntity(String restAPIName,
                                                            String userId,
                                                            String entityGUID,
                                                            String relationshipTypeGuid,
                                                            int fromRelationshipElement,
                                                            int pageSize,
                                                            Date asOfTime,
                                                            String sequencingProperty,
                                                            SequencingOrder sequencingOrder) throws SubjectAreaCheckedException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        return this.callGetRelationshipsForEntity(
                restAPIName,
                userId,
                entityGUID,
                relationshipTypeGuid,
                fromRelationshipElement,
                Collections.singletonList(InstanceStatus.ACTIVE),
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);
    }

    public List<Relationship> callGetRelationshipsForEntity(String restAPIName,
                                                            String userId,
                                                            String entityGUID,
                                                            String relationshipTypeGUID,
                                                            int fromRelationshipElement,
                                                            List<InstanceStatus> limitResultsByStatus,
                                                            Date asOfTime,
                                                            String sequencingProperty,
                                                            SequencingOrder sequencingOrder,
                                                            int pageSize) throws UserNotAuthorizedException,
                                                                                 PropertyServerException,
                                                                                 SubjectAreaCheckedException
    {
        String methodName = "callGetRelationshipsForEntity";
        showMethodNameIfDebugEnabled(methodName);

        try {
            return getRepositoryHandler().getRelationshipsByType(
                            userId,
                            entityGUID,
                            relationshipTypeGUID,
                            limitResultsByStatus,
                            asOfTime,
                            sequencingProperty,
                            sequencingOrder,
                            fromRelationshipElement,
                            pageSize,
                            restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }

        return null;
    }

    public List<EntityDetail> callGetEntitiesForRelationshipEnd2(String restAPIName,
                                                                 String userId,
                                                                 String anchorEntityGUID,
                                                                 String anchorEntityTypeName,
                                                                 String relationshipTypeName) throws PropertyServerException,
                                                                                                     SubjectAreaCheckedException,
                                                                                                     UserNotAuthorizedException
    {
        final String relationshipTypeGUID = getTypeDefGUID(relationshipTypeName);
        return callGetEntitiesForRelationshipEnd(
                restAPIName,
                userId,
                anchorEntityGUID,
                anchorEntityTypeName,
                false,
                relationshipTypeGUID,
                relationshipTypeName,
                0,
                0
        );

    }

    public List<EntityDetail> callGetEntitiesForRelationshipEnd1(String restAPIName,
                                                                 String userId,
                                                                 String anchorEntityGUID,
                                                                 String anchorEntityTypeName,
                                                                 String relationshipTypeName,
                                                                 int startingFrom,
                                                                 int pageSize) throws PropertyServerException,
                                                                                                     SubjectAreaCheckedException,
                                                                                                     UserNotAuthorizedException
    {
        final String relationshipTypeGUID = getTypeDefGUID(relationshipTypeName);
        return callGetEntitiesForRelationshipEnd(
                restAPIName,
                userId,
                anchorEntityGUID,
                anchorEntityTypeName,
                true,
                relationshipTypeGUID,
                relationshipTypeName,
                startingFrom,
                pageSize
                                                );

    }
    public List<EntityDetail> callGetEntitiesForRelationshipEnd2(String restAPIName,
                                                                 String userId,
                                                                 String anchorEntityGUID,
                                                                 String anchorEntityTypeName,
                                                                 String relationshipTypeName,
                                                                 int startingFrom,
                                                                 int pageSize) throws PropertyServerException,
                                                                                      SubjectAreaCheckedException,
                                                                                      UserNotAuthorizedException
    {
        final String relationshipTypeGUID = getTypeDefGUID(relationshipTypeName);
        return callGetEntitiesForRelationshipEnd(
                restAPIName,
                userId,
                anchorEntityGUID,
                anchorEntityTypeName,
                false,
                relationshipTypeGUID,
                relationshipTypeName,
                startingFrom,
                pageSize
                                                );

    }

    public List<EntityDetail> callGetEntitiesForRelationshipEnd(String restAPIName,
                                                                String userId,
                                                                String anchorEntityGUID,
                                                                String anchorEntityTypeName,
                                                                boolean anchorAtEnd1,
                                                                String  relationshipTypeGUID,
                                                                String  relationshipTypeName,
                                                                int startingFrom,
                                                                int pageSize) throws SubjectAreaCheckedException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String methodName = "callGetEntitiesForRelationshipEnd";
        showMethodNameIfDebugEnabled(methodName);

        try {
            return getRepositoryHandler().getEntitiesForRelationshipEnd(
                    userId,
                    anchorEntityGUID,
                    anchorEntityTypeName,
                    anchorAtEnd1,
                    relationshipTypeGUID,
                    relationshipTypeName,
                    startingFrom,
                    pageSize,
                    restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        }  catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }

        return null;
    }

    public InstanceGraph callGetEntityNeighbourhood(String restAPIName,
                                                    String userId,
                                                    String entityGUID,
                                                    List<String> entityTypeGUIDs,
                                                    List<String> relationshipTypeGUIDs,
                                                    List<InstanceStatus> limitResultsByStatus,
                                                    List<String> limitResultsByClassification,
                                                    Date asOfTime,
                                                    int level) throws SubjectAreaCheckedException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        String methodName = "callgetEntityNeighborhood";
        showMethodNameIfDebugEnabled(methodName);

        try {
            return getRepositoryHandler().getEntityNeighborhood(
                            userId,
                            entityGUID,
                            entityTypeGUIDs,
                            relationshipTypeGUIDs,
                            limitResultsByStatus,
                            limitResultsByClassification,
                            asOfTime,
                            level,
                            restAPIName
            );
        } catch (PropertyServerException | UserNotAuthorizedException e) {
            throw e;
        } catch (Throwable error) {
            prepareUnexpectedError(error, methodName);
        }

        return null;
    }


    public List<EntityDetail> findEntitiesByPropertyValue(String restAPIName,
                                                          String userId,
                                                          String typeName,
                                                          FindRequest findRequest) throws PropertyServerException,
                                                                                          SubjectAreaCheckedException,
                                                                                          UserNotAuthorizedException
    {
        return findEntitiesByPropertyValue(
                restAPIName,
                userId,
                typeName,
                findRequest.getSearchCriteria(),
                findRequest.getAsOfTime(),
                findRequest.getStartingFrom(),
                findRequest.getPageSize(),
                findRequest.getSequencingOrder(),
                findRequest.getSequencingProperty()
        );
    }

    public List<EntityDetail> findEntitiesByPropertyValue(String restAPIName,
                                                          String userId,
                                                          String typeName,
                                                          String searchCriteria,
                                                          Date asOfTime,
                                                          int startingFrom,
                                                          int pageSize,
                                                          SequencingOrder sequencingOrder,
                                                          String sequencingProperty) throws SubjectAreaCheckedException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        String entityTypeGUID = this.getTypeDefGUID("findEntitiesByPropertyValue", typeName);
        return this.callFindEntitiesByPropertyValue(
                userId,
                entityTypeGUID,
                searchCriteria,
                startingFrom,
                pageSize,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                restAPIName
        );
    }

    public List<EntityDetail> getEntitiesByType(String restAPIName,
                                                String userId,
                                                String typeName,
                                                FindRequest findRequest) throws PropertyServerException,
                                                                                SubjectAreaCheckedException,
                                                                                UserNotAuthorizedException
    {
        return this.getEntitiesByType(
                restAPIName,
                userId,
                typeName,
                findRequest.getAsOfTime(),
                findRequest.getStartingFrom(),
                findRequest.getPageSize(),
                findRequest.getSequencingProperty(),
                findRequest.getSequencingOrder()
        );
    }

    public List<EntityDetail> getEntitiesByType(String restAPIName,
                                                String userId,
                                                String typeName,
                                                Date asOfTime,
                                                int startingFrom,
                                                int pageSize,
                                                String sequencingProperty,
                                                SequencingOrder sequencingOrder) throws SubjectAreaCheckedException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        String entityTypeGUID = this.getTypeDefGUID("getEntitiesByType", typeName);
        return this.callGetEntitiesByType(
                userId,
                entityTypeGUID,
                asOfTime,
                startingFrom,
                pageSize,
                sequencingProperty,
                sequencingOrder,
                restAPIName
        );
    }

    /**
     * @param relationshipTypeNames relationships type names for search
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param entityGuid   guid of the glossary to be deleted.
     * @param entityTypeName starting entity's type name
     * @param methodName  name of the method being called.
     * @return empty content or not
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws SubjectAreaCheckedException standard exception Subject Area OMAS services
     */
    public boolean isEmptyContent(List<String> relationshipTypeNames,
                                  String userId,
                                  String entityGuid,
                                  String entityTypeName,
                                  String methodName) throws SubjectAreaCheckedException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException {
        for (String relationshipTypeName : relationshipTypeNames) {
            List<Relationship> relationships = getRelationshipsByType(
                    userId,
                    entityGuid,
                    entityTypeName,
                    relationshipTypeName,
                    methodName
            );

            if (CollectionUtils.isNotEmpty(relationships)) {
                return false;
            }
        }

        return true;
    }

    public String getTypeDefGUID(String typeDefName) {
        return getTypeDefGUID(getServiceName(), typeDefName);
    }

    public String getTypeDefGUID(String sourceName,  String typeDefName) {
        return getOMRSRepositoryHelper().getTypeDefByName(sourceName, typeDefName).getGUID();
    }

    public boolean isTypeOf(String actualTypeName, String expectedTypeName) {
        return getOMRSRepositoryHelper().isTypeOf(getServiceName(), actualTypeName, expectedTypeName);
    }

    private void showMethodNameIfDebugEnabled(String methodName) {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
    }

    private void prepareUnexpectedError(Throwable error, String methodName) throws SubjectAreaCheckedException {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getMessage());
        throw new SubjectAreaCheckedException(messageDefinition, this.getClass().getName(), methodName);
    }
}