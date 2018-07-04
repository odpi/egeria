/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.adapters.connectors.database;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;

import java.util.Map;

/**
 * The OCF Database Connection provides access to the secure properties located in Connection.
 */
public class OCFDatabaseConnection extends Connection {
    /**
     * Construct an OCFDatabaseConnection from a Connection.
     *
     * @param connection - template connection to copy.
     */
    public OCFDatabaseConnection(Connection connection)
    {
        super(connection);
    }


    /**
     * Typical Constructor - for constructing a new, independent populated OCFDatabaseConnection.
     *
     * @param type - details of the metadata type for this properties object
     * @param guid - String - unique id
     * @param url - String - URL
     * @param qualifiedName - unique name
     * @param additionalProperties - additional properties for the referenceable object.
     * @param displayName - consumable name
     * @param description - stored description property for the connection.
     * @param connectorType - connector type to copy
     * @param endpoint - endpoint properties
     * @param securedProperties - typically user credentials for the connection
     */
    public OCFDatabaseConnection(ElementType type,
                                 String                 guid,
                                 String                 url,
                                 String                 qualifiedName,
                                 Map<String,Object>     additionalProperties,
                                 String                 displayName,
                                 String                 description,
                                 ConnectorType          connectorType,
                                 Endpoint               endpoint,
                                 Map<String, Object>   securedProperties)
    {
        super();
        this.guid = guid;
        this.url = url;
        this.qualifiedName = qualifiedName;
        this.additionalProperties = additionalProperties;
        this.displayName = displayName;
        this.type = type;
        this.description = displayName;
        this.description = description;
        this.connectorType = connectorType;
        this.endpoint = endpoint;
        this.securedProperties = securedProperties;

    }


    /**
     * Copy/clone constructor
     *
     * @param connectionTemplate - template to clone.
     */
    public OCFDatabaseConnection(OCFDatabaseConnection connectionTemplate)
    {
        super(connectionTemplate);
    }


}
