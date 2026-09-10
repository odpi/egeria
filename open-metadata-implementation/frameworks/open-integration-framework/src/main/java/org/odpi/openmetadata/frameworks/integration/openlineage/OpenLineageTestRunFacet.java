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
 * This class represents the test run facet.  It captures the tests executed during the run and their results.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/TestRunFacet.json#/$defs/TestRunFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageTestRunFacet extends OpenLineageRunFacet
{
    private List<OpenLineageTestRunFacetTestExecution> tests = null;


    /**
     * Default constructor
     */
    public OpenLineageTestRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/TestRunFacet.json#/$defs/TestRunFacet"));
    }


    /**
     * Return the test executions and their results.
     *
     * @return list
     */
    public List<OpenLineageTestRunFacetTestExecution> getTests()
    {
        return tests;
    }


    /**
     * Set up the test executions and their results.
     *
     * @param tests list
     */
    public void setTests(List<OpenLineageTestRunFacetTestExecution> tests)
    {
        this.tests = tests;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageTestRunFacet{" +
                       "tests=" + tests +
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
        OpenLineageTestRunFacet that = (OpenLineageTestRunFacet) objectToCompare;
        return Objects.equals(tests, that.tests);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), tests);
    }
}
