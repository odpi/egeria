/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Properties for the CatalogTarget relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CatalogTargetProperties
{
    private String              catalogTargetName       = null;
    private Map<String, Object> configurationProperties = null;


    /**
     * Default constructor
     */
    public CatalogTargetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public CatalogTargetProperties(CatalogTargetProperties template)
    {
        if (template != null)
        {
            catalogTargetName       = template.getCatalogTargetName();
            configurationProperties = template.getConfigurationProperties();
        }
    }


    /**
     * Set up the target name.
     *
     * @param catalogTargetName String name
     */
    public void setCatalogTargetName(String catalogTargetName)
    {
        this.catalogTargetName = catalogTargetName;
    }


    /**
     * Returns the target name property.
     *
     * @return qualifiedName
     */
    public String getCatalogTargetName()
    {
        return catalogTargetName;
    }


    /**
     * Set up the configuration properties for this action target.  These are used to override the configuration
     * properties in the integration connector's connection whenever it is processing this action target.
     *
     * @param configurationProperties properties typically controlling the behaviour for the integration connector.
     */
    public void setConfigurationProperties(Map<String, Object> configurationProperties)
    {
        this.configurationProperties = configurationProperties;
    }


    /**
     * Return a copy of the configuration properties.  These are used to override the configuration
     * properties in the integration connector's connection whenever it is processing this action target.
     *
     * @return configuration properties typically controlling the behaviour for the integration connector.
     */
    public Map<String, Object> getConfigurationProperties()
    {
        if (configurationProperties == null)
        {
            return null;
        }
        else if (configurationProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(configurationProperties);
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
        return "CatalogTargetProperties{" +
                "catalogTargetName='" + catalogTargetName + '\'' +
                "configurationProperties='" + configurationProperties + '\'' +
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
        CatalogTargetProperties that = (CatalogTargetProperties) objectToCompare;
        return Objects.equals(catalogTargetName, that.catalogTargetName) &&
                Objects.equals(configurationProperties, that.configurationProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(catalogTargetName, configurationProperties);
    }
}