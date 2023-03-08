/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a single data quality assertion in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityAssertionsInputDataSetFacetAssertions
{
    private String  assertion = null;
    private boolean success   = false;
    private String  column    = null;


    /**
     * Default constructor
     */
    public OpenLineageDataQualityAssertionsInputDataSetFacetAssertions()
    {
    }


    /**
     * Return the type of assertion that the data set has been tested against.
     *
     * @return string name
     */
    public String getAssertion()
    {
        return assertion;
    }


    /**
     * Set up the type of assertion that the data set has been tested against.
     *
     * @param assertion string name
     */
    public void setAssertion(String assertion)
    {
        this.assertion = assertion;
    }


    /**
     * Return whether the data passed the assertion test.
     *
     * @return boolean flag
     */
    public boolean isSuccess()
    {
        return success;
    }


    /**
     * Set up whether the data passed the assertion test.
     *
     * @param success boolean flag
     */
    public void setSuccess(boolean success)
    {
        this.success = success;
    }


    /**
     * Return the name of the column that the data was located.  This name should match one of the column names in the schema facet.
     *
     * @return string name
     */
    public String getColumn()
    {
        return column;
    }


    /**
     * Set up the name of the column that the data was located.  This name should match one of the column names in the schema facet.
     *
     * @param column string name
     */
    public void setColumn(String column)
    {
        this.column = column;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDataQualityAssertionsInputDataSetFacetAssertions{" +
                       "assertion='" + assertion + '\'' +
                       ", success=" + success +
                       ", column='" + column + '\'' +
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
        OpenLineageDataQualityAssertionsInputDataSetFacetAssertions that = (OpenLineageDataQualityAssertionsInputDataSetFacetAssertions) objectToCompare;
        return success == that.success &&
                       Objects.equals(assertion, that.assertion) &&
                       Objects.equals(column, that.column);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(assertion, success, column);
    }
}
