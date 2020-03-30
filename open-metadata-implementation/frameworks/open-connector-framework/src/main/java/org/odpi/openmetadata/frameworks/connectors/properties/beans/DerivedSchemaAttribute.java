/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Derived schema elements are used in views to define elements that are calculated using data from other sources.
 * It contains a list of queries and a formula to combine the resulting values.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DerivedSchemaAttribute extends SchemaAttribute
{
    private static final long     serialVersionUID = 1L;

    protected String                          formula = null;
    protected List<SchemaImplementationQuery> queries = null;


    /**
     * Default constructor
     */
    public DerivedSchemaAttribute()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DerivedSchemaAttribute(DerivedSchemaAttribute template)
    {
        super(template);

        if (template != null)
        {
            formula = template.getFormula();
            queries = template.getQueries();
        }
    }


    /**
     * Return the formula used to combine the values of the queries.  Each query is numbers 0, 1, ... and the
     * formula has placeholders in it to show how the query results are combined.
     *
     * @return String formula
     */
    public String getFormula() { return formula; }


    /**
     * Set up the formula used to combine the values of the queries.  Each query is numbers 0, 1, ... and the
     * formula has placeholders in it to show how the query results are combined.
     *
     * @param formula String formula
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
    }


    /**
     * Return the list of queries that are used to create the derived schema element.
     *
     * @return list of queries
     */
    public List<SchemaImplementationQuery> getQueries()
    {
        if (queries == null)
        {
            return null;
        }
        else if (queries.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(queries);
        }
    }


    /**
     * Set up the list of queries that are used to create the derived schema element.
     *
     * @param queries list of queries
     */
    public void setQueries(List<SchemaImplementationQuery> queries)
    {
        this.queries = queries;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement object
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new DerivedSchemaAttribute(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DerivedSchemaAttribute{" +
                "formula='" + formula + '\'' +
                ", queries=" + queries +
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
        if (!(objectToCompare instanceof DerivedSchemaAttribute))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DerivedSchemaAttribute that = (DerivedSchemaAttribute) objectToCompare;
        return Objects.equals(getFormula(), that.getFormula()) &&
                Objects.equals(getQueries(), that.getQueries());
    }
}