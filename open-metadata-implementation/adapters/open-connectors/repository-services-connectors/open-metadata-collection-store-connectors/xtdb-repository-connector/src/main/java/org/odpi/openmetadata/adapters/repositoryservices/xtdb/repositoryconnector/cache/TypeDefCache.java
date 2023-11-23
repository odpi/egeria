/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * A shadow cache of all typeDef information.
 *
 * This is necessary to maintain due to fact that caching in the OMRSRepositoryContentManager
 * does not occur until AFTER the OMRSMetadataCollection operation (i.e. addTypeDef) is fully completed: making it
 * impossible to use the repositoryHelper inside any method that addTypeDef itself may call.
 */
public class TypeDefCache {

    private static final ConcurrentMap<String, TypeDef> knownTypeDefs = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Map<String, PropertyKeywords>> typeDefToPropertyKeywords = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, AttributeTypeDef> knownAttributeTypeDefs = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Set<String>> knownPropertyToTypeDefNames = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> nameToGUID = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, InstanceType> knownInstanceTypes = new ConcurrentHashMap<>();

    /**
     * Cache the provided type definition for use across threads.
     * @param typeDef the type definition to cache
     * @throws InvalidParameterException if there is any issue with the contents of the type definition
     */
    public static void addTypeDef(TypeDef typeDef) throws InvalidParameterException {
        knownTypeDefs.put(typeDef.getGUID(), typeDef);
        nameToGUID.put(typeDef.getName(), typeDef.getGUID());
        cacheTypeDefDetails(typeDef);
    }

    /**
     * Retrieve the type definition for the given GUID.
     * @param typeDefGUID unique identifier of the type definition
     * @return TypeDef that matches the GUID, or null if there is no such type definition
     */
    public static TypeDef getTypeDef(String typeDefGUID) {
        return knownTypeDefs.get(typeDefGUID);
    }

    /**
     * Retrieve the type definition for the given name.
     * @param typeDefName unique name of the type definition
     * @return TypeDef that matches the name, or null if there is no such type definition
     */
    public static TypeDef getTypeDefByName(String typeDefName) {
        String typeDefGUID = nameToGUID.get(typeDefName);
        if (typeDefGUID != null) {
            return knownTypeDefs.get(typeDefGUID);
        } else {
            return null;
        }
    }

    /**
     * Remove the cached type definition.
     * @param typeDefGUID unique identifier of the type definition to remove from the cache
     */
    public static void removeTypeDef(String typeDefGUID) {
        TypeDef removed = knownTypeDefs.remove(typeDefGUID);
        if (removed != null) {
            nameToGUID.remove(removed.getName());
            uncacheTypeDefDetails(removed);
        }
    }

    /**
     * Cache the provided attribute type definition for use across threads.
     * @param typeDef the attribute type definition to cache
     */
    public static void addAttributeTypeDef(AttributeTypeDef typeDef) {
        knownAttributeTypeDefs.put(typeDef.getGUID(), typeDef);
    }

    /**
     * Retrieve the attribute type definition for the given GUID.
     * @param typeDefGUID unique identifier of the attribute type definition
     * @return AttributeTypeDef that matches the GUID, or null if there is no such attribute type definition
     */
    public static AttributeTypeDef getAttributeTypeDef(String typeDefGUID) {
        return knownAttributeTypeDefs.get(typeDefGUID);
    }

    /**
     * Remove the cached attribute type definition.
     * @param typeDefGUID unique identifier of the attribute type definition to remove from the cache
     */
    public static void removeAttributeTypeDef(String typeDefGUID) {
        knownAttributeTypeDefs.remove(typeDefGUID);
    }

    /**
     * Return the map of properties to qualified property keywords for the provided type definition.
     * @param typeDefGUID for which to retrieve qualified property keywords
     * @return {@code Map<String, PropertyMapping>} keyed by unqualified property name with the qualified property keywords as the value
     */
    private static Map<String, PropertyKeywords> getPropertyKeywords(String typeDefGUID) {
        return typeDefToPropertyKeywords.get(typeDefGUID);
    }

