/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.files.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.FilesAndFoldersClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DataFileElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.ArchiveProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DataFileProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileFolderProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

import java.util.List;

/**
 * FilesIntegratorContext provides a wrapper around the Data Manager OMAS clients.
 * It provides the simplified interface to open metadata needed by the FilesIntegratorConnector.
 * It is designed to be used either for cataloguing folders and files
 */
public class FilesIntegratorContext
{
    private FilesAndFoldersClient  client;
    private DataManagerEventClient eventClient;
    private String                 userId;
    private String                 fileServerCapabilityGUID;
    private String                 fileServerCapabilityName;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param client client to map request to
     * @param eventClient client to register for events
     * @param userId integration daemon's userId
     * @param fileServerCapabilityGUID unique identifier of the software server capability for the file manager (or null)
     * @param fileServerCapabilityName unique name of the software server capability for the file manager (or null)
     */
    public FilesIntegratorContext(FilesAndFoldersClient  client,
                                  DataManagerEventClient eventClient,
                                  String                 userId,
                                  String                 fileServerCapabilityGUID,
                                  String                 fileServerCapabilityName)
    {
        this.client                   = client;
        this.eventClient              = eventClient;
        this.userId                   = userId;
        this.fileServerCapabilityGUID = fileServerCapabilityGUID;
        this.fileServerCapabilityName = fileServerCapabilityName;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /*============================================================================================================
     * Start of file management methods
     */

    /**
     * Creates a new folder asset for each element in the pathName.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param parentGUID unique identifier of root object to connect the folder to (fileSystemGUID or folderGUID)
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> createNestedFolders(String parentGUID,
                                            String pathName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return client.createNestedFolders(userId, fileServerCapabilityGUID, fileServerCapabilityName, parentGUID, pathName);
    }


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param fileSystemGUID unique identifier of the file system
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void attachTopLevelFolder(String fileSystemGUID,
                                     String folderGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        client.attachTopLevelFolder(userId, fileServerCapabilityGUID, fileServerCapabilityName, fileSystemGUID, folderGUID);
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param fileSystemGUID unique identifier of the file system
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void detachTopLevelFolder(String fileSystemGUID,
                                     String folderGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        client.detachTopLevelFolder(userId, fileServerCapabilityGUID, fileServerCapabilityName, fileSystemGUID, folderGUID);
    }


    /**
     * Creates a new file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param dataFileProperties details of the data file to add to the catalog as an asset
     * @param connectorProviderName class name of connector provider for connector to access this asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFileToCatalog(DataFileProperties dataFileProperties,
                                             String             connectorProviderName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return client.addDataFileToCatalog(userId, fileServerCapabilityGUID, fileServerCapabilityName, dataFileProperties, connectorProviderName);
    }


    /**
     * Add an asset description a file based on the content of a template object. Link this new asset to the folder structure implied in the path name.
     * If the folder structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param templateGUID unique identifier of the file asset to copy
     * @param templateProperties override properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFileToCatalogFromTemplate(String             templateGUID,
                                                         TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return client.addDataFileToCatalogFromTemplate(userId, fileServerCapabilityGUID, fileServerCapabilityName, templateGUID, templateProperties);
    }


    /**
     * Update the file asset description in the catalog.
     *
     * @param dataFileGUID unique identifier of the data file asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param dataFileProperties properties for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateDataFileInCatalog(String             dataFileGUID,
                                        boolean            isMergeUpdate,
                                        DataFileProperties dataFileProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        client.updateDataFileInCatalog(userId, fileServerCapabilityGUID, fileServerCapabilityName, dataFileGUID, isMergeUpdate, dataFileProperties);
    }


    /**
     * Mark the file asset description in the catalog as archived.
     *
     * @param dataFileGUID unique identifier of the data file asset
     * @param archiveProperties properties to help locate the archive copy
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void archiveDataFileInCatalog(String            dataFileGUID,
                                         ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        client.archiveDataFileInCatalog(userId, fileServerCapabilityGUID, fileServerCapabilityName, dataFileGUID, archiveProperties);
    }


    /**
     * Remove the file asset description from the catalog.
     *
     * @param dataFileGUID unique identifier of the data file asset
     * @param fullPathname unique path and file name for file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteDataFileFromCatalog(String dataFileGUID,
                                          String fullPathname) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        client.deleteDataFileFromCatalog(userId, fileServerCapabilityGUID, fileServerCapabilityName, dataFileGUID, fullPathname);
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
     * @param fileFolderProperties properties to describe the folder properties
     * @param connectorProviderName class name of connector provider for connector to access this asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFolderToCatalog(FileFolderProperties fileFolderProperties,
                                               String               connectorProviderName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return client.addDataFolderToCatalog(userId, fileServerCapabilityGUID, fileServerCapabilityName, fileFolderProperties, connectorProviderName);
    }


    /**
     * Add an asset description a file based on the content of a template object. Link this new asset to the folder structure implied in the path name.
     * If the folder structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param templateGUID unique identifier of the file asset to copy
     * @param templateProperties override properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFolderToCatalogFromTemplate(String             templateGUID,
                                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        return client.addDataFolderToCatalogFromTemplate(userId, fileServerCapabilityGUID, fileServerCapabilityName, templateGUID, templateProperties);
    }


    /**
     * Update the data folder asset description in the catalog.
     *
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param fileFolderProperties properties for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateDataFolderInCatalog(String               dataFolderGUID,
                                          boolean              isMergeUpdate,
                                          FileFolderProperties fileFolderProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        client.updateDataFolderInCatalog(userId, fileServerCapabilityGUID, fileServerCapabilityName, dataFolderGUID, isMergeUpdate, fileFolderProperties);
    }


    /**
     * Mark the data folder asset description in the catalog as archived.
     *
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param archiveProperties properties to help locate the archive copy
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void archiveDataFolderInCatalog(String            dataFolderGUID,
                                           ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        client.archiveDataFolderInCatalog(userId, fileServerCapabilityGUID, fileServerCapabilityName, dataFolderGUID, archiveProperties);
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void attachDataFileAssetToFolder(String folderGUID,
                                            String fileGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        client.attachDataFileAssetToFolder(userId, fileServerCapabilityGUID, fileServerCapabilityName, folderGUID, fileGUID);
    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveDataFileInCatalog to record
     * the fact that the physical file has moved.  Use attachDataFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void detachDataFileAssetFromFolder(String folderGUID,
                                              String fileGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        client.detachDataFileAssetFromFolder(userId, fileServerCapabilityGUID, fileServerCapabilityName, folderGUID, fileGUID);
    }


    /**
     * Retrieve a FolderProperties asset by its unique identifier (GUID).
     *
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return Folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FileFolderElement getFolderByGUID(String   folderGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return client.getFolderByGUID(userId, folderGUID);
    }


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param pathName path name
     *
     * @return FolderProperties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FileFolderElement getFolderByPathName(String   pathName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return client.getFolderByPathName(userId, pathName);
    }


    /**
     * Return the list of folders linked to the file server capability.
     *
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
    public List<FileFolderElement>  getTopLevelFolders(String fileSystemGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return client.getTopLevelFolders(userId, fileSystemGUID, startFrom, pageSize);
    }


    /**
     * Return the list of folders nested inside a folder.
     *
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
    public List<FileFolderElement>  getNestedFolders(String  parentFolderGUID,
                                                     int     startFrom,
                                                     int     pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return client.getNestedFolders(userId, parentFolderGUID, startFrom, pageSize);
    }


    /**
     * Return the list of folders nested inside a folder.
     *
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
    public List<DataFileElement>  getTopLevelDataFiles(String fileSystemGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return client.getTopLevelDataFiles(userId, fileSystemGUID, startFrom, pageSize);
    }


    /**
     * Get the data files inside a folder - both those that are nested and those that are linked.
     *
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
    public List<DataFileElement> getFolderFiles(String  folderGUID,
                                                int     startFrom,
                                                int     pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return client.getFolderFiles(userId, folderGUID, startFrom, pageSize);
    }


    /**
     * Retrieve a FolderProperties asset by its unique identifier (GUID).
     *
     * @param fileGUID unique identifier used to locate the folder
     *
     * @return File properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public DataFileElement getFileByGUID(String fileGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return client.getFileByGUID(userId, fileGUID);
    }


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param pathName path name
     *
     * @return File properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public DataFileElement getFileByPathName(String   pathName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return client.getFileByPathName(userId, pathName);
    }
}
