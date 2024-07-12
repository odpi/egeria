/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * DataFolderMonitorIntegrationConnector monitors a file directory that is linked to a DataFolder asset and
 * maintains the update time of the DataFolder each time one of the files or embedded directories change in some way.
 */
public class DataFolderMonitorIntegrationConnector extends BasicFilesMonitorIntegrationConnectorBase
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
        return new DataFolderMonitorForTarget(connectorName,
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
    FileFolderElement getFolderElement(File dataFolderFile) throws ConnectorCheckedException
    {
        return super.getFolderElement(dataFolderFile,
                                      DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName(),
                                      DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType(),
                                      DataFolderProvider.class.getName());
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        List<DirectoryToMonitor> directoriesToMonitor = super.getDirectoriesToMonitor();

        for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
        {
            /*
             * Sweep one - cataloguing all files
             */
            directoryToMonitor.refresh();
        }
    }
}
