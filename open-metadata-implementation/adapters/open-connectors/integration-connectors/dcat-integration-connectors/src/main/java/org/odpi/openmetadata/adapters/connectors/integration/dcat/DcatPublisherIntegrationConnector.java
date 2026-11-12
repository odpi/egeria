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
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
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
 * been chosen for publication.  An asset is chosen by making it a member of the publishing catalog - a collection that
 * is named in the connector's publishingCatalog configuration property - or of a collection folder nested in it.
 * <p>
 * On each refresh the connector writes the following JSON-LD files to each of its destination directories:
 * <ul>
 *     <li>catalog.jsonld - the publishing catalog as a dcat:Catalog, along with the description of every published
 *     asset.  This is the file to give to a portal that harvests DCAT catalogs.</li>
 *     <li>datasets/&lt;name&gt;.jsonld - the description of each asset that is published as a dcat:Dataset.</li>
 *     <li>data-services/&lt;name&gt;.jsonld - the description of each asset that is published as a dcat:DataService.</li>
 * </ul>
 * A file is only written if its content has changed, and the file for an asset that is no longer in the publishing
 * catalog is removed.  The destination directories are the network address of the endpoint in the connector's
 * connection, and the file folders attached to the connector as catalog targets.
 */
public class DcatPublisherIntegrationConnector extends IntegrationConnectorBase
{
    static final String CATALOG_FILE_NAME      = "catalog.jsonld";
    static final String DATASETS_FOLDER        = "datasets";
    static final String DATA_SERVICES_FOLDER   = "data-services";
    static final String DOCUMENT_FILE_EXTENSION = ".jsonld";

    private DcatGenerator generator             = null;
    private String        publishingCatalogName = DcatConfigurationProperty.DEFAULT_PUBLISHING_CATALOG;
    private String        endpointDirectory     = null;


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

        String configuredCatalogName = super.getStringConfigurationProperty(DcatConfigurationProperty.PUBLISHING_CATALOG.getName(),
                                                                            connectionBean.getConfigurationProperties());

        if ((configuredCatalogName != null) && (! configuredCatalogName.isBlank()))
        {
            publishingCatalogName = configuredCatalogName;
        }

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
     * Publish the DCAT descriptions of the assets in the publishing catalog.
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

