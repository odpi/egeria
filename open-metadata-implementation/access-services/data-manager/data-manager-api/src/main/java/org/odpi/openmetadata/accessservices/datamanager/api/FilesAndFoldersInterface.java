/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DataFileElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.ArchiveProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DataFileProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileFolderProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * FilesAndFoldersInterface is the interface used to record metadata about files and folders.  It can be used to create metadata both on
 * behalf of a file system or a file manager application.  If this is being used for files and folders in a file system, leave the
 * fileManagerCapabilityGUID and fileManagerCapabilityName parameters as null.  If the files and folders are owned by a file manager
 * application and you want the corresponding assets in the catalog to be locked read only to all but the integration technology managing
 * the metadata then set fileManagerCapabilityGUID and fileManagerCapabilityName to the GUId and qualified name of a software server
 * capability that represents the file manager.
 */
public interface FilesAndFoldersInterface
{

    /**
     * Creates a new folder asset for each element in the pathName that is linked from the parent entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param parentGUID unique identifier of root object to connect the folder to (fileSystemGUID or folderGUID)
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> createNestedFolders(String userId,
                                     String fileManagerCapabilityGUID,
                                     String fileManagerCapabilityName,
                                     String parentGUID,
                                     String pathName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param fileSystemGUID unique identifier of the file system to connect folder to
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void attachTopLevelFolder(String userId,
                              String fileManagerCapabilityGUID,
                              String fileManagerCapabilityName,
                              String fileSystemGUID,
                              String folderGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Removed the link between a folder and a file system.
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param fileSystemGUID unique identifier of the file system to disconnect folder from
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void detachTopLevelFolder(String userId,
                              String fileManagerCapabilityGUID,
                              String fileManagerCapabilityName,
                              String fileSystemGUID,
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
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
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
                                      String             fileManagerCapabilityGUID,
                                      String             fileManagerCapabilityName,
                                      DataFileProperties dataFileProperties,
                                      String             connectorProviderName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Add an asset description a file based on the content of a template object. Link this new asset to the folder structure implied in the path name.
     * If the folder structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param templateGUID unique identifier of the file asset to copy
     * @param templateProperties override properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> addDataFileToCatalogFromTemplate(String             userId,
                                                  String             fileManagerCapabilityGUID,
                                                  String             fileManagerCapabilityName,
                                                  String             templateGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Update the file asset description in the catalog.
     *
     * @param userId calling user (assumed to be the owner)
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param dataFileGUID unique identifier of the data file asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param dataFileProperties properties for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateDataFileInCatalog(String             userId,
                                 String             fileManagerCapabilityGUID,
                                 String             fileManagerCapabilityName,
                                 String             dataFileGUID,
                                 boolean            isMergeUpdate,
                                 DataFileProperties dataFileProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Remove the file asset description from the catalog.
     *
     * @param userId calling user (assumed to be the owner)
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param dataFileGUID unique identifier of the data file asset
     * @param archiveProperties properties to help locate the archive copy
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void archiveDataFileInCatalog(String            userId,
                                  String            fileManagerCapabilityGUID,
                                  String            fileManagerCapabilityName,
                                  String            dataFileGUID,
                                  ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Remove the file asset description from the catalog.
     *
     * @param userId calling user (assumed to be the owner)
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param dataFileGUID unique identifier of the data file asset
     * @param fullPathname unique path and file name for file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteDataFileFromCatalog(String userId,
                                   String fileManagerCapabilityGUID,
                                   String fileManagerCapabilityName,
                                   String dataFileGUID,
                                   String fullPathname) throws InvalidParameterException,
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
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
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
                                        String               fileManagerCapabilityGUID,
                                        String               fileManagerCapabilityName,
                                        FileFolderProperties fileFolderProperties,
                                        String               connectorProviderName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;

    /**
     * Creates a new folder asset that is identified as a data asset using a template.  This means the files and sub-folders within
     * it collectively make up the contents of the data asset.  As with other types of file-based asset, links
     * are made to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyDataFolder" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a DataFolder asset called
     * "one/two/three/MyDataFolder".
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param templateGUID unique identifier of the file asset to copy
     * @param templateProperties override properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> addDataFolderToCatalogFromTemplate(String             userId,
                                                    String             fileManagerCapabilityGUID,
                                                    String             fileManagerCapabilityName,
                                                    String             templateGUID,
                                                    TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;


    /**
     * Update the data folder asset description in the catalog.
     *
     * @param userId calling user (assumed to be the owner)
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param fileFolderProperties properties for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateDataFolderInCatalog(String               userId,
                                   String               fileManagerCapabilityGUID,
                                   String               fileManagerCapabilityName,
                                   String               dataFolderGUID,
                                   boolean              isMergeUpdate,
                                   FileFolderProperties fileFolderProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Mark the data folder asset description in the catalog as archived.
     *
     * @param userId calling user (assumed to be the owner)
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param archiveProperties properties to help locate the archive copy
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void archiveDataFolderInCatalog(String            userId,
                                    String            fileManagerCapabilityGUID,
                                    String            fileManagerCapabilityName,
                                    String            dataFolderGUID,
                                    ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Remove the data folder asset description from the catalog.
     *
     * @param userId calling user (assumed to be the owner)
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param fullPathname unique path and file name for file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteDataFolderFromCatalog(String userId,
                                     String fileManagerCapabilityGUID,
                                     String fileManagerCapabilityName,
                                     String dataFolderGUID,
                                     String fullPathname) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void attachDataFileAssetToFolder(String userId,
                                     String fileManagerCapabilityGUID,
                                     String fileManagerCapabilityName,
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
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void detachDataFileAssetFromFolder(String userId,
                                       String fileManagerCapabilityGUID,
                                       String fileManagerCapabilityName,
                                       String folderGUID,
                                       String fileGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Move a data file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Similarly to the endpoint in the connection object.
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void moveDataFileInCatalog(String userId,
                               String fileManagerCapabilityGUID,
                               String fileManagerCapabilityName,
                               String folderGUID,
                               String fileGUID) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException;


    /**
     * Move a data folder from its current parent folder to a new parent folder - this changes the folder's qualified name
     * but not its unique identifier (guid).  Similarly to the endpoint in the connection object.
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param folderGUID unique identifier of the folder
     * @param dataFolderGUID unique identifier of the data folder to move
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void moveDataFolderInCatalog(String userId,
                                 String fileManagerCapabilityGUID,
                                 String fileManagerCapabilityName,
                                 String folderGUID,
                                 String dataFolderGUID) throws InvalidParameterException,
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
     * @param fileSystemGUID unique identifier of the software server capability representing the file system
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
                                                String fileSystemGUID,
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
     * @param fileSystemGUID unique identifier of the software server capability representing the file system
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
                                                String fileSystemGUID,
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
    DataFileElement getFileByPathName(String userId,
                                      String pathName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Find data file by a full or partial path name. The wildcard is specified using regular expressions (RegEx) and the method matches on the
     * pathName property.
     *
     * @param userId calling user
     * @param pathName path name
     * @param startFrom starting point in the list
     * @param pageSize maximum number of results
     *
     * @return list of matching file properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DataFileElement> getFilesByPathName(String userId,
                                             String pathName,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Retrieve data files by the supplied wildcard name.  The wildcard is specified using regular expressions (RegEx) and the method matches on the
     * qualifiedName, name and pathName property.
     *
     * @param userId calling user
     * @param pathName path name
     * @param startFrom starting point in the list
     * @param pageSize maximum number of results
     *
     * @return list of matching file properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DataFileElement> findFilesByPathName(String userId,
                                              String pathName,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;
}
