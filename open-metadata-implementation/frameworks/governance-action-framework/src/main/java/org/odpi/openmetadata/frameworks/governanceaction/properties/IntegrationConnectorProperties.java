/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.DeployedConnectorProperties;

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

public class IntegrationConnectorProperties extends DeployedConnectorProperties
{
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
            usesBlockingCalls = template.getUsesBlockingCalls();
            connection        = template.getConnection();
        }
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
                "usesBlockingCalls=" + usesBlockingCalls +
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
        return (usesBlockingCalls == that.usesBlockingCalls) &&
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
        return Objects.hash(super.hashCode(), usesBlockingCalls, connection);
    }
}
