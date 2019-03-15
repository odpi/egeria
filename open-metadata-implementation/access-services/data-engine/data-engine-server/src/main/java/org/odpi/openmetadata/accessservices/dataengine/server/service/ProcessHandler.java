/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.dataengine.server.util.DataEngineErrorHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.util.EntitiesCreatorHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * ProcessHandler manages Process objects from the property server.  It runs server-side in the DataEngine OMAS
 * and creates process entities with input/output relationships through the OMRSRepositoryConnector.
 */
class ProcessHandler {
    private static final String PROCESS_TYPE_NAME = "Process";
    private static final String DISPLAY_NAME_PROPERTY_NAME = "displayName";
    private static final String PROCESS_PORT_RELATIONSHIP_TYPE_NAME = "ProcessPort";

    private DataEngineErrorHandler errorHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private String serviceName;
    private EntitiesCreatorHelper entitiesCreatorHelper;

    /**
     * Construct the process handler with a link to the property server's connector, a link to the metadata collection
     * and this access service's official name.
     *
     * @param serviceName         name of this service
     * @param repositoryConnector connector to the property server.
     */
    ProcessHandler(String serviceName, OMRSRepositoryConnector repositoryConnector,
                   OMRSMetadataCollection metadataCollection) {
        this.serviceName = serviceName;
        if (repositoryConnector != null) {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            entitiesCreatorHelper = new EntitiesCreatorHelper(serviceName, repositoryHelper, metadataCollection);
        }
        errorHandler = new DataEngineErrorHandler();
    }

    /**
     * Create the process
     *
     * @param userId         the name of the calling user
     * @param processName    the name of the process
     * @param description    the description of the process
     * @param latestChange   the description for the latest change done for the asset
     * @param zoneMembership the list of zones of the process
     * @param displayName    the display name of the process
     *
     * @return the guid of the created process
     *
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request
     * @throws TypeErrorException unknown or invalid type
     * @throws ClassificationErrorException the requested classification is either not known or not valid for the entity
     * @throws StatusNotSupportedException status not supported
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws RepositoryErrorException no metadata collection
     * @throws PropertyErrorException there is a problem with one of the other parameters
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the requesting user is
     * not authorized to issue this request
     * @throws FunctionNotSupportedException the repository does not support this call
     */
    String createProcess(String userId, String processName, String description, String latestChange,
                         List<String> zoneMembership, String displayName) throws
                                                                          UserNotAuthorizedException,
                                                                          TypeErrorException,
                                                                          ClassificationErrorException,
                                                                          StatusNotSupportedException,
                                                                          org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          PropertyErrorException,
                                                                          FunctionNotSupportedException {

        final String methodName = "createProcess";
        errorHandler.validateUserId(userId, methodName);

        InstanceProperties instanceProperties = entitiesCreatorHelper.createAssetInstanceProperties(userId, processName,
                description, latestChange, zoneMembership);
        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, instanceProperties,
                DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);

        return entitiesCreatorHelper.createEntity(userId, instanceProperties, PROCESS_TYPE_NAME);
    }


    /**
     * Create ProcessPort relationships between a Process asset and the corresponding Ports
     *
     * @param userId      the name of the calling user
     * @param processGuid the unique identifier of the process
     * @param ports       list of port unique identifiers
     *
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws TypeErrorException unknown or invalid type
     * @throws StatusNotSupportedException status not supported
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the requesting user
     * is not authorized to issue this request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws RepositoryErrorException no metadata collection
     * @throws PropertyErrorException there is a problem with one of the other parameters
     * @throws EntityNotKnownException the entity instance is not known in the metadata collection
     * @throws FunctionNotSupportedException the repository does not support this call
     */
    void addProcessPortRelationships(String userId, String processGuid, List<String> ports) throws
                                                                                            UserNotAuthorizedException,
                                                                                            TypeErrorException,
                                                                                            StatusNotSupportedException,
                                                                                            FunctionNotSupportedException,
                                                                                            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                                                                            EntityNotKnownException,
                                                                                            InvalidParameterException,
                                                                                            RepositoryErrorException,
                                                                                            PropertyErrorException {
        if (CollectionUtils.isEmpty(ports)) {
            return;
        }
        errorHandler.validateUserId(userId, "addInputRelationship");

        for (String portGuid : ports) {
            entitiesCreatorHelper.addRelationship(userId, PROCESS_PORT_RELATIONSHIP_TYPE_NAME, processGuid, portGuid);
        }
    }
}