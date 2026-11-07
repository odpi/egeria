/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PropertyComparisonOperator is used for search requests against a metadata collection.  It defines how the properties
 * should be compared to find a matching result.
 * The property comparison operator values are:
 * <ul>
 *     <li>
 *         EQ: when the property has a value that precisely equals the provided value. This is the default.
 *     </li>
 *     <li>
 *         NEQ: when the property has a value that is not equal to the provided value.
 *     </li>
 *     <li>
 *         LT: when the property has a value that is strictly less than the provided value.
 *     </li>
 *     <li>
 *         LTE: when the property has a value that is less than, or equal to, the provided value.
 *     </li>
 *     <li>
 *         GT: when the property has a value that is strictly greater than the provided value.
 *     </li>
 *     <li>
 *         GTE: when the property has a value that is greater than, or equal to, the provided value.
 *     </li>
 *     <li>
 *         IN: when the property has a value that equals at least one element of the provided list of values (an
 *         ArrayPropertyValue).  For a property that is itself an array, it is enough that any one of its elements
 *         equals one of the listed values - so IN tests whether the two lists share a value.
 *     </li>
 *     <li>
 *         IS_NULL: when the property has no value.
 *     </li>
 *     <li>
 *         NOT_NULL: when the property has any non-null value.
 *     </li>
 *     <li>
 *         LIKE: when the property has a value that matches the provided value.
 *     </li>
 *     <LI>
 *         NOT_LIKE: when the property has a value that does not match the provided value.
 *     </LI>
 *     <LI>
 *         CASE_INSENSITIVE_LIKE: when the property has a value that matches the provided value if the case is ignored.
 *     </LI>
 *     <LI>
 *         CASE_INSENSITIVE_NOT_LIKE: when the property has a value that does not match the provided value even if the case is ignored.
 *     </LI>
 *     <LI>
 *         STARTS_WITH: when the property has a value that starts with the provided value.
 *     </LI>
 *     <LI>
 *         ENDS_WITH: when the property has a value that ends with the provided value.
 *     </LI>
 *     <LI>
 *         CASE_INSENSITIVE_STARTS_WITH: when the property has a value that starts with the provided value if the case is ignored.
 *     </LI>
 *     <LI>
 *         CASE_INSENSITIVE_ENDS_WITH: when the property has a value that ends with the provided value if the case is ignored.
 *     </LI>
 *     <LI>
 *         CASE_INSENSITIVE_EQ: when the property has a value that matches the provided value if the case is ignored.
 *     </LI>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum PropertyComparisonOperator
{
    /**
     * Equal to
     */
    EQ(0, "Equal to", "Equal to."),

    /**
     * Not equal to
     */
    NEQ(1, "Not equal to", "Not equal to."),

    /**
     * Less than
     */
    LT(2, "Less than", "Less than."),

    /**
     * Less than or equal
     */
    LTE(3, "Less than or equal", "Less than or equal to."),

    /**
     * Greater than
     */
    GT(4, "Greater than", "Greater than."),

    /**
     * Greater than or equal
     */
    GTE(5, "Greater than or equal", "Greater than or equal to."),

    /**
     * In list - has a value equal to at least one in the provided list of values.  An array property matches if
     * any of its elements does.
     */
    IN(6, "In list", "Has a value equal to at least one in the provided list of values.  An array property matches if any of its elements does."),

    /**
     * Is null
     */
    IS_NULL(7, "Is null", "Has no value."),

    /**
     * Is not null - Has any non-null value.
     */
    NOT_NULL(8, "Is not null", "Has any non-null value."),

    /**
     * Has a value that includes the provided string.
     */
    LIKE(9, "Like", "Has a value that includes the provided string.."),

    /**
     * Has a value that does not include the provided string.
     */
    NOT_LIKE(10, "Not Like", "Has a value that does not include the provided string."),

    /**
     * Has a value that includes the provided string if the case is ignored.
     */
    CASE_INSENSITIVE_LIKE(11, "Case Insensitive Like", "Has a value that includes the provided string if the case is ignored."),

    /**
     * Has a value that does not include the provided string even if the case is ignored.
     */
    CASE_INSENSITIVE_NOT_LIKE(12, "Case Insensitive Not Like", "Has a value that does not include the provided string even if the case is ignored."),

    /**
     * Has a value that begins with the provided string.
     */
    STARTS_WITH(13, "Starts With", "Has a value that begins with the provided string.."),

    /**
     * Has a value that ends with the provided string.
     */
    ENDS_WITH(14, "Ends With", "Has a value that ends with the provided string."),

    /**
     * Has a value that starts with the provided string if the case is ignored.
     */
    CASE_INSENSITIVE_STARTS_WITH(15, "Case Insensitive Ends With", "Has a value that starts with the provided string if the case is ignored."),

    /**
     * Has a value that ends with the provided string if the case is ignored.
     */
    CASE_INSENSITIVE_ENDS_WITH(16, "Case Insensitive Ends With", "Has a value that ends with the provided string if the case is ignored."),

    /**
     * Has a value that equals the provided string if the case is ignored.
     */
    CASE_INSENSITIVE_EQ(17, "Case Insensitive Equals", "Has a value that equals the provided string if the case is ignored."),

    ;


    private final int    ordinal;
    private final String name;
    private final String description;

    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal     numerical representation of the search operator
     * @param name        default string name of the search operator
     * @param description default string description of the search operator
     */
    PropertyComparisonOperator(int ordinal, String name, String description)
    {
        this.ordinal     = ordinal;
        this.name        = name;
        this.description = description;
    }

    /**
     * Return the numeric representation of the property comparison operator.
     *
     * @return int ordinal
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name of the property comparison operator.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description of the property comparison operator.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "PropertyComparisonOperator{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
