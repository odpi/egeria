/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.server.builders.SoftwareServerPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SoftwareServerPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class SoftwareServerRegistrationHandler {
    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

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

        return repositoryHandler.createEntity(userId,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, properties, methodName);
    }

    /**
     * Return the properties from a discovery engine definition.  The discovery engine
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId        identifier of calling user
     * @param qualifiedName unique identifier (guid) of the discovery engine definition.
     *
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public String getSoftwareServerCapabilityByQualifiedName(String userId,
                                                             String qualifiedName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException {
        final String methodName = "getSoftwareServerCapabilityByQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);


        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, methodName);

        return retrievedEntity.getGUID();
    }
}