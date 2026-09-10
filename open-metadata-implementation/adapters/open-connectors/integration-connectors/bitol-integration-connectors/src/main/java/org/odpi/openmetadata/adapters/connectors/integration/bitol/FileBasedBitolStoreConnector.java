/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentListener;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * FileBasedBitolStoreConnector stores the Bitol documents (Open Data Contract Standard data contracts and Open Data
 * Product Standard data products) published in the integration daemon to the file system.  Each document is written
 * to {directory}/{kind}/{id}/{version}.yaml (or .json if the document arrived as JSON) so the store can be committed to a
 * git repository as-is.  The directories to write to come from the connection's endpoint and from catalog targets that
 * are Endpoints, Connections or FileFolder assets.
 */
public class FileBasedBitolStoreConnector extends IntegrationConnectorBase implements BitolDocumentListener
{
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
    private static final String            UNPARSED_FOLDER  = "unparsed";

    private BitolFileCataloguer fileCataloguer = null;
    private final List<String> destinationDirectories = new ArrayList<>();


    /**
     * Default constructor
     */
    public FileBasedBitolStoreConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.  This picks up the optional
     * directory named in the connection's endpoint and registers the listener.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        if (integrationContext != null)
        {
            fileCataloguer = new BitolFileCataloguer(integrationContext, auditLog, connectorName);
        }

        Endpoint endpoint = connectionBean.getEndpoint();

        if ((endpoint != null) && (endpoint.getNetworkAddress() != null))
        {
            addDestination(BitolFilesReceiverIntegrationConnector.stripFilePrefix(endpoint.getNetworkAddress()));
        }

