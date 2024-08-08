/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FileFolderElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FileSystemElement;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.FilesAndFoldersHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * FileSystemRESTServices provides the server-side implementation for managing files and folder assets in a
 * file system.
 */
public class FileSystemRESTServices
{
    private static final AssetOwnerInstanceHandler   instanceHandler     = new AssetOwnerInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(FileSystemRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public FileSystemRESTServices()
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
    public GUIDResponse   createFileSystemInCatalog(String                   serverName,
                                                    String                   userId,
                                                    NewFileSystemRequestBody requestBody)
    {
        final String methodName = "createFileSystemInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement,
                                       FileFolderElement,
                                       DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUID(handler.createFileSystem(userId,
                                                          null,
                                                          null,
                                                          requestBody.getUniqueName(),
                                                          requestBody.getDisplayName(),
                                                          requestBody.getDescription(),
                                                          requestBody.getFileSystemType(),
                                                          requestBody.getVersion(),
                                                          requestBody.getPatchLevel(),
                                                          requestBody.getSource(),
                                                          requestBody.getFormat(),
                                                          requestBody.getEncryption(),
                                                          requestBody.getAdditionalProperties(),
                                                          null,
                                                          null,
                                                          null,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDListResponse createFolderStructureInCatalog(String              serverName,
                                                           String              userId,
                                                           String              anchorGUID,
                                                           PathNameRequestBody requestBody)
    {
        final String methodName = "createFolderStructureInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement,
                        FileFolderElement,
                        DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUIDs(handler.createFolderStructureInCatalog(userId,
                                                                         null,
                                                                         null,
                                                                         anchorGUID,
                                                                         requestBody.getPathName(),
                                                                         null,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDListResponse createFolderStructureInCatalog(String              serverName,
                                                           String              userId,
                                                           PathNameRequestBody requestBody)
    {
        final String methodName = "createFolderStructureInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement,
                        FileFolderElement,
                        DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUIDs(handler.createFolderStructureInCatalog(userId,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         requestBody.getPathName(),
                                                                         null,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse attachFolderToFileSystem(String          serverName,
                                                 String          userId,
                                                 String          fileSystemGUID,
                                                 String          folderGUID,
                                                 NullRequestBody requestBody)
    {
        final String folderGUIDParameterName = "folderGUID";
        final String fileSystemGUIDParameterName = "fileSystemGUID";
        final String methodName = "attachFolderToFileSystem";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.attachFolderToFileSystem(userId,
                                             null,
                                             null,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse detachFolderFromFileSystem(String          serverName,
                                                   String          userId,
                                                   String          fileSystemGUID,
                                                   String          folderGUID,
                                                   NullRequestBody requestBody)
    {
        final String folderGUIDParameterName = "folderGUID";
        final String fileSystemGUIDParameterName = "fileSystemGUID";
        final String methodName = "detachFolderFromFileSystem";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.detachFolderFromFileSystem(userId,
                                               null,
                                               null,
                                               fileSystemGUID,
                                               fileSystemGUIDParameterName,
                                               folderGUID,
                                               folderGUIDParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Creates a new data file asset and links it to the folder structure implied in the path name.  If the folder
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
    public GUIDListResponse addDataFileAssetToCatalog(String                  serverName,
                                                      String                  userId,
                                                      NewFileAssetRequestBody requestBody)
    {
        final String methodName = "addDataFileAssetToCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement,
                        FileFolderElement,
                        DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);


                response.setGUIDs(handler.addDataFileAssetToCatalog(userId,
                                                                    null,
                                                                    null,
                                                                    requestBody.getName(),
                                                                    requestBody.getResourceName(),
                                                                    null,
                                                                    requestBody.getDescription(),
                                                                    DeployedImplementationType.DATA_FILE.getDeployedImplementationType(),
                                                                    requestBody.getFullPath(),
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
    public GUIDListResponse addDataFolderAssetToCatalog(String                  serverName,
                                                        String                  userId,
                                                        NewFileAssetRequestBody requestBody)
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
                FilesAndFoldersHandler<FileSystemElement,
                        FileFolderElement,
                        DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUIDs(handler.addDataFolderAssetToCatalog(userId,
                                                                      null,
                                                                      null,
                                                                      requestBody.getFullPath(),
                                                                      requestBody.getName(),
                                                                      requestBody.getResourceName(),
                                                                      null,
                                                                      requestBody.getDescription(),
                                                                      DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType(),
                                                                      null,
                                                                      null,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  attachDataFileAssetToFolder(String          serverName,
                                                     String          userId,
                                                     String          folderGUID,
                                                     String          fileGUID,
                                                     NullRequestBody requestBody)
    {
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName = "fileGUID";
        final String methodName = "attachDataFileAssetToFolder";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);


            handler.attachDataFileAssetToFolder(userId,
                                                null,
                                                null,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  detachDataFileAssetFromFolder(String          serverName,
                                                       String          userId,
                                                       String          folderGUID,
                                                       String          fileGUID,
                                                       NullRequestBody requestBody)
    {
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName = "fileGUID";
        final String methodName = "detachDataFileAssetFromFolder";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.detachDataFileAssetFromFolder(userId,
                                                  null,
                                                  null,
                                                  folderGUID,
                                                  folderGUIDParameterName,
                                                  fileGUID,
                                                  fileGUIDParameterName,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  moveDataFileInCatalog(String          serverName,
                                               String          userId,
                                               String          folderGUID,
                                               String          fileGUID,
                                               NullRequestBody requestBody)
    {
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName = "fileGUID";
        final String methodName = "moveDataFileInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.moveDataFileInCatalog(userId,
                                          null,
                                          null,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param folderGUID new parent folder
     * @param fileGUID unique identifier of the file to move
     * @param requestBody dummy request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  moveDataFolderInCatalog(String          serverName,
                                                 String          userId,
                                                 String          folderGUID,
                                                 String          fileGUID,
                                                 NullRequestBody requestBody)
    {
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName = "fileGUID";
        final String methodName = "moveDataFileInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.moveDataFolderInCatalog(userId,
                                            null,
                                            null,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a FileSystemElement asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param fileSystemGUID unique identifier used to locate the file system
     *
     * @return FileSystemElement properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public FileSystemResponse getFileSystemByGUID(String   serverName,
                                                  String   userId,
                                                  String   fileSystemGUID)
    {
        final String guidParameterName = "fileSystemGUID";
        final String methodName = "getFileSystemByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FileSystemResponse response = new FileSystemResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            FileSystemElement fileSystemElement = handler.getFileSystemByGUID(userId,
                                                                              fileSystemGUID,
                                                                              guidParameterName,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

            response.setElement(fileSystemElement);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a FileSystemElement asset by its unique name.
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
        final String uniqueNameParameterName = "uniqueName";
        final String methodName = "getFileSystemByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FileSystemResponse response = new FileSystemResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setElement(new FileSystemElement(handler.getFileSystemByUniqueName(userId,
                                                                                        uniqueName,
                                                                                        uniqueNameParameterName,
                                                                                        false,
                                                                                        false,
                                                                                        new Date(),
                                                                                        methodName)));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getFileSystems(userId,
                                                     startingFrom,
                                                     maxPageSize,
                                                     false,
                                                     false,
                                                     new Date(),
                                                     methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve a FileFolderElement asset by its unique identifier (GUID).
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return FileFolderElement properties or
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

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setElement(new FileFolderElement(handler.getFolderByGUID(userId,
                                                                          folderGUID,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName)));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @return FileFolderElement properties or
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
                FilesAndFoldersHandler<FileSystemElement,
                        FileFolderElement,
                        DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setElement(new FileFolderElement(handler.getFolderByPathName(userId,
                                                                                  requestBody.getPathName(),
                                                                                  false,
                                                                                  false,
                                                                                  new Date(),
                                                                                  methodName)));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param parentGUID unique identifier of the anchor folder or Filesystem
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
                                              String  parentGUID,
                                              int     startingFrom,
                                              int     maxPageSize)
    {
        final String parentGUIDParameterName = "parentGUID";
        final String methodName = "getNestedFolders";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getNestedFolders(userId,
                                                       parentGUID,
                                                       parentGUIDParameterName,
                                                       startingFrom,
                                                       maxPageSize,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDListResponse  getFolderFiles(String  serverName,
                                            String  userId,
                                            String  folderGUID,
                                            int     startingFrom,
                                            int     maxPageSize)
    {
        final String folderGUIDParameterName = "folderGUID";
        final String methodName              = "getFolderFiles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement,
                    FileFolderElement,
                    DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getFolderFileGUIDs(userId,
                                                         folderGUID,
                                                         folderGUIDParameterName,
                                                         startingFrom,
                                                         maxPageSize,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDListResponse  addAvroFileToCatalog(String                   serverName,
                                                  String                   userId,
                                                  NewFileAssetRequestBody  requestBody)
    {
        final String methodName = "addAvroFileToCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement,
                        FileFolderElement,
                        DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUIDs(handler.addAvroFileToCatalog(userId,
                                                               null,
                                                               null,
                                                               requestBody.getName(),
                                                               requestBody.getResourceName(),
                                                               requestBody.getVersionIdentifier(),
                                                               requestBody.getDescription(),
                                                               DeployedImplementationType.AVRO_FILE.getDeployedImplementationType(),
                                                               requestBody.getFullPath(),
                                                               null,
                                                               null,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
     * @param requestBody properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDListResponse  addCSVFileToCatalog(String                      serverName,
                                                 String                      userId,
                                                 NewCSVFileAssetRequestBody  requestBody)
    {
        final String methodName = "addCSVFileToCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement,
                        FileFolderElement,
                        DataFileElement> handler = instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUIDs(handler.addCSVFileToCatalog(userId,
                                                              null,
                                                              null,
                                                              requestBody.getName(),
                                                              requestBody.getResourceName(),
                                                              requestBody.getVersionIdentifier(),
                                                              requestBody.getDescription(),
                                                              DeployedImplementationType.CSV_FILE.getDeployedImplementationType(),
                                                              requestBody.getFullPath(),
                                                              requestBody.getColumnHeaders(),
                                                              requestBody.getDelimiterCharacter(),
                                                              requestBody.getQuoteCharacter(),
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
