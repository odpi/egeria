/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * The basic file integration connectors monitor changes in a file directory (folder) and updates the open metadata
 * repository/repositories to reflect the changes to both the files and folders underneath it.
 * <br><br>
 * The DataFilesMonitorIntegrationConnector maintains a DataFile asset for each file in the directory (or any subdirectory).
 * When a new file is created, a new DataFile asset is created.  If a file is modified, the lastModified property
 * of the corresponding DataFile asset is updated.  When a file is deleted, its corresponding DataFile asset is also deleted
 * if the "allowCatalogDelete" configuration property is set - otherwise the DataFile asset is archived.  This archiving process
 * classified the DataFile asset to show that its real-world counterpart has gone whilst preserving the knowledge of the file's
 * role in supplying data to other downstream assets in the lineage graph.
 * <br><br>
 * The DataFolderMonitorIntegrationConnector maintains a DataFolder asset for the directory.  The files and directories
 * underneath it are assumed to be elements/records in the DataFolder asset and so each time there is a change to the
 * files and directories under the monitored directory, it results in an update to the storeUpdateTime property
 * of the corresponding DataFolder asset.
 */
package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;
