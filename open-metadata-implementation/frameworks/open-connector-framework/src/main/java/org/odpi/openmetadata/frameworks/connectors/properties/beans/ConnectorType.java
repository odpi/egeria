/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


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
 *         recognizedAdditionalProperties - these are the Connection additional properties recognized by the connector implementation
 *     </li>
 *     <li>
 *         recognizedConfigurationProperties - these are the Connection configuration properties recognized by the connector implementation
 *     </li>
 *     <li>
 *         recognizedSecuredProperties - these are the Connection secured properties recognized by the connector implementation
 *     </li>
 *     <li>
 *         additionalProperties - Any additional properties that the connector provider needs to know in order to
 *         create connector instances.
 *     </li>
 * </ul>
 *
 * The connectorTypeProperties class is simply used to cache the properties for an connector type.
 * It is used by other classes to exchange this information between a metadata repository and a consumer.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorType extends Referenceable
{
    private static final long     serialVersionUID = 1L;

    /*
     * Attributes of a connector type
     */
    protected String       displayName                       = null;
    protected String       description                       = null;
    protected String       connectorProviderClassName        = null;
    protected List<String> recognizedAdditionalProperties    = null;
    protected List<String> recognizedConfigurationProperties = null;
    protected List<String> recognizedSecuredProperties       = null;


    /**
     * Return the standard type for a connector type.
     *
     * @return ElementType object
     */
    public static ElementType getConnectorTypeType()
    {
        final String        elementTypeId                   = "954421eb-33a6-462d-a8ca-b5709a1bd0d4";
        final String        elementTypeName                 = "ConnectorType";
        final long          elementTypeVersion              = 1;
        final String        elementTypeDescription          = "A set of properties describing a type of connector.";
        final String        elementAccessServiceURL         = null;
        final ElementOrigin elementOrigin                   = ElementOrigin.LOCAL_COHORT;
        final String        elementHomeMetadataCollectionId = null;

        ElementType elementType = new ElementType();

        elementType.setElementTypeId(elementTypeId);
        elementType.setElementTypeName(elementTypeName);
        elementType.setElementTypeVersion(elementTypeVersion);
        elementType.setElementTypeDescription(elementTypeDescription);
        elementType.setElementSourceServer(elementAccessServiceURL);
        elementType.setElementOrigin(elementOrigin);
        elementType.setElementMetadataCollectionId(elementHomeMetadataCollectionId);

        return elementType;
    }


    /**
     * Default constructor
     */
    public ConnectorType()
    {
        super();
    }


    /**
     * Copy/clone constructor for a connectorType that is not connected to an asset (either directly or indirectly).
     *
     * @param template template object to copy.
     */
    public ConnectorType(ConnectorType template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
            connectorProviderClassName = template.getConnectorProviderClassName();
            recognizedAdditionalProperties = template.getRecognizedAdditionalProperties();
            recognizedConfigurationProperties = template.getRecognizedConfigurationProperties();
            recognizedSecuredProperties = template.getRecognizedSecuredProperties();
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
     * Returns the stored display name property for the connector type.
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
     * Returns the stored description property for the connector type.
     * If no description is available then null is returned.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * The name of the connector provider class name.
     *
     * @param connectorProviderClassName String class name
     */
    public void setConnectorProviderClassName(String connectorProviderClassName)
    {
        this.connectorProviderClassName = connectorProviderClassName;
    }


    /**
     * Returns the stored connectorProviderClassName property for the connector type.
     * If no connectorProviderClassName is available then null is returned.
     *
     * @return connectorProviderClassName class name (including package name)
     */
    public String getConnectorProviderClassName()
    {
        return connectorProviderClassName;
    }


    /**
     * Set up the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's additionalProperties.
     *
     * @param recognizedAdditionalProperties  list of property names
     */
    public void setRecognizedAdditionalProperties(List<String>   recognizedAdditionalProperties)
    {
        this.recognizedAdditionalProperties = recognizedAdditionalProperties;
    }


    /**
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's additionalProperties.
     *
     * @return list of property names
     */
    public List<String> getRecognizedAdditionalProperties()
    {
        if (recognizedAdditionalProperties == null)
        {
            return null;
        }
        else if (recognizedAdditionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(recognizedAdditionalProperties);
        }
    }


    /**
     * Set up the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's configurationProperties.
     *
     * @param recognizedConfigurationProperties  list of property names
     */
    public void setRecognizedConfigurationProperties(List<String>   recognizedConfigurationProperties)
    {

        this.recognizedConfigurationProperties = recognizedConfigurationProperties;
    }


    /**
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's configurationProperties.
     *
     * @return list of property names
     */
    public List<String> getRecognizedConfigurationProperties()
    {
        if (recognizedConfigurationProperties == null)
        {
            return null;
        }
        else if (recognizedConfigurationProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(recognizedConfigurationProperties);
        }
    }


    /**
     * Set up the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's securedProperties.
     *
     * @param recognizedSecuredProperties  list of property names
     */
    public void  setRecognizedSecuredProperties(List<String>  recognizedSecuredProperties)
    {
        this.recognizedSecuredProperties = recognizedSecuredProperties;
    }


    /**
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's securedProperties.
     *
     * @return list of property names
     */
    public List<String> getRecognizedSecuredProperties()
    {
        if (recognizedSecuredProperties == null)
        {
            return null;
        }
        else if (recognizedSecuredProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(recognizedSecuredProperties);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConnectorType{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", connectorProviderClassName='" + connectorProviderClassName + '\'' +
                ", recognizedAdditionalProperties=" + recognizedAdditionalProperties +
                ", recognizedSecuredProperties=" + recognizedSecuredProperties +
                ", recognizedConfigurationProperties=" + recognizedConfigurationProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        ConnectorType that = (ConnectorType) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getConnectorProviderClassName(), that.getConnectorProviderClassName()) &&
                       Objects.equals(getRecognizedAdditionalProperties(), that.getRecognizedAdditionalProperties()) &&
                       Objects.equals(getRecognizedConfigurationProperties(), that.getRecognizedConfigurationProperties()) &&
                       Objects.equals(getRecognizedSecuredProperties(), that.getRecognizedSecuredProperties());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getConnectorProviderClassName(),
                            getRecognizedAdditionalProperties(),
                            getRecognizedConfigurationProperties(), getRecognizedSecuredProperties());
    }
}
