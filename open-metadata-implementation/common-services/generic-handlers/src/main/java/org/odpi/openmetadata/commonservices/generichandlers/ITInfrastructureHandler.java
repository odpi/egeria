/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostClusterGUID unique identifier of the host cluster
     * @param hostClusterGUIDParameterName parameter supplying the hostClusterGUID
     * @param hostGUID unique identifier of the member host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupHostClusterMember(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String hostClusterGUID,
                                       String hostClusterGUIDParameterName,
                                       String hostGUID,
                                       String hostGUIDParameterName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  hostClusterGUID,
                                  hostClusterGUIDParameterName,
                                  OpenMetadataAPIMapper.HOST_CLUSTER_TYPE_NAME,
                                  hostGUID,
                                  hostGUIDParameterName,
                                  OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                  OpenMetadataAPIMapper.HOST_CLUSTER_MEMBER_TYPE_GUID,
                                  OpenMetadataAPIMapper.HOST_CLUSTER_MEMBER_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a relationship between a host cluster and a host member.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostClusterGUID unique identifier of the host cluster
     * @param hostClusterGUIDParameterName parameter supplying the hostClusterGUID
     * @param hostGUID unique identifier of the member host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearHostClusterMember(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String hostClusterGUID,
                                       String hostClusterGUIDParameterName,
                                       String hostGUID,
                                       String hostGUIDParameterName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      hostClusterGUID,
                                      hostClusterGUIDParameterName,
                                      OpenMetadataAPIMapper.HOST_CLUSTER_TYPE_NAME,
                                      hostGUID,
                                      hostGUIDParameterName,
                                      OpenMetadataAPIMapper.HOST_TYPE_GUID,
                                      OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                      OpenMetadataAPIMapper.HOST_CLUSTER_MEMBER_TYPE_GUID,
                                      OpenMetadataAPIMapper.HOST_CLUSTER_MEMBER_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a host cluster and a host.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostGUID unique identifier of the host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param virtualContainerGUID unique identifier of the virtual container deployed on the host
     * @param virtualContainerGUIDParameterName parameter supplying the virtualContainerGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupHostDeployedVirtualContainer(String userId,
                                                  String externalSourceGUID,
                                                  String externalSourceName,
                                                  String hostGUID,
                                                  String hostGUIDParameterName,
                                                  String virtualContainerGUID,
                                                  String virtualContainerGUIDParameterName,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  hostGUID,
                                  hostGUIDParameterName,
                                  OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                  virtualContainerGUID,
                                  virtualContainerGUIDParameterName,
                                  OpenMetadataAPIMapper.VIRTUAL_CONTAINER_TYPE_NAME,
                                  OpenMetadataAPIMapper.DEPLOYED_VIRTUAL_CONTAINER_TYPE_GUID,
                                  OpenMetadataAPIMapper.DEPLOYED_VIRTUAL_CONTAINER_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a relationship between a host cluster and a host member.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param virtualContainerGUID unique identifier of the host
     * @param virtualContainerGUIDParameterName parameter supplying the virtualContainerGUID
     * @param virtualContainerGUID unique identifier of the virtual container deployed on the host
     * @param virtualContainerGUIDParameterName parameter supplying the virtualContainerGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearHostDeployedVirtualContainer(String userId,
                                                  String externalSourceGUID,
                                                  String externalSourceName,
                                                  String hostGUID,
                                                  String hostGUIDParameterName,
                                                  String virtualContainerGUID,
                                                  String virtualContainerGUIDParameterName,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      hostGUID,
                                      hostGUIDParameterName,
                                      OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                      virtualContainerGUID,
                                      virtualContainerGUIDParameterName,
                                      OpenMetadataAPIMapper.VIRTUAL_CONTAINER_TYPE_GUID,
                                      OpenMetadataAPIMapper.VIRTUAL_CONTAINER_TYPE_NAME,
                                      OpenMetadataAPIMapper.DEPLOYED_VIRTUAL_CONTAINER_TYPE_GUID,
                                      OpenMetadataAPIMapper.DEPLOYED_VIRTUAL_CONTAINER_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a a host and a software server platform.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostGUID unique identifier of the host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param softwareServerPlatformGUIDParameterName parameter supplying the softwareServerPlatformGUID
     * @param deploymentTime date/time that the platform was deployed
     * @param deployer user who issued the deploy command
     * @param platformStatus operational status of the platform
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSoftwareServerPlatformDeployment(String userId,
                                                       String externalSourceGUID,
                                                       String externalSourceName,
                                                       String hostGUID,
                                                       String hostGUIDParameterName,
                                                       String softwareServerPlatformGUID,
                                                       String softwareServerPlatformGUIDParameterName,
                                                       Date   deploymentTime,
                                                       String deployer,
                                                       int    platformStatus,
                                                       String methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  hostGUID,
                                  hostGUIDParameterName,
                                  OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                  softwareServerPlatformGUID,
                                  softwareServerPlatformGUIDParameterName,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_TYPE_NAME,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_TYPE_GUID,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_TYPE_NAME,
                                  builder.getSoftwareServerPlatformDeploymentProperties(deploymentTime,
                                                                                        deployer,
                                                                                        platformStatus,
                                                                                        methodName),
                                  methodName);
    }


    /**
     * Remove a relationship between a host and a software server platform.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostGUID unique identifier of the host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param softwareServerPlatformGUIDParameterName parameter supplying the softwareServerPlatformGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSoftwareServerPlatformDeployment(String userId,
                                                      String externalSourceGUID,
                                                      String externalSourceName,
                                                      String hostGUID,
                                                      String hostGUIDParameterName,
                                                      String softwareServerPlatformGUID,
                                                      String softwareServerPlatformGUIDParameterName,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      hostGUID,
                                      hostGUIDParameterName,
                                      OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                      softwareServerPlatformGUID,
                                      softwareServerPlatformGUIDParameterName,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_TYPE_GUID,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_TYPE_NAME,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_TYPE_GUID,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a software server platform and a software server.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param softwareServerPlatformGUIDParameterName parameter supplying the softwareServerPlatformGUID
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param deploymentTime date/time that the capability was deployed
     * @param deployer user who issued the deploy command
     * @param serverStatus operational status of the server
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSoftwareServerDeployment(String userId,
                                              String externalSourceGUID,
                                              String externalSourceName,
                                              String softwareServerPlatformGUID,
                                              String softwareServerPlatformGUIDParameterName,
                                              String softwareServerGUID,
                                              String softwareServerGUIDParameterName,
                                              Date   deploymentTime,
                                              String deployer,
                                              int    serverStatus,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  softwareServerPlatformGUID,
                                  softwareServerPlatformGUIDParameterName,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_TYPE_NAME,
                                  softwareServerGUID,
                                  softwareServerGUIDParameterName,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_NAME,
                                  OpenMetadataAPIMapper.SERVER_DEPLOYMENT_TYPE_GUID,
                                  OpenMetadataAPIMapper.SERVER_DEPLOYMENT_TYPE_NAME,
                                  builder.getSoftwareServerSupportedCapabilitiesProperties(deploymentTime,
                                                                                           deployer,
                                                                                           serverStatus,
                                                                                           methodName),
                                  methodName);
    }


    /**
     * Remove a relationship between a software server platform and a software server.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param softwareServerPlatformGUIDParameterName parameter supplying the softwareServerPlatformGUID
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSoftwareServerDeployment(String userId,
                                              String externalSourceGUID,
                                              String externalSourceName,
                                              String softwareServerPlatformGUID,
                                              String softwareServerPlatformGUIDParameterName,
                                              String softwareServerGUID,
                                              String softwareServerGUIDParameterName,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareServerPlatformGUID,
                                      softwareServerPlatformGUIDParameterName,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_TYPE_NAME,
                                      softwareServerGUID,
                                      softwareServerGUIDParameterName,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_GUID,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_NAME,
                                      OpenMetadataAPIMapper.SERVER_DEPLOYMENT_TYPE_GUID,
                                      OpenMetadataAPIMapper.SERVER_DEPLOYMENT_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a software server and one of its endpoints.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param endpointGUID unique identifier of the endpoint
     * @param endpointGUIDParameterName parameter supplying the endpointGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSoftwareServerEndpoint(String userId,
                                            String externalSourceGUID,
                                            String externalSourceName,
                                            String softwareServerGUID,
                                            String softwareServerGUIDParameterName,
                                            String endpointGUID,
                                            String endpointGUIDParameterName,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  softwareServerGUID,
                                  softwareServerGUIDParameterName,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_NAME,
                                  endpointGUID,
                                  endpointGUIDParameterName,
                                  OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                  OpenMetadataAPIMapper.SERVER_ENDPOINT_TYPE_GUID,
                                  OpenMetadataAPIMapper.SERVER_ENDPOINT_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a relationship between a software server and one of its endpoints.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param endpointGUID unique identifier of the endpoint
     * @param endpointGUIDParameterName parameter supplying the endpointGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSoftwareServerEndpoint(String userId,
                                            String externalSourceGUID,
                                            String externalSourceName,
                                            String softwareServerGUID,
                                            String softwareServerGUIDParameterName,
                                            String endpointGUID,
                                            String endpointGUIDParameterName,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareServerGUID,
                                      softwareServerGUIDParameterName,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_NAME,
                                      endpointGUID,
                                      endpointGUIDParameterName,
                                      OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                      OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                      OpenMetadataAPIMapper.SERVER_ENDPOINT_TYPE_GUID,
                                      OpenMetadataAPIMapper.SERVER_ENDPOINT_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a software server and a software server capability.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param softwareServerCapabilityGUID unique identifier of the software server capability
     * @param softwareServerCapabilityGUIDParameterName parameter supplying the softwareServerCapabilityGUID
     * @param deploymentTime date/time that the capability was deployed
     * @param deployer user who issued the deploy command
     * @param serverCapabilityStatus operational status of the capability
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSoftwareServerSupportedCapability(String userId,
                                                       String externalSourceGUID,
                                                       String externalSourceName,
                                                       String softwareServerGUID,
                                                       String softwareServerGUIDParameterName,
                                                       String softwareServerCapabilityGUID,
                                                       String softwareServerCapabilityGUIDParameterName,
                                                       Date   deploymentTime,
                                                       String deployer,
                                                       int    serverCapabilityStatus,
                                                       String methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  softwareServerGUID,
                                  softwareServerGUIDParameterName,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_NAME,
                                  softwareServerCapabilityGUID,
                                  softwareServerCapabilityGUIDParameterName,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                  OpenMetadataAPIMapper.SUPPORTED_CAPABILITY_TYPE_GUID,
                                  OpenMetadataAPIMapper.SUPPORTED_CAPABILITY_TYPE_NAME,
                                  builder.getSoftwareServerSupportedCapabilitiesProperties(deploymentTime,
                                                                                           deployer,
                                                                                           serverCapabilityStatus,
                                                                                           methodName),
                                  methodName);
    }


    /**
     * Remove a relationship between a software server and a software server capability.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param softwareServerGUID unique identifier of the software server
     * @param softwareServerGUIDParameterName parameter supplying the softwareServerGUID
     * @param softwareServerCapabilityGUID unique identifier of the software server capability
     * @param softwareServerCapabilityGUIDParameterName parameter supplying the softwareServerCapabilityGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSoftwareServerSupportedCapability(String userId,
                                                       String externalSourceGUID,
                                                       String externalSourceName,
                                                       String softwareServerGUID,
                                                       String softwareServerGUIDParameterName,
                                                       String softwareServerCapabilityGUID,
                                                       String softwareServerCapabilityGUIDParameterName,
                                                       String methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareServerGUID,
                                      softwareServerGUIDParameterName,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_NAME,
                                      softwareServerCapabilityGUID,
                                      softwareServerCapabilityGUIDParameterName,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                      OpenMetadataAPIMapper.SUPPORTED_CAPABILITY_TYPE_GUID,
                                      OpenMetadataAPIMapper.SUPPORTED_CAPABILITY_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a software server capability and an asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param softwareServerCapabilityGUID unique identifier of the software server capability
     * @param softwareServerCapabilityGUIDParameterName parameter supplying the softwareServerCapabilityGUID
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter supplying the assetGUID
     * @param description description of the use
     * @param useType server asset use type
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSoftwareServerCapabilityAssetUse(String userId,
                                                      String externalSourceGUID,
                                                      String externalSourceName,
                                                      String softwareServerCapabilityGUID,
                                                      String softwareServerCapabilityGUIDParameterName,
                                                      String assetGUID,
                                                      String assetGUIDParameterName,
                                                      String description,
                                                      int    useType,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  softwareServerCapabilityGUID,
                                  softwareServerCapabilityGUIDParameterName,
                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                  OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                  builder.getSoftwareServerCapabilitiesAssetUseProperties(description, useType, methodName),
                                  methodName);
    }


    /**
     * Remove a relationship between a software server and a software server capability.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID unique identifier of the software server capability
     * @param assetGUIDParameterName parameter supplying the assetGUID
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter supplying the assetGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSoftwareServerCapabilityAssetUse(String userId,
                                                      String externalSourceGUID,
                                                      String externalSourceName,
                                                      String softwareServerCapabilityGUID,
                                                      String softwareServerCapabilityGUIDParameterName,
                                                      String assetGUID,
                                                      String assetGUIDParameterName,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareServerCapabilityGUID,
                                      softwareServerCapabilityGUIDParameterName,
                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                      assetGUID,
                                      assetGUIDParameterName,
                                      OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                      OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                      OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a host and a network.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostGUID unique identifier of the host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param networkGUID unique identifier of the network
     * @param networkGUIDParameterName parameter supplying the networkGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupHostNetwork(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String hostGUID,
                                 String hostGUIDParameterName,
                                 String networkGUID,
                                 String networkGUIDParameterName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  hostGUID,
                                  hostGUIDParameterName,
                                  OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                  networkGUID,
                                  networkGUIDParameterName,
                                  OpenMetadataAPIMapper.NETWORK_TYPE_NAME,
                                  OpenMetadataAPIMapper.HOST_NETWORK_TYPE_GUID,
                                  OpenMetadataAPIMapper.HOST_NETWORK_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a relationship between a a host and a network.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostGUID unique identifier of the host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param networkGUID unique identifier of the network
     * @param networkGUIDParameterName parameter supplying the networkGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearHostNetwork(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String hostGUID,
                                 String hostGUIDParameterName,
                                 String networkGUID,
                                 String networkGUIDParameterName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      hostGUID,
                                      hostGUIDParameterName,
                                      OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                      networkGUID,
                                      networkGUIDParameterName,
                                      OpenMetadataAPIMapper.NETWORK_TYPE_GUID,
                                      OpenMetadataAPIMapper.NETWORK_TYPE_NAME,
                                      OpenMetadataAPIMapper.HOST_NETWORK_TYPE_GUID,
                                      OpenMetadataAPIMapper.HOST_NETWORK_TYPE_NAME,
                                      methodName);
    }


    /**
     * Create a relationship between a network gateway and the network it connects to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param networkGatewayGUID unique identifier of the network gateway
     * @param networkGatewayGUIDParameterName parameter supplying the networkGatewayGUID
     * @param networkGUID unique identifier of the network
     * @param networkGUIDParameterName parameter supplying the networkGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupNetworkGatewayLink(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String networkGatewayGUID,
                                        String networkGatewayGUIDParameterName,
                                        String networkGUID,
                                        String networkGUIDParameterName,
                                        String methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  networkGatewayGUID,
                                  networkGatewayGUIDParameterName,
                                  OpenMetadataAPIMapper.NETWORK_GATEWAY_TYPE_NAME,
                                  networkGUID,
                                  networkGUIDParameterName,
                                  OpenMetadataAPIMapper.NETWORK_TYPE_NAME,
                                  OpenMetadataAPIMapper.NETWORK_GATEWAY_LINK_TYPE_GUID,
                                  OpenMetadataAPIMapper.NETWORK_GATEWAY_LINK_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a relationship between a network gateway and the network it connects to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param networkGatewayGUID unique identifier of the network gateway
     * @param networkGatewayGUIDParameterName parameter supplying the networkGatewayGUID
     * @param networkGUID unique identifier of the network
     * @param networkGUIDParameterName parameter supplying the networkGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearNetworkGatewayLink(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String networkGatewayGUID,
                                        String networkGatewayGUIDParameterName,
                                        String networkGUID,
                                        String networkGUIDParameterName,
                                        String methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      networkGatewayGUID,
                                      networkGatewayGUIDParameterName,
                                      OpenMetadataAPIMapper.NETWORK_GATEWAY_TYPE_NAME,
                                      networkGUID,
                                      networkGUIDParameterName,
                                      OpenMetadataAPIMapper.NETWORK_TYPE_GUID,
                                      OpenMetadataAPIMapper.NETWORK_TYPE_NAME,
                                      OpenMetadataAPIMapper.NETWORK_GATEWAY_LINK_TYPE_GUID,
                                      OpenMetadataAPIMapper.NETWORK_GATEWAY_LINK_TYPE_NAME,
                                      methodName);
    }



    /**
     * Mark the host as a Cloud Provider.
     *
     * @param userId calling user
     * @param hostGUID unique identifier of host
     * @param hostGUIDParameterName parameter name supplying hostGUID
     * @param providerName name of the cloud provider
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCloudProviderClassification(String userId,
                                                String hostGUID,
                                                String hostGUIDParameterName,
                                                String providerName,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, hostGUIDParameterName, methodName);

        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           hostGUID,
                                           hostGUIDParameterName,
                                           OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                           OpenMetadataAPIMapper.CLOUD_PROVIDER_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.CLOUD_PROVIDER_CLASSIFICATION_NAME,
                                           builder.getCloudProviderProperties(providerName, methodName),
                                           methodName);
    }


    /**
     * Remove the CloudProvider designation from a host.
     *
     * @param userId calling user
     * @param hostGUID unique identifier of host
     * @param hostGUIDParameterName parameter name supplying hostGUID
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCloudProviderClassification(String userId,
                                                   String hostGUID,
                                                   String hostGUIDParameterName,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                hostGUID,
                                                hostGUIDParameterName,
                                                OpenMetadataAPIMapper.HOST_TYPE_NAME,
                                                OpenMetadataAPIMapper.CLOUD_PROVIDER_CLASSIFICATION_GUID,
                                                OpenMetadataAPIMapper.CLOUD_PROVIDER_CLASSIFICATION_NAME,
                                                methodName);
    }



    /**
     * Mark the software server platform as a Cloud Platform.
     *
     * @param userId calling user
     * @param softwareServerPlatformGUID unique identifier of software server platform
     * @param softwareServerPlatformGUIDParameterName parameter name supplying softwareServerPlatformGUID
     * @param implementationType type of cloud platform implementation
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCloudPlatformClassification(String userId,
                                                String softwareServerPlatformGUID,
                                                String softwareServerPlatformGUIDParameterName,
                                                String implementationType,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(softwareServerPlatformGUID, softwareServerPlatformGUIDParameterName, methodName);

        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           softwareServerPlatformGUID,
                                           softwareServerPlatformGUIDParameterName,
                                           OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_TYPE_NAME,
                                           OpenMetadataAPIMapper.CLOUD_PLATFORM_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.CLOUD_PLATFORM_CLASSIFICATION_NAME,
                                           builder.getCloudPlatformProperties(implementationType, methodName),
                                           methodName);
    }


    /**
     * Remove the Cloud Platform designation from a software server platform.
     *
     * @param userId calling user
     * @param softwareServerPlatformGUID unique identifier of software server platform
     * @param softwareServerPlatformParameterName parameter name supplying softwareServerPlatformGUID
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCloudPlatformClassification(String userId,
                                                   String softwareServerPlatformGUID,
                                                   String softwareServerPlatformParameterName,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                softwareServerPlatformGUID,
                                                softwareServerPlatformParameterName,
                                                OpenMetadataAPIMapper.SOFTWARE_SERVER_PLATFORM_TYPE_NAME,
                                                OpenMetadataAPIMapper.CLOUD_PLATFORM_CLASSIFICATION_GUID,
                                                OpenMetadataAPIMapper.CLOUD_PLATFORM_CLASSIFICATION_NAME,
                                                methodName);
    }



    /**
     * Mark the software server as a Cloud Tenant.
     *
     * @param userId calling user
     * @param softwareServerGUID unique identifier of software server
     * @param softwareServerGUIDParameterName parameter name supplying softwareServerGUID
     * @param tenantName name of the tenant
     * @param tenantType type of tenant
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCloudTenantClassification(String userId,
                                              String softwareServerGUID,
                                              String softwareServerGUIDParameterName,
                                              String tenantName,
                                              String tenantType,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(softwareServerGUID, softwareServerGUIDParameterName, methodName);

        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           softwareServerGUID,
                                           softwareServerGUIDParameterName,
                                           OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_NAME,
                                           OpenMetadataAPIMapper.CLOUD_TENANT_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.CLOUD_TENANT_CLASSIFICATION_NAME,
                                           builder.getCloudTenantProperties(tenantName, tenantType, methodName),
                                           methodName);
    }


    /**
     * Remove the Cloud Tenant designation from a software server.
     *
     * @param userId calling user
     * @param softwareServerGUID unique identifier of software server
     * @param softwareServerGUIDParameterName parameter name supplying softwareServerGUID
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCloudTenantClassification(String userId,
                                                 String softwareServerGUID,
                                                 String softwareServerGUIDParameterName,
                                                 String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                softwareServerGUID,
                                                softwareServerGUIDParameterName,
                                                OpenMetadataAPIMapper.SOFTWARE_SERVER_TYPE_NAME,
                                                OpenMetadataAPIMapper.CLOUD_TENANT_CLASSIFICATION_GUID,
                                                OpenMetadataAPIMapper.CLOUD_TENANT_CLASSIFICATION_NAME,
                                                methodName);
    }



    /**
     * Mark the software server capability as a Cloud Service.
     *
     * @param userId calling user
     * @param softwareServerCapabilityGUID unique identifier of software server capability
     * @param softwareServerCapabilityGUIDParameterName parameter name supplying softwareServerCapabilityGUID
     * @param offeringName name of the service
     * @param serviceType type of service
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCloudServiceClassification(String userId,
                                                String softwareServerCapabilityGUID,
                                                String softwareServerCapabilityGUIDParameterName,
                                                String offeringName,
                                                String serviceType,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(softwareServerCapabilityGUID, softwareServerCapabilityGUIDParameterName, methodName);

        ITInfrastructureBuilder builder = new ITInfrastructureBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           softwareServerCapabilityGUID,
                                           softwareServerCapabilityGUIDParameterName,
                                           OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                           OpenMetadataAPIMapper.CLOUD_SERVICE_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.CLOUD_SERVICE_CLASSIFICATION_NAME,
                                           builder.getCloudServiceProperties(offeringName, serviceType, methodName),
                                           methodName);
    }


    /**
     * Remove the Cloud Service designation from a software server capability.
     *
     * @param userId calling user
     * @param softwareServerCapabilityGUID unique identifier of software server capability
     * @param softwareServerCapabilityGUIDParameterName parameter name supplying softwareServerCapabilityGUID
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCloudServiceClassification(String userId,
                                                  String softwareServerCapabilityGUID,
                                                  String softwareServerCapabilityGUIDParameterName,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                softwareServerCapabilityGUID,
                                                softwareServerCapabilityGUIDParameterName,
                                                OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                OpenMetadataAPIMapper.CLOUD_SERVICE_CLASSIFICATION_GUID,
                                                OpenMetadataAPIMapper.CLOUD_SERVICE_CLASSIFICATION_NAME,
                                                methodName);
    }
}
