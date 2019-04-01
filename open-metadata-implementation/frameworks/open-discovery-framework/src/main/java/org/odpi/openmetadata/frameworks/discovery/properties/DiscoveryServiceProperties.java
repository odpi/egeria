/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DiscoveryServiceProperties contains the definition of a discovery service.
 * This definition can be associated with multiple discovery engines.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = RegisteredDiscoveryService.class, name = "RegisteredDiscoveryService")
        })

public class DiscoveryServiceProperties extends Asset
{
    private Connection connection = null;

    /**
     * Default constructor
     */
    public DiscoveryServiceProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public DiscoveryServiceProperties(DiscoveryServiceProperties  template)
    {
        super(template);

        if (template != null)
        {
            connection = template.getConnection();
        }
    }


    /**
     * Return the connection used to create a instance of this discovery service.
     *
     * @return Connection object
     */
    public Connection getConnection()
    {
        return connection;
    }


    /**
     * Set up the connection used to create a instance of this discovery service.
     *
     * @param connection connection object
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DiscoveryServiceProperties{" +
                "connection=" + connection +
                ", displayName='" + displayName + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", zoneMembership=" + zoneMembership +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", meanings=" + meanings +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                ", extendedProperties=" + extendedProperties +
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
        DiscoveryServiceProperties that = (DiscoveryServiceProperties) objectToCompare;
        return Objects.equals(getConnection(), that.getConnection());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getConnection());
    }
}
