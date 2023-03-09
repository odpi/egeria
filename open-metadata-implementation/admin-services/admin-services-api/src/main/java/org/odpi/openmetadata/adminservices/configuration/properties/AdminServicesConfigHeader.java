/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AccessServicesConfigHeader provides a common header for configuration properties.  It implements
 * java.io.Serializable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AccessServiceConfig.class, name = "AccessServiceConfig"),
        @JsonSubTypes.Type(value = CohortConfig.class, name = "CohortConfig"),
        @JsonSubTypes.Type(value = ConformanceSuiteConfig.class, name = "ConformanceSuiteConfig"),
        @JsonSubTypes.Type(value = PlatformConformanceWorkbenchConfig.class, name = "PlatformConformanceWorkbenchConfig"),
        @JsonSubTypes.Type(value = RepositoryConformanceWorkbenchConfig.class, name = "RepositoryConformanceWorkbenchConfig"),
        @JsonSubTypes.Type(value = RepositoryPerformanceWorkbenchConfig.class, name = "RepositoryPerformanceWorkbenchConfig"),
        @JsonSubTypes.Type(value = EnterpriseAccessConfig.class, name = "EnterpriseAccessConfig"),
        @JsonSubTypes.Type(value = EventBusConfig.class, name = "EventBusConfig"),
        @JsonSubTypes.Type(value = LocalRepositoryConfig.class, name = "LocalRepositoryConfig"),
        @JsonSubTypes.Type(value = OMAGServerConfig.class, name = "OMAGServerConfig"),
        @JsonSubTypes.Type(value = OpenLineageServerConfig.class, name = "OpenLineageConfig"),
        @JsonSubTypes.Type(value = RepositoryServicesConfig.class, name = "RepositoryServicesConfig"),
        @JsonSubTypes.Type(value = ResourceEndpointConfig.class, name = "ResourceEndpointConfig"),
        @JsonSubTypes.Type(value = DataEngineProxyConfig.class, name = "DataEngineProxyConfig")
})
public class AdminServicesConfigHeader implements Serializable
{
    private static final long serialVersionUID = 1L;


    /**
     * Default Constructor
     */
    public AdminServicesConfigHeader()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AdminServicesConfigHeader(AdminServicesConfigHeader template)
    {
    }

}
