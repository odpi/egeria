/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the data quality assertions facet in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
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
    }


    /**
     * Return the list of assertions that the data set has been tested against.
     *
     * @return list of assertions
     */
    public List<OpenLineageDataQualityAssertionsInputDataSetFacetAssertions> getAssertions()
    {
        return assertions;
    }


    /**
     * Set up the list of assertions that the data set has been tested against.
     *
     * @param assertions list of assertions
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
