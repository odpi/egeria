/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NetworkGatewayLinkProperties describes the mapping between networks.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NetworkGatewayLinkProperties extends LabeledRelationshipProperties
{
    private String externalEndpointAddress = null;
    private String internalEndpointAddress = null;

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
            externalEndpointAddress = template.getExternalEndpointAddress();
            internalEndpointAddress = template.getInternalEndpointAddress();
        }
    }


    /**
     * Return the external network address.
     *
     * @return string
     */
    public String getExternalEndpointAddress()
    {
        return externalEndpointAddress;
    }


    /**
     * Set up the external network address.
     *
     * @param externalEndpointAddress string
     */
    public void setExternalEndpointAddress(String externalEndpointAddress)
    {
        this.externalEndpointAddress = externalEndpointAddress;
    }


    /**
     * Returns the internal network address.
     *
     * @return string
     */
    public String getInternalEndpointAddress()
    {
        return internalEndpointAddress;
    }


    /**
     * Set up the internal network address.
     *
     * @param internalEndpointAddress string
     */
    public void setInternalEndpointAddress(String internalEndpointAddress)
    {
        this.internalEndpointAddress = internalEndpointAddress;
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
                "externalEndpointAddress='" + externalEndpointAddress + '\'' +
                ", internalEndpointAddress='" + internalEndpointAddress + '\'' +
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
        return Objects.equals(externalEndpointAddress, that.externalEndpointAddress) &&
                       Objects.equals(internalEndpointAddress, that.internalEndpointAddress);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), internalEndpointAddress, externalEndpointAddress);
    }
}
