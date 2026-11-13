/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.dcat;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.integration.dcat.controls.DcatConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.integration.dcat.ffdc.DcatIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.dcat.DcatDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatGenerator;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatCatalog;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataService;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatResource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DcatPublisherIntegrationConnector publishes the Data Catalog Vocabulary (DCAT) descriptions of the assets that have
 * been chosen for publication.  An asset is chosen by making it a member of a collection that is attached to the
 * connector as a catalog target, or of a collection folder nested in such a collection.  Any type of collection may
 * be used - it does not need to be a root collection.
 * <p>
 * Each catalog target collection is published as a separate DCAT catalog in its own subdirectory of the directory
 * named in the network address of the endpoint in the connector's connection.  The subdirectory is named after the
 * collection, and on each refresh the connector writes the following JSON-LD files to it:
 * <ul>
 *     <li>catalog.jsonld - the collection as a dcat:Catalog, along with the description of every published asset.
 *     This is the file to give to a portal that harvests DCAT catalogs.</li>
 *     <li>datasets/&lt;name&gt;.jsonld - the description of each asset that is published as a dcat:Dataset.</li>
 *     <li>data-services/&lt;name&gt;.jsonld - the description of each asset that is published as a dcat:DataService.</li>
 * </ul>
 * A file is only written if its content has changed.  The file for an asset that is no longer in the collection is
 * removed, as is the subdirectory of a collection that is no longer a catalog target.
 */
public class DcatPublisherIntegrationConnector extends IntegrationConnectorBase
{
    static final String CATALOG_FILE_NAME       = "catalog.jsonld";
    static final String DATASETS_FOLDER         = "datasets";
    static final String DATA_SERVICES_FOLDER    = "data-services";
    static final String DOCUMENT_FILE_EXTENSION = ".jsonld";

    private DcatGenerator generator         = null;
    private String        endpointDirectory = null;


    /**
     * Default constructor
     */
    public DcatPublisherIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        Endpoint endpoint = connectionBean.getEndpoint();

        if ((endpoint != null) && (endpoint.getNetworkAddress() != null) && (! endpoint.getNetworkAddress().isBlank()))
        {
            endpointDirectory = DcatFilesReceiverIntegrationConnector.stripFilePrefix(endpoint.getNetworkAddress());
        }

