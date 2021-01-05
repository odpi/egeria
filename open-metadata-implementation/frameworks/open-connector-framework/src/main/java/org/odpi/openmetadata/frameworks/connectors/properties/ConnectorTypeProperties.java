/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.List;
import java.util.Objects;

/**
 * The ConnectorType describe the implementation details of a particular type of OCF connector.
 * The properties for a connector type are defined in model 0201.
 * They include:
 *
 * <ul>
 *     <li>
 *         guid - Globally unique identifier for the connector type.
 *     </li>
 *     <li>
 *         url - External link address for the connector type properties in the metadata repository.  This URL can be
 *         stored as a property in another entity to create an explicit link to this connector type.
 *     </li>
 *     <li>
 *         qualifiedName - The official (unique) name for the connector type. This is often defined by the IT
 *         systems management organization and should be used (when available) on audit logs and error messages.
 *     </li>
 *     <li>
 *         displayName - A consumable name for the connector type.   Often a shortened form of the qualifiedName for use
 *         on user interfaces and messages.  The displayName should be only be used for audit logs and error messages
 *         if the qualifiedName is not set.
 *     </li>
 *     <li>
 *         description - A full description of the connector type covering details of the assets it connects to
 *         along with usage and versioning information.
 *     </li>
 *     <li>
 *         connectorProviderClassName - The connector provider is the factory for a particular type of connector.
 *         This property defines the class name for the connector provider that the Connector Broker should use to request
 *         new connector instances.
 *     </li>
 *     <li>
 *         additionalProperties - Any additional properties that the connector provider needs to know in order to
 *         create connector instances.
 *     </li>
 *     <li>
 *         recognizedAdditionalProperties - List of property names recognised by this connector.
 *     </li>
 *     <li>
 *         recognizedConfigurationProperties - List of property names recognised by this connector.
 *     </li>
 *     <li>
 *         recognizedSecuredProperties - List of property names recognised by this connector.
 *     </li>
 * </ul>
 *
 * The connectorTypeProperties class is simply used to cache the properties for an connector type.
 * It is used by other classes to exchange this information between a metadata repository and a consumer.
 */
public class ConnectorTypeProperties extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected   ConnectorType  connectorTypeBean;


    /**
     * Bean constructor
     *
     * @param connectorTypeBean bean containing the properties
     */
    public ConnectorTypeProperties(ConnectorType   connectorTypeBean)
    {
        super(connectorTypeBean);

        if (connectorTypeBean == null)
        {
            this.connectorTypeBean = new ConnectorType();
        }
        else
        {
            this.connectorTypeBean = connectorTypeBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset description of the asset that this connector type is attached to.
     * @param connectorTypeBean bean containing the properties
     */
    public ConnectorTypeProperties(AssetDescriptor parentAsset,
                                   ConnectorType   connectorTypeBean)
    {
        super(parentAsset, connectorTypeBean);

        if (connectorTypeBean == null)
        {
            this.connectorTypeBean = new ConnectorType();
        }
        else
        {
            this.connectorTypeBean = connectorTypeBean;
        }
    }


    /**
     * Copy/clone constructor for a connectorType that is not connected to an asset (either directly or indirectly).
     *
     * @param templateConnectorType template object to copy.
     */
    public ConnectorTypeProperties(ConnectorTypeProperties templateConnectorType)
    {
        this(null, templateConnectorType);
    }


    /**
     * Copy/clone constructor for a connectorType that is connected to an asset (either directly or indirectly).
     *
     * @param parentAsset description of the asset that this connector type is attached to.
     * @param templateConnectorType template object to copy.
     */
    public ConnectorTypeProperties(AssetDescriptor parentAsset, ConnectorTypeProperties templateConnectorType)
    {
        super(parentAsset, templateConnectorType);

        if (templateConnectorType == null)
        {
            this.connectorTypeBean = new ConnectorType();
        }
        else
        {
            this.connectorTypeBean = templateConnectorType.getConnectorTypeBean();
        }
    }


    /**
     * Return the bean - used in cloning
     *
     * @return ConnectorType bean
     */
    protected  ConnectorType  getConnectorTypeBean()
    {
        return connectorTypeBean;
    }


    /**
     * Returns the stored display name property for the connector type.
     * If no display name is available then null is returned.
     *
     * @return displayName
     */
    public String getDisplayName()
    {
        return connectorTypeBean.getDisplayName();
    }


    /**
     * Returns the stored description property for the connector type.
     * If no description is available then null is returned.
     *
     * @return description
     */
    public String getDescription()
    {
        return connectorTypeBean.getDescription();
    }


    /**
     * Returns the stored connectorProviderClassName property for the connector type.
     * If no connectorProviderClassName is available then null is returned.
     *
     * @return connectorProviderClassName class name (including package name)
     */
    public String getConnectorProviderClassName()
    {
        return connectorTypeBean.getConnectorProviderClassName();
    }


    /**
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's additionalProperties.
     *
     * @return list of property names
     */
    public List<String> getRecognizedAdditionalProperties()
    {
        return connectorTypeBean.getRecognizedAdditionalProperties();
    }


    /**
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's configurationProperties.
     *
     * @return list of property names
     */
    public List<String> getRecognizedConfigurationProperties()
    {
        return connectorTypeBean.getRecognizedConfigurationProperties();
    }


    /**
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's securedProperties.
     *
     * @return list of property names
     */
    public List<String> getRecognizedSecuredProperties()
    {
        return connectorTypeBean.getRecognizedSecuredProperties();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return connectorTypeBean.toString();
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
        ConnectorTypeProperties that = (ConnectorTypeProperties) objectToCompare;
        return Objects.equals(connectorTypeBean, that.connectorTypeBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connectorTypeBean);
    }
}