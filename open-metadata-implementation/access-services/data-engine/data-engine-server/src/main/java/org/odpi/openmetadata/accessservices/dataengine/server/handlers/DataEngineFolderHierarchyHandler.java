/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.FileFolder;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FILE_FOLDER_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FOLDER_HIERARCHY_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.NESTED_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.NESTED_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;

/**
 * FolderHierarchyHandler manages FileFolder objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates FileFolder entities with wire relationships through the OMRSRepositoryConnector.
 */
public class DataEngineFolderHierarchyHandler {

    private final InvalidParameterHandler invalidParameterHandler;
    private final RepositoryHandler repositoryHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final AssetHandler<FileFolder> folderHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param dataEngineCommonHandler provides common Data Engine Omas utilities
     * @param folderHandler provides utilities specific for manipulating FileFolders
     */
    public DataEngineFolderHierarchyHandler(InvalidParameterHandler invalidParameterHandler,
                                            RepositoryHandler repositoryHandler, DataEngineCommonHandler dataEngineCommonHandler,
                                            AssetHandler<FileFolder> folderHandler) {

        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.folderHandler = folderHandler;
    }

    /**
     * Construct the folder structure in which a data file is stored all the way to the SoftwareServerCapability. Care is
     * taken to maintain uniqueness of the relationship NestedFile that is between the file and the first folder.
     *
     * @param fileGuid data file guid
     * @param pathName file path
     * @param externalSourceGuid external source guid
     * @param externalSourceName external source name
     * @param userId user id
     * @param methodName method name
     *
     * @throws InvalidParameterException if invalid parameters
     * @throws PropertyServerException if errors in repository
     * @throws UserNotAuthorizedException if user not authorized
     */
    public void upsertFolderHierarchy(String fileGuid, String pathName, String externalSourceGuid, String externalSourceName,
                                      String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        validateParameters(fileGuid, pathName, externalSourceGuid, externalSourceName, userId, methodName);
        List<FileFolder> folders = extractFolders(pathName, externalSourceName, methodName);

        String folderGuid = "" ;
        String previousEntityGuid = fileGuid;
        String relationshipTypeName = NESTED_FILE_TYPE_NAME;
        for(FileFolder folder : folders){
            if(relationshipTypeName.equals(NESTED_FILE_TYPE_NAME)){
                deleteExistingNestedFileRelationships(fileGuid, externalSourceGuid, externalSourceName, userId, methodName);
            }
            folderGuid = upsertFolder(externalSourceGuid, externalSourceName, folder, userId, methodName);
            dataEngineCommonHandler.upsertExternalRelationship(userId, folderGuid, previousEntityGuid, relationshipTypeName,
                    FILE_FOLDER_TYPE_NAME, externalSourceName, null);

            previousEntityGuid = folderGuid;
            relationshipTypeName = FOLDER_HIERARCHY_TYPE_NAME;
        }

        dataEngineCommonHandler.upsertExternalRelationship(userId, externalSourceGuid, folderGuid, SERVER_ASSET_USE_TYPE_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, externalSourceName, null);
    }

    private void deleteExistingNestedFileRelationships(String fileGuid, String externalSourceGuid, String externalSourceName,
                                                       String userId, String methodName)
            throws UserNotAuthorizedException, PropertyServerException {
        Optional<List<Relationship>> optionalRelationships = Optional.ofNullable(repositoryHandler.getRelationshipsByType(userId, fileGuid, DATA_FILE_TYPE_NAME,
                NESTED_FILE_TYPE_GUID, NESTED_FILE_TYPE_NAME, methodName));
        if(!optionalRelationships.isPresent()){
            return;
        }
        for(Relationship relationship : optionalRelationships.get()) {
            repositoryHandler.removeRelationship(userId, externalSourceGuid, externalSourceName, relationship, methodName);
        }
    }

    private String upsertFolder(String externalSourceGuid, String externalSourceName, FileFolder folder, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Optional<EntityDetail> folderAsEntity = dataEngineCommonHandler.findEntity(userId, folder.getQualifiedName(), FILE_FOLDER_TYPE_NAME);

        if(folderAsEntity.isPresent()){
            return folderAsEntity.get().getGUID();
        }

        return folderHandler.createAssetInRepository(userId, externalSourceGuid, externalSourceName,
                folder.getQualifiedName(), folder.getDisplayName(), folder.getDescription(), folder.getZoneMembership(),
                folder.getOwner(), folder.getOwnerType().getOpenTypeOrdinal(), null,
                null, folder.getOtherOriginValues(), folder.getAdditionalProperties(),
                FILE_FOLDER_TYPE_GUID, FILE_FOLDER_TYPE_NAME, null, methodName);
    }

    /**
    * Extracts each folder path and builds FileFolders, with the qualified name of the form
    * '<externalSourceName>::<path>'. The order is important, meaning the first folder is the one containing the file
    * and the last one the root, and used in creating the folder hierarchy structure al the way to the SoftwareServerCapability
    *
     * @param pathName file path
     * @param externalSourceName name of SoftwareServerCapability
     * @param methodName method name
     *
     * @return list of FileFolders
    */
    private List<FileFolder> extractFolders(String pathName, String externalSourceName, String methodName)
            throws InvalidParameterException {

        // java.nio.file.Paths works with backslash '/' alone, so reverse the slash but remember it to set it correctly in FileFolder's pathName
        boolean fileSeparatorReversed = false;
        if(pathName.contains("\\")){
            pathName = pathName.replace("\\", "/");
            fileSeparatorReversed = true;
        }

        Path path = Paths.get(pathName);
        File parentFile = path.toFile().getParentFile();
        invalidParameterHandler.validateObject(parentFile, "pathName", methodName);

        List<FileFolder> folders = new ArrayList<>();
        // move from parent to parent until the root is reached
        while(parentFile != null){
            String parentFilePath = fileSeparatorReversed ? parentFile.getPath().replace("/", "\\") : parentFile.getPath();
            FileFolder folder = buildFileFolder(parentFilePath, externalSourceName);
            folders.add(folder);
            parentFile = parentFile.getParentFile();
        }
        return folders;
    }

    private FileFolder buildFileFolder(String pathName, String externalSourceName){
        FileFolder fileFolder= new FileFolder();
        fileFolder.setQualifiedName(externalSourceName + "::" + pathName);
        fileFolder.setPathName(pathName);
        fileFolder.setDisplayName(computeDisplayName(pathName));
        fileFolder.setOwnerType(OwnerType.USER_ID);

        return fileFolder;
    }

    /**
     * Will return the name of the last folder in pathName. Takes root folder into account.
     * When called with argument "/folder", return value is "folder".
     * When called with argument "/", return value is "/"
     *
     * @param pathName path
     *
     * @return folder name
     */
    private String computeDisplayName(String pathName){
        return new File(pathName).getName().length() < 1 ? pathName : new File(pathName).getName();
    }

    private void validateParameters(String fileGuid, String pathName, String externalSourceGuid, String externalSourceName,
                                    String userId, String methodName)
            throws InvalidParameterException {
        invalidParameterHandler.validateObject(fileGuid, "fileGuid", methodName);
        invalidParameterHandler.validateObject(pathName, "pathName", methodName);
        invalidParameterHandler.validateObject(externalSourceGuid, "externalSourceGuid", methodName);
        invalidParameterHandler.validateObject(externalSourceName, "externalSourceName", methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
    }

}
