/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.FilesAndFoldersInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DataFileElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * FilesAndFoldersClient is the client for managing the creation of files and folder assets.
 */
public class FilesAndFoldersClient implements FilesAndFoldersInterface
{
    private final String fileServerCapabilityGUIDParameterName = "fileServerCapabilityGUID";
    private final String fileServerCapabilityNameParameterName = "FileServerCapabilityName";
    private final String editURLTemplatePrefix       = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/file-server-capability/{2}/{3}";
    private final String retrieveURLTemplatePrefix   = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}";
    private final String governanceURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}";

    private String   serverName;               /* Initialized in constructor */
    private String   serverPlatformURLRoot;    /* Initialized in constructor */
    private AuditLog auditLog = null;          /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private DataManagerRESTClient   restClient;               /* Initialized in constructor */

    private static NullRequestBody nullRequestBody   = new NullRequestBody();
    


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FilesAndFoldersClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FilesAndFoldersClient(String serverName,
                                 String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        
        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FilesAndFoldersClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 String   userId,
                                 String   password,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;
        
        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public FilesAndFoldersClient(String serverName,
                                 String serverPlatformURLRoot,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        
        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is to be used within an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public FilesAndFoldersClient(String                serverName,
                                 String                serverPlatformURLRoot,
                                 DataManagerRESTClient restClient,
                                 int                   maxPageSize,
                                 AuditLog              auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;
        
        this.restClient = restClient;
    }


    /*============================================================================================================
     * Start of interface methods
     */

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
    public List<String> createNestedFolders(String userId,
                                            String fileServerCapabilityGUID,
                                            String fileServerCapabilityName,
                                            String pathName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "createNestedFolders";
        final String pathNameParameterName       = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        invalidParameterHandler.validateName(pathName, pathNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/folders/top-level";

        PathNameRequestBody requestBody = new PathNameRequestBody();

        requestBody.setFullPath(pathName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          fileServerCapabilityGUID,
                                                                          fileServerCapabilityName);

        return restResult.getGUIDs();
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the parent entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param fileServerCapabilityGUID unique identifier of the software server capability representing the file system or file manager
     * @param fileServerCapabilityName unique name of the software server capability representing the file system or file manager
     * @param parentFolderGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> createNestedFolders(String userId,
                                            String fileServerCapabilityGUID,
                                            String fileServerCapabilityName,
                                            String parentFolderGUID,
                                            String pathName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "createNestedFolders";
        final String parentGUIDParameterName     = "parentFolderGUID";
        final String pathNameParameterName       = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        invalidParameterHandler.validateGUID(parentFolderGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateName(pathName, pathNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/folders/{4}/nested-folders";

        PathNameRequestBody requestBody = new PathNameRequestBody();

        requestBody.setFullPath(pathName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          fileServerCapabilityGUID,
                                                                          fileServerCapabilityName,
                                                                          parentFolderGUID);

        return restResult.getGUIDs();
    }


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
    public void attachTopLevelFolder(String userId,
                                     String fileServerCapabilityGUID,
                                     String fileServerCapabilityName,
                                     String folderGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName                  = "attachTopLevelFolder";
        final String folderGUIDParameterName     = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/folders/top-level/{4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        fileServerCapabilityGUID,
                                        fileServerCapabilityName);
    }


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
    public void detachTopLevelFolder(String userId,
                                     String fileServerCapabilityGUID,
                                     String fileServerCapabilityName,
                                     String folderGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName                  = "detachTopLevelFolder";
        final String folderGUIDParameterName     = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/folders/top-level/{4}/detach";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        fileServerCapabilityGUID,
                                        fileServerCapabilityName);
    }


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
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFileToCatalog(String             userId,
                                             String             fileServerCapabilityGUID,
                                             String             fileServerCapabilityName,
                                             DataFileProperties dataFileProperties,
                                             String             connectorProviderName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                  = "addDataFileToCatalog";
        final String propertiesParameterName     = "dataFileProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        invalidParameterHandler.validateObject(dataFileProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(dataFileProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/files";

        DataFileRequestBody requestBody = new DataFileRequestBody(dataFileProperties);

        requestBody.setConnectorProviderClassName(connectorProviderName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          fileServerCapabilityGUID,
                                                                          fileServerCapabilityName);

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
    public List<String> addDataFolderToCatalog(String               userId,
                                               String               fileServerCapabilityGUID,
                                               String               fileServerCapabilityName,
                                               FileFolderProperties fileFolderProperties,
                                               String               connectorProviderName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                  = "addDataFolderToCatalog";
        final String propertiesParameterName     = "fileFolderProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        invalidParameterHandler.validateObject(fileFolderProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(fileFolderProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/files";

        DataFolderRequestBody requestBody = new DataFolderRequestBody(fileFolderProperties);

        requestBody.setConnectorProviderClassName(connectorProviderName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          fileServerCapabilityGUID,
                                                                          fileServerCapabilityName);

        return restResult.getGUIDs();
    }


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
    public void attachDataFileAssetToFolder(String userId,
                                            String fileServerCapabilityGUID,
                                            String fileServerCapabilityName,
                                            String folderGUID,
                                            String fileGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName              = "attachDataFileAssetToFolder";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName   = "fileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/folders/{4}/files/{5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        fileServerCapabilityGUID,
                                        fileServerCapabilityName,
                                        folderGUID,
                                        fileGUID);
    }


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
    public void detachDataFileAssetFromFolder(String userId,
                                              String fileServerCapabilityGUID,
                                              String fileServerCapabilityName,
                                              String folderGUID,
                                              String fileGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName              = "detachDataFileAssetFromFolder";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName   = "fileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/folders/{4}/files/{5}/detach";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        fileServerCapabilityGUID,
                                        fileServerCapabilityName,
                                        folderGUID,
                                        fileGUID);
    }


    /**
     * Retrieve a FolderProperties asset by its unique identifier (GUID).
     *
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return Folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FileFolderElement getFolderByGUID(String   userId,
                                             String   folderGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName        = "getFolderByGUID";
        final String guidParameterName = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, guidParameterName, methodName);

        final String urlTemplate = retrieveURLTemplatePrefix + "/folders/{2}";

        FileFolderResponse restResult = restClient.callFileFolderGetRESTCall(methodName,
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
    public FileFolderElement getFolderByPathName(String   userId,
                                                 String   pathName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName        = "getFolderByPathName";
        final String nameParameterName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, nameParameterName, methodName);

        final String urlTemplate = retrieveURLTemplatePrefix + "/folders/by-name";

        PathNameRequestBody requestBody = new PathNameRequestBody();

        requestBody.setFullPath(pathName);

        FileFolderResponse restResult = restClient.callFileFolderPostRESTCall(methodName,
                                                                              urlTemplate,
                                                                              requestBody,
                                                                              serverName,
                                                                              userId,
                                                                              pathName);

        return restResult.getFolder();
    }


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
    public List<FileFolderElement>  getTopLevelFolders(String userId,
                                                       String fileServerCapabilityGUID,
                                                       String fileServerCapabilityName,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getTopLevelFolders";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = retrieveURLTemplatePrefix + "/folders?startFrom={3}&pageSize={4}";

        FileFoldersResponse restResult = restClient.callFileFoldersGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             startFrom,
                                                                             validatedPageSize);

        return restResult.getElementList();
    }


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
    public List<FileFolderElement>  getNestedFolders(String  userId,
                                                     String  parentFolderGUID,
                                                     int     startFrom,
                                                     int     pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName        = "getFolderFiles";
        final String guidParameterName = "parentFolderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentFolderGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


        final String urlTemplate = retrieveURLTemplatePrefix + "/folders/{2}/folders?startFrom={3}&pageSize={4}";

        FileFoldersResponse restResult = restClient.callFileFoldersGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               parentFolderGUID,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElementList();
    }


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
    public List<DataFileElement>  getTopLevelDataFiles(String userId,
                                                       String fileServerCapabilityGUID,
                                                       String fileServerCapabilityName,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getTopLevelDataFiles";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileServerCapabilityGUID, fileServerCapabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fileServerCapabilityName, fileServerCapabilityNameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = retrieveURLTemplatePrefix + "/files?startFrom={3}&pageSize={4}";

        DataFilesResponse restResult = restClient.callDataFilesGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           startFrom,
                                                                           validatedPageSize);

        return restResult.getElementList();
    }


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
    public List<DataFileElement> getFolderFiles(String  userId,
                                                String  folderGUID,
                                                int     startFrom,
                                                int     pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName        = "getFolderFiles";
        final String guidParameterName = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


        final String urlTemplate = retrieveURLTemplatePrefix + "/folders/{2}/files?startFrom={3}&pageSize={4}";

        DataFilesResponse restResult = restClient.callDataFilesGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           folderGUID,
                                                                           startFrom,
                                                                           validatedPageSize);

        return restResult.getElementList();
    }


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
    public DataFileElement getFileByGUID(String userId,
                                         String fileGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName        = "getFileByGUID";
        final String guidParameterName = "fileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileGUID, guidParameterName, methodName);

        final String urlTemplate = retrieveURLTemplatePrefix + "/files/{2}";

        DataFileResponse restResult = restClient.callDataFileGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         fileGUID);

        return restResult.getDataFile();
    }


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
    public DataFileElement getFileByPathName(String   userId,
                                             String   pathName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName        = "getFileByPathName";
        final String nameParameterName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, nameParameterName, methodName);

        final String urlTemplate = retrieveURLTemplatePrefix + "/files/by-name";

        PathNameRequestBody requestBody = new PathNameRequestBody();
        
        requestBody.setFullPath(pathName);

        DataFileResponse restResult = restClient.callDataFilePostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          pathName);

        return restResult.getDataFile();
    }
}
