/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the catalog dataset facet.  It identifies the catalog (for example a lakehouse catalog) that the dataset is registered in.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-1-0/CatalogDatasetFacet.json#/$defs/CatalogDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageCatalogDataSetFacet extends OpenLineageDataSetFacet
{
    private String              framework = null;
    private String              type = null;
    private String              name = null;
    private String              metadataUri = null;
    private String              warehouseUri = null;
    private String              source = null;
    private Map<String, String> catalogProperties = null;


    /**
     * Default constructor
     */
    public OpenLineageCatalogDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-1-0/CatalogDatasetFacet.json#/$defs/CatalogDatasetFacet"));
    }


    /**
     * Return the storage framework for which the catalog is configured, for example iceberg or delta.
     *
     * @return string
     */
    public String getFramework()
    {
        return framework;
    }


    /**
     * Set up the storage framework for which the catalog is configured, for example iceberg or delta.
     *
     * @param framework string
     */
    public void setFramework(String framework)
    {
        this.framework = framework;
    }


    /**
     * Return the type of the catalog, for example rest, hive, glue or nessie.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of the catalog, for example rest, hive, glue or nessie.
     *
     * @param type string
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the name of the catalog, as configured in the source system.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the catalog, as configured in the source system.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the URI or connection string to the catalog, if applicable.
     *
     * @return string
     */
    public String getMetadataUri()
    {
        return metadataUri;
    }


    /**
     * Set up the URI or connection string to the catalog, if applicable.
     *
     * @param metadataUri string
     */
    public void setMetadataUri(String metadataUri)
    {
        this.metadataUri = metadataUri;
    }


    /**
     * Return the URI or connection string to the physical location of the data that the catalog describes.
     *
     * @return string
     */
    public String getWarehouseUri()
    {
        return warehouseUri;
    }


    /**
     * Set up the URI or connection string to the physical location of the data that the catalog describes.
     *
     * @param warehouseUri string
     */
    public void setWarehouseUri(String warehouseUri)
    {
        this.warehouseUri = warehouseUri;
    }


    /**
     * Return the source system where the catalog is configured, for example spark or flink.
     *
     * @return string
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source system where the catalog is configured, for example spark or flink.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return the additional catalog properties.
     *
     * @return map
     */
    public Map<String, String> getCatalogProperties()
    {
        return catalogProperties;
    }


    /**
     * Set up the additional catalog properties.
     *
     * @param catalogProperties map
     */
    public void setCatalogProperties(Map<String, String> catalogProperties)
    {
        this.catalogProperties = catalogProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageCatalogDataSetFacet{" +
                       "framework='" + framework + '\'' +
                       ", type='" + type + '\'' +
                       ", name='" + name + '\'' +
                       ", metadataUri='" + metadataUri + '\'' +
                       ", warehouseUri='" + warehouseUri + '\'' +
                       ", source='" + source + '\'' +
                       ", catalogProperties=" + catalogProperties +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", _deleted=" + get_deleted() +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        OpenLineageCatalogDataSetFacet that = (OpenLineageCatalogDataSetFacet) objectToCompare;
        return Objects.equals(framework, that.framework) &&
                       Objects.equals(type, that.type) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(metadataUri, that.metadataUri) &&
                       Objects.equals(warehouseUri, that.warehouseUri) &&
                       Objects.equals(source, that.source) &&
                       Objects.equals(catalogProperties, that.catalogProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), framework, type, name, metadataUri, warehouseUri, source, catalogProperties);
    }
}
