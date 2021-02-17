/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

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
     * @param displayName                      the display name
     * @param qualifiedName                    unique name for this asset
     * @param technicalName                    the stored display name property for the asset
     * @param technicalDescription             the stored description property associated with the asset
     * @param zoneMembership                   initial zones for the asset - or null to allow the security module to set it up
     * @param owner                            identifier of the owner
     * @param ownerType                        is the owner identifier a user id, personal profile or team profile
     * @param additionalProperties             any arbitrary properties not part of the type system
     * @param typeGUID                         identifier of the type that is a subtype of asset - or null to create standard type
     * @param typeName                         name of the type that is a subtype of asset - or null to create standard type
     * @param formula                          formula
     * @param extendedProperties               properties from any subtype
     * @param instanceStatus                   initial status of the Asset in the metadata repository
     * @param methodName                       calling method
     * @return unique identifier of the new asset
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createAssetInRepository(String userId, String externalSourceGUID, String externalSourceName,
                                          String displayName, String qualifiedName, String technicalName, String technicalDescription,
                                          List<String> zoneMembership, String owner, int ownerType, Map<String, String> additionalProperties,
                                          String typeGUID, String typeName, String formula, Map<String, Object> extendedProperties,
                                          InstanceStatus instanceStatus, String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ProcessPropertiesBuilder builder = new ProcessPropertiesBuilder(qualifiedName, displayName, technicalName,
                technicalDescription, zoneMembership, owner, ownerType, typeGUID, typeName, formula, additionalProperties,
                extendedProperties, instanceStatus, repositoryHelper, serviceName, serverName, methodName, userId);

        return this.createBeanInRepository(userId, externalSourceGUID, externalSourceName, typeGUID, typeName, qualifiedName,
                OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, builder, methodName);
    }
}
