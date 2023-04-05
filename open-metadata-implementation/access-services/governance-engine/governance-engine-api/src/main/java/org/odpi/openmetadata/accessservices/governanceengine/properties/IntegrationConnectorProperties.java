/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * IntegrationConnectorProperties contains the definition of an integration connector.
 * This definition can be associated with multiple integration groups.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class IntegrationConnectorProperties extends ReferenceableProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String     displayName       = null;
    private String     versionIdentifier = null;
    private String     description       = null;
    private boolean    usesBlockingCalls = false;
    private Connection connection        = null;


    /**
     * Default constructor
     */
    public IntegrationConnectorProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationConnectorProperties(IntegrationConnectorProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName                  = template.getDisplayName();
            versionIdentifier            = template.getVersionIdentifier();
            description                  = template.getDescription();
            usesBlockingCalls            = template.getUsesBlockingCalls();
            connection                   = template.getConnection();
        }
    }


    /**
     * Returns the stored display name property for the asset.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the asset.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property associated with the asset.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return whether the connector issues blocking calls and needs its own thread to run in.
     *
     * @return flag
     */
    public boolean getUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up whether the connector issues blocking calls and needs its own thread to run in.
     *
     * @param usesBlockingCalls flag
     */
    public void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
    }


    /**
     * Set up the version identifier of the integration connector implementation.
     *
     * @return string version name
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the version identifier of the implementation.
     *
     * @param versionIdentifier string version name
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
    }


    /**
     * Return the connection used to create an instance of this integration connector.
     *
     * @return Connection object
     */
    public Connection getConnection()
    {
        return connection;
    }


    /**
     * Set up the connection used to create an instance of this integration connector.
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
        return "IntegrationConnectorProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", usesBlockingCalls=" + usesBlockingCalls +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", connection=" + connection +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
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
        IntegrationConnectorProperties that = (IntegrationConnectorProperties) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(versionIdentifier, that.versionIdentifier) &&
                       (usesBlockingCalls == that.usesBlockingCalls) &&
                       Objects.equals(connection, that.connection);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, versionIdentifier, usesBlockingCalls, connection);
    }
}
