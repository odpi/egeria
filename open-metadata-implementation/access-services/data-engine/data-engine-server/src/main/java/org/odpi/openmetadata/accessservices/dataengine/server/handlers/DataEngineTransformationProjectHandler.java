/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.TransformationProject;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.TransformationProjectBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.TransformationProjectMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Optional;

import static org.odpi.openmetadata.accessservices.dataengine.server.mappers.TransformationProjectMapper.COLLECTION_MEMBERSHIP_NAME;

/**
 * DataEngineTransformationProjectHandler manages transformation project objects. It runs server-side in the
 * DataEngine OMAS and creates and retrieves transformation project entities through the OMRSRepositoryConnector.
 */
public class DataEngineTransformationProjectHandler {
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final OpenMetadataAPIGenericHandler<TransformationProject> transformationProjectHandler;
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
     * @param transformationProjectHandler  handler for managing schema attributes in the metadata repositories
     */
    public DataEngineTransformationProjectHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  OpenMetadataAPIGenericHandler<TransformationProject> transformationProjectHandler,
                                                  DataEngineRegistrationHandler dataEngineRegistrationHandler,
                                                  DataEngineCommonHandler dataEngineCommonHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.transformationProjectHandler = transformationProjectHandler;
        this.dataEngineRegistrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
    }

    /**
     * Create the schema type entity, with the corresponding schema attributes and relationships if it doesn't exist or
     * updates the existing one.
     *
     * @param userId                the name of the calling user
     * @param transformationProject the transformationProject type values
     * @param externalSourceName    the unique name of the external source
     * @return unique identifier of the schema type in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createTransformationProject(String userId, TransformationProject transformationProject, String externalSourceName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String methodName = "createTransformationProject";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(transformationProject.getQualifiedName(), TransformationProjectMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        TransformationProjectBuilder builder = getTransformationProjectBuilder(transformationProject);

        return transformationProjectHandler.createBeanInRepository(userId, externalSourceGUID, externalSourceName,
                TransformationProjectMapper.COLLECTION_TYPE_GUID, TransformationProjectMapper.COLLECTION_TYPE_NAME,
                transformationProject.getQualifiedName(), TransformationProjectMapper.QUALIFIED_NAME_PROPERTY_NAME, builder, methodName);
    }

    TransformationProjectBuilder getTransformationProjectBuilder(TransformationProject transformationProject) {
        TransformationProjectBuilder builder = new TransformationProjectBuilder(transformationProject.getQualifiedName(),
                transformationProject.getName(), TransformationProjectMapper.COLLECTION_TYPE_NAME, repositoryHelper, serviceName, serverName);
        return builder;
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
    public Optional<EntityDetail> findTransformationProjectEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
            PropertyServerException,
            InvalidParameterException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, TransformationProjectMapper.COLLECTION_TYPE_NAME);
    }

    /**
     * Create CollectionMembership relationships between a Process asset and the Transformation Project. Verifies that the
     * relationship is not present before creating it
     *
     * @param userId             the name of the calling user
     * @param processGUID        the unique identifier of the process
     * @param transformationProjectGuid           the unique identifier of the collection that represents the transformation project
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addProcessTransformationProjectRelationship(String userId, String processGUID, String transformationProjectGuid, String externalSourceName)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {

        dataEngineCommonHandler.upsertExternalRelationship(userId, processGUID, transformationProjectGuid,
                COLLECTION_MEMBERSHIP_NAME, TransformationProjectMapper.COLLECTION_TYPE_NAME, externalSourceName,
                null);
    }
}