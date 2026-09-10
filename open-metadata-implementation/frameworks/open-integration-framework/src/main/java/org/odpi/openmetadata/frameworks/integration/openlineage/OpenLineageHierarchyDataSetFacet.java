/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the hierarchy dataset facet.  It describes the containment hierarchy of the dataset from the highest level to the lowest, for example DATABASE, SCHEMA, TABLE.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/HierarchyDatasetFacet.json#/$defs/HierarchyDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageHierarchyDataSetFacet extends OpenLineageDataSetFacet
{
    private List<OpenLineageHierarchyDataSetFacetLevel> hierarchy = null;


    /**
     * Default constructor
     */
    public OpenLineageHierarchyDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/HierarchyDatasetFacet.json#/$defs/HierarchyDatasetFacet"));
    }


    /**
     * Return the dataset hierarchy levels, from highest to lowest level.
     *
     * @return list
     */
    public List<OpenLineageHierarchyDataSetFacetLevel> getHierarchy()
    {
        return hierarchy;
    }


    /**
     * Set up the dataset hierarchy levels, from highest to lowest level.
     *
     * @param hierarchy list
     */
    public void setHierarchy(List<OpenLineageHierarchyDataSetFacetLevel> hierarchy)
    {
        this.hierarchy = hierarchy;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageHierarchyDataSetFacet{" +
                       "hierarchy=" + hierarchy +
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
        OpenLineageHierarchyDataSetFacet that = (OpenLineageHierarchyDataSetFacet) objectToCompare;
        return Objects.equals(hierarchy, that.hierarchy);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), hierarchy);
    }
}
