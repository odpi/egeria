/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.FileSystemProperties;

import java.io.File;
import java.util.Map;


/**
 * OMSecretsFilesMonitorIntegrationConnector monitors a file directory and catalogues the files it finds.
 * It has additional
 */
public class OMSecretsFilesMonitorIntegrationConnector extends DataFilesMonitorIntegrationConnector
{
    /**
     * Creates a monitor for the target.
     *
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target GUID
     * @param dataFolderGUID optional GUID of the data folder element
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param configurationProperties parameters to further modify the behaviour of the connector.
     * @return directory to monitor structure
     */
    public DirectoryToMonitor createDirectoryToMonitor(String               sourceName,
                                                       String               pathName,
                                                       String               catalogTargetGUID,
                                                       String               dataFolderGUID,
                                                       DeleteMethod         deleteMethod,
                                                       Map<String,String>   templates,
                                                       FileSystemProperties fileSystemProperties,
                                                       Map<String, Object>  configurationProperties) throws ConnectorCheckedException
    {
        return new OMSecretsFilesMonitorForTarget(connectorName,
                                                  sourceName,
                                                  pathName,
                                                  catalogTargetGUID,
                                                  dataFolderGUID,
                                                  deleteMethod,
                                                  templates,
                                                  fileSystemProperties,
                                                  configurationProperties,
                                                  this,
                                                  auditLog);
    }
}
