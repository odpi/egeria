/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client;

import org.odpi.openmetadata.accessservices.assetowner.api.AssetOnboardingFileSystem;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.FileSystemElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.FolderElement;
import org.odpi.openmetadata.accessservices.assetowner.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * FileSystemAssetOwner provides specialist methods for onboarding details of a file system and the files within it.
 * At the top level is a file system.  It can have nested Folders attached and inside the folders are the files.
 */
public class FileSystemAssetOwner extends AssetOwner implements AssetOnboardingFileSystem
{
    /**
     * Create a new client with no authentication embedded in the HTTP request and an audit log.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FileSystemAssetOwner(String   serverName,
                                String   serverPlatformURLRoot,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FileSystemAssetOwner(String serverName,
                                String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     * There is also an audit log destination.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FileSystemAssetOwner(String   serverName,
                                String   serverPlatformURLRoot,
                                String   userId,
                                String   password,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FileSystemAssetOwner(String serverName,
                                String serverPlatformURLRoot,
                                String userId,
                                String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FileSystemAssetOwner(String               serverName,
                                String               serverPlatformURLRoot,
                                AssetOwnerRESTClient restClient,
                                int                  maxPageSize,
                                AuditLog             auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }

    /*
     * ==============================================
     * AssetOnboardingFileSystem
     * ==============================================
     */

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
     *
     * @return unique identifier for the file system
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String   createFileSystemInCatalog(String              userId,
                                              String              uniqueName,
                                              String              displayName,
                                              String              description,
                                              String              type,
                                              String              version,
                                              String              patchLevel,
                                              String              source,
                                              String              format,
                                              String              encryption,
                                              Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String   methodName = "createFileSystemInCatalog";
        final String   pathParameter = "uniqueName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/file-systems";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, pathParameter, methodName);

