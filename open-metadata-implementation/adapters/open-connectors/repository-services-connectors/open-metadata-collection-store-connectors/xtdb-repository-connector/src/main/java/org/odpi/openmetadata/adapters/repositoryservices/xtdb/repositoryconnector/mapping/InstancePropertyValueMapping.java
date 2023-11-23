/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.io.IOException;
import java.util.*;

/**
 * Maps singular InstancePropertyValues between persistence and objects.
 *
 * These cannot simply be serialized to JSON as that would impact the ability to search their values correctly, so we
 * must break apart the values and the types for each property. In general, we will store the complete value into the
 * '.json' portion, but we will also store just the value alone (without any type details) into the '.value' portion.
 *
 * This will allow us to quickly pull back the complete value from a JSON-serialized form (from '.json') while also
 * providing a reliable search point at the '.value'. This class and its subclasses must be responsible for ensuring
 * that these two properties are kept aligned with each other at all times.
 *
 * Furthermore, the naming for the '.value' point used for searching must retain within its overall property name the
 * qualification of which TypeDef defined that property. This is to ensure that different TypeDefinitions that use the same
 * property name, but which have different types (eg. position being a string vs an integer) can be distinguished. This
 * is necessary at a minimum because otherwise we will hit ClassCastExceptions in Clojure due to trying to compare the
 * same property with fundamentally different values (string vs int) if the property name is not qualified with the
 * type in which it is defined. (The '.json' value does not need to be qualified since we do not compare it but only
 * use it for fast-access serde purposes.)
 *
 * See the subclasses of this class, which handle mappings for the various subtypes of InstancePropertyValue for
 * details of each '.value' representation.
 */
public abstract class InstancePropertyValueMapping extends AbstractMapping {

    /**
     * Necessary default constructor to ensure we can use the static objectMapper of the base class.
     */
    protected InstancePropertyValueMapping() {
        super(null);
    }

    /**
     * Convert the provided Egeria value into a XTDB comparable form.
     * @param xtdbConnector connectivity to the repository
     * @param ipv Egeria value to translate to XTDB-comparable value
     * @return Object value that XTDB can compare
     */
    public static Object getValueForComparison(XTDBOMRSRepositoryConnector xtdbConnector, InstancePropertyValue ipv) {
        Object value = null;
        if (ipv != null) {
            InstancePropertyCategory category = ipv.getInstancePropertyCategory();
            switch (category) {
                case PRIMITIVE:
                    value = PrimitivePropertyValueMapping.getPrimitiveValueForComparison((PrimitivePropertyValue) ipv);
                    break;
                case ENUM:
                    value = EnumPropertyValueMapping.getEnumPropertyValueForComparison((EnumPropertyValue) ipv);
                    break;
                case ARRAY:
                    value = ArrayPropertyValueMapping.getArrayPropertyValueForComparison(xtdbConnector, (ArrayPropertyValue) ipv);
                    break;
                case MAP:
                    value = MapPropertyValueMapping.getMapPropertyValueForComparison(xtdbConnector, (MapPropertyValue) ipv);
                    break;
                case STRUCT:
                    value = StructPropertyValueMapping.getStructPropertyValueForComparison(xtdbConnector, (StructPropertyValue) ipv);
                    break;
                case UNKNOWN:
                default:
                    xtdbConnector.logProblem(InstancePropertyValueMapping.class.getName(),
                                             "getValueForComparison",
                                             XTDBAuditCode.UNMAPPED_TYPE,
                                             null,
                                             "InstancePropertyCategory::" + category.name());
                    break;
            }
        }
        return value;
    }

