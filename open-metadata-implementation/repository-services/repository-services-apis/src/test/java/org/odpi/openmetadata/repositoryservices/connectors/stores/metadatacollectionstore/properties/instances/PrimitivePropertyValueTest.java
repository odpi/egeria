/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * PrimitivePropertyValueTest provides test of PrimitivePropertyValue
 */
public class PrimitivePropertyValueTest
{
    private InstancePropertyCategory category           = InstancePropertyCategory.PRIMITIVE;
    private String                   typeGUID           = "TestTypeGUID";
    private String                   typeName           = "TestTypeName";
    private PrimitiveDefCategory     booleanCategory    = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN;
    private Object                   booleanValue       = true;
    private PrimitiveDefCategory     byteCategory       = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE;
    private Object                   byteValue          = new Byte("100");
    private PrimitiveDefCategory     charCategory       = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR;
    private Object                   charValue          = 'A';
    private PrimitiveDefCategory     shortCategory      = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT;
    private Object                   shortValue         = new Short("45");
    private PrimitiveDefCategory     integerCategory    = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT;
    private Object                   integerValue       = (Integer)4749742;
    private PrimitiveDefCategory     longCategory       = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG;
    private Object                   longValue          = (long)474974442;
    private PrimitiveDefCategory     floatCategory      = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT;
    private Object                   floatValue         = (float)555555555;
    private PrimitiveDefCategory     doubleCategory     = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE;
    private Object                   doubleValue        = (double)424242424;
    private PrimitiveDefCategory     bigIntegerCategory = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER;
    private Object                   bigIntegerValue    = new BigInteger("446374279247");
    private PrimitiveDefCategory     bigDecimalCategory = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL;
    private Object                   bigDecimalValue    = new BigDecimal(-4749742);
    private PrimitiveDefCategory     stringCategory     = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;
    private Object                   stringValue        = "TestString";
    private PrimitiveDefCategory     dateCategory       = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE;
    private Object                   dateValue          = new Date(4749742);


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getBooleanTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(booleanCategory);
        testObject.setPrimitiveValue(booleanValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateBooleanObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(booleanCategory));
        assertTrue(testObject.getPrimitiveValue().equals(booleanValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONBoolean()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getBooleanTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateBooleanObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getBooleanTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateBooleanObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getCharTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(charCategory);
        testObject.setPrimitiveValue(charValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateCharObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(charCategory));
        assertTrue(testObject.getPrimitiveValue().equals(charValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONChar()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getCharTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateCharObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getCharTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateCharObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getByteTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(byteCategory);
        testObject.setPrimitiveValue(byteValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateByteObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(byteCategory));
        assertTrue(testObject.getPrimitiveValue().equals(byteValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONByte()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getByteTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateByteObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getByteTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateByteObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getShortTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(shortCategory);
        testObject.setPrimitiveValue(shortValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateShortObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(shortCategory));
        assertTrue(testObject.getPrimitiveValue().equals(shortValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONShort()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getShortTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateShortObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getShortTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateShortObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getIntegerTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(integerCategory);
        testObject.setPrimitiveValue(integerValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateIntegerObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(integerCategory));
        assertTrue(testObject.getPrimitiveValue().equals(integerValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONInteger()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getIntegerTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateIntegerObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getIntegerTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateIntegerObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getLongTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(longCategory);
        testObject.setPrimitiveValue(longValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateLongObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(longCategory));
        assertTrue(testObject.getPrimitiveValue().equals(longValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONLong()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getLongTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateLongObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getLongTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateLongObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getFloatTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(floatCategory);
        testObject.setPrimitiveValue(floatValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateFloatObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(floatCategory));
        assertTrue(testObject.getPrimitiveValue().equals(floatValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONFloat()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getFloatTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateFloatObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getFloatTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateFloatObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getDoubleTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(doubleCategory);
        testObject.setPrimitiveValue(doubleValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateDoubleObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(doubleCategory));
        assertTrue(testObject.getPrimitiveValue().equals(doubleValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONDouble()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getDoubleTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateDoubleObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getDoubleTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateDoubleObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }



    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getBigIntegerTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(bigIntegerCategory);
        testObject.setPrimitiveValue(bigIntegerValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateBigIntegerObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(bigIntegerCategory));
        assertTrue(testObject.getPrimitiveValue().equals(bigIntegerValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONBigInteger()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getBigIntegerTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateBigIntegerObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getBigIntegerTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateBigIntegerObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getBigDecimalTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(bigDecimalCategory);
        testObject.setPrimitiveValue(bigDecimalValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateBigDecimalObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(bigDecimalCategory));
        assertTrue(testObject.getPrimitiveValue().equals(bigDecimalValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONBigDecimal()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getBigDecimalTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateBigDecimalObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getBigDecimalTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateBigDecimalObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getStringTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(stringCategory);
        testObject.setPrimitiveValue(stringValue);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateStringObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(stringCategory));
        assertTrue(testObject.getPrimitiveValue().equals(stringValue));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONString()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getStringTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateStringObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getStringTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateStringObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitivePropertyValue getDateTestObject()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setPrimitiveDefCategory(dateCategory);
        Long timestamp = ((Date)dateValue).getTime();
        testObject.setPrimitiveValue(timestamp);

        return testObject;
    }


    /**
     * Validate the values of a test object
     *
     * @param testObject object to validate
     */
    private void validateDateObject(PrimitivePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
        assertTrue(testObject.getPrimitiveDefCategory().equals(dateCategory));
        Long timestamp = ((Date)dateValue).getTime();
        assertTrue(testObject.getPrimitiveValue().equals(timestamp));
    }


    /**
     * Validate that the constructors set up the attributes correctly.
     */
    @Test public void testConstructors()
    {
        PrimitivePropertyValue testObject = new PrimitivePropertyValue();

        assertTrue(testObject.getInstancePropertyCategory() == category);
        assertTrue(testObject.getTypeGUID() == null);
        assertTrue(testObject.getTypeName() == null);

        PrimitivePropertyValue anotherTestObject = getDateTestObject();

        validateDateObject(new PrimitivePropertyValue(anotherTestObject));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSONDate()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getDateTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateDateObject(objectMapper.readValue(jsonString, PrimitivePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superclass
         */
        InstancePropertyValue testSuperObject = getDateTestObject();
        try
        {
            jsonString = objectMapper.writeValueAsString(testSuperObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateDateObject((PrimitivePropertyValue)objectMapper.readValue(jsonString, InstancePropertyValue.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getDateTestObject().toString().contains("PrimitivePropertyValue"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getDateTestObject().equals(getDateTestObject()));

        InstancePropertyValue testObject = getDateTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getDateTestObject().equals(null));
        assertFalse(getDateTestObject().equals("A String"));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getDateTestObject().hashCode() == getDateTestObject().hashCode());

        InstancePropertyValue testObject = getDateTestObject();
        InstancePropertyValue anotherObject = getDateTestObject();
        anotherObject.setTypeGUID("DifferentGUID");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
