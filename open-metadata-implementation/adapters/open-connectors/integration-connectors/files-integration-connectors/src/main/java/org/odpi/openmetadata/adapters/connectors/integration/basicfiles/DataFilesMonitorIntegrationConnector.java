/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * DataFilesMonitorIntegrationConnector monitors a file directory and catalogues the files it finds.
 */
public class DataFilesMonitorIntegrationConnector extends BasicFilesMonitorIntegrationConnectorBase
{
    /**
     * Creates a monitor for the target.
     *
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target GUID
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param configurationProperties parameters to further modify the behaviour of the connector.
     * @return directory to monitor structure
     */
    public DirectoryToMonitor createDirectoryToMonitor(String              sourceName,
                                                       String              pathName,
                                                       String              catalogTargetGUID,
                                                       DeleteMethod        deleteMethod,
                                                       Map<String,String>  templates,
                                                       Map<String, Object> configurationProperties) throws ConnectorCheckedException
    {
        return new DataFilesMonitorForTarget(connectorName,
                                             sourceName,
                                             pathName,
                                             catalogTargetGUID,
                                             deleteMethod,
                                             templates,
                                             configurationProperties,
                                             this,
                                             this.getFolderElement(new File(pathName)),
                                             auditLog);
    }


    /**
     * Retrieve the Folder element from the open metadata repositories.
     * If the directory does not exist the connector waits for the directory to be created.
     *
     * @param dataFolderFile the directory to retrieve the folder from
     * @throws ConnectorCheckedException there is a problem retrieving the folder element.
     */
    OpenMetadataRootElement getFolderElement(File dataFolderFile) throws ConnectorCheckedException
    {
        return super.getFolderElement(dataFolderFile,
                                      DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName(),
                                      DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * <br>
     * This method performs two sweeps.  It first retrieves the files in the directory and validates that are in the
     * catalog - adding or updating them if necessary.  The second sweep is to ensure that all the assets catalogued
     * in this directory actually exist on the file system.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        List<DirectoryToMonitor> directoriesToMonitor = super.getDirectoriesToMonitor();

        for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
        {
            /*
             * Sweep one - cataloguing all files
             */
            directoryToMonitor.refresh();

            /*
             * Sweep two - ensuring all catalogued files still exist.  Notice that if the directory does not exist in the catalog,
             * it is ignored.  It will be dynamically created when a new file is added.
             */
            try
            {
                OpenMetadataRootElement folder = this.getFolderElement(directoryToMonitor.directoryFile);

                if (folder != null)
                {
                    AssetClient assetClient = integrationContext.getAssetClient();

                    int startFrom = 0;
                    int pageSize  = 100;

                    List<OpenMetadataRootElement> cataloguedFiles = assetClient.getFilesInFolder(folder.getElementHeader().getGUID(),
                                                                                      startFrom,
                                                                                      pageSize);

                    while ((cataloguedFiles != null) && (! cataloguedFiles.isEmpty()))
                    {
                        for (OpenMetadataRootElement dataFile : cataloguedFiles)
                        {
                            if ((dataFile != null) && (dataFile.getProperties() instanceof DataFileProperties dataFileProperties))
                            {
                                if (dataFileProperties.getPathName() != null)
                                {
                                    File file = new File(dataFileProperties.getPathName());

                                    if (! file.exists())
                                    {
                                        this.deleteFileInCatalog(file, dataFile, methodName);
                                    }
                                }
                                else
                                {
                                    if (auditLog != null)
                                    {
                                        auditLog.logMessage(methodName,
                                                            BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT.getMessageDefinition(
                                                                    connectorName,
                                                                    dataFile.toString()));
                                    }
                                }
                            }
                        }

                        startFrom = startFrom + pageSize;
                        cataloguedFiles = assetClient.getFilesInFolder(folder.getElementHeader().getGUID(), startFrom, pageSize);
                    }
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(
                                                  error.getClass().getName(),
                                                  connectorName,
                                                  directoryToMonitor.directoryFile.getAbsolutePath(),
                                                  error.getMessage()),
                                          error);

                }

                throw new FileException(
                        BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(error.getClass().getName(),
                                                                                                                      connectorName,
                                                                                                                      directoryToMonitor.directoryFile.getAbsolutePath(),
                                                                                                                      error.getMessage()),
                        error.getClass().getName(),
                        methodName,
                        error,
                        directoryToMonitor.directoryFile.getAbsolutePath());
            }
        }
    }
}
