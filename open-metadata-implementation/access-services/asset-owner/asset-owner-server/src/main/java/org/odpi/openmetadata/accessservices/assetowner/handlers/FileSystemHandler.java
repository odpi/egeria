/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.handlers;

import org.odpi.openmetadata.accessservices.assetowner.properties.FileSystem;
import org.odpi.openmetadata.accessservices.assetowner.properties.Folder;
import org.odpi.openmetadata.adapters.connectors.avrofile.AvroFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.AssetHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * FileSystemHandler provides the support for managing catalog entries about files and folders.
 */
public class FileSystemHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;
    private AssetHandler            assetHandler;
    private SchemaTypeHandler       schemaTypeHandler;

    private final static String folderDivider = "/";
    private final static String fileSystemDivider = "://";
    private final static String fileTypeDivider = "\\.";


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName  name of this service
     * @param serverName name of the local server
     * @param assetHandler handler for assets
     * @param schemaTypeHandler handler for schema elements
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public FileSystemHandler(String                  serviceName,
                             String                  serverName,
                             AssetHandler            assetHandler,
                             SchemaTypeHandler       schemaTypeHandler,
                             InvalidParameterHandler invalidParameterHandler,
                             RepositoryHandler       repositoryHandler,
                             OMRSRepositoryHelper    repositoryHelper)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.assetHandler = assetHandler;
        this.schemaTypeHandler = schemaTypeHandler;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
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
                    result = new ArrayList<>();

                    for (int i=startingToken; i<endingToken; i++)
                    {
                        result.add(tokens[i]);
                    }
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
     * @param methodName calling method
     *
     * @return unique identifier for the file system
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String   createFileSystemInCatalog(String               userId,
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
                                              String               methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return null;
    }


    /**
     * Create the requested asset.
     *
     * @param userId calling user
     * @param uniqueName qualified name for the file system
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
    private String createFileAsset(String               userId,
                                   String               uniqueName,
                                   String               displayName,
                                   String               description,
                                   String               typeName,
                                   Connection           connection,
                                   String               methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        Asset asset = assetHandler.createEmptyAsset(typeName, methodName);

        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setQualifiedName(typeName + ":" + uniqueName);

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
        String folderGUID = createFileAsset(userId,
                                            pathName,
                                            folderName,
                                            null,
                                            AssetMapper.FILE_FOLDER_TYPE_NAME,
                                            null,
                                            methodName);

        if (anchorGUID != null)
        {
            repositoryHandler.createRelationship(userId,
                                                 AssetMapper.FOLDER_HIERARCHY_TYPE_GUID,
                                                 anchorGUID,
                                                 folderGUID,
                                                 null,
                                                 methodName);
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
        List<String>  folderGUIDs = new ArrayList<>();;

        if ((folderNames != null) && (! folderNames.isEmpty()))
        {
            String pathName = fileSystemName;
            String folderName = null;
            String nextAnchorGUID = anchorGUID;

            for (String folderFragment : folderNames)
            {
                pathName   = pathName   + folderDivider + folderFragment;
                folderName = folderName + folderDivider + folderFragment;

                String folderGUID = createFolderInCatalog(userId,
                                                          nextAnchorGUID,
                                                          pathName,
                                                          folderName,
                                                          methodName);

                folderGUIDs.add(folderGUID);
                nextAnchorGUID = folderGUID;
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
        return null;
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
        return null;
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

    }


    /**
     * Creates a new file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param userId calling user
     * @param pathName pathname of the file
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addFileAssetToCatalog(String   userId,
                                              String   pathName,
                                              String   methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        String      fileSystemName = this.getFileSystemName(pathName);
        String      fileSystemGUID = null;

        if (fileSystemName != null)
        {
            FileSystem  fileSystem = this.getFileSystemByUniqueName(userId, pathName, methodName);

            if (fileSystem == null)
            {
                fileSystemGUID = this.createFileSystemInCatalog(userId,
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
                                                                null);
            }
            else
            {
                fileSystemGUID = fileSystem.getGUID();
            }
        }

        return null;
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
    public void  attachFileAssetToFolder(String   userId,
                                         String   folderGUID,
                                         String   fileGUID,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {

    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveFileInCatalog to record
     * the fact that the physical file has moved.  Use attachFileAssetToFolder to create logical link to a new
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
    public void  detachFileAssetFromFolder(String   userId,
                                           String   folderGUID,
                                           String   fileGUID,
                                           String   methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {

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
    public void  moveFileInCatalog(String   userId,
                                   String   folderGUID,
                                   String   fileGUID,
                                   String   methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {

    }


    /**
     * Retrieve a FileSystem asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param fileSystemGUID unique identifier used to locate the file system
     * @param methodName calling method
     *
     * @return FileSystem properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FileSystem getFileSystemByGUID(String   userId,
                                          String   fileSystemGUID,
                                          String   methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve a FileSystem asset by its unique name.
     *
     * @param userId calling user
     * @param uniqueName unique name ofr the file system
     * @param methodName calling method
     *
     * @return Filesystem properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FileSystem getFileSystemByUniqueName(String   userId,
                                                String   uniqueName,
                                                String   methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve a list of defined FileSystems assets.
     *
     * @param userId calling user
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param methodName calling method
     *
     * @return List of Filesystem unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> getFileSystems(String  userId,
                                       int     startingFrom,
                                       int     maxPageSize,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return null;
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
    public Folder getFolderByGUID(String   userId,
                                  String   folderGUID,
                                  String   methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
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
    public Folder getFolderByPathName(String  userId,
                                      String  pathName,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
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
        return null;
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
     * @return unique identifier (guid) of the asset description that represents the avro file
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String  addAvroFileToCatalog(String userId,
                                        String displayName,
                                        String description,
                                        String fullPath,
                                        String methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        Asset asset = assetHandler.createEmptyAsset(AssetMapper.AVRO_FILE_TYPE_NAME, methodName);

        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setQualifiedName(AssetMapper.AVRO_FILE_TYPE_NAME + ":" + fullPath);

        return assetHandler.addAsset(userId,
                                     asset,
                                     getAvroFileConnection(fullPath),
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
     * @return unique identifier (guid) of the asset
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String  addCSVFileToCatalog(String       userId,
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
        Asset asset = assetHandler.createEmptyAsset(AssetMapper.CSV_FILE_TYPE_NAME, methodName);

        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setQualifiedName(AssetMapper.CSV_FILE_TYPE_NAME + ":" + fullPath);

        SchemaType            schemaType       = null;
        List<SchemaAttribute> schemaAttributes = null;


        if (columnHeaders != null)
        {
            schemaType  = schemaTypeHandler.getTabularSchemaType(asset.getQualifiedName(),
                                                                 asset.getDisplayName(),
                                                                 userId,
                                                                 "CSVFile",
                                                                 columnHeaders);

            schemaAttributes = schemaTypeHandler.getTabularSchemaColumns(schemaType.getQualifiedName(),
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
        extendedProperties.put(AssetMapper.DELIMITER_CHARACTER_PROPERTY_NAME, delimiterCharacter);
        extendedProperties.put(AssetMapper.QUOTE_CHARACTER_PROPERTY_NAME, quoteCharacter);
        asset.setExtendedProperties(extendedProperties);

        return assetHandler.addAsset(userId,
                                     asset,
                                     schemaType,
                                     schemaAttributes,
                                     getCSVFileConnection(fullPath,
                                                          columnHeaders,
                                                          delimiterCharacter,
                                                          quoteCharacter),
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
     * @param fileName name of the file to connect to
     * @return connection object
     */
    private Connection getBasicFileConnection(String            fileName)
    {
        final BasicFileStoreProvider provider = new BasicFileStoreProvider();

        String endpointName    = "FileStore.Endpoint." + fileName;

        final String connectionDescription = "FileStore connection.";

        String connectionName = fileName + "File Store Connection";

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
    private Connection getCSVFileConnection(String            fileName,
                                            List<String>      columnHeaders,
                                            Character         delimiterCharacter,
                                            Character         quoteCharacter)
    {
        final CSVFileStoreProvider provider = new CSVFileStoreProvider();

        String endpointName    = "CSVFileStore.Endpoint." + fileName;

        final String connectionDescription = "CSVFileStore connection.";

        String connectionName = fileName + "CSV File Store Connection";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setGUID(UUID.randomUUID().toString());
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
     * This method creates a connection for a CSV File.  The connection object provides the OCF with the
     * properties to create an appropriate connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param fileName name of the file to connect to
     * @return connection object
     */
    private Connection getAvroFileConnection(String            fileName)
    {
        final AvroFileStoreProvider provider = new AvroFileStoreProvider();

        String endpointName    = "AvroFileStore.Endpoint." + fileName;

        final String connectionDescription = "AvroFileStore connection.";

        String connectionName = fileName + "Avro File Store Connection";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setGUID(UUID.randomUUID().toString());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(getEndpoint(endpointName, fileName));
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
        endpoint.setGUID(UUID.randomUUID().toString());
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription + fileName);
        endpoint.setAddress(fileName);

        return endpoint;
    }
}
