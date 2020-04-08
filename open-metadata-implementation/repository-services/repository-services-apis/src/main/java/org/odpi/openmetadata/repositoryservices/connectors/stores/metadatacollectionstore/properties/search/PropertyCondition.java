/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The PropertyCondition class provides support for searching against a single property using a single comparison
 * mechanism, or for nesting further SearchProperties.
 *
 * The use of {@literal nestedConditions} is mutually exclusive with the use of the {@literal property},
 * {@literal operator}, {@literal value} construct.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PropertyCondition implements Serializable
{

    private static final long    serialVersionUID = 1L;

    private String property;
    private PropertyComparisonOperator operator;
    private InstancePropertyValue value;
    private SearchProperties nestedConditions;

    /**
     * Typical constructor
     */
    public PropertyCondition()
    {
        super();
    }

    /**
     * Copy/clone constructor.
     *
     * @param templateProperties template object to copy.
     */
    public PropertyCondition(PropertyCondition templateProperties)
    {
        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            this.property = templateProperties.getProperty();
            this.operator = templateProperties.getOperator();
            this.value = templateProperties.getValue();
        }
    }

    /**
     * Retrieve the name of the property being compared.
     *
     * @return String
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * Set the name of the property to compare.
     *
     * @param property name of the property to compare
     */
    public void setProperty(String property)
    {
        this.property = property;
    }

    /**
     * Retrieve the operator to use for the comparison.
     *
     * @return SearchOperator
     */
    public PropertyComparisonOperator getOperator()
    {
        return operator;
    }

    /**
     * Set the operator to use for the comparison.
     *
     * @param operator to use for the comparison
     */
    public void setOperator(PropertyComparisonOperator operator)
    {
        this.operator = operator;
    }

    /**
     * Retrieve the value of the property against which to compare.
     *
     * @return InstancePropertyValue
     */
    public InstancePropertyValue getValue()
    {
        return value;
    }

    /**
     * Set the value of hte property against which to compare.
     *
     * @param value against which to compare
     */
    public void setValue(InstancePropertyValue value)
    {
        this.value = value;
    }

    /**
     * Retrieve any nested conditions.
     *
     * @return SearchProperties
     */
    public SearchProperties getNestedConditions()
    {
        return nestedConditions;
    }

    /**
     * Set any nested conditions.
     *
     * @param nestedConditions conditions to nest
     */
    public void setNestedConditions(SearchProperties nestedConditions)
    {
        this.nestedConditions = nestedConditions;
    }

}
