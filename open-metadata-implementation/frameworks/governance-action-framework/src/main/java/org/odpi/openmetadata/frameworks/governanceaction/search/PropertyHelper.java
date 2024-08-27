/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFRuntimeException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;

/**
 * PropertyHelper is used by the governance actions services to manage the contents of the ElementProperties structure.
 * It is a stateless object and so there are no threading concerns with declaring it as a static variable.
 */
public class PropertyHelper
{
    /**
     * Throw an exception if the supplied GUID is null
     *
     * @param guid          unique identifier to validate
     * @param guidParameter name of the parameter that passed the guid.
     * @param methodName    name of the method making the call.
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
     * @param name          unique name to validate
     * @param nameParameter name of the parameter that passed the name.
     * @param methodName    name of the method making the call.
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
     * Test the type of the element to determine if it matches the desired type.  This method works for all categories of elements,
     * ie entities, relationships and classifications.
     *
     * @param elementControlHeader header of the element to test
     * @param expectedType         expected type - nul means any type
     *
     * @return boolean result
     */
    public boolean isTypeOf(ElementControlHeader elementControlHeader,
                            String               expectedType)
    {
        if (expectedType == null)
        {
            return true;
        }

        if ((elementControlHeader != null) && (elementControlHeader.getType() != null))
        {
            List<String> typeList = elementControlHeader.getType().getSuperTypeNames();

            if (typeList == null)
            {
                typeList = new ArrayList<>();
            }

            typeList.add(elementControlHeader.getType().getTypeName());

            return typeList.contains(expectedType);
        }

        return false;
    }


    /**
     * Extract the domain name from an element's header.
     *
     * @param elementControlHeader header of an open metadata element
     * @return string type name
     */
    public String getDomainName(ElementControlHeader elementControlHeader)
    {
        String domainName = elementControlHeader.getType().getTypeName();

        if (elementControlHeader.getType().getSuperTypeNames() != null)
        {
            /*
             * The super types are listed in increasing levels of abstraction.
             * Eg [DataSet, Asset, Referenceable, OpenMetadataRoot].
             * In this example, the domain is Asset (one below Referenceable).
             */
            for (String superTypeName : elementControlHeader.getType().getSuperTypeNames())
            {
                if ((! superTypeName.equals(OpenMetadataType.OPEN_METADATA_ROOT.typeName)) &&
                    (! superTypeName.equals(OpenMetadataType.REFERENCEABLE.typeName)))
                {
                    domainName = superTypeName;
                }
            }
        }

        return domainName;
    }


    /**
     * Return the search properties that requests elements with an exactly matching name in any of the listed property names.
     *
     * @param propertyNames list of property names
     * @param value name to match on
     * @param propertyComparisonOperator set to EQ for exact match and LIKE for fuzzy match
     * @return search properties
     */
    public SearchProperties getSearchPropertiesByName(List<String>               propertyNames,
                                                      String                     value,
                                                      PropertyComparisonOperator propertyComparisonOperator)
    {
        if ((propertyNames != null) && (! propertyNames.isEmpty()))
        {
            SearchProperties searchProperties = new SearchProperties();

            PrimitiveTypePropertyValue propertyValue = new PrimitiveTypePropertyValue();
            propertyValue.setTypeName("string");
            propertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);

            if (PropertyComparisonOperator.LIKE.equals(propertyComparisonOperator))
            {
                propertyValue.setPrimitiveValue(".*" + Pattern.quote(value) + ".*");
            }
            else
            {
                propertyValue.setPrimitiveValue(value);
            }

            List<PropertyCondition> propertyConditions = new ArrayList<>();

            for (String propertyName : propertyNames)
            {
                PropertyCondition propertyCondition = new PropertyCondition();

                propertyCondition.setValue(propertyValue);
                propertyCondition.setProperty(propertyName);

                if (propertyComparisonOperator == null)
                {
                    propertyCondition.setOperator(PropertyComparisonOperator.EQ);
                }
                else
                {
                    propertyCondition.setOperator(propertyComparisonOperator);
                }

                propertyConditions.add(propertyCondition);
            }

            searchProperties.setConditions(propertyConditions);
            searchProperties.setMatchCriteria(MatchCriteria.ANY);

            return searchProperties;
        }

