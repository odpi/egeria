/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.outtopic;

import org.odpi.openmetadata.frameworks.openmetadata.search.ArrayTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.EnumTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.MapTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.StructTypePropertyValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Limits the size of each property in an out topic event, so that the event fits within the largest message that
 * the event bus accepts (1 MiB by default for Apache Kafka).  An event that is too large is dropped by the event bus
 * and every listener misses the change: a Jacquard connector activity report listing 2,454 elements serialised to
 * 1.3 MB and was lost this way.
 * <br><br>
 * A property whose serialised form is estimated to be larger than the limit is shortened: an array or map keeps
 * as many of its leading values as fit, a string is truncated, and any other value is left out.  The stored
 * element is not changed - a listener that needs the whole value reads the element.
 * <br><br>
 * The size is estimated from the values rather than measured by serialising them, which would double the cost of
 * publishing every event.  The estimate allows for the class and type names that each value carries in JSON, which
 * make up most of an event's size when a property holds many short values such as GUIDs.
 */
public class OutTopicEventPropertyLimiter
{
    /**
     * Default limit for one property: an event carries a property in its current and previous properties, so
     * several properties at the limit still fit well within 1 MiB.
     */
    public static final int DEFAULT_MAXIMUM_PROPERTY_SIZE = 64 * 1024;

    /*
     * Approximate number of bytes of JSON each kind of value adds around its content, measured with Jackson.
     * A primitive value appears twice in the JSON - as a typed value and again in the properties' string view -
     * so its content is counted twice: an array of 1,000 GUIDs serialises to 264,879 bytes.
     */
    private static final int PRIMITIVE_OVERHEAD = 180;
    private static final int ENUM_OVERHEAD      = 150;
    private static final int CONTAINER_OVERHEAD = 80;
    private static final int ENTRY_OVERHEAD     = 6;

    private final int maximumPropertySize;


    /**
     * A property that has been shortened.
     *
     * @param propertyName name of the property
     * @param estimatedSize estimated size of the full value in bytes
     * @param valuesKept number of values (array elements, map entries or characters) sent; zero if the property was left out
     */
    public record ShortenedProperty(String propertyName,
                                    long   estimatedSize,
                                    int    valuesKept) {}


    /**
     * Constructor using the default limit.
     */
    public OutTopicEventPropertyLimiter()
    {
        this(DEFAULT_MAXIMUM_PROPERTY_SIZE);
    }


    /**
     * Constructor.
     *
     * @param maximumPropertySize largest estimated size of one property, in bytes
     */
    public OutTopicEventPropertyLimiter(int maximumPropertySize)
    {
        this.maximumPropertySize = maximumPropertySize;
    }


    /**
     * Return the properties with any property that is too large shortened.  The properties passed in are not
     * changed; if nothing needs shortening, they are returned as they are.
     *
     * @param properties properties for the event
     * @param shortenedProperties list to add a description of each shortened property to
     * @return properties to send
     */
    public ElementProperties limitProperties(ElementProperties       properties,
                                             List<ShortenedProperty> shortenedProperties)
    {
        if ((properties == null) || (properties.getPropertyValueMap() == null))
        {
            return properties;
        }

        Map<String, PropertyValue> limitedValues = null;

        for (Map.Entry<String, PropertyValue> entry : properties.getPropertyValueMap().entrySet())
        {
            long estimatedSize = estimateSize(entry.getValue());

            if (estimatedSize > maximumPropertySize)
            {
                if (limitedValues == null)
                {
                    limitedValues = new HashMap<>(properties.getPropertyValueMap());
                }

                PropertyValue shortenedValue = this.shorten(entry.getValue());

                if (shortenedValue == null)
                {
                    limitedValues.remove(entry.getKey());
                }
                else
                {
                    limitedValues.put(entry.getKey(), shortenedValue);
                }

                shortenedProperties.add(new ShortenedProperty(entry.getKey(), estimatedSize, getValueCount(shortenedValue)));
            }
        }

        if (limitedValues == null)
        {
            return properties;
        }

        ElementProperties limitedProperties = new ElementProperties();

        limitedProperties.setPropertyValueMap(limitedValues);

        return limitedProperties;
    }


