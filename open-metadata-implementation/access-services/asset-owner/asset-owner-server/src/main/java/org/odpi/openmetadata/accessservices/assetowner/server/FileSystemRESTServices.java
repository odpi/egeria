/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.accessservices.assetowner.properties.FileSystem;
import org.odpi.openmetadata.accessservices.assetowner.properties.Folder;
import org.odpi.openmetadata.accessservices.assetowner.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.FileSystemHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SoftwareServerCapabilityHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SoftwareServerCapabilityMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * FileSystemRESTServices provides the server-side implementation for managing files and folder assets in a
 * file system.
 */
public class FileSystemRESTServices
{
    private static AssetOwnerInstanceHandler   instanceHandler     = new AssetOwnerInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(FileSystemRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


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

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareServerCapabilityHandler handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

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

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

                response.setGUIDs(handler.createFolderStructureInCatalog(userId,
                                                                         anchorGUID,
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
                FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

                response.setGUIDs(handler.createFolderStructureInCatalog(userId,
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
        final String methodName = "attachFolderToFileSystem";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            handler.attachFolderToFileSystem(userId,
                                             fileSystemGUID,
                                             folderGUID,
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

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            handler.detachFolderFromFileSystem(userId,
                                               fileSystemGUID,
                                               folderGUID,
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

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

                response.setGUIDs(handler.addDataFileAssetToCatalog(userId,
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
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
        final String methodName = "addDataFileAssetToCatalog";

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

                response.setGUIDs(handler.addDataFolderAssetToCatalog(userId,
                                                                      requestBody.getDisplayName(),
                                                                      requestBody.getDescription(),
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

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            handler.attachDataFileAssetToFolder(userId,
                                                folderGUID,
                                                fileGUID,
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

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            handler.detachDataFileAssetFromFolder(userId,
                                                  folderGUID,
                                                  fileGUID,
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
        final String methodName = "moveDataFileInCatalog";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            handler.moveDataFileInCatalog(userId,
                                          folderGUID,
                                          fileGUID,
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
        final String methodName = "moveDataFileInCatalog";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            handler.moveDataFolderInCatalog(userId,
                                            folderGUID,
                                            fileGUID,
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
    public FileSystemResponse getFileSystemByGUID(String   serverName,
                                                  String   userId,
                                                  String   fileSystemGUID)
    {
        final String methodName = "getFileSystemByGUID";

        log.debug("Calling method: " + methodName);

        FileSystemResponse response = new FileSystemResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            response.setFileSystem(new FileSystem(handler.getSoftwareServerCapabilityByGUID(userId,
                                                                                            fileSystemGUID,
                                                                                            methodName)));
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
     * Retrieve a FileSystem asset by its unique name.
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

        log.debug("Calling method: " + methodName);

        FileSystemResponse response = new FileSystemResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            response.setFileSystem(new FileSystem(handler.getSoftwareServerCapabilityByUniqueName(userId,
                                                                                                  uniqueName,
                                                                                                  methodName)));
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

            SoftwareServerCapabilityHandler handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getSoftwareServerCapabilityGUIDsByClassification(userId,
                                                                                       SoftwareServerCapabilityMapper.FILE_SYSTEM_CLASSIFICATION_NAME,
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
    public FolderResponse getFolderByGUID(String   serverName,
                                          String   userId,
                                          String   folderGUID)
    {
        final String methodName = "getFolderByGUID";

        log.debug("Calling method: " + methodName);

        FolderResponse response = new FolderResponse();
        AuditLog       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            response.setFolder(new Folder(handler.getFolderByGUID(userId,
                                                                  folderGUID,
                                                                  methodName)));
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
     * @return Folder properties or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem.
     */
    public FolderResponse getFolderByPathName(String                serverName,
                                              String                userId,
                                              PathNameRequestBody   requestBody)
    {
        final String methodName = "getFolderByPathName";

        log.debug("Calling method: " + methodName);

        FolderResponse response = new FolderResponse();
        AuditLog       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

                response.setFolder(new Folder(handler.getFolderByPathName(userId,
                                                                          requestBody.getFullPath(),
                                                                          methodName)));
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
     * @param anchorGUID unique identifier of the anchor folder or Filesystem
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
                                              String  anchorGUID,
                                              int     startingFrom,
                                              int     maxPageSize)
    {
        final String methodName = "getNestedFolders";

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getNestedFolders(userId,
                                                       anchorGUID,
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
        final String methodName = "getFolderFiles";

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

            response.setGUIDs(handler.getFolderFiles(userId,
                                                     folderGUID,
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

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

                response.setGUIDs(handler.addAvroFileToCatalog(userId,
                                                               requestBody.getDisplayName(),
                                                               requestBody.getDescription(),
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

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                FileSystemHandler handler = instanceHandler.getFileSystemHandler(userId, serverName, methodName);

                response.setGUIDs(handler.addCSVFileToCatalog(userId,
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getFullPath(),
                                                              requestBody.getColumnHeaders(),
                                                              requestBody.getDelimiterCharacter(),
                                                              requestBody.getQuoteCharacter(),
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
}
