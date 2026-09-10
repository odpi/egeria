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
 * This class represents the subset input dataset facet.  It describes the subset (partitions, locations or filter condition) of the dataset that was read.  The condition is a polymorphic tree keyed by its type field, so it is held as a generic map.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/BaseSubsetDatasetFacet.json#/$defs/InputSubsetInputDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageInputSubsetInputDataSetFacet extends OpenLineageInputDataSetInputFacet
{
    private Map<String, Object> inputCondition = null;


    /**
     * Default constructor
     */
    public OpenLineageInputSubsetInputDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/BaseSubsetDatasetFacet.json#/$defs/InputSubsetInputDatasetFacet"));
    }


    /**
     * Return the condition describing the subset read.  The type field of the condition identifies it as a BinarySubsetCondition, CompareSubsetCondition, PartitionSubsetCondition or LocationSubsetCondition.
     *
     * @return map
     */
    public Map<String, Object> getInputCondition()
    {
        return inputCondition;
    }


    /**
     * Set up the condition describing the subset read.  The type field of the condition identifies it as a BinarySubsetCondition, CompareSubsetCondition, PartitionSubsetCondition or LocationSubsetCondition.
     *
     * @param inputCondition map
     */
    public void setInputCondition(Map<String, Object> inputCondition)
    {
        this.inputCondition = inputCondition;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageInputSubsetInputDataSetFacet{" +
                       "inputCondition=" + inputCondition +
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
        OpenLineageInputSubsetInputDataSetFacet that = (OpenLineageInputSubsetInputDataSetFacet) objectToCompare;
        return Objects.equals(inputCondition, that.inputCondition);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), inputCondition);
    }
}