    /**
     * Retrieve a list of all supertypes of the provided type definition.
     * @param typeDefGUID unique identifier of the type definition for which to retrieve all supertypes
     * @return {@code List<TypeDefLink>} of all supertypes
     */
    public static List<TypeDefLink> getAllSuperTypes(String typeDefGUID) {
        List<TypeDefLink> supers = new ArrayList<>();
        TypeDef start = getTypeDef(typeDefGUID);
        if (start != null) {
            TypeDefLink superTypeLink = start.getSuperType();
            while (superTypeLink != null) {
                TypeDef superType = getTypeDef(superTypeLink.getGUID());
                supers.add(superTypeLink);
                superTypeLink = superType.getSuperType();
            }
        }
        return Collections.unmodifiableList(supers);
    }

    /**
     * Retrieve a map of all property keywords for the provided type definition.
     * @param typeDefGUID unique identifier of the type definition for which to retrieve all properties
     * @return {@code Map<String, PropertyKeywords>} keyed by unqualified (simple) property name with the qualified property keywords as the value
     */
    public static Map<String, PropertyKeywords> getAllPropertyKeywordsForTypeDef(String typeDefGUID) {
        Map<String, PropertyKeywords> map = new LinkedHashMap<>();
        List<TypeDefLink> supers = getAllSuperTypes(typeDefGUID);
        for (int i = supers.size(); i > 0; i--) {
            TypeDefLink typeDefLink = supers.get(i - 1);
            Map<String, PropertyKeywords> superMap = getPropertyKeywords(typeDefLink.getGUID());
            if (superMap != null) {
                map.putAll(superMap);
            }
        }
        Map<String, PropertyKeywords> direct = getPropertyKeywords(typeDefGUID);
        if (direct != null) {
            map.putAll(direct);
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Retrieve the property keywords for the provided property in the specified type definition.
     * @param typeDefGUID unique identifier of the type definition for which to retrieve the property
     * @param propertyName name of the property for which to retrieve the keywords
     * @return PropertyKeywords
     */
    public static PropertyKeywords getPropertyKeywords(String typeDefGUID, String propertyName) {
        Map<String, PropertyKeywords> map = getAllPropertyKeywordsForTypeDef(typeDefGUID);
        return map.get(propertyName);
    }

    /**
     * Validate that an entity's type is of the expected/desired type, which may be a subtype of the expected type.
     *
     * @param actualTypeName name of the entity type
     * @param expectedTypeName name of the expected type
     * @return boolean if they match (a null in actualTypeName results in false; a null in expectedType results in true)
     */
    public static boolean isTypeOf(String actualTypeName, String expectedTypeName) {

        if (expectedTypeName == null) {
            return true;
        }

        if (actualTypeName == null) {
            return false;
        }

        if (actualTypeName.equals(expectedTypeName)) {
            return true;
        }

        // Now look for matches in superTypes...
        String typeDefGUID = nameToGUID.get(actualTypeName);
        TypeDef typeDef = knownTypeDefs.get(typeDefGUID);
        TypeDefLink superTypeLink = typeDef.getSuperType();

        while (superTypeLink != null) {
            TypeDef superTypeDef = knownTypeDefs.get(superTypeLink.getGUID());
            if (expectedTypeName.equals(superTypeLink.getName())) {
                // Short-circuit if we find a match
                return true;
            }
            superTypeLink = superTypeDef.getSuperType();
        }

        // If we've fallen through to here, there was no match, so return false
        return false;

    }

    /**
     * Return the names of all type definitions that define the supplied property name.
     * @param propertyName property name to query.
     * @return set of names of the TypeDefs that define a property with this name
     */
    public static Set<String> getAllTypeDefsForProperty(String propertyName) {
        if (propertyName == null) {
            return Collections.emptySet();
        }
        return knownPropertyToTypeDefNames.getOrDefault(propertyName, null);
    }

    /**
     * Retrieve the namespace for the provided type definition's properties.
     * @param typeDefGUID unique identifier of the type definition for which to retrieve the property namespace
     * @return String
     */
    public static String getPropertyNamespaceForType(String typeDefGUID) {
        TypeDef type = TypeDefCache.getTypeDef(typeDefGUID);
        return getPropertyNamespaceForType(type);
    }

    /**
     * Retrieve the instance type representing the provided type definition.
     * @param category of the type definition
     * @param typeName of the type definition
     * @return InstanceType
     * @throws TypeErrorException if there are any errors determining or matching the type definition
     */
    public static InstanceType getInstanceType(TypeDefCategory category,
                                               String typeName) throws TypeErrorException {

        final String methodName = "getInstanceType";

        InstanceType instanceType = knownInstanceTypes.get(typeName);
        if (instanceType != null) {
            return instanceType;
        }

        TypeDef typeDef = getTypeDefByName(typeName);
        if (typeDef == null) {
            throw new TypeErrorException(XTDBErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE.getMessageDefinition(
                    typeName, category.getName()),
                                         TypeDefCache.class.getName(),
                                         methodName);
        }

        if (isValidTypeCategory(category, typeDef)) {

            String typeDefGUID = typeDef.getGUID();
            instanceType = new InstanceType();
            instanceType.setTypeDefCategory(category);
            instanceType.setTypeDefGUID(typeDefGUID);
            instanceType.setTypeDefName(typeDef.getName());
            instanceType.setTypeDefVersion(typeDef.getVersion());

            // Lazily cache the instance type for next time...
            knownInstanceTypes.put(typeName, instanceType);

            return instanceType;

        } else {
            throw new TypeErrorException(XTDBErrorCode.BAD_CATEGORY_FOR_TYPEDEF_ATTRIBUTE.getMessageDefinition(
                    typeName, category.getName()),
                                         TypeDefCache.class.getName(),
                                         methodName);
        }

    }

    /**
     * Retrieve the initial status to use for an instance of the supplied type.
     * @param typeName of the type definition for which to retrieve the initial status
     * @return InstanceStatus
     */
    public static InstanceStatus getInitialStatus(String typeName) {
        TypeDef typeDef = getTypeDefByName(typeName);
        return typeDef == null ? null : typeDef.getInitialStatus();
    }

    /**
     * Return boolean indicating if a classification type can be applied to a specified entity.
     * @param classificationTypeName name of the classification's type (ClassificationDef)
     * @param entityTypeName name of the entity's type (EntityDef)
     * @return boolean indicating if the classification is valid for the entity (true) or not (false)
     */
    public static boolean isValidClassificationForEntity(String classificationTypeName,
                                                         String entityTypeName) {
        TypeDef candidate = getTypeDefByName(classificationTypeName);
        if (candidate instanceof ClassificationDef) {

            ClassificationDef classificationTypeDef = (ClassificationDef) candidate;

            // Build a set of the valid entities
            List<TypeDefLink> validEntityDefs = classificationTypeDef.getValidEntityDefs();
            if (validEntityDefs == null || validEntityDefs.isEmpty()) {
                return true;
            }
            Set<String> validEntityNames = validEntityDefs.stream().map(TypeDefLink::getName).collect(Collectors.toSet());

            // Build a set of the entity types that we are setting up against
            TypeDef entityDef = getTypeDefByName(entityTypeName);
            if (entityDef == null) {
                return false;
            }
            List<TypeDefLink> actualEntityDefs = new ArrayList<>(getAllSuperTypes(entityDef.getGUID()));
            actualEntityDefs.add(entityDef);
            Set<String> actualEntityNames = actualEntityDefs.stream().map(TypeDefLink::getName).collect(Collectors.toSet());

            // Check if there is any overlap between the two sets
            return !Collections.disjoint(validEntityNames, actualEntityNames);

        }

        return false;

    }

    /**
     * Indicates whether the provided category of type definition matches that of the provided type definition.
     * @param category to match
     * @param typeDef to match against
     * @return true if the provided category matches the type definition, false otherwise
     */
    private static boolean isValidTypeCategory(TypeDefCategory category,
                                               TypeDef typeDef) {
        if (category == null || typeDef == null) {
            return false;
        }
        TypeDefCategory retrievedTypeDefCategory = typeDef.getCategory();
        if (retrievedTypeDefCategory != null) {
            return (category.getOrdinal() == retrievedTypeDefCategory.getOrdinal());
        }
        return false;
    }

    /**
     * Retrieve the namespace for the provided type definition's properties.
     * @param type type definition for which to retrieve the property namespace
     * @return String
     */
    private static String getPropertyNamespaceForType(TypeDef type) {
        TypeDefCategory category = type.getCategory();
        String namespace;
        switch (category) {
            case ENTITY_DEF:
                namespace = EntityDetailMapping.ENTITY_PROPERTIES_NS;
                break;
            case RELATIONSHIP_DEF:
                namespace = RelationshipMapping.RELATIONSHIP_PROPERTIES_NS;
                break;
            case CLASSIFICATION_DEF:
                namespace = ClassificationMapping.getNamespaceForProperties(ClassificationMapping.getNamespaceForClassification(EntitySummaryMapping.N_CLASSIFICATIONS, type.getName()));
                break;
            default:
                namespace = null;
                break;
        }
        return namespace;
    }

    /**
     * Cache the full set of details we will use elsewhere regarding the type.
     * @param type type definition for which to cache details
     * @throws InvalidParameterException if there is any issue with the contents of the type definition
     */
    private static void cacheTypeDefDetails(TypeDef type) throws InvalidParameterException {
        String namespace = getPropertyNamespaceForType(type);
        cachePropertiesInType(type, namespace);
        // as this is lazily-built, clear it if there has been a type change
        knownInstanceTypes.remove(type.getName());
    }

    /**
     * Remove the full set of cached details for the type.
     * @param type type definition for which to remove cached details
     */
    private static void uncacheTypeDefDetails(TypeDef type) {
        String typeDefGUID = type.getGUID();
        String typeDefName = type.getName();
        typeDefToPropertyKeywords.remove(typeDefGUID);
        // Not much choice but to iterate through the entire Map...
        for (String propertyName : knownPropertyToTypeDefNames.keySet()) {
            // ... but the removal operation at least is idempotent (no need to first check it is present in the Set)
            knownPropertyToTypeDefNames.get(propertyName).remove(typeDefName);
        }
        knownInstanceTypes.remove(typeDefName);
    }

    /**
     * Cache all property details within the type into our caches.
     * @param type to check for properties
     * @param namespace to use for qualification
     * @throws InvalidParameterException if there is a property that is unknown or that overlaps with another property
     */
    private static void cachePropertiesInType(TypeDef type,
                                              String namespace) throws InvalidParameterException {

        final String methodName = "cachePropertiesInType";

        Map<String, PropertyKeywords> propertyMap = new LinkedHashMap<>();

        String typeDefName = type.getName();
        List<TypeDefAttribute> properties = type.getPropertiesDefinition();
        if (properties != null) {

            // For each property...
            for (TypeDefAttribute property : properties) {
                String simpleName = property.getAttributeName();
                // Add the keywords to use for persistence
                PropertyKeywords propertyMapping = new PropertyKeywords(namespace, typeDefName, simpleName, property);
                PropertyKeywords previous = propertyMap.put(simpleName, propertyMapping);
                if (previous != null) {
                    throw new InvalidParameterException(XTDBErrorCode.DUPLICATE_PROPERTIES.getMessageDefinition(
                            typeDefName, previous.toString()), TypeDefCache.class.getName(), methodName, simpleName);
                }
                // Add this typeDef's name as one that defines a property with this name
                knownPropertyToTypeDefNames.computeIfAbsent(simpleName, k -> new HashSet<>());
                knownPropertyToTypeDefNames.get(simpleName).add(typeDefName);
            }

        }

        typeDefToPropertyKeywords.put(type.getGUID(), Collections.unmodifiableMap(propertyMap));

    }

}
