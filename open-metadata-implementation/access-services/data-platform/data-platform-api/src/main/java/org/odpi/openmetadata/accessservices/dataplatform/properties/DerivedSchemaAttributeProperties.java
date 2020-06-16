/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DerivedSchemaAttributeProperties represents a schema attribute that is derived from other data values.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class DerivedSchemaAttributeProperties extends SchemaAttributeProperties
{
    private static final long     serialVersionUID = 1L;

    private String  formula = null;


    /**
     * Default constructor
     */
    public DerivedSchemaAttributeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DerivedSchemaAttributeProperties(DerivedSchemaAttributeProperties template)
    {
        super(template);

        if (template != null)
        {
            formula = template.getFormula();
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DerivedSchemaAttributeProperties{" +
                "formula='" + formula + '\'' +
                ", elementPosition=" + getElementPosition() +
                ", minCardinality=" + getMinCardinality() +
                ", maxCardinality=" + getMaxCardinality() +
                ", allowsDuplicateValues=" + isAllowsDuplicateValues() +
                ", orderedValues=" + isOrderedValues() +
                ", sortOrder=" + getSortOrder() +
                ", minimumLength=" + getMinimumLength() +
                ", length=" + getLength() +
                ", significantDigits=" + getSignificantDigits() +
                ", nullable=" + isNullable() +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", anchorGUID='" + getAnchorGUID() + '\'' +
                ", nativeJavaClass='" + getNativeJavaClass() + '\'' +
                ", aliases=" + getAliases() +
                ", deprecated=" + isDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
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
        DerivedSchemaAttributeProperties that = (DerivedSchemaAttributeProperties) objectToCompare;
        return Objects.equals(formula, that.formula);
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), formula);
    }
}
