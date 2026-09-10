/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the map of dataset facets for a dataset.  The standard facets from the OpenLineage spec, and the custom facets
 * registered in the OpenLineage registry, are held in named properties.  Any other facets (custom facets, or standard facets not yet modelled) are held in the
 * additionalProperties map, keyed by their facet name, so that nothing is lost on a round trip through these beans.
 * The map key of each facet in the JSON is the facet's key from the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataSetFacets
{
    private OpenLineageDocumentationDataSetFacet        documentation = null;
    private OpenLineageDataSourceDataSetFacet           dataSource = null;
    private OpenLineageSchemaDataSetFacet               schema = null;
    private OpenLineageCatalogDataSetFacet              catalog = null;
    private OpenLineageColumnLineageDataSetFacet        columnLineage = null;
    private OpenLineageDataSetTypeDataSetFacet          datasetType = null;
    private OpenLineageDataSetVersionDataSetFacet       version = null;
    private OpenLineageLifecycleStateChangeDataSetFacet lifecycleStateChange = null;
    private OpenLineageOwnershipDataSetFacet            ownership = null;
    private OpenLineageStorageDataSetFacet              storage = null;
    private OpenLineageSymlinksDataSetFacet             symlinks = null;
    private OpenLineageTagsDataSetFacet                 tags = null;
    private OpenLineageHierarchyDataSetFacet            hierarchy = null;
    private OpenLineageLineageDataSetFacet              lineage = null;
    private OpenLineageDataQualityMetricsDataSetFacet   dataQualityMetrics = null;
    private Map<String, OpenLineageDataSetFacet>        additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageDataSetFacets()
    {
    }


    /**
     * Return the documentation facet.
     *
     * @return facet bean
     */
    public OpenLineageDocumentationDataSetFacet getDocumentation()
    {
        return documentation;
    }


    /**
     * Set up the documentation facet.
     *
     * @param documentation facet bean
     */
    public void setDocumentation(OpenLineageDocumentationDataSetFacet documentation)
    {
        this.documentation = documentation;
    }


    /**
     * Return the dataSource facet.
     *
     * @return facet bean
     */
    public OpenLineageDataSourceDataSetFacet getDataSource()
    {
        return dataSource;
    }


    /**
     * Set up the dataSource facet.
     *
     * @param dataSource facet bean
     */
    public void setDataSource(OpenLineageDataSourceDataSetFacet dataSource)
    {
        this.dataSource = dataSource;
    }


    /**
     * Return the schema facet.
     *
     * @return facet bean
     */
    public OpenLineageSchemaDataSetFacet getSchema()
    {
        return schema;
    }


    /**
     * Set up the schema facet.
     *
     * @param schema facet bean
     */
    public void setSchema(OpenLineageSchemaDataSetFacet schema)
    {
        this.schema = schema;
    }


    /**
     * Return the catalog facet.
     *
     * @return facet bean
     */
    public OpenLineageCatalogDataSetFacet getCatalog()
    {
        return catalog;
    }


    /**
     * Set up the catalog facet.
     *
     * @param catalog facet bean
     */
    public void setCatalog(OpenLineageCatalogDataSetFacet catalog)
    {
        this.catalog = catalog;
    }


    /**
     * Return the columnLineage facet.
     *
     * @return facet bean
     */
    public OpenLineageColumnLineageDataSetFacet getColumnLineage()
    {
        return columnLineage;
    }


    /**
     * Set up the columnLineage facet.
     *
     * @param columnLineage facet bean
     */
    public void setColumnLineage(OpenLineageColumnLineageDataSetFacet columnLineage)
    {
        this.columnLineage = columnLineage;
    }


    /**
     * Return the datasetType facet.
     *
     * @return facet bean
     */
    public OpenLineageDataSetTypeDataSetFacet getDatasetType()
    {
        return datasetType;
    }


    /**
     * Set up the datasetType facet.
     *
     * @param datasetType facet bean
     */
    public void setDatasetType(OpenLineageDataSetTypeDataSetFacet datasetType)
    {
        this.datasetType = datasetType;
    }


    /**
     * Return the version facet.
     *
     * @return facet bean
     */
    public OpenLineageDataSetVersionDataSetFacet getVersion()
    {
        return version;
    }


    /**
     * Set up the version facet.
     *
     * @param version facet bean
     */
    public void setVersion(OpenLineageDataSetVersionDataSetFacet version)
    {
        this.version = version;
    }


    /**
     * Return the lifecycleStateChange facet.
     *
     * @return facet bean
     */
    public OpenLineageLifecycleStateChangeDataSetFacet getLifecycleStateChange()
    {
        return lifecycleStateChange;
    }


    /**
     * Set up the lifecycleStateChange facet.
     *
     * @param lifecycleStateChange facet bean
     */
    public void setLifecycleStateChange(OpenLineageLifecycleStateChangeDataSetFacet lifecycleStateChange)
    {
        this.lifecycleStateChange = lifecycleStateChange;
    }


    /**
     * Return the ownership facet.
     *
     * @return facet bean
     */
    public OpenLineageOwnershipDataSetFacet getOwnership()
    {
        return ownership;
    }


    /**
     * Set up the ownership facet.
     *
     * @param ownership facet bean
     */
    public void setOwnership(OpenLineageOwnershipDataSetFacet ownership)
    {
        this.ownership = ownership;
    }


    /**
     * Return the storage facet.
     *
     * @return facet bean
     */
    public OpenLineageStorageDataSetFacet getStorage()
    {
        return storage;
    }


    /**
     * Set up the storage facet.
     *
     * @param storage facet bean
     */
    public void setStorage(OpenLineageStorageDataSetFacet storage)
    {
        this.storage = storage;
    }


    /**
     * Return the symlinks facet.
     *
     * @return facet bean
     */
    public OpenLineageSymlinksDataSetFacet getSymlinks()
    {
        return symlinks;
    }


    /**
     * Set up the symlinks facet.
     *
     * @param symlinks facet bean
     */
    public void setSymlinks(OpenLineageSymlinksDataSetFacet symlinks)
    {
        this.symlinks = symlinks;
    }


    /**
     * Return the tags facet.
     *
     * @return facet bean
     */
    public OpenLineageTagsDataSetFacet getTags()
    {
        return tags;
    }


    /**
     * Set up the tags facet.
     *
     * @param tags facet bean
     */
    public void setTags(OpenLineageTagsDataSetFacet tags)
    {
        this.tags = tags;
    }


    /**
     * Return the hierarchy facet.
     *
     * @return facet bean
     */
    public OpenLineageHierarchyDataSetFacet getHierarchy()
    {
        return hierarchy;
    }


    /**
     * Set up the hierarchy facet.
     *
     * @param hierarchy facet bean
     */
    public void setHierarchy(OpenLineageHierarchyDataSetFacet hierarchy)
    {
        this.hierarchy = hierarchy;
    }


    /**
     * Return the lineage facet.
     *
     * @return facet bean
     */
    public OpenLineageLineageDataSetFacet getLineage()
    {
        return lineage;
    }


    /**
     * Set up the lineage facet.
     *
     * @param lineage facet bean
     */
    public void setLineage(OpenLineageLineageDataSetFacet lineage)
    {
        this.lineage = lineage;
    }


    /**
     * Return the dataQualityMetrics facet.
     *
     * @return facet bean
     */
    public OpenLineageDataQualityMetricsDataSetFacet getDataQualityMetrics()
    {
        return dataQualityMetrics;
    }


    /**
     * Set up the dataQualityMetrics facet.
     *
     * @param dataQualityMetrics facet bean
     */
    public void setDataQualityMetrics(OpenLineageDataQualityMetricsDataSetFacet dataQualityMetrics)
    {
        this.dataQualityMetrics = dataQualityMetrics;
    }


    /**
     * Return any additional facets that are not modelled as named properties.  They are serialized as
     * top-level facet entries alongside the named facets.
     *
     * @return map of facet name to facet
     */
    @JsonAnyGetter
    public Map<String, OpenLineageDataSetFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional facets that are not modelled as named properties.
     *
     * @param additionalProperties map of facet name to facet
     */
    public void setAdditionalProperties(Map<String, OpenLineageDataSetFacet> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Add a facet that is not modelled as a named property.  Jackson calls this for each unrecognized facet
     * found in the JSON.
     *
     * @param facetName name (key) of the facet
     * @param facet facet bean
     */
    @JsonAnySetter
    public void setAdditionalProperty(String facetName, OpenLineageDataSetFacet facet)
    {
        if (additionalProperties == null)
        {
            additionalProperties = new LinkedHashMap<>();
        }
        additionalProperties.put(facetName, facet);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDataSetFacets{" +
                       "documentation=" + documentation +
                       ", dataSource=" + dataSource +
                       ", schema=" + schema +
                       ", catalog=" + catalog +
                       ", columnLineage=" + columnLineage +
                       ", datasetType=" + datasetType +
                       ", version=" + version +
                       ", lifecycleStateChange=" + lifecycleStateChange +
                       ", ownership=" + ownership +
                       ", storage=" + storage +
                       ", symlinks=" + symlinks +
                       ", tags=" + tags +
                       ", hierarchy=" + hierarchy +
                       ", lineage=" + lineage +
                       ", dataQualityMetrics=" + dataQualityMetrics +
                       ", additionalProperties=" + additionalProperties +
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
        OpenLineageDataSetFacets that = (OpenLineageDataSetFacets) objectToCompare;
        return Objects.equals(documentation, that.documentation) &&
                       Objects.equals(dataSource, that.dataSource) &&
                       Objects.equals(schema, that.schema) &&
                       Objects.equals(catalog, that.catalog) &&
                       Objects.equals(columnLineage, that.columnLineage) &&
                       Objects.equals(datasetType, that.datasetType) &&
                       Objects.equals(version, that.version) &&
                       Objects.equals(lifecycleStateChange, that.lifecycleStateChange) &&
                       Objects.equals(ownership, that.ownership) &&
                       Objects.equals(storage, that.storage) &&
                       Objects.equals(symlinks, that.symlinks) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(hierarchy, that.hierarchy) &&
                       Objects.equals(lineage, that.lineage) &&
                       Objects.equals(dataQualityMetrics, that.dataQualityMetrics) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(documentation, dataSource, schema, catalog, columnLineage, datasetType, version, lifecycleStateChange, ownership, storage, symlinks, tags, hierarchy, lineage, dataQualityMetrics, additionalProperties);
    }
}