        NewFileSystemRequestBody requestBody = new NewFileSystemRequestBody();
        requestBody.setUniqueName(uniqueName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setFileSystemType(type);
        requestBody.setVersion(version);
        requestBody.setPatchLevel(patchLevel);
        requestBody.setSource(source);
        requestBody.setFormat(format);
        requestBody.setEncryption(encryption);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param parentGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String> createFolderStructureInCatalog(String   userId,
                                                       String   parentGUID,
                                                       String   pathName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   methodName = "createFolderStructureInCatalog";
        final String   pathParameter = "pathName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameter, methodName);

        PathNameRequestBody requestBody = new PathNameRequestBody();
        requestBody.setFullPath(pathName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          parentGUID);

        return restResult.getGUIDs();
    }


    /**
     * Creates a new folder asset for each element in the pathName.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String> createFolderStructureInCatalog(String   userId,
                                                       String   pathName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   methodName = "createFolderStructureInCatalog";
        final String   pathParameter = "pathName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameter, methodName);

        PathNameRequestBody requestBody = new PathNameRequestBody();
        requestBody.setFullPath(pathName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId);

        return restResult.getGUIDs();
    }


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void attachFolderToFileSystem(String   userId,
                                         String   fileSystemGUID,
                                         String   folderGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   methodName = "attachFolderToFileSystem";
        final String   fileSystemGUIDParameter = "fileSystemGUID";
        final String   folderGUIDParameter = "folderGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/file-systems/{2}/folders/{3}/attach";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileSystemGUID, fileSystemGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        fileSystemGUID,
                                        folderGUID);
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the file system in the catalog
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void detachFolderFromFileSystem(String   userId,
                                           String   fileSystemGUID,
                                           String   folderGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String   methodName = "detachFolderFromFileSystem";
        final String   fileSystemGUIDParameter = "fileSystemGUID";
        final String   folderGUIDParameter = "folderGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/file-systems/{2}/folders/{3}/detach";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileSystemGUID, fileSystemGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        fileSystemGUID,
                                        folderGUID);
    }


    /**
     * Creates a new data file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param userId calling user
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param pathName pathname of the data file
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String> addDataFileAssetToCatalog(String   userId,
                                                  String   displayName,
                                                  String   description,
                                                  String   pathName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String   methodName = "addDataFileAssetToCatalog";
        final String   pathParameter = "pathName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/data-files";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameter, methodName);

        NewFileAssetRequestBody requestBody = new NewFileAssetRequestBody();
        requestBody.setName(displayName);
        requestBody.setDescription(description);
        requestBody.setFullPath(pathName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId);

        return restResult.getGUIDs();
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
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param pathName pathname of the data folder
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String> addDataFolderAssetToCatalog(String   userId,
                                                    String   displayName,
                                                    String   description,
                                                    String   pathName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   methodName = "addDataFolderAssetToCatalog";
        final String   pathParameter = "pathName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/data-folders";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, pathParameter, methodName);

        NewFileAssetRequestBody requestBody = new NewFileAssetRequestBody();
        requestBody.setName(displayName);
        requestBody.setDescription(description);
        requestBody.setFullPath(pathName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId);

        return restResult.getGUIDs();
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void attachDataFileAssetToFolder(String   userId,
                                            String   folderGUID,
                                            String   fileGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "attachDataFileAssetToFolder";
        final String   fileGUIDParameter = "fileGUID";
        final String   folderGUIDParameter = "folderGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders/{2}/assets/data-files/{3}/attach";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        folderGUID,
                                        fileGUID);
    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveDataFileInCatalog to record
     * the fact that the physical file has moved.  Use attachDataFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void detachDataFileAssetFromFolder(String   userId,
                                              String   folderGUID,
                                              String   fileGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   methodName = "detachDataFileAssetFromFolder";
        final String   fileGUIDParameter = "fileGUID";
        final String   folderGUIDParameter = "folderGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders/{2}/assets/data-files/{3}/detach";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        folderGUID,
                                        fileGUID);
    }


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Similarly to the endpoint in the connection object.
     *
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param fileGUID unique identifier of the file to move
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void moveDataFileInCatalog(String   userId,
                                      String   folderGUID,
                                      String   fileGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String   methodName = "moveDataFileInCatalog";
        final String   fileGUIDParameter = "fileGUID";
        final String   folderGUIDParameter = "folderGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders/{2}/assets/data-files/{3}/move-to";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        folderGUID,
                                        fileGUID);
    }


    /**
     * Move a data folder from its current parent folder to a new parent folder - this changes the folder's qualified name
     * but not its unique identifier (guid).  Similarly to the endpoint in the connection object.
     *
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param dataFolderGUID unique identifier of the file to move
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void moveDataFolderInCatalog(String   userId,
                                        String   folderGUID,
                                        String   dataFolderGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "moveDataFileInCatalog";
        final String   fileGUIDParameter = "fileGUID";
        final String   folderGUIDParameter = "folderGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders/{2}/assets/data-folders/{3}/move-to";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFolderGUID, fileGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        folderGUID,
                                        dataFolderGUID);
    }


    /**
     * Retrieve a FileSystemProperties asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param fileSystemGUID unique identifier used to locate the file system
     *
     * @return FileSystemProperties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public FileSystemElement getFileSystemByGUID(String   userId,
                                                 String   fileSystemGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   methodName = "getFileSystemByGUID";
        final String   fileSystemGUIDParameter = "fileSystemGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/file-systems/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileSystemGUID, fileSystemGUIDParameter, methodName);

        FileSystemResponse restResult = restClient.callFileSystemGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             fileSystemGUID);

        return restResult.getFileSystem();
    }


    /**
     * Retrieve a FileSystemProperties asset by its unique name.
     *
     * @param userId calling user
     * @param uniqueName unique identifier used to locate the folder
     *
     * @return Filesystem properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public FileSystemElement getFileSystemByUniqueName(String userId,
                                                       String uniqueName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   methodName = "getFileSystemByUniqueName";
        final String   nameParameter = "uniqueName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/file-systems/by-name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameter, methodName);

        FileSystemResponse restResult = restClient.callFileSystemGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             uniqueName);

        return restResult.getFileSystem();
    }


    /**
     * Retrieve a list of defined FileSystems assets.
     *
     * @param userId calling user
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return List of Filesystem unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String> getFileSystems(String  userId,
                                       int     startingFrom,
                                       int     maxPageSize) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "getFileSystems";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/file-systems?startingFrom={2}&maximumResults={3}";

        invalidParameterHandler.validateUserId(userId, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         Integer.toString(startingFrom),
                                                                         Integer.toString(maxPageSize));

        return restResult.getGUIDs();
    }


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
    @Override
    public FolderElement getFolderByGUID(String   userId,
                                         String   folderGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   methodName = "getFileSystemByGUID";
        final String   folderGUIDParameter = "folderGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameter, methodName);

        FolderResponse restResult = restClient.callFolderGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
                                                                     userId,
                                                                     folderGUID);

        return restResult.getFolder();
    }


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
    @Override
    public FolderElement getFolderByPathName(String   userId,
                                             String   pathName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String   methodName = "getFileSystemByUniqueName";
        final String   nameParameter = "pathName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders/by-path-name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, nameParameter, methodName);

        FolderResponse restResult = restClient.callFolderGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
                                                                     userId,
                                                                     pathName);

        return restResult.getFolder();
    }


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param userId calling user
     * @param parentGUID unique identifier of the anchor folder or file system
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folder unique identifiers (null means no nested folders)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String>  getNestedFolders(String  userId,
                                          String  parentGUID,
                                          int     startingFrom,
                                          int     maxPageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "getNestedFolders";
        final String   anchorGUIDParameter = "anchorGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/anchor/{2}/folders?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, anchorGUIDParameter, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         parentGUID,
                                                                         Integer.toString(startingFrom),
                                                                         Integer.toString(maxPageSize));

        return restResult.getGUIDs();
    }


    /**
     * Get the files inside a folder - both those that are nested and those that are linked.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the anchor folder
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of file asset unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String>   getFolderFiles(String  userId,
                                         String  folderGUID,
                                         int     startingFrom,
                                         int     maxPageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   methodName = "getFolderFiles";
        final String   anchorGUIDParameter = "folderGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/folders/{2}/assets/data-files?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, anchorGUIDParameter, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         folderGUID,
                                                                         Integer.toString(startingFrom),
                                                                         Integer.toString(maxPageSize));

        return restResult.getGUIDs();
    }
}
