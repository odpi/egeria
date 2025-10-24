/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * FilesAndFoldersHandler provides the support for managing catalog entries about files and folders.
 *
 * @param <FILESYSTEM> the class representing a file system
 * @param <FOLDER> the class representing a folder in the file system
 * @param <FILE> the class representing a file in the file system
 */
public class FilesAndFoldersHandler<FILESYSTEM, FOLDER, FILE>
{
    private final String                  serviceName;
    private final String                  localServerUserId;
    private final OMRSRepositoryHelper    repositoryHelper;
    private final RepositoryHandler       repositoryHandler;

    private final SoftwareCapabilityHandler<FILESYSTEM> fileSystemHandler;
    private final AssetHandler<FOLDER>                  folderHandler;
    private final AssetHandler<FILE>                    fileHandler;


    private final static String folderDivider = "/";
    private final static String fileSystemDivider    = "://";



    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param fileSystemConverter specific converter for the FILESYSTEM bean class
     * @param fileSystemBeanClass name of bean class that is represented by the generic class FILESYSTEM
     * @param folderConverter specific converter for the FOLDER bean class
     * @param folderBeanClass name of bean class that is represented by the generic class FOLDER
     * @param fileConverter specific converter for the FILE bean class
     * @param fileBeanClass name of bean class that is represented by the generic class FILE
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public FilesAndFoldersHandler(OpenMetadataAPIGenericConverter<FILESYSTEM> fileSystemConverter,
                                  Class<FILESYSTEM>                           fileSystemBeanClass,
                                  OpenMetadataAPIGenericConverter<FOLDER>     folderConverter,
                                  Class<FOLDER>                               folderBeanClass,
                                  OpenMetadataAPIGenericConverter<FILE>       fileConverter,
                                  Class<FILE>                                 fileBeanClass,
                                  String                                      serviceName,
                                  String                                      serverName,
                                  InvalidParameterHandler                     invalidParameterHandler,
                                  RepositoryHandler                           repositoryHandler,
                                  OMRSRepositoryHelper                        repositoryHelper,
                                  String                                      localServerUserId,
                                  OpenMetadataServerSecurityVerifier          securityVerifier,
                                  List<String>                                supportedZones,
                                  List<String>                                defaultZones,
                                  List<String>                                publishZones,
                                  AuditLog                                    auditLog)
    {

        this.serviceName             = serviceName;
        this.localServerUserId       = localServerUserId;
        this.repositoryHandler       = repositoryHandler;
        this.repositoryHelper        = repositoryHelper;

        this.fileSystemHandler       = new SoftwareCapabilityHandler<>(fileSystemConverter,
                                                                       fileSystemBeanClass,
                                                                       serviceName,
                                                                       serverName,
                                                                       invalidParameterHandler,
                                                                       repositoryHandler,
                                                                       repositoryHelper,
                                                                       localServerUserId,
                                                                       securityVerifier,
                                                                       supportedZones,
                                                                       defaultZones,
                                                                       publishZones,
                                                                       auditLog);

        this.folderHandler          = new AssetHandler<>(folderConverter,
                                                         folderBeanClass,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);

        this.fileHandler            = new AssetHandler<>(fileConverter,
                                                         fileBeanClass,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);
    }


    /**
     * Return the URL header (if any) from a path name.
     *
     * @param pathName path name of a file
     * @return URL or null
     */
    private String getFileSystemName(String pathName)
    {
        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(fileSystemDivider);

            if (tokens.length > 1)
            {
                result = tokens[0] + fileSystemDivider;
            }
        }

