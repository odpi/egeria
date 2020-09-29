/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * FileSystemRESTServices provides the server-side implementation for managing files and folder assets in a
 * file system.  Notice that the integrator identifiers are not specified for the file system because the files can be
 * changed by so many processes.  If the files are being controlled by an application then use the ContentManagerRESTServices
 */
public class FileSystemRESTServices
{
    private static DataManagerInstanceHandler   instanceHandler     = new DataManagerInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(FileSystemRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public FileSystemRESTServices()
    {
    }


    /**
     * Files live on a file system.  This method creates a top level capability for a file system.
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

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUID(handler.createFileSystem(userId,
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
                                                          methodName));
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
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
        final String methodName = "createFolderStructureInCatalog";

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUIDs(handler.createFolderStructureInCatalog(userId,
                                                                         null,
                                                                         null,
                                                                         parentGUID,
                                                                         requestBody.getFullPath(),
                                                                         methodName));
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                response.setGUIDs(handler.createFolderStructureInCatalog(userId,
                                                                         null,
                                                                         null,
                                                                         requestBody.getFullPath(),
                                                                         methodName));
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
        final String methodName                  = "attachFolderToFileSystem";
        final String fileSystemGUIDParameterName = "fileSystemGUID";
        final String folderGUIDParameterName     = "folderGUID";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.attachFolderToFileSystem(userId,
                                             null,
                                             null,
                                             fileSystemGUID,
                                             fileSystemGUIDParameterName,
                                             folderGUID,
                                             folderGUIDParameterName,
                                             methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
        final String methodName = "detachFolderFromFileSystem";
        final String fileSystemGUIDParameterName = "fileSystemGUID";
        final String folderGUIDParameterName     = "folderGUID";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.detachFolderFromFileSystem(userId,
                                               null,
                                               null,
                                               fileSystemGUID,
                                               fileSystemGUIDParameterName,
                                               folderGUID,
                                               folderGUIDParameterName,
                                               methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
    public GUIDListResponse addDataFileToCatalog(String                 serverName,
                                                 String                 userId,
                                                 NewDataFileRequestBody requestBody)
    {
        final String methodName = "addFileToCatalog";

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.addFileToCatalog(userId,
                                                       null,
                                                       null,
                                                       requestBody.getQualifiedName(),
                                                       requestBody.getDisplayName(),
                                                       requestBody.getDescription(),
                                                       requestBody.getAdditionalProperties(),
                                                       requestBody.getConnectorClassName(),
                                                       requestBody.getTypeName(),
                                                       requestBody.getExtendedProperties(),
                                                       methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
    public GUIDListResponse addDataFolderAssetToCatalog(String                   serverName,
                                                        String                   userId,
                                                        NewDataFolderRequestBody requestBody)
    {
        final String methodName = "addDataFileAssetToCatalog";

        log.debug("Calling method: " + methodName);

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
                                                                      null,
                                                                      null,
                                                                      requestBody.getDisplayName(),
                                                                      requestBody.getDescription(),
                                                                      requestBody.getQualifiedName(),
                                                                      methodName));
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
        final String methodName = "attachDataFileAssetToFolder";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName = "fileGUID";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.attachDataFileAssetToFolder(userId,
                                                null,
                                                null,
                                                folderGUID,
                                                folderGUIDParameterName,
                                                fileGUID,
                                                fileGUIDParameterName,
                                                methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
        final String methodName = "detachDataFileAssetFromFolder";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName = "fileGUID";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.detachDataFileAssetFromFolder(userId,
                                                  null,
                                                  null,
                                                  folderGUID,
                                                  folderGUIDParameterName,
                                                  fileGUID,
                                                  fileGUIDParameterName,
                                                  methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Move a data file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
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
        final String methodName              = "moveDataFileInCatalog";
        final String folderGUIDParameterName = "folderGUID";
        final String fileGUIDParameterName   = "fileGUID";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.moveDataFileInCatalog(userId,
                                          null,
                                          null,
                                          folderGUID,
                                          folderGUIDParameterName,
                                          fileGUID,
                                          fileGUIDParameterName,
                                          methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Move a data folder from its current parent folder to a new parent folder - this changes the folder's qualified name
     * but not its unique identifier (guid).  Also the the endpoint in the connection object.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param newParentFolderGUID new parent folder
     * @param movingFolderGUID unique identifier of the file to move
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
                                                 String          newParentFolderGUID,
                                                 String          movingFolderGUID,
                                                 NullRequestBody requestBody)
    {
        final String methodName = "moveDataFileInCatalog";
        final String newParentFolderGUIDParameterName = "newParentFolderGUID";
        final String movingFolderGUIDParameterName = "movingFolderGUID";


        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            handler.moveDataFolderInCatalog(userId,
                                            null,
                                            null,
                                            newParentFolderGUID,
                                            newParentFolderGUIDParameterName,
                                            movingFolderGUID,
                                            movingFolderGUIDParameterName,
                                            methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        log.debug("Calling method: " + methodName);

        FileSystemResponse response = new FileSystemResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            FileSystemElement element = handler.getFileSystemByGUID(userId, fileSystemGUID, guidParameterName, methodName);
            response.setFileSystem(element);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        log.debug("Calling method: " + methodName);

        FileSystemResponse response = new FileSystemResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            FileSystemElement element = handler.getFileSystemByUniqueName(userId, uniqueName, parameterName, methodName);

            response.setFileSystem(element);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getFileSystems(userId,
                                                     OpenMetadataAPIMapper.FILE_SYSTEM_CLASSIFICATION_NAME,
                                                     startingFrom,
                                                     maxPageSize,
                                                     methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        log.debug("Calling method: " + methodName);

        FileFolderResponse response = new FileFolderResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            FileFolderElement fileFolder = handler.getFolderByGUID(userId, folderGUID, methodName);
            response.setFolder(fileFolder);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        log.debug("Calling method: " + methodName);

        FileFolderResponse response = new FileFolderResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                FileFolderElement fileFolder = handler.getFolderByPathName(userId, requestBody.getFullPath(), methodName);
                response.setFolder(fileFolder);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param parentFolderGUID unique identifier of the folder or Filesystem to query
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folder unique identifiers (null means no nested folders) or
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


        log.debug("Calling method: " + methodName);

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
                                                       methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
        final String methodName = "getFolderFiles";
        final String guidParameterName = "folderGUID";

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getFolderFiles(userId,
                                                     folderGUID,
                                                     guidParameterName,
                                                     startingFrom,
                                                     maxPageSize,
                                                     methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        log.debug("Calling method: " + methodName);

        DataFileResponse response = new DataFileResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                    instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

            DataFileElement dataFile = handler.getDataFileByGUID(userId, fileGUID, guidParameterName, methodName);
            response.setDataFile(dataFile);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        log.debug("Calling method: " + methodName);

        DataFileResponse response = new DataFileResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> handler =
                        instanceHandler.getFilesAndFoldersHandler(userId, serverName, methodName);

                DataFileElement dataFile = handler.getDataFileByPathName(userId, requestBody.getFullPath(), nameParameterName, methodName);

                response.setDataFile(dataFile);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
