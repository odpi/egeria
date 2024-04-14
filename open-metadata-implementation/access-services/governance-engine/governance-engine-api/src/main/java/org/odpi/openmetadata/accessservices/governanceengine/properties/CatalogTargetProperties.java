/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.properties;

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
    private String              catalogTargetName           = null;
    private String              metadataSourceQualifiedName = null;
    private String              connectionName              = null;
    private Map<String, Object> configurationProperties     = null;
    private Map<String, String> templateProperties          = null;


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
            catalogTargetName = template.getCatalogTargetName();
            metadataSourceQualifiedName = template.getMetadataSourceQualifiedName();
            connectionName = template.getConnectionName();
            configurationProperties = template.getConfigurationProperties();
            templateProperties = template.getTemplateProperties();
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
     * Return the qualified name of the metadata source for this integration connector.  This is the qualified name
     * of an appropriate software server capability stored in open metadata.  This software server capability
     * is accessed via the partner OMAS.
     *
     * @return string name
     */
    public String getMetadataSourceQualifiedName()
    {
        return metadataSourceQualifiedName;
    }


    /**
     * Set up the qualified name of the metadata source for this integration connector.  This is the qualified name
     * of an appropriate software server capability stored in open metadata.  This software server capability
     * is accessed via the partner OMAS.
     *
     * @param metadataSourceQualifiedName string name
     */
    public void setMetadataSourceQualifiedName(String metadataSourceQualifiedName)
    {
        this.metadataSourceQualifiedName = metadataSourceQualifiedName;
    }


    /**
     * Set up the configuration properties for this Connection.
     *
     * @param configurationProperties properties that contain additional configuration information for the connector.
     */
    public void setConfigurationProperties(Map<String, Object> configurationProperties)
    {
        this.configurationProperties = configurationProperties;
    }


    /**
     * Return a copy of the configuration properties.  Null means no configuration properties are available.
     *
     * @return configuration properties typically controlling the behaviour for the connector
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
     * Set up the template properties for this connector to use when creating elements for this catalog target.
     *
     * @param templateProperties map of template name to qualified name of template implementation
     */
    public void setTemplateProperties(Map<String, String> templateProperties)
    {
        this.templateProperties = templateProperties;
    }


    /**
     * Return a copy of the template properties.  Null means no template properties are available.
     *
     * @return map of template name to qualified name of template implementation
     */
    public Map<String, String> getTemplateProperties()
    {
        if (templateProperties == null)
        {
            return null;
        }
        else if (templateProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(templateProperties);
        }
    }

    /**
     * Return the name of the connection to access the resource.  If it is null, the connection for
     * the asset that is either the catalog target, or is the anchor of the catalog target is used.
     *
     * @return qualified name of connection
     */
    public String getConnectionName()
    {
        return connectionName;
    }


    /**
     * Set up the name of the connection that the connector should use to access the resource.
     * If it is null, the connection for the asset that is either the catalog target, or is the anchor
     * of the catalog target is used.
     *
     * @param connectionName qualified name of a connection
     */
    public void setConnectionName(String connectionName)
    {
        this.connectionName = connectionName;
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
                ", metadataSourceQualifiedName='" + metadataSourceQualifiedName + '\'' +
                ", connectionName='" + connectionName + '\'' +
                ", configurationProperties=" + configurationProperties +
                ", templateProperties=" + templateProperties +
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
                Objects.equals(connectionName, that.connectionName) &&
                Objects.equals(templateProperties, that.templateProperties) &&
                Objects.equals(metadataSourceQualifiedName, that.metadataSourceQualifiedName) &&
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
        return Objects.hash(catalogTargetName, metadataSourceQualifiedName, connectionName, configurationProperties, templateProperties);
    }
}