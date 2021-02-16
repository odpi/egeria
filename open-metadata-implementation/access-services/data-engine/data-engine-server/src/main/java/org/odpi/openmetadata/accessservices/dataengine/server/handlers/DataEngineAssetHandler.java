/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ProcessPropertiesBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

public class DataEngineAssetHandler<B> extends AssetHandler<B> {
    /**
     * Construct the asset handler with information needed to work with B objects.
     *
     * @param converter               specific converter for this bean class
     * @param beanClass               name of bean class that is represented by the generic class B
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param localServerUserId       userId for this server
     * @param securityVerifier        open metadata security services verifier
     * @param supportedZones          list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones            list of zones that the access service should set in all new B instances.
     * @param publishZones            list of zones that the access service sets up in published B instances.
     * @param auditLog                destination for audit log events.
     */
    public DataEngineAssetHandler(OpenMetadataAPIGenericConverter<B> converter, Class<B> beanClass, String serviceName,
                                  String serverName, InvalidParameterHandler invalidParameterHandler, RepositoryHandler repositoryHandler,
                                  OMRSRepositoryHelper repositoryHelper, String localServerUserId,
                                  OpenMetadataServerSecurityVerifier securityVerifier,
                                  List<String> supportedZones, List<String> defaultZones, List<String> publishZones, AuditLog auditLog) {
        super(converter, beanClass, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
    }

    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId                           calling user
     * @param externalSourceGUID               unique identifier of software server capability representing the caller
     * @param externalSourceName               unique name of software server capability representing the caller
     * @param qualifiedName                    unique name for this asset
     * @param typeGUID                         identifier of the type that is a subtype of asset - or null to create standard type
     * @param typeName                         name of the type that is a subtype of asset - or null to create standard type
     * @param methodName                       calling method
     * @return unique identifier of the new asset
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createAssetInRepository(String userId, String externalSourceGUID, String externalSourceName, String qualifiedName,
                                          String typeGUID, String typeName, ProcessPropertiesBuilder builder, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        return this.createBeanInRepository(userId, externalSourceGUID, externalSourceName, typeGUID, typeName, qualifiedName,
                OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, builder, methodName);
    }

    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param typeGUID identifier of the type that is a subtype of Asset - or null to create standard type
     * @param typeName name of the type that is a subtype of Asset - or null to create standard type
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param builder the builder used for the updated process
     * @param updatedProcess the updated process
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String userId, String externalSourceGUID, String externalSourceName, String assetGUID,
                            String assetGUIDParameterName, String typeGUID, String typeName, boolean isMergeUpdate,
                            ProcessPropertiesBuilder builder, Process updatedProcess, String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        this.updateBeanInRepository(userId, externalSourceGUID, externalSourceName, assetGUID, assetGUIDParameterName,
                typeGUID, typeName, updatedProcess.getZoneMembership(), builder.getInstanceProperties(methodName), isMergeUpdate,
                methodName);
    }
}
