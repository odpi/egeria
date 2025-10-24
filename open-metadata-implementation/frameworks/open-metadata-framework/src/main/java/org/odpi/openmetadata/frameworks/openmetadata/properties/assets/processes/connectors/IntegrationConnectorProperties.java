/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PortProperties is a class for representing a generic port for a process.  The typeName is set to indicate it is either a
 * PortAlias (part of a choreographing process) or PortImplementation (part of an implemented process).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationConnectorProperties extends DeployedConnectorProperties
{
    private boolean usesBlockingCalls = false;


    /**
     * Default constructor
     */
    public IntegrationConnectorProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.INTEGRATION_CONNECTOR.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public IntegrationConnectorProperties(IntegrationConnectorProperties template)
    {
        super(template);

        if (template != null)
        {
            usesBlockingCalls = template.getUsesBlockingCalls();
        }
    }


    /**
     * Return whether the connector uses blocking calls and needs its own private thread.
     *
     * @return boolean
     */
    public boolean getUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up whether the connector uses blocking calls and needs its own private thread.
     *
     * @param usesBlockingCalls boolean
     */
    public void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
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
        return getUsesBlockingCalls() == that.getUsesBlockingCalls();
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), usesBlockingCalls);
    }
}