    /**
     * Convert the provided Egeria value into a XTDB comparable form.
     * @param ipv Egeria value to translate to XTDB-comparable value
     * @return Object value that XTDB can compare
     * @throws InvalidParameterException if the value cannot be persisted
     */
    public static Object getValueForComparison(InstancePropertyValue ipv) throws InvalidParameterException {
        final String methodName = "getValueForComparison";
        Object value = null;
        if (ipv != null) {
            InstancePropertyCategory category = ipv.getInstancePropertyCategory();
            switch (category) {
                case PRIMITIVE:
                    value = PrimitivePropertyValueMapping.getPrimitiveValueForComparison((PrimitivePropertyValue) ipv);
                    break;
                case ENUM:
                    value = EnumPropertyValueMapping.getEnumPropertyValueForComparison((EnumPropertyValue) ipv);
                    break;
                case ARRAY:
                    value = ArrayPropertyValueMapping.getArrayPropertyValueForComparison((ArrayPropertyValue) ipv);
                    break;
                case MAP:
                    value = MapPropertyValueMapping.getMapPropertyValueForComparison((MapPropertyValue) ipv);
                    break;
                case STRUCT:
                    value = StructPropertyValueMapping.getStructPropertyValueForComparison((StructPropertyValue) ipv);
                    break;
                case UNKNOWN:
                default:
                    throw new InvalidParameterException(XTDBErrorCode.UNMAPPABLE_PROPERTY.getMessageDefinition(
                            ipv.getTypeName()), InstancePropertyValueMapping.class.getName(), methodName, methodName);
            }
        }
        return value;
    }

    /**
     * Retrieve a single property value from the provided XTDB representation.
     * @param xtdbConnector connectivity to the repository
     * @param xtdbDoc from which to retrieve the value
     * @param namespace by which the property is qualified
     * @param propertyName of the property
     * @return InstancePropertyValue giving Egeria representation of the value
     */
    static InstancePropertyValue getInstancePropertyValueFromDoc(XTDBOMRSRepositoryConnector xtdbConnector,
                                                                 XtdbDocument xtdbDoc,
                                                                 String namespace,
                                                                 String propertyName) {

        // We will only pull values from the '.json'-qualified portion, given this is a complete JSON serialization
        Object objValue = xtdbDoc.get(PropertyKeywords.getSerializedPropertyKeyword(namespace, propertyName));
        IPersistentMap embeddedValue = (objValue instanceof IPersistentMap) ? (IPersistentMap) objValue : null;
        if (embeddedValue != null) {
            return getInstancePropertyValue(xtdbConnector, namespace, propertyName, embeddedValue);
        }
        return null;

    }

    /**
     * Retrieve a single property value from the provided XTDB representation.
     * @param doc from which to retrieve the value
     * @param namespace by which the property is qualified
     * @param propertyName of the property
     * @return InstancePropertyValue giving Egeria representation of the value
     * @throws IOException on any error deserializing the value
     */
    static InstancePropertyValue getInstancePropertyValueFromMap(IPersistentMap doc,
                                                                 String namespace,
                                                                 String propertyName) throws IOException {

        // We will only pull values from the '.json'-qualified portion, given this is a complete JSON serialization
        Object objValue = doc.valAt(Keyword.intern(PropertyKeywords.getSerializedPropertyKeyword(namespace, propertyName)));
        IPersistentMap embeddedValue = (objValue instanceof IPersistentMap) ? (IPersistentMap) objValue : null;
        if (embeddedValue != null) {
            return getInstancePropertyValue(embeddedValue);
        }
        return null;

    }

