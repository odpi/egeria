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
 * This class represents the map of run facets in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageRunFacets
{
    private OpenLineageParentRunFacet            parent = null;
    private OpenLineageNominalTimeRunFacet       nominalTime = null;
    private Map<String, OpenLineageDataSetFacet> additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageRunFacets()
    {
    }


    /**
     * Return details of the parent process.
     *
     * @return parent facet
     */
    public OpenLineageParentRunFacet getParent()
    {
        return parent;
    }


    /**
     * Set up details of the parent process.
     *
     * @param parent parent facet
     */
    public void setParent(OpenLineageParentRunFacet parent)
    {
        this.parent = parent;
    }


    /**
     * Return the nominal time.
     *
     * @return facet
     */
    public OpenLineageNominalTimeRunFacet getNominalTime()
    {
        return nominalTime;
    }


    /**
     * Set up the nominal time.
     *
     * @param nominalTime facet
     */
    public void setNominalTime(OpenLineageNominalTimeRunFacet nominalTime)
    {
        this.nominalTime = nominalTime;
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
        return "OpenLineageRunFacets{" +
                       "parent=" + parent +
                       ", nominalTime=" + nominalTime +
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
        OpenLineageRunFacets that = (OpenLineageRunFacets) objectToCompare;
        return Objects.equals(parent, that.parent) &&
                       Objects.equals(nominalTime, that.nominalTime) &&
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
        return Objects.hash(parent, nominalTime, additionalProperties);
    }
}
