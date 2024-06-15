/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMRSRepositoryHelper provides methods to repository connectors and repository event mappers to help
 * them build valid type definitions (TypeDefs), entities and relationships.  It is a facade to the
 * repository content manager which holds an in memory cache of all the active TypeDefs in the local server.
 * OMRSRepositoryHelper's purpose is to create an object that the repository connectors and event mappers can
 * create, use and discard without needing to know how to connect to the repository content manager.
 */
public interface OMRSRepositoryPropertiesHelper
{
    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a string property then a logic exception is thrown
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    String getStringProperty(String             sourceName,
                             String             propertyName,
                             InstanceProperties properties,
                             String             methodName);


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
    String removeStringProperty(String             sourceName,
                                String             propertyName,
                                InstanceProperties properties,
                                String             methodName);


    /**
     * Retrieve the ordinal value from an enum property.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return int ordinal or -1 if not found
     */
    int getEnumPropertyOrdinal(String             sourceName,
                               String             propertyName,
                               InstanceProperties properties,
                               String             methodName);


    /**
     * Retrieve the ordinal value from an enum property and then delete it from the properties
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return int ordinal or -1 if not found
     */
    int removeEnumPropertyOrdinal(String             sourceName,
                                  String             propertyName,
                                  InstanceProperties properties,
                                  String             methodName);


    /**
     * Return the requested property or null if property is not found.  If the property is found, it is removed from
     * the InstanceProperties structure. If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    InstanceProperties getMapProperty(String             sourceName,
                                      String             propertyName,
                                      InstanceProperties properties,
                                      String             methodName);



    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    Map<String, String> getStringMapFromProperty(String             sourceName,
                                                 String             propertyName,
                                                 InstanceProperties properties,
                                                 String             methodName);


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
    Map<String, String> removeStringMapFromProperty(String             sourceName,
                                                    String             propertyName,
                                                    InstanceProperties properties,
                                                    String             methodName);


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    Map<String, Boolean> getBooleanMapFromProperty(String             sourceName,
                                                   String             propertyName,
                                                   InstanceProperties properties,
                                                   String             methodName);


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
    Map<String, Boolean> removeBooleanMapFromProperty(String             sourceName,
                                                      String             propertyName,
                                                      InstanceProperties properties,
                                                      String             methodName);


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    Map<String, Long> getLongMapFromProperty(String             sourceName,
                                             String             propertyName,
                                             InstanceProperties properties,
                                             String             methodName);


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
    Map<String, Long> removeLongMapFromProperty(String             sourceName,
                                                String             propertyName,
                                                InstanceProperties properties,
                                                String             methodName);


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    Map<String, Date> getDateMapFromProperty(String             sourceName,
                                             String             propertyName,
                                             InstanceProperties properties,
                                             String             methodName);


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
    Map<String, Date> removeDateMapFromProperty(String             sourceName,
                                                String             propertyName,
                                                InstanceProperties properties,
                                                String             methodName);


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    Map<String, Double> getDoubleMapFromProperty(String             sourceName,
                                                 String             propertyName,
                                                 InstanceProperties properties,
                                                 String             methodName);

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
    Map<String, Double> removeDoubleMapFromProperty(String             sourceName,
                                                    String             propertyName,
                                                    InstanceProperties properties,
                                                    String             methodName);


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    Map<String, Integer> getIntegerMapFromProperty(String             sourceName,
                                                   String             propertyName,
                                                   InstanceProperties properties,
                                                   String             methodName);


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
    Map<String, Integer> removeIntegerMapFromProperty(String             sourceName,
                                                      String             propertyName,
                                                      InstanceProperties properties,
                                                      String             methodName);


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    Map<String, List<String>> getStringArrayStringMapFromProperty(String             sourceName,
                                                                  String             propertyName,
                                                                  InstanceProperties properties,
                                                                  String             methodName);


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
    Map<String, List<String>> removeStringArrayStringMapFromProperty(String             sourceName,
                                                                     String             propertyName,
                                                                     InstanceProperties properties,
                                                                     String             methodName);


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
    Map<String, Object> getMapFromProperty(String             sourceName,
                                           String             propertyName,
                                           InstanceProperties properties,
                                           String             methodName);


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
    Map<String, Object> removeMapFromProperty(String             sourceName,
                                              String             propertyName,
                                              InstanceProperties properties,
                                              String             methodName);

    /**
     * Convert an instance properties object into a map.
     *
     * @param instanceProperties packed properties
     * @return properties stored in Java map
     */
    Map<String, Object> getInstancePropertiesAsMap(InstanceProperties instanceProperties);


