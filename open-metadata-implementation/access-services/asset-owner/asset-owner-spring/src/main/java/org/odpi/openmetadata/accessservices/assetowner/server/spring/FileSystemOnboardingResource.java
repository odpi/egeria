/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server.spring;


import org.odpi.openmetadata.accessservices.assetowner.properties.FileSystem;
import org.odpi.openmetadata.accessservices.assetowner.properties.Folder;
import org.odpi.openmetadata.accessservices.assetowner.rest.NewCSVFileAssetRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.NewFileAssetRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.NewFileSystemRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.PathNameRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.server.FileSystemRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * AssetOnboardingResource supports the server-side capture of REST calls to add new asset definitions.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}/assets")
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
    @RequestMapping(method = RequestMethod.POST, path = "/file-systems")

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
    @RequestMapping(method = RequestMethod.POST, path = "/folders/{anchorGUID}")

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
    @RequestMapping(method = RequestMethod.POST, path = "/folders")

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
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/file-systems/{fileSystemGUID}/folders/{fileSystemGUID}/attach")

    public VoidResponse attachFolderToFileSystem(@PathVariable String  serverName,
                                                 @PathVariable String  userId,
                                                 @PathVariable String  fileSystemGUID,
                                                 @PathVariable String  folderGUID)
    {
        return restAPI.attachFolderToFileSystem(serverName, userId, fileSystemGUID, folderGUID);
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/file-systems/{fileSystemGUID}/folders/{fileSystemGUID}/detach")

    public VoidResponse detachFolderFromFileSystem(@PathVariable String  serverName,
                                                   @PathVariable String  userId,
                                                   @PathVariable String  fileSystemGUID,
                                                   @PathVariable String  folderGUID)
    {
        return restAPI.detachFolderFromFileSystem(serverName, userId, fileSystemGUID, folderGUID);
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
     * @param requestBody pathname of the file
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/files")

    public GUIDListResponse addFileAssetToCatalog(@PathVariable String              serverName,
                                                  @PathVariable String              userId,
                                                  @RequestBody  PathNameRequestBody requestBody)
    {
        return restAPI.addFileAssetToCatalog(serverName, userId, requestBody);
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/folders/{folderGUID}/files/{fileGUID}/attach")

    public VoidResponse  attachFileAssetToFolder(@PathVariable String  serverName,
                                                 @PathVariable String  userId,
                                                 @PathVariable String  folderGUID,
                                                 @PathVariable String  fileGUID)
    {
        return restAPI.attachFileAssetToFolder(serverName, userId, folderGUID, fileGUID);
    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveFileInCatalog to record
     * the fact that the physical file has moved.  Use attachFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/folders/{folderGUID}/files/{fileGUID}/detach")

    public VoidResponse  detachFileAssetFromFolder(@PathVariable String  serverName,
                                                   @PathVariable String  userId,
                                                   @PathVariable String  folderGUID,
                                                   @PathVariable String  fileGUID)
    {
        return restAPI.detachFileAssetFromFolder(serverName, userId, folderGUID, fileGUID);
    }


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param fileGUID unique identifier of the file to move
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/folders/{folderGUID}/files/{fileGUID}/move-to")

    public VoidResponse  moveFileInCatalog(@PathVariable String  serverName,
                                           @PathVariable String  userId,
                                           @PathVariable String  folderGUID,
                                           @PathVariable String  fileGUID)
    {
        return restAPI.moveFileInCatalog(serverName, userId, folderGUID, fileGUID);
    }


    /**
     * Retrieve a FileSystem asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier used to locate the file system
     *
     * @return FileSystem properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/file-systems/{fileSystemGUID}")

    public FileSystem getFileSystemByGUID(@PathVariable String  serverName,
                                          @PathVariable String  userId,
                                          @PathVariable String  fileSystemGUID)
    {
        return restAPI.getFileSystemByGUID(serverName, userId, fileSystemGUID);
    }


    /**
     * Retrieve a FileSystem asset by its unique name.
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
    @RequestMapping(method = RequestMethod.GET, path = "/file-systems/by-name/{uniqueName}")

    public FileSystem getFileSystemByUniqueName(@PathVariable String  serverName,
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
    @RequestMapping(method = RequestMethod.GET, path = "/file-systems")

    public GUIDListResponse getFileSystems(@PathVariable String  serverName,
                                           @PathVariable String  userId,
                                           @RequestParam int     startingFrom,
                                           @RequestParam int     maxPageSize)
    {
        return restAPI.getFileSystems(serverName, userId, startingFrom, maxPageSize);
    }



    /**
     * Retrieve a Folder asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return Folder properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/folders/{folderGUID}")

    public Folder getFolderByGUID(@PathVariable String  serverName,
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
     * @return Folder properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/folders/by-path-name")

    public Folder getFolderByPathName(@PathVariable String                serverName,
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
    @RequestMapping(method = RequestMethod.GET, path = "/{anchorGUID}/folders")

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
    @RequestMapping(method = RequestMethod.GET, path = "/folders/{folderGUID}/files")

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
     * @return unique identifier (guid) of the asset description that represents the avro file or
     * InvalidParameterException full path or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/files/avro")

    public GUIDResponse  addAvroFileToCatalog(@PathVariable String                  serverName,
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
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/files/csv")

    public GUIDResponse addCSVFileToCatalog(@PathVariable String                     serverName,
                                            @PathVariable String                     userId,
                                            @RequestBody  NewCSVFileAssetRequestBody requestBody)
    {
        return restAPI.addCSVFileToCatalog(serverName, userId, requestBody);
    }



}
