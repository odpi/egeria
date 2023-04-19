/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 1L;

    private String   displayName = null;
    private PortType portType = null;


    /**
     * Default constructor
     */
    public PortProperties()
    {
        super();
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
            displayName = template.getDisplayName();
            portType = template.getPortType();
        }
    }


    /**
     * Return a human memorable name for the port.
     *
     * @return string  name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up a human memorable name for the port.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
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
                       "displayName='" + displayName + '\'' +
                       ", portType=" + portType +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        PortProperties that = (PortProperties) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       getPortType() == that.getPortType();
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, portType);
    }
}
