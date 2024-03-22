/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PropertyValue provides a common class for holding an instance type and value.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArrayTypePropertyValue.class, name = "ArrayTypePropertyValue"),
        @JsonSubTypes.Type(value = EnumTypePropertyValue.class, name = "EnumTypePropertyValue"),
        @JsonSubTypes.Type(value = MapTypePropertyValue.class, name = "MapTypePropertyValue"),
        @JsonSubTypes.Type(value = PrimitiveTypePropertyValue.class, name = "PrimitiveTypePropertyValue"),
        @JsonSubTypes.Type(value = StructTypePropertyValue.class, name = "StructTypePropertyValue")
})
public abstract class PropertyValue
{
    /*
     * Common type information that is this is augmented by the subclasses
     */
    private String           typeName         = null;


    /**
     * Default constructor for Jackson
     */
    protected PropertyValue()
    {
    }

    /**
     * Copy/clone constructor initializes the instance property value from the supplied template.
     *
     * @param template PropertyValue
     */
    protected PropertyValue(PropertyValue template)
    {
        if (template != null)
        {
            this.typeName = template.getTypeName();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of PropertyValue
     */
    public abstract PropertyValue cloneFromSubclass();


    /**
     * Return the string version of the value - used for error logging.
     *
     * @return string value
     */
    public abstract String valueAsString();


    /**
     * Return the object version of the value - used for comparisons.
     *
     * @return object value
     */
    public abstract Object valueAsObject();


    /**
     * Default method for "valueAsString".
     * Return the object version of the value - used for comparisons.
     *
     * @param valMap mapping
     * @param <K> key
     * @param <V> value
     * @return Map object values
     */
    protected <K, V extends PropertyValue> Map<K, Object> mapValuesAsObject(Map<K, V> valMap)
    {
        return convertValues(valMap, entry -> (entry.getValue() == null ? null : entry.getValue().valueAsObject()));
    }


    /**
     * Default method for "valueAsObject".
     * Return the object version of the value - used for comparisons.
     *
     * @param valMap mapping
     * @param <K> key
     * @param <V> value
     * @return Map string values
     */
    protected <K, V extends PropertyValue> Map<K, String> mapValuesAsString(Map<K, V> valMap)
    {
        return convertValues(valMap, entry -> (entry.getValue() == null ? "<null>" : entry.getValue().valueAsString()));
    }


    /**
     * Converts an PropertyValue to the values we need.
     * Object, String or whatever.
     *
     * @param valMap values
     * @param mapper converter
     * @param <K> key
     * @param <V> value
     * @param <R> type for return Map values.
     *           For example, types Object {@link #mapValuesAsObject} or String {@link #mapValuesAsString}.
     * @return Map with new values
     */
    private <K, V extends PropertyValue, R> Map<K, R> convertValues(Map<K, V> valMap, Function<Map.Entry<K, V>, R> mapper)
    {
        return Optional.ofNullable(valMap)
                .map(Map::entrySet)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), mapper.apply(e)), HashMap::putAll);
    }


    /**
     * Return the name of the type.
     *
     * @return String type name
     */
    public String getTypeName() {
        return typeName;
    }


    /**
     * Set up the name of the type.
     *
     * @param typeName String type name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "PropertyValue{" +
                ", typeName='" + typeName + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof PropertyValue))
        {
            return false;
        }
        PropertyValue that = (PropertyValue) objectToCompare;
        return Objects.equals(getTypeName(), that.getTypeName());
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTypeName());
    }
}
