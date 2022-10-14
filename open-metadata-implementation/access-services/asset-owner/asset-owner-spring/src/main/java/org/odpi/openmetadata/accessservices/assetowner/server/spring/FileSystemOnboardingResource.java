/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetowner.rest.*;
import org.odpi.openmetadata.accessservices.assetowner.server.FileSystemRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * FileSystemOnboardingResource supports the server-side capture of REST calls to add new file-based asset definitions.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}")

@Tag(name="Asset Owner OMAS", description="The Asset Owner OMAS provides APIs and notifications for tools and applications supporting the work of Asset Owners in protecting and enhancing their assets.\n" +
        "\n", externalDocs=@ExternalDocumentation(description="Asset Owner Open Metadata Access Service (OMAS)",url="https://egeria-project.org/services/omas/asset-owner/overview/"))

public class FileSystemOnboardingResource
{
    private FileSystemRESTServices restAPI = new FileSystemRESTServices();


    /**
     * Default constructor
     */
    public FileSystemOnboardingResource()
    {
    }

    /*
     * ==============================================
     * AssetOnboardingFileSystem
     * ==============================================
     */


    /**
     * Files live on a file system.  This method creates a top level anchor for a file system.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody properties of the file system
     *
     * @return unique identifier for the file system or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/file-systems")

    public GUIDResponse   createFileSystemInCatalog(@PathVariable String                   serverName,
                                                    @PathVariable String                   userId,
                                                    @RequestBody  NewFileSystemRequestBody requestBody)
    {
        return restAPI.createFileSystemInCatalog(serverName, userId, requestBody);
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param anchorGUID root object to connect the folder to
     * @param requestBody pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/{anchorGUID}")

    public GUIDListResponse createFolderStructureInCatalog(@PathVariable String              serverName,
                                                           @PathVariable String              userId,
                                                           @PathVariable String              anchorGUID,
                                                           @RequestBody  PathNameRequestBody requestBody)
    {
        return restAPI.createFolderStructureInCatalog(serverName, userId, anchorGUID, requestBody);
    }


    /**
     * Creates a new folder asset for each element in the pathName.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders")

    public GUIDListResponse createFolderStructureInCatalog(@PathVariable String              serverName,
                                                           @PathVariable String              userId,
                                                           @RequestBody  PathNameRequestBody requestBody)
    {
        return restAPI.createFolderStructureInCatalog(serverName, userId, requestBody);
    }


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param folderGUID unique identifier of the folder in the catalog
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/file-systems/{fileSystemGUID}/folders/{folderGUID}/attach")

    public VoidResponse attachFolderToFileSystem(@PathVariable                  String          serverName,
                                                 @PathVariable                  String          userId,
                                                 @PathVariable                  String          fileSystemGUID,
                                                 @PathVariable                  String          folderGUID,
                                                 @RequestBody(required = false) NullRequestBody requestBody)
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
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/file-systems/{fileSystemGUID}/folders/{folderGUID}/detach")

    public VoidResponse detachFolderFromFileSystem(@PathVariable                  String          serverName,
                                                   @PathVariable                  String          userId,
                                                   @PathVariable                  String          fileSystemGUID,
                                                   @PathVariable                  String          folderGUID,
                                                   @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.detachFolderFromFileSystem(serverName, userId, fileSystemGUID, folderGUID, requestBody);
    }


    /**
     * Creates a new file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody pathname of the data file
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/assets/data-files")

    public GUIDListResponse addDataFileAssetToCatalog(@PathVariable String                  serverName,
                                                      @PathVariable String                  userId,
                                                      @RequestBody  NewFileAssetRequestBody requestBody)
    {
        return restAPI.addDataFileAssetToCatalog(serverName, userId, requestBody);
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
    @PostMapping(path = "/assets/data-folders")

    public GUIDListResponse addDataFolderAssetToCatalog(@PathVariable String                  serverName,
                                                        @PathVariable String                  userId,
                                                        @RequestBody  NewFileAssetRequestBody requestBody)
    {
        return restAPI.addDataFolderAssetToCatalog(serverName, userId, requestBody);
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/{folderGUID}/assets/data-files/{fileGUID}/attach")

    public VoidResponse  attachDataFileAssetToFolder(@PathVariable                  String          serverName,
                                                     @PathVariable                  String          userId,
                                                     @PathVariable                  String          folderGUID,
                                                     @PathVariable                  String          fileGUID,
                                                     @RequestBody(required = false) NullRequestBody requestBody)
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
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @PostMapping(path = "/folders/{folderGUID}/assets/data-files/{fileGUID}/detach")

    public VoidResponse  detachDataFileAssetFromFolder(@PathVariable                  String          serverName,
                                                       @PathVariable                  String          userId,
                                                       @PathVariable                  String          folderGUID,
                                                       @PathVariable                  String          fileGUID,
                                                       @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.detachDataFileAssetFromFolder(serverName, userId, folderGUID, fileGUID, requestBody);
    }


    /**
     * Move a data file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Similarly, for the endpoint in the connection object.
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
    @PostMapping(path = "/folders/{folderGUID}/assets/data-files/{fileGUID}/move-to")

    public VoidResponse  moveDataFileInCatalog(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          folderGUID,
                                               @PathVariable                  String          fileGUID,
                                               @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.moveDataFileInCatalog(serverName, userId, folderGUID, fileGUID, requestBody);
    }


    /**
     * Move a data folder from its current parent folder to a new parent folder - this changes the folder's qualified name
     * but not its unique identifier (guid).  Similarly, for the endpoint in the connection object.
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
    @PostMapping(path = "/folders/{folderGUID}/assets/data-folders/{dataFolderGUID}/move-to")

    public VoidResponse  moveDataFolderInCatalog(@PathVariable                  String          serverName,
                                                 @PathVariable                  String          userId,
                                                 @PathVariable                  String          folderGUID,
                                                 @PathVariable                  String          dataFolderGUID,
                                                 @RequestBody(required = false) NullRequestBody requestBody)
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
    @GetMapping(path = "/file-systems/{fileSystemGUID}")

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
    @GetMapping(path = "/file-systems/by-name/{uniqueName}")

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
    @GetMapping(path = "/file-systems")

    public GUIDListResponse getFileSystems(@PathVariable String  serverName,
                                           @PathVariable String  userId,
                                           @RequestParam int     startingFrom,
                                           @RequestParam int     maxPageSize)
    {
        return restAPI.getFileSystems(serverName, userId, startingFrom, maxPageSize);
    }



    /**
     * Retrieve a FolderProperties asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return FolderProperties properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/folders/{folderGUID}")

    public FolderResponse getFolderByGUID(@PathVariable String  serverName,
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
     * @return FolderProperties properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/folders/by-path-name")

    public FolderResponse getFolderByPathName(@PathVariable String                serverName,
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
     * @param anchorGUID unique identifier of the anchor folder or Filesystem
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folder unique identifiers (null means no nested folders) or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/{anchorGUID}/folders")

    public GUIDListResponse  getNestedFolders(@PathVariable String  serverName,
                                              @PathVariable String  userId,
                                              @PathVariable String  anchorGUID,
                                              @RequestParam int     startingFrom,
                                              @RequestParam int     maxPageSize)
    {
        return restAPI.getNestedFolders(serverName, userId, anchorGUID, startingFrom, maxPageSize);
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
    @GetMapping(path = "/folders/{folderGUID}/files")

    public GUIDListResponse  getFolderFiles(@PathVariable String  serverName,
                                            @PathVariable String  userId,
                                            @PathVariable String  folderGUID,
                                            @RequestParam int     startingFrom,
                                            @RequestParam int     maxPageSize)
    {
        return restAPI.getFolderFiles(serverName, userId, folderGUID, startingFrom, maxPageSize);
    }


    /*
     * ==============================================
     * AssetOnboardingAvroFile
     * ==============================================
     */

    /**
     * Add a simple asset description linked to a connection object for an Avro file.
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
    @PostMapping(path = "/assets/data-files/avro")

    public GUIDListResponse  addAvroFileToCatalog(@PathVariable String                  serverName,
                                                  @PathVariable String                  userId,
                                                  @RequestBody  NewFileAssetRequestBody requestBody)
    {
        return restAPI.addAvroFileToCatalog(serverName, userId, requestBody);
    }


    /*
     * ==============================================
     * AssetOnboardingCSVFile
     * ==============================================
     */


    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param requestBody parameters for the new asset
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/data-files/csv")

    public GUIDListResponse addCSVFileToCatalog(@PathVariable String                     serverName,
                                                @PathVariable String                     userId,
                                                @RequestBody  NewCSVFileAssetRequestBody requestBody)
    {
        return restAPI.addCSVFileToCatalog(serverName, userId, requestBody);
    }
}
