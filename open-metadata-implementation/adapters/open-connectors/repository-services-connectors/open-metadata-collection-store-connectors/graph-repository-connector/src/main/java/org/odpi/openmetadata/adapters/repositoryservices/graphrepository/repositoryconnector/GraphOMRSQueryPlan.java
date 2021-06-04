/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GraphOMRSQueryPlan {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSQueryPlan.class);

    public enum QueryStrategy
    {
        Iterate,
        Delegate
    }


    private QueryStrategy                 queryStrategy;
    private Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute;
    private Map<String, List<String>>     shortPropertyNameToQualifiedPropertyNames;
    private List<String>                  validTypeNames;
    private String                        filterTypeName;





    /*
     * Default CTOR
     */
    public GraphOMRSQueryPlan() {

    }

    /*
     * InstanceProperties constructor - for APIs accepting matchProeprties parameter
     */
    public GraphOMRSQueryPlan(String                repositoryName,
                              String                metadataCollectionId,
                              OMRSRepositoryHelper  repositoryHelper,
                              TypeDefCategory       typeDefCategory,
                              InstanceProperties    matchProperties,
                              String                filterTypeGUID,
                              List<String>          subTypeGUIDs)

    throws InvalidParameterException,
           TypeErrorException
    {

        /*
         * Pre-screen the search properties to extract a list of the property short names it refers to.
         * This is used to filter the following loop and maps to avoid constructing larger maps than are
         * needed and to avoid flagging dups where the duped properties are not part of the query.
         */
        List<String> queryPropertyNames = extractPropertyNamesFromMatchProperties(matchProperties);

        generateQueryPlan(repositoryName,
                          metadataCollectionId,
                          repositoryHelper,
                          typeDefCategory,
                          queryPropertyNames,
                          filterTypeGUID,
                          subTypeGUIDs);
    }


    /*
     * SearchProperties constructor - for APIs accepting searchProperties parameter
     */
    public GraphOMRSQueryPlan(String                repositoryName,
                              String                metadataCollectionId,
                              OMRSRepositoryHelper  repositoryHelper,
                              TypeDefCategory       typeDefCategory,
                              SearchProperties      searchProperties,
                              String                filterTypeGUID,
                              List<String>          subTypeGUIDs)

    throws InvalidParameterException,
           TypeErrorException
    {

        /*
         * Pre-screen the search properties to extract a list of the property short names it refers to.
         * This is used to filter the following loop and maps to avoid constructing larger maps than are
         * needed and to avoid flagging dups where the duped properties are not part of the query.
         */
        List<String> queryPropertyNames = extractPropertyNamesFromSearchProperties(searchProperties, repositoryName);

        generateQueryPlan(repositoryName,
                          metadataCollectionId,
                          repositoryHelper,
                          typeDefCategory,
                          queryPropertyNames,
                          filterTypeGUID,
                          subTypeGUIDs);
    }


    /*
     * SearchProperties constructor - for APIs accepting searchCritera parameter
     */
    public GraphOMRSQueryPlan(String                repositoryName,
                              String                metadataCollectionId,
                              OMRSRepositoryHelper  repositoryHelper,
                              TypeDefCategory       typeDefCategory,
                              String                filterTypeGUID,
                              List<String>          subTypeGUIDs)

    throws InvalidParameterException,
           TypeErrorException
    {

        generateQueryPlan(repositoryName,
                          metadataCollectionId,
                          repositoryHelper,
                          typeDefCategory,
                          null,
                          filterTypeGUID,
                          subTypeGUIDs);
    }



    public QueryStrategy getQueryStrategy()
    {
        return queryStrategy;
    }

    public Map<String, TypeDefAttribute>  getQualifiedPropertyNameToTypeDefinedAttribute()
    {
        return qualifiedPropertyNameToTypeDefinedAttribute;
    }

    public Map<String, List<String>>  getShortPropertyNameToQualifiedPropertyNames()
    {
        return shortPropertyNameToQualifiedPropertyNames;
    }

    public List<String>  getValidTypeNames()
    {
        return validTypeNames;
    }

    public String getFilterTypeName()
    {
        return filterTypeName;
    }



    private void generateQueryPlan(String                repositoryName,
                                   String                metadataCollectionId,
                                   OMRSRepositoryHelper  repositoryHelper,
                                   TypeDefCategory       typeDefCategory,
                                   List<String>          queryPropertyNames,
                                   String                filterTypeGUID,
                                   List<String>          subTypeGUIDs)

    throws TypeErrorException
    {
        final String methodName = "createQueryPlan";
        final String filterTypeGUIDParameterName = "filterTypeGUID";

        /*
         * Initialise the QueryPlan fields
         */
        qualifiedPropertyNameToTypeDefinedAttribute = new HashMap<>();
        shortPropertyNameToQualifiedPropertyNames = new HashMap<>();
        filterTypeName = null;
        validTypeNames = new ArrayList<>();


        /*
         * Convert the filter type GUID to a type name
         */
        if (filterTypeGUID != null)
        {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, filterTypeGUIDParameterName, filterTypeGUID, methodName);
            filterTypeName = typeDef.getName();
        }

        /*
         * Construct the validTypes and populate the property maps
         */
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();
        boolean dups = false;

        /*
         * Types are not presented in any particular order. Not top-down, nor bottom-up.
         */
        for (TypeDef typeDef : allTypeDefs)
        {
            if (typeDef.getCategory() == typeDefCategory)
            {
                String typeName = typeDef.getName();
                if (filterTypeName == null || repositoryHelper.isTypeOf(metadataCollectionId, typeName, filterTypeName))
                {
                    String typeGUID = typeDef.getGUID();
                    /*
                     * If filterTypeName is null there is no filtering, so process the type; if filterTypeName is not null,
                     * validate that this type is the same as, or a subtype of the filter type. If subTypeGUIDs are
                     * specified these filter the valid subTypes.
                     */
                    if (filterTypeName == null || filterTypeName.equals(typeName) || subTypeGUIDs == null || subTypeGUIDs.contains(typeGUID))
                    {
                        /*
                         * The type is valid
                         */
                        validTypeNames.add(typeName);

                        /*
                         * Check the properties for the valid type and add them to the property maps. This will populate the map of
                         * short property name to qualified names (potentially 1:N) and the map of qualified name to TDA (1:1).
                         *
                         * Get all the defs for the filter type (if one is specified) and for all other types process the local
                         * defs only.
                         *
                         * In the event of duplicated property names, the qualifiedPropertyName (and TDA) used are the ones from the
                         * most superior type in the valid types (i.e. nearest the top of the inheritance hierarchy). For this reason,
                         * qualifiedPropertyName is also read from the map returned by the mapperUtils. This is the same method used
                         * in the construction of an entity, so it is consistent with the qualifiedPropertyName and hence the graph
                         * propertyKey used to store the vertex.
                         *
                         * Vertical duplicates are benign and are eliminated in the above processing, but horizontal duplicates may
                         * remain. These are the basis for the selection of the query strategy.
                         */

                        Map<String, String> qualifiedPropertyNames;
                        Map<String, TypeDefAttribute> propertyDefs;

                        if (filterTypeName != null && filterTypeName.equals(typeName))
                        {
                            /*
                             * Process all properties (including inherited)
                             */
                            qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);
                            /*
                             * The unique  property names eliminate vertical dups - i.e. if supertype and subtype both have a property
                             * with the same name
                             */
                            Set<String> uniquePropertyNames = qualifiedPropertyNames.keySet();
                            /*
                             * Create a de-duplicated map of short property name to TDA
                             */
                            propertyDefs = mapperUtils.getUniquePropertyDefsForTypeDef(repositoryName, typeDef, repositoryHelper);
                            /*
                              * Amalgamate these into the persistent maps that cover all the types in the valid set
                             */
                            for (String shortName : uniquePropertyNames)
                            {
                                if (queryPropertyNames == null || queryPropertyNames.contains(shortName))
                                {
                                    TypeDefAttribute tda = propertyDefs.get(shortName);
                                    String qualifiedName = qualifiedPropertyNames.get(shortName);
                                    qualifiedPropertyNameToTypeDefinedAttribute.put(qualifiedName, tda);
                                    List<String> qNameList = shortPropertyNameToQualifiedPropertyNames.get(shortName);
                                    if (qNameList == null)
                                    {
                                        qNameList = new ArrayList<>();
                                        shortPropertyNameToQualifiedPropertyNames.put(shortName, qNameList);
                                    }
                                    else
                                    {
                                        /*
                                         * Since there was already a qNameList entry for this short name there must be a dup
                                         */
                                        dups = true;
                                    }
                                    qNameList.add(qualifiedName);
                                }
                            }
                        }
                        else
                        {
                            /*
                             * Process the local properties. This needs to get the de-duplicated maps that contain only
                             * the most superior type so that vertical dups are eliminated. Otherwise, since the types are
                             * being processed in arbitrary order, there would be a risk of overwriting the supertype's def
                             * with the subtype's def.
                             */
                            qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);
                            /*
                             * Create a de-duplicated map of short property name to TDA
                             */
                            propertyDefs = mapperUtils.getUniquePropertyDefsForTypeDef(repositoryName, typeDef, repositoryHelper);
                            /*
                             * Amalgamate the entries attributable to local props into the persistent maps that cover all the types
                             * in the valid set
                             */
                            List<TypeDefAttribute> localPropertiesDef = typeDef.getPropertiesDefinition();
                            if (localPropertiesDef != null)
                            {
                                for (TypeDefAttribute tda : localPropertiesDef)
                                {
                                    String shortName = tda.getAttributeName();
                                    if (queryPropertyNames == null || queryPropertyNames.contains(shortName))
                                    {
                                        /*
                                         * The property def used is the one from the unique property defs map.
                                         * This is a subtle point but it may be preferable to use the most superior TDA in the hierarchy
                                         * since that is the one that is actually defined for the chosen qualified name.
                                         */
                                        TypeDefAttribute propertyDef = propertyDefs.get(shortName);
                                        String qualifiedName = qualifiedPropertyNames.get(shortName);
                                        qualifiedPropertyNameToTypeDefinedAttribute.put(qualifiedName, propertyDef);
                                        List<String> qNameList = shortPropertyNameToQualifiedPropertyNames.get(shortName);
                                        if (qNameList == null)
                                        {
                                            qNameList = new ArrayList<>();
                                            shortPropertyNameToQualifiedPropertyNames.put(shortName, qNameList);
                                            qNameList.add(qualifiedName);
                                        }
                                        else
                                        {
                                            /*
                                             * There was already an entry for the short name - check if it's for the same qName
                                             */
                                            if (!qNameList.contains(qualifiedName))
                                            {
                                                /*
                                                 * This qName is not already in the map - if it were it would be as a result of a
                                                 * vertically duped property, and could be ignored.
                                                 */
                                                qNameList.add(qualifiedName);
                                                /*
                                                 * Since there was already a different qNameList entry for this short name there must be a dup
                                                 */
                                                dups = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (dups)
        {
            queryStrategy = QueryStrategy.Iterate;
        }
        else
        {
            queryStrategy = QueryStrategy.Delegate;
        }
        return;
    }


    /*
     * Verify that EITHER
     *   property, value and operator are all set and nestedConditions is null
     * OR
     *   nestedConditions is set and the others are null
     */

    // This method is package private - it is used locally and also by the GraphOMRSMetadataCollection class
    boolean validatePropertyCondition(PropertyCondition condition, String repositoryName)

    throws InvalidParameterException
    {
        final String methodName = "validatePropertyCondition";
        boolean localCondition;

        String propertyName = condition.getProperty();
        PropertyComparisonOperator operator = condition.getOperator();
        InstancePropertyValue value = condition.getValue();
        SearchProperties nestedConditions = condition.getNestedConditions();

        if (propertyName != null && operator != null && value != null && nestedConditions == null)
        {
            localCondition = true;
        }
        else if (propertyName == null && operator == null && value == null && nestedConditions != null)
        {
            localCondition = false;
        }
        else
        {
            /*
             * Invalid combination of conditions. Throw InvalidParameterException
             */
            throw new InvalidParameterException(
                    GraphOMRSErrorCode.INVALID_PROPERTY_CONDITION.getMessageDefinition(
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "searchProperties");
        }
        return localCondition;
    }

    /**
     * extractPropertyNamesFromSearchProperties
     *
     * This method extracts a list of the (short) property names referred to in a search properties
     * parameter, as passed to findEntities or findRelationships. This information can be used to
     * pre-screen a query to determine the preferred execution strategy.
     *
     * @param searchProperties - the SearchProperties type parameter to be analysed
     * @return a list of (Strinng) type property names
     */
    List<String> extractPropertyNamesFromSearchProperties(SearchProperties searchProperties, String repositoryName)

    throws InvalidParameterException
    {
        List<String> propertyNames = null;

        if (searchProperties != null)
        {

            propertyNames = new ArrayList<>();

            List<PropertyCondition> conditions = searchProperties.getConditions();
            for (PropertyCondition condition : conditions)
            {
                boolean localCondition = validatePropertyCondition(condition, repositoryName);

                /*
                 * Condition is valid, and localCondition indicates whether to process property, value, operator or to
                 * recurse into nestedConditions.
                 */

                if (localCondition)
                {
                    /* Construct a traversal for the property name, operator and value */
                    String propertyName = condition.getProperty();
                    if (propertyName != null)
                    {
                        propertyNames.add(propertyName);
                    }
                }
                else
                {
                    /*
                     * Recursively process nested conditions....
                     */
                    SearchProperties nestedConditions = condition.getNestedConditions();

                    List<String> moreProperties = extractPropertyNamesFromSearchProperties(nestedConditions, repositoryName);

                    if (moreProperties != null && !moreProperties.isEmpty())
                    {
                        propertyNames.addAll(moreProperties);
                    }
                }
            }
            if (propertyNames.isEmpty())
            {
                propertyNames = null;
            }
        }
        return propertyNames;

    }


    /**
     * extractPropertyNamesFromMatchProperties
     *
     * This method extracts a list of the (short) property names referred to in a match properties
     * parameter, as passed to findEntitiesByProperty or findRelationshipsByProperty. This information
     * can be used to pre-screen a query to determine the preferred execution strategy.
     *
     * @param matchProperties - the InstanceProperties type parameter to be analysed
     * @return a list of (Strinng) type property names
     */
    List<String> extractPropertyNamesFromMatchProperties(InstanceProperties matchProperties)
    {

        List<String> propertyNames = null;

        if (matchProperties != null)
        {
            propertyNames = new ArrayList<>();

            Map<String, InstancePropertyValue> propertyMap = matchProperties.getInstanceProperties();
            if (propertyMap != null)
            {
                Set<String> propertyKeys = propertyMap.keySet();
                if (!propertyKeys.isEmpty())
                {
                    propertyNames.addAll(propertyKeys);
                }
            }
            if (propertyNames.isEmpty())
            {
                propertyNames = null;
            }
        }
        return propertyNames;
    }



}
