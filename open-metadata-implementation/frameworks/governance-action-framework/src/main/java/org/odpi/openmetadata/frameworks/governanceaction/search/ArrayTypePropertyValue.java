/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFRuntimeException;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ArrayTypePropertyValue stores the values of an array within an entity or relationship properties.
 * The elements of the array are stored in an ElementProperties map where the property name is set to the element
 * number and the property value is set to the value of the element in the array.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrayTypePropertyValue extends PropertyValue
{
    private static final long    serialVersionUID = 1L;

    private int               arrayCount  = 0;
    private ElementProperties arrayValues = null;


    /**
     * Default constructor sets the array to empty.
     */
    public ArrayTypePropertyValue()
    {
        super();
    }


    /**
     * Copy/clone constructor set up the array using the supplied template.
     *
     * @param template ArrayTypePropertyValue
     */
    public ArrayTypePropertyValue(ArrayTypePropertyValue template)
    {
        super(template);

        if (template != null)
        {
            arrayCount = template.getArrayCount();
            arrayValues = template.getArrayValues();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of PropertyValue
     */
    public PropertyValue cloneFromSubclass()
    {
        return new ArrayTypePropertyValue(this);
    }


    /**
     * Return the string version of the value - used for error logging.
     *
     * @return string value
     */
    public String valueAsString() { return mapValuesAsString(arrayValues.getPropertyValueMap()).toString(); }


    /**
     * Return the object version of the value - used for comparisons.
     *
     * @return object value
     */
    public Object valueAsObject() {
        return mapValuesAsObject(arrayValues.getPropertyValueMap());
    }


    /**
     * Return the number of elements in the array.
     *
     * @return int array size
     */
    public int getArrayCount() {
        return arrayCount;
    }


    /**
     * Set up the number of elements in the array.
     *
     * @param arrayCount int array size
     */
    public void setArrayCount(int arrayCount) {
        this.arrayCount = arrayCount;
    }


    /**
     * Return a copy of the array elements.
     *
     * @return ElementProperties containing the array elements
     */
    public ElementProperties getArrayValues()
    {
        if (arrayValues == null)
        {
            return null;
        }
        else
        {
            return new ElementProperties(arrayValues);
        }
    }


    /**
     * Add or update an element in the array.
     *
     * @param elementNumber index number of the element in the array
     * @param propertyValue value to store
     */
    public void setArrayValue(int elementNumber, PropertyValue propertyValue)
    {
        if (arrayCount > elementNumber)
        {
            if (arrayValues == null)
            {
                arrayValues = new ElementProperties();
            }
            arrayValues.setProperty(Integer.toString(elementNumber), propertyValue);
        }
        else
        {
            /*
             * Throw runtime exception to show the caller they are not using the array correctly.
             */
            throw new GAFRuntimeException(GAFErrorCode.ARRAY_OUT_OF_BOUNDS.getMessageDefinition(this.getClass().getSimpleName(),
                                                                                                Integer.toString(elementNumber),
                                                                                                Integer.toString(arrayCount)),
                                          this.getClass().getName(),
                                          "setArrayValue");
        }
    }


    /**
     * Set up the array elements in one call.
     *
     * @param arrayValues ElementProperties containing the array elements
     */
    public void setArrayValues(ElementProperties arrayValues) {
        this.arrayValues = arrayValues;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString() {
        return "ArrayTypePropertyValue{" +
                "arrayCount=" + arrayCount +
                ", arrayValues=" + arrayValues +
                ", typeName='" + getTypeName() + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ArrayTypePropertyValue that = (ArrayTypePropertyValue) objectToCompare;
        return arrayCount == that.arrayCount &&
                Objects.equals(arrayValues, that.arrayValues);
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(arrayCount, arrayValues);
    }
}
