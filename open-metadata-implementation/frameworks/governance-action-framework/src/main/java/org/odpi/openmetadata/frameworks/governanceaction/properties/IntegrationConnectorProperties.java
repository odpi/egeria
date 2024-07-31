/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.Objects;

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
    private String     name                       = null;
    private String     versionIdentifier          = null;
    private String     description                = null;
    private String     deployedImplementationType = null;
    private boolean    usesBlockingCalls          = false;
    private Connection connection                 = null;


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
            name                       = template.getName();
            versionIdentifier          = template.getVersionIdentifier();
            description                = template.getDescription();
            deployedImplementationType = template.getDeployedImplementationType();
            usesBlockingCalls          = template.getUsesBlockingCalls();
            connection                 = template.getConnection();
        }
    }


    /**
     * Return the name of the resource that this asset represents.
     *
     * @return string resource name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the resource that this asset represents.
     *
     * @param name string resource name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Set up the version identifier of the resource.
     *
     * @return string version name
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the version identifier of the resource.
     *
     * @param versionIdentifier string version name
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
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
     * Retrieve the name of the technology used for this data asset.
     *
     * @return string name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the name of the technology used for this data asset.
     *
     * @param deployedImplementationType string name
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
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
                "name='" + name + '\'' +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", description='" + description + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
                ", usesBlockingCalls=" + usesBlockingCalls +
                ", connection=" + connection +
                "} " + super.toString();
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
        return Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(versionIdentifier, that.versionIdentifier) &&
                       Objects.equals(deployedImplementationType, that.deployedImplementationType) &&
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
        return Objects.hash(super.hashCode(), name, description, versionIdentifier, usesBlockingCalls, connection);
    }
}
