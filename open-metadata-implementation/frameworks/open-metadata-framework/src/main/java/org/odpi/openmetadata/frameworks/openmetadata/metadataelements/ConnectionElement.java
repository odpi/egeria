/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConnectionElement contains the properties and header for a connection retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectionElement extends OpenMetadataRootElement
{
    private RelatedMetadataElementSummary       connectorType       = null;
    private RelatedMetadataElementSummary       endpoint            = null;
    private List<RelatedMetadataElementSummary> embeddedConnections = null;
    private List<RelatedMetadataElementSummary> parentConnections   = null;
    private List<RelatedMetadataElementSummary> assets              = null;


    /**
     * Default constructor
     */
    public ConnectionElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectionElement(ConnectionElement template)
    {
        super(template);

        if (template != null)
        {
            connectorType = template.getConnectorType();
            endpoint = template.getEndpoint();
            assets = template.getAssets();
            parentConnections = template.getParentConnections();
            embeddedConnections = template.getEmbeddedConnections();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectionElement(OpenMetadataRootElement template)
    {
        super(template);
    }


    /**
     * Set up the connector type properties for this Connection.
     *
     * @param connectorType ConnectorType properties object
     */
    public void setConnectorType(RelatedMetadataElementSummary connectorType)
    {
        this.connectorType = connectorType;
    }


    /**
     * Returns a copy of the properties for this connection's connector type.
     * A null means there is no connection type.
     *
     * @return connector type for the connection
     */
    public RelatedMetadataElementSummary getConnectorType()
    {
        return connectorType;
    }


    /**
     * Set up the endpoint properties for this Connection.
     *
     * @param endpoint Endpoint properties object
     */
    public void setEndpoint(RelatedMetadataElementSummary endpoint)
    {
        this.endpoint = endpoint;
    }


    /**
     * Returns a copy of the properties for this connection's endpoint.
     * Null means no endpoint information available.
     *
     * @return endpoint for the connection
     */
    public RelatedMetadataElementSummary getEndpoint()
    {
        return endpoint;
    }


    /**
     * Return the list of embedded connections for this virtual connection.
     *
     * @return list of EmbeddedConnection objects
     */
    public List<RelatedMetadataElementSummary> getEmbeddedConnections()
    {
        return embeddedConnections;
    }


    /**
     * Set up the list of embedded connections for this virtual connection.
     *
     * @param embeddedConnections list of EmbeddedConnection objects
     */
    public void setEmbeddedConnections(List<RelatedMetadataElementSummary> embeddedConnections)
    {
        this.embeddedConnections = embeddedConnections;
    }


    /**
     * Return the list of connections that this connection is embedded in.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getParentConnections()
    {
        return parentConnections;
    }


    /**
     * Set up the list of connections that this connection is embedded in.
     *
     * @param parentConnections list
     */
    public void setParentConnections(List<RelatedMetadataElementSummary> parentConnections)
    {
        this.parentConnections = parentConnections;
    }


    /**
     * Return the assets reached through this connection.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getAssets()
    {
        return assets;
    }


    /**
     * Set up the assets reached through this connection.
     *
     * @param assets list
     */
    public void setAssets(List<RelatedMetadataElementSummary> assets)
    {
        this.assets = assets;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectionElement{" +
                "connectorType=" + connectorType +
                ", endpoint=" + endpoint +
                ", embeddedConnections=" + embeddedConnections +
                ", parentConnections=" + parentConnections +
                ", assets=" + assets +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ConnectionElement that = (ConnectionElement) objectToCompare;
        return Objects.equals(connectorType, that.connectorType) &&
                Objects.equals(endpoint, that.endpoint) &&
                Objects.equals(assets, that.assets) &&
                Objects.equals(parentConnections, that.parentConnections) &&
                Objects.equals(embeddedConnections, that.embeddedConnections);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connectorType, endpoint, parentConnections, assets, embeddedConnections);
    }
}