    /**
     * Add a single property value to the provided XTDB representation.
     * @param xtdbConnector connectivity to the repository
     * @param instanceType describing the instance to which this property applies
     * @param builder through which to add the property
     * @param propertyName of the property to add / replace
     * @param value of the property
     */
    static void addInstancePropertyValueToDoc(XTDBOMRSRepositoryConnector xtdbConnector,
                                              InstanceType instanceType,
                                              XtdbDocument.Builder builder,
                                              String propertyName,
                                              InstancePropertyValue value) {

        final String methodName = "addInstancePropertyValueToDoc";
        // Persist the serialized form in all cases
        PropertyKeywords keywords = TypeDefCache.getPropertyKeywords(instanceType.getTypeDefGUID(), propertyName);
        if (keywords != null) {
            builder.put(keywords.getEmbeddedPath(), getEmbeddedSerializedForm(xtdbConnector, instanceType.getTypeDefName(), propertyName, value));

            // And then also persist a searchable form
            if (value != null) {
                InstancePropertyCategory category = value.getInstancePropertyCategory();
                switch (category) {
                    case PRIMITIVE:
                        PrimitivePropertyValueMapping.addPrimitivePropertyValueToDoc(
                                builder,
                                keywords,
                                (PrimitivePropertyValue) value
                        );
                        break;
                    case ENUM:
                        EnumPropertyValueMapping.addEnumPropertyValueToDoc(
                                builder,
                                keywords,
                                (EnumPropertyValue) value
                        );
                        break;
                    case ARRAY:
                        ArrayPropertyValueMapping.addArrayPropertyValueToDoc(
                                xtdbConnector,
                                builder,
                                keywords,
                                (ArrayPropertyValue) value
                        );
                        break;
                    case MAP:
                        MapPropertyValueMapping.addMapPropertyValueToDoc(
                                xtdbConnector,
                                builder,
                                keywords,
                                (MapPropertyValue) value
                        );
                        break;
                    case STRUCT:
                        StructPropertyValueMapping.addStructPropertyValueToDoc(
                                xtdbConnector,
                                builder,
                                keywords,
                                (StructPropertyValue) value
                        );
                        break;
                    case UNKNOWN:
                    default:
                        xtdbConnector.logProblem(InstancePropertyValueMapping.class.getName(),
                                                 "addInstancePropertyValueToDoc",
                                                 XTDBAuditCode.UNMAPPED_TYPE,
                                                 null,
                                                 "InstancePropertyValueCategory::" + category.name());
                        break;
                }
            } else {
                // If the value is null, create a null mapping for it (so we explicitly set the property to null for searching)
                builder.put(keywords.getSearchablePath(), null);
            }
        } else {
            xtdbConnector.logProblem(InstancePropertyValueMapping.class.getName(),
                                     methodName,
                                     XTDBAuditCode.UNMAPPED_PROPERTY,
                                     null,
                                     propertyName, instanceType.getTypeDefName());
        }

    }

    /**
     * Add a single property value to the provided XTDB representation.
     * @param doc the XTDB map to which to add the property
     * @param propertyKeywords the property whose value should be set
     * @param value of the property
     * @return IPersistentMap containing the updated value
     * @throws InvalidParameterException if the value cannot be persisted
     * @throws IOException on any error serializing the value
     */
    public static IPersistentMap addInstancePropertyValueToDoc(IPersistentMap doc,
                                                               PropertyKeywords propertyKeywords,
                                                               InstancePropertyValue value)
            throws InvalidParameterException, IOException {

        final String methodName = "addInstancePropertyValueToDoc";
        // Persist the serialized form in all cases
        doc = doc.assoc(propertyKeywords.getEmbeddedKeyword(), getEmbeddedSerializedForm(value));
        Keyword searchablePropertyKeyword = propertyKeywords.getSearchableKeyword();

        // And then also persist a searchable form
        if (value != null) {
            InstancePropertyCategory category = value.getInstancePropertyCategory();
            switch (category) {
                case PRIMITIVE:
                    doc = PrimitivePropertyValueMapping.addPrimitivePropertyValueToDoc(doc,
                            searchablePropertyKeyword,
                            (PrimitivePropertyValue) value);
                    break;
                case ENUM:
                    doc = EnumPropertyValueMapping.addEnumPropertyValueToDoc(doc,
                            searchablePropertyKeyword,
                            (EnumPropertyValue) value);
                    break;
                case ARRAY:
                    doc = ArrayPropertyValueMapping.addArrayPropertyValueToDoc(doc,
                            searchablePropertyKeyword,
                            (ArrayPropertyValue) value);
                    break;
                case MAP:
                    doc = MapPropertyValueMapping.addMapPropertyValueToDoc(doc,
                            searchablePropertyKeyword,
                            (MapPropertyValue) value);
                    break;
                case STRUCT:
                    doc = StructPropertyValueMapping.addStructPropertyValueToDoc(doc,
                            searchablePropertyKeyword,
                            (StructPropertyValue) value);
                    break;
                case UNKNOWN:
                default:
                    throw new InvalidParameterException(XTDBErrorCode.UNMAPPABLE_PROPERTY.getMessageDefinition(
                            value.getTypeName()), InstancePropertyValueMapping.class.getName(), methodName, propertyKeywords.getPropertyName());
            }
        } else {
            // If the value is null, create a null mapping for it (so we explicitly set the property to null for searching)
            doc = doc.assoc(searchablePropertyKeyword, null);
        }

        return doc;

    }

