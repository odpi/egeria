/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.search;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Manages a single condition to use as part of an {@link IGCSearch}.
 */
public class IGCSearchCondition {

    private JsonNodeFactory nf = JsonNodeFactory.instance;

    private String property;
    private String operator;
    private String value = null;

    private Boolean negated = null;

    /**
     * Creates a new search condition directly. For example, the following would search for any assets
     * where the "name" is exactly "Account Number":
     * <ul>
     *     <li>property: "name"</li>
     *     <li>operator: "="</li>
     *     <li>value: "Account Number"</li>
     * </ul>
     *
     * @param property the property of an asset type to search against
     * @param operator the comparison operator to use
     * @param value the value to compare the property against
     */
    public IGCSearchCondition(String property, String operator, String value) {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Creates a new search condition directly. For example, the following would search
     * for any assets where the "assigned_to_terms" property is not null:
     * <ul>
     *     <li>property: "assigned_to_terms"</li>
     *     <li>operator: "isNull"</li>
     *     <li>negated: true</li>
     * </ul>"property"
     *
     * @param property the property of an asset type to search against
     * @param operator the comparison operator to use
     * @param negated whether to invert (negate) the comparison operator or not
     */
    public IGCSearchCondition(String property, String operator, Boolean negated) {
        this.property = property;
        this.operator = operator;
        this.negated = negated;
    }

    /**
     * Creates a new search condition directly. For example, the following would search for any assets
     * where the "name" is not exactly "Account Number":
     * <ul>
     *     <li>property: "name"</li>
     *     <li>operator: "="</li>
     *     <li>value: "Account Number"</li>
     *     <li>negated: true</li>
     * </ul>
     *
     * @param property the property of an asset type to search against
     * @param operator the comparison operator to use
     * @param value the value to compare the property against
     * @param negated whether to invert (negate) the comparison operator or not
     */
    public IGCSearchCondition(String property, String operator, String value, Boolean negated) {
        this.property = property;
        this.operator = operator;
        this.value = value;
        this.negated = negated;
    }

    public String getProperty() { return this.property; }
    public void setProperty(String property) { this.property = property; }

    public String getOperator() { return this.operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getValue() { return this.value; }
    public void setValue(String value) { this.value = value; }

    public Boolean getNegated() { return this.negated; }
    public void setNegated(Boolean negated) { this.negated = negated; }

    /**
     * Returns the JSON object representing the condition.
     *
     * @return ObjectNode
     */
    public ObjectNode getConditionObject() {
        ObjectNode condObj = nf.objectNode();
        condObj.set("property", nf.textNode(getProperty()));
        condObj.set("operator", nf.textNode(getOperator()));
        if (this.value != null) {
            condObj.set("value", nf.textNode(getValue()));
        }
        if (this.negated != null) {
            condObj.set("negated", nf.booleanNode(getNegated()));
        }
        return condObj;
    }

}
