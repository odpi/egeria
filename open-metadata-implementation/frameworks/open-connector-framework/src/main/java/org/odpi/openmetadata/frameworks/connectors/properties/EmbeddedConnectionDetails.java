/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;

import java.util.Map;
import java.util.Objects;

/**
 * EmbeddedConnectionDetails provides a facade for the properties of a Connection that is embedded in a
 * VirtualConnection.
 */
public class EmbeddedConnectionDetails extends AssetPropertyElementBase
{
    protected EmbeddedConnection embeddedConnectionBean;


    /**
     * Bean constructor
     *
     * @param embeddedConnectionBean bean containing all the properties
     */
    public EmbeddedConnectionDetails(EmbeddedConnection embeddedConnectionBean)
    {
        super();

        if (embeddedConnectionBean == null)
        {
            this.embeddedConnectionBean = new EmbeddedConnection();
        }
        else
        {
            this.embeddedConnectionBean = embeddedConnectionBean;
        }
    }


    /**
     * Copy/clone constructor   makes a copy of the supplied object.
     *
     * @param template   template object to copy
     */
    public EmbeddedConnectionDetails(EmbeddedConnectionDetails template)
    {
        super(template);

        if (template == null)
        {
            this.embeddedConnectionBean = new EmbeddedConnection();
        }
        else
        {
            this.embeddedConnectionBean = template.getEmbeddedConnectionBean();
        }
    }


    /**
     * Return the bean with all the properties
     *
     * @return schema link bean
     */
    protected EmbeddedConnection getEmbeddedConnectionBean()
    {
        return embeddedConnectionBean;
    }


    /**
     * Return the position that this connector is in the list of embedded connectors.
     *
     * @return int
     */
    public int getPosition()
    {
        return embeddedConnectionBean.getPosition();
    }


    /**
     * Return the display name of the embedded connection.
     *
     * @return String name
     */
    public String getDisplayName() { return embeddedConnectionBean.getDisplayName(); }


    /**
     * Return the connection properties used to create the connector for this embedded connection.
     *
     * @return String name
     */
    public ConnectionDetails getConnectionProperties()
    {
        Connection embeddedConnection =  embeddedConnectionBean.getEmbeddedConnection();

        if (embeddedConnection == null)
        {
            return null;
        }
        else if (embeddedConnection instanceof VirtualConnection virtualConnection)
        {
            return new VirtualConnectionDetails(virtualConnection);
        }
        else
        {
            return new ConnectionDetails(embeddedConnection);
        }
    }


    /**
     * Return the list of properties associated with this schema link.
     *
     * @return map of properties
     */
    public Map<String, Object> getArguments()
    {
       return embeddedConnectionBean.getArguments();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return embeddedConnectionBean.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof EmbeddedConnectionDetails that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getEmbeddedConnectionBean(), that.getEmbeddedConnectionBean());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return embeddedConnectionBean.hashCode();
    }
}