    /**
     * Translate the provided JSON representation of a value into an Egeria object.
     * @param xtdbConnector connectivity to the repository
     * @param namespace of the property
     * @param property name of the property
     * @param jsonValue to translate
     * @return InstancePropertyValue
     */
    private static InstancePropertyValue getInstancePropertyValue(XTDBOMRSRepositoryConnector xtdbConnector, String namespace, String property, IPersistentMap jsonValue) {
        return getDeserializedValue(xtdbConnector, namespace, property, jsonValue, mapper.getTypeFactory().constructType(InstancePropertyValue.class));
    }

    /**
     * Translate the provided JSON representation of a value into an Egeria object.
     * @param jsonValue to translate
     * @return InstancePropertyValue
     * @throws IOException on any error deserializing the value
     */
    private static InstancePropertyValue getInstancePropertyValue(IPersistentMap jsonValue) throws IOException {
        return getDeserializedValue(jsonValue, mapper.getTypeFactory().constructType(InstancePropertyValue.class));
    }

    /**
     * Retrieve the fully-qualified names for the provided property, everywhere it could appear within a given type.
     * Note that generally the returned Set will only have a single element, however if requested from a sufficiently
     * abstract type (eg. Referenceable) under which different subtypes have the same property defined, the Set will
     * contain a property reference for each of those subtypes' properties.
     * @param xtdbConnector connectivity to the repository
     * @param propertyName of the property for which to qualify type-specific references
     * @param namespace under which to qualify the properties
     * @param limitToTypes limit the type-specific qualifications to only properties that are applicable to these types
     * @param value that will be used for comparison, to limit the properties to include based on their type
     * @return {@code Set<String>} of the property references
     */
    private static Set<String> getNamesForProperty(XTDBOMRSRepositoryConnector xtdbConnector,
                                                   String propertyName,
                                                   String namespace,
                                                   Set<String> limitToTypes,
                                                   InstancePropertyValue value) {
        // Start by determining all valid combinations of propertyName in every type name provided in limitToTypes
        Set<String> validTypesForProperty = TypeDefCache.getAllTypeDefsForProperty(propertyName);
        Set<String> qualifiedNames = new TreeSet<>();

        // since the property itself may actually be defined at the super-type level of one of the limited types, we
        // cannot simply do a set intersection between types but must traverse and take the appropriate (super)type name
        // for qualification
        if (validTypesForProperty != null) {
            for (String typeNameWithProperty : validTypesForProperty) {
                String searchableKeyword = PropertyKeywords.getSearchableValueKeyword(namespace, typeNameWithProperty, propertyName);
                if (!qualifiedNames.contains(searchableKeyword)) { // short-circuit if we already have this one in the list
                    for (String limitToType : limitToTypes) {
                        // Only if the type definition by which we are limiting is a subtype of this type definition should
                        // we consider the type definitions' properties
                        if (TypeDefCache.isTypeOf(limitToType, typeNameWithProperty)) {
                            // Only if the property's types align do we continue with ensuring that the type itself should be included
                            // (While this conditional itself will further loop over cached information, it should do so only
                            // in limited cases due to the short-circuiting above)
                            if (propertyDefMatchesValueType(xtdbConnector, typeNameWithProperty, propertyName, value)) {
                                qualifiedNames.add(searchableKeyword);
                            }
                        }
                    }
                }
            }
        }
        return qualifiedNames;
    }

    /**
     * Retrieve the fully-qualified names for the provided property, everywhere it could appear within a given type.
     * Note that generally the returned Set will only have a single element, however if requested from a sufficiently
     * abstract type (eg. Referenceable) under which different subtypes have the same property defined, the Set will
     * contain a property reference for each of those subtypes' properties.
     * @param xtdbConnector connectivity to the repository
     * @param propertyName of the property for which to qualify type-specific references
     * @param namespace under which to qualify the properties
     * @param limitToTypes limit the type-specific qualifications to only properties that are applicable to these types
     * @param value that will be used for comparison, to limit the properties to include based on their type
     * @return {@code Set<Keyword>} of the property references
     */
    public static Set<Keyword> getKeywordsForProperty(XTDBOMRSRepositoryConnector xtdbConnector,
                                                      String propertyName,
                                                      String namespace,
                                                      Set<String> limitToTypes,
                                                      InstancePropertyValue value) {
        Set<Keyword> keywords = new TreeSet<>();
        Set<String> strings = getNamesForProperty(xtdbConnector, propertyName, namespace, limitToTypes, value);
        for (String string : strings) {
            keywords.add(Keyword.intern(string));
        }
        return keywords;
    }

