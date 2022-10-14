/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PropertyComparisonOperator is used for search requests against a metadata store.  It defines how the properties
 * should be compared to find a matching result.
 *
 * The property comparison operator values are:
 * <ul>
 *     <li>
 *         EQUAL: when the property has a value that precisely equals the provided value. This is the default.
 *     </li>
 *     <li>
 *         NOT_EQUAL: when the property has a value that is not equal to the provided value.
 *     </li>
 *     <li>
 *         LESS_THAN: when the property has a value that is strictly less than the provided value.
 *     </li>
 *     <li>
 *         LESS_THAN_OR_EQUAL: when the property has a value that is less than, or equal to, the provided value.
 *     </li>
 *     <li>
 *         GREATER_THAN: when the property has a value that is strictly greater than the provided value.
 *     </li>
 *     <li>
 *         GREATER_THAN_OR_EQUAL: when the property has a value that is greater than, or equal to, the provided value.
 *     </li>
 *     <li>
 *         IN_LIST: when the property has a value that matches at least one element of the provided list of values.
 *     </li>
 *     <li>
 *         IS_NULL: when the property has no value.
 *     </li>
 *     <li>
 *         IS_NOT_NULL: when the property has any non-null value.
 *     </li>
 *     <li>
 *         LIKE: when the property has a value that matches the provided regular expression. This should only be applied
 *         to String properties, and should still be used even for exact matches for String properties.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum PropertyComparisonOperator implements Serializable
{
    EQ        (0, "Equal to",              "Equal to."),
    NEQ       (1, "Not equal to",          "Not equal to."),
    LT        (2, "Less than",             "Less than."),
    LTE       (3, "Less than or equal",    "Less than or equal to."),
    GT        (4, "Greater than",          "Greater than."),
    GTE       (5, "Greater than or equal", "Greater than or equal to."),
    IN        (6, "In list",               "Has a value of at least one in the provided list of values."),
    IS_NULL   (7, "Is null",               "Has no value."),
    NOT_NULL  (8, "Is not null",           "Has any non-null value."),
    LIKE      (9, "Like",                  "Has a value that matches the provided regular expression (strings only).");

    private static final long serialVersionUID = 1L;

    private final int     ordinal;
    private final String  name;
    private final String  description;

    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal numerical representation of the search operator
     * @param name default string name of the search operator
     * @param description default string description of the search operator
     */
    PropertyComparisonOperator(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }

    /**
     * Return the numeric representation of the property comparison operator.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the property comparison operator.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the property comparison operator.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "PropertyComparisonOperator{" + name + "}";
    }

}
