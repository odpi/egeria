/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * FilesAndFoldersHandler provides the support for managing catalog entries about files and folders.
 *
 * @param <FILESYSTEM> the class representing a file system
 * @param <FOLDER> the class representing a folder in the file system
 * @param <FILE> the class representing a file in the file system
 */
public class FilesAndFoldersHandler<FILESYSTEM, FOLDER, FILE>
{
    private final String                  serviceName;
    private final String                  serverName;
    private final String                  localServerUserId;
    private final OMRSRepositoryHelper    repositoryHelper;
    private final RepositoryHandler       repositoryHandler;
    private final InvalidParameterHandler invalidParameterHandler;

    private final SoftwareCapabilityHandler<FILESYSTEM> fileSystemHandler;
    private final AssetHandler<FOLDER>                  folderHandler;
    private final AssetHandler<FILE>                    fileHandler;

    private final ConnectionHandler<OpenMetadataAPIDummyBean>                                connectionHandler;
    private final SchemaAttributeHandler<OpenMetadataAPIDummyBean, OpenMetadataAPIDummyBean> schemaAttributeHandler;

    private final static String folderDivider = "/";
    private final static String fileSystemDivider = "://";
    private final static String fileTypeDivider = "\\.";

    private final static String defaultAvroFileType = "avro";
    private final static String defaultCSVFileType  = "csv";


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param fileSystemConverter specific converter for the FILESYSTEM bean class
     * @param fileSystemBeanClass name of bean class that is represented by the generic class FILESYSTEM
     * @param folderConverter specific converter for the FOLDER bean class
     * @param folderBeanClass name of bean class that is represented by the generic class FOLDER
     * @param fileConverter specific converter for the FILE bean class
     * @param fileBeanClass name of bean class that is represented by the generic class FILE
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
    public FilesAndFoldersHandler(OpenMetadataAPIGenericConverter<FILESYSTEM> fileSystemConverter,
                                  Class<FILESYSTEM>                           fileSystemBeanClass,
                                  OpenMetadataAPIGenericConverter<FOLDER>     folderConverter,
                                  Class<FOLDER>                               folderBeanClass,
                                  OpenMetadataAPIGenericConverter<FILE>       fileConverter,
                                  Class<FILE>                                 fileBeanClass,
                                  String                                      serviceName,
                                  String                                      serverName,
                                  InvalidParameterHandler                     invalidParameterHandler,
                                  RepositoryHandler                           repositoryHandler,
                                  OMRSRepositoryHelper                        repositoryHelper,
                                  String                                      localServerUserId,
                                  OpenMetadataServerSecurityVerifier          securityVerifier,
                                  List<String>                                supportedZones,
                                  List<String>                                defaultZones,
                                  List<String>                                publishZones,
                                  AuditLog                                    auditLog)
    {

        this.serviceName             = serviceName;
        this.serverName              = serverName;
        this.localServerUserId       = localServerUserId;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler       = repositoryHandler;
        this.repositoryHelper        = repositoryHelper;

        this.fileSystemHandler       = new SoftwareCapabilityHandler<>(fileSystemConverter,
                                                                       fileSystemBeanClass,
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

        this.folderHandler          = new AssetHandler<>(folderConverter,
                                                         folderBeanClass,
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

        this.fileHandler            = new AssetHandler<>(fileConverter,
                                                         fileBeanClass,
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

        OpenMetadataAPIDummyBeanConverter<OpenMetadataAPIDummyBean> dummyConverter = new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper,
                                                                                                                             serviceName,
                                                                                                                             serverName);

        this.connectionHandler = new ConnectionHandler<>(dummyConverter,
                                                         OpenMetadataAPIDummyBean.class,
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

        OpenMetadataAPIDummyBeanConverter<OpenMetadataAPIDummyBean> dummySchemaAttributeConverter =
                new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName);

        this.schemaAttributeHandler = new SchemaAttributeHandler<>(dummySchemaAttributeConverter,
                                                                   OpenMetadataAPIDummyBean.class,
                                                                   dummySchemaAttributeConverter,
                                                                   OpenMetadataAPIDummyBean.class,
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
     * Return the URL header (if any) from a path name.
     *
     * @param pathName path name of a file
     * @return URL or null
     */
    private String getFileSystemName(String pathName)
    {
        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(fileSystemDivider);

            if (tokens.length > 1)
            {
                result = tokens[0] + fileSystemDivider;
            }
        }

        return result;
    }


    /**
     * Return the list of folder names from a path name.
     *
     * @param pathName path name of a file
     * @return list of folder names or null
     */
    private List<String> getFolderNames(String pathName)
    {
        List<String> result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(folderDivider);

            if (tokens.length > 1)
            {
                int startingToken = 0;

                if (tokens[startingToken].length() == 0)
                {
                    startingToken = 1;
                }
                else if (this.getFileSystemName(pathName) != null)
                {
                    startingToken = 2;
                }

                int endingToken = tokens.length;
                if (this.getFileName(pathName) != null)
                {
                    endingToken = endingToken - 1;
                }

                if (startingToken != endingToken)
                {

                    result = new ArrayList<>(Arrays.asList(tokens).subList(startingToken, endingToken));
                }
            }
        }

        return result;
    }


    /**
     * Return the name of the file from the path name.
     *
     * @param pathName path name of a file
     * @return file name (with type) or null
     */
    private String getFileName(String pathName)
    {
        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(folderDivider);

            result = tokens[tokens.length - 1];
        }

        return result;
    }


    /**
     * Return the file type of the file from the path name.
     *
     * @param pathName path name of a file
     * @return file type or null if no file type
     */
    private String getFileType(String pathName)
    {
        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(fileTypeDivider);

            if (tokens.length > 1)
            {
                result = tokens[tokens.length - 1];
            }
        }

        return result;
    }