    /**
     * Return a shortened copy of a value that is too large, or null if it cannot be shortened.
     *
     * @param propertyValue value
     * @return shortened value or null
     */
    private PropertyValue shorten(PropertyValue propertyValue)
    {
        if (propertyValue instanceof ArrayTypePropertyValue arrayValue)
        {
            List<PropertyValue> keptValues = new ArrayList<>();
            ElementProperties   elements   = arrayValue.getArrayValues();
            long                size       = CONTAINER_OVERHEAD;

            for (int elementNumber = 0; (elements != null) && (elementNumber < arrayValue.getArrayCount()); elementNumber++)
            {
                PropertyValue element = elements.getPropertyValue(Integer.toString(elementNumber));

                size = size + ENTRY_OVERHEAD + Integer.toString(elementNumber).length() + estimateSize(element);

                if (size > maximumPropertySize)
                {
                    break;
                }

                keptValues.add(element);
            }

            ArrayTypePropertyValue shortenedValue = new ArrayTypePropertyValue();

            shortenedValue.setTypeName(arrayValue.getTypeName());
            shortenedValue.setArrayCount(keptValues.size());

            for (int elementNumber = 0; elementNumber < keptValues.size(); elementNumber++)
            {
                shortenedValue.setArrayValue(elementNumber, keptValues.get(elementNumber));
            }

            return shortenedValue;
        }
        else if (propertyValue instanceof MapTypePropertyValue mapValue)
        {
            MapTypePropertyValue shortenedValue = new MapTypePropertyValue();
            ElementProperties    mapValues      = mapValue.getMapValues();
            long                 size           = CONTAINER_OVERHEAD;

            shortenedValue.setTypeName(mapValue.getTypeName());

            if ((mapValues != null) && (mapValues.getPropertyValueMap() != null))
            {
                for (Map.Entry<String, PropertyValue> entry : mapValues.getPropertyValueMap().entrySet())
                {
                    size = size + ENTRY_OVERHEAD + entry.getKey().length() + estimateSize(entry.getValue());

                    if (size > maximumPropertySize)
                    {
                        break;
                    }

                    shortenedValue.setMapValue(entry.getKey(), entry.getValue());
                }
            }

            return shortenedValue;
        }
        else if ((propertyValue instanceof PrimitiveTypePropertyValue primitiveValue) && (primitiveValue.getPrimitiveValue() instanceof String stringValue))
        {
            PrimitiveTypePropertyValue shortenedValue = new PrimitiveTypePropertyValue(primitiveValue);

            shortenedValue.setPrimitiveValue(stringValue.substring(0, Math.max(0, (maximumPropertySize - PRIMITIVE_OVERHEAD) / 2)));

            return shortenedValue;
        }

        return null;
    }


    /**
     * Return the number of values in a shortened value.
     *
     * @param propertyValue shortened value (null if left out)
     * @return count
     */
    private int getValueCount(PropertyValue propertyValue)
    {
        if (propertyValue instanceof ArrayTypePropertyValue arrayValue)
        {
            return arrayValue.getArrayCount();
        }
        else if (propertyValue instanceof MapTypePropertyValue mapValue)
        {
            return mapValue.getMapElementCount();
        }
        else if ((propertyValue instanceof PrimitiveTypePropertyValue primitiveValue) && (primitiveValue.getPrimitiveValue() instanceof String stringValue))
        {
            return stringValue.length();
        }

        return 0;
    }


    /**
     * Estimate the size of a value once serialised as JSON.
     *
     * @param propertyValue value
     * @return estimated number of bytes
     */
    static long estimateSize(PropertyValue propertyValue)
    {
        if (propertyValue == null)
        {
            return 0;
        }
        else if (propertyValue instanceof PrimitiveTypePropertyValue primitiveValue)
        {
            Object value = primitiveValue.getPrimitiveValue();

            return PRIMITIVE_OVERHEAD + ((value == null) ? 0 : 2L * value.toString().length());
        }
        else if (propertyValue instanceof EnumTypePropertyValue enumValue)
        {
            return ENUM_OVERHEAD + ((enumValue.getSymbolicName() == null) ? 0 : enumValue.getSymbolicName().length());
        }
        else if (propertyValue instanceof ArrayTypePropertyValue arrayValue)
        {
            return CONTAINER_OVERHEAD + estimateSize(arrayValue.getArrayValues());
        }
        else if (propertyValue instanceof MapTypePropertyValue mapValue)
        {
            return CONTAINER_OVERHEAD + estimateSize(mapValue.getMapValues());
        }
        else if (propertyValue instanceof StructTypePropertyValue structValue)
        {
            return CONTAINER_OVERHEAD + estimateSize(structValue.getAttributes());
        }

        return CONTAINER_OVERHEAD;
    }


    /**
     * Estimate the size of a set of values once serialised as JSON.
     *
     * @param properties values
     * @return estimated number of bytes
     */
    private static long estimateSize(ElementProperties properties)
    {
        long size = 0;

        if ((properties != null) && (properties.getPropertyValueMap() != null))
        {
            for (Map.Entry<String, PropertyValue> entry : properties.getPropertyValueMap().entrySet())
            {
                size = size + ENTRY_OVERHEAD + entry.getKey().length() + estimateSize(entry.getValue());
            }
        }

        return size;
    }
}
