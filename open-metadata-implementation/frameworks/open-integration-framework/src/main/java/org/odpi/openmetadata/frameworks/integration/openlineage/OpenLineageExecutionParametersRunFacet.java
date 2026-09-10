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
 * This class represents the executionParameters run facet.  It captures the parameters passed to the job at runtime.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/ExecutionParametersRunFacet.json#/$defs/ExecutionParametersRunFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageExecutionParametersRunFacet extends OpenLineageRunFacet
{
    private List<OpenLineageExecutionParametersRunFacetParameter> parameters = null;


    /**
     * Default constructor
     */
    public OpenLineageExecutionParametersRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/ExecutionParametersRunFacet.json#/$defs/ExecutionParametersRunFacet"));
    }


    /**
     * Return the parameters passed to the job at runtime.
     *
     * @return list
     */
    public List<OpenLineageExecutionParametersRunFacetParameter> getParameters()
    {
        return parameters;
    }


    /**
     * Set up the parameters passed to the job at runtime.
     *
     * @param parameters list
     */
    public void setParameters(List<OpenLineageExecutionParametersRunFacetParameter> parameters)
    {
        this.parameters = parameters;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageExecutionParametersRunFacet{" +
                       "parameters=" + parameters +
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
        OpenLineageExecutionParametersRunFacet that = (OpenLineageExecutionParametersRunFacet) objectToCompare;
        return Objects.equals(parameters, that.parameters);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), parameters);
    }
}
