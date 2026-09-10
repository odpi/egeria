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
 * This class represents the dataQualityAssertions input dataset facet.  It captures the results of data quality assertions evaluated against the input dataset.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-1-0/DataQualityAssertionsDatasetFacet.json#/$defs/DataQualityAssertionsDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityAssertionsInputDataSetFacet extends OpenLineageInputDataSetInputFacet
{
    private List<OpenLineageDataQualityAssertionsInputDataSetFacetAssertions> assertions = null;


    /**
     * Default constructor
     */
    public OpenLineageDataQualityAssertionsInputDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-1-0/DataQualityAssertionsDatasetFacet.json#/$defs/DataQualityAssertionsDatasetFacet"));
    }


    /**
     * Return the assertions and their results.
     *
     * @return list
     */
    public List<OpenLineageDataQualityAssertionsInputDataSetFacetAssertions> getAssertions()
    {
        return assertions;
    }


    /**
     * Set up the assertions and their results.
     *
     * @param assertions list
     */
    public void setAssertions(List<OpenLineageDataQualityAssertionsInputDataSetFacetAssertions> assertions)
    {
        this.assertions = assertions;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDataQualityAssertionsInputDataSetFacet{" +
                       "assertions=" + assertions +
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
        OpenLineageDataQualityAssertionsInputDataSetFacet that = (OpenLineageDataQualityAssertionsInputDataSetFacet) objectToCompare;
        return Objects.equals(assertions, that.assertions);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assertions);
    }
}
