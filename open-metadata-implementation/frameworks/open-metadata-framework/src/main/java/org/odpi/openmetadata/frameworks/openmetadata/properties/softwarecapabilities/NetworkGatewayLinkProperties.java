/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NetworkGatewayLinkProperties describes the mapping between networks.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NetworkGatewayLinkProperties extends RelationshipBeanProperties
{
    private String       displayName               = null;
    private String       description               = null;
    private List<String> externalEndpointAddresses = null;
    private List<String> internalEndpointAddresses = null;

    /**
     * Default constructor
     */
    public NetworkGatewayLinkProperties()
    {
        super();
        super.typeName = OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public NetworkGatewayLinkProperties(NetworkGatewayLinkProperties template)
    {
        super (template);

        if (template != null)
        {
            displayName               = template.getDisplayName();
            description               = template.getDescription();
            externalEndpointAddresses = template.getExternalEndpointAddresses();
            internalEndpointAddresses = template.getInternalEndpointAddresses();
        }
    }


    /**
     * Return the display name for this link.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this link.
     *
     * @param displayName string
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of this link.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of this link.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the network addresses used by callers to the network gateway.
     *
     * @return list of addresses
     */
    public List<String> getExternalEndpointAddresses()
    {
        return externalEndpointAddresses;
    }


    /**
     * Set up the network addresses used by callers to the network gateway.
     *
     * @param externalEndpointAddresses list of addresses
     */
    public void setExternalEndpointAddresses(List<String> externalEndpointAddresses)
    {
        this.externalEndpointAddresses = externalEndpointAddresses;
    }


    /**
     * Returns the network addresses that the network gateway maps requests to.
     *
     * @return list of addresses
     */
    public List<String> getInternalEndpointAddresses()
    {
        return internalEndpointAddresses;
    }


    /**
     * Set up the network addresses that the network gateway maps requests to.
     *
     * @param internalEndpointAddresses list of addresses
     */
    public void setInternalEndpointAddresses(List<String> internalEndpointAddresses)
    {
        this.internalEndpointAddresses = internalEndpointAddresses;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NetworkGatewayLinkProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", externalEndpointAddresses=" + externalEndpointAddresses +
                ", internalEndpointAddresses=" + internalEndpointAddresses +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        NetworkGatewayLinkProperties that = (NetworkGatewayLinkProperties) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(externalEndpointAddresses, that.externalEndpointAddresses) &&
                       Objects.equals(internalEndpointAddresses, that.internalEndpointAddresses);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, externalEndpointAddresses, internalEndpointAddresses);
    }
}
