/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the map of data set facets in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataSetFacets
{
    private OpenLineageDocumentationDataSetFacet documentation        = null;
    private OpenLineageDataSourceDataSetFacet    dataSource           = null;
    private OpenLineageSchemaDataSetFacet        schema               = null;
    private Map<String, OpenLineageDataSetFacet> additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageDataSetFacets()
    {
    }


    /**
     * Return the document facet.
     *
     * @return facet
     */
    public OpenLineageDocumentationDataSetFacet getDocumentation()
    {
        return documentation;
    }


    /**
     * Set up the document facet.
     *
     * @param documentation facet
     */
    public void setDocumentation(OpenLineageDocumentationDataSetFacet documentation)
    {
        this.documentation = documentation;
    }


    /**
     * Return the data source facet.
     *
     * @return facet
     */
    public OpenLineageDataSourceDataSetFacet getDataSource()
    {
        return dataSource;
    }


    /**
     * Set up the data source facet.
     *
     * @param dataSource facet
     */
    public void setDataSource(OpenLineageDataSourceDataSetFacet dataSource)
    {
        this.dataSource = dataSource;
    }


    /**
     * Return the schema facet.
     *
     * @return facet
     */
    public OpenLineageSchemaDataSetFacet getSchema()
    {
        return schema;
    }


    /**
     * Set up the schema facet.
     *
     * @param schema facet
     */
    public void setSchema(OpenLineageSchemaDataSetFacet schema)
    {
        this.schema = schema;
    }


    /**
     * Return a map of additional data set facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @return data set facet map (map from string to object)
     */
    public Map<String, OpenLineageDataSetFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up a map of additional data set facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @param additionalProperties data set facet map (map from string to object)
     */
    public void setAdditionalProperties(Map<String, OpenLineageDataSetFacet> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
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
        return Objects.hash(documentation, dataSource, schema, additionalProperties);
    }
}
