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

import java.util.List;

/**
 * DeployedAPIHandler manages DeployedAPI objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates port entities with wire relationships through the OMRSRepositoryConnector.
 */
class DeployedAPIHandler {
    private static final String DEPLOYED_API_TYPE_NAME = "DeployedAPI";
    private static final String ASSET_WIRE_RELATIONSHIP_TYPE_NAME = "AssetWire";

    private DataEngineErrorHandler errorHandler;
    private EntitiesCreatorHelper entitiesCreatorHelper;

    /**
     * Construct the deployed api handler with a link to the property server's connector, a link to the metadata
     * collection and this access service's official name.
     *
     * @param serviceName         name of this service
     * @param repositoryConnector connector to the property server
     */
    DeployedAPIHandler(String serviceName, OMRSRepositoryConnector repositoryConnector,
                       OMRSMetadataCollection metadataCollection) {
        if (repositoryConnector != null) {
            OMRSRepositoryHelper repositoryHelper = repositoryConnector.getRepositoryHelper();
            entitiesCreatorHelper = new EntitiesCreatorHelper(serviceName, repositoryHelper, metadataCollection);
        }
        errorHandler = new DataEngineErrorHandler();
    }

    /**
     * Create the the deployed api
     *
     * @param userId         the name of the calling user
     * @param name           the name of the the deployed api
     * @param description    the description of the the deployed api
     * @param latestChange   the description for the latest change done for the the deployed api
     * @param zoneMembership the list of zones of the the deployed api
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
    String createDeployedAPI(String userId, String name, String description, String latestChange,
                             List<String> zoneMembership) throws UserNotAuthorizedException, TypeErrorException,
                                                                 ClassificationErrorException,
                                                                 StatusNotSupportedException,
                                                                 org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                                                 InvalidParameterException, RepositoryErrorException,
                                                                 PropertyErrorException, FunctionNotSupportedException {
        errorHandler.validateUserId(userId, "createDeployedAPI");

        InstanceProperties instanceProperties = entitiesCreatorHelper.createAssetInstanceProperties(userId, name,
                description, latestChange, zoneMembership);
        return entitiesCreatorHelper.createEntity(userId, instanceProperties, DEPLOYED_API_TYPE_NAME);
    }

    /**
     * Create PortInterface relationships between a Port and the corresponding DeployedAPI
     *
     * @param userId          the name of the calling user
     * @param assetGuid       the unique identifier of the asset
     * @param deployedAPIGuid the unique identifier of the deployed api
     *
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws TypeErrorException unknown or invalid type
     * @throws StatusNotSupportedException status not supported
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the requesting user
     * is not authorized to issue this request
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws RepositoryErrorException no metadata collection
     * @throws PropertyErrorException there is a problem with one of the other parameters
     * @throws EntityNotKnownException the entity instance is not known in the metadata collection
     * @throws FunctionNotSupportedException the repository does not support this call
     */
    void addAssetWireRelationship(String userId, String assetGuid, String deployedAPIGuid) throws
                                                                                           UserNotAuthorizedException,
                                                                                           TypeErrorException,
                                                                                           StatusNotSupportedException,
                                                                                           org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                                                                           EntityNotKnownException,
                                                                                           InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           PropertyErrorException,
                                                                                           FunctionNotSupportedException {
        errorHandler.validateUserId(userId, "addAssetWireRelationship");

        entitiesCreatorHelper.addRelationship(userId, ASSET_WIRE_RELATIONSHIP_TYPE_NAME, assetGuid, deployedAPIGuid);
    }
}
