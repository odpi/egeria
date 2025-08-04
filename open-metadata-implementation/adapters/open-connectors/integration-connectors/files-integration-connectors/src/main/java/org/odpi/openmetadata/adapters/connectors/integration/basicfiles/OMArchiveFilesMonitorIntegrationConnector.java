/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;

import java.io.File;
import java.util.Map;


/**
 * DataFilesMonitorIntegrationConnector monitors a file directory and catalogues the files it finds.
 */
public class OMArchiveFilesMonitorIntegrationConnector extends DataFilesMonitorIntegrationConnector
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
        return new OMArchiveFilesMonitorForTarget(connectorName,
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
}