        try
        {
            List<String> destinationDirectories = getDestinationDirectories();

            if (destinationDirectories.isEmpty())
            {
                logRecord(methodName, DcatIntegrationConnectorAuditCode.NO_DESTINATION.getMessageDefinition(connectorName));
                return;
            }

            String publishingCatalogGUID = getPublishingCatalogGUID();

            if (publishingCatalogGUID == null)
            {
                logRecord(methodName,
                          DcatIntegrationConnectorAuditCode.PUBLISHING_CATALOG_NOT_FOUND.getMessageDefinition(connectorName,
                                                                                                              publishingCatalogName,
                                                                                                              DcatConfigurationProperty.PUBLISHING_CATALOG.getName()));
                return;
            }

            /*
             * The assets are kept in the order that they are found, so that the catalog file only changes when the
             * metadata does.
             */
            Map<String, DcatResource> resources = new LinkedHashMap<>();

            collectResources(publishingCatalogGUID, new HashSet<>(), resources);

            List<DcatResource> resourceList = new ArrayList<>(resources.values());
            DcatCatalog        catalog      = generator.generateCatalog(publishingCatalogGUID, resourceList);
            String             catalogJSON  = DcatDocumentFormatter.toJSONLD(generator.getDocument(catalog, resourceList));

            int dataServiceCount = 0;

            for (DcatResource resource : resourceList)
            {
                if (resource instanceof DcatDataService)
                {
                    dataServiceCount++;
                }
            }

            for (String destinationDirectory : destinationDirectories)
            {
                publishToDirectory(new File(destinationDirectory),
                                   resources,
                                   catalogJSON,
                                   resourceList.size() - dataServiceCount,
                                   dataServiceCount);
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
     * Locate the publishing catalog.  The configured name is tried as a qualified name first and then as a name.
     *
     * @return unique identifier of the collection, or null if it does not exist
     * @throws Exception problem accessing open metadata
     */
    private String getPublishingCatalogGUID() throws Exception
    {
        OpenMetadataElement collection = integrationContext.getOpenMetadataStore().getMetadataElementByUniqueName(publishingCatalogName,
                                                                                                                  OpenMetadataProperty.QUALIFIED_NAME.name);

        if ((collection != null) && (propertyHelper.isTypeOf(collection, OpenMetadataType.COLLECTION.typeName)))
        {
            return collection.getElementGUID();
        }

        CollectionClient              collectionClient = integrationContext.getCollectionClient();
        List<OpenMetadataRootElement> collections      = collectionClient.getCollectionsByName(publishingCatalogName,
                                                                                               collectionClient.getQueryOptions(0, integrationContext.getMaxPageSize()));

        if (collections != null)
        {
            for (OpenMetadataRootElement namedCollection : collections)
            {
                if ((namedCollection != null) && (namedCollection.getElementHeader() != null))
                {
                    return namedCollection.getElementHeader().getGUID();
                }
            }
        }

        return null;
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
     * @param resources descriptions of the published assets, keyed by the unique identifier of the asset
     * @param catalogJSON content of the catalog file
     * @param datasetCount number of datasets described in the catalog
     * @param dataServiceCount number of data services described in the catalog
     */
    private void publishToDirectory(File                      destinationDirectory,
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
                                                                                                publishingCatalogName,
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
                                                                                                      "the connector's endpoint or catalog targets",
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
     * Return the directories to publish to: the directory named in the connection's endpoint and the file folders
     * attached to the connector as catalog targets.
     *
     * @return list of directory names
     * @throws Exception problem accessing open metadata
     */
    private List<String> getDestinationDirectories() throws Exception
    {
        List<String> destinationDirectories = new ArrayList<>();

        if (endpointDirectory != null)
        {
            destinationDirectories.add(endpointDirectory);
        }

        if (integrationContext.getIntegrationConnectorGUID() == null)
        {
            return destinationDirectories;
        }

        AssetClient                   assetClient    = integrationContext.getAssetClient();
        int                           startFrom      = 0;
        int                           maxPageSize    = integrationContext.getMaxPageSize();
        List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                     this.getCatalogTargetQueryOptions(assetClient, startFrom, maxPageSize));

        while ((catalogTargets != null) && (! catalogTargets.isEmpty()))
        {
            for (OpenMetadataRootElement catalogTarget : catalogTargets)
            {
                if (catalogTarget != null)
                {
                    String directoryName = getDirectoryName(catalogTarget, assetClient);

                    if ((directoryName != null) && (! destinationDirectories.contains(directoryName)))
                    {
                        destinationDirectories.add(directoryName);
                    }
                }
            }

            if ((maxPageSize <= 0) || (catalogTargets.size() < maxPageSize))
            {
                break;
            }

            startFrom      = startFrom + maxPageSize;
            catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                           this.getCatalogTargetQueryOptions(assetClient, startFrom, maxPageSize));
        }

        return destinationDirectories;
    }


    /**
     * Return the options for retrieving the catalog targets, along with the connections and endpoints that locate them.
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

        queryOptions.setGraphQueryDepth(1);
        queryOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName,
                                                         OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName));

        return queryOptions;
    }


    /**
     * Work out the directory that a catalog target represents.  The catalog target may be a file folder, an endpoint,
     * a connection, or an asset with a connection.
     *
     * @param catalogTarget element attached to the connector as a catalog target
     * @param assetClient client
     * @return directory name or null if the catalog target does not identify a directory
     * @throws Exception problem accessing open metadata
     */
    private String getDirectoryName(OpenMetadataRootElement catalogTarget,
                                    AssetClient             assetClient) throws Exception
    {
        if (propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.ENDPOINT.typeName))
        {
            if ((catalogTarget.getProperties() instanceof EndpointProperties endpointProperties) && (endpointProperties.getNetworkAddress() != null))
            {
                return DcatFilesReceiverIntegrationConnector.stripFilePrefix(endpointProperties.getNetworkAddress());
            }
        }
        else if (propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.CONNECTION.typeName))
        {
            return getEndpointAddress(catalogTarget);
        }
        else if (propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.FILE_FOLDER.typeName))
        {
            if ((catalogTarget.getProperties() instanceof FileFolderProperties folderProperties) && (folderProperties.getPathName() != null))
            {
                return DcatFilesReceiverIntegrationConnector.stripFilePrefix(folderProperties.getPathName());
            }
        }
        else if ((propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.ASSET.typeName)) && (catalogTarget.getConnections() != null))
        {
            for (RelatedMetadataElementSummary connection : catalogTarget.getConnections())
            {
                if ((connection != null) && (connection.getRelatedElement() != null))
                {
                    GetOptions getOptions = assetClient.getGetOptions();

                    getOptions.setGraphQueryDepth(1);
                    getOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName));

                    OpenMetadataRootElement connectionElement = integrationContext.getConnectionClient().getConnectionByGUID(connection.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                             getOptions);
                    String directoryName = getEndpointAddress(connectionElement);

                    if (directoryName != null)
                    {
                        return directoryName;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Return the network address of the endpoint attached to a connection.
     *
     * @param connectionElement connection
     * @return directory name or null
     */
    private String getEndpointAddress(OpenMetadataRootElement connectionElement)
    {
        if ((connectionElement != null) && (connectionElement.getEndpoint() != null) && (connectionElement.getEndpoint().getRelatedElement() != null))
        {
            if ((connectionElement.getEndpoint().getRelatedElement().getProperties() instanceof EndpointProperties endpointProperties) &&
                (endpointProperties.getNetworkAddress() != null))
            {
                return DcatFilesReceiverIntegrationConnector.stripFilePrefix(endpointProperties.getNetworkAddress());
            }
        }

        return null;
    }
}
