/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A virtual connection is for an asset that provides data by delegating requests to one or more other connections.
 * it maintains a list of the connections that are used by its asset.  These are referred to as embedded connections.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VirtualConnection extends Connection
{
    /*
     * Attributes of a virtual connection
     */
    protected List<EmbeddedConnection>       embeddedConnections = null;


    /**
     * Default Constructor
     */
    public VirtualConnection()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateVirtualConnection - element to copy
     */
    public VirtualConnection(VirtualConnection templateVirtualConnection)
    {
        /*
         * Save the parent asset description.
         */
        super(templateVirtualConnection);

        /*
         * Extract additional information from the template if available
         */
        if (templateVirtualConnection != null)
        {
            List<EmbeddedConnection> templateEmbeddedConnections = templateVirtualConnection.getEmbeddedConnections();

            if (templateEmbeddedConnections != null)
            {
                /*
                 * Ensure comment replies has this object's parent asset, not the template's.
                 */
                embeddedConnections = new ArrayList<>(templateEmbeddedConnections);
            }
        }
    }


    /**
     * Return the enumeration of embedded connections for this virtual connection.
     *
     * @return EmbeddedConnections
     */
    public List<EmbeddedConnection> getEmbeddedConnections()
    {
        if (embeddedConnections == null)
        {
            return embeddedConnections;
        }
        else
        {
            return new ArrayList<>(embeddedConnections);
        }
    }


    /**
     * Set up the embedded connections for this virtual connection.
     *
     * @param embeddedConnections - list of Connections
     */
    public void setEmbeddedConnections(List<EmbeddedConnection> embeddedConnections)
    {
        this.embeddedConnections = embeddedConnections;
    }
}