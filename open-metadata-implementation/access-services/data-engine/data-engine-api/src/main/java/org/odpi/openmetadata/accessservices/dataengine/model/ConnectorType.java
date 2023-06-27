/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
/**
 * ConnectorType is a java bean used to create connector types associated with the external data engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ConnectorType  extends Referenceable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * --SETTER--
     * Set up the display name for UIs and reports.
     * @param displayName String name
     * --GETTER--
     * Returns the stored display name property for the connector type.
     * If no display name is available then null is returned.
     * @return displayName
     */
    private String displayName;

    /**
     * --SETTER--
     * Set up description of the element.
     * @param description String
     * --GETTER--
     * Returns the stored description property for the connector type.
     * If no description is available then null is returned.
     * @return description
     */
    private String description;

    /**
     * --SETTER--
     * Set up the type of asset that the connector implementation supports.
     * @param supportedAssetTypeName string name
     * --GETTER--
     * Return the type of asset that the connector implementation supports.
     * @return string name
     */
    private String supportedAssetTypeName;

    /**
     * --SETTER--
     * Set up the format of the data that the connector supports - null for "any".
     * @param expectedDataFormat string name
     * --GETTER--
     * Return the format of the data that the connector supports - null for "any".
     * @return string name
     */
    private String expectedDataFormat;

    /**
     * --SETTER--
     * The name of the connector provider class name.
     * @param connectorProviderClassName String class name
     * --GETTER--
     * Returns the stored connectorProviderClassName property for the connector type.
     * If no connectorProviderClassName is available then null is returned.
     * @return connectorProviderClassName class name (including package name)
     */
    private String connectorProviderClassName;

    /**
     * --SETTER--
     * Set up name of the connector framework that the connector implements - default Open Connector Framework (OCF).
     * @param connectorFrameworkName string name
     * --GETTER--
     * Return name of the connector framework that the connector implements - default Open Connector Framework (OCF).
     * @return string name
     */
    private String connectorFrameworkName;

    /**
     * --SETTER--
     * Set up the language that the connector is implemented in - default Java.
     * @param connectorInterfaceLanguage string name
     * --GETTER--
     * Return the language that the connector is implemented in - default Java.
     * @return string name
     */
    private String connectorInterfaceLanguage;

    /**
     * --SETTER--
     * Set up list of interfaces that the connector supports.
     * @param connectorInterfaces list of names
     * --GETTER--
     * Return list of interfaces that the connector supports.
     * @return list of names
     */
    private List<String> connectorInterfaces;

    /**
     * --SETTER--
     * Set up the name of the organization that supplies the target technology that the connector implementation connects to.
     * @param targetTechnologySource list of names
     * --GETTER--
     * Return the name of the organization that supplies the target technology that the connector implementation connects to.
     * @return string name
     */
    private String targetTechnologySource;

    /**
     * --SETTER--
     * Set up the name of the target technology that the connector implementation connects to.
     * @param targetTechnologyName string name
     * --GETTER--
     * Return the name of the target technology that the connector implementation connects to.
     * @return string name
     */
    private String targetTechnologyName;

    /**
     * --SETTER--
     * Set up the names of the interfaces in the target technology that the connector calls.
     * @param targetTechnologyInterfaces list of interface names
     * --GETTER--
     * Return the names of the interfaces in the target technology that the connector calls.
     * @return list of interface names
     */
    private List<String> targetTechnologyInterfaces;

    /**
     * --SETTER--
     * Set up the versions of the target technology that the connector supports.
     * @param targetTechnologyVersions list of version identifiers
     * --GETTER--
     * Return the versions of the target technology that the connector supports.
     * @return list of version identifiers
     */
    private List<String> targetTechnologyVersions;


    /**
     * --SETTER--
     * Set up the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's additionalProperties.
     * @param recognizedAdditionalProperties  list of property names
     * --GETTER--
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's additionalProperties.
     * @return list of property names
     */
    private List<String> recognizedAdditionalProperties;

    /**
     * --SETTER--
     * Set up the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's configurationProperties.
     * @param recognizedConfigurationProperties  list of property names
     * --GETTER--
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's configurationProperties.
     * @return list of property names
     */
    private List<String> recognizedConfigurationProperties;

    /**
     * --SETTER--
     * Set up the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's securedProperties.
     * @param recognizedSecuredProperties  list of property names
     * --GETTER--
     * Return the list of property names that this connector/connector provider implementation looks for
     * in the Connection object's securedProperties.
     * @return list of property names
     */
    private List<String> recognizedSecuredProperties;
}
