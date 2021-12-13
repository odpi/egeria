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

import java.util.Date;
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
     * @param effectiveFrom starting time for this element (null for all time)
     * @param effectiveTo ending time for this element (null for all time)
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
                                     Date                 effectiveFrom,
                                     Date                 effectiveTo,
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
                                                                                      OpenMetadataAPIMapper.DATA_MANAGER_TYPE_GUID,
                                                                                      OpenMetadataAPIMapper.DATA_MANAGER_TYPE_NAME,
                                                                                      null,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        builder.setFileSystemClassification(userId, format, encryption, methodName);

        String fileSystemGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            OpenMetadataAPIMapper.DATA_MANAGER_TYPE_GUID,
                                                            OpenMetadataAPIMapper.DATA_MANAGER_TYPE_NAME,
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
     * either specialize Software Server Capability or have a special classification.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
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
     * @param extendedProperties properties defined for the subtype
     * @param vendorProperties  properties about the vendor and/or their product
     * @param effectiveFrom when is this element effective from
     * @param effectiveTo when is this element effect to
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
                                                   Map<String, Object>  extendedProperties,
                                                   Map<String, String>  vendorProperties,
                                                   Date                 effectiveFrom,
                                                   Date                 effectiveTo,
                                                   String               methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String typeGUID = OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID;
        String typeName = OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;

        if (specializedTypeName != null)
        {
            typeName = specializedTypeName;
            typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                              OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                              serviceName,
                                                              methodName,
                                                              repositoryHelper);
        }

        SoftwareServerCapabilityBuilder builder = new SoftwareServerCapabilityBuilder(uniqueName,
                                                                                      displayName,
                                                                                      description,
                                                                                      type,
                                                                                      version,
                                                                                      patchLevel,
                                                                                      source,
                                                                                      additionalProperties,
                                                                                      typeGUID,
                                                                                      typeName,
                                                                                      extendedProperties,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        if (classificationName != null)
        {
            builder.setCapabilityTypeClassification(userId, classificationName, methodName);
        }

        String capabilityGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            typeGUID,
                                                            typeName,
                                                            uniqueName,
                                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                            builder,
                                                            methodName);

        if (capabilityGUID != null)
        {
            this.setVendorProperties(userId, capabilityGUID, vendorProperties, methodName);
        }

        return capabilityGUID;
    }



    /**
     * Create specialized Software Server Capabilities entities.  Most software service capabilities
     * either specialize Software Server Capability or have a special classification.
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
    @SuppressWarnings(value = "unused")
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
        return this.createSoftwareServerCapability(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   specializedTypeName,
                                                   classificationName,
                                                   uniqueName,
                                                   displayName,
                                                   description,
                                                   type,
                                                   version,
                                                   patchLevel,
                                                   source,
                                                   additionalProperties,
                                                   null,
                                                   vendorProperties,
                                                   null,
                                                   null,
                                                   methodName);
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateGUIDParameterName parameter name for templateGUID
     * @param qualifiedName unique name for the element - used in other configuration
     * @param qualifiedNameParameterName parameter name for qualifiedName
     * @param displayName short display name for the new element
     * @param description description of the new element
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareServerCapabilityFromTemplate(String userId,
                                                             String externalSourceGUID,
                                                             String externalSourceName,
                                                             String templateGUID,
                                                             String templateGUIDParameterName,
                                                             String qualifiedName,
                                                             String qualifiedNameParameterName,
                                                             String displayName,
                                                             String description,
                                                             String methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        SoftwareServerCapabilityBuilder builder = new SoftwareServerCapabilityBuilder(qualifiedName,
                                                                                      displayName,
                                                                                      description,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                           OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }


    /**
     * Create specialized Software Server Capabilities entities.  Most software service capabilities
     * either specialize Software Server Capability or have a special classification.  Metadata server
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param softwareServerCapabilityGUID unique identifier for the software server capability that is to be classified
     * @param softwareServerCapabilityGUIDParameterName parameter supplying softwareServerCapabilityGUID
     * @param classificationName name of classification if any
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addSoftwareServerCapabilityClassification(String userId,
                                                          String externalSourceGUID,
                                                          String externalSourceName,
                                                          String softwareServerCapabilityGUID,
                                                          String softwareServerCapabilityGUIDParameterName,
                                                          String classificationName,
                                                          Date   effectiveFrom,
                                                          Date   effectiveTo,
                                                          String methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String classificationParameterName  = "classificationName";

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName, null, serviceName, methodName, repositoryHelper);

        invalidParameterHandler.validateGUID(classificationName, classificationParameterName, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           softwareServerCapabilityGUID,
                                           softwareServerCapabilityGUIDParameterName,
                                           OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                           classificationTypeGUID,
                                           classificationName,
                                           this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                           false,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);

    }


    /**
     * Update a software server capability entity - the classification is not updated - assumed not to change as the classification is like its type.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param capabilityGUID         unique identifier for the capability
     * @param capabilityGUIDParameterName parameter providing capabilityGUID
     * @param uniqueName qualified name for the software server capability
     * @param displayName short display name
     * @param description description of the file system
     * @param type type of file system
     * @param version version of file system
     * @param patchLevel patchLevel of software supporting the file system
     * @param source supplier of the software for this file system
     * @param additionalProperties additional properties
     * @param extendedProperties properties defined for the subtype
     * @param vendorProperties  properties about the vendor and/or their product
     * @param isMergeUpdate should the properties be merged with existing properties or replace the existing properties?
     * @param effectiveFrom when is this element effective from
     * @param effectiveTo when is this element effect to
     * @param methodName calling method
     *
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  updateSoftwareServerCapability(String               userId,
                                                String               externalSourceGUID,
                                                String               externalSourceName,
                                                String               capabilityGUID,
                                                String               capabilityGUIDParameterName,
                                                String               uniqueName,
                                                String               displayName,
                                                String               description,
                                                String               type,
                                                String               version,
                                                String               patchLevel,
                                                String               source,
                                                Map<String, String>  additionalProperties,
                                                Map<String, Object>  extendedProperties,
                                                Map<String, String>  vendorProperties,
                                                boolean              isMergeUpdate,
                                                Date                 effectiveFrom,
                                                Date                 effectiveTo,
                                                String               methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        String typeGUID = OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID;
        String typeName = OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;

        SoftwareServerCapabilityBuilder builder = new SoftwareServerCapabilityBuilder(uniqueName,
                                                                                      displayName,
                                                                                      description,
                                                                                      type,
                                                                                      version,
                                                                                      patchLevel,
                                                                                      source,
                                                                                      additionalProperties,
                                                                                      typeGUID,
                                                                                      typeName,
                                                                                      extendedProperties,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    capabilityGUID,
                                    capabilityGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    false,
                                    false,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    this.getEffectiveTime(effectiveFrom, effectiveTo),
                                    methodName);

        this.setVendorProperties(userId, capabilityGUID, vendorProperties, methodName);
    }



    /**
     * Retrieve a list of unique identifiers for defined software server capabilities.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of type
     * @param typeName unique name of type
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return List of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> getSoftwareServerCapabilityGUIDsByType(String userId,
                                                               String typeGUID,
                                                               String typeName,
                                                               int    startingFrom,
                                                               int    maxPageSize,
                                                               Date   effectiveTime,
                                                               String methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return this.getBeanGUIDsByType(userId,
                                       typeGUID,
                                       typeName,
                                       null,
                                       false,
                                       false,
                                       supportedZones,
                                       startingFrom,
                                       maxPageSize,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Retrieve a list of defined software server capabilities.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of type
     * @param typeName unique name of type
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return List of beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getSoftwareServerCapabilitiesByType(String userId,
                                                       String typeGUID,
                                                       String typeName,
                                                       int    startingFrom,
                                                       int    maxPageSize,
                                                       Date   effectiveTime,
                                                       String methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return this.getBeansByType(userId,
                                   typeGUID,
                                   typeName,
                                   null,
                                   false,
                                   false,
                                   supportedZones,
                                   startingFrom,
                                   maxPageSize,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Retrieve a list of defined software server capabilities.
     *
     * @param userId calling user
     * @param classificationName name of classification
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                                                 Date    effectiveTime,
                                                                 String  methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return this.getBeansByClassification(userId,
                                             OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                             classificationName,
                                             false,
                                             false,
                                             startingFrom,
                                             maxPageSize,
                                             effectiveTime,
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
                                                 false,
                                                 false,
                                                 startingFrom,
                                                 maxPageSize,
                                                 new Date(),
                                                 methodName);
    }
}
