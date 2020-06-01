/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.FileSystemBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.SoftwareServerCapabilityBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.SoftwareServerCapabilityConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.ffdc.OMAGOCFErrorCode;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SoftwareServerCapabilityMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * SoftwareServerCapabilityHandler provides the support for managing software server capabilities.
 */
public class SoftwareServerCapabilityHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName  name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public SoftwareServerCapabilityHandler(String                  serviceName,
                                           String                  serverName,
                                           InvalidParameterHandler invalidParameterHandler,
                                           RepositoryHandler       repositoryHandler,
                                           OMRSRepositoryHelper    repositoryHelper)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Files live on a file system.  This method creates a top level anchor for a file system.
     *
     * @param userId calling user
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
        FileSystemBuilder builder = new FileSystemBuilder(uniqueName,
                                                          displayName,
                                                          description,
                                                          type,
                                                          version,
                                                          patchLevel,
                                                          source,
                                                          format,
                                                          encryption,
                                                          additionalProperties,
                                                          null,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        String fileSystemGUID = repositoryHandler.createEntity(userId,
                                                               SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                               SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                               builder.getInstanceProperties(methodName),
                                                               methodName);

        if (fileSystemGUID != null)
        {
            repositoryHandler.classifyEntity(userId,
                                             fileSystemGUID,
                                             SoftwareServerCapabilityMapper.FILE_SYSTEM_CLASSIFICATION_GUID,
                                             SoftwareServerCapabilityMapper.FILE_SYSTEM_CLASSIFICATION_NAME,
                                             builder.getClassificationInstanceProperties(methodName),
                                             methodName);
        }

        return fileSystemGUID;
    }


    /**
     * Create the capability for a relational database platform.
     *
     * @param userId calling user
     * @param uniqueName qualified name for the file system
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
     * @return unique identifier for the database platform
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String   createDatabasePlatform(String               userId,
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
        SoftwareServerCapabilityBuilder builder = new SoftwareServerCapabilityBuilder(uniqueName,
                                                                                      displayName,
                                                                                      description,
                                                                                      type,
                                                                                      version,
                                                                                      patchLevel,
                                                                                      source,
                                                                                      additionalProperties,
                                                                                      null,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        String databasePlatformGUID = repositoryHandler.createEntity(userId,
                                                                     SoftwareServerCapabilityMapper.DATABASE_PLATFORM_TYPE_GUID,
                                                                     SoftwareServerCapabilityMapper.DATABASE_PLATFORM_TYPE_NAME,
                                                                     builder.getInstanceProperties(methodName),
                                                                     methodName);

        if (databasePlatformGUID != null)
        {
            this.setVendorPropertiesForCapability(userId, databasePlatformGUID, vendorProperties, methodName);
        }

        return databasePlatformGUID;
    }



    /**
     * Create the capability for a relational database platform.
     *
     * @param userId calling user
     * @param uniqueName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param type type of file system
     * @param version version of file system
     * @param patchLevel patchLevel of software supporting the file system
     * @param source supplier of the software for this file system
     * @param additionalProperties additional properties
     * @param vendorProperties  properties about the vendor and/or their product
     * @param classificationGUID unique identifier for the type of integrator
     * @param classificationName unique name for the type of integrator
     * @param methodName calling method
     *
     * @return unique identifier for the database platform
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String   createMetadataIntegrator(String               userId,
                                             String               uniqueName,
                                             String               displayName,
                                             String               description,
                                             String               type,
                                             String               version,
                                             String               patchLevel,
                                             String               source,
                                             Map<String, String>  additionalProperties,
                                             Map<String, String>  vendorProperties,
                                             String               classificationGUID,
                                             String               classificationName,
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
                                                                                      null,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        String integratorGUID = repositoryHandler.createEntity(userId,
                                                               SoftwareServerCapabilityMapper.METADATA_INTEGRATION_CAPABILITY_TYPE_GUID,
                                                               SoftwareServerCapabilityMapper.METADATA_INTEGRATION_CAPABILITY_TYPE_NAME,
                                                               builder.getInstanceProperties(methodName),
                                                               methodName);

        if (integratorGUID != null)
        {
            this.setVendorPropertiesForCapability(userId, integratorGUID, vendorProperties, methodName);

            repositoryHandler.classifyEntity(userId,
                                             integratorGUID,
                                             classificationGUID,
                                             classificationName,
                                             null,
                                             methodName);
        }

        return integratorGUID;
    }



    /**
     * Retrieve a software server capability by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param softwareServerCapabilityGUID unique identifier used to locate the element
     * @param methodName calling method
     *
     * @return retrieved properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public SoftwareServerCapability getSoftwareServerCapabilityByGUID(String   userId,
                                                                      String   softwareServerCapabilityGUID,
                                                                      String   methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String  guidName = "softwareServerCapabilityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(softwareServerCapabilityGUID, guidName, methodName);

        EntityDetail  entity = repositoryHandler.getEntityByGUID(userId,
                                                                 softwareServerCapabilityGUID,
                                                                 guidName,
                                                                 SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                 methodName);

        if (entity != null)
        {
            SoftwareServerCapabilityConverter converter = new SoftwareServerCapabilityConverter(entity,
                                                                                                repositoryHelper,
                                                                                                serviceName);

            return converter.getBean();
        }

        return null;
    }


    /**
     * Retrieve a software server capability by its unique name.
     *
     * @param userId calling user
     * @param uniqueName unique name for the file system
     * @param methodName calling method
     *
     * @return  properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public SoftwareServerCapability getSoftwareServerCapabilityByUniqueName(String   userId,
                                                                            String   uniqueName,
                                                                            String   methodName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String  nameName = "uniqueName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameName, methodName);

        FileSystemBuilder builder = new FileSystemBuilder(uniqueName,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        EntityDetail  entity = repositoryHandler.getUniqueEntityByName(userId,
                                                                       uniqueName,
                                                                       nameName,
                                                                       builder.getQualifiedNameInstanceProperties(methodName),
                                                                       SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                       SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                       methodName);

        if (entity != null)
        {
            SoftwareServerCapabilityConverter converter = new SoftwareServerCapabilityConverter(entity,
                                                                                                repositoryHelper,
                                                                                                serviceName);

            return converter.getBean();
        }

        return null;
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
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maxPageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForType(userId,
                                                                           typeGUID,
                                                                           typeName,
                                                                           startingFrom,
                                                                           queryPageSize,
                                                                           methodName);

        if (entities != null)
        {
            List<String>  guids = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    guids.add(entity.getGUID());
                }
            }

            if (! guids.isEmpty())
            {
                return guids;
            }
        }

        return null;
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
    public List<SoftwareServerCapability> getSoftwareServerCapabilitiesByType(String  userId,
                                                                              String  typeGUID,
                                                                              String  typeName,
                                                                              int     startingFrom,
                                                                              int     maxPageSize,
                                                                              String  methodName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maxPageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForType(userId,
                                                                           typeGUID,
                                                                           typeName,
                                                                           startingFrom,
                                                                           queryPageSize,
                                                                           methodName);

        if (entities != null)
        {
            List<SoftwareServerCapability>  softwareServerCapabilities = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    SoftwareServerCapabilityConverter converter = new SoftwareServerCapabilityConverter(entity,
                                                                                                        repositoryHelper,
                                                                                                        serviceName);
                    softwareServerCapabilities.add(converter.getBean());                }
            }

            if (! softwareServerCapabilities.isEmpty())
            {
                return softwareServerCapabilities;
            }
        }

        return null;
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
    public List<SoftwareServerCapability> getSoftwareServerCapabilitiesByClassification(String  userId,
                                                                                        String  classificationName,
                                                                                        int     startingFrom,
                                                                                        int     maxPageSize,
                                                                                        String  methodName) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maxPageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                                         classificationName,
                                                                                         startingFrom,
                                                                                         queryPageSize,
                                                                                         methodName);

        if (entities != null)
        {
            List<SoftwareServerCapability>  softwareServerCapabilities = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    SoftwareServerCapabilityConverter converter = new SoftwareServerCapabilityConverter(entity,
                                                                                                        repositoryHelper,
                                                                                                        serviceName);
                    softwareServerCapabilities.add(converter.getBean());
                }
            }

            if (! softwareServerCapabilities.isEmpty())
            {
                return softwareServerCapabilities;
            }
        }

        return null;
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
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maxPageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                                         classificationName,
                                                                                         startingFrom,
                                                                                         queryPageSize,
                                                                                         methodName);

        if (entities != null)
        {
            List<String>  guids = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    guids.add(entity.getGUID());
                }
            }

            if (! guids.isEmpty())
            {
                return guids;
            }
        }

        return null;
    }


    /**
     * Verify that the integrator identities are either null or refer to a valid software server capability.
     * These values will be used to set up the
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     *
     * @throws InvalidParameterException the integrator GUID or name does not match what is in the metadata repository
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void verifyIntegratorIdentity(String userId,
                                         String integratorGUID,
                                         String integratorName,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        if ((integratorGUID == null) && (integratorName == null))
        {
            return;
        }

        try
        {
            SoftwareServerCapability integrator = this.getSoftwareServerCapabilityByGUID(userId, integratorGUID, methodName);

            if (integrator == null)
            {
                throw new InvalidParameterException(OMAGOCFErrorCode.INTEGRATOR_NOT_RETURNED.getMessageDefinition(serviceName,
                                                                                                                  methodName,
                                                                                                                  integratorGUID,
                                                                                                                  integratorName,
                                                                                                                  null),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    integratorGUID);
            }
            else if (! integratorName.equals(integrator.getQualifiedName()))
            {
                throw new InvalidParameterException(OMAGOCFErrorCode.BAD_INTEGRATOR_NAME.getMessageDefinition(serviceName,
                                                                                                              methodName,
                                                                                                              integratorName,
                                                                                                              integrator.getQualifiedName(),
                                                                                                              integratorGUID),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    integratorGUID);
            }
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(OMAGOCFErrorCode.INTEGRATOR_NOT_RETURNED.getMessageDefinition(serviceName,
                                                                                                              methodName,
                                                                                                              integratorGUID,
                                                                                                              integratorName,
                                                                                                              error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                integratorGUID);
        }

    }


    /**
     * Create the property facet for the vendor properties.
     *
     * @param userId calling user
     * @param capabilityGUID unique identifier of the software server capability
     * @param vendorProperties properties for the vendor
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void setVendorPropertiesForCapability(String               userId,
                                          String               capabilityGUID,
                                          Map<String, String>  vendorProperties,
                                          String               methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        // todo
    }
}
