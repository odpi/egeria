/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Collection;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.CollectionBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.COLLECTION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.COLLECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.REFERENCEABLE_TO_COLLECTION_TYPE_NAME;

/**
 * DataEngineCollectionHandler manages collection objects. It runs server-side in the
 * DataEngine OMAS and creates and retrieves collections entities through the OMRSRepositoryConnector.
 */
public class DataEngineCollectionHandler {
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final OpenMetadataAPIGenericHandler<Collection> collectionOpenMetadataAPIGenericHandler;
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                   name of this service
     * @param serverName                    name of the local server
     * @param invalidParameterHandler       handler for managing parameter errors
     * @param repositoryHelper              provides utilities for manipulating the repository services objects
     * @param dataEngineRegistrationHandler provides calls for retrieving external data engine guid
     * @param dataEngineCommonHandler       provides utilities for manipulating entities
     * @param collectionOpenMetadataAPIGenericHandler helps building model for creating Collection metadata associated with Process assets
     */
    public DataEngineCollectionHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                       OMRSRepositoryHelper repositoryHelper,
                                       OpenMetadataAPIGenericHandler<Collection> collectionOpenMetadataAPIGenericHandler,
                                       DataEngineRegistrationHandler dataEngineRegistrationHandler,
                                       DataEngineCommonHandler dataEngineCommonHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.collectionOpenMetadataAPIGenericHandler = collectionOpenMetadataAPIGenericHandler;
        this.dataEngineRegistrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
    }

    /**
     * Create the schema type entity, with the corresponding schema attributes and relationships if it doesn't exist or
     * updates the existing one.
     *
     * @param userId                the name of the calling user
     * @param collection            the collection type values
     * @param externalSourceName    the unique name of the external source
     * @return unique identifier of the schema type in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createCollection(String userId, Collection collection, String externalSourceName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String methodName = "createCollection";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(collection.getQualifiedName(), QUALIFIED_NAME_PROPERTY_NAME, methodName);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);

        CollectionBuilder builder = getCollectionBuilder(collection);

        return collectionOpenMetadataAPIGenericHandler.createBeanInRepository(userId, externalSourceGUID, externalSourceName, COLLECTION_TYPE_GUID,
                 COLLECTION_TYPE_NAME, builder, null, methodName);
    }

    CollectionBuilder getCollectionBuilder(Collection collection) {
        return new CollectionBuilder(collection.getQualifiedName(),
                collection.getName(), COLLECTION_TYPE_GUID, COLLECTION_TYPE_NAME, repositoryHelper, serviceName, serverName);
    }

    /**
     * Find out if the Transformation Project object is already stored in the repository as a Collection. It uses the fully qualified name to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the process to be searched
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> findCollectionEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
            PropertyServerException,
            InvalidParameterException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, COLLECTION_TYPE_NAME);
    }

    /**
     * Create CollectionMembership relationships between a Process asset and a Collection. Verifies that the
     * relationship is not present before creating it
     *
     * @param userId             the name of the calling user
     * @param processGUID        the unique identifier of the process
     * @param collectionGUID     the unique identifier of the collection
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addCollectionMembershipRelationship(String userId, String processGUID, String collectionGUID, String externalSourceName)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {

        dataEngineCommonHandler.upsertExternalRelationship(userId, processGUID, collectionGUID, REFERENCEABLE_TO_COLLECTION_TYPE_NAME,
                COLLECTION_TYPE_NAME, PROCESS_TYPE_NAME, externalSourceName, null);
    }
}