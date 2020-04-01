/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.DerivedSchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaImplementationQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DerivedSchemaAttributeProperties carries the additional parameters for creating or updating derived schema attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DerivedSchemaAttributeProperties extends SchemaAttributeProperties
{
    private static final long    serialVersionUID = 1L;

    protected String                          formula = null;
    protected List<SchemaImplementationQuery> queries = null;



    /**
     * Default constructor
     */
    public DerivedSchemaAttributeProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public DerivedSchemaAttributeProperties(DerivedSchemaAttributeProperties template)
    {
        super(template);

        if (template != null)
        {
            formula = template.getFormula();
            queries = template.getQueries();
        }
    }


    /**
     * Copy/clone operator.
     *
     * @param objectToFill schema attribute object
     * @return filled object
     */
    public DerivedSchemaAttribute cloneProperties(DerivedSchemaAttribute objectToFill)
    {
        DerivedSchemaAttribute clone = objectToFill;

        if (clone == null)
        {
            clone = new DerivedSchemaAttribute();
        }

        clone.setFormula(this.getFormula());
        clone.setQueries(this.getQueries());

        super.cloneProperties(clone);

        return clone;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DerivedSchemaAttributeProperties{" +
                "formula='" + formula + '\'' +
                ", queries=" + queries +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", deprecated=" + isDeprecated() +
                ", typeName='" + getTypeName() + '\'' +
                ", classifications=" + getClassifications() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
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
        DerivedSchemaAttributeProperties that = (DerivedSchemaAttributeProperties) objectToCompare;
        return Objects.equals(formula, that.formula) &&
                Objects.equals(queries, that.queries);
    }
}