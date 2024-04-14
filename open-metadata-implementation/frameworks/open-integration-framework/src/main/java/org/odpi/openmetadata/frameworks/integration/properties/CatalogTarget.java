/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CatalogTarget contains the properties for one of the integration connector's catalog targets.
 * Each integration connector is optionally linked via the CatalogTarget relationship to one or more elements that the integration connector
 * is working on. The catalogTargetElement contains details of the entity that represents the catalog target.
 * It is extracted from entity proxy two of the catalog target relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CatalogTarget extends CatalogTargetProperties
{
    private String              relationshipGUID            = null;
    private String              catalogTargetName           = null;
    private String              metadataSourceQualifiedName = null;
    private Map<String, Object> configurationProperties     = null;
    private ElementStub         catalogTargetElement        = null;


    /**
     * Default constructor
     */
    public CatalogTarget()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CatalogTarget(CatalogTarget template)
    {
        if (template != null)
        {
            relationshipGUID = template.getRelationshipGUID();
            catalogTargetName = template.getCatalogTargetName();
            metadataSourceQualifiedName = template.getMetadataSourceQualifiedName();
            configurationProperties = template.getConfigurationProperties();
            catalogTargetElement = template.getCatalogTargetElement();
        }
    }


    /**
     * Return the unique identifier of the CatalogTarget relationship.  This is useful if the values in this relationship need to be updated.
     *
     * @return string guid
     */
    public String getRelationshipGUID()
    {
        return relationshipGUID;
    }


    /**
     * Set up the unique identifier of the CatalogTarget relationship.  This is useful if the values in this relationship need to be updated.
     *
     * @param relationshipGUID string guid
     */
    public void setRelationshipGUID(String relationshipGUID)
    {
        this.relationshipGUID = relationshipGUID;
    }


    /**
     * Return the name of the catalog target.
     *
     * @return string
     */
    public String getCatalogTargetName()
    {
        return catalogTargetName;
    }


    /**
     * Set up the name of the catalog target.
     *
     * @param catalogTargetName string
     */
    public void setCatalogTargetName(String catalogTargetName)
    {
        this.catalogTargetName = catalogTargetName;
    }


    /**
     * Return the qualified name used for the metadata collection of catalogued elements.  It is the
     * qualified name of a software capability.
     *
     * @return name
     */
    public String getMetadataSourceQualifiedName()
    {
        return metadataSourceQualifiedName;
    }


    /**
     * Set up the qualified name used for the metadata collection of catalogued elements.  It is the
     * qualified name of a software capability.
     *
     * @param metadataSourceQualifiedName name
     */
    public void setMetadataSourceQualifiedName(String metadataSourceQualifiedName)
    {
        this.metadataSourceQualifiedName = metadataSourceQualifiedName;
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
     * Return details of the catalog target element.  This is extracted from the entity proxy so the classification list may not be complete.
     *
     * @return element stub
     */
    public ElementStub getCatalogTargetElement()
    {
        return catalogTargetElement;
    }


    /**
     * Set up details of the catalog target element.  This is extracted from the entity proxy so the classification list may not be complete.
     *
     * @param catalogTargetElement element stub
     */
    public void setCatalogTargetElement(ElementStub catalogTargetElement)
    {
        this.catalogTargetElement = catalogTargetElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CatalogTarget{" +
                "relationshipGUID='" + relationshipGUID + '\'' +
                ", catalogTargetName='" + catalogTargetName + '\'' +
                ", metadataSourceQualifiedName='" + metadataSourceQualifiedName + '\'' +
                ", configurationProperties='" + configurationProperties + '\'' +
                ", catalogTargetElement=" + catalogTargetElement +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof CatalogTarget that))
        {
            return false;
        }
        return Objects.equals(relationshipGUID, that.relationshipGUID) &&
                Objects.equals(catalogTargetName, that.catalogTargetName) &&
                Objects.equals(metadataSourceQualifiedName, that.metadataSourceQualifiedName) &&
                Objects.equals(configurationProperties, that.configurationProperties) &&
                Objects.equals(catalogTargetElement, that.catalogTargetElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipGUID, catalogTargetName, metadataSourceQualifiedName, configurationProperties, catalogTargetElement);
    }
}
