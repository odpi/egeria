/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DataFileElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.DataFileProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileFolderProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * FilesAndFoldersInterface is the interface used to record metadata about files and folders.
 */
public interface FilesAndFoldersInterface
{
    /**
     * Creates a new folder asset for each element in the pathName.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> createNestedFolders(String userId,
                                     String fileServerCapabilityGUID,
                                     String fileServerCapabilityName,
                                     String pathName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the parent entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param parentGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> createNestedFolders(String userId,
                                     String fileServerCapabilityGUID,
                                     String fileServerCapabilityName,
                                     String parentGUID,
                                     String pathName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void attachTopLevelFolder(String userId,
                              String fileServerCapabilityGUID,
                              String fileServerCapabilityName,
                              String folderGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Removed the link between a folder and a file system.
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void detachTopLevelFolder(String userId,
                              String fileServerCapabilityGUID,
                              String fileServerCapabilityName,
                              String folderGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Creates a new file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param dataFileProperties details of the data file to add to the catalog as an asset
     * @param connectorProviderName class name of connector provider for connector to access this asset
     *
     * @return list of asset GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> addDataFileToCatalog(String             userId,
                                      String             fileServerCapabilityGUID,
                                      String             fileServerCapabilityName,
                                      DataFileProperties dataFileProperties,
                                      String             connectorProviderName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;

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
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param fileFolderProperties properties to describe the folder properties
     * @param connectorProviderName class name of connector provider for connector to access this asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> addDataFolderToCatalog(String               userId,
                                        String               fileServerCapabilityGUID,
                                        String               fileServerCapabilityName,
                                        FileFolderProperties fileFolderProperties,
                                        String               connectorProviderName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void attachDataFileAssetToFolder(String userId,
                                     String fileServerCapabilityGUID,
                                     String fileServerCapabilityName,
                                     String folderGUID,
                                     String fileGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveDataFileInCatalog to record
     * the fact that the physical file has moved.  Use attachDataFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void detachDataFileAssetFromFolder(String userId,
                                       String fileServerCapabilityGUID,
                                       String fileServerCapabilityName,
                                       String folderGUID,
                                       String fileGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Retrieve a FolderProperties asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return FolderProperties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    FileFolderElement getFolderByGUID(String   userId,
                                      String   folderGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName path name
     *
     * @return FolderProperties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    FileFolderElement getFolderByPathName(String   userId,
                                          String   pathName) throws InvalidParameterException, 
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Return the list of folders linked to the file server capability.
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param startFrom starting point in the list
     * @param pageSize maximum number of results
     *
     * @return list of folder properties (null means no nested folders)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<FileFolderElement>  getTopLevelFolders(String userId,
                                                String fileServerCapabilityGUID,
                                                String fileServerCapabilityName,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param userId calling user
     * @param parentFolderGUID unique identifier of the parent folder or file system
     * @param startFrom starting point in the list
     * @param pageSize maximum number of results
     *
     * @return list of folder properties (null means no nested folders)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<FileFolderElement>  getNestedFolders(String  userId,
                                              String  parentFolderGUID,
                                              int     startFrom,
                                              int     pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param startFrom starting point in the list
     * @param pageSize maximum number of results
     *
     * @return list of file properties (null means no top-level files)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DataFileElement>  getTopLevelDataFiles(String userId,
                                                String fileServerCapabilityGUID,
                                                String fileServerCapabilityName,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;



    /**
     * Get the data files inside a folder - both those that are nested and those that are linked.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the parent folder
     * @param startFrom starting point in the list
     * @param pageSize maximum number of results
     *
     * @return list of file properties (null means no files)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DataFileElement> getFolderFiles(String  userId,
                                         String  folderGUID,
                                         int     startFrom,
                                         int     pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Retrieve a FolderProperties asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param fileGUID unique identifier used to locate the folder
     *
     * @return File properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    DataFileElement getFileByGUID(String   userId,
                                  String   fileGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName path name
     *
     * @return File properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    DataFileElement getFileByPathName(String   userId,
                                      String   pathName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;
}