        return null;
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
    private ElementProperties addPrimitivePropertyToInstance(ElementProperties     properties,
                                                             String                propertyName,
                                                             Object                propertyValue,
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
        if (propertyValue != null)
        {
            Long longValue = propertyValue.getTime();

            return addPrimitivePropertyToInstance(properties, propertyName, longValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE);
        }

        return properties;
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
    @SuppressWarnings(value = "unchecked")
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
                else if (mapPropertyValue instanceof List<?> propertyAsList)
                {
                    ArrayTypePropertyValue arrayTypePropertyValue = new ArrayTypePropertyValue();

                    if (! propertyAsList.isEmpty())
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
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addBooleanMapProperty(ElementProperties    properties,
                                                   String               propertyName,
                                                   Map<String, Boolean> mapValues)
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
                ElementProperties  mapElementProperties  = this.addBooleanPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,boolean>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
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
    public ElementProperties addIntMapProperty(ElementProperties    properties,
                                               String               propertyName,
                                               Map<String, Integer> mapValues)
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
                ElementProperties  mapElementProperties  = this.addIntPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,int>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
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
    public ElementProperties addLongMapProperty(ElementProperties properties,
                                                String            propertyName,
                                                Map<String, Long> mapValues)
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
                ElementProperties  mapElementProperties  = this.addLongPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,long>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
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
    public ElementProperties addDateMapProperty(ElementProperties properties,
                                                String            propertyName,
                                                Map<String, Date> mapValues)
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
                ElementProperties  mapElementProperties  = this.addDatePropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,date>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
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
    public ElementProperties addDoubleMapProperty(ElementProperties   properties,
                                                  String              propertyName,
                                                  Map<String, Double> mapValues)
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
                ElementProperties  mapElementProperties  = this.addDoublePropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,double>");
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
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addIntPropertyMap(ElementProperties    properties,
                                               Map<String, Integer> mapValues)
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
                Integer mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT.getName());
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
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addLongPropertyMap(ElementProperties   properties,
                                                Map<String, Long> mapValues)
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
                Long mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG.getName());
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
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addDatePropertyMap(ElementProperties   properties,
                                                Map<String, Date>   mapValues)
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
                Date mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE.getName());
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
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addDoublePropertyMap(ElementProperties   properties,
                                                  Map<String, Double> mapValues)
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
                Double mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE.getName());
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
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addBooleanPropertyMap(ElementProperties    properties,
                                                   Map<String, Boolean> mapValues)
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
                Boolean mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getName());
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
     * Return the requested property or null if property is not found.  If the property is found, it is removed from
     * the InstanceProperties structure.  If the property is not a string property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public String removeStringProperty(String             sourceName,
                                       String             propertyName,
                                       ElementProperties  properties,
                                       String             methodName)
    {
        String  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }

    /**
     * Retrieve the ordinal value from an enum property.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return symbolic name
     */
    public String removeEnumProperty(String             sourceName,
                                     String             propertyName,
                                     ElementProperties  properties,
                                     String             methodName)
    {
        String  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getEnumPropertySymbolicName(sourceName, propertyName, properties, methodName);
            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, String> removeStringMapFromProperty(String             sourceName,
                                                           String             propertyName,
                                                           ElementProperties  properties,
                                                           String             methodName)
    {
        Map<String, String>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }



    /**
     * Return the requested property or 0 if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not an int property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public int    removeIntProperty(String             sourceName,
                                    String             propertyName,
                                    ElementProperties  properties,
                                    String             methodName)
    {
        int  retrievedProperty = 0;

        if (properties != null)
        {
            retrievedProperty = this.getIntProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }



    /**
     * Return the requested property or null if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a date property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public Date    removeDateProperty(String             sourceName,
                                      String             propertyName,
                                      ElementProperties  properties,
                                      String             methodName)
    {
        Date  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getDateProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }



    /**
     * Return the requested property or false if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a boolean property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public boolean removeBooleanProperty(String             sourceName,
                                         String             propertyName,
                                         ElementProperties  properties,
                                         String             methodName)
    {
        boolean  retrievedProperty = false;

        if (properties != null)
        {
            retrievedProperty = this.getBooleanProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }


    /**
     * Return the requested property or 0 if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a long property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public long   removeLongProperty(String             sourceName,
                                     String             propertyName,
                                     ElementProperties properties,
                                     String             methodName)
    {
        long  retrievedProperty = 0;

        if (properties != null)
        {
            retrievedProperty = this.getLongProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a string array property and extracts its values.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not an array property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all the properties of the instance
     * @param methodName method of caller
     * @return array property value or null
     */
    public List<String> removeStringArrayProperty(String             sourceName,
                                                  String             propertyName,
                                                  ElementProperties properties,
                                                  String             methodName)
    {
        List<String>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringArrayProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Object> removeMapFromProperty(String             sourceName,
                                                     String             propertyName,
                                                     ElementProperties properties,
                                                     String             methodName)
    {
        Map<String, Object>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }



    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Long> removeLongMapFromProperty(String             sourceName,
                                                       String             propertyName,
                                                       ElementProperties  properties,
                                                       String             methodName)
    {
        Map<String, Long>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getLongMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Double> removeDoubleMapFromProperty(String             sourceName,
                                                           String             propertyName,
                                                           ElementProperties  properties,
                                                           String             methodName)
    {
        Map<String, Double>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getDoubleMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }



    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Integer> removeIntegerMapFromProperty(String             sourceName,
                                                             String             propertyName,
                                                             ElementProperties  properties,
                                                             String             methodName)
    {
        Map<String, Integer>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getIntegerMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Boolean> removeBooleanMapFromProperty(String             sourceName,
                                                             String             propertyName,
                                                             ElementProperties  properties,
                                                             String             methodName)
    {
        Map<String, Boolean>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getBooleanMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Date> removeDateMapFromProperty(String             sourceName,
                                                          String             propertyName,
                                                          ElementProperties  properties,
                                                          String             methodName)
    {
        Map<String, Date>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getDateMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Remove the named property from the instance properties object.
     *
     * @param propertyName name of property to remove
     * @param properties instance properties object to work on
     */
    protected void removeProperty(String    propertyName, ElementProperties properties)
    {
        if (properties != null)
        {
            Map<String, PropertyValue> instancePropertyValueMap = properties.getPropertyValueMap();

            if (instancePropertyValueMap != null)
            {
                instancePropertyValueMap.remove(propertyName);
                properties.setPropertyValueMap(instancePropertyValueMap);
            }
        }
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
                                                        ElementProperties  properties,
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
                                                          ElementProperties  properties,
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
    public Map<String, Date> getDateMapFromProperty(String             sourceName,
                                                    String             propertyName,
                                                    ElementProperties  properties,
                                                    String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Date>  dateMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    dateMap.put(mapPropertyName, (Date)actualPropertyValue);
                }
            }

            if (! dateMap.isEmpty())
            {
                return dateMap;
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
                    longMap.put(mapPropertyName, Long.parseLong(actualPropertyValue.toString()));
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
    public Map<String, Double> getDoubleMapFromProperty(String             sourceName,
                                                        String             propertyName,
                                                        ElementProperties properties,
                                                        String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Double>  doubleMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    doubleMap.put(mapPropertyName, Double.parseDouble(actualPropertyValue.toString()));
                }
            }

            if (! doubleMap.isEmpty())
            {
                return doubleMap;
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
                    if (propertyValue instanceof MapTypePropertyValue mapTypePropertyValue)
                    {
                        return this.getElementPropertiesAsMap(mapTypePropertyValue.getMapValues());
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the ordinal value from an enum property.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return int ordinal or -1 if not found
     */
    public String getEnumPropertySymbolicName(String             sourceName,
                                              String             propertyName,
                                              ElementProperties  properties,
                                              String             methodName)
    {
        PropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

        if (instancePropertyValue instanceof EnumTypePropertyValue enumTypePropertyValue)
        {
            return enumTypePropertyValue.getSymbolicName();
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
                        if (actualPropertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                        {
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
     * Replace any placeholders found in the string property with the supplied values.
     *
     * @param propertyValue string property from the template
     * @param placeholderProperties map of placeholder names to placeholder values to substitute into the template
     *                              properties
     * @return updated property
     */
    public String replacePrimitiveStringWithPlaceholders(String              propertyValue,
                                                         Map<String, String> placeholderProperties)
    {
        if ((propertyValue == null) || (! propertyValue.contains("{{")))
        {
            /*
             * No placeholders in property.
             */
            return propertyValue;
        }

        if ((placeholderProperties != null) && (! placeholderProperties.isEmpty()))
        {
            for (String placeholderName : placeholderProperties.keySet())
            {
                String placeholderMatchString = "{{"+ placeholderName + "}}";

                if (propertyValue.equals(placeholderMatchString))
                {
                    propertyValue = placeholderProperties.get(placeholderName);
                }
                else
                {
                    String regExMatchString = Pattern.quote(placeholderMatchString);
                    String[] configBits = propertyValue.split(regExMatchString);

                    if (configBits.length == 1)
                    {
                        if (! propertyValue.equals(configBits[0]))
                        {
                            propertyValue = configBits[0] + placeholderProperties.get(placeholderName);
                        }
                    }
                    else if (configBits.length > 1)
                    {
                        StringBuilder newConfigString = new StringBuilder();
                        boolean       firstPart       = true;

                        for (String configBit : configBits)
                        {
                            if (! firstPart)
                            {
                                newConfigString.append(placeholderProperties.get(placeholderName));
                            }

                            firstPart = false;

                            if (configBit != null)
                            {
                                newConfigString.append(configBit);
                            }
                        }

                        if (propertyValue.endsWith(placeholderMatchString))
                        {
                            newConfigString.append(placeholderProperties.get(placeholderName));
                        }

                        propertyValue = newConfigString.toString();
                    }
                }
            }
        }

        return propertyValue;
    }


    /**
     * Return the property name from the template entity that has its placeholder variables resolved.
     *
     * @param sourceName name of calling source
     * @param templateElement template element which has properties that include placeholder values.
     * @param propertyName name of the property to extract
     * @param placeholderProperties placeholder properties to use to resolve the property
     * @return resolved property value
     */
    public String getResolvedStringPropertyFromTemplate(String              sourceName,
                                                        OpenMetadataElement templateElement,
                                                        String              propertyName,
                                                        Map<String, String> placeholderProperties)
    {
        final String methodName = "";

        if (templateElement != null)
        {
            String propertyValue = this.getStringProperty(sourceName,
                                                          propertyName,
                                                          templateElement.getElementProperties(),
                                                          methodName);

            return this.replacePrimitiveStringWithPlaceholders(propertyValue, placeholderProperties);
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
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return primitiveTypePropertyValue.getPrimitiveValue().toString();
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return null;
    }


    /**
     * Locates and extracts a string array property and extracts its values.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all the properties of the instance
     * @param callingMethodName method of caller
     * @return array property value or null
     */
    public List<String> getStringArrayProperty(String             sourceName,
                                               String             propertyName,
                                               ElementProperties properties,
                                               String             callingMethodName)
    {
        final String  thisMethodName = "getStringArrayProperty";

        if (properties != null)
        {
            PropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                /*
                 * The property exists in the supplied properties.   It should be of category ARRAY.
                 * If it is then it can be cast to an ArrayPropertyValue in order to extract the
                 * array size and the values.
                 */

                try
                {
                    if (instancePropertyValue instanceof ArrayTypePropertyValue arrayPropertyValue)
                    {
                        if (arrayPropertyValue.getArrayCount() > 0)
                        {
                            /*
                             * There are values to extract
                             */
                            return getPropertiesAsArray(arrayPropertyValue.getArrayValues());
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, callingMethodName, thisMethodName);
                }
            }
        }

        return null;
    }


    /**
     * Convert the values in the instance properties into a String Array.  It assumes all the elements are primitives.
     *
     * @param elementProperties instance properties containing the values.  They should all be primitive Strings.
     * @return list of strings
     */
    private List<String> getPropertiesAsArray(ElementProperties     elementProperties)
    {
        if (elementProperties != null)
        {
            Map<String, PropertyValue> instancePropertyValues = elementProperties.getPropertyValueMap();
            List<String>               resultingArray = new ArrayList<>();

            for (String arrayOrdinalName : instancePropertyValues.keySet())
            {
                if (arrayOrdinalName != null)
                {
                    int           arrayOrdinalNumber  = Integer.decode(arrayOrdinalName);
                    PropertyValue actualPropertyValue = elementProperties.getPropertyValue(arrayOrdinalName);

                    if (actualPropertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        resultingArray.add(arrayOrdinalNumber, primitiveTypePropertyValue.getPrimitiveValue().toString());
                    }
                }
            }

            return resultingArray;
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
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return Integer.parseInt(primitiveTypePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return 0;
    }



    /**
     * Return the requested property or 0 if property is not found.  If the property is not
     * a long property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public long  getLongProperty(String             sourceName,
                                 String             propertyName,
                                 ElementProperties properties,
                                 String             methodName)
    {
        final String  thisMethodName = "getLongProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return Long.parseLong(primitiveTypePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Exception error)
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
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
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
                catch (Exception error)
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
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return Boolean.parseBoolean(primitiveTypePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return false;
    }


    /**
     * Build up property parameters for a search that returns the metadata collection name.
     *
     * @return search properties
     */
    public SearchProperties getSearchPropertiesForMetadataCollectionName(String metadataCollectionQualifiedName)
    {
        SearchProperties           searchProperties       = new SearchProperties();
        List<PropertyCondition>    conditions             = new ArrayList<>();
        PropertyCondition          condition              = new PropertyCondition();
        PrimitiveTypePropertyValue primitivePropertyValue = new PrimitiveTypePropertyValue();

        primitivePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(metadataCollectionQualifiedName);
        primitivePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        condition.setProperty(OpenMetadataProperty.METADATA_COLLECTION_NAME.name);
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(primitivePropertyValue);

        conditions.add(condition);

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);
        return searchProperties;
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
     * Extract a particular classification from an open metadata element.
     *
     * @param openMetadataElement source element
     * @param classificationName name of the classification to extract
     * @return requested classification, or null
     */
    public AttachedClassification getClassification(OpenMetadataElement openMetadataElement,
                                                    String              classificationName)
    {
        if ((openMetadataElement != null) && (classificationName != null))
        {
            List<AttachedClassification> classifications = openMetadataElement.getClassifications();

            if (classifications != null)
            {
                for (AttachedClassification classification : classifications)
                {
                    if (classificationName.equals(classification.getClassificationName()))
                    {
                        return classification;
                    }
                }
            }
        }

        return null;
    }


    public boolean isClassified(ElementHeader elementHeader,
                                String        classificationName)
    {
        if (elementHeader== null || elementHeader.getClassifications() == null)
        {
            return false;
        }

        for (ElementClassification classification : elementHeader.getClassifications())
        {
            if ((classification != null) && (classificationName.equals(classification.getClassificationName())))
            {
                return true;
            }
        }

        return false;
    }



    /**
     * Extract a particular property from a classification if found attached to an open metadata element.
     *
     * @param openMetadataElement source element
     * @param classificationName name of the classification to extract
     * @param propertyName name of property to extract
     * @param methodName calling method
     * @return string property or null
     */
    public String getStringPropertyFromClassification(OpenMetadataElement openMetadataElement,
                                                      String              classificationName,
                                                      String              propertyName,
                                                      String              methodName)
    {
        AttachedClassification classification = this.getClassification(openMetadataElement, classificationName);

        if (classification != null)
        {
            return this.getStringProperty(classificationName,
                                          propertyName,
                                          classification.getClassificationProperties(),
                                          methodName);
        }

        return null;
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
                                       Exception  unexpectedException)
    {
        throw new GAFRuntimeException(GAFErrorCode.HELPER_LOGIC_EXCEPTION.getMessageDefinition(sourceName,
                                                                                               localMethodName,
                                                                                               originatingMethodName),
                                          this.getClass().getName(),
                                          localMethodName,
                                          unexpectedException);
    }
}
