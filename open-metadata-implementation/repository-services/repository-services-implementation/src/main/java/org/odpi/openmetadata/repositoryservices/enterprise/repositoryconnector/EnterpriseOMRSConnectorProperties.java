/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetProperties;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectorManager;


/**
 * EnterpriseOMRSConnectorProperties provides information about the connections and the metadata collections
 * that the EnterpriseOMRSRepositoryConnector is retrieving information from.
 */
public class EnterpriseOMRSConnectorProperties extends ConnectedAssetProperties
{
    private static final long    serialVersionUID = 1L;


    /**
     * Constructor
     *
     * @param parentConnector connector described by these properties
     * @param connectorManager associated connector manager
     * @param enterpriseMetadataCollectionId collection identifier to use for this connector
     * @param enterpriseMetadataCollectionName name of associated metadata collection
     */
    public EnterpriseOMRSConnectorProperties(EnterpriseOMRSRepositoryConnector parentConnector,
                                             OMRSConnectorManager              connectorManager,
                                             String                            enterpriseMetadataCollectionId,
                                             String                            enterpriseMetadataCollectionName)
    {
        super();
        // TODO not implemented yet
    }


    /**
     * Request the values in the EgeriaConnectedAssetProperties are refreshed with the current values from the
     * metadata repository.  In the case of the
     *
     * @throws PropertyServerException there is a problem connecting to the server to retrieve metadata.
     */
    public void refresh() throws PropertyServerException
    {
        // TODO not implemented yet
    }

}
