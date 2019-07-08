/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.accessservices.assetowner.properties.File;
import org.odpi.openmetadata.accessservices.assetowner.properties.Folder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * AssetOnboardingFileSystem provides specialist methods for building up folder structures for a file system.
 */
public interface AssetOnboardingFileSystem
{
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
    String   createFileSystem(String               userId,
                              String               uniqueName,
                              String               displayName,
                              String               description,
                              String               type,
                              String               version,
                              String               patchLevel,
                              String               source,
                              String               format,
                              String               encryption,
                              Map<String, String>  additionalProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param anchorGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> createFolderStructure(String   userId,
                                       String   anchorGUID,
                                       String   pathName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Creates a new folder asset for each element in the pathName.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> createFolderStructure(String   userId,
                                       String   pathName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Link an existing file asset to a folder.  The file is not changed.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  attachFileAssetToFolder(String   userId,
                                  String   folderGUID,
                                  String   fileGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Remove a link between a file asset and a folder.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  detachFileAssetFromFolder(String   userId,
                                    String   folderGUID,
                                    String   fileGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Move a file from its current parent folder to a new parent folder - this changes the file's qualified name
     * but not its unique identifier (guid).  Note if the file has really moved, its Connection's Endpoint object may
     * also need changing.
     *
     * @param userId calling user
     * @param folderGUID new parent folder
     * @param fileGUID file to move
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  moveFile(String   userId,
                   String   folderGUID,
                   String   fileGUID) throws InvalidParameterException,
                                             UserNotAuthorizedException,
                                             PropertyServerException;


    /**
     * Retrieve a Folder asset by its unique identifier (GUID).
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
    Folder getFolderByGUID(String   userId,
                           String   folderGUID) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException;


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName path name
     *
     * @return Folder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    Folder getFolderByPathName(String   userId,
                               String   pathName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException;


    /**
     * Return the list of folders nested inside a folder.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the anchor folder
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folders (null means no nested folders)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<Folder>  getNestedFolders(String  userId,
                                   String  folderGUID,
                                   int     startingFrom,
                                   int     maxPageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Get the files inside a folder - both those that are nested and those that are linked.
     *
     * @param userId calling user
     * @param folderGUID unique identifier of the anchor folder
     * @param startingFrom starting point in the list
     * @param maxPageSize maximum number of results
     *
     * @return list of folders (null means no nested folders)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<File>   getFolderFiles(String  userId,
                                String  folderGUID,
                                int     startingFrom,
                                int     maxPageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;
}
