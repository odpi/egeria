/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;


import org.odpi.openmetadata.adapters.connectors.datastore.avrofile.AvroFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.AssetBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
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
 * FileSystemHandler provides the support for managing catalog entries about files and folders.
 */
public class FileSystemHandler
{
    private String                          serviceName;
    private String                          serverName;
    private List<String>                    supportedZones;
    private OMRSRepositoryHelper            repositoryHelper;
    private RepositoryHandler               repositoryHandler;
    private InvalidParameterHandler         invalidParameterHandler;
    private AssetHandler                    assetHandler;
    private SchemaTypeHandler               schemaTypeHandler;
    private SoftwareServerCapabilityHandler softwareServerCapabilityHandler;

    private final static String folderDivider = "/";
    private final static String fileSystemDivider = "://";
    private final static String fileTypeDivider = "\\.";

    private final static String defaultAvroFileType = "avro";
    private final static String defaultCSVFileType = "csv";


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName  name of this service
     * @param serverName name of the local server
     * @param supportedZones list of supported zones
     * @param assetHandler handler for assets
     * @param schemaTypeHandler handler for schema elements
     * @param softwareServerCapabilityHandler handler for file systems
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public FileSystemHandler(String                          serviceName,
                             String                          serverName,
                             InvalidParameterHandler         invalidParameterHandler,
                             RepositoryHandler               repositoryHandler,
                             OMRSRepositoryHelper            repositoryHelper,
                             List<String>                    supportedZones,
                             AssetHandler                    assetHandler,
                             SchemaTypeHandler               schemaTypeHandler,
                             SoftwareServerCapabilityHandler softwareServerCapabilityHandler)
    {
        this.serviceName                     = serviceName;
        this.serverName                      = serverName;
        this.supportedZones                  = supportedZones;
        this.assetHandler                    = assetHandler;
        this.schemaTypeHandler               = schemaTypeHandler;
        this.softwareServerCapabilityHandler = softwareServerCapabilityHandler;
        this.invalidParameterHandler         = invalidParameterHandler;
        this.repositoryHandler               = repositoryHandler;
        this.repositoryHelper                = repositoryHelper;
    }


    /**
     * Return the URL header (if any) from a path name.
     *
     * @param pathName path name of a file
     * @return URL or null
     */
    private String getFileSystemName(String  pathName)
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
                if (this.getFileSystemName(pathName) != null)
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
     * Create the requested asset.
     *
     * @param userId calling user
     * @param pathName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param typeName type of file system
     * @param connection connection for the asset
     * @param methodName calling method
     *
     * @return unique identifier for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String createFolder(String                userId,
                                String                pathName,
                                String                displayName,
                                String                description,
                                String                typeName,
                                Connection            connection,
                                String                methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        Asset asset = assetHandler.createEmptyAsset(typeName, methodName);

        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setQualifiedName(pathName);

        return assetHandler.addAsset(userId,
                                     asset,
                                     connection,
                                     methodName);
    }


