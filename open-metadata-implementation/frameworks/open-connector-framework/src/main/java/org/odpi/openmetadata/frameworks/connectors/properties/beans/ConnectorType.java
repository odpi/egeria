/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
 *         supportedAssetTypeName - the type of asset that the connector implementation supports.
 *     </li>
 *     <li>
 *         expectedDataFormat - the format of the data that the connector supports - null for "any".
 *     </li>
 *     <li>
 *         connectorProviderClassName - The connector provider is the factory for a particular type of connector.
 *         This property defines the class name for the connector provider that the Connector Broker should use to request
 *         new connector instances.
 *     </li>
 *     <li>
 *         connectorFrameworkName - name of the connector framework that the connector implements - default Open Connector Framework (OCF).
 *     </li>
 *     <li>
 *         connectorInterfaceLanguage - the language that the connector is implemented in - default Java.
 *     </li>
 *     <li>
 *         connectorInterfaces - list of interfaces that the connector supports.
 *     </li>
 *     <li>
 *         targetTechnologySource - the organization that supplies the target technology that the connector implementation connects to.
 *     </li>
 *     <li>
 *         targetTechnologyName - the name of the target technology that the connector implementation connects to.
 *     </li>
 *     <li>
 *         targetTechnologyInterfaces - the names of the interfaces in the target technology that the connector calls.
 *     </li>
 *     <li>
 *         targetTechnologyVersions - the versions of the target technology that the connector supports.
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
 * The connectorTypeProperties class is simply used to cache the properties for a connector type.
 * It is used by other classes to exchange this information between a metadata repository and a consumer.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorType extends Referenceable
{
    /*
     * Attributes of a connector type
     */
    private String       displayName                       = null;
    private String       description                       = null;
    private String       supportedAssetTypeName            = null;
    private String       deployedImplementationType        = null;
    private String       expectedDataFormat                = null;
    private String       connectorProviderClassName        = null;
    private String       connectorFrameworkName            = null;
    private String       connectorInterfaceLanguage        = null;
    private List<String> connectorInterfaces               = null;
    private String       targetTechnologySource            = null;
    private String       targetTechnologyName              = null;
    private List<String> targetTechnologyInterfaces        = null;
    private List<String> targetTechnologyVersions          = null;
    private List<String> recognizedAdditionalProperties    = null;
    private List<String> recognizedConfigurationProperties = null;
    private List<String> recognizedSecuredProperties       = null;


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
            supportedAssetTypeName = template.getSupportedAssetTypeName();
            deployedImplementationType = template.getDeployedImplementationType();
            expectedDataFormat = template.getExpectedDataFormat();
            connectorProviderClassName = template.getConnectorProviderClassName();
            connectorFrameworkName = template.getConnectorFrameworkName();
            connectorInterfaceLanguage = template.getConnectorInterfaceLanguage();
            connectorInterfaces = template.getConnectorInterfaces();
            targetTechnologySource = template.getTargetTechnologySource();
            targetTechnologyName = template.getTargetTechnologyName();
            targetTechnologyInterfaces = template.getTargetTechnologyInterfaces();
            targetTechnologyVersions = template.getTargetTechnologyVersions();
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
     * Return the type of asset that the connector implementation supports.
     *
     * @return string name
     */
    public String getSupportedAssetTypeName()
    {
        return supportedAssetTypeName;
    }


    /**
     * Set up the type of asset that the connector implementation supports.  The expectation is that a connection with this connector
     * type is attached to assets of this type.
     *
     * @param supportedAssetTypeName string name
     */
    public void setSupportedAssetTypeName(String supportedAssetTypeName)
    {
        this.supportedAssetTypeName = supportedAssetTypeName;
    }


    /**
     * Return the name of the type of technology this connector works with.  This allows the connector to be targeted to the right resource more
     * accurately.
     *
     * @return string name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the name of the type of technology this connector works with.  This allows the connector to be targeted to the right resource more
     * accurately.
     *
     * @param deployedImplementationType string name
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return the format of the data that the connector supports - null for "any".
     *
     * @return string name
     */
    public String getExpectedDataFormat()
    {
        return expectedDataFormat;
    }


    /**
     * Set up the format of the data that the connector supports - null for "any".
     *
     * @param expectedDataFormat string name
     */
    public void setExpectedDataFormat(String expectedDataFormat)
    {
        this.expectedDataFormat = expectedDataFormat;
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
     * Return name of the connector framework that the connector implements - default Open Connector Framework (OCF).
     *
     * @return string name
     */
    public String getConnectorFrameworkName()
    {
        return connectorFrameworkName;
    }


    /**
     * Set up name of the connector framework that the connector implements - default Open Connector Framework (OCF).
     *
     * @param connectorFrameworkName string name
     */
    public void setConnectorFrameworkName(String connectorFrameworkName)
    {
        this.connectorFrameworkName = connectorFrameworkName;
    }


    /**
     * Return the language that the connector is implemented in - default Java.
     *
     * @return string name
     */
    public String getConnectorInterfaceLanguage()
    {
        return connectorInterfaceLanguage;
    }


    /**
     * Set up the language that the connector is implemented in - default Java.
     *
     * @param connectorInterfaceLanguage string name
     */
    public void setConnectorInterfaceLanguage(String connectorInterfaceLanguage)
    {
        this.connectorInterfaceLanguage = connectorInterfaceLanguage;
    }


    /**
     * Return list of interfaces that the connector supports.
     *
     * @return list of names
     */
    public List<String> getConnectorInterfaces()
    {
        return connectorInterfaces;
    }


    /**
     * Set up list of interfaces that the connector supports.
     *
     * @param connectorInterfaces list of names
     */
    public void setConnectorInterfaces(List<String> connectorInterfaces)
    {
        this.connectorInterfaces = connectorInterfaces;
    }


    /**
     * Return the name of the organization that supplies the target technology that the connector implementation connects to.
     *
     * @return string name
     */
    public String getTargetTechnologySource()
    {
        return targetTechnologySource;
    }


    /**
     * Set up the name of the organization that supplies the target technology that the connector implementation connects to.
     *
     * @param targetTechnologySource list of names
     */
    public void setTargetTechnologySource(String targetTechnologySource)
    {
        this.targetTechnologySource = targetTechnologySource;
    }


    /**
     * Return the name of the target technology that the connector implementation connects to.
     *
     * @return string name
     */
    public String getTargetTechnologyName()
    {
        return targetTechnologyName;
    }


    /**
     * Set up the name of the target technology that the connector implementation connects to.
     *
     * @param targetTechnologyName string name
     */
    public void setTargetTechnologyName(String targetTechnologyName)
    {
        this.targetTechnologyName = targetTechnologyName;
    }


    /**
     * Return the names of the interfaces in the target technology that the connector calls.
     *
     * @return list of interface names
     */
    public List<String> getTargetTechnologyInterfaces()
    {
        return targetTechnologyInterfaces;
    }


    /**
     * Set up the names of the interfaces in the target technology that the connector calls.
     *
     * @param targetTechnologyInterfaces list of interface names
     */
    public void setTargetTechnologyInterfaces(List<String> targetTechnologyInterfaces)
    {
        this.targetTechnologyInterfaces = targetTechnologyInterfaces;
    }


    /**
     * Return the versions of the target technology that the connector supports.
     *
     * @return list of version identifiers
     */
    public List<String> getTargetTechnologyVersions()
    {
        return targetTechnologyVersions;
    }


    /**
     * Set up the versions of the target technology that the connector supports.
     *
     * @param targetTechnologyVersions list of version identifiers
     */
    public void setTargetTechnologyVersions(List<String> targetTechnologyVersions)
    {
        this.targetTechnologyVersions = targetTechnologyVersions;
    }


    /**
     * Set up the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's additionalProperties.
     *
     * @param recognizedAdditionalProperties  list of property names
     */
    public void setRecognizedAdditionalProperties(List<String> recognizedAdditionalProperties)
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
            return recognizedAdditionalProperties;
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
            return recognizedConfigurationProperties;
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
            return recognizedSecuredProperties;
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
                       ", supportedAssetTypeName='" + supportedAssetTypeName + '\'' +
                       ", deployedImplementationType='" + deployedImplementationType + '\'' +
                       ", expectedDataFormat='" + expectedDataFormat + '\'' +
                       ", connectorProviderClassName='" + connectorProviderClassName + '\'' +
                       ", connectorFrameworkName='" + connectorFrameworkName + '\'' +
                       ", connectorInterfaceLanguage='" + connectorInterfaceLanguage + '\'' +
                       ", connectorInterfaces=" + connectorInterfaces +
                       ", targetTechnologySource='" + targetTechnologySource + '\'' +
                       ", targetTechnologyName='" + targetTechnologyName + '\'' +
                       ", targetTechnologyInterfaces=" + targetTechnologyInterfaces +
                       ", targetTechnologyVersions=" + targetTechnologyVersions +
                       ", recognizedAdditionalProperties=" + recognizedAdditionalProperties +
                       ", recognizedConfigurationProperties=" + recognizedConfigurationProperties +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", type=" + getType() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", headerVersion=" + getHeaderVersion() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ConnectorType that = (ConnectorType) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(supportedAssetTypeName, that.supportedAssetTypeName) &&
                       Objects.equals(deployedImplementationType, that.deployedImplementationType) &&
                       Objects.equals(expectedDataFormat, that.expectedDataFormat) &&
                       Objects.equals(connectorProviderClassName, that.connectorProviderClassName) &&
                       Objects.equals(connectorFrameworkName, that.connectorFrameworkName) &&
                       Objects.equals(connectorInterfaceLanguage, that.connectorInterfaceLanguage) &&
                       Objects.equals(connectorInterfaces, that.connectorInterfaces) &&
                       Objects.equals(targetTechnologySource, that.targetTechnologySource) &&
                       Objects.equals(targetTechnologyName, that.targetTechnologyName) &&
                       Objects.equals(targetTechnologyInterfaces, that.targetTechnologyInterfaces) &&
                       Objects.equals(targetTechnologyVersions, that.targetTechnologyVersions) &&
                       Objects.equals(recognizedAdditionalProperties, that.recognizedAdditionalProperties) &&
                       Objects.equals(recognizedConfigurationProperties, that.recognizedConfigurationProperties) &&
                       Objects.equals(recognizedSecuredProperties, that.recognizedSecuredProperties);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, supportedAssetTypeName, deployedImplementationType, expectedDataFormat,
                            connectorProviderClassName, connectorFrameworkName, connectorInterfaceLanguage, connectorInterfaces,
                            targetTechnologySource, targetTechnologyName, targetTechnologyInterfaces, targetTechnologyVersions,
                            recognizedAdditionalProperties, recognizedConfigurationProperties, recognizedSecuredProperties);
    }
}
