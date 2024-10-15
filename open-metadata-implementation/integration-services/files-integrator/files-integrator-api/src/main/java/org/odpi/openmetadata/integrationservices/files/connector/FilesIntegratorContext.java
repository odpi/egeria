/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.files.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.ConnectionManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.FilesAndFoldersClient;
import org.odpi.openmetadata.accessservices.datamanager.client.ValidValueManagement;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * FilesIntegratorContext provides a wrapper around the Data Manager OMAS clients.
 * It provides the simplified interface to open metadata needed by the FilesIntegratorConnector.
 * It is designed to be used either for cataloguing folders and files
 */
public class FilesIntegratorContext extends IntegrationContext
{
    private final ConnectionManagerClient connectionManagerClient;
    private final FilesAndFoldersClient   filesAndFoldersClient;
    private final DataManagerEventClient  eventClient;
    private final ValidValueManagement    validValueManagement;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
     * @param filesAndFoldersClient client to map request to
     * @param connectionManagerClient client for managing connections
     * @param validValueManagement client for managing valid value sets and definitions
     * @param eventClient client to register for events
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the asset manager
     * @param externalSourceName unique name of the software server capability for the asset manager
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public FilesIntegratorContext(String                       connectorId,
                                  String                       connectorName,
                                  String                       connectorUserId,
                                  String                       serverName,
                                  OpenIntegrationClient        openIntegrationClient,
                                  GovernanceConfiguration      governanceConfiguration,
                                  OpenMetadataClient           openMetadataStoreClient,
                                  ActionControlInterface       actionControlInterface,
                                  FilesAndFoldersClient        filesAndFoldersClient,
                                  ConnectionManagerClient      connectionManagerClient,
                                  ValidValueManagement         validValueManagement,
                                  DataManagerEventClient       eventClient,
                                  boolean                      generateIntegrationReport,
                                  PermittedSynchronization     permittedSynchronization,
                                  String                       integrationConnectorGUID,
                                  String                       externalSourceGUID,
                                  String                       externalSourceName,
                                  AuditLog                     auditLog,
                                  int                          maxPageSize)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              actionControlInterface,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.filesAndFoldersClient    = filesAndFoldersClient;
        this.connectionManagerClient  = connectionManagerClient;
        this.validValueManagement     = validValueManagement;
        this.eventClient              = eventClient;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }

    /*============================================================================================================
     * Path name management functions
     */


    /**
     * Extract the name of the file system from the path name of a file or directory (folder).  This is everything in front of this string pattern "://".
     * If the string pattern is not found then null is returned.
     *
     * @param pathName path name of file or directory
     * @return file system name or null
     */
    public String getFileSystemName(String  pathName)
    {
        final String fileSystemRegEx = "://";

        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(fileSystemRegEx);

            if (tokens.length > 1)
            {
                result = tokens[0] + fileSystemRegEx;
            }
        }

        return result;
    }


    /**
     * Return a list of directory (folder) names extracted from a path name.  For example, if the pathname is "one/two/three.txt", the method
     * returns ["one", "two" ].
     *
     * @param pathName path name of file or directory
     * @return list of names
     */
    public List<String> getDirectoryNames(String pathName)
    {
        final String  folderDivider = "/";

        List<String> result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(folderDivider);

            if (tokens.length > 1)
            {
                int startingToken = 0;
                if (this.getFileSystemName(pathName) != null)
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
                    result = new ArrayList<>();

                    for (int i=startingToken; i<endingToken; i++)
                    {
                        result.add(tokens[i]);
                    }
                }
            }
        }

        return result;
    }


    /**
     * Retrieves the file name from a pathname.  For example, if the pathname is "one/two/three.txt", the method
     * returns "three.txt".
     *
     * @param pathName path name of file or directory
     * @return file name with its extension (if present)
     */
    private String getFileName(String pathName)
    {
        final String  folderDivider = "/";

        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(folderDivider);

            result = tokens[tokens.length - 1];
        }

        return result;
    }


    /**
     * Retrieves the extension from a path name.  For example, if the pathname is "one/two/three.txt", the method
     * returns "txt".  If the path name has multiple extensions, such as "my-jar.jar.gz", the final extension is returned (ie "gz").
     * Null is returned if there is no file extension in the path name.
     *
     * @param pathName path name of file or directory
     * @return file extension
     */
    public String getFileExtension(String pathName)
    {
        final String  fileTypeDivider = "\\.";

        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(fileTypeDivider);

            if (tokens.length > 1)
            {
                result = tokens[tokens.length - 1];
            }
        }

        return result;
    }



    /*============================================================================================================
     * Start of file management methods
     */

    /**
     * Creates a new folder asset for each element in the pathName.
     * For example, a pathName of "one/two/three" creates 3 new folder assets, one called "one", the next called
     * "one/two" and the last one called "one/two/three".
     *
     * @param parentGUID unique identifier of root object to connect the folder to (fileSystemGUID or folderGUID)
     * @param pathName pathname of the folder (or folders)
     *
     * @return list of GUIDs from the top level to the leaf of the supplied pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> createNestedFolders(String parentGUID,
                                            String pathName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        List<String> nestedFolderGUIDs = filesAndFoldersClient.createNestedFolders(userId,
                                                                                   externalSourceGUID,
                                                                                   externalSourceName,
                                                                                   parentGUID,
                                                                                   pathName);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(parentGUID, parentGUID);
            integrationReportWriter.reportElementUpdate(parentGUID);

            if ((nestedFolderGUIDs != null) && (nestedFolderGUIDs.size() > 0))
            {
                for (String folderGUID : nestedFolderGUIDs)
                {
                    integrationReportWriter.setAnchor(folderGUID, folderGUID);
                    integrationReportWriter.reportElementCreation(folderGUID);
                }
            }
        }

        return nestedFolderGUIDs;
    }


    /**
     * Links a folder to a file system. The folder is not changed.
     *
     * @param fileSystemGUID unique identifier of the file system
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void attachTopLevelFolder(String fileSystemGUID,
                                     String folderGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        filesAndFoldersClient.attachTopLevelFolder(userId, externalSourceGUID, externalSourceName, fileSystemGUID, folderGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(folderGUID, folderGUID);
            integrationReportWriter.setAnchor(fileSystemGUID, fileSystemGUID);
            integrationReportWriter.reportElementUpdate(fileSystemGUID);
            integrationReportWriter.reportElementUpdate(folderGUID);
        }
    }


    /**
     * Removed the link between a folder and a file system.
     *
     * @param fileSystemGUID unique identifier of the file system
     * @param folderGUID unique identifier of the folder in the catalog
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void detachTopLevelFolder(String fileSystemGUID,
                                     String folderGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        filesAndFoldersClient.detachTopLevelFolder(userId, externalSourceGUID, externalSourceName, fileSystemGUID, folderGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(folderGUID, folderGUID);
            integrationReportWriter.setAnchor(fileSystemGUID, fileSystemGUID);
            integrationReportWriter.reportElementUpdate(fileSystemGUID);
            integrationReportWriter.reportElementUpdate(folderGUID);
        }
    }


    /**
     * Creates a new file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param dataFileProperties details of the data file to add to the catalog as an asset
     * @param connectorProviderName class name of connector provider for connector to access this asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFileToCatalog(DataFileProperties dataFileProperties,
                                             String             connectorProviderName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        List<String> nestedFolderGUIDs = filesAndFoldersClient.addDataFileToCatalog(userId,
                                                                                    externalSourceGUID,
                                                                                    externalSourceName,
                                                                                    dataFileProperties,
                                                                                    connectorProviderName);

        if ((nestedFolderGUIDs != null) && (nestedFolderGUIDs.size() > 0) && (integrationReportWriter != null))
        {
            for (String folderGUID : nestedFolderGUIDs)
            {
                integrationReportWriter.setAnchor(folderGUID, folderGUID);
                integrationReportWriter.reportElementCreation(folderGUID);
            }
        }

        return nestedFolderGUIDs;
    }


    /**
     * Add an asset description a file based on the content of a template object. Link this new asset to the folder structure implied in the path name.
     * If the folder structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param templateGUID unique identifier of the file asset to copy
     * @param templateProperties override properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFileToCatalogFromTemplate(String             templateGUID,
                                                         TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        List<String> nestedFolderGUIDs = filesAndFoldersClient.addDataFileToCatalogFromTemplate(userId,
                                                                                                externalSourceGUID,
                                                                                                externalSourceName,
                                                                                                templateGUID,
                                                                                                templateProperties);

        if ((nestedFolderGUIDs != null) && (nestedFolderGUIDs.size() > 0) && (integrationReportWriter != null))
        {
            for (String folderGUID : nestedFolderGUIDs)
            {
                integrationReportWriter.setAnchor(folderGUID, folderGUID);
                integrationReportWriter.reportElementCreation(folderGUID);
            }
        }

        return nestedFolderGUIDs;
    }


    /**
     * Update the file asset description in the catalog.
     *
     * @param dataFileGUID unique identifier of the data file asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param dataFileProperties properties for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateDataFileInCatalog(String             dataFileGUID,
                                        boolean            isMergeUpdate,
                                        DataFileProperties dataFileProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        filesAndFoldersClient.updateDataFileInCatalog(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      dataFileGUID,
                                                      isMergeUpdate,
                                                      dataFileProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(dataFileGUID, dataFileGUID);
            integrationReportWriter.reportElementUpdate(dataFileGUID);
        }
    }


    /**
     * Mark the file asset description in the catalog as archived.
     *
     * @param dataFileGUID unique identifier of the data file asset
     * @param archiveProperties properties to help locate the archive copy
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void archiveDataFileInCatalog(String            dataFileGUID,
                                         ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        filesAndFoldersClient.archiveDataFileInCatalog(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       dataFileGUID,
                                                       archiveProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(dataFileGUID, dataFileGUID);
            integrationReportWriter.reportElementDelete(dataFileGUID);
        }
    }


    /**
     * Remove the file asset description from the catalog.
     *
     * @param dataFileGUID unique identifier of the data file asset
     * @param fullPathname unique path and file name for file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteDataFileFromCatalog(String dataFileGUID,
                                          String fullPathname) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        filesAndFoldersClient.deleteDataFileFromCatalog(userId, externalSourceGUID, externalSourceName, dataFileGUID, fullPathname);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(dataFileGUID, dataFileGUID);
            integrationReportWriter.reportElementDelete(dataFileGUID);
        }
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
     * @param fileFolderProperties properties to describe the folder properties
     * @param connectorProviderName class name of connector provider for connector to access this asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFolderToCatalog(FileFolderProperties fileFolderProperties,
                                               String               connectorProviderName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        List<String> nestedFolderGUIDs = filesAndFoldersClient.addDataFolderToCatalog(userId,
                                                                                      externalSourceGUID,
                                                                                      externalSourceName,
                                                                                      fileFolderProperties,
                                                                                      connectorProviderName);

        if ((nestedFolderGUIDs != null) && (nestedFolderGUIDs.size() > 0) && (integrationReportWriter != null))
        {
            for (String folderGUID : nestedFolderGUIDs)
            {
                integrationReportWriter.setAnchor(folderGUID, folderGUID);
                integrationReportWriter.reportElementCreation(folderGUID);
            }
        }

        return nestedFolderGUIDs;
    }


    /**
     * Add an asset description a file based on the content of a template object. Link this new asset to the folder structure implied in the path name.
     * If the folder structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param templateGUID unique identifier of the file asset to copy
     * @param templateProperties override properties for the asset
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> addDataFolderToCatalogFromTemplate(String             templateGUID,
                                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        List<String> nestedFolderGUIDs = filesAndFoldersClient.addDataFolderToCatalogFromTemplate(userId,
                                                                                                  externalSourceGUID,
                                                                                                  externalSourceName,
                                                                                                  templateGUID,
                                                                                                  templateProperties);

        if ((nestedFolderGUIDs != null) && (nestedFolderGUIDs.size() > 0) && (integrationReportWriter != null))
        {
            for (String folderGUID : nestedFolderGUIDs)
            {
                integrationReportWriter.setAnchor(folderGUID, folderGUID);
                integrationReportWriter.reportElementCreation(folderGUID);
            }
        }

        return nestedFolderGUIDs;
    }


    /**
     * Update the data folder asset description in the catalog.
     *
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param fileFolderProperties properties for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateDataFolderInCatalog(String               dataFolderGUID,
                                          boolean              isMergeUpdate,
                                          FileFolderProperties fileFolderProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        filesAndFoldersClient.updateDataFolderInCatalog(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        dataFolderGUID,
                                                        isMergeUpdate,
                                                        fileFolderProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(dataFolderGUID, dataFolderGUID);
            integrationReportWriter.reportElementUpdate(dataFolderGUID);
        }
    }


    /**
     * Update the data folder asset description in the catalog.
     *
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param isMergeUpdate should the supplied properties completely override the existing properties or augment them?
     * @param fileFolderProperties properties for the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateDataFolderInCatalog(String               metadataSourceGUID,
                                          String               metadataSourceName,
                                          String               dataFolderGUID,
                                          boolean              isMergeUpdate,
                                          FileFolderProperties fileFolderProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        filesAndFoldersClient.updateDataFolderInCatalog(userId,
                                                        metadataSourceGUID,
                                                        metadataSourceName,
                                                        dataFolderGUID,
                                                        isMergeUpdate,
                                                        fileFolderProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(dataFolderGUID, dataFolderGUID);
            integrationReportWriter.reportElementUpdate(dataFolderGUID);
        }
    }


    /**
     * Mark the data folder asset description in the catalog as archived.
     *
     * @param dataFolderGUID unique identifier of the data folder asset
     * @param archiveProperties properties to help locate the archive copy
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void archiveDataFolderInCatalog(String            dataFolderGUID,
                                           ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        filesAndFoldersClient.archiveDataFolderInCatalog(userId, externalSourceGUID, externalSourceName, dataFolderGUID, archiveProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(dataFolderGUID, dataFolderGUID);
            integrationReportWriter.reportElementDelete(dataFolderGUID);
        }
    }


    /**
     * Link an existing file asset to a folder.  The file is not changed as this is used to create a logical link
     * to the folder.
     *
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void attachDataFileAssetToFolder(String folderGUID,
                                            String fileGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        filesAndFoldersClient.attachDataFileAssetToFolder(userId, externalSourceGUID, externalSourceName, folderGUID, fileGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(folderGUID, folderGUID);
            integrationReportWriter.reportElementUpdate(folderGUID);
            integrationReportWriter.setAnchor(fileGUID, fileGUID);
            integrationReportWriter.reportElementUpdate(fileGUID);
        }
    }


    /**
     * Remove a link between a file asset and a folder.  The file is not changed.  Use moveDataFileInCatalog to record
     * the fact that the physical file has moved.  Use attachDataFileAssetToFolder to create logical link to a new
     * folder.
     *
     * @param folderGUID unique identifier of the folder
     * @param fileGUID unique identifier of the file
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void detachDataFileAssetFromFolder(String folderGUID,
                                              String fileGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        filesAndFoldersClient.detachDataFileAssetFromFolder(userId, externalSourceGUID, externalSourceName, folderGUID, fileGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(folderGUID, folderGUID);
            integrationReportWriter.reportElementUpdate(folderGUID);
            integrationReportWriter.setAnchor(fileGUID, fileGUID);
            integrationReportWriter.reportElementUpdate(fileGUID);
        }
    }


    /**
     * Retrieve a FileFolderProperties asset by its unique identifier (GUID).
     *
     * @param folderGUID unique identifier used to locate the folder
     *
     * @return FileFolder properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FileFolderElement getFolderByGUID(String   folderGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return filesAndFoldersClient.getFolderByGUID(userId, folderGUID);
    }


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param pathName path name
     *
     * @return FileFolderProperties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public FileFolderElement getFolderByPathName(String   pathName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return filesAndFoldersClient.getFolderByPathName(userId, pathName);
    }


    /**
     * Return the list of folders linked to the file server capability.
     *
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
    public List<FileFolderElement>  getTopLevelFolders(String fileSystemGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return filesAndFoldersClient.getTopLevelFolders(userId, fileSystemGUID, startFrom, pageSize);
    }


    /**
     * Return the list of folders nested inside a folder.
     *
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
    public List<FileFolderElement>  getNestedFolders(String  parentFolderGUID,
                                                     int     startFrom,
                                                     int     pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return filesAndFoldersClient.getNestedFolders(userId, parentFolderGUID, startFrom, pageSize);
    }


    /**
     * Return the list of folders nested inside a folder.
     *
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
    public List<DataFileElement>  getTopLevelDataFiles(String fileSystemGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return filesAndFoldersClient.getTopLevelDataFiles(userId, fileSystemGUID, startFrom, pageSize);
    }


    /**
     * Get the data files inside a folder - both those that are nested and those that are linked.
     *
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
    public List<DataFileElement> getFolderFiles(String  folderGUID,
                                                int     startFrom,
                                                int     pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return filesAndFoldersClient.getFolderFiles(userId, folderGUID, startFrom, pageSize);
    }


    /**
     * Retrieve a FileFolderProperties asset by its unique identifier (GUID).
     *
     * @param fileGUID unique identifier used to locate the folder
     *
     * @return File properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public DataFileElement getFileByGUID(String fileGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return filesAndFoldersClient.getFileByGUID(userId, fileGUID);
    }


    /**
     * Retrieve a folder by its fully qualified path name.
     *
     * @param pathName path name
     *
     * @return File properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public DataFileElement getFileByPathName(String   pathName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return filesAndFoldersClient.getFileByPathName(userId, pathName);
    }


    /* =====================================================================================================================
     * A schemaType is used to describe complex structures found in the schema of a file
     */

    /**
     * Create a new metadata element to represent a primitive schema type such as a string, integer or character.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPrimitiveSchemaType(PrimitiveSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        String schemaTypeGUID;
        if (externalSourceIsHome)
        {
            schemaTypeGUID = filesAndFoldersClient.createPrimitiveSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID = filesAndFoldersClient.createPrimitiveSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed value.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createLiteralSchemaType(LiteralSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = filesAndFoldersClient.createLiteralSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID = filesAndFoldersClient.createLiteralSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed set of values that are described by a valid value set.
     *
     * @param schemaTypeProperties properties about the schema type to store
     * @param validValuesSetGUID unique identifier of the valid values set to used
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEnumSchemaType(EnumSchemaTypeProperties schemaTypeProperties,
                                       String                   validValuesSetGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = filesAndFoldersClient.createEnumSchemaType(userId,
                                                                        externalSourceGUID,
                                                                        externalSourceName,
                                                                        schemaTypeProperties,
                                                                        validValuesSetGUID);
        }
        else
        {
            schemaTypeGUID = filesAndFoldersClient.createEnumSchemaType(userId,
                                                                        null,
                                                                        null,
                                                                        schemaTypeProperties,
                                                                        validValuesSetGUID);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Retrieve the list of valid value set metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueSetElement> getValidValueSetByName(String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return filesAndFoldersClient.getValidValueSetByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid value set metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueSetElement> findValidValueSet(String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return filesAndFoldersClient.findValidValueSet(userId, searchString, startFrom, pageSize);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createStructSchemaType(StructSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = filesAndFoldersClient.createStructSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID =  filesAndFoldersClient.createStructSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a list of possible schema types that can be used for the attached API parameter.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     * @param schemaTypeOptionGUIDs list of unique identifiers for schema types to link to
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeChoice(SchemaTypeChoiceProperties schemaTypeProperties,
                                         List<String>               schemaTypeOptionGUIDs) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = filesAndFoldersClient.createSchemaTypeChoice(userId, externalSourceGUID, externalSourceName, schemaTypeProperties,
                                                                          schemaTypeOptionGUIDs);
        }
        else
        {
            schemaTypeGUID = filesAndFoldersClient.createSchemaTypeChoice(userId, null, null, schemaTypeProperties, schemaTypeOptionGUIDs);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param schemaTypeProperties properties about the schema type to store
     * @param mapFromSchemaTypeGUID unique identifier of the domain of the map
     * @param mapToSchemaTypeGUID unique identifier of the range of the map
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createMapSchemaType(MapSchemaTypeProperties schemaTypeProperties,
                                      String                  mapFromSchemaTypeGUID,
                                      String                  mapToSchemaTypeGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = filesAndFoldersClient.createMapSchemaType(userId,
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       schemaTypeProperties,
                                                                       mapFromSchemaTypeGUID,
                                                                       mapToSchemaTypeGUID);
        }
        else
        {
            schemaTypeGUID = filesAndFoldersClient.createMapSchemaType(userId,
                                                                       null,
                                                                       null,
                                                                       schemaTypeProperties,
                                                                       mapFromSchemaTypeGUID,
                                                                       mapToSchemaTypeGUID);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = filesAndFoldersClient.createSchemaTypeFromTemplate(userId,
                                                                                externalSourceGUID,
                                                                                externalSourceName,
                                                                                templateGUID,
                                                                                templateProperties);
        }
        else
        {
            schemaTypeGUID = filesAndFoldersClient.createSchemaTypeFromTemplate(userId,
                                                                                null,
                                                                                null,
                                                                                templateGUID,
                                                                                templateProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Update the metadata element representing a schema type.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String               schemaTypeGUID,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        filesAndFoldersClient.updateSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID, isMergeUpdate, schemaTypeProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaTypeGUID);
        }
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        filesAndFoldersClient.removeSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaTypeGUID);
        }
    }


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param properties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaElementRelationship(String                 endOneGUID,
                                               String                 endTwoGUID,
                                               String                 relationshipTypeName,
                                               RelationshipProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        filesAndFoldersClient.setupSchemaElementRelationship(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             endOneGUID,
                                                             endTwoGUID,
                                                             relationshipTypeName,
                                                             properties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(endTwoGUID, endOneGUID);
            integrationReportWriter.reportElementUpdate(endOneGUID);
        }
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementRelationship(String endOneGUID,
                                               String endTwoGUID,
                                               String relationshipTypeName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        filesAndFoldersClient.clearSchemaElementRelationship(userId, externalSourceGUID, externalSourceName, endOneGUID, endTwoGUID, relationshipTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String searchString,
                                                  String typeName,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return filesAndFoldersClient.findSchemaType(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String parentElementGUID,
                                                     String parentElementTypeName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return filesAndFoldersClient.getSchemaTypeForElement(userId, parentElementGUID, parentElementTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String name,
                                                         String typeName,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return filesAndFoldersClient.getSchemaTypeByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String schemaTypeGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return filesAndFoldersClient.getSchemaTypeByGUID(userId, schemaTypeGUID);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String schemaTypeGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return filesAndFoldersClient.getSchemaTypeParent(userId, schemaTypeGUID);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is nested underneath
     * @param schemaAttributeProperties properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttribute(String                    schemaElementGUID,
                                        SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        String schemaAttributeGUID;

        if (externalSourceIsHome)
        {
            schemaAttributeGUID = filesAndFoldersClient.createSchemaAttribute(userId,
                                                                              externalSourceGUID,
                                                                              externalSourceName,
                                                                              schemaElementGUID,
                                                                              schemaAttributeProperties);
        }
        else
        {
            schemaAttributeGUID = filesAndFoldersClient.createSchemaAttribute(userId,
                                                                              null,
                                                                              null,
                                                                              schemaElementGUID,
                                                                              schemaAttributeProperties);
        }

        if ((schemaAttributeGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(schemaElementGUID);
        }

        return schemaAttributeGUID;
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(String             schemaElementGUID,
                                                    String             templateGUID,
                                                    TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        String schemaAttributeGUID;

        if (externalSourceIsHome)
        {
            schemaAttributeGUID = filesAndFoldersClient.createSchemaAttributeFromTemplate(userId,
                                                                                          externalSourceGUID,
                                                                                          externalSourceName,
                                                                                          schemaElementGUID,
                                                                                          templateGUID,
                                                                                          templateProperties);
        }
        else
        {
            schemaAttributeGUID = filesAndFoldersClient.createSchemaAttributeFromTemplate(userId,
                                                                                          null,
                                                                                          null,
                                                                                          schemaElementGUID,
                                                                                          templateGUID,
                                                                                          templateProperties);
        }

        if ((schemaAttributeGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(schemaElementGUID);
        }

        return schemaAttributeGUID;
    }


    /**
     * Connect a schema type to a schema attribute.
     *
     * @param relationshipTypeName name of relationship to create
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaType(String relationshipTypeName,
                                String schemaAttributeGUID,
                                String schemaTypeGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        if (externalSourceIsHome)
        {
            filesAndFoldersClient.setupSchemaType(userId, externalSourceGUID, externalSourceName, relationshipTypeName, schemaAttributeGUID, schemaTypeGUID);
        }
        else
        {
            filesAndFoldersClient.setupSchemaType(userId, null, null, relationshipTypeName, schemaAttributeGUID, schemaTypeGUID);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(schemaTypeGUID, schemaAttributeGUID);
        }
    }


    /**
     * Remove the linked schema types from a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypes(String schemaAttributeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        filesAndFoldersClient.clearSchemaTypes(userId, externalSourceGUID, externalSourceName, schemaAttributeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaAttributeGUID);
        }
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                    schemaAttributeGUID,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        filesAndFoldersClient.updateSchemaAttribute(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    schemaAttributeGUID,
                                                    isMergeUpdate,
                                                    schemaAttributeProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaAttributeGUID);
        }
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String schemaAttributeGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        filesAndFoldersClient.removeSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaAttributeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(schemaAttributeGUID);
        }
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> findSchemaAttributes(String searchString,
                                                             String typeName,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return filesAndFoldersClient.findSchemaAttributes(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attributes associated with a StructSchemaType or nested underneath a schema attribute.
     *
     * @param parentSchemaElementGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getNestedAttributes(String parentSchemaElementGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return filesAndFoldersClient.getNestedAttributes(userId, parentSchemaElementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getSchemaAttributesByName(String name,
                                                                  String typeName,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return filesAndFoldersClient.getSchemaAttributesByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String schemaAttributeGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return filesAndFoldersClient.getSchemaAttributeByGUID(userId, schemaAttributeGUID);
    }




    /* =====================================================================================================================
     * A Connection is the top level object for working with connectors
     */

    /**
     * Create a new metadata element to represent a connection.
     *
     * @param connectionProperties properties about the connection to store
     *
     * @return unique identifier of the new connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnection(ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String connectionGUID = connectionManagerClient.createConnection(userId, null, null, connectionProperties);

        if ((connectionGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(connectionGUID);
        }

        return connectionGUID;
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectionFromTemplate(String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        String connectionGUID = connectionManagerClient.createConnectionFromTemplate(userId,
                                                                                     null,
                                                                                     null,
                                                                                     templateGUID,
                                                                                     templateProperties);

        if ((connectionGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(connectionGUID);
        }

        return connectionGUID;
    }


    /**
     * Update the metadata element representing a connection.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnection(String               connectionGUID,
                                 boolean              isMergeUpdate,
                                 ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        connectionManagerClient.updateConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, isMergeUpdate, connectionProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupConnectorType(String  connectionGUID,
                                   String  connectorTypeGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        connectionManagerClient.setupConnectorType(userId, null, null, connectionGUID, connectorTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConnectorType(String connectionGUID,
                                   String connectorTypeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        connectionManagerClient.clearConnectorType(userId, externalSourceGUID, externalSourceName, connectionGUID, connectorTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEndpoint(String  connectionGUID,
                              String  endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        connectionManagerClient.setupEndpoint(userId, null, null, connectionGUID, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEndpoint(String connectionGUID,
                              String endpointGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        connectionManagerClient.clearEndpoint(userId, externalSourceGUID, externalSourceName, connectionGUID, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param position which order should this connection be processed
     * @param arguments What additional properties should be passed to the embedded connector via the configuration properties
     * @param displayName what does this connector signify?
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEmbeddedConnection(String              connectionGUID,
                                        int                 position,
                                        String              displayName,
                                        Map<String, Object> arguments,
                                        String              embeddedConnectionGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        connectionManagerClient.setupEmbeddedConnection(userId, null, null, connectionGUID, position, displayName, arguments, embeddedConnectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(embeddedConnectionGUID, connectionGUID);
            integrationReportWriter.reportElementUpdate(embeddedConnectionGUID);
        }
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEmbeddedConnection(String connectionGUID,
                                        String embeddedConnectionGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        connectionManagerClient.clearEmbeddedConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, embeddedConnectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(embeddedConnectionGUID, connectionGUID);
            integrationReportWriter.reportElementUpdate(embeddedConnectionGUID);
        }
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param assetSummary summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connectionGUID unique identifier of the  connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAssetConnection(String  assetGUID,
                                     String  assetSummary,
                                     String  connectionGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        connectionManagerClient.setupAssetConnection(userId, null, null, assetGUID, assetSummary, connectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(connectionGUID, assetGUID);
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetConnection(String assetGUID,
                                     String connectionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        connectionManagerClient.clearAssetConnection(userId, externalSourceGUID, externalSourceName, assetGUID, connectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(connectionGUID, assetGUID);
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove the metadata element representing a connection.
     *
     * @param connectionGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnection(String connectionGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        connectionManagerClient.removeConnection(userId, externalSourceGUID, externalSourceName, connectionGUID);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> findConnections(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return connectionManagerClient.findConnections(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name used to retrieve the connection
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> getConnectionsByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return connectionManagerClient.getConnectionsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param connectionGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionElement getConnectionByGUID(String connectionGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return connectionManagerClient.getConnectionByGUID(userId, connectionGUID);
    }


    /**
     * Create a new metadata element to represent an endpoint
     *
     * @param endpointProperties properties about the endpoint to store
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpoint(EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        String endpointGUID = connectionManagerClient.createEndpoint(userId, null, null, endpointProperties);

        if ((endpointGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     *
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(String             networkAddress,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String endpointGUID = connectionManagerClient.createEndpointFromTemplate(userId,
                                                                                 externalSourceGUID,
                                                                                 externalSourceName,
                                                                                 networkAddress,
                                                                                 templateGUID,
                                                                                 templateProperties);

        if ((endpointGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Update the metadata element representing an endpoint.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEndpoint(boolean            isMergeUpdate,
                               String             endpointGUID,
                               EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        connectionManagerClient.updateEndpoint(userId, externalSourceGUID, externalSourceName, isMergeUpdate, endpointGUID, endpointProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(endpointGUID);
        }
    }


    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param endpointGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEndpoint(String endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        connectionManagerClient.removeEndpoint(userId, externalSourceGUID, externalSourceName, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(endpointGUID);
        }
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> findEndpoints(String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return connectionManagerClient.findEndpoints(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsByName(String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return connectionManagerClient.getEndpointsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointElement getEndpointByGUID(String endpointGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return connectionManagerClient.getEndpointByGUID(userId, endpointGUID);
    }


    /**
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> findConnectorTypes(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return connectionManagerClient.findConnectorTypes(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> getConnectorTypesByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return connectionManagerClient.getConnectorTypesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param connectorTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeElement getConnectorTypeByGUID(String connectorTypeGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return connectionManagerClient.getConnectorTypeByGUID(userId, connectorTypeGUID);
    }


    /* =====================================================================================================================
     * A ValidValue is the top level object for working with valid values
     */

    /**
     * Create a new metadata element to represent a valid value.
     *
     * @param validValueProperties properties about the valid value to store
     *
     * @return unique identifier of the new valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createValidValue(ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String validValueGUID = validValueManagement.createValidValue(userId, externalSourceGUID, externalSourceName, validValueProperties);

        if ((validValueGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(validValueGUID);
        }

        return validValueGUID;
    }


    /**
     * Update the metadata element representing a valid value.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param validValueGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param validValueProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateValidValue(String               validValueGUID,
                                 boolean              isMergeUpdate,
                                 ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        validValueManagement.updateValidValue(userId, externalSourceGUID, externalSourceName, validValueGUID, isMergeUpdate, validValueProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(validValueGUID);
        }
    }


    /**
     * Create a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param validValueSetGUID unique identifier of the valid value set
     * @param properties describes the properties of the membership
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupValidValueMember(String                         validValueSetGUID,
                                      ValidValueMembershipProperties properties,
                                      String                         validValueMemberGUID) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        validValueManagement.setupValidValueMember(userId, externalSourceGUID, externalSourceName, validValueSetGUID, properties, validValueMemberGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(validValueMemberGUID, validValueSetGUID);
            integrationReportWriter.reportElementUpdate(validValueSetGUID);
        }
    }


    /**
     * Remove a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param validValueSetGUID unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearValidValueMember(String validValueSetGUID,
                                      String validValueMemberGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        validValueManagement.clearValidValueMember(userId, externalSourceGUID, externalSourceName, validValueSetGUID, validValueMemberGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(validValueMemberGUID, validValueSetGUID);
            integrationReportWriter.reportElementUpdate(validValueSetGUID);
        }
    }


    /**
     * Create a valid value assignment relationship between an element and a valid value (typically, a valid value set) to show that
     * the valid value defines the values that can be stored in the data item that the element represents.
     *
     * @param elementGUID unique identifier of the element
     * @param properties describes the permissions that the role has in the validValue
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupValidValues(String                         elementGUID,
                                 ValidValueAssignmentProperties properties,
                                 String                         validValueGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        validValueManagement.setupValidValues(userId, externalSourceGUID, externalSourceName, elementGUID, properties, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove a valid value assignment relationship between an element and a valid value.
     *
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearValidValues(String elementGUID,
                                 String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        validValueManagement.clearValidValues(userId, externalSourceGUID, externalSourceName, elementGUID, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Create a reference value assignment relationship between an element and a valid value to show that
     * the valid value is a semiformal tag/classification.
     *
     * @param elementGUID unique identifier of the element
     * @param properties describes the quality of the assignment
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupReferenceValueTag(String                             elementGUID,
                                       ReferenceValueAssignmentProperties properties,
                                       String                             validValueGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        validValueManagement.setupReferenceValueTag(userId, externalSourceGUID, externalSourceName, elementGUID, properties, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove a reference value assignment relationship between an element and a valid value.
     *
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearReferenceValueTag(String elementGUID,
                                       String validValueGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        validValueManagement.clearReferenceValueTag(userId, externalSourceGUID, externalSourceName, elementGUID, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove the metadata element representing a valid value.
     *
     * @param validValueGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeValidValue(String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        validValueManagement.removeValidValue(userId, externalSourceGUID, externalSourceName, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(validValueGUID);
        }
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> findValidValues(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return validValueManagement.findValidValues(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> getValidValuesByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return validValueManagement.getValidValuesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid values.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> getAllValidValues(int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return validValueManagement.getAllValidValues(userId, startFrom, pageSize);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param validValueSetGUID          unique identifier of the valid value set
     * @param startFrom                  paging starting point
     * @param pageSize                   maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ValidValueElement> getValidValueSetMembers(String  validValueSetGUID,
                                                           int     startFrom,
                                                           int     pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return validValueManagement.getValidValueSetMembers(userId, validValueSetGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param validValueGUID          unique identifier of valid value to query
     * @param startFrom               paging starting point
     * @param pageSize                maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ValidValueElement> getSetsForValidValue(String  validValueGUID,
                                                        int     startFrom,
                                                        int     pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return validValueManagement.getSetsForValidValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid value set linked to an element as its set of valid values.
     *
     * @param elementGUID unique identifier for the element using the valid value set
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ValidValueElement getValidValuesForConsumer(String elementGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return validValueManagement.getValidValuesForConsumer(userId, elementGUID);
    }


    /**
     * Return information about the consumers linked to a validValue.
     *
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<RelatedElement> getConsumersOfValidValue(String validValueGUID,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return validValueManagement.getConsumersOfValidValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid values linked as reference value tags to an element.
     *
     * @param elementGUID unique identifier for the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of valid values
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ValidValueElement> getReferenceValues(String elementGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return validValueManagement.getReferenceValues(userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Return information about the person roles linked to a validValue.
     *
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<RelatedElement> getAssigneesOfReferenceValue(String validValueGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return validValueManagement.getAssigneesOfReferenceValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param validValueGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueElement getValidValueByGUID(String validValueGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return validValueManagement.getValidValueByGUID(userId, validValueGUID);
    }
}
