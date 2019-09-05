/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.handlers;

import org.odpi.openmetadata.accessservices.assetowner.properties.FileSystem;
import org.odpi.openmetadata.accessservices.assetowner.properties.Folder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

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


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName  name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public FileSystemHandler(String                  serviceName,
                             String                  serverName,
                             InvalidParameterHandler invalidParameterHandler,
                             RepositoryHandler repositoryHandler,
                             OMRSRepositoryHelper repositoryHelper)
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
     * @return List of Filesystem properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<FileSystem> getFileSystems(String  userId,
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

        return null;
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

        return null;
    }
}
