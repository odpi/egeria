/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * SoftwareServerCapabilityHandler provides the support for managing software server capabilities.
 */
public class SoftwareServerCapabilityHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
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
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public SoftwareServerCapabilityHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Files live on a file system.  This method creates a top level anchor for a file system.
     * It has its own method because ot the extra properties in the FileSystem classification
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param uniqueName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param type type of file system
     * @param version version of file system
     * @param patchLevel patchLevel of software supporting the file system
     * @param source supplier of the software for this file system
     * @param format format of files on this file system
     * @param encryption encryption type - null for unencrypted
     * @param additionalProperties additional properties
     * @param vendorProperties  properties about the vendor and/or their product
     * @param methodName calling method
     *
     * @return unique identifier for the file system
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String   createFileSystem(String               userId,
                                     String               externalSourceGUID,
                                     String               externalSourceName,
                                     String               uniqueName,
                                     String               displayName,
                                     String               description,
                                     String               type,
                                     String               version,
                                     String               patchLevel,
                                     String               source,
                                     String               format,
                                     String               encryption,
                                     Map<String, String>  additionalProperties,
                                     Map<String, String>  vendorProperties,
                                     String               methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        SoftwareServerCapabilityBuilder builder = new SoftwareServerCapabilityBuilder(uniqueName,
                                                                                      displayName,
                                                                                      description,
                                                                                      type,
                                                                                      version,
                                                                                      patchLevel,
                                                                                      source,
                                                                                      additionalProperties,
                                                                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                                      OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                                      null,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        builder.setFileSystemClassification(userId, format, encryption, methodName);

        String fileSystemGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                            OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                            uniqueName,
                                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                            builder,
                                                            methodName);

        if (fileSystemGUID != null)
        {
            this.setVendorProperties(userId, fileSystemGUID, vendorProperties, methodName);
        }

        return fileSystemGUID;
    }


    /**
     * Create specialized Software Server Capabilities entities.  Most software service capabilities
     * either specialize Software Server Capability or have a special classification.  Metadata server
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param specializedTypeGUID unique identifier of the software server capability type
     * @param specializedTypeName unique name of the software server capability type
     * @param classificationName name of classification if any
     * @param uniqueName qualified name for the software server capability
     * @param displayName short display name
     * @param description description of the file system
     * @param type type of file system
     * @param version version of file system
     * @param patchLevel patchLevel of software supporting the file system
     * @param source supplier of the software for this file system
     * @param additionalProperties additional properties
     * @param vendorProperties  properties about the vendor and/or their product
     * @param methodName calling method
     *
     * @return unique identifier for the file system
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String   createSoftwareServerCapability(String               userId,
                                                   String               externalSourceGUID,
                                                   String               externalSourceName,
                                                   String               specializedTypeGUID,
                                                   String               specializedTypeName,
                                                   String               classificationName,
                                                   String               uniqueName,
                                                   String               displayName,
                                                   String               description,
                                                   String               type,
                                                   String               version,
                                                   String               patchLevel,
                                                   String               source,
                                                   Map<String, String>  additionalProperties,
                                                   Map<String, String>  vendorProperties,
                                                   String               methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String typeId = OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID;
        String typeName = OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;

        if (specializedTypeGUID != null)
        {
            typeId = specializedTypeGUID;
        }
        if (specializedTypeName != null)
        {
            typeName = specializedTypeName;
        }
        SoftwareServerCapabilityBuilder builder = new SoftwareServerCapabilityBuilder(uniqueName,
                                                                                      displayName,
                                                                                      description,
                                                                                      type,
                                                                                      version,
                                                                                      patchLevel,
                                                                                      source,
                                                                                      additionalProperties,
                                                                                      typeId,
                                                                                      typeName,
                                                                                      null,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        if (classificationName != null)
        {
            builder.setCapabilityTypeClassification(userId, classificationName, methodName);
        }

        String fileSystemGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            typeId,
                                                            typeName,
                                                            uniqueName,
                                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                            builder,
                                                            methodName);

        if (fileSystemGUID != null)
        {
            this.setVendorProperties(userId, fileSystemGUID, vendorProperties, methodName);
        }

        return fileSystemGUID;
    }


    /**
     * Retrieve a list of unique identifiers for defined software server capabilities.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of type
     * @param typeName unique name of type
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param methodName calling method
     *
     * @return List of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> getSoftwareServerCapabilityGUIDsByType(String  userId,
                                                               String  typeGUID,
                                                               String  typeName,
                                                               int     startingFrom,
                                                               int     maxPageSize,
                                                               String  methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return this.getBeanGUIDsByType(userId, typeGUID, typeName, supportedZones, startingFrom, maxPageSize, methodName);
    }


    /**
     * Retrieve a list of defined software server capabilities.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of type
     * @param typeName unique name of type
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param methodName calling method
     *
     * @return List of beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getSoftwareServerCapabilitiesByType(String  userId,
                                                       String  typeGUID,
                                                       String  typeName,
                                                       int     startingFrom,
                                                       int     maxPageSize,
                                                       String  methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return this.getBeansByType(userId, typeGUID, typeName, supportedZones, startingFrom, maxPageSize, methodName);
    }


    /**
     * Retrieve a list of defined software server capabilities.
     *
     * @param userId calling user
     * @param classificationName name of classification
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param methodName calling method
     *
     * @return List of beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getSoftwareServerCapabilitiesByClassification(String  userId,
                                                                 String  classificationName,
                                                                 int     startingFrom,
                                                                 int     maxPageSize,
                                                                 String  methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return this.getBeansByClassification(userId,
                                             OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                             classificationName,
                                             startingFrom,
                                             maxPageSize,
                                             methodName);
    }


    /**
     * Retrieve a list of unique identifiers for defined software server capabilities.
     *
     * @param userId calling user
     * @param classificationName name of the classification that identifies the type of software server capability required
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param methodName calling method
     *
     * @return List of unique identifiers for the retrieved entities
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> getSoftwareServerCapabilityGUIDsByClassification(String  userId,
                                                                         String  classificationName,
                                                                         int     startingFrom,
                                                                         int     maxPageSize,
                                                                         String  methodName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return this.getBeanGUIDsByClassification(userId,
                                                 OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                 classificationName,
                                                 startingFrom,
                                                 maxPageSize,
                                                 methodName);
    }
}
