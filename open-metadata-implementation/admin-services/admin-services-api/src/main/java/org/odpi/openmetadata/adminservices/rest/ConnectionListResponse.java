/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ConnectionListResponse is the response structure used on the OMAS REST API calls that return a
 * list of connections as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectionListResponse extends FFDCResponseBase
{
    private List<Connection> connections = null;


    /**
     * Default constructor
     */
    public ConnectionListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectionListResponse(ConnectionListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.connections = template.getConnections();
        }
    }


    /**
     * Return the connection list result.
     *
     * @return list of objects
     */
    public List<Connection> getConnections()
    {
        if (connections == null)
        {
            return null;
        }
        else if (connections.isEmpty())
        {
            return null;
        }
        else
        {
            return connections;
        }
    }


    /**
     * Set up the connection list result.
     *
     * @param connections list of objects
     */
    public void setConnections(List<Connection> connections)
    {
        this.connections = connections;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the connections and values
     */
    @Override
    public String toString()
    {
        return "ConnectionListResponse{" +
                "connections=" + connections +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof ConnectionListResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(connections, that.connections);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(connections);
    }
}
