/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private List<EmbeddedConnection> embeddedConnections = null;


    /**
     * Return the standard type for a connection type.
     *
     * @return ElementType object
     */
    public static ElementType getVirtualConnectionType()
    {
        final String        elementTypeId                   = "82f9c664-e59d-484c-a8f3-17088c23a2f3";
        final String        elementTypeName                 = "VirtualConnection";
        final long          elementTypeVersion              = 1;
        final String        elementTypeDescription          = "A connector for a virtual resource that needs to retrieve data from multiple places.";

        ElementType elementType = new ElementType();

        elementType.setTypeId(elementTypeId);
        elementType.setTypeName(elementTypeName);
        elementType.setTypeVersion(elementTypeVersion);
        elementType.setTypeDescription(elementTypeDescription);

        return elementType;
    }


    /**
     * Default constructor
     */
    public VirtualConnection()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateVirtualConnection element to copy
     */
    public VirtualConnection(VirtualConnection templateVirtualConnection)
    {
        super(templateVirtualConnection);

        if (templateVirtualConnection != null)
        {
            embeddedConnections = templateVirtualConnection.getEmbeddedConnections();
        }
    }


    /**
     * Return the list of embedded connections for this virtual connection.
     *
     * @return list of EmbeddedConnection objects
     */
    public List<EmbeddedConnection> getEmbeddedConnections()
    {
        if (embeddedConnections == null)
        {
            return null;
        }
        else if (embeddedConnections.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(embeddedConnections);
        }
    }


    /**
     * Set up the list of embedded connections for this virtual connection.
     *
     * @param embeddedConnections list of EmbeddedConnection objects
     */
    public void setEmbeddedConnections(List<EmbeddedConnection> embeddedConnections)
    {
        this.embeddedConnections = embeddedConnections;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "VirtualConnection{" +
                       "displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", connectorType=" + getConnectorType() +
                       ", userId='" + getUserId() + '\'' +
                       ", encryptedPassword='" + getEncryptedPassword() + '\'' +
                       ", clearPassword='" + getClearPassword() + '\'' +
                       ", endpoint=" + getEndpoint() +
                       ", configurationProperties=" + getConfigurationProperties() +
                       ", securedProperties=" + getSecuredProperties() +
                       ", assetSummary='" + getAssetSummary() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", type=" + getType() +
                       ", GUID='" + getGUID() + '\'' +
                       ", URL='" + getURL() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", headerVersion=" + getHeaderVersion() +
                       '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        VirtualConnection that = (VirtualConnection) objectToCompare;
        return Objects.equals(getEmbeddedConnections(), that.getEmbeddedConnections());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getEmbeddedConnections());
    }
}