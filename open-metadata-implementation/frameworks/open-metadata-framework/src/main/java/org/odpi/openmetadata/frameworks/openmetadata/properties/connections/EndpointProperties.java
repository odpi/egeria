/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.connections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EndpointProperties describes the properties of a server endpoint.  The endpoint is linked
 * to the asset manager's server and describes its network endpoint.  It is also linked to connection objects
 * that are used by clients to connect to the asset manager.  A connection is linked to each asset
 * that is hosted on the asset manager.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointProperties extends SupplementaryProperties
{
    private String name             = null;
    private String description      = null;
    private String address          = null;
    private String protocol         = null;
    private String encryptionMethod = null;


    /**
     * Default constructor
     */
    public EndpointProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ENDPOINT.typeName);
    }


    /**
     * Copy/clone constructor for an Endpoint.
     *
     * @param template template object to copy.
     */
    public EndpointProperties(EndpointProperties template)
    {
        super(template);

        if (template != null)
        {
            name             = template.getName();
            description      = template.getResourceDescription();
            address          = template.getAddress();
            protocol         = template.getProtocol();
            encryptionMethod = template.getEncryptionMethod();
        }
    }


    /**
     * Set up the technical name for the endpoint.
     *
     * @param name String name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Returns the stored technical name property for the endpoint.
     * If no technical name is available then null is returned.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the technical description of the endpoint.
     *
     * @param resourceDescription String
     */
    public void setResourceDescription(String resourceDescription)
    {
        this.description = resourceDescription;
    }


    /**
     * Return the technical description for the endpoint.
     *
     * @return String technicalDescription
     */
    public String getResourceDescription()
    {
        return description;
    }


    /**
     * Set up the network address of the endpoint.
     *
     * @param address String resource name
     */
    public void setAddress(String address)
    {
        this.address = address;
    }


    /**
     * Returns the stored address property for the endpoint.
     * If no network address is available then null is returned.
     *
     * @return address
     */
    public String getAddress()
    {
        return address;
    }


    /**
     * Set up the protocol to use for this Endpoint
     *
     * @param protocol String protocol name
     */
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }


    /**
     * Returns the stored protocol property for the endpoint.
     * If no protocol is available then null is returned.
     *
     * @return protocol
     */
    public String getProtocol()
    {
        return protocol;
    }


    /**
     * Set up the encryption method used on this Endpoint.
     *
     * @param encryptionMethod String name
     */
    public void setEncryptionMethod(String encryptionMethod)
    {
        this.encryptionMethod = encryptionMethod;
    }


    /**
     * Returns the stored encryptionMethod property for the endpoint.  This is an open type allowing the information
     * needed to work with a specific encryption mechanism used by the endpoint to be defined.
     * If no encryptionMethod property is available (typically because this is an unencrypted endpoint)
     * then null is returned.
     *
     * @return encryption method information
     */
    public String getEncryptionMethod()
    {
        return encryptionMethod;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EndpointProperties{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", address='" + address + '\'' +
                       ", protocol='" + protocol + '\'' +
                       ", encryptionMethod='" + encryptionMethod + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        EndpointProperties that = (EndpointProperties) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(address, that.address) &&
                       Objects.equals(protocol, that.protocol) &&
                       Objects.equals(encryptionMethod, that.encryptionMethod);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, description, address, protocol, encryptionMethod);
    }
}
