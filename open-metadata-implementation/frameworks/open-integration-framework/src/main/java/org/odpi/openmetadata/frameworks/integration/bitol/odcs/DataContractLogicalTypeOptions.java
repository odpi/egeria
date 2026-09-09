/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the options that refine the logical type of a schema property in an Open Data Contract
 * Standard (ODCS) data contract. The options that apply depend on the logical type: for example minLength,
 * maxLength, pattern and format apply to strings, minimum and maximum apply to dates, times and numbers, and
 * minItems, maxItems and uniqueItems apply to arrays. This bean holds the union of the options for all logical
 * types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractLogicalTypeOptions
{
    private Integer             minLength = null;
    private Integer             maxLength = null;
    private String              pattern = null;
    private String              format = null;
    private Object              minimum = null;
    private Object              exclusiveMinimum = null;
    private Object              maximum = null;
    private Object              exclusiveMaximum = null;
    private Number              multipleOf = null;
    private Boolean             timezone = null;
    private String              defaultTimezone = null;
    private Integer             minProperties = null;
    private Integer             maxProperties = null;
    private List<String>        required = null;
    private Integer             minItems = null;
    private Integer             maxItems = null;
    private Boolean             uniqueItems = null;
    private Map<String, Object> additionalProperties = null;


    /**
     * Default constructor
     */
    public DataContractLogicalTypeOptions()
    {
    }


    /**
     * Return the minimum length of a string value.
     *
     * @return integer value or null if not specified
     */
    public Integer getMinLength()
    {
        return minLength;
    }


    /**
     * Set up the minimum length of a string value.
     *
     * @param minLength integer value or null if not specified
     */
    public void setMinLength(Integer minLength)
    {
        this.minLength = minLength;
    }


    /**
     * Return the maximum length of a string value.
     *
     * @return integer value or null if not specified
     */
    public Integer getMaxLength()
    {
        return maxLength;
    }


    /**
     * Set up the maximum length of a string value.
     *
     * @param maxLength integer value or null if not specified
     */
    public void setMaxLength(Integer maxLength)
    {
        this.maxLength = maxLength;
    }


    /**
     * Return the regular expression that a string value must match.
     *
     * @return string regular expression
     */
    public String getPattern()
    {
        return pattern;
    }


    /**
     * Set up the regular expression that a string value must match.
     *
     * @param pattern string regular expression
     */
    public void setPattern(String pattern)
    {
        this.pattern = pattern;
    }


    /**
     * Return the format of the value, for example a date/time pattern, or i32/i64/f32/f64 for numbers.
     *
     * @return string format
     */
    public String getFormat()
    {
        return format;
    }


    /**
     * Set up the format of the value, for example a date/time pattern, or i32/i64/f32/f64 for numbers.
     *
     * @param format string format
     */
    public void setFormat(String format)
    {
        this.format = format;
    }


    /**
     * Return the inclusive minimum value (a string for dates and times; a number for numbers).
     *
     * @return minimum value
     */
    public Object getMinimum()
    {
        return minimum;
    }


    /**
     * Set up the inclusive minimum value (a string for dates and times; a number for numbers).
     *
     * @param minimum minimum value
     */
    public void setMinimum(Object minimum)
    {
        this.minimum = minimum;
    }


    /**
     * Return the exclusive minimum value (a string for dates and times; a number for numbers).
     *
     * @return minimum value
     */
    public Object getExclusiveMinimum()
    {
        return exclusiveMinimum;
    }


    /**
     * Set up the exclusive minimum value (a string for dates and times; a number for numbers).
     *
     * @param exclusiveMinimum minimum value
     */
    public void setExclusiveMinimum(Object exclusiveMinimum)
    {
        this.exclusiveMinimum = exclusiveMinimum;
    }


    /**
     * Return the inclusive maximum value (a string for dates and times; a number for numbers).
     *
     * @return maximum value
     */
    public Object getMaximum()
    {
        return maximum;
    }


    /**
     * Set up the inclusive maximum value (a string for dates and times; a number for numbers).
     *
     * @param maximum maximum value
     */
    public void setMaximum(Object maximum)
    {
        this.maximum = maximum;
    }


    /**
     * Return the exclusive maximum value (a string for dates and times; a number for numbers).
     *
     * @return maximum value
     */
    public Object getExclusiveMaximum()
    {
        return exclusiveMaximum;
    }


    /**
     * Set up the exclusive maximum value (a string for dates and times; a number for numbers).
     *
     * @param exclusiveMaximum maximum value
     */
    public void setExclusiveMaximum(Object exclusiveMaximum)
    {
        this.exclusiveMaximum = exclusiveMaximum;
    }


    /**
     * Return the value that a numeric value must be a multiple of.
     *
     * @return numeric value
     */
    public Number getMultipleOf()
    {
        return multipleOf;
    }


    /**
     * Set up the value that a numeric value must be a multiple of.
     *
     * @param multipleOf numeric value
     */
    public void setMultipleOf(Number multipleOf)
    {
        this.multipleOf = multipleOf;
    }


    /**
     * Return whether a timestamp or time value carries a timezone.
     *
     * @return boolean flag or null if not specified
     */
    public Boolean getTimezone()
    {
        return timezone;
    }


    /**
     * Set up whether a timestamp or time value carries a timezone.
     *
     * @param timezone boolean flag or null if not specified
     */
    public void setTimezone(Boolean timezone)
    {
        this.timezone = timezone;
    }


    /**
     * Return the default timezone for a timestamp or time value (for example Etc/UTC).
     *
     * @return string timezone
     */
    public String getDefaultTimezone()
    {
        return defaultTimezone;
    }


    /**
     * Set up the default timezone for a timestamp or time value (for example Etc/UTC).
     *
     * @param defaultTimezone string timezone
     */
    public void setDefaultTimezone(String defaultTimezone)
    {
        this.defaultTimezone = defaultTimezone;
    }


    /**
     * Return the minimum number of properties in an object value.
     *
     * @return integer value or null if not specified
     */
    public Integer getMinProperties()
    {
        return minProperties;
    }


    /**
     * Set up the minimum number of properties in an object value.
     *
     * @param minProperties integer value or null if not specified
     */
    public void setMinProperties(Integer minProperties)
    {
        this.minProperties = minProperties;
    }


    /**
     * Return the maximum number of properties in an object value.
     *
     * @return integer value or null if not specified
     */
    public Integer getMaxProperties()
    {
        return maxProperties;
    }


    /**
     * Set up the maximum number of properties in an object value.
     *
     * @param maxProperties integer value or null if not specified
     */
    public void setMaxProperties(Integer maxProperties)
    {
        this.maxProperties = maxProperties;
    }


    /**
     * Return the names of the properties that must be present in an object value.
     *
     * @return list of property names
     */
    public List<String> getRequired()
    {
        return required;
    }


    /**
     * Set up the names of the properties that must be present in an object value.
     *
     * @param required list of property names
     */
    public void setRequired(List<String> required)
    {
        this.required = required;
    }


    /**
     * Return the minimum number of items in an array value.
     *
     * @return integer value or null if not specified
     */
    public Integer getMinItems()
    {
        return minItems;
    }


    /**
     * Set up the minimum number of items in an array value.
     *
     * @param minItems integer value or null if not specified
     */
    public void setMinItems(Integer minItems)
    {
        this.minItems = minItems;
    }


    /**
     * Return the maximum number of items in an array value.
     *
     * @return integer value or null if not specified
     */
    public Integer getMaxItems()
    {
        return maxItems;
    }


    /**
     * Set up the maximum number of items in an array value.
     *
     * @param maxItems integer value or null if not specified
     */
    public void setMaxItems(Integer maxItems)
    {
        this.maxItems = maxItems;
    }


    /**
     * Return whether the items of an array value must be unique.
     *
     * @return boolean flag or null if not specified
     */
    public Boolean getUniqueItems()
    {
        return uniqueItems;
    }


    /**
     * Set up whether the items of an array value must be unique.
     *
     * @param uniqueItems boolean flag or null if not specified
     */
    public void setUniqueItems(Boolean uniqueItems)
    {
        this.uniqueItems = uniqueItems;
    }


    /**
     * Return any options that are not explicitly modelled by this bean.
     *
     * @return map from property name to value
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any options that are not explicitly modelled by this bean.
     *
     * @param additionalProperties map from property name to value
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Add a single property that is not explicitly modelled by this bean.  Called by Jackson for each unrecognized property
     * so that no part of the document is lost on a round trip.
     *
     * @param propertyName name of the property
     * @param propertyValue value of the property
     */
    @JsonAnySetter
    public void setAdditionalProperty(String propertyName,
                                      Object propertyValue)
    {
        if (additionalProperties == null)
        {
            additionalProperties = new HashMap<>();
        }

        additionalProperties.put(propertyName, propertyValue);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractLogicalTypeOptions{" +
                       "minLength=" + minLength +
                       ", maxLength=" + maxLength +
                       ", pattern='" + pattern + '\'' +
                       ", format='" + format + '\'' +
                       ", minimum=" + minimum +
                       ", exclusiveMinimum=" + exclusiveMinimum +
                       ", maximum=" + maximum +
                       ", exclusiveMaximum=" + exclusiveMaximum +
                       ", multipleOf=" + multipleOf +
                       ", timezone=" + timezone +
                       ", defaultTimezone='" + defaultTimezone + '\'' +
                       ", minProperties=" + minProperties +
                       ", maxProperties=" + maxProperties +
                       ", required=" + required +
                       ", minItems=" + minItems +
                       ", maxItems=" + maxItems +
                       ", uniqueItems=" + uniqueItems +
                       ", additionalProperties=" + additionalProperties +
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
        DataContractLogicalTypeOptions that = (DataContractLogicalTypeOptions) objectToCompare;
        return Objects.equals(minLength, that.minLength) &&
                       Objects.equals(maxLength, that.maxLength) &&
                       Objects.equals(pattern, that.pattern) &&
                       Objects.equals(format, that.format) &&
                       Objects.equals(minimum, that.minimum) &&
                       Objects.equals(exclusiveMinimum, that.exclusiveMinimum) &&
                       Objects.equals(maximum, that.maximum) &&
                       Objects.equals(exclusiveMaximum, that.exclusiveMaximum) &&
                       Objects.equals(multipleOf, that.multipleOf) &&
                       Objects.equals(timezone, that.timezone) &&
                       Objects.equals(defaultTimezone, that.defaultTimezone) &&
                       Objects.equals(minProperties, that.minProperties) &&
                       Objects.equals(maxProperties, that.maxProperties) &&
                       Objects.equals(required, that.required) &&
                       Objects.equals(minItems, that.minItems) &&
                       Objects.equals(maxItems, that.maxItems) &&
                       Objects.equals(uniqueItems, that.uniqueItems) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(minLength, maxLength, pattern, format, minimum, exclusiveMinimum, maximum, exclusiveMaximum, multipleOf, timezone, defaultTimezone, minProperties, maxProperties, required, minItems, maxItems, uniqueItems, additionalProperties);
    }
}
