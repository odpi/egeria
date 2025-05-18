/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;


/**
 * OperatingPlatformHandler provides the exchange of metadata about hosts, software server platforms and software servers between the repository and
 * the OMAS.
 */
public class ITInfrastructureHandler<B> extends AssetHandler<B>
{
    /**
     * Construct the operating platform handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of operating platforms that the access service is allowed to serve B instances from.
     * @param defaultZones list of operating platforms that the access service should set in all new B instances.
     * @param publishZones list of operating platforms that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    public ITInfrastructureHandler(OpenMetadataAPIGenericConverter<B> converter,
                                   Class<B>                           beanClass,
                                   String                             serviceName,
                                   String                             serverName,
                                   InvalidParameterHandler            invalidParameterHandler,
                                   RepositoryHandler                  repositoryHandler,
                                   OMRSRepositoryHelper               repositoryHelper,
                                   String                             localServerUserId,
                                   OpenMetadataServerSecurityVerifier securityVerifier,
                                   List<String>                       supportedZones,
                                   List<String>                       defaultZones,
                                   List<String>                       publishZones,
                                   AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
    }


    /**
     * Create a relationship between a host cluster and a host.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param hostClusterGUID unique identifier of the host cluster
     * @param hostClusterGUIDParameterName parameter supplying the hostClusterGUID
     * @param hostGUID unique identifier of the member host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupHostClusterMember(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  hostClusterGUID,
                                       String  hostClusterGUIDParameterName,
                                       String  hostGUID,
                                       String  hostGUIDParameterName,
                                       Date    effectiveFrom,
                                       Date    effectiveTo,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  hostClusterGUID,
                                  hostClusterGUIDParameterName,
                                  OpenMetadataType.HOST_CLUSTER.typeName,
                                  hostGUID,
                                  hostGUIDParameterName,
                                  OpenMetadataType.HOST.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.HOST_CLUSTER_MEMBER_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.HOST_CLUSTER_MEMBER_RELATIONSHIP.typeName,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between a host cluster and a host member.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param hostClusterGUID unique identifier of the host cluster
     * @param hostClusterGUIDParameterName parameter supplying the hostClusterGUID
     * @param hostGUID unique identifier of the member host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearHostClusterMember(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  hostClusterGUID,
                                       String  hostClusterGUIDParameterName,
                                       String  hostGUID,
                                       String  hostGUIDParameterName,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      hostClusterGUID,
                                      hostClusterGUIDParameterName,
                                      OpenMetadataType.HOST_CLUSTER.typeName,
                                      hostGUID,
                                      hostGUIDParameterName,
                                      OpenMetadataType.HOST.typeGUID,
                                      OpenMetadataType.HOST.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.HOST_CLUSTER_MEMBER_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.HOST_CLUSTER_MEMBER_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }



    /**
     * Create a relationship between a software server platform and a software server.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param softwareServerPlatformGUIDParameterName parameter supplying the softwareServerPlatformGUID
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param deploymentTime date/time that the capability was deployed
     * @param deployer user who issued the deploy command
     * @param serverStatus operational status of the server
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSoftwareServerDeployment(String  userId,
                                              String  externalSourceGUID,
                                              String  externalSourceName,
                                              String  softwareServerPlatformGUID,
                                              String  softwareServerPlatformGUIDParameterName,
                                              String  softwareServerGUID,
                                              String  softwareServerGUIDParameterName,
                                              Date    deploymentTime,
                                              String  deployer,
                                              int     serverStatus,
                                              Date    effectiveFrom,
                                              Date    effectiveTo,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getSoftwareServerSupportedCapabilitiesProperties(deploymentTime,
                                                                                                             deployer,
                                                                                                             serverStatus,
                                                                                                             methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  softwareServerPlatformGUID,
                                  softwareServerPlatformGUIDParameterName,
                                  OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                  softwareServerGUID,
                                  softwareServerGUIDParameterName,
                                  OpenMetadataType.SOFTWARE_SERVER.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName,
                                  relationshipProperties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between a software server platform and a software server.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param softwareServerPlatformGUIDParameterName parameter supplying the softwareServerPlatformGUID
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSoftwareServerDeployment(String  userId,
                                              String  externalSourceGUID,
                                              String  externalSourceName,
                                              String  softwareServerPlatformGUID,
                                              String  softwareServerPlatformGUIDParameterName,
                                              String  softwareServerGUID,
                                              String  softwareServerGUIDParameterName,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareServerPlatformGUID,
                                      softwareServerPlatformGUIDParameterName,
                                      OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                      softwareServerGUID,
                                      softwareServerGUIDParameterName,
                                      OpenMetadataType.SOFTWARE_SERVER.typeGUID,
                                      OpenMetadataType.SOFTWARE_SERVER.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a relationship between a software server and one of its endpoints.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param endpointGUID unique identifier of the endpoint
     * @param endpointGUIDParameterName parameter supplying the endpointGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSoftwareServerEndpoint(String  userId,
                                            String  externalSourceGUID,
                                            String  externalSourceName,
                                            String  softwareServerGUID,
                                            String  softwareServerGUIDParameterName,
                                            String  endpointGUID,
                                            String  endpointGUIDParameterName,
                                            Date    effectiveFrom,
                                            Date    effectiveTo,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  softwareServerGUID,
                                  softwareServerGUIDParameterName,
                                  OpenMetadataType.SOFTWARE_SERVER.typeName,
                                  endpointGUID,
                                  endpointGUIDParameterName,
                                  OpenMetadataType.ENDPOINT.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between a software server and one of its endpoints.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param endpointGUID unique identifier of the endpoint
     * @param endpointGUIDParameterName parameter supplying the endpointGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSoftwareServerEndpoint(String  userId,
                                            String  externalSourceGUID,
                                            String  externalSourceName,
                                            String  softwareServerGUID,
                                            String  softwareServerGUIDParameterName,
                                            String  endpointGUID,
                                            String  endpointGUIDParameterName,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareServerGUID,
                                      softwareServerGUIDParameterName,
                                      OpenMetadataType.SOFTWARE_SERVER.typeName,
                                      endpointGUID,
                                      endpointGUIDParameterName,
                                      OpenMetadataType.ENDPOINT.typeGUID,
                                      OpenMetadataType.ENDPOINT.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a relationship between a software server and a software capability.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param softwareCapabilityGUIDParameterName parameter supplying the softwareCapabilityGUID
     * @param deploymentTime date/time that the capability was deployed
     * @param deployer user who issued the deploy command
     * @param serverCapabilityStatus operational status of the capability
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSupportedSoftwareCapability(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  softwareServerGUID,
                                                 String  softwareServerGUIDParameterName,
                                                 String  softwareCapabilityGUID,
                                                 String  softwareCapabilityGUIDParameterName,
                                                 Date    deploymentTime,
                                                 String  deployer,
                                                 int     serverCapabilityStatus,
                                                 Date    effectiveFrom,
                                                 Date    effectiveTo,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getSoftwareServerSupportedCapabilitiesProperties(deploymentTime,
                                                                                                             deployer,
                                                                                                             serverCapabilityStatus,
                                                                                                             methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  softwareServerGUID,
                                  softwareServerGUIDParameterName,
                                  OpenMetadataType.IT_INFRASTRUCTURE.typeName,
                                  softwareCapabilityGUID,
                                  softwareCapabilityGUIDParameterName,
                                  OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName,
                                  relationshipProperties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between a software server and a software capability.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param softwareCapabilityGUIDParameterName parameter supplying the softwareCapabilityGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSupportedSoftwareCapability(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  softwareServerGUID,
                                                 String  softwareServerGUIDParameterName,
                                                 String  softwareCapabilityGUID,
                                                 String  softwareCapabilityGUIDParameterName,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareServerGUID,
                                      softwareServerGUIDParameterName,
                                      OpenMetadataType.IT_INFRASTRUCTURE.typeName,
                                      softwareCapabilityGUID,
                                      softwareCapabilityGUIDParameterName,
                                      OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                      OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a relationship between a software capability and an asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param softwareCapabilityGUIDParameterName parameter supplying the softwareCapabilityGUID
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter supplying the assetGUID
     * @param description description of the use
     * @param useType server asset use type
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSoftwareCapabilityAssetUse(String  userId,
                                                String  externalSourceGUID,
                                                String  externalSourceName,
                                                String  softwareCapabilityGUID,
                                                String  softwareCapabilityGUIDParameterName,
                                                String  assetGUID,
                                                String  assetGUIDParameterName,
                                                String  description,
                                                int     useType,
                                                Date    effectiveFrom,
                                                Date    effectiveTo,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getSoftwareServerCapabilitiesAssetUseProperties(description, useType, methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  softwareCapabilityGUID,
                                  softwareCapabilityGUIDParameterName,
                                  OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataType.ASSET.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                  relationshipProperties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between a software server and a software capability.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param softwareCapabilityGUIDParameterName parameter supplying the assetGUID
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter supplying the assetGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSoftwareCapabilityAssetUse(String  userId,
                                                String  externalSourceGUID,
                                                String  externalSourceName,
                                                String  softwareCapabilityGUID,
                                                String  softwareCapabilityGUIDParameterName,
                                                String  assetGUID,
                                                String  assetGUIDParameterName,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareCapabilityGUID,
                                      softwareCapabilityGUIDParameterName,
                                      OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                      assetGUID,
                                      assetGUIDParameterName,
                                      OpenMetadataType.ASSET.typeGUID,
                                      OpenMetadataType.ASSET.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }

    /**
     * Create a relationship between a network gateway and the network it connects to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param networkGatewayGUID unique identifier of the network gateway
     * @param networkGatewayGUIDParameterName parameter supplying the networkGatewayGUID
     * @param networkGUID unique identifier of The network
     * @param networkGUIDParameterName parameter supplying the networkGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupNetworkGatewayLink(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  networkGatewayGUID,
                                        String  networkGatewayGUIDParameterName,
                                        String  networkGUID,
                                        String  networkGUIDParameterName,
                                        Date    effectiveFrom,
                                        Date    effectiveTo,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  networkGatewayGUID,
                                  networkGatewayGUIDParameterName,
                                  OpenMetadataType.NETWORK_GATEWAY.typeName,
                                  networkGUID,
                                  networkGUIDParameterName,
                                  OpenMetadataType.NETWORK.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP.typeName,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between a network gateway and the network it connects to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param networkGatewayGUID unique identifier of the network gateway
     * @param networkGatewayGUIDParameterName parameter supplying the networkGatewayGUID
     * @param networkGUID unique identifier of the network
     * @param networkGUIDParameterName parameter supplying the networkGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearNetworkGatewayLink(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  networkGatewayGUID,
                                        String  networkGatewayGUIDParameterName,
                                        String  networkGUID,
                                        String  networkGUIDParameterName,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      networkGatewayGUID,
                                      networkGatewayGUIDParameterName,
                                      OpenMetadataType.NETWORK_GATEWAY.typeName,
                                      networkGUID,
                                      networkGUIDParameterName,
                                      OpenMetadataType.NETWORK.typeGUID,
                                      OpenMetadataType.NETWORK.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }



    /**
     * Mark the host as a Cloud Provider.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param hostGUID unique identifier of host
     * @param hostGUIDParameterName parameter name supplying hostGUID
     * @param providerName name of the cloud provider
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCloudProviderClassification(String  userId,
                                                String  externalSourceGUID,
                                                String  externalSourceName,
                                                String  hostGUID,
                                                String  hostGUIDParameterName,
                                                String  providerName,
                                                Date    effectiveFrom,
                                                Date    effectiveTo,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, hostGUIDParameterName, methodName);

        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           hostGUID,
                                           hostGUIDParameterName,
                                           OpenMetadataType.HOST.typeName,
                                           OpenMetadataType.CLOUD_PROVIDER_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.CLOUD_PROVIDER_CLASSIFICATION.typeName,
                                           builder.getCloudProviderProperties(providerName, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the CloudProvider designation from a host.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param hostGUID unique identifier of host
     * @param hostGUIDParameterName parameter name supplying hostGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCloudProviderClassification(String  userId,
                                                   String  externalSourceGUID,
                                                   String  externalSourceName,
                                                   String  hostGUID,
                                                   String  hostGUIDParameterName,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                hostGUID,
                                                hostGUIDParameterName,
                                                OpenMetadataType.HOST.typeName,
                                                OpenMetadataType.CLOUD_PROVIDER_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.CLOUD_PROVIDER_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }



    /**
     * Mark the software server platform as a Cloud Platform.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param softwareServerPlatformGUID unique identifier of software server platform
     * @param softwareServerPlatformGUIDParameterName parameter name supplying softwareServerPlatformGUID
     * @param implementationType type of cloud platform implementation
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCloudPlatformClassification(String  userId,
                                                String  externalSourceGUID,
                                                String  externalSourceName,
                                                String  softwareServerPlatformGUID,
                                                String  softwareServerPlatformGUIDParameterName,
                                                String  implementationType,
                                                Date    effectiveFrom,
                                                Date    effectiveTo,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(softwareServerPlatformGUID, softwareServerPlatformGUIDParameterName, methodName);

        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           softwareServerPlatformGUID,
                                           softwareServerPlatformGUIDParameterName,
                                           OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                           OpenMetadataType.CLOUD_PLATFORM_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.CLOUD_PLATFORM_CLASSIFICATION.typeName,
                                           builder.getCloudPlatformProperties(implementationType, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the Cloud Platform designation from a software server platform.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param softwareServerPlatformGUID unique identifier of software server platform
     * @param softwareServerPlatformParameterName parameter name supplying softwareServerPlatformGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCloudPlatformClassification(String  userId,
                                                   String  externalSourceGUID,
                                                   String  externalSourceName,
                                                   String  softwareServerPlatformGUID,
                                                   String  softwareServerPlatformParameterName ,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                softwareServerPlatformGUID,
                                                softwareServerPlatformParameterName,
                                                OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                                OpenMetadataType.CLOUD_PLATFORM_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.CLOUD_PLATFORM_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }



    /**
     * Mark the software server as a Cloud Tenant.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param softwareServerGUID unique identifier of software server
     * @param softwareServerGUIDParameterName parameter name supplying softwareServerGUID
     * @param tenantName name of the tenant
     * @param tenantType type of tenant
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCloudTenantClassification(String  userId,
                                              String  externalSourceGUID,
                                              String  externalSourceName,
                                              String  softwareServerGUID,
                                              String  softwareServerGUIDParameterName,
                                              String  tenantName,
                                              String  tenantType,
                                              Date    effectiveFrom,
                                              Date    effectiveTo,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(softwareServerGUID, softwareServerGUIDParameterName, methodName);

        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           softwareServerGUID,
                                           softwareServerGUIDParameterName,
                                           OpenMetadataType.SOFTWARE_SERVER.typeName,
                                           OpenMetadataType.CLOUD_TENANT_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.CLOUD_TENANT_CLASSIFICATION.typeName,
                                           builder.getCloudTenantProperties(tenantName, tenantType, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the Cloud Tenant designation from a software server.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param softwareServerGUID unique identifier of software server
     * @param softwareServerGUIDParameterName parameter name supplying softwareServerGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCloudTenantClassification(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  softwareServerGUID,
                                                 String  softwareServerGUIDParameterName,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                softwareServerGUID,
                                                softwareServerGUIDParameterName,
                                                OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                OpenMetadataType.CLOUD_TENANT_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.CLOUD_TENANT_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }



    /**
     * Mark the software capability as a Cloud Service.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param softwareCapabilityGUID unique identifier of software capability
     * @param softwareCapabilityGUIDParameterName parameter name supplying softwareCapabilityGUID
     * @param offeringName name of the service
     * @param serviceType type of service
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCloudServiceClassification(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  softwareCapabilityGUID,
                                               String  softwareCapabilityGUIDParameterName,
                                               String  offeringName,
                                               String  serviceType,
                                               Date    effectiveFrom,
                                               Date    effectiveTo,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(softwareCapabilityGUID, softwareCapabilityGUIDParameterName, methodName);

        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           softwareCapabilityGUID,
                                           softwareCapabilityGUIDParameterName,
                                           OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                           OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeName,
                                           builder.getCloudServiceProperties(offeringName, serviceType, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the Cloud Service designation from a software capability.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param softwareCapabilityGUID unique identifier of software capability
     * @param softwareCapabilityGUIDParameterName parameter name supplying softwareCapabilityGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCloudServiceClassification(String  userId,
                                                  String  externalSourceGUID,
                                                  String  externalSourceName,
                                                  String  softwareCapabilityGUID,
                                                  String  softwareCapabilityGUIDParameterName,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing,
                                                  Date    effectiveTime,
                                                  String  methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                softwareCapabilityGUID,
                                                softwareCapabilityGUIDParameterName,
                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }
}
