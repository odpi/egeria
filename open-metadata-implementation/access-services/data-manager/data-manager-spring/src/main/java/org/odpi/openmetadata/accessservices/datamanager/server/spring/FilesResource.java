/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.accessservices.datamanager.server.FilesRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * FilesResource supports the server-side capture of REST calls to add new file-based asset definitions.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}/filesystems")

@Tag(name="Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data managers " +
                         "such as database servers, event brokers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Data Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/data-manager/overview/"))

public class FilesResource
{
    private FilesRESTServices restAPI = new FilesRESTServices();


    /**
     * Default constructor
     */
    public FilesResource()
    {
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the file server capability entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param parentGUID root object to connect the folder to
     * @param requestBody pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/parent/{parentGUID}")

    public GUIDListResponse createFolderStructureInCatalog(@PathVariable String              serverName,
                                                           @PathVariable String              userId,
                                                           @PathVariable String              parentGUID,
                                                           @RequestBody  PathNameRequestBody requestBody)
    {
        return restAPI.createFolderStructureInCatalog(serverName, userId, parentGUID, requestBody);
    }


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param folderGUID unique identifier of the folder in the catalog
     * @param requestBody external source identifiers - or null for local
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/{fileSystemGUID}/folders/{folderGUID}/attach")

    public VoidResponse attachFolderToFileSystem(@PathVariable                  String                    serverName,
                                                 @PathVariable                  String                    userId,
                                                 @PathVariable                  String                    fileSystemGUID,
                                                 @PathVariable                  String                    folderGUID,
                                                 @RequestBody(required = false) MetadataSourceRequestBody requestBody)
    {
        return restAPI.attachFolderToFileSystem(serverName, userId, fileSystemGUID, folderGUID, requestBody);
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param folderGUID unique identifier of the folder in the catalog
     * @param requestBody external source identifiers - or null for local
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/{fileSystemGUID}/folders/{folderGUID}/detach")

    public VoidResponse detachFolderFromFileSystem(@PathVariable                  String                    serverName,
                                                   @PathVariable                  String                    userId,
                                                   @PathVariable                  String                    fileSystemGUID,
                                                   @PathVariable                  String                    folderGUID,
                                                   @RequestBody(required = false) MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachFolderFromFileSystem(serverName, userId, fileSystemGUID, folderGUID, requestBody);
    }


    /**
     * Add a simple asset description linked to a connection object for a file and to the folder structure implied in the path name.
     * If the folder structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param requestBody properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException full path or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-files")

    public GUIDListResponse addDataFileToCatalog(@PathVariable String              serverName,
                                                 @PathVariable String              userId,
                                                 @RequestBody  DataFileRequestBody requestBody)
    {
        return restAPI.addDataFileToCatalog(serverName, userId, requestBody);
    }


    /**
     * Add an asset description a file based on the content of a template object. Link this new asset to the folder structure implied in the path name.
     * If the folder structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param templateGUID unique identifier of the file asset to copy
     * @param requestBody override properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException full path or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-files/from-template/{templateGUID}")

    public GUIDListResponse addDataFileToCatalogFromTemplate(@PathVariable String              serverName,
                                                             @PathVariable String              userId,
                                                             @PathVariable String              templateGUID,
                                                             @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.addDataFileToCatalogFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the file asset description in the catalog.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param dataFileGUID unique identifier of the data file asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param requestBody properties for the asset
     *
     * @return void or
     * InvalidParameterException full path or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-files/{dataFileGUID}")

    public VoidResponse updateDataFileInCatalog(@PathVariable String              serverName,
                                                @PathVariable String              userId,
                                                @PathVariable String              dataFileGUID,
                                                @RequestParam boolean             isMergeUpdate,
                                                @RequestBody  DataFileRequestBody requestBody)
    {
        return restAPI.updateDataFileInCatalog(serverName, userId, dataFileGUID, isMergeUpdate, requestBody);
    }


    /**
     * Mark the file asset description in the catalog as archived.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param dataFileGUID unique identifier of the data file asset
     * @param requestBody properties to help locate the archive copy
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-files/{dataFileGUID}/archive")

    public VoidResponse archiveDataFileInCatalog(@PathVariable String             serverName,
                                                 @PathVariable String             userId,
                                                 @PathVariable String             dataFileGUID,
                                                 @RequestBody  ArchiveRequestBody requestBody)
    {
        return restAPI.archiveDataFileInCatalog(serverName, userId, dataFileGUID, requestBody);
    }


    /**
     * Remove the file asset description from the catalog.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param dataFileGUID unique identifier of the data file asset
     * @param requestBody full pathname for the asset
     *
     * @return void or
     * InvalidParameterException full path or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-files/{dataFileGUID}/delete")

    public VoidResponse deleteDataFileFromCatalog(@PathVariable String              serverName,
                                                  @PathVariable String              userId,
                                                  @PathVariable String              dataFileGUID,
                                                  @RequestBody  PathNameRequestBody requestBody)
    {
        return restAPI.deleteDataFileFromCatalog(serverName, userId, dataFileGUID, requestBody);
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
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody pathname of the data folder
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/data-folders")

    public GUIDListResponse addDataFolderAssetToCatalog(@PathVariable String                serverName,
                                                        @PathVariable String                userId,
                                                        @RequestBody  DataFolderRequestBody requestBody)
    {
        return restAPI.addDataFolderAssetToCatalog(serverName, userId, requestBody);
    }


    /**
     * Creates a new folder asset that is identified as a data asset using a template.  This means the files and sub-folders within
     * it collectively make up the contents of the data asset.  As with other types of file-based asset, links
     * are made to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyDataFolder" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a DataFolder asset called
     * "one/two/three/MyDataFolder".
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param templateGUID unique identifier of the file asset to copy
     * @param requestBody override properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException full path or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-folders/from-template/{templateGUID}")

    public GUIDListResponse addDataFolderToCatalogFromTemplate(@PathVariable String              serverName,
                                                               @PathVariable String              userId,
                                                               @PathVariable String              templateGUID,
                                                               @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.addDataFolderToCatalogFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the DataFolder asset description in the catalog.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param requestBody properties for the asset
     *
     * @return void or
     * InvalidParameterException full path or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-folders/{dataFolderGUID}")

    public VoidResponse updateDataFolderInCatalog(@PathVariable String                serverName,
                                                  @PathVariable String                userId,
                                                  @PathVariable String                dataFolderGUID,
                                                  @RequestParam boolean               isMergeUpdate,
                                                  @RequestBody  DataFolderRequestBody requestBody)
    {
        return restAPI.updateDataFolderInCatalog(serverName, userId, dataFolderGUID, isMergeUpdate, requestBody);
    }


    /**
     * Mark the data folder asset description in the catalog as archived.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param dataFolderGUID unique identifier of the data file asset
     * @param requestBody properties to help locate the archive copy
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-folders/{dataFolderGUID}/archive")

    public VoidResponse archiveDataFolderInCatalog(@PathVariable String             serverName,
                                                   @PathVariable String             userId,
                                                   @PathVariable String             dataFolderGUID,
                                                   @RequestBody  ArchiveRequestBody requestBody)
    {
        return restAPI.archiveDataFolderInCatalog(serverName, userId, dataFolderGUID, requestBody);
    }


    /**
     * Remove the data folder asset description from the catalog.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param dataFolderGUID unique identifier of the data file asset
     * @param requestBody full pathname for the asset
     *
     * @return void or
     * InvalidParameterException full path or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/data-folders/{dataFolderGUID}/delete")

    public VoidResponse deleteDataFolderFromCatalog(@PathVariable String              serverName,
                                                    @PathVariable String              userId,
                                                    @PathVariable String              dataFolderGUID,
                                                    @RequestBody  PathNameRequestBody requestBody)
    {
        return restAPI.deleteDataFolderFromCatalog(serverName, userId, dataFolderGUID, requestBody);
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     * @param requestBody external source identifiers - or null for local
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/{folderGUID}/data-files/{fileGUID}/attach")

    public VoidResponse  attachDataFileAssetToFolder(@PathVariable                  String                    serverName,
                                                     @PathVariable                  String                    userId,
                                                     @PathVariable                  String                    folderGUID,
                                                     @PathVariable                  String                    fileGUID,
                                                     @RequestBody(required = false) MetadataSourceRequestBody requestBody)
    {
        return restAPI.attachDataFileAssetToFolder(serverName, userId, folderGUID, fileGUID, requestBody);
    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveDataFileInCatalog to record
     * the fact that the physical file has moved.  Use attachDataFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     * @param requestBody external source identifiers - or null for local
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/{folderGUID}/data-files/{fileGUID}/detach")

    public VoidResponse  detachDataFileAssetFromFolder(@PathVariable                  String                    serverName,
                                                       @PathVariable                  String                    userId,
                                                       @PathVariable                  String                    folderGUID,
                                                       @PathVariable                  String                    fileGUID,
                                                       @RequestBody(required = false) MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachDataFileAssetFromFolder(serverName, userId, folderGUID, fileGUID, requestBody);
    }


    /**
     * Move a data file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Similarly to the endpoint in the connection object.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param fileGUID unique identifier of the file to move
     * @param requestBody null request body to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/{folderGUID}/data-files/{fileGUID}/move-to")

    public VoidResponse  moveDataFileInCatalog(@PathVariable                  String                    serverName,
                                               @PathVariable                  String                    userId,
                                               @PathVariable                  String                    folderGUID,
                                               @PathVariable                  String                    fileGUID,
                                               @RequestBody(required = false) MetadataSourceRequestBody requestBody)
    {
        return restAPI.moveDataFileInCatalog(serverName, userId, folderGUID, fileGUID, requestBody);
    }


    /**
     * Move a data folder from its current parent folder to a new parent folder - this changes the folder's qualified name
     * but not its unique identifier (guid).  Similarly to the endpoint in the connection object.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param dataFolderGUID unique identifier of the data folder to move
     * @param requestBody null request body to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/{folderGUID}/data-folders/{dataFolderGUID}/move-to")

    public VoidResponse  moveDataFolderInCatalog(@PathVariable                  String                    serverName,
                                                 @PathVariable                  String                    userId,
                                                 @PathVariable                  String                    folderGUID,
                                                 @PathVariable                  String                    dataFolderGUID,
                                                 @RequestBody(required = false) MetadataSourceRequestBody requestBody)
    {
        return restAPI.moveDataFolderInCatalog(serverName, userId, folderGUID, dataFolderGUID, requestBody);
    }


    /**
     * Retrieve a FileSystemProperties asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier used to locate the file system
     *
     * @return FileSystemProperties properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/filesystems/{fileSystemGUID}")

    public FileSystemResponse getFileSystemByGUID(@PathVariable String  serverName,
                                                  @PathVariable String  userId,
                                                  @PathVariable String  fileSystemGUID)
    {
        return restAPI.getFileSystemByGUID(serverName, userId, fileSystemGUID);
    }


    /**
     * Retrieve a FileSystemProperties asset by its unique name.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param uniqueName unique name for the filesystem
     *
     * @return Filesystem properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/filesystems/by-name/{uniqueName}")

    public FileSystemResponse getFileSystemByUniqueName(@PathVariable String  serverName,
                                                        @PathVariable String  userId,
                                                        @PathVariable String  uniqueName)
    {
        return restAPI.getFileSystemByUniqueName(serverName, userId, uniqueName);
    }


    /**
     * Retrieve a list of defined FileSystems assets.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return List of Filesystem unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/filesystems")

    public GUIDListResponse getFileSystems(@PathVariable String  serverName,
                                           @PathVariable String  userId,
                                           @RequestParam int     startingFrom,
                                           @RequestParam int     maxPageSize)
    {
        return restAPI.getFileSystems(serverName, userId, startingFrom, maxPageSize);
    }



    /**
     * Retrieve a folder asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return folder properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/folders/{folderGUID}")

    public FileFolderResponse getFolderByGUID(@PathVariable String  serverName,
                                              @PathVariable String  userId,
                                              @PathVariable String  folderGUID)
    {
        return restAPI.getFolderByGUID(serverName, userId, folderGUID);
    }


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody path name
     *
     * @return folder properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/by-path-name")

    public FileFolderResponse getFolderByPathName(@PathVariable String                serverName,
                                                  @PathVariable String                userId,
                                                  @RequestBody  PathNameRequestBody   requestBody)
    {
        return restAPI.getFolderByPathName(serverName, userId, requestBody);
    }


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the anchor folder or Filesystem
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folder unique identifiers (null means no nested folders) or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/{fileSystemGUID}/folders")

    public GUIDListResponse  getTopLevelFolders(@PathVariable String  serverName,
                                                @PathVariable String  userId,
                                                @PathVariable String  fileSystemGUID,
                                                @RequestParam int     startingFrom,
                                                @RequestParam int     maxPageSize)
    {
        return restAPI.getTopLevelFolders(serverName, userId, fileSystemGUID, startingFrom, maxPageSize);
    }


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param parentFolderGUID unique identifier of the parent folder
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folder unique identifiers (null means no nested folders) or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/folders/{parentFolderGUID}/folders")

    public GUIDListResponse  getNestedFolders(@PathVariable String  serverName,
                                              @PathVariable String  userId,
                                              @PathVariable String  parentFolderGUID,
                                              @RequestParam int     startingFrom,
                                              @RequestParam int     maxPageSize)
    {
        return restAPI.getNestedFolders(serverName, userId, parentFolderGUID, startingFrom, maxPageSize);
    }


    /**
     * Get the files inside a folder - both those that are nested and those that are linked.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier of the anchor folder
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of file asset unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/folders/{folderGUID}/data-files")

    public GUIDListResponse  getFolderFiles(@PathVariable String  serverName,
                                            @PathVariable String  userId,
                                            @PathVariable String  folderGUID,
                                            @RequestParam int     startingFrom,
                                            @RequestParam int     maxPageSize)
    {
        return restAPI.getFolderFiles(serverName, userId, folderGUID, startingFrom, maxPageSize);
    }


    /**
     * Retrieve a data file asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param dataFileGUID unique identifier used to locate the folder
     *
     * @return data file properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/data-files/{dataFileGUID}")

    public DataFileResponse getDataFileByGUID(@PathVariable String  serverName,
                                              @PathVariable String  userId,
                                              @PathVariable String  dataFileGUID)
    {
        return restAPI.getDataFileByGUID(serverName, userId, dataFileGUID);
    }


    /**
     * Retrieve a data file by its fully qualified path name.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody path name
     *
     * @return data file properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/data-files/by-path-name")

    public DataFileResponse getDataFileByPathName(@PathVariable String                serverName,
                                                  @PathVariable String                userId,
                                                  @RequestBody  PathNameRequestBody   requestBody)
    {
        return restAPI.getDataFileByPathName(serverName, userId, requestBody);
    }


    /**
     * Retrieve data files by the supplied wildcard name.  The wildcard is specified using regular expressions (RegEx).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param requestBody path name
     *
     * @return data file properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/data-files/by-search-path-name")

    public DataFilesResponse getDataFilesByPathName(@PathVariable String              serverName,
                                                    @PathVariable String              userId,
                                                    @RequestParam int                 startingFrom,
                                                    @RequestParam int                 maxPageSize,
                                                    @RequestBody  PathNameRequestBody requestBody)
    {
        return  restAPI.getDataFilesByPathName(serverName, userId, startingFrom, maxPageSize, requestBody);
    }



    /**
     * Retrieve data files by the supplied wildcard name.  The wildcard is specified using regular expressions (RegEx).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     * @param requestBody path name
     *
     * @return data file properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/data-files/by-search-name")

    public DataFilesResponse findDataFilesByPathName(@PathVariable String              serverName,
                                                     @PathVariable String              userId,
                                                     @RequestParam int                 startingFrom,
                                                     @RequestParam int                 maxPageSize,
                                                     @RequestBody  PathNameRequestBody requestBody)
    {
        return  restAPI.findDataFilesByPathName(serverName, userId, startingFrom, maxPageSize, requestBody);
    }
}
