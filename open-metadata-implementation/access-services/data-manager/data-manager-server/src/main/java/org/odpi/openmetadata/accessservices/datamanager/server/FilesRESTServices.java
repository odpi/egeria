/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * FilesRESTServices provides the server-side implementation for managing files and folder assets in a
 * file system.  Notice that the integrator identifiers are not specified for the file system because the files can be
 * changed by so many processes.  If the files are being controlled by an application then use the ContentManagerRESTServices
 */
public class FilesRESTServices
{
    private static final DataManagerInstanceHandler   instanceHandler     = new DataManagerInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(FilesRESTServices.class),
                                                                                  instanceHandler.getServiceName());

    private final RESTExceptionHandler    restExceptionHandler    = new RESTExceptionHandler();
    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Default constructor
     */
    public FilesRESTServices()
    {
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the parent entity.
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
    public GUIDListResponse createFolderStructureInCatalog(String              serverName,
                                                           String              userId,
                                                           String              parentGUID,
                                                           PathNameRequestBody requestBody)
    {
        final String requestBodyParameterName = "requestBody";
        final String methodName = "createFolderStructureInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            invalidParameterHandler.validateObject(requestBody, requestBodyParameterName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.createFolderStructureInCatalog(userId,
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     parentGUID,
                                                                     requestBody.getPathName(),
                                                                     null,
                                                                     null,
                                                                     false,
                                                                     false,
                                                                     new Date(),
                                                                     methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse attachFolderToFileSystem(String                    serverName,
                                                 String                    userId,
                                                 String                    fileSystemGUID,
                                                 String                    folderGUID,
                                                 ExternalSourceRequestBody requestBody)
    {
        final String methodName                  = "attachFolderToFileSystem";
        final String fileSystemGUIDParameterName = "fileSystemGUID";
        final String folderGUIDParameterName     = "folderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            String externalSourceGUID = null;
            String externalSourceName = null;

            if (requestBody != null)
            {
                externalSourceGUID = requestBody.getExternalSourceGUID();
                externalSourceName = requestBody.getExternalSourceName();
            }

            handler.attachFolderToFileSystem(userId,
                                             externalSourceGUID,
                                             externalSourceName,
                                             fileSystemGUID,
                                             fileSystemGUIDParameterName,
                                             folderGUID,
                                             folderGUIDParameterName,
                                             null,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse detachFolderFromFileSystem(String                    serverName,
                                                   String                    userId,
                                                   String                    fileSystemGUID,
                                                   String                    folderGUID,
                                                   MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachFolderFromFileSystem";
        final String fileSystemGUIDParameterName = "fileSystemGUID";
        final String folderGUIDParameterName     = "folderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            String externalSourceGUID = null;
            String externalSourceName = null;

            if (requestBody != null)
            {
                externalSourceGUID = requestBody.getExternalSourceGUID();
                externalSourceName = requestBody.getExternalSourceName();
            }

            handler.detachFolderFromFileSystem(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               fileSystemGUID,
                                               fileSystemGUIDParameterName,
                                               folderGUID,
                                               folderGUIDParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDListResponse addDataFileToCatalog(String              serverName,
                                                 String              userId,
                                                 DataFileRequestBody requestBody)
    {
        final String methodName = "addDataFileToCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                String pathName = requestBody.getPathName();

                if (pathName == null)
                {
                    pathName = requestBody.getQualifiedName();
                }

                response.setGUIDs(handler.addFileToCatalog(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           requestBody.getQualifiedName(),
                                                           requestBody.getName(),
                                                           requestBody.getResourceName(),
                                                           requestBody.getVersionIdentifier(),
                                                           requestBody.getResourceDescription(),
                                                           pathName,
                                                           requestBody.getCreateTime(),
                                                           requestBody.getModifiedTime(),
                                                           requestBody.getEncodingType(),
                                                           requestBody.getEncodingLanguage(),
                                                           requestBody.getEncodingDescription(),
                                                           requestBody.getEncodingProperties(),
                                                           requestBody.getFileName(),
                                                           requestBody.getFileType(),
                                                           requestBody.getFileExtension(),
                                                           requestBody.getDeployedImplementationType(),
                                                           requestBody.getAdditionalProperties(),
                                                           requestBody.getConnectorProviderClassName(),
                                                           requestBody.getTypeName(),
                                                           requestBody.getExtendedProperties(),
                                                           null,
                                                           null,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDListResponse addDataFileToCatalogFromTemplate(String              serverName,
                                                             String              userId,
                                                             String              templateGUID,
                                                             TemplateRequestBody requestBody)
    {
        final String methodName = "addDataFileToCatalogFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                String pathName = requestBody.getPathName();

                if (pathName == null)
                {
                    pathName = requestBody.getQualifiedName();
                }

                response.setGUIDs(handler.addFileToCatalogFromTemplate(userId,
                                                                       requestBody.getExternalSourceGUID(),
                                                                       requestBody.getExternalSourceName(),
                                                                       templateGUID,
                                                                       requestBody.getQualifiedName(),
                                                                       pathName,
                                                                       requestBody.getDisplayName(),
                                                                       requestBody.getVersionIdentifier(),
                                                                       requestBody.getDescription(),
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateDataFileInCatalog(String              serverName,
                                                String              userId,
                                                String              dataFileGUID,
                                                boolean             isMergeUpdate,
                                                DataFileRequestBody requestBody)
    {
        final String methodName = "updateDataFileInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                handler.updateFileInCatalog(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            dataFileGUID,
                                            isMergeUpdate,
                                            requestBody.getQualifiedName(),
                                            requestBody.getName(),
                                            requestBody.getResourceName(),
                                            requestBody.getVersionIdentifier(),
                                            requestBody.getResourceDescription(),
                                            requestBody.getCreateTime(),
                                            requestBody.getModifiedTime(),
                                            requestBody.getEncodingType(),
                                            requestBody.getEncodingLanguage(),
                                            requestBody.getEncodingDescription(),
                                            requestBody.getEncodingProperties(),
                                            requestBody.getFileName(),
                                            requestBody.getFileType(),
                                            requestBody.getFileExtension(),
                                            requestBody.getDeployedImplementationType(),
                                            requestBody.getAdditionalProperties(),
                                            requestBody.getExtendedProperties(),
                                            null,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse archiveDataFileInCatalog(String             serverName,
                                                 String             userId,
                                                 String             dataFileGUID,
                                                 ArchiveRequestBody requestBody)
    {
        final String methodName                = "archiveDataFileInCatalog";
        final String dataFileGUIDParameterName = "dataFileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                handler.archiveFileInCatalog(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            dataFileGUID,
                                            dataFileGUIDParameterName,
                                            requestBody.getArchiveDate(),
                                            requestBody.getArchiveProcess(),
                                            requestBody.getArchiveProperties(),
                                             false,
                                             new Date(),
                                            methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse deleteDataFileFromCatalog(String              serverName,
                                                  String              userId,
                                                  String              dataFileGUID,
                                                  PathNameRequestBody requestBody)
    {
        final String methodName = "deleteDataFileInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                handler.deleteFileFromCatalog(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              dataFileGUID,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
     * @param requestBody pathname of the file
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public GUIDListResponse addDataFolderAssetToCatalog(String                serverName,
                                                        String                userId,
                                                        DataFolderRequestBody requestBody)
    {
        final String methodName = "addDataFolderAssetToCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);


                response.setGUIDs(handler.addDataFolderAssetToCatalog(userId,
                                                                      requestBody.getExternalSourceGUID(),
                                                                      requestBody.getExternalSourceName(),
                                                                      requestBody.getQualifiedName(),
                                                                      requestBody.getPathName(),
                                                                      requestBody.getName(),
                                                                      requestBody.getResourceName(),
                                                                      requestBody.getVersionIdentifier(),
                                                                      requestBody.getResourceDescription(),
                                                                      requestBody.getDeployedImplementationType(),
                                                                      requestBody.getCreateTime(),
                                                                      requestBody.getModifiedTime(),
                                                                      requestBody.getEncodingType(),
                                                                      requestBody.getEncodingLanguage(),
                                                                      requestBody.getEncodingDescription(),
                                                                      requestBody.getEncodingProperties(),
                                                                      requestBody.getAdditionalProperties(),
                                                                      requestBody.getConnectorProviderClassName(),
                                                                      requestBody.getTypeName(),
                                                                      requestBody.getExtendedProperties(),
                                                                      requestBody.getEffectiveFrom(),
                                                                      requestBody.getEffectiveTo(),
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDListResponse addDataFolderToCatalogFromTemplate(String              serverName,
                                                               String              userId,
                                                               String              templateGUID,
                                                               TemplateRequestBody requestBody)
    {
        final String methodName = "addDataFolderToCatalogFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUIDs(handler.addFolderToCatalogFromTemplate(userId,
                                                                         requestBody.getExternalSourceGUID(),
                                                                         requestBody.getExternalSourceName(),
                                                                         templateGUID,
                                                                         requestBody.getQualifiedName(),
                                                                         requestBody.getPathName(),
                                                                         requestBody.getDisplayName(),
                                                                         requestBody.getVersionIdentifier(),
                                                                         requestBody.getDescription(),
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateDataFolderInCatalog(String                serverName,
                                                  String                userId,
                                                  String                dataFolderGUID,
                                                  boolean               isMergeUpdate,
                                                  DataFolderRequestBody requestBody)
    {
        final String methodName = "updateDataFolderInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                handler.updateFolderInCatalog(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              dataFolderGUID,
                                              isMergeUpdate,
                                              requestBody.getPathName(),
                                              requestBody.getName(),
                                              requestBody.getResourceName(),
                                              requestBody.getVersionIdentifier(),
                                              requestBody.getResourceDescription(),
                                              requestBody.getCreateTime(),
                                              requestBody.getModifiedTime(),
                                              requestBody.getEncodingType(),
                                              requestBody.getEncodingLanguage(),
                                              requestBody.getEncodingDescription(),
                                              requestBody.getEncodingProperties(),
                                              requestBody.getAdditionalProperties(),
                                              requestBody.getExtendedProperties(),
                                              null,
                                              null,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse archiveDataFolderInCatalog(String             serverName,
                                                   String             userId,
                                                   String             dataFolderGUID,
                                                   ArchiveRequestBody requestBody)
    {
        final String methodName                  = "archiveDataFolderInCatalog";
        final String dataFolderGUIDParameterName = "dataFolderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                handler.archiveFolderInCatalog(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               dataFolderGUID,
                                               dataFolderGUIDParameterName,
                                               requestBody.getArchiveDate(),
                                               requestBody.getArchiveProcess(),
                                               requestBody.getArchiveProperties(),
                                               false,
                                               new Date(),
                                               methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse deleteDataFolderFromCatalog(String              serverName,
                                                    String              userId,
                                                    String              dataFolderGUID,
                                                    PathNameRequestBody requestBody)
    {
        final String methodName = "deleteDataFolderInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                handler.deleteFolderFromCatalog(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                dataFolderGUID,
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse  attachDataFileAssetToFolder(String                    serverName,
                                                     String                    userId,
                                                     String                    folderGUID,
                                                     String                    fileGUID,
                                                     MetadataSourceRequestBody requestBody)
    {
        final String methodName = "attachDataFileAssetToFolder";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName = "fileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            String externalSourceGUID = null;
            String externalSourceName = null;

            if (requestBody != null)
            {
                externalSourceGUID = requestBody.getExternalSourceGUID();
                externalSourceName = requestBody.getExternalSourceName();
            }

            handler.attachDataFileAssetToFolder(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                folderGUID,
                                                folderGUIDParameterName,
                                                fileGUID,
                                                fileGUIDParameterName,
                                                null,
                                                null,
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse  detachDataFileAssetFromFolder(String                    serverName,
                                                       String                    userId,
                                                       String                    folderGUID,
                                                       String                    fileGUID,
                                                       MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachDataFileAssetFromFolder";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName = "fileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            String externalSourceGUID = null;
            String externalSourceName = null;

            if (requestBody != null)
            {
                externalSourceGUID = requestBody.getExternalSourceGUID();
                externalSourceName = requestBody.getExternalSourceName();
            }

            handler.detachDataFileAssetFromFolder(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  folderGUID,
                                                  folderGUIDParameterName,
                                                  fileGUID,
                                                  fileGUIDParameterName,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Move a data file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Similarly to the endpoint in the connection object.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param fileGUID unique identifier of the file to move
     * @param requestBody  request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  moveDataFileInCatalog(String                    serverName,
                                               String                    userId,
                                               String                    folderGUID,
                                               String                    fileGUID,
                                               MetadataSourceRequestBody requestBody)
    {
        final String methodName              = "moveDataFileInCatalog";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName   = "fileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            String externalSourceGUID = null;
            String externalSourceName = null;

            if (requestBody != null)
            {
                externalSourceGUID = requestBody.getExternalSourceGUID();
                externalSourceName = requestBody.getExternalSourceName();
            }

            handler.moveDataFileInCatalog(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          folderGUID,
                                          folderGUIDParameterName,
                                          fileGUID,
                                          fileGUIDParameterName,
                                          null,
                                          null,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Move a data folder from its current parent folder to a new parent folder - this changes the folder's qualified name
     * but not its unique identifier (guid).  Similarly to the endpoint in the connection object.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param newParentFolderGUID new parent folder
     * @param movingFolderGUID unique identifier of the file to move
     * @param requestBody external source identifiers - or null for local
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  moveDataFolderInCatalog(String                    serverName,
                                                 String                    userId,
                                                 String                    newParentFolderGUID,
                                                 String                    movingFolderGUID,
                                                 MetadataSourceRequestBody requestBody)
    {
        final String methodName = "moveDataFileInCatalog";
        final String newParentFolderGUIDParameterName = "newParentFolderGUID";
        final String movingFolderGUIDParameterName = "movingFolderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            String externalSourceGUID = null;
            String externalSourceName = null;

            if (requestBody != null)
            {
                externalSourceGUID = requestBody.getExternalSourceGUID();
                externalSourceName = requestBody.getExternalSourceName();
            }

            handler.moveDataFolderInCatalog(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            newParentFolderGUID,
                                            newParentFolderGUIDParameterName,
                                            movingFolderGUID,
                                            movingFolderGUIDParameterName,
                                            null,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public FileSystemResponse getFileSystemByGUID(String   serverName,
                                                  String   userId,
                                                  String   fileSystemGUID)
    {
        final String methodName = "getFileSystemByGUID";
        final String guidParameterName = "fileSystemGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FileSystemResponse response = new FileSystemResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            FileSystemElement element = handler.getFileSystemByGUID(userId,
                                                                    fileSystemGUID,
                                                                    guidParameterName,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);
            response.setElement(element);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a FileSystemProperties asset by its unique name.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param uniqueName unique name used to locate the file system
     *
     * @return Filesystem properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public FileSystemResponse getFileSystemByUniqueName(String   serverName,
                                                        String   userId,
                                                        String   uniqueName)
    {
        final String methodName = "getFileSystemByUniqueName";
        final String parameterName = "uniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FileSystemResponse response = new FileSystemResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            FileSystemElement element = handler.getFileSystemByUniqueName(userId,
                                                                          uniqueName,
                                                                          parameterName,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName);

            response.setElement(element);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDListResponse getFileSystems(String  serverName,
                                           String  userId,
                                           int     startingFrom,
                                           int     maxPageSize)
    {
        final String methodName = "getFileSystems";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getFileSystems(userId,
                                                     startingFrom,
                                                     maxPageSize,
                                                     false,
                                                     false,
                                                     new Date(),
                                                     methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve a FileFolderProperties asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return FileFolderProperties properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public FileFolderResponse getFolderByGUID(String   serverName,
                                              String   userId,
                                              String   folderGUID)
    {
        final String methodName = "getFolderByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FileFolderResponse response = new FileFolderResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            FileFolderElement fileFolder = handler.getFolderByGUID(userId,
                                                                   folderGUID,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                   methodName);
            response.setElement(fileFolder);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody path name
     *
     * @return FileFolderProperties properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public FileFolderResponse getFolderByPathName(String                serverName,
                                                  String                userId,
                                                  PathNameRequestBody   requestBody)
    {
        final String methodName = "getFolderByPathName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FileFolderResponse response = new FileFolderResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                FileFolderElement fileFolder = handler.getFolderByPathName(userId,
                                                                           requestBody.getPathName(),
                                                                           false,
                                                                           false,
                                                                           new Date(),
                                                                           methodName);
                response.setElement(fileFolder);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of folders nested inside a filesystem.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier of the Filesystem to query
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folder unique identifiers (null means 'no nested folders') or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public GUIDListResponse  getTopLevelFolders(String  serverName,
                                                String  userId,
                                                String  fileSystemGUID,
                                                int     startingFrom,
                                                int     maxPageSize)
    {
        final String methodName = "getTopLevelFolders";
        final String guidParameterName = "fileSystemGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getTopLevelFolders(userId,
                                                         fileSystemGUID,
                                                         guidParameterName,
                                                         startingFrom,
                                                         maxPageSize,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Return the list of folders nested inside a folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param parentFolderGUID unique identifier of the folder to query
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folder unique identifiers (null means "no nested folders") or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public GUIDListResponse  getNestedFolders(String  serverName,
                                              String  userId,
                                              String  parentFolderGUID,
                                              int     startingFrom,
                                              int     maxPageSize)
    {
        final String methodName = "getNestedFolders";
        final String guidParameterName = "parentFolderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getNestedFolders(userId,
                                                       parentFolderGUID,
                                                       guidParameterName,
                                                       startingFrom,
                                                       maxPageSize,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Get the files inside a folder - both those that are nested and those that are linked.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier of the folder to query
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of file assets or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public DataFilesResponse  getFolderFiles(String  serverName,
                                             String  userId,
                                             String  folderGUID,
                                             int     startingFrom,
                                             int     maxPageSize)
    {
        final String methodName = "getFolderFiles";
        final String guidParameterName = "folderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFilesResponse response = new DataFilesResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setElements(handler.getFolderFiles(userId,
                                                        folderGUID,
                                                        guidParameterName,
                                                        startingFrom,
                                                        maxPageSize,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a data file asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileGUID unique identifier used to locate the file
     *
     * @return data file properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public DataFileResponse getDataFileByGUID(String   serverName,
                                              String   userId,
                                              String   fileGUID)
    {
        final String methodName = "getDataFileByGUID";
        final String guidParameterName = "fileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFileResponse response = new DataFileResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            DataFileElement dataFile = handler.getDataFileByGUID(userId,
                                                                 fileGUID,
                                                                 guidParameterName,
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName);
            response.setDataFile(dataFile);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a data file asset by its fully qualified path name.
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
    public DataFileResponse getDataFileByPathName(String                serverName,
                                                  String                userId,
                                                  PathNameRequestBody   requestBody)
    {
        final String methodName        = "getDataFileByPathName";
        final String nameParameterName = "fullPath";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFileResponse response = new DataFileResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                DataFileElement dataFile = handler.getDataFileByPathName(userId,
                                                                         requestBody.getPathName(),
                                                                         nameParameterName,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName);

                response.setDataFile(dataFile);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Find data file by a full or partial path name. The wildcard is specified using regular expressions (RegEx) and the method matches on the
     * pathName property.
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
    public DataFilesResponse getDataFilesByPathName(String              serverName,
                                                    String              userId,
                                                    int                 startingFrom,
                                                    int                 maxPageSize,
                                                    PathNameRequestBody requestBody)
    {
        final String methodName        = "getDataFileByPathName";
        final String nameParameterName = "fullPath";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFilesResponse response = new DataFilesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                List<DataFileElement> dataFiles = handler.findDataFilesByPathName(userId,
                                                                                  requestBody.getPathName(),
                                                                                  nameParameterName,
                                                                                  startingFrom,
                                                                                  maxPageSize,
                                                                                  false,
                                                                                  false,
                                                                                  new Date(),
                                                                                  methodName);

                response.setElements(dataFiles);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DataFilesResponse findDataFilesByPathName(String              serverName,
                                                     String              userId,
                                                     int                 startingFrom,
                                                     int                 maxPageSize,
                                                     PathNameRequestBody requestBody)
    {
        final String methodName        = "findDataFilesByPathName";
        final String nameParameterName = "fullPath";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFilesResponse response = new DataFilesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                List<DataFileElement> dataFiles = handler.findDataFilesByName(userId,
                                                                              requestBody.getPathName(),
                                                                              nameParameterName,
                                                                              startingFrom,
                                                                              maxPageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

                response.setElements(dataFiles);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
