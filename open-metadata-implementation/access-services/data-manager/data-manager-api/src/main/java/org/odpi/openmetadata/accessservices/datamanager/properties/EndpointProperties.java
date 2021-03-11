/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EndpointProperties describes the properties of a server endpoint.  The endpoint is linked
 * the the data manager's server and describes its network endpoint.  It is also linked to connection objects
 * that are used by clients to connect to the data manager.  A connection is linked to each asset
 * that is hosted on the data manager.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointProperties extends ReferenceableProperties
{
    private static final long     serialVersionUID = 1L;

    private String displayName      = null;
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
            displayName      = template.getDisplayName();
            description      = template.getDescription();
            address          = template.getAddress();
            protocol         = template.getProtocol();
            encryptionMethod = template.getEncryptionMethod();
        }
    }


    /**
     * Set up the display name for UIs and reports.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored display name property for the endpoint.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up description of the element.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the description for the endpoint.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the network address of the Endpoint.
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
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", protocol='" + protocol + '\'' +
                ", encryptionMethod='" + encryptionMethod + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
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
        EndpointProperties that = (EndpointProperties) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
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
        return Objects.hash(super.hashCode(), displayName, description, address, protocol, encryptionMethod);
    }
}