    /**
     * Files live on a file system.  This method creates a top level anchor for a file system.
     * It has its own method because ot the extra properties in the FileSystem classification
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
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
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                     boolean              forLineage,
                                     boolean              forDuplicateProcessing,
                                     Date                 effectiveTime,
                                     String               methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return fileSystemHandler.createFileSystem(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  uniqueName,
                                                  displayName,
                                                  description,
                                                  type,
                                                  version,
                                                  patchLevel,
                                                  source,
                                                  format,
                                                  encryption,
                                                  additionalProperties,
                                                  vendorProperties,
                                                  effectiveFrom,
                                                  effectiveTo,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  methodName);
    }


    /**
     * Create the requested FileFolder asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param pathName full path name for the file system
     * @param name short display name
     * @param versionIdentifier version identifier for the folder
     * @param description description of the file system
     * @param typeName type of file system
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String createFolder(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  pathName,
                                String  name,
                                String  versionIdentifier,
                                String  description,
                                String  typeName,
                                Date    effectiveFrom,
                                Date    effectiveTo,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String folderAssetGUIDParameterName = "folderAssetGUID";

        String folderAssetTypeName = OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME;
        if (typeName != null)
        {
            folderAssetTypeName = typeName;
        }

        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME, pathName);

        return folderHandler.createAssetWithConnection(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       folderAssetGUIDParameterName,
                                                       this.createQualifiedName(folderAssetTypeName, pathName, versionIdentifier),
                                                       name,
                                                       versionIdentifier,
                                                       description,
                                                       null,
                                                       folderAssetTypeName,
                                                       extendedProperties,
                                                       InstanceStatus.ACTIVE,
                                                       true,
                                                       null,
                                                       null,
                                                       pathName,
                                                       null,
                                                       null,
                                                       effectiveFrom,
                                                       effectiveTo,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);
    }


    /**
     * Create the requested asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param fileType file extension name
     * @param fileName name of the file
     * @param pathName qualified name for the file system
     * @param displayName short display name
     * @param versionIdentifier version identifier for the file system
     * @param description description of the file system
     * @param typeName type of file system
     * @param initialExtendedProperties extended properties for a specific file type
     * @param configurationProperties  for the connection
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String createFileAsset(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              fileType,
                                   String              fileName,
                                   String              pathName,
                                   String              displayName,
                                   String              versionIdentifier,
                                   String              description,
                                   String              typeName,
                                   Map<String, Object> initialExtendedProperties,
                                   Map<String, Object> configurationProperties,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   boolean             forLineage,
                                   boolean             forDuplicateProcessing,
                                   Date                effectiveTime,
                                   String              methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String fileAssetGUIDParameterName = "fileAssetGUID";

        Map<String, Object> extendedProperties = new HashMap<>();
        if (initialExtendedProperties != null)
        {
            extendedProperties = new HashMap<>(initialExtendedProperties);
        }

        extendedProperties.put(OpenMetadataAPIMapper.FILE_TYPE_PROPERTY_NAME, fileType);
        extendedProperties.put(OpenMetadataAPIMapper.FILE_NAME_PROPERTY_NAME, fileName);
        extendedProperties.put(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME, pathName);

        String fileAssetTypeName = OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
        if (typeName != null)
        {
            fileAssetTypeName = typeName;
        }

        return fileHandler.createAssetWithConnection(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     fileAssetGUIDParameterName,
                                                     this.createQualifiedName(fileAssetTypeName, pathName, versionIdentifier),
                                                     displayName,
                                                     versionIdentifier,
                                                     description,
                                                     null,
                                                     fileAssetTypeName,
                                                     extendedProperties,
                                                     InstanceStatus.ACTIVE,
                                                     true,
                                                     configurationProperties,
                                                     null,
                                                     pathName,
                                                     null,
                                                     null,
                                                     effectiveFrom,
                                                     effectiveTo,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);
    }


    /**
     * Construct the qualified name for a file resource.
     *
     * @param typeName type of element
     * @param pathName pathname in file system
     * @param versionIdentifier version identifier
     * @return qualified name
     */
    private String createQualifiedName(String typeName,
                                       String pathName,
                                       String versionIdentifier)
    {
        if (versionIdentifier == null)
        {
            return typeName + ":" + pathName;
        }
        else
        {
            return typeName + ":" + pathName + ":" + versionIdentifier;
        }
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectToGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
     * @param folderName name of the leaf folder
     * @param versionIdentifier version identifier for the asset
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String createFolderInCatalog(String   userId,
                                         String   externalSourceGUID,
                                         String   externalSourceName,
                                         String   connectToGUID,
                                         String   pathName,
                                         String   folderName,
                                         String   versionIdentifier,
                                         Date     effectiveFrom,
                                         Date     effectiveTo,
                                         boolean  forLineage,
                                         boolean  forDuplicateProcessing,
                                         Date     effectiveTime,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String connectToParameterName = "connectToGUID";
        final String folderParameterName = "folderGUID";

        String folderGUID = createFolder(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         pathName,
                                         folderName,
                                         versionIdentifier,
                                         null,
                                         OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                         effectiveFrom,
                                         effectiveTo,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);

        if (connectToGUID != null)
        {
            if (repositoryHandler.isEntityATypeOf(userId,
                                                  connectToGUID,
                                                  connectToParameterName,
                                                  OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                  effectiveTime,
                                                  methodName))
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   connectToGUID,
                                                   connectToParameterName,
                                                   OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                   folderGUID,
                                                   folderParameterName,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                   OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                   null,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   effectiveTime,
                                                   methodName);
            }
            else
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   connectToGUID,
                                                   connectToParameterName,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   folderGUID,
                                                   folderParameterName,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_GUID,
                                                   OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_NAME,
                                                   null,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   effectiveTime,
                                                   methodName);
            }
        }

        return folderGUID;
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectToGUID root object to connect the folder to
     * @param fileSystemName name of the root of the file system (can be null)
     * @param folderNames list of the folder names
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<String> createFolderStructureInCatalog(String         userId,
                                                        String         externalSourceGUID,
                                                        String         externalSourceName,
                                                        String         connectToGUID,
                                                        String         fileSystemName,
                                                        List<String>   folderNames,
                                                        Date           effectiveFrom,
                                                        Date           effectiveTo,
                                                        boolean        forLineage,
                                                        boolean        forDuplicateProcessing,
                                                        Date           effectiveTime,
                                                        String         methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String localMethodName = "->createFolderStructureInCatalog";

        List<String>  folderGUIDs = new ArrayList<>();

        if ((folderNames != null) && (! folderNames.isEmpty()))
        {
            String pathName = null;
            String folderName = null;
            String nextConnectToGUID = connectToGUID;

            for (String folderFragment : folderNames)
            {
                if (pathName == null)
                {
                    if (fileSystemName != null)
                    {
                        pathName = fileSystemName + folderFragment;
                    }
                    else
                    {
                        pathName = folderFragment;
                    }
                }
                else
                {
                    pathName = pathName + folderDivider + folderFragment;
                }

                if (folderName != null)
                {
                    folderName = folderName + folderDivider + folderFragment;
                }
                else
                {
                    folderName = folderFragment;
                }

                String currentFolderGUID = this.getFolderGUIDByPathName(userId,
                                                                        pathName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

                if (currentFolderGUID == null)
                {
                    String folderGUID = createFolderInCatalog(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              nextConnectToGUID,
                                                              pathName,
                                                              folderName,
                                                              null,
                                                              effectiveFrom,
                                                              effectiveTo,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName + localMethodName);

                    folderGUIDs.add(folderGUID);
                    nextConnectToGUID = folderGUID;
                }
                else
                {
                    folderGUIDs.add(currentFolderGUID);
                    nextConnectToGUID = currentFolderGUID;
                }
            }
        }

        if (folderGUIDs.isEmpty())
        {
            return null;
        }

        return folderGUIDs;
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectToGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> createFolderStructureInCatalog(String   userId,
                                                       String   externalSourceGUID,
                                                       String   externalSourceName,
                                                       String   connectToGUID,
                                                       String   pathName,
                                                       Date     effectiveFrom,
                                                       Date     effectiveTo,
                                                       boolean  forLineage,
                                                       boolean  forDuplicateProcessing,
                                                       Date     effectiveTime,
                                                       String   methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return createFolderStructureInCatalog(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              connectToGUID,
                                              this.getFileSystemName(pathName),
                                              this.getFolderNames(pathName),
                                              effectiveFrom,
                                              effectiveTo,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);
    }


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param fileSystemGUIDParameterName parameter name for the fileSystemGUID
     * @param folderGUID unique identifier of the folder in the catalog
     * @param folderGUIDParameterName parameter name for the folderGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void attachFolderToFileSystem(String  userId,
                                         String  externalSourceGUID,
                                         String  externalSourceName,
                                         String  fileSystemGUID,
                                         String  fileSystemGUIDParameterName,
                                         String  folderGUID,
                                         String  folderGUIDParameterName,
                                         Date    effectiveFrom,
                                         Date    effectiveTo,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        folderHandler.linkElementToElement(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           fileSystemGUID,
                                           fileSystemGUIDParameterName,
                                           OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                           folderGUID,
                                           folderGUIDParameterName,
                                           OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                           forLineage,
                                           forDuplicateProcessing,
                                           OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                           OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                           null,
                                           effectiveFrom,
                                           effectiveTo,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param fileSystemGUIDParameterName parameter name for the fileSystemGUID
     * @param folderGUID unique identifier of the folder in the catalog
     * @param folderGUIDParameterName parameter name for the folderGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void detachFolderFromFileSystem(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  fileSystemGUID,
                                           String  fileSystemGUIDParameterName,
                                           String  folderGUID,
                                           String  folderGUIDParameterName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        folderHandler.unlinkElementFromElement(userId,
                                               false,
                                               externalSourceGUID,
                                               externalSourceName,
                                               fileSystemGUID,
                                               fileSystemGUIDParameterName,
                                               OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                               folderGUID,
                                               folderGUIDParameterName,
                                               OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                               OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                               OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                               effectiveTime,
                                               methodName);
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param folderGUID unique identifier of the folder
     * @param folderGUIDParameterName parameter providing folderGUID
     * @param fileGUID unique identifier of the file
     * @param fileGUIDParameterName  parameter providing fileGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  attachDataFileAssetToFolder(String   userId,
                                             String   externalSourceGUID,
                                             String   externalSourceName,
                                             String   folderGUID,
                                             String   folderGUIDParameterName,
                                             String   fileGUID,
                                             String   fileGUIDParameterName,
                                             Date     effectiveFrom,
                                             Date     effectiveTo,
                                             boolean  forLineage,
                                             boolean  forDuplicateProcessing,
                                             Date     effectiveTime,
                                             String   methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        fileHandler.linkElementToElement(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         folderGUID,
                                         folderGUIDParameterName,
                                         OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                         fileGUID,
                                         fileGUIDParameterName,
                                         OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                         forLineage,
                                         forDuplicateProcessing,
                                         OpenMetadataAPIMapper.LINKED_FILE_TYPE_GUID,
                                         OpenMetadataAPIMapper.LINKED_FILE_TYPE_NAME,
                                         null,
                                         effectiveFrom,
                                         effectiveTo,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveDataFileInCatalog to record
     * the fact that the physical file has moved.  Use attachDataFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param folderGUID unique identifier of the folder
     * @param folderGUIDParameterName parameter providing folderGUID
     * @param fileGUID unique identifier of the file
     * @param fileGUIDParameterName  parameter providing fileGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  detachDataFileAssetFromFolder(String   userId,
                                               String   externalSourceGUID,
                                               String   externalSourceName,
                                               String   folderGUID,
                                               String   folderGUIDParameterName,
                                               String   fileGUID,
                                               String   fileGUIDParameterName,
                                               boolean  forLineage,
                                               boolean  forDuplicateProcessing,
                                               Date     effectiveTime,
                                               String   methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        folderHandler.unlinkElementFromElement(userId,
                                               false,
                                               externalSourceGUID,
                                               externalSourceName,
                                               fileGUID,
                                               fileGUIDParameterName,
                                               OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                               folderGUID,
                                               folderGUIDParameterName,
                                               OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                               OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               OpenMetadataAPIMapper.LINKED_FILE_TYPE_GUID,
                                               OpenMetadataAPIMapper.LINKED_FILE_TYPE_NAME,
                                               effectiveTime,
                                               methodName);
    }


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also, the endpoint in the connection object.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param newParentFolder unique identifier of the new parent folder
     * @param newParentFolderGUIDParameterName parameter providing newParentFolder
     * @param fileGUID unique identifier of the file
     * @param fileGUIDParameterName  parameter providing fileGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  moveDataFileInCatalog(String   userId,
                                       String   externalSourceGUID,
                                       String   externalSourceName,
                                       String   newParentFolder,
                                       String   newParentFolderGUIDParameterName,
                                       String   fileGUID,
                                       String   fileGUIDParameterName,
                                       Date     effectiveFrom,
                                       Date     effectiveTo,
                                       boolean  forLineage,
                                       boolean  forDuplicateProcessing,
                                       Date     effectiveTime,
                                       String   methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String endpointGUIDParameterName = "endpointGUID";

        String newFolderPathName = folderHandler.getBeanStringPropertyFromRepository(userId,
                                                                                     newParentFolder,
                                                                                     newParentFolderGUIDParameterName,
                                                                                     OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                                                     OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                     effectiveTime,
                                                                                     methodName);
        String existingFilePathName = fileHandler.getBeanStringPropertyFromRepository(userId,
                                                                                      newParentFolder,
                                                                                      newParentFolderGUIDParameterName,
                                                                                      OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                      effectiveTime,
                                                                                      methodName);
        String fileName = this.getFileName(existingFilePathName);
        String fullPathName = newFolderPathName + "/" + fileName;

        InstanceProperties properties = null;

        if ((effectiveFrom != null) || (effectiveTo != null))
        {
            properties = new InstanceProperties();

            properties.setEffectiveFromTime(effectiveFrom);
            properties.setEffectiveToTime(effectiveTo);
        }

        fileHandler.relinkElementToNewElement(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              fileGUID,
                                              fileGUIDParameterName,
                                              OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                              false,
                                              newParentFolder,
                                              newParentFolderGUIDParameterName,
                                              OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                              forLineage,
                                              forDuplicateProcessing,
                                              OpenMetadataAPIMapper.LINKED_FILE_TYPE_GUID,
                                              OpenMetadataAPIMapper.LINKED_FILE_TYPE_NAME,
                                              properties,
                                              effectiveTime,
                                              methodName);

        fileHandler.updateBeanPropertyInRepository(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   fileGUID,
                                                   fileGUIDParameterName,
                                                   OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                                   OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                   OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                   fullPathName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);

        List<String> relationshipPath = new ArrayList<>();
        relationshipPath.add(OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID);
        relationshipPath.add(OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID);

        List<String> endpointGUIDs = new ArrayList<>();
        while (endpointGUIDs != null)
        {
            endpointGUIDs = fileHandler.getRelatedEntityGUIDs(userId,
                                                              fileGUID,
                                                              fileGUIDParameterName,
                                                              OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                                              relationshipPath,
                                                              OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                              0,
                                                              invalidParameterHandler.getMaxPagingSize(),
                                                              effectiveTime,
                                                              methodName);

            if (endpointGUIDs.isEmpty())
            {
                endpointGUIDs = null;
            }
            else
            {
                for (String endpointGUID : endpointGUIDs)
                {
                    if (endpointGUID != null)
                    {
                        fileSystemHandler.updateBeanPropertyInRepository(userId,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         endpointGUID,
                                                                         endpointGUIDParameterName,
                                                                         OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                                                         OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                         fullPathName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);
                    }
                }
            }
        }
    }


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also, the endpoint in the connection object.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param newParentFolderGUID new parent folder
     * @param newParentFolderGUIDParameterName name of parameter supplying newParentFolderGUID
     * @param movingFolderGUID unique identifier of the data folder to move
     * @param movingFolderGUIDParameterName name of parameter supplying movingFolderGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  moveDataFolderInCatalog(String              userId,
                                         String              externalSourceGUID,
                                         String              externalSourceName,
                                         String              newParentFolderGUID,
                                         String              newParentFolderGUIDParameterName,
                                         String              movingFolderGUID,
                                         String              movingFolderGUIDParameterName,
                                         Date                effectiveFrom,
                                         Date                effectiveTo,
                                         boolean             forLineage,
                                         boolean             forDuplicateProcessing,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(newParentFolderGUID, newParentFolderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(movingFolderGUID, movingFolderGUIDParameterName, methodName);

        // todo
        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);
    }


    /**
     * Takes a new file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".  Care is taken to handle the case where the file system and file folders exist in the catalog
     * but are not visible through the user's zones.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param fileAssetGUID unique identifier of file asset
     * @param fileAssetParameterName parameter providing the fileAssetGUID
     * @param pathName pathname of the file
     * @param pathNameParameterName parameter providing the pathName
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<String> addFileAssetPath(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  fileAssetGUID,
                                          String  fileAssetParameterName,
                                          String  pathName,
                                          String  pathNameParameterName,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        List<String> assetGUIDList = new ArrayList<>();

        String fileSystemGUID = null;
        String fileSystemName = this.getFileSystemName(pathName);

        if (fileSystemName != null)
        {
            /*
             * The file's pathname includes the root file system name.  A SoftWareServerCapability entity
             * is created for the file system if it does not exist already.
             */
            fileSystemGUID = fileSystemHandler.getBeanGUIDByQualifiedName(userId,
                                                                          OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                          fileSystemName,
                                                                          pathNameParameterName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

            if (fileSystemGUID == null)
            {
                fileSystemGUID = fileSystemHandler.createFileSystem(localServerUserId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    fileSystemName,
                                                                    fileSystemName,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);
            }
        }

        /*
         * Now set up the folder names.
         */
        List<String> folderNames = this.getFolderNames(pathName);
        List<String> folderGUIDs = null;
        String       fileParentGUID;

        if (folderNames == null)
        {
            fileParentGUID = fileSystemGUID;
        }
        else
        {
            folderGUIDs = this.createFolderStructureInCatalog(localServerUserId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              fileSystemGUID,
                                                              fileSystemName,
                                                              folderNames,
                                                              null,
                                                              null,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName);

            if ((folderGUIDs != null) && (!folderGUIDs.isEmpty()))
            {
                fileParentGUID = folderGUIDs.get(folderGUIDs.size() - 1);
            }
            else
            {
                fileParentGUID = fileSystemGUID;
            }
        }

        /*
         * Now link in the file to the chain
         */
        if ((fileAssetGUID != null) && (fileParentGUID != null))
        {
            /*
             * The assets for the parent part have been created.  Now connect the file asset to its parent.
             * If there are parent folders then need a nestedFiles relationship.  If the root is the file system
             * then the relationship is server asset use.
             */
            if (fileParentGUID.equals(fileSystemGUID))
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   fileParentGUID,
                                                   pathNameParameterName,
                                                   OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                   fileAssetGUID,
                                                   fileAssetParameterName,
                                                   OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                   OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                   (InstanceProperties) null,
                                                   null,
                                                   null,
                                                   effectiveTime,
                                                   methodName);
            }
            else
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   fileParentGUID,
                                                   pathNameParameterName,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   fileAssetGUID,
                                                   fileAssetParameterName,
                                                   OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataAPIMapper.NESTED_FILE_TYPE_GUID,
                                                   OpenMetadataAPIMapper.NESTED_FILE_TYPE_NAME,
                                                   (InstanceProperties) null,
                                                   null,
                                                   null,
                                                   effectiveTime,
                                                   methodName);
            }
        }

        if (fileSystemGUID != null)
        {
            assetGUIDList.add(fileSystemGUID);
        }

        if (folderGUIDs != null)
        {
            assetGUIDList.addAll(folderGUIDs);
        }

        if (fileAssetGUID != null)
        {
            assetGUIDList.add(fileAssetGUID);
        }

        if (assetGUIDList.isEmpty())
        {
            return null;
        }

        return assetGUIDList;
    }


    /**
     * Creates a new file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param name  name for the folder in the catalog
     * @param versionIdentifier version identifier for the folder in the catalog
     * @param description description of the folder in the catalog
     * @param pathName pathname of the file
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFileAssetToCatalog(String   userId,
                                                  String   externalSourceGUID,
                                                  String   externalSourceName,
                                                  String   name,
                                                  String   versionIdentifier,
                                                  String   description,
                                                  String   pathName,
                                                  Date     effectiveFrom,
                                                  Date     effectiveTo,
                                                  boolean  forLineage,
                                                  boolean  forDuplicateProcessing,
                                                  Date     effectiveTime,
                                                  String   methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String pathParameterName = "pathName";
        final String fileAssetParameterName = "fileAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameterName, methodName);

        String fileType = this.getFileType(pathName);
        String fileName = this.getFileName(pathName);
        String fileAssetGUID = this.createFileAsset(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    fileType,
                                                    fileName,
                                                    pathName,
                                                    name,
                                                    versionIdentifier,
                                                    description,
                                                    OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                    null,
                                                    null,
                                                    effectiveFrom,
                                                    effectiveTo,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     fileAssetGUID,
                                     fileAssetParameterName,
                                     pathName,
                                     pathParameterName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }



    /**
     * Set up the extended properties found in the basic data file.
     *
     * @param pathName the fully qualified physical location of the data store
     * @param createTime the time that the file was created
     * @param modifiedTime the time of the latest change to the file
     * @param encodingType the type of encoding used on the file
     * @param encodingLanguage the language used within the file
     * @param encodingDescription the description of the file
     * @param encodingProperties the properties used to drive the encoding
     * @param fileType the type of file override (default is to use the file extension)
     * @param extendedProperties extended properties supplied by the caller
     * @return filled out map or null
     */
    private Map<String, Object> getExtendedProperties(String              pathName,
                                                      Date                createTime,
                                                      Date                modifiedTime,
                                                      String              encodingType,
                                                      String              encodingLanguage,
                                                      String              encodingDescription,
                                                      Map<String, String> encodingProperties,
                                                      String              fileType,
                                                      Map<String, Object> extendedProperties)
    {
        Map<String, Object> assetExtendedProperties = extendedProperties;

        if (assetExtendedProperties == null)
        {
            assetExtendedProperties = new HashMap<>();
        }

        if (pathName != null)
        {
            assetExtendedProperties.put(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME, pathName);
        }

        if (createTime != null)
        {
            assetExtendedProperties.put(OpenMetadataAPIMapper.STORE_CREATE_TIME_PROPERTY_NAME, createTime);
        }

        if (modifiedTime != null)
        {
            assetExtendedProperties.put(OpenMetadataAPIMapper.STORE_UPDATE_TIME_PROPERTY_NAME, modifiedTime);
        }

        if (encodingType != null)
        {
            assetExtendedProperties.put(OpenMetadataAPIMapper.ENCODING_TYPE_PROPERTY_NAME, encodingType);
        }

        if (encodingLanguage != null)
        {
            assetExtendedProperties.put(OpenMetadataAPIMapper.ENCODING_LANGUAGE_PROPERTY_NAME, encodingLanguage);
        }

        if (encodingDescription != null)
        {
            assetExtendedProperties.put(OpenMetadataAPIMapper.ENCODING_DESCRIPTION_PROPERTY_NAME, encodingDescription);
        }

        if (encodingProperties != null)
        {
            assetExtendedProperties.put(OpenMetadataAPIMapper.ENCODING_PROPERTIES_PROPERTY_NAME, encodingProperties);
        }

        if (fileType != null)
        {
            assetExtendedProperties.put(OpenMetadataAPIMapper.FILE_TYPE_PROPERTY_NAME, fileType);
        }

        if (assetExtendedProperties.isEmpty())
        {
            return null;
        }

        return assetExtendedProperties;
    }


    /**
     * Creates a new folder asset that is identified as a data asset.  This means the files and sub-folders within
     * it collectively make up the contents of the data asset.  As with other types of file-based asset, links
     * are made to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyDataFolder" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a DataFolder asset called
     * "one/two/three/MyDataFolder".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param pathName pathname of the file
     * @param name  name for the folder in the catalog
     * @param versionIdentifier  version identifier for the folder in the catalog
     * @param description description of the folder in the catalog
     * @param createTime time that the folder was created
     * @param modifiedTime the time of the latest change to the file
     * @param encodingType the type of encoding used on the file
     * @param encodingLanguage the language used within the file
     * @param encodingDescription the description of the file
     * @param encodingProperties the properties used to drive the encoding
     * @param additionalProperties additional properties for the data folder
     * @param connectorProviderClassName name of the class for the connector's provider - null means used standard connector provider for asset type
     * @param typeName type name of folder
     * @param extendedProperties extended properties supplied by the caller
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFolderAssetToCatalog(String              userId,
                                                    String              externalSourceGUID,
                                                    String              externalSourceName,
                                                    String              pathName,
                                                    String              name,
                                                    String              versionIdentifier,
                                                    String              description,
                                                    Date                createTime,
                                                    Date                modifiedTime,
                                                    String              encodingType,
                                                    String              encodingLanguage,
                                                    String              encodingDescription,
                                                    Map<String, String> encodingProperties,
                                                    Map<String, String> additionalProperties,
                                                    String              connectorProviderClassName,
                                                    String              typeName,
                                                    Map<String, Object> extendedProperties,
                                                    Date                effectiveFrom,
                                                    Date                effectiveTo,
                                                    boolean             forLineage,
                                                    boolean             forDuplicateProcessing,
                                                    Date                effectiveTime,
                                                    String              methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String pathParameterName = "pathName";
        final String folderAssetParameterName = "folderAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameterName, methodName);

        Map<String, Object> assetExtendedProperties = this.getExtendedProperties(pathName,
                                                                                 createTime,
                                                                                 modifiedTime,
                                                                                 encodingType,
                                                                                 encodingLanguage,
                                                                                 encodingDescription,
                                                                                 encodingProperties,
                                                                                 null,
                                                                                 extendedProperties);


        String folderAssetTypeName = OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME;

        if (typeName != null)
        {
            folderAssetTypeName = typeName;
        }

        String folderAssetGUID = fileHandler.createAssetWithConnection(userId,
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       folderAssetParameterName,
                                                                       pathName,
                                                                       name,
                                                                       versionIdentifier,
                                                                       description,
                                                                       additionalProperties,
                                                                       folderAssetTypeName,
                                                                       assetExtendedProperties,
                                                                       InstanceStatus.ACTIVE,
                                                                       true,
                                                                       null,
                                                                       connectorProviderClassName,
                                                                       pathName,
                                                                       null,
                                                                       null,
                                                                       effectiveFrom,
                                                                       effectiveTo,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     folderAssetGUID,
                                     folderAssetParameterName,
                                     pathName,
                                     pathParameterName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Creates a new folder asset that is identified as a data asset.  This means the files and sub-folders within
     * it collectively make up the contents of the data asset.  As with other types of file-based asset, links
     * are made to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyDataFolder" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a DataFolder asset called
     * "one/two/three/MyDataFolder".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param name  name for the folder in the catalog
     * @param versionIdentifier  versionIdentifier for the folder in the catalog
     * @param description description of the folder in the catalog
     * @param pathName pathname of the file
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFolderAssetToCatalog(String              userId,
                                                    String              externalSourceGUID,
                                                    String              externalSourceName,
                                                    String              pathName,
                                                    String              name,
                                                    String              versionIdentifier,
                                                    String              description,
                                                    Date                effectiveFrom,
                                                    Date                effectiveTo,
                                                    boolean             forLineage,
                                                    boolean             forDuplicateProcessing,
                                                    Date                effectiveTime,
                                                    String              methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String pathParameterName = "pathName";
        final String folderAssetParameterName = "folderAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameterName, methodName);

        String folderAssetGUID = this.createFolder(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   pathName,
                                                   name,
                                                   versionIdentifier,
                                                   description,
                                                   OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     folderAssetGUID,
                                     folderAssetParameterName,
                                     pathName,
                                     pathParameterName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Add a simple asset description linked to a connection object for a file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param qualifiedName unique name for the file - typically path and file name
     * @param name short  name for file (defaults to the file name without the path)
     * @param versionIdentifier version identifier for the file
     * @param description description of the file
     * @param pathName  the fully qualified physical location of the data store - default is qualified name
     * @param createTime the time that the file was created
     * @param modifiedTime the time of the latest change to the file
     * @param encodingType the type of encoding used on the file
     * @param encodingLanguage the language used within the file
     * @param encodingDescription the description of the file
     * @param encodingProperties the properties used to drive the encoding
     * @param suppliedFileType the type of file override (default is to use the file extension)
     * @param additionalProperties additional properties from the user
     * @param connectorProviderClassName name of the class for the connector's provider - null means used standard connector provider for asset type
     * @param typeName name of the type (default is File)
     * @param extendedProperties any additional properties for the file type
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  addFileToCatalog(String              userId,
                                          String              externalSourceGUID,
                                          String              externalSourceName,
                                          String              qualifiedName,
                                          String              name,
                                          String              versionIdentifier,
                                          String              description,
                                          String              pathName,
                                          Date                createTime,
                                          Date                modifiedTime,
                                          String              encodingType,
                                          String              encodingLanguage,
                                          String              encodingDescription,
                                          Map<String, String> encodingProperties,
                                          String              suppliedFileType,
                                          Map<String, String> additionalProperties,
                                          String              connectorProviderClassName,
                                          String              typeName,
                                          Map<String, Object> extendedProperties,
                                          Date                effectiveFrom,
                                          Date                effectiveTo,
                                          boolean             forLineage,
                                          boolean             forDuplicateProcessing,
                                          Date                effectiveTime,
                                          String              methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String pathParameterName = "qualifiedName";
        final String fileAssetParameterName = "fileAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, pathParameterName, methodName);

        String fullPath = pathName;

        if (fullPath == null)
        {
            fullPath = qualifiedName;
        }

        String fileType = suppliedFileType;

        if (fileType != null)
        {
            fileType = this.getFileType(fullPath);
        }

        Map<String, Object> assetExtendedProperties = this.getExtendedProperties(fullPath,
                                                                                 createTime,
                                                                                 modifiedTime,
                                                                                 encodingType,
                                                                                 encodingLanguage,
                                                                                 encodingDescription,
                                                                                 encodingProperties,
                                                                                 fileType,
                                                                                 extendedProperties);

        String fileAssetTypeName = typeName;
        if (fileAssetTypeName == null)
        {
            if (defaultCSVFileType.equals(fileType))
            {
                fileAssetTypeName = OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
            }
            else if (defaultAvroFileType.equals(fileType))
            {
                fileAssetTypeName = OpenMetadataAPIMapper.AVRO_FILE_TYPE_NAME;
            }
            else
            {
                fileAssetTypeName = OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
            }
        }

        String fileAssetGUID = fileHandler.createAssetWithConnection(userId,
                                                                     externalSourceGUID,
                                                                     externalSourceName,
                                                                     fileAssetParameterName,
                                                                     fullPath,
                                                                     name,
                                                                     versionIdentifier,
                                                                     description,
                                                                     additionalProperties,
                                                                     fileAssetTypeName,
                                                                     assetExtendedProperties,
                                                                     InstanceStatus.ACTIVE,
                                                                     true,
                                                                     null,
                                                                     connectorProviderClassName,
                                                                     fullPath,
                                                                     null,
                                                                     null,
                                                                     effectiveFrom,
                                                                     effectiveTo,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        if (fileAssetGUID != null)
        {
            return this.addFileAssetPath(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         fileAssetGUID,
                                         fileAssetParameterName,
                                         fullPath,
                                         pathParameterName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }

        return null;
    }


    /**
     * Create a new file asset based on an existing asset but with the supplied path name, display name and description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of the asset description to copy
     * @param fullPath unique path and file name for file
     * @param name short display name for file (defaults to the file name without the path)
     * @param versionIdentifier version identifier of the file
     * @param description description of the file
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  addFileToCatalogFromTemplate(String  userId,
                                                      String  externalSourceGUID,
                                                      String  externalSourceName,
                                                      String  templateGUID,
                                                      String  fullPath,
                                                      String  name,
                                                      String  versionIdentifier,
                                                      String  description,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String pathNameParameterName      = "fullPath";
        final String fileAssetParameterName     = "fileAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fullPath, pathNameParameterName, methodName);

        String fileAssetGUID = fileHandler.addAssetFromTemplate(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                templateGUID,
                                                                templateGUIDParameterName,
                                                                OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                createQualifiedName(OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME, fullPath, versionIdentifier),
                                                                pathNameParameterName,
                                                                name,
                                                                versionIdentifier,
                                                                description,
                                                                fullPath,
                                                                fullPath,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);

        if (fileAssetGUID != null)
        {
            return this.addFileAssetPath(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         fileAssetGUID,
                                         fileAssetParameterName,
                                         fullPath,
                                         pathNameParameterName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }

        return null;
    }


    /**
     * Create a new folder asset based on an existing asset but with the supplied path name, display name and description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of the asset description to copy
     * @param pathName unique path and file name for file
     * @param name short name for file (defaults to the file name without the path)
     * @param versionIdentifier version identifier for the file
     * @param description description of the file
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  addFolderToCatalogFromTemplate(String  userId,
                                                        String  externalSourceGUID,
                                                        String  externalSourceName,
                                                        String  templateGUID,
                                                        String  pathName,
                                                        String  name,
                                                        String  versionIdentifier,
                                                        String  description,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime,
                                                        String  methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String pathNameParameterName      = "pathName";
        final String fileAssetParameterName     = "folderAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(pathName, pathNameParameterName, methodName);

        String folderAssetGUID = fileHandler.addAssetFromTemplate(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  templateGUID,
                                                                  templateGUIDParameterName,
                                                                  OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                                  this.createQualifiedName(OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME, pathName, versionIdentifier),
                                                                  pathNameParameterName,
                                                                  name,
                                                                  versionIdentifier,
                                                                  description,
                                                                  pathName,
                                                                  pathName,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName);

        if (folderAssetGUID != null)
        {
            return this.addFileAssetPath(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         folderAssetGUID,
                                         fileAssetParameterName,
                                         pathName,
                                         pathNameParameterName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }

        return null;
    }


    /**
     * Add a simple asset description linked to a connection object for an Avro file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param name display name for the file in the catalog
     * @param versionIdentifier version identifier for the file
     * @param description description of the file in the catalog
     * @param fullPath full path of the file - used to access the file through the connector
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  addAvroFileToCatalog(String       userId,
                                              String       externalSourceGUID,
                                              String       externalSourceName,
                                              String       name,
                                              String       versionIdentifier,
                                              String       description,
                                              String       fullPath,
                                              Date         effectiveFrom,
                                              Date         effectiveTo,
                                              boolean      forLineage,
                                              boolean      forDuplicateProcessing,
                                              Date         effectiveTime,
                                              String       methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String pathParameterName = "fullPath";
        final String fileAssetParameterName = "fileAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(fullPath, pathParameterName, methodName);

        String fileType = this.getFileType(fullPath);
        String fileName = this.getFileName(fullPath);

        if (fileType == null)
        {
            fileType = defaultAvroFileType;
        }

        String fileAssetGUID = this.createFileAsset(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    fileType,
                                                    fileName,
                                                    fullPath,
                                                    name,
                                                    versionIdentifier,
                                                    description,
                                                    OpenMetadataAPIMapper.AVRO_FILE_TYPE_NAME,
                                                    null,
                                                    null,
                                                    effectiveFrom,
                                                    effectiveTo,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     fileAssetGUID,
                                     fileAssetParameterName,
                                     fullPath,
                                     pathParameterName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param name  name for the file in the catalog
     * @param versionIdentifier version identifier for the file
     * @param description description of the file in the catalog
     * @param fullPath full path of the file - used to access the file through the connector
     * @param columnHeaders does the first line of the file contain the column names. If not pass the list of column headers.
     * @param delimiterCharacter what is the delimiter character - null for default of comma
     * @param quoteCharacter what is the character to group a field that contains delimiter characters
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  addCSVFileToCatalog(String       userId,
                                             String       externalSourceGUID,
                                             String       externalSourceName,
                                             String       name,
                                             String       versionIdentifier,
                                             String       description,
                                             String       fullPath,
                                             List<String> columnHeaders,
                                             Character    delimiterCharacter,
                                             Character    quoteCharacter,
                                             Date         effectiveFrom,
                                             Date         effectiveTo,
                                             boolean      forLineage,
                                             boolean      forDuplicateProcessing,
                                             Date         effectiveTime,
                                             String       methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String pathParameterName = "fullPath";
        final String fileAssetGUIDParameterName = "fileAssetGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(fullPath, pathParameterName, methodName);

        String fileType = this.getFileType(fullPath);
        String fileName = this.getFileName(fullPath);

        if (fileType == null)
        {
            fileType = defaultCSVFileType;
        }

        if (delimiterCharacter == null)
        {
            delimiterCharacter = ',';
        }

        if (quoteCharacter == null)
        {
            quoteCharacter = '\"';
        }

        Map<String, Object> extendedProperties = new HashMap<>();
        extendedProperties.put(OpenMetadataAPIMapper.DELIMITER_CHARACTER_PROPERTY_NAME, delimiterCharacter.toString());
        extendedProperties.put(OpenMetadataAPIMapper.QUOTE_CHARACTER_PROPERTY_NAME, quoteCharacter.toString());
        extendedProperties.put(OpenMetadataAPIMapper.FILE_TYPE_PROPERTY_NAME, fileType);

        Map<String, Object>  configurationProperties = new HashMap<>();

        configurationProperties.put(CSVFileStoreProvider.delimiterCharacterProperty, delimiterCharacter);
        configurationProperties.put(CSVFileStoreProvider.quoteCharacterProperty, quoteCharacter);

        if (columnHeaders != null)
        {
            configurationProperties.put(CSVFileStoreProvider.columnNamesProperty, columnHeaders);
        }

        String fileAssetGUID = this.createFileAsset(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    fileType,
                                                    fileName,
                                                    fullPath,
                                                    name,
                                                    versionIdentifier,
                                                    description,
                                                    OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME,
                                                    extendedProperties,
                                                    configurationProperties,
                                                    effectiveFrom,
                                                    effectiveTo,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);

        if ((columnHeaders != null) && (! columnHeaders.isEmpty()))
        {
            String schemaTypeGUID = schemaAttributeHandler.getAssetSchemaTypeGUID(userId,
                                                                                  externalSourceGUID,
                                                                                  externalSourceName,
                                                                                  fileAssetGUID,
                                                                                  fileAssetGUIDParameterName,
                                                                                  OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                                                                                  effectiveFrom,
                                                                                  effectiveTo,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);

            int columnCount = 0;
            for (String columnName : columnHeaders)
            {
                if (columnName != null)
                {
                    String columnQualifiedName = fullPath + "::" + columnName + "::" + columnCount;
                    String columnDisplayName = columnName + "::" + columnCount;

                    SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(columnQualifiedName,
                                                                                               columnDisplayName,
                                                                                               null,
                                                                                               columnCount,
                                                                                               1,
                                                                                               1,
                                                                                               false,
                                                                                               null,
                                                                                               true,
                                                                                               false,
                                                                                               0,
                                                                                               0,
                                                                                               0,
                                                                                               0,
                                                                                               false,
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               OpenMetadataAPIMapper.TABULAR_FILE_COLUMN_TYPE_GUID,
                                                                                               OpenMetadataAPIMapper.TABULAR_FILE_COLUMN_TYPE_NAME,
                                                                                               null,
                                                                                               repositoryHelper,
                                                                                               serviceName,
                                                                                               serverName);
                    columnCount ++;

                    schemaAttributeBuilder.setAnchors(userId, fileAssetGUID, methodName);

                    SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(columnQualifiedName + ":columnType",
                                                                                OpenMetadataAPIMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_GUID,
                                                                                OpenMetadataAPIMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME,
                                                                                repositoryHelper,
                                                                                serviceName,
                                                                                serverName);

                    schemaTypeBuilder.setDataType("String");

                    schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);
                    schemaAttributeBuilder.setEffectivityDates(effectiveFrom, effectiveTo);

                    schemaAttributeHandler.createNestedSchemaAttribute(userId,
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       schemaTypeGUID,
                                                                       schemaTypeGUIDParameterName,
                                                                       OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                       columnQualifiedName,
                                                                       qualifiedNameParameterName,
                                                                       schemaAttributeBuilder,
                                                                       effectiveFrom,
                                                                       effectiveTo,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
                }
            }
        }

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     fileAssetGUID,
                                     fileAssetGUIDParameterName,
                                     fullPath,
                                     pathParameterName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Update the properties of a DataFile asset description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param dataFileGUID guid of the file asset
     * @param isMergeUpdate should the new properties be merged with the existing properties or completely replace them?
     * @param fullPath unique path and file name for file
     * @param displayName short display name for file (defaults to the file name without the path)
     * @param versionIdentifier versionIdentifier property
     * @param description description of the file
     * @param createTime the time that the file was created
     * @param modifiedTime the time of the latest change to the file
     * @param encodingType the type of encoding used on the file
     * @param encodingLanguage the language used within the file
     * @param encodingDescription the description of the file
     * @param encodingProperties the properties used to drive the encoding
     * @param suppliedFileType the type of file override (default is to use the file extension)
     * @param additionalProperties additional properties from the user
     * @param extendedProperties any additional properties for the file type
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateFileInCatalog(String              userId,
                                    String              externalSourceGUID,
                                    String              externalSourceName,
                                    String              dataFileGUID,
                                    boolean             isMergeUpdate,
                                    String              fullPath,
                                    String              displayName,
                                    String              versionIdentifier,
                                    String              description,
                                    Date                createTime,
                                    Date                modifiedTime,
                                    String              encodingType,
                                    String              encodingLanguage,
                                    String              encodingDescription,
                                    Map<String, String> encodingProperties,
                                    String              suppliedFileType,
                                    Map<String, String> additionalProperties,
                                    Map<String, Object> extendedProperties,
                                    Date                effectiveFrom,
                                    Date                effectiveTo,
                                    boolean             forLineage,
                                    boolean             forDuplicateProcessing,
                                    Date                effectiveTime,
                                    String              methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String dataFileGUIDParameterName = "dataFileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFileGUIDParameterName, methodName);

        String qualifiedName = null;
        String fileType = suppliedFileType;

        if ((fullPath != null) && (fileType == null))
        {
            fileType = this.getFileType(fullPath);
        }
        if (! isMergeUpdate)
        {
            qualifiedName = this.createQualifiedName(OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME, fullPath, versionIdentifier);
        }

        Map<String, Object> assetExtendedProperties = this.getExtendedProperties(fullPath,
                                                                                 createTime,
                                                                                 modifiedTime,
                                                                                 encodingType,
                                                                                 encodingLanguage,
                                                                                 encodingDescription,
                                                                                 encodingProperties,
                                                                                 fileType,
                                                                                 extendedProperties);

        if (fullPath != null)
        {
            if (assetExtendedProperties != null)
            {
                assetExtendedProperties = new HashMap<>();
            }

            assetExtendedProperties.put(OpenMetadataAPIMapper.FILE_NAME_PROPERTY_NAME, this.getFileName(fullPath));
        }

        fileHandler.updateAsset(userId,
                                externalSourceGUID,
                                externalSourceName,
                                dataFileGUID,
                                dataFileGUIDParameterName,
                                qualifiedName,
                                displayName,
                                versionIdentifier,
                                description,
                                additionalProperties,
                                OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                assetExtendedProperties,
                                effectiveFrom,
                                effectiveTo,
                                isMergeUpdate,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime,
                                methodName);
    }



    /**
     * Update the properties of a DataFile asset description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param dataFileGUID guid of the file asset
     * @param isMergeUpdate should the new properties be merged with the existing properties or completely replace them?
     * @param fullPath unique path and file name for file
     * @param versionIdentifier version identifier for the file
     * @param displayName short name for file
     * @param description description of the file
     * @param createTime the time that the file was created
     * @param modifiedTime the time of the latest change to the file
     * @param encodingType the type of encoding used on the file
     * @param encodingLanguage the language used within the file
     * @param encodingDescription the description of the file
     * @param encodingProperties the properties used to drive the encoding
     * @param additionalProperties additional properties from the user
     * @param extendedProperties any additional properties for the file type
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateFolderInCatalog(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
                                      String              dataFileGUID,
                                      boolean             isMergeUpdate,
                                      String              fullPath,
                                      String              displayName,
                                      String              versionIdentifier,
                                      String              description,
                                      Date                createTime,
                                      Date                modifiedTime,
                                      String              encodingType,
                                      String              encodingLanguage,
                                      String              encodingDescription,
                                      Map<String, String> encodingProperties,
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties,
                                      Date                effectiveFrom,
                                      Date                effectiveTo,
                                      boolean             forLineage,
                                      boolean             forDuplicateProcessing,
                                      Date                effectiveTime,
                                      String              methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String dataFileGUIDParameterName = "dataFileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFileGUIDParameterName, methodName);

        String qualifiedName = null;

        if (! isMergeUpdate)
        {
            qualifiedName = this.createQualifiedName(OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME, fullPath, versionIdentifier);
        }

        Map<String, Object> assetExtendedProperties = this.getExtendedProperties(fullPath,
                                                                                 createTime,
                                                                                 modifiedTime,
                                                                                 encodingType,
                                                                                 encodingLanguage,
                                                                                 encodingDescription,
                                                                                 encodingProperties,
                                                                                 null,
                                                                                 extendedProperties);


        fileHandler.updateAsset(userId,
                                externalSourceGUID,
                                externalSourceName,
                                dataFileGUID,
                                dataFileGUIDParameterName,
                                qualifiedName,
                                displayName,
                                versionIdentifier,
                                description,
                                additionalProperties,
                                OpenMetadataAPIMapper.DATA_FOLDER_TYPE_GUID,
                                OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                assetExtendedProperties,
                                effectiveFrom,
                                effectiveTo,
                                isMergeUpdate,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime,
                                methodName);
    }




    /**
     * Archive a DataFile asset description.  This adds the Memento classification to the DataFile entity
     * and removes the attached connection (if any).
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param dataFileGUID guid of the file asset
     * @param dataFileGUIDParameterName parameter name supplying dataFileGUID
     * @param archiveDate date that the file was archived or discovered to have been archived.  Null means now.
     * @param archiveProcess name of archiving process
     * @param archiveProperties properties to help locate the archive copy
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void archiveFileInCatalog(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              dataFileGUID,
                                     String              dataFileGUIDParameterName,
                                     Date                archiveDate,
                                     String              archiveProcess,
                                     Map<String, String> archiveProperties,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFileGUIDParameterName, methodName);

        ReferenceableBuilder builder = new ReferenceableBuilder(repositoryHelper, serviceName, serverName);

        fileHandler.archiveBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            dataFileGUID,
                                            dataFileGUIDParameterName,
                                            OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                            builder.getMementoProperties(archiveDate,
                                                                         userId,
                                                                         archiveProcess,
                                                                         archiveProperties,
                                                                         methodName),
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        String connectionGUID = fileHandler.getAttachedElementGUID(userId,
                                                                   dataFileGUID,
                                                                   dataFileGUIDParameterName,
                                                                   OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                   0,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        if (connectionGUID != null)
        {
            final String connectionGUIDParameterName = "connectionGUID";

            connectionHandler.deleteBeanInRepository(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     connectionGUID,
                                                     connectionGUIDParameterName,
                                                     OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                     OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                     null,
                                                     null,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);
        }
    }




    /**
     * Archive a DataFolder asset description.  This adds the Memento classification to the DataFolder entity
     * and removes the attached connection (if any).
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param dataFolderGUID guid of the file asset
     * @param dataFolderGUIDParameterName parameter name supplying dataFolderGUID
     * @param archiveDate date that the file was archived or discovered to have been archived.  Null means now.
     * @param archiveProcess name of archiving process
     * @param archiveProperties properties to help locate the archive copy
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void archiveFolderInCatalog(String              userId,
                                       String              externalSourceGUID,
                                       String              externalSourceName,
                                       String              dataFolderGUID,
                                       String              dataFolderGUIDParameterName,
                                       Date                archiveDate,
                                       String              archiveProcess,
                                       Map<String, String> archiveProperties,
                                       boolean             forLineage,
                                       boolean             forDuplicateProcessing,
                                       Date                effectiveTime,
                                       String              methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFolderGUID, dataFolderGUIDParameterName, methodName);

        ReferenceableBuilder builder = new ReferenceableBuilder(repositoryHelper, serviceName, serverName);

        fileHandler.archiveBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            dataFolderGUID,
                                            dataFolderGUIDParameterName,
                                            OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                            builder.getMementoProperties(archiveDate,
                                                                         userId,
                                                                         archiveProcess,
                                                                         archiveProperties,
                                                                         methodName),
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        String connectionGUID = fileHandler.getAttachedElementGUID(userId,
                                                                   dataFolderGUID,
                                                                   dataFolderGUIDParameterName,
                                                                   OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                   0,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        if (connectionGUID != null)
        {
            final String connectionGUIDParameterName = "connectionGUID";

            connectionHandler.deleteBeanInRepository(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     connectionGUID,
                                                     connectionGUIDParameterName,
                                                     OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                     OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                     null,
                                                     null,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);
        }
    }


    /**
     * Remove a DataFile asset description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param dataFileGUID guid of the file asset
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteFileFromCatalog(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  dataFileGUID,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String dataFileGUIDParameterName = "dataFileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFileGUIDParameterName, methodName);

        fileHandler.deleteBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           dataFileGUID,
                                           dataFileGUIDParameterName,
                                           OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                           OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                           null,
                                           null,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }



    /**
     * Remove a DataFolder asset description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param dataFolderGUID guid of the file asset
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteFolderFromCatalog(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  dataFolderGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String dataFolderGUIDParameterName = "dataFolderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFolderGUID, dataFolderGUIDParameterName, methodName);

        fileHandler.deleteBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           dataFolderGUID,
                                           dataFolderGUIDParameterName,
                                           OpenMetadataAPIMapper.DATA_FOLDER_TYPE_GUID,
                                           OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                           null,
                                           null,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Retrieve a filesystem by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param softwareServerCapabilityGUID unique identifier used to locate the element
     * @param guidParameterName name of parameter providing the GUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return retrieved properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public  FILESYSTEM getFileSystemByGUID(String  userId,
                                           String  softwareServerCapabilityGUID,
                                           String  guidParameterName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return fileSystemHandler.getBeanFromRepository(userId,
                                                       softwareServerCapabilityGUID,
                                                       guidParameterName,
                                                       OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);
    }


    /**
     * Retrieve a filesystem by its unique name.
     *
     * @param userId calling user
     * @param uniqueName unique name for the file system
     * @param parameterName name of parameter providing the parameter name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return  properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FILESYSTEM getFileSystemByUniqueName(String  userId,
                                                String  uniqueName,
                                                String  parameterName,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return fileSystemHandler.getBeanByQualifiedName(userId,
                                                        OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_GUID,
                                                        OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                        uniqueName,
                                                        parameterName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
    }


    /**
     * Retrieve a list of unique identifiers for defined filesystems.
     *
     * @param userId calling user
     * @param startingFrom starting point in the list
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return List of unique identifiers for the retrieved entities
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> getFileSystems(String  userId,
                                       int     startingFrom,
                                       int     pageSize,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return fileSystemHandler.getSoftwareCapabilityGUIDsByClassification(userId,
                                                                            OpenMetadataAPIMapper.FILE_SYSTEM_CLASSIFICATION_TYPE_NAME,
                                                                            startingFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);
    }


    /**
     * Retrieve a Folder asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return Folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FOLDER getFolderByGUID(String  userId,
                                  String  folderGUID,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String  guidName = "folderGUID";

        return folderHandler.getBeanFromRepository(userId,
                                                   folderGUID,
                                                   guidName,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
    }


    /**
     * Retrieve return the unique identifier of a folder located by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName path name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return string guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String getFolderGUIDByPathName(String  userId,
                                           String  pathName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String  pathNameParameterName = "pathName";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME);

        List<EntityDetail> entities = fileHandler.getEntitiesByValue(userId,
                                                                     pathName,
                                                                     pathNameParameterName,
                                                                     OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                                     specificMatchPropertyNames,
                                                                     true,
                                                                     null,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                     0,
                                                                     0,
                                                                     effectiveTime,
                                                                     methodName);
        if (entities == null)
        {
            return null;
        }
        else if (entities.size() == 1)
        {
            return entities.get(0).getGUID();
        }

        throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                                                                                pathName,
                                                                                                                entities.toString(),
                                                                                                                methodName,
                                                                                                                pathNameParameterName,
                                                                                                                serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName path name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return Folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FOLDER getFolderByPathName(String  userId,
                                      String  pathName,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String  pathNameParameterName = "pathName";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME);

        List<FOLDER> folders = folderHandler.getBeansByValue(userId,
                                                             pathName,
                                                             pathNameParameterName,
                                                             OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                                             OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                             specificMatchPropertyNames,
                                                             true,
                                                             null,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                             0,
                                                             0,
                                                             effectiveTime,
                                                             methodName);
        if (folders == null)
        {
            return null;
        }
        else if (folders.size() == 1)
        {
            return folders.get(0);
        }

        throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                                                                                pathName,
                                                                                                                folders.toString(),
                                                                                                                methodName,
                                                                                                                pathNameParameterName,
                                                                                                                serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Retrieve a folder by its fully qualified path name.  In theory there should be none or one asset returned.
     * However, in complex environments, duplicates are possible.
     *
     * @param userId calling user
     * @param pathName path name
     * @param startingFrom starting point in the list
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<FOLDER> findFoldersByPathName(String  userId,
                                              String  pathName,
                                              int     startingFrom,
                                              int     pageSize,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String  pathNameParameterName = "pathName";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME);

        return folderHandler.getBeansByValue(userId,
                                             pathName,
                                             pathNameParameterName,
                                             OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                             OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                             specificMatchPropertyNames,
                                             false,
                                             null,
                                             null,
                                             forLineage,
                                             forDuplicateProcessing,
                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                             startingFrom,
                                             pageSize,
                                             effectiveTime,
                                             methodName);
    }


    /**
     * Retrieve folder by the supplied wildcard name.  The wildcard is specified using regular expressions (RegEx).
     *
     * @param userId calling user
     * @param name wildcard  name
     * @param startingFrom starting point in the list
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<FOLDER> findFoldersByName(String  userId,
                                          String  name,
                                          int     startingFrom,
                                          int     pageSize,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String  nameName = "name";

        return folderHandler.findAssetsByName(userId,
                                              OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                              OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                              name,
                                              nameName,
                                              startingFrom,
                                              pageSize,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);
    }


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system
     * @param fileSystemParameterName name of parameter providing fileSystemGUID
     * @param startingFrom starting point in the list
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of folder unique identifiers (null means no nested folders)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  getTopLevelFolders(String  userId,
                                            String  fileSystemGUID,
                                            String  fileSystemParameterName,
                                            int     startingFrom,
                                            int     pageSize,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return folderHandler.getAttachedElementGUIDs(userId,
                                                     fileSystemGUID,
                                                     fileSystemParameterName,
                                                     OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                     OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     startingFrom,
                                                     pageSize,
                                                     effectiveTime,
                                                     methodName);
    }

    /**
     * Return the list of folders nested inside a folder.
     *
     * @param userId calling user
     * @param parentFolderGUID unique identifier of the parent folder
     * @param parentFolderParameterName name of parameter providing parentFolderGUID
     * @param startingFrom starting point in the list
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of folder unique identifiers (null means no nested folders)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  getNestedFolders(String  userId,
                                          String  parentFolderGUID,
                                          String  parentFolderParameterName,
                                          int     startingFrom,
                                          int     pageSize,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return folderHandler.getAttachedElementGUIDs(userId,
                                                     parentFolderGUID,
                                                     parentFolderParameterName,
                                                     OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                     OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_GUID,
                                                     OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_NAME,
                                                     OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     startingFrom,
                                                     pageSize,
                                                     effectiveTime,
                                                     methodName);
    }


    /**
     * Get the files inside a folder - both those that are nested and those that are linked.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the anchor folder
     * @param folderGUIDParameterName name of parameter providing folderGUID
     * @param startingFrom starting point in the list
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of file asset unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> getFolderFiles(String  userId,
                                       String  folderGUID,
                                       String  folderGUIDParameterName,
                                       int     startingFrom,
                                       int     pageSize,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return fileHandler.getAttachedElementGUIDs(userId,
                                                   folderGUID,
                                                   folderGUIDParameterName,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_GUID,
                                                   OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_NAME,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   startingFrom,
                                                   pageSize,
                                                   effectiveTime,
                                                   methodName);
    }


    /**
     * Retrieve a Folder asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param dataFileGUID unique identifier used to locate the folder
     * @param dataFileGUIDParameterName name of parameter providing dataFileGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return file properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FILE getDataFileByGUID(String  userId,
                                  String  dataFileGUID,
                                  String  dataFileGUIDParameterName,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return fileHandler.getBeanFromRepository(userId,
                                                 dataFileGUID,
                                                 dataFileGUIDParameterName,
                                                 OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }


    /**
     * Retrieve a data file by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName full path name of data file used in its qualified name
     * @param pathNameParameterName name of parameter providing the pathName
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return file properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FILE getDataFileByPathName(String  userId,
                                      String  pathName,
                                      String  pathNameParameterName,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME);

        List<FILE> files = fileHandler.getBeansByValue(userId,
                                                       pathName,
                                                       pathNameParameterName,
                                                       OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                                       OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                       specificMatchPropertyNames,
                                                       true,
                                                       null,
                                                       null,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                       0,
                                                       0,
                                                       effectiveTime,
                                                       methodName);
        if (files == null)
        {
            return null;
        }
        else if (files.size() == 1)
        {
            return files.get(0);
        }

        throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                                                                pathName,
                                                                                                                files.toString(),
                                                                                                                methodName,
                                                                                                                pathNameParameterName,
                                                                                                                serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Find data file by a full or partial path name. The wildcard is specified using regular expressions (RegEx) and the method matches on the
     * pathName property.
     *
     * @param userId calling user
     * @param pathName path name
     * @param pathNameParameterName name of parameter providing the pathName
     * @param startingFrom place to start returning results from
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching file properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<FILE> findDataFilesByPathName(String  userId,
                                              String  pathName,
                                              String  pathNameParameterName,
                                              int     startingFrom,
                                              int     pageSize,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME);

        return fileHandler.getBeansByValue(userId,
                                           pathName,
                                           pathNameParameterName,
                                           OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                           OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                           specificMatchPropertyNames,
                                           false,
                                           null,
                                           null,
                                           forLineage,
                                           forDuplicateProcessing,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           startingFrom,
                                           pageSize,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Retrieve data files by the supplied wildcard name.  The wildcard is specified using regular expressions (RegEx) and the method matches on the
     * qualifiedName, name and pathName property.
     *
     * @param userId calling user
     * @param name wildcard name
     * @param nameParameterName name of parameter providing the
     * @param startingFrom place to start returning results from
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching file properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<FILE> findDataFilesByName(String  userId,
                                          String  name,
                                          String  nameParameterName,
                                          int     startingFrom,
                                          int     pageSize,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME);

        return fileHandler.getBeansByValue(userId,
                                           name,
                                           nameParameterName,
                                           OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                           OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                           specificMatchPropertyNames,
                                           true,
                                           null,
                                           null,
                                           forLineage,
                                           forDuplicateProcessing,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           startingFrom,
                                           pageSize,
                                           effectiveTime,
                                           methodName);
    }
}
