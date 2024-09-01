/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFRuntimeException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * PrimitiveTypePropertyValue stores a single primitive property.  This is stored in the specific Java class
 * for the property value's type, although it is stored as an object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PrimitiveTypePropertyValue extends PropertyValue
{
    private PrimitiveTypeCategory primitiveTypeCategory = null;
    private Object                primitiveValue        = null;


    /**
     * Default constructor sets the primitive property value to null.
     */
    public PrimitiveTypePropertyValue()
    {
        super();
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.
     *
     * @param template PrimitiveTypePropertyValue
     */
    public PrimitiveTypePropertyValue(PrimitiveTypePropertyValue template)
    {
        super(template);

        if (template != null)
        {
            this.setPrimitiveTypeCategory(template.getPrimitiveTypeCategory());
            this.primitiveValue = template.getPrimitiveValue();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of PropertyValue
     */
    public PropertyValue cloneFromSubclass()
    {
        return new PrimitiveTypePropertyValue(this);
    }


    /**
     * Return the string version of the value - used for error logging.
     *
     * @return string value
     */
    public  String valueAsString()
    {
        return primitiveValue == null ? "<null>" : primitiveValue.toString();
    }


    /**
     * Return the object version of the value - used for comparisons.
     *
     * @return object value
     */
    public  Object valueAsObject()
    {
        return primitiveValue;
    }


    /**
     * Return the category of the primitive's type.  This sets the name and Java Class used for
     * the primitive value.
     *
     * @return PrimitiveTypeCategory
     */
    public PrimitiveTypeCategory getPrimitiveTypeCategory() { return primitiveTypeCategory; }


    /**
     * Set up the name of the type.
     *
     * @param typeName String type name
     */
    public void setTypeName(String typeName)
    {
        for (PrimitiveTypeCategory category : PrimitiveTypeCategory.values())
        {
            if (category.getName().equals(typeName))
            {
                setPrimitiveTypeCategory(category);
            }
        }
    }


    /**
     * Set up the category of the primitive type.  This sets the name and Java Class used for
     * the primitive value.
     *
     * @param primitiveTypeCategory PrimitiveTypeCategory enum
     */
    public void setPrimitiveTypeCategory(PrimitiveTypeCategory primitiveTypeCategory)
    {
        /*
         * Tests that type and value are consistent
         */
        this.primitiveValue = validateValueAgainstType(primitiveTypeCategory, primitiveValue);

        /*
         * All ok so set the category
         */
        this.primitiveTypeCategory = primitiveTypeCategory;

        if (primitiveTypeCategory == null)
        {
            super.setTypeName(null);
        }
        else
        {
            super.setTypeName(primitiveTypeCategory.getName());
        }
    }


    /**
     * Return the primitive value.  It is already set up to be the appropriate type for the primitive
     * as defined in the PrimitiveTypeCategory.
     *
     * @return Object containing the primitive value.
     */
    public Object getPrimitiveValue() { return primitiveValue; }


    /**
     * Set up the primitive value.   Although it is passed in as a java.lang.Object, it should be the correct
     * type as defined by the PrimitiveTypeCategory.
     *
     * @param primitiveValue object contain the primitive value
     */
    public void setPrimitiveValue(Object primitiveValue)
    {
        /*
         * Tests that type and value are consistent
         */
        this.primitiveValue = validateValueAgainstType(primitiveTypeCategory, primitiveValue);
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "PrimitiveTypePropertyValue{" +
                "primitiveValue=" + primitiveValue +
                ", primitiveTypeCategory=" + primitiveTypeCategory +
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
        PrimitiveTypePropertyValue that = (PrimitiveTypePropertyValue) objectToCompare;
        return primitiveTypeCategory == that.primitiveTypeCategory &&
                Objects.equals(primitiveValue, that.primitiveValue);
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getPrimitiveTypeCategory(), getPrimitiveValue());
    }


    /**
     * Ensure that the type and value supplied are compatible.
     *
     * @param primitiveTypeCategory category to test
     * @param primitiveValue value to test
     * @return validated primitive value
     */
    private Object validateValueAgainstType(PrimitiveTypeCategory primitiveTypeCategory,
                                            Object                 primitiveValue)
    {
        final String  methodName = "validateValueAgainstType";

        /*
         * Return if one of the values is missing
         */
        if ((primitiveTypeCategory == null) || (primitiveValue == null))
        {
            return primitiveValue;
        }

        try
        {
            Class<?>    testJavaClass = Class.forName(primitiveTypeCategory.getJavaClassName());

            if (!testJavaClass.isInstance(primitiveValue))
            {
                if (primitiveTypeCategory == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE)
                {
                    /*
                     * Date values are stored as Longs. The RepositoryHelper helper methods
                     * will accept and return Java Date objects, for convenience for callers,
                     * but internally the OMRS stores a date as a Java Long.
                     *
                     * However, with the combination of Spring and Jackson it is possible
                     * for a date that was serialized as a Long to be deserialized as an
                     * Integer. The following conversion repatriates it to Long.
                     */

                    if (primitiveValue instanceof Integer)
                    {
                        Integer castValue = (Integer)primitiveValue;
                        return castValue.longValue();
                    }
                    else
                    {
                        /*
                         * The type of the primitiveValue cannot be used as a date.
                         * It is likely that this has been caused by an invalid deserialization or by
                         * some other code trying to set the value as a type other than Long.
                         * This is an internal error that needs to be debugged and fixed.
                         */
                        throw new GAFRuntimeException(GAFErrorCode.INVALID_PRIMITIVE_TYPE.getMessageDefinition("OM_PRIMITIVE_TYPE_DATE",
                                                                                                               primitiveTypeCategory.getJavaClassName(),
                                                                                                               primitiveValue.getClass().getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                    }
                }
                else if (primitiveTypeCategory == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL)
                {
                    Integer    castValue = (Integer)primitiveValue;

                    return new BigDecimal(castValue);
                }
                else if (primitiveTypeCategory == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGINTEGER)
                {
                    Long    castValue = (Long)primitiveValue;

                    return new BigInteger(castValue.toString());
                }
                else if (primitiveTypeCategory == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BYTE)
                {
                    Integer    castValue = (Integer)primitiveValue;

                    return castValue.toString();
                }
                else if (primitiveTypeCategory == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_CHAR)
                {
                    String    castValue = (String)primitiveValue;

                    return castValue.charAt(0);
                }
                else if (primitiveTypeCategory == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT)
                {
                    Double    castValue = (Double)primitiveValue;

                    return castValue;
                }
                else if (primitiveTypeCategory == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG)
                {
                    Integer    castValue = (Integer)primitiveValue;

                    return castValue;
                }
                else if (primitiveTypeCategory == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_SHORT)
                {
                    Integer    castValue = (Integer)primitiveValue;

                    return castValue.toString();
                }
                else
                {
                    /*
                     * The primitive value supplied is the wrong type.  Throw an exception.
                     */
                    throw new GAFRuntimeException(GAFErrorCode.INVALID_PRIMITIVE_VALUE.getMessageDefinition(primitiveTypeCategory.getJavaClassName(),
                                                                                                            primitiveValue.getClass().getName(),
                                                                                                            primitiveTypeCategory.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
            }
        }
        catch (ClassNotFoundException    unknownPrimitiveClass)
        {
            /*
             * The java class defined in the primitiveTypeCategory is not known.  This is an internal error
             * that needs a code fix in PrimitiveTypeCategory.
             */
            throw new GAFRuntimeException(GAFErrorCode.INVALID_PRIMITIVE_CLASS_NAME.getMessageDefinition(primitiveTypeCategory.getJavaClassName(),
                                                                                                         primitiveTypeCategory.getName()),
                                              this.getClass().getName(),
                                              methodName,
                                              unknownPrimitiveClass);
        }
        catch (Error    invalidPrimitiveCategory)
        {
            /*
             * Some unexpected exception occurred when manipulating the Java Classes.  Probably a coding error.
             */
            throw new GAFRuntimeException(GAFErrorCode.INVALID_PRIMITIVE_CATEGORY.getMessageDefinition(primitiveTypeCategory.getName()),
                                              this.getClass().getName(),
                                              methodName,
                                              invalidPrimitiveCategory);
        }

        return primitiveValue;
    }
}
