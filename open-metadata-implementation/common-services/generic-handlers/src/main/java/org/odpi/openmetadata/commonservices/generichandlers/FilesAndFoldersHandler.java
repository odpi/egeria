/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.adapters.connectors.datastore.avrofile.AvroFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
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
    private String                          serviceName;
    private String                          serverName;
    private String                          localServerUserId;
    private OMRSRepositoryHelper            repositoryHelper;
    private RepositoryHandler               repositoryHandler;
    private InvalidParameterHandler         invalidParameterHandler;

    private SoftwareServerCapabilityHandler<FILESYSTEM> fileSystemHandler;
    private AssetHandler<FOLDER>                        folderHandler;
    private AssetHandler<FILE>                          fileHandler;

    private ConnectionHandler<OpenMetadataAPIDummyBean>                                connectionHandler;
    private ConnectorTypeHandler<OpenMetadataAPIDummyBean>                             connectorTypeHandler;
    private EndpointHandler<OpenMetadataAPIDummyBean>                                  endpointHandler;
    private SchemaAttributeHandler<OpenMetadataAPIDummyBean, OpenMetadataAPIDummyBean> schemaAttributeHandler;

    private final static String folderDivider = "/";
    private final static String fileSystemDivider = "://";
    private final static String fileTypeDivider = "\\.";

    private final static String defaultAvroFileType = "avro";
    private final static String defaultCSVFileType  = "csv";

    private final static BasicFileStoreProvider basicFileStoreProvider = new BasicFileStoreProvider();
    private final static DataFolderProvider     dataFolderProvider     = new DataFolderProvider();
    private final static AvroFileStoreProvider  avroFileStoreProvider  = new AvroFileStoreProvider();
    private final static CSVFileStoreProvider   csvFileStoreProvider   = new CSVFileStoreProvider();



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

        this.fileSystemHandler       = new SoftwareServerCapabilityHandler<>(fileSystemConverter,
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

        OpenMetadataAPIDummyBeanConverter<OpenMetadataAPIDummyBean> dummyConverter =
                new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName);

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

        this.connectorTypeHandler = new ConnectorTypeHandler<>(dummyConverter,
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

        this.endpointHandler = new EndpointHandler<>(dummyConverter,
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
                                                  methodName);
    }


    /**
     * Create the requested asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param pathName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param typeName type of file system
     * @param methodName calling method
     *
     * @return unique identifier for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String createFolder(String     userId,
                                String     externalSourceGUID,
                                String     externalSourceName,
                                String     pathName,
                                String     displayName,
                                String     description,
                                String     typeName,
                                String     methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        String folderAssetTypeName = OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME;
        if (typeName != null)
        {
            folderAssetTypeName = typeName;
        }

        String folderAssetTypeGUID = invalidParameterHandler.validateTypeName(folderAssetTypeName,
                                                                              OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                                              serviceName,
                                                                              methodName,
                                                                              repositoryHelper);

        return folderHandler.createAssetInRepository(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     pathName,
                                                     displayName,
                                                     description,
                                                     null,
                                                     null,
                                                     0,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     folderAssetTypeGUID,
                                                     folderAssetTypeName,
                                                     null,
                                                     methodName);
    }


    /**
     * Create the requested asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param fileType file extension name
     * @param pathName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param typeName type of file system
     * @param initialExtendedProperties extended properties for a specific file type
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
                                   String              pathName,
                                   String              displayName,
                                   String              description,
                                   String              typeName,
                                   Map<String, Object> initialExtendedProperties,
                                   String              methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        Map<String, Object> extendedProperties = new HashMap<>();
        if (initialExtendedProperties != null)
        {
            extendedProperties = new HashMap<>(initialExtendedProperties);
        }

        extendedProperties.put(OpenMetadataAPIMapper.FILE_TYPE_PROPERTY_NAME, fileType);

        String fileAssetTypeName = OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
        if (typeName != null)
        {
            fileAssetTypeName = typeName;
        }

        String fileAssetTypeGUID = invalidParameterHandler.validateTypeName(fileAssetTypeName,
                                                                            OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                            serviceName,
                                                                            methodName,
                                                                            repositoryHelper);

        return fileHandler.createAssetInRepository(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   pathName,
                                                   displayName,
                                                   description,
                                                   null,
                                                   null,
                                                   0,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   fileAssetTypeGUID,
                                                   fileAssetTypeName,
                                                   extendedProperties,
                                                   methodName);
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectToGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
     * @param folderName name of the leaf folder
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
                                         null,
                                         OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                         methodName);

        if (connectToGUID != null)
        {
            if (repositoryHandler.isEntityATypeOf(userId,
                                                  connectToGUID,
                                                  connectToParameterName,
                                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                  methodName))
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   connectToGUID,
                                                   connectToParameterName,
                                                   OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                   folderGUID,
                                                   folderParameterName,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                   OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                   null,
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
                                                   OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_GUID,
                                                   OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_NAME,
                                                   null,
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectToGUID root object to connect the folder to
     * @param fileSystemName name of the root of the file system (can be null)
     * @param folderNames list of the folder names
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

                String currentFolderGUID = this.getFolderGUIDByPathName(userId, pathName, methodName);

                if (currentFolderGUID == null)
                {
                    String folderGUID = createFolderInCatalog(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              nextConnectToGUID,
                                                              pathName,
                                                              folderName,
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectToGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
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
                                              methodName);
    }


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param fileSystemGUIDParameterName parameter name for the fileSystemGUID
     * @param folderGUID unique identifier of the folder in the catalog
     * @param folderGUIDParameterName parameter name for the folderGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void attachFolderToFileSystem(String userId,
                                         String externalSourceGUID,
                                         String externalSourceName,
                                         String fileSystemGUID,
                                         String fileSystemGUIDParameterName,
                                         String folderGUID,
                                         String folderGUIDParameterName,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        folderHandler.linkElementToElement(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           fileSystemGUID,
                                           fileSystemGUIDParameterName,
                                           OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                           folderGUID,
                                           folderGUIDParameterName,
                                           OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                           OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                           OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                           null,
                                           methodName);
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param fileSystemGUIDParameterName parameter name for the fileSystemGUID
     * @param folderGUID unique identifier of the folder in the catalog
     * @param folderGUIDParameterName parameter name for the folderGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void detachFolderFromFileSystem(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String fileSystemGUID,
                                           String fileSystemGUIDParameterName,
                                           String folderGUID,
                                           String folderGUIDParameterName,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        folderHandler.unlinkElementFromElement(userId,
                                               false,
                                               externalSourceGUID,
                                               externalSourceName,
                                               fileSystemGUID,
                                               fileSystemGUIDParameterName,
                                               OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                               folderGUID,
                                               folderGUIDParameterName,
                                               OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                               OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                               OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                               OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                               methodName);
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param folderGUID unique identifier of the folder
     * @param folderGUIDParameterName parameter providing folderGUID
     * @param fileGUID unique identifier of the file
     * @param fileGUIDParameterName  parameter providing fileGUID
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
                                         OpenMetadataAPIMapper.LINKED_FILE_TYPE_GUID,
                                         OpenMetadataAPIMapper.LINKED_FILE_TYPE_NAME,
                                         null,
                                         methodName);
    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveDataFileInCatalog to record
     * the fact that the physical file has moved.  Use attachDataFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param folderGUID unique identifier of the folder
     * @param folderGUIDParameterName parameter providing folderGUID
     * @param fileGUID unique identifier of the file
     * @param fileGUIDParameterName  parameter providing fileGUID
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
                                               OpenMetadataAPIMapper.LINKED_FILE_TYPE_GUID,
                                               OpenMetadataAPIMapper.LINKED_FILE_TYPE_NAME,
                                               methodName);
    }


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param newParentFolder unique identifier of the new parent folder
     * @param newParentFolderGUIDParameterName parameter providing newParentFolder
     * @param fileGUID unique identifier of the file
     * @param fileGUIDParameterName  parameter providing fileGUID
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
                                                                                     methodName);
        String existingFilePathName = fileHandler.getBeanStringPropertyFromRepository(userId,
                                                                                      newParentFolder,
                                                                                      newParentFolderGUIDParameterName,
                                                                                      OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                      methodName);
        String fileName = this.getFileName(existingFilePathName);
        String fullPathName = newFolderPathName + "/" + fileName;

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
                                              OpenMetadataAPIMapper.LINKED_FILE_TYPE_GUID,
                                              OpenMetadataAPIMapper.LINKED_FILE_TYPE_NAME,
                                              null,
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
                                                                         methodName);
                    }
                }
            }
        }
    }


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param newParentFolderGUID new parent folder
     * @param newParentFolderGUIDParameterName name of parameter supplying newParentFolderGUID
     * @param movingFolderGUID unique identifier of the data folder to move
     * @param movingFolderGUIDParameterName name of parameter supplying movingFolderGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  moveDataFolderInCatalog(String   userId,
                                         String   externalSourceGUID,
                                         String   externalSourceName,
                                         String   newParentFolderGUID,
                                         String   newParentFolderGUIDParameterName,
                                         String   movingFolderGUID,
                                         String   movingFolderGUIDParameterName,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(newParentFolderGUID, newParentFolderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(movingFolderGUID, movingFolderGUIDParameterName, methodName);

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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param fileAssetGUID unique identifier of file asset
     * @param fileAssetParameterName parameter providing the fileAssetGUID
     * @param pathName pathname of the file
     * @param pathNameParameterName parameter providing the pathName
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<String> addFileAssetPath(String userId,
                                          String externalSourceGUID,
                                          String externalSourceName,
                                          String fileAssetGUID,
                                          String fileAssetParameterName,
                                          String pathName,
                                          String pathNameParameterName,
                                          String methodName) throws InvalidParameterException,
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
                                                                          OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                          fileSystemName,
                                                                          pathNameParameterName,
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
             * If there are parent folders then need a nested files relationship.  If the root is the file system
             * then the relationship is server asset use.
             */
            if (fileParentGUID.equals(fileSystemGUID))
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   fileParentGUID,
                                                   pathNameParameterName,
                                                   OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                   fileAssetGUID,
                                                   fileAssetParameterName,
                                                   OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                   OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                   OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                   null,
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
                                                   OpenMetadataAPIMapper.NESTED_FILE_TYPE_GUID,
                                                   OpenMetadataAPIMapper.NESTED_FILE_TYPE_NAME,
                                                   null,
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param displayName display name for the folder in the catalog
     * @param description description of the folder in the catalog
     * @param pathName pathname of the file
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
                                                  String   displayName,
                                                  String   description,
                                                  String   pathName,
                                                  String   methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String pathParameterName = "pathName";
        final String fileAssetParameterName = "fileAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameterName, methodName);

        String fileType = this.getFileType(pathName);

        String fileAssetGUID = this.createFileAsset(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    fileType,
                                                    pathName,
                                                    displayName,
                                                    description,
                                                    OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                    null,
                                                    methodName);

        this.addDataFileConnection(userId,
                                   externalSourceGUID,
                                   externalSourceName,
                                   fileAssetGUID,
                                   fileAssetParameterName,
                                   pathName,
                                   methodName);

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     fileAssetGUID,
                                     fileAssetParameterName,
                                     pathName,
                                     pathParameterName,
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param pathName pathname of the file
     * @param displayName display name for the folder in the catalog
     * @param description description of the folder in the catalog
     * @param createTime time that the folder was created
     * @param modifiedTime the time of the latest change to the file
     * @param encodingType the type of encoding used on the file
     * @param encodingLanguage the language used within the file
     * @param encodingDescription the description of the file
     * @param encodingProperties the properties used to drive the encoding
     * @param additionalProperties additional properties for the data folder
     * @param connectorClassName name of the class for the connector's provider
     * @param typeName type name of folder
     * @param extendedProperties extended properties supplied by the caller
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
                                                    String              displayName,
                                                    String              description,
                                                    Date                createTime,
                                                    Date                modifiedTime,
                                                    String              encodingType,
                                                    String              encodingLanguage,
                                                    String              encodingDescription,
                                                    Map<String, String> encodingProperties,
                                                    Map<String, String> additionalProperties,
                                                    String              connectorClassName,
                                                    String              typeName,
                                                    Map<String, Object> extendedProperties,
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


        String folderAssetTypeName = typeName;
        String folderAssetTypeGUID = invalidParameterHandler.validateTypeName(folderAssetTypeName,
                                                                              OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                                                              serviceName,
                                                                              methodName,
                                                                              repositoryHelper);

        String folderAssetGUID = fileHandler.createAssetInRepository(userId,
                                                                     externalSourceGUID,
                                                                     externalSourceName,
                                                                     pathName,
                                                                     displayName,
                                                                     description,
                                                                     null,
                                                                     null,
                                                                     0,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     additionalProperties,
                                                                     folderAssetTypeGUID,
                                                                     folderAssetTypeName,
                                                                     assetExtendedProperties,
                                                                     methodName);

        this.addDataFolderConnection(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     folderAssetGUID,
                                     folderAssetParameterName,
                                     pathName,
                                     connectorClassName,
                                     methodName);

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     folderAssetGUID,
                                     folderAssetParameterName,
                                     pathName,
                                     pathParameterName,
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param displayName display name for the folder in the catalog
     * @param description description of the folder in the catalog
     * @param pathName pathname of the file
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
                                                    String              displayName,
                                                    String              description,
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
                                                   displayName,
                                                   description,
                                                   OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                                   methodName);

        this.addDataFolderConnection(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     folderAssetGUID,
                                     folderAssetParameterName,
                                     pathName,
                                     dataFolderProvider.getClass().getName(),
                                     methodName);

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     folderAssetGUID,
                                     folderAssetParameterName,
                                     pathName,
                                     pathParameterName,
                                     methodName);
    }


    /**
     * Add a simple asset description linked to a connection object for a file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param qualifiedName unique name for the file - typically path and file name
     * @param displayName short display name for file (defaults to the file name without the path)
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
     * @param connectorClassName qualified class name for the connector provider for this type of file
     * @param typeName name of the type (default is File)
     * @param extendedProperties any additional properties for the file type
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
                                          String              displayName,
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
                                          String              connectorClassName,
                                          String              typeName,
                                          Map<String, Object> extendedProperties,
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

        String fileAssetTypeGUID = invalidParameterHandler.validateTypeName(fileAssetTypeName,
                                                                            OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                            serviceName,
                                                                            methodName,
                                                                            repositoryHelper);

        String fileAssetGUID = fileHandler.createAssetInRepository(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   fullPath,
                                                                   displayName,
                                                                   description,
                                                                   null,
                                                                   null,
                                                                   0,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   additionalProperties,
                                                                   fileAssetTypeGUID,
                                                                   fileAssetTypeName,
                                                                   assetExtendedProperties,
                                                                   methodName);

        if (fileAssetGUID != null)
        {
            this.addConnectionForFile(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      fileAssetGUID,
                                      fileAssetParameterName,
                                      fileType,
                                      fullPath,
                                      connectorClassName,
                                      methodName);


            return this.addFileAssetPath(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         fileAssetGUID,
                                         fileAssetParameterName,
                                         fullPath,
                                         pathParameterName,
                                         methodName);
        }

        return null;
    }


    /**
     * Create a new file asset based on an existing asset but with the supplied path name, display name and description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param templateGUID unique identifier of the asset description to copy
     * @param fullPath unique path and file name for file
     * @param displayName short display name for file (defaults to the file name without the path)
     * @param description description of the file
     * @param methodName calling method
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  addFileToCatalogFromTemplate(String userId,
                                                      String externalSourceGUID,
                                                      String externalSourceName,
                                                      String templateGUID,
                                                      String fullPath,
                                                      String displayName,
                                                      String description,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String qualifiedNameParameterName = "fullPath";
        final String fileAssetParameterName     = "fileAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fullPath, qualifiedNameParameterName, methodName);

        String fileAssetGUID = fileHandler.addAssetFromTemplate(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                templateGUID,
                                                                templateGUIDParameterName,
                                                                OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                fullPath,
                                                                qualifiedNameParameterName,
                                                                displayName,
                                                                description,
                                                                fullPath,
                                                                methodName);

        if (fileAssetGUID != null)
        {
            return this.addFileAssetPath(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         fileAssetGUID,
                                         fileAssetParameterName,
                                         fullPath,
                                         qualifiedNameParameterName,
                                         methodName);
        }

        return null;
    }


    /**
     * Create a new file asset based on an existing asset but with the supplied path name, display name and description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param templateGUID unique identifier of the asset description to copy
     * @param pathName unique path and file name for file
     * @param displayName short display name for file (defaults to the file name without the path)
     * @param description description of the file
     * @param methodName calling method
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  addFolderToCatalogFromTemplate(String userId,
                                                        String externalSourceGUID,
                                                        String externalSourceName,
                                                        String templateGUID,
                                                        String pathName,
                                                        String displayName,
                                                        String description,
                                                        String methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String qualifiedNameParameterName = "pathName";
        final String fileAssetParameterName     = "folderAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(pathName, qualifiedNameParameterName, methodName);

        String folderAssetGUID = fileHandler.addAssetFromTemplate(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  templateGUID,
                                                                  templateGUIDParameterName,
                                                                  OpenMetadataAPIMapper.DATA_FOLDER_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                                                  pathName,
                                                                  qualifiedNameParameterName,
                                                                  displayName,
                                                                  description,
                                                                  pathName,
                                                                  methodName);

        if (folderAssetGUID != null)
        {
            return this.addFileAssetPath(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         folderAssetGUID,
                                         fileAssetParameterName,
                                         pathName,
                                         qualifiedNameParameterName,
                                         methodName);
        }

        return null;
    }


    /**
     * Add a simple asset description linked to a connection object for an Avro file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param fullPath full path of the file - used to access the file through the connector
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  addAvroFileToCatalog(String userId,
                                              String externalSourceGUID,
                                              String externalSourceName,
                                              String displayName,
                                              String description,
                                              String fullPath,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String pathParameterName = "fullPath";
        final String fileAssetParameterName = "fileAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(fullPath, pathParameterName, methodName);

        String fileType = this.getFileType(fullPath);

        if (fileType == null)
        {
            fileType = defaultAvroFileType;
        }

        String fileAssetGUID = this.createFileAsset(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    fileType,
                                                    fullPath,
                                                    displayName,
                                                    description,
                                                    OpenMetadataAPIMapper.AVRO_FILE_TYPE_NAME,
                                                    null,
                                                    methodName);

        this.addAvroFileConnection(userId,
                                   externalSourceGUID,
                                   externalSourceName,
                                   fileAssetGUID,
                                   fileAssetParameterName,
                                   fullPath,
                                   methodName);

        return this.addFileAssetPath(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     fileAssetGUID,
                                     fileAssetParameterName,
                                     fullPath,
                                     pathParameterName,
                                     methodName);
    }


    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param fullPath full path of the file - used to access the file through the connector
     * @param columnHeaders does the first line of the file contain the column names. If not pass the list of column headers.
     * @param delimiterCharacter what is the delimiter character - null for default of comma
     * @param quoteCharacter what is the character to group a field that contains delimiter characters
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
                                             String       displayName,
                                             String       description,
                                             String       fullPath,
                                             List<String> columnHeaders,
                                             Character    delimiterCharacter,
                                             Character    quoteCharacter,
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

        String fileAssetGUID = this.createFileAsset(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    fileType,
                                                    fullPath,
                                                    displayName,
                                                    description,
                                                    OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME,
                                                    extendedProperties,
                                                    methodName);

        this.addCSVFileConnection(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  fileAssetGUID,
                                  fileAssetGUIDParameterName,
                                  fullPath,
                                  columnHeaders,
                                  delimiterCharacter,
                                  quoteCharacter,
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
                                                                                               OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_GUID,
                                                                                               OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_NAME,
                                                                                               null,
                                                                                               repositoryHelper,
                                                                                               serviceName,
                                                                                               serverName);
                    columnCount ++;

                    schemaAttributeBuilder.setAnchors(userId, fileAssetGUID, methodName);

                    SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(columnQualifiedName + ":columnType",
                                                                                OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_TYPE_GUID,
                                                                                OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_TYPE_NAME,
                                                                                repositoryHelper,
                                                                                serviceName,
                                                                                serverName);

                    schemaTypeBuilder.setDataType("String");

                    schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);

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
                                     methodName);
    }


    /**
     * Return a connection object to add to the asset.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID         unique identifier of the asset
     * @param assetGUIDParameterName parameter name supplying the asset guid
     * @param fileType file type extension
     * @param fullPath full path of file name
     * @param connectorClassName supplied connector
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void addConnectionForFile(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String assetGUID,
                                      String assetGUIDParameterName,
                                      String fileType,
                                      String fullPath,
                                      String connectorClassName,
                                      String methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        String providerClass   = connectorClassName;

        if (providerClass == null)
        {
            if (defaultCSVFileType.equals(fileType))
            {
                providerClass = csvFileStoreProvider.getClass().getName();
            }
            else if (defaultAvroFileType.equals(fileType))
            {
                providerClass = avroFileStoreProvider.getClass().getName();
            }
        }

        String endpointName   = "File.Endpoint." + fullPath;
        String connectionName = fullPath + " File Connection";

        this.addAssetConnection(userId,
                                externalSourceGUID,
                                externalSourceName,
                                assetGUID,
                                assetGUIDParameterName,
                                fullPath,
                                connectionName,
                                null,
                                providerClass,
                                endpointName,
                                methodName);
    }


    /**
     * This method creates a connection for a File.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID         unique identifier of the asset
     * @param assetGUIDParameterName parameter name supplying the asset guid
     * @param fileName name of the file to connect to
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void  addDataFileConnection(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String assetGUID,
                                        String assetGUIDParameterName,
                                        String fileName,
                                        String methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        String endpointName   = "FileStore.Endpoint." + fileName;
        String connectionName = fileName + " File Store Connection";

        this.addAssetConnection(userId,
                                externalSourceGUID,
                                externalSourceName,
                                assetGUID,
                                assetGUIDParameterName,
                                fileName,
                                connectionName,
                                null,
                                basicFileStoreProvider.getClass().getName(),
                                endpointName,
                                methodName);
    }


    /**
     * This method creates a connection for a CSV File.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID         unique identifier of the asset
     * @param assetGUIDParameterName parameter name supplying the asset guid
     * @param fileName name of the file to connect to
     * @param columnHeaders boolean parameter defining if the column headers are included in the file
     * @param delimiterCharacter character used between the columns
     * @param quoteCharacter character used to group text that includes the delimiter character
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private  void addCSVFileConnection(String       userId,
                                       String       externalSourceGUID,
                                       String       externalSourceName,
                                       String       assetGUID,
                                       String       assetGUIDParameterName,
                                       String       fileName,
                                       List<String> columnHeaders,
                                       Character    delimiterCharacter,
                                       Character    quoteCharacter,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        String endpointName   = "CSVFileStore.Endpoint." + fileName;
        String connectionName = fileName + " CSV File Store Connection";

        Map<String, Object>  configurationProperties = new HashMap<>();

        if (delimiterCharacter != null)
        {
            configurationProperties.put(CSVFileStoreProvider.delimiterCharacterProperty, delimiterCharacter);
        }

        if (quoteCharacter != null)
        {
            configurationProperties.put(CSVFileStoreProvider.quoteCharacterProperty, quoteCharacter);
        }

        if (columnHeaders != null)
        {
            configurationProperties.put(CSVFileStoreProvider.columnNamesProperty, columnHeaders);
        }

        if (configurationProperties.isEmpty())
        {
            configurationProperties = null;
        }

        this.addAssetConnection(userId,
                                externalSourceGUID,
                                externalSourceName,
                                assetGUID,
                                assetGUIDParameterName,
                                fileName,
                                connectionName,
                                configurationProperties,
                                csvFileStoreProvider.getClass().getName(),
                                endpointName,
                                methodName);
    }


    /**
     * This method creates a connection for an Avro File.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID         unique identifier of the asset
     * @param assetGUIDParameterName parameter name supplying the asset guid
     * @param fileName name of the file to connect to
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void addAvroFileConnection(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String assetGUID,
                                       String assetGUIDParameterName,
                                       String fileName,
                                       String methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        String endpointName   = "AvroFileStore.Endpoint." + fileName;
        String connectionName = fileName + " Avro File Store Connection";

        this.addAssetConnection(userId,
                                externalSourceGUID,
                                externalSourceName,
                                assetGUID,
                                assetGUIDParameterName,
                                fileName,
                                connectionName,
                                null,
                                avroFileStoreProvider.getClass().getName(),
                                endpointName,
                                methodName);
    }


    /**
     * This method creates a connection for a data folder.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID         unique identifier of the asset
     * @param assetGUIDParameterName parameter name supplying the asset guid
     * @param folderName name of the file to connect to
     * @param connectorProviderName name of the connector provider for the connection
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private   void addDataFolderConnection(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String assetGUID,
                                           String assetGUIDParameterName,
                                           String folderName,
                                           String connectorProviderName,
                                           String methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        String endpointName   = "DataFolder.Endpoint." + folderName;
        String connectionName = folderName + " Data Folder Connection";

        this.addAssetConnection(userId,
                                externalSourceGUID,
                                externalSourceName,
                                assetGUID,
                                assetGUIDParameterName,
                                folderName,
                                connectionName,
                                null,
                                connectorProviderName,
                                endpointName,
                                methodName);
    }


    /**
     * Return the unique identifier of a connection for the supplied asset.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID         unique identifier of the asset
     * @param assetGUIDParameterName parameter name supplying the asset guid
     * @param fullPathName fully qualified path name of the asset
     * @param connectionName fully qualified name of the connection
     * @param configurationProperties configuration properties for the connection
     * @param connectorProviderClassName Java class name for the connector provider
     * @param endpointName fully qualified name of the endpoint
     * @param methodName calling method
     *
     * @return unique identifier of connector type object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void addAssetConnection(String               userId,
                                    String               externalSourceGUID,
                                    String               externalSourceName,
                                    String               assetGUID,
                                    String               assetGUIDParameterName,
                                    String               fullPathName,
                                    String               connectionName,
                                    Map<String, Object>  configurationProperties,
                                    String               connectorProviderClassName,
                                    String               endpointName,
                                    String               methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String endpointGUIDParameterName      = "endpointGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        String endpointGUID = getEndpoint(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          assetGUID,
                                          endpointName,
                                          fullPathName,
                                          methodName);

        String connectorTypeGUID = getConnectorType(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    connectorProviderClassName,
                                                    methodName);

        connectionHandler.createConnection(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           null,
                                           connectionName,
                                           connectionName,
                                           null,
                                           null,
                                           null,
                                           configurationProperties,
                                           null,
                                           null,
                                           null,
                                           connectorTypeGUID,
                                           connectorTypeGUIDParameterName,
                                           endpointGUID,
                                           endpointGUIDParameterName,
                                           methodName);
    }



    /**
     * Return a connector type for the supplied connector provider class name.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectorProviderClassName Java class name for the connector provider
     * @param methodName calling method
     *
     * @return unique identifier of connector type object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String getConnectorType(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String connectorProviderClassName,
                                    String methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        ConnectorType connectorType;

        if (connectorProviderClassName == null)
        {
            connectorType = basicFileStoreProvider.getConnectorType();
        }
        else if (basicFileStoreProvider.getClass().getName().equals(connectorProviderClassName))
        {
            connectorType = basicFileStoreProvider.getConnectorType();
        }
        else if (dataFolderProvider.getClass().getName().equals(connectorProviderClassName))
        {
            connectorType = dataFolderProvider.getConnectorType();
        }
        else if (avroFileStoreProvider.getClass().getName().equals(connectorProviderClassName))
        {
            connectorType = avroFileStoreProvider.getConnectorType();
        }
        else if (csvFileStoreProvider.getClass().getName().equals(connectorProviderClassName))
        {
            connectorType =  csvFileStoreProvider.getConnectorType();
        }
        else
        {
            connectorType = new ConnectorType();

            connectorType.setConnectorProviderClassName(connectorProviderClassName);
        }

        return connectorTypeHandler.getConnectorTypeForConnection(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  null,
                                                                  connectorType.getQualifiedName(),
                                                                  connectorType.getDisplayName(),
                                                                  connectorType.getDescription(),
                                                                  connectorProviderClassName,
                                                                  connectorType.getRecognizedAdditionalProperties(),
                                                                  connectorType.getRecognizedSecuredProperties(),
                                                                  connectorType.getRecognizedConfigurationProperties(),
                                                                  connectorType.getAdditionalProperties(),
                                                                  methodName);
    }


    /**
     * Create a new endpoint for the connection.  Endpoints can be shared amongst connections if they
     * point to the same network address.
     *
     * @param endpointName name of the endpoint.
     * @param fileName name of the file
     * @return new endpoint
     */
    private String getEndpoint(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String assetGUID,
                               String endpointName,
                               String fileName,
                               String methodName) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String endpointDescription = "Access information to connect to the actual asset: ";

        return endpointHandler.getEndpointForConnection(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        assetGUID,
                                                        endpointName,
                                                        endpointName,
                                                        endpointDescription + fileName,
                                                        fileName,
                                                        null,
                                                        null,
                                                        null,
                                                        methodName);
    }


    /**
     * Update the properties of a DataFile asset description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param dataFileGUID guid of the file asset
     * @param isMergeUpdate should the new properties be merged with the existing properties or completely replace them?
     * @param fullPath unique path and file name for file
     * @param displayName short display name for file (defaults to the file name without the path)
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
                                    String              methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String dataFileGUIDParameterName = "dataFileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFileGUIDParameterName, methodName);

        Map<String, Object> assetExtendedProperties = this.getExtendedProperties(fullPath,
                                                                                 createTime,
                                                                                 modifiedTime,
                                                                                 encodingType,
                                                                                 encodingLanguage,
                                                                                 encodingDescription,
                                                                                 encodingProperties,
                                                                                 suppliedFileType,
                                                                                 extendedProperties);


        fileHandler.updateAsset(userId,
                                externalSourceGUID,
                                externalSourceName,
                                dataFileGUID,
                                dataFileGUIDParameterName,
                                fullPath,
                                displayName,
                                description,
                                additionalProperties,
                                OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                assetExtendedProperties,
                                isMergeUpdate,
                                methodName);
    }



    /**
     * Update the properties of a DataFile asset description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param dataFileGUID guid of the file asset
     * @param isMergeUpdate should the new properties be merged with the existing properties or completely replace them?
     * @param fullPath unique path and file name for file
     * @param displayName short display name for file (defaults to the file name without the path)
     * @param description description of the file
     * @param createTime the time that the file was created
     * @param modifiedTime the time of the latest change to the file
     * @param encodingType the type of encoding used on the file
     * @param encodingLanguage the language used within the file
     * @param encodingDescription the description of the file
     * @param encodingProperties the properties used to drive the encoding
     * @param additionalProperties additional properties from the user
     * @param extendedProperties any additional properties for the file type
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
                                      String              description,
                                      Date                createTime,
                                      Date                modifiedTime,
                                      String              encodingType,
                                      String              encodingLanguage,
                                      String              encodingDescription,
                                      Map<String, String> encodingProperties,
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties,
                                      String              methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String dataFolderGUIDParameterName = "dataFolderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFolderGUIDParameterName, methodName);

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
                                dataFolderGUIDParameterName,
                                fullPath,
                                displayName,
                                description,
                                additionalProperties,
                                OpenMetadataAPIMapper.DATA_FOLDER_TYPE_GUID,
                                OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                assetExtendedProperties,
                                isMergeUpdate,
                                methodName);
    }




    /**
     * Archive a DataFile asset description.  This adds the Memento classification to the DataFile entity
     * and removes the attached connection (if any).
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param dataFileGUID guid of the file asset
     * @param dataFileGUIDParameterName parameter name supplying dataFileGUID
     * @param archiveDate date that the file was archived or discovered to have been archived.  Null means now.
     * @param archiveProcess name of archiving process
     * @param archiveProperties properties to help locate the archive copy
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
                                            methodName);

        String connectionGUID = fileHandler.getAttachedElementGUID(userId,
                                                                   dataFileGUID,
                                                                   dataFileGUIDParameterName,
                                                                   OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                   0,
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
                                                     methodName);
        }
    }




    /**
     * Archive a DataFolder asset description.  This adds the Memento classification to the DataFolder entity
     * and removes the attached connection (if any).
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param dataFolderGUID guid of the file asset
     * @param dataFolderGUIDParameterName parameter name supplying dataFolderGUID
     * @param archiveDate date that the file was archived or discovered to have been archived.  Null means now.
     * @param archiveProcess name of archiving process
     * @param archiveProperties properties to help locate the archive copy
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
                                            methodName);

        String connectionGUID = fileHandler.getAttachedElementGUID(userId,
                                                                   dataFolderGUID,
                                                                   dataFolderGUIDParameterName,
                                                                   OpenMetadataAPIMapper.DATA_FOLDER_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                   0,
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
                                                     methodName);
        }
    }


    /**
     * Remove a DataFile asset description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param dataFileGUID guid of the file asset
     * @param fullPathname unique path and file name for file
     * @param methodName calling method
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteFileFromCatalog(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
                                      String              dataFileGUID,
                                      String              fullPathname,
                                      String              methodName) throws InvalidParameterException,
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
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           fullPathname,
                                           methodName);
    }



    /**
     * Remove a DataFolder asset description.
     *
     * @param userId calling user (assumed to be the owner)
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param dataFolderGUID guid of the file asset
     * @param fullPathname unique path and file name for file
     * @param methodName calling method
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteFolderFromCatalog(String              userId,
                                        String              externalSourceGUID,
                                        String              externalSourceName,
                                        String              dataFolderGUID,
                                        String              fullPathname,
                                        String              methodName) throws InvalidParameterException,
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
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           fullPathname,
                                           methodName);
    }


    /**
     * Retrieve a filesystem by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param softwareServerCapabilityGUID unique identifier used to locate the element
     * @param guidParameterName name of parameter providing the GUID
     * @param methodName calling method
     *
     * @return retrieved properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public  FILESYSTEM getFileSystemByGUID(String   userId,
                                           String   softwareServerCapabilityGUID,
                                           String   guidParameterName,
                                           String   methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return fileSystemHandler.getBeanFromRepository(userId,
                                                       softwareServerCapabilityGUID,
                                                       guidParameterName,
                                                       OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                       methodName);
    }


    /**
     * Retrieve a filesystem by its unique name.
     *
     * @param userId calling user
     * @param uniqueName unique name for the file system
     * @param parameterName name of parameter providing the parameter name
     * @param methodName calling method
     *
     * @return  properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FILESYSTEM getFileSystemByUniqueName(String   userId,
                                                String   uniqueName,
                                                String   parameterName,
                                                String   methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return fileSystemHandler.getBeanByQualifiedName(userId,
                                                        OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                        OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                        uniqueName,
                                                        parameterName,
                                                        methodName);
    }


    /**
     * Retrieve a list of unique identifiers for defined filesystems.
     *
     * @param userId calling user
     * @param startingFrom starting point in the list
     * @param pageSize maximum number of results
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
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return fileSystemHandler.getSoftwareServerCapabilityGUIDsByClassification(userId,
                                                                                  OpenMetadataAPIMapper.FILE_SYSTEM_CLASSIFICATION_TYPE_NAME,
                                                                                  startingFrom,
                                                                                  pageSize,
                                                                                  methodName);
    }


    /**
     * Retrieve a Folder asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     * @param methodName calling method
     *
     * @return Folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FOLDER getFolderByGUID(String   userId,
                                  String   folderGUID,
                                  String   methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String  guidName = "folderGUID";

        return folderHandler.getBeanFromRepository(userId,
                                                   folderGUID,
                                                   guidName,
                                                   OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                   methodName);
    }


    /**
     * Retrieve return the unique identifier of a folder located by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName path name
     * @param methodName calling method
     *
     * @return string guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String getFolderGUIDByPathName(String  userId,
                                          String  pathName,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String  nameName = "pathName";

        return folderHandler.getBeanGUIDByQualifiedName(userId,
                                                        OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                                        OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                        pathName,
                                                        nameName,
                                                        methodName);
    }


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName path name
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
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String  nameName = "pathName";

        return folderHandler.getBeanByQualifiedName(userId,
                                                    OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                                    OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                    pathName,
                                                    nameName,
                                                    methodName);
    }


    /**
     * Retrieve a folder by its fully qualified path name.  In theory there should be none or one asset returned.
     * However in complex environments, duplicates are possible.
     *
     * @param userId calling user
     * @param pathName path name
     * @param methodName calling method
     *
     * @return list of matching folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<FOLDER> findFolderByPathName(String  userId,
                                             String  pathName,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String  nameName = "pathName";

        return folderHandler.findBeansByQualifiedName(userId,
                                                      OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID,
                                                      OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                      pathName,
                                                      nameName,
                                                      0,
                                                      invalidParameterHandler.getMaxPagingSize(),
                                                      methodName);
    }


    /**
     * Retrieve folder by the supplied wildcard name.  The wildcard is specified using regular expressions (RegEx).
     *
     * @param userId calling user
     * @param name wildcard  name
     * @param methodName calling method
     *
     * @return list of matching folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<FOLDER> findFolderByName(String  userId,
                                         String  name,
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
                                              0,
                                              invalidParameterHandler.getMaxPagingSize(),
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
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return folderHandler.getAttachedElementGUIDs(userId,
                                                     fileSystemGUID,
                                                     fileSystemParameterName,
                                                     OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                     OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME,
                                                     startingFrom,
                                                     pageSize,
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
                                                     startingFrom,
                                                     pageSize,
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
                                                   startingFrom,
                                                   pageSize,
                                                   methodName);
    }


    /**
     * Retrieve a Folder asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param dataFileGUID unique identifier used to locate the folder
     * @param dataFileGUIDParameterName name of parameter providing dataFileGUID
     * @param methodName calling method
     *
     * @return file properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FILE getDataFileByGUID(String   userId,
                                  String   dataFileGUID,
                                  String   dataFileGUIDParameterName,
                                  String   methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        return fileHandler.getBeanFromRepository(userId,
                                                 dataFileGUID,
                                                 dataFileGUIDParameterName,
                                                 OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                 methodName);
    }


    /**
     * Retrieve a data file by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName full path name of data file used in its qualified name
     * @param pathNameParameterName name of parameter providing the pathName
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
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return fileHandler.getBeanByQualifiedName(userId,
                                                  OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                                  OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                  pathName,
                                                  pathNameParameterName,
                                                  methodName);
    }


    /**
     * Retrieve a data file by its fully qualified path name.  In theory there should be none or one asset returned.
     * However in complex environments, duplicates are possible.
     *
     * @param userId calling user
     * @param pathName path name
     * @param pathNameParameterName name of parameter providing the pathName
     * @param startingFrom place to start returning results from
     * @param pageSize maximum number of results
     * @param methodName calling method
     *
     * @return list of matching file properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<FILE> findDataFileByPathName(String  userId,
                                             String  pathName,
                                             String  pathNameParameterName,
                                             int     startingFrom,
                                             int     pageSize,
                                             String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return fileHandler.findBeansByQualifiedName(userId,
                                                    OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                                    OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                                    pathName,
                                                    pathNameParameterName,
                                                    startingFrom,
                                                    pageSize,
                                                    methodName);
    }


    /**
     * Retrieve data files by the supplied wildcard name.  The wildcard is specified using regular expressions (RegEx).
     *
     * @param userId calling user
     * @param name wildcard name
     * @param nameParameterName name of parameter providing the
     * @param startingFrom place to start returning results from
     * @param pageSize maximum number of results
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
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return fileHandler.findAssetsByName(userId,
                                            OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID,
                                            OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME,
                                            name,
                                            nameParameterName,
                                            startingFrom,
                                            pageSize,
                                            methodName);
    }
}
