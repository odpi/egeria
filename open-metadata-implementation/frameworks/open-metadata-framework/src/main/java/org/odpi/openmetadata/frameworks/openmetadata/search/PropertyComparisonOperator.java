/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.OpenMetadataRefData;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PropertyComparisonOperator is used for search requests against a metadata store.  It defines how the properties
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
public enum PropertyComparisonOperator implements OpenMetadataRefData
{
    /**
     * Equal to.
     */
    EQ(0, "Equal to", "Equal to.", "718bc029-0308-4f33-86c8-38a84989b494"),

    /**
     * Not equal to.
     */
    NEQ(1, "Not equal to", "Not equal to.", "d01b33c0-fd2b-4b51-9207-86a5a2dd9c2f"),

    /**
     * Less than.
     */
    LT(2, "Less than", "Less than.", "a750c5ba-7522-414f-a20d-ff6ca65422a5"),

    /**
     * Less than or equal to.
     */
    LTE(3, "Less than or equal", "Less than or equal to.", "3386c7fe-dd12-4626-980b-8bef02a6c7e3"),

    /**
     * Greater than.
     */
    GT(4, "Greater than", "Greater than.", "7ab62986-73be-47c4-9c18-85637f412fdb"),

    /**
     * Greater than or equal to.
     */
    GTE(5, "Greater than or equal", "Greater than or equal to.", "9f83955d-8bb1-4934-942a-b7464728bd31"),

    /**
     * Has a value equal to at least one in the provided list of values.  An array property matches if any of
     * its elements does.
     */
    IN(6, "In list", "Has a value equal to at least one in the provided list of values.  An array property matches if any of its elements does.", "6071271c-c845-48c7-8890-bd755736925d"),

    /**
     * Has no value.
     */
    IS_NULL(7, "Is null", "Has no value.", "48ed1744-b601-485d-9cd3-d4f999696d39"),

    /**
     * Has any non-null value.
     */
    NOT_NULL(8, "Is not null", "Has any non-null value.", "f36ea504-d1bb-41eb-a9f7-8a64bc72dc44"),

    /**
     * Has a value that includes the provided string.
     */
    LIKE(9, "Like", "Has a value that includes the provided string.", "66b0484c-ff91-4e5d-a586-b3162d4927d3"),

    /**
     * Has a value that does not include the provided string.
     */
    NOT_LIKE(10, "Not Like", "Has a value that does not include the provided string.", "bc7587df-551d-425f-ba33-3700b9aa0531"),

    /**
     * Has a value that includes the provided string if the case is ignored.
     */
    CASE_INSENSITIVE_LIKE(11, "Case Insensitive Like", "Has a value that includes the provided string if the case is ignored.", "8d27174a-d415-422f-9b34-354c3363f3db"),

    /**
     * Has a value that does not include the provided string even if the case is ignored.
     */
    CASE_INSENSITIVE_NOT_LIKE(12, "Case Insensitive Not Like", "Has a value that does not include the provided string even if the case is ignored.", "a8078232-a97a-49cf-9431-6e5ba1eb924d"),

    /**
     * Has a value that begins with the provided string.
     */
    STARTS_WITH(13, "Starts With", "Has a value that begins with the provided string.", "7cbb90f3-9e4e-42d2-acd5-dec46f9dc605"),

    /**
     * Has a value that ends with the provided string.
     */
    ENDS_WITH(14, "Ends With", "Has a value that ends with the provided string.", "366b236d-9f74-4313-98ee-d5f7df7a6eeb"),

    /**
     * Has a value that starts with the provided string if the case is ignored.
     */
    CASE_INSENSITIVE_STARTS_WITH(15, "Case Insensitive Ends With", "Has a value that starts with the provided string if the case is ignored.", "26573927-90f7-418d-a9ea-a605da09048b"),

    /**
     * Has a value that ends with the provided string if the case is ignored.
     */
    CASE_INSENSITIVE_ENDS_WITH(16, "Case Insensitive Ends With", "Has a value that ends with the provided string if the case is ignored.", "3c84b555-f459-457b-849e-874dd82a3b59"),

    /**
     * Has a value that equals the provided string if the case is ignored.
     */
    CASE_INSENSITIVE_EQ(17, "Case Insensitive Equals", "Has a value that equals the provided string if the case is ignored.", "6a8097c8-4c8f-4efc-8170-7684e370da15"),

    ;

    private final int    ordinal;
    private final String name;
    private final String description;
    private final String descriptionGUID;

    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal     numerical representation of the search operator
     * @param name        default string name of the search operator
     * @param description default string description of the search operator
     */
    PropertyComparisonOperator(int ordinal, String name, String description, String descriptionGUID)
    {
        this.ordinal     = ordinal;
        this.name        = name;
        this.description = description;
        this.descriptionGUID = descriptionGUID;
    }

    /**
     * Return the numeric representation of the property comparison operator.
     *
     * @return int ordinal
     */
    @Override
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name of the property comparison operator.
     *
     * @return String name
     */
    @Override
    public String getDisplayName()
    {
        return name;
    }


    /**
     * Return the default description of the property comparison operator.
     *
     * @return String description
     */
    @Override
    public String getDescription()
    {
        return description;
    }

    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }

    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
    public boolean isDefault()
    {
        return false;
    }


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