        return result;
    }


    /**
     * Return the list of folder names from a path name.
     *
     * @param pathName path name of a file
     * @return list of folder names or null
     */
    private List<String> getFolderNames(String pathName)
    {
        List<String> result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(folderDivider);

            if (tokens.length > 1)
            {
                int startingToken = 0;

                if (tokens[startingToken].isEmpty())
                {
                    startingToken = 1;
                }
                else if (this.getFileSystemName(pathName) != null)
                {
                    startingToken = 2;
                }

                int endingToken = tokens.length;
                if (this.getFileName(pathName) != null)
                {
                    endingToken = endingToken - 1;
                }

                if (startingToken != endingToken)
                {

                    result = new ArrayList<>(Arrays.asList(tokens).subList(startingToken, endingToken));
                }
            }
        }

        return result;
    }


    /**
     * Return the name of the file from the path name.
     *
     * @param pathName path name of a file
     * @return file name (with type) or null
     */
    private String getFileName(String pathName)
    {
        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(folderDivider);

            result = tokens[tokens.length - 1];
        }

        return result;
    }


    /**
     * Create the requested FileFolder asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param pathName full path name for the file system
     * @param name short display name
     * @param resourceName name from the resource
     * @param versionIdentifier version identifier for the folder
     * @param description description of the file system
     * @param deployedImplementationType technology type
     * @param typeName type of file system
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String createFolder(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  pathName,
                                String  name,
                                String  resourceName,
                                String  versionIdentifier,
                                String  description,
                                String  deployedImplementationType,
                                String  typeName,
                                Date    effectiveFrom,
                                Date    effectiveTo,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String folderAssetGUIDParameterName = "folderAssetGUID";

        String folderAssetTypeName = OpenMetadataType.FILE_FOLDER.typeName;
        if (typeName != null)
        {
            folderAssetTypeName = typeName;
        }

        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, pathName);

        return folderHandler.createAssetWithConnection(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       folderAssetGUIDParameterName,
                                                       this.createQualifiedName(folderAssetTypeName, null, pathName, versionIdentifier),
                                                       name,
                                                       resourceName,
                                                       versionIdentifier,
                                                       description,
                                                       deployedImplementationType,
                                                       null,
                                                       folderAssetTypeName,
                                                       extendedProperties,
                                                       InstanceStatus.ACTIVE,
                                                       true,
                                                       null,
                                                       null,
                                                       pathName,
                                                       null,
                                                       null,
                                                       effectiveFrom,
                                                       effectiveTo,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       folderHandler.getSupportedZones(),
                                                       effectiveTime,
                                                       methodName);
    }


    /**
     * Construct the qualified name for a file resource.
     *
     * @param typeName type of element
     * @param qualifiedName supplied qualified name
     * @param pathName pathname in file system
     * @param versionIdentifier version identifier
     * @return qualified name
     */
    private String createQualifiedName(String typeName,
                                       String qualifiedName,
                                       String pathName,
                                       String versionIdentifier)
    {
        if (qualifiedName != null)
        {
            return qualifiedName;
        }

        if (versionIdentifier == null)
        {
            return typeName + "::" + pathName;
        }
        else
        {
            return typeName + "::" + pathName + "::" + versionIdentifier;
        }
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectToGUID root object to connect the folder to
     * @param pathName pathname of the folder (or folders)
     * @param folderName name of the leaf folder
     * @param versionIdentifier version identifier for the asset
     * @param deployedImplementationType technology type
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String createFolderInCatalog(String   userId,
                                         String   externalSourceGUID,
                                         String   externalSourceName,
                                         String   connectToGUID,
                                         String   pathName,
                                         String   folderName,
                                         String   versionIdentifier,
                                         String   deployedImplementationType,
                                         Date     effectiveFrom,
                                         Date     effectiveTo,
                                         boolean  forLineage,
                                         boolean  forDuplicateProcessing,
                                         Date     effectiveTime,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String connectToParameterName = "connectToGUID";
        final String folderParameterName = "folderGUID";

        String folderGUID = createFolder(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         pathName,
                                         folderName,
                                         folderName,
                                         versionIdentifier,
                                         null,
                                         deployedImplementationType,
                                         OpenMetadataType.FILE_FOLDER.typeName,
                                         effectiveFrom,
                                         effectiveTo,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);

        if (connectToGUID != null)
        {
            if (repositoryHandler.isEntityATypeOf(userId,
                                                  connectToGUID,
                                                  connectToParameterName,
                                                  OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                  effectiveTime,
                                                  methodName))
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   connectToGUID,
                                                   connectToParameterName,
                                                   OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                   folderGUID,
                                                   folderParameterName,
                                                   OpenMetadataType.FILE_FOLDER.typeName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeGUID,
                                                   OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                   null,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   effectiveTime,
                                                   methodName);
            }
            else
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   connectToGUID,
                                                   connectToParameterName,
                                                   OpenMetadataType.FILE_FOLDER.typeName,
                                                   folderGUID,
                                                   folderParameterName,
                                                   OpenMetadataType.FILE_FOLDER.typeName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeGUID,
                                                   OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName,
                                                   null,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   effectiveTime,
                                                   methodName);
            }
        }

        return folderGUID;
    }


    /**
     * Creates a new folder asset for each element in the pathName that is linked from the anchor entity.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectToGUID root object to connect the folder to
     * @param fileSystemName name of the root of the file system (can be null)
     * @param folderNames list of the folder names
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public  List<String> createFolderStructureInCatalog(String         userId,
                                                        String         externalSourceGUID,
                                                        String         externalSourceName,
                                                        String         connectToGUID,
                                                        String         fileSystemName,
                                                        List<String>   folderNames,
                                                        Date           effectiveFrom,
                                                        Date           effectiveTo,
                                                        boolean        forLineage,
                                                        boolean        forDuplicateProcessing,
                                                        Date           effectiveTime,
                                                        String         methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String localMethodName = "->createFolderStructureInCatalog";

        List<String>  folderGUIDs = new ArrayList<>();

        if ((folderNames != null) && (! folderNames.isEmpty()))
        {
            String pathName = null;
            String folderName = null;
            String nextConnectToGUID = connectToGUID;

            for (String folderFragment : folderNames)
            {
                if (pathName == null)
                {
                    if (fileSystemName != null)
                    {
                        pathName = fileSystemName + folderFragment;
                    }
                    else
                    {
                        pathName = folderDivider + folderFragment;
                    }
                }
                else
                {
                    pathName = pathName + folderDivider + folderFragment;
                }

                if (folderName != null)
                {
                    folderName = folderName + folderDivider + folderFragment;
                }
                else
                {
                    folderName = folderFragment;
                }

                String currentFolderGUID = this.getFolderGUIDByPathName(userId,
                                                                        pathName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

                if (currentFolderGUID == null)
                {
                    String folderGUID = createFolderInCatalog(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              nextConnectToGUID,
                                                              pathName,
                                                              folderName,
                                                              null,
                                                              DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType(),
                                                              effectiveFrom,
                                                              effectiveTo,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName + localMethodName);

                    folderGUIDs.add(folderGUID);
                    nextConnectToGUID = folderGUID;
                }
                else
                {
                    folderGUIDs.add(currentFolderGUID);
                    nextConnectToGUID = currentFolderGUID;
                }
            }
        }

        if (folderGUIDs.isEmpty())
        {
            return null;
        }

        return folderGUIDs;
    }



    /**
     * Takes a new file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".  Care is taken to handle the case where the file system and file folders exist in the catalog
     * but are not visible through the user's zones.
     *
     * @param userId                 calling user
     * @param externalSourceGUID     guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName     name of the software capability entity that represented the external source
     * @param fileAssetGUID          unique identifier of file asset
     * @param fileAssetParameterName parameter providing the fileAssetGUID
     * @param fileAssetTypeName      name of the type of file or folder
     * @param pathName               pathname of the file
     * @param pathNameParameterName  parameter providing the pathName
     * @param forLineage             the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName             calling method
     * @throws InvalidParameterException  one of the parameters is null or invalid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addFileAssetPath(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  fileAssetGUID,
                                 String  fileAssetParameterName,
                                 String  fileAssetTypeName,
                                 String  pathName,
                                 String  pathNameParameterName,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        String fileSystemGUID = null;
        String fileSystemName = this.getFileSystemName(pathName);

        if (fileSystemName != null)
        {
            /*
             * The file's pathname includes the root file system name.  A SoftWareServerCapability entity
             * is created for the file system if it does not exist already.
             */
            fileSystemGUID = fileSystemHandler.getBeanGUIDByQualifiedName(userId,
                                                                          OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                          OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                          fileSystemName,
                                                                          pathNameParameterName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

            if (fileSystemGUID == null)
            {
                fileSystemGUID = fileSystemHandler.createFileSystem(localServerUserId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    fileSystemName,
                                                                    fileSystemName,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);
            }
        }

        /*
         * Now set up the folder names.
         */
        List<String> folderNames = this.getFolderNames(pathName);
        List<String> folderGUIDs;
        String       fileParentGUID;

        if (folderNames == null)
        {
            fileParentGUID = fileSystemGUID;
        }
        else
        {
            folderGUIDs = this.createFolderStructureInCatalog(localServerUserId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              fileSystemGUID,
                                                              fileSystemName,
                                                              folderNames,
                                                              null,
                                                              null,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName);

            if ((folderGUIDs != null) && (!folderGUIDs.isEmpty()))
            {
                fileParentGUID = folderGUIDs.get(folderGUIDs.size() - 1);
            }
            else
            {
                fileParentGUID = fileSystemGUID;
            }
        }

        /*
         * Now link in the file to the chain
         */
        if ((fileAssetGUID != null) && (fileParentGUID != null))
        {
            /*
             * The assets for the parent part have been created.  Now connect the file asset to its parent.
             * If there are parent folders then need a nestedFiles relationship.  If the root is the file system
             * then the relationship is server asset use.
             */
            if (fileParentGUID.equals(fileSystemGUID))
            {
                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   fileParentGUID,
                                                   pathNameParameterName,
                                                   OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                   fileAssetGUID,
                                                   fileAssetParameterName,
                                                   OpenMetadataType.DATA_FILE.typeName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeGUID,
                                                   OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                   (InstanceProperties) null,
                                                   null,
                                                   null,
                                                   effectiveTime,
                                                   methodName);
            }
            else
            {
                String relationshipTypeGUID = OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeGUID;
                String relationshipTypeName = OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName;

                if (repositoryHelper.isTypeOf(serviceName, fileAssetTypeName, OpenMetadataType.FILE_FOLDER.typeName))
                {
                    relationshipTypeGUID = OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeGUID;
                    relationshipTypeName = OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName;
                }
                else
                {
                    final String folderParameterName = "fileParentGUID";
                    final String fileParameterName = "fileAssetGUID";
                    EntityDetail parentEntity = folderHandler.getEntityFromRepository(userId,
                                                                                      fileParentGUID,
                                                                                      folderParameterName,
                                                                                      OpenMetadataType.FILE_FOLDER.typeName,
                                                                                      null,
                                                                                      null,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      effectiveTime,
                                                                                      methodName);

                    EntityDetail fileEntity = folderHandler.getEntityFromRepository(userId,
                                                                                    fileAssetGUID,
                                                                                    fileParameterName,
                                                                                    OpenMetadataType.DATA_FILE.typeName,
                                                                                    null,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName);

                    if ((parentEntity != null) && (fileEntity != null) &&
                            (repositoryHelper.isTypeOf(serviceName, parentEntity.getType().getTypeDefName(), OpenMetadataType.DATA_FOLDER.typeName)))
                    {
                        folderHandler.addAnchorsClassification(userId,
                                                               fileEntity,
                                                               fileParameterName,
                                                               fileParentGUID,
                                                               parentEntity.getType().getTypeDefName(),
                                                               OpenMetadataType.ASSET.typeName,
                                                               null,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);
                    }
                }

                folderHandler.linkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   fileParentGUID,
                                                   pathNameParameterName,
                                                   OpenMetadataType.FILE_FOLDER.typeName,
                                                   fileAssetGUID,
                                                   fileAssetParameterName,
                                                   fileAssetTypeName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   relationshipTypeGUID,
                                                   relationshipTypeName,
                                                   (InstanceProperties) null,
                                                   null,
                                                   null,
                                                   effectiveTime,
                                                   methodName);
            }
        }
    }



    /**
     * Retrieve return the unique identifier of a folder located by its fully qualified path name.
     *
     * @param userId calling user
     * @param pathName path name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return string guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private String getFolderGUIDByPathName(String  userId,
                                           String  pathName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String  pathNameParameterName = "pathName";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.PATH_NAME.name);

        List<EntityDetail> entities = fileHandler.getEntitiesByValue(userId,
                                                                     pathName,
                                                                     pathNameParameterName,
                                                                     OpenMetadataType.FILE_FOLDER.typeGUID,
                                                                     OpenMetadataType.FILE_FOLDER.typeName,
                                                                     specificMatchPropertyNames,
                                                                     true,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     0,
                                                                     0,
                                                                     effectiveTime,
                                                                     methodName);
        if (entities != null)
        {
            for (EntityDetail entityDetail : entities)
            {
                if (entityDetail != null)
                {
                    return entityDetail.getGUID();
                }
            }
        }

        return null;
    }
}
