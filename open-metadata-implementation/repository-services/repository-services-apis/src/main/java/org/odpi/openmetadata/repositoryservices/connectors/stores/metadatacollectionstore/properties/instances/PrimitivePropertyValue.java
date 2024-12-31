/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * PrimitivePropertyValue stores a single primitive property.  This is stored in the specific Java class
 * for the property value's type, although it is stored as an object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PrimitivePropertyValue extends InstancePropertyValue
{
    @Serial
    private static final long serialVersionUID = 1L;

    private  PrimitiveDefCategory   primitiveDefCategory = null;
    private  Object                 primitiveValue = null;


    /**
     * Default constructor sets the primitive property value to null.
     */
    public PrimitivePropertyValue()
    {
        super(InstancePropertyCategory.PRIMITIVE);
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.
     *
     * @param template PrimitivePropertyValue
     */
    public PrimitivePropertyValue(PrimitivePropertyValue   template)
    {
        super(template);

        if (template != null)
        {
            this.primitiveDefCategory = template.getPrimitiveDefCategory();
            this.primitiveValue = template.getPrimitiveValue();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of InstancePropertyValue
     */
    public  InstancePropertyValue cloneFromSubclass()
    {
        return new PrimitivePropertyValue(this);
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
     * @return PrimitiveDefCategory
     */
    public PrimitiveDefCategory getPrimitiveDefCategory() { return primitiveDefCategory; }


    /**
     * Set up the category of the primitive type.  This sets the name and Java Class used for
     * the primitive value.
     *
     * @param primitiveDefCategory PrimitiveDefCategory enum
     */
    public void setPrimitiveDefCategory(PrimitiveDefCategory primitiveDefCategory)
    {
        /*
         * Tests that type and value are consistent
         */
        this.primitiveValue = validateValueAgainstType(primitiveDefCategory, primitiveValue);

        /*
         * All ok so set the category
         */
        this.primitiveDefCategory = primitiveDefCategory;
    }


    /**
     * Return the primitive value.  It is already set up to be the appropriate type for the primitive
     * as defined in the PrimitiveDefCategory.
     *
     * @return Object containing the primitive value.
     */
    public Object getPrimitiveValue() { return primitiveValue; }


    /**
     * Set up the primitive value.   Although it is passed in as a java.lang.Object, it should be the correct
     * type as defined by the PrimitiveDefCategory.
     *
     * @param primitiveValue object contain the primitive value
     */
    public void setPrimitiveValue(Object primitiveValue)
    {
        /*
         * Tests that type and value are consistent
         */
        this.primitiveValue = validateValueAgainstType(primitiveDefCategory, primitiveValue);
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "PrimitivePropertyValue{" +
                "primitiveValue=" + primitiveValue +
                ", primitiveDefCategory=" + primitiveDefCategory +
                ", instancePropertyCategory=" + getInstancePropertyCategory() +
                ", typeGUID='" + getTypeGUID() + '\'' +
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
        if (! (objectToCompare instanceof PrimitivePropertyValue that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        if (primitiveDefCategory != that.primitiveDefCategory)
        {
            return false;
        }
        return primitiveValue != null ? primitiveValue.equals(that.primitiveValue) : that.primitiveValue == null;
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getPrimitiveDefCategory(), getPrimitiveValue());
    }


    /**
     * Ensure that the type and value supplied are compatible.
     *
     * @param primitiveDefCategory category to test
     * @param primitiveValue value to test
     * @return validated primitive value
     */
    private Object validateValueAgainstType(PrimitiveDefCategory   primitiveDefCategory,
                                            Object                 primitiveValue)
    {
        final String  methodName = "validateValueAgainstType";

        /*
         * Return if one of the values is missing
         */
        if ((primitiveDefCategory == null) || (primitiveValue == null))
        {
            return primitiveValue;
        }

        try
        {
            Class<?>    testJavaClass = Class.forName(primitiveDefCategory.getJavaClassName());

            if (!testJavaClass.isInstance(primitiveValue))
            {
                if (primitiveDefCategory == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE)
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

                    if (primitiveValue instanceof Integer castValue)
                    {
                        return castValue.longValue();
                    }
                    else if (primitiveValue instanceof Date date)
                    {
                        return date.getTime();
                    }
                    else
                    {
                        /*
                         * The type of the primitiveValue cannot be used as a date.
                         * It is likely that this has been caused by an invalid deserialization or by
                         * some other code trying to set the value as a type other than Long.
                         * This is an internal error that needs to be debugged and fixed.
                         */
                        throw new OMRSLogicErrorException(OMRSErrorCode.INVALID_PRIMITIVE_TYPE.getMessageDefinition("OM_PRIMITIVE_TYPE_DATE",
                                                                                                                    primitiveDefCategory.getJavaClassName(),
                                                                                                                    primitiveValue.getClass().getName()),
                                this.getClass().getName(),
                                methodName);
                    }
                }
                else if (primitiveDefCategory == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL)
                {
                    Integer    castValue = (Integer)primitiveValue;

                    return new BigDecimal(castValue);
                }
                else if (primitiveDefCategory == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER)
                {
                    Long    castValue = (Long)primitiveValue;

                    return new BigInteger(castValue.toString());
                }
                else if (primitiveDefCategory == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE)
                {
                    Integer    castValue = (Integer)primitiveValue;

                    return  Byte.valueOf(castValue.toString());
                }
                else if (primitiveDefCategory == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR)
                {
                    String    castValue = (String)primitiveValue;

                    return castValue.charAt(0);
                }
                else if (primitiveDefCategory == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT)
                {
                    Double    castValue = (Double)primitiveValue;

                    return  Float.valueOf(String.valueOf(castValue));
                }
                else if (primitiveDefCategory == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG)
                {
                    Integer    castValue = (Integer)primitiveValue;

                    return Long.valueOf(castValue);
                }
                else if (primitiveDefCategory == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT)
                {
                    Integer    castValue = (Integer)primitiveValue;

                    return Short.valueOf(castValue.toString());
                }
                else
                {
                    /*
                     * The primitive value supplied is the wrong type.  Throw an exception.
                     */
                    throw new OMRSLogicErrorException(OMRSErrorCode.INVALID_PRIMITIVE_VALUE.getMessageDefinition(primitiveDefCategory.getJavaClassName(),
                                                                                                                 primitiveValue.getClass().getName(),
                                                                                                                 primitiveDefCategory.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
            }
        }
        catch (ClassNotFoundException    unknownPrimitiveClass)
        {
            /*
             * The java class defined in the primitiveDefCategory is not known.  This is an internal error
             * that needs a code fix in PrimitiveDefCategory.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.INVALID_PRIMITIVE_CLASS_NAME.getMessageDefinition(primitiveDefCategory.getJavaClassName(),
                                                                                                              primitiveDefCategory.getName()),
                                              this.getClass().getName(),
                                              methodName,
                                              unknownPrimitiveClass);
        }
        catch (Error    invalidPrimitiveCategory)
        {
            /*
             * Some unexpected exception occurred when manipulating the Java Classes.  Probably a coding error.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.INVALID_PRIMITIVE_CATEGORY.getMessageDefinition(primitiveDefCategory.getName()),
                                              this.getClass().getName(),
                                              methodName,
                                              invalidPrimitiveCategory);
        }

        return primitiveValue;
    }
}