        if (integrationContext != null)
        {
            integrationContext.registerBitolListener(this);
        }
    }


    /**
     * Add a new destination directory, creating it if necessary.
     *
     * @param directoryName path name of directory
     * @throws ConnectorCheckedException the directory can not be created
     */
    private synchronized void addDestination(String directoryName) throws ConnectorCheckedException
    {
        final String methodName = "addDestination";

        if ((directoryName != null) && (! destinationDirectories.contains(directoryName)))
        {
            try
            {
                FileUtils.forceMkdir(new File(directoryName));

                destinationDirectories.add(directoryName);
            }
            catch (Exception error)
            {
                throw new ConnectorCheckedException(BitolIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                  error.getClass().getName(),
                                                                                                                                  methodName,
                                                                                                                                  error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * This connector uses the refresh to pick up any new destination directories from its catalog targets.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            AssetClient assetClient = integrationContext.getAssetClient();

            int                           startFrom      = 0;
            int                           maxPageSize    = integrationContext.getMaxPageSize();
            List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                         assetClient.getQueryOptions(startFrom, maxPageSize));

            while (catalogTargets != null)
            {
                for (OpenMetadataRootElement catalogTarget : catalogTargets)
                {
                    if (catalogTarget != null)
                    {
                        addDestination(getDirectoryName(catalogTarget, assetClient));
                    }
                }

                startFrom = startFrom + maxPageSize;

                catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                               assetClient.getQueryOptions(startFrom, maxPageSize));
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract the directory name from a catalog target.  Endpoints supply their network address, connections the network
     * address of their endpoint, file folders their path name and other assets the network address of their first
     * connection's endpoint.
     *
     * @param catalogTarget catalog target element
     * @param assetClient client for retrieving related elements
     * @return directory name or null if none could be found
     * @throws Exception problem retrieving related elements
     */
    private String getDirectoryName(OpenMetadataRootElement catalogTarget,
                                    AssetClient             assetClient) throws Exception
    {
        if (propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.ENDPOINT.typeName))
        {
            if (catalogTarget.getProperties() instanceof EndpointProperties endpointProperties)
            {
                return BitolFilesReceiverIntegrationConnector.stripFilePrefix(endpointProperties.getNetworkAddress());
            }
        }
        else if (propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.CONNECTION.typeName))
        {
            return getEndpointAddress(catalogTarget);
        }
        else if (propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.FILE_FOLDER.typeName))
        {
            if (catalogTarget.getProperties() instanceof FileFolderProperties folderProperties)
            {
                return BitolFilesReceiverIntegrationConnector.stripFilePrefix(folderProperties.getPathName());
            }
        }
        else if ((propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.ASSET.typeName)) && (catalogTarget.getConnections() != null))
        {
            for (RelatedMetadataElementSummary connection : catalogTarget.getConnections())
            {
                if (connection != null)
                {
                    OpenMetadataRootElement connectionElement = integrationContext.getConnectionClient().getConnectionByGUID(connection.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                             assetClient.getGetOptions());

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
     * Return the network address of a connection's endpoint.
     *
     * @param connectionElement connection
     * @return network address or null
     */
    private String getEndpointAddress(OpenMetadataRootElement connectionElement)
    {
        if ((connectionElement != null) && (connectionElement.getEndpoint() != null))
        {
            if (connectionElement.getEndpoint().getRelatedElement().getProperties() instanceof EndpointProperties endpointProperties)
            {
                return BitolFilesReceiverIntegrationConnector.stripFilePrefix(endpointProperties.getNetworkAddress());
            }
        }

        return null;
    }


    /**
     * Store a data contract in each destination directory.
     *
     * @param dataContract parsed document (null if the raw document could not be parsed into the bean)
     * @param rawDocument the document as received (YAML or JSON)
     */
    @Override
    public void processDataContract(DataContract dataContract,
                                    String       rawDocument)
    {
        storeDocument(BitolDocument.DATA_CONTRACT_KIND, dataContract, rawDocument);
    }


    /**
     * Store a data product in each destination directory.
     *
     * @param dataProduct parsed document (null if the raw document could not be parsed into the bean)
     * @param rawDocument the document as received (YAML or JSON)
     */
    @Override
    public void processDataProduct(DataProduct dataProduct,
                                   String      rawDocument)
    {
        storeDocument(BitolDocument.DATA_PRODUCT_KIND, dataProduct, rawDocument);
    }


    /**
     * Write the document to {directory}/{kind}/{id}/{version}.{yaml|json}.  Documents that could not be parsed are
     * written to {directory}/{kind}/unparsed/{timestamp}.{yaml|json} so nothing is lost.
     *
     * @param kind kind of document
     * @param document parsed document (may be null)
     * @param rawDocument document content (may be null if the bean was published directly and failed to serialize)
     */
    private synchronized void storeDocument(String        kind,
                                            BitolDocument document,
                                            String        rawDocument)
    {
        final String methodName = "storeDocument";

        try
        {
            String content = rawDocument;

            if ((content == null) && (document != null))
            {
                content = BitolDocumentFormatter.toYAML(document);
            }

            if (content == null)
            {
                return;
            }

            String extension = content.trim().startsWith("{") ? "json" : "yaml";
            String relativePath;

            if ((document != null) && (document.getId() != null))
            {
                String version = document.getVersion();

                if ((version == null) || (version.isBlank()))
                {
                    version = "unversioned";
                }

                relativePath = kind + File.separator + sanitize(document.getId()) + File.separator + sanitize(version) + "." + extension;
            }
            else
            {
                relativePath = kind + File.separator + UNPARSED_FOLDER + File.separator + ZonedDateTime.now().format(TIMESTAMP_FORMAT) + "." + extension;
            }

            for (String destinationDirectory : destinationDirectories)
            {
                File file = new File(destinationDirectory, relativePath);

                FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, false);

                auditLog.logMessage(methodName,
                                    BitolIntegrationConnectorAuditCode.DOCUMENT_STORED.getMessageDefinition(connectorName,
                                                                                                            kind,
                                                                                                            file.getPath()));

                if (fileCataloguer != null)
                {
                    fileCataloguer.catalogDocumentFile(file, kind, document);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                  rawDocument,
                                  error);
        }
    }


    /**
     * Make a value safe for use as a file or directory name.
     *
     * @param value identifier or version from the document
     * @return value with path separators and other awkward characters replaced
     */
    static String sanitize(String value)
    {
        return value.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