    /**
     * Create the requested asset.
     *
     * @param userId calling user
     * @param pathName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param typeName type of file system
     * @param connection connection for the asset
     * @param methodName calling method
     *
     * @return unique identifier for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String createFileAsset(String                userId,
                                   String                fileType,
                                   String                pathName,
                                   String                displayName,
                                   String                description,
                                   String                typeName,
                                   Connection            connection,
                                   String                methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        Asset asset = assetHandler.createEmptyAsset(typeName, methodName);

        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setQualifiedName(pathName);

        Map<String, Object> extendedProperties = new HashMap<>();
        extendedProperties.put(AssetMapper.FILE_TYPE_PROPERTY_NAME, fileType);

        asset.setExtendedProperties(extendedProperties);

        return assetHandler.addAsset(userId,
                                     asset,
                                     connection,
                                     methodName);
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param anchorGUID root object to connect the folder to
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
                                         String   anchorGUID,
                                         String   pathName,
                                         String   folderName,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String guidParameterName = "anchorGUID";

        String folderGUID = createFolder(userId,
                                         pathName,
                                         folderName,
                                         null,
                                         AssetMapper.FILE_FOLDER_TYPE_NAME,
                                         null,
                                         methodName);

        if (anchorGUID != null)
        {
            if (repositoryHandler.isEntityATypeOf(userId,
                                                  anchorGUID,
                                                  guidParameterName,
                                                  SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                  methodName))
            {
                repositoryHandler.createRelationship(userId,
                                                     AssetMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     anchorGUID,
                                                     folderGUID,
                                                     null,
                                                     methodName);
            }
            else
            {
                repositoryHandler.createRelationship(userId,
                                                     AssetMapper.FOLDER_HIERARCHY_TYPE_GUID,
                                                     anchorGUID,
                                                     folderGUID,
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
     * @param anchorGUID root object to connect the folder to
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
                                                        String         anchorGUID,
                                                        String         fileSystemName,
                                                        List<String>   folderNames,
                                                        String         methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        List<String>  folderGUIDs = new ArrayList<>();

        if ((folderNames != null) && (! folderNames.isEmpty()))
        {
            String pathName = null;
            String folderName = null;
            String nextAnchorGUID = anchorGUID;

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

                Asset currentFolder = this.getFolderByPathName(userId, pathName, methodName);

                if (currentFolder == null)
                {
                    String folderGUID = createFolderInCatalog(userId,
                                                              nextAnchorGUID,
                                                              pathName,
                                                              folderName,
                                                              methodName);

                    folderGUIDs.add(folderGUID);
                    nextAnchorGUID = folderGUID;
                }
                else
                {
                    folderGUIDs.add(currentFolder.getGUID());
                    nextAnchorGUID = currentFolder.getGUID();
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
     * @param anchorGUID root object to connect the folder to
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
                                                       String   anchorGUID,
                                                       String   pathName,
                                                       String   methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return createFolderStructureInCatalog(userId,
                                              anchorGUID,
                                              this.getFileSystemName(pathName),
                                              this.getFolderNames(pathName),
                                              methodName);
    }


    /**
     * Creates a new folder asset for each element in the pathName.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
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
                                                       String   pathName,
                                                       String   methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return createFolderStructureInCatalog(userId,
                                              null,
                                              this.getFileSystemName(pathName),
                                              this.getFolderNames(pathName),
                                              methodName);
    }


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param folderGUID unique identifier of the folder in the catalog
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void attachFolderToFileSystem(String   userId,
                                         String   fileSystemGUID,
                                         String   folderGUID,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        /*
         * Check that the file system is known and that the folder is both known and visible through the
         * supported zones.
         */
        SoftwareServerCapability fileSystem = softwareServerCapabilityHandler.getSoftwareServerCapabilityByGUID(userId, fileSystemGUID, methodName);
        Asset                    folder     = assetHandler.getValidatedVisibleAsset(userId,
                                                                                    supportedZones,
                                                                                    folderGUID,
                                                                                    serviceName,
                                                                                    methodName);

