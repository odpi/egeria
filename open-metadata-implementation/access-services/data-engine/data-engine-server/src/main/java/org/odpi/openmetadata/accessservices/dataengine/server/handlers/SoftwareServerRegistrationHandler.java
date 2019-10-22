/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.server.builders.SoftwareServerPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SoftwareServerPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * SoftwareServerRegistrationHandler manages SoftwareServerCapability objects from the property server.  It runs
 * server-side in the DataEngine OMAS and creates software server capability entities through the
 * OMRSRepositoryConnector.
 */
public class SoftwareServerRegistrationHandler {
    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     */
    public SoftwareServerRegistrationHandler(String serviceName, String serverName,
                                             InvalidParameterHandler invalidParameterHandler,
                                             RepositoryHandler repositoryHandler,
                                             OMRSRepositoryHelper repositoryHelper) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;

    }

    /**
     * Create the software server capability entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the server
     * @param name          the name of the server
     * @param description   the description of the server
     * @param type          the type of the server
     * @param version       the version of the server
     * @param patchLevel    the patch level of the server
     * @param source        the source of the server
     *
     * @return unique identifier of the server in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createSoftwareServerCapability(String userId, String qualifiedName, String name, String description,
                                                 String type, String version, String patchLevel, String source) throws
                                                                                                                InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException {
        final String methodName = "createSoftwareServerCapability";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);

        SoftwareServerPropertiesBuilder builder = new SoftwareServerPropertiesBuilder(qualifiedName, name, description,
                type, version, patchLevel, source, null, null, repositoryHelper,
                serviceName, serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
        return repositoryHandler.createEntity(userId, entityTypeDef.getGUID(), entityTypeDef.getName(), properties,
                methodName);
    }

    /**
     * Return the guid of a software server capability entity
     *
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the server
     *
     * @return the guid of the server
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem retrieving the discovery engine definition
     */
    public String getSoftwareServerCapabilityByQualifiedName(String userId,
                                                             String qualifiedName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException {
        final String methodName = "getSoftwareServerCapabilityByQualifiedName";

        qualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);


        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties,
                entityTypeDef.getGUID(), entityTypeDef.getName(), methodName);

        return retrievedEntity.getGUID();
    }
}