    /**
     * Locates and extracts a string array property and extracts its values.
     * If the property is not an array property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all the properties of the instance
     * @param methodName method of caller
     * @return array property value or null
     */
    List<String> getStringArrayProperty(String             sourceName,
                                        String             propertyName,
                                        InstanceProperties properties,
                                        String             methodName);


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
    List<String> removeStringArrayProperty(String             sourceName,
                                           String             propertyName,
                                           InstanceProperties properties,
                                           String             methodName);


    /**
     * Return the requested property or 0 if property is not found.  If the property is not
     * an int property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    int    getIntProperty(String             sourceName,
                          String             propertyName,
                          InstanceProperties properties,
                          String             methodName);


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
    int    removeIntProperty(String             sourceName,
                             String             propertyName,
                             InstanceProperties properties,
                             String             methodName);


    /**
     * Return the requested property or 0 if property is not found.  If the property is not
     * an int property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    long  getLongProperty(String             sourceName,
                          String             propertyName,
                          InstanceProperties properties,
                          String             methodName);


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
    long   removeLongProperty(String             sourceName,
                              String             propertyName,
                              InstanceProperties properties,
                              String             methodName);


    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a date property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    Date   getDateProperty(String             sourceName,
                           String             propertyName,
                           InstanceProperties properties,
                           String             methodName);


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
    Date   removeDateProperty(String             sourceName,
                              String             propertyName,
                              InstanceProperties properties,
                              String             methodName);


    /**
     * Return the requested property or false if property is not found.  If the property is not
     * a boolean property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    boolean getBooleanProperty(String             sourceName,
                               String             propertyName,
                               InstanceProperties properties,
                               String             methodName);


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
    boolean removeBooleanProperty(String             sourceName,
                                  String             propertyName,
                                  InstanceProperties properties,
                                  String             methodName);


    /**
     * If the supplied property is not null, add it to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addStringPropertyToInstance(String             sourceName,
                                                   InstanceProperties properties,
                                                   String             propertyName,
                                                   String             propertyValue,
                                                   String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addIntPropertyToInstance(String             sourceName,
                                                InstanceProperties properties,
                                                String             propertyName,
                                                int                propertyValue,
                                                String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addLongPropertyToInstance(String             sourceName,
                                                 InstanceProperties properties,
                                                 String             propertyName,
                                                 long               propertyValue,
                                                 String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addDoublePropertyToInstance(String             sourceName,
                                                   InstanceProperties properties,
                                                   String             propertyName,
                                                   double             propertyValue,
                                                   String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addFloatPropertyToInstance(String             sourceName,
                                                  InstanceProperties properties,
                                                  String             propertyName,
                                                  float              propertyValue,
                                                  String             methodName);


    /**
     * If the supplied property is not null, add it to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addDatePropertyToInstance(String             sourceName,
                                                 InstanceProperties properties,
                                                 String             propertyName,
                                                 Date               propertyValue,
                                                 String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addBooleanPropertyToInstance(String             sourceName,
                                                    InstanceProperties properties,
                                                    String             propertyName,
                                                    boolean            propertyValue,
                                                    String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param ordinal  numeric value of property
     * @param symbolicName  String value of property
     * @param description  String description of property value
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addEnumPropertyToInstance(String             sourceName,
                                                 InstanceProperties properties,
                                                 String             propertyName,
                                                 int                ordinal,
                                                 String             symbolicName,
                                                 String             description,
                                                 String             methodName);


    /**
     * If the supplied array property is not null, add it to an instance properties object.  The supplied array is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param arrayValues contents of the array
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addStringArrayPropertyToInstance(String             sourceName,
                                                        InstanceProperties properties,
                                                        String             propertyName,
                                                        List<String>       arrayValues,
                                                        String             methodName);


    /**
     * Add the supplied map property to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addMapPropertyToInstance(String              sourceName,
                                                InstanceProperties  properties,
                                                String              propertyName,
                                                Map<String, Object> mapValues,
                                                String              methodName);


    /**
     * If the supplied map property is not null, add it to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addStringMapPropertyToInstance(String              sourceName,
                                                      InstanceProperties  properties,
                                                      String              propertyName,
                                                      Map<String, String> mapValues,
                                                      String              methodName);


    /**
     * If the supplied map property is not null, add it to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addBooleanMapPropertyToInstance(String               sourceName,
                                                       InstanceProperties   properties,
                                                       String               propertyName,
                                                       Map<String, Boolean> mapValues,
                                                       String               methodName);


    /**
     * If the supplied map property is not null, add it to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addDateMapPropertyToInstance(String             sourceName,
                                                    InstanceProperties properties,
                                                    String             propertyName,
                                                    Map<String, Date>  mapValues,
                                                    String             methodName);


    /**
     * If the supplied map property is not null, add it to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addLongMapPropertyToInstance(String             sourceName,
                                                    InstanceProperties properties,
                                                    String             propertyName,
                                                    Map<String, Long>  mapValues,
                                                    String             methodName);


    /**
     * If the supplied map property is not null, add it to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addDoubleMapPropertyToInstance(String             sourceName,
                                                      InstanceProperties properties,
                                                      String             propertyName,
                                                      Map<String, Double>  mapValues,
                                                      String             methodName);


    /**
     * If the supplied map property is not null, add it to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addIntMapPropertyToInstance(String               sourceName,
                                                   InstanceProperties   properties,
                                                   String               propertyName,
                                                   Map<String, Integer> mapValues,
                                                   String               methodName);


    /**
     * If the supplied map property is not null, add it to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addStringArrayStringMapPropertyToInstance(String                    sourceName,
                                                                 InstanceProperties        properties,
                                                                 String                    propertyName,
                                                                 Map<String, List<String>> mapValues,
                                                                 String                    methodName);


    /**
     * Add the supplied property map to an instance properties object.  Each of the entries in the map is added
     * as a separate property in instance properties unless it is null.  If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     * @throws InvalidParameterException invalid property value
     */
    InstanceProperties addPropertyMapToInstance(String              sourceName,
                                                InstanceProperties  properties,
                                                Map<String, Object> mapValues,
                                                String              methodName) throws InvalidParameterException;



