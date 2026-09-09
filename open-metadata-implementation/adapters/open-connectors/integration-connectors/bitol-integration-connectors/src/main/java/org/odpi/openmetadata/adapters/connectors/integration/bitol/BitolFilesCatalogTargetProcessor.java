/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector;
import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;

/**
 * BitolFilesCatalogTargetProcessor scans the directory described by a FileFolder catalog target for Bitol documents.
 * The directory is located through the connector to the folder if one is available, otherwise from the folder's
 * path name.
 */
public class BitolFilesCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private final BitolDirectoryScanner scanner;


    /**
     * Constructor.
     *
     * @param template catalog target information
     * @param catalogTargetContext context for the catalog target
     * @param connectorToTarget connector to the folder (may be null)
     * @param connectorName name of the integration connector
     * @param auditLog logging destination
     */
    public BitolFilesCatalogTargetProcessor(CatalogTarget        template,
                                            CatalogTargetContext catalogTargetContext,
                                            Connector            connectorToTarget,
                                            String               connectorName,
                                            AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        final String methodName = "BitolFilesCatalogTargetProcessor";

        File   directory  = null;
        String sourceName = OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName + "::" + template.getCatalogTargetName();

        try
        {
            if (connectorToTarget instanceof BasicFolderConnector folderConnector)
            {
                directory = folderConnector.getFile();
            }
        }
        catch (Exception error)
        {
            /*
             * Fall through to the other means of locating the directory.
             */
        }

        if (directory == null)
        {
            String pathName = super.getNetworkAddress();

            if ((pathName == null) && (template.getCatalogTargetElement().getProperties() instanceof FileFolderProperties folderProperties))
            {
                pathName = folderProperties.getPathName();
                sourceName = OpenMetadataType.FILE_FOLDER.typeName + "::" + OpenMetadataProperty.PATH_NAME.name;
            }

            if (pathName != null)
            {
                directory = new File(BitolFilesReceiverIntegrationConnector.stripFilePrefix(pathName));
            }
        }

        this.scanner = new BitolDirectoryScanner(directory, sourceName);

        if (directory != null)
        {
            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.DIRECTORY_MONITORED.getMessageDefinition(connectorName,
                                                                                                            directory.getPath(),
                                                                                                            sourceName));
        }
        else
        {
            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.DIRECTORY_NOT_ACCESSIBLE.getMessageDefinition(connectorName,
                                                                                                                 null,
                                                                                                                 sourceName,
                                                                                                                 "no directory path name found in the catalog target"));
        }
    }


    /**
     * Scan the directory for new and changed Bitol documents.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.refresh();

        if (scanner.getDirectory() != null)
        {
            scanner.scanAndPublish(integrationContext, auditLog, connectorName);
        }
    }
}
