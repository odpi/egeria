/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Endpoint describes the network information necessary for a connector to connect to the server
 * where the Asset is accessible from.  The properties for an endpoint are defined in model 0026.
 * They include:
 * <ul>
 *     <li>
 *         type   definition of the specific metadata type for the endpoint.
 *     </li>
 *     <li>
 *         guid   Globally unique identifier for the endpoint.
 *     </li>
 *     <li>
 *         url   External link address for the endpoint properties in the metadata repository.
 *         This URL can be stored as a property in another entity to create an explicit link to this endpoint.
 *     </li>
 *     <li>
 *         qualifiedName   The official (unique) name for the endpoint. This is often defined by the IT systems management
 *         organization and should be used (when available) on audit logs and error messages.
 *     </li>
 *     <li>
 *         displayName - A consumable name for the endpoint.   Often a shortened form of the qualifiedName for use
 *         on user interfaces and messages.  The displayName should be only be used for audit logs and error messages
 *         if the qualifiedName is not set.
 *     </li>
 *     <li>
 *         description - A description for the endpoint.
 *     </li>
 *     <li>
 *         address - The location of the asset.  For network connected resources, this is typically the
 *         URL and port number (if needed) for the server where the asset is located
 *         (or at least accessible by the connector).  For file-based resources, this is typically the name of the file.
 *     </li>
 *     <li>
 *         protocol - The communication protocol that the connection should use to connect to the server.
 *     </li>
 *     <li>
 *         encryptionMethod - Describes the encryption method to use (if any).  This is an open value allowing
 *         information needed by the connector user to retrieve all the information they need to work with
 *         the endpoint.
 *     </li>
 *     <li>
 *         additionalProperties - Any additional properties that the connector need to know in order to
 *         access the Asset.
 *     </li>
 * </ul>
 *
 * The Endpoint class is simply used to cache the properties for an endpoint.
 * It is used by other classes to exchange this information between a metadata repository and a consumer.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Endpoint extends Referenceable
{
    /*
     * Properties of an Endpoint
     */
    protected   String                 displayName      = null;
    protected   String                 description      = null;
    protected   String                 address          = null;
    protected   String                 protocol         = null;
    protected   String                 encryptionMethod = null;

    /**
     * Return the standard type for an endpoint.
     *
     * @return ElementType object
     */
    public static ElementType getEndpointType()
    {
        final String        elementTypeId                   = "dbc20663-d705-4ff0-8424-80c262c6b8e7";
        final String        elementTypeName                 = "Endpoint";
        final long          elementTypeVersion              = 1;
        final String        elementTypeDescription          = "Description of the network address and related information needed to call a software service.";

        ElementType elementType = new ElementType();

        elementType.setTypeId(elementTypeId);
        elementType.setTypeName(elementTypeName);
        elementType.setTypeVersion(elementTypeVersion);
        elementType.setTypeDescription(elementTypeDescription);

        return elementType;
    }


    /**
     * Default constructor
     */
    public Endpoint()
    {
        super();
    }


    /**
     * Copy/clone constructor for an Endpoint.
     *
     * @param templateEndpoint template object to copy.
     */
    public Endpoint(Endpoint templateEndpoint)
    {
        super(templateEndpoint);

        if (templateEndpoint != null)
        {
            displayName      = templateEndpoint.getDisplayName();
            description      = templateEndpoint.getDescription();
            address          = templateEndpoint.getAddress();
            protocol         = templateEndpoint.getProtocol();
            encryptionMethod = templateEndpoint.getEncryptionMethod();
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
     * @return displayName
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
        return "Endpoint{" +
                       "extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", address='" + address + '\'' +
                       ", protocol='" + protocol + '\'' +
                       ", encryptionMethod='" + encryptionMethod + '\'' +
                       ", headerVersion=" + getHeaderVersion() +
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
        Endpoint endpoint = (Endpoint) objectToCompare;
        return Objects.equals(getDisplayName(), endpoint.getDisplayName()) &&
                       Objects.equals(getDescription(), endpoint.getDescription()) &&
                       Objects.equals(getAddress(), endpoint.getAddress()) &&
                       Objects.equals(getProtocol(), endpoint.getProtocol()) &&
                       Objects.equals(getEncryptionMethod(), endpoint.getEncryptionMethod());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getAddress(), getProtocol(), getEncryptionMethod());
    }
}
