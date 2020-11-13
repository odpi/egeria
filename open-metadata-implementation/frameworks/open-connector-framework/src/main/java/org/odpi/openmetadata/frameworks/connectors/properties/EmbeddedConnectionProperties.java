/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;

import java.util.Map;
import java.util.Objects;

/**
 * EmbeddedConnectionProperties provides a facade for the properties of a Connection that is embedded in a
 * VirtualConnection.
 */
public class EmbeddedConnectionProperties extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    protected EmbeddedConnection embeddedConnectionBean;


    /**
     * Bean constructor
     *
     * @param embeddedConnectionBean bean containing all of the properties
     */
    public EmbeddedConnectionProperties(EmbeddedConnection embeddedConnectionBean)
    {
        super(null);

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
     * Bean constructor with parent asset
     *
     * @param parentAsset   descriptor of parent asset
     * @param embeddedConnectionBean bean containing all of the properties
     */
    public EmbeddedConnectionProperties(AssetDescriptor    parentAsset,
                                        EmbeddedConnection embeddedConnectionBean)
    {
        super(parentAsset);

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
     * @param parentAsset   descriptor of parent asset
     * @param template   template object to copy
     */
    public EmbeddedConnectionProperties(AssetDescriptor parentAsset, EmbeddedConnectionProperties template)
    {
        super(parentAsset, template);

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
     * Return the bean with all of the properties
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
    public ConnectionProperties getConnectionProperties()
    {
        Connection embeddedConnection =  embeddedConnectionBean.getEmbeddedConnection();

        if (embeddedConnection == null)
        {
            return null;
        }
        else
        {
            return new ConnectionProperties(super.parentAsset, embeddedConnection);
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
        if (!(objectToCompare instanceof EmbeddedConnectionProperties))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EmbeddedConnectionProperties that = (EmbeddedConnectionProperties) objectToCompare;
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