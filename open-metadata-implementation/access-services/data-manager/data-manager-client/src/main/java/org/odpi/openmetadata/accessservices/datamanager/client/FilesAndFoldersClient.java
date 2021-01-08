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
    private final String urlTemplatePrefix = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/filesystems";

    private String   serverName;               /* Initialized in constructor */
    private String   serverPlatformURLRoot;    /* Initialized in constructor */
    private AuditLog auditLog = null;          /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private DataManagerRESTClient   restClient;               /* Initialized in constructor */


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
    @Override
    public List<String> createNestedFolders(String userId,
                                            String fileManagerCapabilityGUID,
                                            String fileManagerCapabilityName,
                                            String parentGUID,
                                            String pathName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "createNestedFolders";
        final String parentGUIDParameterName     = "parentGUID";
        final String pathNameParameterName       = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateName(pathName, pathNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/parent/{2}";

        PathNameRequestBody requestBody = new PathNameRequestBody();

        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);
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
     * Links a folder to a file system. The folder is not changed.
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param fileSystemGUID unique identifier of the file system
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void attachTopLevelFolder(String userId,
                                     String fileManagerCapabilityGUID,
                                     String fileManagerCapabilityName,
                                     String fileSystemGUID,
                                     String folderGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName                  = "attachTopLevelFolder";
        final String fileSystemGUIDParameterName = "fileSystemGUID";
        final String folderGUIDParameterName     = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileSystemGUID, fileSystemGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/folders/{3}/attach";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        fileSystemGUID,
                                        folderGUID);
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param userId calling user
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param fileSystemGUID unique identifier of the file system
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void detachTopLevelFolder(String userId,
                                     String fileManagerCapabilityGUID,
                                     String fileManagerCapabilityName,
                                     String fileSystemGUID,
                                     String folderGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName                  = "detachTopLevelFolder";
        final String fileSystemGUIDParameterName = "fileSystemGUID";
        final String folderGUIDParameterName     = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileSystemGUID, fileSystemGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/folders/{3}/detach";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        fileSystemGUID,
                                        folderGUID);
    }


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
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String> addDataFileToCatalog(String             userId,
                                             String             fileManagerCapabilityGUID,
                                             String             fileManagerCapabilityName,
                                             DataFileProperties dataFileProperties,
                                             String             connectorProviderName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                  = "addDataFileToCatalog";
        final String propertiesParameterName     = "dataFileProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(dataFileProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(dataFileProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-files";

        DataFileRequestBody requestBody = new DataFileRequestBody(dataFileProperties);

        requestBody.setConnectorProviderClassName(connectorProviderName);
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId);

        return restResult.getGUIDs();
    }


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
    @Override
    public List<String> addDataFileToCatalogFromTemplate(String             userId,
                                                         String             fileManagerCapabilityGUID,
                                                         String             fileManagerCapabilityName,
                                                         String             templateGUID,
                                                         TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                  = "addDataFileToCatalogFromTemplate";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-files/from-template/{2}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          templateGUID);

        return restResult.getGUIDs();
    }


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
    @Override
    public void updateDataFileInCatalog(String             userId,
                                        String             fileManagerCapabilityGUID,
                                        String             fileManagerCapabilityName,
                                        String             dataFileGUID,
                                        boolean            isMergeUpdate,
                                        DataFileProperties dataFileProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                  = "updateDataFileInCatalog";
        final String dataFileGUIDParameterName   = "dataFileGUID";
        final String propertiesParameterName     = "dataFileProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFileGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(dataFileProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(dataFileProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-files/{2}?isMergeUpdate={3}";

        DataFileRequestBody requestBody = new DataFileRequestBody(dataFileProperties);
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFileGUID,
                                        isMergeUpdate);
    }


    /**
     * Mark the file asset description in the catalog as archived.
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
    @Override
    public void archiveDataFileInCatalog(String            userId,
                                         String            fileManagerCapabilityGUID,
                                         String            fileManagerCapabilityName,
                                         String            dataFileGUID,
                                         ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                = "archiveDataFileInCatalog";
        final String dataFileGUIDParameterName = "dataFileGUID";
        final String propertiesParameterName   = "archiveProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFileGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(archiveProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-files/{2}/archive";

        ArchiveRequestBody requestBody = new ArchiveRequestBody(archiveProperties);
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFileGUID);
    }


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
    @Override
    public void deleteDataFileFromCatalog(String userId,
                                          String fileManagerCapabilityGUID,
                                          String fileManagerCapabilityName,
                                          String dataFileGUID,
                                          String fullPathname) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                 = "deleteDataFileFromCatalog";
        final String dataFileGUIDParameterName  = "dataFileGUID";
        final String qualifiedNameParameterName = "fullPathname";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFileGUID, dataFileGUIDParameterName, methodName);
        invalidParameterHandler.validateName(fullPathname, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-files/{2}/delete";

        PathNameRequestBody requestBody = new PathNameRequestBody();

        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);
        requestBody.setFullPath(fullPathname);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFileGUID);
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
    @Override
    public List<String> addDataFolderToCatalog(String               userId,
                                               String               fileManagerCapabilityGUID,
                                               String               fileManagerCapabilityName,
                                               FileFolderProperties fileFolderProperties,
                                               String               connectorProviderName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                  = "addDataFolderToCatalog";
        final String propertiesParameterName     = "fileFolderProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
         invalidParameterHandler.validateObject(fileFolderProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(fileFolderProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-folders";

        DataFolderRequestBody requestBody = new DataFolderRequestBody(fileFolderProperties);

        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);
        requestBody.setConnectorProviderClassName(connectorProviderName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId);

        return restResult.getGUIDs();
    }


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
    @Override
    public List<String> addDataFolderToCatalogFromTemplate(String             userId,
                                                           String             fileManagerCapabilityGUID,
                                                           String             fileManagerCapabilityName,
                                                           String             templateGUID,
                                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                  = "addDataFolderToCatalogFromTemplate";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-folders/from-template/{2}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          templateGUID);

        return restResult.getGUIDs();
    }


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
    @Override
    public void updateDataFolderInCatalog(String               userId,
                                          String               fileManagerCapabilityGUID,
                                          String               fileManagerCapabilityName,
                                          String               dataFolderGUID,
                                          boolean              isMergeUpdate,
                                          FileFolderProperties fileFolderProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                  = "updateDataFolderInCatalog";
        final String dataFileGUIDParameterName   = "dataFolderGUID";
        final String propertiesParameterName     = "fileFolderProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFolderGUID, dataFileGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(fileFolderProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(fileFolderProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-folders/{2}?isMergeUpdate={3}";

        DataFolderRequestBody requestBody = new DataFolderRequestBody(fileFolderProperties);
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFolderGUID,
                                        isMergeUpdate);
    }


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
    @Override
    public void archiveDataFolderInCatalog(String            userId,
                                           String            fileManagerCapabilityGUID,
                                           String            fileManagerCapabilityName,
                                           String            dataFolderGUID,
                                           ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                = "archiveDataFileInCatalog";
        final String dataFileGUIDParameterName = "dataFolderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFolderGUID, dataFileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-folders/{2}/archive";

        ArchiveRequestBody requestBody = new ArchiveRequestBody(archiveProperties);
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFolderGUID);
    }


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
    @Override
    public void deleteDataFolderFromCatalog(String userId,
                                            String fileManagerCapabilityGUID,
                                            String fileManagerCapabilityName,
                                            String dataFolderGUID,
                                            String fullPathname) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName                  = "deleteDataFolderFromCatalog";
        final String guidParameterName           = "dataFolderGUID";
        final String qualifiedNameParameterName  = "fullPathname";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFolderGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(fullPathname, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-folders/{2}/delete";
        PathNameRequestBody requestBody = new PathNameRequestBody();

        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);
        requestBody.setFullPath(fullPathname);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFolderGUID);
    }
    
    
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
    @Override
    public void attachDataFileAssetToFolder(String userId,
                                            String fileManagerCapabilityGUID,
                                            String fileManagerCapabilityName,
                                            String folderGUID,
                                            String fileGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName              = "attachDataFileAssetToFolder";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName   = "fileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/{4}/data-files/{5}/attach";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
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
     * @param fileManagerCapabilityGUID unique identifier of the software server capability representing an owning external file manager or null
     * @param fileManagerCapabilityName unique name of the software server capability representing an owning external file manager or null
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void detachDataFileAssetFromFolder(String userId,
                                              String fileManagerCapabilityGUID,
                                              String fileManagerCapabilityName,
                                              String folderGUID,
                                              String fileGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName              = "detachDataFileAssetFromFolder";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName   = "fileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/{4}/data-files/{5}/detach";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        folderGUID,
                                        fileGUID);
    }


    /**
     * Move a data file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
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
    @Override
    public void moveDataFileInCatalog(String userId,
                                      String fileManagerCapabilityGUID,
                                      String fileManagerCapabilityName,
                                      String folderGUID,
                                      String fileGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName              = "moveDataFileInCatalog";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName   = "fileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(fileGUID, fileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/{4}/data-files/{5}/move-to";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        folderGUID,
                                        fileGUID);
    }


    /**
     * Move a data folder from its current parent folder to a new parent folder - this changes the folder's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
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
    @Override
    public void moveDataFolderInCatalog(String userId,
                                        String fileManagerCapabilityGUID,
                                        String fileManagerCapabilityName,
                                        String folderGUID,
                                        String dataFolderGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                  = "moveDataFileInCatalog";
        final String folderGUIDParameterName     = "folderGUID";
        final String dataFolderGUIDParameterName = "dataFolderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, folderGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataFolderGUID, dataFolderGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/{4}/data-folders/{5}/move-to";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(fileManagerCapabilityGUID);
        requestBody.setExternalSourceName(fileManagerCapabilityName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        folderGUID,
                                        dataFolderGUID);
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
    @Override
    public FileFolderElement getFolderByGUID(String   userId,
                                             String   folderGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName        = "getFolderByGUID";
        final String guidParameterName = "folderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/{2}";

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
    @Override
    public FileFolderElement getFolderByPathName(String   userId,
                                                 String   pathName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName        = "getFolderByPathName";
        final String nameParameterName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/by-path-name";

        PathNameRequestBody requestBody = new PathNameRequestBody();

        requestBody.setFullPath(pathName);

        FileFolderResponse restResult = restClient.callFileFolderPostRESTCall(methodName,
                                                                              urlTemplate,
                                                                              requestBody,
                                                                              serverName,
                                                                              userId);

        return restResult.getFolder();
    }


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
    @Override
    public List<FileFolderElement>  getTopLevelFolders(String userId,
                                                       String fileSystemGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getTopLevelFolders";
        final String fileSystemGUIDParameterName = "fileSystemGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileSystemGUID, fileSystemGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/folders?startFrom={3}&pageSize={4}";

        FileFoldersResponse restResult = restClient.callFileFoldersGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             fileSystemGUID,
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
    @Override
    public List<FileFolderElement>  getNestedFolders(String  userId,
                                                     String  parentFolderGUID,
                                                     int     startFrom,
                                                     int     pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName        = "getNestedFolders";
        final String guidParameterName = "parentFolderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentFolderGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/{2}/folders?startFrom={3}&pageSize={4}";

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
    @Override
    public List<DataFileElement>  getTopLevelDataFiles(String userId,
                                                       String fileSystemGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getTopLevelDataFiles";
        final String fileSystemGUIDParameterName = "fileSystemGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileSystemGUID, fileSystemGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/data-files?startFrom={3}&pageSize={4}";

        DataFilesResponse restResult = restClient.callDataFilesGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           fileSystemGUID,
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
    @Override
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


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/folders/{2}/data-files?startFrom={3}&pageSize={4}";

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
    @Override
    public DataFileElement getFileByGUID(String userId,
                                         String fileGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName        = "getFileByGUID";
        final String guidParameterName = "fileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fileGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-files/{2}";

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
    @Override
    public DataFileElement getFileByPathName(String   userId,
                                             String   pathName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName        = "getFileByPathName";
        final String nameParameterName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-files/by-path-name";

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


    /**
     * Retrieve data files by the supplied wildcard name.  The wildcard is specified using regular expressions (RegEx).
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
    @Override
    public List<DataFileElement> findFilesByPathName(String userId,
                                                     String pathName,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName        = "findFilesByPathName";
        final String nameParameterName = "pathName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(pathName, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-files/by-search-path-name?startFrom={3}&pageSize={4}";

        PathNameRequestBody requestBody = new PathNameRequestBody();

        requestBody.setFullPath(pathName);

        DataFilesResponse restResult = restClient.callDataFilesPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            pathName,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }
}
