/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.FileFolder;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;



/**
 * FolderHierarchyHandler manages FileFolder objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates FileFolder entities with wire relationships through the OMRSRepositoryConnector.
 */
public class DataEngineFolderHierarchyHandler {

    private final InvalidParameterHandler invalidParameterHandler;
    private final OpenMetadataAPIGenericHandler<Referenceable> genericHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final AssetHandler<FileFolder> folderHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for managing parameter errors
     * @param genericHandler          manages calls to the repository services
     * @param dataEngineCommonHandler provides common Data Engine Omas utilities
     * @param folderHandler           provides utilities specific for manipulating FileFolders
     */
    public DataEngineFolderHierarchyHandler(InvalidParameterHandler invalidParameterHandler,
                                            OpenMetadataAPIGenericHandler<Referenceable> genericHandler,
                                            DataEngineCommonHandler dataEngineCommonHandler,
                                            AssetHandler<FileFolder> folderHandler) {

        this.invalidParameterHandler = invalidParameterHandler;
        this.genericHandler = genericHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.folderHandler = folderHandler;
    }

    /**
     * Construct the folder structure in which a data file is stored all the way to the Engine. Care is
     * taken to maintain uniqueness of the relationship NestedFile that is between the file and the first folder.
     *
     * @param fileGuid           data file guid
     * @param fileType           data file type
     * @param pathName           file path
     * @param externalSourceGuid external source guid
     * @param externalSourceName external source name
     * @param userId             user id
     * @param methodName         method name
     *
     * @throws InvalidParameterException  if invalid parameters
     * @throws PropertyServerException    if errors in repository
     * @throws UserNotAuthorizedException if user not authorized
     */
    public void upsertFolderHierarchy(String fileGuid, String fileType, String pathName, String externalSourceGuid,
                                      String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        if (StringUtils.isEmpty(pathName)) {
            return;
        }
        validateParameters(fileGuid, externalSourceGuid, externalSourceName, userId, methodName);
        List<FileFolder> folders = extractFolders(pathName, externalSourceName, methodName);

        String folderGuid = "";
        String previousEntityGuid = fileGuid;
        String previousEntityType = fileType;
        String relationshipTypeName = OpenMetadataType.NESTED_FILE_TYPE_NAME;
        for (FileFolder folder : folders) {
            if (relationshipTypeName.equals(OpenMetadataType.NESTED_FILE_TYPE_NAME)) {
                deleteExistingNestedFileRelationships(fileGuid, externalSourceGuid, externalSourceName, userId, methodName);
            }
            folderGuid = upsertFolder(externalSourceGuid, externalSourceName, folder, userId, methodName);
            dataEngineCommonHandler.upsertExternalRelationship(userId, folderGuid, previousEntityGuid, relationshipTypeName,
                                                               OpenMetadataType.FILE_FOLDER.typeName, previousEntityType, externalSourceName, null);

            previousEntityGuid = folderGuid;
            previousEntityType = OpenMetadataType.FILE_FOLDER.typeName;
            relationshipTypeName = OpenMetadataType.FOLDER_HIERARCHY_TYPE_NAME;
        }

        dataEngineCommonHandler.upsertExternalRelationship(userId, externalSourceGuid, folderGuid, OpenMetadataType.SERVER_ASSET_USE_TYPE_NAME,
                                                           OpenMetadataType.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, OpenMetadataType.FILE_FOLDER.typeName, externalSourceName, null);
    }

