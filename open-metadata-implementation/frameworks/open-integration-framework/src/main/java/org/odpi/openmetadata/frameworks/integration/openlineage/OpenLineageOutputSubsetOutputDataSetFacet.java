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
 * This class represents the subset output dataset facet.  It describes the subset (partitions, locations or filter condition) of the dataset that was written.  The condition is a polymorphic tree keyed by its type field, so it is held as a generic map.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/BaseSubsetDatasetFacet.json#/$defs/OutputSubsetOutputDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageOutputSubsetOutputDataSetFacet extends OpenLineageOutputDataSetOutputFacet
{
    private Map<String, Object> outputCondition = null;


    /**
     * Default constructor
     */
    public OpenLineageOutputSubsetOutputDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/BaseSubsetDatasetFacet.json#/$defs/OutputSubsetOutputDatasetFacet"));
    }


    /**
     * Return the condition describing the subset written.  The type field of the condition identifies it as a BinarySubsetCondition, CompareSubsetCondition, PartitionSubsetCondition or LocationSubsetCondition.
     *
     * @return map
     */
    public Map<String, Object> getOutputCondition()
    {
        return outputCondition;
    }


    /**
     * Set up the condition describing the subset written.  The type field of the condition identifies it as a BinarySubsetCondition, CompareSubsetCondition, PartitionSubsetCondition or LocationSubsetCondition.
     *
     * @param outputCondition map
     */
    public void setOutputCondition(Map<String, Object> outputCondition)
    {
        this.outputCondition = outputCondition;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageOutputSubsetOutputDataSetFacet{" +
                       "outputCondition=" + outputCondition +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
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
        OpenLineageOutputSubsetOutputDataSetFacet that = (OpenLineageOutputSubsetOutputDataSetFacet) objectToCompare;
        return Objects.equals(outputCondition, that.outputCondition);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), outputCondition);
    }
}
