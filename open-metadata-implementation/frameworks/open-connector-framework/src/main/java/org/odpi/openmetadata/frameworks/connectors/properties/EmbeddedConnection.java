/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

/**
 * The EmbeddedConnection is used within a VirtualConnection.  It contains a connection and additional properties
 * the VirtualConnection uses when working with the EmbeddedConnection.
 */
public class EmbeddedConnection extends AssetPropertyBase
{
    /*
     * Attributes of an embedded connection
     */
    private AdditionalProperties embeddedConnectionProperties = null;
    private Connection           embeddedConnection           = null;


    /**
     * Typical Constructor
     *
     * @param parentAsset descriptor for parent asset
     * @param embeddedConnectionProperties Additional properties
     * @param embeddedConnection Connection
     */
    public EmbeddedConnection(AssetDescriptor      parentAsset,
                              AdditionalProperties embeddedConnectionProperties,
                              Connection embeddedConnection)
    {
        super(parentAsset);

        this.embeddedConnectionProperties = embeddedConnectionProperties;
        this.embeddedConnection = embeddedConnection;
    }

    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateEmbeddedConnection element to copy
     */
    public EmbeddedConnection(AssetDescriptor parentAsset, EmbeddedConnection templateEmbeddedConnection)
    {
        /*
         * Save the parent asset description.
         */
        super(parentAsset, templateEmbeddedConnection);

        if (templateEmbeddedConnection != null)
        {
            AdditionalProperties templateConnectionProperties = templateEmbeddedConnection.getEmbeddedConnectionProperties();
            Connection           templateConnection           = templateEmbeddedConnection.getEmbeddedConnection();

            if (templateConnectionProperties != null)
            {
                embeddedConnectionProperties = new AdditionalProperties(parentAsset, templateConnectionProperties);
            }
            if (templateConnection != null)
            {
                embeddedConnection = new Connection(parentAsset, templateConnection);
            }
        }
    }


    /**
     * Return the properties for the embedded connection.
     *
     * @return AdditionalProperties
     */
    public AdditionalProperties getEmbeddedConnectionProperties()
    {
        if (embeddedConnectionProperties == null)
        {
            return embeddedConnectionProperties;
        }
        else
        {
            return new AdditionalProperties(this.getParentAsset(), embeddedConnectionProperties);
        }
    }


    /**
     * Return the embedded connection.
     *
     * @return Connection object.
     */
    public Connection getEmbeddedConnection()
    {
        if (embeddedConnection == null)
        {
            return embeddedConnection;
        }
        else
        {
            return new Connection(this.getParentAsset(), embeddedConnection);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EmbeddedConnection{" +
                "embeddedConnectionProperties=" + embeddedConnectionProperties +
                ", embeddedConnection=" + embeddedConnection +
                '}';
    }
}