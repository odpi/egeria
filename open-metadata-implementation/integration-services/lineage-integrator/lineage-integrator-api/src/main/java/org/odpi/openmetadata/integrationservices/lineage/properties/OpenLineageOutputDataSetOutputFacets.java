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
 * This class represents the map of output facets in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageOutputDataSetOutputFacets
{
    private OpenLineageOutputStatisticsOutputDataSetFacet    outputStatistics     = null;
    private Map<String, OpenLineageOutputDataSetOutputFacet> additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageOutputDataSetOutputFacets()
    {
    }


    /**
     * Return statistics facet.
     *
     * @return facet
     */
    public OpenLineageOutputStatisticsOutputDataSetFacet getOutputStatistics()
    {
        return outputStatistics;
    }


    /**
     * Set up statistics facet.
     *
     * @param outputStatistics facet
     */
    public void setOutputStatistics(OpenLineageOutputStatisticsOutputDataSetFacet outputStatistics)
    {
        this.outputStatistics = outputStatistics;
    }


    /**
     * Return a map of additional output facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @return output facet map (map from string to object)
     */
    public Map<String, OpenLineageOutputDataSetOutputFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up a map of additional custom facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @param additionalProperties output facet map (map from string to object)
     */
    public void setAdditionalProperties(Map<String, OpenLineageOutputDataSetOutputFacet> additionalProperties)
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
        return "OpenLineageOutputDataSetOutputFacets{" +
                       "outputStatistics=" + outputStatistics +
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
        OpenLineageOutputDataSetOutputFacets that = (OpenLineageOutputDataSetOutputFacets) objectToCompare;
        return Objects.equals(outputStatistics, that.outputStatistics) &&
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
        return Objects.hash(outputStatistics, additionalProperties);
    }
}