    /**
     * Add the supplied property map to an instance properties object.  Each of the entries in the map is added
     * as a separate property in instance properties unless it is null.  If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addStringPropertyMapToInstance(String              sourceName,
                                                      InstanceProperties  properties,
                                                      String              propertyName,
                                                      Map<String, String> mapValues,
                                                      String              methodName);


    /**
     * Add the supplied property map to an instance properties object.  Each of the entries in the map is added
     * as a separate property in instance properties unless it is null.  If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addBooleanPropertyMapToInstance(String              sourceName,
                                                      InstanceProperties   properties,
                                                      String               propertyName,
                                                      Map<String, Boolean> mapValues,
                                                      String               methodName);



    /**
     * Add the supplied property map to an instance properties object.  Each of the entries in the map is added
     * as a separate property in instance properties unless it is null.  If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addDatePropertyMapToInstance(String              sourceName,
                                                    InstanceProperties  properties,
                                                    String              propertyName,
                                                    Map<String, Date>   mapValues,
                                                    String              methodName);


    /**
     * Add the supplied property map to an instance properties object.  Each of the entries in the map is added
     * as a separate property in instance properties unless it is null.  If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addLongPropertyMapToInstance(String              sourceName,
                                                    InstanceProperties  properties,
                                                    String              propertyName,
                                                    Map<String, Long>   mapValues,
                                                    String              methodName);


    /**
     * Add the supplied property map to an instance properties object.  Each of the entries in the map is added
     * as a separate property in instance properties unless it is null.  If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addDoublePropertyMapToInstance(String                sourceName,
                                                      InstanceProperties    properties,
                                                      String                propertyName,
                                                      Map<String, Double>   mapValues,
                                                      String                methodName);


    /**
     * Add the supplied property map to an instance properties object.  Each of the entries in the map is added
     * as a separate property in instance properties unless it is null.  If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addIntPropertyMapToInstance(String               sourceName,
                                                   InstanceProperties   properties,
                                                   String               propertyName,
                                                   Map<String, Integer> mapValues,
                                                   String               methodName);


    /**
     * Convert the provided instance properties and match criteria into an equivalent SearchProperties object.
     *
     * @param sourceName name of the caller
     * @param properties properties object to convert
     * @param matchCriteria match criteria to apply
     * @return SearchProperties object.
     */
    SearchProperties getSearchPropertiesFromInstanceProperties(String             sourceName,
                                                               InstanceProperties properties,
                                                               MatchCriteria      matchCriteria);

}
