/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ConnectorType  extends Referenceable {

    private static final long serialVersionUID = 1L;

    private String displayName;
    private String description;
    private String supportedAssetTypeName;
    private String expectedDataFormat;
    private String connectorProviderClassName;
    private String connectorFrameworkName;
    private String connectorInterfaceLanguage;
    private List<String> connectorInterfaces;
    private String targetTechnologySource;
    private String targetTechnologyName;
    private List<String> targetTechnologyInterfaces;
    private List<String> targetTechnologyVersions;
    private List<String> recognizedAdditionalProperties;
    private List<String> recognizedConfigurationProperties;
    private List<String> recognizedSecuredProperties;
}