        /*
         * Continue with operation if all is ok.
         */
        if ((fileSystem != null) && (folder != null))
        {
            repositoryHandler.createRelationship(userId,
                                                 AssetMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                 fileSystemGUID,
                                                 folderGUID,
                                                 null,
                                                 methodName);
        }
        else if (fileSystem == null)
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        fileSystemGUID,
                                                        SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
        else /* folder == null */
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        folderGUID,
                                                        AssetMapper.FILE_FOLDER_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param folderGUID unique identifier of the folder in the catalog
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void detachFolderFromFileSystem(String   userId,
                                           String   fileSystemGUID,
                                           String   folderGUID,
                                           String   methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        /*
         * Check that the file system is known and that the folder is both known and visible through the
         * supported zones.
         */
        SoftwareServerCapability fileSystem = softwareServerCapabilityHandler.getSoftwareServerCapabilityByGUID(userId, fileSystemGUID, methodName);
        Asset                    folder = assetHandler.getValidatedVisibleAsset(userId,
                                                                                supportedZones,
                                                                                folderGUID,
                                                                                serviceName,
                                                                                methodName);

        /*
         * Continue with operation if all is ok.
         */
        if ((fileSystem != null) && (folder != null))
        {
            repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                                AssetMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                AssetMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                fileSystemGUID,
                                                                SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                folderGUID,
                                                                methodName);
        }
        else if (fileSystem == null)
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        fileSystemGUID,
                                                        SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
        else /* folder == null */
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        folderGUID,
                                                        AssetMapper.FILE_FOLDER_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  attachDataFileAssetToFolder(String   userId,
                                             String   folderGUID,
                                             String   fileGUID,
                                             String   methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        /*
         * Check that the data file and folder are both known and visible through the supported zones.
         */
        Asset      folder = assetHandler.getValidatedVisibleAsset(userId,
                                                                  supportedZones,
                                                                  folderGUID,
                                                                  serviceName,
                                                                  methodName);
        Asset      dataFile = assetHandler.getValidatedVisibleAsset(userId,
                                                                    supportedZones,
                                                                    fileGUID,
                                                                    serviceName,
                                                                    methodName);

        /*
         * Continue with operation if all is ok.
         */
        if ((dataFile != null) && (folder != null))
        {
            repositoryHandler.createRelationship(userId,
                                                 AssetMapper.LINKED_FILE_TYPE_GUID,
                                                 folderGUID,
                                                 fileGUID,
                                                 null,
                                                 methodName);
        }
        else if (dataFile == null)
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        fileGUID,
                                                        AssetMapper.DATA_FILE_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
        else /* folder == null */
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        folderGUID,
                                                        AssetMapper.FILE_FOLDER_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveDataFileInCatalog to record
     * the fact that the physical file has moved.  Use attachDataFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  detachDataFileAssetFromFolder(String   userId,
                                               String   folderGUID,
                                               String   fileGUID,
                                               String   methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        /*
         * Check that the data file and folder are both known and visible through the supported zones.
         */
        Asset      folder = assetHandler.getValidatedVisibleAsset(userId,
                                                                  supportedZones,
                                                                  folderGUID,
                                                                  serviceName,
                                                                  methodName);
        Asset      dataFile = assetHandler.getValidatedVisibleAsset(userId,
                                                                    supportedZones,
                                                                    fileGUID,
                                                                    serviceName,
                                                                    methodName);
        /*
         * Continue with operation if all is ok.
         */
        if ((dataFile != null) && (folder != null))
        {
            repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                                AssetMapper.LINKED_FILE_TYPE_GUID,
                                                                AssetMapper.LINKED_FILE_TYPE_NAME,
                                                                folderGUID,
                                                                AssetMapper.FILE_FOLDER_TYPE_NAME,
                                                                folderGUID,
                                                                methodName);
        }
        else if (dataFile == null)
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        fileGUID,
                                                        AssetMapper.DATA_FILE_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
        else /* folder == null */
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        folderGUID,
                                                        AssetMapper.FILE_FOLDER_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
     *
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param fileGUID unique identifier of the file to move
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  moveDataFileInCatalog(String   userId,
                                       String   folderGUID,
                                       String   fileGUID,
                                       String   methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName   = "fileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameterName, methodName);

        Asset folder = assetHandler.getValidatedVisibleAsset(userId, supportedZones, folderGUID, serviceName, methodName);
        Asset file   = assetHandler.getValidatedVisibleAsset(userId, supportedZones, fileGUID, serviceName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);
    }


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
     *
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param dataFolderGUID unique identifier of the data folder to move
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  moveDataFolderInCatalog(String   userId,
                                         String   folderGUID,
                                         String   dataFolderGUID,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String folderGUIDParameterName = "folderGUID";
        final String dataFolderGUIDParameterName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataFolderGUID, dataFolderGUIDParameterName, methodName);

        Asset folder = assetHandler.getValidatedVisibleAsset(userId, supportedZones, folderGUID, serviceName, methodName);
        Asset dataFolder   = assetHandler.getValidatedVisibleAsset(userId, supportedZones, dataFolderGUID, serviceName, methodName);

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
     * @param fileAssetGUID unique identifier of file asset
     * @param pathName pathname of the file
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<String> addFileAssetPath(String   userId,
                                          String   fileAssetGUID,
                                          String   pathName,
                                          String   methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        String      fileSystemName = this.getFileSystemName(pathName);
        String      fileSystemGUID = null;

        List<String> assetGUIDList = new ArrayList<>();

        if (fileSystemName != null)
        {
            /*
             * The file's pathname includes the root file system name.  A SoftWareServerCapability entity
             * is created for the file system if it does not exist already.
             */
            SoftwareServerCapability  fileSystem = softwareServerCapabilityHandler.getSoftwareServerCapabilityByUniqueName(userId, fileSystemName, methodName);

            if (fileSystem == null)
            {
                fileSystemGUID = softwareServerCapabilityHandler.createFileSystem(userId,
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
            else
            {
                fileSystemGUID = fileSystem.getGUID();
            }
        }

        /*
         * Now set up the folder names.
         */
        List<String> folderNames = this.getFolderNames(pathName);
        List<String> folderGUIDs = null;
        String       fileAnchorGUID;

        if (folderNames == null)
        {
            fileAnchorGUID = fileSystemGUID;
        }
        else
        {
            folderGUIDs = this.createFolderStructureInCatalog(userId,
                                                              fileSystemGUID,
                                                              fileSystemName,
                                                              folderNames,
                                                              methodName);

            if ((folderGUIDs != null) && (!folderGUIDs.isEmpty()))
            {
                fileAnchorGUID = folderGUIDs.get(folderGUIDs.size() - 1);
            }
            else
            {
                fileAnchorGUID = fileSystemGUID;
            }
        }

        /*
         * Now link in the file to the chain
         */
        if ((fileAssetGUID != null) && (fileAnchorGUID != null))
        {
            /*
             * The assets for the parent part have been created.  Now connect the file asset to its anchor.
             * If there are parent folders then need a nested files relationship.  If the anchor is the file system
             * then the relationship is server asset use.
             */
            if (folderNames != null)
            {
                repositoryHandler.createRelationship(userId,
                                                     AssetMapper.NESTED_FILE_TYPE_GUID,
                                                     fileAnchorGUID,
                                                     fileAssetGUID,
                                                     null,
                                                     methodName);
            }
            else
            {
                repositoryHandler.createRelationship(userId,
                                                     AssetMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     fileAnchorGUID,
                                                     fileAssetGUID,
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
                                                  String   displayName,
                                                  String   description,
                                                  String   pathName,
                                                  String   methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String pathParameterName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameterName, methodName);

        String fileType = this.getFileType(pathName);

        String fileAssetGUID = this.createFileAsset(userId,
                                                    fileType,
                                                    pathName,
                                                    displayName,
                                                    description,
                                                    AssetMapper.DATA_FILE_TYPE_NAME,
                                                    this.getDataFileConnection(pathName),
                                                    methodName);

        return this.addFileAssetPath(userId,
                                     fileAssetGUID,
                                     pathName,
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
    public List<String> addDataFolderAssetToCatalog(String   userId,
                                                    String   displayName,
                                                    String   description,
                                                    String   pathName,
                                                    String   methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String pathParameterName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameterName, methodName);

        String fileAssetGUID =  this.createFolder(userId,
                                                  pathName,
                                                  displayName,
                                                  description,
                                                  AssetMapper.DATA_FOLDER_TYPE_NAME,
                                                  this.getDataFolderConnection(pathName),
                                                  methodName);

        return this.addFileAssetPath(userId,
                                     fileAssetGUID,
                                     pathName,
                                     methodName);
    }


    /**
     * Add a simple asset description linked to a connection object for an Avro file.
     *
     * @param userId calling user (assumed to be the owner)
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
                                              String displayName,
                                              String description,
                                              String fullPath,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String pathParameterName = "fullPath";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(fullPath, pathParameterName, methodName);

        String fileType = this.getFileType(fullPath);

        if (fileType == null)
        {
            fileType = defaultAvroFileType;
        }

        String fileAssetGUID = this.createFileAsset(userId,
                                                    fileType,
                                                    fullPath,
                                                    displayName,
                                                    description,
                                                    AssetMapper.AVRO_FILE_TYPE_NAME,
                                                    this.getAvroFileConnection(fullPath),
                                                    methodName);

        return this.addFileAssetPath(userId,
                                     fileAssetGUID,
                                     fullPath,
                                     methodName);
    }


    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param userId calling user (assumed to be the owner)
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(fullPath, pathParameterName, methodName);

        String fileType = this.getFileType(fullPath);

        if (fileType == null)
        {
            fileType = defaultCSVFileType;
        }

        Asset asset = assetHandler.createEmptyAsset(AssetMapper.CSV_FILE_TYPE_NAME, methodName);

        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setQualifiedName(fullPath);

        SchemaType            schemaType       = null;
        List<SchemaAttribute> schemaAttributes = null;


        if (columnHeaders != null)
        {
            schemaType  = this.getTabularSchemaType(schemaTypeHandler,
                                                    asset.getQualifiedName(),
                                                    asset.getDisplayName(),
                                                    userId,
                                                    "CSVFile",
                                                    columnHeaders);

            schemaAttributes = this.getTabularSchemaColumns(schemaTypeHandler,
                                                            schemaType.getQualifiedName(),
                                                            columnHeaders);
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
        extendedProperties.put(AssetMapper.DELIMITER_CHARACTER_PROPERTY_NAME, delimiterCharacter.toString());
        extendedProperties.put(AssetMapper.QUOTE_CHARACTER_PROPERTY_NAME, quoteCharacter.toString());
        extendedProperties.put(AssetMapper.FILE_TYPE_PROPERTY_NAME, fileType);
        asset.setExtendedProperties(extendedProperties);

        String fileAssetGUID = assetHandler.addAsset(userId,
                                                     asset,
                                                     schemaType,
                                                     schemaAttributes,
                                                     getCSVFileConnection(fullPath,
                                                                          columnHeaders,
                                                                          delimiterCharacter,
                                                                          quoteCharacter),
                                                     methodName);

        return this.addFileAssetPath(userId,
                                     fileAssetGUID,
                                     fullPath,
                                     methodName);
    }


    /**
     * Turn the list of column headers into a SchemaType object.  The assumption is that all of the columns contain
     * strings.  Later analysis may update this.
     *
     * @param schemaTypeHandler handler for managing schema elements in the metadata repositories
     * @param anchorQualifiedName unique name for the object that this schema is connected to
     * @param anchorDisplayName human-readable name for the object that this schema is connected to
     * @param author userId of author
     * @param encodingStandard internal encoding
     * @param columnHeaders  list of column headers.
     *
     * @return schema type object
     */
    private SchemaType getTabularSchemaType(SchemaTypeHandler schemaTypeHandler,
                                            String            anchorQualifiedName,
                                            String            anchorDisplayName,
                                            String            author,
                                            String            encodingStandard,
                                            List<String>      columnHeaders)
    {
        ComplexSchemaType    tableSchemaType = schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                                                                                           SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME);

        if (columnHeaders != null)
        {
            tableSchemaType.setAttributeCount(columnHeaders.size());
        }

        tableSchemaType.setQualifiedName(anchorQualifiedName + ":TabularSchema");
        tableSchemaType.setDisplayName(anchorDisplayName + " Tabular Schema");
        tableSchemaType.setAuthor(author);
        tableSchemaType.setVersionNumber("1.0");
        tableSchemaType.setEncodingStandard(encodingStandard);

        return tableSchemaType;
    }


    /**
     * Turn the list of column headers into a SchemaType object.  The assumption is that all of the columns contain
     * strings.  Later analysis may update this.
     *
     * @param schemaTypeHandler handler for managing schema elements in the metadata repositories
     * @param parentSchemaQualifiedName name of the linked schema's qualified name
     * @param columnHeaders   list of column names.
     *
     * @return list of schema attribute objects
     */
    private List<SchemaAttribute> getTabularSchemaColumns(SchemaTypeHandler schemaTypeHandler,
                                                          String            parentSchemaQualifiedName,
                                                          List<String>      columnHeaders)
    {
        List<SchemaAttribute>    tableColumns = new ArrayList<>();

        if (columnHeaders != null)
        {
            int positionCount = 0;
            for (String  columnName : columnHeaders)
            {
                if (columnName != null)
                {
                    SchemaAttribute schemaAttribute = schemaTypeHandler.getEmptySchemaAttribute();

                    schemaAttribute.setQualifiedName(parentSchemaQualifiedName + ":Column:" + columnName);
                    schemaAttribute.setAttributeName(columnName);
                    schemaAttribute.setMinCardinality(1);
                    schemaAttribute.setMaxCardinality(1);
                    schemaAttribute.setElementPosition(positionCount);
                    tableColumns.add(schemaAttribute);
                    positionCount++;
                }
            }
        }

        if (tableColumns.isEmpty())
        {
            return null;
        }
        else
        {
            return tableColumns;
        }
    }


    /**
     * This method creates a connection for a File.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param fileName name of the file to connect to
     * @return connection object
     */
    public Connection getDataFileConnection(String            fileName)
    {
        final BasicFileStoreProvider provider = new BasicFileStoreProvider();

        String endpointName    = "FileStore.Endpoint." + fileName;

        final String connectionDescription = "FileStore connection.";

        String connectionName = fileName + " File Store Connection";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setGUID(UUID.randomUUID().toString());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription + fileName);
        connection.setEndpoint(getEndpoint(endpointName, fileName));
        connection.setConnectorType(provider.getConnectorType());

        return connection;
    }


    /**
     * This method creates a connection for a CSV File.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param fileName name of the file to connect to
     * @param columnHeaders boolean parameter defining if the column headers are included in the file
     * @param delimiterCharacter character used between the columns
     * @param quoteCharacter character used to group text that includes the delimiter character
     * @return connection object
     */
    public Connection getCSVFileConnection(String            fileName,
                                            List<String>      columnHeaders,
                                            Character         delimiterCharacter,
                                            Character         quoteCharacter)
    {
        final CSVFileStoreProvider provider = new CSVFileStoreProvider();

        String endpointName    = "CSVFileStore.Endpoint." + fileName;

        final String connectionDescription = "CSVFileStore connection.";

        String connectionName = fileName + " CSV File Store Connection";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(getEndpoint(endpointName, fileName));
        connection.setConnectorType(provider.getConnectorType());

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

        if (! configurationProperties.isEmpty())
        {
            connection.setConfigurationProperties(configurationProperties);
        }

        return connection;
    }


    /**
     * This method creates a connection for an Avro File.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param fileName name of the file to connect to
     * @return connection object
     */
    public Connection getAvroFileConnection(String            fileName)
    {
        final AvroFileStoreProvider provider = new AvroFileStoreProvider();

        String endpointName    = "AvroFileStore.Endpoint." + fileName;

        final String connectionDescription = "AvroFileStore connection.";

        String connectionName = fileName + " Avro File Store Connection";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(getEndpoint(endpointName, fileName));
        connection.setConnectorType(provider.getConnectorType());

        return connection;
    }


    /**
     * This method creates a connection for a data folder.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param folderName name of the file to connect to
     * @return connection object
     */
    public Connection getDataFolderConnection(String folderName)
    {
        final DataFolderProvider provider = new DataFolderProvider();

        String endpointName    = "DataFolder.Endpoint." + folderName;

        final String connectionDescription = "DataFolder connection.";

        String connectionName = folderName + " Data Folder Connection";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(getEndpoint(endpointName, folderName));
        connection.setConnectorType(provider.getConnectorType());

        return connection;
    }


    /**
     * Create a new endpoint for the connection.
     *
     * @param endpointName name of the endpoint.
     * @param fileName name of the file
     * @return new endpoint
     */
    private Endpoint  getEndpoint(String   endpointName,
                                  String   fileName)
    {
        final String endpointDescription = "Access information to connect to the actual asset: ";
        Endpoint endpoint = new Endpoint();

        endpoint.setType(Endpoint.getEndpointType());
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription + fileName);
        endpoint.setAddress(fileName);

        return endpoint;
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
    public Asset getFolderByGUID(String   userId,
                                 String   folderGUID,
                                 String   methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String  guidName = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, guidName, methodName);

        EntityDetail  entity = repositoryHandler.getEntityByGUID(userId,
                                                                 folderGUID,
                                                                 guidName,
                                                                 AssetMapper.FILE_FOLDER_TYPE_NAME,
                                                                 methodName);

        if (entity != null)
        {
            AssetConverter converter = new AssetConverter(entity,
                                                          null,
                                                          repositoryHelper,
                                                          serviceName);

            return converter.getAssetBean();
        }

        return null;
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
    public Asset getFolderByPathName(String  userId,
                                      String  pathName,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String  nameName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, nameName, methodName);

        AssetBuilder builder = new AssetBuilder(pathName,
                                                null,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        EntityDetail  entity = repositoryHandler.getUniqueEntityByName(userId,
                                                                       pathName,
                                                                       nameName,
                                                                       builder.getQualifiedNameInstanceProperties(methodName),
                                                                       AssetMapper.FILE_FOLDER_TYPE_GUID,
                                                                       AssetMapper.FILE_FOLDER_TYPE_NAME,
                                                                       methodName);

        if (entity != null)
        {
            AssetConverter converter = new AssetConverter(entity,
                                                          null,
                                                          repositoryHelper,
                                                          serviceName);

            return converter.getAssetBean();
        }

        return null;
    }


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param userId calling user
     * @param anchorGUID unique identifier of the anchor folder or file system
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param methodName calling method
     *
     * @return list of folder unique identifiers (null means no nested folders)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String>  getNestedFolders(String  userId,
                                          String  anchorGUID,
                                          int     startingFrom,
                                          int     maxPageSize,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String guidParameterName = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maxPageSize, methodName);

        List<RelatedAsset> relatedAssets;

        if (repositoryHandler.isEntityATypeOf(userId,
                                              anchorGUID,
                                              guidParameterName,
                                              AssetMapper.FILE_FOLDER_TYPE_NAME,
                                              methodName))
        {
            relatedAssets = assetHandler.getRelatedAssets(userId,
                                                          supportedZones,
                                                          anchorGUID,
                                                          AssetMapper.FILE_FOLDER_TYPE_GUID,
                                                          AssetMapper.FILE_FOLDER_TYPE_NAME,
                                                          startingFrom,
                                                          queryPageSize,
                                                          serviceName,
                                                          methodName);
        }
        else
        {
            relatedAssets = assetHandler.getRelatedAssets(userId,
                                                          supportedZones,
                                                          anchorGUID,
                                                          AssetMapper.FOLDER_HIERARCHY_TYPE_GUID,
                                                          AssetMapper.FOLDER_HIERARCHY_TYPE_NAME,
                                                          startingFrom,
                                                          queryPageSize,
                                                          serviceName,
                                                          methodName);
        }

        return this.getRelatedAssetGUIDs(relatedAssets);
    }


    /**
     * Extract the unique identifiers from the returned related assets.
     *
     * @param relatedAssets list of related assets and relationship type.
     * @return list of GUIDs of the assets
     */
    private List<String> getRelatedAssetGUIDs(List<RelatedAsset>  relatedAssets)
    {
        if (relatedAssets != null)
        {
            List<String>  relatedAssetGUIDs = new ArrayList<>();

            for (RelatedAsset relatedAsset : relatedAssets)
            {
                if (relatedAsset != null)
                {
                    Asset asset = relatedAsset.getRelatedAsset();
                    if (asset != null)
                    {
                        if (asset.getGUID() != null)
                        {
                            relatedAssetGUIDs.add(asset.getGUID());
                        }
                    }
                }
            }

            if (! relatedAssetGUIDs.isEmpty())
            {
                return relatedAssetGUIDs;
            }
        }

        return null;
    }


    /**
     * Get the files inside a folder - both those that are nested and those that are linked.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the anchor folder
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
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
                                       int     startingFrom,
                                       int     maxPageSize,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String  guidParameterName = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, guidParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maxPageSize, methodName);

        List<RelatedAsset> files = new ArrayList<>();
        boolean            moreFilesToCome = true;
        int                requestPointer = startingFrom;

        while ((files.size() < queryPageSize) && (moreFilesToCome))
        {
            List<RelatedAsset> relatedAssets = assetHandler.getRelatedAssets(userId,
                                                                             supportedZones,
                                                                             folderGUID,
                                                                             requestPointer,
                                                                             queryPageSize,
                                                                             serviceName,
                                                                             methodName);

            if (relatedAssets == null)
            {
                moreFilesToCome = false;
            }
            else
            {
                if (relatedAssets.size() < maxPageSize)
                {
                    moreFilesToCome = false;
                }
                else
                {
                    requestPointer = requestPointer + relatedAssets.size();
                }

                for (RelatedAsset relatedAsset : relatedAssets)
                {
                    if (relatedAsset != null)
                    {
                        if ((AssetMapper.NESTED_FILE_TYPE_NAME.equals(relatedAsset.getTypeName())) ||
                            (AssetMapper.LINKED_FILE_TYPE_NAME.equals(relatedAsset.getTypeName())))
                        {
                            files.add(relatedAsset);
                        }
                    }
                }
            }

        }

        return this.getRelatedAssetGUIDs(files);
    }
}
