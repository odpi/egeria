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

import java.util.List;
import java.util.Map;


/**
 * OperatingPlatformHandler provides the exchange of metadata about operating platforms between the repository and
 * the OMAS.
 */
public class OperatingPlatformHandler<B> extends OpenMetadataAPIGenericHandler<B>
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
    public OperatingPlatformHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create a definition of a operating platform. 
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param qualifiedName unique name for the operating platform - used in other configuration
     * @param name short display name for the operating platform
     * @param description description of the operating platform
     * @param operatingSystem the operating system running on this platform
     * @param byteOrdering the identifier of the endianness
     * @param additionalProperties additional properties for a operating platform
     * @param suppliedTypeName type of operatingPlatform
     * @param extendedProperties  properties for a operating platform subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  createOperatingPlatform(String              userId,
                                         String              externalSourceGUID,
                                         String              externalSourceName,
                                         String              qualifiedName,
                                         String              name,
                                         String              description,
                                         String              operatingSystem,
                                         int                 byteOrdering,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        String typeName = OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        OperatingPlatformBuilder builder = new OperatingPlatformBuilder(qualifiedName,
                                                                        name,
                                                                        description,
                                                                        operatingSystem,
                                                                        byteOrdering,
                                                                        additionalProperties,
                                                                        typeGUID,
                                                                        suppliedTypeName,
                                                                        extendedProperties,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

        this.createBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_GUID,
                                    OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME,
                                    qualifiedName,
                                    OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                    builder,
                                    methodName);
    }


    /**
     * Update the operating platform.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param operatingPlatformGUID unique identifier of the operating platform to update
     * @param operatingPlatformGUIDParameterName parameter passing the operatingPlatformGUID
     * @param qualifiedName unique name for the operating platform - used in other configuration
     * @param name short display name for the operating platform
     * @param description description of the governance operating platform
     * @param additionalProperties additional properties for a governance operatingPlatform
     * @param extendedProperties  properties for a governance operatingPlatform subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateOperatingPlatform(String              userId,
                                          String              externalSourceGUID,
                                          String              externalSourceName,
                                          String              operatingPlatformGUID,
                                          String              operatingPlatformGUIDParameterName,
                                          String              qualifiedName,
                                          String              name,
                                          String              description,
                                          String              operatingSystem,
                                          int                 byteOrdering,
                                          Map<String, String> additionalProperties,
                                          String              suppliedTypeName,
                                          Map<String, Object> extendedProperties,
                                          String              methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(operatingPlatformGUID, operatingPlatformGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        OperatingPlatformBuilder builder = new OperatingPlatformBuilder(qualifiedName,
                                                                        name,
                                                                        description,
                                                                        operatingSystem,
                                                                        byteOrdering,
                                                                        additionalProperties,
                                                                        typeGUID,
                                                                        typeName,
                                                                        extendedProperties,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    operatingPlatformGUID,
                                    operatingPlatformGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    builder.getInstanceProperties(methodName),
                                    false,
                                    methodName);
    }


    /**
     * Create a relationship between a host and an operating platform.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostGUID unique identifier of the host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param operatingPlatformGUIDParameterName parameter supplying the operatingPlatformGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupHostOperatingPlatform(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String hostGUID,
                                           String hostGUIDParameterName,
                                           String operatingPlatformGUID,
                                           String operatingPlatformGUIDParameterName,
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
                                  operatingPlatformGUID,
                                  operatingPlatformGUIDParameterName,
                                  OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME,
                                  OpenMetadataAPIMapper.HOST_OPERATING_PLATFORM_TYPE_GUID,
                                  OpenMetadataAPIMapper.HOST_OPERATING_PLATFORM_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a relationship between a host and an operating platform..
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param hostGUID unique identifier of the host
     * @param hostGUIDParameterName parameter supplying the hostGUID
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param operatingPlatformGUIDParameterName parameter supplying the operatingPlatformGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearNestedOperatingPlatform(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String hostGUID,
                                             String hostGUIDParameterName,
                                             String operatingPlatformGUID,
                                             String operatingPlatformGUIDParameterName,
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
                                      operatingPlatformGUID,
                                      operatingPlatformGUIDParameterName,
                                      OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_GUID,
                                      OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME,
                                      OpenMetadataAPIMapper.HOST_OPERATING_PLATFORM_TYPE_GUID,
                                      OpenMetadataAPIMapper.HOST_OPERATING_PLATFORM_TYPE_NAME,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a operatingPlatform.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param operatingPlatformGUID unique identifier of the metadata element to remove
     * @param operatingPlatformGUIDParameterName parameter supplying the operatingPlatformGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeOperatingPlatform(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String operatingPlatformGUID,
                                        String operatingPlatformGUIDParameterName,
                                        String methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    operatingPlatformGUID,
                                    operatingPlatformGUIDParameterName,
                                    OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_GUID,
                                    OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME,
                                    null,
                                    null,
                                    methodName);
    }
    
    
    /**
     * Return information about a specific operating platform.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the operating platform
     * @param qualifiedNameParameter name of parameter supplying the qualifiedName
     * @param methodName calling method
     *
     * @return properties of the operating platform
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public B getOperatingPlatform(String   userId,
                                  String   qualifiedName,
                                  String   qualifiedNameParameter,
                                  String   methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        return this.getBeanByUniqueName(userId,
                                        qualifiedName,
                                        qualifiedNameParameter,
                                        OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                        OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_GUID,
                                        OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME,
                                        methodName);
    }


    /**
     * Return information about the defined operating platforms.
     *
     * @param userId calling user
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     * @param methodName calling method
     *
     * @return properties of the operating platform
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getOperatingPlatforms(String   userId,
                                         int      startingFrom,
                                         int      maximumResults,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_GUID,
                                   OpenMetadataAPIMapper.OPERATING_PLATFORM_TYPE_NAME,
                                   supportedZones,
                                   startingFrom,
                                   maximumResults,
                                   methodName);
    }
}
