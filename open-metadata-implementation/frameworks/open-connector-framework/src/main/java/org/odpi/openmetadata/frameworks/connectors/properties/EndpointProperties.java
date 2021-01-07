/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;

import java.util.Objects;

/**
 * The Endpoint describes the network information necessary for a connector to connect to the server
 * where the Asset is accessible from.  The properties for an endpoint are defined in model 0040.
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
 *         information needed by the connector user to retrieve all of the information they need to work with
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
public class EndpointProperties extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected Endpoint  endpointBean;


    /**
     * Bean constructor
     *
     * @param endpointBean bean containing the properties
     */
    public EndpointProperties(Endpoint  endpointBean)
    {
        super(endpointBean);

        if (endpointBean == null)
        {
            this.endpointBean = new Endpoint();
        }
        else
        {
            this.endpointBean = endpointBean;
        }
    }



    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset description of the asset that this endpoint is attached to.
     * @param endpointBean bean containing the properties
     */
    public EndpointProperties(AssetDescriptor parentAsset,
                              Endpoint        endpointBean)
    {
        super(parentAsset, endpointBean);

        if (endpointBean == null)
        {
            this.endpointBean = new Endpoint();
        }
        else
        {
            this.endpointBean = endpointBean;
        }
    }


    /**
     * Copy/clone constructor for an Endpoint that is connected to an Asset (either directly or indirectly).
     *
     * @param templateEndpoint template object to copy.
     */
    public EndpointProperties(EndpointProperties templateEndpoint)
    {
        this(null, templateEndpoint);
    }


    /**
     * Copy/clone constructor for an Endpoint that is connected to an Asset (either directly or indirectly).
     *
     * @param parentAsset description of the asset that this endpoint is attached to.
     * @param templateEndpoint template object to copy.
     */
    public EndpointProperties(AssetDescriptor    parentAsset,
                              EndpointProperties templateEndpoint)
    {
        super(parentAsset, templateEndpoint);

        if (templateEndpoint == null)
        {
            this.endpointBean = new Endpoint();
        }
        else
        {
            this.endpointBean = templateEndpoint.getEndpointBean();
        }
    }


    /**
     * Return the Endpoint bean that contains all of the properties.
     *
     * @return Endpoint bean
     */
    protected Endpoint getEndpointBean()
    {
        return endpointBean;
    }


    /**
     * Returns the stored display name property for the endpoint.
     * If no display name is available then null is returned.
     *
     * @return displayName
     */
    public String getDisplayName()
    {
        return endpointBean.getDisplayName();
    }


    /**
     * Return the description for the endpoint.
     *
     * @return String description
     */
    public String getDescription()
    {
        return endpointBean.getDescription();
    }


    /**
     * Returns the stored address property for the endpoint.
     * If no network address is available then null is returned.
     *
     * @return address
     */
    public String getAddress()
    {
        return endpointBean.getAddress();
    }


    /**
     * Returns the stored protocol property for the endpoint.
     * If no protocol is available then null is returned.
     *
     * @return protocol
     */
    public String getProtocol()
    {
        return endpointBean.getProtocol();
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
        return endpointBean.getEncryptionMethod();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return endpointBean.toString();
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
        return Objects.equals(endpointBean, that.endpointBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), endpointBean);
    }
}