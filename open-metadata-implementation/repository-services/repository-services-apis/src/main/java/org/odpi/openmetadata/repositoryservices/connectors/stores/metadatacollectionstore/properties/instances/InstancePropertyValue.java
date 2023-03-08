/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstancePropertyValue provides a common class for holding an instance type and value.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArrayPropertyValue.class, name = "ArrayPropertyValue"),
        @JsonSubTypes.Type(value = EnumPropertyValue.class, name = "EnumPropertyValue"),
        @JsonSubTypes.Type(value = MapPropertyValue.class, name = "MapPropertyValue"),
        @JsonSubTypes.Type(value = PrimitivePropertyValue.class, name = "PrimitivePropertyValue"),
        @JsonSubTypes.Type(value = StructPropertyValue.class, name = "StructPropertyValue")
})
public abstract class InstancePropertyValue extends InstanceElementHeader
{
    private static final long    serialVersionUID = 1L;

    public static final long  CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION = 1;

    /*
     * Common type information that is this is augmented by the subclasses
     */
    private InstancePropertyCategory instancePropertyCategory = null;
    private String                   typeGUID = null;
    private String                   typeName = null;


    /**
     * Default constructor for Jackson
     */
    protected InstancePropertyValue()
    {
    }

    /**
     * Typical constructor initializes the instance property value to nulls.
     *
     * @param instancePropertyCategory InstancePropertyCategory Enum
     */
    protected InstancePropertyValue(InstancePropertyCategory instancePropertyCategory)
    {
        super();
        this.instancePropertyCategory = instancePropertyCategory;
    }


    /**
     * Copy/clone constructor initializes the instance property value from the supplied template.
     *
     * @param template InstancePropertyValue
     */
    protected InstancePropertyValue(InstancePropertyValue template)
    {
        super(template);

        if (template != null)
        {
            this.instancePropertyCategory = template.getInstancePropertyCategory();
            this.typeGUID = template.getTypeGUID();
            this.typeName = template.getTypeName();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of InstancePropertyValue
     */
    public abstract InstancePropertyValue cloneFromSubclass();


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
    protected <K, V extends InstancePropertyValue> Map<K, Object> mapValuesAsObject(Map<K, V> valMap)
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
    protected <K, V extends InstancePropertyValue> Map<K, String> mapValuesAsString(Map<K, V> valMap)
    {
        return convertValues(valMap, entry -> (entry.getValue() == null ? "<null>" : entry.getValue().valueAsString()));
    }


    /**
     * Converts an InstancePropertyValue to the values we need.
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
    private <K, V extends InstancePropertyValue, R> Map<K, R> convertValues(Map<K, V> valMap, Function<Map.Entry<K, V>, R> mapper)
    {
        return Optional.ofNullable(valMap)
                .map(Map::entrySet)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), mapper.apply(e)), HashMap::putAll);
    }


    /**
     * Return the category of this instance property's type.
     *
     * @return TypeDefCategory enum value
     */
    public InstancePropertyCategory getInstancePropertyCategory() {
        return instancePropertyCategory;
    }


    /**
     * Set up the category for this instance property.  This is used on the JSON deserialization.
     *
     * @param instancePropertyCategory new category
     */
    public void setInstancePropertyCategory(InstancePropertyCategory instancePropertyCategory)
    {
        this.instancePropertyCategory = instancePropertyCategory;
    }


    /**
     * Return the unique GUID for the type.
     *
     * @return String unique identifier
     */
    public String getTypeGUID() {
        return typeGUID;
    }


    /**
     * Set up the unique GUID of the type.
     *
     * @param typeGUID String unique identifier
     */
    public void setTypeGUID(String typeGUID) {
        this.typeGUID = typeGUID;
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
    public void setTypeName(String typeName) {
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
        return "InstancePropertyValue{" +
                "instancePropertyCategory=" + instancePropertyCategory +
                ", typeGUID='" + typeGUID + '\'' +
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
        if (! (objectToCompare instanceof InstancePropertyValue))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        InstancePropertyValue that = (InstancePropertyValue) objectToCompare;

        if (instancePropertyCategory != that.instancePropertyCategory)
        {
            return false;
        }
        if (typeGUID != null ? ! typeGUID.equals(that.typeGUID) : that.typeGUID != null)
        {
            return false;
        }
        return typeName != null ? typeName.equals(that.typeName) : that.typeName == null;
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (instancePropertyCategory != null ? instancePropertyCategory.hashCode() : 0);
        result = 31 * result + (typeGUID != null ? typeGUID.hashCode() : 0);
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        return result;
    }
}