    /**
     * Indicates whether the provided property value is of the same type as the named property in the specified type
     * definition.
     * @param xtdbConnector connectivity to the repository
     * @param typeDefName of the type definition in which the property is defined
     * @param propertyName of the property for which to check the type definition
     * @param value that will be used for comparison, to limit the properties to include based on their type
     * @return boolean true if the value's type matches the property definition's type, otherwise false (if they do not match)
     */
    private static boolean propertyDefMatchesValueType(XTDBOMRSRepositoryConnector xtdbConnector,
                                                       String typeDefName,
                                                       String propertyName,
                                                       InstancePropertyValue value) {

        if (value == null) {
            // If the value is null, we cannot compare types, so must assume that they would match
            return true;
        }

        // Otherwise, determine the type of this property in the model, and only if they match consider including
        // this property
        TypeDef typeDef = TypeDefCache.getTypeDefByName(typeDefName);
        if (typeDef == null) {
            return false;
        }
        List<TypeDefAttribute> typeDefProperties = typeDef.getPropertiesDefinition();
        for (TypeDefAttribute typeDefProperty : typeDefProperties) {
            // Start by finding the property
            if (typeDefProperty.getAttributeName().equals(propertyName)) {
                AttributeTypeDef atd = typeDefProperty.getAttributeType();
                switch (atd.getCategory()) {
                    case PRIMITIVE:
                        PrimitiveDef pd = (PrimitiveDef) atd;
                        PrimitiveDefCategory pdc = pd.getPrimitiveDefCategory();
                        // In the case of a primitive, the value must either be an array (necessary for IN comparison)
                        // or also be a primitive, in which case its primitive type must match
                        return (value.getInstancePropertyCategory().equals(InstancePropertyCategory.ARRAY)) ||
                                (value.getInstancePropertyCategory().equals(InstancePropertyCategory.PRIMITIVE)
                                && ((PrimitivePropertyValue) value).getPrimitiveDefCategory().equals(pdc));
                    case ENUM_DEF:
                        return (value.getInstancePropertyCategory().equals(InstancePropertyCategory.ARRAY)) ||
                                (value.getInstancePropertyCategory().equals(InstancePropertyCategory.ENUM));
                    case COLLECTION:
                        CollectionDef cd = (CollectionDef) atd;
                        switch (cd.getCollectionDefCategory()) {
                            // TODO: these may need deeper checks (eg. that the types within the array match, etc)
                            case OM_COLLECTION_ARRAY:
                                return (value.getInstancePropertyCategory().equals(InstancePropertyCategory.ARRAY));
                            case OM_COLLECTION_MAP:
                                return (value.getInstancePropertyCategory().equals(InstancePropertyCategory.MAP));
                            case OM_COLLECTION_STRUCT:
                                return (value.getInstancePropertyCategory().equals(InstancePropertyCategory.STRUCT));
                            case OM_COLLECTION_UNKNOWN:
                            default:
                                xtdbConnector.logProblem(InstancePropertyValueMapping.class.getName(),
                                                         "propertyDefMatchesValueType",
                                                         XTDBAuditCode.UNMAPPED_TYPE,
                                                         null,
                                                         "CollectionDefCategory::" + cd.getCollectionDefCategory());
                                break;
                        }
                        break;
                    case UNKNOWN_DEF:
                    default:
                        xtdbConnector.logProblem(InstancePropertyValueMapping.class.getName(),
                                                 "propertyDefMatchesValueType",
                                                 XTDBAuditCode.UNMAPPED_TYPE,
                                                 null,
                                                 "AttributeTypeDefCategory::" + atd.getCategory());
                        break;
                }
            }
        }

        // If we have fallen through, the value does not have the same type as the property
        return false;

    }

}
