/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataCollectionDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataCollectionDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEnumDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEnumElementDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataPrimitiveDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataPrimitiveDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TypeValueFactory turns a type's attribute definitions into a set of properties that an instance of that
 * type can actually be created with, and remembers what each one should look like when it is read back.
 * <br>
 * The point of the suite is to prove that every declared attribute of every type survives a round trip
 * through the connector context and the repository, so a value has to be produced for every data type the
 * model uses - primitives, dates, enums, string arrays and the various map flavours - not just for strings.
 * Values are derived from the attribute name so that a failure message shows which property went wrong and
 * what it should have contained.
 * <br>
 * Comparison is done on {@link ElementProperties#getPropertiesAsStrings()} rather than on the typed values.
 * That is deliberate: it is the one representation available uniformly across every property value class,
 * it is what a caller reading the property back generically will see, and it keeps this factory from having
 * to know how each of those classes chooses to model its contents.
 */
final class TypeValueFactory
{
    private final PropertyHelper propertyHelper = new PropertyHelper();

    /** Names of the properties this factory populated. */
    private final Set<String> populatedNames = new LinkedHashSet<>();

    /** Attributes whose data type this factory does not know how to populate, keyed by property name. */
    private final Map<String, String> unsupportedAttributes = new LinkedHashMap<>();

    private ElementProperties elementProperties = null;


    /**
     * Build a value for every supplied attribute.
     *
     * @param attributes attributes to populate
     * @param seed text mixed into generated values so that two instances created in the same run differ
     */
    TypeValueFactory(List<OpenMetadataTypeDefAttribute> attributes,
                     String                             seed)
    {
        for (OpenMetadataTypeDefAttribute attribute : attributes)
        {
            addValue(attribute, seed);
        }
    }


    /**
     * Return the properties to create or update an instance with.
     *
     * @return properties, or null if the type declares no attributes this factory can populate
     */
    ElementProperties getElementProperties()
    {
        return elementProperties;
    }


    /**
     * Return what each populated property should read back as.
     * <br>
     * The expected form is taken from the very properties object that was sent, rendered by the same
     * {@code valueAsString()} implementations that will render the properties read back afterwards.  That
     * keeps the comparison honest about what actually matters - did the value survive - without this class
     * having to predict how each property value class chooses to print itself, which would turn a
     * formatting change into a wave of false failures.
     *
     * @return map of property name to expected string form
     */
    Map<String, String> getExpectedValues()
    {
        Map<String, String> expected = new LinkedHashMap<>();

        if (elementProperties != null)
        {
            Map<String, String> asStrings = elementProperties.getPropertiesAsStrings();

            if (asStrings != null)
            {
                for (String name : populatedNames)
                {
                    if (asStrings.containsKey(name))
                    {
                        expected.put(name, asStrings.get(name));
                    }
                }
            }
        }

        return expected;
    }


    /**
     * Return any attribute this factory could not produce a value for, with the data type that defeated it.
     * <br>
     * A non-empty result is a finding, not a nuisance: it means the model has grown a data type that nothing
     * in this suite - and quite possibly nothing in the wider codebase - knows how to write.
     *
     * @return map of property name to a description of its data type
     */
    Map<String, String> getUnsupportedAttributes()
    {
        return unsupportedAttributes;
    }


    /**
     * Produce and record a value for one attribute.
     *
     * @param attribute attribute to populate
     * @param seed text mixed into the generated value
     */
    private void addValue(OpenMetadataTypeDefAttribute attribute,
                          String                       seed)
    {
        String                       name          = attribute.getAttributeName();
        OpenMetadataAttributeTypeDef attributeType = attribute.getAttributeType();

        if ((name == null) || (attributeType == null))
        {
            return;
        }

        if (attributeType instanceof OpenMetadataPrimitiveDef primitiveDef)
        {
            addPrimitiveValue(name, primitiveDef.getPrimitiveDefCategory(), seed);
        }
        else if (attributeType instanceof OpenMetadataEnumDef enumDef)
        {
            addEnumValue(name, enumDef);
        }
        else if (attributeType instanceof OpenMetadataCollectionDef collectionDef)
        {
            addCollectionValue(name, collectionDef, seed);
        }
        else
        {
            unsupportedAttributes.put(name, attributeType.getClass().getSimpleName());
        }
    }


    /**
     * Add a value for a primitive attribute.
     *
     * @param name property name
     * @param category primitive category
     * @param seed text mixed into the generated value
     */
    private void addPrimitiveValue(String                           name,
                                   OpenMetadataPrimitiveDefCategory category,
                                   String                           seed)
    {
        if (category == null)
        {
            unsupportedAttributes.put(name, "primitive with no category");
            return;
        }

        switch (category)
        {
            case OM_PRIMITIVE_TYPE_STRING ->
            {
                String value = "type-fvt " + name + " " + seed;
                elementProperties = propertyHelper.addStringProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_INT, OM_PRIMITIVE_TYPE_SHORT, OM_PRIMITIVE_TYPE_BYTE ->
            {
                int value = numericValue(name, 1000);
                elementProperties = propertyHelper.addIntProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_LONG, OM_PRIMITIVE_TYPE_BIGINTEGER ->
            {
                long value = numericValue(name, 1000);
                elementProperties = propertyHelper.addLongProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_FLOAT, OM_PRIMITIVE_TYPE_DOUBLE, OM_PRIMITIVE_TYPE_BIGDECIMAL ->
            {
                float value = numericValue(name, 100) + 0.5f;
                elementProperties = propertyHelper.addFloatProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_BOOLEAN ->
            {
                elementProperties = propertyHelper.addBooleanProperty(elementProperties, name, true);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_DATE ->
            {
                // A whole number of milliseconds well inside the range every store can hold, and fixed
                // rather than "now" so that a failure message is reproducible.
                Date value = new Date(1_700_000_000_000L + numericValue(name, 1000));
                elementProperties = propertyHelper.addDateProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_CHAR ->
            {
                // No addCharProperty exists; a char is carried as a single-character string.
                String value = Character.toString((char) ('A' + (Math.abs(name.hashCode()) % 26)));
                elementProperties = propertyHelper.addStringProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            default -> unsupportedAttributes.put(name, "primitive " + category.getName());
        }
    }


    /**
     * Add a value for an enum attribute, chosen from the enum's own element list so that it is always a
     * legal value for that enum however the enum is later extended.
     *
     * @param name property name
     * @param enumDef enum definition
     */
    private void addEnumValue(String              name,
                              OpenMetadataEnumDef enumDef)
    {
        List<OpenMetadataEnumElementDef> elements = enumDef.getElementDefs();

        if ((elements == null) || elements.isEmpty())
        {
            unsupportedAttributes.put(name, "enum " + enumDef.getName() + " has no values");
            return;
        }

        // The first element is used rather than a random one so that a failing run can be repeated exactly.
        OpenMetadataEnumElementDef element = elements.get(0);

        elementProperties = propertyHelper.addEnumProperty(elementProperties, name, enumDef.getName(), element.getValue());
                populatedNames.add(name);
    }


    /**
     * Add a value for an array or map attribute.
     *
     * @param name property name
     * @param collectionDef collection definition
     * @param seed text mixed into the generated value
     */
    private void addCollectionValue(String                    name,
                                    OpenMetadataCollectionDef collectionDef,
                                    String                    seed)
    {
        OpenMetadataCollectionDefCategory category = collectionDef.getCollectionDefCategory();

        if (category == OpenMetadataCollectionDefCategory.OM_COLLECTION_ARRAY)
        {
            List<String> value = List.of("type-fvt " + name + " 0 " + seed, "type-fvt " + name + " 1 " + seed);

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties, name, value);
                populatedNames.add(name);
        }
        else if (category == OpenMetadataCollectionDefCategory.OM_COLLECTION_MAP)
        {
            addMapValue(name, collectionDef, seed);
        }
        else
        {
            unsupportedAttributes.put(name, "collection " + ((category == null) ? "with no category" : category.getName()));
        }
    }


    /**
     * Add a value for a map attribute, using the map's declared value type rather than assuming strings.
     * <br>
     * This matters more than it looks.  Roughly a third of the map attributes in the model hold something
     * other than strings - ints, longs, booleans, dates, doubles - and the repository will happily store a
     * string in any of them.  Writing strings everywhere would therefore pass, while proving nothing about
     * whether a {@code map<string,int>} can actually carry ints.
     *
     * @param name property name
     * @param collectionDef collection definition
     * @param seed text mixed into the generated value
     */
    private void addMapValue(String                    name,
                             OpenMetadataCollectionDef collectionDef,
                             String                    seed)
    {
        List<OpenMetadataPrimitiveDefCategory> argumentTypes = collectionDef.getArgumentTypes();

        // A map declares [keyType, valueType]; anything else is not a shape this factory understands.
        OpenMetadataPrimitiveDefCategory valueType = ((argumentTypes != null) && (argumentTypes.size() == 2))
                                                             ? argumentTypes.get(1)
                                                             : null;

        if (valueType == null)
        {
            unsupportedAttributes.put(name, "map with no declared value type");
            return;
        }

        String key0 = "type-fvt-key-0";
        String key1 = "type-fvt-key-1";

        switch (valueType)
        {
            case OM_PRIMITIVE_TYPE_INT, OM_PRIMITIVE_TYPE_SHORT, OM_PRIMITIVE_TYPE_BYTE ->
            {
                Map<String, Integer> value = new LinkedHashMap<>();

                value.put(key0, numericValue(name, 1000));
                value.put(key1, numericValue(name, 1000) + 1);

                elementProperties = propertyHelper.addIntMapProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_LONG, OM_PRIMITIVE_TYPE_BIGINTEGER ->
            {
                Map<String, Long> value = new LinkedHashMap<>();

                value.put(key0, (long) numericValue(name, 1000));
                value.put(key1, (long) numericValue(name, 1000) + 1L);

                elementProperties = propertyHelper.addLongMapProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_BOOLEAN ->
            {
                Map<String, Boolean> value = new LinkedHashMap<>();

                value.put(key0, true);
                value.put(key1, false);

                elementProperties = propertyHelper.addBooleanMapProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_DATE ->
            {
                Map<String, Date> value = new LinkedHashMap<>();

                value.put(key0, new Date(1_700_000_000_000L + numericValue(name, 1000)));
                value.put(key1, new Date(1_700_000_001_000L + numericValue(name, 1000)));

                elementProperties = propertyHelper.addDateMapProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_DOUBLE, OM_PRIMITIVE_TYPE_FLOAT, OM_PRIMITIVE_TYPE_BIGDECIMAL ->
            {
                Map<String, Double> value = new LinkedHashMap<>();

                value.put(key0, numericValue(name, 100) + 0.5d);
                value.put(key1, numericValue(name, 100) + 1.5d);

                elementProperties = propertyHelper.addDoubleMapProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            case OM_PRIMITIVE_TYPE_STRING, OM_PRIMITIVE_TYPE_CHAR, OM_PRIMITIVE_TYPE_UNKNOWN ->
            {
                // OM_PRIMITIVE_TYPE_UNKNOWN is how map<string,object> arrives - a map that accepts anything,
                // so a string is a legitimate thing to put in it.
                Map<String, String> value = new LinkedHashMap<>();

                value.put(key0, "type-fvt " + name + " 0 " + seed);
                value.put(key1, "type-fvt " + name + " 1 " + seed);

                elementProperties = propertyHelper.addStringMapProperty(elementProperties, name, value);
                populatedNames.add(name);
            }
            default -> unsupportedAttributes.put(name, "map of " + valueType.getName());
        }
    }

    /**
     * Derive a small, stable, non-negative number from a property name, so that different properties of the
     * same instance hold different values and a failure that swaps two properties over is still caught.
     *
     * @param name property name
     * @param bound exclusive upper bound
     * @return number in the range 0..bound-1
     */
    private static int numericValue(String name,
                                    int    bound)
    {
        return Math.abs(name.hashCode()) % bound;
    }
}
