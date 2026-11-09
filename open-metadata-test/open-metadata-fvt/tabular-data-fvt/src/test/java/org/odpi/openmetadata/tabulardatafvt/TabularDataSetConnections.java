/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tabulardatafvt;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds the connection a tabular data set connector needs: where the metadata is, which server holds it,
 * and who to ask as.
 * <br><br>
 * These connectors are configured rather than injected - each one builds its own open metadata client in
 * {@code start()} from its connection - so this is the whole of what they need.
 */
final class TabularDataSetConnections
{
    private TabularDataSetConnections()
    {
    }


    /**
     * Return the connection for a data set.
     *
     * @param dataSetName names the connector instance, which is what appears in its audit log messages
     * @return connection
     */
    static Connection connectionFor(String dataSetName)
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setNetworkAddress(OMAGPlatformExtension.getPlatformURLRoot());

        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put("serverName", OMAGPlatformExtension.SERVER_NAME);
        configurationProperties.put("maxPageSize", OMAGPlatformExtension.MAX_PAGE_SIZE);

        Connection connection = new Connection();

        connection.setQualifiedName("TabularDataFVT::" + dataSetName);
        connection.setDisplayName(dataSetName);
        connection.setEndpoint(endpoint);
        connection.setConfigurationProperties(configurationProperties);
        connection.setUserId(OMAGPlatformExtension.USER_ID);

        return connection;
    }
}
