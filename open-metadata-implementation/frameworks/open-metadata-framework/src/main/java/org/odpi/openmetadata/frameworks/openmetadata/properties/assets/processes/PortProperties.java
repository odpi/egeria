/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PortType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
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
public class PortProperties extends ReferenceableProperties
{
    private


        PortType portType    = null;


    /**
     * Default constructor
     */
    public PortProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.PORT.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public PortProperties(PortProperties template)
    {
        super(template);

        if (template != null)
        {
            portType = template.getPortType();
        }
    }


    /**
     * Return the direction of data flow of the port.
     *
     * @return portType enum
     */
    public PortType getPortType()
    {
        return portType;
    }


    /**
     * Set up the direction of data flow of the port.
     *
     * @param portType portType enum
     */
    public void setPortType(PortType portType)
    {
        this.portType = portType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PortProperties{" +
                "portType=" + portType +
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
        PortProperties that = (PortProperties) objectToCompare;
        return getPortType() == that.getPortType();
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), portType);
    }
}
