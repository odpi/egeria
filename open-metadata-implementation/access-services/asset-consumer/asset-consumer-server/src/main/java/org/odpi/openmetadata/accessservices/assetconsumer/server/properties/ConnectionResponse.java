/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.server.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ConnectionResponse is the response structure used on the Asset Consumer OMAS REST API calls that returns a
 * Connection object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectionResponse extends AssetConsumerOMASAPIResponse
{
    private Connection connection = null;

    /**
     * Default constructor
     */
    public ConnectionResponse()
    {
    }


    /**
     * Return the Connection object.
     *
     * @return connection
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Set up the Connection object.
     *
     * @param connection - connection object
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }


    @Override
    public String toString()
    {
        return "ConnectionResponse{" +
                "connection=" + connection +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