    /**
     * Remove the folder
     *
     * @param userId             the name of the calling user
     * @param folderGUID         unique identifier of the folder to be removed
     * @param externalSourceName the external data engine name
     * @param deleteSemantic     the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeFolder(String userId, String folderGUID, DeleteSemantic deleteSemantic, String externalSourceName) throws
                                                                                                                         InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         FunctionNotSupportedException {
        String methodName = "removeFolder";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(folderGUID, OpenMetadataProperty.GUID.name, methodName);

        dataEngineCommonHandler.removeEntity(userId, folderGUID, OpenMetadataType.FILE_FOLDER.typeName, externalSourceName);
    }

    private void deleteExistingNestedFileRelationships(String fileGuid, String externalSourceGuid, String externalSourceName, String userId,
                                                       String methodName) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        Date now = dataEngineCommonHandler.getNow();
        List<Relationship> relationships = genericHandler.getAttachmentLinks(userId, fileGuid, CommonMapper.GUID_PROPERTY_NAME,
                                                                             OpenMetadataType.DATA_FILE.typeName, OpenMetadataType.NESTED_FILE_TYPE_GUID,
                                                                             OpenMetadataType.NESTED_FILE_TYPE_NAME, null, null, 2,
               false, false,0, invalidParameterHandler.getMaxPagingSize(),  now, methodName);

        if (CollectionUtils.isEmpty(relationships)) {
            return;
        }
        for (Relationship relationship : relationships) {
            genericHandler.deleteRelationship(userId, externalSourceGuid, externalSourceName, relationship.getGUID(),
                    CommonMapper.GUID_PROPERTY_NAME, relationship.getType().getTypeDefName(), false, false,now, methodName);
        }
    }

    private String upsertFolder(String externalSourceGuid, String externalSourceName, FileFolder folder, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Optional<EntityDetail> folderAsEntity = dataEngineCommonHandler.findEntity(userId, folder.getQualifiedName(), OpenMetadataType.FILE_FOLDER.typeName);

        if (folderAsEntity.isPresent()) {
            return folderAsEntity.get().getGUID();
        }

        return folderHandler.createAssetInRepository(userId, externalSourceGuid, externalSourceName,
                folder.getQualifiedName(), folder.getDisplayName(), null, folder.getDescription(), folder.getZoneMembership(),
                folder.getOwner(), folder.getOwnerType().getOpenTypeOrdinal(), null,
                null, folder.getOtherOriginValues(), folder.getAdditionalProperties(),
                                                     OpenMetadataType.FILE_FOLDER.typeGUID, OpenMetadataType.FILE_FOLDER.typeName,  null, null, null,
                InstanceStatus.ACTIVE, dataEngineCommonHandler.getNow(), methodName);
    }

    /**
     * Extracts each folder path and builds FileFolders, with the qualified name of the form
     * '<externalSourceName>::<path>'. The order is important, meaning the first folder is the one containing the file
     * and the last one the root, and used in creating the folder hierarchy structure al the way to the Engine
     *
     * @param pathName           file path
     * @param externalSourceName name of Engine
     * @param methodName         method name
     *
     * @return list of FileFolders
     */
    private List<FileFolder> extractFolders(String pathName, String externalSourceName, String methodName)
            throws InvalidParameterException {

        // java.nio.file.Paths works with backslash '/' alone, so reverse the slash but remember it to set it correctly in FileFolder's pathName
        boolean fileSeparatorReversed = false;
        if (pathName.contains("\\")) {
            pathName = pathName.replace("\\", "/");
            fileSeparatorReversed = true;
        }

        Path path = Paths.get(pathName);
        File parentFile = path.toFile().getParentFile();
        invalidParameterHandler.validateObject(parentFile, "pathName", methodName);

        List<FileFolder> folders = new ArrayList<>();
        // move from parent to parent until the root is reached
        while (parentFile != null) {
            String parentFilePath = fileSeparatorReversed ? parentFile.getPath().replace("/", "\\") : parentFile.getPath();
            FileFolder folder = buildFileFolder(parentFilePath, externalSourceName);
            folders.add(folder);
            parentFile = parentFile.getParentFile();
        }
        return folders;
    }

    private FileFolder buildFileFolder(String pathName, String externalSourceName) {
        FileFolder fileFolder = new FileFolder();
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
    private String computeDisplayName(String pathName) {
        return new File(pathName).getName().length() < 1 ? pathName : new File(pathName).getName();
    }

    private void validateParameters(String fileGuid, String externalSourceGuid, String externalSourceName, String userId, String methodName)
            throws InvalidParameterException {
        invalidParameterHandler.validateObject(fileGuid, "fileGuid", methodName);
        invalidParameterHandler.validateObject(externalSourceGuid, "externalSourceGuid", methodName);
        invalidParameterHandler.validateObject(externalSourceName, "externalSourceName", methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
    }
}
