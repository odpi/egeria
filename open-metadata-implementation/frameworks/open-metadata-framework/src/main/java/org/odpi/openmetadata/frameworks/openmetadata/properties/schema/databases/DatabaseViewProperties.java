/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseViewProperties is a class for representing a relational database view.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseViewProperties extends DatabaseTableProperties
{
    private String                        formula = null;
    private List<DatabaseQueryProperties> queries = null;


    /**
     * Default constructor
     */
    public DatabaseViewProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DatabaseViewProperties(DatabaseViewProperties template)
    {
        super(template);

        if (template != null)
        {
            formula = template.getFormula();
            queries = template.getQueries();
        }
    }


    /**
     * Return the formula used to combine the values of the queries.  Each query is has a identifier and the
     * formula has placeholders for these identifiers in it to show how the query results are combined.
     *
     * @return String formula
     */
    public String getFormula() { return formula; }


    /**
     * Set up the formula used to combine the values of the queries.  Each query is has a identifier and the
     * formula has placeholders for these identifiers in it to show how the query results are combined.
     *
     * @param formula String formula
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
    }


    /**
     * Return the list of individual query targets for a derived column.
     *
     * @return list of queries and their target element
     */
    public List<DatabaseQueryProperties> getQueries()
    {
        return queries;
    }


    /**
     * Set up the list of individual query targets for a derived column.
     *
     * @param queries list of queries and their target element
     */
    public void setQueries(List<DatabaseQueryProperties> queries)
    {
        this.queries = queries;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DatabaseViewProperties{" +
                       "formula='" + formula + '\'' +
                       ", queries=" + queries +
                       ", deprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", aliases='" + getAliases() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DatabaseViewProperties that = (DatabaseViewProperties) objectToCompare;
        return Objects.equals(formula, that.formula) &&
                Objects.equals(queries, that.queries);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), formula, queries);
    }
}
