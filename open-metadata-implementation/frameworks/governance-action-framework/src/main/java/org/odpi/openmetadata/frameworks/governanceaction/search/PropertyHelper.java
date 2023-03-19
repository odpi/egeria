/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFRuntimeException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * PropertyHelper is used by the governance actions services to manage the contents of the ElementProperties structure.
 * It is a stateless object and so there are no threading concerns with declaring it as a static variable.
 */
public class PropertyHelper
{
    /**
     * Throw an exception if the supplied GUID is null
     *
     * @param guid           unique identifier to validate
     * @param guidParameter  name of the parameter that passed the guid.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    public void validateGUID(String guid,
                             String guidParameter,
                             String methodName) throws InvalidParameterException
    {
        if ((guid == null) || (guid.isEmpty()))
        {
            throw new InvalidParameterException(GAFErrorCode.NULL_GUID.getMessageDefinition(guidParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameter);
        }
    }


    /**
     * Throw an exception if the supplied name is null
     *
     * @param name           unique name to validate
     * @param nameParameter  name of the parameter that passed the name.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the name is null
     */
    public void validateMandatoryName(String name,
                                      String nameParameter,
                                      String methodName) throws InvalidParameterException
    {
        if ((name == null) || (name.isEmpty()))
        {
            throw new InvalidParameterException(GAFErrorCode.NULL_NAME.getMessageDefinition(nameParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameter);
        }
    }


    /**
     * Add the supplied primitive property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param propertyCategory category of the property
     * @return element properties object.
     */
    private ElementProperties addPrimitivePropertyToInstance(ElementProperties    properties,
                                                             String               propertyName,
                                                             Object               propertyValue,
                                                             PrimitiveTypeCategory propertyCategory)
    {
        ElementProperties  resultingProperties;

        if (propertyValue != null)
        {
            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();

            primitiveTypePropertyValue.setPrimitiveTypeCategory(propertyCategory);
            primitiveTypePropertyValue.setPrimitiveValue(propertyValue);
            primitiveTypePropertyValue.setTypeName(propertyCategory.getName());

            resultingProperties.setProperty(propertyName, primitiveTypePropertyValue);

            return resultingProperties;
        }
        else
        {
            return properties;
        }
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @return element properties object.
     */
    public ElementProperties addStringProperty(ElementProperties properties,
                                               String            propertyName,
                                               String            propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @return resulting element properties object
     */
    public ElementProperties addIntProperty(ElementProperties properties,
                                            String            propertyName,
                                            int               propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties  properties object to add. Property may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @return resulting element properties object
     */
    public ElementProperties addLongProperty(ElementProperties properties,
                                             String            propertyName,
                                             long              propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties  properties object to add. Property may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @return resulting element properties object
     */
    public ElementProperties addFloatProperty(ElementProperties properties,
                                              String            propertyName,
                                              float             propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties  properties object to add. Property may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @return resulting element properties object
     */
    public ElementProperties addDateProperty(ElementProperties properties,
                                             String            propertyName,
                                             Date              propertyValue)
    {
        Long longValue = propertyValue.getTime();

        return addPrimitivePropertyToInstance(properties, propertyName, longValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @return resulting element properties object
     */
    public ElementProperties addBooleanProperty(ElementProperties properties,
                                                String            propertyName,
                                                boolean           propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyType type of enum
     * @param symbolicName symbol name value of property
     * @return resulting element properties object
     */
    public ElementProperties addEnumProperty(ElementProperties properties,
                                             String            propertyName,
                                             String            propertyType,
                                             String            symbolicName)
    {
        ElementProperties  resultingProperties;


        if (properties == null)
        {
            resultingProperties = new ElementProperties();
        }
        else
        {
            resultingProperties = properties;
        }


        EnumTypePropertyValue enumTypePropertyValue = new EnumTypePropertyValue();

        enumTypePropertyValue.setSymbolicName(symbolicName);
        enumTypePropertyValue.setTypeName(propertyType);

        resultingProperties.setProperty(propertyName, enumTypePropertyValue);

        return resultingProperties;
    }


    /**
     * Add the supplied array property to an element properties object.  The supplied array is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param arrayValues contents of the array
     * @return resulting element properties object
     */
    public ElementProperties addStringArrayProperty(ElementProperties properties,
                                                    String            propertyName,
                                                    List<String>      arrayValues)
    {
        if ((arrayValues != null) && (! arrayValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            ArrayTypePropertyValue arrayTypePropertyValue = new ArrayTypePropertyValue();
            arrayTypePropertyValue.setTypeName("array<string>");
            arrayTypePropertyValue.setArrayCount(arrayValues.size());
            int index = 0;
            for (String arrayValue : arrayValues )
            {
                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();

                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
                primitiveTypePropertyValue.setPrimitiveValue(arrayValue);

                arrayTypePropertyValue.setArrayValue(index, primitiveTypePropertyValue);
                index++;
            }

            resultingProperties.setProperty(propertyName, arrayTypePropertyValue);

            return resultingProperties;
        }

        return properties;
    }


    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addMapProperty(ElementProperties   properties,
                                            String              propertyName,
                                            Map<String, Object> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }


                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = this.addPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();

                    mapTypePropertyValue.setTypeName("map<string,object>");
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
            }
        }

        return properties;
    }




    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return instance properties object.
     */
    public ElementProperties addPropertyMap(ElementProperties   properties,
                                            Map<String, Object> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                Object mapPropertyValue = mapValues.get(mapPropertyName);

                if (mapPropertyValue instanceof String)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Integer)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Long)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Short)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_SHORT);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_SHORT.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Date)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE.getName());
                    /*
                     * Internally, dates are stored as Java Long.
                     */
                    Long timestamp = ((Date) mapPropertyValue).getTime();
                    primitiveTypePropertyValue.setPrimitiveValue(timestamp);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Character)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_CHAR);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_CHAR.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Byte)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BYTE);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BYTE.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Boolean)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Float)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof BigDecimal)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(
                            PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof BigInteger)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(
                            PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGINTEGER);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGINTEGER.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Double)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof List propertyAsList)
                {
                    ArrayTypePropertyValue arrayTypePropertyValue = new ArrayTypePropertyValue();

                    if (propertyAsList.size() != 0)
                    {
                        int index = 0;

                        Map<String, Object> arrayPropertyAsMap = new HashMap<>();

                        for (Object arrayValueObject : propertyAsList)
                        {
                            arrayPropertyAsMap.put(Integer.toString(index), arrayValueObject);
                            index++;
                        }

                        arrayTypePropertyValue.setArrayValues(addPropertyMap(null, arrayPropertyAsMap));
                        arrayTypePropertyValue.setArrayCount(index);
                    }

                    arrayTypePropertyValue.setTypeName("array");

                    resultingProperties.setProperty(mapPropertyName, arrayTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Map)
                {
                    Map<String, Object> propertyAsMap = (Map<String, Object>)mapPropertyValue;

                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();

                    mapTypePropertyValue.setMapValues(addPropertyMap(null, propertyAsMap));
                    mapTypePropertyValue.setTypeName("map");

                    resultingProperties.setProperty(mapPropertyName, mapTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue != null)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_UNKNOWN.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }



    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addStringMapProperty(ElementProperties   properties,
                                                  String              propertyName,
                                                  Map<String, String> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }

                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = this.addStringPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,string>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);
                    
                    return resultingProperties;
                }
            }
        }

        return properties;
    }



    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addStringPropertyMap(ElementProperties   properties,
                                                  Map<String, String> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                String mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                propertyCount++;
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }



    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, String> getStringMapFromProperty(String             sourceName,
                                                        String             propertyName,
                                                        ElementProperties properties,
                                                        String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, String>  stringMapFromProperty = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    stringMapFromProperty.put(mapPropertyName, actualPropertyValue.toString());
                }
            }

            if (! stringMapFromProperty.isEmpty())
            {
                return stringMapFromProperty;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Boolean> getBooleanMapFromProperty(String             sourceName,
                                                          String             propertyName,
                                                          ElementProperties properties,
                                                          String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Boolean>  booleanMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    booleanMap.put(mapPropertyName, (Boolean)actualPropertyValue);
                }
            }

            if (! booleanMap.isEmpty())
            {
                return booleanMap;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Long> getLongMapFromProperty(String             sourceName,
                                                    String             propertyName,
                                                    ElementProperties properties,
                                                    String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Long>  longMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    longMap.put(mapPropertyName, (Long)actualPropertyValue);
                }
            }

            if (! longMap.isEmpty())
            {
                return longMap;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Integer> getIntegerMapFromProperty(String             sourceName,
                                                          String             propertyName,
                                                          ElementProperties properties,
                                                          String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Integer>  integerMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    integerMap.put(mapPropertyName, (Integer) actualPropertyValue);
                }
            }

            if (! integerMap.isEmpty())
            {
                return integerMap;
            }
        }

        return null;
    }
    

    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all the properties of the instance
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Object> getMapFromProperty(String            sourceName,
                                                  String            propertyName,
                                                  ElementProperties properties,
                                                  String            methodName)
    {
        final String  thisMethodName = "getMapFromProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof MapTypePropertyValue)
                    {
                        MapTypePropertyValue mapTypePropertyValue = (MapTypePropertyValue) propertyValue;
                        
                        return this.getElementPropertiesAsMap(mapTypePropertyValue.getMapValues());
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        return null;
    }
    

    /**
     * Convert an element properties object into a map.
     *
     * @param properties packed properties
     * @return properties stored in Java map
     */
    public Map<String, Object> getElementPropertiesAsMap(ElementProperties    properties)
    {
        if (properties != null)
        {
            Map<String, PropertyValue> propertyValues = properties.getPropertyValueMap();
            Map<String, Object>        resultingMap   = new HashMap<>();

            if (propertyValues != null)
            {
                for (String mapPropertyName : propertyValues.keySet())
                {
                    PropertyValue actualPropertyValue = properties.getPropertyValue(mapPropertyName);

                    if (actualPropertyValue != null)
                    {
                        if (actualPropertyValue instanceof PrimitiveTypePropertyValue)
                        {
                            PrimitiveTypePropertyValue primitiveTypePropertyValue = (PrimitiveTypePropertyValue) actualPropertyValue;
                            resultingMap.put(mapPropertyName, primitiveTypePropertyValue.getPrimitiveValue());
                        }
                        else
                        {
                            resultingMap.put(mapPropertyName, actualPropertyValue);
                        }
                    }
                }
            }

            return resultingMap;
        }

        return null;
    }


    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a string property then a logic exception is thrown
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public String getStringProperty(String            sourceName,
                                    String            propertyName,
                                    ElementProperties properties,
                                    String            methodName)
    {
        final String  thisMethodName = "getStringProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue)
                    {
                        PrimitiveTypePropertyValue primitiveTypePropertyValue = (PrimitiveTypePropertyValue) propertyValue;

                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return primitiveTypePropertyValue.getPrimitiveValue().toString();
                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return null;
    }



    /**
     * Return the requested property or 0 if property is not found.  If the property is not
     * an int property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public int    getIntProperty(String             sourceName,
                                 String             propertyName,
                                 ElementProperties properties,
                                 String             methodName)
    {
        final String  thisMethodName = "getIntProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue)
                    {
                        PrimitiveTypePropertyValue primitiveTypePropertyValue = (PrimitiveTypePropertyValue) propertyValue;

                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return Integer.parseInt(primitiveTypePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return 0;
    }


    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a date property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public Date getDateProperty(String             sourceName,
                                String             propertyName,
                                ElementProperties properties,
                                String             methodName)
    {
        final String  thisMethodName = "getDateProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue)
                    {
                        PrimitiveTypePropertyValue primitiveTypePropertyValue = (PrimitiveTypePropertyValue) propertyValue;

                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                Long timestamp = (Long) primitiveTypePropertyValue.getPrimitiveValue();
                                return new Date(timestamp);

                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return null;
    }
    



    /**
     * Return the requested property or false if property is not found.  If the property is not
     * a boolean property then a logic exception is thrown
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public boolean getBooleanProperty(String             sourceName,
                                      String             propertyName,
                                      ElementProperties properties,
                                      String             methodName)
    {
        final String  thisMethodName = "getBooleanProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue)
                    {
                        PrimitiveTypePropertyValue primitiveTypePropertyValue = (PrimitiveTypePropertyValue) propertyValue;

                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return Boolean.parseBoolean(primitiveTypePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return false;
    }



    /**
     * Convert the provided instance properties and match criteria into an equivalent SearchProperties object.
     *
     * @param properties properties object to convert
     * @param matchCriteria match criteria to apply
     * @return SearchProperties object.
     */
    public SearchProperties getSearchProperties(ElementProperties properties,
                                                MatchCriteria     matchCriteria)
    {
        SearchProperties matchProperties = null;
        if (properties != null)
        {
            matchProperties = new SearchProperties();
            List<PropertyCondition> conditions = new ArrayList<>();
            Iterator<String> propertyNames = properties.getPropertyNames();

            while (propertyNames.hasNext())
            {
                String propertyName = propertyNames.next();
                PropertyCondition propertyCondition = new PropertyCondition();
                propertyCondition.setProperty(propertyName);
                PropertyValue propertyValue = properties.getPropertyValue(propertyName);

                if ((propertyValue instanceof PrimitiveTypePropertyValue)
                            && ((PrimitiveTypePropertyValue)propertyValue).getPrimitiveTypeCategory().equals(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING) )
                {
                    // Use the LIKE operator for any strings
                    propertyCondition.setOperator(PropertyComparisonOperator.LIKE);
                }
                else
                {
                    // And the EQUAlS operator for any other type
                    propertyCondition.setOperator(PropertyComparisonOperator.EQ);
                }

                // TODO: we may want to default complex types (lists, etc) to other operators than EQ?
                propertyCondition.setValue(propertyValue);
                conditions.add(propertyCondition);
            }

            matchProperties.setConditions(conditions);
            matchProperties.setMatchCriteria(matchCriteria);
        }
        return matchProperties;
    }


    /**
     * Throws a logic error exception when the repository helper is called with invalid parameters.
     * Normally this means the repository helper methods have been called in the wrong order.
     *
     * @param sourceName name of the calling repository or service
     * @param originatingMethodName method that called the repository validator
     * @param localMethodName local method that detected the error
     */
    private void throwHelperLogicError(String     sourceName,
                                       String     originatingMethodName,
                                       String     localMethodName)
    {
        throw new GAFRuntimeException(GAFErrorCode.HELPER_LOGIC_ERROR.getMessageDefinition(sourceName,
                                                                                           localMethodName,
                                                                                           originatingMethodName),
                                      this.getClass().getName(),
                                      localMethodName);
    }


    /**
     * Throws a logic error exception when the repository helper is called with invalid parameters.
     * Normally this means the repository helper methods have been called in the wrong order.
     *
     * @param sourceName name of the calling repository or service
     * @param originatingMethodName method that called the repository validator
     * @param localMethodName local method that detected the error
     * @param unexpectedException unexpected exception caught by the helper logic
     */
    private void throwHelperLogicError(String     sourceName,
                                       String     originatingMethodName,
                                       String     localMethodName,
                                       Throwable  unexpectedException)
    {
        throw new GAFRuntimeException(GAFErrorCode.HELPER_LOGIC_EXCEPTION.getMessageDefinition(sourceName,
                                                                                               localMethodName,
                                                                                               originatingMethodName),
                                          this.getClass().getName(),
                                          localMethodName,
                                          unexpectedException);
    }
}