        if (integrationContext != null)
        {
            generator = new DcatGenerator(integrationContext);
            generator.setDefaultPublisherName(super.getStringConfigurationProperty(DcatConfigurationProperty.DEFAULT_PUBLISHER.getName(),
                                                                                   connectionBean.getConfigurationProperties()));
        }
    }


    /**
     * Publish the DCAT descriptions of the assets in each of the collections attached as catalog targets.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected before/during refresh
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        if ((integrationContext == null) || (generator == null))
        {
            return;
        }

        if (endpointDirectory == null)
        {
            logRecord(methodName, DcatIntegrationConnectorAuditCode.NO_DESTINATION.getMessageDefinition(connectorName));
            return;
        }

        try
        {
            List<String> publishingCatalogGUIDs = getPublishingCatalogGUIDs();

            if (publishingCatalogGUIDs.isEmpty())
            {
                logRecord(methodName, DcatIntegrationConnectorAuditCode.NO_PUBLISHING_CATALOGS.getMessageDefinition(connectorName));
            }

            File      rootDirectory      = new File(endpointDirectory);
            Set<File> catalogDirectories = new HashSet<>();
            boolean   allPublished       = true;

            for (String publishingCatalogGUID : publishingCatalogGUIDs)
            {
                try
                {
                    catalogDirectories.add(publishCatalog(publishingCatalogGUID, rootDirectory));
                }
                catch (UserNotAuthorizedException error)
                {
                    throw error;
                }
                catch (Exception error)
                {
                    allPublished = false;

                    logExceptionRecord(methodName,
                                       DcatIntegrationConnectorAuditCode.CATALOG_NOT_PUBLISHED.getMessageDefinition(connectorName,
                                                                                                                    publishingCatalogGUID,
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                       error);
                }
            }

            /*
             * The directory of a catalog that failed is not known, so nothing is withdrawn until every catalog
             * publishes cleanly.
             */
            if (allPublished)
            {
                removeWithdrawnCatalogs(rootDirectory, catalogDirectories);
            }
        }
        catch (UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               DcatIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                               error);
        }
    }


    /**
     * Return the unique identifiers of the collections attached to this connector as catalog targets.  Catalog
     * targets that are not collections are reported and ignored.
     *
     * @return list of collection GUIDs, in the order that they are retrieved
     * @throws Exception problem accessing open metadata
     */
    private List<String> getPublishingCatalogGUIDs() throws Exception
    {
        final String methodName = "getPublishingCatalogGUIDs";

        List<String> publishingCatalogGUIDs = new ArrayList<>();

        if (integrationContext.getIntegrationConnectorGUID() == null)
        {
            return publishingCatalogGUIDs;
        }

        AssetClient assetClient = integrationContext.getAssetClient();
        int         startFrom   = 0;
        int         pageSize    = integrationContext.getMaxPageSize();

        if (pageSize <= 0)
        {
            pageSize = 100;
        }

        List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                     getCatalogTargetQueryOptions(assetClient, startFrom, pageSize));

        while ((catalogTargets != null) && (! catalogTargets.isEmpty()))
        {
            for (OpenMetadataRootElement catalogTarget : catalogTargets)
            {
                if ((catalogTarget == null) || (catalogTarget.getElementHeader() == null))
                {
                    continue;
                }

                String catalogTargetGUID = catalogTarget.getElementHeader().getGUID();

                if (! propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.COLLECTION.typeName))
                {
                    logRecord(methodName,
                              DcatIntegrationConnectorAuditCode.CATALOG_TARGET_NOT_COLLECTION.getMessageDefinition(connectorName,
                                                                                                                   catalogTarget.getElementHeader().getType().getTypeName(),
                                                                                                                   catalogTargetGUID));
                }
                else if (! publishingCatalogGUIDs.contains(catalogTargetGUID))
                {
                    publishingCatalogGUIDs.add(catalogTargetGUID);
                }
            }

            if (catalogTargets.size() < pageSize)
            {
                break;
            }

            startFrom      = startFrom + pageSize;
            catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                           getCatalogTargetQueryOptions(assetClient, startFrom, pageSize));
        }

        return publishingCatalogGUIDs;
    }


    /**
     * Return the options for retrieving the catalog targets.  Only the catalog target elements themselves are needed.
     *
     * @param assetClient client
     * @param startFrom paging start
     * @param pageSize paging size
     * @return query options
     */
    private QueryOptions getCatalogTargetQueryOptions(AssetClient assetClient,
                                                      int         startFrom,
                                                      int         pageSize)
    {
        QueryOptions queryOptions = assetClient.getQueryOptions(startFrom, pageSize);

        queryOptions.setGraphQueryDepth(0);

        return queryOptions;
    }


    /**
     * Publish one collection as a DCAT catalog in its own subdirectory of the destination directory.
     *
     * @param publishingCatalogGUID unique identifier of the collection
     * @param rootDirectory the directory named in the connector's endpoint
     * @return the subdirectory that the catalog was published to
     * @throws Exception problem accessing open metadata
     */
    private File publishCatalog(String publishingCatalogGUID,
                                File   rootDirectory) throws Exception
    {
        /*
         * The assets are kept in the order that they are found, so that the catalog file only changes when the
         * metadata does.
         */
        Map<String, DcatResource> resources = new LinkedHashMap<>();

        collectResources(publishingCatalogGUID, new HashSet<>(), resources);

        List<DcatResource> resourceList = new ArrayList<>(resources.values());
        DcatCatalog        catalog      = generator.generateCatalog(publishingCatalogGUID, resourceList);
        String             catalogJSON  = DcatDocumentFormatter.toJSONLD(generator.getDocument(catalog, resourceList));
        int                dataServiceCount = 0;

        for (DcatResource resource : resourceList)
        {
            if (resource instanceof DcatDataService)
            {
                dataServiceCount++;
            }
        }

        File catalogDirectory = new File(rootDirectory, getDirectoryName(catalog.getTitle(), publishingCatalogGUID));

        publishToDirectory(catalogDirectory,
                           catalog.getTitle(),
                           resources,
                           catalogJSON,
                           resourceList.size() - dataServiceCount,
                           dataServiceCount);

        return catalogDirectory;
    }


    /**
     * Generate the DCAT description of each asset that is a member of a collection, or of the collection folders
     * nested in it.
     *
     * @param collectionGUID unique identifier of the collection
     * @param visitedCollectionGUIDs the collections that have already been processed (to protect against loops)
     * @param resources the descriptions generated so far, keyed by the unique identifier of the asset
     * @throws Exception problem accessing open metadata
     */
    private void collectResources(String                    collectionGUID,
                                  Set<String>               visitedCollectionGUIDs,
                                  Map<String, DcatResource> resources) throws Exception
    {
        final String methodName = "collectResources";

        if (! visitedCollectionGUIDs.add(collectionGUID))
        {
            return;
        }

        CollectionClient collectionClient = integrationContext.getCollectionClient();
        int              startFrom        = 0;
        int              pageSize         = integrationContext.getMaxPageSize();

        if (pageSize <= 0)
        {
            pageSize = 100;
        }

        List<OpenMetadataRootElement> members = collectionClient.getCollectionMembers(collectionGUID, getMemberQueryOptions(collectionClient, startFrom, pageSize));

        while ((members != null) && (! members.isEmpty()))
        {
            for (OpenMetadataRootElement member : members)
            {
                if ((member == null) || (member.getElementHeader() == null))
                {
                    continue;
                }

                String memberGUID = member.getElementHeader().getGUID();

                if (propertyHelper.isTypeOf(member.getElementHeader(), OpenMetadataType.COLLECTION_FOLDER.typeName))
                {
                    collectResources(memberGUID, visitedCollectionGUIDs, resources);
                }
                else if ((propertyHelper.isTypeOf(member.getElementHeader(), OpenMetadataType.ASSET.typeName)) && (! resources.containsKey(memberGUID)))
                {
                    try
                    {
                        resources.put(memberGUID, generator.generateResource(memberGUID));
                    }
                    catch (Exception error)
                    {
                        logRecord(methodName,
                                  DcatIntegrationConnectorAuditCode.RESOURCE_NOT_PUBLISHED.getMessageDefinition(connectorName,
                                                                                                                memberGUID,
                                                                                                                error.getClass().getName(),
                                                                                                                error.getMessage()));
                    }
                }
            }

            if (members.size() < pageSize)
            {
                break;
            }

            startFrom = startFrom + pageSize;
            members   = collectionClient.getCollectionMembers(collectionGUID, getMemberQueryOptions(collectionClient, startFrom, pageSize));
        }
    }


    /**
     * Return the options for retrieving the members of a collection.  Only the members themselves are needed.
     *
     * @param collectionClient client
     * @param startFrom paging start
     * @param pageSize paging size
     * @return query options
     */
    private QueryOptions getMemberQueryOptions(CollectionClient collectionClient,
                                               int              startFrom,
                                               int              pageSize)
    {
        QueryOptions queryOptions = collectionClient.getQueryOptions(startFrom, pageSize);

        queryOptions.setGraphQueryDepth(0);

        return queryOptions;
    }


    /**
     * Write the catalog, and the description of each published asset, to a destination directory.  Files are only
     * written if their content has changed.  The files for assets that are no longer published are removed.
     *
     * @param destinationDirectory directory to write to
     * @param catalogTitle name of the collection being published
     * @param resources descriptions of the published assets, keyed by the unique identifier of the asset
     * @param catalogJSON content of the catalog file
     * @param datasetCount number of datasets described in the catalog
     * @param dataServiceCount number of data services described in the catalog
     */
    private void publishToDirectory(File                      destinationDirectory,
                                    String                    catalogTitle,
                                    Map<String, DcatResource> resources,
                                    String                    catalogJSON,
                                    int                       datasetCount,
                                    int                       dataServiceCount)
    {
        final String methodName = "publishToDirectory";

        try
        {
            FileUtils.forceMkdir(destinationDirectory);

            Set<File> currentFiles = new HashSet<>();

            for (Map.Entry<String, DcatResource> resource : resources.entrySet())
            {
                boolean isDataService = resource.getValue() instanceof DcatDataService;
                File    resourceFile  = new File(new File(destinationDirectory, isDataService ? DATA_SERVICES_FOLDER : DATASETS_FOLDER),
                                                 getFileName(resource.getValue().getTitle(), resource.getKey()));

                currentFiles.add(resourceFile);

                if (writeIfChanged(resourceFile, DcatDocumentFormatter.toJSONLD(generator.getDocument(null, List.of(resource.getValue())))))
                {
                    logRecord(methodName,
                              DcatIntegrationConnectorAuditCode.RESOURCE_STORED.getMessageDefinition(connectorName,
                                                                                                     isDataService ? "data service" : "dataset",
                                                                                                     resource.getValue().getTitle(),
                                                                                                     resource.getKey(),
                                                                                                     resourceFile.getPath()));
                }
            }

            removeWithdrawnFiles(new File(destinationDirectory, DATASETS_FOLDER), currentFiles);
            removeWithdrawnFiles(new File(destinationDirectory, DATA_SERVICES_FOLDER), currentFiles);

            File catalogFile = new File(destinationDirectory, CATALOG_FILE_NAME);

            if (writeIfChanged(catalogFile, catalogJSON))
            {
                logRecord(methodName,
                          DcatIntegrationConnectorAuditCode.CATALOG_STORED.getMessageDefinition(connectorName,
                                                                                                catalogTitle,
                                                                                                catalogFile.getPath(),
                                                                                                Integer.toString(datasetCount),
                                                                                                Integer.toString(dataServiceCount)));
            }
        }
        catch (Exception error)
        {
            logRecord(methodName,
                      DcatIntegrationConnectorAuditCode.DIRECTORY_NOT_ACCESSIBLE.getMessageDefinition(connectorName,
                                                                                                      destinationDirectory.getPath(),
                                                                                                      "the connector's endpoint",
                                                                                                      error.getClass().getName() + ": " + error.getMessage()));
        }
    }


    /**
     * Write a file unless it already has the requested content.
     *
     * @param file file to write
     * @param content content of the file
     * @return true if the file was written
     * @throws Exception problem reading or writing the file
     */
    static boolean writeIfChanged(File   file,
                                  String content) throws Exception
    {
        if ((file.isFile()) && (content.equals(FileUtils.readFileToString(file, StandardCharsets.UTF_8))))
        {
            return false;
        }

        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, false);

        return true;
    }


    /**
     * Remove the descriptions of assets that are no longer published from a folder that this connector writes to.
     *
     * @param folder folder of descriptions
     * @param currentFiles the files for the assets that are currently published
     */
    private void removeWithdrawnFiles(File      folder,
                                      Set<File> currentFiles)
    {
        final String methodName = "removeWithdrawnFiles";

        File[] files = folder.listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if ((file.isFile()) && (file.getName().endsWith(DOCUMENT_FILE_EXTENSION)) && (! currentFiles.contains(file)) && (file.delete()))
                {
                    logRecord(methodName, DcatIntegrationConnectorAuditCode.RESOURCE_WITHDRAWN.getMessageDefinition(connectorName, file.getPath()));
                }
            }
        }
    }


    /**
     * Return the name of the file for the description of an asset.  It includes part of the asset's unique identifier
     * so that assets with the same name are written to different files.
     *
     * @param title display name of the asset
     * @param assetGUID unique identifier of the asset
     * @return file name
     */
    static String getFileName(String title,
                              String assetGUID)
    {
        String shortGUID = (assetGUID.length() > 8) ? assetGUID.substring(0, 8) : assetGUID;
        String name      = (title == null) ? "" : title.replaceAll("[^A-Za-z0-9._-]+", "-").replaceAll("^-+|-+$", "");

        if (name.length() > 80)
        {
            name = name.substring(0, 80);
        }

        return name.isEmpty() ? shortGUID + DOCUMENT_FILE_EXTENSION : name + "-" + shortGUID + DOCUMENT_FILE_EXTENSION;
    }


    /**
     * Return the name of the subdirectory for a published catalog.  It includes part of the collection's unique
     * identifier so that collections with the same name are published to different directories.
     *
     * @param title display name of the collection
     * @param collectionGUID unique identifier of the collection
     * @return directory name
     */
    static String getDirectoryName(String title,
                                   String collectionGUID)
    {
        String fileName = getFileName(title, collectionGUID);

        return fileName.substring(0, fileName.length() - DOCUMENT_FILE_EXTENSION.length());
    }


    /**
     * Remove the subdirectories of catalogs that are no longer published.  Only subdirectories that contain a
     * catalog file are removed, so that anything else stored in the destination directory is left alone.
     *
     * @param rootDirectory the directory named in the connector's endpoint
     * @param catalogDirectories the subdirectories of the catalogs that are currently published
     */
    private void removeWithdrawnCatalogs(File      rootDirectory,
                                         Set<File> catalogDirectories)
    {
        final String methodName = "removeWithdrawnCatalogs";

        File[] directories = rootDirectory.listFiles(File::isDirectory);

        if (directories != null)
        {
            for (File directory : directories)
            {
                if ((! catalogDirectories.contains(directory)) && (new File(directory, CATALOG_FILE_NAME).isFile()))
                {
                    try
                    {
                        FileUtils.deleteDirectory(directory);

                        logRecord(methodName, DcatIntegrationConnectorAuditCode.CATALOG_WITHDRAWN.getMessageDefinition(connectorName, directory.getPath()));
                    }
                    catch (Exception error)
                    {
                        logRecord(methodName,
                                  DcatIntegrationConnectorAuditCode.DIRECTORY_NOT_ACCESSIBLE.getMessageDefinition(connectorName,
                                                                                                                  directory.getPath(),
                                                                                                                  "the connector's endpoint",
                                                                                                                  error.getClass().getName() + ": " + error.getMessage()));
                    }
                }
            }
        }
    }
}
